package org.opensrp.connector.atomfeed.it;

import static junit.framework.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.ektorp.CouchDbInstance;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbConnector;
import org.ektorp.impl.StdCouchDbInstance;
import org.ektorp.impl.StdObjectMapperFactory;
import org.ict4h.atomfeed.client.domain.Event;
import org.ict4h.atomfeed.client.domain.FailedEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opensrp.connector.atomfeed.AllFailedEventsCouchImpl;
import org.opensrp.connector.openmrs.service.TestResourceLoader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-applicationContext-opensrp-connector.xml")
public class AllFailedEventsCouchImplTest extends TestResourceLoader {
	
	private AllFailedEventsCouchImpl allFailedEventsCouchImpl;
	
	private CouchDbInstance dbInstance;
	
	private StdCouchDbConnector stdCouchDbConnector;
	
	String feedUri = "feedUri";
	
	String feedUriId = "feedUriId";
	
	String entryId = "entry";
	
	public AllFailedEventsCouchImplTest() throws IOException {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@Before
	public void setup() {
		HttpClient httpClient = new StdHttpClient.Builder().host("localhost").port(5984).username(couchDBUserName)
		        .password(couchDBPassword).socketTimeout(1000).build();
		dbInstance = new StdCouchDbInstance(httpClient);
		
		stdCouchDbConnector = new StdCouchDbConnector("atomfeed", dbInstance, new StdObjectMapperFactory());
		
		stdCouchDbConnector.createDatabaseIfNotExists();
		allFailedEventsCouchImpl = new AllFailedEventsCouchImpl(1, stdCouchDbConnector);
		
	}
	
	@Test
	public void testAddOrUpdate() {
		String feedUri = "apis/feed/patient";
		Event event = new Event(entryId, "/apis/v2/some");
		String errorMessage = "error";
		int retries = 0;
		org.ict4h.atomfeed.client.domain.FailedEvent expectedFailedEvent = new org.ict4h.atomfeed.client.domain.FailedEvent(
		        feedUri, event, errorMessage, retries);
		
		allFailedEventsCouchImpl.addOrUpdate(expectedFailedEvent);
		allFailedEventsCouchImpl.addOrUpdate(expectedFailedEvent);
		org.ict4h.atomfeed.client.domain.FailedEvent actualFailedEvent = allFailedEventsCouchImpl
		        .get(feedUri, event.getId());
		
		allFailedEventsCouchImpl.remove(expectedFailedEvent);
		assertEquals(expectedFailedEvent.toString(), actualFailedEvent.toString());
	}
	
	@Test(expected = NullPointerException.class)
	public void testGetNullPointerException() {
		
		org.ict4h.atomfeed.client.domain.FailedEvent actualFailedEvent = allFailedEventsCouchImpl.get(feedUri, feedUriId);
		
	}
	
	@Test(expected = RuntimeException.class)
	public void testRemove() {
		allFailedEventsCouchImpl.remove(null);
		
	}
	
	@Test
	public void testGetOldestNFailedEvents() {
		
		String feedUri = "apis/feed/patient";
		Event event = new Event(entryId, "/apis/v2/somes");
		String errorMessage = "error";
		int retries = 0;
		org.ict4h.atomfeed.client.domain.FailedEvent failedEvent = new org.ict4h.atomfeed.client.domain.FailedEvent(feedUri,
		        event, errorMessage, retries);
		
		allFailedEventsCouchImpl.addOrUpdate(failedEvent);
		allFailedEventsCouchImpl.addOrUpdate(failedEvent);
		List<FailedEvent> expectedFailedEvent = new ArrayList<>();
		expectedFailedEvent.add(failedEvent);
		
		List<FailedEvent> actualFailedEvent = allFailedEventsCouchImpl.getOldestNFailedEvents(feedUri, 10, 10);
		
		assertEquals(expectedFailedEvent.toString(), actualFailedEvent.toString());
		allFailedEventsCouchImpl.remove(failedEvent);
		
	}
	
	@Test
	public void testGetNumberOfFailedEvents() {
		String feedUri = "apis/feed/patient";
		Event event = new Event(entryId, "/apis/v2/somes");
		String errorMessage = "error";
		int retries = 0;
		org.ict4h.atomfeed.client.domain.FailedEvent failedEvent = new org.ict4h.atomfeed.client.domain.FailedEvent(feedUri,
		        event, errorMessage, retries);
		
		allFailedEventsCouchImpl.addOrUpdate(failedEvent);
		allFailedEventsCouchImpl.addOrUpdate(failedEvent);
		int expectedCount = 1;
		int actualCount = allFailedEventsCouchImpl.getNumberOfFailedEvents(feedUri);
		assertEquals(expectedCount, actualCount);
		allFailedEventsCouchImpl.remove(failedEvent);
	}
}
