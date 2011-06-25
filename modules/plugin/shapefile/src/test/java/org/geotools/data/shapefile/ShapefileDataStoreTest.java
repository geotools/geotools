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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.geotools.TestData;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultQuery;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollections;
import org.geotools.feature.FeatureTypes;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.feature.type.BasicFeatureTypes;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.FeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

/**
 * 
 *
 * @source $URL$
 * @version $Id$
 * @author Ian Schneider
 */
public class ShapefileDataStoreTest extends TestCaseSupport {

    final static String STATE_POP = "shapes/statepop.shp";
    final static String STREAM = "shapes/stream.shp";
    final static String DANISH = "shapes/danish_point.shp";
    final static String CHINESE = "shapes/chinese_poly.shp";
    final static String RUSSIAN = "shapes/rus-windows-1251.shp";
    final static FilterFactory2 ff = CommonFactoryFinder
            .getFilterFactory2(null);
	private ShapefileDataStore store;

    public ShapefileDataStoreTest(String testName) throws IOException {
        super(testName);
    }
    
    @Override
    protected void tearDown() throws Exception {
    	if(store != null) {
    		store.dispose();
    	}
    	super.tearDown();
    }
    

    protected SimpleFeatureCollection loadFeatures(String resource, Query query)
            throws Exception {
        assertNotNull(query);

        URL url = TestData.url(resource);
        store = new ShapefileDataStore(url);
        SimpleFeatureSource fs = store.getFeatureSource(store.getTypeNames()[0]);
        return fs.getFeatures(query);
    }

    protected SimpleFeatureCollection loadLocalFeaturesM2() throws IOException {
        String target = "jar:file:/C:/Documents and Settings/jgarnett/.m2/repository/org/geotools/gt2-sample-data/2.4-SNAPSHOT/gt2-sample-data-2.4-SNAPSHOT.jar!/org/geotools/test-data/shapes/statepop.shp";
        URL url = new URL(target);
        ShapefileDataStore s = new ShapefileDataStore(url);
        SimpleFeatureSource fs = s.getFeatureSource(s.getTypeNames()[0]);
        return fs.getFeatures();
    }

    protected SimpleFeatureCollection loadFeatures(String resource, Charset charset,
            Query q) throws Exception {
        if (q == null)
            q = new DefaultQuery();
        URL url = TestData.url(resource);
        store = new ShapefileDataStore(url, false, charset);
        SimpleFeatureSource fs = store.getFeatureSource(store.getTypeNames()[0]);
        return fs.getFeatures(q);
    }

    protected SimpleFeatureCollection loadFeatures(ShapefileDataStore s)
            throws Exception {
        return s.getFeatureSource(s.getTypeNames()[0]).getFeatures();
    }

    public void testLoad() throws Exception {
        loadFeatures(STATE_POP, Query.ALL);
    }

    public void testLoadDanishChars() throws Exception {
        SimpleFeatureCollection fc = loadFeatures(DANISH, Query.ALL);
        SimpleFeature first = firstFeature(fc);

        // Charlotte (but with the o is stroked)
        assertEquals("Charl\u00F8tte", first.getAttribute("TEKST1"));
    }

