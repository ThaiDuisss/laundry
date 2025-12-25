//package com.laundry.config;
//
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//public class SecurityConfig extends BaseSecurityConfig {
//    @Override
//    protected String[] getPublicEndpoints() {
//        return new String[]{
//                "/swagger-ui/**",
//                "/v3/api-docs/**",
//                "/swagger-ui.html",
//                "/webjars/**",
//                "/actuator/**"
//        };
//    }
//
//
//    @Override
//    protected CustomJwtDecoder getJwtDecoder() {
//        return new CustomJwtDecoder();
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        return super.configure(http);
//    }
//}
