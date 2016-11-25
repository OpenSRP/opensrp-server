package org.opensrp.connector.openmrs;

import static org.mockito.Mockito.*;
import static org.hamcrest.Matcher.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.ict4h.atomfeed.client.AtomFeedProperties;
import org.ict4h.atomfeed.client.domain.Event;
import org.ict4h.atomfeed.client.repository.AllFeeds;
import org.ict4h.atomfeed.client.repository.datasource.WebClient;
import org.ict4h.atomfeed.client.service.AtomFeedClient;
import org.ict4h.atomfeed.client.service.EventWorker;
import org.ict4h.atomfeed.jdbc.AtomFeedJdbcTransactionManager;
import org.ict4h.atomfeed.transaction.AFTransactionManager;
import org.ict4h.atomfeed.transaction.AFTransactionWork;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.opensrp.connector.atomfeed.AllFailedEventsInMemoryImpl;
import org.opensrp.connector.atomfeed.AllMarkersInMemoryImpl;
import org.opensrp.connector.openmrs.service.TestResourceLoader;
import org.opensrp.service.ClientService;
import org.opensrp.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;

import com.sun.syndication.feed.atom.Entry;
import com.sun.syndication.feed.atom.Feed;

public class AtomFeedTest extends TestResourceLoader {

	public AtomFeedTest() throws IOException {
		super();
	}
	
	@Mock
	ClientService cs;
	
	@Mock
	EventService es;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Ignore
	@Test
    public void shouldReadEventsCreatedEvents() throws URISyntaxException {
		WebClient wc = Mockito.mock(WebClient.class);
		Map<String, String> m = any();
		when(wc.fetch(any(URI.class), any(AtomFeedProperties.class), m)).thenReturn("<?xml version=\"1.0\" encoding=\"UTF-8\"?> <feed xmlns=\"http://www.w3.org/2005/Atom\">   <title>Patient AOP</title>   <link rel=\"self\" type=\"application/atom+xml\" href=\"http://localhost:8181/openmrs/atomfeed/patient/recent.form\" />   <link rel=\"via\" type=\"application/atom+xml\" href=\"http://localhost:8181/openmrs/atomfeed/patient/1\" />   <author>     <name>OpenMRS</name>   </author>   <id>bec795b1-3d17-451d-b43e-a094019f6984+1</id>   <generator uri=\"https://github.com/ICT4H/atomfeed\">OpenMRS Feed Publisher</generator>   <updated>2016-03-09T14:47:58Z</updated>   <entry>     <title>Patient</title>     <category term=\"patient\" />     <id>tag:atomfeed.ict4h.org:f376d71e-6ddd-465e-b224-bbe624cbf97f</id>     <updated>2016-03-09T14:47:58Z</updated>     <published>2016-03-09T14:47:58Z</published>     <content type=\"application/vnd.atomfeed+xml\"><![CDATA[/openmrs/ws/rest/v1/patient/42034a79-48bc-47d0-a255-139031665581?v=full]]></content>   </entry> </feed> ");

		PatientAtomfeed paf = new PatientAtomfeed(new AllMarkersInMemoryImpl(), new AllFailedEventsInMemoryImpl(), openmrsOpenmrsUrl, patientService, cs);

		if(pushToOpenmrsForTest){
			paf.processEvents();
		}
        
		EncounterAtomfeed eaf = new EncounterAtomfeed(new AllMarkersInMemoryImpl(), new AllFailedEventsInMemoryImpl(), openmrsOpenmrsUrl, encounterService, es);
		if(pushToOpenmrsForTest){
			eaf.processEvents();
		}
    }
}
