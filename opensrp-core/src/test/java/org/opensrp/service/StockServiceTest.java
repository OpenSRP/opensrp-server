package org.opensrp.service;

import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.opensrp.domain.Stock;
import org.opensrp.repository.AllStocks;
import org.opensrp.service.StockService;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.List;
import static java.util.Arrays.asList;

/**
 * Tests {@link RegistrationDataService}.
 */
public class StockServiceTest {
	
	@Mock
	private AllStocks allStocks;
	
	@Mock
	private StockService stockService;
	
	@Before
	public void setUp() throws Exception {
		initMocks(this);
		stockService = new StockService(allStocks);
		
		Stock stock1 = new Stock(Long.parseLong("123"), "VT", "TT", "4-2", 10, Long.parseLong("20062017"), "TF",
		        Long.parseLong("20062017"), Long.parseLong("12345"));
		Stock stock2 = new Stock(Long.parseLong("123"), "VT", "TT", "4-2", 10, Long.parseLong("20062017"), "TF",
		        Long.parseLong("20062017"), Long.parseLong("12345"));
		when(stockService.getAll()).thenReturn(asList(stock1, stock2));
		when(stockService.findAllByProviderid("4-2")).thenReturn(asList(stock1));
		
	}
	
	@Test
	public void shouldSaveStock() {
		Stock stock = new Stock(Long.parseLong("123"), "VT", "TT", "4-2", 10, Long.parseLong("20062017"), "	TF",
		        Long.parseLong("20062017"), Long.parseLong("12345"));
		
		Stock savedStock = stockService.addorUpdateStock(stock);
		Assert.assertNotNull(savedStock.getServerVersion());
		Assert.assertEquals(savedStock.getProviderid(), stock.getProviderid());
		
	}
	
	@Test
	public void shouldFetchStocksByProcider() throws Exception {
		Stock stock1 = new Stock(Long.parseLong("123"), "VT", "TT", "4-2", 10, Long.parseLong("20062017"), "TF",
		        Long.parseLong("20062017"), Long.parseLong("12345"));
		
		List<Stock> fecthedListByProviderid = stockService.findAllByProviderid("4-2");
		
		Assert.assertEquals(fecthedListByProviderid.get(0).getProviderid(), stock1.getProviderid());
	}
	
	@Test
	public void shouldFetchAll() throws Exception {
		Stock stock1 = new Stock(Long.parseLong("123"), "VT", "TT", "4-2", 10, Long.parseLong("20062017"), "TF",
		        Long.parseLong("20062017"), Long.parseLong("12345"));
		Stock stock2 = new Stock(Long.parseLong("123"), "VT", "TT", "4-2", 10, Long.parseLong("20062017"), "TF",
		        Long.parseLong("20062017"), Long.parseLong("12345"));
		
		List<Stock> fecthedListAll = stockService.getAll();
		
		List<Stock> expectedListAll = asList(stock1, stock2);
		
		Assert.assertEquals(fecthedListAll.get(0).getProviderid(), expectedListAll.get(0).getProviderid());
	}
	
}
