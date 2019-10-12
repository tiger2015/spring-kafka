package com.tiger.kafka.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndTimestamp;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.listener.BatchMessageListener;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;

import java.util.*;

@Configuration
@PropertySource(value = {"file:${user.dir}/config/application.properties", "classpath:application.properties"},
        ignoreResourceNotFound = true)
@ComponentScan(value = {"com.tiger.kafka.consumer"})
@Profile("consumer")
@EnableKafka
@Slf4j
public class KafkaConsumerConfig extends BasicKafkaConsumerConfig {

    @Value("${kafka.consumer.topics}")
    private String[] topics;
    @Value("${kafka.consumer.timestamp-offset.ms}")
    private long timeOffsetInMills;
    @Value("${kafka.listener.concurrency}")
    private int concurrency;
    @Value("${kafka.consumer.poll-timeout.ms}")
    private int pollTimeoutInMillis;

    @Bean
    public KafkaConsumer kafkaConsumer() {
        KafkaConsumer consumer = new KafkaConsumer(kafkaConsumerConfig());
        List<TopicPartition> topicPartitions = new ArrayList<>();
        for (String topic : topics) {
            List<PartitionInfo> list = consumer.partitionsFor(topic);
            for (PartitionInfo partitionInfo : list) {
                log.info("partition info: " + partitionInfo.toString());
                topicPartitions.add(new TopicPartition(partitionInfo.topic(), partitionInfo.partition()));
            }
        }
        consumer.assign(topicPartitions);
        // 指定到不同的时间戳进行消费
        Map<TopicPartition, Long> map = new HashMap<>();
        topicPartitions.forEach(partition -> map.put(partition, System.currentTimeMillis() - timeOffsetInMills));
        Map<TopicPartition, OffsetAndTimestamp> offsetsForTimes = consumer.offsetsForTimes(map);
        offsetsForTimes.forEach((key, value) -> {
            if (value == null) {
                consumer.seekToEnd(Arrays.asList(key));
            } else {
                consumer.seek(key, value.offset());
            }
        });
        // 指定到最新位置进行消费
        //consumer.seekToEnd(topicPartitions);
        return consumer;
    }

    private BatchMessageListener batchMessageListener() {
        return new MyBatchKafkaMessageListener();
    }

    @Bean
    public ConcurrentMessageListenerContainer container() {
        ContainerProperties containerProperties = new ContainerProperties(topics);
        containerProperties.setPollTimeout(pollTimeoutInMillis);
        containerProperties.setMessageListener(batchMessageListener());
        ConcurrentMessageListenerContainer container = new ConcurrentMessageListenerContainer(consumerFactory(),
                containerProperties);
        container.setConcurrency(concurrency);
        return container;
    }
}
