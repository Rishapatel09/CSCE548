package com.calorietracker.controller;

import com.calorietracker.model.CalorieEntry;
import com.calorietracker.service.CalorieBusinessManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class CalorieEntryController {

    private final CalorieBusinessManager manager = new CalorieBusinessManager();

    // GET http://localhost:8080/entries
    @GetMapping("/entries")
    public ResponseEntity<List<CalorieEntry>> getAllEntries() throws Exception {
        return ResponseEntity.ok(manager.getAllEntries());
    }

    // GET http://localhost:8080/entries/{id}
    @GetMapping("/entries/{id}")
    public ResponseEntity<CalorieEntry> getEntryById(@PathVariable int id) throws Exception {
        CalorieEntry entry = manager.getEntryById(id);
        if (entry == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(entry);
    }

    // GET http://localhost:8080/users/{userId}/entries
    @GetMapping("/users/{userId}/entries")
    public ResponseEntity<List<CalorieEntry>> getEntriesByUser(@PathVariable int userId) throws Exception {
        return ResponseEntity.ok(manager.getEntriesByUser(userId));
    }

    // POST http://localhost:8080/entries
    @PostMapping("/entries")
    public ResponseEntity<CalorieEntry> createEntry(@RequestBody CalorieEntry entry) throws Exception {
        int newId = manager.createEntry(entry); // make sure this exists in manager
        entry.setEntryId(newId); // adjust if your id field name differs
        return ResponseEntity.status(HttpStatus.CREATED).body(entry);
    }

    // PUT http://localhost:8080/entries/{id}
    @PutMapping("/entries/{id}")
    public ResponseEntity<String> updateEntry(@PathVariable int id, @RequestBody CalorieEntry entry) throws Exception {
        entry.setEntryId(id);
        boolean ok = manager.updateEntry(entry);
        if (!ok) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Entry not found");
        return ResponseEntity.ok("Entry updated");
    }

    // DELETE http://localhost:8080/entries/{id}
    @DeleteMapping("/entries/{id}")
    public ResponseEntity<String> deleteEntry(@PathVariable int id) throws Exception {
        boolean ok = manager.deleteEntry(id);
        if (!ok) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Entry not found");
        return ResponseEntity.ok("Entry deleted");
    }
}