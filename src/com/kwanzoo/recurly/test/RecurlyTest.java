package com.kwanzoo.recurly.test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;

import junit.framework.TestCase;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.Test;

import com.kwanzoo.recurly.Account;
import com.kwanzoo.recurly.Base;
import com.kwanzoo.recurly.BillingInfo;
import com.kwanzoo.recurly.CreditCard;
import com.kwanzoo.recurly.Subscription;

public class RecurlyTest extends TestCase{
	private String getRandStr(final int n){
		return RandomStringUtils.randomAlphanumeric(n);
	}

	/*private String getRandNum(final int n){
		return RandomStringUtils.randomNumeric(n);
	}*/

	@Override
	public void setUp(){
		Properties p = new Properties();
		try{
			p.load(new FileInputStream(System.getProperty("user.home") + "/" + "recurly_auth"));
			String username = p.getProperty("recurly_username");
			String password = p.getProperty("recurly_password");
			Base.setAuth(username, password);
		}
		catch(IOException e){
			e.printStackTrace();
		}
    }

	//Manipulate Accounts
	@Test public void test1() throws Exception{

		//create fresh account
		final String accountCode = getRandStr(5);
		String username = getRandStr(5);
		String firstName = getRandStr(5);
		String lastName	= getRandStr(5);
		String email = getRandStr(5)+"@site.com";
		String companyName	= getRandStr(5);

		Account account = new Account();

		account.accountCode = accountCode;
		account.username = username;
		account.firstName = firstName;
		account.lastName = lastName;
		account.email = email;
		account.companyName	= companyName;

		account.create();

		//check if find works
		account = Account.get(accountCode);

		assertEquals(accountCode, account.accountCode);
		assertEquals(username, account.username);
		assertEquals(firstName, account.firstName);
		assertEquals(lastName, account.lastName);
		assertEquals(email, account.email);
		assertEquals(companyName, account.companyName);

		//check if update works
		username = getRandStr(5);
		firstName = getRandStr(5);
		lastName = getRandStr(5);
		email = getRandStr(5)+"@site.com";
		companyName	= getRandStr(5);

		account.username = username;
		account.firstName = firstName;
		account.lastName = lastName;
		account.email = email;
		account.companyName	= companyName;

		account.update();

		account = Account.get(accountCode);

		assertEquals(accountCode, account.accountCode);
		assertEquals(username, account.username);
		assertEquals(firstName, account.firstName);
		assertEquals(lastName, account.lastName);
		assertEquals(email, account.email);
		assertEquals(companyName, account.companyName);
	}

	//Manipulate billing info
	@Test public void test2() throws Exception{

		//create fresh account
		final String accountCode = getRandStr(5);
		final String username = getRandStr(5);
		String firstName = getRandStr(5);
		String lastName	= getRandStr(5);
		final String email = getRandStr(5)+"@site.com";
		final String companyName = getRandStr(5);

		Account account = new Account();

		account.accountCode = accountCode;
		account.username = username;
		account.firstName = firstName;
		account.lastName = lastName;
		account.email = email;
		account.companyName	= companyName;

		account.create();

		account = Account.get(accountCode);

		//check if adding billing info for a fresh account works
		final String number	= "1"; //bogus credit card number accepted by test gateway
		String verificationValue = getRandStr(4);
		Integer expirationMonth = (new Random()).nextInt(12) + 1;
		Integer expirationYear = 2012 + (new Random()).nextInt(20);

		firstName = account.firstName;
		lastName = account.lastName;
		String address1 = getRandStr(10);
		String address2 = getRandStr(10);
		String city = "San Fransisco";
		String state = "CA";
		String zip = "20240";
		String country = "US";
		String ipAddress = "127.0.0.1";

		CreditCard creditCard = new CreditCard();
		
		creditCard.number = number;
		creditCard.verificationValue = verificationValue;
		creditCard.expirationMonth = expirationMonth;
		creditCard.expirationYear = expirationYear;

		BillingInfo billingInfo = new BillingInfo(accountCode);
		billingInfo.firstName = firstName;
		billingInfo.lastName = lastName;
		billingInfo.address1 = address1;
		billingInfo.address2 = address2;
		billingInfo.city = city;
		billingInfo.state = state;
		billingInfo.zip = zip;
		billingInfo.country	= country;
		billingInfo.ipAddress = ipAddress;
		billingInfo.creditCard = creditCard;

		billingInfo.update();

		billingInfo = BillingInfo.get(accountCode);

		assertEquals(firstName, billingInfo.firstName);
		assertEquals(lastName, billingInfo.lastName);
		assertEquals(address1, billingInfo.address1);
		assertEquals(address2, billingInfo.address2);
		assertEquals(city, billingInfo.city );
		assertEquals(state, billingInfo.state);
		assertEquals(zip, billingInfo.zip);
		assertEquals(country, billingInfo.country);
		assertEquals(ipAddress, billingInfo.ipAddress);

		//Recurly returns empty for both the fields below
		//assertEquals(billingInfo.creditCard.number);
		//assertEquals(billingInfo.creditCard.verificationValue);

		assertEquals(expirationMonth, billingInfo.creditCard.expirationMonth);
		assertEquals(expirationYear, billingInfo.creditCard.expirationYear);

		//check if updating billing info of an account that already has billing info works
		verificationValue = getRandStr(4);
		expirationMonth = (new Random()).nextInt(12) + 1;
		expirationYear = 2012 + (new Random()).nextInt(20);

		firstName = account.firstName;
		lastName = account.lastName;
		address1 = getRandStr(10);
		address2 = getRandStr(10);
		city = "Some City";
		state = "NJ";
		zip = "94105";
		country	= "IN";
		ipAddress = "127.0.0.1";

		creditCard = new CreditCard();
		creditCard.number = number;
		creditCard.verificationValue = verificationValue;
		creditCard.expirationMonth = expirationMonth;
		creditCard.expirationYear = expirationYear;

		billingInfo = new BillingInfo(accountCode);
		billingInfo.firstName = firstName;
		billingInfo.lastName = lastName;
		billingInfo.address1 = address1;
		billingInfo.address2 = address2;
		billingInfo.city = city;
		billingInfo.state = state;
		billingInfo.zip = zip;
		billingInfo.country = country;
		billingInfo.ipAddress = ipAddress;
		billingInfo.creditCard = creditCard;

		billingInfo.checkedUpdate();

		billingInfo = BillingInfo.get(accountCode);

		assertEquals(firstName, billingInfo.firstName);
		assertEquals(lastName, billingInfo.lastName);
		assertEquals(address1, billingInfo.address1);
		assertEquals(address2, billingInfo.address2);
		assertEquals(city, billingInfo.city );
		assertEquals(state, billingInfo.state);
		assertEquals(zip, billingInfo.zip);
		assertEquals(country, billingInfo.country);
		assertEquals(ipAddress, billingInfo.ipAddress);

	}

