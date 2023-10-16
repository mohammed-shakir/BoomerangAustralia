package models;

public class Card {
    public String name, letter, region, Collections, Animals, Activities;
    public int number;
    public String sortMode;

    public Card(String name, String letter, String region, int number, String Collections, String Animals,
            String Activities) {
        this.name = name;
        this.letter = letter;
        this.number = number;
        this.region = region;
        this.Collections = Collections;
        this.Animals = Animals;
        this.Activities = Activities;
    }

    public boolean equals(Object o) {
        return ((Card) o).letter.equals(letter);
    }
}
