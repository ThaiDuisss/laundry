package com.laundry.config;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KafkaAdminConfig {
    @Value("${spring.kafka.boostrap-servers}")
    String bootstrapServers;

    @Value("${config.kafka.transaction}")
    String  topicTransaction;

    @Value("${config.kafka.partition}")
    int partition;

    @Value("${config.kafka.replicationFactor}")
    int replicationFactor;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> config = new HashMap<>();
        log.info("bootstrapServers: {}", bootstrapServers);
        config.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        return new KafkaAdmin(config);
    }

    @Bean
    public AdminClient adminClient() {
        return AdminClient.create(kafkaAdmin().getConfigurationProperties());
    }

    @Bean
    public KafkaAdmin.NewTopics creNewTopics() {
        return new KafkaAdmin.NewTopics(newTopicsList().toArray(new NewTopic[0]));
    }

    @Bean
    public List<NewTopic> newTopicsList() {
        return List.of(
                new NewTopic(topicTransaction, partition, (short) replicationFactor)
        );
    }

}
