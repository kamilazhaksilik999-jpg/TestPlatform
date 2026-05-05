package com.testplatform.service;

import com.testplatform.config.JwtUtil;
import com.testplatform.dto.AuthDto;
import com.testplatform.model.User;
import com.testplatform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtUtil jwtUtil;

    public AuthDto.AuthResponse register(AuthDto.RegisterRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new RuntimeException("Email уже зарегистрирован");
        }
        User user = new User();
        user.setName(req.getName());
        user.setEmail(req.getEmail());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setAvatarEmoji(pickEmoji(req.getName()));
        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getEmail(), user.getId());
        return new AuthDto.AuthResponse(token, toDto(user));
    }

    public AuthDto.AuthResponse login(AuthDto.LoginRequest req) {
        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new RuntimeException("Неверный пароль");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getId());
        return new AuthDto.AuthResponse(token, toDto(user));
    }

    public AuthDto.AuthResponse googleAuth(AuthDto.GoogleAuthRequest req) {
        // Ищем пользователя по googleId или email
        User user = userRepository.findByGoogleId(req.getGoogleId())
                .orElseGet(() -> userRepository.findByEmail(req.getEmail())
                        .orElseGet(() -> {
                            User newUser = new User();
                            newUser.setEmail(req.getEmail());
                            newUser.setName(req.getName());
                            newUser.setPassword(passwordEncoder.encode(java.util.UUID.randomUUID().toString()));
                            newUser.setAvatarEmoji("🌐");
                            newUser.setGoogleAuth(true);
                            return newUser;
                        }));

        user.setGoogleId(req.getGoogleId());
        user.setGoogleAuth(true);
        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getEmail(), user.getId());
        return new AuthDto.AuthResponse(token, toDto(user));
    }

    public AuthDto.UserDto getMe(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        return toDto(user);
    }

    private String pickEmoji(String name) {
        String[] emojis = {"🦊","🐺","🦁","🐯","🦅","🐬","🦋","🌟","⚡","🎯"};
        return emojis[Math.abs(name.hashCode()) % emojis.length];
    }

    public AuthDto.UserDto toDto(User user) {
        AuthDto.UserDto dto = new AuthDto.UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setAvatarEmoji(user.getAvatarEmoji());
        dto.setBio(user.getBio());
        dto.setTotalPoints(user.getTotalPoints());
        dto.setRankLabel(getRankLabel(user.getTotalPoints()));
        dto.setGoogleAuth(user.isGoogleAuth());
        return dto;
    }

    private String getRankLabel(int pts) {
        if (pts < 100) return "🌱 Новичок";
        if (pts < 500) return "📖 Ученик";
        if (pts < 1000) return "🎓 Студент";
        if (pts < 2500) return "⭐ Знаток";
        if (pts < 5000) return "🏅 Эксперт";
        if (pts < 10000) return "💎 Мастер";
        return "👑 Легенда";
    }
}
