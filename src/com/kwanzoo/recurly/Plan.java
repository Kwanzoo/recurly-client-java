package com.kwanzoo.recurly;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="plan")
public class Plan{
	@XmlElement(name="plan_code") 
	public String 	planCode;
	
	@XmlElement(name="name") 
	public String 	name;
	
	@XmlElement(name="version") 
	public Integer	version;
}