    public void testLoadChineseChars() throws Exception {
        try {
            SimpleFeatureCollection fc = loadFeatures(CHINESE, Charset
                    .forName("GB18030"), null);
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
    
    public void testLoadRussianChars() throws Exception {
        try {
            SimpleFeatureCollection fc = loadFeatures(RUSSIAN, Charset
                    .forName("CP1251"), null);
            SimpleFeatureIterator features = fc.features();
            SimpleFeature f = features.next();
            assertEquals("\u041A\u0438\u0440\u0438\u043B\u043B\u0438\u0446\u0430", f.getAttribute("TEXT"));
            f = features.next();
            assertEquals("\u0421\u043C\u0435\u0448\u0430\u043D\u044B\u0439 12345", f.getAttribute("TEXT"));
            features.close();
        } catch (UnsupportedCharsetException notInstalledInJRE) {
            // this just means you have not installed Russian support into your JRE
            // (as such it represents a bad configuration rather than a test failure)
        }
    }
    

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

    public void testSchema() throws Exception {
        URL url = TestData.url(STATE_POP);
        ShapefileDataStore shapeDataStore = new ShapefileDataStore(url);
        String typeName = shapeDataStore.getTypeNames()[0];
        SimpleFeatureType schema = shapeDataStore.getSchema(typeName);
        List<AttributeDescriptor> attributes = schema.getAttributeDescriptors();
        assertEquals("Number of Attributes", 253, attributes.size());
        shapeDataStore.dispose();
    }

    public void testSpacesInPath() throws Exception {
        URL u = TestData.url(TestCaseSupport.class, "folder with spaces/pointtest.shp");
        File f = DataUtilities.urlToFile(u);
        assertTrue(f.exists());
        ShapefileDataStore s = new ShapefileDataStore(u);
        loadFeatures(s);
        s.dispose();
    }

    /**
     * Test envelope versus old DataSource
     */
    public void testEnvelope() throws Exception {
        SimpleFeatureCollection features = loadFeatures(STATE_POP, Query.ALL);
        ShapefileDataStore s = new ShapefileDataStore(TestData.url(STATE_POP));
        String typeName = s.getTypeNames()[0];
        SimpleFeatureCollection all = s.getFeatureSource(typeName).getFeatures();

        assertEquals(features.getBounds(), all.getBounds());
        s.dispose();
    }

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
        assertEquals("Value of statename is wrong", "Illinois", firstFeature
                .getAttribute("STATE_NAME"));
        assertEquals("Value of land area is wrong", 143986.61,
                ((Double) firstFeature.getAttribute("LAND_KM")).doubleValue(),
                0.001);
    }

    public void testLoadAndCheckParentTypeIsPolygon() throws Exception {
        SimpleFeatureCollection features = loadFeatures(STATE_POP, Query.ALL);
        SimpleFeatureType schema = firstFeature(features).getFeatureType();

        assertTrue(FeatureTypes.isDecendedFrom(schema,
                BasicFeatureTypes.POLYGON));
        assertTrue(FeatureTypes.isDecendedFrom(schema,
                BasicFeatureTypes.POLYGON));
        assertTrue(FeatureTypes.isDecendedFrom(schema,
                FeatureTypes.DEFAULT_NAMESPACE, "polygonFeature"));
    }

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
        if (file.exists())
            file.deleteOnExit();

        file = new File("test.shx");
        if (file.exists()){
            file.deleteOnExit();
        }
    }

    public void testCreateSchemaWithCRS() throws Exception {
        File file = new File("test.shp");
        URL toURL = file.toURI().toURL();
        ShapefileDataStore ds = new ShapefileDataStore(toURL);
        SimpleFeatureType featureType = DataUtilities.createType("test", "geom:MultiPolygon:srid=32615");
        CoordinateReferenceSystem crs = featureType.getGeometryDescriptor().getCoordinateReferenceSystem(); 
        assertNotNull( crs );
        
        ds.createSchema(featureType);
        
        assertEquals("test", ds.getSchema().getTypeName());

        CoordinateReferenceSystem crs2 = ds.getSchema().getGeometryDescriptor().getCoordinateReferenceSystem();
        assertNotNull( crs2 );
        assertEquals( crs.getName(), crs2.getName() );
        ds.dispose();
        
        file.deleteOnExit();
        file = new File("test.dbf");
        file.deleteOnExit();
        file = new File("test.shp");
        file.deleteOnExit();

        file = new File("test.prj");
        if (file.exists())
            file.deleteOnExit();

        file = new File("test.shx");
        if (file.exists()){
            file.deleteOnExit();
        }
        
        file = new File("test.prj");
        if( file.exists()){
            file.deleteOnExit();
        }
    }
    
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

        if (file.exists())
            file.deleteOnExit();

