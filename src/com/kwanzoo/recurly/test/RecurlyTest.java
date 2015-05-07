package com.kwanzoo.recurly.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;

import com.kwanzoo.recurly.Account;
import com.kwanzoo.recurly.AccountResponse;
import com.kwanzoo.recurly.Addon;
import com.kwanzoo.recurly.Adjustment;
import com.kwanzoo.recurly.Base;
import com.kwanzoo.recurly.BillingInfo;
import com.kwanzoo.recurly.Invoice;
import com.kwanzoo.recurly.Invoices;
import com.kwanzoo.recurly.Subscription;
import com.kwanzoo.recurly.Subscriptions;

public class RecurlyTest {

	private String plan1 = "test_plan1";
	private String plan2 = "test_plan2";
	private String plan3 = "test_plan3";
	private String addonCode = "testAddon";

	private String getRandStr(final int n) {
		return RandomStringUtils.randomAlphanumeric(n);
	}

	private String getRandNumber(final int n) {
		return RandomStringUtils.randomNumeric(n);
	}

	@Before
	public void setUp() throws SecurityException, IOException {

		Handler fh = new FileHandler("/tmp/jersey_test.log");
		Logger.getLogger("").addHandler(fh);
		Logger.getLogger("com.sun.jersey").setLevel(Level.FINEST);

		Properties p = new Properties();
		try {
			p.load(new FileInputStream(System.getProperty("user.home") + "/" + "recurly_auth"));
			String apiKey = p.getProperty("recurly_api_key");
			String subdomain = p.getProperty("recurly_subdomain");
			Base.setAuth(apiKey, subdomain);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Manipulate Accounts
	@Test
	public void test1() throws Exception {

		// create fresh account
		final String accountCode = getRandStr(5);
		String username = getRandStr(5);
		String firstName = getRandStr(5);
		String lastName = getRandStr(5);
		String email = getRandStr(5) + "@site.com";
		String companyName = getRandStr(5);

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
		username = getRandStr(5);
		firstName = getRandStr(5);
		lastName = getRandStr(5);
		email = getRandStr(5) + "@site.com";
		companyName = getRandStr(5);

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

		Adjustment charge = new Adjustment(accountCode);
		charge.unitAmountInCents = 500;
		charge.description = "first charge";
		charge.currency = "USD";
		charge.create();

		// add billing info to later add subscription
		String number = "1";
		String verificationValue = getRandNumber(3);
		Integer expirationMonth = (new Random()).nextInt(12) + 1;
		Integer expirationYear = 2012 + (new Random()).nextInt(20);
		String address1 = getRandStr(10);
		String city = "San Fransisco";
		String state = "CA";
		String zip = "20240";
		String country = "US";
		String ipAddress = "127.0.0.1";

		BillingInfo billingInfo = new BillingInfo(accountCode);
		billingInfo.firstName = firstName;
		billingInfo.lastName = lastName;
		billingInfo.address1 = address1;
		billingInfo.city = city;
		billingInfo.state = state;
		billingInfo.zip = zip;
		billingInfo.country = country;
		billingInfo.ipAddress = ipAddress;
		billingInfo.number = number;
		billingInfo.verificationValue = verificationValue;
		billingInfo.month = expirationMonth;
		billingInfo.year = expirationYear;
		billingInfo.update();

		// check if adding subscription works
		Subscription subscription = new Subscription(null, accountCode);
		subscription.currency = "USD";
		subscription.planCode = plan1;
		subscription.unitAmountInCents = 123;
		subscription.create();

		Subscriptions subscriptions = Subscriptions.get(accountCode);
		assertEquals(1, subscriptions.subscription.size());

		// cleanup
		account.delete();
	}

	// Manipulate billing info
	@Test
	public void test2() throws Exception {

		// create fresh account
		final String accountCode = getRandStr(5);
		final String username = getRandStr(5);
		String firstName = getRandStr(5);
		String lastName = getRandStr(5);
		final String email = getRandStr(5) + "@site.com";
		final String companyName = getRandStr(5);

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
		String verificationValue = getRandNumber(3);
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
		billingInfo.number = number;
		billingInfo.verificationValue = verificationValue;
		billingInfo.month = expirationMonth;
		billingInfo.year = expirationYear;
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

		assertEquals(expirationMonth, billingInfo.month);
		assertEquals(expirationYear, billingInfo.year);

		// check if updating billing info of an account that already has billing info works
		verificationValue = getRandNumber(3);
		expirationMonth = (new Random()).nextInt(12) + 1;
		expirationYear = 2012 + (new Random()).nextInt(20);

		firstName = account.firstName;
		lastName = account.lastName;
		address1 = getRandStr(10);
		address2 = getRandStr(10);
		city = "Some City";
		state = "NJ";
		zip = "94105";
		country = "IN";
		ipAddress = "127.0.0.1";

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
		billingInfo.number = number;
		billingInfo.verificationValue = verificationValue;
		billingInfo.month = expirationMonth;
		billingInfo.year = expirationYear;

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
		final String accountCode = getRandStr(5);
		final String username = getRandStr(5);
		String firstName = getRandStr(5);
		String lastName = getRandStr(5);
		final String email = getRandStr(5) + "@site.com";
		final String companyName = getRandStr(5);

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
		final String verificationValue = getRandNumber(3);
		final Integer expirationMonth = (new Random()).nextInt(11) + 1;
		final Integer expirationYear = 2015 + (new Random()).nextInt(20);

		firstName = account.firstName;
		lastName = account.lastName;

		final String address1 = getRandStr(10);
		final String address2 = getRandStr(10);
		final String city = "San Fransisco";
		final String state = "NM";
		final String zip = "99546";
		final String country = "US";
		final String ipAddress = "127.0.0.1";

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
		billingInfo.number = number;
		billingInfo.verificationValue = verificationValue;
		billingInfo.month = expirationMonth;
		billingInfo.year = expirationYear;

		account.billingInfo = billingInfo;

		String planCode = plan2; // one of the plans defined in your recurly account
		Integer quantity = 1;

		Subscription subscription = new Subscription();
		subscription.account = new Account(accountCode);
		subscription.account.billingInfo = billingInfo;
		subscription.planCode = planCode;
		subscription.quantity = quantity;
		subscription.currency = "USD";

		subscription.create();

		Subscriptions subscriptions = Subscriptions.get(accountCode);

		subscription = subscriptions.subscription.get(0);

		assertEquals("USD", subscription.currency);
		assertEquals(planCode, subscription.plan.planCode);
		assertEquals(quantity, subscription.quantity);

		assertNotNull(subscription.account);
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

		assertEquals(expirationMonth, billingInfo.month);
		assertEquals(expirationYear, billingInfo.year);

		// invoice check
		Invoices invoices = Invoices.get(accountCode);
		assertEquals(1, invoices.invoice.size());

		// invoice check with limit
		invoices = Invoices.get(accountCode, 1);
		assertEquals(1, invoices.invoice.size());
		invoices = Invoices.get(accountCode, 2);
		assertEquals(1, invoices.invoice.size());

		// downgrade check

		planCode = plan1; // a plan with lesser features/rate than testplan1
		quantity = 2;

		String uuid = subscription.uuid;

		subscription = new Subscription(uuid, null);
		subscription.timeframe = "now";
		subscription.planCode = planCode;
		subscription.quantity = quantity;

		subscription.update();

		subscription = Subscription.get(uuid);

		assertNotNull(subscription.account);
		assertEquals(planCode, subscription.plan.planCode);
		assertEquals("active", subscription.state);
		assertEquals(quantity, subscription.quantity);

		// invoice check
		invoices = Invoices.get(accountCode);
		assertEquals(2, invoices.invoice.size());

		// upgrade check

		planCode = plan3; // a plan with better features/rate than testplan1
		quantity = 3;

		subscription = new Subscription(uuid, null);
		subscription.timeframe = "now"; // immediate upgrade
		subscription.planCode = planCode;
		subscription.quantity = quantity;

		subscription.update();

		subscription = Subscription.get(uuid);

		assertNotNull(subscription.account);
		assertEquals(planCode, subscription.plan.planCode);
		assertEquals("active", subscription.state);
		assertEquals(quantity, subscription.quantity);

		// invoice list check
		invoices = Invoices.get(accountCode);
		assertEquals(3, invoices.invoice.size());

		// invoice check
		Invoice invoice = invoices.invoice.get(2);
		assertEquals(1, invoice.transactions.size());
		assertEquals(1, invoice.lineItems.size());

		// addon check

		planCode = plan3; // a plan with better features/rate than testplan1
		quantity = 1;
		int addonQuantity = 2;
		int unitAmountInCents = 595;
		Addon addon = new Addon();
		addon.addOnCode = addonCode;
		addon.quantity = addonQuantity;
		addon.unitAmountInCents = unitAmountInCents;
		List<Addon> addons = new ArrayList<Addon>();
		addons.add(addon);

		subscription = new Subscription(uuid, null);
		subscription.timeframe = "now"; // immediate upgrade
		subscription.planCode = planCode;
		subscription.quantity = quantity;
		subscription.addOns = addons;

		subscription.update();

		subscription = Subscription.get(uuid);

		assertNotNull(subscription.account);
		assertEquals(planCode, subscription.plan.planCode);
		assertEquals("active", subscription.state);
		assertEquals(quantity, subscription.quantity);
		assertEquals(1, subscription.addOns.size());
		assertEquals(Integer.valueOf(addonQuantity), subscription.addOns.get(0).quantity);
		assertEquals(Integer.valueOf(unitAmountInCents), subscription.addOns.get(0).unitAmountInCents);
		assertEquals(addonCode, subscription.addOns.get(0).addOnCode);

		// cancel & check with asserts

		subscription = new Subscription(uuid, null);
		subscription.delete();

		subscription = Subscription.get(uuid);
		assertEquals("canceled", subscription.state);

		account.delete();
	}

	@Test
	public void test4() throws Exception {
		// create account with subscription, terminate subscription without refund, create new subscription for same
		// account

		// create fresh account
		final String accountCode = getRandStr(5);
		final String username = getRandStr(5);
		String firstName = getRandStr(5);
		String lastName = getRandStr(5);
		final String email = getRandStr(5) + "@site.com";
		final String companyName = getRandStr(5);

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
		final String verificationValue = getRandNumber(3);
		final Integer expirationMonth = (new Random()).nextInt(11) + 1;
		final Integer expirationYear = 2011 + (new Random()).nextInt(20);

		final String address1 = getRandStr(10);
		final String address2 = getRandStr(10);
		final String city = "San Fransisco";
		final String state = "NM";
		final String zip = "99546";
		final String country = "US";
		final String ipAddress = "127.0.0.1";

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
		billingInfo.number = number;
		billingInfo.verificationValue = verificationValue;
		billingInfo.month = expirationMonth;
		billingInfo.year = expirationYear;

		account.billingInfo = billingInfo;

		String planCode = plan1; // one of the plans defined in your recurly account
		Integer quantity = 1;

		Subscription subscription = new Subscription();
		subscription.account = new Account(accountCode);
		subscription.account.billingInfo = billingInfo;
		subscription.planCode = planCode;
		subscription.quantity = quantity;
		subscription.trialEndsAt = new Date(new Date().getTime() + (long) (45L * 24L * 60L * 60L * 1000L));
		subscription.currency = "USD";

		subscription.create();

		Subscriptions subscriptions = Subscriptions.get(accountCode);
		Date trialEndDate1 = subscriptions.subscription.get(0).trialEndsAt;

		subscriptions.subscription.get(0).delete("refund", "none");

		try {
			Subscription.get(accountCode);
			fail("Exception should be thrown, since subscription is not existing.");
		} catch (Exception e) {
			// ignore
		}

		subscription = new Subscription();
		subscription.account = account;
		subscription.planCode = planCode;
		subscription.quantity = quantity;
		subscription.trialEndsAt = new Date(new Date().getTime() + (long) (30L * 24L * 60L * 60L * 1000L));
		subscription.currency = "USD";
		subscription.create();

		subscriptions = Subscriptions.get(accountCode);

		assertNotNull(subscriptions);
		assertTrue(subscriptions.subscription.get(0).trialEndsAt.before(trialEndDate1));

		// test delete & reopen
		account.delete();

		// test reopen
		account.reopen();

		// cleanup
		account.delete();

	}

	@Test
	public void test5() throws Exception {
		// create fresh account
		final String accountCode = getRandStr(5);
		String username = getRandStr(5);
		String firstName = getRandStr(5);
		String lastName = getRandStr(5);
		String email = getRandStr(5) + "@site.com";
		String companyName = getRandStr(5);

		Account account = new Account();

		account.accountCode = accountCode;
		account.username = username;
		account.firstName = firstName;
		account.lastName = lastName;
		account.email = email;
		account.companyName = companyName;

		// subscribe to plan1
		final String number = "4000-0000-0000-0002";
		final String verificationValue = getRandNumber(3);
		final Integer expirationMonth = (new Random()).nextInt(11) + 1;
		final Integer expirationYear = 2015 + (new Random()).nextInt(20);

		final String address1 = getRandStr(10);
		final String address2 = getRandStr(10);
		final String city = "San Fransisco";
		final String state = "NM";
		final String zip = "99546";
		final String country = "US";
		final String ipAddress = "127.0.0.1";

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
		billingInfo.number = number;
		billingInfo.verificationValue = verificationValue;
		billingInfo.month = expirationMonth;
		billingInfo.year = expirationYear;

		account.billingInfo = billingInfo;

		String planCode = plan1; // one of the plans defined in your recurly account
		Integer quantity = 1;

		Subscription subscription = new Subscription();
		subscription.account = account;
		subscription.account.billingInfo = billingInfo;
		subscription.planCode = planCode;
		subscription.quantity = quantity;
		subscription.trialEndsAt = new Date(new Date().getTime() + (long) (45L * 24L * 60L * 60L * 1000L));
		subscription.currency = "USD";

		try {
			subscription.create();
			fail("Expecting subscription creation to fail");
		} catch (Exception e) {
			assertEquals("Request resulted in HTTP response code: 422", e.getMessage());
		}
	}

	@Test
	public void test6() throws Exception {

		// create fresh account via subscription with success
		final String accountCode = getRandStr(5);
		String username = getRandStr(5);
		String firstName = getRandStr(5);
		String lastName = getRandStr(5);
		String email = getRandStr(5) + "@site.com";
		String companyName = getRandStr(5);

		Account account = new Account();

		account.accountCode = accountCode;
		account.username = username;
		account.firstName = firstName;
		account.lastName = lastName;
		account.email = email;
		account.companyName = companyName;

		// subscribe to plan1
		final String number = "4111-1111-1111-1111";
		final String verificationValue = getRandNumber(3);
		final Integer expirationMonth = (new Random()).nextInt(11) + 1;
		final Integer expirationYear = 2015 + (new Random()).nextInt(20);

		final String address1 = getRandStr(10);
		final String address2 = getRandStr(10);
		final String city = "San Fransisco";
		final String state = "NM";
		final String zip = "99546";
		final String country = "US";
		final String ipAddress = "127.0.0.1";

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
		billingInfo.number = number;
		billingInfo.verificationValue = verificationValue;
		billingInfo.month = expirationMonth;
		billingInfo.year = expirationYear;

		account.billingInfo = billingInfo;

		String planCode = plan1; // one of the plans defined in your recurly account
		Integer quantity = 1;

		Subscription subscription = new Subscription();
		subscription.account = account;
		subscription.account.billingInfo = billingInfo;
		subscription.planCode = planCode;
		subscription.quantity = quantity;
		subscription.trialEndsAt = new Date(new Date().getTime() + (long) (45L * 24L * 60L * 60L * 1000L));
		subscription.currency = "USD";

		subscription.create();
	}

	// Check account response with hosted_login_token parameter that cannot be posted again
	@Test
	public void testAccountResponse() throws Exception {

		// create fresh account
		final String accountCode = getRandStr(5);
		String username = getRandStr(5);
		String firstName = getRandStr(5);
		String lastName = getRandStr(5);
		String email = getRandStr(5) + "@site.com";
		String companyName = getRandStr(5);

		Account account = new Account();

		account.accountCode = accountCode;
		account.username = username;
		account.firstName = firstName;
		account.lastName = lastName;
		account.email = email;
		account.companyName = companyName;

		account.create();

		// check if find works
		AccountResponse accountResponse = AccountResponse.get(accountCode);

		assertEquals(accountCode, accountResponse.accountCode);
		assertEquals(username, accountResponse.username);
		assertEquals(firstName, accountResponse.firstName);
		assertEquals(lastName, accountResponse.lastName);
		assertEquals(email, accountResponse.email);
		assertEquals(companyName, accountResponse.companyName);
		assertNotNull(accountResponse.hostedLoginToken);

		// cleanup
		accountResponse.delete();
	}

	// Check modification with account response object
	@Test
	public void testAccountResponseModification() {

		// create fresh account
		final String accountCode = getRandStr(5);
		String username = getRandStr(5);
		String firstName = getRandStr(5);
		String lastName = getRandStr(5);
		String email = getRandStr(5) + "@site.com";
		String companyName = getRandStr(5);

		AccountResponse account = new AccountResponse();

		account.accountCode = accountCode;
		account.username = username;
		account.firstName = firstName;
		account.lastName = lastName;
		account.email = email;
		account.companyName = companyName;

		try {
			account.create();
			fail("UnsupportedOperationException expected");
		} catch (Exception e) {
		}

		try {
			account.update();
			fail("UnsupportedOperationException expected");
		} catch (Exception e) {
		}

	}

}
