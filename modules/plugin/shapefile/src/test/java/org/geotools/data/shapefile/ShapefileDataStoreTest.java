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
package org.geotools.data.shapefile;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeSet;
import java.util.UUID;
import org.apache.commons.io.FileUtils;
import org.geotools.TestData;
import org.geotools.data.DataStore;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureStore;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.shapefile.files.ShpFileType;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureTypes;
import org.geotools.feature.NameImpl;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.feature.type.BasicFeatureTypes;
import org.geotools.filter.IllegalFilterException;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.util.URLs;
import org.geotools.util.factory.FactoryRegistryException;
import org.junit.After;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateXYZM;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.opengis.feature.Feature;
import org.opengis.feature.FeatureVisitor;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.FeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.Id;
import org.opengis.filter.identity.FeatureId;
import org.opengis.filter.identity.Identifier;
import org.opengis.geometry.BoundingBox;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * @version $Id$
 * @author Ian Schneider
 */
public class ShapefileDataStoreTest extends TestCaseSupport {

    // Custom class for testing
    class CustomTypeClass {

        private int customField;

        CustomTypeClass(int value) {
            this.customField = value;
        }

        @Override
        public String toString() {
            return Integer.toString(customField);
        }
    }

    static final String STATE_POP = "shapes/statepop.shp";
    static final String STREAM = "shapes/stream.shp";
    static final String DANISH = "shapes/danish_point.shp";
    static final String CHINESE = "shapes/chinese_poly.shp";
    static final String RUSSIAN = "shapes/rus-windows-1251.shp";
    static final String UTF8 = "shapes/wgs1snt.shp";
    static final FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
    private ShapefileDataStore store;

    @After
    public void tearDown() throws Exception {
        if (store != null) {
            store.dispose();
        }
        System.clearProperty("org.geotools.shapefile.datetime");
        System.clearProperty("org.geotools.shapefile.reportFieldSizeErrors");
        super.tearDown();
    }

    protected SimpleFeatureCollection loadFeatures(String resource, Query query) throws Exception {
        assertNotNull(query);

        URL url = TestData.url(resource);
        store = new ShapefileDataStore(url);
        SimpleFeatureSource fs = store.getFeatureSource(store.getTypeNames()[0]);
        return fs.getFeatures(query);
    }

    protected SimpleFeatureCollection loadLocalFeaturesM2() throws IOException {
        String target =
                "jar:file:/C:/Documents and Settings/jgarnett/.m2/repository/org/geotools/gt2-sample-data/2.4-SNAPSHOT/gt2-sample-data-2.4-SNAPSHOT.jar!/org/geotools/test-data/shapes/statepop.shp";
        URL url = new URL(target);
        ShapefileDataStore s = new ShapefileDataStore(url);
        SimpleFeatureSource fs = s.getFeatureSource(s.getTypeNames()[0]);
        return fs.getFeatures();
    }

    protected SimpleFeatureCollection loadFeatures(String resource, Charset charset, Query q)
            throws Exception {
        if (q == null) q = new Query();
        URL url = TestData.url(resource);
        store = new ShapefileDataStore(url);
        store.setCharset(charset);
        SimpleFeatureSource fs = store.getFeatureSource(store.getTypeNames()[0]);
        return fs.getFeatures(q);
    }

    protected SimpleFeatureCollection loadFeatures(ShapefileDataStore s) throws Exception {
        return s.getFeatureSource(s.getTypeNames()[0]).getFeatures();
    }

    @Test
    public void testLoad() throws Exception {
        loadFeatures(STATE_POP, Query.ALL);
    }

    @Test
    public void testNullReproject() throws Exception {
        // try to reproject to the target CRS, used to fail due to a missing check in
        // ContentFeatureSource
        CoordinateReferenceSystem targetCRS =
                loadFeatures(STATE_POP, Query.ALL).getSchema().getCoordinateReferenceSystem();
        Query q = new Query(Query.ALL);
        q.setCoordinateSystemReproject(targetCRS);
        // used to get an exception here
        loadFeatures(STATE_POP, q);
    }

    @Test
    public void testLoadDanishChars() throws Exception {
        SimpleFeatureCollection fc = loadFeatures(DANISH, Query.ALL);
        SimpleFeature first = firstFeature(fc);

        // Charlotte (but with the o is stroked)
        assertEquals("Charl\u00F8tte", first.getAttribute("TEKST1"));
    }

    @Test
    public void testLoadChineseChars() throws Exception {
        try {
            SimpleFeatureCollection fc = loadFeatures(CHINESE, Charset.forName("GB18030"), null);
            SimpleFeature first = firstFeature(fc);
            String s = (String) first.getAttribute("NAME");
            assertEquals("\u9ed1\u9f99\u6c5f\u7701", s);
        } catch (UnsupportedCharsetException notInstalledInJRE) {
            // this just means you have not installed
            // chinese support into your JRE
            // (as such it represents a bad configuration
            // rather than a test failure)
            // we only wanted to ensure that if you have Chinese support
            // available - GeoTools can use it
        }
    }

    @Test
    public void testLoadRussianChars() throws Exception {
        try {
            SimpleFeatureCollection fc = loadFeatures(RUSSIAN, Charset.forName("CP1251"), null);
            SimpleFeatureIterator features = fc.features();
            SimpleFeature f = features.next();
            assertEquals(
                    "\u041A\u0438\u0440\u0438\u043B\u043B\u0438\u0446\u0430",
                    f.getAttribute("TEXT"));
            f = features.next();
            assertEquals(
                    "\u0421\u043C\u0435\u0448\u0430\u043D\u044B\u0439 12345",
                    f.getAttribute("TEXT"));
            features.close();
        } catch (UnsupportedCharsetException notInstalledInJRE) {
            // this just means you have not installed Russian support into your JRE
            // (as such it represents a bad configuration rather than a test failure)
        }
    }

    /**
     * This is just another approach to open the shapefile and pass the Charset If the Shape is
     * opened with "ISO-8859-1", the single UTF-8 encoded is shown as two ugly chars. As expected,
     * because it's UTF8 two-byte.
     */
    @Test
    public void testLoadingAndReadingUTF8Wrongly() throws Exception {
        SimpleFeatureCollection features = loadFeatures(UTF8, Charset.forName("ISO-8859-1"), null);

        SimpleFeatureIterator iterator = features.features();
        assertTrue(iterator.hasNext());
        assertEquals(4, features.size());
        SimpleFeature f = iterator.next();
        iterator.close();

        // GEOM, NAME,C,100   VISUAL,C,3      NUM1,N,5        NUM2,N,5
        assertEquals(5, f.getAttributeCount());

        String nameAttribute = (String) f.getAttribute("NAME");

        // We expect that the UTF8 is not understood here and there will be one extra char for the
        // misinterpreted special char
        assertEquals("Iconfee Stra\u00dfe".length() + 1, nameAttribute.length());
    }

    /**
     * This is just another approach to open the shapefile and pass the Charset Now we open the
     * shape with UTF8 charset and expect the attribute to be correctly retuned with 4 chars and
     * including the german special character.
     */
    @Test
    public void testLoadingAndReadingUTF8Correctly() throws Exception {
        SimpleFeatureCollection features = loadFeatures(UTF8, Charset.forName("UTF8"), null);

        SimpleFeatureIterator iterator = features.features();
        assertTrue(iterator.hasNext());
        assertEquals(4, features.size());
        SimpleFeature f = iterator.next();
        iterator.close();

        // GEOM, NAME,C,100   VISUAL,C,3      NUM1,N,5        NUM2,N,5
        assertEquals(5, f.getAttributeCount());

        String nameAttribute = (String) f.getAttribute("NAME");

        // We expect that the UTF8 is not understood here
        assertEquals("Iconfee Stra\u00dfe".length(), nameAttribute.length());
        assertEquals("Iconfee Stra\u00dfe", nameAttribute);
    }

    @Test
    public void testNamespace() throws Exception {
        ShapefileDataStoreFactory factory = new ShapefileDataStoreFactory();
        Map map = new HashMap();

        URI namespace = new URI("http://jesse.com");

        map.put(ShapefileDataStoreFactory.NAMESPACEP.key, namespace);
        map.put(ShapefileDataStoreFactory.URLP.key, TestData.url(STATE_POP));

        ShapefileDataStore store = (ShapefileDataStore) factory.createDataStore(map);
        FeatureType schema = store.getSchema();
        assertEquals(namespace.toString(), schema.getName().getNamespaceURI());
        store.dispose();
    }

    @Test
    public void testSchema() throws Exception {
        URL url = TestData.url(STATE_POP);
        ShapefileDataStore shapeDataStore = new ShapefileDataStore(url);
        String typeName = shapeDataStore.getTypeNames()[0];
        SimpleFeatureType schema = shapeDataStore.getSchema(typeName);
        List<AttributeDescriptor> attributes = schema.getAttributeDescriptors();
        assertEquals("Number of Attributes", 253, attributes.size());
        shapeDataStore.dispose();
    }

