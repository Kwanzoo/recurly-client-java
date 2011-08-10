package com.kwanzoo.recurly.test;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

import junit.framework.TestCase;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.Test;

import com.kwanzoo.recurly.Account;
import com.kwanzoo.recurly.Base;
import com.kwanzoo.recurly.BillingInfo;
import com.kwanzoo.recurly.Charge;
import com.kwanzoo.recurly.CreditCard;
import com.kwanzoo.recurly.InvoiceDetailed;
import com.kwanzoo.recurly.Invoices;
import com.kwanzoo.recurly.Subscription;
import com.kwanzoo.recurly.TransparentResult;

public class RecurlyTest extends TestCase {

	private String[] plans = {};
	private String existingTransparentPostResult = "";
	private int currentYear = 0;

	private String getRandomAlphaNumString(final int n) {
		return RandomStringUtils.randomAlphanumeric(n);
	}

	private String getRandomNumString(final int n) {
		return RandomStringUtils.randomNumeric(n);
	}

	@Override
	public void setUp() {
		Properties props = new Properties();
		try {
			String propsFilePath = System.getProperty("user.home") + "/" + "recurly_auth";
			System.out.println("Reading properties from " + propsFilePath);
			props.load(new FileInputStream(propsFilePath));
			
			String username = props.getProperty("recurly_username");
			String password = props.getProperty("recurly_password");
			System.out.println("Using recurly credentials : " + username + "/" + password);
			
			String plansStr = props.getProperty("plans");
			plans = plansStr.split(",");
			for(int i=0;i<plans.length;i++){
				plans[i] = plans[i].trim();
			}
			System.out.println("Using plans : " + Arrays.toString(plans));
			
			currentYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
			
			Base.setAuth(username, password);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void test0() throws Exception {
		if (!"".equals(existingTransparentPostResult)) {
			Subscription result = TransparentResult.get(existingTransparentPostResult);
			assertEquals("pending", result.state);
		} else {
			fail("create a transparent post demo and setup the transparent post result");
		}
	}

	// Manipulate Accounts
	@Test
	public void test1() throws Exception {

		// create fresh account
		final String accountCode = getRandomAlphaNumString(5);
		String username = getRandomAlphaNumString(5);
		String firstName = getRandomAlphaNumString(5);
		String lastName = getRandomAlphaNumString(5);
		String email = getRandomAlphaNumString(5) + "@site.com";
		String companyName = getRandomAlphaNumString(5);

		Account account = new Account();

		account.accountCode = accountCode;
		account.username = username;
		account.firstName = firstName;
		account.lastName = lastName;
		account.email = email;
		account.companyName = companyName;

		account.create();

		// check if find works
		account = Account.get(accountCode);

		assertEquals(accountCode, account.accountCode);
		assertEquals(username, account.username);
		assertEquals(firstName, account.firstName);
		assertEquals(lastName, account.lastName);
		assertEquals(email, account.email);
		assertEquals(companyName, account.companyName);

		// check if update works
		username = getRandomAlphaNumString(5);
		firstName = getRandomAlphaNumString(5);
		lastName = getRandomAlphaNumString(5);
		email = getRandomAlphaNumString(5) + "@site.com";
		companyName = getRandomAlphaNumString(5);

		account.username = username;
		account.firstName = firstName;
		account.lastName = lastName;
		account.email = email;
		account.companyName = companyName;

		account.update();

		account = Account.get(accountCode);

		assertEquals(accountCode, account.accountCode);
		assertEquals(username, account.username);
		assertEquals(firstName, account.firstName);
		assertEquals(lastName, account.lastName);
		assertEquals(email, account.email);
		assertEquals(companyName, account.companyName);

		Charge charge = new Charge(accountCode);
		charge.amount_in_cents = 500;
		charge.description = "first charge";
		charge.create();

		// cleanup
		account.delete();
	}

	// Manipulate billing info
	@Test
	public void test2() throws Exception {

		// create fresh account
		final String accountCode = getRandomAlphaNumString(5);
		final String username = getRandomAlphaNumString(5);
		String firstName = getRandomAlphaNumString(5);
		String lastName = getRandomAlphaNumString(5);
		final String email = getRandomAlphaNumString(5) + "@site.com";
		final String companyName = getRandomAlphaNumString(5);

		Account account = new Account();

		account.accountCode = accountCode;
		account.username = username;
		account.firstName = firstName;
		account.lastName = lastName;
		account.email = email;
		account.companyName = companyName;

		account.create();

		account = Account.get(accountCode);

		// check if adding billing info for a fresh account works
		final String number = "1"; // bogus credit card number accepted by test gateway
		String verificationValue = getRandomNumString(4);
		Integer expirationMonth = (new Random()).nextInt(12) + 1;
		Integer expirationYear = currentYear + (new Random()).nextInt(20);

		firstName = account.firstName;
		lastName = account.lastName;
		String address1 = getRandomAlphaNumString(10);
		String address2 = getRandomAlphaNumString(10);
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
		billingInfo.country = country;
		billingInfo.ipAddress = ipAddress;
		billingInfo.creditCard = creditCard;

		billingInfo.update();

		billingInfo = BillingInfo.get(accountCode);

		assertEquals(firstName, billingInfo.firstName);
		assertEquals(lastName, billingInfo.lastName);
		assertEquals(address1, billingInfo.address1);
		assertEquals(address2, billingInfo.address2);
		assertEquals(city, billingInfo.city);
		assertEquals(state, billingInfo.state);
		assertEquals(zip, billingInfo.zip);
		assertEquals(country, billingInfo.country);
		assertEquals(ipAddress, billingInfo.ipAddress);

		// Recurly returns empty for both the fields below
		// assertEquals(billingInfo.creditCard.number);
		// assertEquals(billingInfo.creditCard.verificationValue);

		assertEquals(expirationMonth, billingInfo.creditCard.expirationMonth);
		assertEquals(expirationYear, billingInfo.creditCard.expirationYear);

		// check if updating billing info of an account that already has billing info works
		verificationValue = getRandomNumString(4);
		expirationMonth = (new Random()).nextInt(12) + 1;
		
		expirationYear = currentYear + (new Random()).nextInt(20);

		firstName = account.firstName;
		lastName = account.lastName;
		address1 = getRandomAlphaNumString(10);
		address2 = getRandomAlphaNumString(10);
		city = "Some City";
		state = "NJ";
		zip = "94105";
		country = "IN";
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
		assertEquals(city, billingInfo.city);
		assertEquals(state, billingInfo.state);
		assertEquals(zip, billingInfo.zip);
		assertEquals(country, billingInfo.country);
		assertEquals(ipAddress, billingInfo.ipAddress);

		// cleanup
		account.delete();

	}

	// Manipulate subscriptions
	@Test
	public void test3() throws Exception {
		// create fresh account
		final String accountCode = getRandomAlphaNumString(5);
		final String username = getRandomAlphaNumString(5);
		String firstName = getRandomAlphaNumString(5);
		String lastName = getRandomAlphaNumString(5);
		final String email = getRandomAlphaNumString(5) + "@site.com";
		final String companyName = getRandomAlphaNumString(5);

		Account account = new Account();

		account.accountCode = accountCode;
		account.username = username;
		account.firstName = firstName;
		account.lastName = lastName;
		account.email = email;
		account.companyName = companyName;

		account.create();

		account = Account.get(accountCode);

		// subscribe to plan1
		final String number = "1";
		final String verificationValue = getRandomNumString(4);
		final Integer expirationMonth = (new Random()).nextInt(12);
		final Integer expirationYear = 2011 + (new Random()).nextInt(20);

		firstName = account.firstName;
		lastName = account.lastName;

		final String address1 = getRandomAlphaNumString(10);
		final String address2 = getRandomAlphaNumString(10);
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
		billingInfo.country = country;
		billingInfo.ipAddress = ipAddress;
		billingInfo.creditCard = creditCard;

		account.billingInfo = billingInfo;

		String planCode = plans[1]; // one of the plans defined in your recurly account
		Integer quantity = 1;

		Subscription subscription = new Subscription(accountCode);
		subscription.account = account;
		subscription.planCode = planCode;
		subscription.quantity = quantity;

		subscription.create();

		// TODO: get subscription and check with asserts
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
		assertEquals(city, billingInfo.city);
		assertEquals(state, billingInfo.state);
		assertEquals(zip, billingInfo.zip);
		assertEquals(country, billingInfo.country);
		assertEquals(ipAddress, billingInfo.ipAddress);

		// Recurly returns a null for both the fields below.
		// assertEquals(billingInfo.creditCard.number);
		// assertEquals(billingInfo.creditCard.verificationValue);

		assertEquals(expirationMonth, billingInfo.creditCard.expirationMonth);
		assertEquals(expirationYear, billingInfo.creditCard.expirationYear);

		// invoice check
		Invoices invoices = Invoices.get(accountCode);
		assertEquals(1, invoices.invoice.size());

		// downgrade check

		planCode = plans[0]; // a plan with lesser features/rate than testplan1
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

		// invoice check
		invoices = Invoices.get(accountCode);
		assertEquals(2, invoices.invoice.size());

		// upgrade check

		planCode = plans[2]; // a plan with better features/rate than testplan1
		quantity = 3;

		subscription = new Subscription(accountCode);
		subscription.timeframe = "now"; // immediate upgrade
		subscription.planCode = planCode;
		subscription.quantity = quantity;

		subscription.update();

		subscription = Subscription.get(accountCode);

		assertEquals(accountCode, subscription.accountCode);
		assertEquals(planCode, subscription.plan.planCode);
		assertEquals("active", subscription.state);
		assertEquals(quantity, subscription.quantity);

		// invoice check
		invoices = Invoices.get(accountCode);
		assertEquals(3, invoices.invoice.size());

		// detailed invoice check
		InvoiceDetailed invoiceDetailed = InvoiceDetailed.get(invoices.invoice.get(2).id);
		assertEquals(invoices.invoice.get(2).id, invoiceDetailed.id);
		assertEquals(1, invoiceDetailed.payment.size());
		assertEquals(1, invoiceDetailed.line_item.size());

		// cancel & check with asserts

		subscription = new Subscription(accountCode);
		subscription.delete();

		subscription = Subscription.get(accountCode);
		assertEquals("canceled", subscription.state);

		account.delete();
	}

}
