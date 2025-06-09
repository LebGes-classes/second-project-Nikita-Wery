import java.io.Serializable;

public class Product implements Serializable {
    private String productName;
    private String productId;
    private int productPrice; // возможно здесь нужен long
    private String productDescription = "Без описания";
    private String productLocation;

    public Product(String productName, int productPrice, String productLocation, String productDescription) {
        this.productName = productName;
        this.productId = productName + ":" + productPrice;
        this.productPrice = productPrice;
        this.productDescription = productDescription;
        this.productLocation = productLocation;
    }

    public String getId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public String getLocation() {
        return productLocation;
    }

    public String getInfo() {
        return productName + productPrice + productLocation;
    }

    public String getProductDescr() {
        return productDescription;
    }

    public String getFullInfo() {
        return "Имя товара: " + productName + "\n"
                + "Цена товара: " + productPrice + "\n"
                + "Товар находистся: " + productLocation + "\n"
                + "Описание товара: " + "\n"
                + productDescription;
    }
}