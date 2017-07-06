package org.opensrp.web.it;

import static org.motechproject.scheduletracking.api.domain.WindowName.due;
import static org.motechproject.scheduletracking.api.domain.WindowName.earliest;
import static org.motechproject.scheduletracking.api.domain.WindowName.late;
import static org.motechproject.scheduletracking.api.domain.WindowName.max;
import static org.motechproject.util.DateUtil.newDate;
import static org.opensrp.common.util.DateUtil.fakeIt;
import static org.opensrp.web.it.DrishtiScheduleConstants.*;
import static org.opensrp.web.it.DrishtiScheduleConstants.ChildScheduleConstants.*;
import static org.opensrp.integration.ChildImmunizationFields.*;

import java.io.File;
import java.util.Date;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.delivery.schedule.util.FakeSchedule;
import org.motechproject.delivery.schedule.util.ScheduleVisualization;
import org.motechproject.delivery.schedule.util.ScheduleWithCapture;
import org.motechproject.delivery.schedule.util.SetDateAction;
import org.motechproject.model.Time;
import org.motechproject.scheduletracking.api.service.ScheduleTrackingService;
import org.motechproject.testing.utils.BaseUnitTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/applicationContext-opensrp-web.xml")
public class DrishtiSchedulesIntegrationTest extends BaseUnitTest {
    private static final int JANUARY = 1;
    private static final int FEBRUARY = 2;
    private static final int MARCH = 3;
    private static final int APRIL = 4;
    private static final int MAY = 5;
    private static final int JUNE = 6;
    private static final int JULY = 7;
    private static final int AUGUST = 8;
    private static final int SEPTEMBER = 9;
    private static final int OCTOBER = 10;
    private static final int NOVEMBER = 11;
    private static final int DECEMBER = 12;

    @Autowired
    private ScheduleTrackingService trackingService;
    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    private ScheduleWithCapture schedule;
    private ScheduleVisualization visualization;

    @Test
    public void shouldProvideAlertsForANCAtTheRightTimes() throws Exception {
        schedule.enrollFor("Ante Natal Care - Normal", newDate(2012, 1, 1), new Time(14, 0));

        schedule.assertNoAlerts("ANC 1", earliest);
        schedule.assertAlertsStartWith("ANC 1", due, date(1, JANUARY), date(2, JANUARY), date(3, JANUARY), date(4, JANUARY), date(5, JANUARY), date(6, JANUARY),
                date(7, JANUARY), date(8, JANUARY), date(9, JANUARY));
        schedule.assertAlertsStartWith("ANC 1", late, date(25, MARCH), date(26, MARCH), date(27, MARCH), date(28, MARCH), date(29, MARCH),
                date(30, MARCH), date(31, MARCH), date(1, APRIL));
        schedule.assertNoAlerts("ANC 1", max);

        schedule.assertNoAlerts("ANC 2", earliest);
        schedule.assertAlertsStartWith("ANC 2", due, date(8, APRIL), date(9, APRIL), date(10, APRIL), date(11, APRIL), date(12, APRIL), date(13, APRIL), date(14, APRIL),
                date(15, APRIL), date(16, APRIL), date(17, APRIL), date(18, APRIL), date(19, APRIL));
        schedule.assertAlertsStartWith("ANC 2", late, date(1, JULY), date(2, JULY), date(3, JULY), date(4, JULY), date(5, JULY),
                date(6, JULY), date(7, JULY), date(8, JULY));
        schedule.assertNoAlerts("ANC 2", max);

        schedule.assertNoAlerts("ANC 3", earliest);
        schedule.assertAlertsStartWith("ANC 3", due, date(15, JULY), date(16, JULY), date(17, JULY), date(18, JULY), date(19, JULY), date(20, JULY));
        schedule.assertAlertsStartWith("ANC 3", late, date(26, AUGUST), date(27, AUGUST), date(28, AUGUST), date(29, AUGUST), date(30, AUGUST));
        schedule.assertNoAlerts("ANC 3", max);

        schedule.assertNoAlerts("ANC 4", earliest);
        schedule.assertAlertsStartWith("ANC 4", due, date(9, SEPTEMBER), date(10, SEPTEMBER), date(11, SEPTEMBER), date(12, SEPTEMBER), date(13, SEPTEMBER),
                date(14, SEPTEMBER), date(15, SEPTEMBER), date(16, SEPTEMBER), date(17, SEPTEMBER));
        schedule.assertAlertsStartWith("ANC 4", late, date(30, SEPTEMBER), date(1, OCTOBER), date(2, OCTOBER), date(3, OCTOBER), date(4, OCTOBER), date(5, OCTOBER),
                date(6, OCTOBER), date(7, OCTOBER), date(8, OCTOBER));
        schedule.assertNoAlerts("ANC 4", max);

        visualization.outputTo("mother-anc-normal.html", 3);
    }

