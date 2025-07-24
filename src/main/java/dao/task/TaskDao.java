package dao.task;

import entity.task.Task;
import entity.task.TaskStatus;
import util.ConnectionManager;
import util.TimeFormatter;

import java.sql.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TaskDao {

    private static final String SAVE_SQL = """
            INSERT INTO task_tracker.tasks.task(name, description, status, duration, start_time, end_time, user_id)
            VALUES (?,?,?,?,?,?,?)
            RETURNING id;
            """;

    private static final String DELETE_SQL = """
            DELETE FROM task_tracker.tasks.task
            WHERE id = ?;
            """;

    private static final String GET_ALL_BY_USER_ID_SQL = """
            SELECT id, name, description, status, duration, start_time, end_time, user_id
            FROM task_tracker.tasks.task
            WHERE user_id = ?
            """;

    private static final String GET_ALL_SQL = """
            SELECT id, name, description, status, duration, start_time, end_time, user_id
            FROM task_tracker.tasks.task
            """;

    private static final String GET_BY_ID_SQL = GET_ALL_SQL + " WHERE id = ?";

    private static final String UPDATE_SQL = """
            UPDATE task_tracker.tasks.task
            set name = ?, description = ?, status = ?, duration = ?,
            start_time = ?, end_time = ?
            WHERE id = ?;
            """;
    private static final TaskDao INSTANCE = new TaskDao();

    private TaskDao() {

    }

    public static TaskDao getInstance() {
        return INSTANCE;
    }


    public boolean save(Task task) {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL)) {
            preparedStatement.setString(1, task.getName());
            preparedStatement.setString(2, task.getDescription());
            preparedStatement.setString(3, task.getStatus().toString());
            preparedStatement.setLong(4, task.getDuration().toSeconds());
            preparedStatement.setTimestamp(5, Timestamp.valueOf(TimeFormatter.timestamp(task.getStartTime())));
            preparedStatement.setTimestamp(6, Timestamp.valueOf(TimeFormatter.timestamp(task.getEndTime())));
            preparedStatement.setInt(7, task.getUserId());

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                task.setId(resultSet.getLong("id"));
                return true;
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public boolean delete(Long id) {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_SQL)) {
            preparedStatement.setLong(1, id);
            int delete = preparedStatement.executeUpdate();
            return delete > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public boolean update(Task task) {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setString(1, task.getName());
            preparedStatement.setString(2, task.getDescription());
            preparedStatement.setString(3, task.getStatus().toString());
            preparedStatement.setLong(4, task.getDuration().toSeconds());
            preparedStatement.setTimestamp(5, Timestamp.valueOf(TimeFormatter.timestamp(task.getStartTime())));
            preparedStatement.setTimestamp(6, Timestamp.valueOf(TimeFormatter.timestamp(task.getEndTime())));
            preparedStatement.setLong(7, task.getId());

            int update = preparedStatement.executeUpdate();
            return update > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public Optional<Task> get(Long id) {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_BY_ID_SQL)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(resultSetToEntity(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    public List<Task> getAllByUserId(int userId) {
        List<Task> tasks = new ArrayList<>();
        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_BY_USER_ID_SQL)) {
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                tasks.add(resultSetToEntity(resultSet));
            }
            return tasks;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public List<Task> getAll() {
        List<Task> list = new ArrayList<>();
        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_SQL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                list.add(resultSetToEntity(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    private Task resultSetToEntity(ResultSet resultSet) {
        try {
            return new Task(
                    resultSet.getLong("id"),
                    resultSet.getString("name"),
                    resultSet.getString("description"),
                    TaskStatus.valueOf(resultSet.getString("status")),
                    Duration.ofSeconds(resultSet.getLong("duration")),
                    resultSet.getTimestamp("start_time").toLocalDateTime(),
                    resultSet.getTimestamp("end_time").toLocalDateTime(),
                    resultSet.getInt("user_id")
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
