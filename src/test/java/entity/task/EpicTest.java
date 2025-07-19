package entity.task;

import entity.task.Epic;
import entity.task.Subtask;
import entity.task.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.assertj.core.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EpicTest {

    private Epic epic;

    @BeforeEach
    void setEpic() {
        epic = new Epic();
    }

    @Test
    void shouldCalculateStatus() {
        Subtask subtask1 = new Subtask(1,null,null, TaskStatus.NEW,null,null,null,1);
        Subtask subtask2 = new Subtask(2,null,null,TaskStatus.NEW,null,null,null,1);
        epic.addSubtask(subtask1);
        epic.addSubtask(subtask2);

        epic.calculateStatus();
        assertThat(epic.getStatus()).isEqualTo(TaskStatus.NEW);

        subtask1.setStatus(TaskStatus.IN_PROGRESS);
        epic.calculateStatus();
        assertThat(epic.getStatus()).isEqualTo(TaskStatus.IN_PROGRESS);

        subtask2.setStatus(TaskStatus.DONE);
        epic.calculateStatus();
        assertThat(epic.getStatus()).isEqualTo(TaskStatus.IN_PROGRESS);

        subtask1.setStatus(TaskStatus.DONE);
        epic.calculateStatus();
        assertThat(epic.getStatus()).isEqualTo(TaskStatus.DONE);
    }
}
