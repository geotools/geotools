/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2015, Open Source Geospatial Foundation (OSGeo)
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

import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Properties;

import javax.sql.DataSource;

import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureReader;
import org.geotools.factory.FactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.NameImpl;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.test.OnlineTestCase;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Geometry;


/**
 * Test support class for jdbc test cases.
 * <p>
 * This test class fires up a live instance of a jdbc database to provide a
 * live database to work with.
 * </p>
 *
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 *
 *
 *
 * @source $URL$
 */
public abstract class JDBCTestSupport extends OnlineTestCase {
    static {
        // uncomment to turn up logging

//        java.util.logging.ConsoleHandler handler = new java.util.logging.ConsoleHandler();
//        handler.setLevel(java.util.logging.Level.FINE);

//        org.geotools.util.logging.Logging.getLogger("org.geotools.data.jdbc").setLevel(java.util.logging.Level.FINE);
//        org.geotools.util.logging.Logging.getLogger("org.geotools.data.jdbc").addHandler(handler);

//        org.geotools.util.logging.Logging.getLogger("org.geotools.jdbc").setLevel(java.util.logging.Level.FINE);
//        org.geotools.util.logging.Logging.getLogger("org.geotools.jdbc").addHandler(handler);
    }

    protected JDBCTestSetup setup;
    protected JDBCDataStore dataStore;
    protected SQLDialect dialect;

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
        setup.setFixture(fixture);

