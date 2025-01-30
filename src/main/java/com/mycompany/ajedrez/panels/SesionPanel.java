package com.mycompany.ajedrez.panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Clase que representa un panel de sesión para ingresar el nombre de usuario,
 * el nombre de la sala y la contraseña de la sala. Este panel incluye una imagen
 * de fondo y campos de texto para la entrada de datos.
 */
public class SesionPanel extends JPanel {
    /** Campo de texto para el nombre de usuario. */
    private JTextField campoUsuario;

    /** Campo de texto para el nombre de la sala. */
    private JTextField campoSala;

    /** Campo de texto para la contraseña de la sala. */
    private JPasswordField campoContrasena;

    /** Indica si los datos han sido confirmados por el usuario. */
    private boolean datosConfirmados = false;

    /** Imagen de fondo del panel. */
    private BufferedImage imagenFondo;

    /** Referencia al JDialog que contiene este panel. */
    private JDialog dialogo;

    /**
     * Constructor de la clase SesionPanel.
     *
     * @param dialogo El JDialog que contiene este panel.
     */
    public SesionPanel(JDialog dialogo) {
        this.dialogo = dialogo;
        setPreferredSize(new Dimension(768, 832));

        // Cargar la imagen de fondo
        try {
            imagenFondo = ImageIO.read(new File("src/res/fondo_sesion.jpg")); // Cambia la ruta según tu archivo
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("No se pudo cargar la imagen de fondo.");
        }

        // Configurar el layout
        setLayout(new GridBagLayout()); // Usar GridBagLayout para centrar los componentes
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Espaciado entre componentes

        // Campo para el nombre de usuario
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Nombre de usuario:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        campoUsuario = new JTextField(15);
        add(campoUsuario, gbc);

        // Campo para el nombre de la sala
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Nombre de la sala:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        campoSala = new JTextField(15);
        add(campoSala, gbc);

        // Campo para la contraseña de la sala
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Contraseña de la sala:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        campoContrasena = new JPasswordField(15); // Campo de contraseña
        add(campoContrasena, gbc);

        // Botón para confirmar
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2; // Ocupar dos columnas
        gbc.anchor = GridBagConstraints.CENTER; // Centrar el botón
        JButton botonAceptar = new JButton("Aceptar");
        botonAceptar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                datosConfirmados = true;
                dialogo.setVisible(false);
            }
        });
        add(botonAceptar, gbc);

        // Ajustar el tamaño del panel al tamaño de la imagen de fondo
        if (imagenFondo != null) {
            setPreferredSize(new Dimension(imagenFondo.getWidth(), imagenFondo.getHeight()));
        }
    }

    /**
     * Dibuja la imagen de fondo en el panel.
     *
     * @param g El contexto gráfico en el que se dibuja el panel.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Dibujar el panel correctamente

        // Dibujar la imagen de fondo
        if (imagenFondo != null) {
            g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
        }
    }

    /**
     * Devuelve los datos ingresados por el usuario.
     *
     * @return Un array con el nombre de usuario, el nombre de la sala y la contraseña,
     *         o null si los datos no han sido confirmados.
     */
    public String[] obtenerDatos() {
        if (datosConfirmados) {
            // Convertir la contraseña de char[] a String
            String contrasena = new String(campoContrasena.getPassword());
            return new String[]{campoUsuario.getText(), campoSala.getText(), contrasena};
        }
        return null; // Si no se confirmaron los datos
    }
}