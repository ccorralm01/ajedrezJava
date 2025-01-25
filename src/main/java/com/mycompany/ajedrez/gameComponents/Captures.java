package com.mycompany.ajedrez.gameComponents;

import com.mycompany.ajedrez.Pieces.*;
import com.mycompany.ajedrez.managers.SpriteManager;

public class Captures {
    private Object[][] captureGrid; // Grid de 12x2 (pieza + contador)

    public Captures(SpriteManager spriteManager) {
        captureGrid = new Object[12][2];
        initializeCaptureGrid(spriteManager);
    }

    /**
     * Inicializa el grid con una pieza de cada tipo y color.
     */
    private void initializeCaptureGrid(SpriteManager spriteManager) {
        // Piezas negras
        captureGrid[0][0] = new Pawn("negro", spriteManager.getPieceSprite(Piece.BLACK, Piece.PAWN));
        captureGrid[1][0] = new Rook("negro", spriteManager.getPieceSprite(Piece.BLACK, Piece.ROOK));
        captureGrid[2][0] = new Knight("negro", spriteManager.getPieceSprite(Piece.BLACK, Piece.KNIGHT));
        captureGrid[3][0] = new Bishop("negro", spriteManager.getPieceSprite(Piece.BLACK, Piece.BISHOP));
        captureGrid[4][0] = new Queen("negro", spriteManager.getPieceSprite(Piece.BLACK, Piece.QUEEN));
        captureGrid[5][0] = new King("negro", spriteManager.getPieceSprite(Piece.BLACK, Piece.KING));

        // Piezas blancas
        captureGrid[6][0] = new Pawn("blanco", spriteManager.getPieceSprite(Piece.WHITE, Piece.PAWN));
        captureGrid[7][0] = new Rook("blanco", spriteManager.getPieceSprite(Piece.WHITE, Piece.ROOK));
        captureGrid[8][0] = new Knight("blanco", spriteManager.getPieceSprite(Piece.WHITE, Piece.KNIGHT));
        captureGrid[9][0] = new Bishop("blanco", spriteManager.getPieceSprite(Piece.WHITE, Piece.BISHOP));
        captureGrid[10][0] = new Queen("blanco", spriteManager.getPieceSprite(Piece.WHITE, Piece.QUEEN));
        captureGrid[11][0] = new King("blanco", spriteManager.getPieceSprite(Piece.WHITE, Piece.KING));

        // Inicializar los contadores de capturas a 0
        for (int i = 0; i < 12; i++) {
            captureGrid[i][1] = 0;
        }
    }

    /**
     * Incrementa el contador de capturas para una pieza específica.
     */
    public void incrementCaptureCount(Piece piece) {
        for (int i = 0; i < 12; i++) {
            Piece gridPiece = (Piece) captureGrid[i][0];
            if (gridPiece != null && gridPiece.getClass().equals(piece.getClass()) && gridPiece.getColor().equals(piece.getColor())) {
                int currentCount = (int) captureGrid[i][1];
                captureGrid[i][1] = currentCount + 1;
                break;
            }
        }
    }

    public Object[][] getCaptureGrid() {
        return captureGrid;
    }
}