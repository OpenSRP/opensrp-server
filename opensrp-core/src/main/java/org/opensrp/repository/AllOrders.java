package org.opensrp.repository;

import org.ektorp.CouchDbConnector;
import org.ektorp.util.Assert;
import org.ektorp.util.Documents;
import org.motechproject.dao.MotechBaseRepository;
import org.opensrp.common.AllConstants;
import org.opensrp.domain.Order;
import org.opensrp.repository.lucene.LuceneOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AllOrders extends MotechBaseRepository<Order> {

    private LuceneOrderRepository luceneOrderRepository;

    @Autowired
    public AllOrders(@Qualifier(AllConstants.OPENSRP_DATABASE_CONNECTOR) CouchDbConnector couchDbConnector,
                     LuceneOrderRepository luceneOrderRepository) {
        super(Order.class, couchDbConnector);
        this.luceneOrderRepository = luceneOrderRepository;
    }

    public void add(Order order) {
        Assert.isTrue(Documents.isNew(order), "order entity must be new");
        db.create(order);
    }

    public Order findById(String id) {
        return db.get(Order.class, id);
    }

    public List<Order> findByLocationId(String locationId, long serverVersion, String sortOrder,
                                        String orderBy, int limit) {
        return luceneOrderRepository.getByLocationId(locationId, serverVersion, sortOrder, orderBy, limit);
    }
}
