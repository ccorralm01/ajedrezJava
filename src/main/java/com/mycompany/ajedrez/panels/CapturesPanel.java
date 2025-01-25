package com.mycompany.ajedrez.panels;

import com.mycompany.ajedrez.gameComponents.Captures;
import com.mycompany.ajedrez.gameComponents.Piece;
import com.mycompany.ajedrez.managers.SpriteManager;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class CapturesPanel extends JPanel {
    private static final int ROWS = 12; // 6 piezas negras + 6 piezas blancas
    private static final int COLS = 2;  // 2 columnas (pieza + contador)
    private static final int TILE_SIZE = 64; // Tamaño de cada casilla en píxeles

    private Captures captures;
    private SpriteManager spriteManager;

    public CapturesPanel(Captures captures, SpriteManager spriteManager) {
        this.captures = captures;
        this.spriteManager = spriteManager;
        setPreferredSize(new Dimension(COLS * TILE_SIZE, ROWS * TILE_SIZE)); // Tamaño del panel
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawCaptures(g); // Dibujar las piezas y sus contadores
    }

    /**
     * Dibuja las piezas y sus contadores de capturas.
     */
    private void drawCaptures(Graphics g) {
        for (int i = 0; i < ROWS; i++) {
            // Dibujar la pieza en la columna izquierda
            Piece piece = (Piece) captures.getCaptureGrid()[i][0];
            if (piece != null) {
                BufferedImage pieceSprite = piece.getSprite();
                if (pieceSprite != null) {
                    g.drawImage(pieceSprite, 0, i * TILE_SIZE, TILE_SIZE, TILE_SIZE, null);
                }
            }

            // Dibujar el contador en la columna derecha
            int count = (int) captures.getCaptureGrid()[i][1];
            String countText = String.valueOf(count);
            g.setColor(Color.BLACK);
            g.setFont(new Font("minimalPixel", Font.BOLD, 57));
            g.drawString(countText, TILE_SIZE + 20, i * TILE_SIZE + TILE_SIZE / 2 + 20); // Ajuste vertical aumentado a +25
        }
    }

    public Captures getCaptures() {
        return captures;
    }

    public void setCaptures(Captures captures) {
        this.captures = captures;
    }

    public SpriteManager getSpriteManager() {
        return spriteManager;
    }

    public void setSpriteManager(SpriteManager spriteManager) {
        this.spriteManager = spriteManager;
    }
}