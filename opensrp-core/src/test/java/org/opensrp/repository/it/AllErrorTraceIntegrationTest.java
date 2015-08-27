package org.opensrp.repository.it;

import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opensrp.domain.ErrorTrace;
import org.opensrp.repository.AllErrorTrace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-applicationContext-opensrp.xml")
public class AllErrorTraceIntegrationTest {

	@Autowired
	private AllErrorTrace allErrorTrace;
	@Before
	public void setUp() throws Exception {
		initMocks(this);
	}
	
	@Test
	public void shouldAddError()throws Exception
	{
		//ErrorTrace error=new ErrorTrace(new Date(), "Error Testing" , "not availalbe","this is an Testing Error", "unsolved");
		ErrorTrace error=new ErrorTrace();
		error.setErrorType("error loggging test");
		error.setDate(new Date());
		error.setStackTrace("Complete Stack Trace :");
		error.setStatus("unsolved");
		error.setDocumentType("Test Document");
	//	error.setErrorType("test Error");
		allErrorTrace.add(error);
		
	}
	
	
	
}
