package util;

import java.sql.Connection;

public final class ConnectionManager {
    private static final ConnectionPool pool = new ConnectionPool();

    public static Connection open() {
        return pool.get();
    }

    public static void terminate() {
        pool.terminate();
    }


}