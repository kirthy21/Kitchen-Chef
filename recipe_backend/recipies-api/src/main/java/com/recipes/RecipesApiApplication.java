package com.recipes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class RecipesApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(RecipesApiApplication.class, args);
    }
}
