
package org.opensrp.repository;

import java.util.List;

import org.ektorp.CouchDbConnector;
import org.ektorp.DocumentNotFoundException;
import org.ektorp.support.GenerateView;
import org.ektorp.support.View;
import org.motechproject.dao.MotechBaseRepository;
import org.opensrp.common.AllConstants;
import org.opensrp.domain.Client;
import org.opensrp.domain.ErrorTrace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.mysql.jdbc.StringUtils;

/**
 * @author muhammad.ahmed@ihsinformatics.com
 *  Created on May 25, 2015
 */
@Repository
public class AllErrorTrace extends MotechBaseRepository<ErrorTrace> {
	
	
	@Autowired
	protected AllErrorTrace(
			@Qualifier(AllConstants.OPENSRP_ERRORTRACE_DATABASE) CouchDbConnector db) {
		super(ErrorTrace.class, db);
	}

	public ErrorTrace findById(String _id) throws DocumentNotFoundException{
		return  get(_id);
	}
	
	public boolean exists(String id) {
		return findById(id) != null;
	}
	
	@View(name = "all_errors", map = "function(doc) { if (doc.type === 'ErrorTrace') { emit(doc.id); } }")
	public List<ErrorTrace> findAllErrors()  throws DocumentNotFoundException{
		return db.queryView(createQuery("all_errors").includeDocs(true),
				ErrorTrace.class);
	}

	@View(name = "all_errors_by_status", map = "function(doc) { if (doc.type === 'ErrorTrace') { emit(doc.status); } }")
	public List<ErrorTrace> findErrorsByStatus(String status) throws DocumentNotFoundException {
		return db.queryView(createQuery("all_errors_by_status").
				key(status).includeDocs(true), ErrorTrace.class);
	}
}
