package org.opensrp.web.rest.it;

import ch.lambdaj.function.convert.Converter;
import org.codehaus.jackson.JsonNode;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.opensrp.domain.Stock;
import org.opensrp.repository.AllStocks;
import org.opensrp.web.rest.StockResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.util.NestedServletException;

import java.util.Collections;
import java.util.List;

import static ch.lambdaj.collection.LambdaCollections.with;
import static java.util.Arrays.asList;
import static org.junit.Assert.*;
import static org.opensrp.common.AllConstants.Stock.PROVIDERID;
import static org.opensrp.common.AllConstants.Stock.TIMESTAMP;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;

/**
 * TODO: Solve bug at source {@link StockResource} and refactor like {@link EventResourceTest}
 */
public class StockResourceTest extends BaseResourceTest {

	private static final String BASE_URL = "/rest/stockresource";

	@Autowired
	private AllStocks allStocks;

	@Autowired
	private StockResource stockResource;

	@Before
	public void setUp() {
		allStocks.removeAll();
	}

	@After
	public void cleanUp() {
		allStocks.removeAll();
	}

	@Test
	public void testRequiredProperties() {
		List<String> actualRequiredProperties = stockResource.requiredProperties();

		assertEquals(2, actualRequiredProperties.size());
		assertTrue(actualRequiredProperties.contains(PROVIDERID));
		assertTrue(actualRequiredProperties.contains(TIMESTAMP));
	}

	//TODO: `Stock.class` does't contain `timestamp` field.
	@Test
	@Ignore
	public void testStockClassHasAllTheRequiredField() {
		assetClassHasAllRequiredFields(Stock.class, stockResource.requiredProperties());
	}

	//TODO: Error in `couchdb` query.
	@Test
	@Ignore
	public void shouldFindByProviderId() throws Exception {
		Stock expectedStock = new Stock(200l, "vaccineTypeId", "transactionType", "providerId", 3,
				new DateTime(0l, DateTimeZone.UTC).getMillis(), "toFrom", new DateTime(0l, DateTimeZone.UTC).getMillis(),
				223l);
		addObjectToRepository(Collections.singletonList(expectedStock), allStocks);

		JsonNode responseJson = getCallAsJsonNode(BASE_URL + "/provider", "", status().isOk());
		Stock actualStock = mapper.treeToValue(responseJson, Stock.class);

		assertEquals(expectedStock, actualStock);
	}

	@Test
	public void shouldNotFindStock() throws Exception {
		Stock expectedStock = new Stock(200l, "vaccineTypeId", "transactionType", "providerId", 3,
				new DateTime(0l, DateTimeZone.UTC).getMillis(), "toFrom", new DateTime(0l, DateTimeZone.UTC).getMillis(),
				223l);
		addObjectToRepository(Collections.singletonList(expectedStock), allStocks);

		JsonNode responseJson = getCallAsJsonNode(BASE_URL + "/invalidProviderId", "", status().isOk());
		assertNull(responseJson);
	}

	@Test
	public void shouldGetAllStocks() throws Exception {
		String url = BASE_URL + "/getall";
		Stock expectedStock1 = new Stock(200l, "vaccineTypeId", "transactionType", "providerId", 3,
				new DateTime(0l, DateTimeZone.UTC).getMillis(), "toFrom", new DateTime(0l, DateTimeZone.UTC).getMillis(),
				223l);
		Stock expectedStock2 = new Stock(300l, "vaccineTypeId", "transactionType", "providerId1", 3,
				new DateTime(10l, DateTimeZone.UTC).getMillis(), "toFrom", new DateTime(10l, DateTimeZone.UTC).getMillis(),
				223l);
		Stock expectedStock3 = new Stock(400l, "vaccineTypeId", "transactionType", "providerId2", 3,
				new DateTime(100l, DateTimeZone.UTC).getMillis(), "toFrom", new DateTime(100l, DateTimeZone.UTC).getMillis(),
				223l);
		List<Stock> expectedStocks = asList(expectedStock1, expectedStock2, expectedStock3);

		addObjectToRepository(expectedStocks, allStocks);
		JsonNode responseJson = getCallAsJsonNode(url, "", status().isOk());

		final List<Stock> actualStocks = createObjectListFromJson(responseJson.get("stocks"), Stock.class);

		assertTwoListAreSameIgnoringOrder(expectedStocks, actualStocks);
	}

	//TODO: There is bug in production code. in `StockService.java` method `addStock()`.
	@Test
	@Ignore
	public void shouldCreateValidStock() throws Exception {
		Stock expectedStock = new Stock(200l, "vaccineTypeId", "transactionType", "providerId", 3,
				new DateTime(0l, DateTimeZone.UTC).getMillis(), "toFrom", new DateTime(0l, DateTimeZone.UTC).getMillis(),
				223l);
		String postData = mapper.writeValueAsString(expectedStock);

		postCallWithJsonContent(BASE_URL + "/", postData, status().isOk());
		Stock actualStock = allStocks.getAll().get(0);

		assertEquals(expectedStock, actualStock);
	}

