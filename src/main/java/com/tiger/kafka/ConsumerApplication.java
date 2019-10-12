package com.tiger.kafka;


import com.tiger.kafka.consumer.KafkaConsumerConfig;
import com.tiger.kafka.producer.KafkaProducerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.File;

public class ConsumerApplication {
    public static final AnnotationConfigApplicationContext CONTEXT;

    static {
        File file = new File("./config/log4j2.xml");
        if (file.exists()) {
            System.setProperty("log4j.configurationFile", "./config/log4j2.xml");
        }
        CONTEXT = new AnnotationConfigApplicationContext(KafkaConsumerConfig.class);
    }

    public static void main(String[] args) {

    }
}