    @Test
    public void shouldProvideAlertsForLabRemindersATheRightTimes() throws Exception {
        schedule.enrollFor("Lab Reminders", newDate(2012, 1, 1), new Time(14, 0));

        schedule.assertNoAlerts("REMINDER", earliest);
        schedule.assertNoAlerts("REMINDER", due);
        schedule.assertAlerts("REMINDER", late, date(29, JULY), date(5, AUGUST), date(12, AUGUST), date(19, AUGUST),
                date(26, AUGUST), date(2, SEPTEMBER), date(9, SEPTEMBER), date(16, SEPTEMBER), date(23, SEPTEMBER), date(30, SEPTEMBER), date(7, OCTOBER));
        schedule.assertAlerts("REMINDER", max, date(9, OCTOBER), date(10, OCTOBER), date(11, OCTOBER));

        visualization.outputTo("mother-lab-reminder.html", 3);
    }

    @Test
    public void shouldProvideAlertsForExpectedDateOfDeliveryAtTheRightTimes() throws Exception {
        schedule.enrollFor("Expected Date Of Delivery", newDate(2012, 1, 1), new Time(14, 0));

        schedule.assertNoAlerts("EDD", earliest);
        schedule.assertAlerts("EDD", due, date(23, SEPTEMBER), date(30, SEPTEMBER), date(7, OCTOBER));
        schedule.assertAlertsStartWith("EDD", late, date(10, OCTOBER), date(14, OCTOBER), date(17, OCTOBER), date(21, OCTOBER), date(24, OCTOBER), date(28, OCTOBER), date(31, OCTOBER), date(4, NOVEMBER));
        schedule.assertNoAlerts("EDD", max);

        visualization.outputTo("mother-expected-date-of-delivery.html", 3);
    }

    @Test
    @Ignore //TODO revise schedule
    public void shouldProvideAlertsForBCG() throws Exception {
        schedule.enrollFor(CHILD_SCHEDULE_BCG, newDate(2012, JANUARY, 1), new Time(14, 0));

        schedule.assertNoAlerts(BCG_VALUE, earliest);
        schedule.assertAlertsStartWith(BCG_VALUE, due, date(1, JANUARY), date(2, JANUARY), date(3, JANUARY),
                date(4, JANUARY));
        schedule.assertAlertsStartWith(BCG_VALUE, late, date(15, JANUARY), date(16, JANUARY), date(17, JANUARY), date(18, JANUARY));
        schedule.assertNoAlerts(BCG_VALUE, max);

        visualization.outputTo("child-bcg.html", 1);
    }

    @Test
    @Ignore //TODO revise schedule
    public void shouldProvideAlertsForOPV0AndOPV1() throws Exception {
        schedule.enrollFor(CHILD_SCHEDULE_OPV_0_AND_1, newDate(2012, JANUARY, 1), new Time(14, 0));

        schedule.assertNoAlerts("opv_0", earliest);
        schedule.assertAlertsStartWith("opv_0", due, date(1, JANUARY), date(2, JANUARY), date(3, JANUARY), date(4, JANUARY));
        schedule.assertNoAlerts("opv_0", late);
        schedule.assertNoAlerts("opv_0", max);

        schedule.assertAlertsStartWith("opv_1", earliest, date(16, JANUARY), date(17, JANUARY), date(18, JANUARY), date(19, JANUARY));
        schedule.assertAlertsStartWith("opv_1", due, date(12, FEBRUARY), date(13, FEBRUARY), date(14, FEBRUARY), date(15, FEBRUARY));
        schedule.assertAlertsStartWith("opv_1", late, date(26, FEBRUARY), date(27, FEBRUARY), date(28, FEBRUARY), date(29, FEBRUARY));
        schedule.assertNoAlerts("opv_1", max);

        visualization.outputTo("child-opv-0-and-1.html", 1);
    }

    @Test
    @Ignore //TODO revise schedule
    public void shouldProvideAlertsForOPV2() throws Exception {
        schedule.enrollFor("OPV 2", newDate(2012, JANUARY, 1), new Time(14, 0));

        schedule.assertAlertsStartWith("opv_2", earliest, date(1, JANUARY), date(2, JANUARY), date(3, JANUARY), date(4, JANUARY));
        schedule.assertAlertsStartWith("opv_2", due, date(29, JANUARY), date(30, JANUARY), date(31, JANUARY), date(1, FEBRUARY));
        schedule.assertAlertsStartWith("opv_2", late, date(12, FEBRUARY), date(13, FEBRUARY), date(14, FEBRUARY), date(15, FEBRUARY));
        schedule.assertNoAlerts("opv_2", max);

        visualization.outputTo("child-opv-2.html", 1);
    }

    @Test
    @Ignore //TODO revise schedule
    public void shouldProvideAlertsForOPV3() throws Exception {
        schedule.enrollFor("OPV 3", newDate(2012, JANUARY, 1), new Time(14, 0));

        schedule.assertAlertsStartWith("opv_3", earliest, date(1, JANUARY), date(2, JANUARY), date(3, JANUARY), date(4, JANUARY));
        schedule.assertAlertsStartWith("opv_3", due, date(29, JANUARY), date(30, JANUARY), date(31, JANUARY), date(1, FEBRUARY));
        schedule.assertAlertsStartWith("opv_3", late, date(12, FEBRUARY), date(13, FEBRUARY), date(14, FEBRUARY), date(15, FEBRUARY));
        schedule.assertNoAlerts("opv_3", max);

        visualization.outputTo("child-opv-3.html", 1);
    }

