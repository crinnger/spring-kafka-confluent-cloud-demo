package br.com.crinnger.kafka.springdemo.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.*;

import java.util.Map;

import static org.apache.kafka.clients.CommonClientConfigs.GROUP_ID_CONFIG;
import static org.apache.kafka.clients.CommonClientConfigs.SESSION_TIMEOUT_MS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.*;
import static org.apache.kafka.clients.producer.ProducerConfig.*;

//@Configuration
public class KafkaConfiguration {

    //@Bean
    public ProducerFactory<Integer,String> producerFactory(){
        return new DefaultKafkaProducerFactory<>(
                Map.of( org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"pkc-lzvrd.us-west4.gcp.confluent.cloud:9092",
                        RETRIES_CONFIG,0,
                        BUFFER_MEMORY_CONFIG,33554432,
                        KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
                        VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class)
        );
    }

   // @Bean
    public KafkaTemplate<Integer,String> kafkaTemplate(){
        return new KafkaTemplate<>(producerFactory());
    }

   // @Bean
    public Map<String,Object> consumerProperties(){
        return  Map.of(org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"pkc-lzvrd.us-west4.gcp.confluent.cloud:9092",
                        RETRIES_CONFIG,0,
                        GROUP_ID_CONFIG,"string-ccloud",
                        ENABLE_AUTO_COMMIT_CONFIG, false,
                        SESSION_TIMEOUT_MS_CONFIG,15000,
                        BUFFER_MEMORY_CONFIG,33554432,
                        KEY_DESERIALIZER_CLASS_CONFIG, StringSerializer.class,
                        VALUE_DESERIALIZER_CLASS_CONFIG,StringSerializer.class);
    }

   // @Bean
    public ConsumerFactory<Integer,String> consumerFactory(){
        return new DefaultKafkaConsumerFactory<>(consumerProperties());
    }

    //@Bean
    public KafkaAdmin admin(){
        return new KafkaAdmin(Map.of(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG,"pkc-lzvrd.us-west4.gcp.confluent.cloud:9092"));
    }
}
