/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2016, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005, David Zwiers
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
package org.geotools.data.wfs.online;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.ResourceInfo;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.wfs.WFSDataStore;
import org.geotools.data.wfs.WFSDataStoreFactory;
import org.geotools.data.wfs.WFSTestData;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.CRS.AxisOrder;
import org.geotools.util.logging.Logging;
import org.junit.Before;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.FilterVisitor;
import org.opengis.filter.Id;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.spatial.BBOX;
import org.opengis.geometry.BoundingBox;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/** Online tests only run if {@link #SERVER_URL} can be reached. */
public abstract class AbstractWfsDataStoreOnlineTest {

    private static final Logger LOGGER = Logging.getLogger(AbstractWfsDataStoreOnlineTest.class);

    protected static final FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);

    private final String SERVER_URL;

    /** Check for availability only once: true, false, or null for unknown. */
    protected static Boolean serviceAvailable = null;

    /** The DataStore under test, static so we create it only once */
    protected WFSDataStore wfs = null;

    protected final WFSTestData.TestDataType testType;

    protected final String defaultGeometryName;

    protected String axisOrder;

    private final int featureCount;

    /**
     * The filter used for the count test, may be null if {@link #testFeatureSourceGetCountFilter()}
     * and {@link #testFeatureSourceGetFeatures()} are not expected to be run
     */
    private final Id fidFilter;

    /**
     * The filter used for the filter test, may be null if {@link
     * #testFeatureSourceGetFeaturesFilter()} is not expected to run.
     */
    private final Filter spatialFilter;

    private final Class geometryType;

    public AbstractWfsDataStoreOnlineTest(
            String serverURL,
            WFSTestData.TestDataType testType,
            String defaultGeometryName,
            Class geometryType,
            int featureCount,
            Id fidFilter,
            Filter spatialFilter,
            String axisOrder) {
        this.SERVER_URL = serverURL;
        this.testType = testType;
        this.defaultGeometryName = defaultGeometryName;
        this.featureCount = featureCount;
        this.fidFilter = fidFilter;
        this.geometryType = geometryType;
        this.axisOrder = axisOrder;
        this.spatialFilter = spatialFilter;
    }

    @Before
    public void setUp() throws IOException {
        if (serviceAvailable == null) {
            // check for service availability only once
            URL url = new URL(SERVER_URL);
            serviceAvailable = Boolean.FALSE;

            try (InputStream stream = url.openStream()) {
                serviceAvailable = Boolean.TRUE;
            } catch (Throwable t) {
                LOGGER.log(
                        Level.WARNING,
                        "The test server is not available: "
                                + SERVER_URL
                                + ". "
                                + getClass().getSimpleName()
                                + " test disabled ");
                url = null;
                return;
            }
        }

        if (wfs == null && serviceAvailable.booleanValue()) {
            LOGGER.info("Creating test datastore for " + SERVER_URL);

            Map<String, Serializable> params = new HashMap<>();
            params.put(WFSDataStoreFactory.URL.key, SERVER_URL);
            setUpParameters(params);

            WFSDataStoreFactory dataStoreFactory = new WFSDataStoreFactory();
            wfs = dataStoreFactory.createDataStore(params);
            LOGGER.fine("WFS datastore created");
        }
    }

    protected void setUpParameters(Map<String, Serializable> params) {
        params.put(WFSDataStoreFactory.GML_COMPATIBLE_TYPENAMES.key, true);
        params.put(WFSDataStoreFactory.AXIS_ORDER.key, axisOrder);
    }

    @Test
    public void testFeatureSourceInfo()
            throws IOException, NoSuchAuthorityCodeException, FactoryException {
        if (Boolean.FALSE.equals(serviceAvailable)) {
            return;
        }

        SimpleFeatureSource featureSource = wfs.getFeatureSource(testType.FEATURETYPENAME);

        assertNotNull(featureSource);

        ResourceInfo info = featureSource.getInfo();
        assertNotNull(info.getCRS());

        ReferencedEnvelope bounds = info.getBounds();
        assertSame(info.getCRS(), bounds.getCoordinateReferenceSystem());

        assertEquals(testType.FEATURETYPENAME, info.getName());
    }

    @Test
    public void testFeatureSourceGetBounds()
            throws IOException, NoSuchAuthorityCodeException, FactoryException {
        if (Boolean.FALSE.equals(serviceAvailable)) {
            return;
        }

        SimpleFeatureSource featureSource = wfs.getFeatureSource(testType.FEATURETYPENAME);
        assertNotNull(featureSource);

        ReferencedEnvelope infoBounds = featureSource.getInfo().getBounds();
        ReferencedEnvelope bounds = featureSource.getBounds();

        assertEquals(infoBounds, bounds);

        Query query = new Query(featureSource.getInfo().getName());
        CoordinateReferenceSystem queryCrs = CRS.decode("EPSG:23030");
        query.setCoordinateSystem(queryCrs);

        bounds = featureSource.getBounds(query);
        assertNotNull(bounds);
        assertSame(
                "the bounds were not reprojected", queryCrs, bounds.getCoordinateReferenceSystem());

        final String geometryName =
                featureSource.getSchema().getGeometryDescriptor().getLocalName();
        query.setFilter(ff.bbox(geometryName, -180, -90, 180, 90, "EPSG:4326"));

        bounds = featureSource.getBounds(query);
        assertNull(bounds); // too expensive to calculate
    }

    @Test
    public void testFeatureSourceGetCountAll() throws IOException {
        if (Boolean.FALSE.equals(serviceAvailable)) {
            return;
        }

        SimpleFeatureSource featureSource = wfs.getFeatureSource(testType.FEATURETYPENAME);
        assertNotNull(featureSource);

        assertEquals(featureCount, featureSource.getCount(Query.ALL));
    }

    /**
     * Performs a FeatureSource.getCount(Query) with the constructor provided fid filter if the
     * filter is not null.
     */
    @Test
    public void testFeatureSourceGetCountFilter() throws IOException {
        if (Boolean.FALSE.equals(serviceAvailable)) {
            return;
        }

        if (featureCount >= 0) { // server doesn't support feature count anyway, skip
            if (fidFilter == null) {
                LOGGER.info(
                        "Ignoring testFeatureSourceGetCountFilter "
                                + "since the subclass didn't provide a fid filter");
                return;
            }

            SimpleFeatureSource featureSource = wfs.getFeatureSource(testType.FEATURETYPENAME);
            assertNotNull(featureSource);

            Query query = new Query(featureSource.getInfo().getName());
            query.setFilter(fidFilter);

            assertEquals(1, featureSource.getCount(query));
        }
    }

    @Test
    public void testFeatureSourceGetFeatures() throws IOException {
        if (Boolean.FALSE.equals(serviceAvailable)) {
            return;
        }
        if (fidFilter == null) {
            LOGGER.info(
                    "Ignoring testFeatureSourceGetCountFilter "
                            + "since the subclass didn't provide a fid filter");
            return;
        }

        SimpleFeatureSource featureSource = wfs.getFeatureSource(testType.FEATURETYPENAME);
        assertNotNull(featureSource);

        Query query = new Query(testType.FEATURETYPENAME);
        query.setFilter(fidFilter);

        SimpleFeatureCollection features = featureSource.getFeatures(query);
        assertNotNull(features);

        SimpleFeatureType schema = features.getSchema();
        assertNotNull(schema);

        try (SimpleFeatureIterator iterator = features.features()) {
            assertNotNull(iterator);

            assertTrue("Didn't get anything with fidFilter", iterator.hasNext());
            SimpleFeature next = iterator.next();
            assertNotNull(next);
            assertNotNull(next.getDefaultGeometry());
        }
    }

    @Test
    public void testFeatureSourceGetFeaturesFilter() throws IOException {
        if (Boolean.FALSE.equals(serviceAvailable)) {
            return;
        }

        if (spatialFilter == null) {
            LOGGER.info(
                    "Ignoring testFeatureSourceGetCountFilter "
                            + "since the subclass didn't provide a spatial filter");
            return;
        }

        SimpleFeatureSource featureSource = wfs.getFeatureSource(testType.FEATURETYPENAME);
        assertNotNull(featureSource);

        Query query = new Query(testType.FEATURETYPENAME);
        query.setFilter(spatialFilter);

        SimpleFeatureCollection features = featureSource.getFeatures(query);
        assertNotNull(features);

        SimpleFeatureType schema = features.getSchema();
        assertNotNull(schema);

        try (SimpleFeatureIterator iterator = features.features()) {
            assertTrue(iterator.hasNext());
            SimpleFeature next = iterator.next();
            assertNotNull(next);
            assertNotNull(next.getDefaultGeometry());
        }
    }

    @Test
    public void testTypes() throws IOException, NoSuchElementException {
        if (Boolean.FALSE.equals(serviceAvailable)) {
            return;
        }

        assertTrue(wfs instanceof WFSDataStore);

        String[] types = wfs.getTypeNames();
        List<String> typeNames = Arrays.asList(types);
        assertTrue(typeNames.contains(testType.FEATURETYPENAME));

        for (String typeName : types) {
            SimpleFeatureType type = wfs.getSchema(typeName);
            type.getTypeName();
            type.getName().getNamespaceURI();

            SimpleFeatureSource source = wfs.getFeatureSource(typeName);
            source.getBounds();

            SimpleFeatureCollection features = source.getFeatures();
            features.getBounds();
            features.getSchema();

            Query query = new Query(typeName, Filter.INCLUDE, 20, Query.ALL_NAMES, "work already");
            features = source.getFeatures(query);
            features.size();

            try (SimpleFeatureIterator iterator = features.features()) {
                while (iterator.hasNext()) {
                    iterator.next();
                }
            }
        }

        SimpleFeatureType schema = wfs.getSchema(testType.FEATURETYPENAME);
        assertNotNull(schema);
        GeometryDescriptor geometryDescriptor = schema.getGeometryDescriptor();
        assertNotNull(geometryDescriptor);
        assertEquals(defaultGeometryName, geometryDescriptor.getLocalName());
        assertEquals(geometryType, geometryDescriptor.getType().getBinding());
        CoordinateReferenceSystem crs = geometryDescriptor.getCoordinateReferenceSystem();
        assertNotNull(crs);
    }

    @Test
    public void testSingleType() throws IOException, NoSuchElementException {
        if (Boolean.FALSE.equals(serviceAvailable)) {
            return;
        }
        SimpleFeatureType type = wfs.getSchema(testType.FEATURETYPENAME);
        type.getTypeName();
        type.getName().getNamespaceURI();

        SimpleFeatureSource source = wfs.getFeatureSource(testType.FEATURETYPENAME);
        source.getBounds();

        SimpleFeatureCollection features = source.getFeatures();
        features.getBounds();
        features.getSchema();

        Query query =
                new Query(
                        testType.FEATURETYPENAME,
                        Filter.INCLUDE,
                        20,
                        Query.ALL_NAMES,
                        "work already");
        features = source.getFeatures(query);
        features.size();

        try (SimpleFeatureIterator iterator = features.features()) {
            while (iterator.hasNext()) {
                iterator.next();
            }
        }
    }

    /** {@link BBOX} support? */
    @Test
    public void testDataStoreSupportsPlainBBOXInterface() throws Exception {
        if (Boolean.FALSE.equals(serviceAvailable)) {
            return;
        }
        final SimpleFeatureType ft = wfs.getSchema(testType.FEATURETYPENAME);
        SimpleFeatureSource featureSource = wfs.getFeatureSource(testType.FEATURETYPENAME);
        final ReferencedEnvelope bounds = featureSource.getBounds();
        String srsName = CRS.toSRS(bounds.getCoordinateReferenceSystem());

        final BBOX bbox =
                AxisOrder.EAST_NORTH == CRS.getAxisOrder(bounds.getCoordinateReferenceSystem())
                                || WFSDataStoreFactory.AXIS_ORDER_COMPLIANT.equals(axisOrder)
                        ? ff.bbox(
                                defaultGeometryName,
                                bounds.getMinX(),
                                bounds.getMinY(),
                                bounds.getMaxX(),
                                bounds.getMaxY(),
                                srsName)
                        : ff.bbox(
                                defaultGeometryName,
                                bounds.getMinY(),
                                bounds.getMinX(),
                                bounds.getMaxY(),
                                bounds.getMaxX(),
                                srsName);

        /** This one does not implement the deprecated geotools filter interfaces */
        final BBOX strictBBox =
                new BBOX() {

                    @Override
                    public boolean evaluate(Object object) {
                        return bbox.evaluate(object);
                    }

                    @Override
                    public Object accept(FilterVisitor visitor, Object extraData) {
                        return bbox.accept(visitor, extraData);
                    }

                    @Override
                    public Expression getExpression2() {
                        return bbox.getExpression2();
                    }

                    @Override
                    public Expression getExpression1() {
                        return bbox.getExpression1();
                    }

                    @Override
                    public MatchAction getMatchAction() {
                        return MatchAction.ANY;
                    }

                    @Override
                    public BoundingBox getBounds() {
                        return bbox.getBounds();
                    }
                };

        final Query query = new Query(ft.getTypeName());
        query.setPropertyNames(defaultGeometryName);
        query.setFilter(strictBBox);
        query.setHandle("testDataStoreSupportsPlainBBOXInterface");

        try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                wfs.getFeatureReader(query, Transaction.AUTO_COMMIT)) {
            assertNotNull(reader);
            assertTrue(reader.hasNext());
        }

        /*
         *
         * GEOT-6905
         *
        try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                wfs.getFeatureReader(query, Transaction.AUTO_COMMIT)) {
            assertNotNull(reader);
            assertTrue("Issued same query second time returns empty.", reader.hasNext());
        }
        */
    }

    @Test
    public void testDataStoreHandlesAxisFlipping() throws Exception {
        if (Boolean.FALSE.equals(serviceAvailable)) {
            return;
        }

        final SimpleFeatureType ft = wfs.getSchema(testType.FEATURETYPENAME);
        final SimpleFeatureSource featureSource = wfs.getFeatureSource(testType.FEATURETYPENAME);
        final ReferencedEnvelope bounds = featureSource.getBounds();

        CoordinateReferenceSystem wgs84LonLat = CRS.decode("EPSG:4326", true);
        CoordinateReferenceSystem wgs84LatLon = CRS.decode("EPSG:4326", false);

        assertEquals(AxisOrder.EAST_NORTH, CRS.getAxisOrder(wgs84LonLat));
        assertEquals(AxisOrder.NORTH_EAST, CRS.getAxisOrder(wgs84LatLon));

        ReferencedEnvelope lonLat = bounds.transform(wgs84LonLat, true);
        ReferencedEnvelope latLon = bounds.transform(wgs84LatLon, true);

        final BBOX lonLatFilter =
                ff.bbox(
                        defaultGeometryName,
                        lonLat.getMinimum(0),
                        lonLat.getMinimum(1),
                        lonLat.getMaximum(0),
                        lonLat.getMaximum(1),
                        null);

        final BBOX latLonFiler =
                ff.bbox(
                        defaultGeometryName,
                        latLon.getMinimum(0),
                        latLon.getMinimum(1),
                        latLon.getMaximum(0),
                        latLon.getMaximum(1),
                        null);

        final Query query = new Query(ft.getTypeName());
        query.setPropertyNames(defaultGeometryName);
        query.setFilter(lonLatFilter);
        query.setCoordinateSystem(wgs84LonLat);

        final int expectedCount = wfs.getFeatureSource(query.getTypeName()).getFeatures().size();

        try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                wfs.getFeatureReader(query, Transaction.AUTO_COMMIT)) {
            assertTrue(reader.hasNext());
            int count = 0;
            while (reader.hasNext()) {
                reader.next();
                count++;
            }
            assertEquals(expectedCount, count);
        }

        query.setFilter(latLonFiler);
        query.setCoordinateSystem(wgs84LatLon);

        try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                wfs.getFeatureReader(query, Transaction.AUTO_COMMIT)) {
            assertTrue(reader.hasNext());
            int count = 0;
            while (reader.hasNext()) {
                reader.next();
                count++;
            }
            assertEquals(expectedCount, count);
        }
    }

    public void XtestFeatureType() throws Exception {
        if (Boolean.FALSE.equals(serviceAvailable)) {
            return;
        }
        WFSOnlineTestSupport.doFeatureType(wfs, testType.FEATURETYPENAME);
    }

    @Test
    public void testFeatureReader() throws Exception {
        if (Boolean.FALSE.equals(serviceAvailable)) {
            return;
        }
        WFSOnlineTestSupport.doFeatureReader(wfs, testType.FEATURETYPENAME);
    }

    @Test
    public void testFeatureReaderWithQuery() throws Exception {
        if (Boolean.FALSE.equals(serviceAvailable)) {
            return;
        }
        WFSOnlineTestSupport.doFeatureReaderWithQuery(wfs, testType.FEATURETYPENAME);
    }

    @Test
    public void testFeatureReaderWithFilterBBox() throws Exception {
        if (Boolean.FALSE.equals(serviceAvailable)) {
            return;
        }
        ReferencedEnvelope bbox = wfs.getFeatureSource(testType.FEATURETYPENAME).getBounds();
        WFSOnlineTestSupport.doFeatureReaderWithBBox(
                wfs, testType.FEATURETYPENAME, defaultGeometryName, bbox);
    }
}
