package com.zzsoft.rabbitmqdemo.test;

import com.alibaba.fastjson.JSON;
import com.zzsoft.rabbitmqdemo.config.RabbitConfig;
import com.zzsoft.rabbitmqdemo.domain.EmailEntity;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.UUID;


@Component
public class SendMessage {      // 向队列里发送消息

    @Resource(name = "mail")
    RabbitTemplate rabbitTemplate;

    @Value("${spring.mail.username}")
    String from;

    public void sendTest(){

        for (int i=0;i<5;i++){
            String msg="这是第"+(i+1)+"个邮件消息";
            EmailEntity emailEntity=new EmailEntity();
            emailEntity.setId(i+1);
            emailEntity.setHost("www.baidu.com");
            emailEntity.setPassword("123456");
            emailEntity.setUsername("root");
            emailEntity.setMsg(msg);
            CorrelationData correlationData=new CorrelationData();
            correlationData.setId(UUID.randomUUID().toString());
            rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_TOPICS_INFORM,"inform.sms.email",emailEntity,correlationData);
        }

    }


    // 发送邮件
    public void sendMail(String to,String subject,String content){
        SimpleMailMessage mailMessage=new SimpleMailMessage();
        mailMessage.setTo(new String[]{to});
        mailMessage.setSubject(subject);
        mailMessage.setText(content);
        mailMessage.setFrom(from);
        String s = JSON.toJSONString(mailMessage);

        CorrelationData correlationData=new CorrelationData();
        correlationData.setId(UUID.randomUUID().toString());

        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_FANOUT,"inform.email",s,correlationData);
    }



}
