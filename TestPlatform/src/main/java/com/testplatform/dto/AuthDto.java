package com.testplatform.dto;

public class AuthDto {

    public static class RegisterRequest {
        private String name;
        private String email;
        private String password;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class LoginRequest {
        private String email;
        private String password;

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class GoogleAuthRequest {
        private String googleId;
        private String email;
        private String name;
        private String avatarUrl;

        public String getGoogleId() { return googleId; }
        public void setGoogleId(String googleId) { this.googleId = googleId; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getAvatarUrl() { return avatarUrl; }
        public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
    }

    public static class AuthResponse {
        private String token;
        private UserDto user;

        public AuthResponse(String token, UserDto user) {
            this.token = token;
            this.user = user;
        }

        public String getToken() { return token; }
        public UserDto getUser() { return user; }
    }

    public static class UserDto {
        private Long id;
        private String name;
        private String email;
        private String avatarEmoji;
        private String bio;
        private Integer totalPoints;
        private String rankLabel;
        private boolean googleAuth;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getAvatarEmoji() { return avatarEmoji; }
        public void setAvatarEmoji(String avatarEmoji) { this.avatarEmoji = avatarEmoji; }
        public String getBio() { return bio; }
        public void setBio(String bio) { this.bio = bio; }
        public Integer getTotalPoints() { return totalPoints; }
        public void setTotalPoints(Integer totalPoints) { this.totalPoints = totalPoints; }
        public String getRankLabel() { return rankLabel; }
        public void setRankLabel(String rankLabel) { this.rankLabel = rankLabel; }
        public boolean isGoogleAuth() { return googleAuth; }
        public void setGoogleAuth(boolean googleAuth) { this.googleAuth = googleAuth; }
    }
}
