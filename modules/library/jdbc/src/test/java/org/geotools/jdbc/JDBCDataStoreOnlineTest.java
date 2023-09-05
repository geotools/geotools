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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import org.apache.commons.lang3.RandomStringUtils;
import org.geotools.api.data.FeatureReader;
import org.geotools.api.data.FeatureWriter;
import org.geotools.api.data.Query;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.data.Transaction;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.feature.type.AttributeType;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.NameImpl;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.CRS;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.WKTReader;

public abstract class JDBCDataStoreOnlineTest extends JDBCTestSupport {
    @Test
    public void testGetNames() throws IOException {
        String[] typeNames = dataStore.getTypeNames();
        assertTrue(new HashSet<>(Arrays.asList(typeNames)).contains(tname("ft1")));
    }

    @Test
    public void testGetSchema() throws Exception {
        SimpleFeatureType ft1 = dataStore.getSchema(tname("ft1"));
        assertNotNull(ft1);

        assertNotNull(ft1.getDescriptor(aname("geometry")));
        assertNotNull(ft1.getDescriptor(aname("intProperty")));
        assertNotNull(ft1.getDescriptor(aname("doubleProperty")));
        assertNotNull(ft1.getDescriptor(aname("stringProperty")));

        assertTrue(
                Geometry.class.isAssignableFrom(
                        ft1.getDescriptor(aname("geometry")).getType().getBinding()));
        assertTrue(
                Number.class.isAssignableFrom(
                        ft1.getDescriptor(aname("intProperty")).getType().getBinding()));
        assertEquals(
                Double.class, ft1.getDescriptor(aname("doubleProperty")).getType().getBinding());
        assertEquals(
                String.class, ft1.getDescriptor(aname("stringProperty")).getType().getBinding());
    }

    @Test
    @SuppressWarnings("PMD.EmptyControlStatement")
    public void testCreateSchema() throws Exception {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName(tname("ft2"));
        builder.setNamespaceURI(dataStore.getNamespaceURI());
        builder.setCRS(CRS.decode("EPSG:4326"));
        builder.add(aname("geometry"), Geometry.class);
        builder.add(aname("intProperty"), Integer.class);
        builder.add(aname("dateProperty"), Date.class);

        SimpleFeatureType featureType = builder.buildFeatureType();
        dataStore.createSchema(featureType);

        SimpleFeatureType ft2 = dataStore.getSchema(tname("ft2"));

        // JD: making the comparison a bit more lax
        // asertEquals(ft2,featureType);
        assertEqualsLax(ft2, featureType);
        // TODO: we should also check the crs as well
        // assertTrue(CRS.equalsIgnoreMetadata(featureType.getCoordinateReferenceSystem(),
        //        ft2.getCoordinateReferenceSystem()));

        // GEOT-2031
        assertNotSame(ft2, featureType);

        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * FROM ");

        if (dataStore.getDatabaseSchema() != null) {
            dataStore.getSQLDialect().encodeSchemaName(dataStore.getDatabaseSchema(), sql);
            sql.append(".");
        }

        dataStore.getSQLDialect().encodeTableName("ft2", sql);

        try (Connection cx = dataStore.createConnection();
                Statement st = cx.createStatement();
                ResultSet rs = st.executeQuery(sql.toString())) {
        } catch (SQLException e) {
            throw e;
        }
    }

    @Test
    public void testCreateSchemaWithConstraints() throws Exception {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName(tname("ft2"));
        builder.setNamespaceURI(dataStore.getNamespaceURI());
        builder.setCRS(CRS.decode("EPSG:4326"));
        builder.add(aname("geometry"), Geometry.class);
        builder.nillable(false).add(aname("intProperty"), Integer.class);

        builder.length(5).add(aname("stringProperty"), String.class);

        SimpleFeatureType featureType = builder.buildFeatureType();
        dataStore.createSchema(featureType);

        dataStore.getSchema(tname("ft2"));
        // assertEquals(ft2, featureType);

        // grab a writer
        try (FeatureWriter<SimpleFeatureType, SimpleFeature> w =
                dataStore.getFeatureWriter(tname("ft2"), Transaction.AUTO_COMMIT)) {
            w.hasNext();

            SimpleFeature f = w.next();
            f.setAttribute(1, Integer.valueOf(0));
            f.setAttribute(2, "hello");
            w.write();

            w.hasNext();
            f = w.next();
            f.setAttribute(1, null);
            try {
                w.write();
                fail("null value for intProperty should have failed");
            } catch (Exception e) {
            }

            f.setAttribute(1, Integer.valueOf(1));
            f.setAttribute(2, "hello!");
            try {
                w.write();
                fail("string greather than 5 chars should have failed");
            } catch (Exception e) {
            }
        }
    }

