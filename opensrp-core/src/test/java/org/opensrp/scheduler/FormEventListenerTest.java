package org.opensrp.scheduler;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.verify;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.opensrp.common.util.EasyMap.mapOf;

import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.scheduler.domain.MotechEvent;
import org.opensrp.common.AllConstants;
import org.opensrp.common.AllConstants.Config;
import org.opensrp.common.AllConstants.OpenSRPEvent;
import org.opensrp.domain.AppStateToken;
import org.opensrp.dto.form.FormSubmissionDTO;
import org.opensrp.form.domain.FormData;
import org.opensrp.form.domain.FormField;
import org.opensrp.form.domain.FormInstance;
import org.opensrp.form.domain.FormSubmission;
import org.opensrp.form.service.FormSubmissionService;
import org.opensrp.service.ConfigService;
import org.opensrp.service.ErrorTraceService;
import org.opensrp.service.formSubmission.FormSubmissionListener;
import org.opensrp.service.formSubmission.FormSubmissionProcessor;
import org.powermock.api.mockito.internal.verification.VerifyNoMoreInteractions;

import com.google.gson.Gson;

public class FormEventListenerTest {
    @Mock
    private FormSubmissionService formSubmissionService;
    @Mock
    private FormSubmissionProcessor fsp;
    @Mock
    private ConfigService configService;
    @Mock
    private ErrorTraceService errorTraceService;

    private FormSubmissionListener listener;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        listener = new FormSubmissionListener(formSubmissionService, fsp, configService, errorTraceService);
    }

    @Test
    public void shouldDelegateFormSubmissionToSubmissionService() throws Exception {
        List<FormSubmissionDTO> formSubmissions = asList(new FormSubmissionDTO("anm id 1", "instance id 1", "entity id 1", "form name", null, "0", "1"),
                new FormSubmissionDTO("anm id 2", "instance id 2", "entity id 2", "form name", null, "0", "1"));

        listener.submitForms(new MotechEvent(OpenSRPEvent.FORM_SUBMISSION, mapOf("data", (Object) new Gson().toJson(formSubmissions))));

        verify(formSubmissionService).submit(formSubmissions);
    }

    @Test
    public void shouldFetchFormSubmissionsFromSubmissionService() throws Exception {
    	FormSubmission fs1 = new FormSubmission("anm id 1", "instance id 1", "form name", "entity id 1", "1.0", 0L, new FormInstance(new FormData("test","def/bindpath", new ArrayList<FormField>(), null))),
        fs2 = new FormSubmission("anm id 2", "instance id 2", "form name", "entity id 2", "1.0", 0L, new FormInstance(new FormData("test","def/bindpath", new ArrayList<FormField>(), null)));
        List<FormSubmission> formSubmissions = asList(fs1,fs2);
        when(configService.getAppStateTokenByName(Config.FORM_ENTITY_PARSER_LAST_SYNCED_FORM_SUBMISSION)).thenReturn(new AppStateToken("token", 1L, 0));
        when(formSubmissionService.getAllSubmissions(1L, null)).thenReturn(formSubmissions);

        listener.parseForms(new MotechEvent("SUBJECT", null));

        verify(fsp).processFormSubmission(fs1);
        verify(fsp).processFormSubmission(fs2);
        verify(configService, atLeast(2)).updateAppStateToken(eq(Config.FORM_ENTITY_PARSER_LAST_SYNCED_FORM_SUBMISSION), any(Long.class));
        verifyNoMoreInteractions(fsp);
    }

    @Test
    public void shouldNotDoAnythingIfFetchFromSubmissionServiceReturnsEmptyList() throws Exception {
        when(configService.getAppStateTokenByName(Config.FORM_ENTITY_PARSER_LAST_SYNCED_FORM_SUBMISSION)).thenReturn(new AppStateToken("token", 1L, 0));
        when(formSubmissionService.getAllSubmissions(1L, null)).thenReturn(Collections.EMPTY_LIST);

        listener.parseForms(new MotechEvent("SUBJECT", null));

        verifyZeroInteractions(fsp);
    }
}

