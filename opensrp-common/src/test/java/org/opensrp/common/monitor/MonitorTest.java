package org.opensrp.common.monitor;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@RunWith(PowerMockRunner.class)
@PrepareForTest({Probe.class})
public class MonitorTest {

    @Before
    public void setUp() {
        PowerMockito.mockStatic(System.class);
    }

    @Test
    public void probeTest() {
        BDDMockito.given(System.nanoTime()).willReturn(100l, 150l);
        Monitor monitor = new Monitor();
        Probe probe = monitor.start(Metric.REPORTING_ANM_REPORTS_CACHE_TIME);
        long value = probe.value();
        assertEquals(50, value);
    }


    @Test
    public void monitorLoggingTest() {
        final TestAppender appender = new TestAppender();
        final Logger logger = Logger.getLogger(Monitor.LOGGER_NAME);
        logger.addAppender(appender);
        try {
            BDDMockito.given(System.nanoTime()).willReturn(100l, 150l);
            Monitor monitor = new Monitor();
            Probe probe = monitor.start(Metric.REPORTING_ANM_REPORTS_CACHE_TIME);
            monitor.end(probe);
        } finally {
            logger.removeAppender(appender);
        }

        final List<LoggingEvent> log = appender.getLog();
        final LoggingEvent firstLogEntry = log.get(0);
        assertEquals(firstLogEntry.getLevel(), Level.INFO);
        assertTrue(firstLogEntry.getRenderedMessage().contains("50"));
        assertTrue(firstLogEntry.getRenderedMessage().contains(Metric.REPORTING_ANM_REPORTS_CACHE_TIME.name()));
    }
}

class TestAppender extends AppenderSkeleton {
    private final List<LoggingEvent> log = new ArrayList<LoggingEvent>();

    @Override
    public boolean requiresLayout() {
        return false;
    }

    @Override
    protected void append(final LoggingEvent loggingEvent) {
        log.add(loggingEvent);
    }

    @Override
    public void close() {
    }

    public List<LoggingEvent> getLog() {
        return new ArrayList<LoggingEvent>(log);
    }
}
