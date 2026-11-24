/**
 * Nathan Gloman
 * 5/28/26
 * MealMatcher.java
 * Compares the meal's ingredients against the user's pantry, calculates
 * a match score, and returns a ranked list of recommendations
 */
package service;

import model.Meal;
import model.Recommendation;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

public class MealMatcher {

    /**
     * Method rankMeals()
     * Takes a list of meals and user's pantry and returns a
     * ranked list of recommendations. Ranking is sorted from
     * highest to lowest depending on score.
     * @param meals list of meals
     * @param pantry the user's pantry
     * @return results
     */
    public List<Recommendation> rankMeals(List<Meal> meals, Set<String> pantry) {
        List<Recommendation> results = new ArrayList<>();

        // loops through every meal and scores it against the pantry
        for (Meal meal : meals) {
            Set<String> mealIngredients = meal.getIngredients();

            // tracks missing ingredients from pantry
            Set<String> missing = new HashSet<>();

            // counter for present ingredients
            int matchCount = 0;

            // check each ingredient against the pantry
            for (String ingredient : mealIngredients) {
                if (pantry.contains(ingredient)) {
                    matchCount++;               // user has ingredient
                } else {
                    missing.add(ingredient);    // user is missing ingredient
                }
            }

            // the recommendation score
            double score;

            // calculates score as a decimal between 0.0 and 1.0
            // returns 0 if meal has no ingredients
            // avoids dividing by 0
            if (mealIngredients.isEmpty()) {
                score = 0;
            } else {
                score = (double)matchCount / mealIngredients.size();
            }

            // creates and adds a new recommendation to the results list
            results.add(new Recommendation(meal, score, missing));
        }

        // sorts the results from highest to lowest
        results.sort((a, b) -> Double.compare(b.getScore(), a.getScore()));

        return results;
    }
}