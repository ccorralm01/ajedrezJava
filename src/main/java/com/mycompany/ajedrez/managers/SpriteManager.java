package com.mycompany.ajedrez.managers;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class SpriteManager {
    private BufferedImage boardSpriteSheet; // Sprite sheet del tablero
    private BufferedImage piecesSpriteSheet; // Sprite sheet de las piezas

    private BufferedImage[][] pieceSprites; // Sprites de las piezas
    private BufferedImage[][] tileSprites; // Sprites de las casillas del tablero
    private BufferedImage[] hudSprites; // Sprites del hud

    private int spriteSize = 16;

    public SpriteManager(String boardPath, String piecesPath) {
        try {
            boardSpriteSheet = ImageIO.read(new File(boardPath));
            piecesSpriteSheet = ImageIO.read(new File(piecesPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        extractTileSprites(); // Extraer sprites del tablero
        extractPieceSprites(); // Extraer sprites de las piezas
    }

    private void extractTileSprites() {
        spriteSize = 16; // Tamaño de cada chunk
        tileSprites = new BufferedImage[12][12]; // 2 sprites (blanco y negro)
        for (int row = 0; row < 12; row++) { // 6 filas (blanco y negro)
            for (int col = 0; col < 12; col++) { // 6 columnas (6 piezas)
                int x = col * spriteSize;
                int y = row * spriteSize;
                tileSprites[row][col] = boardSpriteSheet.getSubimage(x, y, spriteSize, spriteSize);
            }
        }
    }

    private void extractPieceSprites() {
        spriteSize = 16; // Tamaño de cada sprite
        pieceSprites = new BufferedImage[5][6]; // 5 filas para las piezas (0 a 4), 6 columnas (6 tipos de piezas)
        hudSprites = new BufferedImage[6]; // 6 sprites para el HUD (fila 5)

        // Extraer sprites de las piezas (filas 0 a 4)
        for (int row = 0; row < 5; row++) { // 5 filas (0 a 4)
            for (int col = 0; col < 6; col++) { // 6 columnas (6 tipos de piezas)
                int x = col * spriteSize;
                int y = row * spriteSize;
                pieceSprites[row][col] = piecesSpriteSheet.getSubimage(x, y, spriteSize, spriteSize);
            }
        }

        // Extraer sprites del HUD (fila 5)
        for (int col = 0; col < 6; col++) { // 6 columnas (6 sprites de HUD)
            int x = col * spriteSize;
            int y = 5 * spriteSize; // Fila 5
            hudSprites[col] = piecesSpriteSheet.getSubimage(x, y, spriteSize, spriteSize);
        }
    }

    public BufferedImage getTileSprite(int row, int col) {
        return tileSprites[row][col]; // 0: casilla blanca, 1: casilla negra
    }

    public BufferedImage getPieceSprite(int color, int piece) {
        return pieceSprites[color][piece];
    }

    public BufferedImage getHudSprites(int type) {
        return hudSprites[type];
    }
}
