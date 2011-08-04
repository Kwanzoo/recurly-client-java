package com.kwanzoo.recurly;

import javax.xml.bind.annotation.XmlRootElement;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;

@XmlRootElement(name = "subscription")
public class TransparentResult extends Subscription {

	private static String transparentName = "transparent";
	private static String resourceName = "results";
	private String resultCode;

	public static Subscription get(final String resultCode) throws Exception {
		try {
			ClientResponse cr = getWebResourceBuilderHtml(getResourcePath(resultCode)).get(ClientResponse.class);
			return cr.getEntity(Subscription.class);
		} catch (final UniformInterfaceException uie) {
			throwStatusBasedException(uie.getResponse());
			return null;
		}
	}

	public static BillingInfo getBillingInfo(final String resultCode) throws Exception {
		try {
			ClientResponse cr = getWebResourceBuilderHtml(getResourcePath(resultCode)).get(ClientResponse.class);
			return cr.getEntity(BillingInfo.class);
		} catch (final UniformInterfaceException uie) {
			throwStatusBasedException(uie.getResponse());
			return null;
		}
	}

	public static Transaction getTransactionInfo(final String resultCode) throws Exception {
		try {
			ClientResponse cr = getWebResourceBuilderHtml(getResourcePath(resultCode)).get(ClientResponse.class);
			return cr.getEntity(Transaction.class);
		} catch (final UniformInterfaceException uie) {
			throwStatusBasedException(uie.getResponse());
			return null;
		}
	}

	private static String getResourcePath(String resultCode) {
		return transparentName + "/" + resourceName + "/" + resultCode;
	}

	@Override
	protected String getResourcePath() {
		return transparentName + "/" + resourceName + "/" + resultCode;
	}

	@Override
	protected String getResourceCreationPath() {
		return getResourcePath();
	}

	public TransparentResult() {
	}

	public TransparentResult(final String resultCode) {
		this.resultCode = resultCode;
	}
}
