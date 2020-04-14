package com.recipes.model;

import lombok.Data;
import org.springframework.hateoas.ResourceSupport;

@Data
public class Interaction extends ResourceSupport {
    private Long userId;
    private Long recipeId;
    private String createDate;
    private Long rating;
    private String review;
}
