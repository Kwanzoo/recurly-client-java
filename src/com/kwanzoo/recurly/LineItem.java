package com.kwanzoo.recurly;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "line_item")
public class LineItem {

	@XmlElement(name = "id")
	public String id;
    
    @XmlElement(name="type")
    public String type;
    
	@XmlElement(name = "description")
	public String description;

	@XmlElement(name = "applied_coupon_code")
	public String applied_coupon_code;

	@XmlElement(name = "amount_in_cents")
	public Integer amount_in_cents;

	@XmlElement(name = "start_date")
	public Date start_date;
    
    @XmlElement(name = "end_date")
	public Date end_date;
    
    @XmlElement(name = "created_at")
	public Date created_at;
}
