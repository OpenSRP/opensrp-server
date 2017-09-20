package org.opensrp.repository.lucene;

import static org.opensrp.common.AllConstants.Event.PROVIDER_ID;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.opensrp.scheduler.Action;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.ldriscoll.ektorplucene.CouchDbRepositorySupportWithLucene;
import com.github.ldriscoll.ektorplucene.LuceneQuery;
import com.github.ldriscoll.ektorplucene.LuceneResult;
import com.github.ldriscoll.ektorplucene.designdocument.annotation.FullText;
import com.github.ldriscoll.ektorplucene.designdocument.annotation.Index;
import com.mysql.jdbc.StringUtils;

@FullText({ @Index(name = "by_all_criteria", analyzer = "perfield:{baseEntityId:\"keyword\"}", index = "function(doc) {"
		+ "if (doc.type !== 'Action') return null;"
		+ "var arr1 = ['baseEntityId','providerId','actionTarget','actionType', 'isActionActive','timeStamp','version'];"
		+ "var ret = new Document();" + "for (var i in arr1) {" + "ret.add(doc[arr1[i]], {" + "'field': arr1[i]" + " });}"
		+ "return ret;}") })
@Component
public class LuceneActionRepository extends CouchDbRepositorySupportWithLucene<Action> {

	private LuceneDbConnector ldb;

	@Autowired
	protected LuceneActionRepository(LuceneDbConnector db) {
		super(Action.class, db);
		this.ldb = db;
		initStandardDesignDocument();
	}

	/**
	 * @param providerId- health worker id
	 * @param timeStamp
	 * @param sortBy      Prefix with / for ascending order and \ for descending order (ascending is the
	 *                    default if not specified).
	 * @param sortOrder   either descending or ascending
	 * @param limit
	 * @param team        this is a comma separated string of team members id
	 * @return
	 */
	public List<Action> getByCriteria(String team, String providerId, Long timeStamp, String sortBy, String sortOrder,
	                                  int limit) {
		// create a simple query against the view/search function that we've created
		LuceneQuery query = new LuceneQuery("Action", "by_all_criteria");

		Query qf = new Query(FilterType.AND);

		if (timeStamp != null) {
			qf.between(org.opensrp.common.AllConstants.Action.TIMESTAMP, timeStamp, Long.MAX_VALUE);
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

		if (StringUtils.isEmptyOrWhitespaceOnly(qf.query())) {
			throw new RuntimeException("At least one search filter must be specified");
		}
		query.setQuery(qf.query());
		// stale must not be ok, as we've only just loaded the docs
		query.setStaleOk(false);
		query.setIncludeDocs(true);
		query.setLimit(limit);
		query.setSort((sortOrder.toLowerCase().contains("desc") ? "\\" : "/") + sortBy);

		try {
			LuceneResult result = db.queryLucene(query);
			return ldb.asList(result, Action.class);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
