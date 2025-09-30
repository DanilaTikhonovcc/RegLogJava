import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImplFile implements UserDao {
    private static final String FILE_NAME = "users.json";
    private final Gson gson;

    public UserDaoImplFile() {
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
    }

    @Override
    public void saveMyUser(User user) {
        List<User> users = getAllMyUser();
        users.add(user);
        saveAllUsers(users);
        System.out.println("Пользователь" + user.getLogin() + " сохранен.");
    }

    @Override
    public void deleteMyUser(User user) {
        List<User> users = getAllMyUser();
        users.remove(user);
        saveAllUsers(users);
        System.out.println();
    }

    @Override
    public void updateMyUser(User user) {
        List<User> users = getAllMyUser();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getLogin().equals(user.getLogin())) {
                users.set(i, user);
                break;
            }
        }
        saveAllUsers(users);
        System.out.println("Пользователь " + user.getLogin() + " обновлен");
    }

    @Override
    public List<User> getAllMyUser() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (Reader reader = new FileReader(FILE_NAME)) {
            Type userListType = new TypeToken<ArrayList<User>>(){}.getType();
            List<User> users = gson.fromJson(reader, userListType);
            return users != null ? users : new ArrayList<>();
        } catch (IOException e) {
            System.out.println("Ошибка при чтении JSON файла: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public User getUserByLogin(String login) {
        List<User> users = getAllMyUser();
        for (User user : users) {
            if (user.getLogin().equals(login)) {
                return user;
            }
        }
        return null;
    }

    public void saveAllUsers(List<User> users)
    {
        try (Writer writer = new FileWriter(FILE_NAME)) {
            gson.toJson(users, writer);
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении в JSON файл: " + e.getMessage());
        }
    }

    public void printJsonFileContent() {
        try (Reader reader = new FileReader(FILE_NAME)) {
            Type userListType = new TypeToken<ArrayList<User>>(){}.getType();
            List<User> users = gson.fromJson(reader, userListType);

            if (users == null || users.isEmpty()) {
                System.out.println("JSON файл пуст");
                return;
            }

            String json = gson.toJson(users);
            System.out.println("Содержимое JSON файла:");
            System.out.println(json);
        } catch (IOException e) {
            System.out.println("Ошибка при чтении JSON файла: " + e.getMessage());
        }
}
}
