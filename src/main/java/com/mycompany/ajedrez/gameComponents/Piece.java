package com.mycompany.ajedrez.gameComponents;

import java.awt.image.BufferedImage;
import java.io.Serializable;

public abstract class Piece implements Serializable {
    // Constantes para los tipos de piezas
    public static final int ROOK = 0;   // Torre
    public static final int KNIGHT = 1; // Caballo
    public static final int BISHOP = 2; // Alfil
    public static final int KING = 3;   // Rey
    public static final int QUEEN = 4;  // Reina
    public static final int PAWN = 5;   // Peón

    public static final int BLACK = 0;
    public static final int WHITE = 1;
    public static final int RED = 2;
    public static final int BLUE = 3;

    private String color; // Color de la pieza (usando las constantes anteriores)
    private int type;     // Tipo de pieza (usando las constantes anteriores)
    private transient BufferedImage sprite; // Sprite de la pieza

    public Piece(String color, int type, BufferedImage sprite) {
        this.color = color;
        this.type = type;
        this.sprite = sprite;
    }

    // Getters
    public String getColor() {
        return color;
    }

    public int getType() {
        return type;
    }

    public BufferedImage getSprite() {
        return sprite;
    }

    // Método para obtener el símbolo de la pieza (útil para depuración)
    public String getSymbol() {
        switch (type) {
            case PAWN: return color.equals("blanco") ? "P" : "p";
            case ROOK: return color.equals("blanco") ? "R" : "r";
            case KNIGHT: return color.equals("blanco") ? "N" : "n";
            case BISHOP: return color.equals("blanco") ? "B" : "b";
            case QUEEN: return color.equals("blanco") ? "Q" : "q";
            case KING: return color.equals("blanco") ? "K" : "k";
            default: return "?";
        }
    }

    public abstract boolean isValidMove(int startX, int startY, int endX, int endY, Board board);

    @Override
    public String toString() {
        return "Piece{" +
                "color='" + color + '\'' +
                ", type=" + type +
                ", sprite=" + sprite +
                '}';
    }
}