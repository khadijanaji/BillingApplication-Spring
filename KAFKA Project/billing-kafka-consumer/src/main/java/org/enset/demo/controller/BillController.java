package org.enset.demo.controller;

import org.enset.demo.batch.BillingItermAnalyticsProcessor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@RestController
public class BillController {

    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    private Job job;
    @Autowired
    private BillingItermAnalyticsProcessor billingItermAnalyticsProcessor;

    public BillController(JobLauncher jobLauncher, Job job) { this.jobLauncher = jobLauncher; this.job = job;}

    @GetMapping("/loadData")
    public BatchStatus load() throws Exception {
        Map<String, JobParameter> params=new HashMap<>();
        params.put("time",new JobParameter(System.currentTimeMillis()));
        JobParameters jobParameters=new JobParameters(params);
        JobExecution jobExecution=jobLauncher.run(job,jobParameters);
        while (jobExecution.isRunning()){
            System.out.println("......");
        }
        return jobExecution.getStatus();
    }
    @GetMapping("/analytics")
    public Map<String,Double> analytics(){
        Map<String,Double> map=new HashMap<>();
        System.out.println("Amount : "+billingItermAnalyticsProcessor.getTotalMontant());
        map.put("totalMontant",billingItermAnalyticsProcessor.getTotalMontant());
        return map;
    }
}
