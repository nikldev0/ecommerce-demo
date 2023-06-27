package com.ecommerce.demo.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title("My Ecommerce API")
                        .version("1.0.0")
                        .description("This is a sample Spring Boot RESTful service using springdoc-openapi and OpenAPI 3 to test an Ecommerce implementation."));
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("Public Endpoints")
                .pathsToMatch("/**")
                .build();
    }


}


