package org.enset.demo.batch;

import org.enset.demo.entities.Billing;
import org.enset.demo.repository.BillingRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BillingItemWriter implements ItemWriter<Billing> {
    @Autowired
    private BillingRepository billingRepository;
    @Override
    public void write(List<? extends Billing> bill){
        billingRepository.saveAll(bill);
    }
}
