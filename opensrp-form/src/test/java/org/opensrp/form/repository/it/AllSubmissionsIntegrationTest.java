package org.opensrp.form.repository.it;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.util.DateUtil;
import org.opensrp.form.domain.FormSubmission;
import org.opensrp.form.repository.AllFormSubmissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static java.util.Arrays.asList;
import static junit.framework.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext-opensrp-form.xml")
public class AllSubmissionsIntegrationTest extends TestDatabaseConfig {
    @Autowired
    private AllFormSubmissions formSubmissions;

    @Before
    public void setUp() throws Exception {
        formSubmissions.removeAll();
    }

    @Test
    public void shouldCheckIfFormSubmissionExistsByInstanceId() throws Exception {
        FormSubmission formSubmission = new FormSubmission("anm id 1", "instance id 1", "form name 1", "entity id 1", 1L, "1", null, 0L);
        formSubmissions.add(formSubmission);

        assertTrue(formSubmissions.exists("instance id 1"));
        assertFalse(formSubmissions.exists("Invalid Instance Id"));
    }

    @Test
    public void shouldFetchAllFormSubmissionsAfterServerVersion() throws Exception {
        long baseTimeStamp = DateUtil.now().getMillis();

        FormSubmission firstFormSubmission = new FormSubmission("anm id 1", "instance id 1", "form name 1", "entity id 1", 0L, "1", null, baseTimeStamp);
        formSubmissions.add(firstFormSubmission);

        FormSubmission secondFormSubmission = new FormSubmission("anm id 2", "instance id 2", "form name 1", "entity id 2", 1L, "1", null, baseTimeStamp + 1);
        formSubmissions.add(secondFormSubmission);

        FormSubmission thirdFormSubmission = new FormSubmission("anm id 3", "instance id 3", "form name 1", "entity id 3", 2L, "1", null, baseTimeStamp + 2);
        formSubmissions.add(thirdFormSubmission);

        assertEquals(asList(firstFormSubmission, secondFormSubmission, thirdFormSubmission), formSubmissions.findByServerVersion(0L));
        assertEquals(asList(secondFormSubmission, thirdFormSubmission), formSubmissions.findByServerVersion(firstFormSubmission.serverVersion()));
        assertEquals(asList(thirdFormSubmission), formSubmissions.findByServerVersion(secondFormSubmission.serverVersion()));
        assertEquals(0, formSubmissions.findByServerVersion(thirdFormSubmission.serverVersion()).size());
    }

    @Test
    public void shouldFetchAllFormSubmissions() throws Exception {
        long baseTimeStamp = DateUtil.now().getMillis();

        FormSubmission firstFormSubmission = new FormSubmission("anm id 1", "instance id 1", "form name 1", "entity id 1", 0L, "1", null, baseTimeStamp);
        formSubmissions.add(firstFormSubmission);

        FormSubmission secondFormSubmission = new FormSubmission("anm id 2", "instance id 2", "form name 1", "entity id 2", 1L, "1", null, baseTimeStamp + 1);
        formSubmissions.add(secondFormSubmission);

        FormSubmission thirdFormSubmission = new FormSubmission("anm id 3", "instance id 3", "form name 1", "entity id 3", 2L, "1", null, baseTimeStamp + 2);
        formSubmissions.add(thirdFormSubmission);

        assertEquals(asList(firstFormSubmission, secondFormSubmission, thirdFormSubmission), formSubmissions.allFormSubmissions(0, 3));
        assertEquals(asList(firstFormSubmission, secondFormSubmission), formSubmissions.allFormSubmissions(0, 2));
        assertEquals(asList(firstFormSubmission, secondFormSubmission, thirdFormSubmission), formSubmissions.allFormSubmissions(0, 3));
        assertEquals(asList(secondFormSubmission, thirdFormSubmission), formSubmissions.allFormSubmissions(firstFormSubmission.serverVersion(), 3));

    }

