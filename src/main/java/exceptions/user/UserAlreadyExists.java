package exceptions.user;

public class UserAlreadyExists extends Exception {
    public UserAlreadyExists(String name) {
        super(name + " такой пользователь уже существует!");
    }
}
