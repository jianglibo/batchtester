package com.jianglibo.batch.batch.failedjobs.hasbaditem;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.UnexpectedJobExecutionException;
import org.springframework.batch.core.launch.JobParametersNotFoundException;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.launch.NoSuchJobInstanceException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.test.context.jdbc.Sql;

import com.jianglibo.batch.batch.TbatchBase;

@Sql({"classpath:schema1-all.sql"})
public class TestBadItemInInputJobMultipleInstance extends TbatchBase {
	
	public TestBadItemInInputJobMultipleInstance() {
		super(BadItemInInputBatchConfiguration.JOB_NAME);
	}

	public static int fixtureItemNumber = 100;
	
	private int badLinePosition = 55;
	
	@Before
	public void b() throws NoSuchJobException, IOException {
		setupFixtures(getJobName(), fixtureItemNumber);
		List<String> allLines = Files.readAllLines(getFixturePath(getJobName()));
		// replace a bad line.
		allLines.set(badLinePosition, "1111111111111111");
		Files.write(getFixturePath(getJobName()), allLines);
		clearDb(null);
	}
	
	@Test
	public void t() throws NoSuchJobException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException, NoSuchJobInstanceException, InterruptedException, JobParametersNotFoundException, UnexpectedJobExecutionException {
		
		bassertCountTable(null, 0);
		
		int jobInstanceNumber = countCurrentJobInstanceNumber(getJobName());

		Long jid = jobOperator.startNextInstance(getJobName());
		JobExecution je1 = jobExplorer.getJobExecution(jid);
		
		bassertJobInstanceNumber(getJobName(), jobInstanceNumber + 1);
		// We know job will be failure.
		assertThat(je1.getStatus(), equalTo(BatchStatus.FAILED));
		
		assertTrue("job execution should has only one stepexecution.", je1.getStepExecutions().size() == 1);
		assertThat("the name of the only step execution should be step1", je1.getStepExecutions().iterator().next().getStepName(), equalTo("step1"));
		
		// How many records are inserted by now?
		// Get step execution.
		assertTrue("should insert items", getWriteCount(je1) > 0);
		bassertCountTable(null, getWriteCount(je1));
		
		assertTrue(getWriteCount(je1) % 10 == 0);
		
		int steps = countCurrentStepExecNumber(getJobName());
		
		jid = jobOperator.startNextInstance(getJobName());
		JobExecution je2 = jobExplorer.getJobExecution(jid);
		bassertStepExecutionNumber(getJobName(), steps + 1);
		
		assertThat(je2.getStatus(), equalTo(BatchStatus.FAILED));
		
		// This means same named step in different job instance are separated.
		assertTrue(getWriteCount(je2) > 0);
		
		
		LOGGER.info("there must {} item in db table", countCurrentItemNumberInDb());
	}

}
