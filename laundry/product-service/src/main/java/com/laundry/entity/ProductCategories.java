package com.laundry.entity;

import com.laundry.enums.CategoryEnum;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Where;

import java.util.List;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "is_deleted=false")
public class ProductCategories extends AbstractEntity<Integer> {
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    CategoryEnum name;

    String description;

    @OneToMany(mappedBy = "category")
    List<Items> items;
}
