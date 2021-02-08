package org.enset.demo;

import org.enset.demo.entities.Billing;
import org.enset.demo.model.Bill;
import org.enset.demo.model.ProductItem;
import org.enset.demo.repository.BillRepository;
import org.enset.demo.repository.BillingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.util.HashSet;
import java.util.Random;

@Service
public class KafkaConsumer {
    @Autowired
    private BillingRepository billingRepository;

    @KafkaListener(topics = {"Bills-f"},groupId = "factures-1")
    public void onMessage(Bill bill){
        double s=0;
        Billing billing=new Billing();
        billing.setId(bill.getId());
        billing.setCustomer(bill.getCustomer().getName());
        for (ProductItem productItem: bill.getProductItems())
            s+=productItem.getPrice()*productItem.getQuantity();
        billing.setMontant(s);
        //billing.setDateStrBilling(String.valueOf(bill.getBillingdate()));
        System.out.println("received ==> "+billing.toString());
        billingRepository.save(billing);

        // adding to csv file
        String csvFilename = "src\\main\\resources\\bills.csv";
        try {
            FileWriter csvwriter = new FileWriter(csvFilename,true);
            csvwriter.append(String.valueOf(billing.getId()));
            csvwriter.append(",");
            csvwriter.append(billing.getCustomer());
            csvwriter.append(",");
            csvwriter.append(String.valueOf(billing.getMontant()));
            //csvwriter.append("\n");
            csvwriter.close();
        } catch (Exception e) {
            System.out.println("exception :" + e.getMessage());
        }

    }
}
