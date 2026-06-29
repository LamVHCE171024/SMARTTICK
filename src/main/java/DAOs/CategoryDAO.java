package DAOs;

import DB.DBContext;
import Models.Category;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {
    private final DBContext db = new DBContext();

    private Connection connection() throws SQLException {
        Connection connection = db.getConnection();
        if (connection == null) throw new SQLException("Database connection unavailable");
        return connection;
    }

    public List<String> getAllCategoryNames() {
        List<String> names = new ArrayList<>();
        for (Category category : getAllCategories()) names.add(category.getName());
        return names;
    }

    public int getCategoryIdByName(String categoryName) {
        String sql = "SELECT CategoryID FROM Categories WHERE Name = ?";
        try (Connection connection = connection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, categoryName);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() ? rs.getInt(1) : -1;
            }
        } catch (SQLException ex) {
            return -1;
        }
    }

    public List<Category> getAllCategory() { return getAllCategories(); }

    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        try (Connection connection = connection();
             PreparedStatement statement = connection.prepareStatement("SELECT CategoryID, Name FROM Categories ORDER BY Name");
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) categories.add(new Category(rs.getInt(1), rs.getString(2)));
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        return categories;
    }

    public int createCategory(String name) {
        return executeName("INSERT INTO Categories (Name) VALUES (?)", name, 0);
    }

    public int updateCategory(int id, String name) {
        return executeName("UPDATE Categories SET Name = ? WHERE CategoryID = ?", name, id);
    }

    public int deleteCategory(int id) {
        if (isInUse(id)) return -1;
        try (Connection connection = connection(); PreparedStatement statement = connection.prepareStatement("DELETE FROM Categories WHERE CategoryID = ?")) {
            statement.setInt(1, id);
            return statement.executeUpdate();
        } catch (SQLException ex) {
            return 0;
        }
    }

    public boolean isInUse(int id) {
        try (Connection connection = connection(); PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM Products WHERE CategoryID = ?")) {
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) { return rs.next() && rs.getInt(1) > 0; }
        } catch (SQLException ex) { return true; }
    }

    private int executeName(String sql, String name, int id) {
        if (name == null || name.trim().isEmpty()) return 0;
        try (Connection connection = connection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name.trim());
            if (id > 0) statement.setInt(2, id);
            return statement.executeUpdate();
        } catch (SQLException ex) { return 0; }
    }
}
