package com.zzsoft.rabbitmqdemo;

import com.rabbitmq.client.Channel;
import com.zzsoft.rabbitmqdemo.config.RabbitConfig;
import com.zzsoft.rabbitmqdemo.domain.EmailEntity;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.handler.annotation.Header;

import java.io.IOException;

@SpringBootTest
class RabbitmqDemoApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = RabbitConfig.QUEUE_INFORM_EMAIL)
    public void recrive(EmailEntity emailEntity, Message message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) {
        if (emailEntity.getId()==2){

            try {

                channel.basicAck(tag,false);

            } catch (IOException e) {
                try {
                    channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                e.printStackTrace();
            }

        }

        System.out.println("1:"+emailEntity);

    }

    @RabbitListener(queues = RabbitConfig.QUEUE_INFORM_EMAIL)
    @Test
    public void recriveEmail(EmailEntity emailEntity, Message message, Channel channel){

        System.out.println("email:"+emailEntity);
    }


    @RabbitListener(queues = RabbitConfig.QUEUE_INFORM_SMS)
    @Test
    public void recriveSms(EmailEntity emailEntity, Message message, Channel channel){
        System.out.println("sms:"+emailEntity);
    }

}
