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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import org.geotools.util.logging.Logging;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;

/**
 * Basic test for OGR data store capabilities against file data sources
 *
 * @author aaime
 */
public abstract class OGRDataStoreTest extends TestCaseSupport {

    static final Logger LOGGER = Logging.getLogger(OGRDataStoreTest.class);

    private List<OGRDataStore> stores = new ArrayList<>();

    protected OGRDataStoreTest(Class<? extends OGRDataStoreFactory> dataStoreFactoryClass) {
        super(dataStoreFactoryClass);
    }

    @Override
    protected void tearDown() throws Exception {
        for (OGRDataStore store : stores) {
            store.dispose();
        }
    }

    public void testGetTypeNames() throws FileNotFoundException, IOException {
        OGRDataStore store = createDataStore(getAbsolutePath(STATE_POP), null);
        assertEquals(1, store.getTypeNames().length);
        assertEquals("statepop", store.getTypeNames()[0]);
        store = createDataStore(getAbsolutePath(MIXED), null);
        assertEquals(1, store.getTypeNames().length);
        assertEquals("mixed", store.getTypeNames()[0]);
    }

    public void testSchemaPop() throws Exception {
        OGRDataStore s = createDataStore(getAbsolutePath(STATE_POP), null);
        SimpleFeatureType schema = s.getSchema(s.getTypeNames()[0]);
        assertEquals("Number of Attributes", 253, schema.getAttributeCount());
        assertTrue(
                CRS.equalsIgnoreMetadata(
                        CRS.decode("EPSG:4269", true),
                        schema.getGeometryDescriptor().getCoordinateReferenceSystem()));
    }

    public void testSchemaMix() throws Exception {
        OGRDataStore s = createDataStore(getAbsolutePath(MIXED), null);
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

    /** Test optimized count against actual count */
    public void testOptimizedEnvelope() throws Exception {
        URL url = TestData.url(STATE_POP);
        ShapefileDataStore sds = new ShapefileDataStore(url);
        OGRDataStore s = createDataStore(getAbsolutePath(STATE_POP), null);
        String typeName = s.getTypeNames()[0];

        ReferencedEnvelope expectedBounds = sds.getFeatureSource().getBounds();
        ReferencedEnvelope actualBounds = s.getFeatureSource(typeName).getBounds();
        assertEquals(expectedBounds, actualBounds);
        assertNotNull(actualBounds);
    }

    /** Test count with query startIndex and maxFeatures */
    public void testCountWithOffsetLimit() throws Exception {
        OGRDataStore s = createDataStore(getAbsolutePath(STATE_POP), null);
        String typeName = s.getTypeNames()[0];
        SimpleFeatureSource featureSource = s.getFeatureSource(typeName);
        Query query = new Query();
        query.setStartIndex(1);
        query.setMaxFeatures(1);
        assertEquals(1, featureSource.getCount(query));
    }

    /** Test count versus old DataSource */
    public void testOptimizedCount() throws Exception {
        URL url = TestData.url(STATE_POP);
        ShapefileDataStore sds = new ShapefileDataStore(url);
        OGRDataStore s = createDataStore(getAbsolutePath(STATE_POP), null);
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
        assertEquals(
                "Value of statename is wrong",
                firstFeature(features).getAttribute("STATE_NAME"),
                "Illinois");
        assertEquals(
                "Value of land area is wrong",
                ((Double) firstFeature(features).getAttribute("LAND_KM")).doubleValue(),
                143986.61,
                0.001);
    }

    public void testLoadAndCheckParentTypeIsPolygon() throws Exception {
        FeatureCollection features = loadFeatures(STATE_POP, Query.ALL);
        SimpleFeatureType schema = firstFeature(features).getFeatureType();
        assertEquals(schema.getSuper(), BasicFeatureTypes.POLYGON);
    }

    public void testShapefileComparison() throws Exception {
        URL url = TestData.url(STATE_POP);
        ShapefileDataStore sds = new ShapefileDataStore(url);
        OGRDataStore ods = createDataStore(getAbsolutePath(STATE_POP), null);

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
        OGRDataStore ods = createDataStore(getAbsolutePath(STATE_POP), null);
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
            if (ad1 instanceof GeometryDescriptor) {
                GeometryDescriptor gd1 = (GeometryDescriptor) ad1;
                GeometryDescriptor gd2 = (GeometryDescriptor) ad2;
                assertTrue(
                        CRS.equalsIgnoreMetadata(
                                gd1.getCoordinateReferenceSystem(),
                                gd2.getCoordinateReferenceSystem()));
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
                writer = sds.getFeatureWriter(typeName, Filter.INCLUDE, Transaction.AUTO_COMMIT);
                while (writer.hasNext()) {
                    writer.next();
                }
                writer.remove();
            } finally {
                if (writer != null) {
                    writer.close();
                }
            }
            assertEquals(--idx, countFeatures(loadFeatures(sds, typeName)));
        }
    }

