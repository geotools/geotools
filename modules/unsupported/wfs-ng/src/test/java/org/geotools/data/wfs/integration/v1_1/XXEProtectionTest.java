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

import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.URL;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.data.SimpleFeatureStore;
import org.geotools.api.filter.Filter;
import org.geotools.data.DataUtilities;
import org.geotools.data.ows.Response;
import org.geotools.data.wfs.TestHttpResponse;
import org.geotools.data.wfs.WFSDataStore;
import org.geotools.data.wfs.WFSTestData;
import org.geotools.data.wfs.integration.IntegrationTestWFSClient;
import org.geotools.data.wfs.internal.TransactionRequest;
import org.geotools.data.wfs.internal.WFSConfig;
import org.geotools.http.HTTPResponse;
import org.geotools.ows.ServiceException;
import org.geotools.util.PreventLocalEntityResolver;
import org.junit.Test;
import org.xml.sax.SAXException;

public class XXEProtectionTest {

    @Test
    public void testGetCapabilitiesProtection() throws Exception {
        // the default config has entity resolution disabled
        WFSConfig config = WFSTestData.getGmlCompatibleConfig();
        ((WFSTestData.MutableWFSConfig) config).setEntityResolver(PreventLocalEntityResolver.INSTANCE);

        String baseDirectory = "GeoServer_2.0/1.1.0_XXE_GetCapabilities";
        try {
            new IntegrationTestWFSClient(baseDirectory, config);
            fail("Should have failed with an entity resolution exception");
        } catch (Exception e) {
            assertEntityResolutionDisabled(e);
        }
    }

    private void assertEntityResolutionDisabled(Throwable t) {
        if (t instanceof SAXException se) {
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
        ((WFSTestData.MutableWFSConfig) config).setEntityResolver(PreventLocalEntityResolver.INSTANCE);

        String baseDirectory = "GeoServer_2.0/1.1.0_XXE_GetFeature/";
        IntegrationTestWFSClient client = new IntegrationTestWFSClient(baseDirectory, config);
        WFSDataStore store = new WFSDataStore(client);
        SimpleFeatureSource fs = store.getFeatureSource(store.getTypeNames()[0]);
        try {
            DataUtilities.first(fs.getFeatures());
            fail("Should have failed with an entity resolution exception");
        } catch (Exception e) {
            assertEntityResolutionDisabled(e);
        }
    }

    /** Ensure pull parser we use has entity resolution disabled, make sure it stays that way */
    @Test
    public void testDescribeFeatureTypeProtection() throws Exception {
        WFSConfig config = WFSTestData.getGmlCompatibleConfig();
        ((WFSTestData.MutableWFSConfig) config).setEntityResolver(PreventLocalEntityResolver.INSTANCE);

        String baseDirectory = "GeoServer_2.0/1.1.0_XXE_DescribeFeatureType/";
        IntegrationTestWFSClient client = new IntegrationTestWFSClient(baseDirectory, config);
        WFSDataStore store = new WFSDataStore(client);
        SimpleFeatureSource fs = store.getFeatureSource(store.getTypeNames()[0]);
        try {
            fs.getSchema();
            fail("Should have failed with an entity resolution exception");
        } catch (Exception e) {
            assertEntityResolutionDisabled(e);
        }
    }

    @Test
    public void testTransactionProtection() throws Exception {
        WFSConfig config = WFSTestData.getGmlCompatibleConfig();
        ((WFSTestData.MutableWFSConfig) config).setEntityResolver(PreventLocalEntityResolver.INSTANCE);

        String baseDirectory = "GeoServer_2.0/1.1.0_XXE_Transaction/";
        IntegrationTestWFSClient client = new IntegrationTestWFSClient(baseDirectory, config) {
            @Override
            protected Response mockTransactionSuccess(TransactionRequest request) throws ServiceException, IOException {
                String resource = "Transaction.xml";
                URL contentUrl = new URL(baseDirectory, resource);

                HTTPResponse httpResp = new TestHttpResponse("text/xml", "UTF-8", contentUrl);
                return request.createResponse(httpResp);
            }
        };
        WFSDataStore store = new WFSDataStore(client);
        SimpleFeatureStore fs = (SimpleFeatureStore) store.getFeatureSource(store.getTypeNames()[0]);
        try {
            fs.removeFeatures(Filter.INCLUDE);
        } catch (Exception e) {
            assertEntityResolutionDisabled(e);
        }
    }
}
