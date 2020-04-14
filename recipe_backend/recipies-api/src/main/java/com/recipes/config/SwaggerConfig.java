package com.recipes.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket recipeApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.recipes.controller"))
                .build().apiInfo(recipeApiDocumentation());
    }

    private ApiInfo recipeApiDocumentation() {
        return new ApiInfo("Recipes Backend API",
                "Recipes Backend API",
                "1.0-beta",
                "Terms and Conditions **",
                new Contact("Akhil Alfons K", "http://akhilizer.com", "akhil@akhilizer.com"),
                "MIT License",
                "MIT License",
                new ArrayList<>());
    }

}
