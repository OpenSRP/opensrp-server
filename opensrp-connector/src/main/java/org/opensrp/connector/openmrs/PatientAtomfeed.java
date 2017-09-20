package org.opensrp.connector.openmrs;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;
import org.ict4h.atomfeed.client.AtomFeedProperties;
import org.ict4h.atomfeed.client.domain.Event;
import org.ict4h.atomfeed.client.repository.AllFailedEvents;
import org.ict4h.atomfeed.client.repository.AllFeeds;
import org.ict4h.atomfeed.client.repository.AllMarkers;
import org.ict4h.atomfeed.client.repository.datasource.WebClient;
import org.ict4h.atomfeed.client.service.AtomFeedClient;
import org.ict4h.atomfeed.client.service.EventWorker;
import org.ict4h.atomfeed.transaction.AFTransactionManager;
import org.ict4h.atomfeed.transaction.AFTransactionWork;
import org.json.JSONException;
import org.json.JSONObject;
import org.opensrp.connector.atomfeed.AtomfeedService;
import org.opensrp.connector.openmrs.constants.OpenmrsConstants;
import org.opensrp.connector.openmrs.service.OpenmrsService;
import org.opensrp.connector.openmrs.service.PatientService;
import org.opensrp.domain.Client;
import org.opensrp.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PatientAtomfeed extends OpenmrsService implements EventWorker, AtomfeedService
{
	private Logger log = Logger.getLogger(getClass().getSimpleName());
	private static final String CATEGORY_URL = "/OpenSRP_Patient/recent.form";

	private AtomFeedProperties atomFeedProperties;
	private AFTransactionManager transactionManager;
	private AtomFeedClient client;

	private PatientService patientService;
	private ClientService clientService;

	@Autowired
	public PatientAtomfeed(AllMarkers allMarkers, AllFailedEvents allFailedEvents,
	                       @Value("#{opensrp['openmrs.url']}") String baseUrl, PatientService patientService, ClientService clientService) throws URISyntaxException {
		if(baseUrl != null){
			OPENMRS_BASE_URL = baseUrl;
		}

		this.atomFeedProperties = new AtomFeedProperties();
		this.transactionManager = new AFTransactionManager(){
			@Override
			public <T> T executeWithTransaction(AFTransactionWork<T> action) throws RuntimeException {
				return action.execute();
			}
		};
		WebClient webClient = new WebClient();

		URI uri = new URI(OPENMRS_BASE_URL+OpenmrsConstants.ATOMFEED_URL+CATEGORY_URL);
		this.client = new AtomFeedClient(new AllFeeds(webClient), allMarkers, allFailedEvents, atomFeedProperties, transactionManager, uri, this);

		this.patientService = patientService;
		this.clientService = clientService;
	}

	@Override
	public void process(Event event) {
		log.info("Processing item : "+event.getContent());
		try {
			String content = event.getContent().substring(event.getContent().lastIndexOf("/")+1);
			JSONObject p = patientService.getPatientByUuid(content, true);
			if(p == null){
				throw new RuntimeException("Patient uuid specified in atomfeed content ("+content+") did not return any patient.");
			}
			Client c = patientService.convertToClient(p);
			Client existing = clientService.findClient(c);
			if(existing == null){
				c.setBaseEntityId(UUID.randomUUID().toString());
				clientService.addClient(c);

				JSONObject newId = patientService.addThriveId(c.getBaseEntityId(), p);
				log.info("New Client -> Posted Thrive ID back to OpenMRS : "+newId);
			}
			else {
				String srpIdInOpenmrs = c.getBaseEntityId();
				Client cmerged = clientService.mergeClient(c);
				//TODO what if in any case thrive id is assigned to some other patient
				if(StringUtils.isBlank(srpIdInOpenmrs) || !srpIdInOpenmrs.equalsIgnoreCase(cmerged.getBaseEntityId())){
					// if openmrs doesnot have openSRP UID or have a different UID then update
					JSONObject newId = patientService.addThriveId(cmerged.getBaseEntityId(), p);
					log.info("Existing Client missing Valid SRP UID -> Posted Thrive ID back to OpenMRS : "+newId);
				}
			}
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void cleanUp(Event event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void processEvents() {
		Logger.getLogger(getClass().getName()).info("Processing PatientAtomfeeds");
		client.processEvents();
	}

	@Override
	public void processFailedEvents() {
		client.processFailedEvents();
	}

	void setUrl(String url) {
		OPENMRS_BASE_URL = url;
	}
	PatientService getPatientService() {
		return patientService;
	}

	public void setPatientService(PatientService patientService) {
		this.patientService = patientService;
	}

	ClientService getClientService() {
		return clientService;
	}

	void setClientService(ClientService clientService) {
		this.clientService = clientService;
	}

}
