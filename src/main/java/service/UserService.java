package service;

import dao.user.UserDao;
import entity.user.User;
import exceptions.user.IncorrectPassword;
import exceptions.user.UserAlreadyExists;
import exceptions.user.UserNotFound;

import java.util.Arrays;
import java.util.List;

public class UserService {

    private final UserDao userDao = UserDao.getINSTANCE();

    private final List<String> testLogins = Arrays.asList("testUser1","testUser2");
    private boolean isTest = false;

    public boolean createUser(User user) throws UserAlreadyExists {
        if (testLogins.contains(user.getLogin()) && !isTest) {
            throw new UserAlreadyExists(user.getLogin());
        }
        userDao.save(user);
        return true;
    }

    public boolean deleteUser(User user) {
        return userDao.delete(user.getId());
    }

    public boolean login(String login, String password) throws UserNotFound, IncorrectPassword {
        boolean loginExist = userDao.findByLogin(login);
        boolean passwordAreRight = userDao.findByLoginAndPassword(login, password);

        if (passwordAreRight) {
            return true;
        } else if (loginExist) {
            throw new IncorrectPassword();
        } else {
            throw new UserNotFound();
        }
    }

    public void testStart() {
        isTest = true;
    }
}
