/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.ogr;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.util.Date;

import org.geotools.TestData;
import org.geotools.data.DataStore;
import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureSource;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.feature.type.BasicFeatureTypes;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;

/**
 * Basic test for OGR data store capabilites against file data sources
 * 
 * @author aaime
 * 
 *
 *
 *
 * @source $URL$
 *         http://svn.osgeo.org/geotools/trunk/modules/unsupported/ogr/src/test/java/org/geotools
 *         /data/ogr/OGRDataStoreTest.java $
 */
public abstract class OGRDataStoreTest extends TestCaseSupport {

    protected OGRDataStoreTest(Class<? extends OGRDataStoreFactory> dataStoreFactoryClass) {
        super(dataStoreFactoryClass);
    }

    public void testGetTypeNames() throws FileNotFoundException, IOException {
        OGRDataStore store = new OGRDataStore(getAbsolutePath(STATE_POP), null, null, ogr);
        assertEquals(1, store.getTypeNames().length);
        assertEquals("statepop", store.getTypeNames()[0]);
        store = new OGRDataStore(getAbsolutePath(MIXED), null, null, ogr);
        assertEquals(1, store.getTypeNames().length);
        assertEquals("mixed", store.getTypeNames()[0]);
    }

    public void testSchemaPop() throws Exception {
        OGRDataStore s = new OGRDataStore(getAbsolutePath(STATE_POP), null, null, ogr);
        SimpleFeatureType schema = s.getSchema(s.getTypeNames()[0]);
        assertEquals("Number of Attributes", 253, schema.getAttributeCount());
        assertTrue(CRS.equalsIgnoreMetadata(CRS.decode("EPSG:4269", true), schema
                .getGeometryDescriptor().getCoordinateReferenceSystem()));
    }

    public void testSchemaMix() throws Exception {
        OGRDataStore s = new OGRDataStore(getAbsolutePath(MIXED), null, null, ogr);
        SimpleFeatureType schema = s.getSchema(s.getTypeNames()[0]);
        assertEquals("Number of Attributes", 11, schema.getAttributeCount());
        // mixed geometry types, only way is to use Geometry as geom type
        assertEquals(Geometry.class, schema.getGeometryDescriptor().getType().getBinding());
        // ah, can't compare the WKT against the EPSG database becuase it's
        // apparently broken, it's EPSG:3003 but with (very) different TOWGS84
        // parameters, crazy...
        // assertTrue(CRS.equalsIgnoreMetadata(CRS.decode("EPSG:3003", true),
        // schema
        // .getDefaultGeometry().getCoordinateSystem()));
    }

    /**
     * Test optimized count against actual count
     * 
     * @throws Exception
     */
    public void testOptimizedEnvelope() throws Exception {
        URL url = TestData.url(STATE_POP);
        ShapefileDataStore sds = new ShapefileDataStore(url);
        OGRDataStore s = new OGRDataStore(getAbsolutePath(STATE_POP), null, null, ogr);
        String typeName = s.getTypeNames()[0];

        ReferencedEnvelope expectedBounds = sds.getFeatureSource().getBounds();
        ReferencedEnvelope actualBounds = s.getFeatureSource(typeName).getBounds();
        assertEquals(expectedBounds, actualBounds);
        assertNotNull(actualBounds);
    }
    
    /**
     * Test count versus old DataSource
     */
    public void testOptimizedCount() throws Exception {
        URL url = TestData.url(STATE_POP);
        ShapefileDataStore sds = new ShapefileDataStore(url);
        OGRDataStore s = new OGRDataStore(getAbsolutePath(STATE_POP), null, null, ogr);
        String typeName = s.getTypeNames()[0];

        assertEquals(sds.getCount(Query.ALL), s.getFeatureSource(typeName).getCount(Query.ALL));
    }