    @Test
    public void testCreateSchemaWithNativeTypename() throws Exception {
        assertLargeText(b -> b.userData(JDBCDataStore.JDBC_NATIVE_TYPENAME, getCLOBTypeName()));
    }

    /**
     * Used by testCreateSchemaWithNativeTypename. Allows database specific overrides, defaults to
     * <code>CLOB</code>
     */
    protected String getCLOBTypeName() {
        return "CLOB";
    }

    @Test
    public void testCreateSchemaWithNativeType() throws Exception {
        assertLargeText(b -> b.userData(JDBCDataStore.JDBC_NATIVE_TYPE, Types.CLOB));
    }

    private void assertLargeText(Consumer<SimpleFeatureTypeBuilder> stringCustomizer)
            throws FactoryException, IOException {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        String typeName = tname("ft2");
        builder.setName(typeName);
        builder.setNamespaceURI(dataStore.getNamespaceURI());
        builder.setCRS(CRS.decode("EPSG:4326"));
        builder.add(aname("geometry"), Geometry.class);
        builder.nillable(false).add(aname("intProperty"), Integer.class);
        // database can process this name (also tried going from Type.CLOB to a name,
        // but for example postgresql does have a mapping for it
        String stringProperty = aname("stringProperty");
        stringCustomizer.accept(builder);
        builder.add(stringProperty, String.class);

        SimpleFeatureType featureType = builder.buildFeatureType();
        dataStore.createSchema(featureType);

        // cannot get a consistent type response from different databases, but it should be able
        // to hold a very large string without cutting it
        String largeString = RandomStringUtils.random(Short.MAX_VALUE + 1, true, false);
        try (FeatureWriter<SimpleFeatureType, SimpleFeature> w =
                dataStore.getFeatureWriter(typeName, Transaction.AUTO_COMMIT)) {
            w.hasNext();

            SimpleFeature f = w.next();
            f.setAttribute(1, 123);
            f.setAttribute(2, largeString);
            w.write();
        }

        // table was just created, it only has one feature inside, no need for a filter
        SimpleFeatureCollection fc = dataStore.getFeatureSource(typeName).getFeatures();
        SimpleFeature test = DataUtilities.first(fc);
        assertEquals(largeString, test.getAttribute(stringProperty));
    }

    @Test
    public void testRemoveSchema() throws Exception {
        SimpleFeatureType ft = dataStore.getSchema(tname("ft1"));
        assertNotNull(ft);

        dataStore.removeSchema(tname("ft1"));
        try {
            dataStore.getSchema(tname("ft1"));
            fail("getSchema() should fail if table was deleted");
        } catch (Exception e) {
        }
    }

    @Test
    public void testSimpleIndex() throws Exception {
        SimpleFeatureType ft = dataStore.getSchema(tname("ft1"));
        assertNotNull(ft);

        // check initial status
        String ft1TypeName = ft.getTypeName();
        List<Index> indexes = dataStore.getIndexes(ft1TypeName);
        assertNotNull(indexes);
        final int initialSize = indexes.size();

        // create index
        String indexName = "ft1_str_index";
        Index stringIndex = new Index(ft1TypeName, indexName, false, aname("stringProperty"));
        dataStore.createIndex(stringIndex);

        // check the index has been created
        indexes = dataStore.getIndexes(ft1TypeName);
        assertEquals(initialSize + 1, indexes.size());
        for (Index index : indexes) {
            assertEquals(ft1TypeName, index.getTypeName());
            if (index.getIndexName().equals(indexName)) {
                List<String> attributes = index.getAttributes();
                assertEquals(1, attributes.size());
                assertEquals(aname("stringProperty"), attributes.get(0));
                assertFalse(index.isUnique());
            }
        }

        // drop it
        dataStore.dropIndex(ft1TypeName, indexName);
        indexes = dataStore.getIndexes(ft1TypeName);
        assertEquals(initialSize, indexes.size());
        for (Index index : indexes) {
            assertEquals(ft1TypeName, index.getTypeName());
            if (index.getIndexName().equals(indexName)) {
                fail("the index has not been removed");
            }
        }
    }

    @Test
    public void testMultiColumnIndex() throws Exception {
        SimpleFeatureType ft = dataStore.getSchema(tname("ft1"));
        assertNotNull(ft);

        // check initial status
        String ft1TypeName = ft.getTypeName();
        List<Index> indexes = dataStore.getIndexes(ft1TypeName);
        assertNotNull(indexes);
        final int initialSize = indexes.size();

        // create index
        String indexName = "ft1_str_index";
        Index stringIndex =
                new Index(
                        ft1TypeName,
                        indexName,
                        false,
                        aname("stringProperty"),
                        aname("intProperty"));
        dataStore.createIndex(stringIndex);

        // check the index has been created
        indexes = dataStore.getIndexes(ft1TypeName);
        assertEquals(initialSize + 1, indexes.size());
        for (Index index : indexes) {
            assertEquals(ft1TypeName, index.getTypeName());
            if (index.getIndexName().equals(indexName)) {
                List<String> attributes = index.getAttributes();
                assertEquals(2, attributes.size());
                assertEquals(aname("stringProperty"), attributes.get(0));
                assertEquals(aname("intProperty"), attributes.get(1));
            }
        }

        // drop it
        dataStore.dropIndex(ft1TypeName, indexName);
        indexes = dataStore.getIndexes(ft1TypeName);
        assertEquals(initialSize, indexes.size());
        for (Index index : indexes) {
            assertEquals(ft1TypeName, index.getTypeName());
            if (index.getIndexName().equals(indexName)) {
                fail("the index has not been removed");
            }
        }
    }

