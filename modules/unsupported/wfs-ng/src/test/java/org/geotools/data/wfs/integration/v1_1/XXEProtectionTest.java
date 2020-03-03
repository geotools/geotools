/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
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

import static org.junit.Assert.*;

import java.net.URL;
import org.geotools.data.DataUtilities;
import org.geotools.data.ows.HTTPResponse;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.data.wfs.TestHttpResponse;
import org.geotools.data.wfs.WFSDataStore;
import org.geotools.data.wfs.WFSTestData;
import org.geotools.data.wfs.integration.IntegrationTestWFSClient;
import org.geotools.data.wfs.internal.WFSConfig;
import org.junit.Test;
import org.opengis.filter.Filter;
import org.xml.sax.SAXException;

public class XXEProtectionTest {

    @Test
    public void testGetCapabilitiesProtection() throws Exception {
        // the default config has entity resolution disabled
        WFSConfig config = WFSTestData.getGmlCompatibleConfig();

        String baseDirectory = "GeoServer_2.0/1.1.0_XXE_GetCapabilities";
        try {
            new IntegrationTestWFSClient(baseDirectory, config);
            fail("Should have failed with an entity resolution exception");
        } catch (Exception e) {
            assertEntityResolutionDisabled(e);
        }
    }

    private void assertEntityResolutionDisabled(Throwable t) {
        if (t instanceof SAXException) {
            SAXException se = (SAXException) t;
            String message = se.getMessage();
            if (message != null && message.contains("Entity resolution disallowed")) {
                // fine, we found the message
                return;
            }
        }

        // if we got here we drill down or fail, the message was not found
        if (t.getCause() != null) {
            assertEntityResolutionDisabled(t.getCause());
        } else {
            fail("Could not find the message about entity resolution disabled");
        }
    }

    /** The pull parser we use has entity resolution disabled, make sure it stays that way */
    @Test
    public void testGetFeatureProtection() throws Exception {
        WFSConfig config = WFSTestData.getGmlCompatibleConfig();
        String baseDirectory = "GeoServer_2.0/1.1.0_XXE_GetFeature/";
        IntegrationTestWFSClient client = new IntegrationTestWFSClient(baseDirectory, config);
        WFSDataStore store = new WFSDataStore(client);
        SimpleFeatureSource fs = store.getFeatureSource(store.getTypeNames()[0]);
        try {
            DataUtilities.first(fs.getFeatures());
        } catch (Exception e) {
            // custom check as MXParser does not use the EntityResolver, does not offer a way to
            // configure it
            final String msg = e.getMessage();
            assertNotNull(msg);
            assertTrue(msg.contains("could not resolve entity"));
            assertTrue(msg.contains("xxe"));
        }
    }

    @Test
    public void testTransactionProtection() throws Exception {
        WFSConfig config = WFSTestData.getGmlCompatibleConfig();
        String baseDirectory = "GeoServer_2.0/1.1.0_XXE_Transaction/";
        IntegrationTestWFSClient client =
                new IntegrationTestWFSClient(baseDirectory, config) {
                    @Override
                    protected org.geotools.data.ows.Response mockTransactionSuccess(
                            org.geotools.data.wfs.internal.TransactionRequest request)
                            throws java.io.IOException {
                        String resource = "Transaction.xml";
                        URL contentUrl = new URL(baseDirectory, resource);

                        HTTPResponse httpResp =
                                new TestHttpResponse("text/xml", "UTF-8", contentUrl);
                        return request.createResponse(httpResp);
                    };
                };
        WFSDataStore store = new WFSDataStore(client);
        SimpleFeatureStore fs =
                (SimpleFeatureStore) store.getFeatureSource(store.getTypeNames()[0]);
        try {
            fs.removeFeatures(Filter.INCLUDE);
        } catch (Exception e) {
            assertEntityResolutionDisabled(e);
        }
    }
}
