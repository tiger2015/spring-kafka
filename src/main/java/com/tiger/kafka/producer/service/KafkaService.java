package com.tiger.kafka.producer.service;

public interface KafkaService<K, V> {
    boolean sendMessage(String topic, K key, V value);
}
