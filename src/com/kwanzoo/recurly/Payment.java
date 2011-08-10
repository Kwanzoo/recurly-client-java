package com.kwanzoo.recurly;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "payment")
public class Payment {

	@XmlElement(name = "id")
	public String id;
    
	@XmlElement(name = "reference")
	public String reference;
    
	@XmlElement(name = "message")
	public String message;

	@XmlElement(name = "amount_in_cents")
	public Integer amount_in_cents;
    
	@XmlElement(name = "date")
	public Date date;
}
