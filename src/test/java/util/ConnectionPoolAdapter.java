package util;

public class ConnectionPoolAdapter {

    public static void terminate() {
        ConnectionManager.terminate();
    }
}
