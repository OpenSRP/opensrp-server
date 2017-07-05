package org.opensrp.common.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.methods.*;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyStore;

@Component
public class HttpUtil {


    private HttpUtil() {

    }

    public enum AuthType {
        BASIC, TOKEN, NONE
    }

    private final static DefaultHttpClient httpClient = init();

    public static DefaultHttpClient init() {
        try {
            //TODO add option to ignore cetificate validation in opensrp.prop
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
            CustomCertificateSSLSocketFactory sf = new CustomCertificateSSLSocketFactory(trustStore);
            sf.setHostnameVerifier(CustomCertificateSSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            BasicHttpParams basicHttpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(basicHttpParams, 30000);
            HttpConnectionParams.setSoTimeout(basicHttpParams, 60000);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager connectionManager = new ThreadSafeClientConnManager(basicHttpParams, registry);
            return new DefaultHttpClient(connectionManager, basicHttpParams);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static HttpResponse post(String url, String payload, String data, String username, String password) {
        return post(url, payload, data, "application/json", AuthType.BASIC, username + ":" + password);
    }

    public static HttpResponse post(String url, String payload, String data) {
        return post(url, payload, data, "application/json", AuthType.NONE, "");
    }

    public static HttpResponse postWithToken(String url, String payload, String data, String token) {
        return post(url, payload, data, "application/json", AuthType.TOKEN, token);
    }

    //TODO: Move setting content type in makeConnection function.
    public static HttpResponse post(String url, String payload, String data, String contentType, AuthType authType, String authString) {
        try {
            HttpPost request = (HttpPost) makeConnection(url, payload, RequestMethod.POST, authType, authString);
            request.setHeader(HTTP.CONTENT_TYPE, contentType);
            StringEntity entity = new StringEntity(data == null ? "" : data);
            System.out.println(data);
            entity.setContentEncoding(contentType);
            request.setEntity(entity);
            org.apache.http.HttpResponse response = httpClient.execute(request);
            return createCustomResponseFrom(response);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static HttpResponse get(String url, String payload, String username, String password) {
        return get(url, payload, AuthType.BASIC, username + ":" + password);
    }

    public static HttpResponse delete(String url, String payload, String username, String password) {
        return delete(url, payload, AuthType.BASIC, username + ":" + password);
    }

    public static HttpResponse get(String url, String payload) {
        return get(url, payload, AuthType.NONE, "");
    }

    public static HttpResponse getWithToken(String url, String payload, String token) {
        return get(url, payload, AuthType.BASIC, token);
    }

    public static HttpResponse get(String url, String payload, AuthType authType, String authString) {
        try {
            HttpGet request = (HttpGet) makeConnection(url, payload, RequestMethod.GET, authType, authString);
            org.apache.http.HttpResponse response = httpClient.execute(request);
            return createCustomResponseFrom(response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static HttpResponse delete(String url, String payload, AuthType authType, String authString) {
        try {
            HttpDelete request = (HttpDelete) makeConnection(url, payload, RequestMethod.DELETE, authType, authString);
            org.apache.http.HttpResponse response = httpClient.execute(request);
            return createCustomResponseFrom(response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static HttpResponse createCustomResponseFrom(org.apache.http.HttpResponse response) throws IOException {
        int statusCode = response.getStatusLine().getStatusCode();
        String entity = "";
        if (response.getEntity() != null) {
            entity = IOUtils.toString(response.getEntity().getContent());
        }

        return new HttpResponse(checkSuccessBasedOnHttpCode(statusCode), statusCode, entity);
    }

    static boolean checkSuccessBasedOnHttpCode(int httpCode) {
        if (httpCode >= 400 && httpCode <= 599) {
            return false;
        } else {
            return true;
        }
    }

    static HttpRequestBase makeConnection(String url, String payload, RequestMethod method, AuthType authType, String authString) throws URISyntaxException {
        String charset = "UTF-8";

        if (url.endsWith("/")) {
            url = url.substring(0, url.lastIndexOf("/"));
        }
        url = (url + (StringUtils.isBlank(payload) ? "" : ("?" + payload))).replaceAll(" ", "%20");
        URI urlo = new URI(url);

        HttpRequestBase requestBase = null;
        if (method.equals(RequestMethod.GET)) {
            requestBase = new HttpGet(urlo);
        } else if (method.equals(RequestMethod.POST)) {
            requestBase = new HttpPost(urlo);
        } else if (method.equals(RequestMethod.PUT)) {
            requestBase = new HttpPut(urlo);
        } else if (method.equals(RequestMethod.DELETE)) {
            requestBase = new HttpDelete(urlo);
        }
        requestBase.setURI(urlo);
        requestBase.addHeader("Accept-Charset", charset);

        if (authType.name().equalsIgnoreCase("basic")) {
            String encoded = authString.matches(".+:.+") ? new String(Base64.encodeBase64(authString.getBytes())) : authString;
            requestBase.addHeader("Authorization", "Basic " + encoded);
        } else if (authType.name().equalsIgnoreCase("token")) {
            requestBase.addHeader("Authorization", "Token " + authString);
        }

        System.out.println(url);
        return requestBase;
    }

    public static String removeEndingSlash(String str) {
        return str.endsWith("/") ? str.substring(0, str.lastIndexOf("/")) : str;
    }

    public static String removeTrailingSlash(String str) {
        return str.startsWith("/") ? str.substring(1) : str;
    }
}
