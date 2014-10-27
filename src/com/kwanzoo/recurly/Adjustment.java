package com.kwanzoo.recurly;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "adjustment")
public class Adjustment extends Base {

	private static String resourceName = "adjustments";

	@XmlElement(name = "unit_amount_in_cents")
	public Integer unitAmountInCents;

	@XmlElement(name = "discount_in_cents")
	public Integer discountInCents;

	@XmlElement(name = "tax_in_cents")
	public Integer taxInCents;

	@XmlElement(name = "total_in_cents")
	public Integer totalInCents;

	@XmlElement(name = "description")
	public String description;

	@XmlElement(name = "currency")
	public String currency;

	@XmlElement(name = "uuid")
	public String uuid;

	@XmlElement(name = "accounting_code")
	public String accountingCode;

	@XmlElement(name = "tax_exempt")
	public Boolean taxExempt;

	@XmlElement(name = "product_code")
	public String productCode;

	@XmlElement(name = "origin")
	public String origin;

	@XmlElement(name = "quantity")
	public String quantity;

	@XmlElement(name = "start_date")
	public Date startDate;

	@XmlElement(name = "created_at")
	public Date createdAt;

	@XmlElement(name = "end_date")
	public Date endDate;

	private String accountCode;

	@Override
	protected String getResourcePath() {
		return Account.pluralResourceName + "/" + accountCode + "/" + resourceName;
	}

	@Override
	protected String getResourceCreationPath() {
		return getResourcePath();
	}

	public Adjustment() {
	}

	public Adjustment(final String accountCode) {
		this.accountCode = accountCode;
	}
}
