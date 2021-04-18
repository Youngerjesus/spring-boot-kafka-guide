package com.example.springkafkademo;

import com.example.springkafkademo.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.stream.IntStream;

@Service
public class KafkaProducerService {
    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerService.class);
    private static final String TOPIC = "yongwook";
    private static final String INVALID_TOPIC = "jeongmin";
    private static final String USER_TOPIC = "user";

    private KafkaTemplate<String, String> stringKafkaTemplate;

    private KafkaTemplate<String, User> userKafkaTemplate;

    @Autowired
    public KafkaProducerService(KafkaTemplate<String, String> stringKafkaTemplate, KafkaTemplate<String, User> userKafkaTemplate) {
        this.stringKafkaTemplate = stringKafkaTemplate;
        this.userKafkaTemplate = userKafkaTemplate;
    }

    public void sendMessage(String message) {
        logger.info(String.format("#### -> Producing message -> %s", message));

        ListenableFuture<SendResult<String, String>> future = this.stringKafkaTemplate.send(TOPIC, message);

        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onFailure(Throwable throwable) {
                logger.info("Producing message Failure");
            }

            @Override
            public void onSuccess(SendResult<String, String> stringStringSendResult) {
                logger.info("Producing message Success");
            }
        });
    }

    public void sendMessageUsingInvalidTopic(String message){
        logger.info(String.format("#### -> Invalid Producing message -> %s", message));

        ListenableFuture<SendResult<String, String>> future = this.stringKafkaTemplate.send(INVALID_TOPIC, message);

        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onFailure(Throwable throwable) {
                logger.info("Producing message Failure");
            }

            @Override
            public void onSuccess(SendResult<String, String> stringStringSendResult) {
                logger.info("Producing message Success");
            }
        });
    }

    public void sendUser(String username, int age) {
        logger.info(String.format("#### -> Producing user -> %s", username));

        ListenableFuture<SendResult<String, User>> future = this.userKafkaTemplate.send(USER_TOPIC, "user", new User(username, age));
    }

    public void sendUsers(){
        logger.info(String.format("#### -> Producing users ####"));
        IntStream.rangeClosed(1, 100).forEach(i -> {
            this.userKafkaTemplate.send(USER_TOPIC, "user", new User("BatchUser", i));
        });
    }
}
