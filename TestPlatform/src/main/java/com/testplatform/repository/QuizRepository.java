package com.testplatform.repository;

import com.testplatform.model.Quiz;
import com.testplatform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
    List<Quiz> findByCreator(User creator);
    List<Quiz> findByStatus(String status);
    List<Quiz> findByCategory(String category);
}
