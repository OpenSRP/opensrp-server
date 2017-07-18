package org.opensrp.register.service.scheduling;

import static org.mockito.MockitoAnnotations.initMocks;

import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.bouncycastle.util.Times;
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
import org.opensrp.scheduler.repository.AllActions;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;
import static org.mockito.Mockito.times;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore({ "org.apache.log4j.*", "org.apache.commons.logging.*" })
public class HHSchedulesServiceTest extends TestResourceLoader {
	
    private HHSchedulesService hhSchedulesService;
    @Mock
    private HealthSchedulerService scheduler;    
    @Before
    public void setUp() throws Exception {
        initMocks(this);
        hhSchedulesService = new HHSchedulesService(scheduler);
    }
    
    @Test
    public void shouldTestAnteNatalCareSchedulesServiceMthods() {
    	
        final TestLoggerAppender appender = new TestLoggerAppender();       
        final Logger logger = Logger.getLogger(HHSchedulesService.class.toString());
        logger.setLevel(Level.ALL);
        logger.addAppender(appender);       
        hhSchedulesService.enrollIntoMilestoneOfCensus(entityId,LocalDate.now().toString(), provider,
            scheduleName,eventId);
        Mockito.verify(scheduler,times(1)).enrollIntoSchedule(entityId, scheduleName, LocalDate.now().toString(), eventId);
        
        hhSchedulesService.fullfillMilestone(entityId, provider, scheduleName, LocalDate.now(), eventId);
        Mockito.verify(scheduler,times(1)).fullfillMilestoneAndCloseAlert(entityId, provider, scheduleName, LocalDate.now(), eventId);
        final List<LoggingEvent> log = appender.getLog();       
        final LoggingEvent firstLogEntry = log.get(0);
        Assert.assertEquals(firstLogEntry.getRenderedMessage(), "Enrolling household into Census schedule. Id: entityId1");
        logger.removeAllAppenders();
    }    
    
}
