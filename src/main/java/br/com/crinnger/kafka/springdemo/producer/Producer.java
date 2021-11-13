package br.com.crinnger.kafka.springdemo.producer;

import com.github.javafaker.Faker;
import io.confluent.developer.avro.Hobbit;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Component
public class Producer {
    private final KafkaTemplate<Integer,Hobbit> templateAvro;
    private final KafkaTemplate<Integer,String> template;

    Faker faker;

    //@EventListener(ApplicationStartedEvent.class)
    public void generate(){
        faker=Faker.instance();
        final Flux<Long> interval = Flux.interval(Duration.ofSeconds(1));
        final Flux<String> stringFlux= Flux.fromStream(Stream.generate(() ->  faker.hobbit().quote()));
        Flux.zip(interval,stringFlux)
                .map(objects ->template.send("hobbit",faker.random().nextInt(100),objects.getT2())).blockLast();
    }

    @EventListener(ApplicationStartedEvent.class)
    public void generateAvro(){
        faker=Faker.instance();
        final Flux<Long> interval = Flux.interval(Duration.ofSeconds(1));
        final Flux<Hobbit> stringFlux= Flux.fromStream(Stream.generate(() ->  new Hobbit(faker.hobbit().quote())));
        Flux.zip(interval,stringFlux)
                .map(objects ->templateAvro.send("hobbit-avro",faker.random().nextInt(100),objects.getT2())).blockLast();
    }
}
