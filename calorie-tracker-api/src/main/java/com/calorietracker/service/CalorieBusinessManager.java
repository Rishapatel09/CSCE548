package com.calorietracker.service;

import com.calorietracker.model.AppUser;
import com.calorietracker.model.CalorieEntry;
import com.calorietracker.model.FoodItem;
import com.calorietracker.repository.AppUserDao;
import com.calorietracker.repository.CalorieEntryDao;
import com.calorietracker.repository.FoodItemDao;

import java.sql.SQLException;
import java.util.List;
/**
 * BUSINESS LAYER
 *
 * This class sits between:
 *   Console/Test (or future API)
 *         ↓
 *   BusinessManager (THIS FILE)
 *         ↓
 *   DAO Layer (Project 1)
 *
 * All DAO CRUD methods must be available here.
 * Business rules/validation belong here.
 */
public class CalorieBusinessManager {

    private final AppUserDao userDao;
    private final CalorieEntryDao entryDao;
    private final FoodItemDao foodDao;

    public CalorieBusinessManager() {
        this.userDao = new AppUserDao();
        this.entryDao = new CalorieEntryDao();
        this.foodDao = new FoodItemDao();
    }

    // =========================
    // USER METHODS
    // =========================

    public int createUser(AppUser user) throws Exception {
        if (user == null)
            throw new IllegalArgumentException("User cannot be null");

        if (user.getEmail() == null || user.getEmail().isBlank())
            throw new IllegalArgumentException("Email is required");

        return userDao.create(user);
    }

    public AppUser getUserById(int userId) throws Exception {
        return userDao.readById(userId);
    }

    public List<AppUser> getAllUsers() throws Exception {
        return userDao.readAll();
    }

    public boolean updateUser(AppUser user) throws Exception {
        return userDao.update(user);
    }

    public boolean deleteUser(int userId) throws Exception {
        return userDao.delete(userId);
    }


    // =========================
    // CALORIE ENTRY METHODS
    // =========================

    public int createEntry(CalorieEntry entry) throws Exception {
        if (entry == null)
            throw new IllegalArgumentException("Entry cannot be null");

        if (entry.getUserId() <= 0)
            throw new IllegalArgumentException("Valid userId required");

        return entryDao.create(entry);
    }

    public CalorieEntry getEntryById(int entryId) throws Exception {
        return entryDao.readById(entryId);
    }

    public List<CalorieEntry> getAllEntries() throws Exception {
        return entryDao.readAll();
    }

    public List<CalorieEntry> getEntriesByUser(int userId) throws Exception {
        return entryDao.readAllForUser(userId);
    }

    public boolean updateEntry(CalorieEntry entry) throws Exception {
        return entryDao.update(entry);
    }

    public boolean deleteEntry(int entryId) throws Exception {
        return entryDao.delete(entryId);
    }


 // ============================
// FOOD ITEM METHODS
// ============================

public int createFoodItem(FoodItem item) throws Exception {
    return foodDao.create(item);
}

public FoodItem getFoodItemById(int foodId) throws Exception {
    return foodDao.readById(foodId);
}

public List<FoodItem> getAllFoodItems() throws Exception {
    return foodDao.readAll();
}

public List<FoodItem> getFoodItemsByEntry(int entryId) throws Exception {
    return foodDao.readAllForEntry(entryId);   // <-- correct call
    // OR: return foodDao.readByEntryId(entryId);
}

public boolean updateFoodItem(FoodItem item) throws Exception {
    return foodDao.update(item);
}

public boolean deleteFoodItem(int foodId) throws Exception {
    return foodDao.delete(foodId);
}
}