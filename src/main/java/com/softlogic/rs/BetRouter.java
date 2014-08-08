package com.softlogic.rs;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

import com.softlogic.commonRest.AccountDetails;
import com.softlogic.commonRest.Simple;

public class BetRouter extends RouteBuilder
{
	//private String uri = "cxf:/betFace?serviceClass=" + BetRest.class.getName();
	
	@Override
	public void configure() throws Exception
	{
	
		from("cxfrs:bean:betServer?bindingStyle=SimpleConsumer")
		.recipientList(simple("direct:${header.operationName}"));
		
		from("direct:healthCheck").routeId("HealthCheckRoute")
		.log("Request: type=${header.type}, active=${header.active}, customerData=${body}")
		.beanRef("camelBetHandler","healthCheck");

		from("direct:sayHello").routeId("SayHello")
		.log("Request: type=${header.type}, active=${header.active}, customerData=${body}")
		.beanRef("camelBetHandler","sayHello");
		
		from("direct:newPay").routeId("NewPay")
		.log("Request: type=${header.type}, active=${header.active}, customerData=${body}")
		.unmarshal().json(JsonLibrary.Gson, Simple.class)
		.beanRef("camelBetHandler","specialPay");
													
		
		
		
		
		from("direct:makePayment").routeId("MakePayment")
		.log("Request: type=${header.type}, active=${header.active}, customerData=${body}")
		.beanRef("camelBetHandler","makePayment");
		
		from("direct:simpleProc").routeId("simple")
		.log("Request: type=${header.type}, active=${header.active}, customerData=${body}")
		.beanRef("camelBetHandler","simpleProc");
	}

}
