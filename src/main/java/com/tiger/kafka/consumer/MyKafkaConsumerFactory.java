package com.tiger.kafka.consumer;


import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.Map;
import java.util.Properties;

/**
 * @ClassName MyKafkaConsumerFactory
 * @Description TODO
 * @Author zeng.h
 * @Date 2019/10/12 11:18
 * @Version 1.0
 **/
@Slf4j
public class MyKafkaConsumerFactory<K, V> extends DefaultKafkaConsumerFactory<K, V> {
    public MyKafkaConsumerFactory(Map<String, Object> configs) {
        super(configs);
    }

    public MyKafkaConsumerFactory(Map<String, Object> configs, Deserializer<K> keyDeserializer,
                                  Deserializer<V> valueDeserializer) {
        super(configs, keyDeserializer, valueDeserializer);
    }

    @Override
    public Consumer<K, V> createConsumer(String groupId, String clientIdPrefix, String clientIdSuffixArg, Properties properties) {
        log.info("create consumer 5 ");
        return super.createConsumer(groupId, clientIdPrefix, clientIdSuffixArg, properties);
    }

}
