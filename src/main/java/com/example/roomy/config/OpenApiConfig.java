package com.example.roomy.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class OpenApiConfig {

    @Bean
    public OpenApiCustomizer pageableCustomizer() {
        return openApi -> {
            if (openApi.getPaths() == null) return;

            openApi.getPaths().forEach((path, pathItem) -> {
                if (pathItem.readOperations() == null) return;

                pathItem.readOperations().forEach(operation -> {
                    if (operation.getParameters() == null) return;

                    for (Parameter param : operation.getParameters()) {
                        if (param.getName() == null) continue;

                        switch (param.getName()) {
                            case "page":
                                param.setDescription("Page number (1-based index)");
                                param.setExample(1);
                                param.getSchema().setDefault(1);
                                break;
                            case "size":
                                param.setDescription("Page size");
                                param.setExample(10);
                                break;
                            case "sort":
                                param.setDescription("Sorting format: property,(asc|desc)");
                                param.setExample("name,asc");
                                break;
                        }
                    }
                });
            });
        };
    }
}
