package com.zzsoft.rabbitmqdemo.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;



@Configuration
public class RabbitConfig {

    public static final String QUEUE_INFORM_EMAIL="queue_inform_email";
    public static final String QUEUE_INFORM_SMS="queue_inform_sms";
    public static final String QUEUE_DLX_EMAIL="queue_dlx_email";
    public static final String EXCHANGE_TOPICS_INFORM="exchange_topics_inform";
    public static final String EXCHANGE_DLX="exchange_dlx";
    public static final String EXCHANGE_FANOUT="exchange_fanout";


    @Bean(EXCHANGE_TOPICS_INFORM)
    public Exchange EXCHANGE_TOPICS_INFORM(){
        return ExchangeBuilder.topicExchange(EXCHANGE_TOPICS_INFORM).durable(true).build();
    }


    @Bean(EXCHANGE_DLX)
    public Exchange DLX_EXCHANGE(){
        return ExchangeBuilder.topicExchange(EXCHANGE_DLX).durable(true).build();
    }

    @Bean(QUEUE_INFORM_EMAIL)
    public Queue QUEUE_INFORM_EMAIL(){
        Map<String,Object> map=new HashMap<>();
        map.put("x-message-ttl",10000);
        map.put("x-dead-letter-exchange",EXCHANGE_DLX);
        map.put("x-dead-letter-routing-key","dlx.sms.email");
        Queue queue=new Queue(QUEUE_INFORM_EMAIL,true,false,false,map);
        return queue;
    }

    @Bean("mail")
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        RabbitTemplate.ConfirmCallback confirmCallback=new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String s) {
                String msgId=correlationData.getId();

                if (ack){   // confirm 成功
                    System.out.println(msgId+" confirm成功，更新数据库");
                    System.out.println(s);
                }else {
                    System.out.println(msgId+" confirm失败");
                }
            }
        };

        //回调函数: return返回， 这里是预防消息不可达的情况，比如 MQ 里面没有对应的 exchange、queue 等情况，
        //    如果消息真的不可达，那么就要根据你实际的业务去做对应处理，比如是直接落库，记录补偿，还是放到死信队列里面，之后再进行落库
        //    这里脱开实际业务场景，不大好描述
        RabbitTemplate.ReturnCallback returnCallback=new RabbitTemplate.ReturnCallback() {
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
                System.out.println("message:"+message+" replyCode:"+replyCode+" replyText:"+replyText+" exchange:"+exchange+" routingKey:"+routingKey);
            }
        };

        rabbitTemplate.setConfirmCallback(confirmCallback);
        rabbitTemplate.setReturnCallback(returnCallback);

        return rabbitTemplate;

    }



    @Bean(QUEUE_INFORM_SMS)
    public Queue QUEUE_INFORM_SMS(){
        Queue queue=new Queue(QUEUE_INFORM_SMS);
        return queue;
    }

    @Bean(QUEUE_DLX_EMAIL)
    public Queue QUEUE_DLX_EMAIL(){
        Queue queue=new Queue(QUEUE_DLX_EMAIL);
        return queue;
    }

    @Bean
    public MessageConverter messageConverter(){
        Jackson2JsonMessageConverter jsonConverter = new Jackson2JsonMessageConverter();
        return jsonConverter;
    }

    @Bean
    public Binding BINDING_QUEUE_INFORM_SMS(@Qualifier(EXCHANGE_TOPICS_INFORM) Exchange exchange,@Qualifier(QUEUE_INFORM_SMS) Queue queue){
        return BindingBuilder.bind(queue).to(exchange).with("inform.#.sms.#").noargs();
    }


    @Bean
    public Binding BINDING_QUEUE_INFORM_EMAIL(@Qualifier(EXCHANGE_TOPICS_INFORM) Exchange exchange,@Qualifier(QUEUE_INFORM_EMAIL) Queue queue){

        return BindingBuilder.bind(queue).to(exchange).with("inform.#.email.#").noargs();
    }

    @Bean
    public Binding BINDING_QUEUE_DLX_EMAIL(@Qualifier(EXCHANGE_DLX) Exchange exchange,@Qualifier(QUEUE_DLX_EMAIL) Queue queue){
        return BindingBuilder.bind(queue).to(exchange).with("dlx.#.email").noargs();
    }

    @Bean(EXCHANGE_FANOUT)
    public Exchange EXCHANGE_FANOUT(){
        return ExchangeBuilder.fanoutExchange("exchange_fanout").durable(true).build();
    }

    @Bean("a")
    public Queue QUEUE_a(){
        Map<String,Object> map=new HashMap<>();
        map.put("x-dead-letter-exchange",EXCHANGE_DLX);
        map.put("x-dead-letter-routing-key","dlx.sms.email");
        Queue queue=new Queue("a",true,false,false,map);
        return queue;
    }

    @Bean("b")
    public Queue QUEUE_b(){
        Queue queue=new Queue("b",true,false,false);
        return queue;
    }

    @Bean
    public Binding fanout_binding_a(@Qualifier("a") Queue queue,@Qualifier(EXCHANGE_FANOUT) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with("a").noargs();
    }

    @Bean
    public Binding fanout_binding_b(@Qualifier("b") Queue queue,@Qualifier(EXCHANGE_FANOUT) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with("a").noargs();
    }



}
