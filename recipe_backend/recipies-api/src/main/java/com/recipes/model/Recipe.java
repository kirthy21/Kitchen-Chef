package com.recipes.model;

import lombok.Data;
import org.springframework.hateoas.ResourceSupport;

import java.util.List;
import java.util.Set;

@Data
public class Recipe extends ResourceSupport {
    private Long recipeId;
    private String name;
    private String contributorId;
    private Long minutes;
    private String submittedDate;
    private Set<String> tags;
    private String nutrition;
    private Long numberOfSteps;
    private List<String> steps;
    private String description;
    private Set<String> ingredients;
    private Long numberOfIngredients;
}