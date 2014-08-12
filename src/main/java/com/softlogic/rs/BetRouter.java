package com.softlogic.rs;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

import com.softlogic.commonRest.AccountDetails;
import com.softlogic.commonRest.Simple;

public class BetRouter extends RouteBuilder
{
	@Override
	public void configure() throws Exception
	{	
		onException(Exception.class).
		handled(true).					
		process(new Processor() {
	          public void process(Exchange exchange) throws Exception {
	                Exception cause = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
	               
	                StackTraceElement[] trace = cause.getStackTrace();
	                cause.printStackTrace(System.out);
	                System.out.println("Server exception " + cause.getMessage());
	                // we now have the caused exception
	          }
	        }).
		end();

		from("cxfrs:bean:betServer?bindingStyle=SimpleConsumer")
		.recipientList(simple("direct:${header.operationName}"));
		
		from("direct:healthCheck").routeId("HealthCheckRoute")
		.removeHeader(Exchange.CONTENT_LENGTH)
		.log("Request: type=${header.type}, active=${header.active}, customerData=${body}")
		.beanRef("camelBetHandler","healthCheck");

		from("direct:sayHello").routeId("SayHello")
		.removeHeader(Exchange.CONTENT_LENGTH)
		.log("Request: type=${header.type}, active=${header.active}, customerData=${body}")
		.beanRef("camelBetHandler","sayHello");
		
		from("direct:newPay").routeId("NewPay")
		.removeHeader(Exchange.CONTENT_LENGTH)
		.log("Request: type=${header.type}, active=${header.active}, customerData=${body}")
		.unmarshal().json(JsonLibrary.Gson, Simple.class)
		.beanRef("camelBetHandler","specialPay");
															
		from("direct:makePayment").routeId("MakePayment")
		.removeHeader(Exchange.CONTENT_LENGTH)
		.log("Request: type=${header.type}, active=${header.active}, customerData=${body}")
		.beanRef("camelBetHandler","makePayment");
			
		from("direct:simpleProc").routeId("simple")
		.removeHeader(Exchange.CONTENT_LENGTH)
		.log("Request: type=${header.type}, active=${header.active}, customerData=${body}")
		.beanRef("camelBetHandler","simpleProc");
	}

}
