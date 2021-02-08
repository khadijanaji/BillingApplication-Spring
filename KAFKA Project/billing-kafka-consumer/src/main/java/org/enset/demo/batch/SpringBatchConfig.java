package org.enset.demo.batch;

import org.enset.demo.entities.Billing;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableBatchProcessing
public class SpringBatchConfig {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private ItemReader<Billing> billingItemReader;
    @Autowired
    private ItemWriter<Billing> billingItemWriter;
    /*@Autowired
    private ItemProcessor<BankTransaction,BankTransaction> bankTransactionItemProcessor;
    */

    @Bean
    public Job bankJob(){
        Step step=stepBuilderFactory.get("step-load-data")
                .<Billing,Billing>chunk(100)
                .reader(billingItemReader)
                .writer(billingItemWriter)
                .processor(compositeItemProcessor())
                .build();
        return jobBuilderFactory.get("billing-data-loader-job")
                .incrementer(new RunIdIncrementer())
                .start(step)
                .build();
    }

    @Bean
    public CompositeItemProcessor<Billing, Billing> compositeItemProcessor(){
        List<ItemProcessor<Billing, Billing>> itemProcessors=new ArrayList<>();
        itemProcessors.add(itemProcessor1());
        itemProcessors.add(itemProcessor2());
        CompositeItemProcessor<Billing, Billing> compositeItemProcessor=
                new CompositeItemProcessor<>();
        compositeItemProcessor.setDelegates(itemProcessors);
        return compositeItemProcessor;
    }

    @Bean
    public BillingItemProcessor itemProcessor1(){
        return new BillingItemProcessor();
    }

    @Bean
    public BillingItermAnalyticsProcessor itemProcessor2(){
        return new BillingItermAnalyticsProcessor();
    }

    @Bean
    public FlatFileItemReader<Billing> flatFileItemReader(@Value("${inputFile}") Resource resource){
        FlatFileItemReader<Billing> flatFileItemReader=new FlatFileItemReader<>();
        flatFileItemReader.setName("CSV-READER");
        flatFileItemReader.setLinesToSkip(1);
        flatFileItemReader.setResource(resource);
        flatFileItemReader.setLineMapper(lineMapper());
        return flatFileItemReader;
    }

    @Bean
    public LineMapper<Billing> lineMapper(){
        DefaultLineMapper<Billing> lineMapper=new DefaultLineMapper<>();
        DelimitedLineTokenizer lineTokenizer=new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("id","customer","dateStrBilling","montant");
        lineMapper.setLineTokenizer(lineTokenizer);
        BeanWrapperFieldSetMapper<Billing> fieldSetMapper=new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Billing.class);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;
    }

}
