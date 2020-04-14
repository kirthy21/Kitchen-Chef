package com.recipes.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "com.recipes.db")
public class BigQueryAppProperties {
    private String credientalsName = "ServiceAccountKey.json";
    private String projectId = "bigqueryproject-259721";
}
