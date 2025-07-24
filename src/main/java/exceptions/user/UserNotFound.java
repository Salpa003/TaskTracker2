package exceptions.user;

public class UserNotFound extends Exception {
    public UserNotFound() {
        super("Такой пользователь не существует!");
    }
}
