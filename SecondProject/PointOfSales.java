import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class PointOfSales implements Serializable {
    private String pointLocation;
    private int profit;
    private int countOfSoldProduct;
    private ArrayList<Product> availableProduct = new ArrayList<>();

    public PointOfSales(String pointLocation) {
        this.pointLocation = pointLocation;
    }

    public String getLocation() {
        return pointLocation;
    }

    public int getProductStatus() {
        return availableProduct.size();
    }

    public ArrayList<Product> getAllProduct() {
        return availableProduct;
    }

    public void addProduct(Product product) {
        availableProduct.add(product);
    }

    public void pickUpProduct(String idOfProduct) {
        Iterator<Product> productIterator = availableProduct.iterator();
        while (productIterator.hasNext()) {
            Product nextProduct = productIterator.next();
            if (nextProduct.getId().equals(idOfProduct)) {
                profit += nextProduct.getProductPrice();
                countOfSoldProduct++;
                productIterator.remove();
            }
        }
    }

    public void removeProduct(String idOfProduct) {
        Iterator<Product> productIterator = availableProduct.iterator();
        while (productIterator.hasNext()) {
            Product nextProduct = productIterator.next();
            if (nextProduct.getId().equals(idOfProduct)) {
                productIterator.remove();
            }
        }
    }

    public Product getReturnProduct(String idOfProduct) {
        Product returnedProduct = null;
        for (Product product : availableProduct) {
            if (product.getId().equals(idOfProduct)) {
                returnedProduct = product;
            }
        }
        if (returnedProduct == null) {
            System.out.println("Не удалось вернуть товар");
        }
        return returnedProduct;
    }

    public void getInfoAboutProfit() {
        System.out.println("Всего продано товаров: " + countOfSoldProduct + ", зарабоано: " + profit);
    }
}