package org.enset.billingkafkaproducer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class BillingKafkaProducerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BillingKafkaProducerApplication.class, args);
    }

}