    @Test
    public void shouldProvideAlertsForOPVBooster() throws Exception {
        schedule.enrollFor("OPV BOOSTER", newDate(2012, JANUARY, 1), new Time(14, 0));

        schedule.assertNoAlerts("opvbooster", earliest);
        schedule.assertAlertsStartWith("opvbooster", due, dateWithYear(1, MAY, 2013),
                dateWithYear(2, MAY, 2013), dateWithYear(3, MAY, 2013), dateWithYear(4, MAY, 2013));
        schedule.assertAlertsStartWith("opvbooster", late, dateWithYear(1, JANUARY, 2014),
                dateWithYear(2, JANUARY, 2014), dateWithYear(3, JANUARY, 2014));
        schedule.assertNoAlerts("opvbooster", max);

        visualization.outputTo("child-opv-booster.html", 1);
    }

    @Test
    @Ignore //TODO revise schedule
    public void shouldProvideAlertForMeaslesVaccination() throws Exception {
        schedule.enrollFor(CHILD_SCHEDULE_MEASLES, newDate(2012, JANUARY, 1), new Time(14, 0));

        schedule.assertAlertsStartWith(MEASLES_VALUE, earliest, date(1, JANUARY), date(2, JANUARY), date(3, JANUARY), date(4, JANUARY));
        schedule.assertAlertsStartWith(MEASLES_VALUE, due, date(1, OCTOBER), date(2, OCTOBER), date(3, OCTOBER), date(4, OCTOBER), date(5, OCTOBER));
        schedule.assertAlertsStartWith(MEASLES_VALUE, late, dateWithYear(1, JANUARY, 2013), dateWithYear(2, JANUARY, 2013), dateWithYear(3, JANUARY, 2013),
                dateWithYear(4, JANUARY, 2013));
        schedule.assertNoAlerts(MEASLES_VALUE, max);

        visualization.outputTo("child-measles.html", 4);
    }

    @Ignore
    public void shouldProvideAlertForMeaslesBoosterVaccination() throws Exception {
        schedule.enrollFor(CHILD_SCHEDULE_MEASLES_BOOSTER, newDate(2012, JANUARY, 1), new Time(14, 0));

        schedule.assertAlertsStartWith(MEASLES_BOOSTER_VALUE, earliest, date(1, JANUARY), date(2, JANUARY), date(3, JANUARY));
        schedule.assertAlertsStartWith(MEASLES_BOOSTER_VALUE, due, dateWithYear(1, MAY, 2013),
                dateWithYear(2, MAY, 2013), dateWithYear(3, MAY, 2013), dateWithYear(4, MAY, 2013));
        schedule.assertAlertsStartWith(MEASLES_BOOSTER_VALUE, late, dateWithYear(1, JANUARY, 2014),
                dateWithYear(2, JANUARY, 2014), dateWithYear(3, JANUARY, 2014));
        schedule.assertNoAlerts(MEASLES_BOOSTER_VALUE, max);

        visualization.outputTo("child-measles-booster.html", 4);
    }

    @Test
    public void shouldProvideAlertForDPTBooster1Vaccination() throws Exception {
        schedule.enrollFor("DPT Booster 1", newDate(2012, 1, 1), new Time(14, 0));

        schedule.assertAlertsStartWith("dptbooster_1", earliest, date(1, JANUARY), date(2, JANUARY), date(3, JANUARY), date(4, JANUARY));
        schedule.assertAlertsStartWith("dptbooster_1", due, dateWithYear(1, MAY, 2013), dateWithYear(2, MAY, 2013), dateWithYear(3, MAY, 2013));
        schedule.assertAlertsStartWith("dptbooster_1", late, dateWithYear(1, JANUARY, 2014), dateWithYear(2, JANUARY, 2014), dateWithYear(3, JANUARY, 2014));
        schedule.assertNoAlerts("dptbooster_1", max);

        visualization.outputTo("child-dpt-booster1.html", 4);
    }

    @Test
    public void shouldProvideAlertForDPTBooster2Vaccination() throws Exception {
        schedule.enrollFor(CHILD_SCHEDULE_DPT_BOOSTER2, newDate(2012, 1, 1), new Time(14, 0));

        schedule.assertNoAlerts(DPT_BOOSTER_2_VALUE, earliest);
        schedule.assertAlertsStartWith(DPT_BOOSTER_2_VALUE, due, dateWithYear(1, JANUARY, 2017),
                dateWithYear(2, JANUARY, 2017), dateWithYear(3, JANUARY, 2017));
        schedule.assertNoAlerts(DPT_BOOSTER_2_VALUE, late);
        schedule.assertNoAlerts(DPT_BOOSTER_2_VALUE, max);

        visualization.outputTo("child-dpt-booster2.html", 4);
    }

