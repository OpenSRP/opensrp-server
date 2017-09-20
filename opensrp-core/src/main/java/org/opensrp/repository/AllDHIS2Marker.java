package org.opensrp.repository;

import java.util.List;

import org.ektorp.CouchDbConnector;
import org.ektorp.support.GenerateView;
import org.motechproject.dao.MotechBaseRepository;
import org.opensrp.common.AllConstants;
import org.opensrp.common.AllConstants.DHIS2Constants;
import org.opensrp.domain.DHIS2Marker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class AllDHIS2Marker extends MotechBaseRepository<DHIS2Marker> {
	
	@Autowired
	protected AllDHIS2Marker(@Qualifier(AllConstants.OPENSRP_DATABASE_CONNECTOR) CouchDbConnector db) {
		super(DHIS2Marker.class, db);
	}
	
	@GenerateView
	public List<DHIS2Marker> findByName(String name) {
		return queryView("by_name", name);
	}
	
	public void add() {
		DHIS2Marker dHIS2MarkerEntry = new DHIS2Marker();
		dHIS2MarkerEntry.setName(DHIS2Constants.DHIS2_TRACK_DATA_SYNCER_VERSION_MARKER);
		dHIS2MarkerEntry.setValue(0l);
		this.add(dHIS2MarkerEntry);
	}
	
	public void update() {
		DHIS2Marker lastsync = this.findByName(DHIS2Constants.DHIS2_TRACK_DATA_SYNCER_VERSION_MARKER).get(0);
		lastsync.setValue(System.currentTimeMillis());
		this.update(lastsync);
	}
	
}
