package exceptions.user;

public class IncorrectPassword extends Exception {
    public IncorrectPassword() {
        super("Пароль не правильный");
    }
}
