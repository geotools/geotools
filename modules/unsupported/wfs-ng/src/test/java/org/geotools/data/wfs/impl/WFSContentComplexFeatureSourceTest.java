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

import java.io.ByteArrayOutputStream;
import java.net.URL;
import javax.xml.namespace.QName;
import org.geotools.TestData;
import org.geotools.data.Query;
import org.geotools.data.wfs.TestHttpClient;
import org.geotools.data.wfs.TestWFSClient;
import org.geotools.data.wfs.internal.GetFeatureRequest;
import org.geotools.data.wfs.internal.WFSClient;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.NameImpl;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.http.MockHttpResponse;
import org.geotools.referencing.CRS;
import org.junit.Assert;
import org.junit.Test;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.xml.sax.helpers.NamespaceSupport;

/**
 * WFS returning complex feature source
 *
 * <p>The responses from calling GetConfiguration and GetFeatures is mocked, but some schemas
 * defined within that response are downloaded using ordinary download. Therefore we're using a
 * cached version of those schemas.
 *
 * @author Roar Brænden
 */
public class WFSContentComplexFeatureSourceTest {

    private static final String NS =
            "http://skjema.geonorge.no/SOSI/produktspesifikasjon/StedsnavnForVanligBruk/20181115";

    private static final String PREFIX = "app";

    private static final String STED = "Sted";

    private static final Name STED_NAME = new NameImpl(NS, STED);

    private static final QName REMOTE_STED_NAME = new QName(NS, STED, PREFIX);

    private static final String GEOM_FIELD_NAME = "posisjon";

    private static FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();

    private static final String DEFAULT_SRS = "urn:ogc:def:crs:EPSG::4258";

    private static final Filter FILTER =
            ff.bbox(GEOM_FIELD_NAME, 58.71, 58.73, 7.39, 7.41, "EPSG:4258");

    @Test
    public void testGetFeaturesWithoutArgument() throws Exception {
        final WFSClient client = createWFSClient(true);
        final WFSContentDataAccess dataAccess = createDataAccess(client);

        WFSContentComplexFeatureSource featureSource =
                new WFSContentComplexFeatureSource(STED_NAME, client, dataAccess);

        FeatureCollection<FeatureType, Feature> features = featureSource.getFeatures();
        Assert.assertNotNull(features);
    }

    @Test
    public void testGetFeaturesWithFilter() throws Exception {
        final WFSClient client = createWFSClient(false);
        final WFSContentDataAccess dataAccess = createDataAccess(client);

        WFSContentComplexFeatureSource featureSource =
                new WFSContentComplexFeatureSource(STED_NAME, client, dataAccess);

        FeatureCollection<FeatureType, Feature> collection = featureSource.getFeatures(FILTER);
        Assert.assertNotNull(collection);

        try (FeatureIterator<Feature> features = collection.features()) {
            Assert.assertTrue(features.hasNext());
            Assert.assertNotNull(features.next());
        }
    }

    /**
     * Expects a call for the full FILTER. The filter of subCollection is done while parsing.
     *
     * <p>Should handle two consequent calls on the same FeatureCollection.
     */
    @Test
    public void testGetSubcollectionWithFilter() throws Exception {
        final WFSClient client = createWFSClient(false);
        final WFSContentDataAccess dataAccess = createDataAccess(client);

        WFSContentComplexFeatureSource featureSource =
                new WFSContentComplexFeatureSource(STED_NAME, client, dataAccess);

        FeatureCollection<FeatureType, Feature> collection = featureSource.getFeatures(FILTER);
        NamespaceSupport ns = new NamespaceSupport();
        ns.declarePrefix(PREFIX, NS);

        Filter correctFilter = ff.equals(ff.property("app:stedsnummer", ns), ff.literal(1));
        try (FeatureIterator<Feature> features =
                collection.subCollection(correctFilter).features()) {
            Assert.assertTrue(features.hasNext());
        }

        Filter wrongFilter = ff.equal(ff.property("app:stedsnummer", ns), ff.literal(2));
        try (FeatureIterator<Feature> features = collection.subCollection(wrongFilter).features()) {
            Assert.assertFalse(features.hasNext());
        }

        try (FeatureIterator<Feature> features =
                collection.subCollection(Filter.INCLUDE).features()) {
            Assert.assertTrue(features.hasNext());
        }
    }

