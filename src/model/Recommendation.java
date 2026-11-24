/**
 * Nathan Gloman
 * 5/28/26
 * Recommendation.java
 * Represents the result of matching a meal against the user's
 * pantry. It stores the meal, how well it matched, and what
 * ingredients are missing. The recommendation result is created
 * by MealMatcher then passed to GUI for displaying.
 */
package model;

import java.util.Set;

public class Recommendation {

    private Meal meal;
    private double score;
    private Set<String> missingIngredients;

    /**
     * Constructor Recommendation()
     * Creates a new Recommendation with the given meal, score,
     * and missing ingredients
     * @param meal the meal that was matched
     * @param score match score between 0.0 and 1.0 (1.0 = 100%)
     * @param missingIngredients ingredients missing from pantry
     */
    public Recommendation(Meal meal, double score, Set<String> missingIngredients) {
        this.meal = meal;
        this.score = score;
        this.missingIngredients = missingIngredients;
    }

    /**
     * Method getMeal()
     * Returns the meal that was recommended
     * @return meal
     */
    public Meal getMeal() {
        return meal;
    }

    /**
     * Method getScore()
     * Returns the score as a decimal between 0.0 and 1.0
     * @return score
     */
    public double getScore() {
        return score;
    }

    /**
     * Method getMissingIngredients()
     * Returns the set of ingredients that are missing
     * for this meal
     * @return missingIngredients
     */
    public Set<String> getMissingIngredients() {
        return missingIngredients;
    }
}