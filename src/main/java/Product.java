
import java.util.ArrayList;
import java.util.List;

public class Product {

    private String name;
    private int amount;
    private List<String> categories = new ArrayList<>();


    public Product(String name, int amount, List<String> categories){
        setName(name);
        setAmount(amount);
        setCategories(categories);
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories){
        for(int i = 0; i < categories.size(); i ++)
            getCategories().add(categories.get(i).toLowerCase());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.toLowerCase();
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

}
