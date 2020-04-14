package com.recipes.converter;

import com.google.cloud.bigquery.Field;
import com.google.cloud.bigquery.FieldValueList;
import com.google.cloud.bigquery.TableResult;
import com.recipes.model.Interaction;
import com.recipes.model.Recipe;
import io.swagger.models.auth.In;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class InteractionConverter {

    public static List<Interaction> convert(TableResult recipeResult) {
        List<Interaction> result = new ArrayList<>();
        final List<String> fieldList = recipeResult.getSchema().getFields().stream()
                .map(Field::getName)
                .collect(Collectors.toList());
        recipeResult.iterateAll().forEach(row -> mapData(row, fieldList, result));
        return result;
    }

    private static void mapData(FieldValueList row, List<String> fieldList, List<Interaction> result) {
        Interaction interaction = new Interaction();
        for (String field : fieldList) {
            switch (field) {
                case "user_id":
                    if (!row.get(field).isNull())
                        interaction.setUserId(row.get(field).getLongValue());
                    break;
                case "recipe_id":
                    if (!row.get(field).isNull())
                        interaction.setRecipeId(row.get(field).getLongValue());
                    break;
                case "date":
                    if (!row.get(field).isNull())
                        interaction.setCreateDate(row.get(field).getStringValue());
                    break;
                case "rating":
                    if (!row.get(field).isNull())
                        interaction.setRating(row.get(field).getLongValue());
                    break;
                case "review":
                    if (!row.get(field).isNull())
                        interaction.setReview(row.get(field).getStringValue());
                    break;
            }
        }
        result.add(interaction);
    }

    private static List<String> tokenize(String rawStr){
        String endsRemoved=rawStr.replace("[","").replace("]","");
        List<String> tokens= Arrays.asList(endsRemoved.split("', '"));
        return tokens.stream().map(token->token.replace("'","")).collect(Collectors.toList());
    }
}