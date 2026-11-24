# 🍽️ Fridgorithm

**Smart meal recommendations based on what's already in your pantry.**

Fridgorithm is a Java application that helps users decide what to cook based on the ingredients they currently have available. Users can manage a personal pantry, browse meal recommendations, and see exactly which ingredients they're missing for each dish.

Originally developed as a Java class project, Fridgorithm demonstrates object-oriented programming, data structures, file I/O, JSON parsing, and GUI development using Java Swing.

<img width="1100" height="785" alt="Fridgeorithm Screenshot" src="https://github.com/user-attachments/assets/c281fb7c-ead8-4b13-babe-5a8e60c6479b" />


## ✨ Features

- **Pantry Management:** Add and remove ingredients from your personal pantry.
- **Smart Meal Recommendations:** Automatically ranks meals based on how many required ingredients you already have.
- **Match Percentages:** Each recommendation displays a percentage indicating how closely your pantry matches the recipe.
- **Missing Ingredients:** See which ingredients you need to complete each meal.
- **Preparation Times:** View estimated preparation times for recommended dishes.
- **Persistent Storage:** Your pantry is automatically saved to a text file when you exit and restored the next time you launch the application.
- **Graphical Interface:** Interactive Java Swing GUI with a custom dark-themed design.
- **JSON Meal Database:** Meals, ingredients, and preparation times are loaded from an external JSON file.

## 🖥️ Application Preview

The application features a two-panel layout:

- **Left Panel:** Displays your current pantry and allows you to add or remove ingredients.
- **Right Panel:** Displays ranked meal recommendations with match percentages, preparation times, and missing ingredients.

## ⚙️ How It Works

Fridgorithm compares the ingredients required for each meal against the ingredients stored in the user's pantry.

Each meal receives a match score calculated using the following formula:

**Match Score = (Available Ingredients / Total Required Ingredients) × 100**

Meals are then sorted from highest to lowest match percentage.

For example, if a recipe requires 5 ingredients and you already have 4, it receives an 80% match score.

The application displays the top 5 recommendations, including any ingredients that are missing.

## 🛠️ Technologies & Concepts

**Language & Libraries**
- Java
- Java Swing (GUI)
- JSON-Java (`org.json`)

**Programming Concepts**
- Object-Oriented Programming
- Encapsulation and Classes
- Java Collections Framework (`ArrayList`, `HashSet`, `Set`)
- File Input/Output
- Exception Handling
- JSON Parsing
- Sorting and Ranking Algorithms
- Event-Driven Programming

## 📂 Project Structure

```text
Fridgorithm/
│
├── src/
│   ├── Main.java
│   │
│   ├── model/
│   │   ├── Meal.java
│   │   └── Recommendation.java
│   │
│   ├── service/
│   │   ├── MealLoader.java
│   │   ├── MealMatcher.java
│   │   └── PantryService.java
│   │
│   ├── storage/
│   │   └── PantryStorage.java
│   │
│   └── ui/
│       ├── GUI.java
│       └── ConsoleUI.java
│
├── lib/
│   └── json-20240303.jar
│
├── mealsList.json
├── pantry.txt
└── README.md
```

### Class Responsibilities

| Class | Responsibility |
|---|---|
| `Main` | Application entry point; loads data and launches the GUI. |
| `Meal` | Represents a meal, its ingredients, and preparation time. |
| `Recommendation` | Stores a meal's match score and missing ingredients. |
| `MealLoader` | Loads meal data from the JSON database. |
| `MealMatcher` | Calculates match scores and ranks meals. |
| `PantryService` | Manages pantry ingredients. |
| `PantryStorage` | Handles saving and loading pantry data. |
| `GUI` | Provides the graphical user interface. |
| `ConsoleUI` | Original text-based interface. |

## 🚀 Getting Started

### Prerequisites

- Java Development Kit (JDK 17 or newer recommended).
- A Java IDE such as IntelliJ IDEA, or a terminal.
- The included JSON-Java library (`lib/json-20240303.jar`).

### Installation

1. Clone the repository:

   ```bash
   git clone https://github.com/YOUR-USERNAME/Fridgorithm.git
   ```

2. Navigate to the project directory:

   ```bash
   cd Fridgorithm
   ```

3. Make sure the JSON library is included in your project's classpath:

   ```text
   lib/json-20240303.jar
   ```

4. Compile the source files:

   **Windows:**
   ```bash
   javac -cp "lib/json-20240303.jar" -d out src/Main.java src/model/*.java src/service/*.java src/storage/*.java src/ui/*.java
   ```

   **macOS / Linux:**
   ```bash
   javac -cp "lib/json-20240303.jar" -d out src/Main.java src/model/*.java src/service/*.java src/storage/*.java src/ui/*.java
   ```

5. Run the application:

   **Windows:**
   ```bash
   java -cp "out;lib/json-20240303.jar" Main
   ```

   **macOS / Linux:**
   ```bash
   java -cp "out:lib/json-20240303.jar" Main
   ```

**Note:** Run the application from the project root directory so it can locate `mealsList.json` and `pantry.txt`.

## 📋 Future Improvements

Potential improvements for future versions include:

- Expanding the meal database.
- Adding the ability to create and edit custom recipes.
- Filtering recommendations by preparation time.
- Adding ingredient categories.
- Improving recommendation scoring with ingredient weighting.
- Integrating an external recipe API.
- Adding unit and integration tests.

## 🎓 Project Background

Fridgorithm was developed as part of a Java programming course project.

The project demonstrates practical applications of fundamental programming concepts, including object-oriented design, data structures, file management, and graphical user interface development.

The original implementation used a console-based interface before being expanded into a graphical Java Swing application.

---

**Developed by Nathan Gloman**
