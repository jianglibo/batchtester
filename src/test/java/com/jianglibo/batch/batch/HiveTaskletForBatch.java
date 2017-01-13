package com.jianglibo.batch.batch;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.hadoop.batch.hive.HiveTasklet;

@Configuration
public class HiveTaskletForBatch {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    public DataSource dataSource;
    
    
    // end::readerwriterprocessor[]

    // tag::jobstep[]
    @Bean
    public Job runHiveJob(JobCompletionNotificationListener listener) {
        return jobBuilderFactory.get("runHiveJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(stephive1())
                .end()
                .build();
    }

    @Bean
    public Step stephive1() {
    	HiveTasklet ht = new HiveTasklet();
        return stepBuilderFactory.get("hiveScript").tasklet(ht).build();
    }
    // end::jobstep[]
}
