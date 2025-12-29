package com.example.kafka_lib.util;


import com.example.kafka_lib.constant.EventType;
import com.example.kafka_lib.dto.KafkaMessage;
import com.example.kafka_lib.dto.Meta;

import java.util.UUID;

public class MessageBuilder {
    public static <T>KafkaMessage<T> build(String serviceId, String messageCode, EventType type, T payload){
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
