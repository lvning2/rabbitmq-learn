package com.zzsoft.rabbitmqcustomer.service;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Random;

@Service
public class MailReceiveService {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    JavaMailSender javaMailSender;

    Random random=new Random();

    @RabbitListener(queues = "a")
    public void recriveEmail(String s, Message message, Channel channel) throws IOException {

        System.out.println("A开始消费消息："+s);
        SimpleMailMessage mailMessage = JSON.parseObject(s, SimpleMailMessage.class);

        int i = random.nextInt(10);
        long deliveryTag = message.getMessageProperties().getDeliveryTag();

        try {
            javaMailSender.send(mailMessage);
            channel.basicAck(deliveryTag,false);
        } catch (MailException e) {
            channel.basicNack(deliveryTag,false,false);
            System.out.println("放入死信队列");
            e.printStackTrace();
        }


    }


}
