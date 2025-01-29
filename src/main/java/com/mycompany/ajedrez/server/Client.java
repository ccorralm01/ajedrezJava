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
            salida.flush();
            // Crear y enviar la sala al servidor
            Room room = mjPanel.getNewRoom();
            System.out.println("Room creada: " + room.getRoomName());

            salida.writeObject(room);
            System.out.println("Room enviada: " + room.getRoomName());

            ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream());
            System.out.println("Entrada creada");

            setUpRoom = (Room) entrada.readObject();
            System.out.println("Respuesta recibida");

            mjPanel.onRoomSetUp(setUpRoom);

            // Asignar el turno según el jugador que empieza
            if (Objects.equals(mjPanel.getInputUsuario().getText(), setUpRoom.getPlayerStart())) {
                gameController.setMyTurn(true);
                // Selecciono peon en codigo
                gameController.selectPiece(6, 0);
                gameController.movePiece(5, 0);
            } else {
                gameController.setMyTurn(false);
            }


            // Cada turno
            while (true) {
                if (gameController.getMyTurn()) {
                    // mi turno
                    System.out.println("Esperando movimiento...");
                    // Esperar hasta que movimientoPieza tenga un valor
                    while (movimientoPieza == null) {
                        movimientoPieza = gameController.getLastMovement();
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    salida.writeObject(movimientoPieza);
                    System.out.println("Movimiento enviado: " + movimientoPieza);
                    movimientoPieza = null;
                } else {
                    // recibo turno del contrario
                    System.out.println("Esperando movimiento del contrario...");
                    Object movimientoRecibido = entrada.readObject();
                    System.out.println("Movimiento del jugador contrario recibido: " + movimientoRecibido);
                }

                if(gameController.getMyTurn()) {
                    gameController.setMyTurn(false);
                    System.out.println("Ya no es mi turno");
                } else {
                    gameController.setMyTurn(true);
                    System.out.println("Es mi turno!!!");
                }
            }

        } catch (IOException e) {
            System.err.println("Error en el cliente: " + e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPuerto() {
        return puerto;
    }

    public void setPuerto(int puerto) {
        this.puerto = puerto;
    }

    public boolean isTurno() {
        return turno;
    }

    public void setTurno(boolean turno) {
        this.turno = turno;
    }

    public MultijugadorPanel getMjPanel() {
        return mjPanel;
    }

    public void setMjPanel(MultijugadorPanel mjPanel) {
        this.mjPanel = mjPanel;
    }

    public GameController getGameController() {
        return gameController;
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    public Room getSetUpRoom() {
        return setUpRoom;
    }

    public void setSetUpRoom(Room setUpRoom) {
        this.setUpRoom = setUpRoom;
    }

    public static void main(String[] args) {
        String host = "127.0.0.1";
        int puerto = 6666;

        Client cliente = new Client(host, puerto);
        cliente.iniciar();
    }
}