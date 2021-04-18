package com.example.springkafkademo;

import com.example.springkafkademo.domain.User;
import org.apache.kafka.common.errors.WakeupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class KafkaConsumerService {

    private final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);

    @KafkaListener(topics = "yongwook",
            containerFactory = "stringKafkaListenerContainerFactory")
    public void consume(String message) throws IOException {
        logger.info(String.format("#### -> Consumed message -> %s", message));
    }

    @KafkaListener(topics = "user",
            containerFactory = "userKafkaListenerContainerFactory"
    )
    public void consume(List<User> users){
        logger.info(String.format("#### -> Consumed message -> Success #### Users size:%d ",  users.size()));
        try{
            users.stream().forEach(u -> {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(String.format("User.name: %s, User.age: %d", u.getName(), u.getAge()));
            });
        }catch (WakeupException e){
            logger.info("#### WakeUp Exception ####");
        }
    }
}
