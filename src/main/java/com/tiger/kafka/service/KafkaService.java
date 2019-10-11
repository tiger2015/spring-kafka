package com.tiger.kafka.service;

public interface KafkaService<K, V> {
    boolean sendMessage(String topic, K key, V value);
}
