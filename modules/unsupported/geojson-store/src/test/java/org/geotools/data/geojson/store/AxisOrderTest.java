package org.geotools.data.geojson.store;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.TestData;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.DataUtilities;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.CRS;
import org.geotools.util.logging.Logging;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class AxisOrderTest {

    private static DataStore store;
    private static SimpleFeatureCollection features;
    private static File directory;
    private static FileDataStore parkStore;
    private static GeometryFactory gf = new GeometryFactory();
    static final Logger LOGGER = Logging.getLogger(AxisOrderTest.class);

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        URL states = TestData.url("shapes/statepop.shp");
        states = org.geotools.test.TestData.url(AxisOrderTest.class, "atlas.shp");
        Map<String, Object> params = new HashMap<>();
        params.put(ShapefileDataStoreFactory.URLP.key, states);
        try {
            params.put(
                    ShapefileDataStoreFactory.NAMESPACEP.key,
                    new URI("http://localhost/geotools.xsd"));
        } catch (URISyntaxException e) {
            LOGGER.log(Level.SEVERE, "", e);
        }
        store = DataStoreFinder.getDataStore(params);

        assertNotNull(store);
        features = store.getFeatureSource(store.getTypeNames()[0]).getFeatures();

        try { // Make sure the CRS is correctly set features = new
            ReprojectingFeatureCollection(features, CRS.decode("EPSG:4269"));
        } catch (FactoryException e) {
            LOGGER.log(Level.SEVERE, "", e);
        }
        File parks = org.geotools.test.TestData.file(AxisOrderTest.class, "national-parks.shp");
        parkStore = FileDataStoreFinder.getDataStore(parks);
        directory = File.createTempFile("geojson", "");
        boolean exists = directory.exists();
        if (exists) {

            if (!directory.delete()) {
                throw new IOException("could not delete: " + directory);
            }
        }
        directory.mkdirs();
    }

    private static void ReprojectingFeatureCollection(
            SimpleFeatureCollection features2, CoordinateReferenceSystem decode) {
        // TODO Auto-generated method stub

    }

    @Before
    public void setUp() throws Exception {}

    @AfterClass
    public static void tearClassDown() {
        directory.delete();
    }

    @Test
    public void test27700() throws IOException {
        File file2 = new File(directory, "parks.geojson");

        SimpleFeatureCollection pFeatures = parkStore.getFeatureSource().getFeatures();

        Map<String, Serializable> params2 = new HashMap<>();
        params2.put(GeoJSONDataStoreFactory.FILE_PARAM.key, file2);

        GeoJSONDataStoreFactory factory = new GeoJSONDataStoreFactory();
        DataStore duplicate = factory.createNewDataStore(params2);
        SimpleFeatureType featureType = pFeatures.getSchema();
        duplicate.createSchema(featureType);

        String typeName = duplicate.getTypeNames()[0];
        SimpleFeatureSource source = duplicate.getFeatureSource(typeName);
        if (source instanceof SimpleFeatureStore) {
            SimpleFeatureStore outStore = (SimpleFeatureStore) source;
            outStore.addFeatures(pFeatures);
        } else {
            fail("Can't write to GeoJSONDatastore " + file2);
        }

        String contents = GeoJSONTestSupport.getFileContents(file2);
        int index = contents.indexOf("coordinates");
        assertTrue("No Coordinates in output file", index > 0);
        String test = contents.substring(index + 17, index + 32);
        assertTrue("bad coords", "-3.3496,56.8215".equalsIgnoreCase(test));
        String[] c = test.split(",");
        assertTrue(Double.parseDouble(c[0]) < Double.parseDouble(c[1]));
    }

    @Test
    public void test31287() throws IOException, NoSuchAuthorityCodeException, FactoryException {

        File file2 = new File(directory, "out31287.geojson");

        Map<String, Serializable> params2 = new HashMap<>();
        params2.put(GeoJSONDataStoreFactory.FILE_PARAM.key, file2);

        GeoJSONDataStoreFactory factory = new GeoJSONDataStoreFactory();
        DataStore duplicate = factory.createNewDataStore(params2);
        SimpleFeatureTypeBuilder tBuilder = new SimpleFeatureTypeBuilder();

        CoordinateReferenceSystem crs = CRS.decode("EPSG:31287");

        tBuilder.setCRS(crs);
        tBuilder.setDefaultGeometry("geom");
        tBuilder.add("geom", Point.class);

        tBuilder.setName("out31287");
        SimpleFeatureType schema = tBuilder.buildFeatureType();
        SimpleFeatureBuilder builder = new SimpleFeatureBuilder(schema);
        // roughly Graz
        builder.add(gf.createPoint(new Coordinate(356142.53322693426, 561296.9376779466)));
        SimpleFeature feature = builder.buildFeature("1");
        SimpleFeatureCollection features = DataUtilities.collection(feature);

        duplicate.createSchema(schema);
        String typeName = duplicate.getTypeNames()[0];

        SimpleFeatureSource source = duplicate.getFeatureSource(typeName);
        if (source instanceof SimpleFeatureStore) {
            SimpleFeatureStore outStore = (SimpleFeatureStore) source;
            outStore.addFeatures(features);
        } else {
            fail("Can't write to GeoJSONDatastore " + file2);
        }

        SimpleFeatureStore featureStored =
                (SimpleFeatureStore) duplicate.getFeatureSource(typeName);

        try (SimpleFeatureIterator dups = featureStored.getFeatures().features()) {
            Point expected = gf.createPoint(new Coordinate(15.457678, 47.0850875));
            while (dups.hasNext()) {
                SimpleFeature d = dups.next();
                Geometry dgeom = (Geometry) d.getDefaultGeometry();

                assertCoordsMatch(expected, (Point) dgeom);
            }

        } finally {
            store.dispose();
        }
    }

    private void assertCoordsMatch(Point expected, Point dgeom) {
        assertEquals("Wrong x", expected.getCoordinate().x, dgeom.getCoordinate().x, 0.0001);
        assertEquals("Wrong y", expected.getCoordinate().y, dgeom.getCoordinate().y, 0.0001);
    }
}
