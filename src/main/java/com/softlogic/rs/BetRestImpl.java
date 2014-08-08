package com.softlogic.rs;

import javax.ws.rs.core.Response;

import com.softlogic.commonRest.*;

public class BetRestImpl implements BetRest
{

	@Override
	public Integer healthCheck(String callerID)
	{
		// TODO Auto-generated method stub
		return 42;
	}

	@Override
	public Boolean sayHello(String myName)
	{
		System.out.println( "Spring Hello: " + myName + "\n");
		return false;
	}

	@Override
	public AccountResp makePayment(AccountDetails myAccount)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String simpleProc(Simple sim)
	{
		return sim.getName() + " message " + sim.getMessage();
	}

	@Override
	public Response newPay(String myName)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