    @Test
    public void shouldProvideAlertsForPentavalent1() throws Exception {
        schedule.enrollFor("Penta 1", newDate(2012, JANUARY, 1), new Time(14, 0));

        schedule.assertAlertsStartWith("penta1", earliest, date(1, JANUARY), date(2, JANUARY), date(3, JANUARY), date(4, JANUARY));
        schedule.assertAlertsStartWith("penta1", due, date(12, FEBRUARY), date(13, FEBRUARY), date(14, FEBRUARY), date(15, FEBRUARY));
        schedule.assertAlertsStartWith("penta1", late, date(26, FEBRUARY), date(27, FEBRUARY), date(28, FEBRUARY), date(29, FEBRUARY));
        //schedule.assertNoAlerts("penta1", max);

        visualization.outputTo("child-pentavalent-1.html", 1);
    }

    @Test
    public void shouldProvideAlertsForPentavalent2() throws Exception {
        schedule.enrollFor("Penta 2", newDate(2012, JANUARY, 1), new Time(14, 0));

        schedule.assertAlertsStartWith("penta2", earliest, date(1, JANUARY), date(2, JANUARY), date(3, JANUARY), date(4, JANUARY));
        schedule.assertAlertsStartWith("penta2", due, date(29, JANUARY), date(30, JANUARY), date(31, JANUARY));
        schedule.assertAlertsStartWith("penta2", late, date(12, FEBRUARY), date(13, FEBRUARY), date(14, FEBRUARY), date(15, FEBRUARY));
        //schedule.assertNoAlerts("penta2", max);

        visualization.outputTo("child-pentavalent-2.html", 1);
    }

    @Test
    public void shouldProvideAlertsForPentavalent3() throws Exception {
        schedule.enrollFor("Penta 3", newDate(2012, JANUARY, 1), new Time(14, 0));

        schedule.assertAlertsStartWith("penta3", earliest, date(1, JANUARY), date(2, JANUARY), date(3, JANUARY), date(4, JANUARY));
        schedule.assertAlertsStartWith("penta3", due, date(29, JANUARY), date(30, JANUARY), date(31, JANUARY));
        schedule.assertAlertsStartWith("penta3", late, date(12, FEBRUARY), date(13, FEBRUARY), date(14, FEBRUARY), date(15, FEBRUARY));
        //schedule.assertNoAlerts("penta3", max);

        visualization.outputTo("child-pentavalent-3.html", 1);
    }

    @Test
    @Ignore //TODO revise schedule
    public void shouldProvideAlertForBoosterDoses() throws Exception {
        schedule.enrollFor("Boosters", newDate(2012, 1, 1), new Time(14, 0));

        schedule.assertNoAlerts("REMINDER", earliest);
        schedule.assertAlerts("REMINDER", due, dateWithYear(19, MAY, 2013));
        schedule.assertNoAlerts("REMINDER", late);
        schedule.assertNoAlerts("REMINDER", max);

        visualization.outputTo("child-measles-boosters.html", 4);
    }

    @Test
    public void shouldProvideAlertsForPNCCloseAtTheRightTimes() throws Exception {
        schedule.enrollFor("Auto Close PNC", newDate(2012, 1, 1), new Time(14, 0));

        schedule.assertNoAlerts("Auto Close PNC", earliest);
        schedule.assertAlertsStartWith("Auto Close PNC", due, dateWithYear(26, FEBRUARY, 2012), dateWithYear(27, FEBRUARY, 2012), dateWithYear(28, FEBRUARY, 2012),
                dateWithYear(29, FEBRUARY, 2012));
        schedule.assertNoAlerts("Auto Close PNC", late);
        schedule.assertNoAlerts("Auto Close PNC", max);

        visualization.outputTo("mother-auto-close-pnc.html", 1);
    }

    @Test
    public void shouldProvideAlertsForDMPAInjectableRefillAtTheRightTimes() throws Exception {
        schedule.enrollFor("DMPA Injectable Refill", newDate(2012, JANUARY, 1), new Time(14, 0));

        schedule.assertAlertsStartWith("DMPA Injectable Refill", earliest,
                dateWithYear(1, JANUARY, 2012), dateWithYear(2, JANUARY, 2012), dateWithYear(3, JANUARY, 2012), dateWithYear(4, JANUARY, 2012));
        schedule.assertAlerts("DMPA Injectable Refill", due, dateWithYear(25, MARCH, 2012), dateWithYear(26, MARCH, 2012), dateWithYear(27, MARCH, 2012),
                dateWithYear(28, MARCH, 2012), dateWithYear(29, MARCH, 2012), dateWithYear(30, MARCH, 2012), dateWithYear(31, MARCH, 2012));
        schedule.assertAlertsStartWith("DMPA Injectable Refill", late, dateWithYear(1, APRIL, 2012), dateWithYear(2, APRIL, 2012), dateWithYear(3, APRIL, 2012),
                dateWithYear(4, APRIL, 2012), dateWithYear(5, APRIL, 2012), dateWithYear(6, APRIL, 2012), dateWithYear(7, APRIL, 2012));
        schedule.assertNoAlerts("DMPA Injectable Refill", max);

        visualization.outputTo("ec-dmpa-injectable-refill.html", 1);
    }

