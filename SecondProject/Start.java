import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.*;
import java.util.Scanner;

public class Start implements Serializable {
    UserDatabase userDatabase = new UserDatabase();
    UI uiInterface = new UI();
    AvailableLocations availableLocations = new AvailableLocations();

    public static void main(String[] args) {
        Start start = new Start();
        try {
            // ObjectOutputStream os = new ObjectOutputStream(new
            // FileOutputStream("progress.ser");
            ObjectInputStream is = new ObjectInputStream(new FileInputStream("progress.ser"));
            start = (Start) is.readObject();
        } catch (Exception ex) {
            System.out.println("Похоже вы впревые используете это приложение!");
        }
        start.go();
        try {
            ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream("progress.ser"));
            os.writeObject(start);
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void go() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Войдите в личный кабинет:");
        System.out.println("Введите имя:");
        String nameOfUser = sc.nextLine();
        System.out.println("Введите фамилию");
        String secondNameOfUser = sc.nextLine();
        System.out.println("Введите пароль");
        String passwordOfUser = sc.nextLine();

        User user = null;
        try {
            user = userDatabase.getUser(nameOfUser, secondNameOfUser, passwordOfUser);
        } catch (UserNotFoundException ex) {
            System.out.println(
                    "Похоже вы новый пользователь, пожалуйста создайте аккаунт, для этого добавьте город проживания из присутсвующих:");
            for (String location : availableLocations.getAvailableLocations()) {
                System.out.println(location);
            }
            String cityOfUser = sc.nextLine();
            System.out.println(
                    "Если вы сотрудник комапании, то пожалуйста, введите специальный код, если нет, то просто нажмите Enter");
            String code = sc.nextLine();
            userDatabase.addNewUser(nameOfUser, secondNameOfUser, passwordOfUser, cityOfUser, code);
            try {
                user = userDatabase.getUser(nameOfUser, secondNameOfUser, passwordOfUser);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Пользователь не создался");
            }

        } finally {
            // здесь в любом случае должно показываться какое-то меню
            uiInterface.showMenu(user);
        }

    }
}
