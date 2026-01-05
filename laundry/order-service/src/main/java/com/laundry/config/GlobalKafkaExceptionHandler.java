package com.laundry.config;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.springframework.kafka.listener.ConsumerAwareListenerErrorHandler;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service(value = "globalKafkaExceptionHandler")
public class GlobalKafkaExceptionHandler implements ConsumerAwareListenerErrorHandler {

    @Override
    public Object handleError(
            @NonNull Message<?> message,
            @NonNull ListenerExecutionFailedException exception,
            @NonNull Consumer<?, ?> consumer) {
        if(exception.getCause() instanceof MethodArgumentNotValidException manve) {
            List<Map<String ,String>> errors = manve.getBindingResult()
                    .getAllErrors().stream()
                    .map(objectError -> {
                        String fieldName = objectError instanceof FieldError ? ((FieldError)objectError).getField() : "unknown";
                        return Map.of(fieldName, Objects.requireNonNull(objectError.getDefaultMessage()));
                    }).toList();
            String payloadType = message.getPayload().getClass().getTypeName();
            Object topicName = message.getHeaders().get("kafka_receivedTopic");
            log.error("Received invalid object of type '{}' from '{}' topic failing these validations {}",
                    payloadType, topicName, errors);
        }else {
            log.error("Error handling kafka message", exception.getCause());
        }
        Acknowledgment acknowledgment = message.getHeaders().get(KafkaHeaders.ACKNOWLEDGMENT, Acknowledgment.class);
        if (acknowledgment != null) {
            acknowledgment.acknowledge();
        }
        return null;
    }
}
