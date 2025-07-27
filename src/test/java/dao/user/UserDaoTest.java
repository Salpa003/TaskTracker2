package dao.user;

import entity.user.User;
import exceptions.user.UserAlreadyExists;
import org.junit.jupiter.api.*;
import util.ConnectionPoolAdapter;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserDaoTest {

    private UserDao userDao;

    private User user;

    @BeforeAll
    void setFields() {
        userDao = UserDao.getINSTANCE();
        user = new User("testUser1", "testUser");
    }

    @Test
    void save() {
        try {
            userDao.save(user);
        } catch (UserAlreadyExists e) {
            throw new RuntimeException("Error in creating user");
        }
        Optional<User> maybeUser = userDao.get(user.getId());
        assertThat(maybeUser).isPresent();
        assertThat(maybeUser.get()).isEqualTo(user);
    }

    @Test
    void delete() {
        try {
            userDao.save(user);
        } catch (UserAlreadyExists e) {
            throw new RuntimeException("Error in creating user");
        }

        boolean delete = userDao.delete(user.getId());
        assertThat(delete).isTrue();

        Optional<User> maybeUser = userDao.get(user.getId());
        assertThat(maybeUser).isEmpty();
    }

    @Test
    void update() {
        try {
            userDao.save(user);
        } catch (UserAlreadyExists e) {
            throw new RuntimeException("Error in creating user");
        }

        String newLogin = "CoCoCo";
        user.setLogin(newLogin);

        boolean update = userDao.update(user);
        assertThat(update).isTrue();

        Optional<User> maybeUser = userDao.get(user.getId());
        assertThat(maybeUser).isPresent();
        assertThat(maybeUser.get().getLogin()).isEqualTo(newLogin);
    }

    @AfterEach
    void removeUser() {
        userDao.delete(user.getId());
    }

    @AfterAll
    void closeConnection() {
        ConnectionPoolAdapter.terminate();
    }


}
