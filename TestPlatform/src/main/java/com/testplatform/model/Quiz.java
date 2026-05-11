package com.testplatform.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "quizzes")
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String category;
    private String emoji;
    private String coverColor;
    private String difficulty;
    private String status = "ACTIVE";
    private Integer timerMinutes = 20;
    private Integer passingScore = 60;

    // Вопросы хранятся как JSON-строка
    @Column(columnDefinition = "TEXT")
    private String questionsJson;

    // Теги как строка через запятую
    private String tags;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private User creator;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    private Integer totalAttempts = 0;
    private Double avgScore = 0.0;

    // ── ПРОКТОРИНГ ────────────────────────────────────────────────
    // Включён ли прокторинг для данного теста
    @Column(name = "proctoring_enabled", nullable = false)
    private Boolean proctoringEnabled = false;

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getEmoji() { return emoji; }
    public void setEmoji(String emoji) { this.emoji = emoji; }

    public String getCoverColor() { return coverColor; }
    public void setCoverColor(String coverColor) { this.coverColor = coverColor; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getTimerMinutes() { return timerMinutes; }
    public void setTimerMinutes(Integer timerMinutes) { this.timerMinutes = timerMinutes; }

    public Integer getPassingScore() { return passingScore; }
    public void setPassingScore(Integer passingScore) { this.passingScore = passingScore; }

    public String getQuestionsJson() { return questionsJson; }
    public void setQuestionsJson(String questionsJson) { this.questionsJson = questionsJson; }

    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }

    public User getCreator() { return creator; }
    public void setCreator(User creator) { this.creator = creator; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public Integer getTotalAttempts() { return totalAttempts; }
    public void setTotalAttempts(Integer totalAttempts) { this.totalAttempts = totalAttempts; }

    public Double getAvgScore() { return avgScore; }
    public void setAvgScore(Double avgScore) { this.avgScore = avgScore; }

    public Boolean getProctoringEnabled() { return proctoringEnabled != null && proctoringEnabled; }
    public void setProctoringEnabled(Boolean proctoringEnabled) { this.proctoringEnabled = proctoringEnabled != null && proctoringEnabled; }
}