package com.kwanzoo.recurly;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "invoice")
public class Invoice {

	@XmlElement(name = "subtotal_in_cents")
	public Integer subtotal_in_cents;

	@XmlElement(name = "total_in_cents")
	public Integer total_in_cents;

	@XmlElement(name = "id")
	public String id;

	@XmlElement(name = "account_code")
	public String account_code;

	@XmlElement(name = "invoice_number")
	public Integer invoice_number;

	@XmlElement(name = "date")
	public Date date;

}
