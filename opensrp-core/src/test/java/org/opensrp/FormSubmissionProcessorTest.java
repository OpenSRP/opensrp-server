package org.opensrp;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.opensrp.domain.Client;
import org.opensrp.domain.Event;
import org.opensrp.form.domain.FormSubmission;
import org.opensrp.form.domain.SubFormData;
import org.opensrp.form.service.FormAttributeParser;
import org.opensrp.scheduler.HealthSchedulerService;
import org.opensrp.scheduler.Schedule;
import org.opensrp.scheduler.Schedule.ActionType;
import org.opensrp.service.ClientService;
import org.opensrp.service.EventService;
import org.opensrp.service.formSubmission.FormEntityConverter;
import org.opensrp.service.formSubmission.FormSubmissionProcessor;
import org.opensrp.service.formSubmission.handler.FormSubmissionRouter;
import org.opensrp.service.formSubmission.ziggy.ZiggyService;
import org.opensrp.util.ScheduleBuilder;
import org.opensrp.util.TestResourceLoader;

public class FormSubmissionProcessorTest extends TestResourceLoader{

	public FormSubmissionProcessorTest() throws IOException {
		super();
	}

	@Mock
	private FormSubmissionProcessor fsp;
	@Mock
	private ZiggyService ziggyService;
	@Mock
	private FormSubmissionRouter formSubmissionRouter;
	@Mock
	private FormEntityConverter formEntityConverter;
	@Mock
	private HealthSchedulerService scheduleService;
	@Mock
	private ClientService clientService;
	@Mock
	private EventService eventService;
	
	@Before
	public void setup() throws IOException{
		initMocks(this);
		FormEntityConverter fec = new FormEntityConverter(new FormAttributeParser("/form"));
		fsp = new FormSubmissionProcessor(ziggyService, formSubmissionRouter, 
				fec, scheduleService, clientService, eventService);
	}
	
	@Ignore
	@Test
	public void testFormSubmission() throws Exception{
		FormSubmission submission = getFormSubmissionFor("pnc_reg_form");

		List<Schedule> schl = new ArrayList<Schedule>();
		schl.add(new Schedule(ActionType.enroll, new String[]{submission.formName()}, "Boosters", "REMINDER", new String[]{"birthdate"}, "child", ""));
		when(scheduleService.findAutomatedSchedules(submission.formName())).thenReturn(schl);
		
		fsp.processFormSubmission(submission);
		
		
		int totalEntities = 1;
		for (SubFormData e : submission.subForms()) {
			totalEntities += e.instances().size();
		}
		verify(clientService, times(totalEntities)).addClient(any(Client.class));
		verify(eventService, times(totalEntities)).addEvent(any(Event.class));
		verify(scheduleService, times(totalEntities-1)).enrollIntoSchedule(any(String.class), 
				eq("Boosters"), eq("REMINDER"), any(String.class), eq(submission.getInstanceId()));
	}
}
