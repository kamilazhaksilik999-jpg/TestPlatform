package com.testplatform.service;

import com.testplatform.model.Quiz;
import com.testplatform.model.User;
import com.testplatform.repository.QuizRepository;
import com.testplatform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

@Service
public class QuizService {

    @Autowired private QuizRepository quizRepository;
    @Autowired private UserRepository userRepository;

    public List<Map<String,Object>> getPublicQuizzes() {
        return quizRepository.findByStatus("ACTIVE").stream()
                .map(this::toMap)
                .toList();
    }

    public List<Map<String,Object>> getMyQuizzes(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        return quizRepository.findByCreator(user).stream()
                .map(this::toMap)
                .toList();
    }

    public Map<String,Object> createQuiz(String email, Map<String,Object> body) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        Quiz quiz = new Quiz();
        quiz.setTitle((String) body.get("title"));
        quiz.setCategory((String) body.getOrDefault("category", "IT"));
        quiz.setEmoji((String) body.getOrDefault("emoji", "💻"));
        quiz.setCoverColor((String) body.getOrDefault("coverColor", "#7C3AED"));
        quiz.setDifficulty((String) body.getOrDefault("difficulty", "MEDIUM"));
        quiz.setStatus((String) body.getOrDefault("status", "ACTIVE"));
        quiz.setTimerMinutes(((Number) body.getOrDefault("timerMinutes", 20)).intValue());
        quiz.setPassingScore(((Number) body.getOrDefault("passingScore", 60)).intValue());
        quiz.setQuestionsJson(toJson(body.get("questions")));
        quiz.setTags(joinTags(body.get("tags")));
        quiz.setCreator(user);

        // ── ПРОКТОРИНГ ────────────────────────────────────────────
        Object procObj = body.get("proctoringEnabled");
        boolean procEnabled = false;
        if (procObj instanceof Boolean) procEnabled = (Boolean) procObj;
        else if (procObj instanceof String) procEnabled = Boolean.parseBoolean((String) procObj);
        quiz.setProctoringEnabled(procEnabled);

        quizRepository.save(quiz);

        // Начислить очки
        user.setTotalPoints(user.getTotalPoints() + 50);
        userRepository.save(user);

        return toMap(quiz);
    }

    public Map<String,Object> updateQuiz(String email, Long quizId, Map<String,Object> body) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Тест не найден"));

        if (!quiz.getCreator().getId().equals(user.getId())) {
            throw new RuntimeException("Нет прав для редактирования");
        }

        if (body.containsKey("title")) quiz.setTitle((String) body.get("title"));
        if (body.containsKey("category")) quiz.setCategory((String) body.get("category"));
        if (body.containsKey("emoji")) quiz.setEmoji((String) body.get("emoji"));
        if (body.containsKey("coverColor")) quiz.setCoverColor((String) body.get("coverColor"));
        if (body.containsKey("difficulty")) quiz.setDifficulty((String) body.get("difficulty"));
        if (body.containsKey("status")) quiz.setStatus((String) body.get("status"));
        if (body.containsKey("timerMinutes")) quiz.setTimerMinutes(((Number) body.get("timerMinutes")).intValue());
        if (body.containsKey("passingScore")) quiz.setPassingScore(((Number) body.get("passingScore")).intValue());
        if (body.containsKey("questions")) quiz.setQuestionsJson(toJson(body.get("questions")));
        if (body.containsKey("tags")) quiz.setTags(joinTags(body.get("tags")));

        // ── ПРОКТОРИНГ ────────────────────────────────────────────
        if (body.containsKey("proctoringEnabled")) {
            Object procObj = body.get("proctoringEnabled");
            boolean procEnabled = false;
            if (procObj instanceof Boolean) procEnabled = (Boolean) procObj;
            else if (procObj instanceof String) procEnabled = Boolean.parseBoolean((String) procObj);
            quiz.setProctoringEnabled(procEnabled);
        }

        quiz.setUpdatedAt(LocalDateTime.now());
        quizRepository.save(quiz);
        return toMap(quiz);
    }

    public void deleteQuiz(String email, Long quizId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Тест не найден"));
        if (!quiz.getCreator().getId().equals(user.getId())) {
            throw new RuntimeException("Нет прав для удаления");
        }
        quizRepository.delete(quiz);
    }

    private Map<String,Object> toMap(Quiz q) {
        Map<String,Object> m = new LinkedHashMap<>();
        m.put("id", q.getId());
        m.put("title", q.getTitle());
        m.put("category", q.getCategory());
        m.put("emoji", q.getEmoji());
        m.put("coverColor", q.getCoverColor());
        m.put("difficulty", q.getDifficulty());
        m.put("status", q.getStatus());
        m.put("timerMinutes", q.getTimerMinutes());
        m.put("passingScore", q.getPassingScore());
        m.put("tags", q.getTags() != null ? q.getTags().split(",") : new String[0]);
        m.put("totalAttempts", q.getTotalAttempts());
        m.put("avgScore", q.getAvgScore());
        m.put("createdAt", q.getCreatedAt() != null ? q.getCreatedAt().toString() : null);
        m.put("updatedAt", q.getUpdatedAt() != null ? q.getUpdatedAt().toString() : null);
        // ── ПРОКТОРИНГ ────────────────────────────────────────────
        m.put("proctoringEnabled", q.getProctoringEnabled());
        if (q.getCreator() != null) {
            m.put("creatorId", q.getCreator().getId());
            m.put("creatorName", q.getCreator().getName());
        }
        // Парсим questions из JSON-строки
        if (q.getQuestionsJson() != null && !q.getQuestionsJson().isEmpty()) {
            try {
                com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                m.put("questions", mapper.readValue(q.getQuestionsJson(), Object.class));
            } catch (Exception e) {
                m.put("questions", List.of());
            }
        } else {
            m.put("questions", List.of());
        }
        return m;
    }

    private String toJson(Object obj) {
        if (obj == null) return "[]";
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            return "[]";
        }
    }

    @SuppressWarnings("unchecked")
    private String joinTags(Object tags) {
        if (tags == null) return "";
        if (tags instanceof List) {
            return String.join(",", (List<String>) tags);
        }
        return tags.toString();
    }
}