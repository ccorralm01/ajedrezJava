package com.mycompany.ajedrez.Pieces;

import com.mycompany.ajedrez.gameComponents.Board;
import com.mycompany.ajedrez.gameComponents.Piece;

import java.awt.image.BufferedImage;

public class Rook extends Piece {
    public Rook(String color, BufferedImage sprite) {
        super(color, Piece.ROOK, sprite);
    }

    @Override
    public boolean isValidMove(int startX, int startY, int endX, int endY, Board board) {
        // La torre se mueve en línea recta (misma fila o misma columna)
        return startX == endX || startY == endY;
    }
}
