package com.recipes.converter;

import com.google.cloud.bigquery.Field;
import com.google.cloud.bigquery.FieldValueList;
import com.google.cloud.bigquery.TableResult;
import com.recipes.model.Recipe;

import java.util.*;
import java.util.stream.Collectors;

public class RecipeConverter {

    public static List<Recipe> convert(TableResult recipeResult) {
        List<Recipe> result = new ArrayList<>();
        final List<String> fieldList = recipeResult.getSchema().getFields().stream()
                .map(Field::getName)
                .collect(Collectors.toList());
        recipeResult.iterateAll().forEach(row -> mapData(row, fieldList, result));
        return result;
    }

    private static void mapData(FieldValueList row, List<String> fieldList, List<Recipe> result) {
        Recipe recipe = new Recipe();
        for (String field : fieldList) {
            switch (field) {
                case "id":
                    if (!row.get(field).isNull())
                        recipe.setRecipeId(row.get(field).getLongValue());
                    break;
                case "name":
                    if (!row.get(field).isNull())
                        recipe.setName(row.get(field).getStringValue());
                    break;
                case "contributor_id":
                    if (!row.get(field).isNull())
                        recipe.setContributorId(row.get(field).getStringValue());
                    break;
                case "minutes":
                    if (!row.get(field).isNull())
                        recipe.setMinutes(row.get(field).getLongValue());
                    break;
                case "submitted":
                    if (!row.get(field).isNull())
                        recipe.setSubmittedDate(row.get(field).getStringValue());
                    break;
                case "tags":
                    if (!row.get(field).isNull())
                        recipe.setTags(new HashSet<String>(tokenize(row.get(field).getStringValue())));
                    break;
                case "nutrition":
                    if (!row.get(field).isNull())
                        recipe.setNutrition(row.get(field).getStringValue());
                    break;
                case "steps":
                    if (!row.get(field).isNull())
                        recipe.setSteps(tokenize(row.get(field).getStringValue()));
                    break;
                case "n_steps":
                    if (!row.get(field).isNull())
                        recipe.setNumberOfSteps(row.get(field).getLongValue());
                    break;
                case "description":
                    if (!row.get(field).isNull())
                        recipe.setDescription(row.get(field).getStringValue());
                    break;
                case "ingredients":
                    if (!row.get(field).isNull())
                        recipe.setIngredients(new HashSet<String>(tokenize(row.get(field).getStringValue())));
                    break;
                case "n_ingredients":
                    if (!row.get(field).isNull())
                        recipe.setNumberOfIngredients(row.get(field).getLongValue());
                    break;
            }
        }
        result.add(recipe);
    }

    private static List<String> tokenize(String rawStr){
        String endsRemoved=rawStr.replace("[","").replace("]","");
        List<String> tokens= Arrays.asList(endsRemoved.split("', '"));
        return tokens.stream().map(token->token.replace("'","")).collect(Collectors.toList());
    }
}