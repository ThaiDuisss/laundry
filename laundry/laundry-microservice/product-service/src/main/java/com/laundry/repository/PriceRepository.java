package com.laundry.repository;

import com.laundry.entity.Prices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface PriceRepository extends JpaRepository<Prices, Integer> {
    @Query("SELECT p FROM Prices p WHERE p.item.id = :itemId")
    List<Prices> findByItemId(@Param("itemId") Integer itemId);
}
