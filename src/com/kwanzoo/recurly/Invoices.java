package com.kwanzoo.recurly;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.sun.jersey.api.client.UniformInterfaceException;

@XmlRootElement(name = "invoices")
public class Invoices extends Base {

	private static String resourceName = "invoices";

	@XmlElement(name = "invoice")
	public List<Invoice> invoice;

	public String account_code;

	@Override
	protected String getResourcePath() {
		return Account.pluralResourceName + "/" + account_code + "/" + resourceName;
	}

	private static String getResourcePath(String accountCode) {
		return Account.pluralResourceName + "/" + accountCode + "/" + resourceName;
	}

	@Override
	protected String getResourceCreationPath() {
		return getResourcePath();
	}

	public Invoices() {
	}

	public static Invoices get(final String accountCode) throws Exception {
		try {
			return getWebResourceBuilder(getResourcePath(accountCode)).get(Invoices.class);
		} catch (final UniformInterfaceException uie) {
			throwStatusBasedException(uie.getResponse());
			return null;
		}
	}
}
