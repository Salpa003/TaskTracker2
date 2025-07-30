package service;

import entity.user.User;
import exceptions.user.IncorrectPassword;
import exceptions.user.UserAlreadyExists;
import exceptions.user.UserNotFound;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserServiceTest {
    private UserService service;

    private User existUser;
    private User user;

    @BeforeAll
    void setFields() {
        service = new UserService();
        service.testStart();// Игнорировать Зареверсированные имена (testUser)
        createUsersForTests();
        System.out.println("eee");
    }

    @Test
    void createNewUser() {
        assertThatCode(() -> {
            boolean result = service.createUser(user);
            assertThat(result).isTrue();
        }).doesNotThrowAnyException();
    }

    @Test
    void createOldUser() {
        assertThatThrownBy(() -> service.createUser(existUser))
                .isInstanceOf(UserAlreadyExists.class)
                .hasMessageContaining(existUser.getLogin() + " такой пользователь уже существует!");
    }


    @Test
    void loginExistUser() {
        assertThatCode(() -> {
            boolean result = service.login(existUser.getLogin(), existUser.getPassword());
            assertThat(result).isTrue();
        }).doesNotThrowAnyException();
    }

    @Test
    void loginNotExistUser() {
        assertThatThrownBy(() -> service.login(user.getLogin(), user.getPassword()))
                .isInstanceOf(UserNotFound.class)
                .hasMessageContaining("Такой пользователь не существует!");
    }

    @Test
    void loginExistUserButNotPassword() {
        assertThatThrownBy(() -> service.login(existUser.getLogin(), existUser.getPassword() + "+incorrect chars"))
                .isInstanceOf(IncorrectPassword.class)
                .hasMessageContaining("Пароль не правильный");
    }

    @AfterEach
    void deleteUser() {
        service.deleteUser(user);
    }

    @AfterAll
    void closeConnectionPool() {
        service.deleteUser(existUser);
    }

    private void createUsersForTests() {
        user = new User("testUser1", "123");
        existUser = new User("testUser2", "123");

        try {
            service.createUser(existUser);
        } catch (UserAlreadyExists e) {
            throw new RuntimeException(e);
        }
    }
}