    @Test
    public void testCreateSchemaUTMCRS() throws Exception {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName(tname("ft2"));
        builder.setNamespaceURI(dataStore.getNamespaceURI());
        builder.setCRS(getUTMCRS());
        builder.add(aname("geometry"), Point.class);
        builder.add(aname("intProperty"), Integer.class);
        builder.add(aname("stringProperty"), String.class);

        SimpleFeatureType featureType = builder.buildFeatureType();
        dataStore.createSchema(featureType);

        SimpleFeatureType ft2 = dataStore.getSchema(tname("ft2"));
        assertNotNull(ft2);

        try (FeatureWriter w = dataStore.getFeatureWriter(tname("ft2"), Transaction.AUTO_COMMIT)) {
            w.hasNext();

            // write out a feature with a geometry in the srs, basically accomodate databases that
            // have
            // to query the first feature in order to get the srs for the feature type
            SimpleFeature f = (SimpleFeature) w.next();

            Geometry g = new WKTReader().read("POINT(593493 4914730)");
            g.setSRID(26713);

            f.setAttribute(0, g);
            f.setAttribute(1, Integer.valueOf(0));
            f.setAttribute(2, "hello");
            w.write();
        }

        // clear out the feature type cache
        dataStore.getEntry(new NameImpl(dataStore.getNamespaceURI(), tname("ft2"))).dispose();
        ft2 = dataStore.getSchema(tname("ft2"));
        assertTrue(CRS.equalsIgnoreMetadata(getUTMCRS(), ft2.getCoordinateReferenceSystem()));
    }

    /**
     * Allows subclasses to use a axis order specific version of it (the outer is always east/north,
     * but the wrapped geographic CRS is order dependent)
     */
    protected CoordinateReferenceSystem getUTMCRS() throws FactoryException {
        return decodeEPSG(26713);
    }

    @Test
    public void testCreateSchemaFidColumn() throws Exception {
        // test a case where the feature type we are creating contains a column named "fid"
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName(tname("ft2"));
        builder.setNamespaceURI(dataStore.getNamespaceURI());
        builder.setCRS(getUTMCRS());
        builder.add(aname("geometry"), Point.class);
        builder.add(aname("intProperty"), Integer.class);
        builder.add(aname("stringProperty"), String.class);
        builder.add(aname("fid"), String.class);

        SimpleFeatureType featureType = builder.buildFeatureType();
        dataStore.createSchema(featureType);

        SimpleFeatureType ft2 = dataStore.getSchema(tname("ft2"));
        assertNotNull(ft2.getDescriptor(aname("fid")));
    }

    void assertEqualsLax(SimpleFeatureType e, SimpleFeatureType a) {
        if (e.equals(a)) {
            return;
        }

        // do a lax check
        assertEquals(e.getAttributeCount(), a.getAttributeCount());
        for (int i = 0; i < e.getAttributeCount(); i++) {
            AttributeDescriptor att1 = e.getDescriptor(i);
            AttributeDescriptor att2 = a.getDescriptor(i);

            assertEquals(att1.getName(), att2.getName());
            assertEquals(att1.getMinOccurs(), att2.getMinOccurs());
            assertEquals(att1.getMaxOccurs(), att2.getMaxOccurs());
            assertEquals(att1.isNillable(), att2.isNillable());
            assertEquals(att1.getDefaultValue(), att2.getDefaultValue());

            AttributeType t1 = att1.getType();
            AttributeType t2 = att2.getType();

            assertEquals(t1.getName(), t2.getName());
            assertEquals(t1.getDescription(), t2.getDescription());
            assertEquals(t1.getRestrictions(), t2.getRestrictions());

            // be a bit lax on type mappings
            if (!t1.getBinding().equals(t2.getBinding())) {
                if (Number.class.isAssignableFrom(t1.getBinding())) {
                    assertTrue(Number.class.isAssignableFrom(t2.getBinding()));
                }
                if (Date.class.isAssignableFrom(t2.getBinding())) {
                    assertTrue(Date.class.isAssignableFrom(t2.getBinding()));
                }
            }
        }
    }

