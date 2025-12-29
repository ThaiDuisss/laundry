package com.laundry.utils;




import com.laundry.dto.DTO.KafkaMessage;
import com.laundry.dto.DTO.Meta;
import com.laundry.enums.EventType;

import java.util.UUID;

public class MessageBuilder {
    public static <T> KafkaMessage<T> build(String serviceId, String messageCode, EventType type, T payload){
        KafkaMessage<T> message = new KafkaMessage<>();
        Meta meta = Meta.builder()
                .messageId(generateMessageId())
                .serviceId(serviceId)
                .type(type.toString())
                .build();
        message.setMeta(meta);
        message.setMessageCode(messageCode);
        message.setPayload(payload);
        return message;
    }
    public static String generateMessageId() {
        return UUID.randomUUID().toString();
    }
}
