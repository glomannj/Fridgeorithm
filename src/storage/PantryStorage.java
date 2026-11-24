/**
 * Nathan Gloman
 * 5/28/26
 * PantryStorage.java
 * Handles the reading a writing to pantry.txt
 */
package storage;

import java.util.Set;
import java.util.HashSet;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileWriter;

public class PantryStorage {

    private static final String FILE = "pantry.txt";

    /**
     * Method loadPantry()
     * Reads pantry.txt and returns as a Set of ingredients.
     * Returns an empty set if the file does not exist
     * @return pantry
     */
    public Set<String> loadPantry() {
        Set<String> pantry = new HashSet<>();

        // opens the file
        try (BufferedReader br = new BufferedReader(new FileReader(FILE))) {
            String line;

            // reads each line from the file and adds it to the pantry list
            while ((line = br.readLine()) != null) {
                pantry.add(line.trim().toLowerCase());
            }

        } catch (IOException e) {
            System.out.println("Error saving pantry list: ");
        }

        return pantry;
    }

    /**
     * Method savePantry()
     * Writes the user's pantry to pantry.txt.
     * Overwrites existing data.
     * Called automatically when application is terminated.
     * @param pantry the user's pantry
     */
    public void savePantry(Set<String> pantry) {
        // opens the file for writing
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE))) {

            // writes each ingredient on a new line
            for (String item : pantry) {
                pw.println(item);
            }
        } catch (IOException e) {
            System.out.println("Error saving pantry: " + e.getMessage());
        }
    }
}