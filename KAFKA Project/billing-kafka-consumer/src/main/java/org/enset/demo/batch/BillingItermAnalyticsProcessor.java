package org.enset.demo.batch;

import lombok.Getter;
import org.enset.demo.entities.Billing;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

@Component
public class BillingItermAnalyticsProcessor implements ItemProcessor<Billing,Billing> {
        @Getter private static double totalMontant;
        @Override
        public Billing process (Billing billing) {
            this.totalMontant += billing.getMontant();
            return billing;
        }
}
