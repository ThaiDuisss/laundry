//package com.laundry.config;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.laundry.dto.response.ApiResponse;
//import com.laundry.enums.ErrorEnum;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.http.MediaType;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.AuthenticationEntryPoint;
//
//import java.io.IOException;
//
//public class JwtAuthenticationEntrypoint implements AuthenticationEntryPoint {
//    @Override
//    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
//        ErrorEnum error = ErrorEnum.UNAUTHORIZED;
//        response.setStatus(error.getCode());
//        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//
//        ApiResponse<?> apiResponse = ApiResponse.builder()
//                .code(error.getCode())
//                .message(error.getMessage())
//                .build();
//        ObjectMapper mapper = new ObjectMapper();
//        response.getWriter().write(mapper.writeValueAsString(apiResponse));
//        response.flushBuffer();
//    }
//}
