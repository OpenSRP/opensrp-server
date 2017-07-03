package org.opensrp.web.it;

import static org.mockito.Mockito.*;
import static org.hamcrest.Matcher.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.ektorp.CouchDbConnector;
import org.ict4h.atomfeed.client.AtomFeedProperties;
import org.ict4h.atomfeed.client.domain.Event;
import org.ict4h.atomfeed.client.repository.AllFailedEvents;
import org.ict4h.atomfeed.client.repository.AllFeeds;
import org.ict4h.atomfeed.client.repository.AllMarkers;
import org.ict4h.atomfeed.client.repository.datasource.WebClient;
import org.ict4h.atomfeed.client.service.AtomFeedClient;
import org.ict4h.atomfeed.client.service.EventWorker;
import org.ict4h.atomfeed.jdbc.AtomFeedJdbcTransactionManager;
import org.ict4h.atomfeed.transaction.AFTransactionManager;
import org.ict4h.atomfeed.transaction.AFTransactionWork;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.opensrp.connector.atomfeed.AllFailedEventsCouchImpl;
import org.opensrp.connector.atomfeed.AllFailedEventsInMemoryImpl;
import org.opensrp.connector.atomfeed.AllMarkersCouchImpl;
import org.opensrp.connector.atomfeed.AllMarkersInMemoryImpl;
import org.opensrp.connector.openmrs.EncounterAtomfeed;
import org.opensrp.connector.openmrs.PatientAtomfeed;
import org.opensrp.connector.openmrs.constants.OpenmrsConstants;
import org.opensrp.connector.openmrs.service.EncounterService;
import org.opensrp.repository.AllClients;
import org.opensrp.repository.AllEvents;
import org.opensrp.service.ClientService;
import org.opensrp.service.EventService;
import org.opensrp.web.utils.TestResourceLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/applicationContext-opensrp-web.xml")
public class AtomFeedIntegrationTest extends TestResourceLoader {

	@Autowired
	@Qualifier(OpenmrsConstants.ATOMFEED_DATABASE_CONNECTOR)
	CouchDbConnector cdb;
	
	@Autowired
	ClientService clientService;
	
	@Autowired
	AllClients allClients;
	
	@Autowired
	EventService eventService;
	
	@Autowired
	AllEvents allEvents;

	@Autowired
	private AllMarkersCouchImpl allMarkers;

	@Autowired
	private AllFailedEventsCouchImpl allFailedEvents;
	
	
	@Before
	public void setup(){
		allClients.removeAll();
		allEvents.removeAll();
	}
	
	public AtomFeedIntegrationTest() throws IOException {
		super();
	}

	@Test
    public void shouldReadEventsCreatedEvents() throws URISyntaxException {
		PatientAtomfeed paf = new PatientAtomfeed(allMarkers, allFailedEvents, openmrsOpenmrsUrl, patientService, clientService);

		EncounterAtomfeed eaf = new EncounterAtomfeed(allMarkers, allFailedEvents, openmrsOpenmrsUrl, encounterService, eventService);
		if(pushToOpenmrsForTest){
			paf.processEvents();

			eaf.processEvents();
		}
    }
}
