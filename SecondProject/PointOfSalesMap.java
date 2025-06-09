import java.io.Serializable;
import java.util.ArrayList;

public class PointOfSalesMap implements Serializable {
    private static PointOfSalesMap instance;
    private ArrayList<PointOfSales> mapOfSales = new ArrayList<>();

    public static PointOfSalesMap getInstance() {
        if (instance == null) {
            instance = new PointOfSalesMap();
        }
        return instance;
    }

    protected Object readResolve() {
        instance = this; // Обновляем статическую ссылку
        return this;
    }

    public PointOfSales getPointOfSalesByLocation(String storageLocation) throws NoFindPointException {
        PointOfSales needPoint = null;
        for (PointOfSales pointOfSales : PointOfSalesMap.getInstance().getAllPointOfSaleses()) {
            if (pointOfSales.getLocation().equals(storageLocation)) {
                needPoint = pointOfSales;
            }
        }
        if (needPoint == null) {
            throw new NoFindPointException("Что-то пошло не так, возможно в вашем районе ещё нет постомата(");
        }
        return needPoint;
    }

    public ArrayList<PointOfSales> getAllPointOfSaleses() {
        return mapOfSales;
    }
}