    @Test
    public void testCustomAttributeInSchema() throws Exception {
        File shp = org.geotools.test.TestData.file(this, "dumper");
        shp = new File(shp, "custom.shp");
        SimpleFeatureTypeBuilder ftBuilder = new SimpleFeatureTypeBuilder();
        ftBuilder.setName("custom");
        ftBuilder.setCRS(DefaultGeographicCRS.WGS84);
        ftBuilder.add("defaultGeom", Point.class, DefaultGeographicCRS.WGS84);
        ftBuilder.setDefaultGeometry("defaultGeom");
        ftBuilder.add("custom", CustomTypeClass.class);

        SimpleFeatureType type = ftBuilder.buildFeatureType();
        URL shapeUrl = URLs.fileToUrl(shp);
        ShapefileDataStore shapeDataStore = new ShapefileDataStore(shapeUrl);

        // Before the update, this call would have thrown an IOException
        // due to no mapping found for custom class, failing the test
        shapeDataStore.createSchema(type);

        ListFeatureCollection collection = new ListFeatureCollection(type);
        SimpleFeatureBuilder builder = new SimpleFeatureBuilder(type);
        Object[] values =
                new Object[] {
                    new GeometryFactory().createPoint(new Coordinate(10, 10)),
                    new CustomTypeClass(20)
                };
        builder.addAll(values);

        SimpleFeature feature = builder.buildFeature(type.getTypeName() + '.' + 0);
        collection.add(feature);

        FeatureStore store =
                (FeatureStore) shapeDataStore.getFeatureSource(type.getName().getLocalPart());
        DefaultTransaction transaction = new DefaultTransaction("create");
        store.setTransaction(transaction);
        store.addFeatures(collection);
        transaction.commit();
        shapeDataStore.dispose();

        // Now read it back
        shapeDataStore = new ShapefileDataStore(shapeUrl);
        SimpleFeatureCollection featureCollection = loadFeatures(shapeDataStore);
        assertTrue(
                String.class.equals(
                        featureCollection.features().next().getAttribute("custom").getClass()));
        shapeDataStore.dispose();
    }

    @Test
    public void testSpacesInPath() throws Exception {
        URL u = TestData.url(TestCaseSupport.class, "folder with spaces/pointtest.shp");
        File f = URLs.urlToFile(u);
        assertTrue(f.exists());
        ShapefileDataStore s = new ShapefileDataStore(u);
        loadFeatures(s);
        s.dispose();
    }

    /** Test envelope versus old DataSource */
    @Test
    public void testEnvelope() throws Exception {
        SimpleFeatureCollection features = loadFeatures(STATE_POP, Query.ALL);
        ShapefileDataStore s = new ShapefileDataStore(TestData.url(STATE_POP));
        String typeName = s.getTypeNames()[0];
        SimpleFeatureCollection all = s.getFeatureSource(typeName).getFeatures();

        assertEquals(features.getBounds(), all.getBounds());
        s.dispose();
    }

    @Test
    public void testCreateAndReadQIX() throws Exception {
        File shpFile = copyShapefiles(STATE_POP);
        URL url = shpFile.toURI().toURL();

        String name = shpFile.getName();
        File file =
                new File(shpFile.getParent(), name.substring(0, name.lastIndexOf('.')) + ".qix");

        if (file.exists()) {
            file.delete();
        }
        file.deleteOnExit();

        ShapefileDataStore ds = new ShapefileDataStore(url);
        SimpleFeatureCollection features = ds.getFeatureSource().getFeatures();
        SimpleFeatureIterator indexIter = features.features();

        GeometryFactory factory = new GeometryFactory();
        double area = Double.MAX_VALUE;
        SimpleFeature smallestFeature = null;
        while (indexIter.hasNext()) {
            SimpleFeature newFeature = indexIter.next();

            BoundingBox bounds = newFeature.getBounds();
            Geometry geometry = factory.toGeometry(new ReferencedEnvelope(bounds));
            double newArea = geometry.getArea();

            if (smallestFeature == null || newArea < area) {
                smallestFeature = newFeature;
                area = newArea;
            }
        }
        indexIter.close();

        ShapefileDataStore ds2 = new ShapefileDataStore(url);
        ds2.setIndexed(false);

        // reduce the bounds, thus making the store use the spatial index
        Envelope newBounds = ds.getFeatureSource().getBounds(Query.ALL);
        double dx = newBounds.getWidth() / 4;
        double dy = newBounds.getHeight() / 4;
        newBounds =
                new Envelope(
                        newBounds.getMinX() + dx,
                        newBounds.getMaxX() - dx,
                        newBounds.getMinY() + dy,
                        newBounds.getMaxY() - dy);

        CoordinateReferenceSystem crs = features.getSchema().getCoordinateReferenceSystem();

        performQueryComparison(ds, ds2, new ReferencedEnvelope(newBounds, crs));
        performQueryComparison(ds, ds2, new ReferencedEnvelope(smallestFeature.getBounds()));

        assertTrue(file.exists());
        ds.dispose();
        ds2.dispose();
    }

    @Test
    public void testRemove() throws Exception {
        File shpFile = copyShapefiles(STATE_POP);
        assertTrue(shpFile.exists());
        URL url = shpFile.toURI().toURL();

        ShapefileDataStore ds = new ShapefileDataStore(url);
        ds.removeSchema(ds.getSchema().getTypeName());
        ds.dispose();

        assertFalse(shpFile.exists());
        assertFalse(sibling(shpFile, ".shx").exists());
        assertFalse(sibling(shpFile, ".dbf").exists());
        assertFalse(sibling(shpFile, ".prj").exists());
        assertFalse(sibling(shpFile, ".qix").exists());
        assertFalse(sibling(shpFile, ".fix").exists());
    }

    @Test
    public void testSelectionQuery() throws Exception {
        File shpFile = copyShapefiles(STATE_POP);
        URL url = shpFile.toURI().toURL();
        ShapefileDataStore ds = new ShapefileDataStore(url);
        ds.setIndexed(false);
        SimpleFeatureSource featureSource = ds.getFeatureSource();
        SimpleFeatureType schema = featureSource.getSchema();
        Query query = new Query(schema.getTypeName());
        query.setPropertyNames(new String[0]);
        SimpleFeatureCollection features = featureSource.getFeatures(query);

        assertNotNull("selection query worked", features);
        assertFalse("selection non empty", features.isEmpty());
        assertTrue("selection non empty", features.size() > 0);
        ReferencedEnvelope bounds = features.getBounds();

        final Set<FeatureId> selection = new LinkedHashSet<FeatureId>();
        features.accepts(
                new FeatureVisitor() {
                    public void visit(Feature feature) {
                        selection.add(feature.getIdentifier());
                    }
                },
                null);
        assertFalse(selection.isEmpty());

        // try with filter and no attributes
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
        String geomName = schema.getGeometryDescriptor().getName().getLocalPart();

        query.setFilter(ff.bbox(ff.property(geomName), bounds));
        features = featureSource.getFeatures(query);

        assertNotNull("selection query worked", features);
        assertTrue("selection non empty", !features.isEmpty());
        assertEquals(selection.size(), features.size());
        ds.dispose();
    }

    @Test
    public void testQueryBboxNonGeomAttributes() throws Exception {
        File shpFile = copyShapefiles(STATE_POP);
        URL url = shpFile.toURI().toURL();
        ShapefileDataStore ds = new ShapefileDataStore(url);
        SimpleFeatureSource fs = ds.getFeatureSource();

        // build a query that extracts no geom but uses a bbox filter
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
        Query q = new Query();
        q.setPropertyNames(new String[] {"STATE_NAME", "PERSONS"});
        ReferencedEnvelope queryBounds =
                new ReferencedEnvelope(-75.102613, -72.361859, 40.212597, 41.512517, null);
        q.setFilter(ff.bbox(ff.property(""), queryBounds));

        // Read schema should contain the geometry property
        assertEquals(3, ((ShapefileFeatureStore) fs).delegate.getReadSchema(q).getAttributeCount());
        // Result schema should not contain the geometry property
        assertEquals(
                2, ((ShapefileFeatureStore) fs).delegate.getResultSchema(q).getAttributeCount());

        // grab the features
        SimpleFeatureCollection fc = fs.getFeatures(q);
        assertTrue(fc.size() > 0);
        ds.dispose();
    }

    @Test
    public void testFidFilter() throws Exception {
        File shpFile = copyShapefiles(STATE_POP);
        URL url = shpFile.toURI().toURL();
        ShapefileDataStore ds = new ShapefileDataStore(url);
        SimpleFeatureSource featureSource = ds.getFeatureSource();
        SimpleFeatureCollection features = featureSource.getFeatures();
        SimpleFeatureIterator indexIter = features.features();

        Set<String> expectedFids = new LinkedHashSet<String>();
        final Filter fidFilter;
        try {
            FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
            Set<FeatureId> fids = new HashSet<FeatureId>();
            while (indexIter.hasNext()) {
                SimpleFeature newFeature = indexIter.next();
                String id = newFeature.getID();
                expectedFids.add(id);
                fids.add(ff.featureId(id));
            }
            fidFilter = ff.id(fids);
        } finally {
            indexIter.close();
        }

        Set<String> actualFids = new HashSet<String>();
        {
            features = featureSource.getFeatures(fidFilter);
            try {
                indexIter = features.features();
                while (indexIter.hasNext()) {
                    SimpleFeature next = indexIter.next();
                    String id = next.getID();
                    actualFids.add(id);
                }
            } finally {
                if (indexIter != null) {
                    indexIter.close();
                }
            }
        }

        TreeSet<String> lackingFids = new TreeSet<String>(expectedFids);
        lackingFids.removeAll(actualFids);

        TreeSet<String> unexpectedFids = new TreeSet<String>(actualFids);
        unexpectedFids.removeAll(expectedFids);

        String lacking = String.valueOf(lackingFids);
        String unexpected = String.valueOf(unexpectedFids);
        String failureMsg = "lacking fids: " + lacking + ". Unexpected ones: " + unexpected;
        assertEquals(failureMsg, expectedFids.size(), actualFids.size());
        assertEquals(failureMsg, expectedFids, actualFids);
        ds.dispose();
    }

