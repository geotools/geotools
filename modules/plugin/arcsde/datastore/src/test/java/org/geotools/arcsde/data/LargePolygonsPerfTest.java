package org.geotools.arcsde.data;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.DefaultMapContext;
import org.geotools.map.MapContext;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.styling.BasicPolygonStyle;
import org.geotools.styling.Style;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.esri.sde.sdk.client.SeConnection;
import com.esri.sde.sdk.client.SeException;
import com.esri.sde.sdk.client.SeQuery;
import com.esri.sde.sdk.client.SeQueryInfo;
import com.esri.sde.sdk.client.SeRow;
import com.esri.sde.sdk.client.SeShape;
import com.esri.sde.sdk.client.SeSqlConstruct;
import com.esri.sde.sdk.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.util.Stopwatch;

/**
 * This is a legacy benchmarking suite I'm using to assess the performance of some very large
 * datasets. Will be removed.
 */
@Ignore
public class LargePolygonsPerfTest {

    private static TestData testData;

    private final String typeName = "SDE.SDE.ASSESSPAR_POLY_PUBLIC";

    private final String postgisTypeName = "assesspar_poly_public";

    private final String shapefileTypeName = "ASSESSPAR_POLY_PUBLIC";

    private int featureCount;

    private long totalPoints;

    private int largerPoints;

    private final int numRuns = 3;

