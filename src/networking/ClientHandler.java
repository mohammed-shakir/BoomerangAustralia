package networking;

import java.net.*;
import java.io.*;

public class ClientHandler {
    public int id;
    public Socket socket;
    private ObjectOutputStream outToClient;
    private ObjectInputStream inFromClient;
    static int nextId = 1;

    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.outToClient = new ObjectOutputStream(socket.getOutputStream());
            this.inFromClient = new ObjectInputStream(socket.getInputStream());
            this.id = nextId;
            nextId++;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(Object message) {
        try {
            outToClient.writeObject(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String readMessage() {
        try {
            return (String) inFromClient.readObject();
        } catch (Exception e) {
            return null;
        }
    }

    public void close() {
        try {
            outToClient.close();
            inFromClient.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}