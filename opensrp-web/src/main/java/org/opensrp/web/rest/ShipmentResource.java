package org.opensrp.web.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import org.apache.http.util.TextUtils;
import org.joda.time.DateTime;
import org.opensrp.common.AllConstants;
import org.opensrp.domain.Shipment;
import org.opensrp.service.ShipmentService;
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
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/rest/stockresource/shipment")
public class ShipmentResource extends RestResource<Shipment> {

    private ShipmentService shipmentService;
    private Logger logger = LoggerFactory.getLogger(ShipmentService.class.getName());
    private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            .registerTypeAdapter(DateTime.class, new DateTimeTypeConverter()).create();

    @Autowired
    public ShipmentResource(ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity<HttpStatus> add(@RequestBody String jsonData) {
        if (TextUtils.isEmpty(jsonData)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Shipment newShipment = gson.fromJson(jsonData, Shipment.class);
        shipmentService.add(newShipment);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(value = "/getShipments", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> getShipmentByReceivingFacility(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();

        try {
            String receivingFacilityCode = RestUtils.getStringFilter(AllConstants.Shipment.RECEIVING_FACILITY_CODE, request);
            String sortOrder = "asc";
            String orderBy = AllConstants.Shipment.SERVER_VERSION;
            String limitStringRep = RestUtils.getStringFilter("limit", request);

            int limit = (limitStringRep == null) ? 25 : Integer.valueOf(limitStringRep);
            String serverVersionStringRep = RestUtils.getStringFilter(AllConstants.Shipment.SERVER_VERSION, request);
            long serverVersion = (serverVersionStringRep == null) ? 0 : Long.valueOf(serverVersionStringRep);

            List<Shipment> shipments = shipmentService.findShipmentsByReceivingFacility(receivingFacilityCode,
                    serverVersion, orderBy, sortOrder, limit);

            JsonArray shipmentsArray = (JsonArray) gson.toJsonTree(shipments, new TypeToken<List<Shipment>>() {}.getType());
            response.put("shipments", shipmentsArray);

            return new ResponseEntity<>(gson.toJson(response), HttpStatus.OK);
        } catch(Exception e) {
            response.put("msg", "Error occured");
            logger.error("", e);
            return new ResponseEntity<>(new Gson().toJson(response), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<Shipment> filter(String query) {
        return null;
    }

    @Override
    public List<Shipment> search(HttpServletRequest request) throws ParseException {
        return null;
    }

    @Override
    public Shipment getByUniqueId(String uniqueId) {
        return null;
    }

    @Override
    public List<String> requiredProperties() {
        return null;
    }

    @Override
    public Shipment create(Shipment entity) {
        return null;
    }

    @Override
    public Shipment update(Shipment entity) {
        return null;
    }
}