	//Manipulate subscriptions
	@Test public void test3() throws Exception{
		//create fresh account
		final String accountCode = getRandStr(5);
		final String username = getRandStr(5);
		String firstName = getRandStr(5);
		String lastName	= getRandStr(5);
		final String email = getRandStr(5)+"@site.com";
		final String companyName = getRandStr(5);

		Account account = new Account();

		account.accountCode = accountCode;
		account.username = username;
		account.firstName = firstName;
		account.lastName = lastName;
		account.email = email;
		account.companyName	= companyName;

		account.create();

		account = Account.get(accountCode);

		//subscribe to plan1
		final String number	= "1";
		final String verificationValue = getRandStr(4);
		final Integer expirationMonth = (new Random()).nextInt(12);
		final Integer expirationYear = 2011 + (new Random()).nextInt(20);

		firstName = account.firstName;
		lastName = account.lastName;
		
		final String address1 = getRandStr(10);
		final String address2 = getRandStr(10);
		final String city = "San Fransisco";
		final String state = "NM";
		final String zip = "99546";
		final String country = "US";
		final String ipAddress = "127.0.0.1";

		final CreditCard creditCard = new CreditCard();
		creditCard.number = number;
		creditCard.verificationValue = verificationValue;
		creditCard.expirationMonth = expirationMonth;
		creditCard.expirationYear = expirationYear;

		BillingInfo billingInfo = new BillingInfo(accountCode);
		billingInfo.firstName = firstName;
		billingInfo.lastName = lastName;
		billingInfo.address1 = address1;
		billingInfo.address2 = address2;
		billingInfo.city = city;
		billingInfo.state = state;
		billingInfo.zip = zip;
		billingInfo.country	= country;
		billingInfo.ipAddress = ipAddress;
		billingInfo.creditCard = creditCard;

		account.billingInfo = billingInfo;

		String planCode = "testplan1"; //one of the plans defined in your recurly account
		Integer quantity = 1;

		Subscription subscription = new Subscription(accountCode);
		subscription.account = account;
		subscription.planCode = planCode;
		subscription.quantity = quantity;

		subscription.create();

		//TODO: get subscription and check with asserts
		subscription = Subscription.get(accountCode);

		assertEquals(accountCode, subscription.accountCode);
		assertEquals(planCode, subscription.plan.planCode);
		assertEquals("active", subscription.state);
		assertEquals(quantity, subscription.quantity);

		billingInfo = BillingInfo.get(accountCode);
		assertEquals(firstName, billingInfo.firstName);
		assertEquals(lastName, billingInfo.lastName);
		assertEquals(address1, billingInfo.address1);
		assertEquals(address2, billingInfo.address2);
		assertEquals(city, billingInfo.city );
		assertEquals(state, billingInfo.state);
		assertEquals(zip, billingInfo.zip);
		assertEquals(country, billingInfo.country);
		assertEquals(ipAddress, billingInfo.ipAddress);

		//Recurly returns a null for both the fields below.
		//assertEquals(billingInfo.creditCard.number);
		//assertEquals(billingInfo.creditCard.verificationValue);

		assertEquals(expirationMonth, billingInfo.creditCard.expirationMonth);
		assertEquals(expirationYear, billingInfo.creditCard.expirationYear);

		//downgrade check

		planCode = "testplan0"; // a plan with lesser features/rate than testplan1
		quantity = 2;

		subscription = new Subscription(accountCode);
		subscription.timeframe = "now";
		subscription.planCode = planCode;
		subscription.quantity = quantity;

		subscription.update();

		subscription = Subscription.get(accountCode);

		assertEquals(accountCode, subscription.accountCode);
		assertEquals(planCode, subscription.plan.planCode);
		assertEquals("active", subscription.state);
		assertEquals(quantity, subscription.quantity);

		//upgrade check

		planCode = "testplan2"; // a plan with better features/rate than testplan1
		quantity = 3;

		subscription = new Subscription(accountCode);
		subscription.timeframe = "now"; //immediate upgrade
		subscription.planCode = planCode;
		subscription.quantity = quantity;

		subscription.update();

		subscription = Subscription.get(accountCode);

		assertEquals(accountCode, subscription.accountCode);
		assertEquals(planCode, subscription.plan.planCode);
		assertEquals("active", subscription.state);
		assertEquals(quantity, subscription.quantity);

		//cancel & check with asserts

		subscription = new Subscription(accountCode);
		subscription.delete();

		subscription = Subscription.get(accountCode);
		assertEquals("canceled", subscription.state);
	}
}
