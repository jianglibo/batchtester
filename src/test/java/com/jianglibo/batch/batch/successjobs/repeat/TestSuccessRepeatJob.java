package com.jianglibo.batch.batch.successjobs.repeat;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
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
public class TestSuccessRepeatJob extends TbatchBase {
	
	public TestSuccessRepeatJob() {
		super(SuccessRepeatBatchConfiguration.JOB_NAME);
	}

	public static int fixtureItemNumber = 1000;
	
	@Before
	public void b() throws NoSuchJobException, IOException {
		setupFixtures(fixtureItemNumber);
		clearDb(null);
	}
	
	@Test
	public void t() throws NoSuchJobException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException, NoSuchJobInstanceException {
		bassertCountTable(null, 0);
		
		Job jb = jobRegistry.getJob(getJobName());
		JobExecution je = jobLauncher.run(jb, new JobParameters());
		
		bassertJobInstanceNumber(1);
		assertThat(je.getStatus(), equalTo(BatchStatus.COMPLETED));
		
		bassertCountTable(null, fixtureItemNumber);
		
		// run success job again.
		int currentJobExecNumber = countCurrentJobExecNumber();
		int currentStepExecNumber = countCurrentStepExecNumber();
		
		
		je = jobLauncher.run(jb, new JobParameters());
		
		// If launch job for same parameters, no new job instance should be created.
		bassertJobInstanceNumber(1);
		
		// Because of Step1's allowStartIfComplete is set to true. so exit code should be Completed.
		bassertExitCode(je, ExitStatus.COMPLETED);
		
		// Every execution of job should create a new execution instance.
		bassertJobExecNumber(currentJobExecNumber + 1);
		
		bassertStepExecutionNumber(currentStepExecNumber + 1);
		
		bassertCountTable(null, fixtureItemNumber + fixtureItemNumber);
		
	}

}
