package br.com.crinnger.kafka.springdemo.config;


import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.processor.WallclockTimestampExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;

import java.time.Duration;
import java.util.Map;

import static org.apache.kafka.streams.StreamsConfig.*;

//@Configuration
//@EnableKafka
//@EnableKafkaStreams
public class KafkaStreamConfig {

    //@Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    public KafkaStreamsConfiguration kafkaStreamsConfiguration(){
        return new KafkaStreamsConfiguration(Map.of(
                APPLICATION_ID_CONFIG,"testStreams",
                BOOTSTRAP_SERVERS_CONFIG,"pkc-lzvrd.us-west4.gcp.confluent.cloud:9092",
                DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.Integer().getClass().getName(),
                DEFAULT_VALUE_SERDE_CLASS_CONFIG,Serdes.String().getClass().getName(),
                DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG, WallclockTimestampExtractor.class.getName()
        ));
    }

    //@Bean
    public KStream<Integer,String> kStream(StreamsBuilder streamsBuilder){
        KStream<Integer,String> stream = streamsBuilder.stream("streamingtopic");
        stream.mapValues((ValueMapper<String,String>) String::toUpperCase)
                .groupByKey()
                .windowedBy(TimeWindows.of(Duration.ofSeconds(1)))
                .reduce((String value1,String value2) -> value1 + value2, Named.as("windowStore"))
                .toStream()
                .map((windowedId,value) -> new KeyValue<>(windowedId.key(),value))
                .filter((i,s) -> s.length()>40)
                .to("streamingtopic2");
        stream.print(Printed.toSysOut());
        return stream;
    }
}
