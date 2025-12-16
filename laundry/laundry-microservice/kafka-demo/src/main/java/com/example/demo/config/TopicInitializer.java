package com.example.demo.config;

import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Configuration
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class TopicInitializer {
    AdminClient adminClient;
    List<NewTopic> newTopicsList;

    @PostConstruct
    public void createTopics(){
        try{
            Set<String> topics = adminClient.listTopics().names().get();
            for(NewTopic topic : newTopicsList){
                log.info("Tried to Creating topic {}",topic.name());
                if(!topics.contains(topic.name())){
                    adminClient.createTopics(Collections.singletonList(topic)).all().get();
                    log.info("Created topic {}",topic.name());
                }
                else{
                    log.info("Topic {} already exists",topic.name());
                }
            }
        }catch (Exception e){
            log.info("Failed to create topics {}",e.getMessage());
        }
    }
}
