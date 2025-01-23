package com.mycompany.ajedrez.server;

import com.mycompany.ajedrez.gameComponents.Piece;

import java.io.Serializable;

public class Request implements Serializable {
    String user;
    String roomName;
    String roomPassword;
    private Piece piece;
    private int fromX;
    private int fromY;
    private int toX;
    private int toY;

    public Request(String user, String roomName, String roomPassword, Piece piece, int fromX, int fromY, int toX, int toY) {
        this.user = user;
        this.roomName = roomName;
        this.roomPassword = roomPassword;
        this.piece = piece;
        this.fromX = fromX;
        this.fromY = fromY;
        this.toX = toX;
        this.toY = toY;
    }

    // Getters y setters


    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
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

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public int getFromX() {
        return fromX;
    }

    public void setFromX(int fromX) {
        this.fromX = fromX;
    }

    public int getFromY() {
        return fromY;
    }

    public void setFromY(int fromY) {
        this.fromY = fromY;
    }

    public int getToX() {
        return toX;
    }

    public void setToX(int toX) {
        this.toX = toX;
    }

    public int getToY() {
        return toY;
    }

    public void setToY(int toY) {
        this.toY = toY;
    }

    @Override
    public String toString() {
        return "Request{" +
                ", piece=" + piece +
                ", fromX=" + fromX +
                ", fromY=" + fromY +
                ", toX=" + toX +
                ", toY=" + toY +
                '}';
    }
}