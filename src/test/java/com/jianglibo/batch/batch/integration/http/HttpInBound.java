package com.jianglibo.batch.batch.integration.http;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.integration.annotation.ServiceActivator;

@Configuration
@ImportResource({"classpath:/com/jianglibo/batch/batch/integration/http/http.xml"})
public class HttpInBound {

	
	@ServiceActivator
	@Bean("httpSva")
	public String sva(String pl) {
		return pl + "abc";
	}
}
