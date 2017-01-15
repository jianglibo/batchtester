package com.jianglibo.batch.batch.integration.http;

import org.junit.Test;

import com.jianglibo.batch.batch.TbatchBase;

public class TestHttpInBound extends TbatchBase {

	public TestHttpInBound() {
		super("httpInbound");
	}

	
	@Test
	public void t() {
		Object o = applicationContext.getBean("receiveChannel");
		System.out.print(o);
	}
	
}
