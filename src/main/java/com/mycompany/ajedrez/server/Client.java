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
            setUpRoom = (Room) entrada.readObject();
            System.out.println("Room actualizada: " + setUpRoom);
            mjPanel.onRoomSetUp(setUpRoom);

            asignarTurno();

            // Cada turno
            while (true) {
                if (gameController.getMyTurn()) {
                    System.out.println("Esperando movimiento...");

                    try {
                        Thread.sleep(500);
                        gameController.playSound("src/res/notify.wav");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

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
                    Movement movimientoRecibido = (Movement) entrada.readObject();
                    System.out.println("Movimiento del jugador contrario recibido: " + transformarMovement(movimientoRecibido) + "Es ganador?: " + transformarMovement(movimientoRecibido).isWinMove());
                    Movement movimientoEspejo = transformarMovement(movimientoRecibido);
                    gameController.setSelectedX(movimientoEspejo.getFromX());
                    gameController.setSelectedY(movimientoEspejo.getFromY());
                    gameController.movePiece(movimientoEspejo.getToY(), movimientoEspejo.getToX(), false);
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

    public Movement transformarMovement(Movement movement) {
        int fromX = 7 - movement.getFromX();
        int fromY = 7 - movement.getFromY();
        int toX = 7 - movement.getToX();
        int toY = 7 - movement.getToY();

        // Verificar que las coordenadas estén dentro del rango válido
        if (fromX < 0 || fromX > 7 || fromY < 0 || fromY > 7 || toX < 0 || toX > 7 || toY < 0 || toY > 7) {
            throw new IllegalArgumentException("Coordenadas inválidas después de la transformación: " +
                    "fromX=" + fromX + ", fromY=" + fromY + ", toX=" + toX + ", toY=" + toY);
        }

        return new Movement(fromX, fromY, toX, toY);
    }

    // actualizar el movimiento y notificar
    public void setMovimientoPieza(Movement movimiento) {
        synchronized (lock) {
            gameController.playSound("src/res/move.wav");
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