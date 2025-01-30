package com.mycompany.ajedrez.panels;

import com.mycompany.ajedrez.managers.SpriteManager;
import com.mycompany.ajedrez.menuComponents.Background;
import com.mycompany.ajedrez.menuComponents.MenuComponent;
import com.mycompany.ajedrez.menuComponents.UIUtils;
import com.mycompany.ajedrez.server.Client;
import com.mycompany.ajedrez.server.Room;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class MultijugadorPanel extends JPanel {
    private Background background;
    private MenuComponent input1;
    private MenuComponent input2;
    private MenuComponent input3;
    private JTextField inputUsuario;
    private JTextField inputServidor;
    private JTextField inputClave;
    private MenuComponent botonEntrar;
    private MenuComponent botonSalir;
    private SpriteManager spriteManager;
    private JFrame parentFrame;
    private Room newRoom;
    private Client cliente;

    public MultijugadorPanel(SpriteManager spriteManager, JFrame parentFrame) {
        this.spriteManager = spriteManager;
        this.parentFrame = parentFrame; // Guardar la referencia a la ventana principal
        setPreferredSize(new Dimension(768, 832)); // Tamaño del panel
        setLayout(null); // Usar layout nulo para superponer componentes manualmente

        // Crear un fondo de tipo SIMPLE con un grid de 12x12
        background = new Background(spriteManager, Background.SIMPLE, 13, 12);

        // Crear los títulos (inputs visuales)
        input1 = new MenuComponent(spriteManager, MenuComponent.SIMPLE, "", 7, MenuComponent.TITLE, 72, 6); // Sin texto
        input2 = new MenuComponent(spriteManager, MenuComponent.SIMPLE, "", 7, MenuComponent.TITLE, 72, 6); // Sin texto
        input3 = new MenuComponent(spriteManager, MenuComponent.SIMPLE, "", 7, MenuComponent.TITLE, 72, 6); // Sin texto

        // Crear los JTextField para la entrada de texto
        inputUsuario = UIUtils.crearInput("NOMBRE DE USUARIO", 17);
        inputServidor = UIUtils.crearInput("NOMBRE DEL SERVIDOR", 17);
        inputClave = UIUtils.crearInput("CLAVE DEL SERVIDOR", 17);

        // Crear los botones (ENTRAR y SALIR)
        botonEntrar = new MenuComponent(spriteManager, MenuComponent.SIMPLE, "ENTRAR", 7, MenuComponent.BUTTON2, 72, 6);
        botonSalir = new MenuComponent(spriteManager, MenuComponent.SIMPLE, "SALIR", 7, MenuComponent.BUTTON2, 72, 6);

        // Añadir los JTextField al panel
        add(inputUsuario);
        add(inputServidor);
        add(inputClave);

        // Añadir un MouseListener para detectar clics en los botones
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                handleButtonPress(e.getX(), e.getY());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                handleButtonRelease();
            }
        });
    }

    private void handleButtonPress(int mouseX, int mouseY) {
        // Tamaño de cada tile (64x64 píxeles)
        int tileSize = 64;

        // Calcular la posición de los botones
        int buttonWidth = input1.getGridCols() * tileSize; // Ancho total de los botones
        int buttonX = (getWidth() - buttonWidth) / 2; // Centrar horizontalmente

        // Posiciones verticales de los botones
        int botonEntrarY = 500; // Posición vertical del botón ENTRAR
        int botonSalirY = 600; // Posición vertical del botón SALIR

        // Verificar si se hizo clic en el botón ENTRAR
        if (mouseX >= buttonX && mouseX <= buttonX + buttonWidth &&
                mouseY >= botonEntrarY && mouseY <= botonEntrarY + tileSize) {
            botonEntrar.setType(MenuComponent.PRETY); // Cambiar a PRETY BUTTON2
            iniciarJuego();

        }

        // Verificar si se hizo clic en el botón SALIR
        if (mouseX >= buttonX && mouseX <= buttonX + buttonWidth &&
                mouseY >= botonSalirY && mouseY <= botonSalirY + tileSize) {
            botonSalir.setType(MenuComponent.PRETY); // Cambiar a PRETY BUTTON2
            volverAMenuPrincipal(); // Volver al menú principal
        }

        // Redibujar el panel para reflejar los cambios
        repaint();
    }

    private void iniciarJuego() {
        // Validar los inputs
        String usuario = inputUsuario.getText();
        String servidor = inputServidor.getText();
        String clave = inputClave.getText();

        if (usuario.isEmpty() || servidor.isEmpty() || clave.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Leer configuración desde el archivo
        Properties config = leerConfiguracion("src/res/serverdata.txt");
        if (config == null) {
            JOptionPane.showMessageDialog(this, "Error al cargar la configuración del servidor.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String ip = config.getProperty("ip", "127.0.0.1");
        int puerto;
        try {
            puerto = Integer.parseInt(config.getProperty("puerto", "6666"));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El puerto en el archivo de configuración no es válido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Inicializar la sala y el cliente con la IP y el puerto leídos
        Map<String, String> jugadores = new HashMap<>();
        jugadores.put(usuario, ""); // El color se asignará más tarde
        newRoom = new Room(servidor, clave, jugadores);
        cliente = new Client(ip, puerto);
        cliente.setMjPanel(this);

        // Iniciar cliente en un hilo separado
        new Thread(() -> cliente.iniciar()).start();
    }

    private Properties leerConfiguracion(String filePath) {
        Properties props = new Properties();
        Path path = Paths.get(filePath);

        // Verificar si el archivo existe
        if (!Files.exists(path)) {
            return null;
        }

        // Leer el archivo de configuración
        try (InputStream input = Files.newInputStream(path)) {
            props.load(input);
            return props;
        } catch (IOException e) {
            System.err.println("Error al leer el archivo de configuración: " + e.getMessage());
            return null;
        }
    }


    public void onRoomSetUp(Room room) {
        String versus;
        parentFrame.dispose(); // Cerrar la ventana actual

        ArrayList<String> players = new ArrayList<>(room.getPlayers().keySet()); // Obtener las claves (usuarios) en una lista ordenada

        if (players.isEmpty()) {
            System.err.println("Error: La sala no tiene jugadores.");
            return;
        }

        String currentUser = inputUsuario.getText();
        String lastPlayer = players.getLast();
        String firstPlayer = players.getFirst();

        if (lastPlayer.equals(currentUser)) {
            versus = firstPlayer;
        } else {
            versus = lastPlayer;
        }
        GamePanel gamePanel = new GamePanel(currentUser, versus, inputServidor.getText(), inputClave.getText(), room); // Iniciar el juego
        gamePanel.getGameController().setClient(cliente);
        cliente.setGameController(gamePanel.getGameController());
        /*
        SwingUtilities.invokeLater(() -> {
            GamePanel gamePanel = new GamePanel(currentUser, versus, inputServidor.getText(), inputClave.getText(), room);
            gamePanel.getGameController().setClient(cliente);
            cliente.setGameController(gamePanel.getGameController());
        });

         */

    }


    private void volverAMenuPrincipal() {
        // Cambiar al panel del menú principal
        MenuPanel menuPanel = new MenuPanel(spriteManager, parentFrame);
        parentFrame.setContentPane(menuPanel);
        parentFrame.revalidate(); // Actualizar la ventana
    }

    private void handleButtonRelease() {
        // Restaurar todos los botones a SIMPLE BUTTON2
        botonEntrar.setType(MenuComponent.SIMPLE);
        botonSalir.setType(MenuComponent.SIMPLE);

        // Redibujar el panel para reflejar los cambios
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Obtener el tamaño del panel
        int panelWidth = getWidth();
        int panelHeight = getHeight();

        // Tamaño de cada tile (64x64 píxeles)
        int tileSize = 64;

        // Calcular la posición del fondo para que esté centrado horizontalmente
        int backgroundWidth = background.getGridCols() * tileSize; // Ancho total del fondo
        int backgroundX = (panelWidth - backgroundWidth) / 2; // Centrar horizontalmente
        int backgroundY = 0; // El fondo comienza en la parte superior

        // Dibujar el fondo centrado horizontalmente
        background.draw(g, backgroundX, backgroundY, tileSize);

        // Calcular la posición de los inputs y botones
        int buttonWidth = input1.getGridCols() * tileSize; // Ancho total de los inputs/botones
        int buttonX = (panelWidth - buttonWidth) / 2; // Centrar horizontalmente

        // Desplazamiento adicional para los JTextField
        int desplazamientoDerecha = 20; // Desplazamiento a la derecha

        // Posiciones verticales de los inputs y botones
        int input1Y = 200; // Posición vertical del primer input
        int input2Y = 300; // Posición vertical del segundo input
        int input3Y = 400; // Posición vertical del tercer input
        int botonEntrarY = 500; // Posición vertical del botón ENTRAR
        int botonSalirY = 600; // Posición vertical del botón SALIR

        // Dibujar los textos encima de los inputs
        g.setColor(Color.WHITE); // Color del texto
        g.setFont(new Font("minimalPixel", Font.BOLD, 48));

        // Texto "USUARIO" encima del primer input
        g.drawString("NOMBRE DE USUARIO", buttonX, input1Y - 10);

        // Texto "SERVIDOR" encima del segundo input
        g.drawString("NOMBRE DEL SERVIDOR", buttonX, input2Y - 10);

        // Texto "CONTRASEÑA" encima del tercer input
        g.drawString("CLAVE DEL SERVIDOR", buttonX, input3Y - 10);

        // Dibujar los inputs (Title)
        input1.draw(g, buttonX, input1Y, tileSize);
        input2.draw(g, buttonX, input2Y, tileSize);
        input3.draw(g, buttonX, input3Y, tileSize);

        // Dibujar los botones ENTRAR y SALIR
        botonEntrar.draw(g, buttonX, botonEntrarY, tileSize);
        botonSalir.draw(g, buttonX, botonSalirY, tileSize);

        // Posicionar los JTextField sobre los inputs con un desplazamiento a la derecha
        inputUsuario.setBounds(buttonX + desplazamientoDerecha, input1Y, buttonWidth - desplazamientoDerecha, tileSize);
        inputServidor.setBounds(buttonX + desplazamientoDerecha, input2Y, buttonWidth - desplazamientoDerecha, tileSize);
        inputClave.setBounds(buttonX + desplazamientoDerecha, input3Y, buttonWidth - desplazamientoDerecha, tileSize);
    }

    public Room getNewRoom() {
        return newRoom;
    }

    public Client getCliente() {
        return cliente;
    }

    public JTextField getInputUsuario() {
        return inputUsuario;
    }

    public void setCliente(Client cliente) {
        this.cliente = cliente;
    }
}