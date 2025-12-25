package com.laundry.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Where;

@Entity
@Getter
@Setter
@FieldDefaults(level =  AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "is_deleted=false")
public class UserProfiles extends AbstractEntity<Long> {
    @Column(columnDefinition = "bigint", unique = true)
    Long userId;

    @Column(columnDefinition = "varchar(50)")
    String fullName;

    @Column(columnDefinition = "varchar(10)", unique = true)
    String phoneNumber;

    @Column(columnDefinition = "varchar(255)")
    String avatar;

 

}
