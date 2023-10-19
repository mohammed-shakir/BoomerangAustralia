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
    public void run(String[] args) throws Exception {
        if (args.length == 3) {
            startServer(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]));
        } else if (args.length == 2) {
            startClient(args[0], Integer.parseInt(args[1]), new ClientUI());
        } else {
            System.out.println("Usage: java Boomerang <port> or java Boomerang <ip> <port>");
        }
    }

    private void startServer(int port, int amountOfPlayers, int amountOfBots) {
        Server.getInstance().serverStart(port);
        Server.getInstance().listenToClients(amountOfPlayers);
        Server.getInstance().initiateBots(amountOfBots);

        GameLoop serverGameLoop = new GameLoop();
        serverGameLoop.run();

        Server.getInstance().stopServer();
    }

    private void startClient(String ip, int port, IClientUI clientUI) {
        Client client = new Client(ip, port, clientUI);
        client.awaitMessageFromServer();
    }
}
