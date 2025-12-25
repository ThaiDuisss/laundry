package com.laundry.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.nio.file.Files;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j(topic = "MAIL_SERVICE")
public class MailService {
    JavaMailSender mailSender;
    public void sendEmail(String toEmail, String token) {

        try {
            log.info("Preparing to send email to: {}", toEmail);
            String htmlContent = loadHtmlTemplate();
            String link = "http://localhost:8080/auth/validateEmail/" + token;
            htmlContent = htmlContent.replace("{{TOKEN_LINK}}", link);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject("🔒 Xác nhận đăng ký tài khoản");
            helper.setText(htmlContent, true); // true = nội dung là HTML
            log.info("Sending email to: {}", toEmail);
            mailSender.send(message);
        } catch (IOException | MessagingException e) {
            log.info("Failed to send email to: {}", toEmail);

            throw new RuntimeException(e);
        }
    }
//    public void sendEmailAgain(String email) {
//        User user = userRepository.findByEmailAndProvider(email, Provider.LOCAL).orElseThrow(()-> new AppException(ErrorEnum.USER_NOT_FOUND));
//        String token = jwtService.generateAccessToken(user.getUserId(), email, null);
//        sendEmail(email,token);
//    }

    private String loadHtmlTemplate() throws IOException {
        ClassPathResource resource = new ClassPathResource("templates/verification-email.html");
        return new String(Files.readAllBytes(resource.getFile().toPath()));
    }


}


