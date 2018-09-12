package org.opensrp.connector.openmrs.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.opensrp.common.util.HttpUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public abstract class OpenmrsService {
	@Value("#{opensrp['openmrs.url']}")
	protected String OPENMRS_BASE_URL;
	
	@Value("#{opensrp['openmrs.username']}")
	protected String OPENMRS_USER;
	
	@Value("#{opensrp['openmrs.password']}")
	protected String OPENMRS_PWD;

	@Value("#{opensrp['openmrs.version']}")
	protected String OPENMRS_VERSION;

	public static final SimpleDateFormat OPENMRS_DATE = new SimpleDateFormat("yyyy-MM-dd");
	public static final String PROBABLE_CAUSE_OF_DEATH_CONCEPT= "5002AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
	public static final String PROBABLE_CAUSE_OF_DEATH_TEXT= "160218AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
	public static final String PROBABLE_CAUSE_PARENT_CONCEPT= "5622AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
	public static final String OPENMRS_PROVIDER= "provider";
	public OpenmrsService() {	}
	
	public OpenmrsService(String openmrsUrl, String user, String password) {
    	OPENMRS_BASE_URL = openmrsUrl;
    	OPENMRS_USER = user;
    	OPENMRS_PWD = password;
	}

	/**
	 * returns url after trimming ending slash
	 * @return
	 */
	public String getURL() {
		return HttpUtil.removeEndingSlash(OPENMRS_BASE_URL);
	}
	
	void setURL(String url) {
		OPENMRS_BASE_URL = url;
	}

public static void main(String[] args) {
	System.out.println(OPENMRS_DATE.format(new Date()));
}
	
}
