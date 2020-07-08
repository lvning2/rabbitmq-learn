package com.zzsoft.rabbitmqdemo.controller;

import com.zzsoft.rabbitmqdemo.test.MailService;
import com.zzsoft.rabbitmqdemo.test.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mail")
public class MailController {

    @Autowired
    private MailService mailService;

    @Autowired
    private SendMessage sendMessage;

    @GetMapping("/send")
    public String send(){

        //mailService.sendSimpleMail("1261751961@qq.com","测试","你好很高兴认识你");
        sendMessage.sendMail("1261751961@qq.com","测试","你好!很高兴认识你");
        return "发送成功";

    }

}


