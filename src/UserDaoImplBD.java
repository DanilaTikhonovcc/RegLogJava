import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImplBD implements UserDao {

    private static final String URL = "jdbc:sqlite:users.db";
    private Connection connection;

    public UserDaoImplBD() {
        initializeDatabase();
    }

    private void initializeDatabase() {
        try {
            connection = DriverManager.getConnection(URL);
            String createTableSQL = "CREATE TABLE IF NOT EXISTS users (" +
                    "login TEXT PRIMARY KEY, " +
                    "password TEXT NOT NULL)";
            Statement statement = connection.createStatement();
            statement.execute(createTableSQL);
            System.out.println("База данных инициализирована");
        } catch (SQLException e) {
            System.out.println("Ошибка инициализации БД: " + e.getMessage());
        }
    }

    @Override
    public void saveMyUser(User user) {
        String sql = "INSERT INTO users(login, password) VALUES(?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, user.getLogin());
            pstmt.setString(2, user.getPassword());
            pstmt.executeUpdate();
            System.out.println("Пользователь " + user.getLogin() + " сохранен в БД");
        } catch (SQLException e) {
            System.out.println("Ошибка сохранения пользователя в БД: " + e.getMessage());
        }
    }

    @Override
    public void deleteMyUser(User user) {
        String sql = "DELETE FROM users WHERE login = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, user.getLogin());
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Пользователь " + user.getLogin() + " удален из БД");
            } else {
                System.out.println("Пользователь " + user.getLogin() + " не найден в БД");
            }
        } catch (SQLException e) {
            System.out.println("Ошибка удаления пользователя из БД: " + e.getMessage());
        }
    }

    @Override
    public void updateMyUser(User user) {
        String sql = "UPDATE users SET password = ? WHERE login = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, user.getPassword());
            pstmt.setString(2, user.getLogin());
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Пользователь " + user.getLogin() + " обновлен в БД");
            } else {
                System.out.println("Пользователь " + user.getLogin() + " не найден для обновления");
            }
        } catch (SQLException e) {
            System.out.println("Ошибка обновления пользователя в БД: " + e.getMessage());
        }
    }

    @Override
    public List<User> getAllMyUser() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT login, password FROM users";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String login = rs.getString("login");
                String password = rs.getString("password");
                users.add(new User(login, password));
            }
        } catch (SQLException e) {
            System.out.println("Ошибка получения пользователей из БД: " + e.getMessage());
        }

        return users;
    }

    @Override
    public User getUserByLogin(String login) {
        String sql = "SELECT login, password FROM users WHERE login = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, login);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String userLogin = rs.getString("login");
                String userPassword = rs.getString("password");
                return new User(userLogin, userPassword);
            }
        } catch (SQLException e) {
            System.out.println("Ошибка поиска пользователя в БД: " + e.getMessage());
        }

        return null;
    }

    // Метод для закрытия соединения (важно вызывать при завершении работы)
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Соединение с БД закрыто");
            }
        } catch (SQLException e) {
            System.out.println("Ошибка закрытия соединения с БД: " + e.getMessage());
        }
    }
}