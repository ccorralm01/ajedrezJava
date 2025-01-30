package com.mycompany.ajedrez.managers;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

/**
 * Clase para gestionar los sprites del juego de ajedrez.
 * Se encarga de cargar y extraer los sprites del tablero, piezas y menú.
 */
public class SpriteManager {
    private BufferedImage boardSpriteSheet; // Sprite sheet del tablero
    private BufferedImage piecesSpriteSheet; // Sprite sheet de las piezas
    private BufferedImage menuSpriteSheet; // Sprite sheet del menú

    private BufferedImage[][] pieceSprites; // Sprites de las piezas
    private BufferedImage[][] tileSprites; // Sprites de las casillas del tablero
    private BufferedImage[] hudSprites; // Sprites del HUD
    private BufferedImage[][] menuSprites; // Sprites del menú
    private int spriteSize = 16;

    /**
     * Constructor de la clase SpriteManager.
     * Carga los archivos de sprites y extrae los sprites individuales.
     *
     * @param boardPath Ruta del sprite sheet del tablero.
     * @param piecesPath Ruta del sprite sheet de las piezas.
     * @param menuPath Ruta del sprite sheet del menú.
     */
    public SpriteManager(String boardPath, String piecesPath, String menuPath) {
        try {
            boardSpriteSheet = ImageIO.read(new File(boardPath));
            piecesSpriteSheet = ImageIO.read(new File(piecesPath));
            menuSpriteSheet = ImageIO.read(new File(menuPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        extractTileSprites();
        extractPieceSprites();
        extractMenuSprites();
    }

    /**
     * Extrae los sprites del tablero en una matriz.
     */
    private void extractTileSprites() {
        tileSprites = new BufferedImage[12][12];
        for (int row = 0; row < 12; row++) {
            for (int col = 0; col < 12; col++) {
                int x = col * spriteSize;
                int y = row * spriteSize;
                tileSprites[row][col] = boardSpriteSheet.getSubimage(x, y, spriteSize, spriteSize);
            }
        }
    }

    /**
     * Extrae los sprites de las piezas y del HUD.
     */
    private void extractPieceSprites() {
        pieceSprites = new BufferedImage[5][6];
        hudSprites = new BufferedImage[6];

        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 6; col++) {
                int x = col * spriteSize;
                int y = row * spriteSize;
                pieceSprites[row][col] = piecesSpriteSheet.getSubimage(x, y, spriteSize, spriteSize);
            }
        }

        for (int col = 0; col < 6; col++) {
            int x = col * spriteSize;
            int y = 5 * spriteSize;
            hudSprites[col] = piecesSpriteSheet.getSubimage(x, y, spriteSize, spriteSize);
        }
    }

    /**
     * Extrae los sprites del menú en una matriz.
     */
    private void extractMenuSprites() {
        menuSprites = new BufferedImage[9][9];
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                int x = col * spriteSize;
                int y = row * spriteSize;
                menuSprites[row][col] = menuSpriteSheet.getSubimage(x, y, spriteSize, spriteSize);
            }
        }
    }

    /**
     * Obtiene un sprite de una casilla del tablero.
     *
     * @param row Fila del sprite.
     * @param col Columna del sprite.
     * @return El sprite de la casilla.
     */
    public BufferedImage getTileSprite(int row, int col) {
        return tileSprites[row][col];
    }

    /**
     * Obtiene un sprite de una pieza de ajedrez.
     *
     * @param color Índice del color de la pieza.
     * @param piece Índice del tipo de pieza.
     * @return El sprite de la pieza.
     */
    public BufferedImage getPieceSprite(int color, int piece) {
        return pieceSprites[color][piece];
    }

    /**
     * Obtiene un sprite del HUD.
     *
     * @param type Índice del sprite del HUD.
     * @return El sprite correspondiente.
     */
    public BufferedImage getHudSprites(int type) {
        return hudSprites[type];
    }

    /**
     * Obtiene un sprite del menú.
     *
     * @param row Fila del sprite.
     * @param col Columna del sprite.
     * @return El sprite del menú.
     */
    public BufferedImage getMenuSprite(int row, int col) {
        return menuSprites[row][col];
    }

    /**
     * Obtiene la imagen de victoria dependiendo del tipo de victoria.
     *
     * @param winType Tipo de victoria (nombre del archivo sin extensión).
     * @return La imagen de victoria o null si hay un error.
     */
    public BufferedImage getVictoryImage(String winType) {
        String imagePath = "src/res/" + winType + ".png";
        try {
            return ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}