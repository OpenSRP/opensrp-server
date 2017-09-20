package org.opensrp.connector.atomfeed.it;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotSame;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.ektorp.CouchDbInstance;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbConnector;
import org.ektorp.impl.StdCouchDbInstance;
import org.ektorp.impl.StdObjectMapperFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opensrp.connector.atomfeed.AllMarkersCouchImpl;
import org.opensrp.connector.atomfeed.domain.Marker;
import org.opensrp.connector.openmrs.service.TestResourceLoader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-applicationContext-opensrp-connector.xml")
public class AllMarkersCouchImplTest extends TestResourceLoader {
	
	public AllMarkersCouchImplTest() throws IOException {
		super();
		// TODO Auto-generated constructor stub
	}
	
	private AllMarkersCouchImpl allMarkersCouchImpl;
	
	private CouchDbInstance dbInstance;
	
	private StdCouchDbConnector stdCouchDbConnector;
	
	String str = "2233-fhghghg-888";
	
	@Before
	public void setup() throws URISyntaxException {
		HttpClient httpClient = new StdHttpClient.Builder().host("localhost").port(5984).username(couchDBUserName)
		        .password(couchDBPassword).socketTimeout(1000).build();
		dbInstance = new StdCouchDbInstance(httpClient);
		
		stdCouchDbConnector = new StdCouchDbConnector("atomfeed", dbInstance, new StdObjectMapperFactory());
		
		stdCouchDbConnector.createDatabaseIfNotExists();
		allMarkersCouchImpl = new AllMarkersCouchImpl(1, stdCouchDbConnector);
		
	}
	
	@Test
	public void testFindByfeedUri() throws URISyntaxException {
		URI feedUri = new URI("/apis/patient");
		String expectedEntryId = "qwewewse-fgg-hhhh";
		URI expectedFeedURIForLastReadEntry = new URI(str);
		
		allMarkersCouchImpl.put(feedUri, expectedEntryId, expectedFeedURIForLastReadEntry);
		allMarkersCouchImpl.put(feedUri, expectedEntryId, expectedFeedURIForLastReadEntry);
		String expectedFeedUri = "/apis/patient";
		Marker marker = allMarkersCouchImpl.findByfeedUri(expectedFeedUri);
		assertEquals(expectedFeedUri, marker.getFeedUri());
		assertEquals(expectedEntryId, marker.getLastReadEntryId());
		assertNotSame("expectedEntryId", marker.getLastReadEntryId());
		allMarkersCouchImpl.removeAll();
	}
	
	@Test
	public void testGet() throws URISyntaxException {
		URI feedUri = new URI("/apis/patient");
		String expectedEntryId = "qweweswe-fgg-hhhh";
		URI expectedFeedURIForLastReadEntry = new URI(str);
		
		allMarkersCouchImpl.put(feedUri, expectedEntryId, expectedFeedURIForLastReadEntry);
		allMarkersCouchImpl.put(feedUri, expectedEntryId, expectedFeedURIForLastReadEntry);
		org.ict4h.atomfeed.client.domain.Marker marker = allMarkersCouchImpl.get(feedUri);
		assertEquals(feedUri, marker.getFeedUri());
		assertEquals(expectedEntryId, marker.getLastReadEntryId());
		assertNotSame("expectedEntryId", marker.getLastReadEntryId());
		allMarkersCouchImpl.removeAll();
	}
	
	@Test
	public void testFindAllMarkers() throws URISyntaxException {
		URI feedUri = new URI("/apis/patient");
		String expectedEntryId = "qwewegwe-fgg-hhhh";
		URI expectedFeedURIForLastReadEntry = new URI(str);
		
		allMarkersCouchImpl.put(feedUri, expectedEntryId, expectedFeedURIForLastReadEntry);
		allMarkersCouchImpl.put(feedUri, expectedEntryId, expectedFeedURIForLastReadEntry);
		List<Marker> marker = allMarkersCouchImpl.findAllMarkers();
		int expectedSize = 1;
		assertEquals(expectedSize, marker.size());
		expectedSize = -2;
		assertNotSame(expectedSize, marker.size());
		allMarkersCouchImpl.removeAll();
	}
}
