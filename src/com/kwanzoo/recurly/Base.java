package com.kwanzoo.recurly;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.core.MediaType;

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
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.client.urlconnection.HTTPSProperties;
import com.sun.jersey.core.util.Base64;

public abstract class Base{
	private static final String BaseURI = "https://app.recurly.com";
	private static final WebResource webResource;
	private static String base64AuthStr = "";
	private static final int UNPROCESSABLE_ENTITY_HTTP_CODE = 422;
	
	static{
		webResource = getNewWebResource();
	}
	
	private static TrustManager[] getTrustManager(){
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
        return new TrustManager[] {trustManager};
	}

	private static SSLContext getSSLContext(){
		SSLContext context = null;

        try {
            context = SSLContext.getInstance("SSL");
            context.init(null, getTrustManager(), null);
        }
        catch (final Exception e) {
        	context = null;
        	e.printStackTrace();
        }
        return context;
	}

	private static HostnameVerifier getHostNameVerifier(){
		return new HostnameVerifier() {
            @Override
            public boolean verify(final String hostname, final SSLSession sslSession) {
                return true;
            }
        };
	}

	private static WebResource getNewWebResource(){
		final ClientConfig config = new DefaultClientConfig();
		config.getProperties().put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES, new HTTPSProperties(getHostNameVerifier(), getSSLContext()));
        return Client.create(config).resource(BaseURI);
	}

	public static WebResource.Builder getWebResourceBuilder(final String path){
		return webResource.path(path).header("Authorization", base64AuthStr).accept(MediaType.APPLICATION_XML_TYPE);
	}
	
	//This method needs to be invoked only once, just before performing the first recurly operation
	public static void setAuth(final String recurlyUsername, final String recurlyPassword){
		base64AuthStr = new String(Base64.encode(recurlyUsername + ":" + recurlyPassword));
	}

	//Translates a recurly response to an appropriate recurly exception object.
	public static void throwStatusBasedException(final ClientResponse response) throws Exception{
		final ClientResponse.Status status = response.getClientResponseStatus();
		if(status == null){
			final int statusCode = response.getStatus();
			if(UNPROCESSABLE_ENTITY_HTTP_CODE == statusCode){
				System.out.println(response.getEntity(String.class));
				throw new UnprocessableEntityException(response);
			}
			else{
				System.out.println("ClientResponseStatus is null, but found status Code = " + UNPROCESSABLE_ENTITY_HTTP_CODE);
				throw new UnknownRecurlyException(response);
			}
		}
		else{
			if(status.equals(ClientResponse.Status.BAD_REQUEST)){
				throw new BadRequestException(response);
			}
			else if(status.equals(ClientResponse.Status.UNAUTHORIZED)){
				throw new UnauthorizedAccessException(response);
			}
			else if(status.equals(ClientResponse.Status.PAYMENT_REQUIRED)){
				throw new PaymentRequiredException(response);
			}
			else if(status.equals(ClientResponse.Status.FORBIDDEN)){
				throw new ForbiddenAccessException(response);
			}
			else if(status.equals(ClientResponse.Status.NOT_FOUND)){
				throw new ResourceNotFoundException(response);
			}
			else if(status.equals(ClientResponse.Status.PRECONDITION_FAILED)){
				throw new PreconditionFailedException(response);
			}
			else if(status.equals(ClientResponse.Status.INTERNAL_SERVER_ERROR)){
				throw new InternalServerErrorException(response);
			}
			else if(status.equals(ClientResponse.Status.SERVICE_UNAVAILABLE)){
				throw new ServiceUnavailableException(response);
			}
		}
	}
	
	protected abstract String getResourcePath();
	protected abstract String getResourceCreationPath(); 
	
	//default implementations for create, update and delete operation on a resource.
	//read operations are static methods within respective resource classes.
	public void create() throws Exception{
    	try{
    		getWebResourceBuilder(getResourceCreationPath()).post(this);
    	}
    	catch(final UniformInterfaceException uie){
    		throwStatusBasedException(uie.getResponse());
    	}
    }
        
    public void update() throws Exception{
    	try{
    		getWebResourceBuilder(getResourcePath()).put(this);
    	}
    	catch(final UniformInterfaceException uie){
    		throwStatusBasedException(uie.getResponse());
    	}
    }

    public void delete() throws Exception{
    	try{
    		getWebResourceBuilder(getResourcePath()).delete(this);
    	}
    	catch(final UniformInterfaceException uie){
    		throwStatusBasedException(uie.getResponse());
    	}
    }
}