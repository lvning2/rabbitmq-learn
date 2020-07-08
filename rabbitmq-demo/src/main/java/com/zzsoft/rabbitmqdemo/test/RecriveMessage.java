package com.zzsoft.rabbitmqdemo.test;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import com.zzsoft.rabbitmqdemo.config.RabbitConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Random;


@Component
public class RecriveMessage {   // 接受

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    JavaMailSender javaMailSender;

    Random random=new Random();

//    @RabbitListener(queues = RabbitConfig.QUEUE_INFORM_EMAIL)
//    public void recrive(EmailEntity emailEntity, Message message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) {
//        if (emailEntity.getId()==2){
//
//            try {
//
//                channel.basicAck(tag,false);
//
//            } catch (IOException e) {
//                try {
//                    channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
//                } catch (IOException ioException) {
//                    ioException.printStackTrace();
//                }
//                e.printStackTrace();
//            }
//
//        }
//
//        System.out.println("1:"+emailEntity);
//
//    }

//    @RabbitListener(queues = RabbitConfig.QUEUE_DLX_EMAIL)
//    public void recriveDlx(Object o, Message message, Channel channel){
//        System.out.println("死信："+o);
//    }




//    @RabbitListener(queues = RabbitConfig.QUEUE_INFORM_EMAIL)
//    public void recriveEmail(EmailEntity emailEntity, Message message, Channel channel){
//
//        System.out.println("email:"+emailEntity);
//    }
//
//
//    @RabbitListener(queues = RabbitConfig.QUEUE_INFORM_SMS)
//    public void recriveSms(EmailEntity emailEntity, Message message, Channel channel){
//        System.out.println("sms:"+emailEntity);
//    }


    //@RabbitListener(queues = RabbitConfig.QUEUE_INFORM_EMAIL)
    public void recriveEmail(String s, Message message, Channel channel){
        System.out.println("消费消息："+s);
        SimpleMailMessage mailMessage = JSON.parseObject(s, SimpleMailMessage.class);

        int i = random.nextInt(10);
        if (i%2==0){


        }

        javaMailSender.send(mailMessage);




        System.out.println("email:"+mailMessage);

    }


}



