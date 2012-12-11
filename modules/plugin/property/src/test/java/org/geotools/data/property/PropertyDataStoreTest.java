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
package org.geotools.data.property;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import junit.framework.TestCase;

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
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.Hints;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.Feature;
import org.opengis.feature.FeatureVisitor;
import org.opengis.feature.IllegalAttributeException;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.identity.FeatureId;

/**
 * Test functioning of PropertyDataStore (used as conformance testing and examples for the AbstractDataStore tutorial).
 * 
 * @author Jody Garnett (LISAsoft)
 * 
 * @source $URL$
 */
public class PropertyDataStoreTest extends TestCase {
    PropertyDataStore store;

    static FilterFactory2 ff = (FilterFactory2) CommonFactoryFinder.getFilterFactory(null);

    /**
     * Constructor for SimpleDataStoreTest.
     * 
     * @param arg0
     */
    public PropertyDataStoreTest(String arg0) {
        super(arg0);
    }

    protected void setUp() throws Exception {
        File dir = new File(".", "propertyTestData");
        dir.mkdir();

        File file = new File(dir, "road.properties");
        if (file.exists()) {
            file.delete();
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write("_=id:Integer,name:String");
        writer.newLine();
        writer.write("fid1=1|jody");
        writer.newLine();
        writer.write("fid2=2|brent");
        writer.newLine();
        writer.write("fid3=3|dave");
        writer.newLine();
        writer.write("fid4=4|justin");
        writer.newLine();
        writer.write("fid5=5|");
        writer.close();

        file = new File(dir, "dots.in.name.properties");
        if (file.exists()) {
            file.delete();
        }
        writer = new BufferedWriter(new FileWriter(file));
        writer.write("_=id:Integer,name:String");
        writer.newLine();
        writer.write("fid1=1|jody");
        writer.newLine();
        writer.write("fid2=2|brent");
        writer.newLine();
        writer.write("fid3=3|dave");
        writer.newLine();
        writer.write("fid4=4|justin");
        writer.close();

        file = new File(dir, "multiline.properties");
        if (file.exists()) {
            file.delete();
        }
        writer = new BufferedWriter(new FileWriter(file));
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
        writer.close();

        file = new File(dir, "table.properties");
        if (file.exists()) {
            file.delete();
        }
        writer = new BufferedWriter(new FileWriter(file));
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
        writer.close();

        store = new PropertyDataStore(dir);
        super.setUp();
    }

    protected void tearDown() throws Exception {
        File dir = new File("propertyTestData");
        File list[] = dir.listFiles();
        for (int i = 0; i < list.length; i++) {
            list[i].delete();
        }
        dir.delete();
        super.tearDown();
    }

    public void testGetNames() {
        String names[] = store.getTypeNames();
        Arrays.sort(names);
        assertEquals(4, names.length);
        assertEquals("dots.in.name", names[0]);
        assertEquals("multiline", names[1]);
        assertEquals("road", names[2]);
        assertEquals("table", names[3]);
    }

    public void testGetSchema() throws IOException {
        SimpleFeatureType type = store.getSchema("road");
        assertNotNull(type);
        assertEquals("road", type.getTypeName());
        assertEquals("propertyTestData", type.getName().getNamespaceURI().toString());
        assertEquals(2, type.getAttributeCount());

        AttributeDescriptor id = type.getDescriptor(0);
        AttributeDescriptor name = type.getDescriptor(1);

        assertEquals("id", id.getLocalName());
        assertEquals("class java.lang.Integer", id.getType().getBinding().toString());

        assertEquals("name", name.getLocalName());
        assertEquals("class java.lang.String", name.getType().getBinding().toString());
    }

    public void testGetFeaturesFeatureTypeFilterTransaction1() throws Exception {
        Query roadQuery = new Query("road");
        FeatureReader<SimpleFeatureType, SimpleFeature> reader = store.getFeatureReader(roadQuery,
                Transaction.AUTO_COMMIT);
        int count = 0;
        try {
            while (reader.hasNext()) {
                reader.next();
                count++;
            }
        } finally {
            reader.close();
        }
        assertEquals(5, count);

        Filter selectFid1;

        selectFid1 = ff.id(Collections.singleton(ff.featureId("fid1")));
        reader = store.getFeatureReader(new Query("road", selectFid1), Transaction.AUTO_COMMIT);
        assertEquals(1, count(reader));

        Transaction transaction = new DefaultTransaction();
        reader = store.getFeatureReader(roadQuery, transaction);
        assertEquals(5, count(reader));

        reader = store.getFeatureReader(roadQuery, transaction);
        List<String> list = new ArrayList<String>();
        try {
            while (reader.hasNext()) {
                list.add(reader.next().getID());
            }
        } finally {
            reader.close();
        }
        assertEquals("[fid1, fid2, fid3, fid4, fid5]", list.toString());
    }

    /*
     * Test for FeatureReader<SimpleFeatureType, SimpleFeature> getFeatureReader(String)
     */
    public void testGetFeatureReaderString() throws NoSuchElementException, IOException,
            IllegalAttributeException {
        FeatureReader<SimpleFeatureType, SimpleFeature> reader = store.getFeatureReader("road");
        int count = 0;
        try {
            while (reader.hasNext()) {
                reader.next();
                count++;
            }
        } finally {
            reader.close();
        }
        assertEquals(5, count);
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
        return count(store.getFeatureReader(typeName));
    }

    public void testWriterSkipThrough() throws Exception {
        PropertyFeatureWriter writer = (PropertyFeatureWriter) store.createFeatureWriter("road",
                Transaction.AUTO_COMMIT);

        File in = writer.read;
        File out = writer.write;

        int count = 0;
        while (writer.hasNext()) {
            writer.next();
            count++;
        }
        assertEquals(5, count);
        assertTrue(in.exists());
        assertTrue(out.exists());
        writer.close();
        assertTrue(in.exists());

        assertEquals(5, count("road"));
    }

    public void testWriterChangeName() throws Exception {
        PropertyFeatureWriter writer = (PropertyFeatureWriter) store.createFeatureWriter("road",
                Transaction.AUTO_COMMIT);

        int count = 0;
        while (writer.hasNext()) {
            SimpleFeature f = writer.next();
            f.setAttribute(1, "name " + (count + 1));
            writer.write();
            count++;
        }
        writer.close();
        assertEquals(5, count);
        assertEquals(5, count("road"));
    }

    public void testWriterChangeFirstName() throws Exception {
        PropertyFeatureWriter writer = (PropertyFeatureWriter) store.createFeatureWriter("road",
                Transaction.AUTO_COMMIT);
        SimpleFeature f;
        f = writer.next();
        f.setAttribute(1, "changed");
        writer.write();
        writer.close();
        assertEquals(5, count("road"));
    }

    public void testWriterChangeLastName() throws Exception {
        PropertyFeatureWriter writer = (PropertyFeatureWriter) store.createFeatureWriter("road",
                Transaction.AUTO_COMMIT);
        SimpleFeature f;
        writer.next();
        writer.next();
        writer.next();
        f = writer.next();
        f.setAttribute(1, "changed");
        writer.write();
        writer.close();
        assertEquals(5, count("road"));
    }

    public void testWriterChangeAppend() throws Exception {
        PropertyFeatureWriter writer = (PropertyFeatureWriter) store.createFeatureWriter("road",
                Transaction.AUTO_COMMIT);
        SimpleFeature f;
        writer.next();
        writer.next();
        writer.next();
        writer.next();
        writer.next();
        assertFalse(writer.hasNext());
        f = writer.next();
        assertNotNull(f);
        f.setAttribute(0, new Integer(-1));
        f.setAttribute(1, "new");
        writer.write();
        writer.close();
        assertEquals(6, count("road"));
    }

    public void testWriterAppendLastNull() throws Exception {
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer = store
                .getFeatureWriterAppend("road", Transaction.AUTO_COMMIT);
        SimpleFeature f;
        assertFalse(writer.hasNext());
        f = writer.next();
        assertNotNull(f);
        f.setAttribute(0, new Integer(-1));
        f.setAttribute(1, null); // this made the datastore break
        writer.write();
        writer.close();
        assertEquals(6, count("road"));
    }

    public void testWriterChangeRemoveFirst() throws Exception {
        PropertyFeatureWriter writer = (PropertyFeatureWriter) store.createFeatureWriter("road",
                Transaction.AUTO_COMMIT);

        writer.next();
        writer.remove();
        writer.close();
        assertEquals(4, count("road"));
    }

    public void testWriterChangeRemoveLast() throws Exception {
        PropertyFeatureWriter writer = (PropertyFeatureWriter) store.createFeatureWriter("road",
                Transaction.AUTO_COMMIT);

        writer.next();
        writer.next();
        writer.next();
        writer.remove();
        writer.close();
        assertEquals(4, count("road"));
    }

    public void testWriterChangeRemoveAppend() throws Exception {
        PropertyFeatureWriter writer = (PropertyFeatureWriter) store.createFeatureWriter("road",
                Transaction.AUTO_COMMIT);
        SimpleFeature f;
        writer.next();
        writer.next();
        writer.next();
        writer.next();
        writer.next();

        assertFalse(writer.hasNext());
        f = writer.next();
        assertNotNull(f);
        f.setAttribute(0, new Integer(-1));
        f.setAttribute(1, "new");
        writer.remove();
        writer.close();
        assertEquals(5, count("road"));
    }

    public void testWriterChangeIgnoreAppend() throws Exception {
        PropertyFeatureWriter writer = (PropertyFeatureWriter) store.createFeatureWriter("road",
                Transaction.AUTO_COMMIT);
        SimpleFeature f;
        writer.next();
        writer.next();
        writer.next();
        writer.next();
        writer.next();
        assertFalse(writer.hasNext());
        f = writer.next();
        assertNotNull(f);
        f.setAttribute(0, new Integer(-1));
        f.setAttribute(1, "new");
        writer.close();
        assertEquals(5, count("road"));
    }

    public void testGetFeatureSource() throws Exception {
        SimpleFeatureSource road = store.getFeatureSource("road");
        SimpleFeatureCollection features = road.getFeatures();
        SimpleFeatureIterator reader = features.features();
        List<String> list = new ArrayList<String>();
        try {
            while (reader.hasNext()) {
                list.add(reader.next().getID());
            }
        } finally {
            reader.close();
        }
        assertEquals("[fid1, fid2, fid3, fid4, fid5]", list.toString());
        assertEquals(5, road.getCount(Query.ALL));
        Query q = new Query("road");
        q.setMaxFeatures(2);
        assertEquals(2, road.getCount(q));
        // the feature type is geometryless!
        assertTrue(road.getBounds(Query.ALL).isNull());
        assertEquals(5, features.size());
        // the feature type is geometryless!
        assertTrue(features.getBounds().isNull());
        assertEquals(5, features.size());

    }

    /**
     * In response to <a href="https://jira.codehaus.org/browse/GEOT-1409">GEOT-1409 Property datastore ruins the property file if a string attribute
     * has newlines</a>.
     * 
     * @throws Exception
     */
    public void testMultiLine() throws Exception {
        SimpleFeatureSource road = store.getFeatureSource("multiline");
        FeatureId fid1 = ff.featureId("fid1");
        Filter select = ff.id(Collections.singleton(fid1));
        SimpleFeatureCollection featureCollection = road.getFeatures(select);
        featureCollection.accepts(new FeatureVisitor() {
            public void visit(Feature f) {
                SimpleFeature feature = (SimpleFeature) f;
                String name = (String) feature.getAttribute("name");
                assertEquals("jody \ngarnett", name);
            }
        }, null);
    }

    /**
     * In response to <a href="http://jira.codehaus.org/browse/GEOT-3540">GEOT-3540 PropertyDataStore doesn't support empty trailing spaces</a>.
     * <p>
     * Table with no geoemtry, containing null and empty strings at end of line
     * </p>
     * 
     * @throws Exception
     */
    public void testTable() throws Exception {
        SimpleFeatureSource table = store.getFeatureSource("table");
        // GenericEntity.f004=description-f004|name-f004
        FeatureId fid1 = ff.featureId("GenericEntity.f004");
        Filter select = ff.id(Collections.singleton(fid1));
        SimpleFeatureCollection featureCollection = table.getFeatures(select);
        featureCollection.accepts(new FeatureVisitor() {
            public void visit(Feature f) {
                SimpleFeature feature = (SimpleFeature) f;
                String name = (String) feature.getAttribute("name");
                assertEquals("name-f004", name);
            }
        }, null);
        // GenericEntity.f003=description-f003|<null>
        fid1 = ff.featureId("GenericEntity.f003");
        select = ff.id(Collections.singleton(fid1));
        featureCollection = table.getFeatures(select);
        featureCollection.accepts(new FeatureVisitor() {
            public void visit(Feature f) {
                SimpleFeature feature = (SimpleFeature) f;
                String name = (String) feature.getAttribute("name");
                System.out.println(name);
                assertNull("represent null", name);
            }
        }, null);
        // GenericEntity.f007=description-f007|
        fid1 = ff.featureId("GenericEntity.f007");
        select = ff.id(Collections.singleton(fid1));
        featureCollection = table.getFeatures(select);
        featureCollection.accepts(new FeatureVisitor() {
            public void visit(Feature f) {
                SimpleFeature feature = (SimpleFeature) f;
                String name = (String) feature.getAttribute("name");
                assertEquals("represent empty string", "", name);
            }
        }, null);
        // "  GenericEntity.f009=description-f009| "
        fid1 = ff.featureId("GenericEntity.f009");
        select = ff.id(Collections.singleton(fid1));
        featureCollection = table.getFeatures(select);
        featureCollection.accepts(new FeatureVisitor() {
            public void visit(Feature f) {
                SimpleFeature feature = (SimpleFeature) f;
                String name = (String) feature.getAttribute("name");
                assertEquals("represent empty string", " ", name);
            }
        }, null);
    }

    public void testTransactionIndependence() throws Exception {
        SimpleFeatureType ROAD = store.getSchema("road");
        SimpleFeature chrisFeature = SimpleFeatureBuilder.build(ROAD, new Object[] {
                new Integer(5), "chris" }, "fid5");

        SimpleFeatureStore roadAuto = (SimpleFeatureStore) store.getFeatureSource("road");

        SimpleFeatureStore roadFromClient1 = (SimpleFeatureStore) store.getFeatureSource("road");
        Transaction transaction1 = new DefaultTransaction("Transaction Used by Client 1");
        roadFromClient1.setTransaction(transaction1);

        SimpleFeatureStore roadFromClient2 = (SimpleFeatureStore) store.getFeatureSource("road");
        Transaction transaction2 = new DefaultTransaction("Transaction Used by Client 2");
        roadFromClient2.setTransaction(transaction2);

        FilterFactory2 ff = (FilterFactory2) CommonFactoryFinder.getFilterFactory(null);
        Filter selectFid1 = ff.id(Collections.singleton(ff.featureId("fid1")));

        // Before we edit everything should be the same
        assertEquals("auto before", 5, roadAuto.getFeatures().size());
        assertEquals("client 1 before", 5, roadFromClient1.getFeatures().size());
        assertEquals("client 2 before", 5, roadFromClient2.getFeatures().size());

        // Remove Feature with Fid1
        roadFromClient1.removeFeatures(selectFid1); // road1 removes fid1 on t1

        assertEquals("auto after client 1 removes fid1", 5, roadAuto.getFeatures().size());
        assertEquals("client 1 after client 1 removes fid1", 4, roadFromClient1.getFeatures()
                .size());
        assertEquals("client 2 after client 1 removes fid1", 5, roadFromClient2.getFeatures()
                .size());

        roadFromClient2.addFeatures(DataUtilities.collection(chrisFeature)); // road2 adds fid5 on t2
        assertEquals("auto after client 1 removes fid1 and client 2 adds fid5", 5, roadAuto
                .getFeatures().size());
        assertEquals("client 1 after client 1 removes fid1 and client 2 adds fid5", 4,
                roadFromClient1.getFeatures().size());
        assertEquals("cleint 2 after client 1 removes fid1 and client 2 adds fid5", 6,
                roadFromClient2.getFeatures().size());

        transaction1.commit();
        assertEquals("auto after client 1 commits removal of fid1 (client 2 has added fid5)", 4,
                roadAuto.getFeatures().size());
        assertEquals("client 1 after commiting removal of fid1 (client 2 has added fid5)", 4,
                roadFromClient1.getFeatures().size());
        assertEquals("client 2 after client 1 commits removal of fid1 (client 2 has added fid5)",
                5, roadFromClient2.getFeatures().size());

        transaction2.commit();
        assertEquals("auto after client 2 commits addition of fid5 (fid1 previously removed)", 5,
                roadAuto.getFeatures().size());
        assertEquals("client 1 after client 2 commits addition of fid5 (fid1 previously removed)",
                5, roadFromClient1.getFeatures().size());
        assertEquals("client 2 after commiting addition of fid5 (fid1 previously removed)", 5,
                roadFromClient2.getFeatures().size());
    }

    public void testUseExistingFid() throws Exception {
        SimpleFeatureType ROAD = store.getSchema("road");
        SimpleFeature chrisFeature = SimpleFeatureBuilder.build(ROAD, new Object[] {
                new Integer(5), "chris" }, "fid5");
        chrisFeature.getUserData().put(Hints.USE_PROVIDED_FID, Boolean.TRUE);

        SimpleFeatureStore roadAuto = (SimpleFeatureStore) store.getFeatureSource("road");
        List<FeatureId> fids = roadAuto.addFeatures(DataUtilities.collection(chrisFeature));

        // checke the id was preserved
        assertEquals(1, fids.size());
        FeatureId fid = SimpleFeatureBuilder.createDefaultFeatureIdentifier("fid5");
        assertTrue(fids.contains(fid));

        // manually check the feature with the proper id is actually there
        SimpleFeatureIterator it = roadAuto.getFeatures(ff.id(Collections.singleton(fid)))
                .features();
        assertTrue(it.hasNext());
        SimpleFeature sf = it.next();
        it.close();
        assertEquals(fid, sf.getIdentifier());
    }
}
