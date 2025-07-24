package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConnectionManager {
    private static final ConnectionPool pool = new ConnectionPool();

    public static Connection open() {
        return pool.get();
    }

    public static void terminate() {
        pool.terminate();
    }

}
