import networking.*;
import game.*;

public class Boomerang {

    public Boomerang(String[] args) throws Exception {
        if (args.length == 3) {
            Server(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]));
        } else if (args.length == 2) {
            Client(args[0], Integer.parseInt(args[1]));
        } else {
            System.out.println("Usage: java Boomerang <port> or java Boomerang <ip> <port>");
        }
    }

    public void Server(int port, int amountOfPlayers, int amountOfBots) {
        Server.getInstance().serverStart((port));
        Server.getInstance().listenToClients(amountOfPlayers);
        Server.getInstance().initiateBots(amountOfBots);
        GameLoop serverGameLoop = new GameLoop();
        serverGameLoop.run();
        Server.getInstance().stopServer();
    }

    public void Client(String ip, int port) {
        Client client = new Client(ip, port);
        client.awaitMessageFromServer();
    }

    public static void main(String[] args) {
        try {
            new Boomerang(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
