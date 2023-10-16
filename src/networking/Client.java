package networking;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    private String ipAddress;
    private int port;

    public Client(String ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public void connectToServer() throws Exception {
        // Connect to server
        Socket aSocket = new Socket(ipAddress, port);
        ObjectOutputStream outToServer = new ObjectOutputStream(aSocket.getOutputStream());
        ObjectInputStream inFromServer = new ObjectInputStream(aSocket.getInputStream());
        String nextMessage = "";
        while (!nextMessage.contains("winner")) {
            nextMessage = (String) inFromServer.readObject();
            System.out.println(nextMessage);
            if (nextMessage.contains("Type") || nextMessage.contains("keep")) {
                Scanner in = new Scanner(System.in);
                outToServer.writeObject(in.nextLine());
            }
        }
    }
}