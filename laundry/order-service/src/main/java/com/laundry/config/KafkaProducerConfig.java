package com.laundry.config;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KafkaProducerConfig {
    @Value("${spring.kafka.producer.acks}")
    String acks;

    @Value("${spring.kafka.boostrap-servers}")
    String bootstrapServers;

    @Value("${spring.kafka.producer.compression-type}")
    String compressionType;

    @Value("${spring.kafka.producer.retries}")
    int retries;

    @Value("${spring.kafka.producer.buffer-memory}")
    int bufferMemory;

    @Value("${spring.kafka.producer.linger-ms}")
    int lingerMs;

    @Value("${spring.kafka.producer.batch-size}")
    int batchSize;

    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.ACKS_CONFIG, acks);
        props.put(ProducerConfig.RETRIES_CONFIG, retries);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, batchSize);
        props.put(ProducerConfig.LINGER_MS_CONFIG, lingerMs);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, bufferMemory);
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, compressionType);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerialize.class);
        return props;
    }

    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public KafkaTemplate<String,Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