    @Test
    public void shouldProvideAlertsForOCPRefillAtTheRightTimes() throws Exception {
        schedule.enrollFor("OCP Refill", newDate(2012, JANUARY, 1), new Time(14, 0));

        schedule.assertNoAlerts("OCP Refill", earliest);
        schedule.assertAlerts("OCP Refill", due, dateWithYear(1, JANUARY, 2012), dateWithYear(2, JANUARY, 2012), dateWithYear(3, JANUARY, 2012), dateWithYear(4, JANUARY, 2012),
                dateWithYear(5, JANUARY, 2012), dateWithYear(6, JANUARY, 2012), dateWithYear(7, JANUARY, 2012));
        schedule.assertAlertsStartWith("OCP Refill", late, dateWithYear(8, JANUARY, 2012), dateWithYear(9, JANUARY, 2012), dateWithYear(10, JANUARY, 2012),
                dateWithYear(11, JANUARY, 2012), dateWithYear(12, JANUARY, 2012), dateWithYear(13, JANUARY, 2012), dateWithYear(14, JANUARY, 2012));
        schedule.assertNoAlerts("OCP Refill", max);

        visualization.outputTo("ec-ocp-refill.html", 1);
    }

    @Test
    public void shouldProvideAlertsForCondomRefillAtTheRightTimes() throws Exception {
        schedule.enrollFor("Condom Refill", newDate(2012, JANUARY, 1), new Time(14, 0));

        schedule.assertNoAlerts("Condom Refill", earliest);
        schedule.assertAlerts("Condom Refill", due, dateWithYear(1, JANUARY, 2012), dateWithYear(2, JANUARY, 2012),
                dateWithYear(3, JANUARY, 2012), dateWithYear(4, JANUARY, 2012), dateWithYear(5, JANUARY, 2012), dateWithYear(6, JANUARY, 2012), dateWithYear(7, JANUARY, 2012));
        schedule.assertAlertsStartWith("Condom Refill", late, dateWithYear(8, JANUARY, 2012), dateWithYear(9, JANUARY, 2012), dateWithYear(10, JANUARY, 2012), dateWithYear(11, JANUARY, 2012));
        schedule.assertNoAlerts("Condom Refill", max);

        visualization.outputTo("ec-condom-refill.html", 1);
    }

    @Test
    public void shouldProvideAlertsForFemaleSterilizationFollowupAtTheRightTimes() throws Exception {
        schedule.enrollFor("Female sterilization Followup", newDate(2012, JANUARY, 1), new Time(14, 0));

        schedule.assertNoAlerts("Female sterilization Followup 1", earliest);
        schedule.assertAlerts("Female sterilization Followup 1", due, dateWithYear(1, JANUARY, 2012), dateWithYear(2, JANUARY, 2012));
        schedule.assertAlerts("Female sterilization Followup 1", late, dateWithYear(3, JANUARY, 2012), dateWithYear(4, JANUARY, 2012), dateWithYear(5, JANUARY, 2012), dateWithYear(6, JANUARY, 2012), dateWithYear(7, JANUARY, 2012));
        schedule.assertNoAlerts("Female sterilization Followup 1", max);

        schedule.assertNoAlerts("Female sterilization Followup 2", earliest);
        schedule.assertAlerts("Female sterilization Followup 2", due, dateWithYear(8, JANUARY, 2012), dateWithYear(9, JANUARY, 2012));
        schedule.assertAlertsStartWith("Female sterilization Followup 2", late, dateWithYear(10, JANUARY, 2012), dateWithYear(11, JANUARY, 2012),
                dateWithYear(12, JANUARY, 2012), dateWithYear(13, JANUARY, 2012), dateWithYear(14, JANUARY, 2012), dateWithYear(15, JANUARY, 2012));
        schedule.assertNoAlerts("Female sterilization Followup 2", max);

        schedule.assertNoAlerts("Female sterilization Followup 3", earliest);
        schedule.assertAlertsStartWith("Female sterilization Followup 3", due, dateWithYear(1, FEBRUARY, 2012), dateWithYear(2, FEBRUARY, 2012));
        schedule.assertAlertsStartWith("Female sterilization Followup 3", late, dateWithYear(8, FEBRUARY, 2012), dateWithYear(9, FEBRUARY, 2012),
                dateWithYear(10, FEBRUARY, 2012), dateWithYear(11, FEBRUARY, 2012), dateWithYear(12, FEBRUARY, 2012), dateWithYear(13, FEBRUARY, 2012));
        schedule.assertNoAlerts("Female sterilization Followup 3", max);

        visualization.outputTo("ec-female-sterilization-followup.html", 1);
    }

