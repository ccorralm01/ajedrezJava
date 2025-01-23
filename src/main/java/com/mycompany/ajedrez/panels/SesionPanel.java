package com.mycompany.ajedrez.panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class SesionPanel extends JPanel {
    private JTextField campoUsuario;
    private JTextField campoSala;
    private JPasswordField campoContrasena; // Campo para la contraseña
    private boolean datosConfirmados = false;
    private BufferedImage imagenFondo; // Imagen de fondo
    private JDialog dialogo; // Referencia al JDialog que contiene este panel

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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Dibujar el panel correctamente

        // Dibujar la imagen de fondo
        if (imagenFondo != null) {
            g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
        }
    }

    /**
     * Devuelve los datos ingresados.
     *
     * @return Un array con el nombre de usuario, el nombre de la sala y la contraseña, o null si no se confirmaron los datos.
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