    @Test
    public void testGetFeatureSource() throws Exception {
        SimpleFeatureSource featureSource = dataStore.getFeatureSource(tname("ft1"));
        assertNotNull(featureSource);
    }

    @Test
    public void testGetFeatureReader() throws Exception {
        final GeometryFactory gf = dataStore.getGeometryFactory();

        Query query = new Query(tname("ft1"));
        try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                dataStore.getFeatureReader(query, Transaction.AUTO_COMMIT)) {

            assertFeatureReader(
                    0,
                    3,
                    reader,
                    new SimpleFeatureAssertion() {
                        @Override
                        public int toIndex(SimpleFeature feature) {
                            return ((Number) feature.getAttribute(aname("intProperty"))).intValue();
                        }

                        @Override
                        public void check(int index, SimpleFeature feature) {
                            assertEquals(4, feature.getAttributeCount());
                            Point p = gf.createPoint(new Coordinate(index, index));
                            assertTrue(
                                    p.equalsExact(
                                            (Geometry) feature.getAttribute(aname("geometry"))));

                            Number ip = (Number) feature.getAttribute(aname("intProperty"));
                            assertEquals(index, ip.intValue());
                        }
                    });
        }

        query.setPropertyNames(aname("intProperty"));
        try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                dataStore.getFeatureReader(query, Transaction.AUTO_COMMIT)) {
            for (int i = 0; i < 3; i++) {
                assertTrue(reader.hasNext());

                SimpleFeature feature = reader.next();
                assertEquals(1, feature.getAttributeCount());
            }

            assertFalse(reader.hasNext());
        }

        FilterFactory ff = dataStore.getFilterFactory();
        Filter f = ff.equals(ff.property(aname("intProperty")), ff.literal(1));
        query.setFilter(f);

        try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                dataStore.getFeatureReader(query, Transaction.AUTO_COMMIT)) {
            for (int i = 0; i < 1; i++) {
                assertTrue(reader.hasNext());
                reader.next();
            }

            assertFalse(reader.hasNext());
        }
    }

    @Test
    public void testGetFeatureWriter() throws IOException {
        try (FeatureWriter<SimpleFeatureType, SimpleFeature> writer =
                dataStore.getFeatureWriter(tname("ft1"), Transaction.AUTO_COMMIT)) {
            while (writer.hasNext()) {
                SimpleFeature feature = writer.next();
                feature.setAttribute(aname("stringProperty"), "foo");
                writer.write();
            }
        }

        Query query = new Query(tname("ft1"));
        try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                dataStore.getFeatureReader(query, Transaction.AUTO_COMMIT)) {
            assertTrue(reader.hasNext());

            while (reader.hasNext()) {
                SimpleFeature feature = reader.next();
                assertEquals("foo", feature.getAttribute(aname("stringProperty")));
            }
        }
    }

    @Test
    public void testGetFeatureWriterWithFilter() throws IOException {
        FilterFactory ff = dataStore.getFilterFactory();

        Filter f = ff.equals(ff.property(aname("intProperty")), ff.literal(100));
        SimpleFeatureCollection features = dataStore.getFeatureSource(tname("ft1")).getFeatures(f);
        assertEquals(0, features.size());

        f = ff.equals(ff.property(aname("intProperty")), ff.literal(1));

        try (FeatureWriter<SimpleFeatureType, SimpleFeature> writer =
                dataStore.getFeatureWriter(tname("ft1"), f, Transaction.AUTO_COMMIT)) {
            while (writer.hasNext()) {
                SimpleFeature feature = writer.next();
                feature.setAttribute(aname("intProperty"), Integer.valueOf(100));
                writer.write();
            }
        }

        f = ff.equals(ff.property(aname("intProperty")), ff.literal(100));
        features = dataStore.getFeatureSource(tname("ft1")).getFeatures(f);
        assertEquals(1, features.size());
    }

    @Test
    public void testGetFeatureWriterAppend() throws IOException {
        try (FeatureWriter<SimpleFeatureType, SimpleFeature> writer =
                dataStore.getFeatureWriterAppend(tname("ft1"), Transaction.AUTO_COMMIT)) {
            for (int i = 3; i < 6; i++) {
                SimpleFeature feature = writer.next();
                feature.setAttribute(aname("intProperty"), Integer.valueOf(i));
                writer.write();
            }
        }

        SimpleFeatureCollection features = dataStore.getFeatureSource(tname("ft1")).getFeatures();
        assertEquals(6, features.size());
    }

    @Override
    protected Map<String, Object> createDataStoreFactoryParams() throws Exception {
        Map<String, Object> params = super.createDataStoreFactoryParams();
        // This test expects the write to happen right away. Disable buffering.
        params.put(JDBCDataStoreFactory.BATCH_INSERT_SIZE.key, 1);
        return params;
    }
}
