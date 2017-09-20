package org.opensrp.connector.openmrs.service;

import org.opensrp.common.util.HttpUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public abstract class OpenmrsService {

	@Value("#{opensrp['openmrs.url']}")
	protected String OPENMRS_BASE_URL;

	@Value("#{opensrp['openmrs.username']}")
	protected String OPENMRS_USER;

	@Value("#{opensrp['openmrs.password']}")
	protected String OPENMRS_PWD;

	public static final SimpleDateFormat OPENMRS_DATE = new SimpleDateFormat("yyyy-MM-dd");

	public static final String PROBABLE_CAUSE_PARENT_CONCEPT = "5622AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";

	public OpenmrsService() {
	}

	public OpenmrsService(String openmrsUrl, String user, String password) {
		OPENMRS_BASE_URL = openmrsUrl;
		OPENMRS_USER = user;
		OPENMRS_PWD = password;
	}

	/**
	 * returns url after trimming ending slash
	 *
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
