import java.util.Iterator;
import java.util.ArrayList;
import java.io.Serializable;

public class User implements Serializable {
    private String userName;
    private String userSecondName;
    private String userID;
    private String userPassword;
    private String userLocation;
    private int userStatus = 1;
    private int counter = 1;
    private ArrayList<String> basket = new ArrayList<>();

    public User(String userName, String userSecondName, String userPassword, String userLocation, String code)
            throws NoNameException, NoSecondNameException, NoPasswordException, NoLocationException {
        AvailableLocations availableLocations = new AvailableLocations();
        if (userName == null) {
            throw new NoNameException("Пожалуйста, введите имя");
        } else if (userSecondName == null) {
            throw new NoSecondNameException("Пожалуйста, введите фамилию");
        } else if (userPassword == null) {
            throw new NoPasswordException("Пожалуйста, введите пароль");
        } else if (userLocation == null) {
            throw new NoLocationException("Пожалуйста, введите ваше место проживания");
        } else if (code == null) {
            code = "buyer";
        }

        if (code.equals("worker")) {
            this.userStatus = 0;
        } else {
            this.userStatus = 1;
        }

        this.userID = "U-" + counter++;
        this.userName = userName;
        this.userSecondName = userSecondName;
        this.userPassword = userPassword;
        this.userLocation = userLocation;
    }

    public String getId() {
        return userID;
    }

    public void addProduct(String idOfProduct) {
        basket.add(idOfProduct);
    }

    public String getName() {
        return userName;
    }

    public String getSecondName() {
        return userSecondName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public int getUserStatus() {
        return userStatus;
    }

    public String getLocation() {
        return userLocation;
    }

    public ArrayList<String> getBasket() {
        return basket;
    }

    public void clearBasket() {
        basket.clear();
    }

    public void removeProduct() {

    }

    public void removeProductById(String idOfProduct) {
        Iterator<String> basketIterator = basket.iterator();
        while (basketIterator.hasNext()) {
            String nextId = basketIterator.next();
            if (nextId.equals(idOfProduct)) {
                basketIterator.remove();
            }
        }

    }
}