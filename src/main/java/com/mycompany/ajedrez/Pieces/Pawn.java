package com.mycompany.ajedrez.Pieces;

import com.mycompany.ajedrez.gameComponents.Board;
import com.mycompany.ajedrez.gameComponents.Piece;

import java.awt.image.BufferedImage;

public class Pawn extends Piece {
    public Pawn(String color, BufferedImage sprite) {
        super(color, Piece.PAWN, sprite);
    }

    @Override
    public boolean isValidMove(int startX, int startY, int endX, int endY, Board board) {
        int dx = endX - startX;
        int dy = endY - startY;

        // Movimiento normal: una casilla hacia arriba
        if (dx == -1 && dy == 0) {
            return board.getPiece(endX, endY) == null; // La casilla de destino debe estar vacía
        }

        // Movimiento inicial: dos casillas hacia arriba
        if (dx == -2 && dy == 0 && startX == 6) { // Solo desde la fila inicial (fila 6)
            return board.getPiece(endX, endY) == null && board.getPiece(startX - 1, startY) == null;
        }

        // Captura: una casilla en diagonal hacia arriba
        if (dx == -1 && Math.abs(dy) == 1) {
            Piece targetPiece = board.getPiece(endX, endY);
            return targetPiece != null && !targetPiece.getColor().equals(getColor()); // Debe haber una pieza enemiga
        }

        return false;
    }
}