    @Test
    public void testGeometryFilter() throws Exception {
        File shpFile = copyShapefiles(STREAM);
        URL url = shpFile.toURI().toURL();
        ShapefileDataStore ds = new ShapefileDataStore(url);
        SimpleFeatureSource featureSource = ds.getFeatureSource();
        SimpleFeatureCollection features; // = featureSource.getFeatures();

        GeometryFactory geometryFactory = new GeometryFactory();
        Coordinate coordinate = new Coordinate(-99.0, 38.0);
        Point p = geometryFactory.createPoint(coordinate);
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
        final Filter testFilter = ff.intersects(ff.literal(p), ff.property("the_geom"));
        // System.out.println(testFilter);
        features = featureSource.getFeatures(testFilter);
        assertNotNull(features);
    }

    private ArrayList performQueryComparison(
            ShapefileDataStore indexedDS,
            ShapefileDataStore baselineDS,
            ReferencedEnvelope newBounds)
            throws FactoryRegistryException, IllegalFilterException, IOException {
        SimpleFeatureCollection features;
        SimpleFeatureIterator indexIter;
        FilterFactory2 fac = CommonFactoryFinder.getFilterFactory2(null);
        String geometryName = indexedDS.getSchema().getGeometryDescriptor().getLocalName();

        Filter filter = fac.bbox(fac.property(geometryName), newBounds);

        features = indexedDS.getFeatureSource().getFeatures(filter);
        SimpleFeatureCollection features2 = baselineDS.getFeatureSource().getFeatures(filter);

        SimpleFeatureIterator baselineIter = features2.features();
        indexIter = features.features();

        ArrayList baselineFeatures = new ArrayList();
        ArrayList indexedFeatures = new ArrayList();

        try {
            while (baselineIter.hasNext()) {
                baselineFeatures.add(baselineIter.next());
            }
            while (indexIter.hasNext()) {
                indexedFeatures.add(indexIter.next());
            }
            assertFalse(indexIter.hasNext());
            assertFalse(baselineIter.hasNext());
            assertEquals(baselineFeatures.size(), indexedFeatures.size());
            for (Iterator it = baselineFeatures.iterator(); it.hasNext(); ) {
                SimpleFeature f = (SimpleFeature) it.next();
                assertTrue(
                        f.getID() + ((Geometry) f.getDefaultGeometry()).getEnvelopeInternal(),
                        indexedFeatures.contains(f));
            }
        } finally {
            indexIter.close();
            baselineIter.close();
        }
        return indexedFeatures;
    }

    @Test
    public void testLoadAndVerify() throws Exception {
        SimpleFeatureCollection features = loadFeatures(STATE_POP, Query.ALL);
        // SimpleFeatureCollection features = loadFeaturesM2();
        int count = features.size();

        assertTrue("Have features", count > 0);
        // assertEquals("Number of Features loaded",49,features.size()); // FILE
        // (correct value)
        // assertEquals("Number of Features loaded",3, count); // JAR

        SimpleFeature firstFeature = firstFeature(features);
        SimpleFeatureType schema = firstFeature.getFeatureType();
        assertNotNull(schema.getGeometryDescriptor());
        assertEquals("Number of Attributes", 253, schema.getAttributeCount());
        assertEquals(
                "Value of statename is wrong", "Illinois", firstFeature.getAttribute("STATE_NAME"));
        assertEquals(
                "Value of land area is wrong",
                143986.61,
                ((Double) firstFeature.getAttribute("LAND_KM")).doubleValue(),
                0.001);
    }

    @Test
    public void testLoadAndCheckParentTypeIsPolygon() throws Exception {
        SimpleFeatureCollection features = loadFeatures(STATE_POP, Query.ALL);
        SimpleFeatureType schema = firstFeature(features).getFeatureType();

        assertTrue(FeatureTypes.isDecendedFrom(schema, BasicFeatureTypes.POLYGON));
        assertTrue(FeatureTypes.isDecendedFrom(schema, BasicFeatureTypes.POLYGON));
        assertTrue(
                FeatureTypes.isDecendedFrom(
                        schema, FeatureTypes.DEFAULT_NAMESPACE, "polygonFeature"));
    }

    @Test
    public void testCreateSchemaWithEmptyCRS() throws Exception {
        File file = new File("test.shp");
        URL toURL = file.toURI().toURL();
        ShapefileDataStore ds = new ShapefileDataStore(toURL);
        ds.createSchema(DataUtilities.createType("test", "geom:MultiPolygon"));

        assertEquals("test", ds.getSchema().getTypeName());
        ds.dispose();

        file.deleteOnExit();
        file = new File("test.dbf");
        file.deleteOnExit();
        file = new File("test.shp");
        file.deleteOnExit();

        file = new File("test.prj");
        if (file.exists()) file.deleteOnExit();

        file = new File("test.shx");
        if (file.exists()) {
            file.deleteOnExit();
        }
    }

    @Test
    public void testCreateSchemaWithCRS() throws Exception {
        File file = new File("test.shp");
        URL toURL = file.toURI().toURL();
        ShapefileDataStore ds = new ShapefileDataStore(toURL);
        SimpleFeatureType featureType =
                DataUtilities.createType("test", "geom:MultiPolygon:srid=32615");
        CoordinateReferenceSystem crs =
                featureType.getGeometryDescriptor().getCoordinateReferenceSystem();
        assertNotNull(crs);

        ds.createSchema(featureType);

        assertEquals("test", ds.getSchema().getTypeName());

        CoordinateReferenceSystem crs2 =
                ds.getSchema().getGeometryDescriptor().getCoordinateReferenceSystem();
        assertNotNull(crs2);
        assertTrue(CRS.equalsIgnoreMetadata(crs, crs2));
        ds.dispose();

        file.deleteOnExit();
        file = new File("test.dbf");
        file.deleteOnExit();
        file = new File("test.shp");
        file.deleteOnExit();

        file = new File("test.prj");
        if (file.exists()) file.deleteOnExit();

        file = new File("test.shx");
        if (file.exists()) {
            file.deleteOnExit();
        }

        file = new File("test.prj");
        if (file.exists()) {
            file.deleteOnExit();
        }
    }

    @Test
    public void testCreateSchemaWithPolarStereographics() throws Exception {
        File file = new File("test.shp");
        URL toURL = file.toURI().toURL();
        ShapefileDataStore ds = new ShapefileDataStore(toURL);
        SimpleFeatureType featureType =
                DataUtilities.createType("test", "geom:MultiPolygon:srid=3031");
        CoordinateReferenceSystem crs =
                featureType.getGeometryDescriptor().getCoordinateReferenceSystem();
        assertNotNull(crs);

        ds.createSchema(featureType);

        assertEquals("test", ds.getSchema().getTypeName());

        CoordinateReferenceSystem crs2 =
                ds.getSchema().getGeometryDescriptor().getCoordinateReferenceSystem();
        assertNotNull(crs2);
        assertEquals(crs.getName().getCode(), crs2.getName().getCode());
        ds.dispose();

        file.deleteOnExit();
        file = new File("test.dbf");
        file.deleteOnExit();
        file = new File("test.shp");
        file.deleteOnExit();

        file = new File("test.prj");
        if (file.exists()) file.deleteOnExit();

        file = new File("test.shx");
        if (file.exists()) {
            file.deleteOnExit();
        }

        file = new File("test.prj");
        if (file.exists()) {
            file.deleteOnExit();
        }
    }

    @Test
    public void testForceCRS() throws Exception {
        File file = new File("test.shp");
        URL toURL = file.toURI().toURL();

        ShapefileDataStore ds = new ShapefileDataStore(toURL);
        ds.createSchema(DataUtilities.createType("test", "geom:MultiPolygon"));
        FeatureType before = ds.getSchema();

        ds.forceSchemaCRS(CRS.decode("EPSG:3005"));
        FeatureType after = ds.getSchema();
        ds.dispose();

        assertNotSame(before, after);
        assertNull("4326", before.getCoordinateReferenceSystem());
        assertEquals("NAD83 / BC Albers", after.getCoordinateReferenceSystem().getName().getCode());

        file.deleteOnExit();
        file = new File("test.dbf");
        file.deleteOnExit();
        file = new File("test.shp");
        file.deleteOnExit();

        file = new File("test.prj");

        if (file.exists()) file.deleteOnExit();

        file = new File("test.shx");
        if (file.exists()) file.deleteOnExit();
    }

    private ShapefileDataStore createDataStore(File f) throws Exception {
        SimpleFeatureCollection fc = createFeatureCollection();
        ShapefileDataStore sds = new ShapefileDataStore(f.toURI().toURL());
        writeFeatures(sds, fc);
        return sds;
    }

    private ShapefileDataStore createDataStore() throws Exception {
        return createDataStore(getTempFile());
    }

