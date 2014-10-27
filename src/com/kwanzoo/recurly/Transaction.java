package com.kwanzoo.recurly;

import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.UniformInterfaceException;

@XmlRootElement(name = "transaction")
public class Transaction extends Base {

	private static String resourceName = "transaction";

	@XmlAttribute(name = "source")
	public String source;

	@XmlElement(name = "uuid")
	public String uuid;

	@XmlElement(name = "account_code")
	public String accountCode;

	@XmlElement(name = "action")
	public String action;

	@XmlElement(name = "created_at")
	public Date createdAt;

	@XmlElement(name = "amount_in_cents")
	public Integer amountInCents;

	@XmlElement(name = "tax_in_cents")
	public Integer taxInCents;

	@XmlElement(name = "currency")
	public String currency;

	@XmlElement(name = "payment_method")
	public String paymentMethod;

	@XmlElement(name = "status")
	public String status;

	@XmlElement(name = "message")
	public String message;

	@XmlElement(name = "reference")
	public String reference;

	@XmlElement(name = "cvv_result")
	public String cvvResult;

	@XmlElement(name = "avs_result")
	public String avsResult;

	@XmlElement(name = "avs_result_street")
	public String avsResultStreet;

	@XmlElement(name = "avs_result_postal")
	public String avsResultPostal;

	@XmlElement(name = "test")
	public Boolean test;

	@XmlElement(name = "recurring")
	public Boolean recurring;

	@XmlElement(name = "voidable")
	public Boolean voidable;

	@XmlElement(name = "refundable")
	public Boolean refundable;

	@XmlElement(name = "details")
	public TransactionDetails details;

	private static String getResourcePath(String accountCode) {
		return Account.pluralResourceName + "/" + accountCode + "/" + resourceName;
	}

	public static Transaction get(final String accountCode) throws Exception {
		try {
			return getWebResourceBuilder(getResourcePath(accountCode)).get(new GenericType<Transaction>() {});
		} catch (final UniformInterfaceException uie) {
			throwStatusBasedException(uie.getResponse());
			return null;
		}
	}

	@Override
	protected String getResourcePath() {
		return getResourcePath(accountCode);
	}

	@Override
	protected String getResourceCreationPath() {
		return getResourcePath();
	}

	public Transaction() {
	}

	public Transaction(final String accountCode) {
		this.accountCode = accountCode;
	}
}