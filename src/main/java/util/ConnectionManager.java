package util;

import java.sql.Connection;

public final class ConnectionManager {
    private static ConnectionPool pool = new ConnectionPool();

    public static Connection open() {
      return pool.get();
    }

}
