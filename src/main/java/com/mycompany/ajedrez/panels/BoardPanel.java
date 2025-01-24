package com.mycompany.ajedrez.panels;

import com.mycompany.ajedrez.gameComponents.Board;
import com.mycompany.ajedrez.gameComponents.Piece;
import com.mycompany.ajedrez.managers.SpriteManager;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class BoardPanel extends JPanel {
    private Board board;
    private SpriteManager spriteManager;
    private List<Point> killMoves = new ArrayList<>(); // Lista de movimientos de captura
    private Image killHudImage; // Imagen del Hud.KILL

    public BoardPanel(Board board, SpriteManager spriteManager) {
        this.board = board;
        this.spriteManager = spriteManager;
        setPreferredSize(new Dimension(768, 832)); // Tamaño del tablero (12x12 casillas de 64x64 px)
    }

    /**
     * Establece los movimientos de captura (Hud.KILL) que se deben dibujar.
     */
    public void setKillMoves(List<Point> killMoves) {
        this.killMoves = killMoves;
        repaint(); // Volver a dibujar el panel para reflejar los cambios
    }

    /**
     * Establece la imagen del Hud.KILL.
     */
    public void setKillHudImage(Image killHudImage) {
        this.killHudImage = killHudImage;
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBoard(g); // Dibujar el tablero
        drawKillHud(g); // Dibujar el Hud.KILL
        drawPieces(g); // Dibujar las piezas
    }

    private void drawBoard(Graphics g) {
        int tileSize = 64; // Tamaño de cada casilla en píxeles
        for (int i = 0; i < 12; i++) { // Tablero de 12x12
            for (int j = 0; j < 12; j++) {
                BufferedImage tileSprite = spriteManager.getTileSprite(i, j);
                g.drawImage(tileSprite, j * tileSize, i * tileSize, tileSize, tileSize, null);
            }
        }
    }

    private void drawPieces(Graphics g) {
        int tileSize = 64; // Tamaño de cada casilla en píxeles
        int offset = 2 * tileSize; // Desplazamiento para centrar las fichas en el tablero de 8x8

        for (int i = 0; i < 8; i++) { // Iterar sobre el tablero de 8x8
            for (int j = 0; j < 8; j++) {
                Piece piece = board.getPiece(i, j);
                if (piece != null) {
                    BufferedImage pieceSprite = piece.getSprite();
                    // Aplicar el desplazamiento a las coordenadas de las fichas
                    g.drawImage(pieceSprite, j * tileSize + offset, i * tileSize + offset, tileSize, tileSize, null);
                }
            }
        }
    }

    /**
     * Dibuja el Hud.KILL en las posiciones correspondientes.
     */
    private void drawKillHud(Graphics g) {
        if (killHudImage != null) {
            int tileSize = 64; // Tamaño de cada casilla en píxeles
            int offset = 2 * tileSize; // Desplazamiento para centrar las fichas en el tablero de 8x8

            for (Point move : killMoves) {
                int x = move.x * tileSize + offset; // Calcular la posición X
                int y = move.y * tileSize + offset; // Calcular la posición Y
                g.drawImage(killHudImage, x, y, tileSize, tileSize, null); // Dibujar el Hud.KILL
            }
        }
    }
}