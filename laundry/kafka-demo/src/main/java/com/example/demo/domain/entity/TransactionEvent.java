package com.example.demo.domain.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class TransactionEvent {
    String transactionId;
    String transactionType;
    String debitAccount;
    String creditAccount;
    BigDecimal amount;
    Integer status;
    Long createdAt;
}