    public void testLoadAndVerify() throws Exception {
        SimpleFeatureCollection features = loadFeatures(STATE_POP, Query.ALL);
        int count = features.size();

        assertTrue("Have features", count > 0);
        assertEquals("Number of Features loaded", 49, features.size());
        assertEquals(49, countFeatures(features));

        SimpleFeatureType schema = firstFeature(features).getFeatureType();
        assertNotNull(schema.getGeometryDescriptor());
        assertEquals("Number of Attributes", 253, schema.getAttributeCount());
        assertEquals("Value of statename is wrong", firstFeature(features).getAttribute(
                "STATE_NAME"), "Illinois");
        assertEquals("Value of land area is wrong", ((Double) firstFeature(features).getAttribute(
                "LAND_KM")).doubleValue(), 143986.61, 0.001);
    }

    public void testLoadAndCheckParentTypeIsPolygon() throws Exception {
        FeatureCollection features = loadFeatures(STATE_POP, Query.ALL);
        SimpleFeatureType schema = firstFeature(features).getFeatureType();
        assertEquals(schema.getSuper(), BasicFeatureTypes.POLYGON);
    }

    public void testShapefileComparison() throws Exception {
        URL url = TestData.url(STATE_POP);
        ShapefileDataStore sds = new ShapefileDataStore(url);
        OGRDataStore ods = new OGRDataStore(getAbsolutePath(STATE_POP), null, null, ogr);

        assertFeatureTypeEquals(sds.getSchema(), ods.getSchema(sds.getSchema().getTypeName()));

        Query query = new Query(sds.getSchema().getTypeName());
        FeatureReader sfr = sds.getFeatureReader(query, Transaction.AUTO_COMMIT);
        FeatureReader ofr = ods.getFeatureReader(query, Transaction.AUTO_COMMIT);
        SimpleFeature sf = null;
        SimpleFeature of = null;
        while (true) {
            if (!sfr.hasNext()) {
                assertTrue(!ofr.hasNext());
                break;
            }
            sf = (SimpleFeature) sfr.next();
            of = (SimpleFeature) ofr.next();
            for (int i = 0; i < sds.getSchema().getAttributeCount(); i++) {
                Object shapeAtt = sf.getAttribute(i);
                Object ogrAtt = of.getAttribute(i);
                assertEquals(shapeAtt, ogrAtt);
            }
        }
        sfr.close();
        ofr.close();
        sds.dispose();
        ods.dispose();
    }
    
    public void testLoadGeometry() throws Exception {
        // load up the store and source
        OGRDataStore ods = new OGRDataStore(getAbsolutePath(STATE_POP), null, null, ogr);
        SimpleFeatureSource fs = ods.getFeatureSource("statepop");
        
        // query just the geometry field, check the collection returned
        Query query = new Query("statepop");
        query.setPropertyNames(new String[] {"the_geom"});
        SimpleFeatureCollection fc = fs.getFeatures(query);
        assertTrue(fc.size() > 0);
        SimpleFeatureType schema = fc.getSchema();
        assertEquals(1, schema.getDescriptors().size());
        assertEquals(MultiPolygon.class, schema.getGeometryDescriptor().getType().getBinding());
        
        // get one feature, check it
        SimpleFeatureIterator fi = fc.features();
        SimpleFeature feature = fi.next();
        fi.close();
        schema = feature.getFeatureType();
        assertEquals(1, schema.getDescriptors().size());
        assertEquals(MultiPolygon.class, schema.getGeometryDescriptor().getType().getBinding());
        assertNotNull(feature.getDefaultGeometry());
    }
    
    private void assertFeatureTypeEquals(SimpleFeatureType type1, SimpleFeatureType type2) {
        // general type assertions
        assertEquals(type1.getTypeName(), type2.getTypeName());
        assertEquals(type1.getSuper(), type2.getSuper());
        assertEquals(type1.getAttributeCount(), type2.getAttributeCount());
        
        // compare the attributes
        for (int i = 0; i < type1.getAttributeCount(); i++) {
            AttributeDescriptor ad1 = type1.getDescriptor(i);
            AttributeDescriptor ad2 = type2.getDescriptor(i);
            assertEquals(ad1.getName(), ad2.getName());
            assertEquals(ad1.getType().getBinding(), ad2.getType().getBinding());
            assertEquals(ad1.isNillable(), ad2.isNillable());
            assertEquals(ad1.getMinOccurs(), ad2.getMinOccurs());
            assertEquals(ad1.getMaxOccurs(), ad2.getMaxOccurs());
            if(ad1 instanceof GeometryDescriptor) {
                GeometryDescriptor gd1 = (GeometryDescriptor) ad1;
                GeometryDescriptor gd2 = (GeometryDescriptor) ad2;
                assertTrue(CRS.equalsIgnoreMetadata(gd1.getCoordinateReferenceSystem(), gd2.getCoordinateReferenceSystem()));
            } 
        }
    }


