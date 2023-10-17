package models;

public class Player {

}

/*
 * import java.util.*;
 * 
 * import models.cards.AustralianCard;
 * 
 * import java.nio.ByteBuffer;
 * import java.nio.channels.SocketChannel;
 * import java.nio.charset.StandardCharsets;
 * 
 * 
 * public class Player {
 * public int playerID;
 * public boolean online;
 * public boolean isBot;
 * public SocketChannel connection;
 * public ByteBuffer buffer = ByteBuffer.allocate(1024);
 * public ArrayList<String> region = new ArrayList<String>();
 * Scanner in = new Scanner(System.in);
 * public ArrayList<AustralianCard> nextHand = new ArrayList<AustralianCard>();
 * public ArrayList<AustralianCard> hand = new ArrayList<AustralianCard>();
 * public ArrayList<AustralianCard> draft = new ArrayList<AustralianCard>();
 * HashMap<String, String> sites = new HashMap<String, String>();
 * ArrayList<HashMap<String, Integer>> rScore = new ArrayList<HashMap<String,
 * Integer>>();
 * HashMap<String, Integer> activitiesScore = new HashMap<>();
 * public int regionRoundScore = 0;
 * public int finalScore = 0;
 * 
 * public Player(int playerID, boolean isBot, SocketChannel connection) {
 * this.playerID = playerID;
 * this.connection = connection;
 * this.isBot = isBot;
 * this.online = (connection != null);
 * }
 * 
 * public void sendMessage(Object message) {
 * if (online) {
 * try {
 * buffer.clear();
 * buffer.put(message.toString().getBytes(StandardCharsets.UTF_8));
 * buffer.flip();
 * while (buffer.hasRemaining()) {
 * connection.write(buffer);
 * }
 * } catch (Exception e) {
 * e.printStackTrace();
 * }
 * } else if (!isBot) {
 * System.out.println(message);
 * }
 * }
 * 
 * public String readMessage() {
 * String word = "";
 * if (online) {
 * try {
 * buffer.clear();
 * int bytesRead = connection.read(buffer);
 * if (bytesRead > 0) {
 * buffer.flip();
 * word = StandardCharsets.UTF_8.decode(buffer).toString();
 * }
 * } catch (Exception e) {
 * e.printStackTrace();
 * }
 * } else {
 * try {
 * word = in.nextLine();
 * } catch (Exception e) {
 * e.printStackTrace();
 * }
 * }
 * return word;
 * }
 * 
 * public void addCardToDraft(Player sendToPlayer) {
 * // Print the list of cards in the draft
 * if (draft.size() == 0) {
 * sendMessage("You haven't drafted any cards yet");
 * } else {
 * sendMessage("\n*****************************\nYour current draft: \n" +
 * printCards(draft));
 * }
 * if (sites.size() > 0) {
 * TreeMap<String, String> sortedSites = new TreeMap<String, String>();
 * sortedSites.putAll(sites);
 * sendMessage("Sites from previous rounds:\n" + sortedSites + "\n");
 * 
 * }
 * // Print the list of cards in the hand
 * sendMessage("\nYour current hand: \n" + printCards(hand));
 * // Prompt the user for which cards to keep (remove from hand and add to
 * draft)
 * sendMessage("Type the letter of the card to draft");
 * String response = "";
 * if (!isBot) {
 * response = readMessage();
 * } else {
 * Random rnd = new Random();
 * response = hand.get(rnd.nextInt(hand.size() - 1)).letter;
 * }
 * for (AustralianCard c : hand) {
 * if (c.letter.equalsIgnoreCase(response)) {
 * draft.add(hand.remove(hand.indexOf(c)));
 * break;
 * }
 * }
 * sendToPlayer.nextHand.clear();
 * for (AustralianCard c : hand) {
 * sendToPlayer.nextHand.add(c);
 * } // pass my hand to the next player
 * }
 * 
 * public boolean checkRegionComplete(String theRegion) {
 * ArrayList<String> temp = new ArrayList<String>();
 * for (Map.Entry<String, String> set : sites.entrySet()) {
 * if (set.getValue().equals(theRegion))
 * temp.add(set.getKey());
 * }
 * for (AustralianCard c : draft) {
 * if (c.region.equals(theRegion) && !temp.contains(c.letter)) {
 * temp.add(c.letter);
 * }
 * }
 * if (temp.size() == 4)
 * return true;
 * else
 * return false;
 * }
 * 
 * public String printCards(List<AustralianCard> cards) {
 * // String.format("%-20s", str); % = format sequence. - = string is
 * // left-justified (no - = right-justified). 20 = string will be 20 long. s =
 * // character string format code
 * String printString = String.format("%27s", "Site [letter] (nr):  ");
 * for (AustralianCard c : cards) { // print name letter and number of each card
 * printString += String.format("%-35s", c.name + " [" + c.letter + "] (" +
 * c.number + ")");
 * }
 * printString += "\n" + String.format("%27s", "Region:  ");
 * for (AustralianCard c : cards) { // print name letter and number of each card
 * printString += String.format("%-35s", c.region);
 * }
 * printString += "\n" + String.format("%27s", "Collections:  ");
 * for (AustralianCard c : cards) { // print collections of each card, separate
 * with tab
 * printString += String.format("%-35s", c.Collections);
 * }
 * printString += "\n" + String.format("%27s", "Animals:  ");
 * for (AustralianCard c : cards) { // print animals of each card, separate with
 * tab
 * printString += String.format("%-35s", c.Animals);
 * }
 * printString += "\n" + String.format("%27s", "Activities:  ");
 * for (AustralianCard c : cards) { // print activities of each card, separate
 * with tab
 * printString += String.format("%-35s", c.Activities);
 * }
 * return printString;
 * }
 * 
 * public int numberThings(String aThing, String category) {
 * int nrThings = 0;
 * for (AustralianCard aCard : draft) {
 * if (category.equals("Collections")) {
 * if (aCard.Collections.equals(aThing)) {
 * nrThings++;
 * }
 * } else if (category.equals("Animals")) {
 * if (aCard.Animals.equals(aThing)) {
 * nrThings++;
 * }
 * } else if (category.equals("Activities")) {
 * if (aCard.Activities.equals(aThing)) {
 * nrThings++;
 * }
 * }
 * }
 * return nrThings;
 * }
 * 
 * public int roundScore(int roundNr) {
 * HashMap<String, Integer> score = new HashMap<String, Integer>();
 * // Fix total score and round score... Also, sum up the round score according
 * to
 * // the scoring sheet
 * 
 * // Requirement 10a
 * int throwCatchScore = Math.abs(draft.get(0).number - draft.get(6).number);
 * sendMessage("This round you scored " + throwCatchScore +
 * " as your Throw and catch score");
 * score.put("Throw and Catch score", throwCatchScore);
 * 
 * // Requirement 10b
 * int thisRoundSites = 0;
 * for (AustralianCard draftCard : draft) {
 * if (!sites.containsKey(draftCard.letter)) {
 * thisRoundSites++;
 * sites.put(draftCard.letter, draftCard.region);
 * }
 * }
 * sendMessage("This round you scored " + thisRoundSites +
 * " new sites points and " + regionRoundScore
 * + " points for completing regions");
 * score.put("Tourist sites score", thisRoundSites);
 * 
 * // Requirement 10c - Calculate score for Collections
 * String thisRoundCollections = "";
 * int countRoundCollections = 0;
 * String[] collectionarr = { "Leaves", "Wildflowers", "Shells", "Souvenirs" };
 * for (String thisColl : collectionarr) {
 * int nr = numberThings(thisColl, "Collections");
 * int sumColl = (nr < 8) ? nr * 2 : nr;
 * thisRoundCollections += thisColl + " [" + sumColl + " points]\t";
 * countRoundCollections += sumColl;
 * }
 * sendMessage("This round you scored these collections: " +
 * thisRoundCollections);
 * score.put("Collections score", countRoundCollections);
 * 
 * // Requirement 10d Calculate score for Animals
 * String thisRoundAnimals = "";
 * int countRoundAnimals = 0;
 * String[] animalarr = { "Kangaroos", "Emus", "Wombats", "Koalas", "Platypuses"
 * };
 * for (String thisAnim : animalarr) {
 * int frequency = numberThings(thisAnim, "Animals");
 * if (frequency == 2 && thisAnim.equals("Kangaroos")) {
 * thisRoundAnimals += "Kangaroos [3 points]\t";
 * countRoundAnimals += 3;
 * } else if (frequency == 2 && thisAnim.equals("Emus")) {
 * thisRoundAnimals += "Emus [4 points]\t";
 * countRoundAnimals += 4;
 * } else if (frequency == 2 && thisAnim.equals("Wombats")) {
 * thisRoundAnimals += "Wombats [5 points]\t";
 * countRoundAnimals += 5;
 * } else if (frequency == 2 && thisAnim.equals("Koalas")) {
 * thisRoundAnimals += "Koalas [7 points]\t";
 * countRoundAnimals += 7;
 * } else if (frequency == 2 && thisAnim.equals("Platypuses")) {
 * thisRoundAnimals += "Platypuses [9 points]";
 * countRoundAnimals += 9;
 * }
 * 
 * }
 * sendMessage("This round you scored these Animals: " + thisRoundAnimals);
 * score.put("Animals score", countRoundAnimals);
 * 
 * // Requirement 10e Calculate score for the Activities if the player wants to
 * // score it
 * String[] act = { "Indigenous Culture", "Bushwalking", "Swimming",
 * "Sightseeing" };
 * sendMessage("This round you have gathered the following new activities:");
 * HashMap<String, Integer> newActivitiesMap = new HashMap<>();
 * String newActivities = "";
 * for (String thisAct : act) { // Requirement 10e(ii)
 * if (!activitiesScore.containsKey(thisAct)) {
 * int frequency = numberThings(thisAct, "Activities");
 * newActivities += thisAct + "(# " + frequency + ")\t";
 * newActivitiesMap.put(thisAct, frequency);
 * }
 * }
 * 
 * sendMessage(newActivities + "\nSelect if you wish to score one of them");
 * int countRoundActivities = 0;
 * int[] scoreTable = { 0, 2, 4, 7, 10, 15 };
 * for (Map.Entry<String, Integer> entry : newActivitiesMap.entrySet()) {
 * int frequency = numberThings(entry.getKey(), "Activities");
 * int scoret = (frequency > 0) ? scoreTable[frequency - 1] : 0;
 * sendMessage(
 * "Want to keep " + entry.getKey() + "(" + entry.getValue() + ") [" + scoret +
 * " points]? (Y/N)");
 * String response = "";
 * if (!isBot) {
 * response = readMessage();
 * } else {
 * response = "Y";
 * }
 * if (response.equalsIgnoreCase("Y")) {
 * activitiesScore.put(entry.getKey(), scoret);
 * sendMessage("This round you scored this activity: " + entry.getKey() + "[" +
 * scoret + " points]");
 * // We do not need to add the Activity score to the score variable, since it's
 * // stored separately in the activitiesScore hashmap
 * countRoundActivities = scoret;
 * break; // Requirement 10e(i) exit the loop since you are only allowed to
 * score one
 * // activity per round
 * }
 * }
 * rScore.add(score);
 * if (roundNr == 3) {// last round
 * String t = "Throw and Catch score", to = "Tourist sites score", c =
 * "Collections score",
 * a = "Animals score";
 * int totalAct = 0;
 * for (String anAct : act) {
 * if (activitiesScore.get(anAct) == null)
 * activitiesScore.put(anAct, 0);
 * totalAct += activitiesScore.get(anAct).intValue();
 * }
 * int totalT = rScore.get(0).get(t).intValue() +
 * rScore.get(1).get(t).intValue()
 * + rScore.get(2).get(t).intValue() + rScore.get(3).get(t).intValue();
 * int totalTo = rScore.get(0).get(to).intValue() +
 * rScore.get(1).get(to).intValue()
 * + rScore.get(2).get(to).intValue() + rScore.get(3).get(to).intValue();
 * int totalC = rScore.get(0).get(c).intValue() +
 * rScore.get(1).get(c).intValue()
 * + rScore.get(2).get(c).intValue() + rScore.get(3).get(c).intValue();
 * int totalA = rScore.get(0).get(a).intValue() +
 * rScore.get(1).get(a).intValue()
 * + rScore.get(2).get(a).intValue() + rScore.get(3).get(a).intValue();
 * // Requirement 12
 * String finalScore =
 * "                       Round 1\tRound 2\tRound 3\tRound 4\tTotal\n";
 * finalScore += "Throw and Catch score:   " + rScore.get(0).get(t) + "\t  " +
 * rScore.get(1).get(t)
 * + "\t  " + rScore.get(2).get(t) + "\t  " + rScore.get(3).get(t) + "\t  " +
 * totalT + "\n";
 * finalScore += "  Tourist sites score:   " + rScore.get(0).get(to) + "\t  " +
 * rScore.get(1).get(to)
 * + "\t  " + rScore.get(2).get(to) + "\t  " + rScore.get(3).get(to) + "\t  " +
 * totalTo + "\n";
 * ;
 * finalScore += "    Collections score:   " + rScore.get(0).get(c) + "\t  " +
 * rScore.get(1).get(c)
 * + "\t  " + rScore.get(2).get(c) + "\t  " + rScore.get(3).get(c) + "\t  " +
 * totalC + "\n";
 * finalScore += "        Animals score:   " + rScore.get(0).get(a) + "\t  " +
 * rScore.get(1).get(a)
 * + "\t  " + rScore.get(2).get(a) + "\t  " + rScore.get(3).get(a) + "\t  " +
 * totalA + "\n";
 * finalScore += "                        IndC\tBushw\tSwim\tSights\n";
 * finalScore += "     Activities score:   " + activitiesScore.get(act[0]) +
 * "\t  "
 * + activitiesScore.get(act[1]) + "\t  " + activitiesScore.get(act[2]) + "\t  "
 * + activitiesScore.get(act[3]) + "\t  " + totalAct + "\n";
 * finalScore += "       Region bonuses:   " + (region.size() * 3) + "\n";
 * finalScore += "          Total score:   " + (totalT + totalC + totalA +
 * totalAct + (region.size() * 3))
 * + " points\n";
 * sendMessage("\n*********************************************\n" +
 * finalScore);
 * return (totalT + totalC + totalA + totalAct + (region.size() * 3));
 * }
 * int totalRoundScore = regionRoundScore + throwCatchScore + thisRoundSites +
 * countRoundCollections
 * + countRoundAnimals + countRoundActivities;
 * regionRoundScore = 0; // it has been added to the region list, and will be
 * summed up at the end of the
 * // 4 rounds.
 * return totalRoundScore;
 * }
 * }
 */