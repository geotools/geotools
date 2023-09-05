/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.jdbc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.geotools.api.data.DataStoreFinder;
import org.geotools.api.data.FeatureReader;
import org.geotools.api.feature.Feature;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.feature.type.AttributeType;
import org.geotools.api.feature.type.FeatureType;
import org.geotools.api.feature.type.Name;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.NameImpl;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.test.OnlineTestSupport;
import org.geotools.util.logging.Logging;
import org.locationtech.jts.geom.Geometry;

/**
 * Test support class for jdbc test cases.
 *
 * <p>This test class fires up a live instance of a jdbc database to provide a live database to work
 * with.
 *
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 */
@SuppressWarnings({"PMD.EmptyInitializer", "PMD.EmptyControlStatement"})
public abstract class JDBCTestSupport extends OnlineTestSupport {

    static final Logger LOGGER = Logging.getLogger(JDBCTestSupport.class);

    static {
        // uncomment to turn up logging

        //        java.util.logging.ConsoleHandler handler = new java.util.logging.ConsoleHandler();
        //        handler.setLevel(java.util.logging.Level.FINE);
        //
        //        org.geotools.util.logging.Logging.getLogger(JDBCTestSupport.class)
        //                .setLevel(java.util.logging.Level.FINE);
        //
        //
        // org.geotools.util.logging.Logging.getLogger(JDBCTestSupport.class).addHandler(handler);
        //
        //        org.geotools.util.logging.Logging.getLogger(JDBCTestSupport.class)
        //                .setLevel(java.util.logging.Level.FINE);
        //
        //
        // org.geotools.util.logging.Logging.getLogger(JDBCTestSupport.class).addHandler(handler);
    }

    protected JDBCTestSetup setup;
    protected JDBCDataStore dataStore;
    protected SQLDialect dialect;

    /** Allows implementations to request a longitude first axis ordering for CRSs. */
    protected boolean forceLongitudeFirst = false;

    @Override
    protected Properties createOfflineFixture() {
        return createTestSetup().createOfflineFixture();
    }

    @Override
    protected Properties createExampleFixture() {
        return createTestSetup().createExampleFixture();
    }

    @Override
    protected String getFixtureId() {
        return createTestSetup().createDataStoreFactory().getDatabaseID();
    }

    @Override
    protected boolean isOnline() throws Exception {
        JDBCTestSetup setup = createTestSetup();
        setup.setFixture(getFixture());

        DataSource dataSource = setup.getDataSource();
        try (Connection cx = dataSource.getConnection()) {
            return true;
        } finally {
            try {
                setup.tearDown();
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error occurred tearing down the test setup", e);
            }
        }
    }