    /**
     * Create a test file, then continue removing the first entry until there are no features left.
     */
    public void testRemoveFromFrontAndClose() throws Throwable {
        OGRDataStore sds = createDataStore();

        String typeName = sds.getTypeNames()[0];
        int idx = loadFeatures(sds, typeName).size();

        while (idx > 0) {
            FeatureWriter writer = null;

            try {
                writer = sds.getFeatureWriter(typeName, Filter.INCLUDE, Transaction.AUTO_COMMIT);
                writer.next();
                writer.remove();
            } finally {
                if (writer != null) {
                    writer.close();
                    writer = null;
                }
            }
            idx--;
            assertEquals(idx, countFeatures(loadFeatures(sds, typeName)));
        }
    }

    /**
     * Create a test file, then continue removing the last entry until there are no features left.
     */
    public void testRemoveFromBackAndClose() throws Throwable {
        OGRDataStore sds = createDataStore();
        String typeName = sds.getTypeNames()[0];

        int idx = loadFeatures(sds, typeName).size();
        assertEquals(idx, countFeatures(loadFeatures(sds, typeName)));

        while (idx > 0) {
            FeatureWriter writer = null;
            try {
                writer = sds.getFeatureWriter(sds.getTypeNames()[0], Filter.INCLUDE,
                        Transaction.AUTO_COMMIT);
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
            assertEquals(--idx, countFeatures(loadFeatures(sds, typeName)));
        }

    }

    public void testCreateSchema() throws Exception {
        String[] fileNames = shapeFileNames("test");
        cleanFiles(fileNames);
        String absolutePath = new File(fileNames[0]).getAbsolutePath();
        OGRDataStore ds = new OGRDataStore(absolutePath, "ESRI shapefile", null, ogr);
        SimpleFeatureType schema = DataUtilities.createType("test",
                "geom:MultiPolygon,count:int,level1:double,level2:float,chars:string");
        ds.createSchema(schema);

        // now do some testing
        assertEquals(1, ds.getTypeNames().length);
        assertEquals("test", ds.getTypeNames()[0]);
        SimpleFeatureType ogrSchema = ds.getSchema(ds.getTypeNames()[0]);
        assertEquals(schema.getGeometryDescriptor().getType().getBinding(), 
                    ogrSchema.getGeometryDescriptor().getType().getBinding());
        for (int i = 0; i < schema.getAttributeCount(); i++) {
            AttributeDescriptor at = schema.getDescriptor(i);
            if (at == schema.getGeometryDescriptor())
                continue;

            assertEquals(at.getName(), ogrSchema.getDescriptor(i).getName());
            assertEquals("Wrong type for attribute " + at.getName(), at.getType().getBinding(), ogrSchema
                    .getDescriptor(i).getType().getBinding());
        }
    }

    public void testCreateWriteRead() throws Exception {
        String typeName = "testw";
        String[] files = shapeFileNames(typeName);
        cleanFiles(files);

        File file = new File(files[0]);
        OGRDataStore ds = new OGRDataStore(file.getAbsolutePath(), "ESRI shapefile", null, ogr);
        SimpleFeatureType schema = DataUtilities.createType(typeName,
                "geom:Point,cat:int,name:string");
        ds.createSchema(schema);

        GeometryFactory gf = new GeometryFactory();
        // creating 20 geometries because with only a couple a finalization
        // related error that did blew up the VM would not appear
        SimpleFeature[] features = new SimpleFeature[20];
        for (int i = 0; i < features.length; i++) {
            features[i] = SimpleFeatureBuilder.build(schema,
                    new Object[] { gf.createPoint(new Coordinate(i, i)), new Integer(i), "" + i },
                    null);
        }

        FeatureWriter writer = ds.getFeatureWriterAppend("testw", Transaction.AUTO_COMMIT);
        for (int i = 0; i < features.length; i++) {
            assertFalse(writer.hasNext());
            SimpleFeature f = (SimpleFeature) writer.next();
            f.setAttributes(features[i].getAttributes());
            writer.write();
        }
        writer.close();

        FeatureReader reader = ds.getFeatureReader(new Query("testw"), null);
        for (int i = 0; i < features.length; i++) {
            assertTrue(reader.hasNext());
            SimpleFeature f = (SimpleFeature) reader.next();
            for (int j = 0; j < schema.getAttributeCount(); j++) {
                if (f.getAttribute(j) instanceof Geometry) {
                    // this is necessary because geometry equality is
                    // implemented as Geometry.equals(Geometry)
                    Geometry a = (Geometry) f.getAttribute(j);
                    Geometry b = (Geometry) features[i].getAttribute(j);
                    assertTrue(a.equals(b));
                } else {
                    assertEquals(f.getAttribute(j), features[i].getAttribute(j));
                }
            }
        }
        assertFalse(reader.hasNext());
        reader.close();
    }

    public void testAttributesWritingShapefile() throws Exception {
        FeatureCollection features = createFeatureCollection();
        File tmpFile = getTempFile("test-shp", ".shp");
        tmpFile.delete();
        OGRDataStore s = new OGRDataStore(tmpFile.getAbsolutePath(), "ESRI shapefile", null, ogr);
        writeFeatures(s, features);
    }
    
    public void testAttributesWritingGML() throws Exception {
        if(!ogrSupports("GML")) {
            System.out.println("Skipping GML writing test as OGR was not built to support it");
            
        }
        SimpleFeatureCollection features = createFeatureCollection();
        File tmpFile = getTempFile("test-gml", ".gml");
        tmpFile.delete();
        OGRDataStore s = new OGRDataStore(tmpFile.getAbsolutePath(), "GML", null, ogr);
        s.createSchema(features, true, null);
        assertEquals(1, s.getTypeNames().length);
        assertEquals(features.size(), s.getFeatureSource("junk").getFeatures().size());
    }
    
    public void testAttributesWritingKML() throws Exception {
        if(!ogrSupports("KML")) {
            System.out.println("Skipping KML writing test as OGR was not built to support it");
            
        }
        SimpleFeatureCollection features = createFeatureCollection();
        File tmpFile = getTempFile("test-kml", ".kml");
        tmpFile.delete();
        OGRDataStore s = new OGRDataStore(tmpFile.getAbsolutePath(), "KML", null, ogr);
        s.createSchema(features, true, null);
        assertEquals(1, s.getTypeNames().length);
        assertEquals(features.size(), s.getFeatureSource("junk").getFeatures().size());
    }
    

    public void testAttributeFilters() throws Exception {
        OGRDataStore s = new OGRDataStore(getAbsolutePath(STATE_POP), null, null, ogr);
        FeatureSource fs = s.getFeatureSource(s.getTypeNames()[0]);
        
        // equality filter
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        Filter f = ff.equal(ff.property("STATE_NAME"), ff.literal("New York"), true);
        assertEquals(1, fs.getFeatures(f).size());
        
        // greater than
        f = ff.greater(ff.property("PERSONS"), ff.literal(10000000));
        assertEquals(6, fs.getFeatures(f).size());
        
        // mix in a filter that cannot be encoded
        f = ff.and(f, ff.like(ff.property("STATE_NAME"), "C*"));
        assertEquals(1, fs.getFeatures(f).size());
    }
    
    public void testGeometryFilters() throws Exception {
        OGRDataStore s = new OGRDataStore(getAbsolutePath(STATE_POP), null, null, ogr);
        FeatureSource fs = s.getFeatureSource(s.getTypeNames()[0]);
        
        // from one of the GeoServer demo requests
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        Filter f = ff.bbox("the_geom", -75.102613, 40.212597, -72.361859,41.512517, null);
        assertEquals(4, fs.getFeatures(f).size());
        
        // mix in an attribute filter
        f = ff.and(f, ff.greater(ff.property("PERSONS"), ff.literal("10000000")));
        assertEquals(6, fs.getFeatures(f).size());   
    }

    // ---------------------------------------------------------------------------------------
    // SUPPORT METHODS
    // ---------------------------------------------------------------------------------------

    private int countFeatures(SimpleFeatureCollection features) {
        int count = 0;
        SimpleFeatureIterator fi = features.features();
        while(fi.hasNext()) {
            fi.next();
            count++;
        }
        return count;
    }

    protected SimpleFeatureCollection loadFeatures(String resource, Query query) throws Exception {
        SimpleFeatureSource fs = loadSource(resource, query);
        return fs.getFeatures(query);
    }

    private SimpleFeatureSource loadSource(String resource, Query query) throws IOException {
        assertNotNull(query);

        OGRDataStore s = new OGRDataStore(getAbsolutePath(resource), null, null, ogr);
        return s.getFeatureSource(s.getTypeNames()[0]);
    }

    protected SimpleFeatureCollection loadFeatures(DataStore store, String typeName) throws Exception {
        SimpleFeatureSource fs = store.getFeatureSource(typeName);
        return fs.getFeatures();
    }

    protected ListFeatureCollection createFeatureCollection() throws Exception {
        SimpleFeatureTypeBuilder tbuilder = new SimpleFeatureTypeBuilder();
        tbuilder.setName("junk");
        tbuilder.add("a", Point.class);
        tbuilder.add("b", Byte.class);
        tbuilder.add("c", Short.class);
        tbuilder.add("d", Double.class);
        tbuilder.add("e", Float.class);
        tbuilder.add("f", String.class);
        tbuilder.add("g", Date.class);
        tbuilder.add("h", Boolean.class);
        tbuilder.add("i", Number.class);
        tbuilder.add("j", Long.class);
        tbuilder.add("k", BigDecimal.class);
        tbuilder.add("l", BigInteger.class);
        SimpleFeatureType type = tbuilder.buildFeatureType();
        SimpleFeatureBuilder fb = new SimpleFeatureBuilder(type);
        ListFeatureCollection features = new ListFeatureCollection(type);
        for (int i = 0, ii = 20; i < ii; i++) {
            features.add(fb.buildFeature(null, new Object[] {
                    new GeometryFactory().createPoint(new Coordinate(1, -1)), new Byte((byte) i),
                    new Short((short) i), new Double(i), new Float(i), new String(i + " "),
                    new Date(i), new Boolean(true), new Integer(22),
                    new Long(1234567890123456789L),
                    new BigDecimal(new BigInteger("12345678901234567890123456789"), 2),
                    new BigInteger("12345678901234567890123456789") }));
        }
        return features;
    }

    private void writeFeatures(DataStore s, FeatureCollection<SimpleFeatureType, SimpleFeature> fc) throws Exception {
        SimpleFeatureType schema = fc.getSchema();
        s.createSchema(schema);
        FeatureWriter fw = s.getFeatureWriter(s.getTypeNames()[0], Transaction.AUTO_COMMIT);
        FeatureIterator it = fc.features();
        while (it.hasNext()) {
            SimpleFeature sf = (SimpleFeature) it.next();
            ((SimpleFeature) fw.next()).setAttributes(sf.getAttributes());
            fw.write();
        }
        it.close();
        fw.close();
    }

    private OGRDataStore createDataStore(File f) throws Exception {
        FeatureCollection fc = createFeatureCollection();
        f.delete();
        OGRDataStore sds = new OGRDataStore(f.getAbsolutePath(), "ESRI shapefile", null, ogr);
        writeFeatures(sds, fc);
        return sds;
    }

    private OGRDataStore createDataStore() throws Exception {
        return createDataStore(getTempFile("test-shp", ".shp"));
    }

    private String[] shapeFileNames(String typeName) {
        typeName = "target/" + typeName;
        return new String[] { typeName + ".shp", typeName + ".dbf", typeName + ".shp",
                typeName + ".shx", typeName + ".prj" };
    }

    private void cleanFiles(String[] files) {
        for (int i = 0; i < files.length; i++) {
            File f = new File(files[i]);
            if (f.exists())
                f.delete();
        }
    }
}
