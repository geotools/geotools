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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeTrue;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.NoSuchElementException;
import org.geotools.api.data.DataSourceException;
import org.geotools.api.data.FeatureLock;
import org.geotools.api.data.FeatureLockException;
import org.geotools.api.data.FeatureReader;
import org.geotools.api.data.FeatureWriter;
import org.geotools.api.data.Query;
import org.geotools.api.data.SimpleFeatureLocking;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.data.SimpleFeatureStore;
import org.geotools.api.data.Transaction;
import org.geotools.api.feature.IllegalAttributeException;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.feature.type.Name;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.PropertyIsEqualTo;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FilteringFeatureReader;
import org.geotools.data.InProcessLockingManager;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.NameImpl;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.filter.IllegalFilterException;
import org.geotools.filter.function.FilterFunction_geometryType;
import org.geotools.filter.function.math.FilterFunction_ceil;
import org.geotools.geometry.jts.LiteCoordinateSequence;
import org.geotools.geometry.jts.LiteCoordinateSequenceFactory;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.factory.Hints;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;

public abstract class JDBCDataStoreAPIOnlineTest extends JDBCTestSupport {
    private static final int LOCK_DURATION = 3600 * 1000; // one hour
    protected TestData td;

    @Override
    protected void connect() throws Exception {
        super.connect();

        if (td == null) {
            td = new TestData(((JDBCDataStoreAPITestSetup) setup).getInitialPrimaryKeyValue());
            td.forceLongitudeFirst = this.forceLongitudeFirst;

            td.ROAD = tname(td.ROAD);
            td.ROAD_ID = aname(td.ROAD_ID);
            td.ROAD_GEOM = aname(td.ROAD_GEOM);
            td.ROAD_NAME = aname(td.ROAD_NAME);

            td.RIVER = tname(td.RIVER);
            td.RIVER_ID = aname(td.RIVER_ID);
            td.RIVER_GEOM = aname(td.RIVER_GEOM);
            td.RIVER_FLOW = aname(td.RIVER_FLOW);
            td.RIVER_RIVER = aname(td.RIVER_RIVER);

            td.build();
        }

        if (setup.canResetSchema()) {
            dataStore.setDatabaseSchema(null);
        }
    }

    @Override
    protected abstract JDBCDataStoreAPITestSetup createTestSetup();

    @Test
    public void testGetFeatureTypes() {
        try {
            String[] names = dataStore.getTypeNames();
            assertTrue(contains(names, tname("road")));
            assertTrue(contains(names, tname("river")));
        } catch (IOException e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            fail("An IOException has been thrown!");
        }
    }

    @Test
    public void testGetSchemaRoad() throws IOException {
        SimpleFeatureType expected = td.roadType;
        SimpleFeatureType actual = dataStore.getSchema(tname("road"));
        assertEquals("name", aname(expected.getName()), actual.getName());

        // assertEquals( "compare", 0, DataUtilities.compare( expected, actual ));
        assertEquals("attributeCount", expected.getAttributeCount(), actual.getAttributeCount());

        for (int i = 0; i < expected.getAttributeCount(); i++) {
            AttributeDescriptor expectedAttribute = expected.getDescriptor(i);
            AttributeDescriptor actualAttribute = actual.getDescriptor(i);

            assertAttributesEqual(expectedAttribute, actualAttribute);
        }

        // make sure the geometry is nillable and has minOccurrs to 0
        AttributeDescriptor dg = actual.getGeometryDescriptor();
        assertTrue(dg.isNillable());
        assertEquals(0, dg.getMinOccurs());

        // assertEquals(expected, actual);
    }

    @Test
    public void testGetSchemaRiver() throws IOException {
        SimpleFeatureType expected = td.riverType;
        SimpleFeatureType actual = dataStore.getSchema(tname("river"));
        assertEquals("name", aname(expected.getName()), actual.getName());

        // assertEquals( "compare", 0, DataUtilities.compare( expected, actual ));
        assertEquals("attributeCount", expected.getAttributeCount(), actual.getAttributeCount());
        assertFeatureTypesEqual(expected, actual);

        // assertEquals(expected, actual);
    }

    @Test
    public void testCreateSchema() throws Exception {
        String featureTypeName = tname("building");

        // create a featureType and write it to PostGIS
        CoordinateReferenceSystem crs = decodeEPSG(4326);

        SimpleFeatureTypeBuilder ftb = new SimpleFeatureTypeBuilder();
        ftb.setName(featureTypeName);
        ftb.add(aname("id"), Integer.class);
        ftb.add(aname("name"), String.class);
        ftb.add(aname("the_geom"), Point.class, crs);

        SimpleFeatureType newFT = ftb.buildFeatureType();
        dataStore.createSchema(newFT);

        SimpleFeatureType newSchema = dataStore.getSchema(featureTypeName);
        assertNotNull(newSchema);
        assertEquals(3, newSchema.getAttributeCount());
    }

    @Test
    public void testGetFeatureReader() throws IOException, IllegalAttributeException {
        assertCovered(td.roadFeatures, reader(tname("road")));
        assertEquals(3, count(reader(tname("road"))));
    }

    @Test
    public void testGeometryFactoryHintsGF() throws IOException {
        SimpleFeatureSource fs = dataStore.getFeatureSource(tname("road"));
        assertTrue(fs.getSupportedHints().contains(Hints.JTS_GEOMETRY_FACTORY));

        Query q = new Query(tname("road"));
        GeometryFactory gf = new GeometryFactory(new LiteCoordinateSequenceFactory());
        Hints hints = new Hints(Hints.JTS_GEOMETRY_FACTORY, gf);
        q.setHints(hints);

        try (SimpleFeatureIterator it = fs.getFeatures(q).features()) {
            it.hasNext();

            SimpleFeature f = it.next();
            LineString ls = (LineString) f.getDefaultGeometry();
            assertTrue(ls.getCoordinateSequence() instanceof LiteCoordinateSequence);
        }
    }

    @Test
    public void testGeometryFactoryHintsCS() throws IOException {
        SimpleFeatureSource fs = dataStore.getFeatureSource(tname("road"));
        assertTrue(fs.getSupportedHints().contains(Hints.JTS_COORDINATE_SEQUENCE_FACTORY));

        Query q = new Query(tname("road"));
        Hints hints = new Hints(Hints.JTS_COORDINATE_SEQUENCE_FACTORY, new LiteCoordinateSequenceFactory());
        q.setHints(hints);

        try (SimpleFeatureIterator it = fs.getFeatures(q).features()) {
            it.hasNext();

            SimpleFeature f = it.next();

            LineString ls = (LineString) f.getDefaultGeometry();
            assertTrue(ls.getCoordinateSequence() instanceof LiteCoordinateSequence);
        }
    }

