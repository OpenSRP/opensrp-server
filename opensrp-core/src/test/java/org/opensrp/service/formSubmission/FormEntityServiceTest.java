package org.opensrp.service.formSubmission;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.IOException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.opensrp.domain.Client;
import org.opensrp.domain.Event;
import org.opensrp.form.domain.FormSubmission;
import org.opensrp.form.service.FormAttributeParser;
import org.opensrp.scheduler.HealthSchedulerService;
import org.opensrp.scheduler.ScheduleConfig;
import org.opensrp.scheduler.service.ActionService;
import org.opensrp.scheduler.service.ScheduleService;
import org.opensrp.service.ClientService;
import org.opensrp.service.EventService;
import org.opensrp.service.formSubmission.handler.FormSubmissionRouter;
import org.opensrp.service.formSubmission.ziggy.ZiggyService;
import org.opensrp.util.TestResourceLoader;
import org.opensrp.util.Utils;

import com.google.gson.Gson;


public class FormEntityServiceTest extends TestResourceLoader{
	public FormEntityServiceTest() throws IOException {
		super();
	}

	@Mock
    private ZiggyService ziggyService;
    @Mock
    private FormSubmissionRouter formSubmissionRouter;
    @Mock
    private FormSubmissionProcessor fsp;
    @Mock
    private FormEntityConverter formEntityConverter;
    @Mock
    private ClientService clientService;
    @Mock
    private EventService eventService;
    
    @Spy
    private ScheduleConfig scheduleConfig;
    @Mock
    private ScheduleService schService;
    @Mock 
    private ActionService actionService;
    
    @InjectMocks
    private HealthSchedulerService scheduleService;
    
    @Mock
    private FormEntityConverter fec;
    
    @Before
    public void setUp() throws Exception {
    	scheduleConfig = new ScheduleConfig("/schedules/schedule-config.xls"); 
        initMocks(this);
        fsp = new FormSubmissionProcessor(ziggyService, formSubmissionRouter, formEntityConverter, scheduleService, clientService, eventService);
        fec = new FormEntityConverter(new FormAttributeParser("/form"));
    }

	@Test
    public void shouldProcessNonZiggySubmission() throws Exception {
        FormSubmission fs = getFormSubmissionFor("new_household_registration", 1);

        fsp.processFormSubmission(fs);

        InOrder inOrder = inOrder(formEntityConverter, clientService, eventService, schService, ziggyService, formSubmissionRouter);
        inOrder.verify(formEntityConverter).getClientFromFormSubmission(fs);
        inOrder.verify(formEntityConverter).getEventFromFormSubmission(fs);
        inOrder.verify(formEntityConverter).getDependentClientsFromFormSubmission(fs);
        inOrder.verify(clientService, atLeastOnce()).addClient(any(Client.class));
        inOrder.verify(eventService, atLeastOnce()).addEvent(any(Event.class));
        inOrder.verify(schService).enroll(fs.entityId(), "FW CENSUS", "FW CENSUS", "2015-05-07", fs.instanceId());
        inOrder.verify(ziggyService).isZiggyCompliant(fs.bindType());
        inOrder.verify(formSubmissionRouter).route(fs);

        verifyNoMoreInteractions(formEntityConverter);
        //verifyNoMoreInteractions(clientService);
        //verifyNoMoreInteractions(eventService);
        //verifyNoMoreInteractions(schService);
        verifyNoMoreInteractions(ziggyService);
        verifyNoMoreInteractions(formSubmissionRouter);
    }
	
    @Test 
    public void shouldProcessNonZiggyWomanTTEnrollmentSubmission() throws Exception {
        FormSubmission fs = getFormSubmissionFor("woman_enrollment");

        fsp.processFormSubmission(fs);

        InOrder inOrder = inOrder(formEntityConverter, clientService, eventService, schService, ziggyService, formSubmissionRouter);
        inOrder.verify(formEntityConverter).getClientFromFormSubmission(fs);
        inOrder.verify(formEntityConverter).getEventFromFormSubmission(fs);
        inOrder.verify(formEntityConverter).getDependentClientsFromFormSubmission(fs);
        inOrder.verify(clientService, atLeastOnce()).addClient(any(Client.class));
        inOrder.verify(eventService, atLeastOnce()).addEvent(any(Event.class));
        inOrder.verify(schService).enroll(fs.entityId(), "TT 4", "tt4", "2016-03-25", fs.instanceId());
        inOrder.verify(ziggyService).isZiggyCompliant(fs.bindType());
        inOrder.verify(formSubmissionRouter).route(fs);

        verifyNoMoreInteractions(formEntityConverter);
        //verifyNoMoreInteractions(clientService);
        //verifyNoMoreInteractions(eventService);
        //verifyNoMoreInteractions(schService);
        verifyNoMoreInteractions(ziggyService);
        verifyNoMoreInteractions(formSubmissionRouter);
    }
	
