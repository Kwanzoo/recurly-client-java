package com.kwanzoo.recurly;

import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.UniformInterfaceException;

@XmlRootElement(name = "transaction")
public class Transaction extends Base{

	private static String resourceName = "transaction";

	@XmlAttribute(name = "type")
	public String type;

	@XmlElement(name = "id")
	public String id;
	
	@XmlElement(name = "account_code")
	public String accountCode;

	@XmlElement(name = "action")
	public String action;

	@XmlElement(name = "date")
	public Date date;

	@XmlElement(name = "amount_in_cents")
	public Integer amountInCents;
	
	@XmlElement(name = "status")
	public String status;
	
	@XmlElement(name = "message")
	public String message;
	
	@XmlElement(name = "reference")
	public String reference;
	
	@XmlElement(name = "cvv_result")
	public String cvvResult;
	
	@XmlElement(name = "avs_result")
	public String avs_result;
	
	@XmlElement(name = "avs_result_street")
	public String avs_result_street;
	
	@XmlElement(name = "avs_result_postal")
	public String avs_result_postal;
	
	@XmlElement(name = "test")
	public Boolean test;
	
	@XmlElement(name = "voidable")
	public Boolean voidable;
	
	@XmlElement(name = "refundable")
	public Boolean refundable;
	
	@XmlElement(name = "credit_card")
	public CreditCard credit_card;
	
	@XmlElement(name = "account")
	public Account account;

	private static String getResourcePath(String accountCode){
		return Account.pluralResourceName + "/" + accountCode + "/" + resourceName;
	}
	
	public static Transaction get(final String accountCode) throws Exception{
	   	try{
    		return getWebResourceBuilder(getResourcePath(accountCode)).get(new GenericType<Transaction>(){});
    	}
    	catch(final UniformInterfaceException uie){
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
	
	public Transaction(){}

	public Transaction(final String accountCode){
		this.accountCode = accountCode;
	}
}