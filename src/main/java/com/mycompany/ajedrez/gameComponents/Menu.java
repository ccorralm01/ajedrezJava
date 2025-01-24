package com.mycompany.ajedrez.gameComponents;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Menu {

    private String name; // Nombre o identificador del componente
    private int x, y; // Posición del componente en el menú
    private BufferedImage sprite; // Sprite del componente

    public Menu(String name, int x, int y, BufferedImage sprite) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.sprite = sprite;
    }

    // Getters
    public String getName() {
        return name;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public BufferedImage getSprite() {
        return sprite;
    }

    // Setters (opcionales)
    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setSprite(BufferedImage sprite) {
        this.sprite = sprite;
    }

    // Método abstracto para dibujar el componente (puede ser sobrescrito por las subclases)
    public abstract void draw(Graphics g);
}