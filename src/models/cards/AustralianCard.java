package models.cards;

public class AustralianCard extends Cards {
    private String Collections, Animals, Activities;

    public AustralianCard(String name, String letter, String region, int number, String Collections, String Animals,
            String Activities, boolean hidden) {
        super(name, letter, number, region, hidden);
        this.Collections = Collections;
        this.Animals = Animals;
        this.Activities = Activities;
    }

    @Override
    public String toString() {
        return "AustralianCard {" +
                "Name: '" + name + '\'' +
                ", Letter: '" + letter + '\'' +
                ", Region: '" + region + '\'' +
                ", Number: " + number +
                ", Collections: '" + Collections + '\'' +
                ", Animals: '" + Animals + '\'' +
                ", Activities: '" + Activities + '\'' +
                '}';
    }

    public String printCardDetails(boolean dontShowCard) {
        StringBuilder sb = new StringBuilder();

        if (dontShowCard) {
            return "card(\"Throw Card\")";
        }

        sb.append("\033[32mName: \033[0m").append(name).append(", ").append("Letter: ").append(letter)
                .append(", ")
                .append("Region: ")
                .append(region).append(", ").append("Number: ").append(number).append(", ").append("Collections: ")
                .append(Collections).append(", ").append("Animals: ").append(Animals).append(", ")
                .append("Activities: ")
                .append(Activities);

        return sb.toString();

    }

    public boolean getHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public String getLetter() {
        return letter;
    }

    public int getNumber() {
        return this.number;
    }

    public String getCollectionItem() {
        return this.Collections;
    }

    public String getRegion() {
        return this.region;
    }

    public String getName() {
        return this.name;
    }

    public String getAnimalType() {
        return this.Animals;
    }

    public String getActivity() {
        return this.Activities;
    }
}
