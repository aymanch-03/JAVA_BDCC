package org.ayman.chatapp;

import java.io.*;
import java.net.*;

public class ChatClient {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public ChatClient(String host, int port) throws IOException {
        socket = new Socket(host, port);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    public String readMessage() throws IOException {
        return in.readLine();
    }

    public void close() throws IOException {
        socket.close();
    }
}