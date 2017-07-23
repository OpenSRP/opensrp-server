package org.opensrp.web.rest;

import static java.text.MessageFormat.format;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.ArrayList;

import org.joda.time.DateTime;
import org.json.JSONObject;
import org.opensrp.domain.Report;
import org.opensrp.service.ReportService;
import org.opensrp.util.DateTimeTypeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

@Controller
@RequestMapping(value = "/rest/report/")
public class ReportResource {
	
	private static Logger logger = LoggerFactory.getLogger(ReportResource.class.toString());
	
	private ReportService reportService;
	
	Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
	        .registerTypeAdapter(DateTime.class, new DateTimeTypeConverter()).create();
	
	@Autowired
	public ReportResource(ReportService reportService) {
		this.reportService = reportService;
	}
	
	/**
	 * adding dhis2 reports to opensrp
	 * 
	 * @param report model payload
	 * @return report object
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(headers = { "Accept=application/json" }, method = POST, value = "/add")
	public ResponseEntity<HttpStatus> save(@RequestBody String data) {
		try {
			JSONObject syncData = new JSONObject(data);
			if (!syncData.has("reports")) {
				return new ResponseEntity<>(BAD_REQUEST);
			}
			ArrayList<Report> reports = (ArrayList<Report>) gson.fromJson(syncData.getString("reports"),
			    new TypeToken<ArrayList<Report>>() {}.getType());
			for (Report report : reports) {
				try {
					reportService.addorUpdateReport(report);
				}
				catch (Exception e) {
					logger.error("Report" + report.getId() + " failed to sync", e);
				}
			}
		}
		catch (Exception e) {
			logger.error(format("Sync data processing failed with exception {0}.- ", e));
			return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(CREATED);
	}
}
