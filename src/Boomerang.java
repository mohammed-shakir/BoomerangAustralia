import networking.*;
import game.*;

public class Boomerang {
    public static void main(String[] args) {
        try {
            BoomerangApp boomerangApp = new BoomerangApp();
            boomerangApp.run(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class BoomerangApp {
    public static boolean TEST_1 = false; // Requirment for test 1

    public void run(String[] args) throws Exception {
        if (args.length == 3) {
            startServer(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]));
        } else if (args.length == 2) {
            startClient(args[0], Integer.parseInt(args[1]), new ClientUI());
        } else {
            System.out.println("Usage: java Boomerang <port> or java Boomerang <ip> <port>");
        }
    }

    public void startServer(int port, int amountOfPlayers, int amountOfBots) {
        if ((amountOfPlayers + amountOfBots) > 4 || (amountOfPlayers + amountOfBots) < 2 || amountOfPlayers < 0
                || amountOfBots < 0) {
            System.out.println("Invalid amount of players");
            throw new IllegalArgumentException();
        }

        // Check Test 1
        if (TEST_1) {
            return;
        }

        Server.getInstance().serverStart(port);
        Server.getInstance().listenToClients(amountOfPlayers);
        Server.getInstance().initiateBots(amountOfBots);

        // Start game
        GameLoop serverGameLoop = new GameLoop();
        serverGameLoop.run();

        Server.getInstance().stopServer();
    }

    public void startClient(String ip, int port, IClientUI clientUI) {
        Client client = new Client(ip, port, clientUI);
        client.awaitMessageFromServer();
    }
}
