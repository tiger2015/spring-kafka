package com.tiger.kafka.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Slf4j
@Component
public class ConsumeTask {

    @Autowired
    private Consumer<String, String> consumer;


    @Scheduled(fixedRate = 200)
    public void consume() {
        ConsumerRecords<String, String> records = consumer.poll(Duration.ZERO);
        for (ConsumerRecord record : records) {
            log.info("receive:" + record.toString());
        }
    }
}
