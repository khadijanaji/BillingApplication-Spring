package org.enset.kafkastreamsbilling;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.*;
import org.enset.kafkastreamsbilling.model.Bill;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
@Component
public class KafkaStreamConsumer {
    ObjectMapper objectMapper=new ObjectMapper();
    @Bean
    private void start(){
        Properties properties=new Properties();
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
        properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG,"steams-consumer-25");
        properties.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG,1000);
        StreamsBuilder streamsBuilder=new StreamsBuilder();
        KStream<String,String> kStream=
                streamsBuilder.stream("Bills", Consumed.with(Serdes.String(), Serdes.String()));

        KStream<Windowed<String>, Double> counts_for_5_secondes = kStream
                .map((k, v) -> new KeyValue<>(k, billObject(v)))
                .map((k, v) -> new KeyValue<>(k, v.getMontant()))
                .groupByKey(Grouped.with(Serdes.String(), Serdes.Double()))
                .windowedBy(TimeWindows.of(Duration.ofSeconds(20)))
                .reduce(Double::sum)
                .toStream();

        KStream<String, Double> counts = kStream
                .map((k, v) -> new KeyValue<>(k, billObject(v)))
                .map((k, v) -> new KeyValue<>(k, v.getMontant()))
                .groupByKey(Grouped.with(Serdes.String(), Serdes.Double()))
                .reduce(Double::sum)
                .toStream();

        counts_for_5_secondes.foreach((k,v)->{
            System.out.println("************** Total Bills after 5 secondes ***************");
            System.out.println("Customer = "+k.key()+", Total = "+v);
            System.out.println("**********************************************************");
        });

        counts.foreach((k,v)->{
            System.out.println("************** Total bills for customers ***************");
            System.out.println("Customer = "+k+", Total = "+v);
            System.out.println("**********************************************************");
        });

        Topology topology=streamsBuilder.build();
        KafkaStreams kafkaStreams=new KafkaStreams(topology,properties);
        kafkaStreams.start();
    }

    public Bill billObject(String strObject){
        Bill bill = new Bill();
        try {
            bill = objectMapper.readValue(strObject, Bill.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bill;
    }
}
