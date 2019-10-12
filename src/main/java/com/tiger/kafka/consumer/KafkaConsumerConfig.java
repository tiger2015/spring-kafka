package com.tiger.kafka.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndTimestamp;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@PropertySource(value = {"file:${user.dir}/config/application.properties", "classpath:application.properties"},
        ignoreResourceNotFound = true)
@ComponentScan(value = {"com.tiger.kafka.consumer"})
@EnableKafka
@EnableScheduling
@Slf4j
public class KafkaConsumerConfig {

    @Value("${kafka.bootstrap-servers}")
    private String servers;

    @Value("${kafka.consumer.auto-offset-reset}")
    private String autoOffsetReset;

    @Value("${kafka.consumer.enable-auto-commit}")
    private boolean autoCommit;

    @Value("${kafka.consumer.auto.commit.interval.ms}")
    private int autoCommitIntervalInMillis;

    @Value("${kafka.consumer.group-id}")
    private String groupId;

    @Value("${kafka.listener.concurrency}")
    private int currency;

    @Value("${kafka.consumer.max-poll-records}")
    private int maxPollRecords;

    @Value("${kafka.consumer.poll-timeout.ms}")
    private int pollTimeoutInMillis;

    @Value("${kafka.consumer.key.deserializer}")
    private String keyDeserializer;

    @Value("${kafka.consumer.value.deserializer}")
    private String valueDeserializer;

    @Value("${kafka.consumer.topics}")
    private String[] topics;

    private Map<String, Object> kafkaConsumerConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, autoCommit);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, autoCommitIntervalInMillis);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, keyDeserializer);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, valueDeserializer);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxPollRecords);
        return props;
    }

    private ConsumerFactory consumerFactory() {
        //return new DefaultKafkaConsumerFactory<>(kafkaConsumerConfig());
        return new MyKafkaConsumerFactory<>(kafkaConsumerConfig());
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setConcurrency(currency);
        // 设置批量消费，则需要对应的Listener接口
        factory.setBatchListener(true);
        factory.getContainerProperties().setPollTimeout(pollTimeoutInMillis);
        return factory;
    }


   // @Bean
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
        topicPartitions.forEach(partition -> map.put(partition, System.currentTimeMillis() - 24 * 3600 * 1000L));
        Map<TopicPartition, OffsetAndTimestamp> offsetsForTimes = consumer.offsetsForTimes(map);
        offsetsForTimes.forEach((key, value) -> consumer.seek(key, value.offset()));
        //指定到最新位置进行消费
        // consumer.seekToEnd(topicPartitions);
        return consumer;
    }


}
