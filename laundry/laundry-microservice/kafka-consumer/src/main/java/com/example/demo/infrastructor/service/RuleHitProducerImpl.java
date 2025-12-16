package com.example.demo.infrastructor.service;

import com.example.demo.model.constant.MessageCode;
import com.example.demo.model.event.RuleHitEvent;
import com.example.demo.service.RuleHitProducer;
import com.example.kafka_lib.constant.EventType;
import com.example.kafka_lib.util.MessageBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Slf4j
@Component
public class RuleHitProducerImpl implements RuleHitProducer {
    @Value("${application-name}")
    private String serviceId;

    @Value("${kafka.rule-hit.topic}")
    private String ruleHitTopic;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void send(RuleHitEvent event) {
        try {
            var message = MessageBuilder.build(
                    serviceId,
                    MessageCode.RULE_HIT.getCode(),
                    EventType.EVENT,
                    event
            );

            kafkaTemplate.send(ruleHitTopic, message);
        } catch (Exception e) {
            log.error("Failed to produce the message to topic: " + ruleHitTopic);
            e.printStackTrace();
        }
    }
}
