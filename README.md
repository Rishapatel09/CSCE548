# Calorie Tracker

A full-stack web application for tracking daily calorie intake. Users can log meals, track nutritional data, and manage food items.

## Tech Stack

| Layer | Technology | Port |
|-------|-----------|------|
| Database | MySQL 8.x | 3306 |
| Backend API | Spring Boot 4 / Java 17 | 8080 |
| Frontend | HTML + CSS + JavaScript | (open in browser) |

## Setup

### 1. Clone the repo
```bash
git clone https://github.com/Rishapatel09/CSCE548.git
cd CSCE548
```

### 2. Set up the database
```bash
mysql -u root -p < db_creation.sql
mysql -u root -p < db_inserts.sql
```

### 3. Set your MySQL password

Open `calorie-tracker-api/src/main/java/com/calorietracker/repository/DBUtil.java` and update:
```java
private static final String PASS = "your_mysql_password";
```

### 4. Run the backend
```bash
cd calorie-tracker-api
gradlew.bat bootRun      # Windows
./gradlew bootRun        # Mac / Linux
```

Verify it's running: http://localhost:8080/users

### 5. Open the frontend

Open `Frontend/index.html` in your browser. The app connects to `http://localhost:8080` by default.

## Features

- **Users** — create, view, update, and delete user accounts
- **Calorie Entries** — log meals with date, time, meal type, and macros (calories, protein, carbs, fats)
- **Food Items** — add individual food items to each meal entry with serving info and nutritional data

## API Endpoints

### Users
| Method | Endpoint | Action |
|--------|----------|--------|
| GET | `/users` | Get all users |
| GET | `/users/{id}` | Get user by ID |
| POST | `/users` | Create user |
| PUT | `/users/{id}` | Update user |
| DELETE | `/users/{id}` | Delete user |

### Calorie Entries
| Method | Endpoint | Action |
|--------|----------|--------|
| GET | `/entries` | Get all entries |
| GET | `/entries/{id}` | Get entry by ID |
| GET | `/users/{id}/entries` | Get entries for a user |
| POST | `/entries` | Create entry |
| PUT | `/entries/{id}` | Update entry |
| DELETE | `/entries/{id}` | Delete entry |

### Food Items
| Method | Endpoint | Action |
|--------|----------|--------|
| GET | `/food-items` | Get all food items |
| GET | `/food-items/{id}` | Get food item by ID |
| GET | `/food-items/entry/{id}` | Get food items for an entry |
| POST | `/food-items` | Create food item |
| PUT | `/food-items/{id}` | Update food item |
| DELETE | `/food-items/{id}` | Delete food item |

## Database Schema

```
app_user        — user_id, name, email, created_at
calorie_entry   — entry_id, user_id, entry_date, entry_time, meal_type,
                  total_calories, total_protein, total_carbs, total_fats, notes
food_item       — food_id, entry_id, name, description, serving_size,
                  calories_per_serving, protein, carbs, fats, servings, notes
```

## Project Structure

```
CSCE548/
├── Frontend/
│   └── index.html              # Full UI — no build step needed
├── calorie-tracker-api/
│   └── src/main/java/com/calorietracker/
│       ├── controller/         # REST endpoints
│       ├── service/            # Business logic
│       ├── repository/         # Database access (DAO)
│       └── model/              # Data models
├── db_creation.sql             # Creates tables
├── db_inserts.sql              # Sample data
└── README.md
```

## Troubleshooting

**400 Bad Request on create/update** — Make sure `application.properties` contains:
```
spring.jackson.deserialization.fail-on-null-for-primitives=false
```

**MySQL access denied** — Check the password in `DBUtil.java`.

**Connection refused** — Make sure the backend is running on port 8080 before using the frontend.

**Permission denied on gradlew (Mac/Linux)** — Run `chmod +x gradlew` first.
