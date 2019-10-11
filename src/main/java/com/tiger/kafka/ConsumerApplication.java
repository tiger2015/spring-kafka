package com.tiger.kafka;


import com.tiger.kafka.consumer.KafkaConsumerConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ConsumerApplication {

    public static final AnnotationConfigApplicationContext CONTEXT =
            new AnnotationConfigApplicationContext(KafkaConsumerConfig.class);
    public static void main(String[] args) {

    }
}
