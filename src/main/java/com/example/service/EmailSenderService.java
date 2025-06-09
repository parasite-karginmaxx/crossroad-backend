package com.example.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailSenderService {

    private final JavaMailSender mailSender;

    public void send(String to, String subject, String text) {
        //System.out.printf(">>> [email] to: %s%nsubject: %s%ntext: %s%n", to, subject, text);

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            message.setFrom("crossroadhotel@yandex.ru");

            mailSender.send(message);
            System.out.println(">>> Письмо отправлено успешно");
        } catch (Exception e) {
            System.out.println(">>> Ошибка при отправке письма:");
            e.printStackTrace();
        }
    }
}
