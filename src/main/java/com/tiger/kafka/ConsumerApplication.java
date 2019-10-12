package com.tiger.kafka;


import com.tiger.kafka.consumer.ConsumerApplicationConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.File;

public class ConsumerApplication {
    public static final AnnotationConfigApplicationContext CONTEXT;

    static {
        File file = new File("./config/log4j2.xml");
        if (file.exists()) {
            System.setProperty("log4j.configurationFile", "./config/log4j2.xml");
        }
        System.setProperty("spring.profiles.active","consumer");
//        System.setProperty("spring.profiles.active", "listener");
        CONTEXT = new AnnotationConfigApplicationContext(ConsumerApplicationConfig.class);
    }

    public static void main(String[] args) {

    }
}
