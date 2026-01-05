package com.laundry.repository;

import com.laundry.entity.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<com.laundry.entity.OrderItems, Long> {
}
