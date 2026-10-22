package mcoe.dao;

import mcoe.config.Database;
import mcoe.model.Employee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmployeeDao {
    public void create(String name, String email, String department) throws SQLException {
        String sql = "INSERT INTO employees (name, email, department) VALUES (?, ?, ?)";
        try (Connection connection = Database.openConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setString(2, email);
            statement.setString(3, department);
            statement.executeUpdate();
        }
    }

    public List<Employee> findActive() throws SQLException {
        String sql = "SELECT id, name, email, department, status, created_at, updated_at "
                + "FROM employees WHERE status = 'ACTIVE' ORDER BY id DESC";
        List<Employee> employees = new ArrayList<>();
        try (Connection connection = Database.openConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet results = statement.executeQuery()) {
            while (results.next()) {
                employees.add(map(results));
            }
        }
        return employees;
    }

    public Optional<Employee> findById(long id) throws SQLException {
        String sql = "SELECT id, name, email, department, status, created_at, updated_at "
                + "FROM employees WHERE id = ?";
        try (Connection connection = Database.openConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            try (ResultSet results = statement.executeQuery()) {
                return results.next() ? Optional.of(map(results)) : Optional.empty();
            }
        }
    }

    public void update(long id, String name, String email, String department) throws SQLException {
        String sql = "UPDATE employees SET name = ?, email = ?, department = ? "
                + "WHERE id = ? AND status = 'ACTIVE'";
        try (Connection connection = Database.openConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setString(2, email);
            statement.setString(3, department);
            statement.setLong(4, id);
            statement.executeUpdate();
        }
    }

    public void deactivate(long id) throws SQLException {
        String sql = "UPDATE employees SET status = 'INACTIVE' WHERE id = ? AND status = 'ACTIVE'";
        try (Connection connection = Database.openConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        }
    }

    private Employee map(ResultSet results) throws SQLException {
        return new Employee(
                results.getLong("id"),
                results.getString("name"),
                results.getString("email"),
                results.getString("department"),
                results.getString("status"),
                results.getTimestamp("created_at").toLocalDateTime(),
                results.getTimestamp("updated_at").toLocalDateTime());
    }
}
