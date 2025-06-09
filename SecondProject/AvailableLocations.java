import java.io.Serializable;
import java.util.ArrayList;

public class AvailableLocations implements Serializable {
    public ArrayList<String> availableLocations = new ArrayList<String>();

    public AvailableLocations() {
        availableLocations.add("Вахитовский район");
        availableLocations.add("Приволжский район");
        availableLocations.add("Cоветский район");
        availableLocations.add("Кировский район");
        availableLocations.add("Московский район");
        availableLocations.add("Ново-Савиновский район");
        availableLocations.add("Авиастроительный район");
    }

    public ArrayList<String> getAvailableLocations() {
        return availableLocations;
    }

}