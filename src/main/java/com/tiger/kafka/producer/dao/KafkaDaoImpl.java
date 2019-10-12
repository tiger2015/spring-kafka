package com.tiger.kafka.producer.dao;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Repository
@Slf4j
public class KafkaDaoImpl<K, V> implements KafkaDao<K, V> {

    @Autowired
    private KafkaTemplate<K, V> kafkaTemplate;

    @Override
    public boolean sendMessage(ProducerRecord<K, V> record) {
        ListenableFuture future = kafkaTemplate.send(record);
        future.addCallback(new ListenableFutureCallback() {
            @Override
            public void onFailure(Throwable throwable) {
            }

            @Override
            public void onSuccess(Object o) {
                log.info("send message success");
            }
        });
        return true;
    }
}