    @Test
    public void testGetFeatureReaderLake() throws IOException, IllegalFilterException {
        try (Transaction t = new DefaultTransaction();
                FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                        dataStore.getFeatureReader(new Query(tname("lake")), t)) {
            assertNotNull(reader);
            assertTrue(reader.hasNext());
            assertNotNull(reader.next());
            assertFalse(reader.hasNext());
        }
    }

    @Test
    public void testGetFeatureReaderFilterPrePost() throws IOException, IllegalFilterException {

        FilterFactory factory = CommonFactoryFinder.getFilterFactory(null);
        FilterFunction_geometryType geomTypeExpr = new FilterFunction_geometryType();

        geomTypeExpr.setParameters(Collections.singletonList(factory.property(aname("geom"))));

        PropertyIsEqualTo filter = factory.equals(geomTypeExpr, factory.literal("Polygon"));
        try (Transaction t = new DefaultTransaction()) {
            try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                    dataStore.getFeatureReader(new Query(tname("road"), filter), t)) {
                assertNotNull(reader);
                assertFalse(reader.hasNext());
            }

            filter = factory.equals(geomTypeExpr, factory.literal("LineString"));
            try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                    dataStore.getFeatureReader(new Query(tname("road"), filter), t)) {
                assertTrue(reader.hasNext());
            }
        }
    }

    @Test
    public void testGetFeatureReaderFilterPrePostWithNoGeometry() throws IOException, IllegalFilterException {
        // GEOT-1069, make sure the post filter is run even if the geom property is not requested
        try (Transaction t = new DefaultTransaction()) {
            FilterFactory factory = CommonFactoryFinder.getFilterFactory(null);
            FilterFunction_geometryType geomTypeExpr = new FilterFunction_geometryType();
            geomTypeExpr.setParameters(Collections.singletonList(factory.property(aname("geom"))));

            PropertyIsEqualTo filter = factory.equals(geomTypeExpr, factory.literal("Polygon"));

            Query query = new Query(tname("road"), filter);
            query.setPropertyNames(Collections.singletonList(aname("id")));

            try (FeatureReader<SimpleFeatureType, SimpleFeature> reader = dataStore.getFeatureReader(query, t)) {
                // if the above statement didn't throw an exception, we're content
                assertNotNull(reader);
            }

            filter = factory.equals(geomTypeExpr, factory.literal("LineString"));
            query.setFilter(filter);
            try (FeatureReader<SimpleFeatureType, SimpleFeature> reader = dataStore.getFeatureReader(query, t)) {
                assertTrue(reader.hasNext());
            }
        }
    }

    @Test
    public void testGetFeatureReaderFilterWithAttributesNotRequested() throws Exception {
        // this is here to avoid http://jira.codehaus.org/browse/GEOT-1069
        // to come up again
        dataStore.getSchema(tname("river"));
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        PropertyIsEqualTo f = ff.equals(ff.property(aname("flow")), ff.literal(4.5));

        Query q = new Query(tname("river"));
        q.setPropertyNames(aname("geom"));
        q.setFilter(f);

        // with GEOT-1069 an exception is thrown here
        try (Transaction t = new DefaultTransaction();
                FeatureReader<SimpleFeatureType, SimpleFeature> reader = dataStore.getFeatureReader(q, t)) {
            assertTrue(reader.hasNext());
            assertEquals(1, reader.getFeatureType().getAttributeCount());
            reader.next();
            assertFalse(reader.hasNext());
        }
    }

    @Test
    public void testGetFeatureReaderFilterWithAttributesNotRequested2() throws Exception {
        dataStore.getSchema(tname("river"));

        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        FilterFunction_ceil ceil = new FilterFunction_ceil();
        ceil.setParameters(Collections.singletonList(ff.property(aname("flow"))));

        PropertyIsEqualTo f = ff.equals(ceil, ff.literal(5));

        Query q = new Query(tname("river"));
        q.setPropertyNames(aname("geom"));
        q.setFilter(f);

        // with GEOT-1069 an exception is thrown here
        try (Transaction t = new DefaultTransaction();
                FeatureReader<SimpleFeatureType, SimpleFeature> reader = dataStore.getFeatureReader(q, t)) {

            assertTrue(reader.hasNext());
            assertEquals(1, reader.getFeatureType().getAttributeCount());
            reader.next();
            assertFalse(reader.hasNext());
        }
    }

    @Test
    @SuppressWarnings("PMD.UnusedLocalVariable")
    public void testGetFeatureInvalidFilter() throws Exception {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        PropertyIsEqualTo f = ff.equals(ff.property("invalidAttribute"), ff.literal(5));

        Query q = new Query(tname("river"));
        q.setPropertyNames(aname("geom"));
        q.setFilter(f);

        // make sure a complaint related to the invalid filter is thrown here
        try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                dataStore.getFeatureReader(q, Transaction.AUTO_COMMIT)) {
            fail("This query should have failed, it contains an invalid filter");
        } catch (Exception e) {
            // good, it's supposed to fail
        }
    }

    @Test
    public void testGetFeatureReaderMutability() throws IOException, IllegalAttributeException {
        try (FeatureReader<SimpleFeatureType, SimpleFeature> reader = reader(tname("road"))) {
            while (reader.hasNext()) {
                SimpleFeature feature = reader.next();
                feature.setAttribute(aname("name"), null);
            }
        }

        try (FeatureReader<SimpleFeatureType, SimpleFeature> reader = reader(tname("road"))) {
            while (reader.hasNext()) {
                SimpleFeature feature = reader.next();
                assertNotNull(feature.getAttribute(aname("name")));
            }

            // close early to check next() after it
            reader.close();

            reader.next();
            fail("next should fail with an IOException");
        } catch (Exception expected) {
            // fine, it's expected
        }
    }

    @Test
    public void testGetFeatureReaderConcurrency() throws NoSuchElementException, IOException {
        try (FeatureReader<SimpleFeatureType, SimpleFeature> reader1 = reader(tname("road"));
                FeatureReader<SimpleFeatureType, SimpleFeature> reader2 = reader(tname("road"));
                FeatureReader<SimpleFeatureType, SimpleFeature> reader3 = reader(tname("river"))) {
            while (reader1.hasNext() || reader2.hasNext() || reader3.hasNext()) {
                assertContains(td.roadFeatures, reader1.next());

                assertTrue(reader2.hasNext());
                assertContains(td.roadFeatures, reader2.next());

                if (reader3.hasNext()) {
                    assertContains(td.riverFeatures, reader3.next());
                }
            }

            try {
                assertFalse(reader1.hasNext());
                reader1.next();
                fail("next should fail with an NoSuchElementException");
            } catch (Exception expectedNoElement) {
                // this is new to me, I had expected an IOException
            }

            try {
                reader2.next();
                fail("next should fail with an NoSuchElementException");
            } catch (Exception expectedNoElement) {
            }

            try {
                reader3.next();
                fail("next should fail with an NoSuchElementException");
            } catch (Exception expectedNoElement) {
            }

            reader1.close();
            reader2.close();
            reader3.close();

            try {
                reader1.next();
                fail("next should fail with an IOException");
            } catch (IOException expectedClosed) {
            }

            try {
                reader2.next();
                fail("next should fail with an IOException");
            } catch (IOException expectedClosed) {
            }

            try {
                reader3.next();
                fail("next should fail with an IOException");
            } catch (IOException expectedClosed) {
            }
        }
    }

    @Test
    public void testGetFeatureReaderFilterAutoCommit()
            throws NoSuchElementException, IOException, IllegalAttributeException {
        SimpleFeatureType type = dataStore.getSchema(tname("road"));
        try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                dataStore.getFeatureReader(new Query(tname("road"), Filter.INCLUDE), Transaction.AUTO_COMMIT)) {
            assertFalse(reader instanceof FilteringFeatureReader);
            assertEquals(type, reader.getFeatureType());
            assertEquals(td.roadFeatures.length, count(reader));
        }

        try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                dataStore.getFeatureReader(new Query(tname("road"), Filter.EXCLUDE), Transaction.AUTO_COMMIT)) {
            assertFalse(reader.hasNext());
            assertEquals(type, reader.getFeatureType());
            assertEquals(0, count(reader));
        }

        try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                dataStore.getFeatureReader(new Query(tname("road"), td.rd1Filter), Transaction.AUTO_COMMIT)) {
            // assertTrue(reader instanceof FilteringFeatureReader);
            assertEquals(type, reader.getFeatureType());
            assertEquals(1, count(reader));
        }
    }

    @Test
    public void testGetFeatureReaderFilterTransaction()
            throws NoSuchElementException, IOException, IllegalAttributeException {
        SimpleFeatureType type = dataStore.getSchema(tname("road"));

        try (Transaction t = new DefaultTransaction()) {
            try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                    dataStore.getFeatureReader(new Query(tname("road"), Filter.EXCLUDE), t)) {
                assertFalse(reader.hasNext());
                assertEquals(type, reader.getFeatureType());
                assertEquals(0, count(reader));
            }

            try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                    dataStore.getFeatureReader(new Query(tname("road"), Filter.INCLUDE), t)) {
                assertEquals(type, reader.getFeatureType());
                assertEquals(td.roadFeatures.length, count(reader));
            }

            try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                    dataStore.getFeatureReader(new Query(tname("road"), td.rd1Filter), t)) {
                assertEquals(type, reader.getFeatureType());
                assertEquals(1, count(reader));
            }

            try (FeatureWriter<SimpleFeatureType, SimpleFeature> writer =
                    dataStore.getFeatureWriter(tname("road"), Filter.INCLUDE, t)) {
                SimpleFeature feature;

                while (writer.hasNext()) {
                    feature = writer.next();

                    if (feature.getID().equals(td.roadFeatures[0].getID())) {
                        writer.remove();
                    }
                }
            }

            try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                    dataStore.getFeatureReader(new Query(tname("road"), Filter.EXCLUDE), t)) {
                assertEquals(0, count(reader));
            }

            try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                    dataStore.getFeatureReader(new Query(tname("road"), Filter.INCLUDE), t)) {
                assertEquals(td.roadFeatures.length - 1, count(reader));
            }

            try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                    dataStore.getFeatureReader(new Query(tname("road"), td.rd1Filter), t)) {
                assertEquals(0, count(reader));
            }

            t.rollback();
            try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                    dataStore.getFeatureReader(new Query(tname("road"), Filter.EXCLUDE), t)) {
                assertEquals(0, count(reader));
            }

            try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                    dataStore.getFeatureReader(new Query(tname("road"), Filter.INCLUDE), t)) {
                assertEquals(td.roadFeatures.length, count(reader));
            }

            try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                    dataStore.getFeatureReader(new Query(tname("road"), td.rd1Filter), t)) {
                assertEquals(1, count(reader));
            }
        }
    }

    @Test
    public void testGetFeatureWriterClose() throws Exception {
        try (FeatureWriter<SimpleFeatureType, SimpleFeature> writer =
                dataStore.getFeatureWriter(tname("road"), Filter.INCLUDE, Transaction.AUTO_COMMIT)) {
            writer.close();

            try {
                assertFalse(writer.hasNext());
                fail("Should not be able to use a closed writer");
            } catch (Exception expected) {
            }

            try {
                assertNull(writer.next());
                fail("Should not be able to use a closed writer");
            } catch (Exception expected) {
            }
        }
    }

    @Test
    public void testGetFeatureWriterRemove() throws IOException {
        try (FeatureWriter<SimpleFeatureType, SimpleFeature> writer = writer(tname("road"))) {
            SimpleFeature feature;

            while (writer.hasNext()) {
                feature = writer.next();

                if (feature.getID().equals(td.roadFeatures[0].getID())) {
                    writer.remove();
                }
            }
        }

        assertEquals(td.roadFeatures.length - 1, count(tname("road")));
    }

    @Test
    public void testGetFeatureWriterRemoveAll() throws IOException {
        try (FeatureWriter<SimpleFeatureType, SimpleFeature> writer = writer(tname("road"))) {
            while (writer.hasNext()) {
                writer.next();
                writer.remove();
            }
        }

        assertEquals(0, count(tname("road")));
    }

    @Test
    public void testGetFeaturesWriterAdd() throws IOException {
        try (FeatureWriter<SimpleFeatureType, SimpleFeature> writer =
                dataStore.getFeatureWriter(tname("road"), Transaction.AUTO_COMMIT)) {
            while (writer.hasNext()) {
                writer.next();
            }

            assertFalse(writer.hasNext());

            SimpleFeature feature = writer.next();
            feature.setAttributes(td.newRoad.getAttributes());
            writer.write();

            assertFalse(writer.hasNext());

            assertEquals(td.roadFeatures.length + 1, count(tname("road")));
        }
    }

    @Test
    public void testGetFeaturesWriterModify() throws IOException {
        try (FeatureWriter<SimpleFeatureType, SimpleFeature> writer = writer(tname("road"))) {

            while (writer.hasNext()) {
                SimpleFeature feature = writer.next();

                if (feature.getID().equals(td.roadFeatures[0].getID())) {
                    feature.setAttribute(aname("name"), "changed");
                    writer.write();
                }
            }
        }

        SimpleFeature feature = feature(tname("road"), td.roadFeatures[0].getID());
        assertNotNull(feature);
        assertEquals("changed", feature.getAttribute(aname("name")));
    }

    @Test
    public void testGetFeatureWriterTypeNameTransaction() throws NoSuchElementException, IOException {
        try (FeatureWriter<SimpleFeatureType, SimpleFeature> writer =
                dataStore.getFeatureWriter(tname("road"), Transaction.AUTO_COMMIT)) {
            assertEquals(td.roadFeatures.length, count(writer));
        }
    }

    @Test
    public void testGetFeatureWriterAppendTypeNameTransaction() throws Exception {
        try (FeatureWriter<SimpleFeatureType, SimpleFeature> writer =
                dataStore.getFeatureWriterAppend(tname("road"), Transaction.AUTO_COMMIT)) {
            assertEquals(0, count(writer));
        }
    }

    /*
     * Test for FeatureWriter getFeatureWriter(String, boolean, Transaction) @task REVISIT:
     * JDBCDataStore currently does not return these proper instanceof's. If we want to guarantee
     * that people can't append to a request with a FeatureWriter then we could add the
     * functionality to JDBCDataStore by having getFeatureWriter(.. Filter ...) check to see if the
     * FeatureWriter returned is instanceof FilteringFeatureWriter, and if not then just wrap it in
     * a FilteringFeatureWriter(writer, Filter.INCLUDE). I think it'd be a bit of unnecessary
     * overhead, but if we want it it's easy to do. It will guarantee that calls with Filter won't
     * ever append. Doing with Filter.INCLUDE, however, would require a bit of reworking, as the
     * Filter getFeatureWriter is currently where we do the bulk of the work.
     */
    @Test
    public void testGetFeatureWriterFilter() throws NoSuchElementException, IOException {
        try (FeatureWriter<SimpleFeatureType, SimpleFeature> writer =
                dataStore.getFeatureWriter(tname("road"), Filter.EXCLUDE, Transaction.AUTO_COMMIT)) {
            // see task above
            // assertTrue(writer instanceof EmptyFeatureWriter);
            assertEquals(0, count(writer));
        }

        try (FeatureWriter<SimpleFeatureType, SimpleFeature> writer =
                dataStore.getFeatureWriter(tname("road"), Filter.INCLUDE, Transaction.AUTO_COMMIT)) {
            // assertFalse(writer instanceof FilteringFeatureWriter);
            assertEquals(td.roadFeatures.length, count(writer));
        }

        try (FeatureWriter<SimpleFeatureType, SimpleFeature> writer =
                dataStore.getFeatureWriter(tname("road"), td.rd1Filter, Transaction.AUTO_COMMIT)) {
            // assertTrue(writer instanceof FilteringFeatureWriter);
            assertEquals(1, count(writer));
        }
    }

    @Test
    @SuppressWarnings("PMD.UnusedLocalVariable")
    public void testGetFeatureWriterInvalidFilter() {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        PropertyIsEqualTo f = ff.equals(ff.property("invalidAttribute"), ff.literal(5));

        // make sure a complaint related to the invalid filter is thrown here
        try (FeatureWriter<SimpleFeatureType, SimpleFeature> writer =
                dataStore.getFeatureWriter("river", f, Transaction.AUTO_COMMIT)) {
            fail("This query should have failed, it contains an invalid filter");
        } catch (Exception e) {
            // fine
        }
    }

    /** Test two transactions one removing feature, and one adding a feature. */
    @Test
    public void testTransactionIsolation() throws Exception {
        try (Transaction t1 = new DefaultTransaction();
                Transaction t2 = new DefaultTransaction()) {

            SimpleFeature feature;
            SimpleFeature[] ORIGINAL = td.roadFeatures;
            SimpleFeature[] REMOVE = new SimpleFeature[ORIGINAL.length - 1];
            SimpleFeature[] ADD = new SimpleFeature[ORIGINAL.length + 1];
            SimpleFeature[] FINAL = new SimpleFeature[ORIGINAL.length];

            try (FeatureWriter<SimpleFeatureType, SimpleFeature> writer1 =
                            dataStore.getFeatureWriter(tname("road"), td.rd1Filter, t1);
                    FeatureWriter<SimpleFeatureType, SimpleFeature> writer2 =
                            dataStore.getFeatureWriterAppend(tname("road"), t2)) {
                int i;
                int index = 0;

                for (i = 0; i < ORIGINAL.length; i++) {
                    feature = ORIGINAL[i];

                    if (!feature.getID().equals(td.roadFeatures[0].getID())) {
                        REMOVE[index++] = feature;
                    }
                }

                for (i = 0; i < ORIGINAL.length; i++) {
                    ADD[i] = ORIGINAL[i];
                }

                ADD[i] = td.newRoad; // will need to update with Fid from database

                for (i = 0; i < REMOVE.length; i++) {
                    FINAL[i] = REMOVE[i];
                }

                FINAL[i] = td.newRoad; // will need to update with Fid from database

                // start off with ORIGINAL
                try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                        dataStore.getFeatureReader(new Query(tname("road"), Filter.INCLUDE), Transaction.AUTO_COMMIT)) {
                    assertTrue(
                            "Sanity check failed: before modification reader didn't match original content",
                            covers(reader, ORIGINAL));
                }

                // writer 1 removes road.rd1 on t1
                // -------------------------------
                // - tests transaction independence from DataStore
                while (writer1.hasNext()) {
                    feature = writer1.next();
                    assertEquals(td.roadFeatures[0].getID(), feature.getID());
                    writer1.remove();
                }

                // still have ORIGINAL and t1 has REMOVE
                try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                        dataStore.getFeatureReader(new Query(tname("road"), Filter.INCLUDE), Transaction.AUTO_COMMIT)) {
                    assertTrue("Feature deletion managed to leak out of transaction?", covers(reader, ORIGINAL));
                }

                try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                        dataStore.getFeatureReader(new Query(tname("road"), Filter.INCLUDE), t1)) {
                    assertTrue(covers(reader, REMOVE));
                }

                // close writer1
                // --------------
                // ensure that modification is left up to transaction commmit
                writer1.close();

                // We still have ORIGIONAL and t1 has REMOVE
                try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                        dataStore.getFeatureReader(new Query(tname("road"), Filter.INCLUDE), Transaction.AUTO_COMMIT)) {
                    assertTrue(covers(reader, ORIGINAL));
                }

                try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                        dataStore.getFeatureReader(new Query(tname("road"), Filter.INCLUDE), t1)) {
                    assertTrue(covers(reader, REMOVE));
                }

                // writer 2 adds road.rd4 on t2
                // ----------------------------
                // - tests transaction independence from each other
                feature = writer2.next();
                feature.setAttributes(td.newRoad.getAttributes());
                writer2.write();
            }

            // HACK: ?!? update ADD and FINAL with new FID from database
            //
            try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                    dataStore.getFeatureReader(new Query(tname("road"), Filter.INCLUDE), t2)) {
                td.newRoad = findFeature(reader, aname("name"), "r4");
                // System.out.println("newRoad:" + td.newRoad);
                ADD[ADD.length - 1] = td.newRoad;
                FINAL[FINAL.length - 1] = td.newRoad;
            }

            // We still have ORIGINAL and t2 has ADD
            try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                    dataStore.getFeatureReader(new Query(tname("road"), Filter.INCLUDE), Transaction.AUTO_COMMIT)) {
                assertTrue(covers(reader, ORIGINAL));
            }

            try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                    dataStore.getFeatureReader(new Query(tname("road"), Filter.INCLUDE), t2)) {
                assertMatched(ADD, reader); // broken due to FID problem
            }

            // Still have ORIGIONAL and t2 has ADD
            try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                    dataStore.getFeatureReader(new Query(tname("road"), Filter.INCLUDE), Transaction.AUTO_COMMIT)) {
                assertTrue(covers(reader, ORIGINAL));
            }
            try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                    dataStore.getFeatureReader(new Query(tname("road"), Filter.INCLUDE), t2)) {
                assertTrue(coversLax(reader, ADD));
            }

            // commit t1
            // ---------
            // -ensure that delayed writing of transactions takes place
            //
            t1.commit();

            // We now have REMOVE, as does t1 (which has not additional diffs)
            // t2 will have FINAL
            try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                    dataStore.getFeatureReader(new Query(tname("road"), Filter.INCLUDE), Transaction.AUTO_COMMIT)) {
                assertTrue(covers(reader, REMOVE));
            }
            try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                    dataStore.getFeatureReader(new Query(tname("road"), Filter.INCLUDE), t1)) {
                assertTrue(covers(reader, REMOVE));
            }
            try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                    dataStore.getFeatureReader(new Query(tname("road"), Filter.INCLUDE), t2)) {
                assertTrue(coversLax(reader, FINAL));
            }

            // commit t2
            // ---------
            // -ensure that everyone is FINAL at the end of the day
            t2.commit();

            // We now have Number( remove one and add one)
            try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                    dataStore.getFeatureReader(new Query(tname("road"), Filter.INCLUDE), Transaction.AUTO_COMMIT)) {
                assertTrue(coversLax(reader, FINAL));
            }

            try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                    dataStore.getFeatureReader(new Query(tname("road"), Filter.INCLUDE), t1)) {
                assertTrue(coversLax(reader, FINAL));
            }

            try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                    dataStore.getFeatureReader(new Query(tname("road"), Filter.INCLUDE), t2)) {
                assertTrue(coversLax(reader, FINAL));
            }
        }
    }

    /**
     * Tests that if 2 transactions attempt to modify the same feature without committing, that the second transaction
     * does not lock up waiting to obtain the lock.
     *
     * @author chorner
     */
    @Test
    public void testGetFeatureWriterConcurrency() throws Exception {
        try (Transaction t1 = new DefaultTransaction();
                FeatureWriter<SimpleFeatureType, SimpleFeature> writer1 =
                        dataStore.getFeatureWriter(tname("road"), td.rd1Filter, t1)) {
            assertTrue(writer1.hasNext());
            SimpleFeature f1 = writer1.next();
            f1.setAttribute("name", "r1_");
            writer1.write();

            try (Transaction t2 = new DefaultTransaction();
                    FeatureWriter<SimpleFeatureType, SimpleFeature> writer2 =
                            dataStore.getFeatureWriter(tname("road"), td.rd1Filter, t2)) {
                assertTrue(writer2.hasNext());
                SimpleFeature f2 = writer2.next();
                f2.setAttribute("name", "r1__");

                try {
                    writer2.write(); // this will either lock up or toss chunks
                    fail("Feature lock should have failed");
                } catch (FeatureLockException e) {
                    // success (test-wise... our write failed quite well too)
                    assertEquals(tname("road") + ".rd1", e.getFeatureID());
                }

                t1.rollback(); // don't save

                t2.rollback();
            }
        }
    }

    // Feature Source Testing
    @Test
    public void testGetFeatureSourceRoad() throws Exception {
        SimpleFeatureSource road = dataStore.getFeatureSource(tname("road"));

        assertFeatureTypesEqual(td.roadType, road.getSchema());
        assertSame(dataStore, road.getDataStore());

        int count = road.getCount(Query.ALL);
        assertTrue(count == 3 || count == -1);

        ReferencedEnvelope bounds = road.getBounds(Query.ALL);
        assertTrue(bounds == null || areReferencedEnvelopesEqual(bounds, td.roadBounds));

        SimpleFeatureCollection all = road.getFeatures();
        assertEquals(3, all.size());
        // assertEquals(td.roadBounds, all.getBounds());
        assertTrue(areReferencedEnvelopesEqual(td.roadBounds, all.getBounds()));

        SimpleFeatureCollection expected = DataUtilities.collection(td.roadFeatures);

        assertCovers("all", expected, all);
        //        assertEquals(td.roadBounds, all.getBounds());
        assertTrue(areReferencedEnvelopesEqual(td.roadBounds, all.getBounds()));

        SimpleFeatureCollection some = road.getFeatures(td.rd12Filter);
        assertEquals(2, some.size());

        ReferencedEnvelope e = new ReferencedEnvelope(decodeEPSG(4326));
        e.include(td.roadFeatures[0].getBounds());
        e.include(td.roadFeatures[1].getBounds());
        //        assertEquals(e, some.getBounds());
        assertTrue(areReferencedEnvelopesEqual(e, some.getBounds()));

        assertEquals(some.getSchema(), road.getSchema());

        Query query = new Query(tname("road"), td.rd12Filter, new String[] {aname("name")});

        SimpleFeatureCollection half = road.getFeatures(query);
        assertEquals(2, half.size());
        assertEquals(1, half.getSchema().getAttributeCount());

        try (SimpleFeatureIterator reader = half.features()) {
            SimpleFeatureType type = half.getSchema();
            SimpleFeatureType actual = reader.next().getFeatureType();

            assertEquals(type.getName(), actual.getName());
            assertEquals(type.getAttributeCount(), actual.getAttributeCount());

            for (int i = 0; i < type.getAttributeCount(); i++) {
                assertEquals(type.getDescriptor(i), actual.getDescriptor(i));
            }

            assertNull(type.getGeometryDescriptor()); // geometry is null, therefore no bounds
            assertEquals(type.getGeometryDescriptor(), actual.getGeometryDescriptor());
            assertEquals(type, actual);

            ReferencedEnvelope b = half.getBounds();
            ReferencedEnvelope expectedBounds = td.roadBounds;
            // assertEquals(expectedBounds, b);
            assertTrue(areReferencedEnvelopesEqual(expectedBounds, b));
        }
    }

    @Test
    public void testGetFeatureSourceRiver() throws NoSuchElementException, IOException, IllegalAttributeException {
        SimpleFeatureSource river = dataStore.getFeatureSource(tname("river"));

        assertFeatureTypesEqual(td.riverType, river.getSchema());
        assertSame(dataStore, river.getDataStore());

        SimpleFeatureCollection all = river.getFeatures();
        assertEquals(2, all.size());

        // assertEquals(td.riverBounds, all.getBounds());
        assertTrue(areReferencedEnvelopesEqual(td.riverBounds, all.getBounds()));

        assertTrue("rivers", covers(all.features(), td.riverFeatures));

        SimpleFeatureCollection expected = DataUtilities.collection(td.riverFeatures);
        assertCovers("all", expected, all);
        // assertEquals(td.riverBounds, all.getBounds());
        assertTrue(areReferencedEnvelopesEqual(td.riverBounds, all.getBounds()));
    }

    //
    // Feature Store Testing
    //
    @Test
    public void testGetFeatureStoreModifyFeatures1() throws IOException {
        SimpleFeatureStore road = (SimpleFeatureStore) dataStore.getFeatureSource(tname("road"));

        // FilterFactory factory = FilterFactoryFinder.createFilterFactory();
        // rd1Filter = factory.createFidFilter( roadFeatures[0].getID() );
        Object changed = Integer.valueOf(5);
        road.modifyFeatures(new NameImpl(aname("id")), changed, td.rd1Filter);

        SimpleFeatureCollection results = road.getFeatures(td.rd1Filter);
        try (SimpleFeatureIterator features = results.features()) {
            assertTrue(features.hasNext());
            assertEquals(5, ((Number) features.next().getAttribute(aname("id"))).intValue());
        }
    }

    @Test
    public void testGetFeatureStoreModifyFeatures2() throws IOException {
        SimpleFeatureStore road = (SimpleFeatureStore) dataStore.getFeatureSource(tname("road"));

        FilterFactory factory = CommonFactoryFinder.getFilterFactory(null);

        // td.rd1Filter = factory.createFidFilter(td.roadFeatures[0].getID());
        Filter rd1Filter = factory.id(Collections.singleton(factory.featureId(td.roadFeatures[0].getID())));

        road.modifyFeatures(
                new Name[] {
                    new NameImpl(aname("name")),
                },
                new Object[] {
                    "changed",
                },
                rd1Filter);

        SimpleFeatureCollection results = road.getFeatures(td.rd1Filter);
        try (SimpleFeatureIterator features = results.features()) {
            assertTrue(features.hasNext());
            assertEquals("changed", features.next().getAttribute(aname("name")));
        }
    }

    /** Test with a filter that won't be matched after the modification is done, was throwing an NPE before the fix */
    @Test
    public void testGetFeatureStoreModifyFeatures3() throws IOException {
        SimpleFeatureStore road = (SimpleFeatureStore) dataStore.getFeatureSource(tname("road"));

        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        PropertyIsEqualTo filter = ff.equals(ff.property(aname("name")), ff.literal("r1"));

        road.modifyFeatures(
                new Name[] {
                    new NameImpl(aname("name")),
                },
                new Object[] {
                    "changed",
                },
                filter);
    }

    @Test
    public void testGetFeatureStoreRemoveFeatures() throws IOException {
        SimpleFeatureStore road = (SimpleFeatureStore) dataStore.getFeatureSource(tname("road"));

        road.removeFeatures(td.rd1Filter);
        assertEquals(0, road.getFeatures(td.rd1Filter).size());
        assertEquals(td.roadFeatures.length - 1, road.getFeatures().size());
    }

    @Test
    public void testGetFeatureStoreRemoveAllFeatures() throws IOException {
        SimpleFeatureStore road = (SimpleFeatureStore) dataStore.getFeatureSource(tname("road"));

        road.removeFeatures(Filter.INCLUDE);
        assertEquals(0, road.getFeatures().size());
    }

    @Test
    public void testGetFeatureStoreAddFeatures() throws IOException {
        try (FeatureReader<SimpleFeatureType, SimpleFeature> reader = DataUtilities.reader(td.newRoad)) {

            SimpleFeatureStore road = (SimpleFeatureStore) dataStore.getFeatureSource(tname("road"));

            road.addFeatures(DataUtilities.collection(reader));
            assertEquals(td.roadFeatures.length + 1, count(tname("road")));
        }
    }

    @Test
    public void testGetFeatureStoreSetFeatures() throws NoSuchElementException, IOException, IllegalAttributeException {
        try (FeatureReader<SimpleFeatureType, SimpleFeature> reader = DataUtilities.reader(td.newRoad)) {
            SimpleFeatureStore road = (SimpleFeatureStore) dataStore.getFeatureSource(tname("road"));
            assertEquals(3, count(tname("road")));
            road.setFeatures(reader);
            assertEquals(1, count(tname("road")));
        }
    }

    boolean isLocked(String typeName, String fid) {
        InProcessLockingManager lockingManager = (InProcessLockingManager) dataStore.getLockingManager();

        return lockingManager.isLocked(typeName, fid);
    }

    //
    // FeatureLocking Testing
    //

    /*
     * Test for void lockFeatures()
     */
    @Test
    public void testLockFeatures() throws IOException {
        FeatureLock lock = new FeatureLock("test", LOCK_DURATION);
        SimpleFeatureLocking road = (SimpleFeatureLocking) dataStore.getFeatureSource(tname("road"));
        road.setFeatureLock(lock);

        assertFalse(isLocked(tname("road"), tname("road") + ".1"));
        assertTrue(road.lockFeatures() > 0);
        assertTrue(isLocked(tname("road"), tname("road") + ".1"));
    }

    @Test
    public void testUnLockFeatures() throws IOException {
        FeatureLock lock = new FeatureLock("test", LOCK_DURATION);
        SimpleFeatureLocking road = (SimpleFeatureLocking) dataStore.getFeatureSource(tname("road"));
        road.setFeatureLock(lock);
        road.lockFeatures();

        try {
            road.unLockFeatures();
            fail("unlock should fail due on AUTO_COMMIT");
        } catch (IOException expected) {
        }

        try (Transaction t = new DefaultTransaction()) {
            road.setTransaction(t);

            try {
                road.unLockFeatures();
                fail("unlock should fail due lack of authorization");
            } catch (IOException expected) {
            }

            t.addAuthorization(lock.getAuthorization());
            road.unLockFeatures();
        }
    }

    @Test
    public void testLockFeatureInteraction() throws IOException {
        FeatureLock lockA = new FeatureLock("LockA", LOCK_DURATION);
        FeatureLock lockB = new FeatureLock("LockB", LOCK_DURATION);
        try (Transaction t1 = new DefaultTransaction();
                Transaction t2 = new DefaultTransaction()) {
            SimpleFeatureLocking road1 = (SimpleFeatureLocking) dataStore.getFeatureSource(tname("road"));
            SimpleFeatureLocking road2 = (SimpleFeatureLocking) dataStore.getFeatureSource(tname("road"));
            road1.setTransaction(t1);
            road2.setTransaction(t2);
            road1.setFeatureLock(lockA);
            road2.setFeatureLock(lockB);

            assertFalse(isLocked(tname("road"), tname("road") + ".0"));
            assertFalse(isLocked(tname("road"), tname("road") + ".1"));
            assertFalse(isLocked(tname("road"), tname("road") + ".2"));

            assertEquals(1, road1.lockFeatures(td.rd1Filter));
            assertTrue(isLocked(tname("road"), tname("road") + "." + td.initialFidValue));
            assertFalse(isLocked(tname("road"), tname("road") + "." + (td.initialFidValue + 1)));
            assertFalse(isLocked(tname("road"), tname("road") + "." + (td.initialFidValue + 2)));

            road2.lockFeatures(td.rd2Filter);
            assertTrue(isLocked(tname("road"), tname("road") + "." + td.initialFidValue));
            assertTrue(isLocked(tname("road"), tname("road") + "." + (td.initialFidValue + 1)));
            assertFalse(isLocked(tname("road"), tname("road") + "." + (td.initialFidValue + 2)));

            try {
                road1.unLockFeatures(td.rd1Filter);
                fail("need authorization");
            } catch (IOException expected) {
            }

            t1.addAuthorization(lockA.getAuthorization());

            try {
                road1.unLockFeatures(td.rd2Filter);
                fail("need correct authorization");
            } catch (IOException expected) {
            }

            road1.unLockFeatures(td.rd1Filter);
            assertFalse(isLocked(tname("road"), tname("road") + "." + td.initialFidValue));
            assertTrue(isLocked(tname("road"), tname("road") + "." + (td.initialFidValue + 1)));
            assertFalse(isLocked(tname("road"), tname("road") + "." + (td.initialFidValue + 2)));

            t2.addAuthorization(lockB.getAuthorization());
            road2.unLockFeatures(td.rd2Filter);
            assertFalse(isLocked(tname("road"), tname("road") + "." + td.initialFidValue));
            assertFalse(isLocked(tname("road"), tname("road") + "." + (td.initialFidValue + 1)));
            assertFalse(isLocked(tname("road"), tname("road") + "." + (td.initialFidValue + 2)));
        }
    }

    @Test
    public void testGetFeatureLockingExpire() throws Exception {
        FeatureLock lock = new FeatureLock("Timed", 1000);

        SimpleFeatureLocking road = (SimpleFeatureLocking) dataStore.getFeatureSource(tname("road"));
        road.setFeatureLock(lock);
        assertFalse(isLocked(tname("road"), tname("road") + "." + td.initialFidValue));

        road.lockFeatures(td.rd1Filter);
        assertTrue(isLocked(tname("road"), tname("road") + "." + td.initialFidValue));
        long then = System.currentTimeMillis();
        do {
            Thread.sleep(1000);
        } while (System.currentTimeMillis() - then < 1000);
        assertFalse(isLocked(tname("road"), tname("road") + "." + td.initialFidValue));
    }

    int count(String typeName) throws IOException {
        // return count(reader(typeName));
        // makes use of optimization if any
        return dataStore.getFeatureSource(typeName).getFeatures().size();
    }

    /**
     * Counts the number of Features returned by the specified reader.
     *
     * <p>This method will close the reader.
     */
    int count(FeatureReader<SimpleFeatureType, SimpleFeature> reader) throws IOException {
        if (reader == null) {
            return -1;
        }

        int count = 0;

        try (reader) {
            while (reader.hasNext()) {
                reader.next();
                count++;
            }
        } catch (NoSuchElementException e) {
            // bad dog!
            throw new DataSourceException("hasNext() lied to me at:" + count, e);
        } catch (IllegalAttributeException e) {
            throw new DataSourceException("next() could not understand feature at:" + count, e);
        }

        return count;
    }

    /** Ensure readers contents equal those in the feature array */
    void assertCovered(SimpleFeature[] features, FeatureReader<SimpleFeatureType, SimpleFeature> reader)
            throws NoSuchElementException, IOException, IllegalAttributeException {
        int count = 0;

        try (reader) {
            while (reader.hasNext()) {
                assertContains(features, reader.next());
                count++;
            }
        }

        assertEquals(features.length, count);
    }

    void assertCovers(String msg, SimpleFeatureCollection c1, SimpleFeatureCollection c2) {
        if (c1 == c2) {
            return;
        }

        assertNotNull(msg, c1);
        assertNotNull(msg, c2);
        assertEquals(msg + " size", c1.size(), c2.size());

        SimpleFeature f;
        SimpleFeature g;

        try (SimpleFeatureIterator i = c1.features()) {
            while (i.hasNext()) {
                f = i.next();

                boolean found = false;

                try (SimpleFeatureIterator j = c2.features()) {
                    while (j.hasNext() && !found) {
                        g = j.next();
                        found = f.getID().equals(g.getID());
                    }
                    j.close();

                    assertTrue(msg + " " + f.getID(), found);
                }
            }
        }
    }

    void assertContains(SimpleFeature[] array, SimpleFeature expected) {
        assertNotNull(array);
        assertNotEquals(0, array.length);
        assertNotNull(expected);

        for (SimpleFeature simpleFeature : array) {
            if (id(simpleFeature.getID(), simpleFeature).equals(expected.getID())) {
                return;
            }
        }

        fail("Contains " + expected);
    }

    String id(String raw, SimpleFeature f) {
        if (raw == null) {
            return null;
        }
        if (raw.startsWith(f.getType().getTypeName() + ".")) {
            return tname(f.getType().getTypeName())
                    + raw.substring(f.getType().getTypeName().length());
        }

        return raw;
    }

    boolean contains(Object[] array, Object expected) {
        if (array == null || array.length == 0) {
            return false;
        }

        for (Object o : array) {
            if (o.equals(expected)) {
                return true;
            }
        }

        return false;
    }

    FeatureReader<SimpleFeatureType, SimpleFeature> reader(String typeName) throws IOException {
        return dataStore.getFeatureReader(new Query(typeName, Filter.INCLUDE), Transaction.AUTO_COMMIT);
    }

    FeatureWriter<SimpleFeatureType, SimpleFeature> writer(String typeName) throws IOException {
        return dataStore.getFeatureWriter(typeName, Transaction.AUTO_COMMIT);
    }

    /** Counts the number of Features in the specified writer. This method will close the writer. */
    protected int count(FeatureWriter<SimpleFeatureType, SimpleFeature> writer)
            throws NoSuchElementException, IOException, IllegalAttributeException {
        int count = 0;

        try (writer) {
            while (writer.hasNext()) {
                writer.next();
                count++;
            }
        }

        return count;
    }

    protected SimpleFeature feature(String typeName, String fid)
            throws NoSuchElementException, IOException, IllegalAttributeException {

        try (FeatureReader<SimpleFeatureType, SimpleFeature> reader = reader(typeName)) {
            SimpleFeature f;
            while (reader.hasNext()) {
                f = reader.next();

                if (fid.equals(f.getID())) {
                    return f;
                }
            }
        }

        return null;
    }

    boolean covers(SimpleFeatureIterator reader, SimpleFeature[] array)
            throws NoSuchElementException, IOException, IllegalAttributeException {
        SimpleFeature feature;
        int count = 0;

        try (reader) {
            while (reader.hasNext()) {
                feature = reader.next();

                assertContains(array, feature);
                //                if (!contains(array, feature)) {
                //                    return false;
                //                }
                count++;
            }
        }

        return count == array.length;
    }

    /** Ensure that FeatureReader<SimpleFeatureType, SimpleFeature> reader contains exactly the contents of array. */
    boolean covers(FeatureReader<SimpleFeatureType, SimpleFeature> reader, SimpleFeature[] array)
            throws NoSuchElementException, IOException, IllegalAttributeException {
        SimpleFeature feature;
        int count = 0;

        try (reader) {
            while (reader.hasNext()) {
                feature = reader.next();

                assertContains(array, feature);
                //                if (!contains(array, feature)) {
                //                    return false;
                //                }
                count++;
            }
        }

        return count == array.length;
    }

    boolean coversLax(FeatureReader<SimpleFeatureType, SimpleFeature> reader, SimpleFeature[] array)
            throws NoSuchElementException, IOException, IllegalAttributeException {
        SimpleFeature feature;
        int count = 0;

        try (reader) {
            while (reader.hasNext()) {
                feature = reader.next();

                if (!containsLax(array, feature)) {
                    return false;
                }

                count++;
            }
        }

        return count == array.length;
    }

    /** Like contain but based on match rather than equals */
    boolean containsLax(SimpleFeature[] array, SimpleFeature expected) {
        if (array == null || array.length == 0) {
            return false;
        }

        expected.getFeatureType();
        for (SimpleFeature simpleFeature : array) {
            if (simpleFeature.getID().equals(expected.getID())) {
                return true;
            }

            //            if (match(array[i], expected)) {
            //                return true;
            //            }
        }

        return false;
    }

    /**
     * Search for feature based on AttributeDescriptor.
     *
     * <p>If attributeName is null, we will search by feature.getID()
     *
     * <p>The provided reader will be closed by this operations.
     *
     * @param reader reader to search through
     * @param attributeName attributeName, or null for featureID
     * @param value value to match
     * @return Feature
     * @throws NoSuchElementException if a match could not be found
     * @throws IOException We could not use reader
     * @throws IllegalAttributeException if attributeName did not match schema
     */
    SimpleFeature findFeature(
            FeatureReader<SimpleFeatureType, SimpleFeature> reader, String attributeName, Object value)
            throws NoSuchElementException, IOException, IllegalAttributeException {
        SimpleFeature f;

        try (reader) {
            while (reader.hasNext()) {
                f = reader.next();

                if (attributeName == null) {
                    if (value.equals(f.getID())) {
                        return f;
                    }
                } else {
                    if (value.equals(f.getAttribute(attributeName))) {
                        return f;
                    }
                }
            }
        }

        if (attributeName == null) {
            throw new NoSuchElementException("No match for FID=" + value);
        } else {
            throw new NoSuchElementException("No match for " + attributeName + "=" + value);
        }
    }

    /**
     * Ensure readers contents match those in the feature array
     *
     * <p>Implemented using match on attribute types, not feature id
     */
    void assertMatched(SimpleFeature[] array, FeatureReader<SimpleFeatureType, SimpleFeature> reader) throws Exception {
        SimpleFeature feature;
        int count = 0;

        try (reader) {
            while (reader.hasNext()) {
                feature = reader.next();
                assertMatch(array, feature);
                count++;
            }
        }

        assertEquals("array not matched by reader", array.length, count);
    }

    void assertMatch(SimpleFeature[] array, SimpleFeature feature) {
        assertNotNull(array);
        assertNotEquals(array.length, 0);

        for (SimpleFeature simpleFeature : array) {
            if (simpleFeature.getID().equals(feature.getID())) {
                return;
            }

            //            if (match(array[i], feature)) {
            //                return;
            //            }
        }

        // System.out.println("not found:" + feature);
        fail("array has no match for " + feature);
    }

    /** Compare based on attributes not getID allows comparison of Diff contents */
    boolean match(SimpleFeature expected, SimpleFeature actual) {
        SimpleFeatureType type = expected.getFeatureType();

        for (int i = 0; i < type.getAttributeCount(); i++) {
            Object av = actual.getAttribute(i);
            Object ev = expected.getAttribute(i);

            if (av == null && ev != null) {
                return false;
            } else if (ev == null && av != null) {
                return false;
            } else if (!av.equals(ev)) {
                return false;
            }
        }

        return true;
    }

    @Test
    public void testGeneralization() throws Exception {
        SimpleFeatureSource fs = dataStore.getFeatureSource(tname("lake"));

        assumeTrue(fs.getSupportedHints().contains(Hints.GEOMETRY_GENERALIZATION));

        SimpleFeatureCollection fColl = fs.getFeatures();
        Geometry original = null;
        try (SimpleFeatureIterator iterator = fColl.features()) {
            if (iterator.hasNext()) {
                original = (Geometry) iterator.next().getDefaultGeometry();
            }
        }
        double width = original.getEnvelope().getEnvelopeInternal().getWidth();

        Query query = new Query();
        Hints hints = new Hints(Hints.GEOMETRY_GENERALIZATION, width / 2);
        query.setHints(hints);

        Geometry generalized = null;
        fColl = fs.getFeatures(query);
        try (SimpleFeatureIterator iterator = fColl.features()) {
            if (iterator.hasNext()) {
                generalized = (Geometry) iterator.next().getDefaultGeometry();
            }
        }
        assertTrue(original.getNumPoints() >= generalized.getNumPoints());
    }

    @Test
    public void testSimplification() throws Exception {
        SimpleFeatureSource fs = dataStore.getFeatureSource(tname("road"));

        assumeTrue(fs.getSupportedHints().contains(Hints.GEOMETRY_SIMPLIFICATION));

        SimpleFeatureCollection fColl = fs.getFeatures();
        Geometry original = null;
        try (SimpleFeatureIterator iterator = fColl.features()) {
            if (iterator.hasNext()) {
                original = (Geometry) iterator.next().getDefaultGeometry();
            }
        }
        double width = original.getEnvelope().getEnvelopeInternal().getWidth();

        Query query = new Query();
        Hints hints = new Hints(Hints.GEOMETRY_SIMPLIFICATION, width / 2);
        query.setHints(hints);

        Geometry simplified = null;
        fColl = fs.getFeatures(query);
        try (SimpleFeatureIterator iterator = fColl.features()) {
            if (iterator.hasNext()) simplified = (Geometry) iterator.next().getDefaultGeometry();
        }
        assertTrue(original.getNumPoints() >= simplified.getNumPoints());
    }

    @Override
    protected Map<String, Object> createDataStoreFactoryParams() throws Exception {
        Map<String, Object> params = super.createDataStoreFactoryParams();
        // This test expects the write to happen right away. Disable buffering.
        params.put(JDBCDataStoreFactory.BATCH_INSERT_SIZE.key, 1);
        return params;
    }
}
