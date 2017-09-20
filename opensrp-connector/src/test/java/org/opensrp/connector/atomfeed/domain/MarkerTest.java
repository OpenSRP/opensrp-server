package org.opensrp.connector.atomfeed.domain;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.MockitoAnnotations.initMocks;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore({ "org.apache.log4j.*", "org.apache.commons.logging.*" })
public class MarkerTest {
	
	@Before
	public void setUp() throws Exception {
		initMocks(this);
		
	}
	
	@Test
	public void testConstructorSetetrAndGetter() throws URISyntaxException {
		URI feedUri = new URI("/ddd/ddd");
		String lastReadEntryId = "qwewewe-it-23243";
		URI feedURIForLastReadEntry = new URI("dff/ggg");
		org.ict4h.atomfeed.client.domain.Marker marker = new org.ict4h.atomfeed.client.domain.Marker(feedUri,
		        lastReadEntryId, feedURIForLastReadEntry);
		Marker m = new Marker(marker);
		assertNotNull(m);
		String expectedFeedURI = "api/v1/marker";
		m.setFeedUri(expectedFeedURI);
		String expectedFeedURIForLastReadEntry = "weerrt-44555-6666";
		m.setFeedURIForLastReadEntry(expectedFeedURIForLastReadEntry);
		String expectedLastReadEntryId = "3456-mjui-hyyy";
		m.toMarker();
		m.setLastReadEntryId(expectedLastReadEntryId);
		assertEquals(expectedFeedURIForLastReadEntry, m.getFeedURIForLastReadEntry());
		assertEquals(expectedLastReadEntryId, m.getLastReadEntryId());
		assertEquals(expectedFeedURI, m.getFeedUri());
		
	}
}
