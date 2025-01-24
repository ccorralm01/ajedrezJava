package com.mycompany.ajedrez.gameComponents;

import com.mycompany.ajedrez.Pieces.*;
import com.mycompany.ajedrez.managers.SpriteManager;

public class Board {
    private Piece[][] board;
    private SpriteManager spriteManager;

    public Board(SpriteManager spriteManager) {
        this.spriteManager = spriteManager;
        board = new Piece[8][8];
        initializeBoard();
    }

    public Piece getPiece(int x, int y) {
        return board[x][y];
    }

    public void setPiece(int x, int y, Piece piece) {
        board[x][y] = piece;
    }

    public void movePiece(int startX, int startY, int endX, int endY) {
        Piece piece = board[startX][startY];
        board[startX][startY] = null; // Eliminar la pieza de la posición inicial
        board[endX][endY] = piece;   // Colocar la pieza en la posición final
    }

    private void initializeBoard() {
        // Colocar las piezas blancas
        board[0][0] = new Rook("blanco", spriteManager.getPieceSprite(Piece.WHITE, Piece.ROOK));
        board[0][1] = new Knight("blanco", spriteManager.getPieceSprite(Piece.WHITE, Piece.KNIGHT));
        board[0][2] = new Bishop("blanco", spriteManager.getPieceSprite(Piece.WHITE, Piece.BISHOP));
        board[0][3] = new Queen("blanco", spriteManager.getPieceSprite(Piece.WHITE, Piece.QUEEN));
        board[0][4] = new King("blanco", spriteManager.getPieceSprite(Piece.WHITE, Piece.KING));
        board[0][5] = new Bishop("blanco", spriteManager.getPieceSprite(Piece.WHITE, Piece.BISHOP));
        board[0][6] = new Knight("blanco", spriteManager.getPieceSprite(Piece.WHITE, Piece.KNIGHT));
        board[0][7] = new Rook("blanco", spriteManager.getPieceSprite(Piece.WHITE, Piece.ROOK));
        for (int i = 0; i < 8; i++) {
            board[1][i] = new Pawn("blanco", spriteManager.getPieceSprite(Piece.WHITE, Piece.PAWN));
        }


        // Colocar las piezas negras
        board[7][0] = new Rook("negro", spriteManager.getPieceSprite(Piece.BLACK, Piece.ROOK));
        board[7][1] = new Knight("negro", spriteManager.getPieceSprite(Piece.BLACK, Piece.KNIGHT));
        board[7][2] = new Bishop("negro", spriteManager.getPieceSprite(Piece.BLACK, Piece.BISHOP));
        board[7][3] = new Queen("negro", spriteManager.getPieceSprite(Piece.BLACK, Piece.QUEEN));
        board[7][4] = new King("negro", spriteManager.getPieceSprite(Piece.BLACK, Piece.KING));
        board[7][5] = new Bishop("negro", spriteManager.getPieceSprite(Piece.BLACK, Piece.BISHOP));
        board[7][6] = new Knight("negro", spriteManager.getPieceSprite(Piece.BLACK, Piece.KNIGHT));
        board[7][7] = new Rook("negro", spriteManager.getPieceSprite(Piece.BLACK, Piece.ROOK));
        // board[7][7] = new Rook("negro", spriteManager.getPieceSprite(Piece.BLACK, Piece.ROOK));
        for (int i = 0; i < 8; i++) {
            board[6][i] = new Pawn("negro", spriteManager.getPieceSprite(Piece.BLACK, Piece.PAWN));
        }
    }
}