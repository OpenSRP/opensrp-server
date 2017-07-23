package org.opensrp.repository.lucene;

import static org.opensrp.common.AllConstants.Stock.DATE_CREATED;
import static org.opensrp.common.AllConstants.Stock.DATE_UPDATED;
import static org.opensrp.common.AllConstants.Stock.IDENTIFIER;
import static org.opensrp.common.AllConstants.Stock.PROVIDERID;
import static org.opensrp.common.AllConstants.Stock.TO_FROM;
import static org.opensrp.common.AllConstants.Stock.TRANSACTION_TYPE;
import static org.opensrp.common.AllConstants.Stock.VACCINE_TYPE_ID;
import static org.opensrp.common.AllConstants.Stock.VALUE;

import java.io.IOException;
import java.util.List;

import org.opensrp.common.AllConstants.BaseEntity;
import org.opensrp.domain.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.ldriscoll.ektorplucene.CouchDbRepositorySupportWithLucene;
import com.github.ldriscoll.ektorplucene.LuceneQuery;
import com.github.ldriscoll.ektorplucene.LuceneResult;
import com.github.ldriscoll.ektorplucene.designdocument.annotation.FullText;
import com.github.ldriscoll.ektorplucene.designdocument.annotation.Index;
import com.mysql.jdbc.StringUtils;

@FullText({
        @Index(name = "by_all_criteria", analyzer = "perfield:{identifier:\"keyword\",providerid:\"keyword\"}", index = "function(doc) { if (doc.type !== 'Stock') return null; var arr1 = ['identifier', 'vaccine_type_id', 'transaction_type', 'providerid', 'value', 'to_from', 'sync_status', 'timeStamp']; var ret = new Document(); var serverVersion = doc.serverVersion; ret.add(serverVersion, { 'field': 'serverVersion' }); for (var i in arr1) { ret.add(doc[arr1[i]], { 'field': arr1[i] }); } if (doc.date_created) { var dc = doc.date_updated; ret.add(dc, { 'field': 'dateCreated' }); } if (doc.date_updated) { var da = doc.date_updated; ret.add(da, { 'field': 'dateUpdated' }); } return ret; }"),
        @Index(name = "by _all_criteria_v2", analyzer = "perfield:{baseEntityId:\"keyword\",locationId:\"keyword\"}", index = "function(doc) { if (doc.type !== 'Stock') return null; var arr1 = ['identifier', 'vaccine_type_id', 'transaction_type', 'providerid', 'value', 'to_from', 'sync_status', 'serverVersion']; var ret = new Document(); var serverVersion = doc.serverVersion; ret.add(serverVersion, { 'field': 'serverVersion' }); for (var i in arr1) { ret.add(doc[arr1[i]], { 'field': arr1[i] }); } if (doc.date_created) { var dc = doc.date_updated; ret.add(dc, { 'field': 'dateCreated' }); } if (doc.date_updated) { var da = doc.date_updated; ret.add(da, { 'field': 'dateUpdated' }); } return ret; }") })
@Component
public class LuceneStockRepository extends CouchDbRepositorySupportWithLucene<Stock> {
	
	private LuceneDbConnector ldb;
	
	@Autowired
	protected LuceneStockRepository(LuceneDbConnector db) {
		super(Stock.class, db);
		this.ldb = db;
		initStandardDesignDocument();
	}
	
	public List<Stock> getByCriteria(String identifier, String vaccine_type_id, String transaction_type, String providerid,
	                                 String value, String date_created, String to_from, String date_updated,
	                                 Long serverVersion, String sortBy, String sortOrder, int limit) {
		// create a simple query against the view/search function that we've created
		LuceneQuery query = new LuceneQuery("Stock", "by_all_criteria");
		
		Query qf = new Query(FilterType.AND);
		if (!StringUtils.isEmptyOrWhitespaceOnly(identifier)) {
			qf.eq(IDENTIFIER, identifier);
		}
		if (!StringUtils.isEmptyOrWhitespaceOnly(vaccine_type_id)) {
			qf.eq(VACCINE_TYPE_ID, vaccine_type_id);
		}
		if (!StringUtils.isEmptyOrWhitespaceOnly(transaction_type)) {
			qf.eq(TRANSACTION_TYPE, transaction_type);
		}
		if (!StringUtils.isEmptyOrWhitespaceOnly(providerid)) {
			qf.eq(PROVIDERID, providerid);
		}
		if (!StringUtils.isEmptyOrWhitespaceOnly(value)) {
			qf.eq(VALUE, value);
		}
		if (!StringUtils.isEmptyOrWhitespaceOnly(date_created)) {
			qf.eq(DATE_CREATED, date_created);
		}
		if (!StringUtils.isEmptyOrWhitespaceOnly(to_from)) {
			qf.eq(TO_FROM, to_from);
		}
		if (!StringUtils.isEmptyOrWhitespaceOnly(date_updated)) {
			qf.eq(DATE_UPDATED, date_updated);
		}
		
		if (serverVersion != null) {
			qf.between(BaseEntity.SERVER_VERSIOIN, serverVersion, Long.MAX_VALUE);
		}
		
		if (StringUtils.isEmptyOrWhitespaceOnly(qf.query())) {
			throw new RuntimeException("Atleast one search filter must be specified");
		}
		query.setQuery(qf.query());
		// stale must not be ok, as we've only just loaded the docs
		query.setStaleOk(false);
		query.setLimit(limit);
		query.setIncludeDocs(true);
		query.setSort((sortOrder.toLowerCase().contains("desc") ? "\\" : "/") + sortBy);
		
		try {
			LuceneResult result = db.queryLucene(query);
			return ldb.asList(result, Stock.class);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<Stock> getByCriteria(String identifier, String vaccine_type_id, String transaction_type, String providerid,
	                                 String value, String date_created, String to_from, String date_updated,
	                                 String serverVersion) {
		LuceneQuery query = new LuceneQuery("Stock", "by_all_criteria");
		
		Query qf = new Query(FilterType.AND);
		if (!StringUtils.isEmptyOrWhitespaceOnly(identifier)) {
			qf.eq(IDENTIFIER, identifier);
		}
		if (!StringUtils.isEmptyOrWhitespaceOnly(vaccine_type_id)) {
			qf.eq(VACCINE_TYPE_ID, vaccine_type_id);
		}
		if (!StringUtils.isEmptyOrWhitespaceOnly(transaction_type)) {
			qf.eq(TRANSACTION_TYPE, transaction_type);
		}
		if (!StringUtils.isEmptyOrWhitespaceOnly(providerid)) {
			qf.eq(PROVIDERID, providerid);
		}
		if (!StringUtils.isEmptyOrWhitespaceOnly(value)) {
			qf.eq(VALUE, value);
		}
		if (!StringUtils.isEmptyOrWhitespaceOnly(date_created)) {
			qf.eq(DATE_CREATED, date_created);
		}
		if (!StringUtils.isEmptyOrWhitespaceOnly(to_from)) {
			qf.eq(TO_FROM, to_from);
		}
		if (!StringUtils.isEmptyOrWhitespaceOnly(date_updated)) {
			qf.eq(DATE_UPDATED, date_updated);
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
			return ldb.asList(result, Stock.class);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<Stock> getByCriteria(String query) {
		// create a simple query against the view/search function that we've created
		LuceneQuery q = new LuceneQuery("Stock", "by_all_criteria");
		
		q.setQuery(query);
		// stale must not be ok, as we've only just loaded the docs
		q.setStaleOk(false);
		q.setIncludeDocs(true);
		
		try {
			LuceneResult result = db.queryLucene(q);
			return ldb.asList(result, Stock.class);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
