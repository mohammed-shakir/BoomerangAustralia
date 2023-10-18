package models.cards;

public abstract class Cards {
    protected String name, letter, region;
    protected int number;
    protected boolean hidden = false;

    public Cards(String name, String letter, int number, String region, boolean hidden) {
        this.name = name;
        this.letter = letter;
        this.number = number;
        this.region = region;
        this.hidden = hidden;
    }

    public abstract String printCardDetails();

    public abstract boolean getHidden();

    public abstract void setHidden(boolean hidden);

    public abstract String getLetter();

    public abstract int getNumber();
}