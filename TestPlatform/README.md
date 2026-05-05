# ⚡ TestPlatform

IT-платформа для тестирования по программированию и технологиям.

## 🚀 Запуск

### Требования
- Java 17+
- Maven 3.8+

### Запуск
```bash
cd TestPlatform
mvn spring-boot:run
```

Открой браузер: **http://localhost:8080**

---

## 📋 Что включено

### Backend (Spring Boot)
- ✅ Регистрация и вход с JWT токенами
- ✅ Вход через Google OAuth (симуляция, для реального — нужен Google Client ID)
- ✅ H2 файловая БД (данные сохраняются в папке `data/`)
- ✅ REST API: `/api/auth/*`, `/api/quizzes/*`
- ✅ Spring Security с JWT фильтром

### Frontend (React)
- ✅ Тёмный профессиональный интерфейс
- ✅ Каталог только IT-тестов (8+ категорий)
- ✅ Профиль с созданными тестами
- ✅ Создание и редактирование тестов
- ✅ Прохождение тестов с таймером
- ✅ Результаты с оценкой

### Категории тестов
Python, JavaScript, Java, C++, Алгоритмы, Базы данных, DevOps, Frontend, Сети, Кибербезопасность

---

## 🗄️ База данных

Данные хранятся в `./data/testplatform.mv.db`.

H2 Console (для отладки): http://localhost:8080/h2-console
- URL: `jdbc:h2:file:./data/testplatform`
- User: `sa`
- Password: (пусто)

---

## 🔐 Google OAuth (настройка)

Для реального Google входа:
1. Создай проект в [Google Cloud Console](https://console.cloud.google.com)
2. Включи Google OAuth 2.0
3. Добавь в `application.properties`:
   ```
   spring.security.oauth2.client.registration.google.client-id=YOUR_CLIENT_ID
   spring.security.oauth2.client.registration.google.client-secret=YOUR_CLIENT_SECRET
   ```
4. В `pom.xml` добавь зависимость `spring-boot-starter-oauth2-client`

---

## 📁 Структура проекта

```
TestPlatform/
├── src/main/java/com/testplatform/
│   ├── Main.java                    # Точка входа
│   ├── config/
│   │   ├── SecurityConfig.java      # Spring Security
│   │   ├── JwtUtil.java             # JWT токены
│   │   └── JwtFilter.java           # JWT фильтр
│   ├── controller/
│   │   ├── AuthController.java      # /api/auth/*
│   │   └── QuizController.java      # /api/quizzes/*
│   ├── model/
│   │   ├── User.java                # Пользователь (БД)
│   │   └── Quiz.java                # Тест (БД)
│   ├── repository/
│   │   ├── UserRepository.java
│   │   └── QuizRepository.java
│   ├── service/
│   │   ├── AuthService.java         # Логика авторизации
│   │   └── QuizService.java         # Логика тестов
│   └── dto/
│       └── AuthDto.java             # DTO для авторизации
├── src/main/resources/
│   ├── static/index.html            # Весь фронтенд
│   └── application.properties
└── pom.xml
```
