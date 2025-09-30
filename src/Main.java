import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AppRegAuth authSystem = new AppRegAuth();

        // Добавляем обработчик завершения для закрытия соединений
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (authSystem.getUserDao() instanceof UserDaoImplBD) {
                ((UserDaoImplBD) authSystem.getUserDao()).closeConnection();
            }
        }));

        while (true) {
            System.out.println("\n=== Система регистрации и авторизации ===");
            System.out.println("1 - Регистрация");
            System.out.println("2 - Авторизация");
            System.out.println("3 - Показать всех пользователей");
            System.out.println("4 - Сохранить данные в файл");
            System.out.println("5 - Показать Json файл");
            System.out.println("6 - Переключиться на БД");
            System.out.println("7 - Переключиться на файловое хранилище");
            System.out.println("8 - Выход");
            System.out.print("Выберите действие: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    handleRegistration(scanner, authSystem);
                    break;

                case "2":
                    handleAuthorization(scanner, authSystem);
                    break;

                case "3":
                    authSystem.showAllUsers();
                    break;

                case "4":
                    authSystem.saveAllUsersToFile();
                    break;

                case "5":
                    authSystem.showJsonFile();
                    break;

                case "6":
                    authSystem.setUserDao(new UserDaoImplBD());
                    System.out.println("Переключились на хранение в БД");
                    break;

                case "7":
                    authSystem.setUserDao(new UserDaoImplFile());
                    System.out.println("Переключились на файловое хранилище");
                    break;

                case "8":
                    System.out.println("Выход из программы. До свидания!");
                    if (authSystem.getUserDao() instanceof UserDaoImplBD) {
                        ((UserDaoImplBD) authSystem.getUserDao()).closeConnection();
                    }
                    scanner.close();
                    return;

                default:
                    System.out.println("Неверный выбор! Попробуйте снова.");
            }
        }
    }

    private static void handleRegistration(Scanner scanner, AppRegAuth authSystem) {
        System.out.print("Введите логин для регистрации: ");
        String regLogin = scanner.nextLine().trim();

        if (regLogin.isEmpty()) {
            System.out.println("Ошибка: логин не может быть пустым!");
            return;
        }

        System.out.print("Введите пароль: ");
        String regPassword = scanner.nextLine().trim();

        if (regPassword.isEmpty()) {
            System.out.println("Ошибка: пароль не может быть пустым!");
            return;
        }

        if (authSystem.registr(regLogin, regPassword)) {
            System.out.println("Регистрация успешна!");
        }
    }

    private static void handleAuthorization(Scanner scanner, AppRegAuth authSystem) {
        System.out.print("Введите логин: ");
        String authLogin = scanner.nextLine().trim();

        if (authLogin.isEmpty()) {
            System.out.println("Ошибка: логин не может быть пустым!");
            return;
        }

        System.out.print("Введите пароль: ");
        String authPassword = scanner.nextLine().trim();

        if (authPassword.isEmpty()) {
            System.out.println("Ошибка: пароль не может быть пустым!");
            return;
        }

        if (authSystem.auth(authLogin, authPassword)) {
            System.out.printf("Привет! %s\n", authLogin);
            System.out.println("Авторизация успешна!");
        }
    }
}