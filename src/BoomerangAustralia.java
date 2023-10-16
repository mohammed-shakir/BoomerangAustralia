import java.util.*;
import java.net.*;
import java.util.concurrent.*;

import models.Card;
import models.Player;
import networking.Client;
import networking.Server;

public class BoomerangAustralia {
	public ServerSocket aSocket;
	public ArrayList<Player> players = new ArrayList<Player>();
	public Card[] arrayDeck;
	public ArrayList<Card> deck;

	public BoomerangAustralia(String[] params) throws Exception {
		// Requirement 1, 2-4 players
		if (params.length == 2 && ((Integer.valueOf(params[0]).intValue() + Integer.valueOf(params[1]).intValue()) >= 2)
				&& ((Integer.valueOf(params[0]).intValue() + Integer.valueOf(params[1]).intValue()) <= 4)) {
			this.initGame(Integer.valueOf(params[0]).intValue(), Integer.valueOf(params[1]).intValue());
		} else if (params.length == 1) {
			client(params[0]);
		} else {
			System.out.println("This game is for a total of 2-4 players/bots");
			System.out.println("Server syntax: java BoomerangAustralia numPlayers numBots");
			System.out.println("Client syntax: IP");
		}
	}

	public void initGame(int numPlayers, int numBots) {
		arrayDeck = new Card[28]; // Requirement 2
		arrayDeck[0] = new Card("The Bungle Bungles", "A", "Western Australia", 1, "Leaves", "", "Indigenous Culture");
		arrayDeck[1] = new Card("The Pinnacles", "B", "Western Australia", 1, "", "Kangaroos", "Sightseeing");
		arrayDeck[2] = new Card("Margaret River", "C", "Western Australia", 1, "Shells", "Kangaroos", "");
		arrayDeck[3] = new Card("Kalbarri National Park", "D", "Western Australia", 1, "Wildflowers", "",
				"Bushwalking");
		arrayDeck[4] = new Card("Uluru", "E", "Northern Territory", 4, "", "Emus", "Indigenous Culture");
		arrayDeck[5] = new Card("Kakadu National Park", "F", "Northern Territory", 4, "", "Wombats", "Sightseeing");
		arrayDeck[6] = new Card("Nitmiluk National Park", "G", "Northern Territory", 4, "Shells", "Platypuses", "");
		arrayDeck[7] = new Card("King's Canyon", "H", "Northern Territory", 4, "", "Koalas", "Swimming");
		arrayDeck[8] = new Card("The Great Barrier Reef", "I", "Queensland", 6, "Wildflowers", "", "Sightseeing");
		arrayDeck[9] = new Card("The Whitsundays", "J", "Queensland", 6, "", "Kangaroos", "Indigenous Culture");
		arrayDeck[10] = new Card("Daintree Rainforest", "K", "Queensland", 6, "Souvenirs", "", "Bushwalking");
		arrayDeck[11] = new Card("Surfers Paradise", "L", "Queensland", 6, "Wildflowers", "", "Swimming");
		arrayDeck[12] = new Card("Barossa Valley", "M", "South Australia", 3, "", "Koalas", "Bushwalking");
		arrayDeck[13] = new Card("Lake Eyre", "N", "South Australia", 3, "", "Emus", "Swimming");
		arrayDeck[14] = new Card("Kangaroo Island", "O", "South Australia", 3, "", "Kangaroos", "Bushwalking");
		arrayDeck[15] = new Card("Mount Gambier", "P", "South Australia", 3, "Wildflowers", "", "Sightseeing");
		arrayDeck[16] = new Card("Blue Mountains", "Q", "New South Whales", 5, "", "Wombats", "Indigenous Culture");
		arrayDeck[17] = new Card("Sydney Harbour", "R", "New South Whales", 5, "", "Emus", "Sightseeing");
		arrayDeck[18] = new Card("Bondi Beach", "S", "New South Whales", 5, "", "Wombats", "Swimming");
		arrayDeck[19] = new Card("Hunter Valley", "T", "New South Whales", 5, "", "Emus", "Bushwalking");
		arrayDeck[20] = new Card("Melbourne", "U", "Victoria", 2, "", "Wombats", "Bushwalking");
		arrayDeck[21] = new Card("The MCG", "V", "Victoria", 2, "Leaves", "", "Indigenous Culture");
		arrayDeck[22] = new Card("Twelve Apostles", "W", "Victoria", 2, "Shells", "", "Swimming");
		arrayDeck[23] = new Card("Royal Exhibition Building", "X", "Victoria", 2, "Leaves", "Platypuses", "");
		arrayDeck[24] = new Card("Salamanca Markets", "Y", "Tasmania", 7, "Leaves", "Emus", "");
		arrayDeck[25] = new Card("Mount Wellington", "Z", "Tasmania", 7, "", "Koalas", "Sightseeing");
		arrayDeck[26] = new Card("Port Arthur", "*", "Tasmania", 7, "Leaves", "", "Indigenous Culture");
		arrayDeck[27] = new Card("Richmond", "-", "Tasmania", 7, "", "Kangaroos", "Swimming");
		String[] regions = { "Wester Australia", "Northern Territory", "Queensland", "South Australia",
				"New South Whales", "Victoria", "Tasmania" };
		ArrayList<String> finishedRegions = new ArrayList<String>();
		try {
			deck = new ArrayList<>(Arrays.asList(arrayDeck));
			Collections.shuffle(deck); // Requirement 3
			server(numPlayers, numBots);
			// Requirement 4
			for (Player player : players) {
				for (int i = 0; i < 7; i++) {
					player.hand.add(deck.remove(0));
				}
			}
			// Requirement 12 - play 4 rounds
			for (int round = 0; round < 4; round++) { // Requirement 11
				// Draft 6 cards, the 7th card is sent to the previous player's hand
				for (int i = 0; i < 6; i++) { // Requirement 8
					ExecutorService threadpool = Executors.newFixedThreadPool(players.size());
					for (int p = 0; p < players.size(); p++) {
						final int currentPlayerIndex = p;
						Player sendHand;
						if (i < 5) {
							// Requirement 6 (Pass remaining cards to the next player)
							sendHand = (p < (players.size() - 1)) ? players.get(p + 1) : players.get(0);
						} else {
							// Requirement 9 (The final card is passed to the previous player)
							sendHand = (p > 0) ? players.get(p - 1) : players.get(players.size() - 1);
						}
						// Requirement 7 - Make sure every player can draft their card at the same time
						Runnable task = new Runnable() {
							@Override
							public void run() {
								players.get(currentPlayerIndex).addCardToDraft(sendHand);
							}
						};
						threadpool.execute(task);
					}
					threadpool.shutdown();

					// wait for all the hands to switch places
					while (!threadpool.isTerminated()) {
						Thread.sleep(100);
					}

					for (Player sendToPlayer : players) {
						sendToPlayer.hand.clear();
						for (Card c : sendToPlayer.nextHand) {
							sendToPlayer.hand.add(c);
						} // grab the cards passed on from the previous player
					}
					// Requirement 5 (Keep throw card hidden)
					if (i > 0) {
						// Requirement 7 (Show cards)
						for (int pID = 0; pID < players.size(); pID++) {
							for (Player sendToPlayer : players) {
								Player id = players.get(pID);
								sendToPlayer.sendMessage("\nPlayer " + pID + " has drafted\n"
										+ id.printCards(id.draft.subList(1, id.draft.size())));
							}
						}
					}
				}
				// Check if I'm the first to complete a region, add regionBonusScore if that's
				// the case
				// Requirement 10b(i+ii)
				for (int r = 0; r < regions.length; r++) {
					boolean regionComplete = false;
					for (Player player : players) {
						if (!finishedRegions.contains(regions[r]) && (player.checkRegionComplete(regions[r]))) {
							player.region.add(regions[r]);
							player.regionRoundScore += 3;
							regionComplete = true;
						}
					}
					if (regionComplete) {
						finishedRegions.add(regions[r]); // Requirement 10b(ii)
					}
				}

				for (Player player : players) {
					player.draft.add(player.hand.remove(0)); // Requirement 9 - last card on hand is added to draft
					// Requirement 10 - scoring
					player.sendMessage("********************************\nYour draft this round: \n"
							+ player.printCards(player.draft) + "\n");
					player.finalScore = player.roundScore(round);
					player.sendMessage("The following regions have now been completed: " + finishedRegions);
				}

				deck = new ArrayList<>(Arrays.asList(arrayDeck)); // Requirement 11 & Requirement 2
				Collections.shuffle(deck); // Requirement 3
				// Requirement 4
				for (Player player : players) {
					player.draft.clear();
					player.hand.clear();
					for (int i = 0; i < 7; i++) {
						player.hand.add(deck.remove(0));
					}
				}
			}
			Player highScore = players.get(0);
			for (Player player : players) {
				if (player.finalScore > highScore.finalScore) {
					highScore = player;
				}
			}
			for (Player player : players) {
				player.sendMessage("The winner is player: " + players.indexOf(highScore) + " with "
						+ highScore.finalScore + " points");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void client(String ipAddress) throws Exception {
		Client client = new Client(ipAddress, 2048);
		client.connectToServer();
	}

	public void server(int numberPlayers, int numberOfBots) throws Exception {
		Server server = new Server(2048, players);
		server.startServer(numberPlayers, numberOfBots);
	}

	public static void main(String argv[]) {
		try {
			new BoomerangAustralia(argv);
		} catch (Exception e) {

		}
	}

}
