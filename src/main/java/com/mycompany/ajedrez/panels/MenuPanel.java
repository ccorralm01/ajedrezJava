package com.mycompany.ajedrez.panels;

import com.mycompany.ajedrez.managers.SpriteManager;
import com.mycompany.ajedrez.menuComponents.Background;
import com.mycompany.ajedrez.menuComponents.MenuComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MenuPanel extends JLayeredPane {
    private Background background;
    private MenuComponent button1;
    private MenuComponent button2;
    private MenuComponent button3; // Nuevo botón para opciones
    private SpriteManager spriteManager;
    private JFrame parentFrame; // Referencia a la ventana principal

    public MenuPanel(SpriteManager spriteManager, JFrame parentFrame) {
        this.spriteManager = spriteManager;
        this.parentFrame = parentFrame; // Guardar la referencia a la ventana principal
        setPreferredSize(new Dimension(768, 832)); // Tamaño del panel del menú

        // Establecer el color de fondo azul pastel
        Color azulPastel = new Color(173, 216, 230); // Código RGB para azul pastel
        setBackground(azulPastel); // Establecer el color de fondo del JLayeredPane

        // Crear un fondo de tipo SIMPLE con un grid de 12x12
        background = new Background(spriteManager, Background.SIMPLE, 13, 12);

        // Crear un JPanel para el fondo y añadirlo a la capa DEFAULT_LAYER
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Dibujar el fondo
                background.draw(g, 0, 0, 64); // Tamaño de cada tile (64x64 píxeles)
            }
        };
        backgroundPanel.setBounds(0, 0, 768, 832); // Establecer posición y tamaño del fondo
        add(backgroundPanel, JLayeredPane.DEFAULT_LAYER);

        // Crear un JLabel para el texto "PIXEL CHESS"
        JLabel titleLabel = new JLabel("PIXEL CHESS");
        titleLabel.setFont(new Font("minimalPixel", Font.BOLD, 102)); // Fuente y tamaño
        titleLabel.setForeground(Color.BLACK); // Color del texto
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER); // Centrar el texto

        // Calcular la posición del título
        int titleWidth = 768; // Ancho del panel
        int titleHeight = 100; // Altura del título
        int titleX = 0; // Centrar horizontalmente
        int titleY = 50; // Posición vertical del título

        // Establecer posición y tamaño del JLabel
        titleLabel.setBounds(titleX, titleY, titleWidth, titleHeight);

        // Añadir el JLabel a la capa PALETTE_LAYER
        add(titleLabel, JLayeredPane.PALETTE_LAYER);

        // Crear los botones (por defecto SIMPLE BUTTON2)
        button1 = new MenuComponent(spriteManager, MenuComponent.SIMPLE, "MULTIJUGADOR", 7, MenuComponent.BUTTON2, 72, 6);
        button2 = new MenuComponent(spriteManager, MenuComponent.SIMPLE, "SALIR", 7, MenuComponent.BUTTON2, 72, 6);
        button3 = new MenuComponent(spriteManager, MenuComponent.SIMPLE, "OPCIONES", 7, MenuComponent.BUTTON2, 72, 6); // Nuevo botón

        // Crear JPanels transparentes para los botones
        JPanel button1Panel = createButtonPanel(button1, 250); // Posición vertical del primer botón
        JPanel button2Panel = createButtonPanel(button2, 450); // Posición vertical del segundo botón
        JPanel button3Panel = createButtonPanel(button3, 350); // Posición vertical del nuevo botón (en medio)

        // Añadir los paneles a la capa PALETTE_LAYER
        add(button1Panel, JLayeredPane.PALETTE_LAYER);
        add(button2Panel, JLayeredPane.PALETTE_LAYER);
        add(button3Panel, JLayeredPane.PALETTE_LAYER);

        // Añadir un MouseListener para detectar clics en los botones
        button1Panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                button1.setType(MenuComponent.PRETY); // Cambiar a PRETY BUTTON2
                button1Panel.repaint(); // Redibujar el panel del botón
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                button1.setType(MenuComponent.SIMPLE); // Restaurar a SIMPLE BUTTON2
                button1Panel.repaint(); // Redibujar el panel del botón
                cargarMultijugadorPanel(); // Cambiar al panel de multijugador
            }
        });

        button2Panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                button2.setType(MenuComponent.PRETY); // Cambiar a PRETY BUTTON2
                button2Panel.repaint(); // Redibujar el panel del botón
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                button2.setType(MenuComponent.SIMPLE); // Restaurar a SIMPLE BUTTON2
                button2Panel.repaint(); // Redibujar el panel del botón
                System.exit(0); // Cerrar la aplicación
            }
        });

        button3Panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                button3.setType(MenuComponent.PRETY); // Cambiar a PRETY BUTTON2
                button3Panel.repaint(); // Redibujar el panel del botón
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                button3.setType(MenuComponent.SIMPLE); // Restaurar a SIMPLE BUTTON2
                button3Panel.repaint(); // Redibujar el panel del botón
                cargarOptionPanel(); // Cambiar al panel de opciones
            }
        });
    }

    private JPanel createButtonPanel(MenuComponent button, int y) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                // No llamar a super.paintComponent(g) para evitar dibujar el fondo
                button.draw(g, 0, 0, 64); // Tamaño de cada tile (64x64 píxeles)
            }
        };
        panel.setOpaque(false); // Hacer el panel transparente
        int tileSize = 64;
        int width = button.getGridCols() * tileSize;
        int x = (768 - width) / 2; // Centrar horizontalmente
        panel.setBounds(x, y, width, tileSize); // Establecer posición y tamaño del panel
        return panel;
    }

    private void cargarMultijugadorPanel() {
        // Crear el panel de multijugador
        MultijugadorPanel multijugadorPanel = new MultijugadorPanel(spriteManager, parentFrame);

        // Cambiar el contenido de la ventana principal
        parentFrame.setContentPane(multijugadorPanel);
        parentFrame.revalidate(); // Actualizar la ventana
    }

    private void cargarOptionPanel() {
        // Crear el panel de opciones
        OptionPanel optionPanel = new OptionPanel(spriteManager, parentFrame);

        // Cambiar el contenido de la ventana principal
        parentFrame.setContentPane(optionPanel);
        parentFrame.revalidate(); // Actualizar la ventana
    }
}