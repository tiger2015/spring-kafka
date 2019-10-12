package com.tiger.kafka.consumer;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(value = {KafkaConsumerConfig.class, KafkaListenerConsumerConfig.class})
public class ConsumerApplicationConfig {
}
