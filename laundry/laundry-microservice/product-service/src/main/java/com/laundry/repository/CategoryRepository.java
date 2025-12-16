package com.laundry.repository;

import com.laundry.entity.ProductCategories;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<ProductCategories, Integer> {
}
