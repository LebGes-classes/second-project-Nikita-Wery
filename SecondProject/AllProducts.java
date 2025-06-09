import java.util.Iterator;
import java.io.Serializable;
import java.util.ArrayList;

public class AllProducts implements Serializable {
    private static AllProducts instance;
    private ArrayList<Product> allProducts = new ArrayList<>();

    public static AllProducts getInstance() {
        if (instance == null) {
            instance = new AllProducts();
        }
        return instance;
    }

    public ArrayList<Product> getAllProducts() {
        return allProducts;
    }

    protected Object readResolve() {
        instance = this; // Обновляем статическую ссылку
        return this;
    }

    public Product getProduct(String idOfProduct) {
        Product returnedProduct = null;
        for (Product product : allProducts) {
            if (product.getId().equals(idOfProduct.trim())) {
                returnedProduct = product;
            }
        }
        return returnedProduct;
    }

    public void addProduct(Product product) {
        allProducts.add(product);
    }

    public void sellProduct(String idOfProduct) {
        boolean sellStatus = false;
        Iterator<Product> productIterator = allProducts.iterator();
        while (productIterator.hasNext()) {
            Product nextProduct = productIterator.next();
            if (nextProduct.getId().equals(idOfProduct)) {
                productIterator.remove();
                sellStatus = true;
            }
        }
        if (!sellStatus) {
            System.out.println("Похоже, что такого товара нет");
        }
    }
}