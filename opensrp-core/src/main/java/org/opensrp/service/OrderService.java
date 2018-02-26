package org.opensrp.service;

import org.apache.http.util.TextUtils;
import org.joda.time.DateTime;
import org.opensrp.domain.Order;
import org.opensrp.repository.AllOrders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    private final AllOrders allOrders;

    @Autowired
    public OrderService(AllOrders allOrders) {this.allOrders = allOrders;}

    public synchronized Order addOrder(Order order) {
        // assumes id field is always populated in Order data model object
        // any time an order is created in  couchDB.
        // ASSUMPTION: If an Order comes with an id, then it already exists in CouchDB
        if (!TextUtils.isEmpty(order.getId())) {
            throw new IllegalArgumentException("Order object should not have id field populated. " +
                    "Alternatively, " + order.getId() + " already exists");
        }
        order.setDateCreated(new DateTime());
        allOrders.add(order);
        return order;
    }
}
