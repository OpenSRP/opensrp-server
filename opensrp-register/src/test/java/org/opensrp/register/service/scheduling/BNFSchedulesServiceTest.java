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
public class BNFSchedulesServiceTest extends TestResourceLoader {
	
    private BNFSchedulesService bnfSchedulesService;
    @Mock
    private HealthSchedulerService scheduler;
    @Mock
    private AllActions allActions;
    @Before
    public void setUp() throws Exception {
        initMocks(this);
        bnfSchedulesService = new BNFSchedulesService(scheduler,allActions);
    }
    
    @Test
    public void shouldTestAnteNatalCareSchedulesServiceMthods() {
    	
        final TestLoggerAppender appender = new TestLoggerAppender();       
        final Logger logger = Logger.getLogger(BNFSchedulesService.class.toString());
        logger.setLevel(Level.ALL);
        logger.addAppender(appender);       
        bnfSchedulesService.enrollBNF(entityId, scheduleName, LocalDate.now(), eventId);
        Mockito.verify(scheduler,times(1)).enrollIntoSchedule(entityId, scheduleName, LocalDate.now(), eventId);        
        final List<LoggingEvent> log = appender.getLog();       
        final LoggingEvent firstLogEntry = log.get(0);
        Assert.assertEquals(firstLogEntry.getRenderedMessage(), "Enrolling Mother into BNF schedule. Id: eventID 1");
        logger.removeAllAppenders();
    }    
    
}
