import java.util.List;
import java.util.ArrayList;

public class AppRegAuth {
    private List<User> users;
    private UserDao userDao;

    public AppRegAuth() {
        this.users = new ArrayList<>();
        this.userDao = new UserDaoImplFile();
        loadUsersFromDao();
    }

    public AppRegAuth(UserDao userDao) {
        this.userDao = userDao;
        loadUsersFromDao();
    }

    public boolean registr(String login, String password) {
        if (getUserByLogin(login) != null) {
            System.out.println("Ошибка: пользователь с логином '" + login + "' уже существует!");
            return false;
        }

        User newUser = new User(login, password);
        users.add(newUser);
        userDao.saveMyUser(newUser);
        System.out.println("Регистрация прошла успешно!");
        return true;
    }

    public boolean auth(String login, String password) {
        for (User user: users) {
            if (user.getLogin().equals(login) && user.getPassword().equals(password)) {
                System.out.println("Авторизация прошла успешно! Добро пожаловать, " + login);
                return true;
            }
        }
        System.out.println("Неверный логин или пароль");
        return false;
    }

    public List<User> getUsers() {
        return new ArrayList<>(users);
    }

    public void showAllUsers() {
        System.out.println("\n--- Зарегистрированные пользователи ---");
        if (users.isEmpty()) {
            System.out.println("Пользователей нет.");
        } else {
            for (int i = 0; i < users.size(); i++) {
                System.out.println((i + 1) + ". " + users.get(i).getLogin());
            }
        }
    }

    public User getUserByLogin(String login) {
        for (User user : users) {
            if (user.getLogin().equals(login)) {
                return user;
            }
        }
        return null;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
        loadUsersFromDao();
    }

    private void loadUsersFromDao() {
        this.users = userDao.getAllMyUser();
    }

    public void showJsonFile() {
        if (userDao instanceof UserDaoImplFile) {
            ((UserDaoImplFile) userDao).printJsonFileContent();
        }
    }

    public void saveAllUsersToFile() {
        if (userDao instanceof UserDaoImplFile) {
            ((UserDaoImplFile) userDao).saveAllUsers(users);
        } else {
            System.out.println("Сохранение в файл доступно только для файлового хранилища");
        }
    }

    public UserDao getUserDao() {
        return this.userDao;
    }

}