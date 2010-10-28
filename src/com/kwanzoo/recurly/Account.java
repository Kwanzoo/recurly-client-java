package com.kwanzoo.recurly;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.UniformInterfaceException;

@XmlRootElement(name="account")
public class Account extends Base{
	public static String pluralResourceName = "accounts";

	//Required
    @XmlElement(name="account_code")
    public String accountCode;
    
    @XmlElement(name="username")
    public String username;
    
    @XmlElement(name="first_name")
    public String firstName;
    
    @XmlElement(name="last_name")
    public String lastName;
    
    @XmlElement(name="email")
    public String email;
    
    @XmlElement(name="company_name")
    public String companyName;
    
    @XmlElement(name="balance_in_cents")
    public Integer balanceInCents;
    
    @XmlElement(name="created_at")
    public Date createdAt;
    
    @XmlElement(name="billing_info")
    public BillingInfo billingInfo;
    
    private static String getResourcePath(String accountCode){
		return pluralResourceName + "/" + accountCode;
	}
    
    public static Account get(final String accountCode) throws Exception{
    	try{
    		return getWebResourceBuilder(getResourcePath(accountCode)).get(new GenericType<Account>(){});
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
		return pluralResourceName;
	}  
	    
    public Account(){}

    public Account(final String accountCode){
    	this.accountCode = accountCode;
    }
}