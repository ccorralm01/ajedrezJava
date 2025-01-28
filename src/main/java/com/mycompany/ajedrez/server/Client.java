package com.mycompany.ajedrez.server;

import com.mycompany.ajedrez.GameController;
import com.mycompany.ajedrez.panels.MultijugadorPanel;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Client {
    private String host;
    private int puerto;
    private boolean turno;
    private MultijugadorPanel mjPanel;
    private GameController gameController;

    private Room setUpRoom;

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

            /*
            // Cada turno
            while (true) {
                if (turno) {
                    // get movimiento
                    // Aquí deberías implementar la lógica para obtener el movimiento del jugador
                }
                if (respuesta instanceof Room respuestaRoom) {
                    // Lógica para manejar la respuesta de la sala
                } else if (respuesta instanceof Movement movement) {
                    System.out.println("Se recibió una solicitud: " + movement);
                } else {
                    System.out.println("Se recibió un tipo de objeto desconocido: " + respuesta.getClass().getName());
                }
            }
             */
        } catch (IOException e) {
            System.err.println("Error en el cliente: " + e.getMessage());
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