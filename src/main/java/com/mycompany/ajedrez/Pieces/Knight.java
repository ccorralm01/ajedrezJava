package com.mycompany.ajedrez.Pieces;

import com.mycompany.ajedrez.gameComponents.Board;
import com.mycompany.ajedrez.gameComponents.Piece;

import java.awt.image.BufferedImage;

public class Knight extends Piece {
    public Knight(String color, BufferedImage sprite) {
        super(color, Piece.KNIGHT, sprite);
    }

    @Override
    public boolean isValidMove(int startX, int startY, int endX, int endY, Board board) {
        // El caballo se mueve en forma de L
        int dx = Math.abs(endX - startX);
        int dy = Math.abs(endY - startY);
        return (dx == 2 && dy == 1) || (dx == 1 && dy == 2);
    }
}