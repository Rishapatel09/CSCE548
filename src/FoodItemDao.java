import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FoodItemDao {

    public int create(FoodItem f) throws SQLException {
        String sql = """
            INSERT INTO food_item
            (entry_id, name, description, serving_size, calories_per_serving, protein, carbs, fats, servings, notes)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, f.getEntryId());
            ps.setString(2, f.getName());
            ps.setString(3, f.getDescription());
            ps.setString(4, f.getServingSize());
            ps.setDouble(5, f.getCaloriesPerServing());
            ps.setDouble(6, f.getProtein());
            ps.setDouble(7, f.getCarbs());
            ps.setDouble(8, f.getFats());
            ps.setDouble(9, f.getServings());
            ps.setString(10, f.getNotes());

            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }

    public FoodItem readById(int foodId) throws SQLException {
        String sql = "SELECT * FROM food_item WHERE food_id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, foodId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        }
        return null;
    }

    public List<FoodItem> readAll() throws SQLException {
        List<FoodItem> list = new ArrayList<>();
        String sql = "SELECT * FROM food_item ORDER BY food_id";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    public List<FoodItem> readAllForEntry(int entryId) throws SQLException {
        List<FoodItem> list = new ArrayList<>();
        String sql = "SELECT * FROM food_item WHERE entry_id=? ORDER BY food_id";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, entryId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        }
        return list;
    }

    public boolean update(FoodItem f) throws SQLException {
        String sql = """
            UPDATE food_item
            SET entry_id=?, name=?, description=?, serving_size=?,
                calories_per_serving=?, protein=?, carbs=?, fats=?, servings=?, notes=?
            WHERE food_id=?
            """;
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, f.getEntryId());
            ps.setString(2, f.getName());
            ps.setString(3, f.getDescription());
            ps.setString(4, f.getServingSize());
            ps.setDouble(5, f.getCaloriesPerServing());
            ps.setDouble(6, f.getProtein());
            ps.setDouble(7, f.getCarbs());
            ps.setDouble(8, f.getFats());
            ps.setDouble(9, f.getServings());
            ps.setString(10, f.getNotes());
            ps.setInt(11, f.getFoodId());

            return ps.executeUpdate() == 1;
        }
    }

    public boolean delete(int foodId) throws SQLException {
        String sql = "DELETE FROM food_item WHERE food_id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, foodId);
            return ps.executeUpdate() == 1;
        }
    }

    private FoodItem map(ResultSet rs) throws SQLException {
        FoodItem f = new FoodItem();
        f.setFoodId(rs.getInt("food_id"));
        f.setEntryId(rs.getInt("entry_id"));
        f.setName(rs.getString("name"));
        f.setDescription(rs.getString("description"));
        f.setServingSize(rs.getString("serving_size"));
        f.setCaloriesPerServing(rs.getDouble("calories_per_serving"));
        f.setProtein(rs.getDouble("protein"));
        f.setCarbs(rs.getDouble("carbs"));
        f.setFats(rs.getDouble("fats"));
        f.setServings(rs.getDouble("servings"));
        f.setNotes(rs.getString("notes"));
        return f;
    }
}
