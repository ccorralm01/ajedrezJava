package com.mycompany.ajedrez;

import com.mycompany.ajedrez.managers.SpriteManager;
import com.mycompany.ajedrez.panels.MenuPanel;

import javax.swing.*;
import java.awt.*;

public class Main {

    public void runMain() {
        // Crear el SpriteManager con las rutas de los archivos de sprites
        SpriteManager spriteManager = new SpriteManager("src/res/Board.png", "src/res/pieces.png", "src/res/menu.png");

        // Crear la ventana principal
        JFrame frame = new JFrame("PIXEL CHESS");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(173, 216, 230));
        frame.setResizable(false);

        // Crear el panel del menú
        MenuPanel menuPanel = new MenuPanel(spriteManager, frame);
        frame.add(menuPanel, BorderLayout.CENTER); // Añadir el panel del menú a la ventana

        // Configurar la ventana
        frame.pack();
        frame.setLocationRelativeTo(null); // Centrar la ventana en la pantalla
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        Main main = new Main();
        main.runMain();
    }
}