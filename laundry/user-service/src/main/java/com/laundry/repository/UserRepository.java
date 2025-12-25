package com.laundry.repository;

import com.laundry.entity.UserProfiles;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends BaseRepository<UserProfiles, Long> {
    boolean existsByPhoneNumber(String phoneNumber);
    Optional<UserProfiles> getByUserId(Long userId);
    Optional<UserProfiles> findByUserId(Long userId);
    @Query(value = "SELECT * FROM user_profiles u WHERE LOWER(u.full_name) LIKE LOWER(CONCAT('%', :keyword, '%'))",
            nativeQuery = true)    List<UserProfiles> findByFullName(@Param("keyword") String key);
}
