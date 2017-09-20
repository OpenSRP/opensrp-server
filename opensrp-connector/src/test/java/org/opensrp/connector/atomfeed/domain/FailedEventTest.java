package org.opensrp.connector.atomfeed.domain;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.MockitoAnnotations.initMocks;

import org.ict4h.atomfeed.client.domain.Event;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore({ "org.apache.log4j.*", "org.apache.commons.logging.*" })
public class FailedEventTest {
	
	@Before
	public void setUp() throws Exception {
		initMocks(this);
		
	}
	
	@Test
	public void testConstructorSetetrAndGetter() {
		String feedUri = "api/feed/patient";
		Event event = new Event("entry", "/api/v2/some");
		String errorMessage = "error";
		int retries = 0;
		org.ict4h.atomfeed.client.domain.FailedEvent fEvent = new org.ict4h.atomfeed.client.domain.FailedEvent(feedUri,
		        event, errorMessage, retries);
		FailedEvent failedEvent = new FailedEvent(feedUri, fEvent, errorMessage, retries);
		assertNotNull(failedEvent);
		String expectedErrorMessage = "errorMessage";
		failedEvent.setErrorMessage(expectedErrorMessage);
		assertEquals(expectedErrorMessage, failedEvent.getErrorMessage());
		int exoectedErrorHashCode = 400;
		failedEvent.setErrorHashCode(exoectedErrorHashCode);
		assertEquals(exoectedErrorHashCode, failedEvent.getErrorHashCode());
		
		String expectedTitle = "Failed Event";
		failedEvent.setTitle(expectedTitle);
		assertEquals(expectedTitle, failedEvent.getTitle());
		
		String expectedEventId = "eventId";
		failedEvent.setEventId(expectedEventId);
		assertEquals(expectedEventId, failedEvent.getEventId());
		
		int expectedRetries = 0;
		failedEvent.setRetries(expectedRetries);
		assertEquals(expectedRetries, failedEvent.getRetries());
		
		String expectedCventContent = "eventContent";
		failedEvent.setEventContent(expectedCventContent);
		assertEquals(expectedCventContent, failedEvent.getEventContent());
		
		String expectedFeedUri = "/api/feed/aptient";
		failedEvent.setFeedUri(expectedFeedUri);
		assertEquals(expectedFeedUri, failedEvent.getFeedUri());
		
		long expectedFailedAt = 0;
		failedEvent.setFailedAt(expectedFailedAt);
		assertEquals(expectedFailedAt, failedEvent.getFailedAt());
		failedEvent.toFailedEvent();
		
	}
}
