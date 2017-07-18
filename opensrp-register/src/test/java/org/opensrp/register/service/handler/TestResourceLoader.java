package org.opensrp.register.service.handler;

import static org.mockito.MockitoAnnotations.initMocks;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.junit.Before;
import org.motechproject.scheduletracking.api.repository.AllSchedules;
import org.opensrp.domain.Client;
import org.opensrp.domain.Event;
import org.opensrp.domain.Obs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;


public class TestResourceLoader {
    public String entityId = "entityId1";
    public String scheduleName = "opv 1";
    public String provider = "anm";
    public String eventId = "eventID 1";
    public String caseId = "caseId";
    public String milestone = "milestone";
    @Autowired
    private AllSchedules allSchedules;	
    
	
    public String getFile() throws IOException {
        ResourceLoader loader = new DefaultResourceLoader();
        String scheduleConfigFilesPath = "./../assets/schedules/schedule-configs";
        File scheduleConfigsFolder = null;
        if (scheduleConfigsFolder == null && loader.getResource(scheduleConfigFilesPath).exists())
            scheduleConfigFilesPath = loader.getResource(scheduleConfigFilesPath).getURI().getPath();
        scheduleConfigsFolder = new File(scheduleConfigFilesPath);
        String scheduleConfigMapping = "";
        File[] scheduleFiles = scheduleConfigsFolder.listFiles();
        for (int i = 0; i < scheduleFiles.length; i++) {
            final File fileEntry = scheduleFiles[i];
            String scheduleConfig = FileUtils.readFileToString(new File(fileEntry.getAbsolutePath()), "UTF-8");
            scheduleConfigMapping += (i + 1 == scheduleFiles.length) ? scheduleConfig : scheduleConfig.concat(",");			
        }		
        return scheduleConfigMapping;
    }   
    
		
    public List<String> jsonArrayToList(JSONArray jsonArray) throws JSONException {
        List<String> values = new ArrayList<String>();
        if (jsonArray == null) {
            return values;
        }		
        for (int i = 0; i < jsonArray.length(); i++) {
            values.add((String) jsonArray.get(i));
        }
        return values;
    }
    
