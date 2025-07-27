package util;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ConnectionPool {
    private static BlockingQueue<Connection> modifiedConnections;
    private static List<Connection> commonConnections;
    private static final String POOL_SIZE_KEY = "db.pool.size";
    private static final int DEFAULT_POOL_SIZE = 5;

    public ConnectionPool() {
        int size = getSize();
        modifiedConnections = new ArrayBlockingQueue<>(size);
        commonConnections = new ArrayList<>(size);
        addConnections(size);
    }


    public Connection get() {
        try {
            return modifiedConnections.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void terminate() {
        commonConnections.forEach(c -> {
            try {
                c.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private int getSize() {
        String poolSize = AppProperties.get(POOL_SIZE_KEY);
        return poolSize == null ? DEFAULT_POOL_SIZE : Integer.parseInt(poolSize);
    }

    //Добавляются специальные соединения, у которых вызов close() возвращает соединения на место, а не закрывает его.
    private void addConnections(int count) {
        ConnectionOpener opener = new ConnectionOpener();
        for (int i = 0; i < count; i++) {
            Connection connection = opener.open();
            Connection proxyConnection = (Connection) Proxy.newProxyInstance(
                    ConnectionPool.class.getClassLoader(),
                    new Class[]{Connection.class},
                    (proxy, method, args) -> {
                        if (method.getName().equals("close")) {
                            modifiedConnections.add((Connection) proxy);
                            return null;
                        } else {
                            return method.invoke(connection, args);
                        }
                    }
            );
            commonConnections.add(connection);
            modifiedConnections.add(proxyConnection);
        }
    }

    private static class ConnectionOpener {
        private static final String URL_KEY = "db.url";
        private static final String USER_KEY = "db.user";
        private static final String PASSWORD_KEY = "db.password";
        private static final String DRIVER_KEY = "db.driver";

        static {
            try {
                Class.forName(AppProperties.get(DRIVER_KEY));
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        public Connection open() {
            try {
                return DriverManager.getConnection(
                        AppProperties.get(URL_KEY),
                        AppProperties.get(USER_KEY),
                        AppProperties.get(PASSWORD_KEY)
                );
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
