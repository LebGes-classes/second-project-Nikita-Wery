import java.io.Serializable;

public class Cell implements Serializable {
    private String cellId;
    private String cellLocation;
    private int countOfProduct;
    private Product product;

    public String getId() {
        return cellId;
    }

    public Cell(String productId, String productLocation, Product product) {
        this.cellId = productId;
        this.cellLocation = productLocation;
        this.product = product;
        // countOfProduct++;
    }

    public Cell(Product product, int countOfProduct) {
        this.cellId = product.getId();
        this.cellLocation = product.getLocation();
        this.product = product;
        this.countOfProduct = countOfProduct;
    }

    public String getInfo() {
        return cellId.split(":")[0] + cellId.split(":")[1] + cellLocation + Integer.toString(countOfProduct);
    }

    public int getCountOfProduct() {
        return countOfProduct;
    }

    public void getProductFromCell() throws NoProductException {
        if (countOfProduct == 0) {
            throw new NoProductException("Хм..., похоже всё уже раcпродали");
        }
        countOfProduct--;
    }

    public void addProduct(int count) {
        countOfProduct += count;
    }

    public Product getProduct() {
        return product;
    }

}