    /**
     * Create a set of features, then remove every other one, updating the remaining. Test for
     * removal and proper update after reloading...
     */
    /* Note that, when reading the DBF part of shape file set, the type of an N,0 feature will be inferred to be
     * Long; this may cause data loss when reading a file created with a BigInteger feature.
     */
    @Test
    public void testUpdating() throws Throwable {
        ShapefileDataStore sds = createDataStore();
        loadFeatures(sds);

        FeatureWriter<SimpleFeatureType, SimpleFeature> writer = null;
        try {
            writer =
                    sds.getFeatureWriter(
                            sds.getTypeNames()[0], Filter.INCLUDE, Transaction.AUTO_COMMIT);
            while (writer.hasNext()) {
                SimpleFeature feat = writer.next();
                Integer b = (Integer) feat.getAttribute(1);
                if (b.byteValue() % 2 == 0) {
                    writer.remove();
                } else {
                    feat.setAttribute(1, Byte.valueOf((byte) -1));
                }
            }
        } finally {
            if (writer != null) writer.close();
        }
        SimpleFeatureCollection fc = loadFeatures(sds);

        assertEquals(10, fc.size());
        SimpleFeatureIterator features = null;
        try {
            features = fc.features();
            for (SimpleFeatureIterator i = features; i.hasNext(); ) {
                assertEquals(-1, ((Integer) i.next().getAttribute(1)).byteValue());
            }
        } finally {
            if (features != null) {
                features.close();
            }
        }
        sds.dispose();
    }

    @Test
    public void testUpdateMultipleAttributesNoAutocommit() throws Exception {
        // create feature type
        SimpleFeatureType type =
                DataUtilities.createType(
                        "junk", "the_geom:Point,b:java.lang.Integer,c:java.lang.Integer");
        DefaultFeatureCollection features = new DefaultFeatureCollection();

        SimpleFeatureBuilder build = new SimpleFeatureBuilder(type);
        SimpleFeature feature = null;
        for (int i = 0; i < 3; i++) {
            build.add(new GeometryFactory().createPoint(new Coordinate(i, i)));
            build.add(i);
            feature = build.buildFeature(null);
            features.add(feature);
        }

        // store features
        File tmpFile = getTempFile();
        tmpFile.createNewFile();
        ShapefileDataStore s = new ShapefileDataStore(tmpFile.toURI().toURL());
        writeFeatures(s, features);

        // read them back
        FeatureReader<SimpleFeatureType, SimpleFeature> reader = s.getFeatureReader();
        // System.out.println(DataUtilities.list(features));
        reader.close();

        Transaction transaction = new DefaultTransaction();
        SimpleFeatureStore store =
                (SimpleFeatureStore) s.getFeatureSource(s.getSchema().getTypeName(), transaction);

        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
        Query query = new Query(s.getSchema().getTypeName());
        for (int i = 0; i < 3; i++) {
            query.setFilter(ff.equal(ff.property("b"), ff.literal(i), true));
            store.modifyFeatures(
                    new String[] {"b", "c"}, new Integer[] {-1 * i, i}, query.getFilter());
        }

        transaction.commit();

        reader = s.getFeatureReader();
        Set<Object> numOfDistinctValues = new HashSet<Object>();
        try {
            while (reader.hasNext()) {
                SimpleFeature f = reader.next();
                // System.out.println(f);
                assertEquals(f.getAttribute("b"), -1 * (Integer) f.getAttribute("c"));
                numOfDistinctValues.add(f.getAttribute("b"));
            }
            // ensure that each feature has a distinct value for attribute 'b'
            assertEquals(
                    "Wrong number of distinct values for attribute 'b'",
                    store.getFeatures().size(),
                    numOfDistinctValues.size());
        } finally {
            reader.close();
        }
        s.dispose();
    }

    /**
     * Create a test file, then continue removing the first entry until there are no features left.
     */
    @Test
    public void testRemoveFromFrontAndClose() throws Throwable {
        ShapefileDataStore sds = createDataStore();

        int idx = loadFeatures(sds).size();

        while (idx > 0) {
            FeatureWriter<SimpleFeatureType, SimpleFeature> writer = null;

            try {
                writer =
                        sds.getFeatureWriter(
                                sds.getTypeNames()[0], Filter.INCLUDE, Transaction.AUTO_COMMIT);
                SimpleFeature feature = writer.next();
                // System.out.println(feature);
                writer.remove();
            } finally {
                if (writer != null) {
                    writer.close();
                    writer = null;
                }
            }
            assertEquals(--idx, loadFeatures(sds).size());
        }
        sds.dispose();
    }

    /**
     * Create a test file, then continue removing the first entry until there are no features left.
     */
    @Test
    public void testRemoveFromFrontAndCloseTransaction() throws Throwable {
        ShapefileDataStore sds = createDataStore();

        int idx = loadFeatures(sds).size();

        while (idx > 0) {
            Transaction t = new DefaultTransaction();
            FeatureWriter<SimpleFeatureType, SimpleFeature> writer = null;

            try {
                writer = sds.getFeatureWriter(sds.getTypeNames()[0], Filter.INCLUDE, t);
                writer.next();
                writer.remove();
            } finally {
                if (writer != null) {
                    writer.close();
                    writer = null;
                }
            }
            t.commit();
            t.close();
            assertEquals(--idx, loadFeatures(sds).size());
        }
        sds.dispose();
    }

    /**
     * Create a test file, then continue removing the last entry until there are no features left.
     */
    @Test
    public void testRemoveFromBackAndClose() throws Throwable {
        ShapefileDataStore sds = createDataStore();

        int idx = loadFeatures(sds).size();

        while (idx > 0) {
            FeatureWriter<SimpleFeatureType, SimpleFeature> writer = null;
            try {
                writer =
                        sds.getFeatureWriter(
                                sds.getTypeNames()[0], Filter.INCLUDE, Transaction.AUTO_COMMIT);
                while (writer.hasNext()) {
                    writer.next();
                }
                writer.remove();
            } finally {
                if (writer != null) {
                    writer.close();
                    writer = null;
                }
            }
            assertEquals(--idx, loadFeatures(sds).size());
        }
        sds.dispose();
    }

    @Test
    public void testWriteShapefileWithNoRecords() throws Exception {
        SimpleFeatureType featureType = DataUtilities.createType("whatever", "a:Polygon,b:String");

        File tempFile = getTempFile();
        ShapefileDataStore shapefileDataStore = new ShapefileDataStore(tempFile.toURI().toURL());
        shapefileDataStore.createSchema(featureType);

        FeatureWriter<SimpleFeatureType, SimpleFeature> featureWriter =
                shapefileDataStore.getFeatureWriter(
                        shapefileDataStore.getTypeNames()[0], Transaction.AUTO_COMMIT);

        // don't add any features to the data store....

        // this should create a shapefile with no records. Not sure about the
        // semantics of this,
        // but it's meant to be used in the context of a FeatureCollection
        // iteration,
        // where the SimpleFeatureCollection has nothing in it.
        featureWriter.close();
        shapefileDataStore.dispose();
    }

    @Test
    public void testTransaction() throws Exception {
        ShapefileDataStore sds = createDataStore();

        int idx = sds.getFeatureSource().getCount(Query.ALL);

        SimpleFeatureStore store = (SimpleFeatureStore) sds.getFeatureSource(sds.getTypeNames()[0]);

        Transaction transaction = new DefaultTransaction();
        store.setTransaction(transaction);
        SimpleFeature[] newFeatures1 = new SimpleFeature[1];
        SimpleFeature[] newFeatures2 = new SimpleFeature[2];
        GeometryFactory fac = new GeometryFactory();
        newFeatures1[0] = DataUtilities.template(sds.getSchema());
        newFeatures1[0].setDefaultGeometry(fac.createPoint(new Coordinate(0, 0)));
        newFeatures2[0] = DataUtilities.template(sds.getSchema());
        newFeatures2[0].setDefaultGeometry(fac.createPoint(new Coordinate(0, 0)));
        newFeatures2[1] = DataUtilities.template(sds.getSchema());
        newFeatures2[1].setDefaultGeometry(fac.createPoint(new Coordinate(0, 0)));

        store.addFeatures(DataUtilities.collection(newFeatures1));
        store.addFeatures(DataUtilities.collection(newFeatures2));
        transaction.commit();
        transaction.close();
        assertEquals(idx + 3, sds.getCount(Query.ALL));
        sds.dispose();
    }

    @Test
    public void testDeletedDbf() throws Exception {
        // this shapefile has 4 records that are marked as deleted only inside the dbf, but
        // not in the headers
        URL u = TestData.url(TestCaseSupport.class, "deleted/archsites.dbf");
        File shpFile = URLs.urlToFile(u);

        ShapefileDataStore store = new ShapefileDataStore(URLs.fileToUrl(shpFile));
        ContentFeatureSource fs = store.getFeatureSource();
        // this one reads the shp header, which still contains trace of all records
        assertEquals(25, fs.getCount(Query.ALL));
        // now read manually and check we skip the records with the dbf entry marked as deleted
        SimpleFeatureIterator fi = fs.getFeatures().features();
        int count = 0;
        while (fi.hasNext()) {
            fi.next();
            count++;
        }
        fi.close();
        assertEquals(21, count);
    }

