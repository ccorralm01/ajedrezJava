package com.mycompany.ajedrez.Pieces;

import com.mycompany.ajedrez.gameComponents.Board;
import com.mycompany.ajedrez.gameComponents.Piece;

import java.awt.image.BufferedImage;

public class King extends Piece {
    public King(String color, BufferedImage sprite) {
        super(color, Piece.KING, sprite);
    }

    @Override
    public boolean isValidMove(int startX, int startY, int endX, int endY, Board board) {
        // El rey se mueve una casilla en cualquier dirección
        int dx = Math.abs(endX - startX);
        int dy = Math.abs(endY - startY);
        return dx <= 1 && dy <= 1;
    }
}