    public List<Event> getEvents(String opv3Date) {
        List<Event> events = new ArrayList<>();
        Event eventObj1 = new Event();
        eventObj1.setBaseEntityId("ooo-yyy-yyy");
        eventObj1.setEventType("Vaccination");
        eventObj1.setProviderId("anm");
       
        List<Obs> observations1 = new ArrayList<>();       
        Obs obs1 = new Obs();
        obs1.setFieldCode("1418AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        obs1.setFieldDataType("calculate");
        obs1.setFieldType("concept");
        obs1.setParentCode("783AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        obs1.setFormSubmissionField("opv1_dose");        
        List<Object> value = new ArrayList<>();
        value.add("1");
        obs1.setValues(value);        
        observations1.add(obs1);
        
        Obs obs2 = new Obs();
        obs2.setFieldCode("1410AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        obs2.setFieldDataType("date");
        obs2.setFieldType("concept");
        obs2.setParentCode("783AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        obs2.setFormSubmissionField("opv1_retro");        
        List<Object> value2 = new ArrayList<>();
        value2.add("2015-12-16");
        obs2.setValues(value);
        observations1.add(obs2);
        eventObj1.setObs(observations1);        
        
        Event eventObj2 = new Event();
        eventObj2.setBaseEntityId("ooo-yyy-yyy");
        eventObj2.setEventType("Vaccination");
        eventObj2.setProviderId("anm");
        List<Obs> observations2 = new ArrayList<>();
        
        
        Obs obs3 = new Obs();
        obs3.setFieldCode("1418AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        obs3.setFieldDataType("calculate");
        obs3.setFieldType("concept");
        obs3.setParentCode("783AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        obs3.setFormSubmissionField("opv0_dose");        
        List<Object> value3 = new ArrayList<>();
        value3.add("0");
        obs3.setValues(value3);        
        observations2.add(obs3);
        
        Obs obs4 = new Obs();
        obs4.setFieldCode("1410AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        obs4.setFieldDataType("date");
        obs4.setFieldType("concept");
        obs4.setParentCode("783AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        obs4.setFormSubmissionField("opv0_retro");        
        List<Object> value4 = new ArrayList<>();
        value4.add("2013-12-16");
        obs4.setValues(value4);
        observations2.add(obs4);        
        
        eventObj2.setObs(observations2);
        
       
        Event eventObj3 = new Event();
        eventObj3.setBaseEntityId("ooo-yyy-yyy");
        eventObj3.setEventType("Vaccination");
        eventObj3.setProviderId("anm");
        List<Obs> observations3 = new ArrayList<>();
        
        
        Obs obs5 = new Obs();
        obs5.setFieldCode("1418AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        obs5.setFieldDataType("calculate");
        obs5.setFieldType("concept");
        obs5.setParentCode("783AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        obs5.setFormSubmissionField("opv3_dose");        
        List<Object> value5 = new ArrayList<>();
        value5.add("3");
        obs5.setValues(value5);        
        observations3.add(obs5);
        
        Obs obs6 = new Obs();
        obs6.setFieldCode("1410AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        obs6.setFieldDataType("date");
        obs6.setFieldType("concept");
        obs6.setParentCode("783AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        obs6.setFormSubmissionField("opv3_retro");        
        List<Object> value6 = new ArrayList<>();
        value6.add(opv3Date);
        obs6.setValues(value6);
        observations3.add(obs6);        
        
        eventObj3.setObs(observations3);      
        
        events.add(eventObj1); 
        events.add(eventObj2);
        events.add(eventObj3);
        return events;
    }
    public Event geteventOfVaccination() throws IOException {
        String baseEntityId = "ooo-yyy-yyy";
        String eventType = "Vaccination";        
        DateTime eventDate = new DateTime();
        String entityType = "";
        String providerId = "anm";
        String locationId = "";
        String formSubmissionId = "";
        Event event = new Event(baseEntityId, eventType, eventDate, entityType, providerId, locationId, formSubmissionId);
        event.setId("23456");
        event.setDateCreated(new DateTime("2017-02-02"));
        String scheduleName = "VIT A 1";
        String schedulesStr = getFile();
        List<Object> values = new ArrayList<>();
        values.add("2016-07-10");
        Obs observation1  = new Obs("concept", "date", "1410AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "783AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", values, "", "");
        List<Obs> obs = new ArrayList<>();
        obs.add(observation1);		
        Client client = new Client("ooo-yyy-yyy", "hmmm", "hummm", "lssssss", new DateTime("1995-12-28T00:00:00.000Z"), new DateTime(), true, true, "Female", "", "");
        List<Object> values1 = new ArrayList<>();
        values1.add("2017-06-08 09:33:39");		
        Obs observation2  = new Obs("client", "birthdate", "163137AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "783AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", values1, "", "birthdate");
        event.addObs(observation1);
        event.addObs(observation2);
        return event;
    }
    
    public Event geteventOfBirthRegistration() throws IOException {
        String baseEntityId = "ooo-yyy-yyy";        
        String eventType = "Birth Registration";
        DateTime eventDate = new DateTime();
        String entityType = "";
        String providerId = "anm";
        String locationId = "";
        String formSubmissionId = "";
        Event event = new Event(baseEntityId, eventType, eventDate, entityType, providerId, locationId, formSubmissionId);
        event.setId("23456");
        event.setDateCreated(new DateTime("2017-02-02"));
        String scheduleName = "VIT A 1";
        String schedulesStr = getFile();
        List<Object> values = new ArrayList<>();
        values.add("2016-07-10");
        Obs observation1  = new Obs("concept", "date", "1410AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "783AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", values, "", "");
        List<Obs> obs = new ArrayList<>();
        obs.add(observation1);		
        Client client = new Client("ooo-yyy-yyy", "hmmm", "hummm", "lssssss", new DateTime("1995-12-28T00:00:00.000Z"), new DateTime(), true, true, "Female", "", "");
        List<Object> values1 = new ArrayList<>();
        values1.add("2017-06-08 09:33:39");		
        Obs observation2  = new Obs("client", "birthdate", "163137AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "783AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", values1, "", "birthdate");
        event.addObs(observation1);
        event.addObs(observation2);
        return event;
    }
    public List<Client> getClients() {
    	List<Client> clients = new ArrayList<>();
    	Client client = new Client("ooo-yyy-yyy");
    	client.setFirstName("Client");
    	client.setId("2345");
    	clients.add(client);
    	client.setBirthdate(new DateTime());
    	return clients;
    }
    
}
