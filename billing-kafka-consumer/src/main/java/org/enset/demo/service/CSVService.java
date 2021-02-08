package org.enset.demo.service;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.enset.demo.helper.CSVHelper;
import org.enset.demo.model.Bill;
import org.enset.demo.repository.BillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CSVService {

    @Autowired
    BillRepository repository;

    public ByteArrayInputStream load() {
        List<Bill> tutorials = repository.findAll();
        ByteArrayInputStream in = CSVHelper.tutorialsToCSV(tutorials);
        return in;
    }
}
