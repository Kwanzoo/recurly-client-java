package com.kwanzoo.recurly;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement(name = "error")
public class Error {

	@XmlAttribute(name = "field")
	public String field;

	@XmlAttribute(name = "symbol")
	public String symbol;

	@XmlAttribute(name = "lang")
	public String lang;

	@XmlValue
	public String error;

}