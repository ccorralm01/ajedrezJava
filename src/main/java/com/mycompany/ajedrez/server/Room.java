package com.mycompany.ajedrez.server;

import java.io.Serializable;
import java.util.ArrayList;

public class Room implements Serializable {
    String roomName;
    String roomPassword;
    boolean isEnded;
    String host;
    ArrayList<String> players = new ArrayList<>();

    public Room(String roomName, String roomPassword, String host, ArrayList<String> players, boolean isEnded) {
        this.roomName = roomName;
        this.roomPassword = roomPassword;
        this.host = host;
        this.players = players;
        this.isEnded = false;
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

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public ArrayList<String> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<String> players) {
        this.players = players;
    }

    public boolean isEnded() {
        return isEnded;
    }

    public void setEnded(boolean ended) {
        isEnded = ended;
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomName='" + roomName + '\'' +
                ", roomPassword='" + roomPassword + '\'' +
                ", isEnded=" + isEnded +
                ", host='" + host + '\'' +
                ", players=" + players +
                '}';
    }
}
