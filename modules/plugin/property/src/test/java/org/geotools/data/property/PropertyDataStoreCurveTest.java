/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014 - 2015, Open Source Geospatial Foundation (OSGeo)
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
import junit.framework.TestCase;
import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.store.ContentFeatureStore;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.CircularRing;
import org.geotools.geometry.jts.CircularString;
import org.geotools.geometry.jts.CompoundRing;
import org.geotools.geometry.jts.CurvedGeometry;
import org.geotools.geometry.jts.WKTReader2;
import org.geotools.util.factory.Hints;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.WKTReader;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;

/**
 * Test functioning of PropertyDataStore (used as conformance testing).
 *
 * @author Jody Garnett (LISAsoft)
 */
public class PropertyDataStoreCurveTest extends TestCase {
    PropertyDataStore store;

    static FilterFactory2 ff = (FilterFactory2) CommonFactoryFinder.getFilterFactory(null);

    /** Constructor for SimpleDataStoreTest. */
    public PropertyDataStoreCurveTest(String arg0) {
        super(arg0);
    }

    protected void setUp() throws Exception {
        File dir = new File(".", "propertyCurveTestData");
        dir.mkdir();

        File file = new File(dir, "curvelines.properties");
        if (file.exists()) {
            file.delete();
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write("_=geom:LineString:4326,name:String");
        writer.newLine();
        writer.write(
                "cp.1=COMPOUNDCURVE(CIRCULARSTRING(0 0, 2 0, 2 1, 2 3, 4 3),(4 3, 4 5, 1 4, 0 0))|Compound");
        writer.newLine();
        writer.write("cp.2=CIRCULARSTRING(-10 0, -8 2, -6 0, -8 -2, -10 0)|Circle ");
        writer.newLine();
        writer.write("cp.3=CIRCULARSTRING(-7 -8, -5 -6, -3 -8, -1 -10, 1 -8))|Wave");
        writer.newLine();
        writer.close();

        store = new PropertyDataStore(dir);
        super.setUp();
    }

    protected void tearDown() throws Exception {
        File dir = new File("propertyCurveTestData");
        File list[] = dir.listFiles();
        for (int i = 0; i < list.length; i++) {
            list[i].delete();
        }
        dir.delete();
        super.tearDown();
    }

    public void testReadCurves() throws Exception {
        String names[] = store.getTypeNames();
        assertEquals(1, names.length);
        assertEquals("curvelines", names[0]);
        Query query = new Query("curvelines");
        FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                store.getFeatureReader(query, Transaction.AUTO_COMMIT);
        try {
            assertTrue(reader.hasNext());
            Object geom = reader.next().getDefaultGeometry();
            assertTrue(geom instanceof CompoundRing);
            CurvedGeometry<?> curved = (CurvedGeometry<?>) geom;
            assertEquals(Double.MAX_VALUE, curved.getTolerance());

            assertTrue(reader.hasNext());
            geom = reader.next().getDefaultGeometry();
            assertTrue(geom instanceof CircularRing);
            curved = (CurvedGeometry<?>) geom;
            assertEquals(Double.MAX_VALUE, curved.getTolerance());

            assertTrue(reader.hasNext());
            geom = reader.next().getDefaultGeometry();
            assertTrue(geom instanceof CircularString);
            curved = (CurvedGeometry<?>) geom;
            assertEquals(Double.MAX_VALUE, curved.getTolerance());
        } finally {
            reader.close();
        }
    }

    public void testWriteCurves() throws Exception {
        // wipe out the original features
        ContentFeatureStore fs = (ContentFeatureStore) store.getFeatureSource("curvelines");
        fs.removeFeatures(Filter.INCLUDE);

        // build the features from code
        SimpleFeatureType schema = fs.getSchema();
        WKTReader reader = new WKTReader2();
        Geometry compoundGeometry =
                reader.read(
                        "COMPOUNDCURVE(CIRCULARSTRING(0 0, 2 0, 2 1, 2 3, 4 3),(4 3, 4 5, 1 4, 0 0))");
        SimpleFeature cp1 =
                SimpleFeatureBuilder.build(
                        schema, new Object[] {compoundGeometry, "Compound"}, "cp.1");
        Geometry circleGeometry = reader.read("CIRCULARSTRING(-10 0, -8 2, -6 0, -8 -2, -10 0)");
        SimpleFeature cp2 =
                SimpleFeatureBuilder.build(schema, new Object[] {circleGeometry, "Circle"}, "cp.2");
        Geometry waveGeometry = reader.read("CIRCULARSTRING(-7 -8, -5 -6, -3 -8, -1 -10, 1 -8))");
        SimpleFeature cp3 =
                SimpleFeatureBuilder.build(schema, new Object[] {waveGeometry, "Wave"}, "cp.3");

        // write them out
        ListFeatureCollection fc = new ListFeatureCollection(schema);
        fc.add(cp1);
        fc.add(cp2);
        fc.add(cp3);
        fs.addFeatures((SimpleFeatureCollection) fc);

        // run the read test for verification
        testReadCurves();
    }

    public void testReadCurvesWithTolerance() throws Exception {
        String names[] = store.getTypeNames();
        assertEquals(1, names.length);
        assertEquals("curvelines", names[0]);
        Query query = new Query("curvelines");
        query.getHints().put(Hints.LINEARIZATION_TOLERANCE, 0.1);
        FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                store.getFeatureReader(query, Transaction.AUTO_COMMIT);
        try {
            int count = 0;
            while (reader.hasNext()) {
                Object geom = reader.next().getDefaultGeometry();
                assertTrue(geom instanceof CurvedGeometry);
                CurvedGeometry<?> curved = (CurvedGeometry<?>) geom;
                assertEquals(0.1, curved.getTolerance(), 0d);
            }
        } finally {
            reader.close();
        }
    }
}
