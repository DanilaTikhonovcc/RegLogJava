import java.util.List;

public interface UserDao {
    void saveMyUser(User user);

    void deleteMyUser(User user);

    void updateMyUser(User user);

    List<User> getAllMyUser();

    User getUserByLogin(String login);
}
