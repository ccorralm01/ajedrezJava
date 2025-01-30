/**
 * Representa un componente del menú en el juego de ajedrez, como títulos y botones.
 * Permite la generación y renderizado de elementos visuales en el menú.
 */
package com.mycompany.ajedrez.menuComponents;

import com.mycompany.ajedrez.managers.SpriteManager;

import java.awt.*;
import java.awt.image.BufferedImage;

public class MenuComponent {

    private SpriteManager spriteManager;

    /** Estilos del recurso */
    public static final int SIMPLE = 0;
    public static final int MINIMAL = 1;
    public static final int PRETY = 2;

    /** Tipos de recurso */
    public static final int TITLE = 3;
    public static final int BUTTON1 = 4;
    public static final int BUTTON2 = 5;

    private int type; // Tipo de título o botón
    private String text; // Texto del componente
    private int gridCols; // Número de columnas que ocupa
    private int spriteRow; // Fila en el sprite sheet
    private BufferedImage[] titleSprites; // Sprites del componente
    private int fontSize;
    private int fontOffset;

    /**
     * Constructor de MenuComponent.
     *
     * @param spriteManager Gestor de sprites
     * @param type Tipo de componente (SIMPLE, MINIMAL, PRETY)
     * @param text Texto a mostrar
     * @param gridCols Número de columnas que ocupa el componente
     * @param spriteRow Fila en el sprite sheet
     * @param fontSize Tamaño de la fuente
     * @param fontOffset Desplazamiento vertical del texto
     */
    public MenuComponent(SpriteManager spriteManager, int type, String text, int gridCols, int spriteRow, int fontSize, int fontOffset) {
        this.spriteManager = spriteManager;
        this.type = type;
        this.text = text;
        this.gridCols = gridCols;
        this.spriteRow = spriteRow;
        this.fontSize = fontSize;
        this.fontOffset = fontOffset;
        this.titleSprites = new BufferedImage[gridCols];
        generateTitle();
    }

    /**
     * Genera los sprites del título basados en el tipo y la fila del sprite sheet.
     */
    public void generateTitle() {
        int spriteColOffset = type * 3;
        titleSprites[0] = getSprite(spriteRow, spriteColOffset);
        titleSprites[gridCols - 1] = getSprite(spriteRow, spriteColOffset + 2);
        for (int col = 1; col < gridCols - 1; col++) {
            titleSprites[col] = getSprite(spriteRow, spriteColOffset + 1);
        }
    }

    /**
     * Obtiene un sprite específico desde el gestor de sprites.
     *
     * @param row Fila en el sprite sheet
     * @param col Columna en el sprite sheet
     * @return Imagen del sprite
     */
    private BufferedImage getSprite(int row, int col) {
        return spriteManager.getMenuSprite(row, col);
    }

    /**
     * Dibuja el componente en la pantalla.
     *
     * @param g Objeto Graphics para dibujar
     * @param x Coordenada X de inicio
     * @param y Coordenada Y de inicio
     * @param tileSize Tamaño de cada sprite en píxeles
     */
    public void draw(Graphics g, int x, int y, int tileSize) {
        for (int col = 0; col < gridCols; col++) {
            int drawX = x + (col * tileSize);
            g.drawImage(titleSprites[col], drawX, y, tileSize, tileSize, null);
        }
        Font font = new Font("minimalPixel", Font.BOLD, fontSize);
        g.setFont(font);
        if (type == PRETY) {
            font = new Font("minimalPixel", Font.BOLD, fontSize - 5);
        } else {
            g.setColor(Color.WHITE);
        }
        FontMetrics metrics = g.getFontMetrics(font);
        int textWidth = metrics.stringWidth(text);
        int textX = x + ((gridCols * tileSize) - textWidth) / 2;
        int textY = y + (tileSize + metrics.getAscent()) / 2 - fontOffset;
        g.drawString(text, textX, textY);
    }

    // Métodos getter y setter
    public SpriteManager getSpriteManager() { return spriteManager; }
    public void setSpriteManager(SpriteManager spriteManager) { this.spriteManager = spriteManager; }
    public int getType() { return type; }
    public void setType(int type) { this.type = type; generateTitle(); }
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public int getGridCols() { return gridCols; }
    public void setGridCols(int gridCols) { this.gridCols = gridCols; }
    public int getSpriteRow() { return spriteRow; }
    public void setSpriteRow(int spriteRow) { this.spriteRow = spriteRow; }
    public BufferedImage[] getTitleSprites() { return titleSprites; }
    public void setTitleSprites(BufferedImage[] titleSprites) { this.titleSprites = titleSprites; }
}
