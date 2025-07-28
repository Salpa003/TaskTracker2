package dao.task;

import entity.task.Epic;
import entity.task.Subtask;
import entity.task.Task;
import entity.task.TaskStatus;
import util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class SubtaskDao {
    //todo
    private TaskDao taskDao;
    private EpicDao epicDao;

    private static final String SAVE_SQL = """
            INSERT INTO task_tracker.tasks.subtask(id, epicid)
            VALUES (?,?);
            """;

    private static final String GET_ALL_SQL = """
            SELECT id, epicid
            FROM task_tracker.tasks.subtask
            """;

    private static final String GET_SQL = GET_ALL_SQL + " WHERE id = ? ";

    private static final SubtaskDao INSTANCE = new SubtaskDao();

    private SubtaskDao() {
        taskDao = TaskDao.getInstance();
        epicDao = EpicDao.getInstance();
    }
    public static SubtaskDao getInstance() {
        return INSTANCE;
    }

    public boolean save(Subtask subtask) {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL)) {
            Task task = subtask;
            boolean save = taskDao.save(task);
            if (save) {
                preparedStatement.setLong(1, task.getId());
                preparedStatement.setLong(2, subtask.getEpic().getId());
               return preparedStatement.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public Optional<Subtask> get(long id) {
        try (Connection connection = ConnectionManager.open();
        PreparedStatement preparedStatement = connection.prepareStatement(GET_SQL)) {
            preparedStatement.setLong(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
               return Optional.ofNullable(resultSetToEntity(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    private Subtask resultSetToEntity(ResultSet resultSet) {
        try {
            Long taskId = resultSet.getLong("id");
            Long epicId = resultSet.getLong("epicId");

            Optional<Task> maybeTask = taskDao.get(taskId);
            if (maybeTask.isPresent()) {
                Task task = maybeTask.get();
                Epic epic = epicDao.get(epicId);
                return new Subtask(task,epic);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
