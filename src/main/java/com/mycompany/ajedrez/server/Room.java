package com.mycompany.ajedrez.server;

import java.io.Serializable;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Room implements Serializable {
    String roomName;
    String roomPassword;
    boolean isEnded;
    Map<String, String> players = new HashMap<>(); // Nombre del jugador -> Color ("blanco" o "negro")
    private transient Socket socket; // No se serializa
    private String playerStart;

    public Room(String roomName, String roomPassword, Map<String, String> players) {
        this.roomName = roomName;
        this.roomPassword = roomPassword;
        this.players = players;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomPassword() {
        return roomPassword;
    }

    public void setRoomPassword(String roomPassword) {
        this.roomPassword = roomPassword;
    }

    public Map<String, String> getPlayers() {
        return players;
    }

    public void setPlayers(Map<String, String> players) {
        this.players = players;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    // Método para asignar colores a los jugadores
    public void asignarPrimeraJugada() {
        Random rand = new Random();

        // Obtener los jugadores (nombres) de la sala
        String[] jugadores = players.keySet().toArray(new String[0]);

        // Elegir aleatoriamente uno de los jugadores para empezar
        int indiceAleatorio = rand.nextInt(jugadores.length);
        playerStart = jugadores[indiceAleatorio];

        // Asignar colores
        for (String jugador : jugadores) {
            if (jugador.equals(playerStart)) {
                players.put(jugador, "blanco"); // El jugador que empieza recibe las blancas
            } else {
                players.put(jugador, "negro"); // El otro jugador recibe las negras
            }
        }

        // Asignar el turno al jugador que empieza
        if (playerStart.equals(jugadores[0])) {
            // El primer jugador en la lista empieza
            System.out.println("El jugador que empieza es: " + playerStart);
        } else {
            // El segundo jugador en la lista empieza
            System.out.println("El jugador que empieza es: " + playerStart);
        }
    }


    public String getPlayerStart() {
        return playerStart;
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomName='" + roomName + '\'' +
                ", roomPassword='" + roomPassword + '\'' +
                ", isEnded=" + isEnded +
                ", players=" + players +
                '}';
    }
}