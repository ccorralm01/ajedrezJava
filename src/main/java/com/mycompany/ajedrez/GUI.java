package com.mycompany.ajedrez;

import com.mycompany.ajedrez.gameComponents.Board;
import com.mycompany.ajedrez.gameComponents.Captures;
import com.mycompany.ajedrez.managers.AnimationManager;
import com.mycompany.ajedrez.managers.SpriteManager;
import com.mycompany.ajedrez.menuComponents.MenuComponent;
import com.mycompany.ajedrez.panels.BoardPanel;
import com.mycompany.ajedrez.panels.CapturesPanel;
import com.mycompany.ajedrez.panels.HudPanel;
import com.mycompany.ajedrez.panels.MultijugadorPanel;
import com.mycompany.ajedrez.server.Room;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

public class GUI extends JFrame {
    private MenuComponent botonSalir; // Botón para volver al menú principal
    private Room newRoom;

    public GUI(String usuario, String versus, String servidor, String clave, Room newRoom) {
        // Iniciar el juego con los datos ingresados
        SpriteManager spriteManager = new SpriteManager("src/res/Board.png", "src/res/pieces.png", "src/res/menu.png");
        Board board = new Board(spriteManager, usuario, versus, newRoom);
        Captures captures = new Captures(spriteManager); // Crear el objeto Captures
        setResizable(false);

        // Crear paneles
        BoardPanel boardPanel = new BoardPanel(board, spriteManager);
        HudPanel hudPanel = new HudPanel(spriteManager);
        CapturesPanel capturesPanel = new CapturesPanel(captures, spriteManager); // Crear el CapturesPanel

        // Configurar el JLayeredPane para el tablero y el HUD
        JLayeredPane layeredPane = new JLayeredPane() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Dibujar un color de fondo sólido (#C58557)
                g.setColor(new Color(0xC5, 0x85, 0x57)); // Color #C58557
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        layeredPane.setPreferredSize(new Dimension(896, 832)); // Tamaño total: 896x832 (768 + 128 de ancho)

        // Añadir BoardPanel al JLayeredPane
        boardPanel.setBounds(0, 0, 768, 768); // BoardPanel ocupa 768x768px
        boardPanel.setOpaque(false); // Hacer transparente
        layeredPane.add(boardPanel, JLayeredPane.DEFAULT_LAYER); // Capa intermedia

        // Añadir HudPanel al JLayeredPane
        hudPanel.setBounds(0, 0, 768, 768); // HudPanel se superpone al BoardPanel
        hudPanel.setOpaque(false); // Hacer transparente
        layeredPane.add(hudPanel, JLayeredPane.PALETTE_LAYER); // Capa superior

        // Añadir CapturesPanel a la derecha del BoardPanel
        capturesPanel.setBounds(768, 0, 128, 768); // CapturesPanel en los siguientes 128px de ancho
        capturesPanel.setOpaque(false); // Hacer transparente
        layeredPane.add(capturesPanel, JLayeredPane.PALETTE_LAYER); // Capa superior

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
        botonSalirPanel.setOpaque(false); // Hacer transparente

        // Calcular la posición centrada del botón
        int botonWidth = botonSalir.getGridCols() * 64; // Ancho del botón (7 columnas x 64px)
        int botonX = (896 - botonWidth) / 2; // Centrar horizontalmente
        int botonY = 768; // Posición vertical debajo del BoardPanel y CapturesPanel

        // Posicionar el botón centrado
        botonSalirPanel.setBounds(botonX, botonY, botonWidth, 64); // Tamaño basado en gridCols
        layeredPane.add(botonSalirPanel, JLayeredPane.PALETTE_LAYER); // Capa superior

        // Añadir el JLayeredPane al JFrame
        add(layeredPane);

        // Inicializar controlador y animación
        GameController gameController = new GameController(board, boardPanel, hudPanel, capturesPanel,spriteManager);
        AnimationManager animationManager = new AnimationManager(hudPanel);

        // Configurar ventana
        setTitle("PIXEL CHESS | " + usuario + " VS " + versus + " | Servidor: " + servidor + " | Clave: " + clave);
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
        Map<String, String> jugadores = new HashMap<>();
        jugadores.put("userDebug1", "blanco");
        jugadores.put("userDebug2", "negro");
        SwingUtilities.invokeLater(() -> {
            new GUI("", "", "", "", new Room("debug", "debug", jugadores));
        });
    }
}