package com.zzsoft.rabbitmqdemo.controller;

import com.zzsoft.rabbitmqdemo.test.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class TestController {

    @Autowired
    SendMessage sendMessage;

    @Autowired
    ApplicationContext ioc;


    @RequestMapping("/test")
    public String test(){
        sendMessage.sendTest();
        return "ok";
    }


    @RequestMapping("/recrive")
    public void recrive(){



    }



}




