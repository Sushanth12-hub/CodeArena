package com.codearena.dao;
import com.codearena.model.Result;
import com.codearena.util.Database;
import java.sql.*;
import java.util.*;

public class ResultDao {
    public record Progress(int attempts, int bestPercentage, int averagePercentage) { }
    public int save(int userId, int topicId, int score, int total, Map<Integer, String> answers, Map<Integer, String> correctAnswers) throws SQLException {
        try (Connection c = Database.connect()) {
            c.setAutoCommit(false);
            try (PreparedStatement attempt = c.prepareStatement("INSERT INTO attempts(user_id, topic_id, score, total_marks) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
                attempt.setInt(1, userId); attempt.setInt(2, topicId); attempt.setInt(3, score); attempt.setInt(4, total); attempt.executeUpdate();
                try (ResultSet keys = attempt.getGeneratedKeys()) { keys.next(); int id = keys.getInt(1);
                    try (PreparedStatement answer = c.prepareStatement("INSERT INTO student_answers(attempt_id, question_id, submitted_answer, is_correct) VALUES (?, ?, ?, ?)")) {
                        for (Map.Entry<Integer, String> item : answers.entrySet()) { answer.setInt(1, id); answer.setInt(2, item.getKey()); answer.setString(3, item.getValue()); answer.setBoolean(4, item.getValue().equalsIgnoreCase(correctAnswers.get(item.getKey()))); answer.addBatch(); } answer.executeBatch();
                    } c.commit(); return id;
                }
            } catch (SQLException e) { c.rollback(); throw e; }
        }
    }
    public List<Result> history(Integer userId) throws SQLException {
        List<Result> results = new ArrayList<>();
        String sql = "SELECT a.attempt_id, u.name, s.subject_name, t.topic_name, a.score, a.total_marks, a.attempt_date FROM attempts a JOIN users u ON u.user_id=a.user_id JOIN topics t ON t.topic_id=a.topic_id JOIN subjects s ON s.subject_id=t.subject_id" + (userId == null ? " ORDER BY a.attempt_date DESC" : " WHERE a.user_id=? ORDER BY a.attempt_date DESC");
        try (Connection c = Database.connect(); PreparedStatement s = c.prepareStatement(sql)) { if (userId != null) s.setInt(1, userId); try (ResultSet rs = s.executeQuery()) { while (rs.next()) results.add(new Result(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5), rs.getInt(6), rs.getTimestamp(7).toLocalDateTime())); } } return results;
    }
    public Progress progress(int userId) throws SQLException {
        String sql = "SELECT COUNT(*), COALESCE(MAX(score * 100 / NULLIF(total_marks, 0)), 0), COALESCE(AVG(score * 100.0 / NULLIF(total_marks, 0)), 0) FROM attempts WHERE user_id=?";
        try (Connection c = Database.connect(); PreparedStatement s = c.prepareStatement(sql)) { s.setInt(1, userId); try (ResultSet rs = s.executeQuery()) { rs.next(); return new Progress(rs.getInt(1), rs.getInt(2), (int)Math.round(rs.getDouble(3))); } }
    }
}
