package br.com.crinnger.kafka.springdemo.processor;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Locale;

@Component
public class Processor {

    @Autowired
    public void process(StreamsBuilder builder){
        final Serde<String> stringSerde= Serdes.String();
        final Serde<Long> longSerde = Serdes.Long();
        final Serde<Integer> integerSerdeSerde = Serdes.Integer();
        KStream<Integer,String> textLines = builder.stream("hobbit", Consumed.with(integerSerdeSerde,stringSerde));
        KTable<String, Long> wordCounts=textLines
                .flatMapValues(value -> Arrays.asList(value.toLowerCase().split("\\W+")))
                .groupBy((key,value)->value, Grouped.with(stringSerde,stringSerde))
                .count();
        wordCounts.toStream().to("stream-wordcount-topic", Produced.with(stringSerde,longSerde));
    }
}