    public void testCreateSchema() throws Exception {
        String[] fileNames = shapeFileNames("test");
        cleanFiles(fileNames);
        String absolutePath = new File(fileNames[0]).getAbsolutePath();
        OGRDataStore ds = createDataStore(absolutePath, "ESRI shapefile");
        SimpleFeatureType schema =
                DataUtilities.createType(
                        "test",
                        "geom:MultiPolygon,count:int,level1:double,level2:float,chars:string");
        ds.createSchema(schema);

        // now do some testing
        assertEquals(1, ds.getTypeNames().length);
        assertEquals("test", ds.getTypeNames()[0]);
        SimpleFeatureType ogrSchema = ds.getSchema(ds.getTypeNames()[0]);
        assertEquals(
                schema.getGeometryDescriptor().getType().getBinding(),
                ogrSchema.getGeometryDescriptor().getType().getBinding());
        for (int i = 0; i < schema.getAttributeCount(); i++) {
            AttributeDescriptor at = schema.getDescriptor(i);
            if (at == schema.getGeometryDescriptor()) continue;

            assertEquals(at.getName(), ogrSchema.getDescriptor(i).getName());
            assertEquals(
                    "Wrong type for attribute " + at.getName(),
                    at.getType().getBinding(),
                    ogrSchema.getDescriptor(i).getType().getBinding());
        }
    }

