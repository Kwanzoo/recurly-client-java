package com.kwanzoo.recurly;

import java.util.List;

import javax.ws.rs.client.ResponseProcessingException;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "subscriptions")
public class Subscriptions extends Base {

	private static String resourceName = "subscriptions";

	@XmlElement(name = "subscription")
	public List<Subscription> subscription;

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

	public Subscriptions() {
	}

	public static Subscriptions get(final String accountCode) throws Exception {
		try {
			return getWebResourceBuilder(getResourcePath(accountCode)).get(Subscriptions.class);
		} catch (final ResponseProcessingException rpe) {
			throwStatusBasedException(rpe.getResponse());
			return null;
		}
	}

	public static Subscriptions get(final String accountCode, final int page) throws Exception {
		try {
			return getWebResourceBuilder(getResourcePath(accountCode), "page", page + "").get(Subscriptions.class);
		} catch (final ResponseProcessingException rpe) {
			throwStatusBasedException(rpe.getResponse());
			return null;
		}
	}
}
