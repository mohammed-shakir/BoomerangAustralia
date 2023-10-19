package networking;

import java.net.*;
import java.util.ArrayList;

import models.player.Bot;
import models.player.HumanPlayer;
import models.player.Player;

public class Server {
    private int port;
    public ServerSocket aSocket;
    public ArrayList<ClientHandler> clients = new ArrayList<ClientHandler>();
    private static Server server_instance = null;
    public ArrayList<Player> players = new ArrayList<Player>();

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
                System.out.println("Player " + players.get(players.size() - 1).id + "connected");
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

    public void sendMessageToPlayer(int id, String message) {
        for (ClientHandler client : clients) {
            if (client.id == id) {
                client.sendMessage(message);
                break;
            }
        }
    }

    public void initiateBots(int amountOfBots) {
        int amountOfPlayers = players.size();
        for (int i = 1; i <= amountOfBots; i++) {
            Bot bot = new Bot(amountOfPlayers + i);
            players.add(bot);
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
