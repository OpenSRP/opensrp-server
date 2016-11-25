
package org.opensrp.connector.openmrs.service;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.opensrp.domain.RelationShip;
import org.hamcrest.Matchers;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.opensrp.domain.*;
import org.opensrp.repository.AllDrugs;
import org.opensrp.connector.openmrs.constants.OpenmrsHouseHold;
import org.opensrp.form.domain.FormSubmission;
import org.opensrp.form.service.FormAttributeParser;
import org.opensrp.service.formSubmission.FormEntityConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.Log4jNestedDiagnosticContextFilter;

import com.google.gson.JsonIOException;
import com.mysql.jdbc.log.Log;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.opensrp.repository.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-applicationContext-opensrp.xml")
public class OrderTest extends TestResourceLoader{
	public OrderTest() throws IOException {
		super();
	}
	private final Logger log = Logger.getLogger(getClass().getSimpleName());
	OrderService os;
	EncounterService es;
	FormEntityConverter oc;
	PatientService ps;
	OpenmrsUserService us;
	HouseholdService hhs;

	SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
	
	@Before
	public void setup() throws IOException{
		openmrsOpenmrsUrl="https://endtbtest.irdresearch.org/openmrs/";
		openmrsUsername="superman";
		openmrsPassword="Admin123";
		ps = new PatientService(openmrsOpenmrsUrl, openmrsUsername, openmrsPassword);
		us = new OpenmrsUserService(openmrsOpenmrsUrl, openmrsUsername, openmrsPassword);
		es = new EncounterService(openmrsOpenmrsUrl, openmrsUsername, openmrsPassword);
		os = new OrderService(openmrsOpenmrsUrl, openmrsUsername, openmrsPassword);
		es.setPatientService(ps);
		es.setUserService(us);
		hhs = new HouseholdService(openmrsOpenmrsUrl, openmrsUsername, openmrsPassword);
		hhs.setPatientService(ps);
		hhs.setEncounterService(es);
		final FormAttributeParser fam = new FormAttributeParser(formDirPath);
		oc = new FormEntityConverter(fam);
	}
	
	@Autowired
    AllDrugs allDrugs;
	@Autowired
    AllOrder allDrugsOrders;
	
    
    @Test
	public void drugTest() throws JSONException, ParseException, IOException {
		if(pushToOpenmrsForTest){
			allDrugs.removeAll();
			ArrayList<Drug> Drugs=os.getAlldrug();
			for(int i=0;i<Drugs.size();i++)
			allDrugs.add(Drugs.get(i));
			}
			List<Drug> allDrugInDB = allDrugs.getAll();
	        assertEquals(41, allDrugInDB.size());
    }
    
    @Test
	public void drugOrderTest() throws JSONException, ParseException, IOException {
		String uuid="17b2418b-3e8d-4820-a515-cbd1c1cf3221";
    	if(pushToOpenmrsForTest){
    		allDrugsOrders.removeAll();
			DrugOrder DrugOrders=os.getAlldrugOrders(uuid);
			allDrugsOrders.add(DrugOrders);
			List<DrugOrder> allDrugsOrdersInDB = allDrugsOrders.getAll();
			assertEquals(1, allDrugsOrdersInDB.size());
    }
  }
}