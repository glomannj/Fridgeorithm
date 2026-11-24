/**
 * Nathan Gloman
 * 5/28/26
 * CS 123
 * Student Showcase
 * Fridgorithm
 * This program gives the user meal recommendations based upon available
 * ingredients. It imports a JSON file that contains a list of meals along
 * with associated ingredients and estimated preparation time. Users are
 * able to add and remove ingredients to a pantry list that is saved to
 * a text file upon closure of the application. When re-opened, the saved
 * pantry list will be loaded in for continuous use.
 *
 * Main.java
 * The entry point of Fridgorithm.
 * Meals are loaded in from mealsList.json.
 * Saved pantry is loaded in from pantry.txt.
 * Executes the GUI with meals and pantry passed to it.
 */

import service.MealLoader;
import storage.PantryStorage;
import ui.GUI;
import ui.ConsoleUI;

import javax.swing.*;
import java.util.List;

import java.util.Set;

public class Main {

    public static void main(String[] args) {
        MealLoader loader = new MealLoader();
        PantryStorage storage = new PantryStorage();

        List<model.Meal> meals = loader.loadMeals("mealsList.json");
        Set<String> pantry = storage.loadPantry();

        // GUI version of application
        SwingUtilities.invokeLater(() -> new GUI(meals, pantry));

        //Console version of application
//        ConsoleUI ui = new ConsoleUI(meals, pantry);
//        ui.start();
    }
}