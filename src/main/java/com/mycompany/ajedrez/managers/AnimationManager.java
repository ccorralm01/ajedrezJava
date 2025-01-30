package com.mycompany.ajedrez.managers;

import com.mycompany.ajedrez.panels.HudPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * La clase AnimationManager gestiona la animación de elementos en el panel HUD (Heads-Up Display).
 * Actualmente, se utiliza para animar una flecha que se mueve hacia arriba y hacia abajo.
 */
public class AnimationManager {
    private HudPanel hudPanel; // Panel HUD que contiene los elementos a animar
    private Timer animationTimer; // Temporizador para controlar la animación
    private int arrowOffsetY = 0; // Desplazamiento vertical de la flecha
    private boolean arrowDirectionUp = true; // Dirección de movimiento de la flecha (arriba o abajo)

    /**
     * Constructor de la clase AnimationManager.
     *
     * @param hudPanel El panel HUD que se va a animar.
     */
    public AnimationManager(HudPanel hudPanel) {
        this.hudPanel = hudPanel;
        initAnimationTimer();
    }

    /**
     * Inicializa el temporizador de animación.
     * Configura un Timer que actualiza la posición de la flecha y redibuja el panel HUD.
     */
    private void initAnimationTimer() {
        animationTimer = new Timer(20, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (arrowDirectionUp) {
                    arrowOffsetY -= 1; // Mover la flecha hacia arriba
                    if (arrowOffsetY < -5) {
                        arrowDirectionUp = false; // Cambiar dirección si se alcanza el límite superior
                    }
                } else {
                    arrowOffsetY += 1; // Mover la flecha hacia abajo
                    if (arrowOffsetY > 5) {
                        arrowDirectionUp = true; // Cambiar dirección si se alcanza el límite inferior
                    }
                }
                hudPanel.setArrowOffsetY(arrowOffsetY); // Actualizar el desplazamiento en el panel HUD
                hudPanel.repaint(); // Redibujar el panel HUD para reflejar los cambios
            }
        });
    }

    /**
     * Inicia la animación.
     * Comienza el temporizador para que la flecha se mueva.
     */
    public void startAnimation() {
        animationTimer.start();
    }

    /**
     * Detiene la animación.
     * Detiene el temporizador para que la flecha deje de moverse.
     */
    public void stopAnimation() {
        animationTimer.stop();
    }
}