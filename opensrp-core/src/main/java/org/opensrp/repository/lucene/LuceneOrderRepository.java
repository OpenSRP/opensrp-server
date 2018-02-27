package org.opensrp.repository.lucene;

import com.github.ldriscoll.ektorplucene.CouchDbRepositorySupportWithLucene;
import com.github.ldriscoll.ektorplucene.LuceneQuery;
import com.github.ldriscoll.ektorplucene.LuceneResult;
import com.github.ldriscoll.ektorplucene.designdocument.annotation.FullText;
import com.github.ldriscoll.ektorplucene.designdocument.annotation.Index;
import org.opensrp.common.AllConstants;
import org.opensrp.domain.Order;
import org.opensrp.exception.LuceneIOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;


@FullText({
        @Index(name = "by_all_criteria", analyzer = "perfield:{locationId:\"keyword\"}", index = "function(doc) { if (doc.type !== 'Order') return null; var arr1 = ['clientIdentifier', 'locationId', 'providerId', 'serverVersion']; var ret = new Document(); var serverVersion = doc.serverVersion; ret.add(serverVersion, { 'field': 'serverVersion' }); for (var i in arr1) { ret.add(doc[arr1[i]], { 'field': arr1[i] }); } if (doc.dateCreated) { var dc = doc.dateCreated; ret.add(dc, { 'field': 'dateCreated' }); } if (doc.dateEdited) { var de = doc.dateEdited; ret.add(de, { 'field': 'dateEdited' }); } if (doc.dateCreatedByClient) { var dcbc = doc.dateCreatedByClient; ret.add(dcbc, { 'field': 'dateCreatedByClient' }); } return ret; }")
})
@Component
public class LuceneOrderRepository extends CouchDbRepositorySupportWithLucene<Order> {

    private LuceneDbConnector luceneDbConnector;

    @Autowired
    protected LuceneOrderRepository(LuceneDbConnector db) {
        super(Order.class, db);
        this.luceneDbConnector = db;
    }

    /**
     * Returns a list of {@link org.opensrp.domain.Stock} from the specified locationId from the specified serverVersion.
     *
     *
     * @param locationId
     * @param serverVersion
     * @param sortOrder
     * @param orderBy
     * @return
     */
    public List<Order> getByLocationId(String locationId, long serverVersion, String sortOrder, String orderBy, int limit) {
        LuceneQuery luceneQuery = new LuceneQuery("Order", "by_all_criteria");
        Query query = new Query(FilterType.AND);

        query.eq(AllConstants.Order.LOCATION_ID, locationId);
        query.between(AllConstants.Order.SERVER_VERSION, serverVersion, Long.MAX_VALUE);

        luceneQuery.setQuery(query.query());
        luceneQuery.setStaleOk(false);
        luceneQuery.setIncludeDocs(true);
        luceneQuery.setLimit(limit);

        if (sortOrder != null && orderBy != null) {
            luceneQuery.setSort((sortOrder.toLowerCase().contains("desc") ? "\\" : "/") +  orderBy);
        }

        try {
            LuceneResult result = db.queryLucene(luceneQuery);
            return luceneDbConnector.asList(result, Order.class);
        } catch (IOException e) {
            throw new LuceneIOException(e);
        }
    }
}