    /**
     * Creates feature collection with all the stuff we care about from simple types, to Geometry
     * and date.
     *
     * <p>As we care about supporting more stuff please add on to the end of this list...
     *
     * @return SimpleFeatureCollection For use in testing.
     */
    private SimpleFeatureCollection createFeatureCollection() throws Exception {
        SimpleFeatureType featureType = createExampleSchema();
        SimpleFeatureBuilder build = new SimpleFeatureBuilder(featureType);

        DefaultFeatureCollection features = new DefaultFeatureCollection();
        for (int i = 0, ii = 20; i < ii; i++) {

            build.add(new GeometryFactory().createPoint(new Coordinate(1, -1)));
            build.add(Byte.valueOf((byte) i));
            build.add(Short.valueOf((short) i));
            build.add(Double.valueOf(i));
            build.add(Float.valueOf(i));
            build.add(new String(i + " "));
            build.add(new Date(i));
            build.add(Boolean.valueOf(true));
            build.add(Integer.valueOf(22));
            build.add(Long.valueOf(1234567890123456789L));
            build.add(new BigDecimal(new BigInteger("12345678901234567890123456789"), 2));
            build.add(new BigInteger("12345678901234567890123456789"));
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTimeInMillis(i);
            build.add(calendar);
            build.add(UUID.randomUUID());

            SimpleFeature feature = build.buildFeature(null);
            features.add(feature);
        }
        return features;
    }

    private SimpleFeatureType createExampleSchema() {
        SimpleFeatureTypeBuilder build = new SimpleFeatureTypeBuilder();
        build.setName("junk");
        build.add("a", Point.class);
        build.add("b", Byte.class);
        build.add("c", Short.class);
        build.add("d", Double.class);
        build.add("e", Float.class);
        build.add("f", String.class);
        build.add("g", Date.class);
        build.add("h", Boolean.class);
        build.add("i", Number.class);
        build.add("j", Long.class);
        build.add("k", BigDecimal.class);
        build.add("l", BigInteger.class);
        build.add("m", Calendar.class);
        build.add("n", UUID.class);
        build.add("o", byte[].class);

        return build.buildFeatureType();
    }

    @Test
    public void testAttributesWriting() throws Exception {
        SimpleFeatureCollection features = createFeatureCollection();
        File tmpFile = getTempFile();
        tmpFile.createNewFile();
        ShapefileDataStore s = new ShapefileDataStore(tmpFile.toURI().toURL());
        writeFeatures(s, features);
        s.dispose();
    }

    @Test
    public void testWriteReadStandardNumbers() throws Exception {
        // create feature type
        SimpleFeatureType type =
                DataUtilities.createType("junk", "a:Point,b:java.lang.Float,c:java.lang.Double");
        DefaultFeatureCollection features = new DefaultFeatureCollection();

        Double aFloat = 123456.78901234567890123456789;
        Double aDouble = 1234567890.123456789;

        SimpleFeatureBuilder build = new SimpleFeatureBuilder(type);
        build.add(new GeometryFactory().createPoint(new Coordinate(1, -1)));
        build.add(aFloat);
        build.add(aDouble);

        SimpleFeature feature = build.buildFeature(null);
        features.add(feature);

        // store features
        File tmpFile = getTempFile();
        tmpFile.createNewFile();
        ShapefileDataStore s = new ShapefileDataStore(tmpFile.toURI().toURL());
        writeFeatures(s, features);

        // read them back
        FeatureReader<SimpleFeatureType, SimpleFeature> reader = s.getFeatureReader();
        try {
            SimpleFeature f = reader.next();

            assertEquals("Float", aFloat, (Double) f.getAttribute("b"), 0.0001);
            assertEquals("Double", aDouble, f.getAttribute("c"));
        } finally {
            reader.close();
        }
        s.dispose();
    }

    @Test
    public void testWriteReadBigNumbers() throws Exception {
        // create feature type
        SimpleFeatureType type =
                DataUtilities.createType(
                        "junk", "a:Point,b:java.math.BigDecimal,c:java.math.BigInteger");
        DefaultFeatureCollection features = new DefaultFeatureCollection();

        BigInteger bigInteger = new BigInteger("1234567890123456789");
        BigDecimal bigDecimal = new BigDecimal(bigInteger, 2);

        SimpleFeatureBuilder build = new SimpleFeatureBuilder(type);
        build.add(new GeometryFactory().createPoint(new Coordinate(1, -1)));
        build.add(bigDecimal);
        build.add(bigInteger);

        SimpleFeature feature = build.buildFeature(null);
        features.add(feature);

        // store features
        File tmpFile = getTempFile();
        tmpFile.createNewFile();
        ShapefileDataStore s = new ShapefileDataStore(tmpFile.toURI().toURL());
        writeFeatures(s, features);

        // read them back
        FeatureReader<SimpleFeatureType, SimpleFeature> reader = s.getFeatureReader();
        try {
            SimpleFeature f = reader.next();

            assertEquals(
                    "big decimal",
                    bigDecimal.doubleValue(),
                    ((Number) f.getAttribute("b")).doubleValue(),
                    0.00001);
            assertEquals(
                    "big integer",
                    bigInteger.longValue(),
                    ((Number) f.getAttribute("c")).longValue(),
                    0.00001);
        } finally {
            reader.close();
        }
        s.dispose();
    }

