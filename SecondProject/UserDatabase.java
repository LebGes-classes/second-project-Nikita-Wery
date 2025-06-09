import java.io.Serializable;
import java.util.ArrayList;

public class UserDatabase implements Serializable {

    private ArrayList<User> allUsers = new ArrayList<>();

    public User getUser(String name, String secondName, String password) throws UserNotFoundException {
        User findUser = null;
        for (User user : allUsers) {
            if (user.getName().equals(name) & user.getSecondName().equals(secondName)
                    & user.getUserPassword().equals(password)) {
                findUser = user;
                // Проверочный код нижу
                System.out.println("Пользователь найден");
            }
        }
        if (findUser == null) {
            throw new UserNotFoundException("Нет такого пользователя");
        }
        return findUser;
    }

    public void addNewUser(String userName, String userSecondName, String userPassword, String userLocation,
            String code) {
        try {
            allUsers.add(new User(userName, userSecondName, userPassword, userLocation, code));
            // Проверка создания нового пользователя
            System.out.println("Новый пользовтель, создан");
        } catch (NoNameException e) {
            e.printStackTrace();
            System.out.println("Пожалуйста, введите ваше имя");
        } catch (NoSecondNameException e) {
            e.printStackTrace();
            System.out.println("Пожалуйста, введите вашу фамилию");
        } catch (NoPasswordException e) {
            e.printStackTrace();
            System.out.println("Пожалуйста, введите пароль");
        } catch (NoLocationException e) {
            e.printStackTrace();
            System.out.println("Пожалуйста, выберите место проживания");
        }

    }
}