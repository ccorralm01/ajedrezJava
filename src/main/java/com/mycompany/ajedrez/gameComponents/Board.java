package com.mycompany.ajedrez.gameComponents;

import com.mycompany.ajedrez.Pieces.*;
import com.mycompany.ajedrez.managers.SpriteManager;
import com.mycompany.ajedrez.server.Room;

import java.util.Objects;

public class Board {
    private Piece[][] board;
    private SpriteManager spriteManager;
    private String myPieces;
    private String enemyPieces;

    public Board(SpriteManager spriteManager, String usuario, String versus, Room room) {
        this.spriteManager = spriteManager;
        board = new Piece[8][8];
        myPieces = room.getPlayers().get(usuario);
        enemyPieces = room.getPlayers().get(versus);
        // System.out.println("Piezas aliadas: " + myPieces);
        // System.out.println("Piezas enemigas: " + enemyPieces);
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
        boolean isWhite = Objects.equals(myPieces, "blanco");
        int myColor = isWhite ? Piece.WHITE : Piece.BLACK;
        int enemyColor = isWhite ? Piece.BLACK : Piece.WHITE;

        // Colocar las piezas enemigas
        board[0][0] = new Rook(enemyPieces, spriteManager.getPieceSprite(enemyColor, Piece.ROOK));
        board[0][1] = new Knight(enemyPieces, spriteManager.getPieceSprite(enemyColor, Piece.KNIGHT));
        board[0][2] = new Bishop(enemyPieces, spriteManager.getPieceSprite(enemyColor, Piece.BISHOP));
        board[0][4] = new King(enemyPieces, spriteManager.getPieceSprite(enemyColor, Piece.QUEEN));
        board[0][3] = new Queen(enemyPieces, spriteManager.getPieceSprite(enemyColor, Piece.KING));
        board[0][5] = new Bishop(enemyPieces, spriteManager.getPieceSprite(enemyColor, Piece.BISHOP));
        board[0][6] = new Knight(enemyPieces, spriteManager.getPieceSprite(enemyColor, Piece.KNIGHT));
        board[0][7] = new Rook(enemyPieces, spriteManager.getPieceSprite(enemyColor, Piece.ROOK));
        for (int i = 0; i < 8; i++) {
            board[1][i] = new Pawn(enemyPieces, spriteManager.getPieceSprite(enemyColor, Piece.PAWN));
        }

        // Colocar las piezas aliadas
        board[7][0] = new Rook(myPieces, spriteManager.getPieceSprite(myColor, Piece.ROOK));
        board[7][1] = new Knight(myPieces, spriteManager.getPieceSprite(myColor, Piece.KNIGHT));
        board[7][2] = new Bishop(myPieces, spriteManager.getPieceSprite(myColor, Piece.BISHOP));
        board[7][3] = new Queen(myPieces, spriteManager.getPieceSprite(myColor, Piece.QUEEN));
        board[7][4] = new King(myPieces, spriteManager.getPieceSprite(myColor, Piece.KING));
        board[7][5] = new Bishop(myPieces, spriteManager.getPieceSprite(myColor, Piece.BISHOP));
        board[7][6] = new Knight(myPieces, spriteManager.getPieceSprite(myColor, Piece.KNIGHT));
        board[7][7] = new Rook(myPieces, spriteManager.getPieceSprite(myColor, Piece.ROOK));
        for (int i = 0; i < 8; i++) {
            board[6][i] = new Pawn(myPieces, spriteManager.getPieceSprite(myColor, Piece.PAWN));
        }
    }
}