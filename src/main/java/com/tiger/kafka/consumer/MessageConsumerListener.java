package com.tiger.kafka.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.ConsumerSeekAware;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Slf4j
@Profile("kafkaListener")
public class MessageConsumerListener implements ConsumerSeekAware {

    @KafkaListener(topics = "${kafka.consumer.topics}", containerFactory = "kafkaListenerContainerFactory")
    public void batchConsume(List<ConsumerRecord<String, String>> consumerRecords) {
        for (ConsumerRecord<String, String> record : consumerRecords) {
            log.info("receive message:" + record.key() + "," + record.value());
        }
    }

    @Override
    public void registerSeekCallback(ConsumerSeekCallback callback) {
    }

    @Override
    public void onPartitionsAssigned(Map<TopicPartition, Long> assignments, ConsumerSeekCallback callback) {
        for (TopicPartition topicPartition : assignments.keySet()) {
            callback.seekToEnd(topicPartition.topic(), topicPartition.partition());
        }
        log.info("seek to partition offset to end");
    }

    @Override
    public void onIdleContainer(Map<TopicPartition, Long> assignments, ConsumerSeekCallback callback) {

    }
}
