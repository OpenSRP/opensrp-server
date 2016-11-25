package org.opensrp.repository;

import java.util.List;
import java.util.Map;

import org.ektorp.ComplexKey;
import org.ektorp.CouchDbConnector;
import org.ektorp.support.GenerateView;
import org.ektorp.support.View;
import org.joda.time.DateTime;
import org.motechproject.dao.MotechBaseRepository;
import org.opensrp.common.AllConstants;
import org.opensrp.domain.AppStateToken;
import org.opensrp.domain.Drug;
import org.opensrp.domain.Event;
import org.opensrp.repository.lucene.LuceneDrugRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.mysql.jdbc.StringUtils;

@Repository
public class AllDrugs extends MotechBaseRepository<Drug> {
	
	private LuceneDrugRepository ldr;

	@Autowired
	protected AllDrugs(@Qualifier(AllConstants.OPENSRP_DATABASE_CONNECTOR) CouchDbConnector db, 
			LuceneDrugRepository ldr) {
		super(Drug.class, db);
		this.ldr = ldr;
	}

	 @GenerateView
		public List<Drug> findById(String id) {
	    	return queryView("by_id", id);
		}

	 @View(name = "all_drugs_by_drugName", map = "function(doc) {if (doc.type === 'Drug') {emit(doc.drugName);}}")
	 public List<Drug> findAllByName(String key) {
			return db.queryView(createQuery("all_drugs_by_drugName").key(key).includeDocs(true), Drug.class);
		}
	 
	 @View(name = "all_drugs_by_route", map = "function(doc) {if (doc.type === 'Drug')  {emit(doc.route);}}")
	 public List<Drug> findAllByRoute(String key) {
			return db.queryView(createQuery("all_drugs_by_route").key(key).includeDocs(true), Drug.class);
		}
}
