package com.mycompany.ajedrez.server;

import java.io.Serializable;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Room implements Serializable {
    String roomName;
    String roomPassword;
    boolean isEnded;
    Map<String, String> players = new HashMap<>(); // Nombre del jugador -> Color ("blanco" o "negro")
    private transient Socket socket; // No se serializa

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
    public void asignarColores() {
        if (players.size() == 2) {
            String[] colores = {"blanco", "negro"};
            int i = 0;
            for (String jugador : players.keySet()) {
                players.put(jugador, colores[i]);
                i++;
            }
        }
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