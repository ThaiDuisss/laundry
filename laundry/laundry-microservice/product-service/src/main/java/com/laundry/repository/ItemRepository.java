package com.laundry.repository;

import com.laundry.entity.Items;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends BaseRepository<Items, Integer> {
    @Query("Select i from Items i where i.category.id = :categoryId")
    List<Items> findByCategoryId(@Param("categoryId")Integer categoryId);
    @Query("SELECT i FROM Items i " +
            "WHERE (:categoryId IS NULL OR :categoryId = 0 OR i.category.id = :categoryId) " +
            "AND (:keyword IS NULL OR i.name LIKE CONCAT('%', :keyword, '%'))")
    Page<Items> search(@Param("categoryId") int categoryId, @Param("keyword") String keyword, Pageable pageable);
}
