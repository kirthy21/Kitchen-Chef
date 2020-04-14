package com.recipes.service;

import com.google.cloud.bigquery.JobId;
import com.recipes.client.InteractionBigQueryClient;
import com.recipes.client.RecipeBigQueryClient;
import com.recipes.config.BigQueryAppProperties;
import com.recipes.model.Interaction;
import com.recipes.model.Recipe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class InteractionService {

    private static String RESULT_LIMIT=" LIMIT 25";
    @Autowired
    private InteractionBigQueryClient interactionBigQueryClient;
    @Autowired
    private BigQueryAppProperties properties;

    @Cacheable("recipe-interactions")
    public List<Interaction> findByRecipeId(final Long recipeId) {
        final JobId jobId = JobId.of(UUID.randomUUID().toString());
        try {
            return interactionBigQueryClient.findInteractionByRecipeId(recipeId,jobId);
        } catch (Exception e) {
            log.error("RecipeService",e);
            return null;
        }
    }

    @Cacheable("user-interactions")
    public List<Interaction> findByUserId(final Long userId) {
        final JobId jobId = JobId.of(UUID.randomUUID().toString());
        try {
            return interactionBigQueryClient.findInteractionByUserId(userId,jobId);
        } catch (Exception e) {
            log.error("RecipeService",e);
            return null;
        }
    }

    @Cacheable("interactions")
    public List<Interaction> search(String searchType, String query) {
        StringBuilder bqQuery=new StringBuilder("SELECT * FROM "+interactionstable()+" as interactions WHERE ");

        if(searchType.equalsIgnoreCase("review")){
            bqQuery.append("interactions.review LIKE '%"+query.trim()+"%'");
        }
        if(searchType.equalsIgnoreCase("rating")){
            bqQuery.append("interactions.rating =="+query.trim());
        }
        bqQuery.append(RESULT_LIMIT);
        return interactionBigQueryClient.executeQueryForRecipes(bqQuery.toString());
    }

    private String interactionstable(){
        return "`"+properties.getProjectId()+".recipesdb.interactions`";
    }
}
