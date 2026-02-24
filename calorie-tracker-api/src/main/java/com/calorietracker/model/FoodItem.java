package com.calorietracker.model;

import java.sql.Timestamp;
public class FoodItem {
    private int foodId;
    private int entryId;
    private String name;
    private String description;
    private String servingSize;
    private double caloriesPerServing, protein, carbs, fats;
    private double servings;
    private String notes;

    public FoodItem() {}

    public int getFoodId() { return foodId; }
    public void setFoodId(int foodId) { this.foodId = foodId; }

    public int getEntryId() { return entryId; }
    public void setEntryId(int entryId) { this.entryId = entryId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getServingSize() { return servingSize; }
    public void setServingSize(String servingSize) { this.servingSize = servingSize; }

    public double getCaloriesPerServing() { return caloriesPerServing; }
    public void setCaloriesPerServing(double caloriesPerServing) { this.caloriesPerServing = caloriesPerServing; }

    public double getProtein() { return protein; }
    public void setProtein(double protein) { this.protein = protein; }

    public double getCarbs() { return carbs; }
    public void setCarbs(double carbs) { this.carbs = carbs; }

    public double getFats() { return fats; }
    public void setFats(double fats) { this.fats = fats; }

    public double getServings() { return servings; }
    public void setServings(double servings) { this.servings = servings; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    @Override
    public String toString() {
        return "Food #" + foodId + " (entry " + entryId + ") | " + name +
                " | " + servingSize + " | cal=" + caloriesPerServing +
                " P=" + protein + " C=" + carbs + " F=" + fats +
                " | servings=" + servings + " | " + notes;
    }
}
