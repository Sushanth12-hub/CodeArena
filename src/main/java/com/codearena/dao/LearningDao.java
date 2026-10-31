package com.codearena.dao;
import com.codearena.model.*;
import com.codearena.util.Database;
import java.sql.*;
import java.util.*;

public class LearningDao {
    public List<Subject> subjects() throws SQLException {
        List<Subject> subjects = new ArrayList<>();
        try (Connection c = Database.connect(); PreparedStatement s = c.prepareStatement("SELECT * FROM subjects ORDER BY subject_name"); ResultSet rs = s.executeQuery()) {
            while (rs.next()) subjects.add(new Subject(rs.getInt("subject_id"), rs.getString("subject_name"), rs.getString("description")));
        } return subjects;
    }
    public List<Topic> topics(int subjectId) throws SQLException {
        List<Topic> topics = new ArrayList<>();
        String sql = "SELECT t.*, COALESCE(m.content, '') content FROM topics t LEFT JOIN learning_material m ON m.topic_id=t.topic_id WHERE t.subject_id=? ORDER BY t.topic_name";
        try (Connection c = Database.connect(); PreparedStatement s = c.prepareStatement(sql)) {
            s.setInt(1, subjectId); try (ResultSet rs = s.executeQuery()) { while (rs.next()) topics.add(topic(rs)); }
        } return topics;
    }
    public Topic topic(int id) throws SQLException {
        String sql = "SELECT t.*, COALESCE(m.content, '') content FROM topics t LEFT JOIN learning_material m ON m.topic_id=t.topic_id WHERE t.topic_id=?";
        try (Connection c = Database.connect(); PreparedStatement s = c.prepareStatement(sql)) {
            s.setInt(1, id); try (ResultSet rs = s.executeQuery()) { return rs.next() ? topic(rs) : null; }
        }
    }
    public List<Question> questions(int topicId) throws SQLException {
        List<Question> questions = new ArrayList<>();
        String sql = "SELECT * FROM questions WHERE topic_id=? ORDER BY question_id";
        try (Connection c = Database.connect(); PreparedStatement s = c.prepareStatement(sql)) {
            s.setInt(1, topicId); try (ResultSet rs = s.executeQuery()) { while (rs.next()) questions.add(question(c, rs)); }
        } return questions;
    }
    public void addQuestion(int topicId, String text, String correct, String explanation, Map<String, String> options) throws SQLException {
        String sql = "INSERT INTO questions(topic_id, question_text, question_type, correct_answer, marks, explanation) VALUES (?, ?, 'MCQ', ?, 1, ?)";
        try (Connection c = Database.connect()) {
            c.setAutoCommit(false);
            try (PreparedStatement question = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                question.setInt(1, topicId); question.setString(2, text); question.setString(3, correct.toUpperCase()); question.setString(4, explanation); question.executeUpdate();
                try (ResultSet keys = question.getGeneratedKeys()) { keys.next(); int questionId = keys.getInt(1);
                    try (PreparedStatement option = c.prepareStatement("INSERT INTO options(question_id, option_label, option_text) VALUES (?, ?, ?)")) {
                        for (String label : List.of("A", "B", "C", "D")) { option.setInt(1, questionId); option.setString(2, label); option.setString(3, options.get(label)); option.addBatch(); }
                        option.executeBatch();
                    }
                } c.commit();
            } catch (SQLException exception) { c.rollback(); throw exception; }
        }
    }
    public List<Question> allQuestions() throws SQLException {
        List<Question> questions = new ArrayList<>();
        try (Connection c = Database.connect(); PreparedStatement s = c.prepareStatement("SELECT * FROM questions ORDER BY question_id DESC"); ResultSet rs = s.executeQuery()) { while (rs.next()) questions.add(question(c, rs)); }
        return questions;
    }
    public void deleteQuestion(int id) throws SQLException {
        try (Connection c = Database.connect()) {
            c.setAutoCommit(false);
            try (PreparedStatement option = c.prepareStatement("DELETE FROM options WHERE question_id=?"); PreparedStatement question = c.prepareStatement("DELETE FROM questions WHERE question_id=?")) {
                option.setInt(1, id); option.executeUpdate(); question.setInt(1, id); question.executeUpdate(); c.commit();
            } catch (SQLException exception) { c.rollback(); throw exception; }
        }
    }
    private Topic topic(ResultSet rs) throws SQLException { return new Topic(rs.getInt("topic_id"), rs.getInt("subject_id"), rs.getString("topic_name"), rs.getString("description"), rs.getString("content")); }
    private Question question(Connection c, ResultSet rs) throws SQLException {
        List<Option> options = new ArrayList<>();
        try (PreparedStatement s = c.prepareStatement("SELECT option_label, option_text FROM options WHERE question_id=? ORDER BY option_label")) { s.setInt(1, rs.getInt("question_id")); try (ResultSet o = s.executeQuery()) { while (o.next()) options.add(new Option(o.getString(1), o.getString(2))); } }
        return new Question(rs.getInt("question_id"), rs.getInt("topic_id"), rs.getString("question_text"), rs.getString("correct_answer"), rs.getInt("marks"), rs.getString("explanation"), options);
    }
}
