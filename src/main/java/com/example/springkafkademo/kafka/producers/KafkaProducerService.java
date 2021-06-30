package com.example.springkafkademo.kafka.producers;

import com.example.springkafkademo.domain.User;
import com.example.springkafkademo.infra.ControlPlane;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.errors.AuthorizationException;
import org.apache.kafka.common.errors.OutOfOrderSequenceException;
import org.apache.kafka.common.errors.ProducerFencedException;
import org.apache.kafka.common.errors.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {
    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerService.class);
    private static final String TOPIC = "message";
    private static final String USER_TOPIC = "user";

    private final KafkaTemplate<String, String> stringKafkaTemplate;
    private final KafkaTemplate<String, User> userKafkaTemplate;
    private final ControlPlane controlPlane;

    /**
     * 파티션에 데이터를 보낼때 String 데이터 전송도 가능합니다.
     * @param message 보낼 메시지를 말합니다.
     */
    public void sendMessage(String message) {
        logger.info(String.format("#### -> Producing message -> %s", message));
        try{
            send(message);
        }
        catch (ProducerFencedException | OutOfOrderSequenceException | AuthorizationException e) {
            logger.info(e.getMessage());
        }
        catch (TimeoutException e) {
            logger.error("Producing message Timeout Exception " + e.getMessage());
            controlPlane.refusingTraffic();
        }
        catch (KafkaException e) {
            logger.error("Producing message Kafka Exception " + e.getMessage());
        }
    }

    private void send(String message) {
        stringKafkaTemplate.send(TOPIC, message)
                .addCallback(result -> {
                    assert result != null;
                    RecordMetadata metadata = result.getRecordMetadata();
                    logger.info("Producing message Success topic {} partition {} offset {}",
                            metadata.topic(),
                            metadata.partition(),
                            metadata.offset());
                },
                exception -> logger.error("Producing message Failure " + exception.getMessage()));
    }

    /**
     * 파티션에 데이터를 보낼때 객체 데이터 전송도 가능합니다.
     * @param User 보낼 메시지를 말합니다.
     */
    public void sendUser(User user) {
        logger.info(String.format("#### -> Producing User -> %s", user.toString()));
        try{
            send(user);
        }
        catch (ProducerFencedException | OutOfOrderSequenceException | AuthorizationException e) {
            logger.info(e.getMessage());
        }
        catch (TimeoutException e) {
            logger.error("Producing User Timeout Exception " + e.getMessage());
            controlPlane.refusingTraffic();
        }
        catch (KafkaException e) {
            logger.error("Producing User Kafka Exception " + e.getMessage());
        }
    }

    private void send(User user) {
        userKafkaTemplate.send(USER_TOPIC, user)
                .addCallback(result -> {
                    assert result != null;
                    RecordMetadata metadata = result.getRecordMetadata();
                    logger.info("Producing message Success topic {} partition {} offset {}",
                            metadata.topic(),
                            metadata.partition(),
                            metadata.offset());
                },
                exception -> logger.error("Producing User Failure " + exception.getMessage()));
    }
}
