package com.tiger.kafka.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.BatchMessageListener;

import java.util.List;

@Slf4j
public class MyBatchKafkaMessageListener implements BatchMessageListener<String, String> {
    @Override
    public void onMessage(List<ConsumerRecord<String, String>> data) {
        for (ConsumerRecord<String, String> record : data) {
            log.info("receive:" + record.key() + ", " + record.value());
        }
    }
}
