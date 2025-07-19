package entity.task;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    private Epic epic;

    public Subtask(long id, String name, String description, TaskStatus status, Duration duration,
                   LocalDateTime startTime, LocalDateTime endTime,int userId, Epic epic) {
        super(id, name, description, status, duration, startTime, endTime,userId);
        this.epic = epic;
    }

    public Subtask(long id, String name, String description, TaskStatus status, Duration duration,
                   LocalDateTime startTime, LocalDateTime endTime,int userID) {
        super(id, name, description, status, duration, startTime, endTime,userID);
    }

    public Subtask() {
        super();
    }

    public Epic getEpic() {
        return epic;
    }

    public void setEpic(Epic epic) {
        this.epic = epic;
    }
}
