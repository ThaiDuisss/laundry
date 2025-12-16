//package com.laundry.config;
//
//import org.springframework.data.domain.AuditorAware;
//import org.springframework.security.core.context.SecurityContextHolder;
//
//import java.util.Optional;
//
//public class AuditorAwareImpl implements AuditorAware<Long> {
//    @Override
//    public Optional<Long> getCurrentAuditor() {
//        String name = SecurityContextHolder.getContext().getAuthentication().getName();
//        return Optional.empty();
//    }
//}
