package com.laundry.fallback;

import com.laundry.dto.response.ApiResponse;
import com.laundry.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.http.ResponseEntity;
import java.lang.reflect.Proxy;
@Slf4j
public class FallbackBase<T> implements FallbackFactory<T> {
    private final Class<T> clientClass;

    public FallbackBase(Class<T> clientClass) {
        this.clientClass = clientClass;
    }

    @Override
    public T create(Throwable cause) {
        if(cause instanceof AppException) {
            log.error("AppException in {}: {}", clientClass.getSimpleName(), cause.getMessage());
            throw (AppException) cause;
        }
        log.error("Fallback triggered for {}: {}", clientClass.getSimpleName(), cause.getMessage());
        return (T) Proxy.newProxyInstance(
                clientClass.getClassLoader(),
                new Class<?>[]{clientClass},
                (proxy, method, args) -> {
                    log.warn("Method {} is being called in fallback mode.", method.getName());
                    throw new AppException(
                            503,
                            clientClass.getSimpleName() + " service is currently unavailable. Please try again later."
                                        );
                }
        );
    }
}
