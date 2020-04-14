package com.recipes.client;

import com.google.cloud.bigquery.*;
import com.recipes.config.BigQueryAppProperties;
import com.recipes.converter.InteractionConverter;
import com.recipes.converter.RecipeConverter;
import com.recipes.exception.BigQuerySearchException;
import com.recipes.model.Interaction;
import com.recipes.model.Recipe;
import com.recipes.utility.GoogleCredentialsUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@EnableConfigurationProperties({BigQueryAppProperties.class})
public class InteractionBigQueryClient {

    @Autowired
    private BigQueryAppProperties properties;

    private BigQuery createBigQuery(){
        BigQuery bigquery;
        try {
            bigquery = BigQueryOptions.newBuilder().setProjectId(properties.getProjectId()).setCredentials(GoogleCredentialsUtility.getCredentials(properties.getCredientalsName(),this.getClass().getClassLoader())).build().getService();
        } catch (IOException ex) {
            throw new BigQuerySearchException("IO Exception thrown - can't find google credentials file", ex);
        }
        return bigquery;
    }
    public List<Interaction> findInteractionByRecipeId(final Long recipeId, final JobId jobId) throws BigQuerySearchException {
        BigQuery bigquery=createBigQuery();
        final QueryJobConfiguration queryConfig =
                QueryJobConfiguration.newBuilder(
                        "SELECT * " +
                                "FROM `"+properties.getProjectId()+".recipesdb.interactions` as interactions " +
                                "WHERE interactions.recipe_id =  " + recipeId)
                        .setUseLegacySql(false)
                        .build();

        // Create a job ID so that we can safely retry.
        Job queryJob = bigquery.create(JobInfo.newBuilder(queryConfig).setJobId(jobId).build());

        // Wait for the query to complete.
        try {
            queryJob = queryJob.waitFor();

            // Check for errors
            if (queryJob == null) {
                throw new BigQuerySearchException("Job no longer exists");
            } else if (queryJob.getStatus().getError() != null) {
                // You can also look at queryJob.getStatus().getExecutionErrors() for all
                // errors, not just the latest one.
                throw new BigQuerySearchException(queryJob.getStatus().getError().toString());
            }

            final TableResult result = queryJob.getQueryResults();

           return InteractionConverter.convert(result);
        } catch (InterruptedException ex) {
            throw new BigQuerySearchException("InterruptedException - query job failed", ex);
        }
    }

    public List<Interaction> findInteractionByUserId(final Long userId, final JobId jobId) throws BigQuerySearchException {
        BigQuery bigquery=createBigQuery();
        final QueryJobConfiguration queryConfig =
                QueryJobConfiguration.newBuilder(
                        "SELECT * " +
                                "FROM `"+properties.getProjectId()+".recipesdb.interactions` as interactions " +
                                "WHERE interactions.user_id = " + userId)
                        .setUseLegacySql(false)
                        .build();

        // Create a job ID so that we can safely retry.
        Job queryJob = bigquery.create(JobInfo.newBuilder(queryConfig).setJobId(jobId).build());

        // Wait for the query to complete.
        try {
            queryJob = queryJob.waitFor();

            // Check for errors
            if (queryJob == null) {
                throw new BigQuerySearchException("Job no longer exists");
            } else if (queryJob.getStatus().getError() != null) {
                // You can also look at queryJob.getStatus().getExecutionErrors() for all
                // errors, not just the latest one.
                throw new BigQuerySearchException(queryJob.getStatus().getError().toString());
            }

            final TableResult result = queryJob.getQueryResults();

            return InteractionConverter.convert(result);
        } catch (InterruptedException ex) {
            throw new BigQuerySearchException("InterruptedException - query job failed", ex);
        }
    }


    public List<Interaction> executeQueryForRecipes(String query) throws BigQuerySearchException {
        BigQuery bigquery=createBigQuery();
        final JobId jobId = JobId.of(UUID.randomUUID().toString());
        final QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(query) .setUseLegacySql(false) .build();

        // Create a job ID so that we can safely retry.
        Job queryJob = bigquery.create(JobInfo.newBuilder(queryConfig).setJobId(jobId).build());

        // Wait for the query to complete.
        try {
            queryJob = queryJob.waitFor();

            // Check for errors
            if (queryJob == null) {
                throw new BigQuerySearchException("Job no longer exists");
            } else if (queryJob.getStatus().getError() != null) {
                // You can also look at queryJob.getStatus().getExecutionErrors() for all
                // errors, not just the latest one.
                throw new BigQuerySearchException(queryJob.getStatus().getError().toString());
            }

            final TableResult result = queryJob.getQueryResults();

            return InteractionConverter.convert(result);
        } catch (InterruptedException ex) {
            throw new BigQuerySearchException("InterruptedException - query job failed", ex);
        }
    }


}
