package com.mycompany.ajedrez;

import com.mycompany.ajedrez.server.Request;
import com.mycompany.ajedrez.server.Room;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Cliente {

    final String HOST = "127.0.0.1";
    final int PUERTO = 5000;

    public Cliente() {}

    public void sendRequestToServer(Request request) {
        try (Socket socket = new Socket(HOST, PUERTO);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            // Enviar el objeto Request al servidor
            out.writeObject(request);
            System.out.println("Request enviado al servidor: " + request);

            // Recibir la respuesta del servidor
            receiveMessageFromServer(in);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendRoomToServer(Room room) {
        try (Socket socket = new Socket(HOST, PUERTO);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            // Enviar el objeto Room al servidor
            out.writeObject(room);
            System.out.println("Room enviado al servidor: " + room);

            // Recibir la respuesta del servidor
            receiveMessageFromServer(in);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void receiveMessageFromServer(ObjectInputStream in) {
        try {
            // Leer el objeto enviado por el servidor
            Object respuesta = in.readObject();
            System.out.println("Respuesta recibida del servidor: " + respuesta);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}