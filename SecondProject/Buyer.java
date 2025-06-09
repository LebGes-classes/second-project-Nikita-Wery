import java.awt.Stroke;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;

public class Buyer extends User {
    private int buyerMoney;
    public static final String[] BUYERFUNCTION = { "Купить товар", "Вернуть товар", "Забрать товар" };
    AllProducts menuOfProducts = new AllProducts();
    PointOfSalesMap pointOfSalesMap = new PointOfSalesMap();

    Scanner sc = new Scanner(System.in);

    public Buyer(String userName, String userSecondName, String userPassword, String userLocation, String code,
            ArrayList<String> basketOfUser)
            throws NoNameException, NoSecondNameException, NoPasswordException, NoLocationException {
        super(userName, userSecondName, userPassword, userLocation, code);
    }

    public void putMoney(int count) {
        if (count >= 0) {
            buyerMoney += count;
        } else {
            System.out.println("Извините, такую сумму нельзя положить");
        }
        System.out.println("Ваш баланс: " + buyerMoney);
    }

    public void buy(String idOfProduct) throws NotEnoughMoneyException {
        if (buyerMoney < AllProducts.getInstance().getProduct(idOfProduct).getProductPrice()) {
            throw new NotEnoughMoneyException("Пожалуйста, пополните баланс");
            // System.out.println("Пожалуйста, пополните баланс");
        }

        boolean findPoint = false;
        for (Storage storage : StorageMap.getInstance().getAllStorages()) {
            PointOfSales pointOfSales = null;
            // ищем ближайший город и проверяем наличие товара в нём
            if (storage.getLocation().equals(this.getLocation())) {
                // Проверка
                System.out.println("Нашёл");
                findPoint = true;
                if (!storage.findElem(idOfProduct)) {
                    for (Storage storageWithProduct : StorageMap.getInstance().getAllStorages()) {
                        if (storageWithProduct.findElem(idOfProduct)) {
                            storageWithProduct.moveProduct(storage, idOfProduct);
                        }
                    }
                }
                // нужно переместить товар в постомат который находится в location user-a
                try {
                    pointOfSales = PointOfSalesMap.getInstance().getPointOfSalesByLocation(this.getLocation());
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Похоже в вашем районе пока что нет пунктов выдачи");
                }
                try {
                    pointOfSales.addProduct(storage.saleOfProduct(idOfProduct));
                    // pointOfSales.pickUpProduct(idOfProduct);
                } catch (Exception e) {
                    System.out.println("Похоже вашего товара нет на складе");
                    e.printStackTrace();
                }

            }
        }
        if (!findPoint) {
            System.out.println("Увы, в вашем городе нет пунктов выдачи или складов");
        }
    }

    public void returnProduct(String idOfPoduct) throws NoProductException, NoLocationException {
        boolean findPointOfSales = false;
        for (PointOfSales pointOfSales : PointOfSalesMap.getInstance().getAllPointOfSaleses()) {
            if (pointOfSales.getLocation().equals(this.getLocation())) {
                findPointOfSales = true;
                Product productToReturn = pointOfSales.getReturnProduct(idOfPoduct);
                pointOfSales.removeProduct(idOfPoduct);
                Storage storageToRetun = StorageMap.getInstance().getStorageByLocation(this.getLocation());
                storageToRetun.addProduct(productToReturn.getProductName(),
                        Integer.toString(productToReturn.getProductPrice()),
                        1, productToReturn.getLocation(), productToReturn.getProductDescr());
            }
        }
        if (!findPointOfSales) {
            throw new NoProductException("Неправильный id товара");
        }
    }

    public void takeProduct(ArrayList<String> basket) {
        PointOfSales pointOfSales = null;
        try {
            pointOfSales = PointOfSalesMap.getInstance().getPointOfSalesByLocation(this.getLocation());
        } catch (Exception ex) {
            System.out.println("Нет такого пункта продаж");
        }
        for (String idOfProduct : basket) {
            pointOfSales.pickUpProduct(idOfProduct);
        }
    }
}