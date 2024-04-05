package com.scent.email;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class EmailController {
	
    private final EmailService emailService;

    @Autowired
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/api/send-email")
    public String sendEmail(@RequestBody EmailRequest emailRequest) {
        // 이메일을 보내는 로직
        //String to = "buskingworld@naver.com"; // 관리자 이메일 주소
    	List<String> toList = Arrays.asList("scdata@naver.com", "scmasterz@naver.com", "eventsc@naver.com");
    	String subject = emailRequest.getSubject();
        String text = emailRequest.getText();

        emailService.sendEmail(toList, subject, text);

        return "이메일이 성공적으로 전송되었습니다.";
    }
}