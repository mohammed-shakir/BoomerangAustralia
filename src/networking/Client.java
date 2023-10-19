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
        System.out.print("Enter the letter of the card you want to choose: ");
        String cardLetter = scanner.nextLine().trim();
        while (cardLetter.length() != 1) {
            System.out.print("Invalid input. Please enter a single letter for the card you want to choose: ");
            cardLetter = scanner.nextLine().trim();
        }
        return cardLetter;
    }

    public void awaitMessageFromServer() {
        try {
            while (true) {
                String message = readMessageFromServer();
                if (message.startsWith("PROMPT") || message.startsWith("Invalid")) {
                    String inputMessage = promptUserForMessage();
                    sendMessage(inputMessage);
                } else if (message.equals("Game Over")) {
                    System.out.println(message);
                    socket.close();
                    System.exit(0);
                    break;
                } else {
                    System.out.println(message);
                }
            }
        } catch (Exception e) {
            System.out.println("No message from server");
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
