package DAOs;

import DB.DBContext;
import Models.AttributeDetail;
import Models.Category;
import Models.Product;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/** Product persistence and reporting queries for the SMARTTICK catalog. */
public class ProductDAO {

    private static final Logger LOGGER = Logger.getLogger(ProductDAO.class.getName());
    private final DBContext db = new DBContext();

    private Connection connection() throws SQLException {
        Connection connection = db.getConnection();
        if (connection == null) {
            throw new SQLException("Database connection is unavailable. Check db.properties and SQL Server.");
        }
        return connection;
    }

    private Product mapProduct(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setProductId(rs.getInt("ProductID"));
        product.setBrandId(rs.getInt("BrandID"));
        product.setCategoryId(rs.getInt("CategoryID"));
        product.setModel(rs.getString("Model"));
        product.setFullName(rs.getString("FullName"));
        product.setDescription(rs.getString("Description"));
        product.setDeleted(rs.getInt("IsDeleted"));
        product.setPrice(rs.getLong("Price"));
        product.setImage(rs.getString("Image"));
        product.setImage1(rs.getString("Image1"));
        product.setImage2(rs.getString("Image2"));
        product.setImage3(rs.getString("Image3"));
        product.setQuantity(rs.getInt("Quantity"));
        product.setStock(rs.getInt("Stock"));
        try {
            product.setCategoryName(rs.getString("CategoryName"));
        } catch (SQLException ignored) {
            // Column is optional for simple catalog queries.
        }
        try {
            product.setBrandName(rs.getString("BrandName"));
        } catch (SQLException ignored) {
            // Column is optional for simple catalog queries.
        }
        return product;
    }

    private ArrayList<Product> queryProducts(String sql, SqlBinder binder) {
        ArrayList<Product> products = new ArrayList<>();
        try (Connection connection = connection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            if (binder != null) {
                binder.bind(statement);
            }
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    products.add(mapProduct(rs));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Product query failed", ex);
        }
        return products;
    }

    @FunctionalInterface
    private interface SqlBinder {
        void bind(PreparedStatement statement) throws SQLException;
    }

    public ArrayList<Product> getAllProducts() {
        return queryProducts(baseSelect() + " WHERE p.IsDeleted = 0 AND p.Stock > 0 ORDER BY p.ProductID DESC", null);
    }

    public ArrayList<Product> getAllAvailabilityProducts() {
        return queryProducts(baseSelect() + " ORDER BY p.IsDeleted, p.FullName", null);
    }

    /** Products in completed orders of a customer, newest purchase first. */
    public ArrayList<Product> getPurchasedProducts(int customerId) {
        String sql = baseSelect()
                + " JOIN OrderDetails od ON p.ProductID = od.ProductID "
                + " JOIN Orders o ON od.OrderID = o.OrderID "
                + " WHERE o.CustomerID = ? AND o.Status = 4 AND p.IsDeleted = 0 "
                + " GROUP BY p.ProductID, p.BrandID, p.CategoryID, p.Model, p.FullName, p.Description, "
                + "p.IsDeleted, p.Price, p.Image, p.Image1, p.Image2, p.Image3, p.Quantity, p.Stock, c.Name, b.Name "
                + " ORDER BY MAX(o.OrderedDate) DESC";
        return queryProducts(sql, statement -> statement.setInt(1, customerId));
    }

