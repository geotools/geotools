/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.shapefile.shp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.URL;
import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.geometry.jts.LiteCoordinateSequence;
import org.geotools.test.TestData;
import org.geotools.util.factory.Hints;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/** @author ian */
public class ZMHandlersTest {

    /**
     * Test method for {@link org.geotools.data.shapefile.shp.PointHandler#read(java.nio.ByteBuffer,
     * org.geotools.data.shapefile.shp.ShapeType, boolean)}.
     */
    @Test
    public void testReadMZPoints() throws ShapefileException, IOException {
        URL url = TestData.url(ShapefileDataStore.class, "mzvalues/zmpoints.shp");
        ShapefileDataStore store = new ShapefileDataStore(url);
        SimpleFeature feature = DataUtilities.first(store.getFeatureSource().getFeatures());
        Geometry geom = (Geometry) feature.getDefaultGeometry();
        assertEquals("wrong x", 10, geom.getCoordinate().getX(), 0.00001);
        assertEquals("wrong y", 5, geom.getCoordinate().getY(), 0.00001);
        assertEquals("wrong z", 1, geom.getCoordinate().getZ(), 0.00001);
        assertEquals("wrong m", 20, geom.getCoordinate().getM(), 0.00001);
        store.dispose();
    }

    @Test
    public void testReadMPoints() throws ShapefileException, IOException {
        URL url = TestData.url(ShapefileDataStore.class, "mzvalues/mpoints.shp");
        ShapefileDataStore store = new ShapefileDataStore(url);
        SimpleFeature feature = DataUtilities.first(store.getFeatureSource().getFeatures());
        Geometry geom = (Geometry) feature.getDefaultGeometry();
        assertEquals("wrong x", 10, geom.getCoordinate().getX(), 0.00001);
        assertEquals("wrong y", 5, geom.getCoordinate().getY(), 0.00001);

        assertEquals("wrong m", 20, geom.getCoordinate().getM(), 0.00001);
        store.dispose();
    }

