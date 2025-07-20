package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConnectionManager {
    private static final ConnectionPool pool = new ConnectionPool();

    public static Connection open(Boolean a) {
        return pool.get();
    }

    // сравнить разницу с пулом и без
    public static Connection open() {
        int a = 1;//2
        if (a == 2) {
            try {
                return DriverManager.getConnection(
                        AppProperties.get("db.url"),
                        AppProperties.get("db.user"),
                        AppProperties.get("db.password"));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            return open(false);
        }
    }

    public static void terminate() {
        System.out.println("Terminate-");
        pool.terminate();
    }

}
