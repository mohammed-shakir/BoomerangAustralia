package networking;

import java.util.Scanner;

public class ClientUI implements IClientUI {
    @Override
    public String promptUserForMessage() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the letter of the card you want to choose: ");
        String cardLetter = scanner.nextLine().trim();
        while (cardLetter.length() != 1) {
            System.out.print("Invalid input. Please enter a single letter for the card you want to choose: ");
            cardLetter = scanner.nextLine().trim();
        }
        return cardLetter;
    }

    @Override
    public void displayMessage(String message) {
        System.out.println(message);
    }
}