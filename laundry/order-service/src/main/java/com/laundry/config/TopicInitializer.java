package com.laundry.config;

import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

@Configuration
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class TopicInitializer {
    AdminClient adminClient;
    List<NewTopic> topics;

    @PostConstruct
    public void createTopics(){
        try {
            Set<String> topics = adminClient.listTopics().names().get();
            for (NewTopic topic : this.topics) {
                if (!topics.contains(topic.name())) {
                    adminClient.createTopics(List.of(topic)).all().get();
                    log.info("Topic {} created successfully", topic.name());
                } else {
                    log.info("Topic {} already exists", topic.name());
                }
            }
        } catch (Exception e) {
            log.info("Failed to create topics {}",e.getMessage());
        }
    }
}