    // We expect this to fail -- the test shows that unless we enable checking, we may suffer data
    // corruption
    @Test(expected = AssertionError.class)
    public void testWriteReadBiggerNumbers() throws Exception {
        // create feature type
        SimpleFeatureType type =
                DataUtilities.createType(
                        "junk", "a:Point,b:java.math.BigDecimal,c:java.math.BigInteger");
        DefaultFeatureCollection features = new DefaultFeatureCollection();

        BigInteger bigInteger = new BigInteger("12345678901234567890123456789");
        BigDecimal bigDecimal = new BigDecimal(bigInteger, 2);

        SimpleFeatureBuilder build = new SimpleFeatureBuilder(type);
        build.add(new GeometryFactory().createPoint(new Coordinate(1, -1)));
        build.add(bigDecimal);
        build.add(bigInteger);

        SimpleFeature feature = build.buildFeature(null);
        features.add(feature);

        // store features
        File tmpFile = getTempFile();
        tmpFile.createNewFile();
        System.clearProperty("org.geotools.shapefile.reportFieldSizeErrors");
        ShapefileDataStore s = new ShapefileDataStore(tmpFile.toURI().toURL());
        writeFeatures(s, features);

        // read them back
        FeatureReader<SimpleFeatureType, SimpleFeature> reader = s.getFeatureReader();
        try {
            SimpleFeature f = reader.next();

            assertEquals(
                    "big decimal",
                    bigDecimal.doubleValue(),
                    ((Number) f.getAttribute("b")).doubleValue(),
                    0.00001);
            assertEquals(
                    "big integer",
                    bigInteger.longValue(),
                    ((Number) f.getAttribute("c")).longValue(),
                    0.00001);
        } finally {
            reader.close();
        }
        s.dispose();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWriteBiggerNumbersWithCheck() throws Exception {
        // create feature type
        SimpleFeatureType type =
                DataUtilities.createType(
                        "junk", "a:Point,b:java.math.BigDecimal,c:java.math.BigInteger");
        DefaultFeatureCollection features = new DefaultFeatureCollection();

        BigInteger bigInteger = new BigInteger("12345678901234567890123456789");
        BigDecimal bigDecimal = new BigDecimal(bigInteger, 2);

        SimpleFeatureBuilder build = new SimpleFeatureBuilder(type);
        build.add(new GeometryFactory().createPoint(new Coordinate(1, -1)));
        build.add(bigDecimal);
        build.add(bigInteger);

        SimpleFeature feature = build.buildFeature(null);
        features.add(feature);

        // store features
        File tmpFile = getTempFile();
        tmpFile.createNewFile();
        System.setProperty("org.geotools.shapefile.reportFieldSizeErrors", "true");
        ShapefileDataStore s = new ShapefileDataStore(tmpFile.toURI().toURL());
        writeFeatures(s, features);

        // read them back
        FeatureReader<SimpleFeatureType, SimpleFeature> reader = s.getFeatureReader();
        try {
            SimpleFeature f = reader.next();

            assertEquals(
                    "big decimal",
                    bigDecimal.doubleValue(),
                    ((Number) f.getAttribute("b")).doubleValue(),
                    0.00001);
            assertEquals(
                    "big integer",
                    bigInteger.longValue(),
                    ((Number) f.getAttribute("c")).longValue(),
                    0.00001);
        } finally {
            reader.close();
        }
        s.dispose();
    }

    @Test
    public void testGeometriesWriting() throws Exception {

        String[] wktResources = new String[] {"line", "multiline", "polygon", "multipolygon"};

        for (int i = 0; i < wktResources.length; i++) {
            Geometry geom = readGeometry(wktResources[i]);
            String testName = wktResources[i];
            try {

                runWriteReadTest(geom, false);
                make3D(geom);
                testName += "3d";
                runWriteReadTest(geom, true);
            } catch (Throwable e) {
                throw new Exception("Error in " + testName, e);
            }
        }
    }

    private void make3D(Geometry g) {
        Coordinate[] c = g.getCoordinates();
        for (int i = 0, ii = c.length; i < ii; i++) {
            c[i].setZ(42 + i);
        }
    }

    private void writeFeatures(ShapefileDataStore s, SimpleFeatureCollection fc) throws Exception {
        s.createSchema(fc.features().next().getFeatureType());
        try (FeatureWriter<SimpleFeatureType, SimpleFeature> fw =
                        s.getFeatureWriter(s.getTypeNames()[0], Transaction.AUTO_COMMIT);
                SimpleFeatureIterator it = fc.features()) {
            while (it.hasNext()) {
                SimpleFeature feature = it.next();
                SimpleFeature newFeature = fw.next();
                newFeature.setAttributes(feature.getAttributes());

                fw.write();
            }
        }
    }

    private void runWriteReadTest(Geometry geom, boolean d3) throws Exception {
        // make features

        SimpleFeatureTypeBuilder ftb = new SimpleFeatureTypeBuilder();
        ftb.setName("Junk");
        ftb.add("a", geom.getClass());
        SimpleFeatureType type = ftb.buildFeatureType();

        DefaultFeatureCollection features = new DefaultFeatureCollection();
        SimpleFeatureBuilder build = new SimpleFeatureBuilder(type);
        for (int i = 0, ii = 20; i < ii; i++) {
            build.set(0, geom.copy());
            SimpleFeature feature = build.buildFeature(null);

            features.add(feature);
        }

        // set up file
        File tmpFile = getTempFile();
        tmpFile.delete();

        // write features
        ShapefileDataStore shapeDataStore = new ShapefileDataStore(tmpFile.toURI().toURL());
        shapeDataStore.createSchema(type);
        writeFeatures(shapeDataStore, features);
        shapeDataStore.dispose();

        // read features
        shapeDataStore = new ShapefileDataStore(tmpFile.toURI().toURL());
        SimpleFeatureCollection fc = loadFeatures(shapeDataStore);
        SimpleFeatureIterator fci = fc.features();
        // verify
        while (fci.hasNext()) {
            SimpleFeature f = fci.next();
            Geometry fromShape = (Geometry) f.getDefaultGeometry();

            if (fromShape instanceof GeometryCollection) {
                if (!(geom instanceof GeometryCollection)) {
                    fromShape = ((GeometryCollection) fromShape).getGeometryN(0);
                }
            }
            try {
                // check if the original is valid as we're going to fix unclosed rings
                // as we read them out of the shapefile
                if (geom.isValid()) {
                    Coordinate[] c1 = geom.getCoordinates();
                    Coordinate[] c2 = fromShape.getCoordinates();
                    for (int cc = 0, ccc = c1.length; cc < ccc; cc++) {
                        if (d3) assertTrue(c1[cc].equals3D(c2[cc]));
                        else assertTrue(c1[cc].equals2D(c2[cc]));
                    }
                }
            } catch (Throwable t) {
                fail(
                        "Bogus : "
                                + Arrays.asList(geom.getCoordinates())
                                + " : "
                                + Arrays.asList(fromShape.getCoordinates()));
            }
        }
        fci.close();
        tmpFile.delete();
        shapeDataStore.dispose();
    }

    @Test
    public void testGetCount() throws Exception {
        assertTrue(copyShapefiles(STREAM).canRead()); // The following test
        // seems to fail in the
        // URL point into the
        // JAR file.
        ShapefileDataStore store =
                new ShapefileDataStore(TestData.url(TestCaseSupport.class, STREAM));
        int count = 0;
        FeatureReader<SimpleFeatureType, SimpleFeature> reader = store.getFeatureReader();
        try {
            while (reader.hasNext()) {
                count++;
                reader.next();
            }
            assertEquals(count, store.getCount(Query.ALL));

            // check SimpleFeatureCollection size
            SimpleFeatureSource featureSource = store.getFeatureSource();
            SimpleFeatureCollection features = featureSource.getFeatures();
            assertEquals(count, features.size());
            assertFalse(features.isEmpty());

            // execute Query that returns all features

            FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
            SimpleFeatureType schema = featureSource.getSchema();
            String geomName = schema.getGeometryDescriptor().getName().getLocalPart();

            Query query = new Query(schema.getTypeName());
            query.setPropertyNames(Query.ALL_NAMES);
            ReferencedEnvelope bounds = features.getBounds();
            query.setFilter(ff.bbox(ff.property(geomName), bounds));

            features = featureSource.getFeatures(query);
            // check SimpleFeatureCollection size
            assertEquals(count, features.size());
            assertFalse(features.isEmpty());

            // execute Query that uses offset
            query = new Query(Query.ALL);
            query.setStartIndex(2);

            features = featureSource.getFeatures(query);
            assertEquals(count - 2, features.size());
            assertEquals(count - 2, featureSource.getCount(query));

            // execute Query that uses limit
            query = new Query(Query.ALL);
            query.setMaxFeatures(count / 2);

            features = featureSource.getFeatures(query);
            assertEquals(count / 2, features.size());
            assertEquals(count / 2, featureSource.getCount(query));

            // execute Query that uses both limit and offset
            query = new Query(Query.ALL);
            query.setMaxFeatures(count / 2);
            query.setStartIndex(2);

            features = featureSource.getFeatures(query);
            assertEquals(Math.min(count - 2, count / 2), features.size());
            assertEquals(Math.min(count - 2, count / 2), featureSource.getCount(query));

            // execute Query that doesn't return any feature
            query = new Query(Query.ALL);
            bounds =
                    new ReferencedEnvelope(
                            bounds.getMaxX() + 1,
                            bounds.getMaxX() + 2,
                            bounds.getMaxY() + 1,
                            bounds.getMaxY() + 2,
                            bounds.getCoordinateReferenceSystem());
            query.setFilter(ff.bbox(ff.property(geomName), bounds));

            features = featureSource.getFeatures(query);
            // check SimpleFeatureCollection size
            assertEquals(0, features.size());
            assertTrue(features.isEmpty());

        } finally {
            reader.close();
        }
        store.dispose();
    }

    /** Checks if feature reading optimizations still allow to execute the queries or not */
    @Test
    public void testGetReaderOptimizations() throws Exception {
        URL url = TestData.url(STATE_POP);
        ShapefileDataStore s = new ShapefileDataStore(url);

        // attributes other than geometry can be ignored here
        Query query =
                new Query(s.getSchema().getTypeName(), Filter.INCLUDE, new String[] {"the_geom"});
        FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                s.getFeatureReader(query, Transaction.AUTO_COMMIT);
        assertEquals(1, reader.getFeatureType().getAttributeCount());
        assertEquals("the_geom", reader.getFeatureType().getDescriptor(0).getLocalName());

        // here too, the filter is using the geometry only
        GeometryFactory gc = new GeometryFactory();
        LinearRing ring =
                gc.createLinearRing(
                        new Coordinate[] {
                            new Coordinate(0, 0), new Coordinate(10, 0),
                            new Coordinate(10, 10), new Coordinate(0, 10),
                            new Coordinate(0, 0)
                        });
        Polygon polygon = gc.createPolygon(ring, null);

        ReferencedEnvelope bounds = new ReferencedEnvelope(polygon.getEnvelopeInternal(), null);
        Filter gf = ff.bbox(ff.property("the_geom"), bounds);

        query = new Query(s.getSchema().getTypeName(), gf, new String[] {"the_geom"});

        reader.close();
        reader = s.getFeatureReader(query, Transaction.AUTO_COMMIT);
        assertEquals(1, reader.getFeatureType().getAttributeCount());
        assertEquals("the_geom", reader.getFeatureType().getDescriptor(0).getLocalName());

        reader.close();

        // here not, we need state_name in the feature type, so open the dbf
        // file please
        Filter cf = ff.equals(ff.property("STATE_NAME"), ff.literal("Illinois"));
        query = new Query(s.getSchema().getTypeName(), cf, new String[] {"the_geom"});
        reader = s.getFeatureReader(query, Transaction.AUTO_COMMIT);
        assertEquals(1, reader.getFeatureType().getAttributeCount());
        assertEquals("the_geom", reader.getFeatureType().getDescriptor(0).getLocalName());
        reader.close();
        s.dispose();
    }

    @Test
    public void testWrite() throws Exception {
        // create feature type
        SimpleFeatureType type =
                DataUtilities.createType(
                        "junk", "a:Point,b:java.math.BigDecimal,c:java.math.BigInteger");

        BigInteger bigInteger = new BigInteger("1234567890123456789");
        BigDecimal bigDecimal = new BigDecimal(bigInteger, 2);

        SimpleFeatureBuilder builder = new SimpleFeatureBuilder(type);
        builder.add(new GeometryFactory().createPoint(new Coordinate(1, -1)));
        builder.add(bigDecimal);
        builder.add(bigInteger);

        SimpleFeature feature = builder.buildFeature(null);
        ;

        // store features
        File tmpFile = getTempFile();
        tmpFile.createNewFile();
        ShapefileDataStore s = new ShapefileDataStore(tmpFile.toURI().toURL());
        s.createSchema(type);

        // was failing in GEOT-2427
        Transaction t = new DefaultTransaction();
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer =
                s.getFeatureWriter(s.getTypeNames()[0], t);
        SimpleFeature feature1 = writer.next();
        writer.close();
        s.dispose();
    }

    private void doTestReadWriteDate(String str_date) throws Exception {

        final boolean datetime_enabled = Boolean.getBoolean("org.geotools.shapefile.datetime");

        File file = org.geotools.test.TestData.temp(this, "timestamp.shp");

        URL toURL = file.toURI().toURL();

        ShapefileDataStore ds = new ShapefileDataStore(toURL);
        ds.setTimeZone(TimeZone.getTimeZone("UTC"));
        ds.createSchema(
                DataUtilities.createType(
                        "test",
                        "geom:Point,timestamp:java.util.Date,date:java.util.Date,timestamp2:java.util.Date,timestamp3:java.util.Date"));

        final FeatureWriter<SimpleFeatureType, SimpleFeature> fw;
        fw = ds.getFeatureWriterAppend(ds.getSchema().getTypeName(), Transaction.AUTO_COMMIT);
        final SimpleFeature sf;

        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd Z");

        Date date = dateFormatter.parse(str_date + " GMT");

        Calendar timestampCal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

        timestampCal.setTime(date);

        timestampCal.add(Calendar.MILLISECOND, 1);
        // Set timestamp 00:00:00.001 at the same day
        Date timestamp = timestampCal.getTime();

        timestampCal.add(Calendar.MILLISECOND, 12 * 60 * 60 * 1000);
        // Set timestamp2 12:00:00.001 at the same day
        Date timestamp2 = timestampCal.getTime();

        timestampCal.add(
                Calendar.MILLISECOND, 11 * 60 * 60 * 1000 + 59 * 60 * 1000 + 59 * 1000 + 998);
        // Set timestamp3 to  23:59:59.999 at the same day
        Date timestamp3 = timestampCal.getTime();

        // Write the values to the shapefile and close the datastore.
        sf = fw.next();
        sf.setAttribute(0, new GeometryFactory().createPoint(new Coordinate(1, -1)));
        sf.setAttribute(1, timestamp);
        sf.setAttribute(2, date);
        sf.setAttribute(3, timestamp2);
        sf.setAttribute(4, timestamp3);
        // Cleanup
        fw.close();

        // Open the shapefile for reading to verify it's contents.
        final FeatureReader<SimpleFeatureType, SimpleFeature> fr;
        fr = ds.getFeatureReader();

        assertTrue(fr.hasNext());
        final SimpleFeature sf1 = fr.next();

        // Check the read values match with the written ones.
        Date timestamp_ = (Date) sf1.getAttribute(1);
        Date timestamp2_ = (Date) sf1.getAttribute(3);
        Date timestamp3_ = (Date) sf1.getAttribute(4);

        if (datetime_enabled) {
            // if datetime support is enabled, check it matches the real timestamp
            assertEquals(timestamp, timestamp_);
            assertEquals(timestamp2, timestamp2_);
            assertEquals(timestamp3, timestamp3_);
        } else {
            // if datetime support is not enabled, test it matches the plain date
            assertEquals(date, timestamp_);
            assertEquals(date, timestamp2_);
            assertEquals(date, timestamp3_);
        }

        Date date_ = (Date) sf1.getAttribute(2);
        assertEquals(date, date_);

        // Cleanup
        fr.close();
        ds.dispose();
    }

    @Test
    public void testReadWriteDatetimeDisabled() throws Exception {
        System.setProperty("org.geotools.shapefile.datetime", "false");
        doTestReadWriteDate("1984-09-16");
    }

    @Test
    public void testReadWriteDatetimeEnabled() throws Exception {
        System.setProperty("org.geotools.shapefile.datetime", "true");
        doTestReadWriteDate("1984-09-16");
    }

    @Test
    public void testReadWriteDatetimeBeforeNewYear() throws Exception {
        System.setProperty("org.geotools.shapefile.datetime", "true");
        doTestReadWriteDate("1999-12-31");
    }

    @Test
    public void testReadWriteDatetimeAfterNewYear() throws Exception {
        System.setProperty("org.geotools.shapefile.datetime", "true");
        doTestReadWriteDate("2000-01-01");
    }

    @Test
    public void testIndexOutOfDate() throws Exception {
        File shpFile = copyShapefiles(STATE_POP);
        ShpFileType fix = ShpFileType.FIX;
        File fixFile = sibling(shpFile, fix.extension);
        fixFile.delete();
        ShapefileDataStore ds = new ShapefileDataStore(shpFile.toURI().toURL());
        ds.indexManager.createFidIndex();

        assertFalse(ds.indexManager.isIndexStale(fix));
        long fixMod = fixFile.lastModified();
        shpFile.setLastModified(fixMod + 1000000);
        assertTrue(ds.indexManager.isIndexStale(fix));
        assertTrue(fixFile.setLastModified(shpFile.lastModified()));
        assertFalse(ds.indexManager.isIndexStale(fix));
        assertTrue(fixFile.delete());
        assertTrue(ds.indexManager.isIndexStale(fix));
        ds.dispose();
    }

    /**
     * Issueing a request, whether its a query, update or delete, with a fid filter where feature
     * ids match the {@code <typeName>.<number>} structure but the {@code <typeName>} part does not
     * match the actual typeName, shoud ensure the invalid fids are ignored
     */
    @Test
    public void testWipesOutInvalidFidsFromFilters() throws Exception {
        final ShapefileDataStore ds = createDataStore();
        SimpleFeatureStore store;
        store = (SimpleFeatureStore) ds.getFeatureSource();

        final String validFid1, validFid2, invalidFid1, invalidFid2;
        {
            SimpleFeatureIterator features = store.getFeatures().features();
            validFid1 = features.next().getID();
            validFid2 = features.next().getID();
            invalidFid1 = "_" + features.next().getID();
            invalidFid2 = features.next().getID() + "abc";
            features.close();
        }
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
        Set<Identifier> ids = new HashSet<Identifier>();
        ids.add(ff.featureId(validFid1));
        ids.add(ff.featureId(validFid2));
        ids.add(ff.featureId(invalidFid1));
        ids.add(ff.featureId(invalidFid2));
        Filter fidFilter = ff.id(ids);

        final SimpleFeatureType schema = store.getSchema();
        final String typeName = schema.getTypeName();
        // get a property of type String to update its value by the given filter
        assertEquals(2, count(ds, typeName, fidFilter));

        store.modifyFeatures(new NameImpl("f"), "modified", fidFilter);
        Filter modifiedFilter = ff.equals(ff.property("f"), ff.literal("modified"));
        assertEquals(2, count(ds, typeName, modifiedFilter));

        final int initialCount = store.getCount(Query.ALL);
        store.removeFeatures(fidFilter);
        final int afterCount = store.getCount(Query.ALL);
        assertEquals(initialCount - 2, afterCount);
        ds.dispose();
    }

    @Test
    public void testCountTransaction() throws Exception {
        // http://jira.codehaus.org/browse/GEOT-2357

        final ShapefileDataStore ds = createDataStore();
        SimpleFeatureStore store;
        store = (SimpleFeatureStore) ds.getFeatureSource();
        Transaction t = new DefaultTransaction();
        store.setTransaction(t);

        int initialCount = store.getCount(Query.ALL);

        SimpleFeatureIterator features = store.getFeatures().features();
        String fid = features.next().getID();
        features.close();

        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        String typeName = store.getSchema().getTypeName();
        Id id = ff.id(Collections.singleton(ff.featureId(fid)));

        assertEquals(-1, store.getCount(new Query(typeName, id)));
        assertEquals(1, count(ds, typeName, id, t));

        store.removeFeatures(id);

        assertEquals(-1, store.getCount(new Query(store.getSchema().getTypeName(), id)));
        assertEquals(initialCount - 1, count(ds, typeName, Filter.INCLUDE, t));
        assertEquals(0, count(ds, typeName, id, t));
        ds.dispose();
    }

    private int count(DataStore ds, String typeName, Filter filter) throws Exception {
        return count(ds, typeName, filter, Transaction.AUTO_COMMIT);
    }

    private int count(DataStore ds, String typeName, Filter filter, Transaction t)
            throws Exception {
        FeatureReader<SimpleFeatureType, SimpleFeature> reader;
        reader = ds.getFeatureReader(new Query(typeName, filter), t);
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

    @Test
    public void testLinestringOnePoint() throws Exception {
        URL u = TestData.url(TestCaseSupport.class, "lsOnePoint/lsOnePoint.shp");
        File f = URLs.urlToFile(u);
        assertTrue(f.exists());

        store = new ShapefileDataStore(u);
        SimpleFeatureSource fs = store.getFeatureSource(store.getTypeNames()[0]);
        SimpleFeatureCollection fc = fs.getFeatures();
        assertEquals(3, fc.size());

        int i = 418;
        SimpleFeatureIterator it = fc.features();
        while (it.hasNext()) {
            SimpleFeature sf = it.next();
            assertEquals("Activity" + i, sf.getAttribute("Name"));

            if (i == 419) {
                assertNotNull(sf.getDefaultGeometry());
                assertTrue(sf.getDefaultGeometry() instanceof MultiLineString);
                MultiLineString mls = (MultiLineString) sf.getDefaultGeometry();
                assertEquals(1, mls.getNumGeometries());
                LineString ls = (LineString) mls.getGeometryN(0);
                assertEquals(2, ls.getNumPoints());
                assertEquals(ls.getStartPoint(), ls.getEndPoint());
            }
            i++;
        }
        it.close();

        assertEquals(421, i);

        assertEquals(3, fc.toArray().length);
    }

    @Test
    public void testIndexCreation() throws Exception {
        File shpFile = copyShapefiles(STATE_POP);
        String name = shpFile.getName();
        File qixFile =
                new File(shpFile.getParentFile(), name.substring(0, name.length() - 4) + ".qix");
        qixFile.delete();
        URL url = shpFile.toURI().toURL();
        ShapefileDataStore ds = new ShapefileDataStore(url);
        try {
            ds.setIndexed(false);
            ds.setIndexCreationEnabled(false);

            performSpatialQuery(ds);
            assertFalse(qixFile.exists());

            ds.setIndexed(true);
            performSpatialQuery(ds);
            assertFalse(qixFile.exists());

            ds.setIndexCreationEnabled(true);
            performSpatialQuery(ds);
            assertTrue(qixFile.exists());
        } finally {
            ds.dispose();
        }
    }

    @Test
    public void testFeatureStoreHints() throws Exception {
        File shpFile = copyShapefiles(STATE_POP);
        URL url = shpFile.toURI().toURL();
        ShapefileDataStore ds = new ShapefileDataStore(url);
        ShapefileFeatureStore store = (ShapefileFeatureStore) ds.getFeatureSource("statepop");
        assertEquals(store.getSupportedHints(), store.delegate.getSupportedHints());
    }

    @Test
    public void testReadDelete() throws Exception {
        File shpFile = copyShapefiles(STATE_POP);
        ShapefileDataStore ds = new ShapefileDataStore(shpFile.toURI().toURL());
        try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                ds.getFeatureReader(new Query(ds.getTypeNames()[0]), Transaction.AUTO_COMMIT)) {
            reader.getFeatureType();
            while (reader.hasNext()) {
                reader.next();
            }
        }
        ds.dispose();

        assertTrue(shpFile.delete());
        assertTrue(sibling(shpFile, "shx").delete());
        assertTrue(sibling(shpFile, "dbf").delete());
    }

    private void performSpatialQuery(ShapefileDataStore ds) throws IOException {
        SimpleFeatureSource featureSource = ds.getFeatureSource();
        SimpleFeatureType schema = featureSource.getSchema();
        Query query = new Query(schema.getTypeName());
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
        String geomName = schema.getGeometryDescriptor().getName().getLocalPart();
        ReferencedEnvelope bounds = featureSource.getBounds();
        // before it was working with / 2, that is, point bbox, now it does not, accuracy issue
        // maybe? Winding?
        bounds.expandBy(-bounds.getWidth() / 2.1, -bounds.getHeight() / 2.1);
        query.setFilter(ff.bbox(ff.property(geomName), bounds));
        SimpleFeatureCollection features = featureSource.getFeatures(query);
        SimpleFeatureIterator iterator = features.features();
        try {
            iterator.next();
        } finally {
            iterator.close();
        }
    }

    /**
     * This is useful to dump a UTF16 character to an UT16 escape sequence, basically the only way
     * to represent the chars we don't have on the keyboard (such as chinese ones :))
     */
    public static String charToHex(char c) {
        // Returns hex String representation of char c
        byte hi = (byte) (c >>> 8);
        byte lo = (byte) (c & 0xff);
        return byteToHex(hi) + byteToHex(lo);
    }

    public static String byteToHex(byte b) {
        // Returns hex String representation of byte b
        char[] hexDigit = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
        };
        char[] array = {hexDigit[(b >> 4) & 0x0f], hexDigit[b & 0x0f]};
        return new String(array);
    }