    @Test
    public void shouldFetchFormSubmissionsBasedOnANMIDTimeStampAndBatchSize() throws Exception {
        long baseTimeStamp = DateUtil.now().getMillis();
        FormSubmission firstFormSubmission = new FormSubmission("ANM 1", "instance id 1", "form name 1", "entity id 1", 0L, "1", null, baseTimeStamp);
        formSubmissions.add(firstFormSubmission);

        FormSubmission secondFormSubmission = new FormSubmission("ANM 1", "instance id 2", "form name 1", "entity id 2", 1L, "1", null, baseTimeStamp + 1);
        formSubmissions.add(secondFormSubmission);

        FormSubmission thirdFormSubmission = new FormSubmission("ANM 1", "instance id 3", "form name 1", "entity id 3", 2L, "1", null, baseTimeStamp + 2);
        formSubmissions.add(thirdFormSubmission);

        assertEquals(asList(firstFormSubmission, secondFormSubmission, thirdFormSubmission), formSubmissions.findByANMIDAndServerVersion("ANM 1", 0L, null));
        assertEquals(asList(secondFormSubmission, thirdFormSubmission), formSubmissions.findByANMIDAndServerVersion("ANM 1", firstFormSubmission.serverVersion(), null));
        assertEquals(asList(thirdFormSubmission), formSubmissions.findByANMIDAndServerVersion("ANM 1", secondFormSubmission.serverVersion(), null));
        assertEquals(asList(firstFormSubmission, secondFormSubmission), formSubmissions.findByANMIDAndServerVersion("ANM 1", 0L, 2));

        assertEquals(0, formSubmissions.findByANMIDAndServerVersion("ANM 1", thirdFormSubmission.serverVersion(), null).size());
    }
    @Test
    public void shouldFindByFormName(){    	
    	 long baseTimeStamp = DateUtil.now().getMillis();
         FormSubmission firstFormSubmission = new FormSubmission("ANM 1", "instance id 1", "DemoForm", "entity id 1", 0L, "1", null, baseTimeStamp);
         formSubmissions.add(firstFormSubmission);
         assertEquals(asList(firstFormSubmission), formSubmissions.findByFormName("DemoForm", 0l));
         FormSubmission secondFormSubmission = new FormSubmission("ANM 1", "instance id 2", "form name 1", "entity id 2", 1L, "1", null, baseTimeStamp + 1);
         assertNotSame(asList(secondFormSubmission), formSubmissions.findByFormName("DemoForm", 0l));
    }
    @Test
    public void shouldGetAllFormSubmissions(){
    	 long baseTimeStamp = DateUtil.now().getMillis();
         FormSubmission firstFormSubmission = new FormSubmission("ANM 1", "instance id 1", "DemoForm Name", "entity id 1", 0L, "1", null, baseTimeStamp);
         formSubmissions.add(firstFormSubmission);
         assertEquals(asList(firstFormSubmission), formSubmissions.allFormSubmissions(getStdCouchDbConnectorForOpensrpForm(),0, 1));
         FormSubmission secondFormSubmission = new FormSubmission("ANM 1", "instance id 2", "form name 1", "entity id 2", 1L, "1", null, baseTimeStamp + 1);
         assertNotSame(asList(secondFormSubmission), formSubmissions.allFormSubmissions(getStdCouchDbConnectorForOpensrpForm(),0, 1));
    }
    @Test
    public void shouldFindByMetadata(){
    	 long baseTimeStamp = DateUtil.now().getMillis();
         FormSubmission firstFormSubmission = new FormSubmission("ANM 2", "instance id 7", "DemoForm ff", "entity id 78", 0L, "1", null, baseTimeStamp);
         Map<String, Object> metadata = new HashMap<>();
         metadata.put("formType", new String("type"));
         firstFormSubmission.setMetadata(metadata);
         formSubmissions.add(firstFormSubmission);
         assertEquals(asList(firstFormSubmission), formSubmissions.findByMetadata("formType","type"));
         FormSubmission secondFormSubmission = new FormSubmission("ANM 1", "instance id 2", "form name 1", "entity id 2", 1L, "1", null, baseTimeStamp + 1);
         assertNotSame(asList(secondFormSubmission), formSubmissions.findByMetadata("formType","type"));
    }
}