    @Test
    public void shouldProvideAlertsForMaleSterilizationFollowupAtTheRightTimes() throws Exception {
        schedule.enrollFor("Male sterilization Followup", newDate(2012, JANUARY, 1), new Time(14, 0));

        schedule.assertNoAlerts("Male sterilization Followup 1", earliest);
        schedule.assertAlerts("Male sterilization Followup 1", due, dateWithYear(1, JANUARY, 2012), dateWithYear(2, JANUARY, 2012));
        schedule.assertAlertsStartWith("Male sterilization Followup 1", late, dateWithYear(3, JANUARY, 2012), dateWithYear(4, JANUARY, 2012),
                dateWithYear(5, JANUARY, 2012), dateWithYear(6, JANUARY, 2012), dateWithYear(7, JANUARY, 2012), dateWithYear(8, JANUARY, 2012), dateWithYear(9, JANUARY, 2012));
        schedule.assertNoAlerts("Male sterilization Followup 1", max);

        schedule.assertNoAlerts("Male sterilization Followup 2", earliest);
        schedule.assertAlertsStartWith("Male sterilization Followup 2", due, dateWithYear(1, APRIL, 2012), dateWithYear(2, APRIL, 2012), dateWithYear(3, APRIL, 2012));
        schedule.assertAlertsStartWith("Male sterilization Followup 2", late, dateWithYear(8, APRIL, 2012), dateWithYear(9, APRIL, 2012),
                dateWithYear(10, APRIL, 2012), dateWithYear(11, APRIL, 2012), dateWithYear(12, APRIL, 2012), dateWithYear(13, APRIL, 2012), dateWithYear(14, APRIL, 2012));
        schedule.assertNoAlerts("Male sterilization Followup 2", max);

        visualization.outputTo("ec-male-sterilization-followup.html", 1);
    }

    @Test
    public void shouldProvideAlertsForIUDFollowupAtTheRightTimes() throws Exception {
        schedule.enrollFor("IUD Followup", newDate(2012, JANUARY, 1), new Time(14, 0));

        schedule.assertAlertsStartWith("IUD Followup 1", earliest, dateWithYear(1, JANUARY, 2012), dateWithYear(2, JANUARY, 2012), dateWithYear(3, JANUARY, 2012));
        schedule.assertAlertsStartWith("IUD Followup 1", due, dateWithYear(1, FEBRUARY, 2012), dateWithYear(2, FEBRUARY, 2012), dateWithYear(3, FEBRUARY, 2012));
        schedule.assertAlertsStartWith("IUD Followup 1", late, dateWithYear(8, FEBRUARY, 2012), dateWithYear(9, FEBRUARY, 2012),
                dateWithYear(10, FEBRUARY, 2012), dateWithYear(11, FEBRUARY, 2012), dateWithYear(12, FEBRUARY, 2012), dateWithYear(13, FEBRUARY, 2012));
        schedule.assertNoAlerts("IUD Followup 1", max);

        schedule.assertNoAlerts("IUD Followup 2", earliest);
        schedule.assertAlertsStartWith("IUD Followup 2", due, dateWithYear(1, MAY, 2012), dateWithYear(2, MAY, 2012), dateWithYear(3, MAY, 2012));
        schedule.assertAlertsStartWith("IUD Followup 2", late, dateWithYear(8, MAY, 2012), dateWithYear(9, MAY, 2012), dateWithYear(10, MAY, 2012)
                , dateWithYear(11, MAY, 2012), dateWithYear(12, MAY, 2012), dateWithYear(13, MAY, 2012), dateWithYear(14, MAY, 2012), dateWithYear(15, MAY, 2012));
        schedule.assertNoAlerts("IUD Followup 2", max);

        visualization.outputTo("ec-iud-followup.html", 1);
    }

    @Test
    public void shouldProvideAlertsForFPFollowupAtTheRightTimes() throws Exception {
        schedule.enrollFor("FP Followup", newDate(2012, JANUARY, 1), new Time(14, 0));

        schedule.assertAlerts("FP Followup", earliest, dateWithYear(1, JANUARY, 2012), dateWithYear(2, JANUARY, 2012), dateWithYear(3, JANUARY, 2012));
        schedule.assertAlerts("FP Followup", due, dateWithYear(4, JANUARY, 2012), dateWithYear(5, JANUARY, 2012), dateWithYear(6, JANUARY, 2012), dateWithYear(7, JANUARY, 2012));
        schedule.assertAlertsStartWith("FP Followup", late, dateWithYear(8, JANUARY, 2012), dateWithYear(9, JANUARY, 2012), dateWithYear(10, JANUARY, 2012),
                dateWithYear(11, JANUARY, 2012), dateWithYear(12, JANUARY, 2012), dateWithYear(13, JANUARY, 2012), dateWithYear(14, JANUARY, 2012));
        schedule.assertNoAlerts("FP Followup", max);

        visualization.outputTo("ec-fp-followup.html", 1);
    }

