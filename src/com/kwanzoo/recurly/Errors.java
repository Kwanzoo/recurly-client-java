package com.kwanzoo.recurly;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "errors")
public class Errors {

	@XmlElement(name = "error")
	public List<Error> error;

}