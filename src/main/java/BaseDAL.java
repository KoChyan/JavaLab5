import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;

public interface BaseDAL {

    public void addNewToProductsTable(String name, int amount) throws SQLException;

    public void addNewToCategoriesTable(String category)throws SQLException;

    public void addNewToProductsCategoriesTable(int ID_product, int ID_category) throws SQLException;

    public HashSet<String> getNamesFromProductsTable() throws SQLException;

    public HashSet<String> getNamesFromCategoriesTable() throws SQLException;

    public int getProductID(String productName) throws SQLException;

    public int getCategoryID(String categoryName) throws SQLException;

    public ArrayList<int[]> getConnectionsFromProductsCategoriesTable() throws SQLException;

    public void changeAmountOfProductInProductsTable(String productName, int newAmount) throws SQLException;

    public void deleteConnectionFromProductsCategoriesTable(int product_id) throws SQLException;

    public ArrayList<Integer> getIdOfMissingProducts() throws SQLException;

    public void deleteMissingProductsFromProductsTable() throws SQLException;

    public ArrayList<String> getProductCategories(int product_id) throws SQLException;

    public ArrayList<String> getPopularCategories() throws SQLException;


}
