/**
 * Clase que representa un fondo en el menú del juego de ajedrez.
 * Se compone de diferentes tipos de fondos con bordes y rellenos generados a partir de un {@link SpriteManager}.
 */
package com.mycompany.ajedrez.menuComponents;

import com.mycompany.ajedrez.managers.SpriteManager;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Background {
    private SpriteManager spriteManager;

    /** Tipos de fondo disponibles */
    public static final int SIMPLE = 0;
    public static final int MINIMAL = 1;
    public static final int PRETY = 2;

    /** Piezas del background (izquierda, centro, derecha) */
    public static final int LEFT = 0;
    public static final int MID = 1;
    public static final int RIGHT = 2;

    private int type; // Tipo de fondo (SIMPLE, MINIMAL, PRETY)
    private int gridRows; // Número de filas del fondo
    private int gridCols; // Número de columnas del fondo
    private BufferedImage[][] background; // Matriz de sprites del fondo

    /**
     * Constructor de la clase Background.
     *
     * @param spriteManager Gestor de sprites que proporciona los gráficos del fondo
     * @param type Tipo de fondo (SIMPLE, MINIMAL o PRETY)
     * @param gridRows Número de filas en la cuadrícula del fondo
     * @param gridCols Número de columnas en la cuadrícula del fondo
     */
    public Background(SpriteManager spriteManager, int type, int gridRows, int gridCols) {
        this.spriteManager = spriteManager;
        this.type = type;
        this.gridRows = gridRows;
        this.gridCols = gridCols;
        this.background = new BufferedImage[gridRows][gridCols];
        generateBackground();
    }

    /**
     * Genera la estructura del fondo con bordes y relleno.
     */
    public void generateBackground() {
        int spriteRowOffset = type * 3; // Desplazamiento de fila según el tipo

        // Colocar las esquinas
        background[0][0] = getSprite(spriteRowOffset + 0, 0);
        background[0][gridCols - 1] = getSprite(spriteRowOffset + 0, 2);
        background[gridRows - 1][0] = getSprite(spriteRowOffset + 2, 0);
        background[gridRows - 1][gridCols - 1] = getSprite(spriteRowOffset + 2, 2);

        // Colocar los bordes
        for (int col = 1; col < gridCols - 1; col++) {
            background[0][col] = getSprite(spriteRowOffset + 0, 1);
            background[gridRows - 1][col] = getSprite(spriteRowOffset + 2, 1);
        }

        for (int row = 1; row < gridRows - 1; row++) {
            background[row][0] = getSprite(spriteRowOffset + 1, 0);
            background[row][gridCols - 1] = getSprite(spriteRowOffset + 1, 2);
        }

        // Rellenar el interior con el sprite central
        for (int row = 1; row < gridRows - 1; row++) {
            for (int col = 1; col < gridCols - 1; col++) {
                background[row][col] = getSprite(spriteRowOffset + 1, 1);
            }
        }
    }

    /**
     * Obtiene un sprite específico del menú.
     *
     * @param row Fila del sprite
     * @param col Columna del sprite
     * @return Imagen del sprite correspondiente
     */
    private BufferedImage getSprite(int row, int col) {
        return spriteManager.getMenuSprite(row, col);
    }

    /**
     * Dibuja el fondo en la posición especificada.
     *
     * @param g Objeto Graphics para dibujar
     * @param x Posición x en pantalla
     * @param y Posición y en pantalla
     * @param tileSize Tamaño de cada sprite en píxeles
     */
    public void draw(Graphics g, int x, int y, int tileSize) {
        for (int row = 0; row < gridRows; row++) {
            for (int col = 0; col < gridCols; col++) {
                int drawX = x + (col * tileSize);
                int drawY = y + (row * tileSize);
                g.drawImage(background[row][col], drawX, drawY, tileSize, tileSize, null);
            }
        }
    }

    // Métodos getter y setter
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
