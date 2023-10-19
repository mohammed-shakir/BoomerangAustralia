package networking;

import java.io.*;
import java.net.*;

public class Client {
    private String ipAddress;
    private int port;
    private Socket socket;
    private ObjectOutputStream outToServer;
    private ObjectInputStream inFromServer;
    private IClientUI clientUI;

    public Client(String ipAddress, int port, IClientUI clientUI) {
        try {
            this.ipAddress = ipAddress;
            this.port = port;
            this.socket = new Socket(ipAddress, port);
            this.outToServer = new ObjectOutputStream(socket.getOutputStream());
            this.inFromServer = new ObjectInputStream(socket.getInputStream());
            this.clientUI = clientUI;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void awaitMessageFromServer() {
        try {
            while (true) {
                String message = readMessageFromServer();
                if (message.startsWith("PROMPT") || message.startsWith("Invalid")) {
                    String inputMessage = clientUI.promptUserForMessage();
                    sendMessage(inputMessage);
                } else if (message.equals("Game Over")) {
                    clientUI.displayMessage("\n" + message);
                    socket.close();
                    System.exit(0);
                    break;
                } else {
                    clientUI.displayMessage(message);
                }
            }
        } catch (Exception e) {
            clientUI.displayMessage("No message from server");
        }
    }

    public String readMessageFromServer() {
        try {
            return (String) inFromServer.readObject();
        } catch (Exception e) {
            return "No message from server";
        }
    }

    public void sendMessage(String message) {
        try {
            outToServer.writeObject(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
