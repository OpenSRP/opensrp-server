package org.opensrp.common.util;


import org.apache.http.Header;
import org.apache.http.client.methods.HttpRequestBase;
import org.junit.Test;
import sun.misc.BASE64Encoder;

import java.net.URISyntaxException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.opensrp.common.util.HttpUtil.*;

public class HttpUtilIntegrationTest {

    @Test
    public void testRemovingTrailingSlashes() {
        String inputStringWithOneSlash = "/inputString";
        String expectedOutputStringForOneSlash = "inputString";

        String outputStringForOneSlash = HttpUtil.removeTrailingSlash(inputStringWithOneSlash);

        assertEquals(expectedOutputStringForOneSlash, outputStringForOneSlash);
    }

    @Test
    public void testRemoveEndingSlashes() {
        String inputStringWithOneSlash = "inputString/";
        String expectedOutputStringForOneSlash = "inputString";

        String outputStringForOneSlash = HttpUtil.removeEndingSlash(inputStringWithOneSlash);

        assertEquals(expectedOutputStringForOneSlash, outputStringForOneSlash);
    }

    @Test(expected = URISyntaxException.class)
    public void testMakeConnectionThorowsUriSyntaxException() throws URISyntaxException {
        String invalidUrl = "http://invalidURL^$&%$&^";
        makeConnection(invalidUrl,"", RequestMethod.DELETE, HttpUtil.AuthType.BASIC, "");
    }

    @Test
    public void testMakingConnectionWithTrailingSlashes() throws URISyntaxException {
        String url = "www.google.com/";
        String expectedUrl = "www.google.com";
        String payLoad = "";
        RequestMethod method = RequestMethod.GET;
        HttpUtil.AuthType authType = HttpUtil.AuthType.NONE;

        HttpRequestBase requestBase = makeConnection(url, payLoad, method, authType, "");

        assertEquals(expectedUrl, requestBase.getURI().toString());
        assertEquals(method.name(), requestBase.getMethod());
    }

    @Test
    public void testMakingValidConnectionWithoutPayload() throws URISyntaxException {
        String url = "www.google.com";
        String payLoad = "";
        RequestMethod method = RequestMethod.GET;
        HttpUtil.AuthType authType = HttpUtil.AuthType.NONE;

        HttpRequestBase requestBase = makeConnection(url, payLoad, method, authType, "");

        assertEquals(url, requestBase.getURI().toString());
        assertEquals(method.name(), requestBase.getMethod());
    }

    @Test
    public void testMakingConnectionWithPayload() throws URISyntaxException {
        String url = "www.google.com";
        String payLoad = "payload";
        String expectedUrl = url + "?" + payLoad;
        RequestMethod method = RequestMethod.GET;
        HttpUtil.AuthType authType = HttpUtil.AuthType.NONE;

        HttpRequestBase requestBase = makeConnection(url, payLoad, method, authType, "");

        assertEquals(expectedUrl, requestBase.getURI().toString());
        assertEquals(method.name(), requestBase.getMethod());
    }

    @Test
    public void testMakingConnectionWithPostMethod() throws URISyntaxException {
        String url = "www.google.com";
        String payLoad = "payload";
        String expectedUrl = url + "?" + payLoad;
        RequestMethod method = RequestMethod.POST;
        HttpUtil.AuthType authType = HttpUtil.AuthType.NONE;

        HttpRequestBase requestBase = makeConnection(url, payLoad, method, authType, "");

        assertEquals(expectedUrl, requestBase.getURI().toString());
        assertEquals(method.name(), requestBase.getMethod());
    }

    @Test
    public void testMakingConnectionWithDeleteMethod() throws URISyntaxException {
        String url = "www.google.com";
        String payLoad = "payload";
        String expectedUrl = url + "?" + payLoad;
        RequestMethod method = RequestMethod.DELETE;
        HttpUtil.AuthType authType = HttpUtil.AuthType.NONE;

        HttpRequestBase requestBase = makeConnection(url, payLoad, method, authType, "");

        assertEquals(expectedUrl, requestBase.getURI().toString());
        assertEquals(method.name(), requestBase.getMethod());
    }

    @Test
    public void testMakingConnectionWithPutMethod() throws URISyntaxException {
        String url = "www.google.com";
        String payLoad = "payload";
        String expectedUrl = url + "?" + payLoad;
        RequestMethod method = RequestMethod.PUT;
        HttpUtil.AuthType authType = HttpUtil.AuthType.NONE;

        HttpRequestBase requestBase = makeConnection(url, payLoad, method, authType, "");

        assertEquals(expectedUrl, requestBase.getURI().toString());
        assertEquals(method.name(), requestBase.getMethod());
    }

    @Test(expected = NullPointerException.class)
    public void testMakingConnectionWithNullMethod() throws URISyntaxException {
        String url = "www.google.com";
        String payLoad = "payload";
        String expectedUrl = url + "?" + payLoad;
        RequestMethod method = null;
        HttpUtil.AuthType authType = HttpUtil.AuthType.NONE;

        HttpRequestBase requestBase = makeConnection(url, payLoad, method, authType, "");

        assertEquals(expectedUrl, requestBase.getURI().toString());
        assertEquals(method.name(), requestBase.getMethod());
    }

