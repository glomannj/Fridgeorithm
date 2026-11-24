/**
 * Nathan Gloman
 * 5/28/26
 * Meal.java
 * A representation of a single meal. This class holds the
 * name, ingredients, and prep time and provides a way to
 * access the data.
 */
package model;

import java.util.Set;

public class Meal {

    private String name;
    private Set<String> ingredients;
    private int prepTime;

    /**
     * Constructor Meal()
     * Creates a new Meal with given name, ingredients, and prep-time.
     * @param name the name of the meal
     * @param ingredients the set of ingredients required for the meal
     * @param prepTime Estimated time to prepare in minutes
     */
    public Meal(String name, Set<String> ingredients, int prepTime) {
        this.name = name;
        this.ingredients = ingredients;
        this.prepTime = prepTime;
    }

    /**
     * Method getName()
     * Returns the name of the meal
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Method getIngredients()
     * Returns the set of ingredients required for the meal
     * @return ingredients
     */
    public Set<String> getIngredients() {
        return ingredients;
    }

    /**
     * Method getPrepTime()
     * Returns the prep time of the meal in minutes
     * @return prepTime
     */
    public int getPrepTime() {
        return prepTime;
    }
}