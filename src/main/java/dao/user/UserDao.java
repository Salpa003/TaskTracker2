package dao.user;

import dao.Dao;
import entity.user.User;
import util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDao implements Dao<Integer, User> {

    private static final String SAVE_SQL = """
            INSERT INTO todo.users.users(login, password)
            VALUES (?,?)
            RETURNING id;
            """;

    private static final String GET_ALL_SQL = """
            SELECT id, login, password
            FROM todo.users.users
            """;

    private static final String GET_BY_ID_SQL = GET_ALL_SQL + " WHERE id = ?";

    private static final String DELETE_SQL = """
            DELETE FROM todo.users.users
            WHERE id = ?;
            """;

    private static final String UPDATE_SQL = """
            UPDATE todo.users.users
            SET login = ?, password = ?
            WHERE id = ?;
            """;
    private static final UserDao INSTANCE = new UserDao();

    private UserDao() {

    }

    public static UserDao getINSTANCE() {
        return INSTANCE;
    }

    @Override
    public boolean save(User user) {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL)) {
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getPassword());

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user.setId(resultSet.getInt("id"));
                return true;
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean delete(Integer id) {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_SQL)) {
            preparedStatement.setInt(1, id);

            int delete = preparedStatement.executeUpdate();
            return delete > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean update(User user) {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
                preparedStatement.setString(1,user.getLogin());
                preparedStatement.setString(2, user.getPassword());
                preparedStatement.setInt(3, user.getId());

                int update = preparedStatement.executeUpdate();
                return update > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<User> get(Integer id) {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_BY_ID_SQL)) {
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(resultSetToEntity(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_SQL)) {
           ResultSet resultSet = preparedStatement.executeQuery();
           while (resultSet.next()) {
               users.add(resultSetToEntity(resultSet));
           }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }

    private User resultSetToEntity(ResultSet resultSet) {
        try {
            return new User(
                    resultSet.getInt("id"),
                    resultSet.getString("login"),
                    resultSet.getString("password")
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
