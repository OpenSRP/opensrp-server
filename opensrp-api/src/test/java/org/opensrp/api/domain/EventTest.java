package org.opensrp.api.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.joda.time.DateTime;
import org.junit.Test;

public class EventTest {
	
    @Test
    public void shouldTestEvent(){
        Event firstEvent = new Event();
        firstEvent.withLocationId("locationId2");
        firstEvent.withEventDate(new DateTime());
        firstEvent.withEntityType("entityType");
        firstEvent.withFormSubmissionId("formSubmissionId");
        firstEvent.withEventType("eventType");
        firstEvent.withProviderId("providerId");
        firstEvent.withIdentifier("ff", "dd");
        firstEvent.getIdentifiers();
        firstEvent.getIdentifier("");
        firstEvent.getIdentifierMatchingRegex("");
        Event secondEvent = new Event("baseEntityId", "eventType", new DateTime(), "entityType", "providerId", "locationId", "formSubmissionId");
        firstEvent.setBaseEntityId("baseEntityId");
        Assert.assertEquals(firstEvent.getBaseEntityId(), "baseEntityId");
        firstEvent.withBaseEntityId("baseEntityId");
        firstEvent.setEntityType("entityType");
        Assert.assertEquals(firstEvent.getEntityType(), "entityType");
        firstEvent.setEventDate(new DateTime());
//        Assert.assertEquals(firstEvent.getEventDate(), new DateTime());
        firstEvent.setEventType("eventType");
        Assert.assertEquals(firstEvent.getEventType(),"eventType");
        firstEvent.setProviderId("providerId");
        Assert.assertEquals(firstEvent.getProviderId(),"providerId");
        Obs obs = new Obs();
        obs.setFieldCode("1418AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        obs.setFieldDataType("calculate");
        obs.setFieldType("concept");
        obs.setParentCode("783AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        obs.setFormSubmissionField("opv1_dose");        
        List<Object> value = new ArrayList<>();
        value.add("1");
        obs.setValues(value); 
        List<Obs> observations = new ArrayList<>();
        firstEvent.withObs(obs);
        observations.add(obs);
        firstEvent.withObs(observations);
        
        firstEvent.setObs(null);
        firstEvent.addObs(obs);
        
        firstEvent.setObs(observations);
        Assert.assertEquals(firstEvent.getObs(), observations);
        
        Map<String, String> identifiers = new HashMap<>();
        identifiers.put("identifier1", "identifier");
        
        firstEvent.withIdentifier("identifier2", "identifier2");
        firstEvent.setIdentifiers(identifiers);
        Assert.assertEquals(firstEvent.getIdentifiers(), identifiers);
        Assert.assertEquals(firstEvent.getIdentifier("identifier1"), "identifier");        
        Assert.assertEquals(firstEvent.getIdentifierMatchingRegex("identifier1"), "identifier");
        firstEvent.setIdentifiers(null);
        firstEvent.addIdentifier("identifierType", "identifier");
        firstEvent.removeIdentifier("identifierType"); 
        firstEvent.withIdentifiers(identifiers);
        Assert.assertNull(firstEvent.getIdentifier("identifierType"));
        firstEvent.setLocationId("locationId");
        Assert.assertEquals(firstEvent.getLocationId(), "locationId");
        firstEvent.setFormSubmissionId("formSubmissionId");
        Assert.assertEquals(firstEvent.getFormSubmissionId(), "formSubmissionId");
        firstEvent.addDetails("key1", "value1");
        
        Map<String, String> details = new HashMap<>();
        details.put("key", "value");
        firstEvent.setDetails(details);
        Assert.assertEquals(firstEvent.getDetails(), details);        
        firstEvent.setVersion(0L);
        Assert.assertEquals(firstEvent.getVersion(), 0l);       
        
    }
    
    @Test
    public void shouldTestParentClassBaseObject(){
        Event event = new Event();
        User user = new User("oooo-r34444-jgu45");
        User user1 = new User("oooo-r34444-jgu45");
        event.setCreator(user);
        Assert.assertEquals(user, event.getCreator());
        Assert.assertNotSame(user1, event.getCreator());
        
        user.setDateCreated(new Date());
        Assert.assertEquals(new Date(), user.getDateCreated());
        
        User editor = new User("oooo-r34444-jgu45");
        editor.setEditor(editor);
        user.setEditor(editor);
        User editor1 = new User("oooo-r34444-jgu45");
        Assert.assertEquals(editor, user.getEditor());
        Assert.assertNotSame(editor1, user.getEditor());
        
        event.setDateEdited(new Date());
        Assert.assertEquals(new Date(), event.getDateEdited());
        Date dt = new Date();
        Calendar c = Calendar.getInstance(); 
        c.setTime(dt); 
        c.add(Calendar.DATE, 1);
        dt = c.getTime();        
        Assert.assertNotSame(dt, event.getDateEdited());
        
        event.setVoided(true);
        Assert.assertTrue(event.getVoided());
        Assert.assertFalse(!event.getVoided());
        
        event.setDateVoided(new Date());
        Assert.assertEquals(new Date(), event.getDateVoided());
        
        event.setVoider(user);
        Assert.assertEquals(user, event.getVoider());
        Assert.assertNotSame(user1, event.getVoider());
        
        event.setVoidReason("fake data");
        Assert.assertEquals("fake data", event.getVoidReason());
        Assert.assertNotSame("fakes data", event.getVoidReason());
        
        event.withCreator(user);
        Assert.assertEquals(user, event.getCreator());
        Assert.assertNotSame(user1, event.getCreator());
        
        event.withDateCreated(new Date());
        event.withEditor(user);
        Assert.assertEquals(editor, user.getEditor());
        Assert.assertNotSame(editor1, user.getEditor());
        event.withDateEdited(new Date());
        event.withVoided(true);
        Assert.assertTrue(event.getVoided());
        Assert.assertFalse(!event.getVoided());
        
        event.withDateVoided(new Date());
        event.withVoider(editor);
        Assert.assertEquals(editor, user.getEditor());
        Assert.assertNotSame(editor1, user.getEditor());
        event.withVoidReason("fake data");
        Assert.assertEquals("fake data", event.getVoidReason());
        Assert.assertNotSame("fakes data", event.getVoidReason());
        
    	
    }

}
