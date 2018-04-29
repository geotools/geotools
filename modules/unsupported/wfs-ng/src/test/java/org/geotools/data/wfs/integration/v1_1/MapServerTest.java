/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2013-2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.wfs.integration.v1_1;

import static org.geotools.data.wfs.WFSTestData.url;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.URL;
import org.apache.commons.io.IOUtils;
import org.geotools.data.ows.HTTPClient;
import org.geotools.data.ows.HTTPResponse;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.wfs.AbstractTestHTTPClient;
import org.geotools.data.wfs.TestHttpResponse;
import org.geotools.data.wfs.WFSDataStore;
import org.geotools.data.wfs.WFSTestData;
import org.geotools.data.wfs.internal.WFSClient;
import org.geotools.feature.NameImpl;
import org.geotools.ows.ServiceException;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;

public class MapServerTest {

    private WFSDataStore getWFSDataStore(HTTPClient httpClient)
            throws IOException, ServiceException {
        URL capabilitiesUrl =
                new URL(
                        "http://127.0.0.1:8888/mapserver?service=WFS&version=1.1.0&REQUEST=GetCapabilities");

        WFSDataStore wfs =
                new WFSDataStore(
                        new WFSClient(
                                capabilitiesUrl, httpClient, WFSTestData.getGmlCompatibleConfig()));

        return wfs;
    }

    @Test
    public void testGetFeatures() throws Exception {

        WFSDataStore wfs =
                getWFSDataStore(
                        new AbstractTestHTTPClient() {

                            @Override
                            public HTTPResponse get(URL url) throws IOException {
                                if (url.getQuery().contains("REQUEST=GetCapabilities")) {
                                    return new TestHttpResponse(
                                            url("MapServer/GetCapabilities_1_1_0.xml"), "text/xml");
                                } else {
                                    return new TestHttpResponse(
                                            url("MapServer/GetFeature_GovernmentalUnitCE.xml"),
                                            "text/xml");
                                }
                            }

                            @Override
                            public HTTPResponse post(
                                    URL url, InputStream postContent, String postContentType)
                                    throws IOException {
                                String request =
                                        new String(IOUtils.toByteArray(postContent), "UTF-8");
                                if (request.contains("<wfs:DescribeFeatureType")) {
                                    return new TestHttpResponse(
                                            url(
                                                    "MapServer/DescribeFeatureType_GovernmentalUnitCE.xsd"),
                                            "text/xml");
                                } else {
                                    return new TestHttpResponse(
                                            url("MapServer/GetFeature_GovernmentalUnitCE.xml"),
                                            "text/xml");
                                }
                            }
                        });

        SimpleFeatureSource source =
                wfs.getFeatureSource(
                        new NameImpl(
                                "http://mapserver.gis.umn.edu/mapserver", "ms_GovernmentalUnitCE"));
        SimpleFeatureCollection features = source.getFeatures();
        SimpleFeatureIterator reader = features.features();
        SimpleFeature sf = null;
        try {
            if (reader.hasNext()) {
                sf = reader.next();
                assertNotNull(sf);
                assertTrue(sf.getAttribute("typeAbbreviation") instanceof String);
                assertTrue(sf.getAttribute("number") instanceof BigInteger);
                assertTrue(sf.getAttribute("doubleNumber") instanceof Double);
            }
            assertNotNull(sf);
        } finally {
            reader.close();
        }
    }
}
