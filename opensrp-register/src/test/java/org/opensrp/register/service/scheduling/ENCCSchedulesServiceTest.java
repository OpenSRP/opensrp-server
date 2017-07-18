package org.opensrp.register.service.scheduling;

import static org.mockito.Mockito.times;
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
import org.mockito.Mock;
import org.mockito.Mockito;
import org.opensrp.common.util.TestLoggerAppender;
import org.opensrp.register.service.handler.TestResourceLoader;
import org.opensrp.scheduler.HealthSchedulerService;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;


@RunWith(PowerMockRunner.class)
@PowerMockIgnore({ "org.apache.log4j.*", "org.apache.commons.logging.*" })
public class ENCCSchedulesServiceTest extends TestResourceLoader {
	
    private ENCCSchedulesService enccSchedulesService;
    @Mock
    private HealthSchedulerService scheduler;
    @Before
    public void setUp() throws Exception {
        initMocks(this);
        enccSchedulesService = new ENCCSchedulesService(scheduler);
    }
    
    @Test
    public void shouldTestELCOScheduleServiceMethods() {    	
        final TestLoggerAppender appender = new TestLoggerAppender();       
        final Logger logger = Logger.getLogger(ENCCSchedulesService.class.toString());
        logger.setLevel(Level.ALL);
        logger.addAppender(appender);
        
        enccSchedulesService.enrollIntoCorrectMilestoneOfENCCCare(entityId,scheduleName, LocalDate.now(),
            eventId);   	
        Mockito.verify(scheduler,times(1)).enrollIntoSchedule(entityId, scheduleName,LocalDate.now().toString(), eventId);
       
        enccSchedulesService.fullfillMilestone(entityId, provider,scheduleName, LocalDate.now(),
            eventId);
        Mockito.verify(scheduler,times(1)).fullfillMilestoneAndCloseAlert(entityId, provider, scheduleName, LocalDate.now(), eventId);
        
        enccSchedulesService.unEnrollFromSchedule(entityId, provider, scheduleName,eventId);
        Mockito.verify(scheduler,times(1)).unEnrollFromSchedule(entityId, provider, scheduleName, eventId);

        final List<LoggingEvent> log = appender.getLog();
        final LoggingEvent firstLogEntry = log.get(0);
        Assert.assertEquals(firstLogEntry.getRenderedMessage(), "Enrolling with Entity id:entityId1 to ENCC schedule, milestone: opv 1.");
        final LoggingEvent secondLogEntry = log.get(1);
        Assert.assertEquals(secondLogEntry.getRenderedMessage(), "fullfillMilestone with id: :entityId1"); 
        final LoggingEvent thirdLogEntry = log.get(2);
        Assert.assertEquals(thirdLogEntry.getRenderedMessage(), "Un-enrolling ENCC with Entity id:entityId1 from schedule: opv 1"); 
        logger.removeAllAppenders();
    }
    
    @Test
    public void shouldGetException() {
        Mockito.doThrow(new RuntimeException()).when(scheduler).fullfillMilestoneAndCloseAlert(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.any(LocalDate.class), Mockito.anyString());
        enccSchedulesService.fullfillMilestone("", "", "", null,"");
    }
    
}
