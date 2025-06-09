import java.io.Serializable;
import java.util.ArrayList;

public class StorageMap implements Serializable {
    private static StorageMap instance;
    private ArrayList<Storage> storageMap = new ArrayList<>();

    public static StorageMap getInstance() {
        if (instance == null) {
            instance = new StorageMap();
        }
        return instance;
    }

    protected Object readResolve() {
        instance = this; // Обновляем статическую ссылку
        return this;
    }

    // protected Object readResolve() {
    // return INSTANCE;
    // }

    public boolean findTheNearestStorage(String productId, String userLocation) {
        boolean findNearestStorage = false;
        for (Storage store : StorageMap.getInstance().getAllStorages()) {
            if (store.findElem(productId) & store.getLocation().equals(userLocation)) {
                findNearestStorage = true;
            }
        }
        return findNearestStorage;
    }

    public ArrayList<Storage> getAllStorages() {
        return storageMap;
    }

    public void addStorage(String location) {
        StorageMap.getInstance().getAllStorages().add(new Storage(location));
    }

    public Storage getStorageByLocation(String locationOfStorage) throws NoLocationException {
        Storage returnedStorage = null;
        for (Storage storage : StorageMap.getInstance().getAllStorages()) {
            if (storage.getLocation().equals(locationOfStorage)) {
                returnedStorage = storage;
            }
        }
        if (returnedStorage == null) {
            throw new NoLocationException(locationOfStorage);
        }
        return returnedStorage;
    }
}
