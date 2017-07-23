package org.opensrp.service;

import java.util.List;

import org.ektorp.CouchDbConnector;
import org.joda.time.DateTime;
import org.opensrp.domain.Stock;
import org.opensrp.repository.AllStocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StockService {
	
	private final AllStocks allStocks;
	
	@Autowired
	public StockService(AllStocks allStocks) {
		this.allStocks = allStocks;
	}
	
	public List<Stock> findAllByProviderid(String providerid) {
		return allStocks.findAllByProviderid(providerid);
	}
	
	public Stock getById(String id) {
		return allStocks.findById(id);
	}
	
	public List<Stock> getAll() {
		return allStocks.getAll();
	}
	
	public List<Stock> findStocks(String identifier, String vaccine_type_id, String transaction_type, String providerid,
	                              String value, String date_created, String to_from, String date_updated,
	                              Long serverVersion, String sortBy, String sortOrder, int limit) {
		return allStocks.findStocks(identifier, vaccine_type_id, transaction_type, providerid, value, date_created, to_from,
		    date_updated, serverVersion, sortBy, sortOrder, limit);
	}
	
	public List<Stock> findStocks(String identifier, String vaccine_type_id, String transaction_type, String providerid,
	                              String value, String date_created, String to_from, String date_updated,
	                              String serverVersion) {
		return allStocks.findStocks(identifier, vaccine_type_id, transaction_type, providerid, value, date_created, to_from,
		    date_updated, serverVersion);
	}
	
	public List<Stock> findAllStocks() {
		return allStocks.findAllStocks();
	}
	
	public synchronized Stock addStock(CouchDbConnector targetDb, Stock stock) {
		
		stock.setDateCreated(new DateTime());
		allStocks.add(targetDb, stock);
		return stock;
	}
	
	public Stock find(Stock stock) {
		Stock st = allStocks.findById(stock.getId());
		if (st != null) {
			throw new IllegalArgumentException("Stock with same id exist " + st.getId() + " exist.");
		} else {
			return stock;
		}
	}
	
	public synchronized Stock addStock(Stock stock) {
		Stock st = find(stock);
		if (st != null) {
			throw new IllegalArgumentException("A stock already exists with given id. Consider updating data.[" + st.getId()
			        + "]");
		}
		
		return stock;
	}
	
	public synchronized Stock addorUpdateStock(Stock stock) {
		if (stock.getId() != null && getById(stock.getId()) != null) {
			stock.setDateEdited(DateTime.now());
			stock.setServerVersion(null);
			stock.setRevision(getById(stock.getId()).getRevision());
			allStocks.update(stock);
		} else {
			stock.setDateCreated(DateTime.now());
			allStocks.add(stock);
		}
		return stock;
	}
	
	public void updateStock(Stock updatedStock) {
		// If update is on original entity
		if (updatedStock.isNew()) {
			throw new IllegalArgumentException(
			        "Stock to be updated is not an existing and persisting domain object. Update database object instead of new pojo");
		}
		
		updatedStock.setDateEdited(DateTime.now());
		
		allStocks.update(updatedStock);
	}
	
	public Stock find(String uniqueId) {
		List<Stock> sList = allStocks.findAllByProviderid(uniqueId);
		if (sList.size() > 1) {
			throw new IllegalArgumentException("Multiple events with identifier " + uniqueId + " exist.");
		} else if (sList.size() != 0) {
			return sList.get(0);
		}
		return null;
	}
	
	public Stock mergeStock(Stock updatedStock) {
		Stock original = find(updatedStock);
		if (original == null) {
			throw new IllegalArgumentException("No stock found with given id. Consider adding new!");
		}
		original.setDateEdited(DateTime.now());
		allStocks.update(original);
		return original;
	}
	
	public List<Stock> findStocksBy(String identifier, String vaccine_type_id, String transaction_type, String providerid,
	                                String value, String date_created, String to_from, String sync_status,
	                                String date_updated, String serverVersion) {
		return allStocks.findStocks(identifier, vaccine_type_id, transaction_type, providerid, value, date_created, to_from,
		    date_updated, serverVersion);
	}
	
}
