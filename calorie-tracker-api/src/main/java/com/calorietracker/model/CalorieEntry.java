package com.calorietracker.model;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

public class CalorieEntry {
    private int entryId;
    private int userId;
    private Date entryDate;
    private Time entryTime;
    private String mealType;
    private double totalCalories, totalProtein, totalCarbs, totalFats;
    private String notes;
    private Timestamp createdAt, updatedAt;

    public CalorieEntry() {}

    public int getEntryId() { return entryId; }
    public void setEntryId(int entryId) { this.entryId = entryId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public Date getEntryDate() { return entryDate; }
    public void setEntryDate(Date entryDate) { this.entryDate = entryDate; }

    public Time getEntryTime() { return entryTime; }
    public void setEntryTime(Time entryTime) { this.entryTime = entryTime; }

    public String getMealType() { return mealType; }
    public void setMealType(String mealType) { this.mealType = mealType; }

    public double getTotalCalories() { return totalCalories; }
    public void setTotalCalories(double totalCalories) { this.totalCalories = totalCalories; }

    public double getTotalProtein() { return totalProtein; }
    public void setTotalProtein(double totalProtein) { this.totalProtein = totalProtein; }

    public double getTotalCarbs() { return totalCarbs; }
    public void setTotalCarbs(double totalCarbs) { this.totalCarbs = totalCarbs; }

    public double getTotalFats() { return totalFats; }
    public void setTotalFats(double totalFats) { this.totalFats = totalFats; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public String toString() {
        return "Entry #" + entryId + " (user " + userId + ") | " +
                entryDate + " " + entryTime + " | " + mealType +
                " | cal=" + totalCalories + " P=" + totalProtein +
                " C=" + totalCarbs + " F=" + totalFats +
                " | " + notes;
    }
}
