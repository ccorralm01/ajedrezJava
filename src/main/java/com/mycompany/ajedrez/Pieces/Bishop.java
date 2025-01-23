package com.mycompany.ajedrez.Pieces;

import com.mycompany.ajedrez.gameComponents.Board;
import com.mycompany.ajedrez.gameComponents.Piece;

import java.awt.image.BufferedImage;

public class Bishop extends Piece {
    public Bishop(String color, BufferedImage sprite) {
        super(color, Piece.BISHOP, sprite);
    }

    @Override
    public boolean isValidMove(int startX, int startY, int endX, int endY, Board board) {
        // El alfil se mueve en diagonal
        return Math.abs(endX - startX) == Math.abs(endY - startY);
    }
}