    @Test
    public void testGetFeaturesWithNameQuery() throws Exception {
        final WFSClient client = createWFSClient(true);
        final WFSContentDataAccess dataAccess = createDataAccess(client);

        WFSContentComplexFeatureSource featureSource =
                new WFSContentComplexFeatureSource(STED_NAME, client, dataAccess);

        Query query = new Query(STED);

        FeatureCollection<FeatureType, Feature> collection = featureSource.getFeatures(query);
        Assert.assertNotNull(collection);

        try (FeatureIterator<Feature> features = collection.features()) {
            Assert.assertTrue(features.hasNext());
            Assert.assertNotNull(features.next());
        }
    }

    @Test
    public void testGetBounds() throws Exception {
        final WFSClient client = createWFSClient(true);
        final WFSContentDataAccess dataAccess = createDataAccess(client);

        ReferencedEnvelope envelope = dataAccess.getFeatureSource(STED_NAME).getBounds();
        Assert.assertNotNull(envelope);
        ReferencedEnvelope actual =
                new ReferencedEnvelope(
                        58.111523, 70.671561, 6.034809, 30.528068, CRS.decode(DEFAULT_SRS));
        assertEnvelope(actual, envelope);

        CoordinateReferenceSystem utm33 = CRS.decode("EPSG:32633");
        Query reprojectQuery = new Query();
        reprojectQuery.setCoordinateSystemReproject(utm33);

        ReferencedEnvelope envelopeReprojected =
                dataAccess.getFeatureSource(STED_NAME).getBounds(reprojectQuery);
        ReferencedEnvelope actualReprojected = actual.transform(utm33, true);
        assertEnvelope(actualReprojected, envelopeReprojected);

        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        Query filteredQuery =
                new Query(STED, ff.bbox("posisjon", 60.0, 20.0, 61.0, 21.0, DEFAULT_SRS));
        ReferencedEnvelope envelopeFilter =
                dataAccess.getFeatureSource(STED_NAME).getBounds(filteredQuery);
        Assert.assertNull("We're assuming a null when using a filter.", envelopeFilter);
    }

    private void assertEnvelope(ReferencedEnvelope actual, ReferencedEnvelope test) {
        Assert.assertTrue(
                CRS.equalsIgnoreMetadata(
                        actual.getCoordinateReferenceSystem(),
                        test.getCoordinateReferenceSystem()));
        Assert.assertEquals(actual.getMinX(), test.getMinX(), 0.1);
        Assert.assertEquals(actual.getMaxX(), test.getMaxX(), 0.1);
        Assert.assertEquals(actual.getMinY(), test.getMinY(), 0.1);
        Assert.assertEquals(actual.getMaxY(), test.getMaxY(), 0.1);
    }

    private TestWFSClient createWFSClient(boolean usingGet) throws Exception {
        final URL capabilities = new URL("https://wfs.geonorge.no/skwms1/wfs.stedsnavn");

        TestHttpClient mockHttp = new TestHttpClient();
        mockHttp.expectGet(
                new URL(
                        "https://wfs.geonorge.no/skwms1/wfs.stedsnavn?REQUEST=GetCapabilities&SERVICE=WFS"),
                new MockHttpResponse(
                        TestData.file(TestHttpClient.class, "KartverketNo/GetCapabilities.xml"),
                        "text/xml"));

        TestWFSClient client = new TestWFSClient(capabilities, mockHttp);
        if (usingGet) {
            client.setProtocol(false); // Http GET method
        }

        mockHttp.expectGet(
                new URL(
                        "https://wfs.geonorge.no/skwms1/wfs.stedsnavn?REQUEST=GetFeature&RESULTTYPE=RESULTS&OUTPUTFORMAT=application%2Fgml%2Bxml%3B+version%3D3.2&VERSION=2.0.0&TYPENAMES=app%3ASted&SERVICE=WFS"),
                new MockHttpResponse(
                        TestData.file(TestHttpClient.class, "KartverketNo/GetFeature_sted.xml"),
                        "text/xml"));

        GetFeatureRequest request = client.createGetFeatureRequest();
        request.setTypeName(REMOTE_STED_NAME);
        request.setFilter(FILTER);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        request.performPostOutput(out);
        String postContent = out.toString(client.getConfig().getDefaultEncoding().name());

        mockHttp.expectPost(
                new URL("https://wfs.geonorge.no/skwms1/wfs.stedsnavn"),
                postContent,
                "text/xml",
                new MockHttpResponse(
                        TestData.file(TestHttpClient.class, "KartverketNo/GetFeature_sted.xml"),
                        "text/xml"));

        return client;
    }

    private WFSContentDataAccess createDataAccess(WFSClient client) throws Exception {
        final WFSContentDataAccess dataAccess = new WFSContentDataAccess(client);
        dataAccess.setCacheLocation(TestData.file(TestHttpClient.class, "KartverketNo/cache"));
        return dataAccess;
    }
}
