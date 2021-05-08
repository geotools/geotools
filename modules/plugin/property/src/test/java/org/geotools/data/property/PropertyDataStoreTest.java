/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.property;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotEquals;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.data.store.ContentFeatureCollection;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.NameImpl;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.geometry.jts.WKTReader2;
import org.geotools.util.factory.Hints;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.CoordinateSequenceFactory;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.locationtech.jts.geom.impl.PackedCoordinateSequence;
import org.locationtech.jts.geom.impl.PackedCoordinateSequenceFactory;
import org.opengis.feature.IllegalAttributeException;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.identity.FeatureId;
import org.opengis.filter.sort.SortOrder;

/**
 * Test functioning of PropertyDataStore.
 *
 * @author Jody Garnett, Refractions Research Inc.
 */
public class PropertyDataStoreTest {
    private PropertyDataStore store;
    static FilterFactory2 ff = (FilterFactory2) CommonFactoryFinder.getFilterFactory(null);

    @Before
    public void setUp() throws Exception {
        File dir = new File(".", "propertyTestData");
        dir.mkdir();

        File file = new File(dir, "road.properties");
        if (file.exists()) {
            file.delete();
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("_=id:Integer,name:String,geom:Point");
            writer.newLine();
            writer.write("fid1=1|jody|POINT(0 0)");
            writer.newLine();
            writer.write("fid2=2|brent|POINT(1 1)");
            writer.newLine();
            writer.write("fid3=3|dave|POINT(2 2)");
            writer.newLine();
            writer.write("fid4=4|justin|POINT(3 3)");
            writer.newLine();
            writer.write("fid5=5||");
        }

        file = new File(dir, "dots.in.name.properties");
        if (file.exists()) {
            file.delete();
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("_=id:Integer,name:String");
            writer.newLine();
            writer.write("fid1=1|jody");
            writer.newLine();
            writer.write("fid2=2|brent");
            writer.newLine();
            writer.write("fid3=3|dave");
            writer.newLine();
            writer.write("fid4=4|justin");
        }

        file = new File(dir, "multiline.properties");
        if (file.exists()) {
            file.delete();
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("_=id:Integer,name:String");
            writer.newLine();
            writer.write("fid1=1|jody \\");
            writer.newLine();
            writer.write("       garnett");
            writer.newLine();
            writer.write("fid2=2|brent");
            writer.newLine();
            writer.write("fid3=3|dave");
            writer.newLine();
            writer.write("fid4=4|justin\\\n");
        }

        file = new File(dir, "table.properties");
        if (file.exists()) {
            file.delete();
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("_=description:String,name:String");
            writer.newLine();
            writer.write("GenericEntity.f004=description-f004|name-f004");
            writer.newLine();
            writer.write("GenericEntity.f003=description-f003|<null>");
            writer.newLine();
            writer.write("GenericEntity.f007=description-f007|");
            writer.newLine();
            writer.write("  GenericEntity.f009=description-f009| ");
            writer.newLine();
        }

        store = new PropertyDataStore(dir, "propertyTestData");
    }

    @After
    public void tearDown() throws Exception {
        if (store != null) {
            store.dispose();
        }

        File dir = new File("propertyTestData");
        File[] list = dir.listFiles();
        for (File file : list) {
            file.delete();
        }
        dir.delete();
    }

    @Test
    public void testGetNames() throws IOException {
        String[] names = store.getTypeNames();
        Arrays.sort(names);
        Assert.assertEquals(4, names.length);
        Assert.assertEquals("dots.in.name", names[0]);
        Assert.assertEquals("multiline", names[1]);
        Assert.assertEquals("road", names[2]);
        Assert.assertEquals("table", names[3]);
    }

