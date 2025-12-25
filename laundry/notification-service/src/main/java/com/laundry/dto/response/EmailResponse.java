package com.laundry.dto.response;

import com.laundry.dto.request.Recipient;
import com.laundry.dto.request.Sender;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;


@Getter
@Setter
@FieldDefaults(level =  AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailResponse {
    String messageId;
}
