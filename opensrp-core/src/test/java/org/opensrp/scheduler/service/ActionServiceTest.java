package org.opensrp.scheduler.service;

import com.google.gson.Gson;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.opensrp.dto.ActionData;
import org.opensrp.dto.AlertStatus;
import org.opensrp.dto.BeneficiaryType;
import org.opensrp.dto.MonthSummaryDatum;
import org.opensrp.scheduler.Action;
import org.opensrp.scheduler.Alert;
import org.opensrp.scheduler.Alert.AlertType;
import org.opensrp.scheduler.Alert.TriggerType;
import org.opensrp.scheduler.repository.AllActions;
import org.opensrp.scheduler.repository.AllAlerts;
import org.opensrp.service.BaseEntityService;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.opensrp.dto.AlertStatus.normal;
import static org.opensrp.scheduler.service.ActionService.ALL_PROVIDERS;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;


@RunWith(PowerMockRunner.class)
@PrepareForTest({DateTime.class, org.motechproject.util.DateUtil.class})
public class ActionServiceTest {
    public static final String ANM_1 = "ANM 1";
    public static final String CASE_X = "Case X";
    public static final String REASON_FOR_CLOSE = "reason for close";
    public static final DateTime DATE_TIME = new DateTime(0l);
    public static final String SCHDEDULE = "schdedule";
    @Mock
    private AllActions allActions;
    
    @Mock
    private AllAlerts allAlerts;
    
    @Mock
    private BaseEntityService baseEntityService;

