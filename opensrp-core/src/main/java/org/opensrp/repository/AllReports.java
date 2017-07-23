package org.opensrp.repository;

import java.util.List;

import org.ektorp.ComplexKey;
import org.ektorp.CouchDbConnector;
import org.ektorp.support.GenerateView;
import org.ektorp.support.View;
import org.joda.time.DateTime;
import org.motechproject.dao.MotechBaseRepository;
import org.opensrp.common.AllConstants;
import org.opensrp.domain.Report;
import org.opensrp.repository.lucene.LuceneReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class AllReports extends MotechBaseRepository<Report> {
	
	private LuceneReportRepository lrr;
	
	@Autowired
	protected AllReports(@Qualifier(AllConstants.OPENSRP_DATABASE_CONNECTOR) CouchDbConnector db,
	    LuceneReportRepository lrr) {
		super(Report.class, db);
		this.lrr = lrr;
	}
	
	public Report findById(String id) {
		Report report = db.get(Report.class, id);
		return report;
	}
	
	@GenerateView
	public List<Report> getAll() {
		return super.getAll();
	}
	
	@GenerateView
	public List<Report> findByBaseEntityId(String baseEntityId) {
		return queryView("by_baseEntityId", baseEntityId);
	}
	
	@View(name = "all_reports_by_identifier", map = "function(doc) {if (doc.type === 'Report') {for(var key in doc.identifiers) {emit(doc.identifiers[key]);}}}")
	public List<Report> findAllByIdentifier(String identifier) {
		return db.queryView(createQuery("all_reports_by_identifier").key(identifier).includeDocs(true), Report.class);
	}
	
	@View(name = "all_reports_by_base_entity_and_type", map = "function(doc) { if (doc.type === 'Report'){  emit([doc.baseEntityId, doc.reportType], doc); } }")
	public List<Report> findByBaseEntityAndType(String baseEntityId, String reportType) {
		return db.queryView(createQuery("all_reports_by_base_entity_and_type").key(ComplexKey.of(baseEntityId, reportType))
		        .includeDocs(true),
		    Report.class);
	}
	
	@View(name = "reports_by_empty_server_version", map = "function(doc) { if (doc.type == 'Report' && !doc.serverVersion) { emit(doc._id, doc); } }")
	public List<Report> findByEmptyServerVersion() {
		return db.queryView(createQuery("reports_by_empty_server_version").limit(200).includeDocs(true), Report.class);
	}
	
	@View(name = "reports_by_version", map = "function(doc) { if (doc.type === 'Report') { emit([doc.serverVersion], null); } }")
	public List<Report> findByServerVersion(long serverVersion) {
		ComplexKey startKey = ComplexKey.of(serverVersion + 1);
		ComplexKey endKey = ComplexKey.of(Long.MAX_VALUE);
		return db.queryView(createQuery("reports_by_version").startKey(startKey).endKey(endKey).includeDocs(true),
		    Report.class);
	}
	
	@View(name = "all_reports_by_base_entity_and_form_submission", map = "function(doc) { if (doc.type === 'Report'){  emit([doc.baseEntityId, doc.formSubmissionId], doc); } }")
	public List<Report> findByBaseEntityAndFormSubmissionId(String baseEntityId, String formSubmissionId) {
		return db.queryView(createQuery("all_reports_by_base_entity_and_form_submission")
		        .key(ComplexKey.of(baseEntityId, formSubmissionId)).includeDocs(true),
		    Report.class);
	}
	
	public List<Report> findReports(String team, String providerId, String locationId, String baseEntityId,
	                                Long serverVersion, String sortBy, String sortOrder, int limit) {
		return lrr.getByCriteria(team, providerId, locationId, baseEntityId, serverVersion, sortBy, sortOrder, limit);
	}
	
	public List<Report> findReports(String baseEntityId, DateTime from, DateTime to, String reportType, String providerId,
	                                String locationId, DateTime lastEditFrom, DateTime lastEditTo) {
		return lrr.getByCriteria(baseEntityId, from, to, reportType, providerId, locationId, lastEditFrom, lastEditTo);
	}
	
	public List<Report> findReportsByDynamicQuery(String query) {
		return lrr.getByCriteria(query);
	}
}
