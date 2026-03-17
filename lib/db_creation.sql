CREATE DATABASE IF NOT EXISTS calorie_tracker;
USE calorie_tracker;

SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS food_item;
DROP TABLE IF EXISTS calorie_entry;
DROP TABLE IF EXISTS app_user;
SET FOREIGN_KEY_CHECKS = 1;

-- 1) USERS
CREATE TABLE app_user (
  user_id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  email VARCHAR(150) UNIQUE NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- 2) CALORIE ENTRIES (belongs to a user)
CREATE TABLE calorie_entry (
  entry_id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT NOT NULL,
  entry_date DATE NOT NULL,
  entry_time TIME NOT NULL,
  meal_type VARCHAR(50) NOT NULL,

  total_calories DECIMAL(10,2) NOT NULL,
  total_protein  DECIMAL(10,2) NOT NULL,
  total_carbs    DECIMAL(10,2) NOT NULL,
  total_fats     DECIMAL(10,2) NOT NULL,

  notes TEXT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

  CONSTRAINT fk_entry_user
    FOREIGN KEY (user_id)
    REFERENCES app_user(user_id)
    ON DELETE CASCADE
) ENGINE=InnoDB;

-- 3) FOOD ITEMS (belongs to an entry)
CREATE TABLE food_item (
  food_id INT AUTO_INCREMENT PRIMARY KEY,
  entry_id INT NOT NULL,

  name VARCHAR(100) NOT NULL,
  description VARCHAR(255),
  serving_size VARCHAR(60) NOT NULL,

  calories_per_serving DECIMAL(10,2) NOT NULL,
  protein DECIMAL(10,2) NOT NULL,
  carbs   DECIMAL(10,2) NOT NULL,
  fats    DECIMAL(10,2) NOT NULL,

  servings DECIMAL(10,2) NOT NULL DEFAULT 1.00,
  notes VARCHAR(255),

  CONSTRAINT fk_food_entry
    FOREIGN KEY (entry_id)
    REFERENCES calorie_entry(entry_id)
    ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE INDEX idx_entry_user_date ON calorie_entry(user_id, entry_date);
CREATE INDEX idx_food_entry ON food_item(entry_id);
