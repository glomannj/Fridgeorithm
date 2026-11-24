/**
 * Nathan Gloman
 * 5/28/26
 * ConsoleUI.java
 * Provides a text-based interface for the application. This was
 * the original version before the GUI was added. It handles all
 * user input and output through the terminal.
 */
package ui;

import model.Recommendation;
import service.PantryService;
import service.MealMatcher;
import storage.PantryStorage;
import model.Meal;

import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class ConsoleUI {

    private List<Meal> meals;               // the full list of meals from JSON
    private PantryService pantryService;    // manages user's pantry
    private MealMatcher matcher;            // handles meal matching and scoring
    private PantryStorage storage;          // handles pantry saving to text file
    private Scanner scanner;

    /**
     * Constructor ConsoleUI()
     * Initializes all services
     * @param meals the list of meals
     * @param pantry the user's pantry
     */
    public ConsoleUI(List<Meal> meals, Set<String> pantry) {
        this.meals = meals;
        this.pantryService = new PantryService(pantry);
        this.matcher = new MealMatcher();
        this.storage = new PantryStorage();
        this.scanner = new Scanner(System.in);
    }

    /**
     * Method start()
     * Initiates the main loop of the application.
     * Keeps running until user exits
     */
    public void start() {
        boolean running = true;

        while (running) {
            // the main menu
            System.out.println("\nWelcome to the Fridgorithm");
            System.out.println("1. View Pantry");
            System.out.println("2. Add Ingredient");
            System.out.println("3. Recommend a Meal");
            System.out.println("4. Exit");

            // input validation
            int choice = getValidChoice();

            // executes chosen action
            switch (choice) {
                case 1 -> showPantry();
                case 2 -> addIngredient();
                case 3 -> recommend();
                case 4 -> running = exitApp();
                default -> System.out.println("Invalid choice");
            }
        }
    }

    /**
     * Method getValidChoice()
     * Continuously prompts the user until a
     * valid number between 1 and 4 is entered.
     * @return choice
     */
    private int getValidChoice() {
        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();

            if (isNumeric(input)) {
                int choice = Integer.parseInt(input);

                if (choice >= 1 && choice <= 4) {
                    return choice;
                } else {
                    System.out.println("Please enter a number between 1 and 4.");
                }

            } else {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    /**
     * Method isNumeric()
     * Used in getValidChoice() to validate user input
     * @param str the user's input
     * @return true if string is a digit, false otherwise
     */
    private boolean isNumeric(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }

        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }

        return true;
    }

    private void showPantry() {
        System.out.println("Pantry:");
        for (String i : pantryService.getPantry()) {
            System.out.println("- " + i);
        }
    }

    /**
     * Method addIngredients()
     * Displays all ingredients in the pantry
     */
    private void addIngredient() {
        System.out.print("Ingredient: ");
        String line = scanner.nextLine();
        pantryService.addIngredient(line);
    }

    /**
     * Method recommend()
     * Ranks all meals against the pantry and displays
     * the top recommendations based on score.
     */
    private void recommend() {

        List<Recommendation> ranked =
                matcher.rankMeals(meals, pantryService.getPantry());

        System.out.println("Top Recommendations:");

        // displays the top 5 recommendations
        for (int i = 0; i < Math.min(5, ranked.size()); i++) {
            Recommendation r = ranked.get(i);

            System.out.println("\n#" + (i + 1));
            System.out.println("Dish: " + r.getMeal().getName());
            System.out.println("Match: " + Math.round(r.getScore() * 100) + "%");
            System.out.println("Prep Time: " + r.getMeal().getPrepTime() + " mins");
            System.out.println("Missing: " + r.getMissingIngredients());
        }
    }

    /**
     * Method exitApp()
     * Saves the pantry list and exits the application
     * @return false to exit
     */
    private boolean exitApp() {
        storage.savePantry(pantryService.getPantry());
        System.out.println("Goodbye! Your pantry has been saved.");

        // sets running in start() to false to exit app
        return false;
    }
}