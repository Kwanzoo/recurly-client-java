package com.kwanzoo.recurly;

import javax.ws.rs.client.ResponseProcessingException;
import javax.ws.rs.core.GenericType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Response class for {@link Account} objects. Required since request class {@link Account} may not contain
 * hostedLoginToken parameter.
 */
@XmlRootElement(name = "account")
public class AccountResponse extends Base {

	public static String pluralResourceName = "accounts";

	// Required
	@XmlElement(name = "account_code")
	public String accountCode;

	@XmlElement(name = "username")
	public String username;

	@XmlElement(name = "first_name")
	public String firstName;

	@XmlElement(name = "last_name")
	public String lastName;

	@XmlElement(name = "email")
	public String email;

	@XmlElement(name = "company_name")
	public String companyName;

	@XmlElement(name = "balance_in_cents")
	public Integer balanceInCents;

	@XmlElement(name = "billing_info")
	public BillingInfo billingInfo;

	@XmlElement(name = "hosted_login_token")
	public String hostedLoginToken;

	private static String getResourcePath(String accountCode) {
		return pluralResourceName + "/" + accountCode;
	}

	public static AccountResponse get(final String accountCode) throws Exception {
		try {
			return getWebResourceBuilder(getResourcePath(accountCode)).get(new GenericType<AccountResponse>() {});
		} catch (final ResponseProcessingException rpe) {
			throwStatusBasedException(rpe.getResponse());
			return null;
		}
	}

	@Override
	protected String getResourcePath() {
		return getResourcePath(accountCode);
	}

	@Override
	protected String getResourceCreationPath() {
		return pluralResourceName;
	}

	public AccountResponse() {
	}

	public AccountResponse(final String accountCode) {
		this.accountCode = accountCode;
	}

	@Override
	public void create() throws Exception {
		throw new UnsupportedOperationException(
				"AccountResponse class can only be used for retrieving and deleting data, use Account class for creating data.");
	}

	@Override
	public void update() throws Exception {
		throw new UnsupportedOperationException(
				"AccountResponse class can only be used for retrieving and deleting data, use Account class for updating data.");
	}

}