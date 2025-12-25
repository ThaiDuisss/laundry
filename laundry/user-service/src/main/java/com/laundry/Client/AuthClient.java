    package com.laundry.Client;

    import org.springframework.cloud.openfeign.FeignClient;
    import org.springframework.http.MediaType;
    import org.springframework.web.bind.annotation.DeleteMapping;
    import org.springframework.web.bind.annotation.PathVariable;

    @FeignClient(name = "auth-service", url = "http://localhost:8081")
    public interface AuthClient {
        @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
        Object delete(@PathVariable("id") Long id);
    }
