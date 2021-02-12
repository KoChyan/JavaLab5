import java.sql.SQLException;
import java.util.ArrayList;


public interface BaseBLL {

    public boolean isProductsTableContains(String name) throws SQLException;

    public boolean isCategoriesTableContains(String name) throws SQLException;

    public void addNewProduct(Product product) throws SQLException;

    public boolean isProductsCategoriesTableContainsConnection(String name, String category) throws SQLException;

    public void changeAmountOfProduct(String name, int amount) throws SQLException;

    public void deleteMissingProducts() throws SQLException;

    public ArrayList<String> getProductCategories(String name) throws SQLException;

    public ArrayList<String> getPopularCategories() throws SQLException;


}
