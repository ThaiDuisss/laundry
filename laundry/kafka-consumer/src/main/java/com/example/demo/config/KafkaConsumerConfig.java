package com.example.demo.config;

import com.fasterxml.jackson.databind.deser.std.StringDeserializer;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KafkaConsumerConfig {

    @Value("${spring.kafka.consumer.group-id}")
    String groupId;

    @Value("${spring.kafka.producer.retries}")
    int retries;

    @Value("${spring.kafka.consumer.auto-offset-reset}")
    String autoOffsetReset;

    @Value("${spring.kafka.consumer.enable-auto-commit}")
    boolean enableAutoCommit;

    @Value("${spring.kafka.bootstrap-servers}")
    String bootstrapServers;

    @Value("${spring.kafka.consumer.fetch-max-wait-ms}")
    int fetchMaxWaitMs;

    @Value("${spring.kafka.consumer.fetch-min-bytes}")
    int fetchMinBytes;

    @Value("${spring.kafka.consumer.heartbeat-interval-ms}")
    int heartbeatIntervalMs;

    @Value("${spring.kafka.consumer.max-poll-interval-ms}")
    int maxPollIntervalMs;

    @Value("${spring.kafka.consumer.max-poll-records}")
    int maxPollRecords;

    @Value("${spring.kafka.consumer.session-timeout-ms}")
    int sessionTimeoutMs;

    @Bean
    public Map<String, Object> consumerConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, enableAutoCommit);
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxPollRecords);
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, sessionTimeoutMs);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
        props.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, fetchMinBytes);
        props.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, fetchMaxWaitMs);
        props.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, heartbeatIntervalMs);
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, maxPollIntervalMs);
        return props;
    }

    @Bean
    public ConsumerFactory<String, Object>  consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfig());
    }

    @Bean

}
