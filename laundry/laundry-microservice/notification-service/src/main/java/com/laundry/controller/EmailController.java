package com.laundry.controller;

import com.laundry.dto.DTO.NotificationEvent;
import com.laundry.service.MailService;
import com.laundry.service.WebSocketService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
@Slf4j(topic = "EMAIL_CONTROLLER")
public class EmailController {
    MailService mailService;
    WebSocketService webSocketService;

    @GetMapping("/email")
    public String testEmail() {
        return "Email Service is up and running";
    }

    @KafkaListener(topics = "validate-email")
    public void listenEmailTopic(NotificationEvent notificate) {
        log.info("Received email validation event: {}", notificate);
        webSocketService.sendMessageToUser(notificate.getRecipient());
    }

    @KafkaListener(topics = "notification-delivery")
    public void listenNotificationTopic(NotificationEvent message) {
        log.info("Received notification event: {}", message);
        mailService.sendEmail(message.getRecipient(), message.getBody());
    }
}

