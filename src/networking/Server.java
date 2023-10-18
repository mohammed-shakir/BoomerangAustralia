package networking;

import java.net.*;
import java.util.ArrayList;

import models.player.HumanPlayer;
import models.player.Player;

import java.io.*;

public class Server {
    private int port;
    public ServerSocket aSocket;
    public ArrayList<ClientHandler> clients = new ArrayList<ClientHandler>();
    private static Server server_instance = null;
    public ArrayList<Player> players = new ArrayList<Player>();

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

        public ArrayList<ClientHandler> getClients() {
            return clients;
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

    public Server() {
    }

    public void serverStart(int port) {
        try {
            this.port = port;
            this.aSocket = new ServerSocket(port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static synchronized Server getInstance() {
        if (server_instance == null)
            server_instance = new Server();

        return server_instance;
    }

    public boolean acceptClient() {
        try {
            Socket connection = aSocket.accept();
            ClientHandler handler = new ClientHandler(connection);
            clients.add(handler);

            HumanPlayer newPlayer = new HumanPlayer(handler.id);
            players.add(newPlayer);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void listenToClients(int amountOfPlayers) {
        while (clients.size() < amountOfPlayers) {
            if (acceptClient()) {
                System.out.println("Player " + players.get(players.size() - 1).id + " connected");
            }
        }
        broadcastMessage("\033[32mStart Game \033[0m \n");
    }

    public void broadcastMessage(String message) {
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }

    public String readMessageFromClient(int id) {
        return clients.get(id).readMessage();
    }

    public ArrayList<String> waitForClientMessages() {
        ThreadPool<String> pool = new ThreadPool<String>(clients.size());
        for (int i = 0; i < clients.size(); i++) {
            int id = i;
            pool.submit_task(() -> readMessageFromClient(id));
        }
        return pool.run_tasks();
    }

    public Player getPlayerById(int id) {
        return players.get(id);
    }

    public void sendMessageToPlayer(int id, String message) {
        for (ClientHandler client : clients) {
            if (client.id == id) {
                client.sendMessage(message);
                break;
            }
        }
    }

    public void stopServer() {
        try {
            for (ClientHandler client : clients) {
                client.close();
            }
            if (!this.aSocket.isClosed()) {
                this.aSocket.close();
            }
            System.out.println("Game Over");
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
