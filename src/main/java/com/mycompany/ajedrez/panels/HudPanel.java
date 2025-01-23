package com.mycompany.ajedrez.panels;

import com.mycompany.ajedrez.gameComponents.Hud;
import com.mycompany.ajedrez.managers.SpriteManager;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class HudPanel extends JPanel {
    private SpriteManager spriteManager;
    private Hud selectedHud = null; // Indicador de selección
    private int arrowOffsetY = 0; // Desplazamiento vertical de la flechita
    private List<Point> validMoves = new ArrayList<>(); // Lista de movimientos válidos
    private int selectedX = -1, selectedY = -1; // Coordenadas de la pieza seleccionada

    public HudPanel(SpriteManager spriteManager) {
        this.spriteManager = spriteManager;
        setOpaque(false); // Hacer el panel transparente
    }

    public void setSelectedHud(Hud selectedHud) {
        this.selectedHud = selectedHud;
    }


    public void setValidMoves(List<Point> validMoves) {
        this.validMoves = validMoves;
    }

    public void setSelectedPosition(int x, int y) {
        this.selectedX = x;
        this.selectedY = y;
    }

    public void setArrowOffsetY(int arrowOffsetY) {
        this.arrowOffsetY = arrowOffsetY;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawHud(g);
    }

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