package com.jianglibo.batch.batch.failedjobs.inputmissing;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.launch.NoSuchJobInstanceException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.test.context.jdbc.Sql;

import com.jianglibo.batch.batch.TbatchBase;

@Sql({"classpath:schema1-all.sql"})
public class TestMissingInputJob extends TbatchBase {
	
	public TestMissingInputJob() {
		super(MissingInputBatchConfiguration.JOB_NAME);
	}

	@Before
	public void b() throws NoSuchJobException, IOException {
		setupFixtures(getJobName(), 1000);
		clearDb(null);
	}
	
	@Test
	public void t() throws NoSuchJobException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException, NoSuchJobInstanceException {
		bassertCountTable(null, 0);
		Job jb = jobRegistry.getJob(getJobName());
		JobExecution je = jobLauncher.run(jb, new JobParameters());
		
		bassertJobInstanceNumber(getJobName(), 1);
		assertThat(je.getStatus(), equalTo(BatchStatus.FAILED));
		
		// No record should be inserted.
		bassertCountTable(null, 0);
		
		// run failed job again.
		int currentJobExecNumber = countCurrentJobExecNumber(getJobName());
		int currentStepExecNumber = countCurrentStepExecNumber(getJobName());
		jobLauncher.run(jb, new JobParameters());
		
		// If launch job for same parameters, no new job instance should be created.
		bassertJobInstanceNumber(getJobName(), 1);
		
		// Every execution of job should create a new execution instance.
		bassertJobExecNumber(getJobName(), currentJobExecNumber + 1);
		
		bassertStepExecutionNumber(getJobName(), currentStepExecNumber + 1);
		
	}

}