    @Test
    public void shouldProvideAlertsForFPReferralFollowupAtTheRightTimes() throws Exception {
        schedule.enrollFor("FP Referral Followup", newDate(2012, JANUARY, 1), new Time(14, 0));

        schedule.assertAlerts("FP Referral Followup", earliest, dateWithYear(1, JANUARY, 2012));
        schedule.assertAlerts("FP Referral Followup", due, dateWithYear(2, JANUARY, 2012));
        schedule.assertAlertsStartWith("FP Referral Followup", late, dateWithYear(3, JANUARY, 2012), dateWithYear(4, JANUARY, 2012),
                dateWithYear(5, JANUARY, 2012), dateWithYear(6, JANUARY, 2012), dateWithYear(7, JANUARY, 2012), dateWithYear(8, JANUARY, 2012));
        schedule.assertNoAlerts("FP Referral Followup", max);

        visualization.outputTo("ec-fp-referral-followup.html", 1);
    }

    @Test
    @Ignore //TODO revise schedule
    public void shouldProvideAlertsForTT1AtTheRightTimes() throws Exception {
        schedule.enrollFor("TT 1", newDate(2012, JANUARY, 1), new Time(14, 0));

        schedule.assertNoAlerts("TT 1", earliest);
        schedule.assertAlertsStartWith("TT 1", due, dateWithYear(1, JANUARY, 2012), dateWithYear(2, JANUARY, 2012), dateWithYear(3, JANUARY, 2012), dateWithYear(4, JANUARY, 2012));
        schedule.assertAlertsStartWith("TT 1", late, dateWithYear(25, MARCH, 2012), dateWithYear(26, MARCH, 2012), dateWithYear(27, MARCH, 2012), dateWithYear(28, MARCH, 2012));
        schedule.assertNoAlerts("TT 1", max);

        visualization.outputTo("mother-tt-1.html", 1);
    }

    @Test
    @Ignore //TODO revise schedule
    public void shouldProvideAlertsForTT2AtTheRightTimes() throws Exception {
        schedule.enrollFor("TT 2", newDate(2012, JANUARY, 1), new Time(14, 0));

        schedule.assertAlertsStartWith("TT 2", earliest, dateWithYear(1, JANUARY, 2012), dateWithYear(2, JANUARY, 2012), dateWithYear(3, JANUARY, 2012));
        schedule.assertAlertsStartWith("TT 2", due, dateWithYear(29, JANUARY, 2012), dateWithYear(30, JANUARY, 2012), dateWithYear(31, JANUARY, 2012));
        schedule.assertAlertsStartWith("TT 2", late, dateWithYear(12, FEBRUARY, 2012), dateWithYear(13, FEBRUARY, 2012), dateWithYear(14, FEBRUARY, 2012));
        schedule.assertNoAlerts("TT 2", max);

        visualization.outputTo("mother-tt-2.html", 1);
    }

    @Test
    public void shouldProvideAlertsForIFA1() throws Exception {
        schedule.enrollFor("IFA 1", newDate(2012, 1, 1), new Time(14, 0));

        schedule.assertAlertsStartWith("IFA 1", earliest, date(1, JANUARY), date(2, JANUARY), date(3, JANUARY));
        schedule.assertAlertsStartWith("IFA 1", due, date(8, APRIL), date(9, APRIL), date(10, APRIL), date(11, APRIL));
        schedule.assertAlertsStartWith("IFA 1", late, date(22, APRIL), date(23, APRIL), date(24, APRIL), date(25, APRIL));
        schedule.assertNoAlerts("IFA 1", max);

        visualization.outputTo("mother-ifa-1.html", 1);
    }

    @Test
    public void shouldProvideAlertsForIFA2() throws Exception {
        schedule.enrollFor("IFA 2", newDate(2012, 1, 1), new Time(14, 0));

        schedule.assertAlertsStartWith("IFA 2", earliest, date(1, JANUARY), date(2, JANUARY), date(3, JANUARY));
        schedule.assertAlertsStartWith("IFA 2", due, date(1, FEBRUARY), date(2, FEBRUARY), date(3, FEBRUARY), date(4, FEBRUARY));
        schedule.assertAlertsStartWith("IFA 2", late, date(15, FEBRUARY), date(16, FEBRUARY), date(17, FEBRUARY), date(18, FEBRUARY), date(19, FEBRUARY));
        schedule.assertNoAlerts("IFA 2", max);

        visualization.outputTo("mother-ifa-2.html", 1);
    }

    @Test
    public void shouldProvideAlertsForIFA3() throws Exception {
        schedule.enrollFor("IFA 3", newDate(2012, 1, 1), new Time(14, 0));

        schedule.assertAlertsStartWith("IFA 3", earliest, date(1, JANUARY), date(2, JANUARY), date(3, JANUARY));
        schedule.assertAlertsStartWith("IFA 3", due, date(1, FEBRUARY), date(2, FEBRUARY), date(3, FEBRUARY), date(4, FEBRUARY));
        schedule.assertAlertsStartWith("IFA 3", late, date(15, FEBRUARY), date(16, FEBRUARY), date(17, FEBRUARY), date(18, FEBRUARY));
        schedule.assertNoAlerts("IFA 3", max);

        visualization.outputTo("mother-ifa-3.html", 1);
    }

