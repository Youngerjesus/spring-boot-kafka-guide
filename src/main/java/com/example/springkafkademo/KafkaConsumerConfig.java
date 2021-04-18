package com.example.springkafkademo;

import com.example.springkafkademo.domain.User;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {

    @Value(value = "${kafka.bootstrapAddress}")
    String bootstrapAddress;

    @Value(value = "group_id")
    String groupId;

    @Value(value = "earliest")
    String autoOffsetResetStrategy;

    @Value(value = "false")
    String enableAutoCommit;

    @Value(value = "5000")
    String autoCommitIntervalMs;

    @Value(value = "30000")
    String sessionTimeoutMs;

    @Value(value = "100")
    String maxPollRecords;

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> props = new HashMap<>();

        props.put(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapAddress);
        props.put(
                ConsumerConfig.GROUP_ID_CONFIG,
                groupId);
        props.put(
                ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,
                enableAutoCommit
        );
        props.put(
                ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG,
                autoCommitIntervalMs
        );
        props.put(
                ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG,
                sessionTimeoutMs
        );
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetResetStrategy);

        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new StringDeserializer());
    }

    @Bean("stringKafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, String>
    stringKafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setConcurrency(3);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.BATCH);
        return factory;
    }


    @Bean("userKafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, User> userKafkaListenerContainerFactory(){
        ConcurrentKafkaListenerContainerFactory<String, User> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(userConsumerFactory());
        factory.setConcurrency(3);
        factory.setBatchListener(true);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.BATCH);
        return factory;
    }

    private ConsumerFactory<String, User> userConsumerFactory() {
        Map<String, Object> props = new HashMap<>();

        JsonDeserializer<User> deserializer = new JsonDeserializer<>(User.class, false);
        props.put(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapAddress);
        props.put(
                ConsumerConfig.GROUP_ID_CONFIG,
                groupId);
        props.put(
                ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,
                enableAutoCommit
        );
        props.put(
                ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG,
                autoCommitIntervalMs
        );
        props.put(
                ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG,
                sessionTimeoutMs
        );
        props.put(
                ConsumerConfig.MAX_POLL_RECORDS_CONFIG,
                maxPollRecords
        );
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,
                autoOffsetResetStrategy);

        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new JsonDeserializer<>(User.class));
    }
}
