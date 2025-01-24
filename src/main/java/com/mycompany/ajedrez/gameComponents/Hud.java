package com.mycompany.ajedrez.gameComponents;

import java.awt.image.BufferedImage;

public class Hud {
    // Constantes para los tipos de piezas
    public static final int KILL = 0;
    public static final int MOVE = 1;
    public static final int SELECTED = 2;
    public static final int LINE = 5;


    private String line; // Linea de la que sacamos el asset
    private int type;     // Tipo de hud (usando las constantes anteriores)
    private BufferedImage sprite; // Sprite de la pieza

    public Hud(String line, int type, BufferedImage sprite) {
        this.line = line;
        this.type = type;
        this.sprite = sprite;
    }

    // Getters
    public String getLine() {
        return line;
    }

    public int getType() {
        return type;
    }

    public BufferedImage getSprite() {
        return sprite;
    }
}
