package org.opensrp.web;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.ServletException;
import java.io.IOException;

//TODO: Find a way to test authentication filter.
@Ignore
public class AuthenticationFilterTest {

	@Test
	public void testAuthenticationFilter() throws ServletException, IOException {
		AuthenticationFilter authenticationFilterUnderTest = new AuthenticationFilter();

		//authenticationFilterUnderTest(new MockModeService(ModeService.ONLINE));
		MockFilterChain mockChain = new MockFilterChain();
		MockHttpServletRequest mockRequest = new MockHttpServletRequest("GET", "/authenticate-user/");
		mockRequest.addHeader("www-authenticate", "tests");

		MockHttpServletResponse mockResponse = new MockHttpServletResponse();
		mockResponse.addHeader("www-authenticate", "tests");

		authenticationFilterUnderTest.doFilter(mockRequest, mockResponse, mockChain);

		System.out.println(mockResponse.getHeaderNames());

		//assertEquals("/",mockResponse.getForwardedUrl());
	}

}
