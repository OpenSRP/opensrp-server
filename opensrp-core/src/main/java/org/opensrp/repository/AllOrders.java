package org.opensrp.repository;

import org.ektorp.CouchDbConnector;
import org.ektorp.support.View;
import org.ektorp.util.Assert;
import org.ektorp.util.Documents;
import org.motechproject.dao.MotechBaseRepository;
import org.opensrp.common.AllConstants;
import org.opensrp.domain.Order;
import org.opensrp.domain.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AllOrders extends MotechBaseRepository<Order> {

    private CouchDbConnector couchDbConnector;

    @Autowired
    public AllOrders(@Qualifier(AllConstants.OPENSRP_DATABASE_CONNECTOR) CouchDbConnector couchDbConnector) {
        super(Order.class, couchDbConnector);
        this.couchDbConnector = couchDbConnector;
    }

    public void add(Order order) {
        Assert.isTrue(Documents.isNew(order), "order entity must be new");
        couchDbConnector.create(order);
    }

    public Order findById(String id) {
        return couchDbConnector.get(Order.class, id);
    }

    /*

    @View(name = "all_orders", map = "function(doc) { if (doc.type === 'Order') { emit(doc.dateCreated); } }")
    public List<Order> findAllStocks() {
        return db.queryView(createQuery("all_orders").includeDocs(true), Order.class);
    }*/
}
