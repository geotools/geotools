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
package org.geotools.data.shapefile.ng;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.geotools.TestData;
import org.geotools.data.DataStore;
import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.shapefile.ng.files.ShpFiles;
import org.geotools.data.shapefile.ng.shp.IndexFile;
import org.geotools.data.shapefile.ng.shp.ShapefileReader;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.FeatureCollections;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

/**
 * 
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Ian Schneider
 * @author James Macgill
 */
public class ShapefileTest extends TestCaseSupport {

    public final String STATEPOP = "shapes/statepop.shp";
    public final String STATEPOP_IDX = "shapes/statepop.shx";
    public final String POINTTEST = "shapes/pointtest.shp";
    public final String POLYGONTEST = "shapes/polygontest.shp";
    public final String HOLETOUCHEDGE = "shapes/holeTouchEdge.shp";
    public final String EXTRAATEND = "shapes/extraAtEnd.shp";

    public ShapefileTest(String testName) throws IOException {
        super(testName);
    }

    public void testLoadingStatePop() throws Exception {
        loadShapes(STATEPOP, 49);
        loadMemoryMapped(STATEPOP, 49);
    }

    public void testLoadingSamplePointFile() throws Exception {
        loadShapes(POINTTEST, 10);
        loadMemoryMapped(POINTTEST, 10);
    }

    public void testLoadingSamplePolygonFile() throws Exception {
        loadShapes(POLYGONTEST, 2);
        loadMemoryMapped(POLYGONTEST, 2);
    }

    public void testLoadingTwice() throws Exception {
        loadShapes(POINTTEST, 10);
        loadShapes(POINTTEST, 10);
        loadShapes(STATEPOP, 49);
        loadShapes(STATEPOP, 49);
        loadShapes(POLYGONTEST, 2);
        loadShapes(POLYGONTEST, 2);
    }

    /**
     * It is posible for a point in a hole to touch the edge of its containing
     * shell This test checks that such polygons can be loaded ok.
     */
    public void testPolygonHoleTouchAtEdge() throws Exception {
        loadShapes(HOLETOUCHEDGE, 1);
        loadMemoryMapped(HOLETOUCHEDGE, 1);
    }

    /**
     * It is posible for a shapefile to have extra information past the end of
     * the normal feature area, this tests checks that this situation is delt
     * with ok.
     */
    public void testExtraAtEnd() throws Exception {
        loadShapes(EXTRAATEND, 3);
        loadMemoryMapped(EXTRAATEND, 3);
    }

    public void testIndexFile() throws Exception {
        copyShapefiles(STATEPOP);
        copyShapefiles(STATEPOP_IDX);
        final URL url1 = TestData.url(STATEPOP); // Backed by InputStream
        final URL url2 = TestData.url(TestCaseSupport.class, STATEPOP); // Backed by File
        final URL url3 = TestData.url(TestCaseSupport.class, STATEPOP_IDX);
        final ShapefileReader reader1 = new ShapefileReader(new ShpFiles(url1),
                false, false, new GeometryFactory());
        final ShapefileReader reader2 = new ShapefileReader(new ShpFiles(url2),
                false, false, new GeometryFactory());
        final IndexFile index = new IndexFile(new ShpFiles(url3), false);
        try {
            for (int i = 0; i < index.getRecordCount(); i++) {
                if (reader1.hasNext()) {

                    Geometry g1 = (Geometry) reader1.nextRecord().shape();
                    Geometry g2 = (Geometry) reader2.shapeAt(2 * (index
                            .getOffset(i)));
                    assertTrue(g1.equalsExact(g2));

                } else {
                    fail("uneven number of records");
                }
                // assertEquals(reader1.nextRecord().offset(),index.getOffset(i));
            }
        } finally {
            index.close();
            reader2.close();
            reader1.close();
        }
    }

    public void testHolyPolygons() throws Exception {
        SimpleFeatureType type = DataUtilities.createType("junk",
                "a:MultiPolygon");
        SimpleFeatureCollection features = FeatureCollections.newCollection();

        File tmpFile = getTempFile();
        tmpFile.delete();

        // write features
        DataStore s = new ShapefileDataStore(tmpFile.toURI().toURL());
        s.createSchema(type);
        String typeName = type.getTypeName();
        SimpleFeatureStore store = (SimpleFeatureStore) s.getFeatureSource(s.getTypeNames()[0]);

        store.addFeatures(features);
        s.dispose();
        

        s = new ShapefileDataStore(tmpFile.toURI().toURL());
        typeName = s.getTypeNames()[0];
        SimpleFeatureSource source = s.getFeatureSource(typeName);
        SimpleFeatureCollection fc = source.getFeatures();

        ShapefileReadWriteTest.compare(features, fc);
        s.dispose();
    }

