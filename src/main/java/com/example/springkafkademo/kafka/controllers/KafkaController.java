package com.example.springkafkademo.kafka.controllers;

import com.example.springkafkademo.domain.User;
import com.example.springkafkademo.kafka.producers.KafkaProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/kafka")
public class KafkaController {
    private final KafkaProducerService producer;

    @Autowired
    KafkaController(KafkaProducerService producer){
        this.producer = producer;
    }

    @PostMapping(value = "/publish/message")
    public void sendMessageToKafkaTopic(@RequestParam String message){
        this.producer.sendMessage(message);
    }

    @PostMapping(value = "/publish/user")
    public void sendUserToKafkaTopic(@RequestBody User user){
        this.producer.sendUser(user);
    }
}