    /** Accepts either a numeric category ID or a category name. */
    public ArrayList<Product> getAllProductsByCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            return getAllProducts();
        }
        String value = category.trim();
        boolean numeric = value.matches("\\d+");
        String sql = baseSelect()
                + " WHERE p.IsDeleted = 0 AND p.Stock > 0 AND "
                + (numeric ? "p.CategoryID = ?" : "c.Name = ?")
                + " ORDER BY p.ProductID DESC";
        return queryProducts(sql, statement -> {
            if (numeric) {
                statement.setInt(1, Integer.parseInt(value));
            } else {
                statement.setString(1, value);
            }
        });
    }

    public ArrayList<Product> searchCatalog(String keyword, String category, String brand,
            Long minPrice, Long maxPrice, String sort) {
        StringBuilder sql = new StringBuilder(baseSelect()).append(" WHERE p.IsDeleted = 0 AND p.Stock > 0 ");
        List<Object> parameters = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND (p.FullName LIKE ? OR p.Model LIKE ?) ");
            String pattern = "%" + keyword.trim() + "%";
            parameters.add(pattern);
            parameters.add(pattern);
        }
        if (category != null && !category.trim().isEmpty()) {
            sql.append(" AND c.Name = ? ");
            parameters.add(category.trim());
        }
        if (brand != null && !brand.trim().isEmpty()) {
            sql.append(" AND b.Name = ? ");
            parameters.add(brand.trim());
        }
        if (minPrice != null && minPrice >= 0) {
            sql.append(" AND p.Price >= ? ");
            parameters.add(minPrice);
        }
        if (maxPrice != null && maxPrice >= 0) {
            sql.append(" AND p.Price <= ? ");
            parameters.add(maxPrice);
        }

        if ("priceAsc".equals(sort) || "price_asc".equals(sort)) {
            sql.append(" ORDER BY p.Price ASC ");
        } else if ("priceDesc".equals(sort) || "price_desc".equals(sort)) {
            sql.append(" ORDER BY p.Price DESC ");
        } else if ("name".equals(sort) || "name_asc".equals(sort)) {
            sql.append(" ORDER BY p.FullName ASC ");
        } else {
            sql.append(" ORDER BY p.ProductID DESC ");
        }

        return queryProducts(sql.toString(), statement -> {
            for (int i = 0; i < parameters.size(); i++) {
                statement.setObject(i + 1, parameters.get(i));
            }
        });
    }

    private String baseSelect() {
        return "SELECT p.ProductID, p.BrandID, p.CategoryID, p.Model, p.FullName, p.Description, "
                + "p.IsDeleted, p.Price, p.Image, p.Image1, p.Image2, p.Image3, p.Quantity, p.Stock, "
                + "c.Name AS CategoryName, b.Name AS BrandName "
                + "FROM Products p JOIN Categories c ON p.CategoryID = c.CategoryID "
                + "JOIN Brands b ON p.BrandID = b.BrandID ";
    }

    public boolean isModelExists(String model) {
        return exists("SELECT COUNT(*) FROM Products WHERE Model = ?", model);
    }

    public boolean isFullnameExists(String fullName) {
        return exists("SELECT COUNT(*) FROM Products WHERE FullName = ?", fullName);
    }

    private boolean exists(String sql, String value) {
        if (value == null || value.trim().isEmpty()) {
            return false;
        }
        try (Connection connection = connection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, value.trim());
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Duplicate check failed", ex);
            return false;
        }
    }

    /** Retained for compatibility with the original controller. */
    public ArrayList<Product> findProductsByFilter(ArrayList<String> filters, String category) {
        return searchCatalog(null, category, extractBrand(filters), extractMinPrice(filters), extractMaxPrice(filters), null);
    }

    /** Retained for compatibility with the original controller. */
    public ArrayList<Product> filterProductsByPrice(ArrayList<String> filters) {
        return searchCatalog(null, null, extractBrand(filters), extractMinPrice(filters), extractMaxPrice(filters), null);
    }

    private String extractBrand(List<String> filters) {
        if (filters == null) {
            return null;
        }
        for (String filter : filters) {
            if (filter == null) {
                continue;
            }
            int first = filter.indexOf('\'');
            int last = filter.lastIndexOf('\'');
            if (first >= 0 && last > first) {
                return filter.substring(first + 1, last).replace("''", "'");
            }
        }
        return null;
    }

    private Long extractMinPrice(List<String> filters) {
        return extractPrice(filters, true);
    }

    private Long extractMaxPrice(List<String> filters) {
        return extractPrice(filters, false);
    }

    private Long extractPrice(List<String> filters, boolean minimum) {
        if (filters == null) {
            return null;
        }
        Long selected = null;
        for (String filter : filters) {
            if (filter == null) {
                continue;
            }
            java.util.regex.Matcher matcher = java.util.regex.Pattern.compile("(\\d{5,})").matcher(filter);
            while (matcher.find()) {
                long value = Long.parseLong(matcher.group(1));
                selected = selected == null ? value : (minimum ? Math.min(selected, value) : Math.max(selected, value));
            }
        }
        return selected;
    }

    public ArrayList<String> getAllBrandByCategory(String category) {
        ArrayList<String> brands = new ArrayList<>();
        String sql = "SELECT DISTINCT b.Name FROM Brands b JOIN Products p ON b.BrandID = p.BrandID "
                + "JOIN Categories c ON p.CategoryID = c.CategoryID WHERE c.Name = ? AND p.IsDeleted = 0 ORDER BY b.Name";
        try (Connection connection = connection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, category);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    brands.add(rs.getString(1));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Cannot load brands", ex);
        }
        return brands;
    }

    public ArrayList<Product> getProductList() {
        return queryProducts(baseSelect() + " ORDER BY p.IsDeleted DESC, p.ProductID DESC", null);
    }

    public Product getProductByID(int productId) {
        ArrayList<Product> products = queryProducts(baseSelect() + " WHERE p.ProductID = ?",
                statement -> statement.setInt(1, productId));
        if (products.isEmpty()) {
            return null;
        }
        Product product = products.get(0);
        List<AttributeDetail> details = new AttributeDAO().getAttributesByProductID(productId);
        product.setAttributeDetails(details);
        HashMap<String, String> attributeMap = new HashMap<>();
        for (AttributeDetail detail : details) {
            attributeMap.put(detail.getAttributeName(), detail.getAttributeInfor());
        }
        product.setAttributes(attributeMap);
        return product;
    }

    public int deleteProduct(int productId) {
        return setDeleted(productId, 1);
    }

    public int restoreProduct(int productId) {
        return setDeleted(productId, 0);
    }

    private int setDeleted(int productId, int deleted) {
        try (Connection connection = connection();
             PreparedStatement statement = connection.prepareStatement("UPDATE Products SET IsDeleted = ? WHERE ProductID = ?")) {
            statement.setInt(1, deleted);
            statement.setInt(2, productId);
            return statement.executeUpdate();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Cannot update product status", ex);
            return 0;
        }
    }

    /** Returns the generated ProductID or 0 when insertion fails. */
    public int createProduct(Product product) {
        String sql = "INSERT INTO Products (BrandID, CategoryID, Model, FullName, Description, IsDeleted, Price, "
                + "Image, Image1, Image2, Image3, Quantity, Stock) "
                + "VALUES ((SELECT BrandID FROM Brands WHERE Name = ?), "
                + "(SELECT CategoryID FROM Categories WHERE Name = ?), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = connection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, product.getBrandName());
            statement.setString(2, product.getCategoryName());
            statement.setString(3, product.getModel());
            statement.setString(4, product.getFullName());
            statement.setString(5, product.getDescription());
            statement.setInt(6, product.getDeleted());
            statement.setLong(7, product.getPrice());
            statement.setString(8, product.getImage());
            statement.setString(9, product.getImage1());
            statement.setString(10, product.getImage2());
            statement.setString(11, product.getImage3());
            statement.setInt(12, Math.max(0, product.getQuantity()));
            statement.setInt(13, Math.max(0, product.getStock()));
            if (statement.executeUpdate() == 0) {
                return 0;
            }
            try (ResultSet keys = statement.getGeneratedKeys()) {
                return keys.next() ? keys.getInt(1) : 0;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Cannot create product", ex);
            return 0;
        }
    }

    public int addAttributeDetail(AttributeDetail detail) {
        String sql = "INSERT INTO AttributeDetails (AttributeID, ProductID, AttributeInfor) VALUES (?, ?, ?)";
        try (Connection connection = connection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, detail.getAttributeId());
            statement.setInt(2, detail.getProductId());
            statement.setString(3, detail.getAttributeInfor());
            return statement.executeUpdate();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Cannot add product attribute", ex);
            return 0;
        }
    }

    public int updateProduct(Product product) {
        String updateProduct = "UPDATE Products SET Model = ?, FullName = ?, Description = ?, Price = ?, "
                + "Image = ?, Image1 = ?, Image2 = ?, Image3 = ?, IsDeleted = ?, Stock = ?, "
                + "BrandID = (SELECT BrandID FROM Brands WHERE Name = ?), "
                + "CategoryID = (SELECT CategoryID FROM Categories WHERE Name = ?) WHERE ProductID = ?";
        try (Connection connection = connection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(updateProduct)) {
                statement.setString(1, product.getModel());
                statement.setString(2, product.getFullName());
                statement.setString(3, product.getDescription());
                statement.setLong(4, product.getPrice());
                statement.setString(5, product.getImage());
                statement.setString(6, product.getImage1());
                statement.setString(7, product.getImage2());
                statement.setString(8, product.getImage3());
                statement.setInt(9, product.getDeleted());
                statement.setInt(10, Math.max(0, product.getStock()));
                statement.setString(11, product.getBrandName());
                statement.setString(12, product.getCategoryName());
                statement.setInt(13, product.getProductId());
                int affected = statement.executeUpdate();
                if (affected == 0) {
                    connection.rollback();
                    return 0;
                }
            }

            if (product.getAttributeDetails() != null) {
                String upsert = "MERGE AttributeDetails AS target USING (SELECT ? AS AttributeID, ? AS ProductID) AS source "
                        + "ON target.AttributeID = source.AttributeID AND target.ProductID = source.ProductID "
                        + "WHEN MATCHED THEN UPDATE SET AttributeInfor = ? "
                        + "WHEN NOT MATCHED THEN INSERT (AttributeID, ProductID, AttributeInfor) VALUES (?, ?, ?);";
                try (PreparedStatement statement = connection.prepareStatement(upsert)) {
                    for (AttributeDetail detail : product.getAttributeDetails()) {
                        statement.setInt(1, detail.getAttributeId());
                        statement.setInt(2, product.getProductId());
                        statement.setString(3, detail.getAttributeInfor());
                        statement.setInt(4, detail.getAttributeId());
                        statement.setInt(5, product.getProductId());
                        statement.setString(6, detail.getAttributeInfor());
                        statement.addBatch();
                    }
                    statement.executeBatch();
                }
            }
            connection.commit();
            return 1;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Cannot update product", ex);
            return 0;
        }
    }

    public List<Product> searchProductByName(String keyword) {
        return searchCatalog(keyword, null, null, null, null, null);
    }

    public List<Product> sortProduct(String keyword, String sort) {
        return searchCatalog(keyword, null, null, null, null, "DESC".equalsIgnoreCase(sort) ? "priceDesc" : "priceAsc");
    }

    public List<Map<String, Object>> getSalesData(String period) throws SQLException {
        String dateExpression = "DAY".equalsIgnoreCase(period) ? "CONVERT(varchar(10), o.OrderedDate, 120)" :
                ("YEAR".equalsIgnoreCase(period) ? "CONVERT(varchar(4), YEAR(o.OrderedDate))" : "FORMAT(o.OrderedDate, 'yyyy-MM')");
        String sql = "SELECT " + dateExpression + " AS [date], SUM(od.Quantity) AS total "
                + "FROM Orders o JOIN OrderDetails od ON o.OrderID = od.OrderID "
                + "GROUP BY " + dateExpression + " ORDER BY [date]";
        return executeQuery(sql);
    }

    public List<Map<String, Object>> getTopSellingProducts() throws SQLException {
        return executeQuery("SELECT TOP 10 p.FullName, SUM(od.Quantity) AS totalSold FROM OrderDetails od "
                + "JOIN Products p ON od.ProductID = p.ProductID GROUP BY p.FullName ORDER BY totalSold DESC");
    }

    public List<Map<String, Object>> getLowStockProducts() throws SQLException {
        return executeQuery("SELECT FullName, Stock FROM Products WHERE IsDeleted = 0 AND Stock < 10 ORDER BY Stock ASC");
    }

    public List<Map<String, Object>> getCategorySales() throws SQLException {
        return executeQuery("SELECT c.Name, COALESCE(SUM(od.Quantity), 0) AS totalSold FROM Categories c "
                + "LEFT JOIN Products p ON c.CategoryID = p.CategoryID "
                + "LEFT JOIN OrderDetails od ON p.ProductID = od.ProductID GROUP BY c.Name ORDER BY totalSold DESC");
    }

    private List<Map<String, Object>> executeQuery(String sql) throws SQLException {
        List<Map<String, Object>> data = new ArrayList<>();
        try (Connection connection = connection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            ResultSetMetaData metadata = rs.getMetaData();
            while (rs.next()) {
                Map<String, Object> record = new HashMap<>();
                for (int i = 1; i <= metadata.getColumnCount(); i++) {
                    record.put(metadata.getColumnLabel(i), rs.getObject(i));
                }
                data.add(record);
            }
        }
        return data;
    }

    public Map<String, Object> getDashboardStats() throws SQLException {
        Map<String, Object> stats = new HashMap<>();
        String sql = "SELECT "
                + "(SELECT COUNT(*) FROM Customers WHERE IsDeleted = 0) AS totalCustomers, "
                + "(SELECT COUNT(*) FROM Products) AS totalProducts, "
                + "(SELECT COUNT(*) FROM Products WHERE IsDeleted = 0 AND Stock > 0) AS inStockProducts, "
                + "(SELECT COUNT(*) FROM Products WHERE IsDeleted = 0 AND Stock BETWEEN 1 AND 9) AS lowStockProducts, "
                + "(SELECT COUNT(*) FROM Products WHERE IsDeleted = 1) AS hiddenProducts, "
                + "(SELECT COALESCE(SUM(Stock), 0) FROM Products WHERE IsDeleted = 0) AS totalInventory, "
                + "(SELECT COUNT(*) FROM Orders) AS totalOrders, "
                + "(SELECT COALESCE(SUM(TotalAmount), 0) FROM Orders WHERE Status = 4) AS totalRevenue";
        try (Connection connection = connection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            if (rs.next()) {
                ResultSetMetaData metadata = rs.getMetaData();
                for (int i = 1; i <= metadata.getColumnCount(); i++) {
                    stats.put(metadata.getColumnLabel(i), rs.getObject(i));
                }
            }
        }
        return stats;
    }

    public List<Map<String, Object>> getWeeklySales(String category) throws SQLException {
        List<Map<String, Object>> sales = new ArrayList<>();
        String sql = "SELECT p.FullName, SUM(od.Quantity) AS totalSold FROM OrderDetails od "
                + "JOIN Products p ON od.ProductID = p.ProductID JOIN Categories c ON p.CategoryID = c.CategoryID "
                + "JOIN Orders o ON od.OrderID = o.OrderID WHERE c.Name = ? "
                + "AND o.OrderedDate >= DATEADD(DAY, -7, GETDATE()) GROUP BY p.FullName ORDER BY totalSold DESC";
        try (Connection connection = connection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, category);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("productName", rs.getString("FullName"));
                    row.put("totalSold", rs.getInt("totalSold"));
                    sales.add(row);
                }
            }
        }
        return sales;
    }

    public List<Map<String, Object>> getNewCustomers() throws SQLException {
        return executeQuery("SELECT TOP 5 FullName AS name, Email AS email FROM Customers "
                + "WHERE IsDeleted = 0 ORDER BY CreatedDate DESC");
    }

    public List<Map<String, Object>> getNewProducts() throws SQLException {
        return executeQuery("SELECT TOP 10 p.ProductID AS id, p.FullName AS name, c.Name AS category, b.Name AS brand, "
                + "p.Price AS price, p.Stock AS stock FROM Products p JOIN Categories c ON p.CategoryID = c.CategoryID JOIN Brands b ON p.BrandID = b.BrandID "
                + "ORDER BY p.ProductID DESC");
    }

    public List<Category> getAllCategories() {
        return new CategoryDAO().getAllCategories();
    }

    public List<Product> getProductsByCategory(int categoryID) {
        return getAllProductsByCategory(String.valueOf(categoryID));
    }

    public List<Product> getNewImportedProducts() {
        String sql = baseSelect() + " WHERE p.ProductID IN (SELECT DISTINCT d.ProductID FROM ImportStockDetails d "
                + "JOIN ImportStocks i ON d.ImportID = i.ImportID WHERE i.ImportDate >= DATEADD(DAY, -7, GETDATE())) "
                + "AND p.IsDeleted = 0 AND p.Stock > 0 "
                + "ORDER BY p.ProductID DESC";
        return queryProducts(sql, null);
    }

    public boolean checkDuplicateProduct(String fullName, String model, int productId) {
        String sql = "SELECT COUNT(*) FROM Products WHERE (FullName = ? OR Model = ?) AND ProductID <> ?";
        try (Connection connection = connection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, fullName);
            statement.setString(2, model);
            statement.setInt(3, productId);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Duplicate check failed", ex);
            return true;
        }
    }

    public List<Product> getProductsSortedByStatus(String sortBy) {
        String order;
        if ("Price".equals(sortBy)) {
            order = "p.Price";
        } else if ("FullName".equals(sortBy)) {
            order = "p.FullName";
        } else {
            order = "p.IsDeleted DESC, p.ProductID DESC";
        }
        return queryProducts(baseSelect() + " ORDER BY " + order, null);
    }
}
