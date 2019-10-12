package com.tiger.kafka;


import com.tiger.kafka.producer.config.KafkaProducerConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.File;

public class ProducerApplication {

    public static final AnnotationConfigApplicationContext CONTEXT;

    static {
        File file = new File("./config/log4j2.xml");
        if (file.exists()) {
            System.setProperty("log4j.configurationFile", "./config/log4j2.xml");
        }
        CONTEXT = new AnnotationConfigApplicationContext(KafkaProducerConfig.class);
    }

    public static void main(String[] args) {

    }
}
