import java.io.FileOutputStream;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.util.Scanner;
import java.util.function.ObjDoubleConsumer;

public class UI implements Serializable {
    AvailableLocations availableLocations = new AvailableLocations();
    PointOfSalesMap pointOfSalesMap = PointOfSalesMap.getInstance();
    StorageMap storageMap = StorageMap.getInstance();

    public void showMenu(User user) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Добро пожаловать " + user.getName());
        int counter = 1;
        if (user.getUserStatus() == 0) {
            Worker activeWorker = null;
            try {
                activeWorker = new Worker(user.getName(), user.getSecondName(), user.getUserPassword(),
                        user.getLocation(), "worker");
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("Вы можете:");
            for (String function : Worker.WORKERFUNCTION) {
                System.out.println(counter++ + ")" + function);
            }
            // String option = sc.nextLine().trim();
            String option = Integer.toString(sc.nextInt());
            sc.nextLine();
            while (!option.equals("0")) {
                switch (option.trim()) {
                    case ("1"):
                        if (storageMap.getAllStorages().size() >= 2) {
                            for (Storage storage : storageMap.getAllStorages()) {
                                System.out.println(storage.getLocation());
                            }
                            System.out.println(
                                    "Пожалуйста, введите расположение склада из которого нужно перекинуть товары");
                            try {
                                String nameOfStorageFrom = sc.nextLine();
                                Storage storageFrom = storageMap.getStorageByLocation(nameOfStorageFrom);
                                System.out.println(
                                        "Пожалуйста, введите id(Имя:Цена) товара из присутствующих на складе:");
                                for (Cell cell : storageFrom.getAllCells()) {
                                    System.out.println(cell.getInfo());
                                }
                                String idOfProduct = sc.nextLine();
                                System.out.println("Пожалуйста, введите имя склада, куда отправятся товары");
                                String nameOfStrorageTo = sc.nextLine();
                                activeWorker.sendProductToAnotherStorage(nameOfStorageFrom, nameOfStrorageTo,
                                        idOfProduct);
                                System.out.println("Отлично, товары уже отправлены на нужный склад");
                            } catch (NoLocationException ex) {
                                System.out.println("Не удалось получить storage по локации");
                            }
                        } else {
                            System.out.println("Хм, похоже у вас не так уж и много складов");
                        }
                        break;
                    case ("2"):
                        System.out.println("Локации доступных магазинов:");
                        for (Storage storage : storageMap.getAllStorages()) {
                            System.out.println(storage.getLocation());
                        }
                        System.out.println("Пожалуйста, введите локации нужной точки");
                        String selectingStore = sc.nextLine().trim();
                        boolean findStorage = false;
                        for (Storage storage : storageMap.getAllStorages()) {
                            if (storage.getLocation().equals(selectingStore)) {
                                System.out.println("Вот все имеющиеся товары на складе:");
                                for (Cell cell : storage.getAllCells()) {
                                    System.out.println(cell.getInfo());
                                }
                                findStorage = true;
                                System.out.println("Введите имя товара");
                                String nameOfProduct = sc.nextLine().trim();
                                System.out.println("Введите цену товара");
                                String productPrice = sc.nextLine().trim();
                                int tryToParse = 0;
                                try {
                                    tryToParse = Integer.parseInt(productPrice);
                                } catch (NumberFormatException e) {
                                    System.out.println("Вы не можете ввести такое число");
                                }
                                System.out.println("Введите количество товара");
                                int countOfProduct = Integer.parseInt(sc.nextLine());
                                storage.addProduct(nameOfProduct, productPrice, countOfProduct, storage.getLocation(),
                                        null);
                            }
                        }
                        if (!findStorage) {
                            System.out.println("Похоже такой точки ещй не открыто");
                        }
                        break;
                    case ("3"):
                        System.out.println("Открытые на текущий момент склады:");
                        for (Storage storage : storageMap.getAllStorages()) {
                            System.out.println(storage.getLocation());
                        }
                        System.out.println("Введите локацию нового склада из имеющихся, ещё не открытых");
                        String locationOfStorage = sc.nextLine().trim();
                        if (!availableLocations.getAvailableLocations().contains(locationOfStorage)) {
                            System.out.println("Пожалуйста, введите существующую локацию");
                            locationOfStorage = sc.nextLine().trim();
                            if (availableLocations.getAvailableLocations().contains(locationOfStorage)) {
                                boolean notFindStorage = true;
                                for (Storage storage : storageMap.getAllStorages()) {
                                    if (storage.getLocation().equals(locationOfStorage) & notFindStorage) {
                                        System.out.println("Пожалуйса, введите адрес склада, который ещё не открыт");
                                        locationOfStorage = sc.nextLine().trim();
                                        notFindStorage = false;
                                    }
                                }
                                storageMap.addStorage(locationOfStorage);
                            }
                        }
                        break;
                    case ("4"):
                        System.out.println("Введите, какой склад вы хотели бы закрыть, из имеющихся на данный момент");
                        for (Storage storage : storageMap.getAllStorages()) {
                            System.out.println(storage.getLocation());
                        }
                        String locationOfCloseStorage = sc.nextLine();
                        if (!availableLocations.getAvailableLocations().contains(locationOfCloseStorage)) {
                            System.out.println("Пожалуйста, введите существующую локацию");
                            locationOfCloseStorage = sc.nextLine();
                            if (availableLocations.getAvailableLocations().contains(locationOfCloseStorage)) {
                                boolean findStore = false;
                                for (Storage storage : storageMap.getAllStorages()) {
                                    if (storage.getLocation().equals(locationOfCloseStorage)) {
                                        findStore = true;
                                    }
                                }
                                if (!findStore) {
                                    System.out.println("Введите из имеющихся");
                                    locationOfCloseStorage = sc.nextLine();
                                }
                                activeWorker.closeStorage(locationOfCloseStorage);
                            }
                        }
                        break;
                    case ("5"):
                        System.out
                                .println("Введите, какой пункт продаж вы бы хотели открыть из тех, что ещё не открыты");
                        for (PointOfSales pointOfSales : pointOfSalesMap.getAllPointOfSaleses()) {
                            System.out.println(pointOfSales.getLocation());
                        }
                        String locationOfNewPointOfSales = sc.nextLine().trim();
                        if (!availableLocations.getAvailableLocations().contains(locationOfNewPointOfSales)) {
                            System.out.println("Пожалуйста, введите существующую локацию");
                            locationOfNewPointOfSales = sc.nextLine();
                            if (availableLocations.getAvailableLocations().contains(locationOfNewPointOfSales)) {
                                boolean notFindPointOfSales = true;
                                for (PointOfSales pointOfSales : pointOfSalesMap.getAllPointOfSaleses()) {
                                    if (pointOfSales.getLocation().equals(locationOfNewPointOfSales)
                                            & notFindPointOfSales) {
                                        System.out.println("Пожалуйса, введите адрес пункта, который ещё не открыт");
                                        locationOfNewPointOfSales = sc.nextLine();
                                        notFindPointOfSales = false;
                                    }
                                }
                                activeWorker.openPointOfSales(locationOfNewPointOfSales);
                            }
                        }
                        break;
                    case ("6"):
                        System.out.println("Введите, какой пункт продаж вы бы хотели закрыть");
                        for (PointOfSales pointOfSales : pointOfSalesMap.getAllPointOfSaleses()) {
                            System.out.println(pointOfSales.getLocation());
                        }

                        String locationOfClosePointOfSales = sc.nextLine();
                        if (!availableLocations.getAvailableLocations().contains(locationOfClosePointOfSales)) {
                            System.out.println("Пожалуйста, введите существующую локацию");
                            locationOfClosePointOfSales = sc.nextLine();
                            if (availableLocations.getAvailableLocations().contains(locationOfClosePointOfSales)) {
                                boolean findClosePointOfSales = false;
                                for (PointOfSales pointOfSales : pointOfSalesMap.getAllPointOfSaleses()) {
                                    if (pointOfSales.getLocation().equals(locationOfClosePointOfSales)) {
                                        findClosePointOfSales = true;
                                    }
                                }
                                if (findClosePointOfSales) {
                                    System.out.println("Введите из имеющихся");
                                    locationOfCloseStorage = sc.nextLine();
                                }
                                activeWorker.closePointOfSales(locationOfClosePointOfSales);
                            }
                        }
                        break;
                    case ("7"):
                        if (storageMap.getAllStorages().size() > 0) {
                            System.out.println("Введите район склада, о котором хотите получить информацию");
                            for (Storage storage : storageMap.getAllStorages()) {
                                System.out.println(storage.getLocation());
                            }
                            String locationOfStorageInformation = sc.nextLine();
                            if (!availableLocations.getAvailableLocations().contains(locationOfStorageInformation)) {
                                System.out.println("Пожалуйста, введите существующую локацию");
                                locationOfStorageInformation = sc.nextLine();
                                if (availableLocations.getAvailableLocations()
                                        .contains(locationOfStorageInformation)) {
                                    boolean findInformation = false;
                                    for (Storage storage : storageMap.getAllStorages()) {
                                        if (storage.getLocation().equals(locationOfStorageInformation)) {
                                            findInformation = true;
                                        }
                                    }
                                    if (!findInformation) {
                                        System.out.println("Введите из имеющихся");
                                        locationOfCloseStorage = sc.nextLine();
                                    }
                                    try {
                                        activeWorker.getInfoAboutStorage(locationOfStorageInformation);
                                    } catch (NoLocationException ex) {
                                        System.out.println("Пожалуйста, введите название склада корректно");
                                    }
                                }
                            }
                        } else {
                            System.out.println("Для начала, вам следует откыть хотя бы 1 склад");
                        }
                        break;
                    case ("8"):
                        if (pointOfSalesMap.getAllPointOfSaleses().size() > 0) {
                            System.out.println("Введите район пункта продаж, о котором хотите получить информацию");
                            for (PointOfSales pointOfSales : pointOfSalesMap.getAllPointOfSaleses()) {
                                System.out.println(pointOfSales.getLocation());
                            }
                            String locationOfInfoPointOfSales = sc.nextLine();
                            if (!availableLocations.getAvailableLocations().contains(locationOfInfoPointOfSales)) {
                                System.out.println("Пожалуйста, введите существующую локацию");
                                locationOfNewPointOfSales = sc.nextLine();
                                if (!availableLocations.getAvailableLocations().contains(locationOfInfoPointOfSales)) {
                                    boolean findPointOfSales = false;

                                    for (PointOfSales pointOfSales : pointOfSalesMap.getAllPointOfSaleses()) {
                                        if (pointOfSales.getLocation().equals(locationOfInfoPointOfSales)) {
                                            findPointOfSales = true;
                                        }
                                    }
                                    if (!findPointOfSales) {
                                        System.out.println("Введите из имеющихся");
                                        locationOfCloseStorage = sc.nextLine();
                                    }
                                    activeWorker.getInfoAboutPointOfSales(locationOfInfoPointOfSales);
                                }
                            }
                        } else {
                            System.out.println("Для начала, вам следует откыть хотя бы 1 точку");
                        }
                        break;
                    case ("9"):
                        if (storageMap.getAllStorages().size() > 0) {
                            System.out.println("Введите район склада, о товарах которого хотите получить информацию");
                            for (Storage storage : storageMap.getAllStorages()) {
                                System.out.println(storage.getLocation());
                            }
                            String locationOfStorageInformation = sc.nextLine().trim();
                            if (!availableLocations.getAvailableLocations().contains(locationOfStorageInformation)) {
                                System.out.println("Пожалуйста, введите существующую локацию");
                                locationOfStorageInformation = sc.nextLine();
                                if (availableLocations.getAvailableLocations().contains(locationOfStorageInformation)) {
                                    boolean findInformation = false;
                                    for (Storage storage : storageMap.getAllStorages()) {
                                        if (storage.getLocation().equals(locationOfStorageInformation)) {
                                            findInformation = true;
                                        }
                                    }
                                    if (!findInformation) {
                                        System.out.println("Введите из имеющихся");
                                        locationOfStorageInformation = sc.nextLine();
                                    }
                                    try {
                                        activeWorker.getInfoAboutProductOnStorage(locationOfStorageInformation);
                                    } catch (NoLocationException ex) {
                                        System.out.println("Пожалуйста, введите название склада корректно");
                                    }
                                }
                            }
                        } else {
                            System.out.println("Для начала, вам следует откыть хотя бы 1 склад");
                        }
                        break;
                    case ("10"):
                        if (pointOfSalesMap.getAllPointOfSaleses().size() > 0) {
                            System.out.println(
                                    "Введите район точки продаж, о товарах которой хотите получить информацию");
                            for (PointOfSales pointOfSales : pointOfSalesMap.getAllPointOfSaleses()) {
                                System.out.println(pointOfSales.getLocation());
                            }
                            String locationOfStorageInformation = sc.nextLine();
                            if (!availableLocations.getAvailableLocations().contains(locationOfStorageInformation)) {
                                System.out.println("Пожалуйста, введите существующую локацию");
                                locationOfStorageInformation = sc.nextLine();
                                if (availableLocations.getAvailableLocations().contains(locationOfStorageInformation)) {
                                    boolean findInformation = false;

                                    for (PointOfSales pointOfSales : pointOfSalesMap.getAllPointOfSaleses()) {
                                        if (pointOfSales.getLocation().equals(locationOfStorageInformation)) {
                                            findInformation = true;
                                        }
                                    }
                                    if (!findInformation) {
                                        System.out.println("Введите из имеющихся");
                                        locationOfCloseStorage = sc.nextLine();
                                    }
                                    activeWorker.getInfoAboutProductOnPointOfSales(locationOfStorageInformation);
                                }
                            }

                        } else {
                            System.out.println("Для начала, вам следует откыть хотя бы 1 точку");
                        }
                        break;
                    case ("11"):
                        System.out.println("Введите район точки продаж, доходность которой хотите узнать");
                        for (PointOfSales pointOfSales : pointOfSalesMap.getAllPointOfSaleses()) {
                            System.out.println(pointOfSales.getLocation());
                        }
                        String locationOfStorageInformation = sc.nextLine();
                        sc.nextLine();

                        if (!availableLocations.getAvailableLocations().contains(locationOfStorageInformation)) {
                            System.out.println("Пожалуйста, введите существующую локацию");
                            locationOfStorageInformation = sc.nextLine();
                            if (!availableLocations.getAvailableLocations().contains(locationOfStorageInformation)) {
                                boolean findInformation = false;
                                for (PointOfSales pointOfSales : pointOfSalesMap.getAllPointOfSaleses()) {
                                    if (pointOfSales.getLocation().equals(locationOfStorageInformation)) {
                                        findInformation = true;
                                    }
                                }
                                if (!findInformation) {
                                    System.out.println("Введите из имеющихся");
                                    locationOfCloseStorage = sc.nextLine();
                                }
                                activeWorker.getInfoAboutProductOnPointOfSales(locationOfStorageInformation);
                            }
                        }
                        break;

                    default:
                        throw new AssertionError();
                }
                System.out.println("Введите следующую команду, если хотите завершить работу, то нажмите 0");
                option = sc.nextLine().trim();
            }
        } else if (user.getUserStatus() == 1) {
            Buyer activeBuyer = null;
            for (String function : Buyer.BUYERFUNCTION) {
                System.out.println(counter++ + ")" + function);
            }
            try {
                activeBuyer = new Buyer(user.getName(), user.getSecondName(), user.getUserPassword(),
                        user.getLocation(), null, user.getBasket());
                for (String idOfProduct : user.getBasket()) {
                    System.out.println(idOfProduct);
                }
                System.out.println(user.getBasket().equals(activeBuyer.getBasket()));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            String option = Integer.toString(sc.nextInt());
            sc.nextLine();
            while (!option.equals("0")) {
                switch (option) {
                    case "1":
                        System.out.println();
                        System.out.println("Каталог:");
                        for (Product product : AllProducts.getInstance().getAllProducts()) {
                            System.out.println(product.getFullInfo());
                        }
                        System.out.println("Введитe, id(Имя:Цена) товара, который вы бы хотели купить");
                        String pickProduct = sc.nextLine().trim();
                        try {
                            activeBuyer.buy(pickProduct);
                            user.addProduct(pickProduct);
                        } catch (NotEnoughMoneyException ex) {
                            System.out.println("Пожалуйста, пополните баланс");
                            activeBuyer.putMoney(sc.nextInt());
                            sc.nextLine();
                        }
                        break;
                    case "2":
                        if (user.getBasket().size() > 0) {
                            System.out.println("В вашей корзине:");
                            for (String idOfProduct : user.getBasket()) {
                                System.out.println(idOfProduct);
                            }
                            System.out.println("Введите id товара, который хотите вернуть");
                            String returnProduct = sc.nextLine().trim();
                            try {
                                activeBuyer.returnProduct(returnProduct);
                                user.removeProductById(returnProduct);
                            } catch (NoProductException ex) {
                                System.out.println("Похоже, вы ввели неправильный id");
                            } catch (NoLocationException ex) {
                                System.out.println("Пожалуйста, введите название склада корректно");
                            }
                        } else {
                            System.out.println("Ваша корзина пуста");
                        }
                        break;
                    case "3":
                        if (user.getBasket().size() > 0) {
                            System.out.println("В вашей корзине:");
                            for (String idOfProduct : user.getBasket()) {
                                System.out.println(idOfProduct);
                            }
                            activeBuyer.takeProduct(user.getBasket());
                            user.clearBasket();
                            System.out.println("Вы забрали товары");
                        } else {
                            System.out.println("Увы, вам нечего забирать");
                        }
                        break;
                    default:
                        break;
                }
                System.out.println("Введите команду");
                option = sc.nextLine().trim();
            }
        }
    }
}