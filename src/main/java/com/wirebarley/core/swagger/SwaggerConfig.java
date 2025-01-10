package com.wirebarley.core.swagger;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        final String securitySchemeName = "Authorization";

        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(
                        new Components()
                                .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .bearerFormat("JWT")
                                )
                )
                .info(apiInfo());
    }

    @Bean
    public OperationCustomizer customize() {
        return (operation, handlerMethod) -> {
            String path = operation.getOperationId();
            if ("join".equals(path) || "login".equals(path)) {
                operation.setParameters(null);
            } else {
                operation.addParametersItem(new Parameter().in("header").required(true).description("발급된 JWT Token 정보").name("authorization"));
            }
            return operation;
        };
    }

    private Info apiInfo(){
        return new Info()
                .title("Bank-API Document")
                .description("wirebarley Coding Test RestFul-API Project")
                .version("1.0.0");
    }

}