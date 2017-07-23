package org.opensrp.web.rest;

import static java.text.MessageFormat.format;
import static org.opensrp.common.AllConstants.Stock.DATE_CREATED;
import static org.opensrp.common.AllConstants.Stock.DATE_UPDATED;
import static org.opensrp.common.AllConstants.Stock.IDENTIFIER;
import static org.opensrp.common.AllConstants.Stock.PROVIDERID;
import static org.opensrp.common.AllConstants.Stock.TO_FROM;
import static org.opensrp.common.AllConstants.Stock.TRANSACTION_TYPE;
import static org.opensrp.common.AllConstants.Stock.VACCINE_TYPE_ID;
import static org.opensrp.common.AllConstants.Stock.VALUE;
import static org.opensrp.common.AllConstants.Stock.TIMESTAMP;
import static org.opensrp.web.rest.RestUtils.getIntegerFilter;
import static org.opensrp.web.rest.RestUtils.getStringFilter;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.joda.time.DateTime;
import org.json.JSONObject;
import org.opensrp.common.AllConstants.BaseEntity;
import org.opensrp.domain.Stock;
import org.opensrp.service.StockService;
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.mysql.jdbc.StringUtils;

@Controller
@RequestMapping(value = "/rest/stockresource/")
public class StockResource extends RestResource<Stock> {
	
	private static Logger logger = LoggerFactory.getLogger(StockResource.class.toString());
	
	private StockService stockService;
	
	Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
	        .registerTypeAdapter(DateTime.class, new DateTimeTypeConverter()).create();
	
	@Autowired
	public StockResource(StockService stockService) {
		this.stockService = stockService;
	}
	
	@Override
	public Stock getByUniqueId(String uniqueId) {
		return stockService.find(uniqueId);
	}
	
	/**
	 * Fetch all the stocks
	 * 
	 * @param none
	 * @return a map response with stocks, and optionally msg when an error occurs
	 */
	
	@RequestMapping(value = "/getall", method = RequestMethod.GET)
	@ResponseBody
	protected ResponseEntity<String> getAll() {
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			List<Stock> stocks = new ArrayList<Stock>();
			stocks = stockService.findAllStocks();
			JsonArray stocksArray = (JsonArray) gson.toJsonTree(stocks, new TypeToken<List<Stock>>() {}.getType());
			response.put("stocks", stocksArray);
			return new ResponseEntity<>(gson.toJson(response), HttpStatus.OK);
		}
		catch (Exception e) {
			response.put("msg", "Error occurred");
			logger.error("", e);
			return new ResponseEntity<>(new Gson().toJson(response), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/**
	 * Fetch stocks ordered by serverVersion ascending order
	 * 
	 * @param request
	 * @return a map response with events, clients and optionally msg when an error occurs
	 */
	@RequestMapping(value = "/sync", method = RequestMethod.GET)
	@ResponseBody
	protected ResponseEntity<String> sync(HttpServletRequest request) {
		Map<String, Object> response = new HashMap<String, Object>();
		
		try {
			String identifier = getStringFilter(IDENTIFIER, request);
			String vaccine_type_id = getStringFilter(VACCINE_TYPE_ID, request);
			String transaction_type = getStringFilter(TRANSACTION_TYPE, request);
			String providerId = getStringFilter(PROVIDERID, request);
			String value = getStringFilter(VALUE, request);
			String date_created = getStringFilter(DATE_CREATED, request);
			String to_from = getStringFilter(TO_FROM, request);
			String date_updated = getStringFilter(DATE_UPDATED, request);
			String serverVersion = getStringFilter(BaseEntity.SERVER_VERSIOIN, request);
			Long lastSyncedServerVersion = null;
			if (serverVersion != null) {
				lastSyncedServerVersion = Long.valueOf(serverVersion) + 1;
			}
			Integer limit = getIntegerFilter("limit", request);
			if (limit == null || limit.intValue() == 0) {
				limit = 25;
			}
			
			List<Stock> stocks = new ArrayList<Stock>();
			stocks = stockService.findStocks(identifier, vaccine_type_id, transaction_type, providerId, value, date_created,
			    to_from, date_updated, lastSyncedServerVersion, BaseEntity.SERVER_VERSIOIN, "asc", limit);
			JsonArray stocksArray = (JsonArray) gson.toJsonTree(stocks, new TypeToken<List<Stock>>() {}.getType());
			
			response.put("stocks", stocksArray);
			
			return new ResponseEntity<>(gson.toJson(response), HttpStatus.OK);
			
		}
		catch (Exception e) {
			response.put("msg", "Error occurred");
			logger.error("", e);
			return new ResponseEntity<>(new Gson().toJson(response), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(headers = { "Accept=application/json" }, method = POST, value = "/add")
	public ResponseEntity<HttpStatus> save(@RequestBody String data) {
		try {
			JSONObject syncData = new JSONObject(data);
			if (!syncData.has("stocks")) {
				return new ResponseEntity<>(BAD_REQUEST);
			}
			ArrayList<Stock> stocks = (ArrayList<Stock>) gson.fromJson(syncData.getString("stocks"),
			    new TypeToken<ArrayList<Stock>>() {}.getType());
			for (Stock stock : stocks) {
				try {
					stockService.addorUpdateStock(stock);
				}
				catch (Exception e) {
					logger.error("Stock" + stock.getId() + " failed to sync", e);
				}
			}
		}
		catch (Exception e) {
			logger.error(format("Sync data processing failed with exception {0}.- ", e));
			return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(CREATED);
	}
	
	@Override
	public Stock create(Stock stock) {
		return stockService.addStock(stock);
	}
	
	@Override
	public List<String> requiredProperties() {
		List<String> p = new ArrayList<>();
		p.add(PROVIDERID);
		p.add(TIMESTAMP);
		return p;
	}
	
	@Override
	public Stock update(Stock stock) {
		return stockService.mergeStock(stock);
	}
	
	@Override
	public List<Stock> search(HttpServletRequest request) throws ParseException {
		String stockId = getStringFilter("identifier", request);
		String vaccine_type_id = getStringFilter(VACCINE_TYPE_ID, request);
		String transaction_type = getStringFilter(TRANSACTION_TYPE, request);
		String providerId = getStringFilter(PROVIDERID, request);
		String value = getStringFilter(VALUE, request);
		String date_created = getStringFilter(DATE_CREATED, request);
		String to_from = getStringFilter(TO_FROM, request);
		String date_updated = getStringFilter(DATE_UPDATED, request);
		String serverVersion = getStringFilter(TIMESTAMP, request);
		
		if (!StringUtils.isEmptyOrWhitespaceOnly(stockId)) {
			Stock stock = stockService.find(stockId);
			if (stock == null) {
				return new ArrayList<>();
			}
		}
		return stockService.findStocks(stockId, vaccine_type_id, transaction_type, providerId, value, date_created, to_from,
		    date_updated, serverVersion);
	}
	
	@Override
	public List<Stock> filter(String query) {
		return stockService.findAllStocks();
	}
	
}
