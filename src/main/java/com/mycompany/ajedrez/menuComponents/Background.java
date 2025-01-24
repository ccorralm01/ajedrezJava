package com.mycompany.ajedrez.menuComponents;

import com.mycompany.ajedrez.managers.SpriteManager;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Background {
    private SpriteManager spriteManager;

    // Tipos de fondo
    public static final int SIMPLE = 0;
    public static final int MINIMAL = 1;
    public static final int PRETY = 2;

    // Piezas del background (izquierda, centro, derecha)
    public static final int LEFT = 0;
    public static final int MID = 1;
    public static final int RIGHT = 2;

    private int type; // Tipo de fondo (SIMPLE (0), MINIMAL (1), PRETY (2))
    private int gridRows; // Número de filas que ocupa el fondo
    private int gridCols; // Número de columnas que ocupa el fondo
    private BufferedImage[][] background;

    public Background(SpriteManager spriteManager, int type, int gridRows, int gridCols) {
        this.spriteManager = spriteManager;
        this.type = type;
        this.gridRows = gridRows;
        this.gridCols = gridCols;
        this.background = new BufferedImage[gridRows][gridCols];
        generateBackground();
    }

    public void generateBackground() {
        int spriteRowOffset = type * 3; // Desplazamiento de fila según el tipo

        // Colocar las esquinas
        background[0][0] = getSprite(spriteRowOffset + 0, 0); // Esquina superior izquierda
        background[0][gridCols - 1] = getSprite(spriteRowOffset + 0, 2); // Esquina superior derecha
        background[gridRows - 1][0] = getSprite(spriteRowOffset + 2, 0); // Esquina inferior izquierda
        background[gridRows - 1][gridCols - 1] = getSprite(spriteRowOffset + 2, 2); // Esquina inferior derecha

        // Colocar las paredes (bordes)
        for (int col = 1; col < gridCols - 1; col++) {
            // Borde superior
            background[0][col] = getSprite(spriteRowOffset + 0, 1);
            // Borde inferior
            background[gridRows - 1][col] = getSprite(spriteRowOffset + 2, 1);
        }

        for (int row = 1; row < gridRows - 1; row++) {
            // Borde izquierdo
            background[row][0] = getSprite(spriteRowOffset + 1, 0);
            // Borde derecho
            background[row][gridCols - 1] = getSprite(spriteRowOffset + 1, 2);
        }

        // Rellenar el interior con el sprite central
        for (int row = 1; row < gridRows - 1; row++) {
            for (int col = 1; col < gridCols - 1; col++) {
                background[row][col] = getSprite(spriteRowOffset + 1, 1); // Sprite central
            }
        }
    }

    private BufferedImage getSprite(int row, int col) {
        return spriteManager.getMenuSprite(row, col);
    }

    public void draw(Graphics g, int x, int y, int tileSize) {
        for (int row = 0; row < gridRows; row++) {
            for (int col = 0; col < gridCols; col++) {
                int drawX = x + (col * tileSize);
                int drawY = y + (row * tileSize);
                g.drawImage(background[row][col], drawX, drawY, tileSize, tileSize, null);
            }
        }
    }

    public SpriteManager getSpriteManager() {
        return spriteManager;
    }

    public void setSpriteManager(SpriteManager spriteManager) {
        this.spriteManager = spriteManager;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getGridRows() {
        return gridRows;
    }

    public void setGridRows(int gridRows) {
        this.gridRows = gridRows;
    }

    public int getGridCols() {
        return gridCols;
    }

    public void setGridCols(int gridCols) {
        this.gridCols = gridCols;
    }

    public BufferedImage[][] getBackground() {
        return background;
    }

    public void setBackground(BufferedImage[][] background) {
        this.background = background;
    }
}