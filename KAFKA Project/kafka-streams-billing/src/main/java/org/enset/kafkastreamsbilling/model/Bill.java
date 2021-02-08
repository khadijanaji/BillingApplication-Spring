package org.enset.kafkastreamsbilling.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Bill {
    private Long id;
    private double montant;
    private String customer;
}
