package com.laundry.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Where;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "is_deleted=false")
public class OrderItems extends AbstractEntity<Long> {
    String name;

    Long prices;

    String avatar;

    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    Orders order;
}
