/*
 * GeoTools - The Open Source Java GIS Toolkit http://geotools.org
 *
 * (C) 2021, Open Source Geospatial Foundation (OSGeo)
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

import java.net.URL;
import org.geotools.TestData;
import org.geotools.data.Query;
import org.geotools.data.wfs.TestHttpClient;
import org.geotools.data.wfs.TestWFSClient;
import org.geotools.data.wfs.internal.WFSClient;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.NameImpl;
import org.geotools.http.MockHttpResponse;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;

/**
 * WFS returning complex feature source
 *
 * <p>The response from calling GetConfiguration is mocked, but some schemas defined within that
 * response are downloaded to a temporary cache. Without this set we will get a Failed to resolve...
 * exception. Because of the schemas, we must also define preferred http method as GET.
 *
 * @author Roar Brænden
 */
public class WFSContentComplexFeatureSourceTest {

    private static final String STED = "Sted";

    private static final Name STED_NAME =
            new NameImpl(
                    "http://skjema.geonorge.no/SOSI/produktspesifikasjon/StedsnavnForVanligBruk/20181115",
                    STED);

    @Test
    public void testGetFeaturesWithoutArgument() throws Exception {
        final WFSClient client = createWFSClient();
        final WFSContentDataAccess dataAccess = createDataAccess(client);

        WFSContentComplexFeatureSource featureSource =
                new WFSContentComplexFeatureSource(STED_NAME, client, dataAccess);

        FeatureCollection<FeatureType, Feature> features = featureSource.getFeatures();
        Assert.assertNotNull(features);
    }

    @Test
    @Ignore
    public void testGetFeaturesWithNameQuery() throws Exception {
        final WFSClient client = createWFSClient();
        final WFSContentDataAccess dataAccess = createDataAccess(client);

        WFSContentComplexFeatureSource featureSource =
                new WFSContentComplexFeatureSource(STED_NAME, client, dataAccess);

        Query query = new Query(STED);

        FeatureCollection<FeatureType, Feature> collection = featureSource.getFeatures(query);
        Assert.assertNotNull(collection);

        try (FeatureIterator<Feature> features = collection.features()) {
            Assert.assertNotNull(features);
        }
    }

    private TestWFSClient createWFSClient() throws Exception {
        final URL capabilities = new URL("https://wfs.geonorge.no/skwms1/wfs.stedsnavn");

        TestHttpClient mockHttp = new TestHttpClient();
        mockHttp.expectGet(
                new URL(
                        "https://wfs.geonorge.no/skwms1/wfs.stedsnavn?REQUEST=GetCapabilities&SERVICE=WFS"),
                new MockHttpResponse(
                        TestData.file(TestHttpClient.class, "KartverketNo/GetCapabilities.xml"),
                        "text/xml"));

        TestWFSClient client = new TestWFSClient(capabilities, mockHttp);
        client.setProtocol(false); // Http GET method

        return client;
    }

    private WFSContentDataAccess createDataAccess(WFSClient client) throws Exception {
        final WFSContentDataAccess dataAccess = new WFSContentDataAccess(client);
        dataAccess.setCacheLocation(TestData.file(TestHttpClient.class, "KartverketNo/cache"));
        return dataAccess;
    }
}
