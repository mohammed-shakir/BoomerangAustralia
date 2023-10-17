package models.cards;

public abstract class Cards {
    protected String name, letter, region;
    protected int number;

    public Cards(String name, String letter, int number, String region) {
        this.name = name;
        this.letter = letter;
        this.number = number;
        this.region = region;
    }

    public abstract String displayCard();
}