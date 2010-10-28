package com.kwanzoo.recurly;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="credit_card")
public class CreditCard {
    @XmlElement(name="number")
    public String number;
    
    @XmlElement(name="last_four")
    public String lastFour;
    
    @XmlElement(name="type")
    public String type;
    
    @XmlElement(name="verification_value")
    public String verificationValue;
    
    @XmlElement(name="month")
    public Integer expirationMonth;
    
    @XmlElement(name="year")
    public Integer expirationYear;
}
