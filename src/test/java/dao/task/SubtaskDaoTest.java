package dao.task;

import dao.user.UserDao;
import entity.task.Epic;
import entity.task.Subtask;
import entity.task.Task;
import entity.task.TaskStatus;
import entity.user.User;
import exceptions.user.UserAlreadyExists;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SubtaskDaoTest {

    private SubtaskDao dao;

    private Subtask subtask;

    private int userId;

    @BeforeAll
    void setFields() {
        dao = SubtaskDao.getInstance();
        userId = createUser();
        subtask = new Subtask("subtask","descritpion", TaskStatus.NEW, Duration.ZERO,
                LocalDateTime.of(2025,1,1,1,1,1),
                LocalDateTime.of(2025,1,1,1,1,1),
                userId, null);
    }



   // @Test
    void saveAndGet() {
        boolean save = dao.save(subtask);
        assertThat(save).isTrue();

        Optional<Subtask> maybeTask = dao.get(subtask.getId());
        assertThat(maybeTask).isPresent();
        assertThat(maybeTask.get()).isEqualTo(subtask);
        assertThat(maybeTask.get().getEpic()).isEqualTo(subtask.getEpic());
    }

    @AfterEach
    void clean() {
        deleteUser();
    }


    private int createUser() {
        UserDao userDao = UserDao.getINSTANCE();
        User user = new User("testUser1","123");
        try {
            userDao.save(user);
        } catch (UserAlreadyExists e) {
            throw new RuntimeException(e);
        }
        return user.getId();
    }

    private void deleteUser() {
        UserDao userDao = UserDao.getINSTANCE();
        userDao.delete(userId);
    }
}
