package util;

import java.io.IOException;
import java.util.Properties;

public class AppProperties {
    private final static Properties PROPERTIES = new Properties();

    static {
        load();
    }

    public static String get(String key) {
        return PROPERTIES.getProperty(key);
    }

    private static void load() {
        try {
            PROPERTIES.load(ConnectionManager.class.getClassLoader().getResourceAsStream("application.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
