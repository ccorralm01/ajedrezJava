package com.mycompany.ajedrez.server;

import java.io.*;
import java.net.Socket;
import java.util.Map;

public class JuegoHilo implements Runnable {
    private final Socket socketJugador1;
    private ObjectInputStream entradaJugador1;
    private ObjectOutputStream salidaJugador1;

    public JuegoHilo(Map<String, Socket> jugadoresDeSala, String playerStart) {
        System.out.println("Jugadores en sala antes de asignar sockets: " + jugadoresDeSala);
        System.out.println("Jugador que empieza: " + playerStart);

        this.socketJugador1 = jugadoresDeSala.get(playerStart); // Obtener el socket del jugador inicial

        if (socketJugador1 == null) {
            System.err.println("El socket del jugador que empieza es nulo.");
            throw new IllegalArgumentException("No se encontró el socket para el jugador que empieza.");
        }
    }

    @Override
    public void run() {
        System.out.println("Hilo de juego iniciado");

        try {

             /*
            InputStream rawInput = socketJugador1.getInputStream();
            byte[] buffer = new byte[1024];
            int bytesRead = rawInput.read(buffer);
            System.out.println("Datos recibidos en bruto: " + new String(buffer, 0, bytesRead));
            */

            // Crear flujos de comunicación respetando el orden correcto
            salidaJugador1 = new ObjectOutputStream(socketJugador1.getOutputStream());
            salidaJugador1.flush();
            entradaJugador1 = new ObjectInputStream(socketJugador1.getInputStream());

            System.out.println("Flujos de comunicación creados para el jugador que empieza");

            // Recibir solo el primer movimiento del jugador que empieza
            Object movimiento1 = entradaJugador1.readObject();
            System.out.println("Primer movimiento recibido del jugador que empieza: " + movimiento1);

        } catch (EOFException e) {
            System.err.println("El jugador se ha desconectado inesperadamente.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error en el hilo del juego: " + e.getMessage());
            e.printStackTrace();
        } finally {
            cerrarRecursos();
        }
    }

    private void cerrarRecursos() {
        try {
            if (entradaJugador1 != null) entradaJugador1.close();
            if (salidaJugador1 != null) salidaJugador1.close();
            if (socketJugador1 != null && !socketJugador1.isClosed()) socketJugador1.close();
        } catch (IOException e) {
            System.err.println("Error al cerrar los recursos: " + e.getMessage());
        }
    }
}
