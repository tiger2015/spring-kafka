package com.tiger.kafka.producer.dao;

import org.apache.kafka.clients.producer.ProducerRecord;

public interface KafkaDao<K, V> {
    boolean sendMessage(ProducerRecord<K, V> record);
}