    public void testSkippingRecords() throws Exception {
        final URL url = TestData.url(STATEPOP);
        final ShapefileReader r = new ShapefileReader(new ShpFiles(url), false,
                false, new GeometryFactory());
        try {
            int idx = 0;
            while (r.hasNext()) {
                idx++;
                r.nextRecord();
            }
            assertEquals(49, idx);
        } finally {
            r.close();
        }
    }

    public void testDuplicateColumnNames() throws Exception {
        File file = TestData.file(TestCaseSupport.class, "bad/state.shp");
        ShapefileDataStore dataStore = new ShapefileDataStore(file.toURI().toURL());
        SimpleFeatureSource states = dataStore.getFeatureSource();
        SimpleFeatureType schema = states.getSchema();
        assertEquals(6, schema.getAttributeCount());
        assertTrue(states.getCount(Query.ALL) > 0);
        dataStore.dispose();
    }

    public void testShapefileReaderRecord() throws Exception {
        final URL c1 = TestData.url(STATEPOP);
        ShapefileReader reader = new ShapefileReader(new ShpFiles(c1), false,
                false, new GeometryFactory());
        URL c2;
        try {
            ArrayList offsets = new ArrayList();
            while (reader.hasNext()) {
                ShapefileReader.Record record = reader.nextRecord();
                offsets.add(new Integer(record.offset()));
                Geometry geom = (Geometry) record.shape();
                assertEquals(new Envelope(record.minX, record.maxX,
                        record.minY, record.maxY), geom.getEnvelopeInternal());
                record.toString();
            }
            copyShapefiles(STATEPOP);
            reader.close();
            c2 = TestData.url(TestCaseSupport.class, STATEPOP);
            reader = new ShapefileReader(new ShpFiles(c2), false, false, new GeometryFactory());
            for (int i = 0, ii = offsets.size(); i < ii; i++) {
                reader.shapeAt(((Integer) offsets.get(i)).intValue());
            }
        } finally {
            reader.close();
        }
    }
    
	public void testNullGeometries() throws Exception {
		// Write a point shapefile with one null geometry
		Map<String, Serializable> params = new HashMap<String, Serializable>();
		File tmp = File.createTempFile("test", ".dbf");
		if (!tmp.delete()) {
			throw new IllegalStateException("Unable to clear temp file");
		}
		URL shpUrl = tmp.toURI().toURL();
		params.put("url", shpUrl);
		ShapefileDataStore ds = (ShapefileDataStore) new ShapefileDataStoreFactory()
				.createNewDataStore(params);
		SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
		tb.setName("shapefile");
		tb.add("the_geom", Point.class);
		ds.createSchema(tb.buildFeatureType());
		Transaction transaction = Transaction.AUTO_COMMIT;
		FeatureWriter<SimpleFeatureType, SimpleFeature> writer = ds.getFeatureWriter(ds.getTypeNames()[0], Transaction.AUTO_COMMIT);
		SimpleFeature feature = writer.next();
		feature.setAttribute(0, null);
		writer.close();
		transaction.commit();
		ds.dispose();

		// Read the same file and check the geometry is null
		ShpFiles shpFiles = new ShpFiles(shpUrl);
		ShapefileReader reader = new ShapefileReader(shpFiles, false, true,
				new GeometryFactory(), false);
		try {
			assertTrue(reader.hasNext());
			assertTrue(reader.nextRecord().shape() == null);
		} finally {
			reader.close();
		}
	}

    protected void loadShapes(String resource, int expected) throws Exception {
        final URL url = TestData.url(resource);
        ShapefileReader reader = new ShapefileReader(new ShpFiles(url), false,
                false, new GeometryFactory());
        int cnt = 0;
        try {
            while (reader.hasNext()) {
                reader.nextRecord().shape();
                cnt++;
            }
        } finally {
            reader.close();
        }
        assertEquals("Number of Geometries loaded incorect for : " + resource,
                expected, cnt);
    }

    public void testReadingSparse() throws IOException {
        File file = TestData.file(TestCaseSupport.class, "sparse/sparse.shp");
        ShapefileReader reader = new ShapefileReader(new ShpFiles(file), false, false, new GeometryFactory());
        int cnt = 0;
        try {
            while (reader.hasNext()) {
                reader.nextRecord().shape();
                cnt++;
            }
        } finally {
            reader.close();
        }
        assertEquals("Did not read all Geometries from sparse file.", 31, cnt);
    }

    protected void loadMemoryMapped(String resource, int expected)
            throws Exception {
        final URL url = TestData.url(resource);
        ShapefileReader reader = new ShapefileReader(new ShpFiles(url), false,
                false, new GeometryFactory());
        int cnt = 0;
        try {
            while (reader.hasNext()) {
                reader.nextRecord().shape();
                cnt++;
            }
        } finally {
            reader.close();
        }
        assertEquals("Number of Geometries loaded incorect for : " + resource,
                expected, cnt);
    }
}
