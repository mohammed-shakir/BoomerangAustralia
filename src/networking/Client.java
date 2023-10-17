package networking;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    private String ipAddress;
    private int port;
    private Socket socket;
    private ObjectOutputStream outToServer;
    private ObjectInputStream inFromServer;

    public Client(String ipAddress, int port) {
        try {
            this.ipAddress = ipAddress;
            this.port = port;
            this.socket = new Socket(ipAddress, port);
            this.outToServer = new ObjectOutputStream(socket.getOutputStream());
            this.inFromServer = new ObjectInputStream(socket.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String promptUserForMessage() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your message for this round: ");
        return scanner.nextLine();
    }

    public void awaitMessageFromServer() {
        try {
            while (true) {
                String message = readMessageFromServer();
                System.out.println(message);
                if (message.startsWith("Round ")) {
                    String inputMessage = promptUserForMessage();
                    sendMessage(inputMessage);
                }
                if (message.equals("Game Over")) {
                    socket.close();
                    System.exit(0);
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("No message from server");
        }
    }

    private void handleRounds() {
        while (true) {
            String inputMessage = promptUserForMessage();
            sendMessage(inputMessage);
            String response = readMessageFromServer();
            if (response != null && !response.startsWith("Received messages for round")) {
                System.out.println(response);
                break;
            }
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
