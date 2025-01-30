package com.mycompany.ajedrez.panels;

import com.mycompany.ajedrez.gameComponents.Hud;
import com.mycompany.ajedrez.managers.SpriteManager;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa el panel del HUD (Heads-Up Display) en el juego de ajedrez.
 * Este panel se encarga de dibujar los indicadores visuales, como los movimientos válidos
 * y la selección de piezas, superpuestos sobre el tablero de ajedrez.
 */
public class HudPanel extends JPanel {
    /** Gestor de sprites utilizado para obtener las imágenes del HUD. */
    private SpriteManager spriteManager;

    /** Indicador de selección actual en el HUD. */
    private Hud selectedHud = null;

    /** Desplazamiento vertical de la flecha de selección. */
    private int arrowOffsetY = 0;

    /** Lista de movimientos válidos para la pieza seleccionada. */
    private List<Point> validMoves = new ArrayList<>();

    /** Coordenadas de la pieza seleccionada en el tablero. */
    private int selectedX = -1, selectedY = -1;

    /**
     * Constructor de la clase HudPanel.
     *
     * @param spriteManager Gestor de sprites que proporciona las imágenes del HUD.
     */
    public HudPanel(SpriteManager spriteManager) {
        this.spriteManager = spriteManager;
        setOpaque(false); // Hacer el panel transparente
    }

    /**
     * Establece el HUD seleccionado.
     *
     * @param selectedHud El HUD que representa la selección actual.
     */
    public void setSelectedHud(Hud selectedHud) {
        this.selectedHud = selectedHud;
    }

    /**
     * Establece la lista de movimientos válidos para la pieza seleccionada.
     *
     * @param validMoves Lista de puntos (coordenadas) que representan los movimientos válidos.
     */
    public void setValidMoves(List<Point> validMoves) {
        this.validMoves = validMoves;
    }

    /**
     * Establece la posición de la pieza seleccionada en el tablero.
     *
     * @param x Coordenada X de la pieza seleccionada.
     * @param y Coordenada Y de la pieza seleccionada.
     */
    public void setSelectedPosition(int x, int y) {
        this.selectedX = x;
        this.selectedY = y;
    }

    /**
     * Establece el desplazamiento vertical de la flecha de selección.
     *
     * @param arrowOffsetY Desplazamiento vertical en píxeles.
     */
    public void setArrowOffsetY(int arrowOffsetY) {
        this.arrowOffsetY = arrowOffsetY;
    }

    /**
     * Método sobrescrito para dibujar los componentes del HUD.
     *
     * @param g El contexto gráfico en el que se dibuja el HUD.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawHud(g);
    }

    /**
     * Dibuja los elementos del HUD, como los movimientos válidos y la selección de piezas.
     *
     * @param g El contexto gráfico en el que se dibuja el HUD.
     */
    private void drawHud(Graphics g) {
        int tileSize = 64; // Tamaño de cada casilla en píxeles
        int offset = 2 * tileSize; // Desplazamiento para centrar las fichas en el tablero de 8x8

        // Dibujar los indicadores de movimiento válido
        for (Point move : validMoves) {
            Hud moveHud = new Hud("move", Hud.MOVE, spriteManager.getHudSprites(Hud.MOVE));
            BufferedImage moveSprite = moveHud.getSprite();
            g.drawImage(moveSprite, move.x * tileSize + offset, move.y * tileSize + offset, tileSize, tileSize, null);
        }

        // Dibujar el indicador SELECTED si hay una pieza seleccionada
        if (selectedHud != null && selectedX != -1 && selectedY != -1) {
            // Verificar que la casilla superior esté dentro del tablero
            if (selectedY - 1 >= 0) {
                BufferedImage hudSprite = selectedHud.getSprite();
                // Aplicar el desplazamiento vertical de la animación
                g.drawImage(hudSprite, selectedX * tileSize + offset, (selectedY - 1) * tileSize + offset + arrowOffsetY, tileSize, tileSize, null);
            }
        }
    }
}