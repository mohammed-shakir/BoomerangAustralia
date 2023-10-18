import networking.*;
import game.*;

public class Boomerang {

    public Boomerang(String[] args) throws Exception {
        if (args.length == 1) {
            Server.getInstance().serverStart(Integer.parseInt(args[0]));
            Server.getInstance().listenToClients(2);
            GameLoop serverGameLoop = new GameLoop();
            serverGameLoop.run();
            Server.getInstance().stopServer();
        } else if (args.length == 2) {
            Client client = new Client(args[0], Integer.parseInt(args[1]));
            client.awaitMessageFromServer();
        } else {
            System.out.println("Usage: java Boomerang <port> or java Boomerang <ip> <port>");
        }
    }

    public static void main(String[] args) {
        try {
            new Boomerang(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
