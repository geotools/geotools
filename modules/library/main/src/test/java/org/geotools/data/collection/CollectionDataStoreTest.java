/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.collection;

import java.io.IOException;
import java.util.NoSuchElementException;

import org.geotools.data.DataTestCase;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultQuery;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.DiffFeatureReader;
import org.geotools.data.EmptyFeatureReader;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureWriter;
import org.geotools.data.FilteringFeatureReader;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.TransactionStateDiff;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.IllegalAttributeException;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;


/**
 * DOCUMENT ME!
 *
 * @author Jody Garnett, Refractions Research
 * @source $URL$
 */
public class CollectionDataStoreTest extends DataTestCase {
    CollectionDataStore data;

    /**
     * Constructor for MemoryDataStoreTest.
     *
     * @param arg0
     */
    public CollectionDataStoreTest(String arg0) {
        super(arg0);
    }

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        data = new CollectionDataStore(DataUtilities.collection(roadFeatures));
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        data = null;
    }

    public void testGetFeatureTypes() {
        String[] names = data.getTypeNames();
        assertEquals(1, names.length);
        assertTrue(contains(names, "road"));
    }

    boolean contains(Object[] array, Object expected) {
        if ((array == null) || (array.length == 0)) {
            return false;
        }

        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(expected)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Like contain but based on match rather than equals
     *
     * @param array DOCUMENT ME!
     * @param expected DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    boolean containsLax(SimpleFeature[] array, SimpleFeature expected) {
        if ((array == null) || (array.length == 0)) {
            return false;
        }

        SimpleFeatureType type = expected.getFeatureType();

        for (int i = 0; i < array.length; i++) {
            if (match(array[i], expected)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Compare based on attributes not getID allows comparison of Diff contents
     *
     * @param expected DOCUMENT ME!
     * @param actual DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    boolean match(SimpleFeature expected, SimpleFeature actual) {
        SimpleFeatureType type = expected.getFeatureType();

        for (int i = 0; i < type.getAttributeCount(); i++) {
            Object av = actual.getAttribute(i);
            Object ev = expected.getAttribute(i);

            if ((av == null) && (ev != null)) {
                return false;
            } else if ((ev == null) && (av != null)) {
                return false;
            } else if (av instanceof Geometry && ev instanceof Geometry) {
                Geometry ag = (Geometry) av;
                Geometry eg = (Geometry) ev;

                if (!ag.equals(eg)) {
                    return false;
                }
            } else if (!av.equals(ev)) {
                return false;
            }
        }

        return true;
    }

    public void testGetSchema() throws IOException {
        assertSame(roadType, data.getSchema("road"));
    }

    void assertCovers(String msg, SimpleFeatureCollection c1, SimpleFeatureCollection c2) {
        if (c1 == c2) {
            return;
        }

        assertNotNull(msg, c1);
        assertNotNull(msg, c2);
        assertEquals(msg + " size", c1.size(), c2.size());

        SimpleFeature f;

        for (SimpleFeatureIterator i = c1.features(); i.hasNext();) {
            f = i.next();
            assertTrue(msg + " " + f.getID(), c2.contains(f));
        }
    }

    public void testGetFeatureReader() throws IOException, IllegalAttributeException {
         FeatureReader<SimpleFeatureType, SimpleFeature> reader = data.getFeatureReader("road");
        assertCovered(roadFeatures, reader);
        assertEquals(false, reader.hasNext());
    }

//    public void testGetFeatureReaderMutability() throws IOException, IllegalAttributeException {
//         FeatureReader<SimpleFeatureType, SimpleFeature> reader = data.getFeatureReader("road");
//        Feature feature;
//
//        while (reader.hasNext()) {
//            feature = (Feature) reader.next();
//            feature.setAttribute("name", null);
//        }
//
//        reader.close();
//
//        reader = data.getFeatureReader("road");
//
//        while (reader.hasNext()) {
//            feature = (Feature) reader.next();
//            assertNotNull(feature.getAttribute("name"));
//        }
//
//        reader.close();
//
//        try {
//            reader.next();
//            fail("next should fail with an IOException");
//        } catch (IOException expected) {
//        }
//    }

    public void testGetFeatureReaderConcurancy()
        throws NoSuchElementException, IOException, IllegalAttributeException {
         FeatureReader<SimpleFeatureType, SimpleFeature> reader1 = data.getFeatureReader("road");
         FeatureReader<SimpleFeatureType, SimpleFeature> reader2 = data.getFeatureReader("road");

        SimpleFeature feature1;
        SimpleFeature feature2;

        while (reader1.hasNext() || reader2.hasNext()) {
            assertTrue(contains(roadFeatures, reader1.next()));
            assertTrue(contains(roadFeatures, reader2.next()));
        }

        try {
            reader1.next();
            fail("next should fail with an IOException");
        } catch (IOException expected) {
        }

        try {
            reader2.next();
            fail("next should fail with an IOException");
        } catch (IOException expected) {
        }

        reader1.close();
        reader2.close();
    }

    public void testGetFeatureReaderFilterAutoCommit()
        throws NoSuchElementException, IOException, IllegalAttributeException {
        SimpleFeatureType type = data.getSchema("road");
         FeatureReader<SimpleFeatureType, SimpleFeature> reader;

        reader = data.getFeatureReader(new DefaultQuery("road"), Transaction.AUTO_COMMIT);
        assertFalse(reader instanceof FilteringFeatureReader);
        assertEquals(type, reader.getFeatureType());
        assertEquals(roadFeatures.length, count(reader));

        reader = data.getFeatureReader(new DefaultQuery("road", Filter.EXCLUDE), Transaction.AUTO_COMMIT);
        assertTrue(reader instanceof EmptyFeatureReader);

        assertEquals(type, reader.getFeatureType());
        assertEquals(0, count(reader));

        reader = data.getFeatureReader(new DefaultQuery("road", rd1Filter), Transaction.AUTO_COMMIT);
        assertTrue(reader instanceof FilteringFeatureReader);
        assertEquals(type, reader.getFeatureType());
        assertEquals(1, count(reader));
    }

    public void testGetFeatureReaderFilterTransaction()
        throws NoSuchElementException, IOException, IllegalAttributeException {
        Transaction t = new DefaultTransaction();
        SimpleFeatureType type = data.getSchema("road");
         FeatureReader<SimpleFeatureType, SimpleFeature> reader;

        reader = data.getFeatureReader(new DefaultQuery("road", Filter.EXCLUDE), t);
        assertTrue(reader instanceof EmptyFeatureReader);
        assertEquals(type, reader.getFeatureType());
        assertEquals(0, count(reader));

        reader = data.getFeatureReader(new DefaultQuery("road"), t);
        assertTrue(reader instanceof DiffFeatureReader);
        assertEquals(type, reader.getFeatureType());
        assertEquals(roadFeatures.length, count(reader));

        reader = data.getFeatureReader(new DefaultQuery("road", rd1Filter), t);
        //assertTrue(reader instanceof DiffFeatureReader);  //currently it is being wraped by a FilteringFeatureReader
        assertEquals(type, reader.getFeatureType());
        assertEquals(1, count(reader));

        TransactionStateDiff state = (TransactionStateDiff) t.getState(data);
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer = state.writer("road", Filter.INCLUDE);
        SimpleFeature feature;

        while (writer.hasNext()) {
            feature = writer.next();

            if (feature.getID().equals("road.rd1")) {
                writer.remove();
            }
        }

        reader = data.getFeatureReader(new DefaultQuery("road", Filter.EXCLUDE), t);
        assertEquals(0, count(reader));

        reader = data.getFeatureReader(new DefaultQuery("road"), t);
        assertEquals(roadFeatures.length - 1, count(reader));

        reader = data.getFeatureReader(new DefaultQuery("road", rd1Filter), t);
        assertEquals(0, count(reader));

        t.rollback();
        reader = data.getFeatureReader(new DefaultQuery("road", Filter.EXCLUDE), t);
        assertEquals(0, count(reader));

        reader = data.getFeatureReader(new DefaultQuery("road"), t);
        assertEquals(roadFeatures.length, count(reader));

        reader = data.getFeatureReader(new DefaultQuery("road", rd1Filter), t);
        assertEquals(1, count(reader));
    }

    void assertCovered(SimpleFeature[] features,  FeatureReader<SimpleFeatureType, SimpleFeature> reader)
        throws NoSuchElementException, IOException, IllegalAttributeException {
        int count = 0;

        try {
            while (reader.hasNext()) {
                assertTrue(contains(features, reader.next()));
                count++;
            }
        } finally {
            reader.close();
        }

        assertEquals(features.length, count);
    }

    /**
     * Ensure that  FeatureReader<SimpleFeatureType, SimpleFeature> reader contains extactly the contents of array.
     *
     * @param reader DOCUMENT ME!
     * @param array DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws NoSuchElementException DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     * @throws IllegalAttributeException DOCUMENT ME!
     */
    boolean covers(FeatureReader <SimpleFeatureType, SimpleFeature> reader, SimpleFeature[] array)
        throws NoSuchElementException, IOException, IllegalAttributeException {
        SimpleFeature feature;
        int count = 0;

        try {
            while (reader.hasNext()) {
                feature = reader.next();

                if (!contains(array, feature)) {
                    return false;
                }

                count++;
            }
        } finally {
            reader.close();
        }

        return count == array.length;
    }

    boolean coversLax(FeatureReader <SimpleFeatureType, SimpleFeature> reader, SimpleFeature[] array)
        throws NoSuchElementException, IOException, IllegalAttributeException {
        SimpleFeature feature;
        int count = 0;

        try {
            while (reader.hasNext()) {
                feature = reader.next();

                if (!containsLax(array, feature)) {
                    return false;
                }

                count++;
            }
        } finally {
            reader.close();
        }

        return count == array.length;
    }

    void dump(FeatureReader <SimpleFeatureType, SimpleFeature> reader)
        throws NoSuchElementException, IOException, IllegalAttributeException {
        SimpleFeature feature;
        int count = 0;

        try {
            while (reader.hasNext()) {
                feature = reader.next();
                System.out.println(count + " feature:" + feature);
                count++;
            }
        } finally {
            reader.close();
        }
    }

    void dump(Object[] array) {
        for (int i = 0; i < array.length; i++) {
            System.out.println(i + " feature:" + array[i]);
        }
    }

    // SimpleFeature Source Testing
    public void testGetFeatureSourceRoad() throws IOException {
        SimpleFeatureSource road = data.getFeatureSource("road");

        assertSame(roadType, road.getSchema());
        assertSame(data, road.getDataStore());
        assertEquals(3, road.getCount(Query.ALL));
        assertEquals(new Envelope(1, 5, 0, 4), road.getBounds(Query.ALL));

        SimpleFeatureCollection all = road.getFeatures();
        assertEquals(3, all.size());
        assertEquals(roadBounds, all.getBounds());

        SimpleFeatureCollection expected = DataUtilities.collection(roadFeatures);

        assertCovers("all", expected, all);
        assertEquals(roadBounds, all.getBounds());

        SimpleFeatureCollection some = road.getFeatures(rd12Filter);
        assertEquals(2, some.size());
        assertEquals(rd12Bounds, some.getBounds());
        assertEquals(some.getSchema(), road.getSchema());

        DefaultQuery query = new DefaultQuery( road.getSchema().getTypeName(), rd12Filter, new String[] { "name" });
        
        SimpleFeatureCollection half = road.getFeatures(query);
        assertEquals(2, half.size());
        assertEquals(1, half.getSchema().getAttributeCount());

        SimpleFeatureIterator reader = half.features();
        SimpleFeatureType type = half.getSchema();
        reader.close();

        SimpleFeatureType actual = half.getSchema();
        String name = type.getTypeName();
        assertEquals(name, actual.getTypeName());
        assertEquals(type.getName().getNamespaceURI(), actual.getName().getNamespaceURI());
        assertEquals(type.getAttributeCount(), actual.getAttributeCount());

        for (int i = 0; i < type.getAttributeCount(); i++) {
            assertEquals(type.getDescriptor(i), actual.getDescriptor(i));
        }

        assertNull(type.getGeometryDescriptor());
        assertEquals(type.getGeometryDescriptor(), actual.getGeometryDescriptor());
        assertEquals(type, actual);

        Envelope b = half.getBounds();
        assertEquals(new Envelope(1,5,0,4), b);        
    }

    
}
