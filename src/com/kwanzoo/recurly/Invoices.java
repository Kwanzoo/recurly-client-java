package com.kwanzoo.recurly;

import java.util.List;

import javax.ws.rs.client.ResponseProcessingException;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

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
		} catch (final ResponseProcessingException rpe) {
			throwStatusBasedException(rpe.getResponse());
			return null;
		}
	}

	public static Invoices get(final String accountCode, final int limit) throws Exception {
		try {
			return getWebResourceBuilder(getResourcePath(accountCode), "per_page", limit + "").get(Invoices.class);
		} catch (final ResponseProcessingException rpe) {
			throwStatusBasedException(rpe.getResponse());
			return null;
		}
	}
}
