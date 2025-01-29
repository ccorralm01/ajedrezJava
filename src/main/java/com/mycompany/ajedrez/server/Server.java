package com.mycompany.ajedrez.server;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class Server implements Runnable {
    private Map<String, Room> salasPendientes;
    private Map<String, Socket> jugadoresEnSala;
    private int puerto;
    private ServerSocket servidorSocket;

    private Socket socketPrimerJugador;
    private Socket socketSegundoJugador;


    public Server(int puerto) {
        this.puerto = puerto;
        this.salasPendientes = new HashMap<>();
        this.jugadoresEnSala = new HashMap<>();
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
                        System.out.println(salaPendiente.getPlayers());

                        // Recupera el key diferente al que ya hay guardado
                        String nombreJugador2 = salaPendiente.getPlayers().keySet().stream()
                                .filter(k -> !k.equals(jugadoresEnSala.entrySet().iterator().next().getKey()))
                                .findFirst()
                                .orElse(null);

                        jugadoresEnSala.put(nombreJugador2, socket);
                        // Jugadores en sala: {cesar=Socket[addr=/127.0.0.1,port=62308,localport=6666], paco=Socket[addr=/127.0.0.1,port=62309,localport=6666]}

                        salaPendiente.asignarPrimeraJugada();

                        // Notificar a ambos clientes
                        ObjectOutputStream salida1 = new ObjectOutputStream(salaPendiente.getSocket().getOutputStream());
                        ObjectOutputStream salida2 = new ObjectOutputStream(socket.getOutputStream());

                        salida1.writeObject(salaPendiente);
                        salida2.writeObject(salaPendiente);
                        System.out.println("Sala creada: " + salaPendiente);

                        String playerStart = salaPendiente.getPlayerStart();

                        // Eliminar la sala pendiente
                        salasPendientes.remove(room.getRoomName());

                        // Iniciar hilo de juego
                        System.out.println("Jugadores en sala: " + jugadoresEnSala);

                        for (Map.Entry<String, Socket> entry : jugadoresEnSala.entrySet()) {
                            String jugador = entry.getKey();
                            Socket socketJugador = entry.getValue();
                            if (!jugador.equals(playerStart)) {
                                socketSegundoJugador = socketJugador;
                            } else {
                                socketPrimerJugador = socketJugador;
                            }
                        }

                        // Primer movimiento: el jugador con el turno inicial hace el primer movimiento
                        Movement primerMovimiento = (Movement) entrada.readObject();
                        System.out.println("Primer movimiento recibido: " + primerMovimiento);

                        // Enviar el primer movimiento al segundo jugador
                        ObjectOutputStream salidaSegundoJugador = new ObjectOutputStream(socketSegundoJugador.getOutputStream());
                        salidaSegundoJugador.writeObject(primerMovimiento);
                        System.out.println("Primer movimiento enviado al segundo jugador: " + primerMovimiento);

                        // Alternar los turnos
                        while (true) {
                            // Esperar movimiento del primer jugador (ya realizado, lo recibe del segundo)
                            Movement movimientoSegundoJugador = (Movement) entrada.readObject();
                            System.out.println("Movimiento del segundo jugador recibido: " + movimientoSegundoJugador);

                            // Enviar movimiento al primer jugador
                            ObjectOutputStream salidaPrimerJugador = new ObjectOutputStream(socketPrimerJugador.getOutputStream());
                            salidaPrimerJugador.writeObject(movimientoSegundoJugador);
                            System.out.println("Movimiento enviado al primer jugador: " + movimientoSegundoJugador);

                            // Esperar movimiento del primer jugador
                            Movement movimientoPrimerJugador = (Movement) entrada.readObject();
                            System.out.println("Movimiento del primer jugador recibido: " + movimientoPrimerJugador);

                            // Enviar movimiento al segundo jugador
                            ObjectOutputStream salidaSegundoJugadorTurno = new ObjectOutputStream(socketSegundoJugador.getOutputStream());
                            salidaSegundoJugadorTurno.writeObject(movimientoPrimerJugador);
                            System.out.println("Movimiento enviado al segundo jugador: " + movimientoPrimerJugador);
                        }

                        // new Thread(new JuegoHilo( jugadoresEnSala, playerStart )).start();
                    } else {
                        // Contraseña incorrecta
                        ObjectOutputStream salida = new ObjectOutputStream(socket.getOutputStream());
                        salida.writeObject(room);
                    }
                } else {
                    // No hay sala pendiente, guardar la nueva
                    room.setSocket(socket);
                    salasPendientes.put(room.getRoomName(), room);
                    String jugador = room.getPlayers().entrySet().iterator().next().getKey();
                    jugadoresEnSala.put(jugador, socket);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error en el servidor: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Server server = new Server(6666);
        server.run();
    }
}