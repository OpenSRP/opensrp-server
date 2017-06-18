package org.opensrp.common.util;


import org.apache.http.client.methods.HttpRequestBase;
import org.junit.Test;

import java.net.URISyntaxException;

import static junit.framework.Assert.assertEquals;
import static org.opensrp.common.util.HttpUtil.makeConnection;

public class HttpUtilTest {

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
}
