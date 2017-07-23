package org.opensrp.web.rest.it;

import static org.mockito.MockitoAnnotations.initMocks;
import static org.opensrp.common.AllConstants.Stock.IDENTIFIER;
import static org.opensrp.common.AllConstants.Stock.PROVIDERID;
import static org.opensrp.common.AllConstants.Stock.VACCINE_TYPE_ID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.opensrp.service.StockService;
import org.opensrp.web.rest.StockResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/applicationContext-opensrp-web.xml")
public class StockResourceTest {

	private static final String BASE_URL = "http://localhost:8080/opensrp-web/stock";  
	private RestTemplate restTemplate;  

	MockHttpServletRequest req;

	@Mock
	private MockHttpServletResponse resp = new MockHttpServletResponse();

	@Autowired
	private StockResource ss;
	@Autowired
	private StockService stockService;
	 @Before
	    public void setUp() throws Exception {
	        initMocks(this);
	      
	        
	    }
	
	@Test
	public void testClientSearch() throws Exception {
		  MockHttpServletRequest mockRequest = new MockHttpServletRequest();
		  //  mockRequest.setContentType(MediaType.APPLICATION_JSON.toString());
		  mockRequest.setMethod("GET");
		  mockRequest.setRequestURI("/rest/stock/sync/");
		  mockRequest.setAttribute(HandlerMapping.class.getName() + ".introspectTypeLevelMapping", true);
		mockRequest.addParameter(IDENTIFIER, "003");
		mockRequest.addParameter(VACCINE_TYPE_ID, "VTID");
		mockRequest.addParameter(PROVIDERID, "4-2");
		

		
		  AnnotationMethodHandlerAdapter handlerAdapter = new AnnotationMethodHandlerAdapter();
		  HttpMessageConverter[] messageConverters = {new MappingJacksonHttpMessageConverter()};
		  handlerAdapter.setMessageConverters(messageConverters);
		
		  MockHttpServletResponse mockResponse = new MockHttpServletResponse();
		  mockResponse.addHeader("chii", "noa");
		  handlerAdapter.handle(mockRequest, mockResponse, ss);
		
		  String actual = mockResponse.getContentAsString();
		  System.out.println(actual);

	}
}
