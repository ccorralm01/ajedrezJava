package com.mycompany.ajedrez;

import com.mycompany.ajedrez.gameComponents.Board;
import com.mycompany.ajedrez.managers.AnimationManager;
import com.mycompany.ajedrez.managers.SpriteManager;
import com.mycompany.ajedrez.panels.BoardPanel;
import com.mycompany.ajedrez.panels.HudPanel;
import com.mycompany.ajedrez.panels.SesionPanel;
import com.mycompany.ajedrez.server.Room;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GUI extends JFrame {
    public GUI() {
        // Crear el diálogo de sesión
        JDialog dialogo = new JDialog(this, "Pantalla Previa", true);
        SesionPanel sesionPanel = new SesionPanel(dialogo); // Pasar el JDialog al SesionPanel
        dialogo.setResizable(false);
        dialogo.add(sesionPanel);
        dialogo.pack();
        dialogo.setLocationRelativeTo(this);
        dialogo.setVisible(true); // Mostrar el diálogo (el código se detiene aquí hasta que se cierre)

        // Obtener los datos después de cerrar el diálogo
        String[] datos = sesionPanel.obtenerDatos();

        // Validar si se ingresaron datos
        if (datos == null || datos[0].isEmpty() || datos[1].isEmpty()) {
            // Si no se ingresaron datos, mostrar un mensaje y cerrar la aplicación
            JOptionPane.showMessageDialog(this, "Debes ingresar un nombre de usuario y un nombre de sala.", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0); // Cerrar la aplicación
        }

        // Si los datos están presentes, continuar con la carga del juego
        String nombreUsuario = datos[0];
        String nombreSala = datos[1];
        String contrasena = datos[2] == null ? "" : datos[2];

        Room room = new Room(nombreSala, contrasena, nombreUsuario, new ArrayList<>(), false);
        new Cliente().sendRoomToServer(room);

        System.out.println("Nombre de usuario: " + nombreUsuario);
        System.out.println("Nombre de la sala: " + nombreSala);

        // Iniciar el juego con los datos ingresados
        SpriteManager spriteManager = new SpriteManager("src/res/Board.png", "src/res/pieces.png");
        Board board = new Board(spriteManager);

        // Crear paneles
        BoardPanel boardPanel = new BoardPanel(board, spriteManager);
        HudPanel hudPanel = new HudPanel(spriteManager);

        // Configurar layout
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(768, 832)); // Tamaño del tablero

        // Añadir paneles al JLayeredPane
        boardPanel.setBounds(0, 0, 768, 832); // Establecer posición y tamaño del BoardPanel
        hudPanel.setBounds(0, 0, 768, 832);   // Establecer posición y tamaño del HudPanel

        layeredPane.add(boardPanel, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(hudPanel, JLayeredPane.PALETTE_LAYER);

        // Añadir el JLayeredPane al JFrame
        add(layeredPane);

        // Inicializar controlador y animación
        GameController gameController = new GameController(board, boardPanel, hudPanel, spriteManager);
        AnimationManager animationManager = new AnimationManager(hudPanel);
        gameController.setRoom(room);

        // Configurar ventana
        setTitle("Ajedrez - Usuario: " + nombreUsuario + " - Sala: " + nombreSala);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack(); // Ajustar el tamaño de la ventana al contenido
        setLocationRelativeTo(null); // Centrar la ventana en la pantalla
        setVisible(true);

        gameController.setMyTurn(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new GUI();
        });
    }
}