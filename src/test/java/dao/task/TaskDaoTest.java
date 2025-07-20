package dao.task;

import dao.user.UserDao;
import entity.task.Task;
import entity.task.TaskStatus;
import entity.user.User;
import org.junit.jupiter.api.*;
import util.ConnectionManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TaskDaoTest {

    private TaskDao dao = TaskDao.getInstance();
    private int userId;

   private Task task ;


    @BeforeAll
    void createUser() {
        UserDao userDao = UserDao.getINSTANCE();
        List<User> users = userDao.getAll();
        for (User user : users) {
            if (user.getLogin().equals("test"))
                userId = user.getId();
        }
        if (userId == 0) {
            User user = new User("test","test");
            boolean save = userDao.save(user);
            if (!save) {
                throw new RuntimeException("Error in creating user");
            }
            userId = user.getId();
        }
        task = new Task("test","test", TaskStatus.NEW, Duration.ZERO,
                LocalDateTime.of(2025,1,1,1,1,1),
                LocalDateTime.of(2025,2,1,1,1,1), userId);
    }

    @Test
    void saveAndGet() {
        boolean save = dao.save(task);
        assertThat(save).isTrue();

        Optional<Task> maybeTask = dao.get(task.getId());
        assertThat(maybeTask).isPresent();
        assertThat(maybeTask.get()).isEqualTo(task);
    }

    @Test
    void delete() {
       boolean save = dao.save(task);
       assertThat(save).isTrue();

      boolean delete = dao.delete(task.getId());
      assertThat(delete).isTrue();

      Optional<Task> maybeTask = dao.get(task.getId());
      assertThat(maybeTask).isEmpty();
    }

    @Test
    void update() {
        boolean save = dao.save(task);
        assertThat(save).isTrue();

        Task newTask = new Task(task.getId(),"new","new",TaskStatus.NEW,Duration.ZERO,
                LocalDateTime.of(2025,1,1,1,1),
                LocalDateTime.of(2025,1,1,1,1),task.getUserId());
        boolean update = dao.update(newTask);
        assertThat(update).isTrue();

        Optional<Task> maybeTask = dao.get(task.getId());
        assertThat(maybeTask).isPresent();
        assertThat(maybeTask.get()).isEqualTo(newTask);
    }

    @Test
    void getAllByUserId() {
        boolean save = dao.save(task);
        assertThat(save).isTrue();

        List<Task> tasks = dao.getAllByUserId(task.getUserId());
        assertThat(tasks).size().isSameAs(1);
        assertThat(tasks.get(0)).isEqualTo(task);
    }

    @Test
    void getAll() {
        Task task1 = new Task("test","test", TaskStatus.NEW, Duration.ZERO,
                LocalDateTime.of(2025,1,1,1,1,1),
                LocalDateTime.of(2025,2,1,1,1,1), userId);
        Task task2 = new Task("test","test", TaskStatus.NEW, Duration.ZERO,
                LocalDateTime.of(2025,1,1,1,1,1),
                LocalDateTime.of(2025,2,1,1,1,1), userId);

       boolean s = dao.save(task1);
       boolean s2 = dao.save(task2);
       assertThat(s).isTrue().isEqualTo(s2);

       List<Task> tasks = dao.getAll();
       assertThat(tasks).size().isSameAs(2);
       assertThat(tasks).contains(task1, task2);

       boolean d = dao.delete(task1.getId());
       boolean d2 = dao.delete(task2.getId());
        assertThat(d).isTrue().isEqualTo(d2);
    }

    @AfterEach
    void removeTask() {
        dao.delete(task.getId());
    }

    @AfterAll
    void removeUser() {
        UserDao userDao = UserDao.getINSTANCE();
        userDao.delete(userId);
        ConnectionManager.terminate();
    }

}
