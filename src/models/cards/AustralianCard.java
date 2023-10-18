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

    public String printCardDetails() {
        StringBuilder sb = new StringBuilder();
        sb.append("Name: ").append(name).append(", ").append("Letter: ").append(letter).append(", ").append("Region: ")
                .append(region).append(", ").append("Number: ").append(number).append(", ").append("Collections: ")
                .append(Collections).append(", ").append("Animals: ").append(Animals).append(", ")
                .append("Activities: ")
                .append(Activities);

        return sb.toString();
    }

    public String displayCard() {
        StringBuilder sb = new StringBuilder();
        String separator = "+-----------------------------------------+";
        sb.append(separator).append("\n");
        sb.append("| Name       : ").append(String.format("%-29s", name)).append("|\n");
        sb.append("| Letter     : ").append(String.format("%-29s", letter)).append("|\n");
        sb.append("| Region     : ").append(String.format("%-29s", region)).append("|\n");
        sb.append("| Number     : ").append(String.format("%-29d", number)).append("|\n");
        sb.append("| Collection : ").append(String.format("%-29s", Collections)).append("|\n");
        sb.append("| Animal     : ").append(String.format("%-29s", Animals)).append("|\n");
        sb.append("| Activity   : ").append(String.format("%-29s", Activities)).append("|\n");
        sb.append(separator).append("\n");

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
}
