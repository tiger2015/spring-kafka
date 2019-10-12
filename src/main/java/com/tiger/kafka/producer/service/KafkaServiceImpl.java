package com.tiger.kafka.producer.service;

import com.tiger.kafka.producer.dao.KafkaDao;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KafkaServiceImpl<K, V> implements KafkaService<K, V> {
    @Autowired
    private KafkaDao<K, V> kafkaDao;

    @Override
    public boolean sendMessage(String topic, K key, V value) {
        ProducerRecord<K, V> record = new ProducerRecord<>(topic, key, value);
        return kafkaDao.sendMessage(record);
    }
}
