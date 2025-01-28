package com.mycompany.ajedrez.server;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class Server implements Runnable {
    private Map<String, Room> salasPendientes;
    private int puerto;
    private ServerSocket servidorSocket;

    public Server(int puerto) {
        this.puerto = puerto;
        this.salasPendientes = new HashMap<>();
    }

    @Override
    public void run() {
        try {
            servidorSocket = new ServerSocket(puerto);
            System.out.println("Servidor escuchando en el puerto " + puerto);

            while (true) {
                Socket socket = servidorSocket.accept();
                System.out.println("Nuevo cliente conectado.");

                // Recepción de la Room
                ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream());
                Room room = (Room) entrada.readObject();

                System.out.println("Sala recibida: " + room.getRoomName());

                // Verificar si ya hay una sala pendiente con el mismo nombre y contraseña
                if (salasPendientes.containsKey(room.getRoomName())) {
                    Room salaPendiente = salasPendientes.get(room.getRoomName());
                    if (salaPendiente.getRoomPassword().equals(room.getRoomPassword())) {
                        // Emparejar jugadores
                        System.out.println("Emparejando jugadores en la sala: " + room.getRoomName());

                        salaPendiente.getPlayers().putAll(room.getPlayers());
                        salaPendiente.asignarPrimeraJugada();

                        // Notificar a ambos clientes
                        ObjectOutputStream salida1 = new ObjectOutputStream(salaPendiente.getSocket().getOutputStream());
                        ObjectOutputStream salida2 = new ObjectOutputStream(socket.getOutputStream());

                        salida1.writeObject(salaPendiente);
                        salida2.writeObject(salaPendiente);
                        System.out.println("Sala creada: " + salaPendiente);

                        // Eliminar la sala pendiente
                        salasPendientes.remove(room.getRoomName());

                        // Iniciar hilo de juego
                        new Thread(new JuegoHilo(salaPendiente.getSocket(), socket)).start();
                    } else {
                        // Contraseña incorrecta
                        ObjectOutputStream salida = new ObjectOutputStream(socket.getOutputStream());
                        salida.writeObject(room);
                    }
                } else {
                    // No hay sala pendiente, guardar la nueva
                    room.setSocket(socket);
                    salasPendientes.put(room.getRoomName(), room);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error en el servidor: " + e.getMessage());
        }
    }

    public void stop() {
        try {
            if (servidorSocket != null) {
                servidorSocket.close();
                System.out.println("Servidor detenido.");
            }
        } catch (IOException e) {
            System.err.println("Error al cerrar el servidor: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Server server = new Server(6666);
        server.run();
    }
}
