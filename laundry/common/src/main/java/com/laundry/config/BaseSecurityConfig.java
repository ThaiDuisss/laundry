//package com.laundry.config;
//
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.web.SecurityFilterChain;
//
//public abstract class BaseSecurityConfig {
//
//    protected abstract String[] getPublicEndpoints();
//
//
//    protected abstract CustomJwtDecoder getJwtDecoder();
//
//    protected JwtAuthenticationEntrypoint getAuthenticationEntryPoint() {
//        return new JwtAuthenticationEntrypoint();
//    }
//
//    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
//        http.securityMatcher("/**")
//                .authorizeHttpRequests(req -> req.requestMatchers(getPublicEndpoints()).permitAll()
//                        .anyRequest().authenticated());
//
//        http.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwtConfigurer ->
//                        jwtConfigurer.decoder(getJwtDecoder()))
//                .authenticationEntryPoint(getAuthenticationEntryPoint()));
//
//        http.csrf(AbstractHttpConfigurer::disable);
//        http.sessionManagement(session ->
//                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//        );
//
//        return http.build();
//    }
//}