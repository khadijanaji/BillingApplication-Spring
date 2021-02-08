package org.enset.billingkafkaproducer.service;

import lombok.extern.slf4j.Slf4j;
import org.enset.billingkafkaproducer.model.Bill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
@Component
@Slf4j
public class BillingSource implements ApplicationRunner {
    @Autowired
    private KafkaTemplate<String, Bill> kafkaTemplate;
    private final String topic="Bills";

    @Override
    public void run(ApplicationArguments applicationArguments) {
        List<String> names= Arrays.asList("Hassan","Mohamed","Hanane","Yassine","Samir","Aziz");
        Runnable runnable=()->{
            String customer=names.get(new Random().nextInt(names.size()));
            Bill bill=new Bill();
            bill.setMontant(new Random().nextInt(10000));
            bill.setCustomer(customer);
            kafkaTemplate.send(topic,bill.getCustomer(),bill);
        };
        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(runnable,5,5, TimeUnit.SECONDS);
    }
}

