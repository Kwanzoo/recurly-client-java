package com.kwanzoo.recurly;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "add_on")
public class Addon {

	@XmlElement(name = "add_on_code")
	public String addOnCode;

	@XmlElement(name = "quantity")
	public Integer quantity;

	@XmlElement(name = "unit_amount_in_cents")
	public Integer unitAmountInCents;
}