    public static void main(String[] argv) {
        LargePolygonsPerfTest test = new LargePolygonsPerfTest();
        try {
            LargePolygonsPerfTest.oneTimeSetUp();
            test.setUp();
            // test.testRender();
            LargePolygonsPerfTest.oneTimeTearDown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Before
    public void setUp() {
        totalPoints = largerPoints = featureCount = 0;
    }

    @BeforeClass
    public static void oneTimeSetUp() throws IOException {
        testData = new TestData();
        testData.setUp();
    }

    @AfterClass
    public static void oneTimeTearDown() {
        testData.tearDown(false, true);
    }

    private void log(String s) {
        System.err.println(s);
    }

    @Test
    public void testRenderPostGIS() throws Exception {
        DataStore dataStore = getPostGISDataStore();
        SimpleFeatureSource featureSource;
        featureSource = dataStore.getFeatureSource(postgisTypeName);
        try {
            testRender(featureSource);
        } finally {
            dataStore.dispose();
        }
    }

    @Test
    public void testRenderShapefile() throws Exception {
        DataStore dataStore = getShapefileDataStore();
        SimpleFeatureSource featureSource;
        featureSource = dataStore.getFeatureSource(shapefileTypeName);
        try {
            testRender(featureSource);
        } finally {
            dataStore.dispose();
        }
    }

    @Test
    public void testRenderArcSDE() throws Exception {
        SimpleFeatureSource featureSource;
        ArcSDEDataStore dataStore = testData.getDataStore();
        featureSource = dataStore.getFeatureSource(typeName);
        try {
            testRender(featureSource);
        } finally {
            dataStore.dispose();
        }
    }

    private void testRender(SimpleFeatureSource featureSource) throws Exception {
        SimpleFeatureType schema = featureSource.getSchema();
        CoordinateReferenceSystem crs = schema.getCoordinateReferenceSystem();
        ReferencedEnvelope bounds = featureSource.getBounds();

        MapContext context = new DefaultMapContext(crs);
        Style style = new BasicPolygonStyle();
        context.addLayer(featureSource, style);
        context.setAreaOfInterest(bounds);
        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setContext(context);

        BufferedImage image = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        Rectangle paintArea = new Rectangle(800, 600);
        ReferencedEnvelope mapArea = bounds;
        Stopwatch sw = new Stopwatch();
        long totalTime = 0;
        for (int i = 0; i < numRuns; i++) {
            sw.start();
            renderer.paint(graphics, paintArea, mapArea);
            sw.stop();
            totalTime += sw.getTime();
            log("Layer rendered in " + sw.getTimeString());
            sw.reset();
            File output = new File("testRender.png");
            log("writing rendered image to " + output.getAbsolutePath());
            ImageIO.write(image, "png", output);
        }
        log("-- Average rendering time after " + numRuns + " runs: "
                + ((totalTime / numRuns) / 1000) + "s");
    }

    @Test
    public void testFeatureSourceArcSDE() throws IOException {
        DataStore dataStore = testData.getDataStore();
        SimpleFeatureSource featureSource;
        featureSource = dataStore.getFeatureSource(typeName);
        try {
            testFeatureSource(featureSource);
        } finally {
            dataStore.dispose();
        }
    }

    @Test
    public void testFeatureSourcePostGIS() throws IOException {
        DataStore dataStore = getPostGISDataStore();
        SimpleFeatureSource featureSource;
        featureSource = dataStore.getFeatureSource(postgisTypeName);

        try {
            testFeatureSource(featureSource);
        } finally {
            dataStore.dispose();
        }
    }

    @Test
    public void testFeatureSourceShapefile() throws IOException {
        DataStore dataStore = getShapefileDataStore();
        SimpleFeatureSource featureSource;
        featureSource = dataStore.getFeatureSource(shapefileTypeName);

        try {
            testFeatureSource(featureSource);
        } finally {
            dataStore.dispose();
        }
    }

    private DataStore getPostGISDataStore() throws IOException {
        Map<String, Serializable> params = new HashMap<String, Serializable>();
        params.put("dbtype", "postgis");
        params.put("host", "192.168.1.10");
        params.put("port", "5432");
        params.put("database", "postgis");
        params.put("schema", "public");
        params.put("user", "postgres");
        params.put("passwd", "admin");
        params.put("wkbenabled", "true");
        params.put("loose bbox", "true");
        params.put("estimated extent", "true");

        DataStore dataStore = DataStoreFinder.getDataStore(params);
        return dataStore;
    }

    private DataStore getShapefileDataStore() throws IOException {
        Map<String, Serializable> params = new HashMap<String, Serializable>();
        params.put("url", "file:/home/groldan/tmp/shp/ASSESSPAR_POLY_PUBLIC.shp");
        DataStore dataStore = DataStoreFinder.getDataStore(params);
        return dataStore;
    }

    private void testFeatureSource(SimpleFeatureSource fs) throws IOException {
        log("-----------------------------------------------------");
        log("Testing feature source");
        final SimpleFeatureType schema = fs.getSchema();
        final String typeName = schema.getTypeName();
        final String spatialColName = schema.getGeometryDescriptor().getLocalName();

        Query query = new Query(typeName, Filter.INCLUDE, new String[] { spatialColName });
        long runTime = 0;
        for (int run = 0; run < numRuns; run++) {
            runTime += iterate(fs, query);
        }
        double avg = runTime / numRuns;
        log("--- Avg iteration time with FeatureSource for " + numRuns + " runs: " + (avg / 1000D)
                + "s");
        log("-----------------------------------------------------");
    }

    @Test
    public void testFeatureSourceNoGeom() throws IOException {
        log("-----------------------------------------------------");
        log("Testing feature source without geometry");
        DataStore ds = testData.getDataStore();
        SimpleFeatureSource fs = ds.getFeatureSource(typeName);
        try {
            Query query = new Query(typeName, Filter.INCLUDE, new String[] { "TOWN_ID" });
            long runTime = 0;
            for (int run = 0; run < numRuns; run++) {
                runTime += iterate(fs, query);
            }
            double avg = runTime / numRuns;
            log("--- Avg iteration time with FeatureSource for " + numRuns + " runs: "
                    + (avg / 1000D) + "s");
        } finally {
            ds.dispose();
        }
        log("-----------------------------------------------------");
    }

    @Test
    public void testSeQueryWithSeShapeFecthing() throws IOException, SeException {
        log("-----------------------------------------------------");
        log("Testing SeQuery with SeShape fetching");

        Map<String, Serializable> props = testData.getConProps();

        SeConnection conn = new SeConnection(String.valueOf(props.get("server")),
                Integer.parseInt(String.valueOf(props.get("port"))), String.valueOf(props
                        .get("instance")), String.valueOf(props.get("user")), String.valueOf(props
                        .get("password")));

        try {
            SeQuery query;
            SeQueryInfo queryInfo;

            queryInfo = new SeQueryInfo();
            queryInfo.setColumns(new String[] { "SHAPE" });
            queryInfo.setConstruct(new SeSqlConstruct(typeName));

            long runTime = 0;
            for (int run = 0; run < numRuns; run++) {
                query = new SeQuery(conn);
                runTime += iterateWithSeShapeFetching(query, queryInfo);
            }
            double avg = runTime / numRuns;
            log("--- Avg iteration time with SeShape fetching for " + numRuns + " runs: "
                    + (avg / 1000D) + "s");
        } finally {
            conn.close();
        }
        log("-----------------------------------------------------");
    }

    @Test
    public void testSeQueryWithGeometryFactoryFecthing() throws IOException, SeException {
        log("-----------------------------------------------------");
        log("Testing SeQuery with GeometryFactory fetching");

        Map<String, Serializable> props = testData.getConProps();
        SeConnection conn = new SeConnection(String.valueOf(props.get("server")),
                Integer.parseInt(String.valueOf(props.get("port"))), String.valueOf(props
                        .get("instance")), String.valueOf(props.get("user")), String.valueOf(props
                        .get("password")));

        try {
            SeQuery query;
            SeQueryInfo queryInfo;

            queryInfo = new SeQueryInfo();
            queryInfo.setColumns(new String[] { "SHAPE" });
            queryInfo.setConstruct(new SeSqlConstruct(typeName));

            long runTime = 0;
            for (int run = 0; run < numRuns; run++) {
                query = new SeQuery(conn);
                runTime += iterateWithGeometryFactory(query, queryInfo);
            }
            double avg = runTime / numRuns;
            log("--- Avg iteration time with GeometryFactory for " + numRuns + " runs: "
                    + (avg / 1000D) + "s");
        } finally {
            conn.close();
        }
        log("-----------------------------------------------------");
    }

    @SuppressWarnings("unused")
    private long iterateWithSeShapeFetching(SeQuery query, SeQueryInfo seQueryInfo)
            throws SeException {
        Stopwatch sw = new Stopwatch();
        log("- Executing query " + Arrays.asList(seQueryInfo.getColumns()));
        query.prepareQueryInfo(seQueryInfo);
        query.execute();
        try {
            log("- Iterating: " + Arrays.asList(seQueryInfo.getColumns()));
            SeRow row = query.fetch();
            final int ncols = row.getNumColumns();
            final int shapeIdx = ncols - 1;
            Object value;
            sw.start();
            featureCount = 0;
            totalPoints = 0;
            while (row != null) {
                featureCount++;
                for (int i = 0; i < ncols; i++) {
                    value = row.getObject(i);
                    if (i == shapeIdx) {
                        SeShape shape = (SeShape) value;
                        totalPoints += shape.getNumOfPoints();
                        double[][][] allCoords = shape.getAllCoords();
                        largerPoints = Math.max(largerPoints, shape.getNumOfPoints());
                    }
                }
                row = query.fetch();
            }
            sw.stop();
        } finally {
            query.close();
        }
        log("\t- " + featureCount + " features iterated in " + sw.getTimeString());
        log("\t\t- total poinst: " + totalPoints + ", larger geometry: " + largerPoints + " points"
                + " avg geom points: " + (totalPoints / featureCount));
        return sw.getTime();
    }

    @SuppressWarnings("unused")
    private long iterateWithGeometryFactory(SeQuery query, SeQueryInfo seQueryInfo)
            throws SeException {
        Stopwatch sw = new Stopwatch();
        query.prepareQueryInfo(seQueryInfo);
        query.execute();
        try {
            log("- Iterating: " + Arrays.asList(seQueryInfo.getColumns()));
            SeRow row = query.fetch();
            final int ncols = row.getNumColumns();
            final int shapeIdx = ncols - 1;
            Object value;
            Geometry geom;
            int numPoints;
            featureCount = 0;
            totalPoints = 0;
            largerPoints = 0;
            GeometryFactory seGeomFac = new SeToJTSGeometryFactory();
            sw.start();
            while (row != null) {
                featureCount++;
                for (int i = 0; i < ncols; i++) {
                    if (i == shapeIdx) {
                        geom = (Geometry) row.getGeometry(seGeomFac, i);
                        numPoints = geom.getNumPoints();
                        totalPoints += numPoints;
                        largerPoints = Math.max(largerPoints, numPoints);
                    } else {
                        value = row.getObject(i);
                    }
                }
                row = query.fetch();
            }
            sw.stop();
        } finally {
            query.close();
        }
        log("\t- " + featureCount + " features iterated in " + sw.getTimeString());
        log("\t\t- total poinst: " + totalPoints + ", larger geometry: " + largerPoints
                + " points, avg points: " + (totalPoints / featureCount));
        return sw.getTime();
    }

    @SuppressWarnings("unused")
    private long iterate(SimpleFeatureSource fs, Query query) throws IOException {
        Stopwatch sw = new Stopwatch();
        SimpleFeatureCollection features;
        features = fs.getFeatures(query);

        SimpleFeatureIterator iterator;
        iterator = features.features();

        log("- Iterating: " + Arrays.asList(query.getPropertyNames()));
        SimpleFeature feature;
        final int shapeIdx = fs.getSchema().getAttributeCount() - 1;
        try {
            Geometry defaultGeometry;
            featureCount = 0;
            totalPoints = 0;
            int npoints;
            sw.start();
            while (iterator.hasNext()) {
                feature = iterator.next();
                featureCount++;

                defaultGeometry = (Geometry) feature.getDefaultGeometry();
                npoints = defaultGeometry == null ? 0 : defaultGeometry.getNumPoints();
                totalPoints += npoints;
                largerPoints = Math.max(largerPoints, npoints);
            }
            sw.stop();
        } finally {
            iterator.close();
        }
        log("\t- " + featureCount + " features iterated in " + sw.getTimeString());
        log("\t\t- total poinst: " + totalPoints + ", larger geometry: " + largerPoints
                + " points, avg points:" + (totalPoints / featureCount));
        return sw.getTime();
    }
}
