package com.example.springkafkademo;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
    Spring Kafka를 사용하지 않고 KafkaConsumer를 사용할 때 동작하는 원리에 대한 클래스 파일입니다.
 */
@Service
public abstract class AbstractConsumer<K, V> implements Runnable{
    private final Logger logger;
    private final KafkaConsumer<K,V> consumer;
    private final List<String> topics;

    public AbstractConsumer(KafkaConsumer<K, V> consumer, List<String> topics) {
        this.consumer = consumer;
        this.topics = topics;
        logger = LoggerFactory.getLogger(AbstractConsumer.class);
    }

    public abstract void process(ConsumerRecord<K, V> record);

    @Override
    public void run() {
        try (consumer){
            consumer.subscribe(topics);

            while (true){
                ConsumerRecords<K, V> records = consumer.poll(Long.MAX_VALUE);
                records.forEach(record -> process(record));
                consumer.commitSync();
            }
        }catch (WakeupException e){
            consumer.commitSync();
        }
    }

    public void shutdown(){
        consumer.wakeup();
    }

    public static void main(String[] args) {
//        Runtime.getRuntime().addShutdownHook(new ShutdownThread());
//        ExecutorService executorService = Executors.newCachedThreadPool();
//        for (int i = 0; i < CONSUMER_COUNT; i++) {
//            ConsumerWorker worker = new ConsumerWorker(configs, TOPIC_NAME, i);
//            workerThreads.add(worker);
//            executorService.execute(worker);
//        }
    }

    static class ShutdownThread extends Thread {
        public void run() {
//            workerThreads.forEach(ConsumerWorker::shutdown);
//            System.out.println("Bye");
        }
    }
}
