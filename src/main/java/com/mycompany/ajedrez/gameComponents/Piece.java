package com.mycompany.ajedrez.gameComponents;

import java.awt.image.BufferedImage;
import java.io.Serializable;

/**
 * La clase abstracta Piece representa una pieza de ajedrez genérica.
 * Proporciona una estructura base para todas las piezas del juego, como peones, torres, caballos, etc.
 */
public abstract class Piece implements Serializable {
    // Constantes para los tipos de piezas
    public static final int ROOK = 0;   // Torre
    public static final int KNIGHT = 1; // Caballo
    public static final int BISHOP = 2; // Alfil
    public static final int KING = 3;   // Rey
    public static final int QUEEN = 4;  // Reina
    public static final int PAWN = 5;   // Peón

    // Constantes para los colores de las piezas
    public static final int BLACK = 0; // Color negro
    public static final int WHITE = 1; // Color blanco

    private String color; // Color de la pieza (usando las constantes anteriores)
    private int type;     // Tipo de pieza (usando las constantes anteriores)
    private transient BufferedImage sprite; // Sprite de la pieza (no serializable)

    /**
     * Constructor de la clase Piece.
     *
     * @param color  El color de la pieza ("blanco" o "negro").
     * @param type   El tipo de pieza, definido por las constantes de la clase.
     * @param sprite La imagen (sprite) asociada a la pieza.
     */
    public Piece(String color, int type, BufferedImage sprite) {
        this.color = color;
        this.type = type;
        this.sprite = sprite;
    }

    /**
     * Obtiene el color de la pieza.
     *
     * @return El color de la pieza ("blanco" o "negro").
     */
    public String getColor() {
        return color;
    }

    /**
     * Obtiene el tipo de pieza.
     *
     * @return El tipo de pieza, definido por las constantes de la clase.
     */
    public int getType() {
        return type;
    }

    /**
     * Obtiene la imagen (sprite) asociada a la pieza.
     *
     * @return La imagen (BufferedImage) de la pieza.
     */
    public BufferedImage getSprite() {
        return sprite;
    }

    /**
     * Obtiene el símbolo de la pieza, útil para representación textual o depuración.
     *
     * @return Un carácter que representa la pieza (por ejemplo, "P" para peón blanco, "p" para peón negro).
     */
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

    /**
     * Método abstracto para validar si un movimiento es válido para la pieza.
     * Debe ser implementado por las subclases para definir las reglas de movimiento específicas de cada pieza.
     *
     * @param startX La coordenada x (fila) de la posición inicial.
     * @param startY La coordenada y (columna) de la posición inicial.
     * @param endX   La coordenada x (fila) de la posición final.
     * @param endY   La coordenada y (columna) de la posición final.
     * @param board  El tablero de juego, utilizado para verificar obstáculos y otras condiciones.
     * @return true si el movimiento es válido, false en caso contrario.
     */
    public abstract boolean isValidMove(int startX, int startY, int endX, int endY, Board board);

    /**
     * Representación textual de la pieza, útil para depuración.
     *
     * @return Una cadena que describe la pieza, incluyendo su color, tipo y sprite.
     */
    @Override
    public String toString() {
        return "Piece{" +
                "color='" + color + '\'' +
                ", type=" + type +
                ", sprite=" + sprite +
                '}';
    }
}