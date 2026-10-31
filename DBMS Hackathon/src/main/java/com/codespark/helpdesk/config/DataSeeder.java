package com.codespark.helpdesk.config;

import com.codespark.helpdesk.model.Category;
import com.codespark.helpdesk.model.Role;
import com.codespark.helpdesk.model.User;
import com.codespark.helpdesk.repository.CategoryRepository;
import com.codespark.helpdesk.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

// Runs once at startup and fills an empty database with demo data.
@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final CategoryRepository categoryRepo;
    private final UserRepository userRepo;
    private final PasswordEncoder encoder;

    @Override
    public void run(String... args) {
        if (categoryRepo.count() > 0) return;   // already seeded

        List<Category> saved = categoryRepo.saveAll(List.of(
                Category.builder().name("Billing").slaHours(12).build(),
                Category.builder().name("Technical").slaHours(24).build(),
                Category.builder().name("Account").slaHours(24).build(),
                Category.builder().name("Delivery").slaHours(24).build(),
                Category.builder().name("General").slaHours(48).build()));

        Map<String, Category> cat = saved.stream()
                .collect(Collectors.toMap(Category::getName, Function.identity()));
        String pw = encoder.encode("password123");

        userRepo.saveAll(List.of(
                user("Admin", "admin@helpdesk.com", pw, Role.ADMIN, null),
                user("Ravi (Billing)", "ravi@helpdesk.com", pw, Role.AGENT, cat.get("Billing")),
                user("Sneha (Billing)", "sneha@helpdesk.com", pw, Role.AGENT, cat.get("Billing")),
                user("Arjun (Technical)", "arjun@helpdesk.com", pw, Role.AGENT, cat.get("Technical")),
                user("Priya (Technical)", "priya@helpdesk.com", pw, Role.AGENT, cat.get("Technical")),
                user("Kiran (Account)", "kiran@helpdesk.com", pw, Role.AGENT, cat.get("Account")),
                user("Meena (Delivery)", "meena@helpdesk.com", pw, Role.AGENT, cat.get("Delivery")),
                user("Vikram (General)", "vikram@helpdesk.com", pw, Role.AGENT, cat.get("General")),
                user("Anil Customer", "anil@gmail.com", pw, Role.CUSTOMER, null),
                user("Divya Customer", "divya@gmail.com", pw, Role.CUSTOMER, null)));

        System.out.println(">>> Seeded 5 categories and 10 users (password: password123)");
    }

    private User user(String name, String email, String pw, Role role, Category category) {
        return User.builder().name(name).email(email).password(pw)
                .role(role).category(category).build();
    }
}
