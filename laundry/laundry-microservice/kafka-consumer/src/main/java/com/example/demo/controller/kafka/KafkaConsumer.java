package com.example.demo.controller.kafka;

import com.example.demo.service.RuleEngine;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.RetriableException;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

@Slf4j(topic = "KAFKA-Test")
@RequiredArgsConstructor
@Component
public class KafkaConsumer {
    private final RuleEngine ruleEngine;

    @RetryableTopic(
            attempts = "4", // 1 primary topic + 3 retry topic + 1 DLT topic
            backoff = @Backoff(delay = 1000, multiplier = 2), // Delay theo cấp số nhân
            dltStrategy =  DltStrategy.FAIL_ON_ERROR, //No retry on DLT topic
            autoCreateTopics = "true", // true for test, false: production, deployed
            include = {RetriableException.class, RuntimeException.class} // RuntimeException for test, nên tự custom các exception có thể retry

    )
    @KafkaListener(properties = {"spring.json.value.default.type=com.example.demo.model.dto.TransactionMessageKafka"},
    topics = "${kafka.transaction.topic}",
    concurrency = "${kafka.transaction.concurrency}")
    @SneakyThrows
    public void listenTransactions(@Payload String message) {
        log.info("retry is {}", message);

//        log.info("Received a transaction message - code: {} - meta: {} - payload: {}", transactionMessageKafka.getMessageCode(), transactionMessageKafka.getMeta(), transactionMessageKafka.getPayload());
//        ruleEngine.process(transactionMessageKafka.getPayload());
        throw new RuntimeException("LOI ROI NHE {}");
    }

    @DltHandler
    void retryBookingCommand(@Payload String message) {
        log.info("Message is {}", message);
    }
}
