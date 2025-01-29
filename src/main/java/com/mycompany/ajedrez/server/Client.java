package com.mycompany.ajedrez.server;

import com.mycompany.ajedrez.GameController;
import com.mycompany.ajedrez.panels.MultijugadorPanel;

import java.io.*;
import java.net.*;
import java.util.Objects;

public class Client {
    private String host;
    private int puerto;
    private boolean turno;
    private MultijugadorPanel mjPanel;
    private GameController gameController;

    private Room setUpRoom;
    private Movement movimientoPieza = null;

    // Objeto para sincronización
    private final Object lock = new Object();

    public Client(String host, int puerto) {
        this.host = host;
        this.puerto = puerto;
        this.turno = false;
    }

    public void iniciar() {
        try (
                Socket socket = new Socket(host, puerto);
                ObjectOutputStream salida = new ObjectOutputStream(socket.getOutputStream());
        ) {

            Room room = mjPanel.getNewRoom();
            salida.writeObject(room);
            System.out.println("Room enviada: " + room.getRoomName());

            ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream());
            System.out.println("Entrada creada");
            setUpRoom = (Room) entrada.readObject();
            mjPanel.onRoomSetUp(setUpRoom);

            asignarTurno();

            // Cada turno
            while (true) {
                if (gameController.getMyTurn()) {
                    System.out.println("Esperando movimiento...");
                    // simularTurno();

                    // Esperar a que movimientoPieza tenga un valor
                    synchronized (lock) {
                        while (movimientoPieza == null) {
                            lock.wait(); // Espera hasta que se notifique
                        }
                    }

                    salida.writeObject(movimientoPieza);
                    System.out.println("Movimiento enviado: " + movimientoPieza);
                    movimientoPieza = null; // Reiniciar para el próximo turno
                } else {
                    // Recibo turno del contrario
                    System.out.println("Esperando movimiento del contrario...");
                    Object movimientoRecibido = entrada.readObject();
                    System.out.println("Movimiento del jugador contrario recibido: " + movimientoRecibido);
                }

                if (gameController.getMyTurn()) {
                    gameController.setMyTurn(false);
                    System.out.println("Ya no es mi turno");
                } else {
                    gameController.setMyTurn(true);
                    System.out.println("Es mi turno!!!");
                }
            }

        } catch (IOException | InterruptedException e) {
            System.err.println("Error en el cliente: " + e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void asignarTurno() {
        // Asignar el turno según el jugador que empieza
        if (Objects.equals(mjPanel.getInputUsuario().getText(), setUpRoom.getPlayerStart())) {
            gameController.setMyTurn(true);
        } else {
            gameController.setMyTurn(false);
        }
    }

    public void simularTurno() {
        gameController.selectPiece(6, 0);
        gameController.movePiece(5, 0);
    }

    // actualizar el movimiento y notificar
    public void setMovimientoPieza(Movement movimiento) {
        synchronized (lock) {
            this.movimientoPieza = movimiento;
            lock.notify();
        }
    }

    public void setMjPanel(MultijugadorPanel mjPanel) {
        this.mjPanel = mjPanel;
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    public static void main(String[] args) {
        String host = "127.0.0.1";
        int puerto = 6666;

        Client cliente = new Client(host, puerto);
        cliente.iniciar();
    }
}