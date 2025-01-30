package com.mycompany.ajedrez.gameComponents;

import java.awt.image.BufferedImage;

/**
 * La clase Hud representa un elemento de la interfaz de usuario (HUD) en el juego de ajedrez.
 * Puede ser utilizado para mostrar información como movimientos, selecciones, líneas de guía, etc.
 */
public class Hud {
    // Constantes para los tipos de piezas
    public static final int KILL = 0;    // Representa un evento de captura de pieza.
    public static final int MOVE = 1;    // Representa un evento de movimiento de pieza.
    public static final int SELECTED = 2; // Representa una pieza seleccionada.
    public static final int LINE = 5;    // Representa una línea de guía o referencia.

    private String line; // Línea de la que se extrae el recurso gráfico (asset).
    private int type;    // Tipo de HUD, definido por las constantes anteriores.
    private BufferedImage sprite; // Sprite o imagen asociada al HUD.

    /**
     * Constructor de la clase Hud.
     *
     * @param line   La línea de la que se extrae el recurso gráfico.
     * @param type   El tipo de HUD, definido por las constantes de la clase.
     * @param sprite La imagen o sprite asociado al HUD.
     */
    public Hud(String line, int type, BufferedImage sprite) {
        this.line = line;
        this.type = type;
        this.sprite = sprite;
    }

    /**
     * Obtiene la línea de la que se extrae el recurso gráfico.
     *
     * @return La línea asociada al HUD.
     */
    public String getLine() {
        return line;
    }

    /**
     * Obtiene el tipo de HUD.
     *
     * @return El tipo de HUD, definido por las constantes de la clase.
     */
    public int getType() {
        return type;
    }

    /**
     * Obtiene el sprite o imagen asociado al HUD.
     *
     * @return La imagen (BufferedImage) del HUD.
     */
    public BufferedImage getSprite() {
        return sprite;
    }
}