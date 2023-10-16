package networking;

import java.net.*;
import java.util.ArrayList;
import models.Player;
import java.io.*;

public class Server {
    private int port;
    private ArrayList<Player> players;
    public ServerSocket aSocket;

    public Server(int port, ArrayList<Player> players) {
        this.port = port;
        this.players = players;
    }

    public void startServer(int numberPlayers, int numberOfBots) throws Exception {
        players.add(new Player(0, false, null, null, null)); // add this instance as a player
        // Open for connections if there are online players
        for (int i = 0; i < numberOfBots; i++) {
            players.add(new Player(i + 1, true, null, null, null)); // add a bot
        }
        if (numberPlayers > 1)
            aSocket = new ServerSocket(port);
        for (int i = numberOfBots + 1; i < numberPlayers + numberOfBots; i++) {
            Socket connectionSocket = aSocket.accept();
            ObjectInputStream inFromClient = new ObjectInputStream(connectionSocket.getInputStream());
            ObjectOutputStream outToClient = new ObjectOutputStream(connectionSocket.getOutputStream());
            players.add(new Player(i, false, connectionSocket, inFromClient, outToClient)); // add an online client
            System.out.println("Connected to player " + i);
            outToClient.writeObject("You connected to the server as player " + i + "\n");
        }
    }
}