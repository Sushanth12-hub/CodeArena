package com.codearena.model;
import java.time.LocalDateTime;
public record Result(int attemptId, String student, String subject, String topic, int score,
                     int totalMarks, LocalDateTime attemptedAt) {
    public int percentage() { return totalMarks == 0 ? 0 : score * 100 / totalMarks; }
}
