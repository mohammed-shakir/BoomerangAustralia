package game;

import java.util.ArrayList;

import networking.Server;

public class GameLoop {
    private final int LOOP_COUNT = 4;

    public GameLoop() {
    }

    public void run() {
        for (int i = 0; i < LOOP_COUNT; i++) {
            System.out.println("Round " + (i + 1));

            ArrayList<String> messages = Server.getInstance().waitForClientMessages();
            for (String message : messages) {
                System.out.println(message);
            }

            Server.getInstance().broadcastMessage("Received messages for round " + (i + 1));
        }
    }
}
