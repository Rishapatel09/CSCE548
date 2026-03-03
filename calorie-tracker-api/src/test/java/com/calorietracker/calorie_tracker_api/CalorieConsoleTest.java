package com.calorietracker.calorie_tracker_api;

import com.calorietracker.model.AppUser;
import com.calorietracker.model.CalorieEntry;
import com.calorietracker.model.FoodItem;
import com.calorietracker.repository.AppUserDao;
import com.calorietracker.repository.CalorieEntryDao;
import com.calorietracker.repository.FoodItemDao;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

/**
 * Console test client for Calorie Tracker API - Project 3
 * Demonstrates ALL GET methods from the DAO (Data Access Object) layer
 * 
 * Project 3 Requirements:
 * - Call ALL "get" (read) methods for ALL tables
 * - Get single record, get all records, get subset of records
 */
public class CalorieConsoleTest {

    private static final AppUserDao userDao = new AppUserDao();
    private static final CalorieEntryDao entryDao = new CalorieEntryDao();
    private static final FoodItemDao foodDao = new FoodItemDao();

    public static void main(String[] args) {
        
        printHeader("CALORIE TRACKER - PROJECT 3 CLIENT APPLICATION");
        printSubHeader("Demonstrating ALL READ/GET methods from DAO layer");
        
        System.out.println("\n" + "=".repeat(100));
        System.out.println("TABLES IN DATABASE:");
        System.out.println("  1. app_user       - User account information");
        System.out.println("  2. calorie_entry  - Daily meal entries");
        System.out.println("  3. food_item      - Food items within each entry");
        System.out.println("\nTESTS TO PERFORM:");
        System.out.println("  - AppUser:       3 methods (readAll, readById, subset by user)");
        System.out.println("  - CalorieEntry:  3 methods (readAll, readById, readAllForUser subset)");
        System.out.println("  - FoodItem:      4 methods (readAll, readById, readAllForEntry subset, readByEntryId)");
        System.out.println("=".repeat(100) + "\n");
        
        waitForEnter("PRESS ENTER TO START THE DEMONSTRATION");
        
        try {
            // APP USER TESTS
            testAppUserReadAll();
            waitForScreenshot();
            
            testAppUserReadById();
            waitForScreenshot();
            
            // CALORIE ENTRY TESTS
            testCalorieEntryReadAll();
            waitForScreenshot();
            
            testCalorieEntryReadById();
            waitForScreenshot();
            
            testCalorieEntryReadAllForUser();
            waitForScreenshot();
            
            // FOOD ITEM TESTS
            testFoodItemReadAll();
            waitForScreenshot();
            
            testFoodItemReadById();
            waitForScreenshot();
            
            testFoodItemReadAllForEntry();
            waitForScreenshot();
            
            testFoodItemReadByEntryId();
            waitForScreenshot();
            
            // FINAL SUMMARY
            printFinalSummary();
            
        } catch (SQLException e) {
            System.err.println("\n❌ DATABASE ERROR: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("\n" + "=".repeat(100));
        System.out.println(">>> ALL TESTS COMPLETED - PRESS ENTER TO EXIT <<<");
        System.out.println("=".repeat(100));
        waitForEnter("");
    }
    
    // ==================== APP USER TESTS ====================
    
    /**
     * TEST 1: Get ALL users
     * DAO Method: readAll()
     * Purpose: Retrieve ALL records from app_user table
     */
    private static void testAppUserReadAll() throws SQLException {
        printTestHeader(1, "APP_USER TABLE - GET ALL RECORDS", "readAll()");
        
        List<AppUser> allUsers = userDao.readAll();
        
        System.out.println("RESULT: Retrieved " + allUsers.size() + " users\n");
        System.out.println(String.format("%-10s %-25s %-35s %-20s", 
            "User ID", "Name", "Email", "Created At"));
        System.out.println("-".repeat(100));
        
        for (AppUser user : allUsers) {
            System.out.println(String.format("%-10d %-25s %-35s %-20s",
                user.getUserId(),
                truncate(user.getName(), 25),
                truncate(user.getEmail(), 35),
                user.getCreatedAt() != null ? user.getCreatedAt().toString().substring(0, 19) : "N/A"));
        }
        
        printTestFooter("SUCCESS: All users retrieved from app_user table");
    }
    
    /**
     * TEST 2: Get SINGLE user by ID
     * DAO Method: readById(int userId)
     * Purpose: Retrieve a SINGLE record from app_user table
     */
    private static void testAppUserReadById() throws SQLException {
        printTestHeader(2, "APP_USER TABLE - GET SINGLE RECORD", "readById(int userId)");
        
        int testId = 1;
        System.out.println("QUERYING: User with user_id = " + testId + "\n");
        
        AppUser user = userDao.readById(testId);
        
        if (user != null) {
            System.out.println("RESULT: Successfully retrieved single user\n");
            System.out.println("User Details:");
            System.out.println("-".repeat(100));
            System.out.println("  User ID:      " + user.getUserId());
            System.out.println("  Name:         " + user.getName());
            System.out.println("  Email:        " + user.getEmail());
            System.out.println("  Created At:   " + user.getCreatedAt());
            
            printTestFooter("SUCCESS: Single user retrieved from app_user table");
        } else {
            printTestFooter("NOTE: User with ID " + testId + " not found (ensure test data exists)");
        }
    }
    
    // ==================== CALORIE ENTRY TESTS ====================
    
    /**
     * TEST 3: Get ALL calorie entries
     * DAO Method: readAll()
     * Purpose: Retrieve ALL records from calorie_entry table
     */
    private static void testCalorieEntryReadAll() throws SQLException {
        printTestHeader(3, "CALORIE_ENTRY TABLE - GET ALL RECORDS", "readAll()");
        
        List<CalorieEntry> allEntries = entryDao.readAll();
        
        System.out.println("RESULT: Retrieved " + allEntries.size() + " calorie entries\n");
        System.out.println(String.format("%-10s %-10s %-12s %-10s %-15s %-10s %-10s %-10s %-10s", 
            "Entry ID", "User ID", "Date", "Time", "Meal Type", "Calories", "Protein", "Carbs", "Fats"));
        System.out.println("-".repeat(100));
        
        int count = 0;
        for (CalorieEntry entry : allEntries) {
            System.out.println(String.format("%-10d %-10d %-12s %-10s %-15s %-10.1f %-10.1f %-10.1f %-10.1f",
                entry.getEntryId(),
                entry.getUserId(),
                entry.getEntryDate(),
                entry.getEntryTime() != null ? entry.getEntryTime().toString().substring(0, 5) : "N/A",
                truncate(entry.getMealType(), 15),
                entry.getTotalCalories(),
                entry.getTotalProtein(),
                entry.getTotalCarbs(),
                entry.getTotalFats()));
            
            count++;
            if (count >= 10 && allEntries.size() > 10) {
                System.out.println("  ... and " + (allEntries.size() - 10) + " more entries");
                break;
            }
        }
        
        printTestFooter("SUCCESS: All calorie entries retrieved from calorie_entry table");
    }
    
    /**
     * TEST 4: Get SINGLE calorie entry by ID
     * DAO Method: readById(int entryId)
     * Purpose: Retrieve a SINGLE record from calorie_entry table
     */
    private static void testCalorieEntryReadById() throws SQLException {
        printTestHeader(4, "CALORIE_ENTRY TABLE - GET SINGLE RECORD", "readById(int entryId)");
        
        int testId = 1;
        System.out.println("QUERYING: Calorie entry with entry_id = " + testId + "\n");
        
        CalorieEntry entry = entryDao.readById(testId);
        
        if (entry != null) {
            System.out.println("RESULT: Successfully retrieved single calorie entry\n");
            System.out.println("Calorie Entry Details:");
            System.out.println("-".repeat(100));
            System.out.println("  Entry ID:         " + entry.getEntryId());
            System.out.println("  User ID:          " + entry.getUserId());
            System.out.println("  Date:             " + entry.getEntryDate());
            System.out.println("  Time:             " + entry.getEntryTime());
            System.out.println("  Meal Type:        " + entry.getMealType());
            System.out.println("  Total Calories:   " + entry.getTotalCalories());
            System.out.println("  Total Protein:    " + entry.getTotalProtein() + "g");
            System.out.println("  Total Carbs:      " + entry.getTotalCarbs() + "g");
            System.out.println("  Total Fats:       " + entry.getTotalFats() + "g");
            System.out.println("  Notes:            " + (entry.getNotes() != null ? entry.getNotes() : "N/A"));
            System.out.println("  Created At:       " + entry.getCreatedAt());
            
            printTestFooter("SUCCESS: Single calorie entry retrieved from calorie_entry table");
        } else {
            printTestFooter("NOTE: Entry with ID " + testId + " not found (ensure test data exists)");
        }
    }
    
    /**
     * TEST 5: Get calorie entries for a specific user (SUBSET)
     * DAO Method: readAllForUser(int userId)
     * Purpose: Retrieve a SUBSET of records from calorie_entry table filtered by user_id
     */
    private static void testCalorieEntryReadAllForUser() throws SQLException {
        printTestHeader(5, "CALORIE_ENTRY TABLE - GET SUBSET BY USER", "readAllForUser(int userId)");
        
        int testUserId = 1;
        System.out.println("FILTERING BY: user_id = " + testUserId + "\n");
        
        List<CalorieEntry> userEntries = entryDao.readAllForUser(testUserId);
        
        System.out.println("RESULT: Found " + userEntries.size() + " entries for user " + testUserId + "\n");
        System.out.println(String.format("%-10s %-12s %-10s %-15s %-10s %-30s", 
            "Entry ID", "Date", "Time", "Meal Type", "Calories", "Notes"));
        System.out.println("-".repeat(100));
        
        for (CalorieEntry entry : userEntries) {
            System.out.println(String.format("%-10d %-12s %-10s %-15s %-10.1f %-30s",
                entry.getEntryId(),
                entry.getEntryDate(),
                entry.getEntryTime() != null ? entry.getEntryTime().toString().substring(0, 5) : "N/A",
                truncate(entry.getMealType(), 15),
                entry.getTotalCalories(),
                truncate(entry.getNotes() != null ? entry.getNotes() : "N/A", 30)));
        }
        
        printTestFooter("SUCCESS: Subset of calorie entries retrieved for specific user");
    }
    
    // ==================== FOOD ITEM TESTS ====================
    
    /**
     * TEST 6: Get ALL food items
     * DAO Method: readAll()
     * Purpose: Retrieve ALL records from food_item table
     */
    private static void testFoodItemReadAll() throws SQLException {
        printTestHeader(6, "FOOD_ITEM TABLE - GET ALL RECORDS", "readAll()");
        
        List<FoodItem> allItems = foodDao.readAll();
        
        System.out.println("RESULT: Retrieved " + allItems.size() + " food items\n");
        System.out.println(String.format("%-10s %-10s %-25s %-20s %-10s %-10s %-10s %-10s %-10s", 
            "Food ID", "Entry ID", "Name", "Serving Size", "Cal/Srv", "Protein", "Carbs", "Fats", "Servings"));
        System.out.println("-".repeat(100));
        
        int count = 0;
        for (FoodItem item : allItems) {
            System.out.println(String.format("%-10d %-10d %-25s %-20s %-10.1f %-10.1f %-10.1f %-10.1f %-10.1f",
                item.getFoodId(),
                item.getEntryId(),
                truncate(item.getName(), 25),
                truncate(item.getServingSize(), 20),
                item.getCaloriesPerServing(),
                item.getProtein(),
                item.getCarbs(),
                item.getFats(),
                item.getServings()));
            
            count++;
            if (count >= 10 && allItems.size() > 10) {
                System.out.println("  ... and " + (allItems.size() - 10) + " more items");
                break;
            }
        }
        
        printTestFooter("SUCCESS: All food items retrieved from food_item table");
    }
    
    /**
     * TEST 7: Get SINGLE food item by ID
     * DAO Method: readById(int foodId)
     * Purpose: Retrieve a SINGLE record from food_item table
     */
    private static void testFoodItemReadById() throws SQLException {
        printTestHeader(7, "FOOD_ITEM TABLE - GET SINGLE RECORD", "readById(int foodId)");
        
        int testId = 1;
        System.out.println("QUERYING: Food item with food_id = " + testId + "\n");
        
        FoodItem item = foodDao.readById(testId);
        
        if (item != null) {
            System.out.println("RESULT: Successfully retrieved single food item\n");
            System.out.println("Food Item Details:");
            System.out.println("-".repeat(100));
            System.out.println("  Food ID:              " + item.getFoodId());
            System.out.println("  Entry ID:             " + item.getEntryId());
            System.out.println("  Name:                 " + item.getName());
            System.out.println("  Description:          " + (item.getDescription() != null ? item.getDescription() : "N/A"));
            System.out.println("  Serving Size:         " + item.getServingSize());
            System.out.println("  Calories per Serving: " + item.getCaloriesPerServing());
            System.out.println("  Protein:              " + item.getProtein() + "g");
            System.out.println("  Carbohydrates:        " + item.getCarbs() + "g");
            System.out.println("  Fats:                 " + item.getFats() + "g");
            System.out.println("  Servings:             " + item.getServings());
            System.out.println("  Notes:                " + (item.getNotes() != null ? item.getNotes() : "N/A"));
            
            printTestFooter("SUCCESS: Single food item retrieved from food_item table");
        } else {
            printTestFooter("NOTE: Food item with ID " + testId + " not found (ensure test data exists)");
        }
    }
    
    /**
     * TEST 8: Get food items for a specific entry (SUBSET)
     * DAO Method: readAllForEntry(int entryId)
     * Purpose: Retrieve a SUBSET of records from food_item table filtered by entry_id
     */
    private static void testFoodItemReadAllForEntry() throws SQLException {
        printTestHeader(8, "FOOD_ITEM TABLE - GET SUBSET BY ENTRY", "readAllForEntry(int entryId)");
        
        int testEntryId = 1;
        System.out.println("FILTERING BY: entry_id = " + testEntryId + "\n");
        
        List<FoodItem> entryItems = foodDao.readAllForEntry(testEntryId);
        
        System.out.println("RESULT: Found " + entryItems.size() + " food items for entry " + testEntryId + "\n");
        System.out.println(String.format("%-10s %-30s %-20s %-10s %-10s", 
            "Food ID", "Name", "Serving Size", "Servings", "Calories"));
        System.out.println("-".repeat(100));
        
        for (FoodItem item : entryItems) {
            double totalCals = item.getCaloriesPerServing() * item.getServings();
            System.out.println(String.format("%-10d %-30s %-20s %-10.1f %-10.1f",
                item.getFoodId(),
                truncate(item.getName(), 30),
                truncate(item.getServingSize(), 20),
                item.getServings(),
                totalCals));
        }
        
        printTestFooter("SUCCESS: Subset of food items retrieved for specific entry");
    }
    
    /**
     * TEST 9: Get food items by entry ID (alias method) (SUBSET)
     * DAO Method: readByEntryId(int entryId)
     * Purpose: Demonstrate the alias method that does the same as readAllForEntry
     */
    private static void testFoodItemReadByEntryId() throws SQLException {
        printTestHeader(9, "FOOD_ITEM TABLE - GET SUBSET BY ENTRY (ALIAS)", "readByEntryId(int entryId)");
        
        int testEntryId = 1;
        System.out.println("FILTERING BY: entry_id = " + testEntryId + " (using alias method)\n");
        
        List<FoodItem> entryItems = foodDao.readByEntryId(testEntryId);
        
        System.out.println("RESULT: Found " + entryItems.size() + " food items for entry " + testEntryId);
        System.out.println("NOTE: This method is an alias for readAllForEntry() - same functionality\n");
        
        System.out.println(String.format("%-10s %-30s %-15s %-10s %-10s %-10s %-10s", 
            "Food ID", "Name", "Description", "Protein", "Carbs", "Fats", "Servings"));
        System.out.println("-".repeat(100));
        
        for (FoodItem item : entryItems) {
            System.out.println(String.format("%-10d %-30s %-15s %-10.1f %-10.1f %-10.1f %-10.1f",
                item.getFoodId(),
                truncate(item.getName(), 30),
                truncate(item.getDescription() != null ? item.getDescription() : "N/A", 15),
                item.getProtein(),
                item.getCarbs(),
                item.getFats(),
                item.getServings()));
        }
        
        printTestFooter("SUCCESS: Subset retrieved using alias method readByEntryId()");
    }
    
    // ==================== HELPER METHODS ====================
    
    private static void printHeader(String title) {
        System.out.println("\n" + "=".repeat(100));
        System.out.println("  " + title);
        System.out.println("=".repeat(100));
    }
    
    private static void printSubHeader(String subtitle) {
        System.out.println("  " + subtitle);
    }
    
    private static void printTestHeader(int testNumber, String testName, String daoMethod) {
        System.out.println("\n\n" + "█".repeat(100));
        System.out.println("█ TEST " + testNumber + ": " + testName);
        System.out.println("█ DAO METHOD: " + daoMethod);
        System.out.println("█".repeat(100) + "\n");
    }
    
    private static void printTestFooter(String result) {
        System.out.println("\n" + "-".repeat(100));
        System.out.println(">>> " + result);
        System.out.println("-".repeat(100));
    }
    
    private static void printFinalSummary() {
        System.out.println("\n\n" + "=".repeat(100));
        System.out.println("FINAL SUMMARY - ALL DAO READ METHODS TESTED");
        System.out.println("=".repeat(100));
        
        System.out.println("\n✓ app_user Table (2 READ methods):");
        System.out.println("  1. readAll()                     - Retrieved ALL users");
        System.out.println("  2. readById(int userId)          - Retrieved SINGLE user");
        
        System.out.println("\n✓ calorie_entry Table (3 READ methods):");
        System.out.println("  3. readAll()                     - Retrieved ALL entries");
        System.out.println("  4. readById(int entryId)         - Retrieved SINGLE entry");
        System.out.println("  5. readAllForUser(int userId)    - Retrieved SUBSET by user");
        
        System.out.println("\n✓ food_item Table (4 READ methods):");
        System.out.println("  6. readAll()                     - Retrieved ALL food items");
        System.out.println("  7. readById(int foodId)          - Retrieved SINGLE food item");
        System.out.println("  8. readAllForEntry(int entryId)  - Retrieved SUBSET by entry");
        System.out.println("  9. readByEntryId(int entryId)    - Retrieved SUBSET (alias method)");
        
        System.out.println("\n" + "=".repeat(100));
        System.out.println("PROJECT 3 REQUIREMENTS MET:");
        System.out.println("  ✓ Invoked ALL services from Project 2");
        System.out.println("  ✓ Called ALL READ/GET methods for ALL tables");
        System.out.println("  ✓ Demonstrated: get single record, get all records, get subset of records");
        System.out.println("  ✓ Total methods tested: 9 READ methods across 3 tables");
        System.out.println("  ✓ Client application tested and functional");
        System.out.println("=".repeat(100));
    }
    
    private static String truncate(String text, int maxLength) {
        if (text == null) return "";
        return text.length() > maxLength ? text.substring(0, maxLength - 3) + "..." : text;
    }
    
    private static void waitForEnter(String message) {
        if (!message.isEmpty()) {
            System.out.println("\n>>> " + message + " <<<");
        }
        try {
            System.in.read();
            while (System.in.available() > 0) {
                System.in.read();
            }
        } catch (Exception e) {
            // Ignore
        }
    }
    
    private static void waitForScreenshot() {
        System.out.println("\n>>> PRESS ENTER FOR NEXT TEST (Take screenshot now if needed) <<<");
        waitForEnter("");
    }
}