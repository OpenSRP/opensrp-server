package org.opensrp.form.repository.it;

import static org.mockito.MockitoAnnotations.initMocks;

import org.ektorp.CouchDbInstance;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbConnector;
import org.ektorp.impl.StdCouchDbInstance;
import org.ektorp.impl.StdObjectMapperFactory;

public class TestDatabaseConfig {	
    private CouchDbInstance dbInstance;
    private StdCouchDbConnector stdCouchDbConnectorOpensrpForm;	
    
    public TestDatabaseConfig(){
        initMocks(this);
        HttpClient httpClient = new StdHttpClient.Builder() 
            .host("localhost") 
            .username("rootuser").password("adminpass")
            .port(5984)
            .socketTimeout(1000) 
            .build(); 
        dbInstance = new StdCouchDbInstance(httpClient);
        stdCouchDbConnectorOpensrpForm = new StdCouchDbConnector("opensrp-form", dbInstance, new StdObjectMapperFactory());
        stdCouchDbConnectorOpensrpForm.createDatabaseIfNotExists();			
    }	

    public StdCouchDbConnector getStdCouchDbConnectorForOpensrpForm(){
        return stdCouchDbConnectorOpensrpForm;
    }
	
    public CouchDbInstance getDbInstance() {
        return dbInstance;
    }

    public void setDbInstance(CouchDbInstance dbInstance) {
        this.dbInstance = dbInstance;
    }
}
