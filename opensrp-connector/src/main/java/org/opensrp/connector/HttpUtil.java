
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
import org.opensrp.common.util.HttpResponse;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import com.mysql.jdbc.StringUtils;

/**
 * The class is a gateway to connect to external system via http for reading and writing data. All get and post 
 * requests use Basic Authorization.
 */
@Component
public class HttpUtil {

    public HttpUtil() {
    }

    /**
     * Posts the data using Http POST.
     * @param url The complete http url of remote server service
     * @param payload The query param to send along with request (could be null if not applicable)
     * @param data JSON Data to write to input stream
     * @param username Username authorized for request (uses Basic Authorization)
     * @param password Password authorized for request (uses Basic Authorization)
     * @return
     */
    public static HttpResponse post(String url, String payload, String data, String username,String password) {
        try {
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
    
    /**
     * Gets the data using Http GET
     * @param url The complete http url of remote server service
     * @param payload The query param to send along with request (could be null if not applicable)
     * @param username Username authorized for request (uses Basic Authorization)
     * @param password Password authorized for request (uses Basic Authorization)
     * @return
     */
    public static HttpResponse get(String url, String payload, String username, String password) {
        try {
            HttpURLConnection con = makeConnection(url, payload, HttpMethod.GET, true, username, password);
            return new HttpResponse(con.getResponseCode() == HttpStatus.SC_OK, IOUtils.toString(con.getInputStream()));
        } 
        catch(FileNotFoundException e){
        	return new HttpResponse(true, "");
        }
        catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    static HttpURLConnection makeConnection(String url, String payload, HttpMethod requestMethod, boolean useBasicAuth, String username, String password) throws IOException {
    	String charset = "UTF-8";

        if(url.endsWith("/")){
        	url = url.substring(0, url.lastIndexOf("/"));
        }
        url = (url+(StringUtils.isEmptyOrWhitespaceOnly(payload)?"":("?"+payload))).replaceAll(" ", "%20");
    	URL urlo = new URL(url);
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