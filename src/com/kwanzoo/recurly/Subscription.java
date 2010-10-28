package com.kwanzoo.recurly;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.UniformInterfaceException;

@XmlRootElement(name="subscription")
public class Subscription extends Base{
	private static String resourceName = "subscription";

	@XmlElement(name="plan_code")
	public String planCode;
	
	@XmlElement(name="quantity")
	public Integer quantity;
	
	@XmlElement(name="unit_amount")
	public Integer	unitAmount;
	
	@XmlElement(name="timeframe")
	public String 	timeframe;
	
	@XmlElement(name="account") 					
	public Account account;

	@XmlElement(name="account_code") 				
	public String accountCode;
	
	@XmlElement(name="plan") 						
	public Plan plan;
	
	@XmlElement(name="state") 						
	public String state;
	
	@XmlElement(name="total_amount_in_cents") 		
	public Integer totalAmountInCents;
	
	@XmlElement(name="activated_at") 				
	public Date activatedAt;
	
	@XmlElement(name="canceled_at") 				
	public Date canceledAt;
	
	@XmlElement(name="expires_at") 					
	public Date expiresAt;
	
	@XmlElement(name="current_period_started_at") 	
	public Date currentPeriodStartedAt;
	
	@XmlElement(name="current_period_ends_at") 		
	public Date	currentPeriodEndsAt;
	
	@XmlElement(name="trial_started_at") 			
	public Date trialStartedAt;
	
	@XmlElement(name="trial_ends_at") 				
	public Date trialEndsAt;

	private static String getResourcePath(String accountCode){
		return Account.pluralResourceName + "/" + accountCode + "/" + resourceName;
	}
	
	public static Subscription get(final String accountCode) throws Exception{
	   	try{
    		return getWebResourceBuilder(getResourcePath(accountCode)).get(new GenericType<Subscription>(){});
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
	
	public Subscription(){}

	public Subscription(final String accountCode){
		this.accountCode = accountCode;
	}
}