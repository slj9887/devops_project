package com.sk.skala.support;

import com.sk.skala.domain.Todo;
import com.sk.skala.repository.TodoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner init(TodoRepository repo) {
        return args -> {
            if (repo.count() == 0) {
                repo.save(Todo.builder().title("first task").done(false).build());
                repo.save(Todo.builder().title("second task").done(true).build());
            }
        };
    }
}