    @Test
    public void testReadZPoints() throws IOException {
        // tests that Point with Z and without optional M are correctly parsed
        URL url = TestData.url(ShapefileDataStore.class, "mzvalues/pointZ.shp");
        ShapefileDataStore store = new ShapefileDataStore(url);
        Query q = new Query(store.getTypeNames()[0]);
        q.getHints().put(Hints.FEATURE_2D, Boolean.TRUE);
        try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                store.getFeatureReader(q, Transaction.AUTO_COMMIT)) {
            while (reader.hasNext()) {
                SimpleFeature f = reader.next();
                assertNotNull(f.getDefaultGeometry());
            }
        }
        store.dispose();
    }

    @Test
    public void testReadMZMultiPointsWithOptionalM() throws ShapefileException, IOException {
        URL url = TestData.url(ShapefileDataStore.class, "mzvalues/zmmultipoints.shp");
        ShapefileDataStore store = new ShapefileDataStore(url);
        SimpleFeature feature = DataUtilities.first(store.getFeatureSource().getFeatures());
        MultiPoint geom = (MultiPoint) feature.getDefaultGeometry();
        Coordinate coordinate = geom.getCoordinates()[0];

        assertEquals("wrong x", 1208.5983, coordinate.getX(), 0.001);
        assertEquals("wrong y", 924.8555, coordinate.getY(), 0.001);
        assertEquals("wrong z", 20, coordinate.getZ(), 0.00001);
        assertEquals("wrong m", 10, coordinate.getM(), 0.00001);
        store.dispose();
    }

    @Test
    public void testReadZMultiPointsWithOptionalM() throws ShapefileException, IOException {
        // test multipoints with type ArcZ and optional M are parsed
        URL url = TestData.url(ShapefileDataStore.class, "mzvalues/multipointsz.shp");
        ShapefileDataStore store = new ShapefileDataStore(url);
        SimpleFeature feature = DataUtilities.first(store.getFeatureSource().getFeatures());
        MultiPoint geom = (MultiPoint) feature.getDefaultGeometry();
        Coordinate coordinate = geom.getCoordinates()[0];

        assertEquals("wrong x", 24.54682976316134, coordinate.getX(), 0d);
        assertEquals("wrong y", 60.81447758588624, coordinate.getY(), 0d);
        assertEquals("wrong z", 0d, coordinate.getZ(), 0d);
        store.dispose();
    }

    @Test
    public void testReadMMultiPoints() throws ShapefileException, IOException {
        URL url = TestData.url(ShapefileDataStore.class, "mzvalues/zmmultipoints.shp");
        ShapefileDataStore store = new ShapefileDataStore(url);
        SimpleFeature feature = DataUtilities.first(store.getFeatureSource().getFeatures());
        MultiPoint geom = (MultiPoint) feature.getDefaultGeometry();
        Coordinate coordinate = geom.getCoordinates()[0];

        assertEquals("wrong x", 1208.5983, coordinate.getX(), 0.001);
        assertEquals("wrong y", 924.8555, coordinate.getY(), 0.001);
        assertEquals("wrong m", 10, coordinate.getM(), 0.00001);
        store.dispose();
    }

    @Test
    public void testReadMZLineWithOptionalM() throws ShapefileException, IOException {
        // test line with type ArcTypeZ and optional M is parsed
        URL url = TestData.url(ShapefileDataStore.class, "mzvalues/mzlines.shp");
        ShapefileDataStore store = new ShapefileDataStore(url);
        SimpleFeature feature = DataUtilities.first(store.getFeatureSource().getFeatures());
        MultiLineString geom = (MultiLineString) feature.getDefaultGeometry();
        Coordinate coordinate = geom.getCoordinates()[0];

        assertEquals("wrong x", 589.4648, coordinate.getX(), 0.001);
        assertEquals("wrong y", 909.9963, coordinate.getY(), 0.001);
        assertEquals("wrong z", 20, coordinate.getZ(), 0.00001);
        assertEquals("wrong m", 10, coordinate.getM(), 0.00001);
        store.dispose();
    }

    @Test
    public void testReadMLine() throws ShapefileException, IOException {
        URL url = TestData.url(ShapefileDataStore.class, "mzvalues/mlines.shp");
        ShapefileDataStore store = new ShapefileDataStore(url);
        SimpleFeature feature = DataUtilities.first(store.getFeatureSource().getFeatures());
        MultiLineString geom = (MultiLineString) feature.getDefaultGeometry();
        Coordinate coordinate = geom.getCoordinates()[0];

        assertEquals("wrong x", 589.4648, coordinate.getX(), 0.001);
        assertEquals("wrong y", 909.9963, coordinate.getY(), 0.001);
        assertEquals("wrong m", 10, coordinate.getM(), 0.00001);
        store.dispose();
    }

    @Test
    public void testReadZLineWithOptionalM() throws ShapefileException, IOException {
        URL url = TestData.url(ShapefileDataStore.class, "mzvalues/linez.shp");
        ShapefileDataStore store = new ShapefileDataStore(url);
        SimpleFeature feature = DataUtilities.first(store.getFeatureSource().getFeatures());
        MultiLineString geom = (MultiLineString) feature.getDefaultGeometry();
        Coordinate coordinate = geom.getCoordinates()[0];

        assertEquals("wrong x", 340d, coordinate.getX(), 0.001);
        assertEquals("wrong y", 377d, coordinate.getY(), 0.001);
        assertEquals("wrong z", 3d, coordinate.getZ(), 0.00001);
        store.dispose();
    }

    @Test
    public void testGEOT6521() throws IOException {
        // can we read the tazmania_roads shapefile
        URL url = TestData.url(ShapefileDataStore.class, "taz_shapes/tasmania_roads.shp");
        ShapefileDataStore store = new ShapefileDataStore(url);

        SimpleFeature feature = DataUtilities.first(store.getFeatureSource().getFeatures());

        MultiLineString geom = (MultiLineString) feature.getDefaultGeometry();
        Coordinate[] coordinates = geom.getCoordinates();
        double[] expected = {
            146.468582,
            -41.241478,
            -41.241478,
            146.574768,
            -41.251186,
            -41.251186,
            146.640411,
            -41.255154,
            -41.255154,
            146.766129,
            -41.332348,
            -41.332348,
            146.794189,
            -41.344170,
            -41.344170,
            146.822174,
            -41.362988,
            -41.362988,
            146.863434,
            -41.380234,
            -41.380234,
            146.899521,
            -41.379452,
            -41.379452,
            146.929504,
            -41.378227,
            -41.378227,
            147.008041,
            -41.356079,
            -41.356079,
            147.098343,
            -41.362919,
            -41.362919
        };
        for (int i = 0, k = 0; i < coordinates.length; i++, k += 3) {
            Coordinate coordinate = coordinates[i];
            assertEquals("wrong x", expected[k], coordinate.getX(), 0.001);
            assertEquals("wrong y", expected[k + 1], coordinate.getY(), 0.001);
            assertEquals("wrong z", expected[k + 2], coordinate.getZ(), 0.00001);
        }

        //        Query query = new Query();
        //        query.getHints().put(Hints.FEATURE_2D, Boolean.TRUE);
        //        try (SimpleFeatureIterator itr =
        // store.getFeatureSource().getFeatures(query).features()) {
        //            while (itr.hasNext()) {
        //                SimpleFeature f = itr.next();
        //                System.out.println(f.getDefaultGeometryProperty());
        //            }
        //        }
        store.dispose();
    }

    /**
     * Checks we can read all the features in the given shapefile as a flat geometry set.
     *
     * @returns the total number of features read
     */
    private int readFlatFeatures(String filename) throws IOException {
        int numFeatures = 0;
        URL url = TestData.url(ShapefileDataStore.class, filename);
        ShapefileDataStore store = new ShapefileDataStore(url);
        try {
            Query q = new Query(store.getTypeNames()[0]);
            q.getHints().put(Hints.FEATURE_2D, Boolean.TRUE);
            try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                    store.getFeatureReader(q, Transaction.AUTO_COMMIT)) {
                while (reader.hasNext()) {
                    SimpleFeature f = reader.next();
                    assertNotNull(f.getDefaultGeometry());
                    numFeatures++;
                }
                return numFeatures;
            }
        } finally {
            store.dispose();
        }
    }

    @Test
    public void testReadFlatLines() throws IOException {
        // can we read the tazmania_roads shapefile as a flat geometry set.
        assertEquals("wrong num features", 14, readFlatFeatures("taz_shapes/tasmania_roads.shp"));
    }

    @Test
    public void testReadFlatPolygons() throws IOException {
        assertEquals("wrong num features", 2, readFlatFeatures("mzvalues/mzpolygons.shp"));
    }

    @Test
    public void testReadFlatPoints() throws IOException {
        assertEquals("wrong num features", 99, readFlatFeatures("mzvalues/zmpoints.shp"));
    }

    @Test
    public void testReadFlatMultiPoints() throws IOException {
        assertEquals("wrong num features", 2, readFlatFeatures("mzvalues/zmmultipoints.shp"));
    }

    /**
     * Checks we can read all the features in the given shapefile as regular (3D) geometry.
     *
     * @returns the total number of features read
     */
    private int readAllFeatures(String filename) throws IOException {
        int numFeatures = 0;
        URL url = TestData.url(ShapefileDataStore.class, filename);
        ShapefileDataStore store = new ShapefileDataStore(url);
        try {
            Query q = new Query(store.getTypeNames()[0]);
            try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                    store.getFeatureReader(q, Transaction.AUTO_COMMIT)) {
                while (reader.hasNext()) {
                    SimpleFeature f = reader.next();
                    assertNotNull(f.getDefaultGeometry());
                    numFeatures++;
                }
                return numFeatures;
            }
        } finally {
            store.dispose();
        }
    }

    @Test
    public void testReadAllLines() throws IOException {
        // tests the parsing for all the variants of ZM Lines

        // ArcTypeZ with optional M
        assertEquals("wrong num features", 2, readAllFeatures("mzvalues/mzlines.shp"));
        // ArcTypeM
        assertEquals("wrong num features", 2, readAllFeatures("mzvalues/mlines.shp"));
        // ArcTypeZ with no optional M
        assertEquals("wrong num features", 1, readAllFeatures("mzvalues/linez.shp"));
        assertEquals("wrong num features", 4, readAllFeatures("mzvalues/zlines.shp"));
    }

    @Test
    public void testReadAllPolygons() throws IOException {
        // tests the parsing for all the variants of ZM Polygons

        // ArcTypeZ with optional M
        assertEquals("wrong num features", 2, readAllFeatures("mzvalues/mzpolygons.shp"));
        // ArcTypeM
        assertEquals("wrong num features", 2, readAllFeatures("mzvalues/mpolygons.shp"));
        // ArcTypeZ with no optional M
        assertEquals("wrong num features", 1, readAllFeatures("mzvalues/polygonz.shp"));
        assertEquals("wrong num features", 4, readAllFeatures("mzvalues/zpolygons.shp"));
    }

    @Test
    public void testReadAllPoints() throws IOException {
        // tests the parsing for all the variants of ZM Points

        // ArcTypeZ with optional M
        assertEquals("wrong num features", 99, readAllFeatures("mzvalues/zmpoints.shp"));
        // ArcTypeM
        assertEquals("wrong num features", 99, readAllFeatures("mzvalues/mpoints.shp"));
        // ArcTypeZ with no optional M
        assertEquals("wrong num features", 1, readAllFeatures("mzvalues/pointZ.shp"));
    }

    @Test
    public void testReadAllMultiPoints() throws IOException {
        // tests the parsing for all the variants of ZM MultiPoints

        // ArcTypeZ with optional M
        assertEquals("wrong num features", 2, readAllFeatures("mzvalues/zmmultipoints.shp"));
        // ArcTypeM
        assertEquals("wrong num features", 2, readAllFeatures("mzvalues/mmultipoints.shp"));
        // ArcTypeZ with no optional M
        assertEquals("wrong num features", 1, readAllFeatures("mzvalues/multipointsz.shp"));
        assertEquals("wrong num features", 4, readAllFeatures("mzvalues/zmultipoints.shp"));
    }

    @Test
    public void testReadMZPolygon() throws ShapefileException, IOException {
        URL url = TestData.url(ShapefileDataStore.class, "mzvalues/mzpolygons.shp");
        ShapefileDataStore store = new ShapefileDataStore(url);
        SimpleFeature feature = DataUtilities.first(store.getFeatureSource().getFeatures());
        MultiPolygon geom = (MultiPolygon) feature.getDefaultGeometry();
        Coordinate coordinate = geom.getCoordinates()[0];

        assertEquals("wrong x", 96.944404, coordinate.getX(), 0.001);
        assertEquals("wrong y", 653.67509, coordinate.getY(), 0.001);
        assertEquals("wrong z", 20, coordinate.getZ(), 0.00001);
        assertEquals("wrong m", 10, coordinate.getM(), 0.00001);
        store.dispose();
    }

    @Test
    public void testReadMPolygon() throws ShapefileException, IOException {
        URL url = TestData.url(ShapefileDataStore.class, "mzvalues/mpolygons.shp");
        ShapefileDataStore store = new ShapefileDataStore(url);
        SimpleFeature feature = DataUtilities.first(store.getFeatureSource().getFeatures());
        MultiPolygon geom = (MultiPolygon) feature.getDefaultGeometry();
        Coordinate coordinate = geom.getCoordinates()[0];

        assertEquals("wrong x", 96.944404, coordinate.getX(), 0.001);
        assertEquals("wrong y", 653.67509, coordinate.getY(), 0.001);
        assertEquals("wrong m", 10, coordinate.getM(), 0.00001);
        store.dispose();
    }

    @Test
    public void testReadZPolygonWithOptionalM() throws ShapefileException, IOException {
        // test Polygon with type ArcZ and optional M is parsed
        URL url = TestData.url(ShapefileDataStore.class, "mzvalues/polygonz.shp");
        ShapefileDataStore store = new ShapefileDataStore(url);
        SimpleFeature feature = DataUtilities.first(store.getFeatureSource().getFeatures());
        MultiPolygon geom = (MultiPolygon) feature.getDefaultGeometry();
        Coordinate coordinate = geom.getCoordinates()[0];

        assertEquals("wrong x", 665d, coordinate.getX(), 0.001);
        assertEquals("wrong y", 294d, coordinate.getY(), 0.001);
        assertEquals("wrong z", 1d, coordinate.getZ(), 0.00001);
        store.dispose();
    }

    /**
     * GeoServer can't display PolygonZ shapefile
     *
     * @throws ShapefileException
     * @throws IOException
     */
    @Test
    public void testGeot6599() throws ShapefileException, IOException {
        URL url = TestData.url(ShapefileDataStore.class, "mzvalues/building.shp");
        ShapefileDataStore store = new ShapefileDataStore(url);
        SimpleFeature feature = DataUtilities.first(store.getFeatureSource().getFeatures());

        Double[][] expected = {
            {-72.7143910882242, 41.7223262507114, 0.0},
            {-72.7144337255386, 41.7223237677143, 73.2161},
            {-72.7144418180624, 41.7223378289697, 77.1782},
            {-72.7144182172218, 41.7223454525823, 67.7665},
            {-72.7143004326287, 41.7223834996066, 68.0616},
            {-72.7142721209997, 41.7223343062061, 69.9239},
            {-72.7143910882242, 41.7223262507114, 0.0}
        };

        MultiPolygon geom = (MultiPolygon) feature.getDefaultGeometry();

        for (int i = 0; i < expected.length; i++) {
            Coordinate coordinate = geom.getCoordinates()[i];
            assertEquals("wrong x", expected[i][0], coordinate.getX(), 0.001);
            assertEquals("wrong y", expected[i][1], coordinate.getY(), 0.001);
            assertEquals("wrong z", expected[i][2], coordinate.getZ(), 0.00001);
        }

        Query q = new Query(store.getTypeNames()[0]);

        if (q.getHints() == null) {
            q.setHints(new Hints(Hints.FEATURE_2D, Boolean.TRUE));
        } else {
            q.getHints().put(Hints.FEATURE_2D, Boolean.TRUE);
        }
        try (SimpleFeatureIterator itr = store.getFeatureSource().getFeatures(q).features()) {
            while (itr.hasNext()) {
                feature = itr.next();
                geom = (MultiPolygon) feature.getDefaultGeometry();
                // System.out.println(feature.getID());
                if (geom != null) {
                    // System.out.println(geom.getDimension());
                    // System.out.println(geom.getCoordinate());
                    LiteCoordinateSequence.cloneGeometry(geom);
                }
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }
        store.dispose();
    }

    @Test
    public void testReadZPointsFalse2D() throws IOException {
        // tests that Point with Z and without optional M and with Feature2D false are correctly
        // parsed
        URL url = TestData.url(ShapefileDataStore.class, "mzvalues/pointZ.shp");
        ShapefileDataStore store = new ShapefileDataStore(url);
        Query q = new Query(store.getTypeNames()[0]);
        q.getHints().put(Hints.FEATURE_2D, Boolean.FALSE);
        try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                store.getFeatureReader(q, Transaction.AUTO_COMMIT)) {
            while (reader.hasNext()) {
                SimpleFeature f = reader.next();
                assertNotNull(f.getDefaultGeometry());
            }
        }
        store.dispose();
    }
}
