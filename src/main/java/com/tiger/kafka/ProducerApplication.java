package com.tiger.kafka;


import com.tiger.kafka.producer.KafkaProducerConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ProducerApplication {

    public static final AnnotationConfigApplicationContext CONTEXT;

    static {
//        System.setProperty("log4j.configurationFile","./config/log4j2.xml");
        CONTEXT = new AnnotationConfigApplicationContext(KafkaProducerConfig.class);
    }

    public static void main(String[] args) {


    }
}
