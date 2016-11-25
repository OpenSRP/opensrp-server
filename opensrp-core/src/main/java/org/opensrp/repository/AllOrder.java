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
import org.opensrp.domain.DrugOrder;
import org.opensrp.domain.Event;
import org.opensrp.repository.lucene.LuceneClientRepository;
import org.opensrp.repository.lucene.LuceneDrugRepository;
import org.opensrp.repository.lucene.LuceneOrderRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.mysql.jdbc.StringUtils;

@Repository
public class AllOrder extends MotechBaseRepository<DrugOrder> {
	
	private LuceneOrderRespository lOr;

	@Autowired
	protected AllOrder(@Qualifier(AllConstants.OPENSRP_DATABASE_CONNECTOR) CouchDbConnector db, 
		LuceneOrderRespository lOr) {
		super(DrugOrder.class, db);
		this.lOr = lOr;
	}

	 @GenerateView
		public List<DrugOrder> findById(String id) {
	    	return queryView("by_id", id);
		}

	 @View(name = "all_drugorders_by_drugName", map = "function(doc) {if (doc.type === 'DrugOrder') {emit(doc.drugName);}}")
	 public List<DrugOrder> findAllByName(String key) {
			return db.queryView(createQuery("all_drugorders_by_drugName").key(key).includeDocs(true), DrugOrder.class);
		}
	 
	 @View(name = "all_drugorders_by_patientUuid", map = "function(doc) {if (doc.type === 'DrugOrder')  {emit(doc.patientUuid);}}")
	 public List<DrugOrder> findAllByRoute(String key) {
			return db.queryView(createQuery("all_drugorders_by_patientUuid").key(key).includeDocs(true), DrugOrder.class);
		}
}
