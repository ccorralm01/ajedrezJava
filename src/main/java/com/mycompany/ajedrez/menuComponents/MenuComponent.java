package com.mycompany.ajedrez.menuComponents;

import com.mycompany.ajedrez.managers.SpriteManager;

import java.awt.*;
import java.awt.image.BufferedImage;

public class MenuComponent {

    private SpriteManager spriteManager;

    // Estilo del recurso
    public static final int SIMPLE = 0;
    public static final int MINIMAL = 1;
    public static final int PRETY = 2;

    // Tipo de recurso
    public static final int TITLE = 3;
    public static final int BUTTON1 = 4;
    public static final int BUTTON2 = 5;

    private int type; // Tipo de título (SIMPLE (0), MINIMAL (1), PRETY (2))
    private String text; // Texto del título
    private int gridCols; // Número de columnas que ocupa el título (se ajusta al texto)
    private int spriteRow; // Fila en el sprite sheet (pasada por parámetro)
    private BufferedImage[] titleSprites; // Sprites del título
    private int fontSize;
    private int fontOffset;

    public MenuComponent(SpriteManager spriteManager, int type, String text, int gridCols, int spriteRow, int fontSize, int fontOffset) {
        this.spriteManager = spriteManager;
        this.type = type;
        this.text = text;
        this.gridCols = gridCols; // Número de columnas del título
        this.spriteRow = spriteRow; // Fila en el sprite sheet
        this.fontSize = fontSize;
        this.fontOffset = fontOffset;
        this.titleSprites = new BufferedImage[gridCols];
        generateTitle();
    }

    public void generateTitle() {
        int spriteColOffset = type * 3; // Desplazamiento de columna según el tipo

        // Colocar las esquinas y repetir el centro
        titleSprites[0] = getSprite(spriteRow, spriteColOffset + 0); // Esquina izquierda
        titleSprites[gridCols - 1] = getSprite(spriteRow, spriteColOffset + 2); // Esquina derecha

        // Rellenar el centro con el sprite central
        for (int col = 1; col < gridCols - 1; col++) {
            titleSprites[col] = getSprite(spriteRow, spriteColOffset + 1); // Sprite central
        }
    }

    private BufferedImage getSprite(int row, int col) {
        return spriteManager.getMenuSprite(row, col);
    }

    public void draw(Graphics g, int x, int y, int tileSize) {
        // Dibujar el título
        for (int col = 0; col < gridCols; col++) {
            int drawX = x + (col * tileSize);
            g.drawImage(titleSprites[col], drawX, y, tileSize, tileSize, null);
        }

        // Dibujar el texto en el centro del título
        Font font = new Font("minimalPixel", Font.BOLD, fontSize); // Fuente del texto
        g.setFont(font);

        // Cambiar el color del texto según el tipo de botón
        if (type == PRETY) {
            font = new Font("minimalPixel", Font.BOLD, fontSize-5); // Fuente del texto
        } else {
            g.setColor(Color.WHITE);
            font = new Font("minimalPixel", Font.BOLD, fontSize); // Fuente del texto
        }

        // Calcular la posición del texto
        FontMetrics metrics = g.getFontMetrics(font);
        int textWidth = metrics.stringWidth(text);
        int textX = x + ((gridCols * tileSize) - textWidth) / 2; // Centrar horizontalmente
        int textY = y + (tileSize + metrics.getAscent()) / 2 - fontOffset; // Centrar verticalmente + 10 px abajo

        g.drawString(text, textX, textY);
    }


    // Getters y Setters
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
        generateTitle(); // Regenerar el título con el nuevo tipo
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getGridCols() {
        return gridCols;
    }

    public void setGridCols(int gridCols) {
        this.gridCols = gridCols;
    }

    public int getSpriteRow() {
        return spriteRow;
    }

    public void setSpriteRow(int spriteRow) {
        this.spriteRow = spriteRow;
    }

    public BufferedImage[] getTitleSprites() {
        return titleSprites;
    }

    public void setTitleSprites(BufferedImage[] titleSprites) {
        this.titleSprites = titleSprites;
    }
}