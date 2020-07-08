package com.zzsoft.rabbitmqdemo.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Async
    public void sendSimpleMail(String to,String subject,String content){
        SimpleMailMessage mailMessage=new SimpleMailMessage();
        mailMessage.setTo(to);
        mailMessage.setSubject(subject);
        mailMessage.setText(content);
        mailMessage.setFrom(from);

        javaMailSender.send(mailMessage);
    }




}



