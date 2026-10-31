package com.codearena.dao;
import com.codearena.model.*;
import com.codearena.util.Database;
import com.codearena.util.PasswordUtil;
import java.sql.*;

public class UserDao {
    public User authenticate(String email, String password) throws SQLException {
        String sql = "SELECT user_id, name, email, role FROM users WHERE email = ? AND password_hash = ?";
        try (Connection connection = Database.connect(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email.trim().toLowerCase()); statement.setString(2, PasswordUtil.hash(password));
            try (ResultSet rs = statement.executeQuery()) {
                if (!rs.next()) return null;
                return "ADMIN".equals(rs.getString("role")) ? new Admin(rs.getInt("user_id"), rs.getString("name"), rs.getString("email")) : new Student(rs.getInt("user_id"), rs.getString("name"), rs.getString("email"));
            }
        }
    }
    public void register(String name, String email, String password) throws SQLException {
        String sql = "INSERT INTO users(name, email, password_hash, role) VALUES (?, ?, ?, 'STUDENT')";
        try (Connection connection = Database.connect(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name.trim()); statement.setString(2, email.trim().toLowerCase()); statement.setString(3, PasswordUtil.hash(password)); statement.executeUpdate();
        }
    }
}
