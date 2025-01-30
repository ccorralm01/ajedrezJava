package com.mycompany.ajedrez.server;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Server implements Runnable {
    private Map<String, Room> salasPendientes;
    private Map<String, Socket> jugadoresEnSala;
    private int puerto;
    private ServerSocket servidorSocket;

    private ObjectInputStream primeraEntrada;
    ArrayList<ClienteConectado> clientesConectados;
    ClienteConectado primerJugadorConectado;
    ClienteConectado segundoJugadorConectado;

    ObjectInputStream entradaJugadorPrimerTurno;
    ObjectInputStream entradaJugadorSegundoTurno;
    ObjectOutputStream salidaJugadorPrimerTurno;
    ObjectOutputStream salidaJugadorSegundoTurno;

    public Server(int puerto) {
        this.puerto = puerto;
        this.salasPendientes = new HashMap<>();
        this.jugadoresEnSala = new HashMap<>();
        this.clientesConectados = new ArrayList<>();

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

                clientesConectados.add(new ClienteConectado(socket, entrada, null, null));

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

                        salaPendiente.asignarPrimeraJugada();
                        String playerStart = salaPendiente.getPlayerStart();

                        System.out.println("Jugadores en sala: " + jugadoresEnSala);

                        ObjectOutputStream salida1 = new ObjectOutputStream(salaPendiente.getSocket().getOutputStream());
                        ObjectOutputStream salida2 = new ObjectOutputStream(socket.getOutputStream());
                        clientesConectados.getFirst().setSalida(salida1);
                        clientesConectados.getLast().setSalida(salida2);
                        clientesConectados.getFirst().setRoom(salaPendiente);
                        clientesConectados.getLast().setRoom(salaPendiente);

                        primerJugadorConectado = clientesConectados.getFirst();
                        segundoJugadorConectado = clientesConectados.getLast();

                        System.out.println("Clientes conectados: " + clientesConectados);
                        // Enviar la sala actualizada a ambos jugadores
                        primerJugadorConectado.getSalida().writeObject(salaPendiente);
                        segundoJugadorConectado.getSalida().writeObject(salaPendiente);
                        System.out.println("Sala creada: " + salaPendiente);
                        // Eliminar la sala pendiente
                        salasPendientes.remove(room.getRoomName());

                        // Determinar que jugador es el que empieza, el 1ro que se conectó o el 2do
                        Socket socketDelJugadorQueEmpieza = null;
                        for (Map.Entry<String, Socket> entry : jugadoresEnSala.entrySet()) {
                            if (entry.getKey().equals(playerStart)) {
                                socketDelJugadorQueEmpieza = entry.getValue();
                                break;
                            }
                        }
                        if(primerJugadorConectado.getSocket() == socketDelJugadorQueEmpieza) {
                            System.out.println("Empieza el que se conectó primero");

                            // El primer turno lo tiene el primer jugador conectado
                            entradaJugadorPrimerTurno = primerJugadorConectado.getEntrada();
                            salidaJugadorPrimerTurno = primerJugadorConectado.getSalida();
                            entradaJugadorSegundoTurno = segundoJugadorConectado.getEntrada();
                            salidaJugadorSegundoTurno = segundoJugadorConectado.getSalida();

                        } else {
                            System.out.println("Empieza el que se conectó después");

                            // El primer turno lo tiene el segundo jugador conectado
                            entradaJugadorPrimerTurno = segundoJugadorConectado.getEntrada();
                            salidaJugadorPrimerTurno = segundoJugadorConectado.getSalida();
                            entradaJugadorSegundoTurno = primerJugadorConectado.getEntrada();
                            salidaJugadorSegundoTurno = primerJugadorConectado.getSalida();
                        }


                        // Primer movimiento: el jugador con el turno inicial hace el primer movimiento
                        System.out.println("Esperando primer movimiento...");
                        Movement primerMovimiento = (Movement) entradaJugadorPrimerTurno.readObject();
                        System.out.println("Primer movimiento recibido: " + primerMovimiento);

                        // Enviar el primer movimiento al segundo jugador
                        salidaJugadorSegundoTurno.writeObject(primerMovimiento);
                        System.out.println("Primer movimiento enviado al segundo jugador: " + primerMovimiento);

                        /*
                        // Alternar los turnos
                        while (true) {
                            // Esperar movimiento del segundo jugador
                            Movement movimientoSegundoJugador = (Movement) entradaSegundoJugador.readObject();
                            System.out.println("Movimiento del segundo jugador recibido: " + movimientoSegundoJugador);

                            // Enviar movimiento al primer jugador
                            salidaPrimerJugador.writeObject(movimientoSegundoJugador);
                            System.out.println("Movimiento enviado al primer jugador: " + movimientoSegundoJugador);

                            // Esperar movimiento del primer jugador
                            Movement movimientoPrimerJugador = (Movement) entradaPrimerJugador.readObject();
                            System.out.println("Movimiento del primer jugador recibido: " + movimientoPrimerJugador);

                            // Enviar movimiento al segundo jugador
                            salidaSegundoJugador.writeObject(movimientoPrimerJugador);
                            System.out.println("Movimiento enviado al segundo jugador: " + movimientoPrimerJugador);
                        }

                         */


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
                    primeraEntrada = entrada; // Guardar la entrada del primer jugador
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