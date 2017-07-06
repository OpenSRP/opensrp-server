package org.opensrp.form.service.it;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.util.DateUtil;
import org.opensrp.form.domain.FormSubmission;
import org.opensrp.form.repository.AllFormSubmissions;
import org.opensrp.form.repository.it.TestDatabaseConfig;
import org.opensrp.form.service.FormSubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext-opensrp-form.xml")
public class FormSubmissionServiceTest extends TestDatabaseConfig{
    @Autowired
    private AllFormSubmissions formSubmissions;
    private FormSubmissionService formSubmissionService;
    
    @Before
    public void setUp() throws Exception {
        formSubmissionService = new FormSubmissionService(formSubmissions);		 
    }
	 
    @SuppressWarnings("deprecation")
    @Test
    public void shouldFindByFormName(){
        long baseTimeStamp = DateUtil.now().getMillis();
        String provider = "ANM 6";
        FormSubmission firstFormSubmission = new FormSubmission("ANM 6", "instance id 77", "DemoForm77", "entity id 778", 0L, "1", null, baseTimeStamp);
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("formType", new String("type"));
        firstFormSubmission.setMetadata(metadata);
        formSubmissions.add(firstFormSubmission);        
        assertEquals(provider, formSubmissionService.findByFormName("DemoForm77", 0l).get(0).anmId());
        assertNotSame("er", formSubmissionService.findByFormName("DemoForm77", 0l).get(0).anmId());
    }
	
    @Test(expected=IndexOutOfBoundsException.class)
    public void shouldGetRuntimeExceptionForFindByFormName(){
        long baseTimeStamp = DateUtil.now().getMillis();
        String provider = "ANM 6";
        FormSubmission firstFormSubmission = new FormSubmission("ANM 6", "instance id 77", "DemoForm77", "entity id 778", 0L, "1", null, baseTimeStamp);
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("formType", new String("type"));
        firstFormSubmission.setMetadata(metadata);
        formSubmissions.add(firstFormSubmission);
        formSubmissionService.findByFormName("DemoForm77t", 0l).get(0).anmId();
    }
	
    @SuppressWarnings("deprecation")
    @Test
    public void shouldFindByInstanceId(){
        long baseTimeStamp = DateUtil.now().getMillis();
        String provider = "ANM 6";
        String formName = "DemoForm77";
        FormSubmission firstFormSubmission = new FormSubmission("ANM 6", "instance id 77", formName, "entity id 778", 0L, "1", null, baseTimeStamp);
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("formType", new String("type"));
        firstFormSubmission.setMetadata(metadata);
        formSubmissions.add(firstFormSubmission);        
        assertEquals(formName, formSubmissionService.findByInstanceId("instance id 77").formName());
        assertNotSame("fff", formSubmissionService.findByInstanceId("instance id 77").formName());
    }
	
    @Test
    public void shouldFindByMetadata(){
        long baseTimeStamp = DateUtil.now().getMillis();
        FormSubmission firstFormSubmission = new FormSubmission("ANM 2", "instance id 7", "DemoForm ff", "entity id 78", 0L, "1", null, baseTimeStamp);
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("formType", new String("type"));
        firstFormSubmission.setMetadata(metadata);
        formSubmissions.add(firstFormSubmission);
        assertEquals("type", formSubmissionService.findByMetadata("formType","type").get(0).getMetadata("formType"));
        assertNotSame("types", formSubmissionService.findByMetadata("formType","type").get(0).getMetadata("formType"));
    }
	 
    @Test
    public void shouldGetNewSubmissionsForANM(){
        long baseTimeStamp = DateUtil.now().getMillis();
        String provider = "ANM 5";
        FormSubmission firstFormSubmission = new FormSubmission(provider, "instance id 7", "DemoForm ff", "entity id 78", 0L, "1", null, baseTimeStamp);
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("formType", new String("type"));
        firstFormSubmission.setMetadata(metadata);
        formSubmissions.add(firstFormSubmission);
        assertEquals(provider, formSubmissionService.getNewSubmissionsForANM(provider,0l,1).get(0).anmId());
        assertNotSame("pro", formSubmissionService.getNewSubmissionsForANM(provider,0l,1).get(0).anmId());
    }

    @Test
    public void shouldGetAllFormSubmissions(){
        long baseTimeStamp = DateUtil.now().getMillis();
        FormSubmission firstFormSubmission = new FormSubmission("ANM 1", "instance id 1", "DemoForm Name", "entity id 1", 0L, "1", null, baseTimeStamp);
        formSubmissions.add(firstFormSubmission);
        assertEquals(1, formSubmissionService.getAllSubmissions(getStdCouchDbConnectorForOpensrpForm(),0L, 1).size());	    	
    }

}