    /**
     * A test method testing the correct bounds of shapefiles without any data, but having [0:0,0:0]
     * in their header. The bounds must pass the isEmpty() and isNull() tests.
     *
     * @throws IOException, if the shapefile can not be read
     * @author Hendrik Peilke (IBYKUS AG)
     */
    @Test
    public void testBoundsEmpty() throws IOException {
        File file = TestData.file(TestCaseSupport.class, "empty-shapefile/empty-shapefile.shp");
        ShapefileDataStore dataStore = new ShapefileDataStore(file.toURI().toURL());
        ReferencedEnvelope bounds = dataStore.getFeatureSource().getBounds();
        assertTrue(
                "bounds of a shapefile without any data must be empty",
                bounds.isEmpty() && bounds.isNull());
    }

    /** Tests measures (M) values on shp output for Point type */
    @Test
    public void testPointZMSupport() throws Exception {
        // create feature type
        SimpleFeatureType type =
                DataUtilities.createType(
                        "junk", "a:Point,b:java.math.BigDecimal,c:java.math.BigInteger");
        DefaultFeatureCollection features = new DefaultFeatureCollection();

        BigInteger bigInteger = new BigInteger("1234567890123456789");
        BigDecimal bigDecimal = new BigDecimal(bigInteger, 2);

        SimpleFeatureBuilder build = new SimpleFeatureBuilder(type);
        build.add(new GeometryFactory().createPoint(new CoordinateXYZM(1, -1, 1, 2)));
        build.add(bigDecimal);
        build.add(bigInteger);

        SimpleFeature feature = build.buildFeature(null);
        features.add(feature);

        // store features
        File tmpFile = getTempFile();
        tmpFile.createNewFile();
        ShapefileDataStore s = new ShapefileDataStore(tmpFile.toURI().toURL());
        writeFeatures(s, features);
        File expected = new File(getClass().getResource("test-data/measure/pointzm.shp").toURI());
        // compare byte stream produced in shp file
        assertTrue(FileUtils.contentEquals(tmpFile, expected));
    }

