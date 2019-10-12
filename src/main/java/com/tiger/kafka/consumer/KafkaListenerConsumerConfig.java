package com.tiger.kafka.consumer;


import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

@Configuration
@PropertySource(value = {"file:${user.dir}/config/application.properties", "classpath:application.properties"},
        ignoreResourceNotFound = true)
@ComponentScan(value = {"com.tiger.kafka.consumer"})
@Profile("listener")
@EnableKafka
public class KafkaListenerConsumerConfig extends BasicKafkaConsumerConfig {

    @Value("${kafka.listener.concurrency}")
    private int concurrency;
    @Value("${kafka.consumer.poll-timeout.ms}")
    private int pollTimeoutInMillis;

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setConcurrency(concurrency);
        // 设置批量消费，则需要对应的Listener接口
        factory.setBatchListener(true);
        factory.getContainerProperties().setPollTimeout(pollTimeoutInMillis);
        return factory;
    }

    @Bean
    public Consumer consumer() {
        return new KafkaConsumer(kafkaConsumerConfig());
    }
}
