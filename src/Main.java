import java.sql.Date;
import java.sql.Time;

public class Main {
    public static void main(String[] args) throws Exception {

        AppUserDao userDao = new AppUserDao();
        CalorieEntryDao entryDao = new CalorieEntryDao();
        FoodItemDao foodDao = new FoodItemDao();

        System.out.println("=== USERS ===");
        for (AppUser u : userDao.readAll()) System.out.println(u);

        // --- CREATE USER ---
        AppUser newUser = new AppUser();
        newUser.setName("Console User");
        newUser.setEmail("console_user_" + System.currentTimeMillis() + "@example.com");
        int newUserId = userDao.create(newUser);
        System.out.println("\nCreated user_id = " + newUserId);

        // --- CREATE ENTRY (requires user_id) ---
        CalorieEntry e = new CalorieEntry();
        e.setUserId(newUserId);
        e.setEntryDate(Date.valueOf("2024-02-10"));
        e.setEntryTime(Time.valueOf("08:30:00"));
        e.setMealType("Breakfast");
        e.setTotalCalories(500);
        e.setTotalProtein(30);
        e.setTotalCarbs(50);
        e.setTotalFats(15);
        e.setNotes("Console create entry");
        int entryId = entryDao.create(e);
        System.out.println("Created entry_id = " + entryId);

        // --- CREATE FOOD (requires entry_id) ---
        FoodItem f = new FoodItem();
        f.setEntryId(entryId);
        f.setName("Oatmeal");
        f.setDescription("Rolled oats");
        f.setServingSize("1 bowl");
        f.setCaloriesPerServing(250);
        f.setProtein(10);
        f.setCarbs(45);
        f.setFats(5);
        f.setServings(1.0);
        f.setNotes("Console create food");
        int foodId = foodDao.create(f);
        System.out.println("Created food_id = " + foodId);

        // --- READ ---
        System.out.println("\nREAD ENTRY:");
        System.out.println(entryDao.readById(entryId));

        System.out.println("\nFOODS FOR ENTRY " + entryId + ":");
        for (FoodItem fi : foodDao.readAllForEntry(entryId)) System.out.println(fi);

        // --- UPDATE ---
        CalorieEntry updateEntry = entryDao.readById(entryId);
        updateEntry.setNotes("Updated note from console");
        boolean updated = entryDao.update(updateEntry);
        System.out.println("\nUpdated entry? " + updated);

        // --- DELETE food then entry then user (safe order) ---
        boolean delFood = foodDao.delete(foodId);
        System.out.println("Deleted food? " + delFood);

        boolean delEntry = entryDao.delete(entryId);
        System.out.println("Deleted entry? " + delEntry);

        boolean delUser = userDao.delete(newUserId);
        System.out.println("Deleted user? " + delUser);

        System.out.println("\nDONE.");
    }
}
