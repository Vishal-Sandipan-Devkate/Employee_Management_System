package mcoe.dao;

import mcoe.config.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UserDao {
    public Optional<User> findEnabledByUsername(String username) throws SQLException {
        String sql = "SELECT username, password_hash, role FROM users "
                + "WHERE username = ? AND enabled = TRUE";
        try (Connection connection = Database.openConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            try (ResultSet results = statement.executeQuery()) {
                if (!results.next()) {
                    return Optional.empty();
                }
                return Optional.of(new User(
                        results.getString("username"),
                        results.getString("password_hash"),
                        results.getString("role")));
            }
        }
    }

    public record User(String username, String passwordHash, String role) {
    }
}
