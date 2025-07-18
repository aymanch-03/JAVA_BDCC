package org.ayman.chatapp;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    private static final int PORT = 12345;
    private static Set<ClientHandler> clientHandlers = Collections.synchronizedSet(new HashSet<>());

    public static void main(String[] args) {
        System.out.println("[Server] Chat server started on port " + PORT);
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("[Server] New client connected: " + clientSocket.getInetAddress());
                ClientHandler handler = new ClientHandler(clientSocket);
                clientHandlers.add(handler);
                new Thread(handler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void broadcast(String message, ClientHandler sender) {
        synchronized (clientHandlers) {
            for (ClientHandler handler : clientHandlers) {
                if (handler != sender) {
                    handler.sendMessage(message);
                }
            }
        }
    }

    static void removeClient(ClientHandler handler) {
        clientHandlers.remove(handler);
    }

    static class ClientHandler implements Runnable {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println("[Server] Received: " + message);
                    ChatServer.broadcast(message, this);
                }
            } catch (IOException e) {
                System.out.println("[Server] Client disconnected: " + socket.getInetAddress());
            } finally {
                ChatServer.removeClient(this);
                try {
                    socket.close();
                } catch (IOException ignored) {
                }
            }
        }

        public void sendMessage(String message) {
            out.println(message);
        }
    }
}