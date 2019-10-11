package com.tiger.kafka.consumer;

import com.tiger.kafka.model.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.ConsumerSeekAware;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

//@Component
@Slf4j
public class MessageConsumer implements ConsumerSeekAware {

   // @KafkaListener(id = "group1", topics = {""}, containerFactory = "kafkaListenerContainerFactory")
    public void consume(User user, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                        @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key,
                        @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long timestamp) {
        log.info("receive message:user:" + user.getName() + ", age:" + user.getAge());
    }

   // @KafkaListener(id = "group2", topics = {""}, containerFactory = "kafkaListenerContainerFactory")
    public void batchConsume(List<ConsumerRecord> consumerRecords) {
        log.info("receive message size:" + consumerRecords.size());
    }


   // @KafkaListener(id = "group3", topics = {"result"}, containerFactory = "kafkaListenerContainerFactory")
    public void consumeResult(String value, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                              @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                              @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key) {
        log.info("receive message key:" + key + ", value:" + value);
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
