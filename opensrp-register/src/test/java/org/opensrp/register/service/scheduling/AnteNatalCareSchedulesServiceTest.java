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
import static org.mockito.Mockito.doThrow;;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore({ "org.apache.log4j.*", "org.apache.commons.logging.*" })
public class AnteNatalCareSchedulesServiceTest extends TestResourceLoader {
	
    private AnteNatalCareSchedulesService anteNatalCareSchedulesService;
    @Mock
    private HealthSchedulerService scheduler;
    @Before
    public void setUp() throws Exception {
        initMocks(this);
        anteNatalCareSchedulesService = new AnteNatalCareSchedulesService(scheduler);
    }
    
    @Test
    public void shouldTestAnteNatalCareSchedulesServiceMthods() {
    	
        final TestLoggerAppender appender = new TestLoggerAppender();       
        final Logger logger = Logger.getLogger(AnteNatalCareSchedulesService.class.toString());
        logger.setLevel(Level.ALL);
        logger.addAppender(appender);
        anteNatalCareSchedulesService.enrollMother(entityId, scheduleName, LocalDate.now(), eventId);    	
        InOrder inOrder = inOrder(scheduler);
        inOrder.verify(scheduler).enrollIntoSchedule(entityId,scheduleName, LocalDate.now().toString(),
            "eventID 1");
        anteNatalCareSchedulesService.unEnrollFromAllSchedules(entityId, eventId);
        inOrder.verify(scheduler).unEnrollFromAllSchedules(entityId, eventId);
        anteNatalCareSchedulesService.unEnrollFromSchedule(entityId,provider, scheduleName,eventId);
        inOrder.verify(scheduler).unEnrollFromSchedule(entityId, provider, scheduleName, eventId);
        anteNatalCareSchedulesService.fullfillMilestone(entityId, provider, scheduleName, LocalDate.now(), eventId);
        inOrder.verify(scheduler).fullfillMilestoneAndCloseAlert(entityId, provider, scheduleName,  LocalDate.now(), eventId);
        final List<LoggingEvent> log = appender.getLog();
        final LoggingEvent secondLogEntry = log.get(1);
        Assert.assertEquals(secondLogEntry.getRenderedMessage(), "fullfillMilestone with id: :" + entityId);
        final LoggingEvent firstLogEntry = log.get(0);
        Assert.assertEquals(firstLogEntry.getRenderedMessage(), "Un-enrolling ANC with Entity id:entityId1 from schedule: opv 1");
        logger.removeAllAppenders();
    }
    
    @Test
    public void shouldGetException() {    	
    	Mockito.doThrow(new RuntimeException()).when(scheduler).fullfillMilestoneAndCloseAlert(entityId, provider, scheduleName, null, eventId);
    	anteNatalCareSchedulesService.fullfillMilestone(entityId, provider, scheduleName, null, eventId);
    	
    }
    
}
