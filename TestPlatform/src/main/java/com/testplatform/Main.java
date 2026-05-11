package com.testplatform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
        System.out.println("\n========================================");
        System.out.println("  🚀 TestPlatform успешно запущен!");
        System.out.println("  Браузер : http://localhost:8080");
        System.out.println("  Console   http://localhost:8080/h2-console");
        System.out.println("========================================\n");
    }
}
