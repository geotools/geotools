/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.ows.wmts;

import java.util.Collections;
import java.util.HashMap;
import org.junit.Assert;
import org.junit.Test;

/** @author Roar Brænden */
public class WMTSHelperTest {

    @Test
    public void testReplaceToken() throws Exception {
        String resultWithout =
                WMTSHelper.replaceToken("http://dummy.net/without/token", "token", "value");
        Assert.assertEquals(
                "baseUrl should be unaltered.", "http://dummy.net/without/token", resultWithout);

        String resultWith =
                WMTSHelper.replaceToken("http://dummy.net/with/{token}", "token", "value");
        Assert.assertEquals(
                "token should be replaced with value", "http://dummy.net/with/value", resultWith);

        String resultWithNullValue =
                WMTSHelper.replaceToken("http://dummy.net/with/{token}", "token", null);
        Assert.assertEquals(
                "token should be replaced with empty string",
                "http://dummy.net/with/",
                resultWithNullValue);
    }

    @Test
    public void testAppendQueryString() throws Exception {

        String resultPlain =
                WMTSHelper.appendQueryString(
                        "http://dummy.net/something/without/querstion",
                        Collections.singletonMap("request", "yes"));
        Assert.assertEquals(
                "A question mark should be added.",
                "http://dummy.net/something/without/querstion?request=yes",
                resultPlain);

        String resultExisting =
                WMTSHelper.appendQueryString(
                        "http://dummy.net/something/parameter/exists?request=original",
                        Collections.singletonMap("request", "new"));
        Assert.assertEquals(
                "An existing parameter should be kept.",
                "http://dummy.net/something/parameter/exists?request=original",
                resultExisting);

        HashMap<String, String> params = new HashMap<>();
        params.put("param2", "2");
        params.put("param3", "<3");
        String resultTwoWithEncoding =
                WMTSHelper.appendQueryString(
                        "http://dummy.net/something/with/parameters?request=one", params);
        Assert.assertTrue("param2 is missing", resultTwoWithEncoding.contains("&param2=2"));
        Assert.assertTrue(
                "param3 is missing or not encoded.",
                resultTwoWithEncoding.contains("&param3=%3C3"));

        params.put("param3", "{TileCol}");
        String resultPlaceholderKept =
                WMTSHelper.appendQueryString(
                        "http://dummy.net/something/with/parameters?request=one", params);
        Assert.assertTrue(
                "Values with { } should be kept un-encoded.",
                resultPlaceholderKept.contains("&param3={TileCol}"));
    }

    @Test
    public void testSpacesInUrl() throws Exception {
        String result = WMTSHelper.usePercentEncodingForSpace("name with space");
        Assert.assertEquals(
                "Spaces should be replaced with %20 instead of +", "name%20with%20space", result);
    }
}
