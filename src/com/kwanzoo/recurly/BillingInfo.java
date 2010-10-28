package com.kwanzoo.recurly;

import javax.security.auth.login.AccountNotFoundException;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


import com.kwanzoo.recurly.exception.ResourceNotFoundException;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.UniformInterfaceException;

@XmlRootElement(name="billing_info")
public class BillingInfo extends Base{
	private static String resourceName = "billing_info";

	public String accountCode;

	public BillingInfo(){}

	public BillingInfo(final String accountCode){
		this.accountCode = accountCode;
	}

	//Required
    @XmlElement(name="first_name") 
    public String firstName;

    //Required
    @XmlElement(name="last_name") 
    public String lastName;

    //Required
    @XmlElement(name="address1")
    public String address1;

    @XmlElement(name="address2")
    public String address2;
    
    @XmlElement(name="city") 
    public String city;
    
    @XmlElement(name="state")
    public String state;

    //Required
    @XmlElement(name="zip")
    public String zip;

    @XmlElement(name="country")
    public String country;

    @XmlElement(name="ip_address")
    public String ipAddress;

    //Required
    @XmlElement(name="credit_card")
    public CreditCard creditCard;
    
    private static String getResourcePath(String accountCode){
    	return Account.pluralResourceName + "/" + accountCode + "/" + resourceName;
    }

    public static BillingInfo get(final String accountCode) throws Exception{
    	try{
    		return getWebResourceBuilder(getResourcePath(accountCode)).get(new GenericType<BillingInfo>(){});
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
	protected String getResourceCreationPath(){
		//A BillingInfo resource is contained within an Account resource
		//No way to create a stand alone BillingInfo resource.
		return null;
	}  
	   
	//checks if the account exists before updating the subscription.
	public void checkedUpdate() throws Exception{
    	try{
    		Account.get(accountCode);
    	}
    	catch(final ResourceNotFoundException e){
    		throw new AccountNotFoundException();
    	}
    	update();
    }
}