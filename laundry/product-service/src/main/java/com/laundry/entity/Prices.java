package com.laundry.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Where;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Where(clause = "is_deleted=false")
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"item_id", "service_id"})
)
public class Prices extends AbstractEntity<Integer> {
    @ManyToOne
    @JoinColumn(name = "item_id")
    Items item;

    @ManyToOne
    @JoinColumn(name = "service_id")
    Services service;

    BigDecimal price;
}
