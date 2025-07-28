package dao.task;

import entity.task.Subtask;
import entity.task.Task;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class SubtaskDaoTest {

    private SubtaskDao dao = SubtaskDao.getInstance();

    private Subtask subtask = new Subtask();



    @Test
    void saveAndGet() {
        boolean save = dao.save(subtask);
        assertThat(save).isTrue();

        Optional<Subtask> maybeTask = dao.get(subtask.getId());
        assertThat(maybeTask).isPresent();
        assertThat(maybeTask.get()).isEqualTo(subtask);
        assertThat(maybeTask.get().getEpic()).isEqualTo(subtask.getEpic());
    }
}
