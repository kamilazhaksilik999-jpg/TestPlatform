package com.testplatform.controller;

import com.testplatform.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {

    @Autowired
    private QuizService quizService;

    // Публичный список тестов
    @GetMapping("/public")
    public ResponseEntity<?> getPublic() {
        return ResponseEntity.ok(quizService.getPublicQuizzes());
    }

    // Мои тесты (авторизация обязательна)
    @GetMapping("/my")
    public ResponseEntity<?> getMy(Authentication auth) {
        try {
            return ResponseEntity.ok(quizService.getMyQuizzes(auth.getName()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Создать тест
    @PostMapping
    public ResponseEntity<?> create(Authentication auth, @RequestBody Map<String,Object> body) {
        try {
            return ResponseEntity.ok(quizService.createQuiz(auth.getName(), body));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Обновить тест
    @PutMapping("/{id}")
    public ResponseEntity<?> update(Authentication auth,
                                     @PathVariable Long id,
                                     @RequestBody Map<String,Object> body) {
        try {
            return ResponseEntity.ok(quizService.updateQuiz(auth.getName(), id, body));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Удалить тест
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(Authentication auth, @PathVariable Long id) {
        try {
            quizService.deleteQuiz(auth.getName(), id);
            return ResponseEntity.ok(Map.of("success", true));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
