package com.kwanzoo.recurly;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.ResponseProcessingException;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

import org.glassfish.jersey.internal.util.Base64;

import com.kwanzoo.recurly.exception.BadRequestException;
import com.kwanzoo.recurly.exception.ForbiddenAccessException;
import com.kwanzoo.recurly.exception.InternalServerErrorException;
import com.kwanzoo.recurly.exception.PaymentRequiredException;
import com.kwanzoo.recurly.exception.PreconditionFailedException;
import com.kwanzoo.recurly.exception.ResourceNotFoundException;
import com.kwanzoo.recurly.exception.ServiceUnavailableException;
import com.kwanzoo.recurly.exception.UnauthorizedAccessException;
import com.kwanzoo.recurly.exception.UnknownRecurlyException;
import com.kwanzoo.recurly.exception.UnprocessableEntityException;

public abstract class Base {

	@Provider
	private static final class HttpStatusCodeFilter implements ClientResponseFilter {

		@Override
		public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext)
				throws IOException {
			// any HTTP response codes >= 300 are treated as exception
			if (responseContext.getStatus() >= 300) {
				throw new RuntimeException("Request resulted in HTTP response code: " + responseContext.getStatus());
			}
		}
	}

	private static String baseURI = "https://[subdomain].recurly.com/v2";
	private static WebTarget webResource = null;
	private static String base64AuthStr = "";
	private static final int UNPROCESSABLE_ENTITY_HTTP_CODE = 422;

	private static TrustManager[] getTrustManager() {
		final X509TrustManager trustManager = new X509TrustManager() {

			@Override
			public void checkClientTrusted(final X509Certificate[] arg0, final String arg1) throws CertificateException {
				return;
			}

			@Override
			public void checkServerTrusted(final X509Certificate[] arg0, final String arg1) throws CertificateException {
				return;
			}

			@Override
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}
		};
		return new TrustManager[] { trustManager };
	}

	private static SSLContext getSSLContext() {
		SSLContext context = null;

		try {
			context = SSLContext.getInstance("SSL");
			context.init(null, getTrustManager(), null);
		} catch (final Exception e) {
			context = null;
			e.printStackTrace();
		}
		return context;
	}

	private static HostnameVerifier getHostNameVerifier() {
		return new HostnameVerifier() {

			@Override
			public boolean verify(final String hostname, final SSLSession sslSession) {
				return true;
			}
		};
	}

	private static WebTarget getNewWebResource() {
		WebTarget client = ClientBuilder.newBuilder().sslContext(getSSLContext())
				.hostnameVerifier(getHostNameVerifier()).build().target(baseURI);
		return client;
	}
	public static Invocation.Builder getWebResourceBuilder(final String path) {
		return webResource.path(path).request(MediaType.APPLICATION_XML_TYPE).header("Authorization", base64AuthStr)
				.header("Content-Type", "application/xml; charset=utf-8");
	}

	public static Invocation.Builder getWebResourceBuilder(final String path, final String paramKey,
			final String paramValue) {
		return webResource.path(path).queryParam(paramKey, paramValue).request(MediaType.APPLICATION_XML_TYPE)
				.header("Authorization", base64AuthStr);
	}

	public static Invocation.Builder getWebResourceBuilderHtml(final String path) {
		return webResource.path(path).request(MediaType.TEXT_HTML).header("Authorization", base64AuthStr);
	}

	// This method needs to be invoked only once, just before performing the first recurly operation
	public static void setAuth(final String recurlyApiKey, final String recurlySubdomain) {
		baseURI = baseURI.replace("[subdomain]", recurlySubdomain);
		base64AuthStr = new String(Base64.encode(recurlyApiKey.getBytes()));
		webResource = getNewWebResource();
		// webResource.register(new LoggingFilter());
		webResource.register(new HttpStatusCodeFilter());
	}

	// Translates a recurly response to an appropriate recurly exception object.
	public static void throwStatusBasedException(final Response response) throws Exception {
		final Status status = Response.Status.fromStatusCode(response.getStatus());
		if (status == null) {
			final int statusCode = response.getStatus();
			if (UNPROCESSABLE_ENTITY_HTTP_CODE == statusCode) {
				// System.out.println(response.getEntity(String.class));
				throw new UnprocessableEntityException(response);
			} else {
				System.out.println("ClientResponseStatus is null, but found status Code = "
						+ UNPROCESSABLE_ENTITY_HTTP_CODE);
				throw new UnknownRecurlyException(response);
			}
		} else {
			if (status.equals(Response.Status.BAD_REQUEST)) {
				throw new BadRequestException(response);
			} else if (status.equals(Response.Status.UNAUTHORIZED)) {
				throw new UnauthorizedAccessException(response);
			} else if (status.equals(Response.Status.PAYMENT_REQUIRED)) {
				throw new PaymentRequiredException(response);
			} else if (status.equals(Response.Status.FORBIDDEN)) {
				throw new ForbiddenAccessException(response);
			} else if (status.equals(Response.Status.NOT_FOUND)) {
				throw new ResourceNotFoundException(response);
			} else if (status.equals(Response.Status.PRECONDITION_FAILED)) {
				throw new PreconditionFailedException(response);
			} else if (status.equals(Response.Status.INTERNAL_SERVER_ERROR)) {
				throw new InternalServerErrorException(response);
			} else if (status.equals(Response.Status.SERVICE_UNAVAILABLE)) {
				throw new ServiceUnavailableException(response);
			}
		}
	}

	protected abstract String getResourcePath();
	protected abstract String getResourceCreationPath();

	// default implementations for create, update and delete operation on a resource.
	// read operations are static methods within respective resource classes.
	public void create() throws Exception {
		try {
			getWebResourceBuilder(getResourceCreationPath()).post(Entity.xml(this));
		} catch (final ResponseProcessingException rpe) {
			throwStatusBasedException(rpe.getResponse());
		}
	}

	public void update() throws Exception {
		try {
			getWebResourceBuilder(getResourcePath()).put(Entity.xml(this));
		} catch (final ResponseProcessingException rpe) {
			throwStatusBasedException(rpe.getResponse());
		}
	}

	public void delete() throws Exception {
		try {
			getWebResourceBuilder(getResourcePath()).delete();
		} catch (final ResponseProcessingException rpe) {
			throwStatusBasedException(rpe.getResponse());
		}
	}

	/**
	 * Delete with the option to pass an additional key value pair, that will be appended to the resource path
	 * 
	 * @param paramKey
	 * @param paramValue
	 * @throws Exception
	 */
	public void delete(String paramKey, String paramValue) throws Exception {
		try {
			getWebResourceBuilder(getResourcePath(), paramKey, paramValue).delete();
		} catch (final ResponseProcessingException rpe) {
			throwStatusBasedException(rpe.getResponse());
		}
	}
}