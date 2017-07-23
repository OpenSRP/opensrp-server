package org.opensrp.connector.dhis2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;
import org.opensrp.common.util.HttpResponse;
import org.opensrp.common.util.HttpUtil;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import com.mysql.jdbc.StringUtils;

@Component
public class Dhis2HttpUtils extends DHIS2Service {
	
	public Dhis2HttpUtils() {
		
	}
	
	public Dhis2HttpUtils(String dhis2Url, String user, String password) {
		super(dhis2Url, user, password);
	}
	
	public static HttpResponse post(String url, String payload, String data, String username, String password) {
		new TurnOffCertificateValidation().ForHTTPSConnections();
		String output = null;
		if (url.endsWith("/")) {
			url = url.substring(0, url.lastIndexOf("/"));
		}
		url = (url + (StringUtils.isEmptyOrWhitespaceOnly(payload) ? "" : ("?" + payload))).replaceAll(" ", "%20");
		try {
			URL urlo = new URL(url);
			HttpURLConnection con = (HttpURLConnection) urlo.openConnection();
			con.setRequestProperty("Content-Type", "application/json");
			String charset = "UTF-8";
			con.setRequestProperty("Accept-Charset", charset);
			String encoded = new String(Base64.encodeBase64((username + ":" + password).getBytes()));
			con.setRequestProperty("Authorization", "Basic " + encoded);
			con.setRequestMethod(HttpMethod.POST.name());
			con.setFixedLengthStreamingMode(data.toString().getBytes().length);
			con.setDoOutput(true);
			PrintWriter writer = new PrintWriter(new OutputStreamWriter(con.getOutputStream(), charset), true); // true = autoFlush, important!
			writer.print(data.toString());
			if (writer != null)
				writer.close();
			int statusCode = con.getResponseCode();
			if (statusCode != HttpURLConnection.HTTP_OK) {
				// throw some exception
			}
			BufferedReader br = new BufferedReader(new InputStreamReader((con.getInputStream())));
			StringBuilder sb = new StringBuilder();
			
			while ((output = br.readLine()) != null) {
				sb.append(output);
			}
			System.out.println(sb.toString());
			return new HttpResponse(con.getResponseCode() == HttpStatus.SC_OK, sb.toString());
		}
		catch (FileNotFoundException e) {
			return new HttpResponse(true, "");
		}
		catch (IllegalArgumentException e) {
			throw new IllegalArgumentException(e);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public JSONObject get(String url, String payload) throws JSONException {
		HttpResponse op = HttpUtil.get(HttpUtil.removeEndingSlash(this.DHIS2_BASE_URL) + "/" + url, payload, this.DHIS2_USER,
		    this.DHIS2_PWD);
		JSONObject response = new JSONObject(op.body());
		
		return response;
	}
	
	public JSONObject post(String url, String payload, String data) throws JSONException {
		HttpResponse op = HttpUtil.post(HttpUtil.removeEndingSlash(this.DHIS2_BASE_URL) + "/" + url, payload, data,
		    this.DHIS2_USER, this.DHIS2_PWD);
		JSONObject response = new JSONObject(op.body());
		
		return response;
	}
	
}
