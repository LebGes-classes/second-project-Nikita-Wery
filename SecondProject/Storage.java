import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class Storage implements Serializable {
    private String storageLocation;
    ArrayList<Cell> allCells = new ArrayList<>();
    AllProducts allProducts = AllProducts.getInstance();

    public Storage(String storageLocation) {
        this.storageLocation = storageLocation;
    }

    public String getId() {
        return storageLocation;
    }

    public Cell getCellByIndex(int index) {
        return allCells.get(index);
    }

    public int getSize() {
        return allCells.size();
    }

    public ArrayList<Cell> getAllCells() {
        return allCells;
    }

    /*
     * public boolean hasProduct(int idOfProduct) {
     * boolean result = false;
     * for (Cell cell : allCells) {
     * if (cell.getId() == idOfProduct) {
     * result = true;
     * }
     * }
     * return result;
     * }
     */

    public boolean findElem(String idOfProduct) {
        boolean findStatus = false;
        for (Cell cell : allCells) {
            if (cell.getId().equals(idOfProduct)) {
                findStatus = true;
            }
        }
        return findStatus;
    }

    public Product saleOfProduct(String idOfProduct) throws NoProductException {
        boolean sellStatus = false;
        Product returnedProduct = null;
        Iterator<Cell> cellIterator = allCells.iterator();
        while (cellIterator.hasNext()) {
            Cell nextCell = cellIterator.next();
            if (nextCell.getId().equals(idOfProduct)) {
                nextCell.getProductFromCell();
                returnedProduct = nextCell.getProduct();
                sellStatus = true;
                if (nextCell.getCountOfProduct() == 0) {
                    cellIterator.remove();
                    AllProducts.getInstance().sellProduct(idOfProduct);
                }
            }
        }
        if (!sellStatus) {
            throw new NoProductException("На складе нет такого товара");
        }
        return returnedProduct;
    }

    public void addProduct(String nameOfProduct, String productPrice, int countOfAccessibleProduct,
            String storageLocation, String productDescr) {
        String productId = nameOfProduct + ":" + productPrice;
        boolean findProduct = false;
        for (Cell cell : allCells) {
            if (cell.getId().equals(productId)) {
                findProduct = true;
                cell.addProduct(countOfAccessibleProduct);
            }
        }
        if (productDescr == null) {
            System.out.println("Пожалуйста, добавьте описание");
            Scanner sc = new Scanner(System.in);
            productDescr = sc.nextLine();
        }
        if (!findProduct) {
            // System.out.println("Пожалуйста, добавьте описание");
            // Scanner sc = new Scanner(System.in);
            // String productDescr = sc.nextLine();
            Product product = new Product(nameOfProduct, Integer.parseInt(productPrice), storageLocation, productDescr);
            allCells.add(new Cell(product, countOfAccessibleProduct));
            allProducts.addProduct(product);
        }
    }

    public String getLocation() {
        return storageLocation;
    }

    public void moveProduct(Storage unloadingStorage, String idOfProduct) {
        for (Cell cell : allCells) {
            if (cell.getId().equals(idOfProduct)) {
                try {
                    int countOfAccessibleProduct = cell.getCountOfProduct();
                    for (int i = 0; i < countOfAccessibleProduct / 2; i++) {
                        cell.getProductFromCell();
                    }
                    unloadingStorage.addProduct(idOfProduct.split(":")[0], idOfProduct.split(":")[1],
                            countOfAccessibleProduct / 2, unloadingStorage.getLocation(),
                            cell.getProduct().getProductDescr());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