        file = new File("test.shx");
        if (file.exists())
            file.deleteOnExit();
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
     * Create a set of features, then remove every other one, updating the
     * remaining. Test for removal and proper update after reloading...
     */
    public void testUpdating() throws Throwable {
            ShapefileDataStore sds = createDataStore();
            loadFeatures(sds);

            FeatureWriter<SimpleFeatureType, SimpleFeature> writer = null;
            try {
                writer = sds.getFeatureWriter(sds.getTypeNames()[0],
                        Filter.INCLUDE, Transaction.AUTO_COMMIT);
                while (writer.hasNext()) {
                    SimpleFeature feat = writer.next();
                    Byte b = (Byte) feat.getAttribute(1);
                    if (b.byteValue() % 2 == 0) {
                        writer.remove();
                    } else {
                        feat.setAttribute(1, new Byte((byte) -1));
                    }
                }
            } finally {
                if (writer != null)
                    writer.close();
            }
            SimpleFeatureCollection fc = loadFeatures(sds);

            assertEquals(10, fc.size());
            for (SimpleFeatureIterator i = fc.features(); i.hasNext();) {
                assertEquals(-1, ((Byte) i.next().getAttribute(1)).byteValue());
            }
            sds.dispose();
    }

    /**
     * Create a test file, then continue removing the first entry until there
     * are no features left.
     */
    public void testRemoveFromFrontAndClose() throws Throwable {
        ShapefileDataStore sds = createDataStore();

        int idx = loadFeatures(sds).size();

        while (idx > 0) {
            FeatureWriter<SimpleFeatureType, SimpleFeature> writer = null;

            try {
                writer = sds.getFeatureWriter(sds.getTypeNames()[0],
                        Filter.INCLUDE, Transaction.AUTO_COMMIT);
                writer.next();
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
     * Create a test file, then continue removing the first entry until there
     * are no features left.
     */
    public void testRemoveFromFrontAndCloseTransaction() throws Throwable {
        ShapefileDataStore sds = createDataStore();

        int idx = loadFeatures(sds).size();

        while (idx > 0) {
            Transaction t = new DefaultTransaction();
            FeatureWriter<SimpleFeatureType, SimpleFeature> writer = null;

            try {
                writer = sds.getFeatureWriter(sds.getTypeNames()[0],
                        Filter.INCLUDE, t);
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
     * Create a test file, then continue removing the last entry until there are
     * no features left.
     */
    public void testRemoveFromBackAndClose() throws Throwable {
            ShapefileDataStore sds = createDataStore();

            int idx = loadFeatures(sds).size();

            while (idx > 0) {
                FeatureWriter<SimpleFeatureType, SimpleFeature> writer = null;
                try {
                    writer = sds.getFeatureWriter(sds.getTypeNames()[0],
                            Filter.INCLUDE, Transaction.AUTO_COMMIT);
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
    
    public void testWriteShapefileWithNoRecords() throws Exception {
        SimpleFeatureType featureType = DataUtilities.createType("whatever",
                "a:Polygon,b:String");

        File tempFile = getTempFile();
        ShapefileDataStore shapefileDataStore = new ShapefileDataStore(tempFile
                .toURI().toURL());
        shapefileDataStore.createSchema(featureType);

        FeatureWriter<SimpleFeatureType, SimpleFeature> featureWriter = shapefileDataStore.getFeatureWriter(
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

    /**
     * Creates feature collection with all the stuff we care about from simple
     * types, to Geometry and date.
     * <p>
     * As we care about supporting more stuff please add on to the end of this
     * list...
     * 
     * @return SimpleFeatureCollection For use in testing.
     * @throws Exception
     */
    private SimpleFeatureCollection createFeatureCollection() throws Exception {
        SimpleFeatureType featureType = createExampleSchema();
        SimpleFeatureBuilder build = new SimpleFeatureBuilder(featureType);

        SimpleFeatureCollection features = FeatureCollections.newCollection();
        for (int i = 0, ii = 20; i < ii; i++) {

            build.add(new GeometryFactory().createPoint(new Coordinate(1, -1)));
            build.add(new Byte((byte) i));
            build.add(new Short((short) i));
            build.add(new Double(i));
            build.add(new Float(i));
            build.add(new String(i + " "));
            build.add(new Date(i));
            build.add(new Boolean(true));
            build.add(new Integer(22));
            build.add(new Long(1234567890123456789L));
            build.add(new BigDecimal(new BigInteger(
                    "12345678901234567890123456789"), 2));
            build.add(new BigInteger("12345678901234567890123456789"));
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTimeInMillis(i);
            build.add(calendar);

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

        return build.buildFeatureType();
    }

    public void testAttributesWriting() throws Exception {
        SimpleFeatureCollection features = createFeatureCollection();
        File tmpFile = getTempFile();
        tmpFile.createNewFile();
        ShapefileDataStore s = new ShapefileDataStore(tmpFile.toURI().toURL());
        writeFeatures(s, features);
        s.dispose();
    }

    public void testWriteReadBigNumbers() throws Exception {
        // create feature type
        SimpleFeatureType type = DataUtilities.createType("junk",
                "a:Point,b:java.math.BigDecimal,c:java.math.BigInteger");
        SimpleFeatureCollection features = FeatureCollections.newCollection();

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
         FeatureReader<SimpleFeatureType, SimpleFeature> reader = s.getFeatureReader("junk");
        try {
            SimpleFeature f = reader.next();

            assertEquals("big decimal", bigDecimal.doubleValue(), ((Number) f
                    .getAttribute("b")).doubleValue(), 0.00001);
            assertEquals("big integer", bigInteger.longValue(), ((Number) f
                    .getAttribute("c")).longValue(), 0.00001);
        } finally {
            reader.close();
        }
        s.dispose();
    }

    public void testGeometriesWriting() throws Exception {

//        String[] wktResources = new String[] { "point", "multipoint", "line",
//                "multiline", "polygon", "multipolygon" };
        String[] wktResources = new String[] { "line",
                "multiline", "polygon", "multipolygon" };

        for (int i = 0; i < wktResources.length; i++) {
            Geometry geom = readGeometry(wktResources[i]);
            String testName = wktResources[i];
            try {

                runWriteReadTest(geom, false);
                make3D(geom);
                testName += "3d";
                runWriteReadTest(geom, true);
            } catch (Throwable e) {
                e.printStackTrace();
                throw new Exception("Error in " + testName, e);
            }

        }

    }

    private void make3D(Geometry g) {
        Coordinate[] c = g.getCoordinates();
        for (int i = 0, ii = c.length; i < ii; i++) {
            c[i].z = 42 + i;
        }
    }

    private void writeFeatures(ShapefileDataStore s, SimpleFeatureCollection fc)
            throws Exception {
        s.createSchema(fc.features().next().getFeatureType());
        FeatureWriter<SimpleFeatureType, SimpleFeature> fw = s.getFeatureWriter(s.getTypeNames()[0],
                Transaction.AUTO_COMMIT);
        SimpleFeatureIterator it = fc.features();
        while (it.hasNext()) {
            SimpleFeature feature = it.next();
            SimpleFeature newFeature = fw.next();
            newFeature.setAttributes(feature.getAttributes());

            fw.write();
        }
        fw.close();
    }

    private void runWriteReadTest(Geometry geom, boolean d3) throws Exception {
        // make features

        SimpleFeatureTypeBuilder ftb = new SimpleFeatureTypeBuilder();
        ftb.setName("Junk");
        ftb.add("a", geom.getClass());
        SimpleFeatureType type = ftb.buildFeatureType();

        SimpleFeatureCollection features = FeatureCollections.newCollection();
        SimpleFeatureBuilder build = new SimpleFeatureBuilder(type);
        for (int i = 0, ii = 20; i < ii; i++) {
            build.set(0, (Geometry) geom.clone());
            SimpleFeature feature = build.buildFeature(null);

            features.add(feature);
        }

        // set up file
        File tmpFile = getTempFile();
        tmpFile.delete();

        // write features
        ShapefileDataStore shapeDataStore = new ShapefileDataStore(tmpFile
                .toURI().toURL());
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
                    fromShape = ((GeometryCollection) fromShape)
                            .getGeometryN(0);
                }
            }
            try {
                // check if the original is valid as we're going to fix unclosed rings
                // as we read them out of the shapefile
                if(geom.isValid()) {
                    Coordinate[] c1 = geom.getCoordinates();
                    Coordinate[] c2 = fromShape.getCoordinates();
                    for (int cc = 0, ccc = c1.length; cc < ccc; cc++) {
                        if (d3)
                            assertTrue(c1[cc].equals3D(c2[cc]));
                        else
                            assertTrue(c1[cc].equals2D(c2[cc]));
                    }
                }
            } catch (Throwable t) {
                fail("Bogus : " + Arrays.asList(geom.getCoordinates()) + " : "
                        + Arrays.asList(fromShape.getCoordinates()));
            }
        }
        tmpFile.delete();
        shapeDataStore.dispose();
    }

    public void testGetCount() throws Exception {
        assertTrue(copyShapefiles(STREAM).canRead()); // The following test
                                                        // seems to fail in the
                                                        // URL point into the
                                                        // JAR file.
        ShapefileDataStore store = (ShapefileDataStore) new ShapefileDataStoreFactory()
                .createDataStore(TestData.url(TestCaseSupport.class, STREAM));
        int count = 0;
         FeatureReader<SimpleFeatureType, SimpleFeature> reader = store.getFeatureReader();
        try {
            while (reader.hasNext()) {
                count++;
                reader.next();
            }
            assertEquals(count, store.getCount(Query.ALL));
        } finally {
            reader.close();
        }
        store.dispose();
    }

    /**
     * Checks if feature reading optimizations still allow to execute the
     * queries or not
     * 
     * @throws Exception
     */
    public void testGetReaderOptimizations() throws Exception {
        URL url = TestData.url(STATE_POP);
        ShapefileDataStore s = new ShapefileDataStore(url);

        // attributes other than geometry can be ignored here
        Query query = new DefaultQuery(s.getSchema().getTypeName(),
                Filter.INCLUDE, new String[] { "the_geom" });
         FeatureReader<SimpleFeatureType, SimpleFeature> reader = s.getFeatureReader(s.getSchema().getTypeName(),
                query);
        assertEquals(1, reader.getFeatureType().getAttributeCount());
        assertEquals("the_geom", reader.getFeatureType().getDescriptor(0)
                .getLocalName());

        // here too, the filter is using the geometry only
        GeometryFactory gc = new GeometryFactory();
        LinearRing ring = gc.createLinearRing(new Coordinate[] {
                new Coordinate(0, 0), new Coordinate(10, 0),
                new Coordinate(10, 10), new Coordinate(0, 10),
                new Coordinate(0, 0) });
        Polygon polygon = gc.createPolygon(ring, null);

        ReferencedEnvelope bounds = new ReferencedEnvelope(polygon
                .getEnvelopeInternal(), null);
        Filter gf = ff.bbox(ff.property("the_geom"), bounds);

        query = new DefaultQuery(s.getSchema().getTypeName(), gf,
                new String[] { "the_geom" });

        reader.close();
        reader = s.getFeatureReader(s.getSchema().getTypeName(), query);
        assertEquals(1, reader.getFeatureType().getAttributeCount());
        assertEquals("the_geom", reader.getFeatureType().getDescriptor(0)
                .getLocalName());

        reader.close();

        // here not, we need state_name in the feature type, so open the dbf
        // file please
        Filter cf = ff
                .equals(ff.property("STATE_NAME"), ff.literal("Illinois"));
        query = new DefaultQuery(s.getSchema().getTypeName(), cf,
                new String[] { "the_geom" });
        reader = s.getFeatureReader(s.getSchema().getTypeName(), query);
        assertEquals(s.getSchema(), reader.getFeatureType());
        reader.close();
        s.dispose();
    }
    
    public void testWrite() throws Exception {
        // create feature type
        SimpleFeatureType type = DataUtilities.createType("junk","a:Point,b:java.math.BigDecimal,c:java.math.BigInteger");

        BigInteger bigInteger = new BigInteger("1234567890123456789");
        BigDecimal bigDecimal = new BigDecimal(bigInteger, 2);

        SimpleFeatureBuilder builder = new SimpleFeatureBuilder(type);
        builder.add(new GeometryFactory().createPoint(new Coordinate(1, -1)));
        builder.add(bigDecimal);
        builder.add(bigInteger);
        
        
        SimpleFeature feature = builder.buildFeature(null);;

        // store features
        File tmpFile = getTempFile();
        tmpFile.createNewFile();
        ShapefileDataStore s = new ShapefileDataStore(tmpFile.toURI().toURL());
        s.createSchema(type);
        
        // was failing in GEOT-2427
        Transaction t= new DefaultTransaction();
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer = s.getFeatureWriter(s.getTypeNames()[0], t);
        SimpleFeature feature1 = writer.next();
        s.dispose();
    }
    
    private void doTestReadWriteDate(String str_date) throws Exception {
        
        final boolean datetime_enabled = Boolean.getBoolean("org.geotools.shapefile.datetime");
        
        File file = org.geotools.test.TestData.temp(this, "timestamp.shp");
                
        URL toURL = file.toURI().toURL();
        
        ShapefileDataStore ds = new ShapefileDataStore(toURL);
        ds.setDbftimeZone(TimeZone.getTimeZone("UTC"));
        ds.createSchema(DataUtilities.createType("test",
                        "geom:Point,timestamp:java.util.Date,date:java.util.Date,timestamp2:java.util.Date,timestamp3:java.util.Date"));

        final FeatureWriter<SimpleFeatureType, SimpleFeature> fw;
        fw = ds.getFeatureWriterAppend(Transaction.AUTO_COMMIT);
        final SimpleFeature sf;
        
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd Z");
        
        Date date = (Date) dateFormatter.parse(str_date + " GMT");

        Calendar timestampCal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
       
        timestampCal.setTime(date);
        
        timestampCal.add(Calendar.MILLISECOND, 1);
        // Set timestamp 00:00:00.001 at the same day
        Date timestamp = timestampCal.getTime();
        
        timestampCal.add(Calendar.MILLISECOND,  12*60*60*1000);
        // Set timestamp2 12:00:00.001 at the same day
        Date timestamp2 = timestampCal.getTime();

        timestampCal.add(Calendar.MILLISECOND,  11*60*60*1000+ 59*60*1000 + 59*1000 + 998);
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
        fr =  ds.getFeatureReader();
        
        assertTrue(fr.hasNext());
        final SimpleFeature sf1 = fr.next();  

        // Check the read values match with the written ones.
        Date timestamp_ = (Date) sf1.getAttribute(1);
        Date timestamp2_ = (Date) sf1.getAttribute(3);
        Date timestamp3_ = (Date) sf1.getAttribute(4);         
        
        if (datetime_enabled){
            // if datetime support is enabled, check it matches the real timestamp
            assertEquals(timestamp, timestamp_);
            assertEquals(timestamp2, timestamp2_);
            assertEquals(timestamp3, timestamp3_);
        }else{
            // if datetime support is not enabled, test it matches the plain date
            assertEquals(date , timestamp_);
            assertEquals(date , timestamp2_);
            assertEquals(date , timestamp3_);
        }
        
        Date date_ = (Date) sf1.getAttribute(2);
        assertEquals(date , date_);
        
        // Cleanup
        fr.close();
        ds.dispose();
      }
    
    public void testReadWriteDatetimeDisabled() throws Exception{
        System.setProperty("org.geotools.shapefile.datetime", "false");
        doTestReadWriteDate("1984-09-16");
    }
    
    public void testReadWriteDatetimeEnabled() throws Exception{
        System.setProperty("org.geotools.shapefile.datetime", "true");
        doTestReadWriteDate("1984-09-16");
    }
    
    public void testReadWriteDatetimeBeforeNewYear() throws Exception{
        System.setProperty("org.geotools.shapefile.datetime", "true");
        doTestReadWriteDate("1999-12-31");
    }
    
    public void testReadWriteDatetimeAfterNewYear() throws Exception{
        System.setProperty("org.geotools.shapefile.datetime", "true");
        doTestReadWriteDate("2000-01-01");
    }
    
    
    /**
     * This is useful to dump a UTF16 character to an UT16 escape sequence,
     * basically the only way to represent the chars we don't have on the
     * keyboard (such as chinese ones :))
     * 
     * @param c
     * @return
     */
    static public String charToHex(char c) {
        // Returns hex String representation of char c
        byte hi = (byte) (c >>> 8);
        byte lo = (byte) (c & 0xff);
        return byteToHex(hi) + byteToHex(lo);
    }

    static public String byteToHex(byte b) {
        // Returns hex String representation of byte b
        char[] hexDigit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f' };
        char[] array = { hexDigit[(b >> 4) & 0x0f], hexDigit[b & 0x0f] };
        return new String(array);
    }
}
