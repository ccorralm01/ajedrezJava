package com.mycompany.ajedrez.managers;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class SpriteManager {
    private BufferedImage boardSpriteSheet; // Sprite sheet del tablero
    private BufferedImage piecesSpriteSheet; // Sprite sheet de las piezas
    private BufferedImage menuSpriteSheet; // Sprite sheet del menú

    private BufferedImage[][] pieceSprites; // Sprites de las piezas
    private BufferedImage[][] tileSprites; // Sprites de las casillas del tablero
    private BufferedImage[] hudSprites; // Sprites del HUD
    private BufferedImage[][] menuSprites; // Sprites del menú
    private int spriteSize = 16;

    public SpriteManager(String boardPath, String piecesPath, String menuPath) {
        try {
            boardSpriteSheet = ImageIO.read(new File(boardPath));
            piecesSpriteSheet = ImageIO.read(new File(piecesPath));
            menuSpriteSheet = ImageIO.read(new File(menuPath)); // Cargar la imagen del menú
        } catch (IOException e) {
            e.printStackTrace();
        }
        extractTileSprites(); // Extraer sprites del tablero
        extractPieceSprites(); // Extraer sprites de las piezas
        extractMenuSprites(); // Extraer sprites del menú
    }

    private void extractTileSprites() {
        spriteSize = 16; // Tamaño de cada chunk
        tileSprites = new BufferedImage[12][12]; // 12 filas y 12 columnas
        for (int row = 0; row < 12; row++) {
            for (int col = 0; col < 12; col++) {
                int x = col * spriteSize;
                int y = row * spriteSize;
                tileSprites[row][col] = boardSpriteSheet.getSubimage(x, y, spriteSize, spriteSize);
            }
        }
    }

    private void extractPieceSprites() {
        spriteSize = 16; // Tamaño de cada sprite
        pieceSprites = new BufferedImage[5][6]; // 5 filas para las piezas, 6 columnas
        hudSprites = new BufferedImage[6]; // 6 sprites para el HUD

        // Extraer sprites de las piezas (filas 0 a 4)
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 6; col++) {
                int x = col * spriteSize;
                int y = row * spriteSize;
                pieceSprites[row][col] = piecesSpriteSheet.getSubimage(x, y, spriteSize, spriteSize);
            }
        }

        // Extraer sprites del HUD (fila 5)
        for (int col = 0; col < 6; col++) {
            int x = col * spriteSize;
            int y = 5 * spriteSize; // Fila 5
            hudSprites[col] = piecesSpriteSheet.getSubimage(x, y, spriteSize, spriteSize);
        }
    }

    private void extractMenuSprites() {
        spriteSize = 16; // Tamaño de cada sprite
        menuSprites = new BufferedImage[9][9]; // 9 filas y 9 columnas

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                int x = col * spriteSize;
                int y = row * spriteSize;
                menuSprites[row][col] = menuSpriteSheet.getSubimage(x, y, spriteSize, spriteSize);
            }
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

    public BufferedImage getMenuSprite(int row, int col) {
        return menuSprites[row][col];
    }
}