    public void testCreateWriteRead() throws Exception {
        String typeName = "testw";
        String[] files = shapeFileNames(typeName);
        cleanFiles(files);

        File file = new File(files[0]);
        OGRDataStore ds = createDataStore(file.getAbsolutePath(), "ESRI shapefile");
        SimpleFeatureType schema =
                DataUtilities.createType(typeName, "geom:Point,cat:int,name:string");
        ds.createSchema(schema);

        GeometryFactory gf = new GeometryFactory();
        // creating 20 geometries because with only a couple a finalization
        // related error that did blew up the VM would not appear
        SimpleFeature[] features = new SimpleFeature[20];
        for (int i = 0; i < features.length; i++) {
            features[i] =
                    SimpleFeatureBuilder.build(
                            schema,
                            new Object[] {
                                gf.createPoint(new Coordinate(i, i)), Integer.valueOf(i), "" + i
                            },
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
        OGRDataStore s = createDataStore(tmpFile.getAbsolutePath(), "ESRI shapefile");
        writeFeatures(s, features);
    }

    public void testAttributesWritingGeoJSON() throws Exception {
        if (!ogrSupports("GeoJSON")) {
            LOGGER.warning("Skipping GeoJSON writing test as OGR was not built to support it");
            return;
        }

        SimpleFeatureCollection features = createFeatureCollection();
        File tmpFile = getTempFile("test-geojson", ".json");
        tmpFile.delete();
        OGRDataStore s = createDataStore(tmpFile.getAbsolutePath(), "GeoJSON");
        s.createSchema(features, true, null);
        assertEquals(1, s.getTypeNames().length);
        // OGR GeoJSON layer name is "OGRGeoJSON" in GDAL up to 2.1 and "junk" (set from feature
        // collection name) in GDAL 2.2 or later. See: http://www.gdal.org/drv_geojson.html
        SimpleFeatureCollection fc = s.getFeatureSource(s.getTypeNames()[0]).getFeatures();
        assertEquals(features.size(), fc.size());
        // Read
        int c = 0;
        SimpleFeatureIterator it = fc.features();
        try {
            while (it.hasNext()) {
                SimpleFeature f = it.next();
                assertNotNull(f);
                assertNotNull(f.getDefaultGeometry());
                c++;
            }
        } finally {
            it.close();
        }
        assertEquals(fc.size(), c);
    }

    public void testAttributesWritingCsv() throws Exception {
        if (!ogrSupports("CSV")) {
            LOGGER.warning("Skipping CSV writing test as OGR was not built to support it");
            return;
        }
        SimpleFeatureCollection features = createFeatureCollection();
        File tmpFile = getTempFile("test-csv", ".csv");
        tmpFile.delete();
        OGRDataStore s = createDataStore(tmpFile.getAbsolutePath(), "CSV");
        s.createSchema(features, true, new String[] {"GEOMETRY=AS_WKT"});
        assertEquals(1, s.getTypeNames().length);
        String typeName = tmpFile.getName().substring(0, tmpFile.getName().lastIndexOf(".csv"));
        SimpleFeatureCollection fc = s.getFeatureSource(typeName).getFeatures();
        assertEquals(features.size(), fc.size());
        // Read
        int c = 0;
        SimpleFeatureIterator it = fc.features();
        try {
            while (it.hasNext()) {
                SimpleFeature f = it.next();
                assertNotNull(f);
                assertNotNull(f.getDefaultGeometry());
                c++;
            }
        } finally {
            it.close();
        }
        assertEquals(fc.size(), c);
    }

    public void testAttributesWritingGmt() throws Exception {
        if (!ogrSupports("GMT")) {
            LOGGER.warning("Skipping GMT writing test as OGR was not built to support it");
            return;
        }
        SimpleFeatureCollection features = createFeatureCollection();
        File tmpFile = getTempFile("test-gmt", ".gmt");
        tmpFile.delete();
        OGRDataStore s = createDataStore(tmpFile.getAbsolutePath(), "GMT");
        s.createSchema(features, true, null);
        try {
            assertEquals(1, s.getTypeNames().length);
        } catch (IOException e) {
            if (e.getMessage().startsWith("OGR could not open")) {
                LOGGER.log(Level.WARNING, "OGR is missing some support library, test skipped", e);
                return;
            } else {
                throw e;
            }
        }
        String typeName = tmpFile.getName().substring(0, tmpFile.getName().lastIndexOf(".gmt"));
        SimpleFeatureCollection fc = s.getFeatureSource(typeName).getFeatures();
        assertEquals(features.size(), fc.size());
        // Read
        int c = 0;
        SimpleFeatureIterator it = fc.features();
        try {
            while (it.hasNext()) {
                SimpleFeature f = it.next();
                assertNotNull(f);
                assertNotNull(f.getDefaultGeometry());
                c++;
            }
        } finally {
            it.close();
        }
        assertEquals(fc.size(), c);
    }

    public void testAttributesWritingGpx() throws Exception {
        if (!ogrSupports("GPX")) {
            LOGGER.warning("Skipping GPX writing test as OGR was not built to support it");
            return;
        }
        SimpleFeatureCollection features = createFeatureCollection();
        File tmpFile = getTempFile("test-gpx", ".gpx");
        tmpFile.delete();
        OGRDataStore s = createDataStore(tmpFile.getAbsolutePath(), "GPX");
        s.createSchema(features, true, new String[] {"GPX_USE_EXTENSIONS=YES"});
        // waypoints, routes, tracks, route_points, track_points
        try {
            assertEquals(5, s.getTypeNames().length);
        } catch (IOException e) {
            if (e.getMessage().startsWith("OGR could not open")) {
                LOGGER.log(Level.WARNING, "OGR is missing some support library, test skipped", e);
                return;
            } else {
                throw e;
            }
        }
        SimpleFeatureCollection fc = s.getFeatureSource("waypoints").getFeatures();
        assertEquals(features.size(), fc.size());
        // Read
        int c = 0;
        SimpleFeatureIterator it = fc.features();
        try {
            while (it.hasNext()) {
                SimpleFeature f = it.next();
                assertNotNull(f);
                assertNotNull(f.getDefaultGeometry());
                c++;
            }
        } finally {
            it.close();
        }
        assertEquals(fc.size(), c);
    }

    public void testAttributesWritingGML() throws Exception {
        if (!ogrSupports("GML")) {
            LOGGER.warning("Skipping GML writing test as OGR was not built to support it");
            return;
        }
        SimpleFeatureCollection features = createFeatureCollection();
        File tmpFile = getTempFile("test-gml", ".gml");
        tmpFile.delete();
        OGRDataStore s = createDataStore(tmpFile.getAbsolutePath(), "GML");
        s.createSchema(features, true, null);
        try {
            assertEquals(1, s.getTypeNames().length);
        } catch (IOException e) {
            if (e.getMessage().startsWith("OGR could not open")) {
                LOGGER.log(Level.WARNING, "OGR is missing some support library, test skipped", e);
                return;
            } else {
                throw e;
            }
        }
        SimpleFeatureCollection fc = s.getFeatureSource("junk").getFeatures();
        assertEquals(features.size(), fc.size());
        // Read
        int c = 0;
        SimpleFeatureIterator it = fc.features();
        try {
            while (it.hasNext()) {
                SimpleFeature f = it.next();
                assertNotNull(f);
                assertNotNull(f.getDefaultGeometry());
                c++;
            }
        } finally {
            it.close();
        }
        assertEquals(fc.size(), c);
    }

    public void testAttributesWritingKML() throws Exception {
        if (!ogrSupports("KML")) {
            LOGGER.warning("Skipping KML writing test as OGR was not built to support it");
            return;
        }
        SimpleFeatureCollection features = createFeatureCollection();
        File tmpFile = getTempFile("test-kml", ".kml");
        tmpFile.delete();
        OGRDataStore s = createDataStore(tmpFile.getAbsolutePath(), "KML");
        s.createSchema(features, true, null);
        try {
            assertEquals(1, s.getTypeNames().length);
        } catch (IOException e) {
            if (e.getMessage().startsWith("OGR could not open")) {
                LOGGER.log(Level.WARNING, "OGR is missing some support library, test skipped", e);
                return;
            } else {
                throw e;
            }
        }
        SimpleFeatureCollection fc = s.getFeatureSource("junk").getFeatures();
        assertEquals(features.size(), fc.size());
        // Read
        int c = 0;
        SimpleFeatureIterator it = fc.features();
        try {
            while (it.hasNext()) {
                SimpleFeature f = it.next();
                assertNotNull(f);
                assertNotNull(f.getDefaultGeometry());
                c++;
            }
        } finally {
            it.close();
        }
        assertEquals(fc.size(), c);
    }

    public void testAttributesWritingGeoRSS() throws Exception {
        if (!ogrSupports("GeoRSS")) {
            LOGGER.warning("Skipping GeoRSS writing test as OGR was not built to support it");
            return;
        }
        // Write
        SimpleFeatureCollection features = createFeatureCollection();
        File tmpFile = getTempFile("test-georss", ".rss");
        tmpFile.delete();
        OGRDataStore s = createDataStore(tmpFile.getAbsolutePath(), "GeoRSS");
        s.createSchema(
                features,
                true,
                new String[] {
                    "FORMAT=RSS",
                    "GEOM_DIALECT=SIMPLE",
                    "USE_EXTENSIONS=YES",
                    "TITLE=f",
                    "DESCRIPTION=f"
                });
        try {
            assertEquals(1, s.getTypeNames().length);
        } catch (IOException e) {
            if (e.getMessage().startsWith("OGR could not open")) {
                LOGGER.log(Level.WARNING, "OGR is missing some support library, test skipped", e);
                return;
            } else {
                throw e;
            }
        }
        SimpleFeatureCollection fc = s.getFeatureSource("georss").getFeatures();
        assertEquals(features.size(), fc.size());
        // Read
        int c = 0;
        SimpleFeatureIterator it = fc.features();
        try {
            while (it.hasNext()) {
                SimpleFeature f = it.next();
                assertNotNull(f);
                assertNotNull(f.getDefaultGeometry());
                c++;
            }
        } finally {
            it.close();
        }
        assertEquals(fc.size(), c);
    }

    public void testAttributeFilters() throws Exception {
        OGRDataStore s = createDataStore(getAbsolutePath(STATE_POP), null);
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

    public void testAttributesWritingSqlite() throws Exception {
        if (!ogrSupports("SQLite")) {
            LOGGER.warning("Skipping SQLLite writing test as OGR was not built to support it");
            return;
        }
        SimpleFeatureCollection features = createFeatureCollection();
        File tmpFile = getTempFile("test-sql", ".sqlite");
        tmpFile.delete();
        OGRDataStore s = createDataStore(tmpFile.getAbsolutePath(), "SQLite");
        s.createSchema(features, true, null);
        assertEquals(1, s.getTypeNames().length);
        SimpleFeatureCollection fc = s.getFeatureSource("junk").getFeatures();
        assertEquals(features.size(), fc.size());
        // Read
        int c = 0;
        SimpleFeatureIterator it = fc.features();
        try {
            while (it.hasNext()) {
                SimpleFeature f = it.next();
                assertNotNull(f);
                assertNotNull(f.getDefaultGeometry());
                c++;
            }
        } finally {
            it.close();
        }
        assertEquals(fc.size(), c);
    }

    public void testAttributesWritingSqliteWithSorting() throws Exception {
        if (!ogrSupports("SQLite")) {
            LOGGER.warning("Skipping SQLLite writing test as OGR was not built to support it");
            return;
        }
        SimpleFeatureCollection features = createFeatureCollection();
        File tmpFile = getTempFile("test-sql", ".sqlite");
        tmpFile.delete();
        OGRDataStore s = createDataStore(tmpFile.getAbsolutePath(), "SQLite");
        s.createSchema(features, true, null);
        assertEquals(1, s.getTypeNames().length);
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        Query query = new Query();
        query.setSortBy(
                new org.opengis.filter.sort.SortBy[] {
                    ff.sort("float", org.opengis.filter.sort.SortOrder.ASCENDING)
                });
        SimpleFeatureCollection fc = s.getFeatureSource("junk").getFeatures(query);
        assertEquals(features.size(), fc.size());
        // Read
        int c = 0;
        SimpleFeatureIterator it = fc.features();
        try {
            while (it.hasNext()) {
                SimpleFeature f = it.next();
                assertNotNull(f);
                assertNotNull(f.getDefaultGeometry());
                c++;
            }
        } finally {
            it.close();
        }
        assertEquals(fc.size(), c);
    }

    public void testAttributesWritingSqliteFromUpperCaseAttributes() throws Exception {
        if (!ogrSupports("SQLite")) {
            LOGGER.warning("Skipping SQLite writing test as OGR was not built to support it");
            return;
        }
        SimpleFeatureCollection features = createFeatureCollectionWithUpperCaseAttributes();
        File tmpFile = getTempFile("test-sqlite", ".db");
        tmpFile.delete();
        OGRDataStore s = createDataStore(tmpFile.getAbsolutePath(), "SQLite");
        s.createSchema(features, true, null);
        assertEquals(1, s.getTypeNames().length);
        SimpleFeatureCollection fc = s.getFeatureSource("points").getFeatures();
        assertEquals(features.size(), fc.size());
        // Read
        int c = 0;
        SimpleFeatureIterator it = fc.features();
        try {
            while (it.hasNext()) {
                SimpleFeature f = it.next();
                assertNotNull(f);
                assertNotNull(f.getDefaultGeometry());
                assertNotNull(f.getAttribute("name"));
                c++;
            }
        } finally {
            it.close();
        }
        assertEquals(fc.size(), c);
    }

    protected ListFeatureCollection createFeatureCollectionWithUpperCaseAttributes()
            throws Exception {
        SimpleFeatureTypeBuilder tbuilder = new SimpleFeatureTypeBuilder();
        tbuilder.setName("points");
        tbuilder.add("geom", Point.class);
        tbuilder.add("NAME", String.class);
        SimpleFeatureType type = tbuilder.buildFeatureType();
        SimpleFeatureBuilder fb = new SimpleFeatureBuilder(type);
        ListFeatureCollection features = new ListFeatureCollection(type);
        for (int i = 0, ii = 20; i < ii; i++) {
            features.add(
                    fb.buildFeature(
                            null,
                            new Object[] {
                                new GeometryFactory().createPoint(new Coordinate(1, -1)),
                                "Point" + String.valueOf(i)
                            }));
        }
        return features;
    }

    public void testGeometryFilters() throws Exception {
        OGRDataStore s = createDataStore(getAbsolutePath(STATE_POP), null);
        FeatureSource fs = s.getFeatureSource(s.getTypeNames()[0]);

        // from one of the GeoServer demo requests
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        Filter f = ff.bbox("the_geom", -75.102613, 40.212597, -72.361859, 41.512517, null);

        int numberOfFeatures = fs.getFeatures(f).size();
        // If GDAL is compiled with GEOS,
        // the actual geometries are compared and the result will be 4
        // If GDAL is NOT compiled with GEOS,
        // only the bounding boxes are compared the result will be 5
        assertEquals(s.ogr.IsGEOSEnabled() ? 4 : 5, numberOfFeatures);

        // mix in an attribute filter
        f = ff.and(f, ff.greater(ff.property("PERSONS"), ff.literal("10000000")));
        assertEquals(6, fs.getFeatures(f).size());
    }

    public void testGeot5588() throws Exception {
        // Definition Table
        //   Type NATIVE Charset "Neutral"
        //   Fields 3
        //     byte_field Integer (3) ;
        //     short_field Integer (5) ;
        //     int_field_0 Integer ;

        String tabFile = getAbsolutePath("geot5588/geot5588a.tab");
        OGRDataStore s = createDataStore(tabFile, "MapInfo File");

        SimpleFeatureType schema = s.getSchema("geot5588a");
        assertEquals(Short.class, schema.getDescriptor("byte_field").getType().getBinding());
        assertEquals(Integer.class, schema.getDescriptor("short_field").getType().getBinding());
        assertEquals(BigInteger.class, schema.getDescriptor("int_field_0").getType().getBinding());

        // Check contents
        FeatureSource<SimpleFeatureType, SimpleFeature> fs = s.getFeatureSource("geot5588a");
        try (FeatureIterator<SimpleFeature> iterator = fs.getFeatures().features()) {
            // Test data should contain one feature
            assertTrue(iterator.hasNext());
            SimpleFeature feature = iterator.next();
            assertEquals((short) 999, feature.getAttribute("byte_field"));
            assertEquals(99999, feature.getAttribute("short_field"));
            assertEquals(BigInteger.valueOf(257), feature.getAttribute("int_field_0"));

            // There shouldn't be any more features from here
            assertFalse(iterator.hasNext());
        }
    }

    public OGRDataStore createDataStore(String tabFile, String s) throws IOException {
        OGRDataStore store = new OGRDataStore(tabFile, s, null, ogr);
        stores.add(store);
        return store;
    }

    // ---------------------------------------------------------------------------------------
    // SUPPORT METHODS
    // ---------------------------------------------------------------------------------------

    private int countFeatures(SimpleFeatureCollection features) {
        int count = 0;
        try (SimpleFeatureIterator fi = features.features()) {
            while (fi.hasNext()) {
                fi.next();
                count++;
            }
        }
        return count;
    }

    protected SimpleFeatureCollection loadFeatures(String resource, Query query) throws Exception {
        SimpleFeatureSource fs = loadSource(resource, query);
        return fs.getFeatures(query);
    }

    private SimpleFeatureSource loadSource(String resource, Query query) throws IOException {
        assertNotNull(query);

        OGRDataStore s = createDataStore(getAbsolutePath(resource), null);
        return s.getFeatureSource(s.getTypeNames()[0]);
    }

    protected SimpleFeatureCollection loadFeatures(DataStore store, String typeName)
            throws Exception {
        SimpleFeatureSource fs = store.getFeatureSource(typeName);
        return fs.getFeatures();
    }

    protected ListFeatureCollection createFeatureCollection() throws Exception {
        SimpleFeatureTypeBuilder tbuilder = new SimpleFeatureTypeBuilder();
        tbuilder.setName("junk");
        tbuilder.add("point", Point.class);
        tbuilder.add("byte", Byte.class);
        tbuilder.add("short", Short.class);
        tbuilder.add("double", Double.class);
        tbuilder.add("float", Float.class);
        tbuilder.add("string", String.class);
        tbuilder.add("date", Date.class);
        tbuilder.add("boolean", Boolean.class);
        tbuilder.add("number", Number.class);
        tbuilder.add("long", Long.class);
        tbuilder.add("bigdecimal", BigDecimal.class);
        tbuilder.add("biginteger", BigInteger.class);
        SimpleFeatureType type = tbuilder.buildFeatureType();
        SimpleFeatureBuilder fb = new SimpleFeatureBuilder(type);
        ListFeatureCollection features = new ListFeatureCollection(type);
        for (int i = 0, ii = 20; i < ii; i++) {
            features.add(
                    fb.buildFeature(
                            null,
                            new Object[] {
                                new GeometryFactory().createPoint(new Coordinate(1, -1)),
                                Byte.valueOf((byte) i),
                                Short.valueOf((short) i),
                                Double.valueOf(i),
                                Float.valueOf(i),
                                new String(i + " "),
                                new Date(i),
                                Boolean.valueOf(true),
                                Integer.valueOf(22),
                                Long.valueOf(1234567890123456789L),
                                new BigDecimal(new BigInteger("12345678901234567890123456789"), 2),
                                new BigInteger("12345678901234567890123456789")
                            }));
        }
        return features;
    }

    private void writeFeatures(DataStore s, FeatureCollection<SimpleFeatureType, SimpleFeature> fc)
            throws Exception {
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
        OGRDataStore sds = createDataStore(f.getAbsolutePath(), "ESRI shapefile");
        writeFeatures(sds, fc);
        return sds;
    }

    private OGRDataStore createDataStore() throws Exception {
        return createDataStore(getTempFile("test-shp", ".shp"));
    }

    private String[] shapeFileNames(String typeName) {
        typeName = "target/" + typeName;
        return new String[] {
            typeName + ".shp",
            typeName + ".dbf",
            typeName + ".shp",
            typeName + ".shx",
            typeName + ".prj"
        };
    }

    private void cleanFiles(String[] files) {
        for (int i = 0; i < files.length; i++) {
            File f = new File(files[i]);
            if (f.exists()) f.delete();
        }
    }
}
