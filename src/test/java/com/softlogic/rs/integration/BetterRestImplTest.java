package com.softlogic.rs.integration;

import static org.junit.Assert.*;

import javax.ws.rs.PUT;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.softlogic.commonRest.AccountDetails;
import com.softlogic.commonRest.AccountResp;
import com.softlogic.commonRest.Simple;
import com.softlogic.rs.JSONParseExceptionMapper;

public class BetterRestImplTest
{
	private RestTemplate restTemplate;
	private static String REST_URL = "http://localhost:8080/services";

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	
	}
	
	@Before
	public void setUp() throws Exception {
		restTemplate = new RestTemplate();
	}
	
	@Test
	public void testHealthCheck() {
		Integer healthCheckID = restTemplate.getForObject(REST_URL + "/healthCheck/1", Integer.class);
		assertEquals(new Integer(91), healthCheckID);
	}
	
	@Test
	public void testSayHello()
	{
		Boolean success = restTemplate.postForObject(REST_URL + "/sayHello", "name", Boolean.class);
		assertEquals(true, success);
	}
	
	@Test
	public void testSimpleProc()
	{
		Simple simple = new Simple();
		simple.setName("Hayden");
		simple.setMessage("Junit Test");
		
		String returnMessage = restTemplate.postForObject(REST_URL + "/simpleProc", simple, String.class);
		String expectedMessage = simple.getName() + " message better " + simple.getMessage();
		assertEquals(expectedMessage, returnMessage);
	}
	
	@Test
	public void testSimpleProcWithStringJSONRequest() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		String json = "{\"name\":\"Peter\",\"message\":\"Junit Test\"}";
		HttpEntity<String> requestEntity = new HttpEntity<String>(json, headers);
		
		ResponseEntity<String> responseEntity = 
				restTemplate.exchange(REST_URL + "/simpleProc", 
									  HttpMethod.POST, 
									  requestEntity, 
									  String.class);
		
		String expectedMessage = "Peter message better Junit Test";
		assertEquals(expectedMessage, responseEntity.getBody());
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
	}
	
	@Test
	public void testMakePayment() {
		AccountDetails accountDetails = new AccountDetails();
		accountDetails.setAccountNumber(1);
		accountDetails.setChannel("23");
		accountDetails.setJurisdiction("VIC");
		accountDetails.setSaltedPasswordHash("password");
		accountDetails.setTier("Tier 2");
			
		AccountResp accountResp = restTemplate.postForObject(REST_URL + "/makePayment", accountDetails, AccountResp.class);
		
		assertEquals("Savings", accountResp.getType());
		assertEquals(false, accountResp.isFlexi());
		assertEquals("1000", accountResp.getAmount());
	}

	@Test
	public void testErrorInvalidJSONAttribute() {
		AccountDetails accountDetails = new AccountDetails();
		accountDetails.setAccountNumber(1);
		accountDetails.setChannel("23");
		accountDetails.setJurisdiction("VIC");
		accountDetails.setSaltedPasswordHash("password");
		accountDetails.setTier("Tier 2");
		
		try {
			restTemplate.postForObject(REST_URL + "/simpleProc", accountDetails, String.class);
			
			assertFalse("Should never reach this point, exception should be thrown", true);
			
		} catch (HttpClientErrorException e) {
			assertEquals(JSONParseExceptionMapper.ERROR_INVALIDE_ATTR_NAME, e.getResponseBodyAsString());
		}
	}
	
	@Test
	public void testErrorInvalidJsonReqest() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		String json = "{\"name\":\"Peter\",\"message\":\"testing\"ZZZ}";
		HttpEntity<String> requestEntity = new HttpEntity<String>(json, headers);
		
		try {
			restTemplate.exchange(REST_URL + "/simpleProc", 
								  HttpMethod.POST, 
								  requestEntity, 
								  String.class);
			
			assertFalse("Should never reach this point, exception should be thrown", true);
			
		} catch (HttpClientErrorException e) {
			assertEquals(JSONParseExceptionMapper.ERROR_INVALID_JSON_REQUEST, e.getResponseBodyAsString());
		}
	}
	
	@After
	public void tearDown() throws Exception {
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}
	
}
