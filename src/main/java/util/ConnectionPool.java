package util;

//todo полный рефракторинг класса
import java.io.IOException;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ConnectionPool {
    private static final Properties PROPERTIES = new Properties();
    private static final String POOL_SIZE_KEY = "db.pool.size";
    private static BlockingQueue<Connection> connections;
    private static final ConnectionOpener connectionOpener = new ConnectionOpener();
    private static final int DEFAULT_POOL_SIZE = 5;
    private List<Connection> realConnection;

    public ConnectionPool() {
        String poolSize = PROPERTIES.getProperty(POOL_SIZE_KEY);
        int size = poolSize == null ? DEFAULT_POOL_SIZE : Integer.parseInt(poolSize);
        connections = new ArrayBlockingQueue<>(size);
        realConnection = new ArrayList<>(size);
        addConnections(size);
    }

    public Connection get() {
        try {
            return connections.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void terminate() {
        realConnection.forEach(c-> {
            try {
                c.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static void main(String[] args) throws SQLException {
        ConnectionPool connectionPool = new ConnectionPool();
        Connection connection = connectionPool.get();
        connection.close();
        connectionPool.terminate();
    }

    private void addConnections(int count) {
        for (int i = 0; i < count; i++) {
            Connection connection = connectionOpener.open();
           Connection connection2 = (Connection) Proxy.newProxyInstance(ConnectionPool.class.getClassLoader(),new Class[]{Connection.class},(proxy, method, args) -> {
                if (method.getName().equals("close")) {
                    System.out.println("Вернули ");
                    connections.add((Connection) proxy);
                } else {
                    method.invoke(connection, args);
                }
           return proxy;
           });
            realConnection.add(connection);
            connections.add(connection2);
        }
    }

    private static class ConnectionOpener {
        private static final String URL_KEY = "db.url";
        private static final String USER_KEY = "db.user";
        private static final String PASSWORD_KEY = "db.password";
        private static final String DRIVER_KEY = "db.driver";

        public ConnectionOpener() {
            load();
        }

        public Connection open() {
            try {
                return DriverManager.getConnection(
                        PROPERTIES.getProperty(URL_KEY),
                        PROPERTIES.getProperty(USER_KEY),
                        PROPERTIES.getProperty(PASSWORD_KEY)
                );
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        private void load() {
            try {
                PROPERTIES.load(ConnectionManager.class.getClassLoader().getResourceAsStream("application.properties"));
                Class.forName(PROPERTIES.getProperty(DRIVER_KEY));
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
