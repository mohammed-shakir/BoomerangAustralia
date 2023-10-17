package game;

import java.util.ArrayList;

import networking.Server;

public class GameLoop {

    private final Server server;
    private final int LOOP_COUNT = 4;

    public GameLoop(Server server) {
        this.server = server;
    }

    public void run() {
        for (int i = 0; i < LOOP_COUNT; i++) {
            System.out.println("Round " + (i + 1));

            ArrayList<String> messages = server.waitForClientMessages();
            for (String message : messages) {
                System.out.println(message);
            }

            server.broadcastMessage("Received messages for round " + (i + 1));
        }
    }
}
