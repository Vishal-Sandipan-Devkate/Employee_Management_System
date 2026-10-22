package mcoe.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class Database {
    private Database() {
    }

    public static Connection openConnection() throws SQLException {
        String url = requiredSetting("DB_URL", "jdbc:mysql://localhost:3306/sat");
        String user = requiredSetting("DB_USER", null);
        String password = requiredSetting("DB_PASSWORD", null);
        return DriverManager.getConnection(url, user, password);
    }

    private static String requiredSetting(String name, String defaultValue) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            value = defaultValue;
        }
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(name + " environment variable is required");
        }
        return value;
    }
}
