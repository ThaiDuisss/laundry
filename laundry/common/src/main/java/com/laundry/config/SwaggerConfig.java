package com.laundry.config;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${spring.application.name:unknown}")
    private String serviceName;

    @Bean
    public OpenAPI customOpenAPI() {
        String basePath = getBasePath(serviceName);
        return new OpenAPI()
                .servers(List.of(
                    new Server().url("http://localhost:8080" + basePath).description("API Gateway")
                ))    .info(new Info()
                        .title("Laundry Service API")
                        .version("v1")
                        .description("API documentation for Laundry Service"))
                .addSecurityItem(new SecurityRequirement().addList("BearerAuth"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("BearerAuth",
                                new SecurityScheme()
                                        .name("Authorization")
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        )
                );
    }

    private String getBasePath(String serviceName) {
        switch (serviceName) {
            case "auth-service": return "/auth";
            case "user-service": return "/users";
            case "product-service": return "/products";
            case "support-service": return "/support";
            case "notification-service": return "/notifications";
            case "order-service": return "/orders";
            case "payment-service": return "/payments";
            case "file-service": return "/files";
            case "cart-service": return "/cart";
            default: return "";
        }
    }


}
