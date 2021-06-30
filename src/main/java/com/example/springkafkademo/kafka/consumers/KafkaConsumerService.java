package com.example.springkafkademo.kafka.consumers;

import com.example.springkafkademo.domain.User;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.WakeupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final Logger logger;

    public KafkaConsumerService() {
        this.logger = LoggerFactory.getLogger(KafkaConsumerService.class);;
    }

    @KafkaListener(topics = "message",
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
            process(users);
        }catch (WakeupException e){
            logger.info("#### WakeUp Exception ####");
        }
    }

    private void process(List<User> users) {
        users.stream().forEach(u -> System.out.println(String.format("User.name: %s, User.age: %d", u.getName(), u.getAge())));
    }
}
