package com.mycompany.ajedrez.panels;

import com.mycompany.ajedrez.managers.SpriteManager;
import com.mycompany.ajedrez.menuComponents.Background;
import com.mycompany.ajedrez.menuComponents.MenuComponent;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

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
        inputIp = crearInput("IP DEL SERVIDOR");
        inputPuerto = crearInput("PUERTO DEL SERVIDOR");

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

    // Método para cargar los datos guardados desde el archivo
    private void cargarDatosGuardados() {
        try {
            Path path = Paths.get(SERVER_DATA_PATH);
            if (Files.exists(path)) { // Verificar si el archivo existe
                BufferedReader reader = Files.newBufferedReader(path);
                String ip = reader.readLine(); // Leer la primera línea (IP)
                String puerto = reader.readLine(); // Leer la segunda línea (Puerto)
                reader.close();

                if (ip != null && puerto != null) {
                    inputIp.setText(ip); // Establecer la IP en el campo de texto
                    inputPuerto.setText(puerto); // Establecer el puerto en el campo de texto
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo de configuración: " + e.getMessage());
        }
    }

    // Método para guardar los datos en el archivo
    private void guardarDatos(String ip, String puerto) {
        Path path = Paths.get(SERVER_DATA_PATH);
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            writer.write(ip + "\n"); // Escribir la IP
            writer.write(puerto + "\n"); // Escribir el puerto
        } catch (IOException e) {
            System.err.println("Error al guardar el archivo de configuración: " + e.getMessage());
        }
    }

    private void aceptarConfiguracion() {
        // Obtener los valores de los inputs
        String ip = inputIp.getText().trim();
        String puerto = inputPuerto.getText().trim();

        // Validar que los campos no estén vacíos
        if (ip.isEmpty() || puerto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validar el formato de la IP
        if (!validarIP(ip)) {
            JOptionPane.showMessageDialog(this, "La IP no tiene un formato válido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validar el puerto
        if (!validarPuerto(puerto)) {
            JOptionPane.showMessageDialog(this, "El puerto debe ser un número entre 0 y 65535.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Guardar los datos en el archivo
        guardarDatos(ip, puerto);

        // Si los inputs son válidos, procesar la configuración
        System.out.println("IP del servidor: " + ip);
        System.out.println("Puerto del servidor: " + puerto);
        volverAMenuPrincipal(); // Volver al menú principal
        // Aquí puedes agregar la lógica para guardar la configuración o cambiar de panel
    }
    private JTextField crearInput(String placeholder) {
        JTextField input = new JTextField(placeholder);
        input.setFont(new Font("minimalPixel", Font.PLAIN, 48)); // Fuente más grande
        input.setForeground(Color.WHITE); // Color del texto
        input.setBackground(new Color(0, 0, 0, 0)); // Fondo transparente
        input.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Espaciado interno
        input.setOpaque(false); // Hacer el JTextField transparente

        // Limitar el número máximo de caracteres a 17
        input.setDocument(new PlainDocument() {
            @Override
            public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
                if ((getLength() + str.length()) <= 17) { // Permitir solo 17 caracteres
                    super.insertString(offs, str, a);
                }
            }
        });

        return input;
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