    @Test
    public void shouldProvideAlertsForHbTest1AtTheRightTimes() throws Exception {
        schedule.enrollFor("Hb Test 1", newDate(2012, JANUARY, 1), new Time(14, 0));

        schedule.assertNoAlerts("Hb Test 1", earliest);
        schedule.assertAlertsStartWith("Hb Test 1", due, dateWithYear(1, JANUARY, 2012), dateWithYear(2, JANUARY, 2012), dateWithYear(3, JANUARY, 2012),
                dateWithYear(4, JANUARY, 2012), dateWithYear(5, JANUARY, 2012), dateWithYear(6, JANUARY, 2012));
        schedule.assertAlertsStartWith("Hb Test 1", late, dateWithYear(25, MARCH, 2012), dateWithYear(26, MARCH, 2012), dateWithYear(27, MARCH, 2012),
                dateWithYear(28, MARCH, 2012));
        schedule.assertNoAlerts("Hb Test 1", max);

        visualization.outputTo("mother-hb-test-1.html", 1);
    }

    @Test
    public void shouldProvideAlertsForHbFollowupTestAtTheRightTimes() throws Exception {
        schedule.enrollFor("Hb Followup Test", newDate(2012, JANUARY, 1), new Time(14, 0));

        schedule.assertAlertsStartWith("Hb Followup Test", earliest, dateWithYear(1, JANUARY, 2012), dateWithYear(2, JANUARY, 2012),
                dateWithYear(3, JANUARY, 2012), dateWithYear(4, JANUARY, 2012));
        schedule.assertAlertsStartWith("Hb Followup Test", due, dateWithYear(1, FEBRUARY, 2012), dateWithYear(2, FEBRUARY, 2012),
                dateWithYear(3, FEBRUARY, 2012), dateWithYear(4, FEBRUARY, 2012));
        schedule.assertAlertsStartWith("Hb Followup Test", late, dateWithYear(15, FEBRUARY, 2012), dateWithYear(16, FEBRUARY, 2012), dateWithYear(17, FEBRUARY, 2012));
        schedule.assertNoAlerts("Hb Followup Test", max);

        visualization.outputTo("mother-hb-followup-test.html", 1);
    }

    @Test
    public void shouldProvideAlertsForHbTest2AtTheRightTimes() throws Exception {
        schedule.enrollFor("Hb Test 2", newDate(2012, JANUARY, 1), new Time(14, 0));

        schedule.assertNoAlerts("Hb Test 2", earliest);
        schedule.assertAlertsStartWith("Hb Test 2", due, dateWithYear(15, JULY, 2012), dateWithYear(16, JULY, 2012), dateWithYear(17, JULY, 2012), dateWithYear(18, JULY, 2012));
        schedule.assertAlertsStartWith("Hb Test 2", late, dateWithYear(29, JULY, 2012), dateWithYear(30, JULY, 2012), dateWithYear(31, JULY, 2012));
        schedule.assertNoAlerts("Hb Test 2", max);

        visualization.outputTo("mother-hb-test-2.html", 1);
    }

    @Test
    public void shouldProvideAlertsForDeliveryPlanAtTheRightTimes() throws Exception {
        schedule.enrollFor("Delivery Plan", newDate(2012, JANUARY, 1), new Time(14, 0));

        schedule.assertAlertsStartWith("Delivery Plan", earliest, dateWithYear(1, JANUARY, 2012), dateWithYear(2, JANUARY, 2012), dateWithYear(3, JANUARY, 2012), dateWithYear(4, JANUARY, 2012));
        schedule.assertAlertsStartWith("Delivery Plan", due, dateWithYear(26, AUGUST, 2012), dateWithYear(27, AUGUST, 2012), dateWithYear(28, AUGUST, 2012), dateWithYear(29, AUGUST, 2012));
        schedule.assertAlertsStartWith("Delivery Plan", late, dateWithYear(9, SEPTEMBER, 2012), dateWithYear(10, SEPTEMBER, 2012), dateWithYear(11, SEPTEMBER, 2012));
        schedule.assertNoAlerts("Delivery Plan", max);

        visualization.outputTo("mother-delivery-plan.html", 1);
    }

    @Before
    public void setUp() throws Exception {
        FakeSchedule fakeSchedule = new FakeSchedule(trackingService, schedulerFactoryBean, new SetDateAction() {
            @Override
            public void setTheDateTo(LocalDate date) {
                mockCurrentDate(date);
                fakeIt(date);
            }
        });

        String outputDir = null;
        if (new File("opensrp-web").exists()) {
            outputDir = "opensrp-web/doc/schedules/";
        } else if (new File("doc").exists()) {
            outputDir = "doc/schedules/";
        }
        visualization = new ScheduleVisualization(fakeSchedule, outputDir);

        schedule = new ScheduleWithCapture(fakeSchedule, visualization);
    }

    @BeforeClass
    public static void turnOffSpringLogging() {
        Logger logger = Logger.getLogger("org.springframework");
        logger.setLevel(Level.FATAL);
    }

    private Date date(int day, int month) {
        return dateWithYear(day, month, 2012);
    }

    private Date dateWithYear(int day, int month, int year) {
        return new DateTime(year, month, day, 14, 0).toDate();
    }
}
