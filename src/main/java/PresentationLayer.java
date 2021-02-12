import java.sql.SQLException;
import java.util.*;

//«Склад» - хранится информация о имеющихся в наличии товарах
//        (название, количество, список категорий, к которым относится данный
//        товар).
//        Приложение должно позволять:
//        1. Добавлять информацию о товарах и категориях.
//        2. Изменять количество выбранного товара на складе.
//        3. Удалять из базы все товары, которых по факту нет на складе
//        (количество равно 0).
//        4. Выводить все категории, к которым относится выбранный товар.
//        5. Рассчитывать наиболее популярные категории (к наиболее
//        популярным относят три категории, в которых есть наибольшее
//        число товаров.

public class PresentationLayer {

    public static void main(String[] args) throws SQLException {
        BaseBLL bll = new BusinessLogicLayer();
        String name;
        int amount;
        List<String> categories;

        String request = "";

        while (!request.equals("exit")) {

            request = inputValid("Введите запрос");
            if (request.equals("добавить новый продукт")) {
                //получение информации о продукте от пользователя
                name = enterParam("название продукта");

                //проверяем есть ли уже такой товар
                if (bll.isProductsTableContains(name.toLowerCase())) {
                    System.out.println("Такой товар уже есть");
                    continue;
                }

                amount = enterParam("количество", 1);
                categories = enterParam();

                //передача данных в bll
                bll.addNewProduct(new Product(name, amount, categories));
                categories.clear();
                System.out.println("Продукт успешно добавлен");
            }
            if (request.equals("изменить количество товара")) {
                //получение информации от пользователя
                name = enterParam("название продукта");
                if (!bll.isProductsTableContains(name)) {
                    System.out.println("Такого товара нет на складе!");
                    continue;
                }
                amount = enterParam("количество", 0);
                //передача данных в bll
                bll.changeAmountOfProduct(name, amount);
                System.out.println("Количество товара успешно изменено");
            }
            if (request.equals("удалить товары, которых нет на складе")) {
                bll.deleteMissingProducts();
                System.out.println("Отсутствующие на складе товары удалены");
            }
            if (request.equals("вывести все категории товара")) {
                name = enterParam("название продукта");
                categories = bll.getProductCategories(name);
                printCategories(name, categories);
                categories.clear();
                System.out.println("Все категории выведены");
            }
            if (request.equals("вывести популярные категории")) {
                ArrayList<String> popularCategories = bll.getPopularCategories();
                printCategories(popularCategories);
                System.out.println("Популярные категории выведены");
            }
        }
    }

    public static void printCategories(String productName, List<String> categories) {
        System.out.print(productName + " относится к категориям: ");
        for (String category : categories)
            System.out.print(category + ", ");
        System.out.println();
    }

    public static void printCategories(ArrayList<String> categories) {
        for (String category : categories)
            System.out.println(category);
    }

    public static String inputValid(String message) {
        System.out.println(message);
        Scanner scanner = new Scanner(System.in);
        String string;

        while (true) {
            try {
                string = scanner.nextLine();

                if (string.trim().length() > 0)
                    break;
                else {
                    throw new Exception();
                }
            } catch (Exception e) {
                System.out.print("Введите непустую строку: ");
                scanner.next();
            }
        }
        return string.toLowerCase();
    }

    public static int inputValid(String message, int minValue) {
        System.out.println(message);
        int value;
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                value = scanner.nextInt();
                if (value >= minValue)
                    break;
                else {
                    throw new Exception();
                }
            } catch (Exception e) {
                System.out.print("Неверное значение, попробуйте снова: ");
                scanner.next();
            }
        }
        return value;
    }

    public static String enterParam(String paramName) {
        return inputValid("Введите " + paramName + ": ");
    }

    public static int enterParam(String paramName, int minValue) {
        return inputValid("Введите " + paramName + ": ", minValue);
    }

    public static List<String> enterParam() {
        List<String> categories = new ArrayList();
        String stringIn;
        char[] incorrectChars = {',', '@', '!', '.', '+', '-', '/'};

        while (true) {
            try {
                stringIn = inputValid("Введите категории через пробел: ");
                if (isContains(stringIn, incorrectChars))
                    throw new Exception();
                break;
            } catch (Exception e) {
                System.out.println("Неверный формат");
                continue;
            }
        }

        String[] categoriesWithDuplicates = stringIn.split(" ");
        //записываем в лист категории без дубликатов
        for (String category : categoriesWithDuplicates) {
            if (!categories.contains(category))
                categories.add(category);
        }

        return categories;
    }

    public static boolean isContains(String string, char[] symbols) {
        char[] stringSymbols = string.toCharArray();

        for (int i = 0; i < stringSymbols.length; i++)
            for (int j = 0; j < symbols.length; j++) {
                if (stringSymbols[i] == symbols[j])
                    return true;
            }

        return false;
    }

}
