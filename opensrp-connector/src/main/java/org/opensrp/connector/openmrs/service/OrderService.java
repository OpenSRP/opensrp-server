package org.opensrp.connector.openmrs.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.opensrp.domain.*;
import org.opensrp.connector.HttpUtil;
import org.opensrp.domain.Drug;
import org.opensrp.repository.AllDrugs;
import org.opensrp.repository.AllOrder;
import org.opensrp.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class OrderService extends OpenmrsService{
	private static final String ENCOUNTER_URL = "ws/rest/v1/encounter";//"ws/rest/emrapi/encounter";
	private static final String ENCOUNTER__TYPE_URL = "ws/rest/v1/encountertype";
	private static final String ORDER_URL = "ws/rest/v1/order";
	private static final String ORDER_TYPE_URL = "ws/rest/v1/ordertype";
	private static final String PATIENT_URL = "ws/rest/v1/patient";
	private static final String USER_URL = "ws/rest/v1/user";
	private static final String DRUG_URL = "ws/rest/v1/drug";
	public static final String OPENMRS_UUID_IDENTIFIER_TYPE = "OPENMRS_UUID";
	
	private PatientService patientService;
	private OpenmrsUserService userService;
	private ClientService clientService;

	@Autowired
	public OrderService(PatientService patientService, OpenmrsUserService userService, ClientService clientService) {
		this.patientService = patientService;
		this.userService = userService;
		this.clientService = clientService;
	}
	@Autowired
	public AllDrugs all_Drugs;
	
	@Autowired
	public AllOrder  all_DrugOrders;
	
	public OrderService(String openmrsUrl, String user, String password) {
    	super(openmrsUrl, user, password);
	}
	
	public OrderService() {
		// TODO Auto-generated constructor stub
	}
	public JSONObject getDrugByUuid(String uuid) throws JSONException
    {
    	return new JSONObject(HttpUtil.get(getURL()
    			+"/"+DRUG_URL+"/"+uuid, "v=full", OPENMRS_USER, OPENMRS_PWD).body());
    }
	
	public JSONArray getAllDrugFromOpenMRS() throws JSONException
    {
		JSONArray res = new JSONObject(HttpUtil.get(getURL()+"/"+DRUG_URL, "v=full", 
    			OPENMRS_USER, OPENMRS_PWD).body()).getJSONArray("results");
				return res;
    }
    
	public List <Drug> getAllDrugFromDB() throws JSONException
    {
		List<Drug> allTheDrugInDB = all_Drugs.getAll();	
		return allTheDrugInDB;
    }
	
	public JSONObject getDrugOrderforOpenMRS(String uuid) throws JSONException
	{
		JSONObject jsonObject=new JSONObject(HttpUtil.get(getURL()
    			+"/"+ORDER_URL+"/"+uuid, "v=full", OPENMRS_USER, OPENMRS_PWD).body());
		
		return jsonObject;
	}
	
	public JSONObject getPatientforOpenMRS(String uuid) throws JSONException
	{
		JSONObject jsonObject=new JSONObject(HttpUtil.get(getURL()
    			+"/"+PATIENT_URL+"/"+uuid, "v=full", OPENMRS_USER, OPENMRS_PWD).body());
		
		return jsonObject;
	}
	
	public JSONObject getOrdererforOpenMRS(String uuid) throws JSONException
	{
		JSONObject jsonObject=new JSONObject(HttpUtil.get(getURL()
    			+"/"+USER_URL+"/"+uuid, "v=full", OPENMRS_USER, OPENMRS_PWD).body());
		
		return jsonObject;
	}
	
	public JSONObject getDrugsforOpenMRS(String uuid) throws JSONException
	{
		JSONObject jsonObject=new JSONObject(HttpUtil.get(getURL()
    			+"/"+DRUG_URL+"/"+uuid, "v=full", OPENMRS_USER, OPENMRS_PWD).body());
		
		return jsonObject;
	}
	
	public List <DrugOrder> getAllDrugOrderFromDB() throws JSONException
    {
		List<DrugOrder> allDrugOrderInDB = all_DrugOrders.getAll();	
		return allDrugOrderInDB;
    }
	
	public ArrayList<Drug> getAlldrug() throws JSONException
	{
		JSONObject p;
		ArrayList<Drug>AllDrugs = new ArrayList<Drug>();
		JSONArray jsArray=getAllDrugFromOpenMRS();
		for(int i=0;i<jsArray.length();i++)
		{
		p=jsArray.getJSONObject(i);
		String drugUuid=p.getString("uuid");
		String drugName=p.getString("name");
		String concept=p.getString("concept");
		
		JSONObject jsonObj = new JSONObject(concept);
		String conceptName=jsonObj.getString("display");
		String conceptUuid=jsonObj.getString("uuid");
		
		Map<String, String> codes= new HashMap<>();
		codes.put("openmrs_uuid", drugUuid);
		codes.put("openmrs_concept_uuid", conceptUuid);
		codes.put("openmrs_route_uuid", conceptUuid);
		String strength=p.getString("strength");
		String mini=p.getString("minimumDailyDose");
		String max=p.getString("maximumDailyDose");
		String description=p.getString("description");
		String units=p.getString("units");
		String combination=p.getString("combination");
		
		JSONObject route=p.getJSONObject("dosageForm");
		String routeName=route.getString("display");
		Drug Drugs = new Drug(drugName,conceptName,codes,routeName,null,strength,units,max,mini,description,combination);
		AllDrugs.add(Drugs);
		}
		return AllDrugs;
	}
	
	public DrugOrder getAlldrugOrders(String uuid) throws JSONException
	{
		Map<String, String> map=new HashMap<String, String>();
		JSONObject DrugOrders= getDrugOrderforOpenMRS(uuid);
		System.out.println("\n "+DrugOrders);
		String drugName=DrugOrders.getString("display");
		String dosingType=DrugOrders.getString("dosingType");
		String dosingInstruction=DrugOrders.getString("dosingInstructions");
		String orderNumber=DrugOrders.getString("orderNumber");
		String dateActivated=DrugOrders.getString("dateActivated");
		String dateStopped=DrugOrders.getString("dateStopped");
		String action=DrugOrders.getString("action");
		String quantity=DrugOrders.getString("quantity");
		String dose=DrugOrders.getString("dose");
		String type=DrugOrders.getString("type");
		String urgency=DrugOrders.getString("urgency");
		map.put("openMRSUuid", DrugOrders.getString("uuid"));
		JSONObject p=DrugOrders.getJSONObject("patient");
		JSONObject temp= getPatientforOpenMRS(p.getString("uuid"));
		System.out.println("\n"+temp);
		String baseEntityId=temp.getJSONArray("identifiers").getJSONObject(0).getString("uuid");
		map.put("openMRSUuidPatient", p.getString("uuid"));
		p=DrugOrders.getJSONObject("concept");
		map.put("openMRSUuidConcept", p.getString("uuid"));
		p=DrugOrders.getJSONObject("careSetting");
		map.put("openMRSUuidCareSetting", p.getString("uuid"));
		p=DrugOrders.getJSONObject("orderer");
		String ordererName=p.getString("display");
		map.put("openMRSUuidOrderer", p.getString("uuid"));
		p=DrugOrders.getJSONObject("drug");
		temp= getDrugsforOpenMRS(p.getString("uuid"));
		drugName=temp.getString("display");
		map.put("openMRSUuidDrug", p.getString("uuid"));
		p=DrugOrders.getJSONObject("doseUnits");
		map.put("openMRSUuidDoseUnits", p.getString("uuid"));
		p=DrugOrders.getJSONObject("route");
		String route=p.getString("display");
		map.put("openMRSUuidRoute", p.getString("uuid"));
		p=DrugOrders.getJSONObject("quantityUnits");
		String quantityUnits=p.getString("display");;
		DrugOrder drugOrders=new DrugOrder(baseEntityId,ordererName,drugName, null, map, orderNumber, action, null, dateActivated, dateStopped, null, urgency, dosingInstruction, dosingType,null, quantity,route,quantityUnits);
		System.out.println("Results: "+ drugOrders);
		return drugOrders;
	}
	
	
	public JSONObject getDrug(String uuid) throws JSONException
	{
		JSONObject jsonObject=new JSONObject(HttpUtil.get(getURL()
    			+"/"+DRUG_URL+"/"+uuid, "v=full", OPENMRS_USER, OPENMRS_PWD).body());
		Drug d=new Drug(uuid);
		return jsonObject;
	}
}
