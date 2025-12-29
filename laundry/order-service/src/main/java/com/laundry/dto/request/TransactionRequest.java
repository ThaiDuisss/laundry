package com.laundry.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class TransactionRequest {
    String transactionType;
    String debitAccount;
    String creditAccount;
    BigDecimal amount;
    Integer status;
    Long createdAt;
}