    /** Tests measures (M) values on shp output for MultiPoint type */
    @Test
    public void testMultiPointZMSupport() throws Exception {
        // create feature type
        SimpleFeatureType type =
                DataUtilities.createType(
                        "junk", "a:MultiPoint,b:java.math.BigDecimal,c:java.math.BigInteger");
        DefaultFeatureCollection features = new DefaultFeatureCollection();

        BigInteger bigInteger = new BigInteger("1234567890123456789");
        BigDecimal bigDecimal = new BigDecimal(bigInteger, 2);

        SimpleFeatureBuilder build = new SimpleFeatureBuilder(type);
        GeometryFactory gf = new GeometryFactory();
        build.add(
                gf.createMultiPoint(
                        new Point[] {
                            gf.createPoint(new CoordinateXYZM(1, -1, 1, 2)),
                            gf.createPoint(new CoordinateXYZM(1, 3, 1, 4)),
                            gf.createPoint(new CoordinateXYZM(3, 4, 1, 2))
                        }));
        build.add(bigDecimal);
        build.add(bigInteger);

        SimpleFeature feature = build.buildFeature(null);
        features.add(feature);

        // store features
        File tmpFile = getTempFile();
        tmpFile.createNewFile();
        ShapefileDataStore s = new ShapefileDataStore(tmpFile.toURI().toURL());
        writeFeatures(s, features);
        File expected =
                new File(getClass().getResource("test-data/measure/multipointszm.shp").toURI());
        // compare byte stream produced in shp file
        assertTrue(FileUtils.contentEquals(tmpFile, expected));
    }

    /** Tests measures (M) values on shp output for MultiPolygon type */
    @Test
    public void testPolygonZMSupport() throws Exception {
        // create feature type
        SimpleFeatureType type =
                DataUtilities.createType(
                        "junk", "a:Polygon,b:java.math.BigDecimal,c:java.math.BigInteger");
        DefaultFeatureCollection features = new DefaultFeatureCollection();

        BigInteger bigInteger = new BigInteger("1234567890123456789");
        BigDecimal bigDecimal = new BigDecimal(bigInteger, 2);

        SimpleFeatureBuilder build = new SimpleFeatureBuilder(type);
        GeometryFactory gf = new GeometryFactory();
        build.add(
                gf.createMultiPolygon(
                        new Polygon[] {
                            gf.createPolygon(
                                    new CoordinateXYZM[] {
                                        new CoordinateXYZM(1, -1, 1, 1),
                                        new CoordinateXYZM(2, 0, 1, 2),
                                        new CoordinateXYZM(3, 1, 1, 2),
                                        new CoordinateXYZM(1, -1, 1, 1)
                                    })
                        }));
        build.add(bigDecimal);
        build.add(bigInteger);

        SimpleFeature feature = build.buildFeature(null);
        features.add(feature);

        // store features
        File tmpFile = getTempFile();
        tmpFile.createNewFile();
        ShapefileDataStore s = new ShapefileDataStore(tmpFile.toURI().toURL());
        writeFeatures(s, features);
        File expected =
                new File(getClass().getResource("test-data/measure/multipolygonzm.shp").toURI());
        // compare byte stream produced in shp file
        assertTrue(FileUtils.contentEquals(tmpFile, expected));
    }

    /** Tests measures (M) values on shp output for LineString type */
    @Test
    public void testLineStringZMSupport() throws Exception {
        // create feature type
        SimpleFeatureType type =
                DataUtilities.createType(
                        "junk", "a:LineString,b:java.math.BigDecimal,c:java.math.BigInteger");
        DefaultFeatureCollection features = new DefaultFeatureCollection();

        BigInteger bigInteger = new BigInteger("1234567890123456789");
        BigDecimal bigDecimal = new BigDecimal(bigInteger, 2);

        SimpleFeatureBuilder build = new SimpleFeatureBuilder(type);
        GeometryFactory gf = new GeometryFactory();
        build.add(
                gf.createMultiLineString(
                        new LineString[] {
                            gf.createLineString(
                                    new CoordinateXYZM[] {
                                        new CoordinateXYZM(1, -1, 1, 1),
                                        new CoordinateXYZM(2, 0, 1, 2),
                                        new CoordinateXYZM(3, 1, 1, 2)
                                    })
                        }));
        build.add(bigDecimal);
        build.add(bigInteger);

        SimpleFeature feature = build.buildFeature(null);
        features.add(feature);

        // store features
        File tmpFile = getTempFile();
        tmpFile.createNewFile();
        ShapefileDataStore s = new ShapefileDataStore(tmpFile.toURI().toURL());
        writeFeatures(s, features);
        File expected =
                new File(getClass().getResource("test-data/measure/multilinezm.shp").toURI());
        // compare byte stream produced in shp file
        assertTrue(FileUtils.contentEquals(tmpFile, expected));
    }
}
