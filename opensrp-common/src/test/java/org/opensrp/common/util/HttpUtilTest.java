package org.opensrp.common.util;


import org.junit.Test;

import java.net.URISyntaxException;

import static junit.framework.Assert.assertEquals;

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
        HttpUtil.makeConnection(invalidUrl,"", RequestMethod.DELETE, HttpUtil.AuthType.BASIC, "");
    }
}
