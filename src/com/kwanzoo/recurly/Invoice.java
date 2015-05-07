package com.kwanzoo.recurly;

import java.util.Date;
import java.util.List;

import javax.ws.rs.client.ResponseProcessingException;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "invoice")
public class Invoice extends Base {

	private static String resourceName = "invoices";

	@XmlElement(name = "subtotal_in_cents")
	public Integer subtotalInCents;

	@XmlElement(name = "total_in_cents")
	public Integer totalInCents;

	@XmlElement(name = "tax_in_cents")
	public Integer taxInCents;

	@XmlElement(name = "currency")
	public String currency;

	@XmlElement(name = "uuid")
	public String uuid;

	@XmlElement(name = "invoice_number")
	public Integer invoiceNumber;

	@XmlElement(name = "vat_number")
	public String vatNumber;

	@XmlElement(name = "po_number")
	public String poNumber;

	@XmlElement(name = "state")
	public String state;

	@XmlElement(name = "created_at")
	public Date createdAt;

	@XmlElement(name = "closed_at")
	public Date closedAt;

	@XmlElement(name = "tax_type")
	public String taxType;

	@XmlElement(name = "tax_rate")
	public Double taxRate;

	@XmlElement(name = "net_terms")
	public Integer netTerms;

	@XmlElement(name = "collection_method")
	public String collectionMethod;

	@XmlElementWrapper(name = "transactions")
	@XmlElement(name = "transaction")
	public List<Transaction> transactions;

	@XmlElementWrapper(name = "line_items")
	@XmlElement(name = "adjustment")
	public List<Adjustment> lineItems;

	@Override
	protected String getResourcePath() {
		return resourceName + "/" + uuid;
	}

	protected static String getResourcePath(String invoiceId) {
		return resourceName + "/" + invoiceId;
	}

	@Override
	protected String getResourceCreationPath() {
		return getResourcePath();
	}

	public Invoice() {
	}

	public Invoice(final String invoice_id) {
		this.uuid = invoice_id;
	}

	public static Invoice get(final String invoiceId) throws Exception {
		try {
			return getWebResourceBuilder(getResourcePath(invoiceId)).get(Invoice.class);
		} catch (final ResponseProcessingException rpe) {
			throwStatusBasedException(rpe.getResponse());
			return null;
		}
	}
}