        try {
            DataSource dataSource = setup.getDataSource();
            Connection cx = dataSource.getConnection();
            cx.close();
            return true;
        }
        catch (Throwable t) {
            throw new RuntimeException(t);
        }
        finally {
            try {
                setup.tearDown();
            }
            catch(Exception e) {
                System.out.println("Error occurred tearing down the test setup");
            }
        }
    }

    @Override
    protected void connect() throws Exception {
        //create the test harness
        if (setup == null) {
            setup = createTestSetup();
        }

        setup.setFixture(fixture);
        setup.setUp();

        //initialize the database
        setup.initializeDatabase();

        //initialize the data
        setup.setUpData();

        //create the dataStore
        //TODO: replace this with call to datastore factory
        HashMap params = createDataStoreFactoryParams();
        params.putAll(fixture);
        //JDBCDataStoreFactory factory = setup.createDataStoreFactory();
        dataStore = (JDBCDataStore) DataStoreFinder.getDataStore(params);
        //dataStore = factory.createDataStore( params );
        if(dataStore == null) {
            JDBCDataStoreFactory factory = setup.createDataStoreFactory();
            dataStore = factory.createDataStore( params );
        }
        setup.setUpDataStore(dataStore);
        dialect = dataStore.getSQLDialect();
    }

    protected abstract JDBCTestSetup createTestSetup();

    protected HashMap createDataStoreFactoryParams() throws Exception{
        HashMap params = new HashMap();
        params.put( JDBCDataStoreFactory.NAMESPACE.key, "http://www.geotools.org/test" );
        params.put( JDBCDataStoreFactory.SCHEMA.key, "geotools" );
        params.put( JDBCDataStoreFactory.DATASOURCE.key, setup.getDataSource() );
        return params;
    }

    @Override
    protected void disconnect() throws Exception {
        setup.tearDown();
        dataStore.dispose();
    }

    /**
     * Returns the table name as the datastore understands it (some datastore are incapable of supporting
     * mixed case names for example)
     */
    protected String tname( String raw ) {
        return setup.typeName( raw );
    }

    /**
     * Returns the attribute name as the datastore understands it (some datastore are incapable of supporting
     * mixed case names for example)
     */
    protected String aname( String raw ) {
        return setup.attributeName( raw );
    }

    /**
     * Returns the attribute name as the datastore understands it (some datastore are incapable of supporting
     * mixed case names for example)
     */
    protected Name aname( Name raw ) {
        return new NameImpl( raw.getNamespaceURI(), aname( raw.getLocalPart() ) );
    }

    /**
     * Checkes the two feature types are equal, taking into consideration the eventual modification
     * the datastore had to perform in order to actually manage the type (change in names case, for example)
     */
    protected void assertFeatureTypesEqual(SimpleFeatureType expected, SimpleFeatureType actual) {
        for (int i = 0; i < expected.getAttributeCount(); i++) {
            AttributeDescriptor expectedAttribute = expected.getDescriptor(i);
            AttributeDescriptor actualAttribute = actual.getDescriptor(i);

            assertAttributesEqual(expectedAttribute,actualAttribute);
        }

        // make sure the geometry is nillable and has minOccurrs to 0
        if(expected.getGeometryDescriptor() != null) {
            AttributeDescriptor dg = actual.getGeometryDescriptor();
            assertTrue(dg.isNillable());
            assertEquals(0, dg.getMinOccurs());
        }
    }

    /**
     * Checkes the two feature types are equal, taking into consideration the eventual modification
     * the datastore had to perform in order to actually manage the type (change in names case, for example)
     */
    protected void assertAttributesEqual(AttributeDescriptor expected, AttributeDescriptor actual) {
        assertEquals(aname(expected.getName()), actual.getName());
        assertEquals(expected.getMinOccurs(), actual.getMinOccurs());
        assertEquals(expected.getMaxOccurs(), actual.getMaxOccurs());
        assertEquals(expected.isNillable(), actual.isNillable());
        assertEquals(expected.getDefaultValue(), actual.getDefaultValue());

        AttributeType texpected = expected.getType();
        AttributeType tactual = actual.getType();

        if ( Number.class.isAssignableFrom( texpected.getBinding() ) ) {
            assertTrue( Number.class.isAssignableFrom( tactual.getBinding() ) );
        }
        else if ( Geometry.class.isAssignableFrom( texpected.getBinding())) {
            assertTrue( Geometry.class.isAssignableFrom( tactual.getBinding()));
        }
        else {
            assertTrue(texpected.getBinding().isAssignableFrom(tactual.getBinding()));
        }
    }

    protected void assertAttributeValuesEqual(Object expected, Object actual) {
        if (expected == null) {
            assertNull(actual);
            return;
        }

        if (expected instanceof Geometry) {
            assertTrue(((Geometry)expected).equals((Geometry)actual));
            return;
        }

        assertEquals(expected, actual);
    }

    protected boolean areCRSEqual(CoordinateReferenceSystem crs1, CoordinateReferenceSystem crs2) {

        if (crs1==null && crs2==null) {
            return true;
        }

        if (crs1==null ) {
            return false;
        }

        return crs1.equals(crs2);
    }

    protected boolean areReferencedEnvelopesEuqal(ReferencedEnvelope e1, ReferencedEnvelope e2) {

        if (e1==null && e2 ==null) {
            return true;
        }
        if (e1==null || e2 == null) {
            return false;
        }

        boolean equal =
                Math.round(e1.getMinX())==Math.round(e2.getMinX()) &&
                Math.round(e1.getMinY())==Math.round(e2.getMinY()) &&
                Math.round(e1.getMaxX())==Math.round(e2.getMaxX()) &&
                Math.round(e1.getMaxY())==Math.round(e2.getMaxY());

        if (!equal) {
            return false;
        }
        return areCRSEqual(e1.getCoordinateReferenceSystem(), e2.getCoordinateReferenceSystem());
    }

    public static interface FeatureAssertion<F extends Feature> {
        int toIndex(F feature);
        void check(int index, F feature);
    }
    public static interface SimpleFeatureAssertion extends FeatureAssertion<SimpleFeature>{}

    protected <FT extends FeatureType,F extends Feature>
    void assertFeatureCollection(int startIndex,
            int numberExpected,
            FeatureCollection<FT,F> collection,
            FeatureAssertion assertion) {
        assertFeatureIterator(startIndex,numberExpected,collection.features(),assertion);
    }
    protected <F extends Feature>
    void assertFeatureIterator(int startIndex,
            int numberExpected,
            FeatureIterator<F> iter,
            FeatureAssertion assertion) {
        try {
            boolean[] loadedFeatures = new boolean[numberExpected];
            for (int j = startIndex; j < numberExpected+startIndex; j++) {
                F feature = iter.next();

                assertNotNull(feature);

                int i = assertion.toIndex(feature);

                assertTrue(loadedFeatures.length > i-startIndex);
                assertTrue(i > startIndex-1);
                assertFalse(loadedFeatures[i-startIndex]);

                loadedFeatures[i-startIndex] = true;

                assertion.check(i, feature);

            }
            assertFalse(iter.hasNext());
            for (int i = 0; i < numberExpected; i++) {
                assertTrue("feature "+i+" is missing",loadedFeatures[i]);
            }
        } finally {
            iter.close();
        }

    }
    protected <F extends Feature>
    void assertFeatureIterator(int startIndex,
            int numberExpected,
            final Iterator<F> iterator,
            FeatureAssertion assertion) {
        FeatureIterator<F> adapter = new FeatureIterator<F>() {
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
        };
        assertFeatureIterator(startIndex,numberExpected,adapter,assertion);
    }
    protected <FT extends FeatureType,F extends Feature>
    void assertFeatureReader(int startIndex,
            int numberExpected,
            final FeatureReader<FT,F> reader,
            FeatureAssertion assertion) throws IOException {
        FeatureIterator<F> iter = new FeatureIterator<F>(){

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
        };

        assertFeatureIterator(startIndex,numberExpected,iter,assertion);
    }
}