	@Test(expected = NestedServletException.class)
	public void shouldFailCreateStockWithOutProviderId() throws Exception {
		Stock expectedStock = new Stock(200l, "vaccineTypeId", "transactionType", "providerId", 3,
				new DateTime(0l, DateTimeZone.UTC).getMillis(), "toFrom", new DateTime(0l, DateTimeZone.UTC).getMillis(),
				223l);
		expectedStock.setProviderid(null);
		String postData = mapper.writeValueAsString(expectedStock);

		postCallWithJsonContent(BASE_URL + "/", postData, status().isOk());
	}

	//TODO: `Stock.class` doesn't have a field call `timestamp`
	/*@Test(expected = NestedServletException.class)
	public void shouldFailCreateStockWithOutTimestamp() throws Exception{
		Stock expectedStock = new Stock(200l, "vaccineTypeId", "transactionType", "providerId", 3,
				new DateTime(0l, DateTimeZone.UTC).getMillis(), "toFrom", new DateTime(0l, DateTimeZone.UTC).getMillis(),
				223l);
		expectedStock.setProviderid(null);
		String postData = mapper.writeValueAsString(expectedStock);

		postCallWithJsonContent(BASE_URL + "/", postData, status().isOk());
	}*/

	@Test
	public void shouldCreateValidStockUsingAddUrl() throws Exception {
		Stock expectedStock1 = new Stock(200l, "vaccineTypeId", "transactionType", "providerId", 3,
				new DateTime(0l, DateTimeZone.UTC).getMillis(), "toFrom", new DateTime(0l, DateTimeZone.UTC).getMillis(),
				223l);
		Stock expectedStock2 = new Stock(300l, "vaccineTypeId", "transactionType", "providerId1", 3,
				new DateTime(10l, DateTimeZone.UTC).getMillis(), "toFrom", new DateTime(10l, DateTimeZone.UTC).getMillis(),
				223l);
		Stock expectedStock3 = new Stock(400l, "vaccineTypeId", "transactionType", "providerId2", 3,
				new DateTime(100l, DateTimeZone.UTC).getMillis(), "toFrom", new DateTime(100l, DateTimeZone.UTC).getMillis(),
				223l);
		List<Stock> expectedStocks = asList(expectedStock1, expectedStock2, expectedStock3);
		String postData = "{\"stocks\":" + mapper.writeValueAsString(expectedStocks) + "}";

		postCallWithJsonContent(BASE_URL + "/add", postData, status().isCreated());

		List<Stock> actualStocks = with(allStocks.getAll()).convert(new Converter<Stock, Stock>() {

			@Override
			public Stock convert(Stock stock) {
				stock.setDateCreated(null);
				return stock;
			}
		});

		assertTwoListAreSameIgnoringOrder(expectedStocks, actualStocks);
	}

	@Test
	@Ignore
	public void shouldUpdateExistingStockUsingAddUrl() throws Exception {
		Stock expectedStock = new Stock(200l, "vaccineTypeId", "transactionType", "providerId", 3,
				new DateTime(0l, DateTimeZone.UTC).getMillis(), "toFrom", new DateTime(0l, DateTimeZone.UTC).getMillis(),
				223l);
		Stock unchangedStock = new Stock(300l, "vaccineTypeId", "transactionType", "providerId1", 3,
				new DateTime(10l, DateTimeZone.UTC).getMillis(), "toFrom", new DateTime(10l, DateTimeZone.UTC).getMillis(),
				223l);
		Stock unchangedStock2 = new Stock(400l, "vaccineTypeId", "transactionType", "providerId2", 3,
				new DateTime(100l, DateTimeZone.UTC).getMillis(), "toFrom", new DateTime(100l, DateTimeZone.UTC).getMillis(),
				223l);
		List<Stock> stocks = asList(expectedStock, unchangedStock, unchangedStock2);
		addObjectToRepository(stocks, allStocks);
		//expectedStock = allStocks.f
		expectedStock.setProviderid("updatedProviderId");
		List<Stock> expectedStocks = asList(expectedStock, unchangedStock, unchangedStock2);
		String postData = "{\"stocks\":" + mapper.writeValueAsString(Collections.singletonList(expectedStock)) + "}";

		postCallWithJsonContent(BASE_URL + "/add", postData, status().isCreated());

		List<Stock> actualStocks = with(allStocks.getAll()).convert(new Converter<Stock, Stock>() {

			@Override
			public Stock convert(Stock stock) {
				stock.setDateCreated(null);
				return stock;
			}
		});

		assertTwoListAreSameIgnoringOrder(expectedStocks, actualStocks);
	}

}
