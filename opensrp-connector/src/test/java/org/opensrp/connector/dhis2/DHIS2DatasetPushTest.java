package org.opensrp.connector.dhis2;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.scheduler.domain.MotechEvent;
import org.opensrp.api.domain.Location;
import org.opensrp.connector.openmrs.service.OpenmrsLocationService;
import org.opensrp.connector.openmrs.service.TestResourceLoader;
import org.opensrp.domain.Hia2Indicator;
import org.opensrp.domain.Report;
import org.opensrp.service.ConfigService;
import org.opensrp.service.ReportService;

public class DHIS2DatasetPushTest extends TestResourceLoader {
	
	@Mock
	DHIS2DatasetPush mockDatasetPush;
	
	@Mock
	Dhis2HttpUtils dhis2HttpUtils;
	
	@Mock
	OpenmrsLocationService mockOpenmrsLocationService;
	
	@Mock
	ConfigService mockConfig;
	
	@Mock
	ReportService mockReportService;
	
	String orgUnitId = "gGl6WgM3qzS";
	
	String hia2ReportId = "XQDrq0oQEyN";
	
	public DHIS2DatasetPushTest() throws IOException {
		super();
		initMocks(this);
	}
	
	@Before
	public void setUp() throws JSONException {
		JSONObject apiResponse = new JSONObject();
		JSONObject pager = new JSONObject();
		
		pager.put("page", 1);
		pager.put("pageCount", 1);
		pager.put("total", 1);
		pager.put("pageSize", 50);
		
		JSONArray dataSets = new JSONArray();
		
		JSONObject hia2Report = new JSONObject();
		
		hia2Report.put("id", hia2ReportId);
		hia2Report.put("displayName", "HIA2");
		
		dataSets.put(hia2Report);
		
		apiResponse.put("pager", pager);
		apiResponse.put("dataSets", dataSets);
		
		when(dhis2HttpUtils.get(anyString(), anyString())).thenReturn(apiResponse);
		when(mockConfig.getAppStateTokenByName(DhisSchedulerConfig.dhis2_syncer_sync_report_by_date_updated))
		        .thenReturn(null);
		
		Location location = new Location();
		location.addAttribute("dhis_ou_id", orgUnitId);
		when(mockOpenmrsLocationService.getLocation(anyString())).thenReturn(location);
	}
	
	public Report createHIA2ReportData(List<Hia2Indicator> dataElements) throws JSONException {
		Report hia2Report = new Report("test", "9e4fc064-d8e7-4fcb-942e-cbcf6524fb24", new DateTime(2017, 05, 22, 0, 0),
		        "HIA2", "5f52c82f-ea29-469e-96d6-f95a6cc8fbe9", "biddemo", "", 1, 0, dataElements);
		
		return hia2Report;
	}
	
	@Test
	public void testGetDHIS2ReportId() throws JSONException {
		// Expected DHIS2 API response
		
		dhis2DatasetPush.dhis2HttpUtils = dhis2HttpUtils;
		String dhis2ReportId = dhis2DatasetPush.getDHIS2ReportId("HIA2");
		
		assertEquals(hia2ReportId, dhis2ReportId);
	}
	
	@Test
	public void testCreateDHIS2Dataset() throws JSONException {
		Hia2Indicator chn1005 = new Hia2Indicator("CHN1-005", "n0uHub5ubqH", "100");
		Hia2Indicator chn1010 = new Hia2Indicator("CHN1-010", "IWwblgpMxiS", "150");
		
		List<Hia2Indicator> dataElements = new ArrayList<Hia2Indicator>();
		
		dataElements.add(chn1005);
		dataElements.add(chn1010);
		
		Report report = this.createHIA2ReportData(dataElements);
		
		dhis2DatasetPush.dhis2HttpUtils = dhis2HttpUtils;
		dhis2DatasetPush.openmrsLocationService = this.mockOpenmrsLocationService;
		
		JSONObject dhis2DatasetToPush = dhis2DatasetPush.createDHIS2Dataset(report);
		
		// Dataset ID
		assertEquals(hia2ReportId, dhis2DatasetToPush.get("dataSet"));
		//completeDate
		assertEquals("2017-05-22", dhis2DatasetToPush.get("completeDate"));
		// period
		assertEquals("201705", dhis2DatasetToPush.get("period"));
		// orgUnit
		assertEquals(orgUnitId, dhis2DatasetToPush.get("orgUnit"));
		// dataValues
		JSONArray dataValues = dhis2DatasetToPush.getJSONArray("dataValues");
		//assertEquals(2, dataValues.length());
		
		for (int i = 0; i < dataValues.length(); i++) {
			JSONObject dataValue = dataValues.getJSONObject(i);
			Hia2Indicator dataElement = dataElements.get(i);
			
			assertEquals(dataValue.get("value"), dataElement.getValue());
		}
	}
	
	@Test
	public void testUnknownDataElementsAreIgnored() throws JSONException {
		Hia2Indicator chn1015 = new Hia2Indicator("CHN1-015", "unknown", "250");
		
		List<Hia2Indicator> dataElements = new ArrayList<Hia2Indicator>();
		
		dataElements.add(chn1015);
		
		Report report = this.createHIA2ReportData(dataElements);
		
		dhis2DatasetPush.dhis2HttpUtils = dhis2HttpUtils;
		dhis2DatasetPush.openmrsLocationService = this.mockOpenmrsLocationService;
		
		JSONObject dhis2DatasetToPush = dhis2DatasetPush.createDHIS2Dataset(report);
		
		// Dataset ID
		assertEquals(hia2ReportId, dhis2DatasetToPush.get("dataSet"));
		JSONArray dataValues = dhis2DatasetToPush.getJSONArray("dataValues");
		assertEquals(0, dataValues.length());
	}
	
	@Test
	public void testPushToDHIS2() throws JSONException {
		// Test Data setup
		MotechEvent event = new MotechEvent(DHIS2DatasetPush.SCHEDULER_DHIS2_DATA_PUSH_SUBJECT);
		JSONObject apiResponse = new JSONObject();
		apiResponse.put("status", "SUCCESS");
		
		Hia2Indicator chn1005 = new Hia2Indicator("CHN1-005", "n0uHub5ubqH", "100");
		Hia2Indicator chn1010 = new Hia2Indicator("CHN1-010", "IWwblgpMxiS", "150");
		
		List<Hia2Indicator> dataElements = new ArrayList<Hia2Indicator>();
		
		dataElements.add(chn1005);
		dataElements.add(chn1010);
		
		Report report = this.createHIA2ReportData(dataElements);
		
		List<Report> reports = new ArrayList<Report>();
		reports.add(report);
		
		// Customized mock api responses setup
		when(dhis2HttpUtils.post(anyString(), anyString(), anyString())).thenReturn(apiResponse);
		when(mockReportService.findByServerVersion(anyInt())).thenReturn(reports);
		
		dhis2DatasetPush.dhis2HttpUtils = dhis2HttpUtils;
		dhis2DatasetPush.openmrsLocationService = this.mockOpenmrsLocationService;
		dhis2DatasetPush.config = mockConfig;
		dhis2DatasetPush.reportService = mockReportService;
		
		// Test payload is synced with DHIS2
		dhis2DatasetPush.pushToDHIS2(event);
		
		verify(dhis2HttpUtils, times(1)).post(anyString(), anyString(), anyString());
		verify(mockReportService, times(1)).updateReport(report);
	}
	
}
