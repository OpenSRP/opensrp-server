package org.opensrp.repository.lucene;

import static org.opensrp.common.AllConstants.BaseEntity.BASE_ENTITY_ID;
import static org.opensrp.common.AllConstants.BaseEntity.LAST_UPDATE;
import static org.opensrp.common.AllConstants.Report.REPORT_DATE;
import static org.opensrp.common.AllConstants.Report.REPORT_TYPE;
import static org.opensrp.common.AllConstants.Report.LOCATION_ID;
import static org.opensrp.common.AllConstants.Report.PROVIDER_ID;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.joda.time.DateTime;
import org.opensrp.common.AllConstants.BaseEntity;
import org.opensrp.domain.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.ldriscoll.ektorplucene.CouchDbRepositorySupportWithLucene;
import com.github.ldriscoll.ektorplucene.LuceneQuery;
import com.github.ldriscoll.ektorplucene.LuceneResult;
import com.github.ldriscoll.ektorplucene.designdocument.annotation.FullText;
import com.github.ldriscoll.ektorplucene.designdocument.annotation.Index;
import com.mysql.jdbc.StringUtils;

@FullText({
        @Index(name = "by_all_criteria", analyzer = "perfield:{baseEntityId:\"keyword\",locationId:\"keyword\"}", index = "function(doc) {   if(doc.type !== 'Report') return null;   var arr1 = ['baseEntityId','reportType','providerId','locationId'];   var ret = new Document(); var serverVersion = doc.serverVersion;ret.add(serverVersion, {'field': 'serverVersion'});  for (var i in arr1){     ret.add(doc[arr1[i]], {'field':arr1[i]});   }   if(doc.reportDate){     var bd=doc.reportDate.substring(0,19);      ret.add(bd, {'field':'reportDate','type':'date'});   }          var crd = doc.dateCreated.substring(0, 19);     ret.add(crd, {'field' : 'lastEdited','type' : 'date'});          if(doc.dateEdited){     var led = doc.dateEdited.substring(0, 19);     ret.add(led, {'field' : 'lastEdited','type' : 'date'});         }        return ret;   }"),
        @Index(name = "by_all_criteria_v2", analyzer = "perfield:{baseEntityId:\"keyword\",locationId:\"keyword\"}", index = "function(doc) {   if(doc.type !== 'Report') return null;   var arr1 = ['baseEntityId','reportType','providerId','locationId'];   var ret = new Document(); var serverVersion = doc.serverVersion;ret.add(serverVersion, {'field': 'serverVersion'});  for (var i in arr1){     ret.add(doc[arr1[i]], {'field':arr1[i]});   }   if(doc.reportDate){     var bd=doc.reportDate.substring(0,19);      ret.add(bd, {'field':'reportDate','type':'date'});   }          var crd = doc.dateCreated.substring(0, 19);     ret.add(crd, {'field' : 'lastEdited','type' : 'date'});          if(doc.dateEdited){     var led = doc.dateEdited.substring(0, 19);     ret.add(led, {'field' : 'lastEdited','type' : 'date'});         }        return ret;   }") })

@Component
public class LuceneReportRepository extends CouchDbRepositorySupportWithLucene<Report> {
	
	private LuceneDbConnector ldb;
	
	@Autowired
	protected LuceneReportRepository(LuceneDbConnector db) {
		super(Report.class, db);
		this.ldb = db;
		initStandardDesignDocument();
	}
	