    @Test
    public void testMakingConnectionWithBasicAuthType() throws URISyntaxException {
        String url = "www.google.com";
        RequestMethod method = RequestMethod.PUT;
        HttpUtil.AuthType authType = HttpUtil.AuthType.BASIC;
        String authString = "userName:pass";
        String expectedAuthString = "Basic " + new BASE64Encoder().encode(authString.getBytes());

        HttpRequestBase requestBase = makeConnection(url, "", method, authType, authString);
        Header[] headers = requestBase.getHeaders("Authorization");
        String outputAuthString = headers[0].getValue();

        assertEquals(url, requestBase.getURI().toString());
        assertEquals(method.name(), requestBase.getMethod());
        assertEquals(expectedAuthString, outputAuthString);
    }

    @Test
    public void testMakingConnectionWithToken() throws URISyntaxException {
        String url = "www.google.com";
        RequestMethod method = RequestMethod.PUT;
        HttpUtil.AuthType authType = HttpUtil.AuthType.TOKEN;
        String authString = "userName:pass";
        String expectedAuthString = "Token " + authString;

        HttpRequestBase requestBase = makeConnection(url, "", method, authType, authString);
        Header[] headers = requestBase.getHeaders("Authorization");
        String outputAuthString = headers[0].getValue();

        assertEquals(url, requestBase.getURI().toString());
        assertEquals(method.name(), requestBase.getMethod());
        assertEquals(expectedAuthString, outputAuthString);
    }

    @Test
    public void testSuccessfulDeleteMethod() {
        String url = "http://httpbin.org/delete";
        String payLoad = "payload";
        HttpUtil.AuthType authType = HttpUtil.AuthType.NONE;

        HttpResponse response = delete(url, payLoad, authType, "");

        assertEquals(200, response.statusCode().intValue());
        assertTrue(response.isSuccess());
    }

    @Test
    public void testUnsuccessfulDeleteMethod() {
        String url = "http://httpbin.org";
        String payLoad = "payload";
        HttpUtil.AuthType authType = HttpUtil.AuthType.NONE;

        HttpResponse response = delete(url, payLoad, authType, "");

        assertEquals(405, response.statusCode().intValue());
        assertFalse(response.isSuccess());
    }

    @Test(expected = RuntimeException.class)
    public void testDeleteMethodForException() {
        delete(null, null, "", null);
    }

    @Test
    public void testSuccessfulGetMethod() {
        String url = "http://httpbin.org/get";
        HttpUtil.AuthType authType = HttpUtil.AuthType.NONE;

        HttpResponse response = get(url, "", authType, "");

        assertEquals(200, response.statusCode().intValue());
        assertTrue(response.isSuccess());
    }

    @Test
    public void testSuccessfulGetMethodWithNoContent() {
        String url = "http://httpstat.us/204";
        HttpUtil.AuthType authType = HttpUtil.AuthType.NONE;

        HttpResponse response = get(url, "", authType, "");

        assertEquals(204, response.statusCode().intValue());
        assertTrue(response.isSuccess());
    }

    @Test
    public void testUnsuccessfulGetMethod() {
        String url = "http://httpbin.org/delete";
        String payLoad = "payload";
        HttpUtil.AuthType authType = HttpUtil.AuthType.NONE;

        HttpResponse response = get(url, payLoad, authType, "");

        assertEquals(405, response.statusCode().intValue());
        assertFalse(response.isSuccess());
    }

    @Test(expected = RuntimeException.class)
    public void testGetMethodForException() {
        get(null, null, "", null);
    }


    @Test
    public void testSuccessfulPostMethod() {
        String url = "http://httpbin.org/post";
        HttpUtil.AuthType authType = HttpUtil.AuthType.NONE;

        HttpResponse response = post(url,"", "", "text", authType, "");

        assertEquals(200, response.statusCode().intValue());
        assertTrue(response.isSuccess());
    }

    @Test
    public void testSuccessfulPostMethodWithNoContent() {
        String url = "http://httpstat.us/204";
        HttpUtil.AuthType authType = HttpUtil.AuthType.NONE;

        HttpResponse response = post(url, "","","", authType, "");

        assertEquals(204, response.statusCode().intValue());
        assertTrue(response.isSuccess());
    }

    @Test
    public void testUnsuccessfulPostMethod() {
        String url = "http://httpbin.org/get";
        String payLoad = "payload";
        HttpUtil.AuthType authType = HttpUtil.AuthType.NONE;

        HttpResponse response = post(url, "","","", authType, "");

        assertEquals(405, response.statusCode().intValue());
        assertFalse(response.isSuccess());
    }

    @Test(expected = RuntimeException.class)
    public void testPostMethodForException() {
        post(null, null,null, "", null);
    }

    @Test
    public void testCheckHttpCallSuccessBasedOnReturnCode() {
        assertTrue(checkSuccessBasedOnHttpCode(200));
        assertTrue(checkSuccessBasedOnHttpCode(303));
        assertFalse(checkSuccessBasedOnHttpCode(404));
        assertFalse(checkSuccessBasedOnHttpCode(505));
    }
}
