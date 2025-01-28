package com.mycompany.ajedrez.server;

import java.io.*;
import java.net.Socket;

public class JuegoHilo implements Runnable {
    private Socket socket1;
    private Socket socket2;

    public JuegoHilo(Socket socket1, Socket socket2) {
        this.socket1 = socket1;
        this.socket2 = socket2;
    }

    @Override
    public void run() {
        try (
                ObjectInputStream entrada1 = new ObjectInputStream(socket1.getInputStream());
                ObjectOutputStream salida1 = new ObjectOutputStream(socket2.getOutputStream());
                ObjectInputStream entrada2 = new ObjectInputStream(socket2.getInputStream());
                ObjectOutputStream salida2 = new ObjectOutputStream(socket1.getOutputStream());
        ) {
            // Lógica del juego
            while (true) {
                // Recibir movimientos o acciones de los jugadores
                Movement movimientoJugador1 = (Movement) entrada1.readObject();
                Movement movimientoJugador2 = (Movement) entrada2.readObject();

                if(movimientoJugador1 != null) {
                    salida1.writeObject(movimientoJugador1);
                } else if(movimientoJugador2 != null) {
                    salida2.writeObject(movimientoJugador2);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error en el hilo del juego: " + e.getMessage());
        }
    }
}
