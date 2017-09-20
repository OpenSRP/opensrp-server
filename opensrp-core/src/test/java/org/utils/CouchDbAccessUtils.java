package org.utils;

import com.github.ldriscoll.ektorplucene.LuceneAwareCouchDbConnector;
import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbInstance;
import org.motechproject.dao.MotechBaseRepository;
import org.opensrp.domain.BaseDataObject;

import java.io.IOException;
import java.util.List;

public final class CouchDbAccessUtils {

	public static final String TEST_REMOTE_URL = "http://localhost:5984";

	private CouchDbAccessUtils() {

	}

	public static <T, R extends MotechBaseRepository> void addObjectToRepository(List<T> objectList, R repository) {
		for (T object : objectList) {
			repository.add(object);
		}
	}

	public static CouchDbConnector getCouchDbConnector(String dbName) throws IOException {
		org.ektorp.http.HttpClient httpClient = new StdHttpClient.Builder().url(TEST_REMOTE_URL).build();
		CouchDbInstance couchDbInstance = new StdCouchDbInstance(httpClient);
		CouchDbConnector couchDbConnector = new LuceneAwareCouchDbConnector(dbName, couchDbInstance);
		return couchDbConnector;
	}

	public static <T extends BaseDataObject> void purgeDateCreatedEditedAndVoidedField(List<T> objecstToPurge) {
		for (T objectToPurge : objecstToPurge) {
			objectToPurge.setDateCreated(null);
			objectToPurge.setDateEdited(null);
			objectToPurge.setDateVoided(null);
		}
	}
}