    private ActionService service;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        allActions.removeAll();
        allAlerts.removeAll();
        service = new ActionService(allActions, allAlerts);
    }

    @Test
    public void shouldSaveAlertActionForEntity() throws Exception {
        DateTime dueDate = DateTime.now().minusDays(1);
        DateTime expiryDate = dueDate.plusWeeks(2);

        service.alertForBeneficiary("mother", "Case X", "ANM ID M", "Ante Natal Care - Normal", "ANC 1", normal, dueDate, expiryDate);
        Action action = new Action("Case X", "ANM ID M", ActionData.createAlert("mother", "Ante Natal Care - Normal", "ANC 1", normal, dueDate, expiryDate));


        verify(allActions).addOrUpdateAlert(action);
        verify(allAlerts).addOrUpdateScheduleNotificationAlert("mother", "Case X", "ANM ID M", "Ante Natal Care - Normal", "ANC 1", normal, dueDate, expiryDate);
    }

    @Test 
    public void shouldSaveAlertActionForEntityWithActionObject() throws Exception {
        DateTime dueDate = DateTime.now().minusDays(1);
        DateTime expiryDate = dueDate.plusWeeks(2);

        Action action = new Action("Case X", "ANM ID M", ActionData.createAlert("mother", "Ante Natal Care - Normal", "ANC 1", normal, dueDate, expiryDate));

        service.alertForBeneficiary(action);

        verify(allActions).addOrUpdateAlert(action);
       // verify(allAlerts).addOrUpdateScheduleNotificationAlert("mother", "Case X", "ANM ID M", "Ante Natal Care - Normal", "ANC 1", normal, dueDate, expiryDate);
    }


    @Test
    public void shouldCreateACloseActionForAVisitOfAChild() throws Exception {
        service.markAlertAsClosed("Case X", "ANM 1", "OPV 1", "2012-01-01");

        verify(allActions).add(new Action("Case X", "ANM 1", ActionData.markAlertAsClosed("OPV 1", "2012-01-01")));
    }


    @Test
    public void shouldCreateADeleteAllActionForAMother() throws Exception {
        service.markAllAlertsAsInactive("Case X");

        verify(allActions).markAllAsInActiveFor("Case X");
    }

    @Test
    public void shouldMarkAnAlertAsInactive() throws Exception {
        service.markAlertAsInactive("ANM 1","Case X", "Schedule 1");

        verify(allActions).markAlertAsInactiveFor("ANM 1","Case X", "Schedule 1");
    }


    @Test
    public void shouldReportForIndicator() {
        ActionData summaryActionData = ActionData.reportForIndicator("ANC", "30", new Gson().toJson(asList(new MonthSummaryDatum("3", "2012", "2", "2", asList("CASE 5", "CASE 6")))));

        service.reportForIndicator("ANM X", summaryActionData);

        verify(allActions).add(new Action("", "ANM X", summaryActionData));
    }

    @Test
    public void shouldDeleteAllActionsWithTargetReport() {
        service.deleteReportActions();

        verify(allActions).deleteAllByTarget("report");
    }

    @Test
    public void shouldCreateACloseActionForAVisitOfAMother() throws Exception {
        service.markAlertAsClosed("Case X", "ANM ID 1", "ANC 1", "2012-12-12");

        Action action = new Action("Case X", "ANM ID 1", ActionData.markAlertAsClosed("ANC 1", "2012-12-12"));
        verify(allActions).add(action);
        verify(allAlerts).markAlertAsCompleteFor("ANM ID 1", "Case X", "ANC 1", "2012-12-12");
    }

    @Test
    public void shouldCloseAlertBasedOnCaseId() throws Exception {
        service.markAlertAsClosed(CASE_X, "ANC 1", "2012-12-12");

        Action action = new Action(CASE_X, ALL_PROVIDERS, ActionData.markAlertAsClosed("ANC 1", "2012-12-12"));
        verify(allActions).add(action);
        verify(allAlerts).markAlertAsCompleteFor( CASE_X, "ANC 1", "2012-12-12");
    }

    @Test
    public void shouldCloseBeneficiary() throws Exception {
        PowerMockito.mockStatic(org.motechproject.util.DateUtil.class);
        DateTime dateTime = mock(DateTime.class);

        when(org.motechproject.util.DateUtil.now()).thenReturn(new DateTime(0l));
        PowerMockito.whenNew(DateTime.class).withNoArguments().thenReturn(dateTime);
        when(dateTime.toLocalDate()).thenReturn(new LocalDate(0l));

        service.closeBeneficiary(BeneficiaryType.mother, CASE_X, ANM_1, REASON_FOR_CLOSE);

        Action action = new Action(CASE_X, ANM_1, ActionData.closeBeneficiary(BeneficiaryType.mother.name(), REASON_FOR_CLOSE));
        DateTime defaultDate = service.getCurrentDateTime();
        Alert alert = new Alert(ANM_1, CASE_X, BeneficiaryType.mother.name(), Alert.AlertType.notification,
                Alert.TriggerType.caseClosed, null, null,
                defaultDate, defaultDate, AlertStatus.urgent, null);
       
        verify(allActions).add(action);
        verify(allAlerts).add(alert);
    }


    @Test
    public void shouldReturnAlertsBasedOnANMIDAndTimeStamp() throws Exception {
        List<Action> alertActions = Arrays.asList(new Action("Case X", "ANM 1", ActionData.createAlert("mother", "Ante Natal Care - Normal", "ANC 1", normal, DateTime.now(), DateTime.now().plusDays(3))));
        when(allActions.findByProviderIdAndTimeStamp("ANM 1", 1010101)).thenReturn(alertActions);

        List<Action> alerts = service.getNewAlertsForANM("ANM 1", 1010101);

        assertEquals(1, alerts.size());
        assertEquals(alertActions, alerts);
    }

    @Test
    public void shouldFindAllByCriteria() {
        List<Action> alerts = service.findByCriteria("team", "ANM 1", 1010101, "sortBy", "sortOrder", 1);
        verify(allActions).findByCriteria("team", "ANM 1", 1010101, "sortBy", "sortOrder", 1);

    }

    @Test
    public void shouldGetAlertsForProvider() {
        service.getAlertsForProvider(ANM_1, 200l);
        verify(allAlerts).findByProviderAndTimestamp(ANM_1, 200l);
    }

    @Test
    public void shouldGetAlertsActiveForProvider() {
        service.getAlertsActiveForProvider(ANM_1, DATE_TIME.getMillis());

        verify(allAlerts).findActiveByProviderAndTimestamp(ANM_1, new DateTime(0l).getMillis());
    }

    @Test
    public void shouldFindByCaseIdScheduleAndTimeStamp() {
        service.findByCaseIdScheduleAndTimeStamp(CASE_X, SCHDEDULE, DATE_TIME, DATE_TIME);

        verify(allActions).findByCaseIdScheduleAndTimeStamp(CASE_X, SCHDEDULE, DATE_TIME, DATE_TIME);
    }

    @Test
    public void shouldFindByCaseIdAndTimeStamp() {
        service.findByCaseIdAndTimeStamp(CASE_X, DATE_TIME.getMillis());

        verify(allActions).findByCaseIdAndTimeStamp(CASE_X, DATE_TIME.getMillis());
    }

    @Test
    public void shouldFindAlertByEntityIdScheduleAndTimeStamp() {
        service.findAlertByEntityIdScheduleAndTimeStamp(CASE_X, SCHDEDULE, DATE_TIME, DATE_TIME);

        verify(allAlerts).findByEntityIdTriggerAndTimeStamp(CASE_X, SCHDEDULE, DATE_TIME, DATE_TIME);
    }

}