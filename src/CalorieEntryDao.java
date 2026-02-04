import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CalorieEntryDao {

    public int create(CalorieEntry e) throws SQLException {
        String sql = """
            INSERT INTO calorie_entry
            (user_id, entry_date, entry_time, meal_type, total_calories, total_protein, total_carbs, total_fats, notes)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, e.getUserId());
            ps.setDate(2, e.getEntryDate());
            ps.setTime(3, e.getEntryTime());
            ps.setString(4, e.getMealType());
            ps.setDouble(5, e.getTotalCalories());
            ps.setDouble(6, e.getTotalProtein());
            ps.setDouble(7, e.getTotalCarbs());
            ps.setDouble(8, e.getTotalFats());
            ps.setString(9, e.getNotes());

            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }

    public CalorieEntry readById(int entryId) throws SQLException {
        String sql = "SELECT * FROM calorie_entry WHERE entry_id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, entryId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        }
        return null;
    }

    public List<CalorieEntry> readAll() throws SQLException {
        List<CalorieEntry> list = new ArrayList<>();
        String sql = "SELECT * FROM calorie_entry ORDER BY entry_id";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    public List<CalorieEntry> readAllForUser(int userId) throws SQLException {
        List<CalorieEntry> list = new ArrayList<>();
        String sql = "SELECT * FROM calorie_entry WHERE user_id=? ORDER BY entry_id";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        }
        return list;
    }

    public boolean update(CalorieEntry e) throws SQLException {
        String sql = """
            UPDATE calorie_entry
            SET user_id=?, entry_date=?, entry_time=?, meal_type=?,
                total_calories=?, total_protein=?, total_carbs=?, total_fats=?, notes=?
            WHERE entry_id=?
            """;
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, e.getUserId());
            ps.setDate(2, e.getEntryDate());
            ps.setTime(3, e.getEntryTime());
            ps.setString(4, e.getMealType());
            ps.setDouble(5, e.getTotalCalories());
            ps.setDouble(6, e.getTotalProtein());
            ps.setDouble(7, e.getTotalCarbs());
            ps.setDouble(8, e.getTotalFats());
            ps.setString(9, e.getNotes());
            ps.setInt(10, e.getEntryId());

            return ps.executeUpdate() == 1;
        }
    }

    public boolean delete(int entryId) throws SQLException {
        // food_item has ON DELETE CASCADE, so deleting entry deletes its foods
        String sql = "DELETE FROM calorie_entry WHERE entry_id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, entryId);
            return ps.executeUpdate() == 1;
        }
    }

    private CalorieEntry map(ResultSet rs) throws SQLException {
        CalorieEntry e = new CalorieEntry();
        e.setEntryId(rs.getInt("entry_id"));
        e.setUserId(rs.getInt("user_id"));
        e.setEntryDate(rs.getDate("entry_date"));
        e.setEntryTime(rs.getTime("entry_time"));
        e.setMealType(rs.getString("meal_type"));
        e.setTotalCalories(rs.getDouble("total_calories"));
        e.setTotalProtein(rs.getDouble("total_protein"));
        e.setTotalCarbs(rs.getDouble("total_carbs"));
        e.setTotalFats(rs.getDouble("total_fats"));
        e.setNotes(rs.getString("notes"));
        e.setCreatedAt(rs.getTimestamp("created_at"));
        e.setUpdatedAt(rs.getTimestamp("updated_at"));
        return e;
    }
}
