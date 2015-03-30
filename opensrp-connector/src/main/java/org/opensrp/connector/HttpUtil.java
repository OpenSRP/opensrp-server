package org.opensrp.connector;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.io.IOUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.opensrp.common.util.HttpResponse;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import com.mysql.jdbc.StringUtils;

@Component
public class HttpUtil {

    public HttpUtil() {
        BasicHttpParams basicHttpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(basicHttpParams, 30000);
        HttpConnectionParams.setSoTimeout(basicHttpParams, 60000);

        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));

        ClientConnectionManager connectionManager = new ThreadSafeClientConnManager(basicHttpParams, registry);
    }

    public static HttpResponse post(String url, String payload, String data, String username,String password) {
        try {
//			String boundary = Long.toHexString(System.currentTimeMillis()); // Just generate some unique random value.
	        String CRLF = "\r\n"; // Line separator required by multipart/form-data.

      //      conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
		//	conn.setRequestProperty("Accept-Charset", charset);

            //con.setFixedLengthStreamingMode(request.toString().length());
			//con.addRequestProperty("Referer", "http://blog.dahanne.net");
			// Start the query
//			conn.connect();
			//OutputStream output = con.getOutputStream();
        	
        	HttpURLConnection con = makeConnection(url, payload, HttpMethod.POST, true, username, password);
        	con.setDoOutput(true);
        	con.setRequestProperty("Content-Type", "application/json");
			String charset = "utf-8";
			PrintWriter writer = new PrintWriter(new OutputStreamWriter(con.getOutputStream(), charset ), true); // true = autoFlush, important!

			// Send normal param.
		    System.out.println(data);
		    writer.append(data);
		    if (writer != null) writer.close();

            return new HttpResponse(con.getResponseCode() == HttpStatus.SC_OK, IOUtils.toString(con.getInputStream()));
			
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public static HttpResponse get(String url, String payload, String username, String password) {
        try {
            HttpURLConnection con = makeConnection(url, payload, HttpMethod.GET, true, username, password);
            return new HttpResponse(con.getResponseCode() == HttpStatus.SC_OK, IOUtils.toString(con.getInputStream()));
        } 
        catch(FileNotFoundException e){
        	return new HttpResponse(true, "");
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public static HttpURLConnection makeConnection(String url, String payload, HttpMethod requestMethod, boolean useBasicAuth, String username, String password) throws IOException {
    	String charset = "UTF-8";

        if(url.endsWith("/")){
        	url = url.substring(0, url.lastIndexOf("/"));
        }
    	URL urlo = new URL(url+(StringUtils.isEmptyOrWhitespaceOnly(payload)?"":("?"+payload)));
		HttpURLConnection conn = (HttpURLConnection) urlo.openConnection();
		conn.setRequestProperty("Accept-Charset", charset);
		
		if(useBasicAuth){
			String encoded = new String(Base64.encodeBase64((username+":"+password).getBytes()));
	        conn.setRequestProperty("Authorization", "Basic "+encoded);
		}
		
		conn.setRequestMethod(requestMethod.name());
		
		return conn;
	}
    
    public static String removeEndingSlash(String str){
		return str.endsWith("/")?str.substring(0, str.lastIndexOf("/")):str;
	}
    public static String removeTrailingSlash(String str){
		return str.startsWith("/")?str.substring(1):str;
	}
}
