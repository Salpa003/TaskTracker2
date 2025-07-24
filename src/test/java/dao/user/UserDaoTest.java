package dao.user;

import entity.user.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserDaoTest {

//    private UserDao userDao;
//
//    private int userId;
//    @BeforeAll
//    void setUserDao() {
//        userDao = UserDao.getINSTANCE();
//    }
//
//    @Test
//    void save() {
//        User user = new User("test2","testUser");
//        boolean save = userDao.save(user);
//        assertThat(save).isTrue();
//
//        userId = user.getId();
//
//        Optional<User> maybeUser = userDao.get(user.getId());
//        assertThat(maybeUser).isPresent();
//        assertThat(maybeUser.get()).isEqualTo(user);
//    }
//
//    @Test
//    void delete() {
//        User user = new User("test2","testUser");
//        boolean save = userDao.save(user);
//        assertThat(save).isTrue();
//
//        boolean delete = userDao.delete(user.getId());
//        assertThat(delete).isTrue();
//
//        Optional<User> maybeUser = userDao.get(user.getId());
//        assertThat(maybeUser).isEmpty();
//    }
//
//    @Test
//    void update() {
//        User user = new User("test2","testUser");
//        boolean save = userDao.save(user);
//        assertThat(save).isTrue();
//
//        String newLogin = "CoCoCo";
//        user.setLogin(newLogin);
//
//        boolean update = userDao.update(user);
//        assertThat(update).isTrue();
//
//        userId = user.getId();
//
//        Optional<User> maybeUser = userDao.get(user.getId());
//        assertThat(maybeUser).isPresent();
//        assertThat(maybeUser.get().getLogin()).isEqualTo(newLogin);
//    }
//
//    @AfterEach
//    void removeUser() {
//        userDao.delete(userId);
//    }
//


}
