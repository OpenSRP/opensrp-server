package org.opensrp.repository.lucene;

import static org.opensrp.common.AllConstants.BaseEntity.*;
import static org.opensrp.common.AllConstants.Drug.*;

import java.io.IOException;
import java.util.List;

import org.joda.time.DateTime;
import org.opensrp.domain.Client;
import org.opensrp.domain.*;
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
        index = "function (doc) {  if(doc.type !== 'Drug') return null;  var docl = new Array();  var len = doc.name ? doc.name.length : 1;  for(var al = 0; al < len; al++) {    var arr1 = ['name', 'baseName']; }")
})
@Component
public class LuceneDrugRepository extends CouchDbRepositorySupportWithLucene<Drug>{

	private LuceneDbConnector ldb;
	
	@Autowired
	protected LuceneDrugRepository(LuceneDbConnector db) {
		super(Drug.class, db);
		this.ldb = db;
		initStandardDesignDocument();
	}
	
	public List<Drug> getByCriteria(String name,String baseName){
		return getByCriteria(name,null,baseName,null
				,null,null,null,null,null,null,null,null,null);
	}
	
	public List<Drug> getByCriteria(String name,String nameUuid,String baseName, String baseNameUuid
			, String route, String creator, String creatorUuid, String doseStrenght
			, String units, String maxDailyDose,String miniDailyDose,String Description,String combination) {
		// create a simple query against the view/search function that we've created
		LuceneQuery query = new LuceneQuery("Drug", "by_all_criteria");
		
		Query q = new Query(FilterType.OR);
		if(!StringUtils.isEmptyOrWhitespaceOnly(name) || !StringUtils.isEmptyOrWhitespaceOnly(baseName)){
			q.like(NAME, name);
			q.like(BASENAME, baseName);
		}
		
		/*if(!StringUtils.isEmptyOrWhitespaceOnly(nameUuid) || !StringUtils.isEmptyOrWhitespaceOnly(baseNameUuid)){
			q.like(NAMEUUID, nameUuid);
			q.like(BASENAMEUUID, baseNameUuid);
		}*/
		
		query.setQuery(q.query());
		// stale must not be ok, as we've only just loaded the docs
		query.setStaleOk(false);
		query.setIncludeDocs(true);

		try {
			LuceneResult result = db.queryLucene(query);
			return ldb.asList(result, Drug.class);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} 
	}
	
	public List<Drug> getByCriteria(String query) {
		// create a simple query against the view/search function that we've created
		LuceneQuery lq = new LuceneQuery("Drug", "by_all_criteria");
		
		lq.setQuery(query);
		// stale must not be ok, as we've only just loaded the docs
		lq.setStaleOk(false);
		lq.setIncludeDocs(true);

		try {
			LuceneResult result = db.queryLucene(lq);
			return ldb.asList(result, Drug.class);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} 
	}
}
