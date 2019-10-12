package com.tiger.kafka.producer;

import com.tiger.kafka.service.KafkaService;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.core.appender.mom.kafka.KafkaProducerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@EnableScheduling
@Slf4j
public class MessageProducer {
    @Autowired
    private KafkaService<String, String> kafkaService;
    private static Random random = new Random();

    @Scheduled(fixedRate = 1000L)
    public void sendMessage() {
        long current = System.currentTimeMillis() / 1000L;
        for (int i = 0; i < 1000; i++) {
            kafkaService.sendMessage("test", i + "", "hello-" + current);
        }
//        if (KafkaProducerConfig.gridVrs == null) {
//            return;
//        }
//        for (int i = 0; i < KafkaProducerConfig.gridVrs.length; i++) {
//            kafkaService.sendMessage("gridvrs", KafkaProducerConfig.gridVrs[i], "1");
//        }
    }
}
