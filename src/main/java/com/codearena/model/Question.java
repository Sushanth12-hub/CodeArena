package com.codearena.model;
import java.util.List;
public record Question(int id, int topicId, String text, String correctAnswer, int marks,
                       String explanation, List<Option> options) { }
