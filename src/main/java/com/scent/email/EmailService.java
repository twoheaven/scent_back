package com.scent.email;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(List<String> toList, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
    	message.setFrom("scdata@naver.com");
        message.setTo(toList.toArray(new String[0]));
        message.setSubject(subject);
        message.setText(text);

        mailSender.send(message);
    }
}