package com.tiger.kafka.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Slf4j
@PropertySource(value = {"file:${user.dir}/config/application.properties","classpath:application.properties"},
        ignoreResourceNotFound = true)
@ComponentScan(value = {"com.tiger.kafka.dao", "com.tiger.kafka.service", "com.tiger.kafka.producer"})
public class KafkaProducerConfig {
    @Value("${kafka.bootstrap-servers}")
    private String servers;

    @Value("${kafka.producer.key.serializer}")
    private String keySerializer;

    @Value("${kafka.producer.value.serializer}")
    private String valueSerializer;

    @Bean
    public Map<String, Object> producerProperties() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializer);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer);
        log.info("init producer config");
        return props;
    }

    @Bean
    public ProducerFactory producerFactory() {
        ProducerFactory producerFactory = new DefaultKafkaProducerFactory(producerProperties());
        return producerFactory;
    }

    @Bean
    public KafkaTemplate kafkaTemplate(@Autowired ProducerFactory producerFactory) {
        KafkaTemplate kafkaTemplate = new KafkaTemplate(producerFactory);
        return kafkaTemplate;
    }
}
