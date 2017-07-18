package org.opensrp.register.service.scheduling;

import static org.mockito.Mockito.inOrder;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.opensrp.common.util.TestLoggerAppender;
import org.opensrp.register.service.handler.TestResourceLoader;
import org.opensrp.scheduler.HealthSchedulerService;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;


@RunWith(PowerMockRunner.class)
@PowerMockIgnore({ "org.apache.log4j.*", "org.apache.commons.logging.*" })
public class ElcoSchedulesServiceTest extends TestResourceLoader {
	
    private ELCOScheduleService elcoScheduleService;
    @Mock
    private HealthSchedulerService scheduler;
    @Before
    public void setUp() throws Exception {
        initMocks(this);
        elcoScheduleService = new ELCOScheduleService(scheduler);
    }
    
    @Test
    public void shouldTestELCOScheduleServiceMethods() {
    	
        final TestLoggerAppender appender = new TestLoggerAppender();       
        final Logger logger = Logger.getLogger(ELCOScheduleService.class.toString());
        logger.setLevel(Level.ALL);
        logger.addAppender(appender);
        
        elcoScheduleService.enrollIntoMilestoneOfMisElco(caseId, "2017-02-02",eventId,scheduleName) ;   	
        Mockito.verify(scheduler,times(1)).enrollIntoSchedule(caseId, scheduleName, "2017-02-02", eventId);
       
        elcoScheduleService.imediateEnrollIntoMilestoneOfPSRF(caseId, "2017-02-02", provider, scheduleName,eventId);
        Mockito.verify(scheduler,times(2)).enrollIntoSchedule(caseId, scheduleName, "2017-02-02", eventId);
        
        elcoScheduleService.unEnrollFromScheduleCensus(caseId,provider,scheduleName,eventId);
        Mockito.verify(scheduler,times(1)).fullfillMilestoneAndCloseAlert(caseId, provider, scheduleName, new LocalDate(), eventId);
       
        elcoScheduleService.unEnrollFromScheduleOfPSRF(caseId, provider, scheduleName, eventId);
        Mockito.verify(scheduler,times(2)).fullfillMilestoneAndCloseAlert(caseId, provider, scheduleName, new LocalDate(), eventId);
        
        elcoScheduleService.fullfillMilestone(entityId, provider, scheduleName, LocalDate.now(),
            eventId);
        Mockito.verify(scheduler).fullfillMilestoneAndCloseAlert(entityId, provider, scheduleName, LocalDate.now(), eventId);
        
        final List<LoggingEvent> log = appender.getLog();
        final LoggingEvent firstLogEntry = log.get(0);
        Assert.assertEquals(firstLogEntry.getRenderedMessage(), "Enrolling Elco into MisElco schedule. Id: caseId");
        final LoggingEvent secondLogEntry = log.get(1);
        Assert.assertEquals(secondLogEntry.getRenderedMessage(), "Enrolling Elco into PSRF schedule. Id: caseId");        
        logger.removeAllAppenders();
    }
    
    @Test
    public void shouldGetException() {
        Mockito.doThrow(new RuntimeException()).when(scheduler).fullfillMilestoneAndCloseAlert(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.any(LocalDate.class), Mockito.anyString());
        elcoScheduleService.fullfillMilestone("", "", "", null,"");
        elcoScheduleService.unEnrollFromScheduleCensus("","","","");
        elcoScheduleService.unEnrollFromScheduleOfPSRF(caseId, provider, scheduleName, eventId);

    	
    }
    
}