    @Override
    protected void connect() throws Exception {
        // create the test harness
        if (setup == null) {
            setup = createTestSetup();
        }

        setup.setFixture(getFixture());
        setup.setUp();

        // initialize the database
        setup.initializeDatabase();

        // initialize the data
        setup.setUpData();

        // create the dataStore
        Map<String, Object> params = createDataStoreFactoryParams();
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> temp = (Map<String, Object>) ((HashMap) params).clone();
            getFixture().forEach((k, v) -> temp.put((String) k, v));
            dataStore = (JDBCDataStore) DataStoreFinder.getDataStore(temp);
        } catch (Exception e) {
            // ignore
        }
        if (dataStore == null) {
            JDBCDataStoreFactory factory = setup.createDataStoreFactory();
            dataStore = factory.createDataStore(params);
        }
        setup.setUpDataStore(dataStore);
        dialect = dataStore.getSQLDialect();
    }

    protected abstract JDBCTestSetup createTestSetup();

    protected Map<String, Object> createDataStoreFactoryParams() throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put(JDBCDataStoreFactory.NAMESPACE.key, "http://www.geotools.org/test");
        String testSchema = getFixture().getProperty(JDBCDataStoreFactory.SCHEMA.key, "geotools");
        params.put(JDBCDataStoreFactory.SCHEMA.key, testSchema);
        params.put(JDBCDataStoreFactory.DATASOURCE.key, setup.getDataSource());

        // Enable batch insert in the tests. Some tests will revert that back to the default because
        // their code is not closing the writer correctly and would fail. They are left as is to
        // check that production code behaving like that would be saved by the fact that the default
        // value is 1.
        params.put(JDBCDataStoreFactory.BATCH_INSERT_SIZE.key, 100);
        return params;
    }

    @Override
    protected void disconnect() throws Exception {
        setup.tearDown();
        dataStore.dispose();
    }

    /**
     * Returns the table name as the datastore understands it (some datastore are incapable of
     * supporting mixed case names for example)
     */
    protected String tname(String raw) {
        return setup.typeName(raw);
    }

    /**
     * Returns the attribute name as the datastore understands it (some datastore are incapable of
     * supporting mixed case names for example)
     */
    protected String aname(String raw) {
        return setup.attributeName(raw);
    }

    /**
     * Returns the attribute name as the datastore understands it (some datastore are incapable of
     * supporting mixed case names for example)
     */
    protected Name aname(Name raw) {
        return new NameImpl(raw.getNamespaceURI(), aname(raw.getLocalPart()));
    }

    /**
     * Checkes the two feature types are equal, taking into consideration the eventual modification
     * the datastore had to perform in order to actually manage the type (change in names case, for
     * example)
     */
    protected void assertFeatureTypesEqual(SimpleFeatureType expected, SimpleFeatureType actual) {
        for (int i = 0; i < expected.getAttributeCount(); i++) {
            AttributeDescriptor expectedAttribute = expected.getDescriptor(i);
            AttributeDescriptor actualAttribute = actual.getDescriptor(i);

            assertAttributesEqual(expectedAttribute, actualAttribute);
        }

        // make sure the geometry is nillable and has minOccurrs to 0
        if (expected.getGeometryDescriptor() != null) {
            AttributeDescriptor dg = actual.getGeometryDescriptor();
            assertTrue(dg.isNillable());
            assertEquals(0, dg.getMinOccurs());
        }
    }

    /**
     * Checkes the two feature types are equal, taking into consideration the eventual modification
     * the datastore had to perform in order to actually manage the type (change in names case, for
     * example)
     */
    protected void assertAttributesEqual(AttributeDescriptor expected, AttributeDescriptor actual) {
        assertEquals(aname(expected.getName()), actual.getName());
        assertEquals(expected.getMinOccurs(), actual.getMinOccurs());
        assertEquals(expected.getMaxOccurs(), actual.getMaxOccurs());
        assertEquals(expected.isNillable(), actual.isNillable());
        assertEquals(expected.getDefaultValue(), actual.getDefaultValue());

        AttributeType texpected = expected.getType();
        AttributeType tactual = actual.getType();

        if (Number.class.isAssignableFrom(texpected.getBinding())) {
            assertTrue(Number.class.isAssignableFrom(tactual.getBinding()));
        } else if (Geometry.class.isAssignableFrom(texpected.getBinding())) {
            assertTrue(Geometry.class.isAssignableFrom(tactual.getBinding()));
        } else {
            assertTrue(texpected.getBinding().isAssignableFrom(tactual.getBinding()));
        }
    }

    @SuppressWarnings("PMD.SimplifiableTestAssertion")
    protected void assertAttributeValuesEqual(Object expected, Object actual) {
        if (expected == null) {
            assertNull(actual);
            return;
        }

        if (expected instanceof Geometry) {
            assertTrue(((Geometry) expected).equals((Geometry) actual));
            return;
        }

        assertEquals(expected, actual);
    }

    /**
     * Returns the {@link CoordinateReferenceSystem} denoted by epsgCode with an axis order taking
     * into account the {@link #forceLongitudeFirst} setting.
     */
    protected CoordinateReferenceSystem decodeEPSG(int epsgCode) throws FactoryException {
        return CRS.decode(String.format("EPSG:%d", epsgCode), forceLongitudeFirst);
    }

    protected boolean areCRSEqual(CoordinateReferenceSystem crs1, CoordinateReferenceSystem crs2) {

        if (crs1 == null && crs2 == null) return true;

        if (crs1 == null) return false;

        return crs1.equals(crs2);
    }

    protected boolean areReferencedEnvelopesEqual(ReferencedEnvelope e1, ReferencedEnvelope e2) {

        if (e1 == null && e2 == null) return true;
        if (e1 == null || e2 == null) return false;

        boolean equal =
                Math.round(e1.getMinX()) == Math.round(e2.getMinX())
                        && Math.round(e1.getMinY()) == Math.round(e2.getMinY())
                        && Math.round(e1.getMaxX()) == Math.round(e2.getMaxX())
                        && Math.round(e1.getMaxY()) == Math.round(e2.getMaxY());

        if (!equal) return false;
        return areCRSEqual(e1.getCoordinateReferenceSystem(), e2.getCoordinateReferenceSystem());
    }

    public static interface FeatureAssertion<F extends Feature> {
        int toIndex(F feature);

        void check(int index, F feature);
    }

    public static interface SimpleFeatureAssertion extends FeatureAssertion<SimpleFeature> {}

    protected <FT extends FeatureType, F extends Feature> void assertFeatureCollection(
            int startIndex,
            int numberExpected,
            FeatureCollection<FT, F> collection,
            FeatureAssertion<F> assertion) {
        assertFeatureIterator(startIndex, numberExpected, collection.features(), assertion);
    }

    protected <F extends Feature> void assertFeatureIterator(
            int startIndex,
            int numberExpected,
            FeatureIterator<F> iter,
            FeatureAssertion<F> assertion) {
        try {
            boolean[] loadedFeatures = new boolean[numberExpected];
            for (int j = startIndex; j < numberExpected + startIndex; j++) {
                F feature = iter.next();

                assertNotNull(feature);

                int i = assertion.toIndex(feature);

                assertTrue(loadedFeatures.length > i - startIndex);
                assertTrue(i > startIndex - 1);
                assertFalse(loadedFeatures[i - startIndex]);

                loadedFeatures[i - startIndex] = true;

                assertion.check(i, feature);
            }
            assertFalse(iter.hasNext());
            for (int i = 0; i < numberExpected; i++) {
                assertTrue("feature " + i + " is missing", loadedFeatures[i]);
            }
        } finally {
            iter.close();
        }
    }

    protected <F extends Feature> void assertFeatureIterator(
            int startIndex,
            int numberExpected,
            final Iterator<F> iterator,
            FeatureAssertion<F> assertion) {
        try (FeatureIterator<F> adapter =
                new FeatureIterator<F>() {
                    @Override
                    public boolean hasNext() {
                        return iterator.hasNext();
                    }

                    @Override
                    public F next() {
                        return iterator.next();
                    }

                    @Override
                    public void close() {}
                }) {
            assertFeatureIterator(startIndex, numberExpected, adapter, assertion);
        }
    }

    protected <FT extends FeatureType, F extends Feature> void assertFeatureReader(
            int startIndex,
            int numberExpected,
            final FeatureReader<FT, F> reader,
            FeatureAssertion<F> assertion)
            throws IOException {
        try (FeatureIterator<F> iter =
                new FeatureIterator<F>() {

                    @Override
                    public boolean hasNext() {
                        try {
                            return reader.hasNext();
                        } catch (IOException e) {
                            throw new AssertionError(e);
                        }
                    }

                    @Override
                    public F next() throws NoSuchElementException {
                        try {
                            return reader.next();
                        } catch (IOException e) {
                            throw new AssertionError(e);
                        }
                    }

                    @Override
                    public void close() {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            throw new AssertionError(e);
                        }
                    }
                }) {
            assertFeatureIterator(startIndex, numberExpected, iter, assertion);
        }
    }
}
