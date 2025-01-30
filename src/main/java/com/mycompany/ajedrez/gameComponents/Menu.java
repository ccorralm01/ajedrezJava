package com.mycompany.ajedrez.gameComponents;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * La clase abstracta Menu representa un componente genérico de un menú en el juego de ajedrez.
 * Proporciona una estructura base para elementos de menú, como botones, etiquetas, etc.
 */
public abstract class Menu {

    private String name; // Nombre o identificador del componente
    private int x, y; // Posición del componente en el menú
    private BufferedImage sprite; // Sprite del componente

    /**
     * Constructor de la clase Menu.
     *
     * @param name   El nombre o identificador del componente.
     * @param x      La coordenada x (horizontal) del componente en el menú.
     * @param y      La coordenada y (vertical) del componente en el menú.
     * @param sprite La imagen (sprite) asociada al componente.
     */
    public Menu(String name, int x, int y, BufferedImage sprite) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.sprite = sprite;
    }

    /**
     * Obtiene el nombre o identificador del componente.
     *
     * @return El nombre del componente.
     */
    public String getName() {
        return name;
    }

    /**
     * Obtiene la coordenada x (horizontal) del componente en el menú.
     *
     * @return La coordenada x del componente.
     */
    public int getX() {
        return x;
    }

    /**
     * Obtiene la coordenada y (vertical) del componente en el menú.
     *
     * @return La coordenada y del componente.
     */
    public int getY() {
        return y;
    }

    /**
     * Obtiene la imagen (sprite) asociada al componente.
     *
     * @return La imagen (BufferedImage) del componente.
     */
    public BufferedImage getSprite() {
        return sprite;
    }

    /**
     * Establece la coordenada x (horizontal) del componente en el menú.
     *
     * @param x La nueva coordenada x del componente.
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Establece la coordenada y (vertical) del componente en el menú.
     *
     * @param y La nueva coordenada y del componente.
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Establece la imagen (sprite) asociada al componente.
     *
     * @param sprite La nueva imagen (BufferedImage) del componente.
     */
    public void setSprite(BufferedImage sprite) {
        this.sprite = sprite;
    }

    /**
     * Método abstracto para dibujar el componente en la pantalla.
     * Debe ser implementado por las subclases para definir cómo se dibuja el componente.
     *
     * @param g El objeto Graphics utilizado para dibujar el componente.
     */
    public abstract void draw(Graphics g);
}