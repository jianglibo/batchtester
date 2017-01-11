package com.jianglibo.batch.batch;

import java.nio.file.Paths;

import javax.sql.DataSource;

import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;
import org.springframework.transaction.PlatformTransactionManager;

public class BatchCfgBase implements ApplicationContextAware {
	
    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;
    
    @Autowired
    public JobRegistry jobRegistry;

    @Autowired
    public DataSource dataSource;
	
    @Autowired
    public PlatformTransactionManager transactionManager;
    
	private String jobName;
	
	protected ApplicationContext applicationContext;
	
	
	public BatchCfgBase(String jobName) {
		this.setJobName(jobName);
	}
	
	public Resource getFixtureResoruce() {
		return applicationContext.getResource("file:///" + Paths.get(TbatchBase.BATCH_FIXTURE_BASE, getJobName() + ".csv").toAbsolutePath().toString());
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
	
	

}
