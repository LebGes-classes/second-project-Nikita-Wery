import java.util.Iterator;
import java.util.Scanner;

public class Worker extends User {
    public static final String[] WORKERFUNCTION = { "Перемещать товар", "Закупить товар", /*
                                                                                           * "Нанять сотрудника",
                                                                                           * "Уволить сотрудника" ,
                                                                                           */
            "Открыть новый склад", "Закрыть склад", "Открыть пункт продаж", "Закрыть пункт продаж",
            "Получить информацию о складе", "Получить информацию о пункте продаж",
            "Получить информацию о товарах на складе", "Получить информацию о товарах на пункте продаж",
            "Информация о доходности пункта продаж",
            "Информация о доходности предприятия" };
    Scanner sc = new Scanner(System.in);
    PointOfSalesMap pointOfSalesMap = new PointOfSalesMap();

    // public static String[] getUserFunction() {
    // return WORKERFUNCTION;
    // }

    public Worker(String userName, String userSecondName, String userPassword, String userLocation, String code)
            throws NoNameException, NoSecondNameException, NoPasswordException, NoLocationException {
        super(userName, userSecondName, userPassword, userLocation, code);
    }

    public void openStorage() {
        String newStorageLocation = sc.nextLine();
        StorageMap.getInstance().getAllStorages().add(new Storage(newStorageLocation));
    }

    public void closeStorage(String locationOfStorageToClose) {
        Iterator<Storage> storagIterator = StorageMap.getInstance().getAllStorages().iterator();
        while (storagIterator.hasNext()) {
            Storage nextStorage = storagIterator.next();
            if (nextStorage.getId().equals(locationOfStorageToClose)) {
                storagIterator.remove();
            }
        }
    }

    public void openPointOfSales(String locationOfPointOfSales) {
        PointOfSalesMap.getInstance().getAllPointOfSaleses().add(new PointOfSales(locationOfPointOfSales));
    }

    public void closePointOfSales(String locationOfPoint) {
        Iterator<PointOfSales> pointOfSalesIterator = PointOfSalesMap.getInstance().getAllPointOfSaleses().iterator();
        while (pointOfSalesIterator.hasNext()) {
            PointOfSales nextPointOfSales = pointOfSalesIterator.next();
            if (nextPointOfSales.getLocation().equals(locationOfPoint)) {
                pointOfSalesIterator.remove();
            }
        }
    }

    public void sendProductToAnotherStorage(String nameOfStorageFrom, String nameOfStorageTo, String idOfProduct)
            throws NoLocationException {
        StorageMap.getInstance().getStorageByLocation(nameOfStorageFrom).moveProduct(
                StorageMap.getInstance().getStorageByLocation(nameOfStorageTo),
                idOfProduct);
    }

    public void getInfoAboutStorage(String locationOfStorage) throws NoLocationException {
        System.out
                .println("Всего на складе " + StorageMap.getInstance().getStorageByLocation(locationOfStorage).getSize()
                        + " различных видов товаров");
    }

    public void getInfoAboutPointOfSales(String locationOfPointOfSales) {
        try {
            System.out.println("Всего в пункте продаж "
                    + pointOfSalesMap.getPointOfSalesByLocation(locationOfPointOfSales).getProductStatus()
                    + " различных товаров");
        } catch (Exception e) {
            System.out.println("Неправильно введена локация");
            e.printStackTrace();
        }
    }

    public void getInfoAboutProductOnStorage(String loctationOfStorage) throws NoLocationException {
        for (Cell cell : StorageMap.getInstance().getStorageByLocation(loctationOfStorage).getAllCells()) {
            System.out.println(cell.getInfo());
        }
    }

    public void getInfoAboutProductOnPointOfSales(String locationOfPoint) {
        try {
            for (Product product : pointOfSalesMap.getPointOfSalesByLocation(locationOfPoint).getAllProduct()) {
                System.out.println(product.getInfo());
            }
        } catch (Exception e) {
            System.out.println("Нет такого товара");
            e.printStackTrace();
        }
    }

    public void getInfoAboutProfitOfPoint(String locationOfPoint) {
        try {
            pointOfSalesMap.getPointOfSalesByLocation(locationOfPoint).getInfoAboutProfit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}