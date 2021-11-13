package br.com.crinnger.kafka.springdemo.consumer;


import io.confluent.developer.avro.Hobbit;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class Consumer {

    @KafkaListener(topics = {"hobbit"}, groupId = "spring-boot-kafka")
    public void consume(ConsumerRecord<Integer,String> record){
        System.out.println("Key = " + record.key() +" value = " + record.value());
    }

    @KafkaListener(topics = {"hobbit"}, groupId = "spring-boot-kafka2")
    public void consume(String quote){
        System.out.println(quote);
    }


    @KafkaListener(topics = {"hobbit-avro"}, groupId = "spring-boot-kafka")
    public void consumeAvro(ConsumerRecord<Integer, Hobbit> record){
//        Hobbit hobbit=  record.value();

        System.out.println("Avro Key = " + record.key() +" value = " + record.value());

        if(record.value() instanceof Hobbit){
            System.out.println("Avro Hobbit =" +  record.value().getQuote());
        }
    }
}
