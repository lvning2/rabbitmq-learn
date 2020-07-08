package com.zzsoft.rabbitmqcustomer;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRabbit
public class RabbitmqCustomerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RabbitmqCustomerApplication.class, args);
    }

}
