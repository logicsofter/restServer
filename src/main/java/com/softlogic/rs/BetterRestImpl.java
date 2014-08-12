package com.softlogic.rs;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import com.softlogic.commonRest.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class BetterRestImpl implements BetRest {

	public Integer healthCheck(String callerID) 
	{
		System.out.println( "Server Received: " + callerID + "\n");
		return 91;
	}

	public Boolean sayHello(String myName)
	{
		System.out.println( "Hello: " + myName + "\n");
		return true;
	}

	public AccountResp makePayment(AccountDetails myAccount)
	{
		System.out.println( "Account " + myAccount.toString() + "\n");
		return new AccountResp("Savings", "1000", false, new AccountTx("Savings", "123456", 3));
	}
	
	public String simpleProc(Simple sim)
	{
		return sim.getName() + " message better " + sim.getMessage();
	}

	public Response newPay(String myName)
	{
		System.out.println("Server Received: " + myName.toString());

		GsonBuilder gsonBuilder = new GsonBuilder();
		Gson gson = gsonBuilder.create();
		
		Simple sim = gson.fromJson(myName, Simple.class);

		if (sim != null)
		{
			System.out.println("Server Received: " + sim.getName());
		}
		
		ResponseBuilder respB = Response.ok();
		respB.entity("All is well");
		return respB.build();
	}
	public Response specialPay(Simple sim)
	{
		GsonBuilder gsonBuilder = new GsonBuilder();
		Gson gson = gsonBuilder.create();

		if (sim != null)
		{
			System.out.println("Server Received: " + sim.getName());
		}
		
		AccountResp resp = new AccountResp("Withdrawl", "1000", false, new AccountTx("Withdraw", "654322", 2));
		String retVal = gson.toJson(resp);		
		
		ResponseBuilder respB = Response.ok();
		respB.entity(retVal);
		return respB.build();
	}

}
