package com.codearena.service;
import com.codearena.dao.ResultDao;
import com.codearena.model.Question;
import java.sql.SQLException;
import java.util.*;

public class QuizService {
    private final ResultDao results = new ResultDao();
    public QuizOutcome markAndSave(int userId, int topicId, List<Question> questions, Map<Integer, String> submitted) throws SQLException {
        int score = 0, total = 0; Map<Integer, String> correct = new HashMap<>();
        for (Question question : questions) { total += question.marks(); correct.put(question.id(), question.correctAnswer()); if (question.correctAnswer().equalsIgnoreCase(submitted.getOrDefault(question.id(), ""))) score += question.marks(); }
        int attemptId = results.save(userId, topicId, score, total, submitted, correct);
        return new QuizOutcome(attemptId, score, total, questions, submitted);
    }
    public record QuizOutcome(int attemptId, int score, int total, List<Question> questions, Map<Integer, String> submitted) { public int percentage() { return total == 0 ? 0 : score * 100 / total; } }
}
