package com.mycompany.ajedrez.panels;

import com.mycompany.ajedrez.managers.SpriteManager;
import com.mycompany.ajedrez.menuComponents.Background;
import com.mycompany.ajedrez.menuComponents.MenuComponent;
import com.mycompany.ajedrez.menuComponents.UIUtils;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Properties;

public class OptionPanel extends JPanel {
    private Background background;
    private MenuComponent input1;
    private MenuComponent input2;
    private JTextField inputIp;
    private JTextField inputPuerto;
    private MenuComponent botonAceptar;
    private MenuComponent botonSalir;
    private SpriteManager spriteManager;
    private JFrame parentFrame; // Referencia a la ventana principal

    private static final String SERVER_DATA_PATH = "src/res/serverdata.txt";

    public OptionPanel(SpriteManager spriteManager, JFrame parentFrame) {
        this.spriteManager = spriteManager;
        this.parentFrame = parentFrame;
        setPreferredSize(new Dimension(768, 832));
        setLayout(null);

        // Crear un fondo de tipo SIMPLE con un grid de 12x12
        background = new Background(spriteManager, Background.SIMPLE, 13, 12);

        // Crear los títulos (inputs visuales)
        input1 = new MenuComponent(spriteManager, MenuComponent.SIMPLE, "", 7, MenuComponent.TITLE, 72, 6);
        input2 = new MenuComponent(spriteManager, MenuComponent.SIMPLE, "", 7, MenuComponent.TITLE, 72, 6);

        // Crear los JTextField para la entrada de texto
        inputIp = UIUtils.crearInput("IP DEL SERVIDOR", 17);
        inputPuerto = UIUtils.crearInput("PUERTO DEL SERVIDOR", 17);

        // Cargar los datos guardados (si existen)
        cargarDatosGuardados();

        // Crear los botones (ACEPTAR y SALIR)
        botonAceptar = new MenuComponent(spriteManager, MenuComponent.SIMPLE, "ACEPTAR", 7, MenuComponent.BUTTON2, 72, 6);
        botonSalir = new MenuComponent(spriteManager, MenuComponent.SIMPLE, "SALIR", 7, MenuComponent.BUTTON2, 72, 6);

        // Añadir los JTextField al panel
        add(inputIp);
        add(inputPuerto);

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

    private void cargarDatosGuardados() {
        Properties props = new Properties();
        Path path = Paths.get(SERVER_DATA_PATH);

        // Si el archivo no existe, crearlo con valores por defecto
        if (!Files.exists(path)) {
            props.setProperty("ip", "127.0.0.1"); // IP por defecto
            props.setProperty("puerto", "5000");  // Puerto por defecto

            try (OutputStream output = Files.newOutputStream(path)) {
                props.store(output, "Server Configuration"); // Crear el archivo con valores por defecto
            } catch (IOException e) {
                System.err.println("Error al crear el archivo de configuración: " + e.getMessage());
            }
        }

        // Cargar los datos desde el archivo (ya sea el recién creado o uno existente)
        try (InputStream input = Files.newInputStream(path)) {
            props.load(input); // Cargar las propiedades desde el archivo

            // Obtener los valores de las propiedades
            String ip = props.getProperty("ip", "127.0.0.1"); // Valor por defecto si no existe
            String puerto = props.getProperty("puerto", "5000"); // Valor por defecto si no existe

            // Establecer los valores en los campos de texto
            inputIp.setText(ip);
            inputPuerto.setText(puerto);
        } catch (IOException e) {
            System.err.println("Error al leer el archivo de configuración: " + e.getMessage());
        }
    }

    private void guardarDatos(String ip, String puerto) {
        Properties props = new Properties();
        props.setProperty("ip", ip);
        props.setProperty("puerto", puerto);

        Path path = Paths.get(SERVER_DATA_PATH);
        try (OutputStream output = Files.newOutputStream(path)) {
            props.store(output, "Server Configuration");
        } catch (IOException e) {
            System.err.println("Error al guardar el archivo de configuración: " + e.getMessage());
        }
    }

    private void aceptarConfiguracion() {
        String ip = inputIp.getText().trim();
        String puerto = inputPuerto.getText().trim();

        if (ip.isEmpty() || puerto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!validarIP(ip)) {
            JOptionPane.showMessageDialog(this, "La IP no tiene un formato válido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!validarPuerto(puerto)) {
            JOptionPane.showMessageDialog(this, "El puerto debe ser un número entre 0 y 65535.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        guardarDatos(ip, puerto);
        volverAMenuPrincipal();
    }

    private void handleButtonPress(int mouseX, int mouseY) {
        // Tamaño de cada tile (64x64 píxeles)
        int tileSize = 64;

        // Calcular la posición de los botones
        int buttonWidth = input1.getGridCols() * tileSize; // Ancho total de los botones
        int buttonX = (getWidth() - buttonWidth) / 2; // Centrar horizontalmente

        // Posiciones verticales de los botones
        int botonAceptarY = 400; // Posición vertical del botón ACEPTAR
        int botonSalirY = 500; // Posición vertical del botón SALIR

        // Verificar si se hizo clic en el botón ACEPTAR
        if (mouseX >= buttonX && mouseX <= buttonX + buttonWidth &&
                mouseY >= botonAceptarY && mouseY <= botonAceptarY + tileSize) {
            botonAceptar.setType(MenuComponent.PRETY); // Cambiar a PRETY BUTTON2
            aceptarConfiguracion(); // Procesar la configuración
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


    private boolean validarIP(String ip) {
        // Expresión regular para validar una dirección IPv4
        String regex = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
        return ip.matches(regex);
    }

    private boolean validarPuerto(String puerto) {
        try {
            int puertoNum = Integer.parseInt(puerto);
            return puertoNum >= 0 && puertoNum <= 65535; // El puerto debe estar en el rango 0-65535
        } catch (NumberFormatException e) {
            return false; // Si no es un número, es inválido
        }
    }

    private void volverAMenuPrincipal() {
        // Cambiar al panel del menú principal
        MenuPanel menuPanel = new MenuPanel(spriteManager, parentFrame);
        parentFrame.setContentPane(menuPanel);
        parentFrame.revalidate(); // Actualizar la ventana
    }

    private void handleButtonRelease() {
        // Restaurar todos los botones a SIMPLE BUTTON2
        botonAceptar.setType(MenuComponent.SIMPLE);
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
        int botonAceptarY = 400; // Posición vertical del botón ACEPTAR
        int botonSalirY = 500; // Posición vertical del botón SALIR

        // Dibujar los textos encima de los inputs
        g.setColor(Color.WHITE); // Color del texto
        g.setFont(new Font("minimalPixel", Font.BOLD, 48));

        // Texto "IP DEL SERVIDOR" encima del primer input
        g.drawString("IP DEL SERVIDOR", buttonX, input1Y - 10);

        // Texto "PUERTO DEL SERVIDOR" encima del segundo input
        g.drawString("PUERTO DEL SERVIDOR", buttonX, input2Y - 10);

        // Dibujar los inputs (Title)
        input1.draw(g, buttonX, input1Y, tileSize);
        input2.draw(g, buttonX, input2Y, tileSize);

        // Dibujar los botones ACEPTAR y SALIR
        botonAceptar.draw(g, buttonX, botonAceptarY, tileSize);
        botonSalir.draw(g, buttonX, botonSalirY, tileSize);

        // Posicionar los JTextField sobre los inputs con un desplazamiento a la derecha
        inputIp.setBounds(buttonX + desplazamientoDerecha, input1Y, buttonWidth - desplazamientoDerecha, tileSize);
        inputPuerto.setBounds(buttonX + desplazamientoDerecha, input2Y, buttonWidth - desplazamientoDerecha, tileSize);
    }
}