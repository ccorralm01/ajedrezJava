package com.mycompany.ajedrez;

import com.mycompany.ajedrez.gameComponents.Board;
import com.mycompany.ajedrez.managers.AnimationManager;
import com.mycompany.ajedrez.managers.SpriteManager;
import com.mycompany.ajedrez.menuComponents.MenuComponent;
import com.mycompany.ajedrez.panels.BoardPanel;
import com.mycompany.ajedrez.panels.HudPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GUI extends JFrame {
    private MenuComponent botonSalir; // Botón para volver al menú principal

    public GUI(String usuario, String servidor, String clave) {
        // Iniciar el juego con los datos ingresados
        SpriteManager spriteManager = new SpriteManager("src/res/Board.png", "src/res/pieces.png", "src/res/menu.png");
        Board board = new Board(spriteManager);
        setResizable(false);

        // Crear paneles
        BoardPanel boardPanel = new BoardPanel(board, spriteManager);
        HudPanel hudPanel = new HudPanel(spriteManager);

        // Configurar layout
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(768, 832)); // Tamaño del tablero

        // Añadir paneles al JLayeredPane
        boardPanel.setBounds(0, 0, 768, 832); // Establecer posición y tamaño del BoardPanel
        hudPanel.setBounds(0, 0, 768, 832);   // Establecer posición y tamaño del HudPanel
        boardPanel.setOpaque(false);
        hudPanel.setOpaque(false);

        layeredPane.add(boardPanel, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(hudPanel, JLayeredPane.PALETTE_LAYER);

        // Crear el botón "SALIR"
        botonSalir = new MenuComponent(spriteManager, MenuComponent.SIMPLE, "SALIR", 7, MenuComponent.BUTTON2, 72, 6);

        // Crear un JPanel para envolver el botón "SALIR"
        JPanel botonSalirPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Dibujar el botón "SALIR" en el panel
                botonSalir.draw(g, 0, 0, 64); // Tamaño de cada tile (64x64 píxeles)
            }
        };

        // Posicionar el botón debajo del tablero
        int botonX = (768 - (botonSalir.getGridCols() * 64)) / 2; // Centrar horizontalmente
        int botonY = 832 + 20; // Colocar debajo del tablero (832 es la altura del tablero)
        botonSalirPanel.setBounds(botonX, botonY, botonSalir.getGridCols() * 64, 64); // Tamaño basado en gridCols

        // Añadir el panel del botón al JLayeredPane
        layeredPane.add(botonSalirPanel, JLayeredPane.PALETTE_LAYER);

        // Ajustar el tamaño del JLayeredPane para incluir el botón
        layeredPane.setPreferredSize(new Dimension(768, 832 + 84)); // 832 (tablero) + 84 (espacio para el botón)

        // Añadir el JLayeredPane al JFrame
        add(layeredPane);

        // Inicializar controlador y animación
        GameController gameController = new GameController(board, boardPanel, hudPanel, spriteManager);
        AnimationManager animationManager = new AnimationManager(hudPanel);

        // Configurar ventana
        setTitle("PIXEL CHESS | Usuario: " + usuario + " | Servidor: " + servidor + " | Clave: " + clave);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack(); // Ajustar el tamaño de la ventana al contenido
        setLocationRelativeTo(null); // Centrar la ventana en la pantalla
        setVisible(true);

        gameController.setMyTurn(true);

        // Añadir un MouseListener para detectar clics en el botón "SALIR"
        botonSalirPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                botonSalir.setType(MenuComponent.PRETY); // Cambiar a PRETY BUTTON2
                botonSalirPanel.repaint(); // Redibujar el panel del botón
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                botonSalir.setType(MenuComponent.SIMPLE); // Restaurar a SIMPLE BUTTON2
                botonSalirPanel.repaint(); // Redibujar el panel del botón
                volverAMenuPrincipal(); // Volver al menú principal
            }
        });
    }

    private void volverAMenuPrincipal() {
        // Cerrar la ventana actual (GUI)
        this.dispose();

        // Abrir una nueva ventana con el menú principal
        Main main = new Main();
        main.runMain();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new GUI("", "", "");
        });
    }
}