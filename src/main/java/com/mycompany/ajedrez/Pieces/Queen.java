package com.mycompany.ajedrez.Pieces;

import com.mycompany.ajedrez.gameComponents.Board;
import com.mycompany.ajedrez.gameComponents.Piece;

import java.awt.image.BufferedImage;

public class Queen extends Piece {
    public Queen(String color, BufferedImage sprite) {
        super(color, Piece.QUEEN, sprite);
    }

    @Override
    public boolean isValidMove(int startX, int startY, int endX, int endY, Board board) {
        // La reina se mueve en línea recta o diagonal
        return startX == endX || startY == endY || Math.abs(endX - startX) == Math.abs(endY - startY);
    }
}