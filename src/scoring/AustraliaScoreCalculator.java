package scoring;

import models.cards.*;
import java.util.*;
import java.util.stream.Collectors;

public class AustraliaScoreCalculator implements ScoreCalculator {
    private static Set<String> previouslyVisitedRegions = new HashSet<>();

    @Override
    public int calculateThrowAndCatchScore(Cards throwCard, Cards catchCard) {
        if (throwCard == null || catchCard == null) {
            return 0;
        }

        int throwCardNumber = throwCard.getNumber();
        int catchCardNumber = catchCard.getNumber();

        return Math.abs(throwCardNumber - catchCardNumber);
    }

    @Override
    public int calculateTouristSitesScore(List<Cards> chosenCards) {
        // Map of region names to the sites in that region
        Map<String, List<String>> regionMap = new HashMap<>() {
            {
                put("Western Australia", Arrays.asList("A", "B", "C", "D"));
                put("Northern Territory", Arrays.asList("E", "F", "G", "H"));
                put("Queensland", Arrays.asList("I", "J", "K", "L"));
                put("South Australia", Arrays.asList("M", "N", "O", "P"));
                put("New South Wales", Arrays.asList("Q", "R", "S", "T"));
                put("Victoria", Arrays.asList("U", "V", "W", "X"));
                put("Tasmania", Arrays.asList("Y", "Z", "*", "-"));
            }
        };

        List<String> visitedSites = chosenCards.stream().map(Cards::getLetter).collect(Collectors.toList());

        int score = 0;
        List<String> completedRegions = new ArrayList<>();

        // Award points for fully visited regions.
        for (Map.Entry<String, List<String>> entry : regionMap.entrySet()) {
            if (visitedSites.containsAll(entry.getValue()) && !previouslyVisitedRegions.contains(entry.getKey())) {
                completedRegions.add(entry.getKey());
                score += 3;
            }
        }

        score += visitedSites.size();

        previouslyVisitedRegions.addAll(completedRegions);

        return score;
    }

    @Override
    public int calculateCollectionsScore(List<Cards> chosenCards) {
        Map<String, Integer> collectionValues = new HashMap<>();
        collectionValues.put("Leaves", 1);
        collectionValues.put("Wildflowers", 2);
        collectionValues.put("Shells", 3);
        collectionValues.put("Souvenirs", 5);

        int totalScore = 0;
        for (Cards card : chosenCards) {
            if (card instanceof AustralianCard) {
                AustralianCard ausCard = (AustralianCard) card;
                String collectionItem = ausCard.getCollectionItem();
                if (collectionValues.containsKey(collectionItem)) {
                    totalScore += collectionValues.get(collectionItem);
                }
            }
        }

        // If the total score from collections is 7 or below, double it.
        if (totalScore <= 7) {
            totalScore *= 2;
        }

        return totalScore;
    }

    @Override
    public int calculateAnimalsScore(List<Cards> chosenCards) {
        Map<String, Integer> animalCounts = new HashMap<>();
        Map<String, Integer> animalScores = new HashMap<>();

        animalScores.put("Kangaroo", 3);
        animalScores.put("Emu", 4);
        animalScores.put("Wombat", 5);
        animalScores.put("Koala", 7);
        animalScores.put("Platypus", 9);

        // Count occurrences of each animal.
        for (Cards card : chosenCards) {
            String animal = ((AustralianCard) card).getAnimalType();
            animalCounts.put(animal, animalCounts.getOrDefault(animal, 0) + 1);
        }

        int totalScore = 0;

        // Calculate the score based on pairs of animals.
        for (String animal : animalCounts.keySet()) {
            int pairs = animalCounts.get(animal) / 2;
            totalScore += pairs * animalScores.getOrDefault(animal, 0);
        }

        return totalScore;
    }

    @Override
    public int calculateActivitiesScore(List<Cards> chosenCards) {
        Map<String, Integer> activityCount = new HashMap<>();

        // Count occurrences of each activity.
        for (Cards card : chosenCards) {
            String activity = ((AustralianCard) card).getActivity();
            if (activity != null) {
                activityCount.put(activity, activityCount.getOrDefault(activity, 0) + 1);
            }
        }

        int maxActivityCount = 0;
        for (Integer count : activityCount.values()) {
            maxActivityCount = Math.max(maxActivityCount, count);
        }

        switch (maxActivityCount) {
            case 1:
                return 0;
            case 2:
                return 2;
            case 3:
                return 4;
            case 4:
                return 7;
            case 5:
                return 10;
            case 6:
                return 15;
            default:
                return 0;
        }
    }

    @Override
    public int calculateTotalScore(List<Cards> chosenCards) {
        int throwAndCatchScore = calculateThrowAndCatchScore(chosenCards.get(0),
                chosenCards.get(chosenCards.size() - 1));
        int touristSitesScore = calculateTouristSitesScore(chosenCards);
        int collectionsScore = calculateCollectionsScore(chosenCards);
        int animalsScore = calculateAnimalsScore(chosenCards);
        int activitiesScore = calculateActivitiesScore(chosenCards);

        return throwAndCatchScore + touristSitesScore + collectionsScore + animalsScore + activitiesScore;
    }
}
