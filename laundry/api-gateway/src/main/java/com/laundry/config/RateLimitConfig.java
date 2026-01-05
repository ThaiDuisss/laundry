package com.laundry.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Configuration
public class RateLimitConfig {

    @Bean
    public KeyResolver ipKeyResolver() {
        return exchange -> {
            String ip = exchange.getRequest().getHeaders().getFirst("X-Forwarded-For");
            if (ip == null || ip.isEmpty()) {
                ip = exchange.getRequest()
                        .getRemoteAddress()
                        .getAddress()
                        .getHostAddress();
            }
            return Mono.just(ip.split(",")[0].trim());
        };
    }
}
