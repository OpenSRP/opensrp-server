package org.opensrp.connector.dhis2;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.opensrp.domain.Event;
import org.opensrp.domain.Obs;
import org.opensrp.repository.AllEvents;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*import static org.opensrp.common.AllConstants.DHIS2.*;
*/
@Service
public class DHIS2AggregateConnector extends DHIS2Service {
	
	@Autowired
	private AllEvents allEvents;
	
	public DHIS2AggregateConnector() {
		
	}
	
	public DHIS2AggregateConnector(String dhis2Url, String user, String password) {
		super(dhis2Url, user, password);
	}
	
	public JSONObject getAggregatedDataCount() throws JSONException {
		Date date = new Date();
		String modifiedDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
		Calendar now = Calendar.getInstance();
		now.add(Calendar.MONTH, -1);
		int year = now.get(Calendar.YEAR);
		int month = now.get(Calendar.MONTH) + 1;
		int length = (int) (Math.log10(month) + 1);
		String formatted;
		
		if (length < 2) {
			formatted = String.format("%02d", month);
		} else {
			formatted = Integer.toString(month);
		}
		
		String periodTime = Integer.toString(year) + formatted;
		Integer birthPlaceInHealthFacilityCount = 0;
		Integer birthPlaceInHome = 0;
		Integer birthUnderWeightCount = 0;
		Integer brtc = 0;
		
		List<Event> eventList = allEvents.findEventByEventTypeBetweenTwoDates("Birth Registration");
		if (eventList.isEmpty()) {
			System.out.println("Empty:Data");
		} else {
			for (Event event : eventList) {
				List<Obs> obs = event.getObs();
				if (!obs.isEmpty()) {
					for (Obs obs2 : obs) {
						if (obs2.getFormSubmissionField() != null
						        && obs2.getFormSubmissionField().equalsIgnoreCase("Place_Birth")) {
							List<Object> values = obs2.getHumanReadableValues();
							if (!values.isEmpty()) {
								for (Object object : values) {
									if (object.toString().equalsIgnoreCase("Health facility")) {
										birthPlaceInHealthFacilityCount++;
									} else if (object.toString().equalsIgnoreCase("Home")) {
										birthPlaceInHome++;
									} else {
										
									}
								}
							}
							
						}
						
						if (obs2.getFormSubmissionField() != null
						        && obs2.getFormSubmissionField().equalsIgnoreCase("Birth_Weight")) {
							List<Object> values = obs2.getValues();
							if (!values.isEmpty()) {
								for (Object object : values) {
									try {
										Double value = Double.parseDouble((String) object);
										double conditionValue = value;
										System.out.println("conditionValue:" + conditionValue);
										if (conditionValue < 3.5) {
											birthUnderWeightCount++;
										} else {
											
										}
									}
									catch (Exception e) {
										System.out.println("Birth_Weight Message:" + e.getMessage());
									}
								}
							}
						}
						
					}
				}
				brtc++;
			}
		}
		
		JSONArray eventDataValues = new JSONArray();
		
		/**
		 * Count for Birth place of Health_Facility of current month
		 */
		
		JSONObject birhtPlaceInHealthFacility = new JSONObject();
		birhtPlaceInHealthFacility.put("dataElement", "ii7lOGQqEq5");
		birhtPlaceInHealthFacility.put("value", birthPlaceInHealthFacilityCount);
		/**
		 * Count for Birth place of Home of current month
		 */
		JSONObject birhtPlaceInHome = new JSONObject();
		birhtPlaceInHome.put("dataElement", "yNWOJ0OOOQD");
		birhtPlaceInHome.put("value", birthPlaceInHome);
		
		/**
		 * Count Birth Under WeightCount of current month
		 */
		JSONObject birthUnderWeight = new JSONObject();
		birthUnderWeight.put("dataElement", "Wtf7iSiQdUJ");
		birthUnderWeight.put("value", birthUnderWeightCount);
		
		/**
		 * Count for Total new birth registrations of current month
		 */
		JSONObject brtcObject = new JSONObject();
		brtcObject.put("dataElement", "xMlVHstzOgC");
		brtcObject.put("value", brtc);
		
		eventDataValues.put(brtcObject);
		eventDataValues.put(birhtPlaceInHome);
		eventDataValues.put(birhtPlaceInHealthFacility);
		eventDataValues.put(birthUnderWeight);
		/**
		 * Vaccination data count start form here
		 **/
		
		int opv_0 = 0;
		int opv_1 = 0;
		int opv_2 = 0;
		int bcg = 0;
		int pcv_1 = 0;
		int pcv_2 = 0;
		int penta_1 = 0;
		int rota_1 = 0;
		int rota_2 = 0;
		int penta_2 = 0;
		
		List<Event> vaccinetList = allEvents.findEventByEventTypeBetweenTwoDates("Vaccination");
		if (vaccinetList.isEmpty()) {
			System.out.println("Empty:Data");
		} else {
			for (Event event : vaccinetList) {
				List<Obs> obs = event.getObs();
				if (!obs.isEmpty()) {
					for (Obs obs2 : obs) {
						
						if (obs2.getFormSubmissionField() != null && obs2.getFormSubmissionField().equalsIgnoreCase("opv_0")) {
							opv_0++;
						} else if (obs2.getFormSubmissionField() != null
						        && obs2.getFormSubmissionField().equalsIgnoreCase("opv_1")) {
							opv_1++;
						} else if (obs2.getFormSubmissionField() != null
						        && obs2.getFormSubmissionField().equalsIgnoreCase("opv_2")) {
							opv_2++;
						} else if (obs2.getFormSubmissionField() != null
						        && obs2.getFormSubmissionField().equalsIgnoreCase("bcg")) {
							bcg++;
						} else if (obs2.getFormSubmissionField() != null
						        && obs2.getFormSubmissionField().equalsIgnoreCase("pcv_1")) {
							pcv_1++;
						} else if (obs2.getFormSubmissionField() != null
						        && obs2.getFormSubmissionField().equalsIgnoreCase("pcv_2")) {
							pcv_2++;
						} else if (obs2.getFormSubmissionField() != null
						        && obs2.getFormSubmissionField().equalsIgnoreCase("penta_1")) {
							penta_1++;
						} else if (obs2.getFormSubmissionField() != null
						        && obs2.getFormSubmissionField().equalsIgnoreCase("penta_2")) {
							penta_2++;
						} else if (obs2.getFormSubmissionField() != null
						        && obs2.getFormSubmissionField().equalsIgnoreCase("rota_1")) {
							rota_1++;
						} else if (obs2.getFormSubmissionField() != null
						        && obs2.getFormSubmissionField().equalsIgnoreCase("rota_2")) {
							rota_2++;
						} else {
							
						}
					}
				}
			}
		}
		
		/**
		 * Count for Total opv_0 of current month
		 */
		JSONObject opv_0Object = new JSONObject();
		opv_0Object.put("dataElement", "XYqYdPiapTB");
		opv_0Object.put("value", opv_0);
		eventDataValues.put(opv_0Object);
		
		/**
		 * Count for Total opv_1 of current month
		 */
		JSONObject opv_1Object = new JSONObject();
		opv_1Object.put("dataElement", "ghHOqHNST3Z");
		opv_1Object.put("value", opv_1);
		eventDataValues.put(opv_1Object);
		
		/**
		 * Count for Total opv_2 of current month
		 */
		JSONObject opv_2Object = new JSONObject();
		opv_2Object.put("dataElement", "jY9SUZVxPHZ");
		opv_2Object.put("value", opv_2);
		eventDataValues.put(opv_2Object);
		
		/**
		 * Count for Total bcg of current month
		 */
		JSONObject bcgObject = new JSONObject();
		bcgObject.put("dataElement", "MNe2NbiMPi4");
		bcgObject.put("value", bcg);
		eventDataValues.put(bcgObject);
		
		/**
		 * Count for Total pcv_1 of current month
		 */
		JSONObject pcv_1Object = new JSONObject();
		pcv_1Object.put("dataElement", "DF4I78hJCyE");
		pcv_1Object.put("value", pcv_1);
		eventDataValues.put(pcv_1Object);
		
		/**
		 * Count for Total pcv_2 of current month
		 */
		JSONObject pcv_2Object = new JSONObject();
		pcv_2Object.put("dataElement", "IMh3lVLICJM");
		pcv_2Object.put("value", pcv_2);
		eventDataValues.put(pcv_2Object);
		
		/**
		 * Count for Total penta_1 of current month
		 */
		JSONObject penta_1Object = new JSONObject();
		penta_1Object.put("dataElement", "dYqIehgysyx");
		penta_1Object.put("value", penta_1);
		eventDataValues.put(penta_1Object);
		
		/**
		 * Count for Total penta_2 of current month
		 */
		JSONObject penta_2Object = new JSONObject();
		penta_2Object.put("dataElement", "belqjUALCbL");
		penta_2Object.put("value", penta_2);
		eventDataValues.put(penta_2Object);
		
		/**
		 * Count for Total rota_1 of current month
		 */
		JSONObject rota_1Object = new JSONObject();
		rota_1Object.put("dataElement", "MR1zrXS829u");
		rota_1Object.put("value", rota_1);
		eventDataValues.put(rota_1Object);
		
		/**
		 * Count for Total rota_2 of current month
		 */
		JSONObject rota_2Object = new JSONObject();
		rota_2Object.put("dataElement", "dxH32jHc21V");
		rota_2Object.put("value", rota_2);
		eventDataValues.put(rota_2Object);
		
		JSONObject eventDataSet = new JSONObject();
		eventDataSet.put("dataSet", "fDoHorjO5Sr");
		eventDataSet.put("completeData", modifiedDate);
		eventDataSet.put("period", periodTime);
		eventDataSet.put("orgUnit", "IDc0HEyjhvL");
		eventDataSet.put("dataValues", eventDataValues);
		return eventDataSet;
		
	}
	
	public JSONObject aggredateDataSendToDHIS2(JSONObject aggregateData) throws JSONException {
		return new JSONObject(Dhis2HttpUtils.post(DHIS2_BASE_URL.replaceAll("\\s+", "") + "dataValueSets", "",
		    aggregateData.toString(), DHIS2_USER.replaceAll("\\s+", ""), DHIS2_PWD.replaceAll("\\s+", "")).body());
	}
}
