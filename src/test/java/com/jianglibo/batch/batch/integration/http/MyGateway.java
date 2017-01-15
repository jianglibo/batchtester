package com.jianglibo.batch.batch.integration.http;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.GatewayHeader;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway(name = "myGateway", defaultRequestChannel = "inputC",
defaultHeaders = @GatewayHeader(name = "calledMethod",
                         expression="#gatewayMethod.name"))
public interface MyGateway {

	   @Gateway(requestChannel = "inputA", replyTimeout = 2, requestTimeout = 200)
	   String echo(String payload);
}
