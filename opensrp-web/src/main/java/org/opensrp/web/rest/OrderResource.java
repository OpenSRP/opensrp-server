package org.opensrp.web.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;
import org.opensrp.common.AllConstants;
import org.opensrp.domain.Order;
import org.opensrp.service.OrderService;
import org.opensrp.util.DateTimeTypeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/rest/stockresource/order")
public class OrderResource extends RestResource<Order> {

    private OrderService orderService;
    private static Logger logger = LoggerFactory.getLogger(OrderResource.class.getName());
    private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
            .registerTypeAdapter(DateTime.class, new DateTimeTypeConverter()).create();

    @Autowired
    public OrderResource(OrderService orderService) {
        this.orderService = orderService;
    }

    @RequestMapping(headers = {"Accept=application/json"}, method = RequestMethod.POST, value = "/add")
    public ResponseEntity<HttpStatus> addOrders(@RequestBody String jsonData) {

        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            if (!jsonObject.has("orders")) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            String ordersString = jsonObject.getString("orders");
            ArrayList<Order> orderArrayList = gson.fromJson(ordersString,
                    new TypeToken<ArrayList<Order>>(){}.getType());

            for(Order order: orderArrayList) {
                orderService.addOrder(order);
            }
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (JSONException e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<Order> filter(String query) {
        return null;
    }

    @Override
    public List<Order> search(HttpServletRequest request) throws ParseException {
        return null;
    }

    @Override
    public Order getByUniqueId(String uniqueId) {
        return null;
    }

    @Override
    public List<String> requiredProperties() {
        List<String> properties = new ArrayList<>();
        properties.add(AllConstants.Order.PROVIDER_ID);
        properties.add(AllConstants.Order.LOCATION_ID);
        properties.add(AllConstants.Order.DATE_CREATED);

        return properties;
    }

    @Override
    public Order create(Order entity) {
        return orderService.addOrder(entity);
    }


    @Override
    public Order update(Order entity) {
        return null;
    }
}
