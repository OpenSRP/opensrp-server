package org.opensrp.service;

import org.ektorp.CouchDbConnector;
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
        Order resultOrder = allOrders.findById(order.getId());
        if (resultOrder != null) {
            throw new IllegalArgumentException("Order with the same id " +
                    order.getId() + " already exists");
        }
        order.setDateCreated(new DateTime());
        allOrders.add(order);
        return order;
    }
}
