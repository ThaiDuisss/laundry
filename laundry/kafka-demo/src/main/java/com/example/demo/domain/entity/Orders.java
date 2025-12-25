package com.example.demo.domain.entity;

import com.example.demo.domain.constant.OrderStatus;
import com.laundry.entity.AbstractEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Where;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "is_deleted=false")
public class Orders extends AbstractEntity<Long> implements Serializable {
    Long userId;

    Long staffId;

    String address;

    String phoneNumber;

    BigDecimal totalPrice;

    OrderStatus status = OrderStatus.PENDING;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    List<OrderItems> orderItems;
}
