package com.laundry.config;

import com.laundry.repository.IdentityClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.List;

@Configuration
public class WebClientConfig {
    @Bean
    WebClient identityWebClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:8081")
                .build();
    }
    @Bean
    IdentityClient identityClient() {
        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(WebClientAdapter.create(identityWebClient()))
                .build();
        return factory.createClient(IdentityClient.class);
    }

    @Bean
    CorsWebFilter corsWebFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:3000")); // Ghi đúng URL FE
        config.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type", "X-Requested-With", "Origin", "Accept"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowCredentials(true); // ⚠️ Bắt buộc khi có cookie hoặc token

        // 🧠 Cho phép expose cookie/header về lại frontend
        config.setExposedHeaders(List.of("Authorization", "Set-Cookie"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsWebFilter(source);
    }

}
