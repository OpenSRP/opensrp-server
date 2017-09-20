package org.opensrp.form.repository.it;

import org.ektorp.CouchDbInstance;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbConnector;
import org.ektorp.impl.StdCouchDbInstance;
import org.ektorp.impl.StdObjectMapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;

import static org.mockito.MockitoAnnotations.initMocks;

public class TestDatabaseConfig {

	private CouchDbInstance dbInstance;

	private StdCouchDbConnector stdCouchDbConnectorOpensrpForm;

	@Autowired
	//@Value("#{opensrp['openmrs.url']}")
	@Value("#{opensrp['couchdb.username']}")
	public String userName;

	@Autowired
	@Value("#{opensrp['couchdb.password']}")
	private String password;

	@Autowired
	@Value("#{opensrp['couchdb.server']}")
	private String url;

	@Autowired
	@Value("#{opensrp['couchdb.port']}")
	private int port;

	public TestDatabaseConfig() {
		initMocks(this);
	}

	@PostConstruct
	public void intCouchDbConfiguration() {
		HttpClient httpClient = new StdHttpClient.Builder().host(url).username(userName).password(password).port(port)
				.socketTimeout(1000).build();
		dbInstance = new StdCouchDbInstance(httpClient);
		stdCouchDbConnectorOpensrpForm = new StdCouchDbConnector("opensrp-form", dbInstance, new StdObjectMapperFactory());
		stdCouchDbConnectorOpensrpForm.createDatabaseIfNotExists();
	}

	public StdCouchDbConnector getStdCouchDbConnectorForOpensrpForm() {
		return stdCouchDbConnectorOpensrpForm;
	}

	public CouchDbInstance getDbInstance() {
		return dbInstance;
	}

	public void setDbInstance(CouchDbInstance dbInstance) {
		this.dbInstance = dbInstance;
	}
}
