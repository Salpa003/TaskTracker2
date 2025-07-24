package entity.task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    private List<Subtask> subtasks = new ArrayList<>();

    public Epic(long id, String name, String description, TaskStatus status, Duration duration,
                LocalDateTime startTime, LocalDateTime endTime, int userID) {
        super(id, name, description, status, duration, startTime, endTime, userID);
    }

    public Epic() {
        super();
    }

    public List<Subtask> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(List<Subtask> subtasks) {
        this.subtasks = subtasks;
    }

    public void addSubtask(Subtask subtask) {
        subtasks.add(subtask);
    }

    public void deleteSubtask(Subtask subtask) {
        subtasks.remove(subtask);
    }


    public void calculateStatus() {
        boolean isNew = true;
        boolean isDone = true;

        for (Subtask subtask : subtasks) {
            if (subtask.getStatus() != TaskStatus.NEW)
                isNew = false;
            if (subtask.getStatus() != TaskStatus.DONE)
                isDone = false;
        }
        if (isNew)
            setStatus(TaskStatus.NEW);
        else if (isDone)
            setStatus(TaskStatus.DONE);
        else
            setStatus(TaskStatus.IN_PROGRESS);
    }
}
