package com.mycompany.ajedrez.managers;

import com.mycompany.ajedrez.panels.HudPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AnimationManager {
    private HudPanel hudPanel;
    private Timer animationTimer;
    private int arrowOffsetY = 0;
    private boolean arrowDirectionUp = true;

    public AnimationManager(HudPanel hudPanel) {
        this.hudPanel = hudPanel;
        initAnimationTimer();
    }

    private void initAnimationTimer() {
        animationTimer = new Timer(20, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (arrowDirectionUp) {
                    arrowOffsetY -= 1;
                    if (arrowOffsetY < -5) {
                        arrowDirectionUp = false;
                    }
                } else {
                    arrowOffsetY += 1;
                    if (arrowOffsetY > 5) {
                        arrowDirectionUp = true;
                    }
                }
                hudPanel.setArrowOffsetY(arrowOffsetY); // Actualizar el valor en HudPanel
                hudPanel.repaint(); // Redibujar el HudPanel
            }
        });
    }

    public void startAnimation() {
        animationTimer.start();
    }

    public void stopAnimation() {
        animationTimer.stop();
    }
}