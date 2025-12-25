package com.example.demo.infrastructor.service;

import com.example.demo.domain.constant.MessageCode;
import com.example.demo.service.TransactionProducer;
import com.example.kafka_lib.constant.EventType;
import com.example.kafka_lib.util.MessageBuilder;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j(topic = "Message send")
public class TransactionProducerImpl {
    @NonFinal
    @Value("${kafka.transaction}")
    String transactionTopic;
    @NonFinal
    @Value("${application-name}")
    String serviceId;

    KafkaTemplate<String, Object> kafkaTemplate;

    public void send(String topic, String messageCode, String key, Object value) {
        var message = MessageBuilder.build(serviceId, messageCode, EventType.EVENT, value);
        kafkaTemplate.send(topic, key, message)
                .whenComplete((res, ex) -> {
                    if (ex == null) {
                        var metadata = res.getRecordMetadata();
                        log.info("Message sent to topic={}, partition={} offset={} value={}"
                                , metadata.topic(), metadata.partition(), metadata.offset(), value);
                    } else {
                        log.error("Failed to send message: value={}, error={}", value, ex.getMessage());
                    }
                });
    }
}
