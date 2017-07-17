package org.opensrp.repository.it;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.ektorp.CouchDbConnector;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.motechproject.model.MotechBaseDataObject;
import org.motechproject.scheduletracking.api.domain.Enrollment;
import org.motechproject.scheduletracking.api.domain.EnrollmentStatus;
import org.motechproject.scheduletracking.api.domain.WindowName;
import org.motechproject.scheduletracking.api.repository.AllEnrollments;
import org.opensrp.api.domain.Client;
import org.opensrp.common.AllConstants;
import org.opensrp.domain.Event;
import org.opensrp.form.domain.FormSubmission;
import org.opensrp.form.service.FormSubmissionService;
import org.opensrp.repository.AllClients;
import org.opensrp.repository.AllEvents;
import org.opensrp.scheduler.Action;
import org.opensrp.scheduler.Alert;
import org.opensrp.scheduler.AlertCreationAction;
import org.opensrp.scheduler.HealthSchedulerService;
import org.opensrp.scheduler.repository.AllActions;
import org.opensrp.scheduler.repository.AllAlerts;
import org.opensrp.service.ClientService;
import org.opensrp.service.EventService;
import org.opensrp.service.formSubmission.FormEntityConverter;
import org.opensrp.service.formSubmission.FormSubmissionProcessor;
import org.opensrp.service.formSubmission.handler.CustomFormSubmissionHandler;
import org.opensrp.service.formSubmission.handler.FormSubmissionRouter;
import org.opensrp.service.formSubmission.handler.HandlerMapper;
import org.opensrp.service.formSubmission.ziggy.EntityDataMap;
import org.opensrp.service.formSubmission.ziggy.ZiggyService;
import org.opensrp.util.ScheduleBuilder;
import org.opensrp.util.TestResourceLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-applicationContext-opensrp.xml")
public class FormLifeCycleTest extends TestResourceLoader{
	public FormLifeCycleTest() throws IOException {
		super();
	}
	
	private List<MotechBaseDataObject> docsToRemove = new ArrayList<>();

	@Autowired
	@Qualifier(AllConstants.OPENSRP_DATABASE_CONNECTOR) 
	private CouchDbConnector db;
	@Autowired
	private FormSubmissionProcessor fsp;
	@Autowired
	private ZiggyService ziggyService;
	@Autowired
	private FormSubmissionRouter formSubmissionRouter;
	@Autowired
	private FormEntityConverter formEntityConverter;
	/*@Autowired
	private ActionService actionService;
	@Autowired
	private ScheduleService schService;*/
	@Autowired
	private HealthSchedulerService scheduleService;
	@Autowired
	private ClientService clientService;
	@Autowired
	private EventService eventService;
	@Autowired
	private HandlerMapper hmap;
	@Autowired
	private EntityDataMap edmap;

	@Mock
	private FormSubmissionService formSubmissionService;
	
    private AlertCreationAction reminderAction;
    
    private DateTime dueWindowStart;
    private DateTime lateWindowStart;
    private DateTime maxWindowStart;

    @Autowired
    private AllActions allActions;
    @Autowired
    private AllAlerts allAlerts;
    @Autowired
    private AllClients allClients;
    @Autowired
    private AllEvents allEvents;
    
    @Autowired
    private AllEnrollments allEnrollments;
    
	@Before
	public void setup() throws IOException{
		initMocks(this);
		
		allEnrollments.removeAll();
		allClients.removeAll();
		allEvents.removeAll();
		allActions.removeAll();
		allAlerts.removeAll();
		
		fsp = new FormSubmissionProcessor(ziggyService, formSubmissionRouter, 
				formEntityConverter, scheduleService, clientService, eventService);
		reminderAction = new AlertCreationAction(scheduleService, formSubmissionService);

        dueWindowStart = DateTime.now();
        lateWindowStart = DateTime.now().plusDays(10);
        maxWindowStart = DateTime.now().plusDays(20);
	}
	
