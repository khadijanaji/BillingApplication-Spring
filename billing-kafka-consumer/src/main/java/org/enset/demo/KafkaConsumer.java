package org.enset.demo;
import org.enset.demo.model.Bill;
import org.enset.demo.repository.BillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.util.HashSet;
import java.util.Random;

@Service
public class KafkaConsumer {
    @Autowired
    private BillRepository billRepository;
    private HashSet<Integer> ids=new HashSet<>();
    private FileWriter csvwriter;

    @KafkaListener(topics = {"Bills"},groupId = "factures")
    public void onMessage(Bill bill){
        int id=1;
        while (ids.contains(id)){
            id = new Random().nextInt(10000);
        }
        ids.add(id);
        bill.setId(new Long(id));
        System.out.println("received ==> "+bill.toString());
        billRepository.save(bill);
        // adding to csv file
        String csvFilename = "src\\main\\resources\\bills.csv";
        try {
            csvwriter = new FileWriter(csvFilename,true);
            csvwriter.append(String.valueOf(bill.getId()));
            csvwriter.append(",");
            csvwriter.append(bill.getCustomer());
            csvwriter.append(",");
            csvwriter.append(String.valueOf(bill.getMontant()));
            csvwriter.append("\n");
            csvwriter.close();
        } catch (Exception e) {
            System.out.println("exception :" + e.getMessage());
        }
    }
}
