package com.calorietracker.controller;

import com.calorietracker.model.FoodItem;
import com.calorietracker.service.CalorieBusinessManager;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/food-items")
public class FoodItemController {

    private final CalorieBusinessManager manager = new CalorieBusinessManager();

    @GetMapping
    public List<FoodItem> getAll() throws Exception {
        return manager.getAllFoodItems();
    }

    @GetMapping("/{id}")
    public FoodItem getById(@PathVariable int id) throws Exception {
        return manager.getFoodItemById(id);
    }

    @GetMapping("/entry/{entryId}")
    public List<FoodItem> getByEntry(@PathVariable int entryId) throws Exception {
        return manager.getFoodItemsByEntry(entryId);
    }

    @PostMapping
    public int create(@RequestBody FoodItem item) throws Exception {
        return manager.createFoodItem(item);
    }

    @PutMapping("/{id}")
    public boolean update(@PathVariable int id, @RequestBody FoodItem item) throws Exception {
        item.setFoodId(id);
        return manager.updateFoodItem(item);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable int id) throws Exception {
        return manager.deleteFoodItem(id);
    }
}