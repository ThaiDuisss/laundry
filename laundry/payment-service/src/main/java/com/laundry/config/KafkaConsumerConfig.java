package com.laundry.config;

import com.fasterxml.jackson.databind.deser.std.StringDeserializer;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListenerConfigurer;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerEndpointRegistrar;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;
import org.springframework.messaging.handler.annotation.support.MessageHandlerMethodFactory;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class KafkaConsumerConfig implements KafkaListenerConfigurer {
    @Value("${spring.kafka.consumer.group-id}")
    String groupId;

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

    @Value("${spring.kafka.listener.concurrency}")
    int concurrency;

    @Value("${spring.kafka.consumer.client-id}")
    String clientId;

    @Autowired
    LocalValidatorFactoryBean validatorFactory;

    @Bean
    public Map<String, Object> consumerConfigs() {
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
    public ConsumerFactory<String, Object> scanExecNotufyConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> scanNotifyRequestListener() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConcurrency(concurrency);
        factory.setBatchListener(true);
        factory.setConsumerFactory(scanExecNotufyConsumerFactory());
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        factory.getContainerProperties().setGroupId(groupId);
        factory.getContainerProperties().setClientId(clientId + "_" + System.currentTimeMillis());
        factory.getContainerProperties().setConsumerRebalanceListener(new CustomerRebalanceListener());
        return factory;
    }

    @Override
    public void configureKafkaListeners(KafkaListenerEndpointRegistrar registrar) {
        registrar.setMessageHandlerMethodFactory(kafkaHandleMethodFactory());
    }

    @Bean
    public MessageHandlerMethodFactory kafkaHandleMethodFactory() {
        DefaultMessageHandlerMethodFactory factory = new DefaultMessageHandlerMethodFactory();
        factory.setValidator(validatorFactory);
        return factory;
    }

    @Bean
    public LocalValidatorFactoryBean validator() {
        return new LocalValidatorFactoryBean();
    }

    public static class CustomerRebalanceListener implements ConsumerRebalanceListener{

        public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
            log.info("Rebalance Partitions revoked: {}", partitions);
        }

        @Override
        public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
            log.info("Rebalancing - Partitions Assigned: {}", partitions);
        }
    }
}