	@Test
    public void shouldProcessZiggySubmission() throws Exception {
        FormSubmission fs = getFormSubmissionFor("new_household_registration", 1);

        when(ziggyService.isZiggyCompliant("household")).thenReturn(true);
        
        fsp.processFormSubmission(fs);

        InOrder inOrder = inOrder(formEntityConverter, clientService, eventService, schService, ziggyService, formSubmissionRouter);
        inOrder.verify(formEntityConverter).getClientFromFormSubmission(fs);
        inOrder.verify(formEntityConverter).getEventFromFormSubmission(fs);
        inOrder.verify(formEntityConverter).getDependentClientsFromFormSubmission(fs);
        inOrder.verify(clientService, atLeastOnce()).addClient(any(Client.class));
        inOrder.verify(eventService, atLeastOnce()).addEvent(any(Event.class));
        inOrder.verify(schService).enroll(fs.entityId(), "FW CENSUS", "FW CENSUS", "2015-05-07", fs.instanceId());
        inOrder.verify(ziggyService).isZiggyCompliant(fs.bindType());
        inOrder.verify(ziggyService).saveForm(Utils.getZiggyParams(fs), new Gson().toJson(fs.instance()));

        verifyZeroInteractions(formSubmissionRouter);
        verifyNoMoreInteractions(formEntityConverter);
       // verifyNoMoreInteractions(clientService);
       // verifyNoMoreInteractions(eventService);
       // verifyNoMoreInteractions(schService);
        verifyNoMoreInteractions(ziggyService);
    }
    
    /*TODO
    @Test
    public void shouldSortAllSubmissionsAndSaveEachOne() throws Exception {
        long baseTimeStamp = DateUtil.now().getMillis();
        String paramsForEarlierFormSubmission = new Gson().toJson(create(ANM_ID, "anm id 1").put(INSTANCE_ID, "instance id 1").put(ENTITY_ID, "entity id 1").put(FORM_NAME, "form name 1").put(CLIENT_VERSION, String.valueOf(baseTimeStamp)).put(SERVER_VERSION, String.valueOf(1L)).map());
        String paramsForLaterFormSubmission = new Gson().toJson(create(ANM_ID, "anm id 2").put(INSTANCE_ID, "instance id 2").put(ENTITY_ID, "entity id 2").put(FORM_NAME, "form name 1").put(CLIENT_VERSION, String.valueOf(baseTimeStamp + 1)).put(SERVER_VERSION, String.valueOf(2L)).map());
        String paramsForVeryLateFormSubmission = new Gson().toJson(create(ANM_ID, "anm id 2").put(INSTANCE_ID, "instance id 3").put(ENTITY_ID, "entity id 3").put(FORM_NAME, "form name 1").put(CLIENT_VERSION, String.valueOf(baseTimeStamp + 2)).put(SERVER_VERSION, String.valueOf(3L)).map());
        FormSubmission earlierFormSubmission = FormSubmissionBuilder.create().withANMId("anm id 1").addFormField("field 1", "value 1").withTimeStamp(baseTimeStamp).withServerVersion(1L).build();
        FormSubmission laterFormSubmission = FormSubmissionBuilder.create().withANMId("anm id 2").withInstanceId("instance id 2").withEntityId("entity id 2").addFormField("field 1", "value 2").withTimeStamp(baseTimeStamp + 1).withServerVersion(2L).build();
        FormSubmission veryLateFormSubmission = FormSubmissionBuilder.create().withANMId("anm id 2").withInstanceId("instance id 3").withEntityId("entity id 3").addFormField("field 1", "value 3").withTimeStamp(baseTimeStamp + 2).withServerVersion(3L).build();
        List<FormSubmission> formSubmissions = asList(laterFormSubmission, earlierFormSubmission, veryLateFormSubmission);
        FormExportToken formExportToken = new FormExportToken(0L);
        when(allFormExportTokens.getAll()).thenReturn(asList(formExportToken));

        submissionService.process(formSubmissions);

        InOrder inOrder = inOrder(ziggyService, allFormExportTokens);
        inOrder.verify(ziggyService).saveForm(paramsForEarlierFormSubmission, new Gson().toJson(earlierFormSubmission.instance()));
        inOrder.verify(allFormExportTokens).update(formExportToken.withVersion(1L));
        inOrder.verify(ziggyService).saveForm(paramsForLaterFormSubmission, new Gson().toJson(laterFormSubmission.instance()));
        inOrder.verify(allFormExportTokens).update(formExportToken.withVersion(2L));
        inOrder.verify(ziggyService).saveForm(paramsForVeryLateFormSubmission, new Gson().toJson(veryLateFormSubmission.instance()));
        inOrder.verify(allFormExportTokens).update(formExportToken.withVersion(3L));
        verifyNoMoreInteractions(ziggyService);
    }*/
}
