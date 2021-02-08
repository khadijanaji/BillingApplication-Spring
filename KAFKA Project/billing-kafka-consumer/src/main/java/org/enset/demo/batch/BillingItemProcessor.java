package org.enset.demo.batch;

import org.enset.demo.entities.Billing;
import org.springframework.batch.item.ItemProcessor;

import java.text.SimpleDateFormat;

//@Component
public class BillingItemProcessor implements ItemProcessor<Billing,Billing>{
    @Override
    public Billing process(Billing billing) throws Exception {
        return billing;
    }
}
