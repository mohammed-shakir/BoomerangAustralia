package models.cards;

public class AustralianCard extends Cards {
    private String Collections, Animals, Activities;

    public AustralianCard(String name, String letter, String region, int number, String Collections, String Animals,
            String Activities) {
        super(name, letter, number, region);
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

    public void displayCard() {
        String separator = "+-----------------------------------------+";
        System.out.println(separator);
        System.out.println("| Name       : " + String.format("%-29s", name) + "|");
        System.out.println("| Letter     : " + String.format("%-29s", letter) + "|");
        System.out.println("| Region     : " + String.format("%-29s", region) + "|");
        System.out.println("| Number     : " + String.format("%-29d", number) + "|");
        System.out.println("| Collection : " + String.format("%-29s", Collections) + "|");
        System.out.println("| Animal     : " + String.format("%-29s", Animals) + "|");
        System.out.println("| Activity   : " + String.format("%-29s", Activities) + "|");
        System.out.println(separator);
        System.out.println();
    }
}
