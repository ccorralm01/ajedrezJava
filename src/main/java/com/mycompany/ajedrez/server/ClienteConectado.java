package com.mycompany.ajedrez.server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClienteConectado {
    private Socket socket;
    private ObjectInputStream entrada;
    private ObjectOutputStream salida;
    private Room room;

    public ClienteConectado(Socket socket, ObjectInputStream entrada, ObjectOutputStream salida, Room room) {
        this.socket = socket;
        this.entrada = entrada;
        this.salida = salida;
        this.room = room;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socketPrimerJugador) {
        this.socket = socketPrimerJugador;
    }

    public ObjectInputStream getEntrada() {
        return entrada;
    }

    public void setEntrada(ObjectInputStream entrada) {
        this.entrada = entrada;
    }

    public ObjectOutputStream getSalida() {
        return salida;
    }

    public void setSalida(ObjectOutputStream salida) {
        this.salida = salida;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    @Override
    public String toString() {
        return String.format(
                "ClienteConectado {\n" +
                        "  Socket: %s\n" +
                        "  Entrada: %s\n" +
                        "  Salida: %s\n" +
                        "  Room: %s\n" +
                        "}",
                socket, entrada, salida, room
        );
    }

}
