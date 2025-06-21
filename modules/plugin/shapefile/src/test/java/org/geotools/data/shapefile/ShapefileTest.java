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
package org.geotools.data.shapefile;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.geotools.TestData;
import org.geotools.api.data.DataStore;
import org.geotools.api.data.FeatureReader;
import org.geotools.api.data.FeatureWriter;
import org.geotools.api.data.FileDataStore;
import org.geotools.api.data.FileDataStoreFinder;
import org.geotools.api.data.Query;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.data.SimpleFeatureStore;
import org.geotools.api.data.Transaction;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.Id;
import org.geotools.data.DataUtilities;
import org.geotools.data.shapefile.files.ShpFiles;
import org.geotools.data.shapefile.shp.IndexFile;
import org.geotools.data.shapefile.shp.ShapefileReader;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.util.ScreenMap;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.operation.transform.IdentityTransform;
import org.junit.Test;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

/**
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

    private static final String SHP_FILTER_BEFORE_SCREENMAP = "filter-before-screenmap";
    private static final String SHP_SCREENMAP_WITH_DELETED_ROW = "screenmap-deleted";

    @Test
    public void testLoadingStatePop() throws Exception {
        loadShapes(STATEPOP, 49);
        loadMemoryMapped(STATEPOP, 49);
    }

    @Test
    public void testLoadingSamplePointFile() throws Exception {
        loadShapes(POINTTEST, 10);
        loadMemoryMapped(POINTTEST, 10);
    }

    @Test
    public void testLoadingSamplePolygonFile() throws Exception {
        loadShapes(POLYGONTEST, 2);
        loadMemoryMapped(POLYGONTEST, 2);
    }

    @Test
    public void testLoadingTwice() throws Exception {
        loadShapes(POINTTEST, 10);
        loadShapes(POINTTEST, 10);
        loadShapes(STATEPOP, 49);
        loadShapes(STATEPOP, 49);
        loadShapes(POLYGONTEST, 2);
        loadShapes(POLYGONTEST, 2);
    }

    /**
     * It is posible for a point in a hole to touch the edge of its containing shell This test checks that such polygons
     * can be loaded ok.
     */
    @Test
    public void testPolygonHoleTouchAtEdge() throws Exception {
        loadShapes(HOLETOUCHEDGE, 1);
        loadMemoryMapped(HOLETOUCHEDGE, 1);
    }

    /**
     * It is posible for a shapefile to have extra information past the end of the normal feature area, this tests
     * checks that this situation is delt with ok.
     */
    @Test
    public void testExtraAtEnd() throws Exception {
        loadShapes(EXTRAATEND, 3);
        loadMemoryMapped(EXTRAATEND, 3);
    }

    @Test
    public void testIndexFile() throws Exception {
        copyShapefiles(STATEPOP);
        copyShapefiles(STATEPOP_IDX);
        final URL url1 = TestData.url(STATEPOP); // Backed by InputStream
        final URL url2 = TestData.url(TestCaseSupport.class, STATEPOP); // Backed by File
        final URL url3 = TestData.url(TestCaseSupport.class, STATEPOP_IDX);
        try (ShapefileReader reader1 = new ShapefileReader(new ShpFiles(url1), false, false, new GeometryFactory());
                ShapefileReader reader2 = new ShapefileReader(new ShpFiles(url2), false, false, new GeometryFactory());
                IndexFile index = new IndexFile(new ShpFiles(url3), false)) {
            for (int i = 0; i < index.getRecordCount(); i++) {
                if (reader1.hasNext()) {

                    Geometry g1 = (Geometry) reader1.nextRecord().shape();
                    Geometry g2 = (Geometry) reader2.shapeAt(2 * index.getOffset(i));
                    assertTrue(g1.equalsExact(g2));

                } else {
                    fail("uneven number of records");
                }
                // assertEquals(reader1.nextRecord().offset(),index.getOffset(i));
            }
        }
    }

    @Test
    public void testHolyPolygons() throws Exception {
        SimpleFeatureType type = DataUtilities.createType("junk", "a:MultiPolygon");
        SimpleFeatureCollection features = new DefaultFeatureCollection();

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

    @Test
    public void testSkippingRecords() throws Exception {
        final URL url = TestData.url(STATEPOP);
        try (ShapefileReader r = new ShapefileReader(new ShpFiles(url), false, false, new GeometryFactory())) {
            int idx = 0;
            while (r.hasNext()) {
                idx++;
                r.nextRecord();
            }
            assertEquals(49, idx);
        }
    }

    @Test
    public void testDuplicateColumnNames() throws Exception {
        File file = TestData.file(TestCaseSupport.class, "bad/state.shp");
        ShapefileDataStore dataStore = new ShapefileDataStore(file.toURI().toURL());
        SimpleFeatureSource states = dataStore.getFeatureSource();
        SimpleFeatureType schema = states.getSchema();
        assertEquals(6, schema.getAttributeCount());
        assertTrue(states.getCount(Query.ALL) > 0);
        dataStore.dispose();
    }

    @Test
    public void testShapefileReaderRecord() throws Exception {
        final URL c1 = TestData.url(STATEPOP);
        URL c2;
        ArrayList<Integer> offsets = new ArrayList<>();
        try (ShapefileReader reader = new ShapefileReader(new ShpFiles(c1), false, false, new GeometryFactory())) {

            while (reader.hasNext()) {
                ShapefileReader.Record record = reader.nextRecord();
                offsets.add(Integer.valueOf(record.offset()));
                Geometry geom = (Geometry) record.shape();
                assertEquals(
                        new Envelope(record.minX, record.maxX, record.minY, record.maxY), geom.getEnvelopeInternal());
            }
            copyShapefiles(STATEPOP);
            reader.close();
            c2 = TestData.url(TestCaseSupport.class, STATEPOP);
        }

        try (ShapefileReader reader = new ShapefileReader(new ShpFiles(c2), false, false, new GeometryFactory())) {
            for (Integer offset : offsets) {
                reader.shapeAt(offset.intValue());
            }
        }
    }

    @Test
    public void testNullGeometries() throws Exception {
        // Write a point shapefile with one null geometry
        Map<String, Serializable> params = new HashMap<>();
        File tmp = File.createTempFile("test", ".dbf");
        markTempFile(tmp);
        if (!tmp.delete()) {
            throw new IllegalStateException("Unable to clear temp file");
        }
        URL shpUrl = tmp.toURI().toURL();
        params.put("url", shpUrl);
        ShapefileDataStore ds = (ShapefileDataStore) new ShapefileDataStoreFactory().createNewDataStore(params);
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName("shapefile");
        tb.add("the_geom", Point.class);
        ds.createSchema(tb.buildFeatureType());
        try (Transaction transaction = Transaction.AUTO_COMMIT;
                FeatureWriter<SimpleFeatureType, SimpleFeature> writer =
                        ds.getFeatureWriter(ds.getTypeNames()[0], Transaction.AUTO_COMMIT)) {
            SimpleFeature feature = writer.next();
            feature.setAttribute(0, null);
            transaction.commit();
        }
        ds.dispose();

        // Read the same file and check the geometry is null
        ShpFiles shpFiles = new ShpFiles(shpUrl);
        try (ShapefileReader reader = new ShapefileReader(shpFiles, false, true, new GeometryFactory(), false)) {
            assertTrue(reader.hasNext());
            assertNull(reader.nextRecord().shape());
        }
    }

    protected void loadShapes(String resource, int expected) throws Exception {
        final URL url = TestData.url(resource);

        int cnt = 0;
        try (ShapefileReader reader = new ShapefileReader(new ShpFiles(url), false, false, new GeometryFactory())) {
            while (reader.hasNext()) {
                reader.nextRecord().shape();
                cnt++;
            }
        }
        assertEquals("Number of Geometries loaded incorect for : " + resource, expected, cnt);
    }

    @Test
    public void testReadingSparse() throws IOException {
        File file = TestData.file(TestCaseSupport.class, "sparse/sparse.shp");
        int cnt = 0;
        try (ShapefileReader reader = new ShapefileReader(new ShpFiles(file), false, false, new GeometryFactory())) {
            while (reader.hasNext()) {
                reader.nextRecord().shape();
                cnt++;
            }
        }
        assertEquals("Did not read all Geometries from sparse file.", 31, cnt);
    }

    @Test
    /* GEOT-7127 */
    public void testReadingEmptyDbf() throws IOException {
        File file = TestData.file(TestCaseSupport.class, "missing/aaa1.shp");
        int cnt = 0;
        try (ShapefileReader reader = new ShapefileReader(new ShpFiles(file), false, false, new GeometryFactory())) {
            while (reader.hasNext()) {
                reader.nextRecord().shape();
                cnt++;
            }
        }
        assertEquals("Did not read all Geometries from shapefile with missing datafile.", 1, cnt);

        FileDataStore ds = FileDataStoreFinder.getDataStore(file);
        assertNotNull("Null schema", ds.getFeatureSource().getSchema());
        File missingFile = TestData.file(TestCaseSupport.class, "missing/aaa2.shp");
        FileDataStore ds2 = FileDataStoreFinder.getDataStore(missingFile);
        assertNotNull("null datastore", ds2);
        assertNotNull("null schema", ds2.getFeatureSource().getSchema());
        String name = ds2.getTypeNames()[0];
        assertEquals("bad name", "aaa2", name);

        try (SimpleFeatureIterator itr = ds2.getFeatureSource().getFeatures().features()) {
            while (itr.hasNext()) {
                SimpleFeature f = itr.next();
                assertNotNull(f);
            }
        }
    }

    @Test
    public void testScreenMapIndexedReader() throws Exception {
        URL shpUrl = TestData.url(this, SHP_FILTER_BEFORE_SCREENMAP + "/" + SHP_FILTER_BEFORE_SCREENMAP + ".shp");

        Map<String, Serializable> params = new HashMap<>();
        params.put(ShapefileDataStoreFactory.URLP.key, shpUrl);
        params.put(ShapefileDataStoreFactory.CREATE_SPATIAL_INDEX.key, Boolean.TRUE);

        ShapefileDataStore ds = (ShapefileDataStore) new ShapefileDataStoreFactory().createDataStore(params);

        // make a fid query to get a indexed reader

        String fidPrefix = ds.getTypeName().getLocalPart();
        Filter filter =
                ff.id(ff.featureId(fidPrefix + ".0"), ff.featureId(fidPrefix + ".1"), ff.featureId(fidPrefix + ".2"));
        // force creation of a fid index
        ds.indexManager.hasFidIndex(true);
        try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                ds.getFeatureReader(new Query(ds.getTypeNames()[0], filter), Transaction.AUTO_COMMIT)) {
            assertTrue(reader instanceof IndexedShapefileFeatureReader);

            // prepare a screenmap that will heavily prune features
            ScreenMap screenMap = new ScreenMap(-180, -90, 360, 180);
            screenMap.setSpans(1.0, 1.0);
            screenMap.setTransform(IdentityTransform.create(2));
            ((ShapefileFeatureReader) reader).setScreenMap(screenMap);
            ((ShapefileFeatureReader) reader).setSimplificationDistance(1.0);

            int count = 0;
            while (reader.hasNext()) {
                SimpleFeature feature = reader.next();
                assertNotNull(feature);
                assertNotSame(ShapefileFeatureReader.SKIP, feature.getDefaultGeometry());
                count++;
            }
            assertEquals(1, count);
        }
    }

    @Test
    public void testScreenMapWithDeletedRow() throws Exception {
        // test screen map optimization without filterBeforeScreenMap enhancement
        // ensure that initial deleted record does not cause ScreenMap to return no elements
        // first record is marked as deleted, all subsequent records are ScreenMap coincident
        boolean isFilterBeforeScreenMap = false;
        Integer filterFid = null;
        String expectedName = "b";
        int expectedFid = 2;

        testScreenMap(SHP_SCREENMAP_WITH_DELETED_ROW, isFilterBeforeScreenMap, filterFid, expectedName, expectedFid);
    }

    @Test
    public void testScreenMap() throws Exception {
        // test screen map optimization without filterBeforeScreenMap enhancement
        // first record is named "a" and has fid 1, all subsequent records are ScreenMap coincident
        boolean isFilterBeforeScreenMap = false;
        Integer filterFid = null;
        String expectedName = "a";
        int expectedFid = 1;

        testScreenMap(SHP_FILTER_BEFORE_SCREENMAP, isFilterBeforeScreenMap, filterFid, expectedName, expectedFid);
    }

    @Test
    public void testFilterBeforeScreenMap() throws Exception {
        // test screen map optimization with filterBeforeScreenMap enhancement
        // first record is filtered out, all subsequent records are ScreenMap coincident
        boolean isFilterBeforeScreenMap = true;
        Integer filterFid = 2;
        String expectedName = "b";
        int expectedFid = filterFid;

        testScreenMap(SHP_FILTER_BEFORE_SCREENMAP, isFilterBeforeScreenMap, filterFid, expectedName, expectedFid);
    }

    @Test
    public void testFilterBeforeScreenMapWithDeletedRow() throws Exception {
        // test screen map optimization with filterBeforeScreenMap enhancement and deleted row in
        // DBF
        // first record is filtered out, all subsequent records are ScreenMap coincident
        boolean isFilterBeforeScreenMap = true;
        Integer filterFid = 3;
        String expectedName = "c";
        int expectedFid = filterFid;

        testScreenMap(SHP_SCREENMAP_WITH_DELETED_ROW, isFilterBeforeScreenMap, filterFid, expectedName, expectedFid);
    }

    private void testScreenMap(
            String shpName, boolean isFilterBeforeScreenMap, Integer filterFid, String expectedName, int expectedFid)
            throws Exception {
        URL shpUrl = TestData.url(this, shpName + "/" + shpName + ".shp");

        Map<String, Serializable> params = new HashMap<>();
        params.put(ShapefileDataStoreFactory.URLP.key, shpUrl);

        ShapefileDataStore ds = (ShapefileDataStore) new ShapefileDataStoreFactory().createDataStore(params);

        Query query = new Query(ds.getTypeNames()[0]);
        if (isFilterBeforeScreenMap && filterFid != null) {
            FilterFactory factory = CommonFactoryFinder.getFilterFactory(null);
            Id id = factory.id(Collections.singleton(ff.featureId(shpName + "." + filterFid.toString())));
            query.setFilter(id);
        }
        try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                ds.getFeatureReader(query, Transaction.AUTO_COMMIT)) {
            ScreenMap screenMap = new ScreenMap(-180, -90, 360, 180);
            screenMap.setSpans(1.0, 1.0);
            screenMap.setTransform(IdentityTransform.create(2));

            ((ShapefileFeatureReader) reader).setScreenMap(screenMap);
            ((ShapefileFeatureReader) reader).setSimplificationDistance(1.0);

            assertTrue(reader.hasNext());
            SimpleFeature feature = reader.next();
            assertFalse(reader.hasNext());

            assertNotNull(feature);
            assertNotEquals(ShapefileFeatureReader.SKIP, feature.getDefaultGeometry());
            assertEquals(expectedName, feature.getAttribute("NAME"));
            assertEquals(expectedFid, feature.getAttribute("feature_id"));
        }
    }

    protected void loadMemoryMapped(String resource, int expected) throws Exception {
        final URL url = TestData.url(resource);
        int cnt = 0;
        try (ShapefileReader reader = new ShapefileReader(new ShpFiles(url), false, false, new GeometryFactory())) {
            while (reader.hasNext()) {
                reader.nextRecord().shape();
                cnt++;
            }
        }
        assertEquals("Number of Geometries loaded incorect for : " + resource, expected, cnt);
    }
}
