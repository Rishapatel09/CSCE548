import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AppUserDao {

    public int create(AppUser u) throws SQLException {
        String sql = "INSERT INTO app_user (name, email) VALUES (?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, u.getName());
            ps.setString(2, u.getEmail());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }

    public AppUser readById(int userId) throws SQLException {
        String sql = "SELECT user_id, name, email, created_at FROM app_user WHERE user_id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new AppUser(
                            rs.getInt("user_id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getTimestamp("created_at")
                    );
                }
            }
        }
        return null;
    }

    public List<AppUser> readAll() throws SQLException {
        List<AppUser> list = new ArrayList<>();
        String sql = "SELECT user_id, name, email, created_at FROM app_user ORDER BY user_id";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new AppUser(
                        rs.getInt("user_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getTimestamp("created_at")
                ));
            }
        }
        return list;
    }

    public boolean update(AppUser u) throws SQLException {
        String sql = "UPDATE app_user SET name=?, email=? WHERE user_id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, u.getName());
            ps.setString(2, u.getEmail());
            ps.setInt(3, u.getUserId());
            return ps.executeUpdate() == 1;
        }
    }

    public boolean delete(int userId) throws SQLException {
        String sql = "DELETE FROM app_user WHERE user_id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            return ps.executeUpdate() == 1;
        }
    }
}
