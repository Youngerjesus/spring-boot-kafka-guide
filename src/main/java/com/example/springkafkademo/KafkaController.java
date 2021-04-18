package com.example.springkafkademo;

import com.example.springkafkademo.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/kafka")
public class KafkaController {
    private final KafkaProducerService producer;

    @Autowired
    KafkaController(KafkaProducerService producer){
        this.producer = producer;
    }

    @PostMapping(value = "/publish/default")
    public void sendMessageToKafkaTopic(@RequestParam("message") String message){
        this.producer.sendMessage(message);
    }

    @PostMapping(value = "/publish/invalid")
    public void sendMessageToKafkaTopicUsingInvalidTopic(@RequestParam("message") String message){
        this.producer.sendMessageUsingInvalidTopic(message);
    }

    @PostMapping(value = "/publish/user")
    public void sendUserToKafkaTopic(@RequestParam("name") String username, @RequestParam("age") int age){
        this.producer.sendUser(username, age);
    }

    @PostMapping(value = "/publish/users")
    public void sendUsersToKafkaTopic(){
        this.producer.sendUsers();
    }
}
