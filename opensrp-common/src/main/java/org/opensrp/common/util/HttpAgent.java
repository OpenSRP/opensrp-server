package org.opensrp.common.util;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.io.IOUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.Security;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//TODO: Find difference between this and HttpUtil class.
@Component
public class HttpAgent {

    private final DefaultHttpClient httpClient;

    public HttpAgent() {
        BasicHttpParams basicHttpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(basicHttpParams, 30000);
        HttpConnectionParams.setSoTimeout(basicHttpParams, 60000);

        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        registry.register(new Scheme("https", sslSocketFactoryWithDrishtiCertificate(), 443));

        ClientConnectionManager connectionManager = new ThreadSafeClientConnManager(basicHttpParams, registry);
        httpClient = new DefaultHttpClient(connectionManager, basicHttpParams);
    }

    public HttpResponse post(String url, String data, String contentType) {
        HttpPost request = new HttpPost(url);
        try {
            request.setHeader(HTTP.CONTENT_TYPE, contentType);
            StringEntity entity = new StringEntity(data);
            entity.setContentEncoding(contentType);
            request.setEntity(entity);
            org.apache.http.HttpResponse response = httpClient.execute(request);
            return new HttpResponse(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK, IOUtils.toString(response.getEntity().getContent()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public HttpResponse put(String url, Map<String, String> formParams) {
        HttpPut request = new HttpPut(url);
        try {
            List<NameValuePair> urlParameters = new ArrayList<>();
            for (String param : formParams.keySet()) {
                urlParameters.add(new BasicNameValuePair(param, formParams.get(param)));
            }
            request.setEntity(new UrlEncodedFormEntity(urlParameters));
            org.apache.http.HttpResponse response = httpClient.execute(request);
            return new HttpResponse(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK,
                    IOUtils.toString(response.getEntity().getContent()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public HttpResponse get(String url) {
        HttpGet request = new HttpGet(url);
        try {
            org.apache.http.HttpResponse response = httpClient.execute(request);
            return new HttpResponse(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK, IOUtils.toString(response.getEntity().getContent()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public HttpResponse getWithSocketTimeout(String url) {
        HttpGet request = new HttpGet(url);
        try {
            httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 0);
            org.apache.http.HttpResponse response = httpClient.execute(request);
            return new HttpResponse(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK, IOUtils.toString(response.getEntity().getContent()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private SocketFactory sslSocketFactoryWithDrishtiCertificate() {
        try {
            Security.addProvider(new BouncyCastleProvider());
            KeyStore trustedKeystore = KeyStore.getInstance("BKS");
            InputStream inputStream = this.getClass().getResourceAsStream("/drishti_client.keystore");
            try {
                trustedKeystore.load(inputStream, "phone red pen".toCharArray());
            } finally {
                inputStream.close();
            }

            SSLSocketFactory socketFactory = new SSLSocketFactory(trustedKeystore);
            final X509HostnameVerifier oldVerifier = SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
            socketFactory.setHostnameVerifier(oldVerifier);
            return socketFactory;
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }
}
