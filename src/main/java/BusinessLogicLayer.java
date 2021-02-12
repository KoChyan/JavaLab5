import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class BusinessLogicLayer implements BaseBLL {
    BaseDAL dll;

    public BusinessLogicLayer() throws SQLException {
        dll = new DataAccessLayer();
    }

    @Override
    public void changeAmountOfProduct(String productName, int newAmount) throws SQLException {
        //если такой продукт есть, то изменяем количество
        if (isProductsTableContains(productName))
            dll.changeAmountOfProductInProductsTable(productName, newAmount);
    }

    @Override
    public void addNewProduct(Product product) throws SQLException {

        //добавление нового продукта в таблицу products
        if (!isProductsTableContains(product.getName()))
            dll.addNewToProductsTable(product.getName(), product.getAmount());

        //добавление новых категорий в таблицу categories
        List<String> categories = product.getCategories();
        for (String category : categories) {
            //Если в таблице категорий не содержится данной категории, то добавляем её
            if (!isCategoriesTableContains(category))
                dll.addNewToCategoriesTable(category);
        }

        //добавляем связи продуктов и категорий в таблицу productsCategories
        for (String category : categories) {
            //если данной связи не содержится в таблице, то добавляем
            if (!isProductsCategoriesTableContainsConnection(product.getName(), category)) {
                dll.addNewToProductsCategoriesTable(
                        dll.getProductID(product.getName()),
                        dll.getCategoryID(category)
                );
            }
        }
    }

    @Override
    public boolean isProductsTableContains(String productName) throws SQLException {
        return dll.getNamesFromProductsTable().contains(productName);
    }

    @Override
    public boolean isCategoriesTableContains(String categoryName) throws SQLException {
        return dll.getNamesFromCategoriesTable().contains(categoryName);
    }

    @Override
    public boolean isProductsCategoriesTableContainsConnection(String name, String category) throws SQLException {
        int[] connectionID = {
                dll.getProductID(name), dll.getCategoryID(category)
        };

        return dll.getConnectionsFromProductsCategoriesTable().contains(connectionID);

    }

    @Override
    public void deleteMissingProducts() throws SQLException {
        //получаем id товаров, количество которых на складе 0, чтобы потом удалить все связи
        ArrayList<Integer> idOfMissingProducts = dll.getIdOfMissingProducts();

        //удаляем продукт из таблицы products
        dll.deleteMissingProductsFromProductsTable();

        //удаляем все связи продукт-категория из таблицы productsCategories с этими продуктами
        for (int id : idOfMissingProducts)
            dll.deleteConnectionFromProductsCategoriesTable(id);
    }

    @Override
    public ArrayList<String> getProductCategories(String name) throws SQLException {
        int id = dll.getProductID(name);
        return dll.getProductCategories(id);
    }

    @Override
    public ArrayList<String> getPopularCategories() throws SQLException {
        return dll.getPopularCategories();
    }

}
