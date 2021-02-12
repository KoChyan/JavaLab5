
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;


public class DataAccessLayer implements BaseDAL {

    private final String URL = "jdbc:mysql://localhost:3306/warehouse?useUnicode=true&serverTimezone=UTC";
    private final String USERNAME = "root";
    private final String PASSWORD = "root";
    private Connection connection;
    private Statement statement;

    public DataAccessLayer() throws SQLException {
        setConnection(DriverManager.getConnection(URL, USERNAME, PASSWORD));
        setStatement(getConnection().createStatement());
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Statement getStatement() {
        return statement;
    }

    public void setStatement(Statement statement) {
        this.statement = statement;
    }

    public void close() throws SQLException {
        getConnection().close();
        getStatement().close();
    }

    @Override
    public void addNewToProductsTable(String name, int amount) throws SQLException {
        //добавляем новый продукт в таблицу
        PreparedStatement prepared = getConnection().prepareStatement(
                "INSERT INTO products(name, amount)"
                        + "VALUES(?, ?);"
        );

        prepared.setString(1, name.toLowerCase());
        prepared.setInt(2, amount);
        prepared.execute();
        prepared.clearParameters();
    }

    @Override
    public void addNewToCategoriesTable(String category) throws SQLException {

        //добавляем категорию в таблицу
        PreparedStatement prepared = prepared = getConnection().prepareStatement(
                "INSERT INTO categories(name) VALUES(?);"
        );
        prepared.setString(1, category);
        prepared.execute();
        prepared.clearParameters();
    }

    @Override
    public void addNewToProductsCategoriesTable(int ID_product, int ID_category) throws SQLException {
        PreparedStatement prepared = getConnection().prepareStatement(
                "INSERT INTO productsCategories (ID_product, ID_category)"
                        + "VALUES(?, ?);"
        );
        prepared.setInt(1, ID_product);
        prepared.setInt(2, ID_category);
        prepared.execute();
        prepared.clearParameters();
    }

    @Override
    public HashSet<String> getNamesFromProductsTable() throws SQLException {
        HashSet<String> names = new HashSet<>();
        ResultSet rs = getStatement().executeQuery("SELECT (name) FROM products");
        while (rs.next())
            names.add((rs.getString("name")));
        return names;
    }

    @Override
    public HashSet<String> getNamesFromCategoriesTable() throws SQLException {
        HashSet<String> names = new HashSet<>();
        ResultSet rs = getStatement().executeQuery("SELECT (name) FROM categories");
        while (rs.next())
            names.add((rs.getString("name")));
        return names;
    }

    public ArrayList<int[]> getConnectionsFromProductsCategoriesTable() throws SQLException {
        ArrayList<int[]> categoryIdProductId = new ArrayList();
        ResultSet rs = getStatement().executeQuery(
                "SELECT ID_category, ID_product FROM productscategories"
        );
        while (rs.next()) {
            int[] connectionID = {
                    rs.getInt("ID_category"), rs.getInt("ID_product")
            };
            categoryIdProductId.add(connectionID);
        }

        return categoryIdProductId;
    }

    @Override
    public int getProductID(String productName) throws SQLException {
        int id = -1;
        ResultSet rs;
        PreparedStatement prepared = getConnection().prepareStatement(
                "SELECT ID_product FROM products WHERE name = ?;"
        );
        prepared.setString(1, productName);
        rs = prepared.executeQuery();
        prepared.clearParameters();
        if (rs.next())
            id = rs.getInt("ID_product");
        return id;
    }

    @Override
    public int getCategoryID(String categoryName) throws SQLException {
        int id = -1;
        ResultSet rs;
        PreparedStatement prepared = getConnection().prepareStatement(
                "SELECT ID_category FROM categories WHERE name = ?"
        );
        prepared.setString(1, categoryName);
        rs = prepared.executeQuery();
        prepared.clearParameters();
        if (rs.next())
            id = rs.getInt("ID_category");
        return id;
    }

    @Override
    public void changeAmountOfProductInProductsTable(String productName, int newAmount) throws SQLException {
        PreparedStatement prepared = getConnection().prepareStatement(
                "UPDATE products SET amount = ? WHERE name = ?;"
        );
        prepared.setInt(1, newAmount);
        prepared.setString(2, productName);
        prepared.execute();
        prepared.clearParameters();
    }

    @Override
    public void deleteConnectionFromProductsCategoriesTable(int product_id) throws SQLException {
        PreparedStatement prepared = getConnection().prepareStatement(
                "DELETE FROM  productscategories WHERE ID_product = ?;"
        );
        prepared.setInt(1, product_id);
        prepared.execute();
        prepared.clearParameters();
    }

    @Override
    public ArrayList<Integer> getIdOfMissingProducts() throws SQLException {
        ArrayList<Integer> id = new ArrayList<>();
        ResultSet rs = getStatement().executeQuery(
                "SELECT ID_product FROM products WHERE amount = 0;"
        );
        while (rs.next()) {
            id.add(rs.getInt("ID_product"));
        }
        return id;
    }

    @Override
    public void deleteMissingProductsFromProductsTable() throws SQLException {
        getStatement().execute("DELETE FROM products WHERE amount = 0;");
    }

    @Override
    public ArrayList<String> getProductCategories(int product_id) throws SQLException {
        ArrayList<String> categories = new ArrayList<>();
        ResultSet rs;
        PreparedStatement prepared = getConnection().prepareStatement(
                "SELECT categories.name FROM categories INNER JOIN productscategories " +
                        "ON categories.ID_category = productscategories.ID_category WHERE ID_product = ?"
        );
        prepared.setInt(1, product_id);
        rs = prepared.executeQuery();
        prepared.clearParameters();

        while (rs.next()) {
            categories.add(rs.getString("name"));
        }
        return categories;
    }

    @Override
    public ArrayList<String> getPopularCategories() throws SQLException {
        ArrayList<String> categories = new ArrayList<>();

        ResultSet rs = getStatement().executeQuery(
                "SELECT categories.name, SUM(products.amount) FROM categories INNER JOIN  productscategories " +
                        "ON categories.ID_category = productscategories.ID_category " +
                        "INNER JOIN products ON productscategories.ID_product = products.ID_product " +
                        "GROUP BY (categories.name)" +
                        "ORDER BY SUM(products.amount) DESC LIMIT 0, 3"
        );

        while (rs.next())
            categories.add(rs.getString(1) + ":  " + rs.getInt(2));

        return categories;
    }

}

