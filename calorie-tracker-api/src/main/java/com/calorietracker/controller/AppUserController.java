package com.calorietracker.controller;

import com.calorietracker.model.AppUser;
import com.calorietracker.service.CalorieBusinessManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class AppUserController {

    private final CalorieBusinessManager manager = new CalorieBusinessManager();

    // GET http://localhost:8080/users
    @GetMapping
    public ResponseEntity<List<AppUser>> getAllUsers() throws Exception {
        return ResponseEntity.ok(manager.getAllUsers());
    }

    // GET http://localhost:8080/users/{id}
    @GetMapping("/{id}")
    public ResponseEntity<AppUser> getUserById(@PathVariable int id) throws Exception {
        AppUser user = manager.getUserById(id); // make sure this exists in manager
        if (user == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(user);
    }

    // POST http://localhost:8080/users
    @PostMapping
    public ResponseEntity<AppUser> createUser(@RequestBody AppUser user) throws Exception {
        int newId = manager.createUser(user); // make sure this exists in manager
        user.setUserId(newId); // adjust if your field name differs (id/userId)
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    // PUT http://localhost:8080/users/{id}
    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable int id, @RequestBody AppUser user) throws Exception {
        user.setUserId(id); // adjust if needed
        boolean ok = manager.updateUser(user);
        if (!ok) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        return ResponseEntity.ok("User updated");
    }

    // DELETE http://localhost:8080/users/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable int id) throws Exception {
        boolean ok = manager.deleteUser(id);
        if (!ok) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        return ResponseEntity.ok("User deleted");
    }

    // quick test: GET http://localhost:8080/users/ping
    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }
}