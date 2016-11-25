package org.opensrp.repository.lucene;

import static org.opensrp.common.AllConstants.BaseEntity.*;
import static org.opensrp.common.AllConstants.DrugOrder.*;

import java.io.IOException;
import java.util.List;

import org.joda.time.DateTime;
import org.opensrp.domain.Client;
import org.opensrp.domain.DrugOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.ldriscoll.ektorplucene.CouchDbRepositorySupportWithLucene;
import com.github.ldriscoll.ektorplucene.LuceneQuery;
import com.github.ldriscoll.ektorplucene.LuceneResult;
import com.github.ldriscoll.ektorplucene.designdocument.annotation.FullText;
import com.github.ldriscoll.ektorplucene.designdocument.annotation.Index;
import com.mysql.jdbc.StringUtils;

@FullText({
    @Index(
        name = "by_all_criteria",
        index = "function (doc) {  if(doc.type !== 'DrugOrder') return null;  var docl = new Array();  var len = doc.name ? doc.name.length : 1;  for(var al = 0; al < len; al++) {    var arr1 = ['orderType', 'drugName']; }")
})
@Component
public class LuceneOrderRespository extends CouchDbRepositorySupportWithLucene<DrugOrder>{

	private LuceneDbConnector ldb;
	
	@Autowired
	protected LuceneOrderRespository(LuceneDbConnector db) {
		super(DrugOrder.class, db);
		this.ldb = db;
		initStandardDesignDocument();
	}
	
	public List<DrugOrder> getByCriteria(String name){
		return getByCriteriaFilter(name);
	}
	
	public List<DrugOrder> getByCriteriaFilter(String name) {
		// create a simple query against the view/search function that we've created
		LuceneQuery query = new LuceneQuery("DrugOrder", "by_all_criteria");
		
		Query q = new Query(FilterType.valueOf(name));
		q.eq(drugName,name);
		query.setQuery(q.query());
		// stale must not be ok, as we've only just loaded the docs
		query.setStaleOk(false);
		query.setIncludeDocs(true);

		try {
			LuceneResult result = db.queryLucene(query);
			return ldb.asList(result, DrugOrder.class);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} 
	}
}
