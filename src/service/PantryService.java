/**
 * Nathan Gloman
 * 5/28/26
 * PantryService.java
 * Manages the user's pantry while program is running.
 * Allows access and modifications.
 */
package service;

import java.util.Set;

public class PantryService {

    private Set<String> pantry;

    /**
     * Constructor PantryService()
     * Initializes the pantry
     * @param pantry the user's pantry
     */
    public PantryService(Set<String> pantry) {
        this.pantry = pantry;
    }

    /**
     * Method getPantry()
     * Returns the full pantry set.
     * Used by MealMatcher for ingredients comparison.
     * Used by GUI to display pantry list.
     * @return pantry
     */
    public Set<String> getPantry() {
        return pantry;
    }

    /**
     * Method addIngredient()
     * Converts ingredients to lowercase and adds to pantry
     * @param ingredient
     */
    public void addIngredient(String ingredient) {
        pantry.add(ingredient.toLowerCase());
    }
}