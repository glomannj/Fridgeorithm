/**
 * Nathan Gloman
 * 5/28/26
 * MealLoader.java
 * Reads the mealsList JSON file and converts each entry into
 * a meal object. It's called at execution and the result is
 * passed into GUI.
 */
package service;

import model.Meal;

import org.json.JSONArray;
import org.json.JSONObject;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.io.IOException;

public class MealLoader {

    /**
     * Method loadMeals()
     * Reads the JSON file and returns a list of Meals.
     * @param filePath the file to be read
     * @return meals or empty list if file was not read
     */
    public List<Meal> loadMeals(String filePath) {
        List<Meal> meals = new ArrayList<>();

        // The try block was written using generative Ai
        try {
            // reads the entire JSON file into a String a parse it into JSON array
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            JSONArray jsonArray = new JSONArray(content);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);

                // extracts the keys from the JSON object
                String name = obj.getString("name");
                int prepTime = obj.getInt("prepTime");
                JSONArray ingredientsArray = obj.getJSONArray("ingredients");

                // converts the ingredients array into a Set of lowercase strings
                Set<String> ingredients = new HashSet<>();
                for (int j = 0; j < ingredientsArray.length(); j++) {
                    ingredients.add(ingredientsArray.getString(j).trim().toLowerCase());
                }

                // creates a new meal and adds to the list
                meals.add(new Meal(name, ingredients, prepTime));
            }

        } catch (IOException e) {
            System.out.println("Error loading meal list: " + e.getMessage());
        }

        return meals;
    }
}