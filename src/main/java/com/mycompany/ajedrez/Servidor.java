package com.mycompany.ajedrez;

import com.mycompany.ajedrez.server.Request;
import com.mycompany.ajedrez.server.Room;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Servidor {
    ServerSocket servidor = null;
    final int PUERTO = 5000;
    private ArrayList<Room> rooms = new ArrayList<>(); // Lista de salas
    private Map<String, Socket> userSockets = new HashMap<>(); // Mapa para asociar usuarios con sus sockets
}