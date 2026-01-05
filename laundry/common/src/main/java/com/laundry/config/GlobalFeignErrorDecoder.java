//package com.laundry.config;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.laundry.dto.response.ApiResponse;
//import com.laundry.exception.AppException;
//import feign.Response;
//import feign.codec.ErrorDecoder;
//import org.apache.commons.io.IOUtils;
//import org.springframework.stereotype.Component;
//
//import java.nio.charset.StandardCharsets;
//@Component
//public class GlobalFeignErrorDecoder implements ErrorDecoder {
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//    @Override
//    public Exception decode(String methodKey, Response response) {
//        String messaString = "";
//        int code = 0;
//        try {
//            String body = IOUtils.toString(response.body().asInputStream(), StandardCharsets.UTF_8);
//
//            ApiResponse<?> apiError = objectMapper.readValue(body, ApiResponse.class);
//            messaString = apiError.getMessage();
//            code = apiError.getCode();
//
//            throw new AppException(code, messaString);
//
//        } catch (Exception e) {
//            throw new AppException(code, messaString);
//        }
//    }
//}
