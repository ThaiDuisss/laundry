package com.laundry.entity;

import com.laundry.enums.ServiceEnum;
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
public class Services extends AbstractEntity<Integer> {
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    ServiceEnum name;

    String description;

    @OneToMany(mappedBy = "service")
    List<Prices> prices;
}
