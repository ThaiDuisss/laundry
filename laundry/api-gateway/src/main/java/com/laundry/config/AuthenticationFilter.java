package com.laundry.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laundry.dto.response.ApiResponse;
import com.laundry.service.IdentityService;
import com.laundry.utils.SecurityUtils;
import com.laundry.utils.Utils;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

@Component
@Slf4j
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@EnableWebFluxSecurity
public class AuthenticationFilter implements GlobalFilter, Ordered {
    ObjectMapper objectMapper;
    AntPathMatcher pathMatcher = new AntPathMatcher();
    IdentityService identityService;
    @NonFinal
    List<String> WHITE_LIST = List.of(
            "/auth/sign-in",
            "/auth/register",
            "/auth/validateEmail",
            "/auth/refresh-token",
            "/auth/introspect",
            "/auth/logout",
            "/files/media/download/**",
            "/actuator/**",
            "/auth/mail-again",
            "/products/**",
            "/cart/add-cart",
            "/cart/get-cart"
    );
    @NonFinal
    List<String> SWAGGER_WHITELIST = List.of(
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/webjars/**",
            "/v3/api-docs/**",
            "/*/v3/api-docs/**",
            "/auth/validateEmail/**",
            "/products/**",
            "/actuator/**",
            "/files/media/download/**" //<-- Cho phép mọi service con, ví dụ /notifications/v3/api-docs
    );

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable);
        return http.build();
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("AuthenticationFilter called{} ", exchange.getRequest());

        List<String> authHeader = exchange.getRequest().getHeaders().get(org.springframework.http.HttpHeaders.AUTHORIZATION);

        if (CollectionUtils.isEmpty(authHeader)) {
            if (isPublicPath(exchange.getRequest())) {
                return chain.filter(exchange);
            }
            return unauthenticated(exchange.getResponse());
        }
        String token = authHeader.get(0).replaceAll("Bearer ", "");
        log.info("Token: {}", token);

        return identityService.introspect(token).flatMap(userId -> {
            if (userId.getCode() != 200 || Objects.isNull(userId.getData() )) {
                if (isPublicPath(exchange.getRequest())) {
                    return chain.filter(exchange);
                }
                return unauthenticated(exchange.getResponse());
            }
            return identityService.getInfo(Long.parseLong(userId.getData().toString())).flatMap(userProfile -> {
                if (userProfile == null || userProfile.getCode() != 200) {
                    if (isPublicPath(exchange.getRequest())) {
                        return chain.filter(exchange);
                    }
                    return unauthenticated(exchange.getResponse());
                }
                String userJson = Utils.convertObjectToJson(userProfile.getData());
                String userBase64 = SecurityUtils.encodeBase64(userJson);
                return chain.filter(exchange.mutate().request(exchange.getRequest()
                                .mutate()
                                .header("X-User-Info", userBase64)
                                .header("X-User-Id", userId.getData().toString()).build())
                        .build());
            });
        });
    }

    public boolean isPublicPath(ServerHttpRequest request) {
        String path = request.getURI().getPath();
        log.info("Request path: {}", path);
        return WHITE_LIST.stream().anyMatch(path::startsWith)
                || SWAGGER_WHITELIST.stream().anyMatch(p -> pathMatcher.match(p, path));
    }

    @Override
    public int getOrder() {
        return -1;
    }

    Mono<Void> unauthenticated(ServerHttpResponse response) {
        ApiResponse<?> apiResponse = ApiResponse.<String>builder()
                .code(401)
                .status("UNAUTHORIZED")
                .message("You are not authenticated to access this resource")
                .build();
        String body = "";
        try {
            body = objectMapper.writeValueAsString(apiResponse);
        } catch (Exception e) {
            log.error("Error while serializing response: {}", e.getMessage());
        }
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add(org.springframework.http.HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return response.writeWith(Mono.just(response.bufferFactory().wrap(body.getBytes())));
    }
}
