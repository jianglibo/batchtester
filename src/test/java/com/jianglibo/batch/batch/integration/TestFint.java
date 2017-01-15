package com.jianglibo.batch.batch.integration;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.nio.file.Path;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.integration.dsl.IntegrationFlow;

import com.jianglibo.batch.batch.TbatchBase;
import com.jianglibo.batch.batch.integration.Fint.CountingChannelInterceptor;
import com.jianglibo.batch.batch.util.FixtureUtil;

public class TestFint extends TbatchBase {

	public TestFint() {
		super(null);
	}
	
	@Before
	public void b() throws IOException {
		FixtureUtil.clearFolder("fint");
	}
	
	@After
	public void a() throws IOException {
		FixtureUtil.clearFolder("fint");
	}
	
	
	@Test
	public void t() throws InterruptedException, IOException {
		IntegrationFlow iflow = applicationContext.getBean("fileReadingFlow", IntegrationFlow.class);
		Path datap = FixtureUtil.createFixture("fint", ".txt", 2);
		FixtureUtil.createFixture("fint", ".txt", 2);
		Thread.sleep(2000);
		CountingChannelInterceptor cci = applicationContext.getBean("cci", CountingChannelInterceptor.class);
		assertThat("should processed 2 messages", cci.getSendCount().get(), equalTo(2));
	}

}
