/*
 * GeoTools - The Open Source Java GIS Toolkit http://geotools.org
 *
 * (C) 2023, Open Source Geospatial Foundation (OSGeo)
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation; version 2.1 of
 * the License.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 */
package org.geotools.data.wfs.impl;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import org.apache.commons.io.FileUtils;
import org.geotools.TestData;
import org.geotools.data.wfs.TestHttpClient;
import org.geotools.data.wfs.TestWFSClient;
import org.geotools.http.MockHttpResponse;
import org.geotools.xml.resolver.SchemaCache;
import org.junit.Assert;
import org.junit.Test;

public class WFSContentDataAccessTest {

    static final String WFS_URL = "https://wfs.geonorge.no/skwms1/wfs.stedsnavn";

    /**
     * When no cache is set WFSContentDataAccess will use SchemaResolver to download and parse
     * DescribeFeatureType. Those are based on http requests, and that depends on SchemaResolver
     * being backed by a SchemaCache with download to a temporary folder.
     */
    @Test
    public void getShemaWithoutCache() throws Exception {
        TestHttpClient mockHttp = createHttpClient();
        TestWFSClient testClient = new TestWFSClient(new URL(WFS_URL), mockHttp);

        WFSContentDataAccess dataAccess = new WFSContentDataAccess(testClient);
        File tempCache;
        try {
            SchemaCache cache = dataAccess.getSchemaParser().getResolver().getCache();
            Assert.assertNotNull(cache);
            tempCache = cache.getDirectory();
            Assert.assertNotNull(tempCache);
            Assert.assertEquals(FileUtils.getTempDirectory(), tempCache.getParentFile());
            Assert.assertTrue(tempCache.exists());
        } finally {
            dataAccess.dispose();
        }
        Assert.assertFalse(tempCache.exists());
    }

    private TestHttpClient createHttpClient() throws IOException {
        TestHttpClient mockHttp = new TestHttpClient();
        mockHttp.expectGet(
                new URL("https://wfs.geonorge.no/skwms1/wfs.stedsnavn?REQUEST=GetCapabilities&SERVICE=WFS"),
                new MockHttpResponse(TestData.file(mockHttp, "KartverketNo/GetCapabilities.xml"), "text/xml"));
        return mockHttp;
    }
}
