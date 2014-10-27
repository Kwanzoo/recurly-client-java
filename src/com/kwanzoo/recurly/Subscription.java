package com.kwanzoo.recurly;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.UniformInterfaceException;

@XmlRootElement(name = "subscription")
public class Subscription extends Base {

	private static String pluralResourceName = "subscriptions";

	// not populated in request, but used for resource path
	private String uuidInternal;

	private String accountCode;

	@XmlElement(name = "uuid")
	public String uuid;

	@XmlElement(name = "plan_code")
	public String planCode;

	@XmlElement(name = "quantity")
	public Integer quantity;

	@XmlElement(name = "unit_amount_in_cents")
	public Integer unitAmountInCents;

	@XmlElement(name = "timeframe")
	public String timeframe;

	@XmlElement(name = "account")
	public Account account;

	@XmlElement(name = "plan")
	public Plan plan;

	@XmlElement(name = "currency")
	public String currency;

	@XmlElement(name = "state")
	public String state;

	@XmlElement(name = "total_amount_in_cents")
	public Integer totalAmountInCents;

	@XmlElement(name = "activated_at")
	public Date activatedAt;

	@XmlElement(name = "canceled_at")
	public Date canceledAt;

	@XmlElement(name = "expires_at")
	public Date expiresAt;

	@XmlElement(name = "current_period_started_at")
	public Date currentPeriodStartedAt;

	@XmlElement(name = "current_period_ends_at")
	public Date currentPeriodEndsAt;

	@XmlElement(name = "trial_started_at")
	public Date trialStartedAt;

	@XmlElement(name = "trial_ends_at")
	public Date trialEndsAt;

	@XmlElementWrapper(name = "subscription_add_ons")
	@XmlElement(name = "subscription_add_on")
	public List<Addon> addOns;

	private static String getResourcePath(String subscriptionUuid) {
		return pluralResourceName + "/" + subscriptionUuid;
	}

	public static Subscription get(final String subscriptionUuid) throws Exception {
		try {
			return getWebResourceBuilder(getResourcePath(subscriptionUuid)).get(new GenericType<Subscription>() {});
		} catch (final UniformInterfaceException uie) {
			throwStatusBasedException(uie.getResponse());
			return null;
		}
	}

	@Override
	public void delete(String paramKey, String paramValue) throws Exception {
		try {
			getWebResourceBuilder(getResourceDeletionPath(), paramKey, paramValue).put(this);
		} catch (final UniformInterfaceException uie) {
			throwStatusBasedException(uie.getResponse());
		}
	}

	@Override
	public void delete() throws Exception {
		try {
			getWebResourceBuilder(getResourceCancelPath()).put(this);
		} catch (final UniformInterfaceException uie) {
			throwStatusBasedException(uie.getResponse());
		}
	}

	private String getResourceDeletionPath() {
		return pluralResourceName + "/" + (uuidInternal != null ? uuidInternal : uuid) + "/terminate";
	}

	private String getResourceCancelPath() {
		return pluralResourceName + "/" + (uuidInternal != null ? uuidInternal : uuid) + "/cancel";
	}

	@Override
	protected String getResourcePath() {
		return getResourcePath(this.uuidInternal);
	}

	@Override
	protected String getResourceCreationPath() {
		if (accountCode != null && !accountCode.trim().isEmpty()) {
			return Account.pluralResourceName + "/" + accountCode + "/" + pluralResourceName;
		} else {
			return pluralResourceName;
		}
	}
	public Subscription() {
	}

	/**
	 * Allows creation of a new subscription either for an existing account or including a new account as well as
	 * updating/deleting an existing subscription
	 * 
	 * @param uuid
	 *            if set, updates an existing subscription, can be null
	 * @param accountCode
	 *            if set, creates a subscription for an existing account, can be null
	 */
	public Subscription(final String uuid, final String accountCode) {
		this.uuidInternal = uuid;
		this.accountCode = accountCode;
	}
}