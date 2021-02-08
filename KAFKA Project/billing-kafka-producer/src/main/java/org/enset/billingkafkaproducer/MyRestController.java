package org.enset.billingkafkaproducer;

import org.apache.commons.logging.Log;
import org.enset.billingkafkaproducer.model.Bill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.HashSet;
import java.util.Random;

@RestController
public class MyRestController {
    @Autowired
    private KafkaTemplate<String, Bill> kafkaTemplate;
    private final String topic="Bills";

    public MyRestController(KafkaTemplate<String, Bill> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @GetMapping("/send/{customer}")
    public String send(@PathVariable String customer){
        Bill bill=new Bill();
        bill.setMontant(new Random().nextInt(10000));
        bill.setCustomer(customer);;
        kafkaTemplate.send(topic,bill.getCustomer(),bill);
        return "message published";
    }
}

