package com.recipes.service;

import com.google.cloud.bigquery.JobId;
import com.recipes.config.BigQueryAppProperties;
import com.recipes.model.Recipe;
import com.recipes.client.RecipeBigQueryClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class RecipeService {

    private static String RESULT_LIMIT=" LIMIT 25";
    @Autowired
    private RecipeBigQueryClient recipeBigQueryClient;
    @Autowired
    private BigQueryAppProperties properties;

    @Cacheable("recipe")
    public Recipe findById(final Long recipeId) {
        final JobId jobId = JobId.of(UUID.randomUUID().toString());
        try {
            return recipeBigQueryClient.findRecipeById(recipeId,jobId);
        } catch (Exception e) {
            log.error("RecipeService",e);
            return null;
        }
    }

    @Cacheable("recipes")
    public List<Recipe> search(String searchType, String query) {
        StringBuilder bqQuery=new StringBuilder("SELECT * FROM "+reccipestable()+" as recipes WHERE ");
        boolean general=false;
        if(searchType.equalsIgnoreCase("ALL")){
            general=true;
        }
        if(searchType.equalsIgnoreCase("ingredients") || general){
            bqQuery.append("recipes.ingredients LIKE '%"+query.trim()+"%'");
        }
        if (general) bqQuery.append(" OR ");
        if(searchType.equalsIgnoreCase("name")|| general){
            bqQuery.append("recipes.name LIKE '%"+query.trim()+"%'");
        }
        if (general) bqQuery.append(" OR ");
        if(searchType.equalsIgnoreCase("tags") || general){
            bqQuery.append("recipes.tags LIKE '%"+query.trim()+"%'");
        }
        if (general) bqQuery.append(" OR ");
        if(searchType.equalsIgnoreCase("steps") || general){
            bqQuery.append("recipes.steps LIKE '%"+query.trim()+"%'");
        }
        if (general) bqQuery.append(" OR ");
        if(searchType.equalsIgnoreCase("description") || general){
            bqQuery.append("recipes.description LIKE '%"+query.trim()+"%'");
        }
        bqQuery.append(RESULT_LIMIT);
        return recipeBigQueryClient.executeQueryForRecipes(bqQuery.toString());
    }

    private String reccipestable(){
        return "`"+properties.getProjectId()+".recipesdb.recipes`";
    }
}