	@Ignore @Test //TODO 
	public void shouldCreateClientAndEventAndSchedulesAllDynamic() throws Exception {
		FormSubmission fs = getFormSubmissionFor("child_enrollment",1);
		//child birthdate is 10/Nov/2015
		fsp.processFormSubmission(fs);
		
		assertNotNull(clientService.getByBaseEntityId(fs.entityId()));
		List<Event> evl = allEvents.findByBaseEntityId(fs.entityId());
		assertTrue(evl.size() == 1);
		//TODO assertTrue(eventService.findEventsBy(fs.entityId(), null, "Enrollement Vaccination Register", null, "pkchild", 0, Long.MAX_VALUE).size() == 1);
		Enrollment p1schedule = scheduleService.getEnrollment(fs.entityId(), "PENTAVALENT 1");
		assertNotNull(p1schedule);
		assertTrue(p1schedule.isActive());
		assertFalse(p1schedule.isCompleted());
		assertEquals(p1schedule.getStatus(), EnrollmentStatus.ACTIVE);
		assertEquals(p1schedule.getLastFulfilledDate(), null);
		assertTrue(p1schedule.getFulfillments().isEmpty());
		
		Enrollment m1schedule = scheduleService.getActiveEnrollment(fs.entityId(), "Measles 1");
		assertNotNull(m1schedule);
		assertTrue(m1schedule.isActive());
		assertFalse(m1schedule.isCompleted());
		assertEquals(m1schedule.getStatus(), EnrollmentStatus.ACTIVE);
		assertEquals(m1schedule.getLastFulfilledDate(), null);
		assertTrue(m1schedule.getFulfillments().isEmpty());

		when(formSubmissionService.findByInstanceId(fs.instanceId())).thenReturn(fs);
		
		reminderAction.invoke(ScheduleBuilder.event(fs.entityId(), "PENTAVALENT 1", "penta1", 
				WindowName.earliest, p1schedule.getStartOfWindowForCurrentMilestone(WindowName.due),
				p1schedule.getStartOfWindowForCurrentMilestone(WindowName.late), p1schedule.getStartOfWindowForCurrentMilestone(WindowName.max)), null);
		
		List<Action> acl = allActions.findByCaseIdScheduleAndTimeStamp(fs.entityId(), p1schedule.getScheduleName(), new DateTime(0), new DateTime(System.currentTimeMillis()));
		assertTrue(acl.size() == 1);
		Action ac = acl.get(0);
		assertEquals(fs.anmId(), ac.providerId());
		assertEquals(fs.entityId(), ac.baseEntityId());
		assertEquals("createAlert", ac.getActionType());
		assertEquals("alert", ac.getActionTarget());
		assertEquals(true, ac.getIsActionActive());
		assertEquals(fs.bindType(), ac.data().get("beneficiaryType"));
		assertEquals(p1schedule.getScheduleName(), ac.data().get("scheduleName"));
		assertEquals("pentavalent_1", ac.data().get("visitCode"));
		assertEquals(p1schedule.getStartOfWindowForCurrentMilestone(WindowName.due).toLocalDate().toString(), ac.data().get("startDate"));
		assertEquals(p1schedule.getStartOfWindowForCurrentMilestone(WindowName.late).toLocalDate().toString(), ac.data().get("expiryDate"));
		
		List<Alert> all = allAlerts.findByEntityIdTriggerAndTimeStamp(fs.entityId(), p1schedule.getScheduleName(), new DateTime(0), new DateTime(System.currentTimeMillis()));
		assertTrue(all.size() == 1);
		Alert al = all.get(0);
		assertEquals(fs.anmId(), al.providerId());
		assertEquals(fs.entityId(), al.entityId());
		assertEquals("upcoming", al.alertStatus());
		assertEquals("notification", al.alertType());
		assertEquals("pkchild", al.beneficiaryType());
		assertEquals(p1schedule.getStartOfWindowForCurrentMilestone(WindowName.late).toLocalDate().toString(), al.expiryDate());
		assertEquals(p1schedule.getStartOfWindowForCurrentMilestone(WindowName.due).toLocalDate().toString(), al.startDate());
		assertEquals(true, al.isActive());
		assertEquals("penta1", al.triggerCode());
		assertEquals("PENTAVALENT 1", al.triggerName());
		assertEquals("schedule", al.triggerType());

		//TODO followup handling
	}
	
	@Test
	@Ignore //FIXME
	public void shouldCreateClientAndEventAndSchedulesWithRouter() throws Exception {
		hmap.addCustomFormSubmissionHandler("pnc_1st_registration", new CustomFormSubmissionHandler() {
			@Override
			public void handle(FormSubmission submission) {
				assertEquals("pnc_1st_registration", submission.formName());
				assertEquals("b716d938-1aea-40ae-a081-9ddddddcccc9", submission.entityId());
				assertEquals("admin", submission.anmId());
				assertEquals("demo_mother", submission.bindType());
				System.out.println("I want to call scheduler myself :D");
			}
		});
		FormSubmission fs = getFormSubmissionFor("pnc_1st_registration");
		fsp.processFormSubmission(fs);
		
	}
	
	@Test@Ignore
	public void shouldCreateClientAndEventAndSchedulesWithZiggy() throws Exception {
		hmap.addCustomFormSubmissionHandler("new_household_registration", new CustomFormSubmissionHandler() {
			@Override
			public void handle(FormSubmission submission) {
				assertEquals("new_household_registration", submission.formName());
				assertEquals("b716d938-1aea-40ae-a081-9ddddddcccc9", submission.entityId());
				assertEquals("admin", submission.anmId());
				assertEquals("household", submission.bindType());
				System.out.println("I am Ziggy scheduler");
			}
		});
        edmap.addEntity("household", Client.class);

        FormSubmission fs = getFormSubmissionFor("new_household_registration", 1);
        
        fsp.processFormSubmission(fs);
	}
}
