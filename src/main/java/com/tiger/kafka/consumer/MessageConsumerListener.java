package com.tiger.kafka.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.OffsetAndTimestamp;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.ConsumerSeekAware;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
@Profile("listener")
public class MessageConsumerListener implements ConsumerSeekAware {

    @Autowired
    private Consumer consumer;

    @Value("${kafka.consumer.topics}")
    private String[] topics;
    @Value("${kafka.consumer.timestamp-offset.ms}")
    private long timeOffsetInMills;
    private Map<String, OffsetAndTimestamp> partitionOffsetAndTimestampMap = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        log.info("init partition info");
        List<PartitionInfo> partitionInfos = new ArrayList<>();
        for (String topic : topics) {
            partitionInfos.addAll(consumer.partitionsFor(topic));
        }
        Map<TopicPartition, Long> timeStampPartitionMap = new HashMap<>();
        log.info("search time offset:" + timeOffsetInMills);
        partitionInfos.forEach(partition -> timeStampPartitionMap.put(new TopicPartition(partition.topic(),
                        partition.partition()),
                System.currentTimeMillis() - timeOffsetInMills));
        Map<TopicPartition, OffsetAndTimestamp> map = consumer.offsetsForTimes(timeStampPartitionMap);
        map.forEach((key, value) -> {
            log.info("partition info:" + key.toString() + ",offset:" + value);
            if (value != null && key != null) {
                partitionOffsetAndTimestampMap.put(key.toString(), value);
            }
        });
    }


    @KafkaListener(topics = "${kafka.consumer.topics}", containerFactory = "kafkaListenerContainerFactory")
    public void batchConsume(List<ConsumerRecord<String, String>> consumerRecords) {
        for (ConsumerRecord<String, String> record : consumerRecords) {
            //log.info("receive message:" + record.key() + "," + record.value());
        }
    }

    @Override
    public void registerSeekCallback(ConsumerSeekCallback callback) {
    }

    @Override
    public void onPartitionsAssigned(Map<TopicPartition, Long> assignments, ConsumerSeekCallback callback) {
        log.info("on partition assigned");
        assignments.forEach((key, value) -> {
            if (partitionOffsetAndTimestampMap.containsKey(key.toString())) {
                callback.seek(key.topic(), key.partition(),
                        partitionOffsetAndTimestampMap.get(key.toString()).offset());
                log.info("partition:" + key.toString() + " seek to:" + partitionOffsetAndTimestampMap.get(key.toString()).offset());
            } else {
                callback.seekToEnd(key.topic(), key.partition());
                log.info("partition:" + key.toString() + " seek to end");
            }
        });
    }

    @Override
    public void onIdleContainer(Map<TopicPartition, Long> assignments, ConsumerSeekCallback callback) {

    }
}
