package com.recipes.controller;

import com.recipes.model.Recipe;
import com.recipes.service.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Slf4j
@CrossOrigin(origins = "http://www.appspot.com", maxAge = 3600)
@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    @Autowired
    private RecipeService recipeService ;

    @RequestMapping(value = "/{recipeId}",method = RequestMethod.GET)
    public ResponseEntity<Recipe> findById(@PathVariable Long recipeId) {
        Recipe recipe=recipeService.findById(recipeId);
        if(recipe!=null){
            addSelfLink(recipe);
            return ResponseEntity.ok(recipe);
        }else {
            return ResponseEntity.badRequest().build();
        }

    }

    @RequestMapping(value = "/search/{searchType}",method = RequestMethod.GET)
    public ResponseEntity<List<Recipe>> search(@PathVariable String searchType, @RequestParam String searchQuery) {
        List<Recipe> recipes=recipeService.search(searchType,searchQuery);
        if(recipes!=null){
            recipes.stream().forEach(this::addSelfLink);
            return ResponseEntity.ok(recipes);
        }else {
            return ResponseEntity.badRequest().build();
        }
    }

    private void addSelfLink(Recipe recipe){
        Link selfLink = linkTo(RecipeController.class).slash(recipe.getRecipeId()).withSelfRel();
        recipe.add(selfLink);
    }
}