	public List<Report> getByCriteria(String baseEntityId, DateTime reportDatefrom, DateTime reportDateto, String reportType,
	                                  String providerId, String locationId, DateTime lastEditFrom, DateTime lastEditTo) {
		// create a simple query against the view/search function that we've created
		LuceneQuery query = new LuceneQuery("Report", "by_all_criteria");
		
		Query qf = new Query(FilterType.AND);
		if (reportDatefrom != null && reportDateto != null) {
			qf.between(REPORT_DATE, reportDatefrom, reportDateto);
		}
		if (lastEditFrom != null && lastEditTo != null) {
			qf.between(LAST_UPDATE, lastEditFrom, lastEditTo);
		}
		if (!StringUtils.isEmptyOrWhitespaceOnly(baseEntityId)) {
			qf.eq(BASE_ENTITY_ID, baseEntityId);
		}
		if (!StringUtils.isEmptyOrWhitespaceOnly(reportType)) {
			qf.eq(REPORT_TYPE, reportType);
		}
		if (!StringUtils.isEmptyOrWhitespaceOnly(providerId)) {
			qf.eq(PROVIDER_ID, providerId);
		}
		if (!StringUtils.isEmptyOrWhitespaceOnly(locationId)) {
			qf.eq(LOCATION_ID, locationId);
		}
		
		if (StringUtils.isEmptyOrWhitespaceOnly(qf.query())) {
			throw new RuntimeException("Atleast one search filter must be specified");
		}
		query.setQuery(qf.query());
		// stale must not be ok, as we've only just loaded the docs
		query.setStaleOk(false);
		query.setIncludeDocs(true);
		
		try {
			LuceneResult result = db.queryLucene(query);
			return ldb.asList(result, Report.class);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * @param providerId- health worker id
	 * @param locationId
	 * @param baseEntityId
	 * @param serverVersion
	 * @param sortBy Prefix with / for ascending order and \ for descending order (ascending is the
	 *            default if not specified).
	 * @param sortOrder either descending or ascending
	 * @param limit
	 * @param team this is a comma separated string of team members id
	 * @return
	 */
	public List<Report> getByCriteria(String team, String providerId, String locationId, String baseEntityId,
	                                  Long serverVersion, String sortBy, String sortOrder, int limit) {
		// create a simple query against the view/search function that we've created
		LuceneQuery query = new LuceneQuery("Report", "by_all_criteria_v2");
		
		Query qf = new Query(FilterType.AND);
		
		if (serverVersion != null) {
			qf.between(BaseEntity.SERVER_VERSIOIN, serverVersion, Long.MAX_VALUE);
		}
		
		if (team != null && !team.isEmpty()) {
			//convert team string to list
			String[] idsArray = org.apache.commons.lang.StringUtils.split(team, ",");
			List<String> ids = new ArrayList<String>(Arrays.asList(idsArray));
			//include providerId records also
			if (providerId != null && !ids.contains(providerId)) {
				ids.add(providerId);
			}
			qf.inList(PROVIDER_ID, ids);
		} else if ((providerId != null && !StringUtils.isEmptyOrWhitespaceOnly(providerId))) {
			qf.eq(PROVIDER_ID, providerId);
		}
		
		if (!StringUtils.isEmptyOrWhitespaceOnly(locationId)) {
			qf.eq(LOCATION_ID, locationId);
		}
		
		if (!StringUtils.isEmptyOrWhitespaceOnly(baseEntityId)) {
			if (baseEntityId.contains(",")) {
				Query q = new Query(FilterType.OR);
				String[] idsArray = org.apache.commons.lang.StringUtils.split(baseEntityId, ",");
				List<String> ids = new ArrayList<String>(Arrays.asList(idsArray));
				q.inList(BASE_ENTITY_ID, ids);
				
				qf.addToQuery(q);
			} else {
				qf.eq(BASE_ENTITY_ID, baseEntityId);
			}
		}
		
		if (StringUtils.isEmptyOrWhitespaceOnly(qf.query())) {
			throw new RuntimeException("Atleast one search filter must be specified");
		}
		query.setQuery(qf.query());
		// stale must not be ok, as we've only just loaded the docs
		query.setStaleOk(false);
		query.setIncludeDocs(true);
		query.setLimit(limit);
		query.setSort((sortOrder.toLowerCase().contains("desc") ? "\\" : "/") + sortBy);
		
		try {
			LuceneResult result = db.queryLucene(query);
			return ldb.asList(result, Report.class);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<Report> getByCriteria(String query) {
		// create a simple query against the view/search function that we've created
		LuceneQuery q = new LuceneQuery("Report", "by_all_criteria");
		
		q.setQuery(query);
		// stale must not be ok, as we've only just loaded the docs
		q.setStaleOk(false);
		q.setIncludeDocs(true);
		
		try {
			LuceneResult result = db.queryLucene(q);
			return ldb.asList(result, Report.class);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