    @Test
    public void testGetSchema() throws IOException {
        SimpleFeatureType type = store.getSchema("road");
        Assert.assertNotNull(type);
        Assert.assertEquals("road", type.getTypeName());
        Assert.assertEquals("propertyTestData", type.getName().getNamespaceURI().toString());
        Assert.assertEquals(3, type.getAttributeCount());

        AttributeDescriptor id = type.getDescriptor(0);
        AttributeDescriptor name = type.getDescriptor(1);

        Assert.assertEquals("id", id.getLocalName());
        Assert.assertEquals("class java.lang.Integer", id.getType().getBinding().toString());

        Assert.assertEquals("name", name.getLocalName());
        Assert.assertEquals("class java.lang.String", name.getType().getBinding().toString());
    }

    @Test
    public void testGetFeaturesFeatureTypeFilterTransaction1() throws Exception {
        Query roadQuery = new Query("road");
        try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                store.getFeatureReader(roadQuery, Transaction.AUTO_COMMIT)) {
            int count = 0;
            while (reader.hasNext()) {
                reader.next();
                count++;
            }
            Assert.assertEquals(5, count);
        }

        Filter selectFid1 = ff.id(Collections.singleton(ff.featureId("fid1")));
        try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                store.getFeatureReader(new Query("road", selectFid1), Transaction.AUTO_COMMIT)) {
            Assert.assertEquals(1, count(reader));
        }

        try (Transaction transaction = new DefaultTransaction()) {
            try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                    store.getFeatureReader(roadQuery, transaction)) {
                Assert.assertEquals(5, count(reader));
            }

            try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                    store.getFeatureReader(roadQuery, transaction)) {
                List<String> list = new ArrayList<>();
                while (reader.hasNext()) {
                    list.add(reader.next().getID());
                }
                Assert.assertEquals("[fid1, fid2, fid3, fid4, fid5]", list.toString());
            }
        }
    }

    /*
     * Test for  FeatureReader<SimpleFeatureType, SimpleFeature> getFeatureReader(String)
     */
    @Test
    public void testGetFeatureReaderString()
            throws NoSuchElementException, IOException, IllegalAttributeException {
        Query query = new Query("road");
        int count = 0;
        try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                store.getFeatureReader(query, Transaction.AUTO_COMMIT)) {
            while (reader.hasNext()) {
                reader.next();
                count++;
            }
        }
        Assert.assertEquals(5, count);
    }

    private int count(FeatureReader<SimpleFeatureType, SimpleFeature> reader) throws Exception {
        int count = 0;
        try {
            while (reader.hasNext()) {
                reader.next();
                count++;
            }
        } finally {
            reader.close();
        }
        return count;
    }

    private int count(String typeName) throws Exception {
        Query query = new Query("road");
        try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                store.getFeatureReader(query, Transaction.AUTO_COMMIT)) {
            return count(reader);
        }
    }

    @Test
    public void testWriterSkipThrough() throws Exception {
        Query query = new Query("road");
        PropertyFeatureStore featureStore = (PropertyFeatureStore) store.getFeatureSource("road");
        try (PropertyFeatureWriter writer =
                (PropertyFeatureWriter) featureStore.getWriterInternal(query, 0x01 << 0)) {

            File in = writer.read;
            File out = writer.write;

            int count = 0;
            while (writer.hasNext()) {
                writer.next();
                count++;
            }
            Assert.assertEquals(5, count);
            Assert.assertTrue(in.exists());
            Assert.assertTrue(out.exists());
            Assert.assertTrue(in.exists());
        }

        Assert.assertEquals(5, count("road"));
    }

    @Test
    public void testWriterChangeName() throws Exception {
        Query query = new Query("road");
        PropertyFeatureStore featureStore = (PropertyFeatureStore) store.getFeatureSource("road");
        try (PropertyFeatureWriter writer =
                (PropertyFeatureWriter) featureStore.getWriterInternal(query, 0x01 << 0)) {

            int count = 0;
            while (writer.hasNext()) {
                SimpleFeature f = writer.next();
                f.setAttribute(1, "name " + (count + 1));
                writer.write();
                count++;
            }
            Assert.assertEquals(5, count);
        }

        Assert.assertEquals(5, count("road"));
    }

    @Test
    public void testWriterChangeFirstName() throws Exception {
        Query query = new Query("road");
        PropertyFeatureStore featureStore = (PropertyFeatureStore) store.getFeatureSource("road");
        try (PropertyFeatureWriter writer =
                (PropertyFeatureWriter) featureStore.getWriterInternal(query, 0x01 << 0)) {

            SimpleFeature f = writer.next();
            f.setAttribute(1, "changed");
            writer.write();
        }
        Assert.assertEquals(5, count("road"));
    }

    @Test
    public void testWriterChangeLastName() throws Exception {
        Query query = new Query("road");
        PropertyFeatureStore featureStore = (PropertyFeatureStore) store.getFeatureSource("road");
        try (PropertyFeatureWriter writer =
                (PropertyFeatureWriter) featureStore.getWriterInternal(query, 0x01 << 0)) {

            writer.next();
            writer.next();
            writer.next();
            SimpleFeature f = writer.next();
            f.setAttribute(1, "changed");
            writer.write();
        }
        Assert.assertEquals(5, count("road"));
    }

    @Test
    public void testWriterChangeAppend() throws Exception {
        Query query = new Query("road");
        PropertyFeatureStore featureStore = (PropertyFeatureStore) store.getFeatureSource("road");
        try (PropertyFeatureWriter writer =
                (PropertyFeatureWriter) featureStore.getWriterInternal(query, 0x01 << 0)) {

            writer.next();
            writer.next();
            writer.next();
            writer.next();
            writer.next();
            Assert.assertFalse(writer.hasNext());
            SimpleFeature f = writer.next();
            Assert.assertNotNull(f);
            f.setAttribute(0, Integer.valueOf(-1));
            f.setAttribute(1, "new");
            writer.write();
        }
        Assert.assertEquals(6, count("road"));
    }

    @Test
    public void testWriterAppendLastNull() throws Exception {
        try (FeatureWriter<SimpleFeatureType, SimpleFeature> writer =
                store.getFeatureWriterAppend("road", Transaction.AUTO_COMMIT)) {

            Assert.assertFalse(writer.hasNext());
            SimpleFeature f = writer.next();
            Assert.assertNotNull(f);
            f.setAttribute(0, Integer.valueOf(-1));
            f.setAttribute(1, null); // this made the datastore break
            writer.write();
        }
        Assert.assertEquals(6, count("road"));
    }

    @Test
    public void testWriterChangeRemoveFirst() throws Exception {
        Query query = new Query("road");
        PropertyFeatureStore featureStore = (PropertyFeatureStore) store.getFeatureSource("road");
        try (PropertyFeatureWriter writer =
                (PropertyFeatureWriter) featureStore.getWriterInternal(query, 0x01 << 0)) {

            writer.next();
            writer.remove();
        }
        Assert.assertEquals(4, count("road"));
    }

    @Test
    public void testWriterChangeRemoveLast() throws Exception {
        Query query = new Query("road");
        PropertyFeatureStore featureStore = (PropertyFeatureStore) store.getFeatureSource("road");
        try (PropertyFeatureWriter writer =
                (PropertyFeatureWriter) featureStore.getWriterInternal(query, 0x01 << 0)) {

            writer.next();
            writer.next();
            writer.next();
            writer.remove();
        }
        Assert.assertEquals(4, count("road"));
    }

    @Test
    public void testWriterChangeRemoveAppend() throws Exception {
        Query query = new Query("road");
        PropertyFeatureStore featureStore = (PropertyFeatureStore) store.getFeatureSource("road");
        try (PropertyFeatureWriter writer =
                (PropertyFeatureWriter) featureStore.getWriterInternal(query, 0x01 << 0)) {

            writer.next();
            writer.next();
            writer.next();
            writer.next();
            writer.next();

            Assert.assertFalse(writer.hasNext());
            SimpleFeature f = writer.next();
            Assert.assertNotNull(f);
            f.setAttribute(0, Integer.valueOf(-1));
            f.setAttribute(1, "new");
            writer.remove();
        }
        Assert.assertEquals(5, count("road"));
    }

    @Test
    public void testWriterChangeIgnoreAppend() throws Exception {
        Query query = new Query("road");
        PropertyFeatureStore featureStore = (PropertyFeatureStore) store.getFeatureSource("road");
        try (PropertyFeatureWriter writer =
                (PropertyFeatureWriter) featureStore.getWriterInternal(query, 0x01 << 0)) {

            writer.next();
            writer.next();
            writer.next();
            writer.next();
            writer.next();
            Assert.assertFalse(writer.hasNext());
            SimpleFeature f = writer.next();
            Assert.assertNotNull(f);
            f.setAttribute(0, Integer.valueOf(-1));
            f.setAttribute(1, "new");
        }
        Assert.assertEquals(5, count("road"));
    }

    @Test
    public void testGetFeatureSource() throws Exception {
        SimpleFeatureSource road = store.getFeatureSource("road");
        SimpleFeatureCollection features = road.getFeatures();
        List<String> list = new ArrayList<>();
        try (SimpleFeatureIterator reader = features.features()) {
            while (reader.hasNext()) {
                SimpleFeature next = reader.next();
                list.add(next.getID());
            }
        }
        Assert.assertEquals("[fid1, fid2, fid3, fid4, fid5]", list.toString());

        int count = road.getCount(Query.ALL);
        Assert.assertEquals(5, count);

        Assert.assertFalse(road.getBounds(Query.ALL).isNull());
        Assert.assertEquals(5, features.size());

        Assert.assertFalse(features.getBounds().isNull());
        Assert.assertEquals(5, features.size());
    }

    /**
     * In response to <a href="https://jira.codehaus.org/browse/GEOT-1409">GEOT-1409 Property
     * datastore ruins the property file if a string attribute has newlines</a>.
     */
    @Test
    public void testMultiLine() throws Exception {
        SimpleFeatureSource road = store.getFeatureSource("multiline");
        FeatureId fid1 = ff.featureId("fid1");
        Filter select = ff.id(Collections.singleton(fid1));
        SimpleFeatureCollection featureCollection = road.getFeatures(select);
        featureCollection.accepts(
                f -> {
                    SimpleFeature feature = (SimpleFeature) f;
                    String name = (String) feature.getAttribute("name");
                    Assert.assertEquals("jody \ngarnett", name);
                },
                null);
    }

    @Test
    public void testGeometryFactoryHint() throws Exception {
        final GeometryFactory gf =
                new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING), 1234);
        SimpleFeatureSource road = store.getFeatureSource("road");
        FeatureId fid1 = ff.featureId("fid1");
        Filter select = ff.id(Collections.singleton(fid1));
        Query q = new Query(road.getSchema().getTypeName(), select);
        q.getHints().put(Hints.JTS_GEOMETRY_FACTORY, gf);
        SimpleFeatureCollection featureCollection = road.getFeatures(q);
        featureCollection.accepts(
                f -> {
                    SimpleFeature feature = (SimpleFeature) f;
                    Geometry g = (Geometry) feature.getDefaultGeometry();
                    Assert.assertEquals(1234, g.getSRID());
                },
                null);
    }

    @Test
    public void testCoordinateSequenceHint() throws Exception {
        CoordinateSequenceFactory csFactory = new PackedCoordinateSequenceFactory();
        SimpleFeatureSource road = store.getFeatureSource("road");
        FeatureId fid1 = ff.featureId("fid1");
        Filter select = ff.id(Collections.singleton(fid1));
        Query q = new Query(road.getSchema().getTypeName(), select);
        q.getHints().put(Hints.JTS_COORDINATE_SEQUENCE_FACTORY, csFactory);
        SimpleFeatureCollection featureCollection = road.getFeatures(q);
        featureCollection.accepts(
                f -> {
                    SimpleFeature feature = (SimpleFeature) f;
                    Point p = (Point) feature.getDefaultGeometry();
                    assertThat(
                            p.getCoordinateSequence(), instanceOf(PackedCoordinateSequence.class));
                },
                null);
    }

    /**
     * In response to <a href="http://jira.codehaus.org/browse/GEOT-3540">GEOT-3540
     * PropertyDataStore doesn't support empty trailing spaces</a>.
     *
     * <p>Table with no geoemtry, containing null and empty strings at end of line
     */
    @Test
    public void testTable() throws Exception {
        SimpleFeatureSource table = store.getFeatureSource("table");
        // GenericEntity.f004=description-f004|name-f004
        FeatureId fid1 = ff.featureId("GenericEntity.f004");
        Filter select = ff.id(Collections.singleton(fid1));
        SimpleFeatureCollection featureCollection = table.getFeatures(select);
        featureCollection.accepts(
                f -> {
                    SimpleFeature feature = (SimpleFeature) f;
                    String name = (String) feature.getAttribute("name");
                    Assert.assertEquals("name-f004", name);
                },
                null);
        // GenericEntity.f003=description-f003|<null>
        fid1 = ff.featureId("GenericEntity.f003");
        select = ff.id(Collections.singleton(fid1));
        featureCollection = table.getFeatures(select);
        featureCollection.accepts(
                f -> {
                    SimpleFeature feature = (SimpleFeature) f;
                    String name = (String) feature.getAttribute("name");
                    Assert.assertNull("represent null", name);
                },
                null);
        // GenericEntity.f007=description-f007|
        fid1 = ff.featureId("GenericEntity.f007");
        select = ff.id(Collections.singleton(fid1));
        featureCollection = table.getFeatures(select);
        featureCollection.accepts(
                f -> {
                    SimpleFeature feature = (SimpleFeature) f;
                    String name = (String) feature.getAttribute("name");
                    Assert.assertEquals("represent empty string", "", name);
                },
                null);
        // "  GenericEntity.f009=description-f009| "
        fid1 = ff.featureId("GenericEntity.f009");
        select = ff.id(Collections.singleton(fid1));
        featureCollection = table.getFeatures(select);
        featureCollection.accepts(
                f -> {
                    SimpleFeature feature = (SimpleFeature) f;
                    String name = (String) feature.getAttribute("name");
                    Assert.assertEquals("represent empty string", " ", name);
                },
                null);
    }

    @Test
    public void testTransactionIndependence() throws Exception {
        WKTReader2 wkt = new WKTReader2();
        SimpleFeatureType ROAD = store.getSchema("road");
        SimpleFeature chrisFeature =
                SimpleFeatureBuilder.build(
                        ROAD,
                        new Object[] {Integer.valueOf(5), "chris", wkt.read("POINT(6 6)")},
                        "fid5");

        SimpleFeatureStore roadAuto = (SimpleFeatureStore) store.getFeatureSource("road");

        SimpleFeatureStore roadFromClient1 = (SimpleFeatureStore) store.getFeatureSource("road");
        try (Transaction transaction1 = new DefaultTransaction("Transaction Used by Client 1");
                Transaction transaction2 = new DefaultTransaction("Transaction Used by Client 2")) {
            roadFromClient1.setTransaction(transaction1);

            SimpleFeatureStore roadFromClient2 =
                    (SimpleFeatureStore) store.getFeatureSource("road");

            roadFromClient2.setTransaction(transaction2);

            FilterFactory2 ff = (FilterFactory2) CommonFactoryFinder.getFilterFactory(null);
            Filter selectFid1 = ff.id(Collections.singleton(ff.featureId("fid1")));

            // Before we edit everything should be the same
            Assert.assertEquals("auto before", 5, roadAuto.getFeatures().size());
            Assert.assertEquals("client 1 before", 5, roadFromClient1.getFeatures().size());
            Assert.assertEquals("client 2 before", 5, roadFromClient2.getFeatures().size());
            Assert.assertEquals("client 1 before", 5, roadFromClient1.getCount(Query.ALL));
            Assert.assertEquals("client 2 before", 5, roadFromClient2.getCount(Query.ALL));

            ReferencedEnvelope bounds = roadAuto.getFeatures().getBounds();
            ReferencedEnvelope client1Bounds = roadFromClient1.getFeatures().getBounds();
            ReferencedEnvelope client2Bounds = roadFromClient2.getFeatures().getBounds();
            Assert.assertEquals("client 1 before", bounds, client1Bounds);
            Assert.assertEquals("client 2 before", bounds, client2Bounds);

            // Remove Feature with Fid1
            roadFromClient1.removeFeatures(selectFid1); // road1 removes fid1 on t1

            Assert.assertEquals(
                    "auto after client 1 removes fid1", 5, roadAuto.getFeatures().size());
            Assert.assertEquals(
                    "client 1 after client 1 removes fid1",
                    4,
                    roadFromClient1.getFeatures().size());
            Assert.assertEquals(
                    "client 2 after client 1 removes fid1",
                    5,
                    roadFromClient2.getFeatures().size());
            Assert.assertEquals(
                    "client 1 after client 1 removes fid1", 4, roadFromClient1.getCount(Query.ALL));
            Assert.assertEquals(
                    "client 2 after client 1 removes fid1", 5, roadFromClient2.getCount(Query.ALL));

            bounds = roadAuto.getFeatures().getBounds();
            client1Bounds = roadFromClient1.getFeatures().getBounds();
            client2Bounds = roadFromClient2.getFeatures().getBounds();
            assertNotEquals("client 1 after client 1 removes fid1", bounds, client1Bounds);
            Assert.assertEquals("client 2 after client 1 removes fid1", bounds, client2Bounds);

            roadFromClient2.addFeatures(
                    DataUtilities.collection(chrisFeature)); // road2 adds fid5 on t2
            Assert.assertEquals(
                    "auto after client 1 removes fid1 and client 2 adds fid5",
                    5,
                    roadAuto.getFeatures().size());
            Assert.assertEquals(
                    "client 1 after client 1 removes fid1 and client 2 adds fid5",
                    4,
                    roadFromClient1.getFeatures().size());
            Assert.assertEquals(
                    "client 2 after client 1 removes fid1 and client 2 adds fid5",
                    6,
                    roadFromClient2.getFeatures().size());

            bounds = roadAuto.getFeatures().getBounds();
            client1Bounds = roadFromClient1.getFeatures().getBounds();
            client2Bounds = roadFromClient2.getFeatures().getBounds();
            assertNotEquals(
                    "client 1 after client 1 removes fid1 and client 2 adds fid5",
                    bounds,
                    client1Bounds);
            assertNotEquals(
                    "client 2 after client 1 removes fid1 and client 2 adds fid5",
                    bounds,
                    client2Bounds);

            transaction1.commit();
            Assert.assertEquals(
                    "auto after client 1 commits removal of fid1 (client 2 has added fid5)",
                    4,
                    roadAuto.getFeatures().size());
            Assert.assertEquals(
                    "client 1 after commiting removal of fid1 (client 2 has added fid5)",
                    4,
                    roadFromClient1.getFeatures().size());
            Assert.assertEquals(
                    "client 2 after client 1 commits removal of fid1 (client 2 has added fid5)",
                    5,
                    roadFromClient2.getFeatures().size());

            bounds = roadAuto.getFeatures().getBounds();
            client1Bounds = roadFromClient1.getFeatures().getBounds();
            client2Bounds = roadFromClient2.getFeatures().getBounds();
            Assert.assertEquals(
                    "client 1 after commiting removal of fid1 (client 2 has added fid5)",
                    bounds,
                    client1Bounds);
            assertNotEquals(
                    "client 2 after client 1 commits removal of fid1 (client 2 has added "
                            + "fid5)",
                    bounds,
                    client2Bounds);

            transaction2.commit();
            Assert.assertEquals(
                    "auto after client 2 commits addition of fid5 (fid1 previously removed)",
                    5,
                    roadAuto.getFeatures().size());
            Assert.assertEquals(
                    "client 1 after client 2 commits addition of fid5 (fid1 previously removed)",
                    5,
                    roadFromClient1.getFeatures().size());
            Assert.assertEquals(
                    "client 2 after commiting addition of fid5 (fid1 previously removed)",
                    5,
                    roadFromClient2.getFeatures().size());

            bounds = roadAuto.getFeatures().getBounds();
            client1Bounds = roadFromClient1.getFeatures().getBounds();
            client2Bounds = roadFromClient2.getFeatures().getBounds();
            Assert.assertEquals(
                    "client 1 after commiting addition of fid5 (fid1 previously removed)",
                    bounds,
                    client1Bounds);
            Assert.assertEquals(
                    "client 2 after commiting addition of fid5 (fid1 previously removed)",
                    bounds,
                    client2Bounds);
        }
    }

    @Test
    public void testUseExistingFid() throws Exception {
        SimpleFeatureType ROAD = store.getSchema("road");
        SimpleFeature chrisFeature =
                SimpleFeatureBuilder.build(
                        ROAD, new Object[] {Integer.valueOf(5), "chris"}, "fid5");
        chrisFeature.getUserData().put(Hints.USE_PROVIDED_FID, Boolean.TRUE);

        SimpleFeatureStore roadAuto = (SimpleFeatureStore) store.getFeatureSource("road");
        SimpleFeatureCollection addCollection = DataUtilities.collection(chrisFeature);
        List<FeatureId> fids = roadAuto.addFeatures(addCollection);

        // check the id was preserved
        Assert.assertEquals(1, fids.size());
        FeatureId fid = SimpleFeatureBuilder.createDefaultFeatureIdentifier("fid5");
        Assert.assertTrue(fids.contains(fid));

        // manually check the feature with the proper id is actually there
        try (SimpleFeatureIterator it =
                roadAuto.getFeatures(ff.id(Collections.singleton(fid))).features()) {
            Assert.assertTrue(it.hasNext());
            SimpleFeature sf = it.next();
            Assert.assertEquals(fid, sf.getIdentifier());
        }
    }

    @Test
    public void testSortOnUnrequestedProperties() throws Exception {
        ContentFeatureSource fs = store.getFeatureSource("road");
        Query q = new Query("road");
        q.setPropertyNames("name");
        q.setSortBy(ff.sort("id", SortOrder.DESCENDING));

        ContentFeatureCollection fc = fs.getFeatures(q);
        String[] expectedNames = {"", "justin", "dave", "brent", "jody"};
        try (SimpleFeatureIterator fi = fc.features()) {
            int i = 0;
            while (fi.hasNext()) {
                SimpleFeature feature = fi.next();
                String name = (String) feature.getAttribute("name");
                String expectedName = expectedNames[i];
                Assert.assertEquals(expectedName, name);
                i++;
            }
        }
    }

    @Test
    public void testRemoveSchema() throws Exception {
        File dir = Files.createTempDirectory("layers").toFile();
        File file1 =
                Files.createFile(Paths.get(dir.getAbsolutePath(), "points.properties")).toFile();
        File file2 =
                Files.createFile(Paths.get(dir.getAbsolutePath(), "lines.properties")).toFile();
        File file3 =
                Files.createFile(Paths.get(dir.getAbsolutePath(), "polygon.properties")).toFile();
        Map<String, Serializable> params = new HashMap<>();
        params.put("directory", dir);
        DataStore store = DataStoreFinder.getDataStore(params);
        // File 1
        Assert.assertTrue(file1.exists());
        store.removeSchema("points");
        Assert.assertFalse(file1.exists());
        // File 2
        Assert.assertTrue(file2.exists());
        store.removeSchema("lines.properties");
        Assert.assertFalse(file2.exists());
        // File 3
        Assert.assertTrue(file3.exists());
        store.removeSchema(new NameImpl("polygon"));
        Assert.assertFalse(file3.exists());
    }
}
