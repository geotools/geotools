/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.process.vector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.image.RenderedImage;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import javax.media.jai.iterator.RectIter;
import javax.media.jai.iterator.RectIterFactory;
import org.geotools.coverage.grid.GridCoordinates2D;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.NameImpl;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.process.Process;
import org.geotools.process.Processors;
import org.geotools.process.feature.AbstractFeatureCollectionProcessFactory;
import org.geotools.referencing.crs.DefaultEngineeringCRS;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.geometry.DirectPosition;
import org.opengis.metadata.spatial.PixelOrientation;
import org.opengis.referencing.operation.MathTransform2D;
import org.opengis.referencing.operation.TransformException;
import org.opengis.util.ProgressListener;

/**
 * Unit tests for rasterizing vector features.
 *
 * @author Michael Bedward
 */
public class VectorToRasterProcessTest {

    @Test
    public void testCreateProcess() throws Exception {
        Process p = Processors.createProcess(new NameImpl("vec", "VectorToRaster"));
        assertNotNull(p);
    }

    @Test
    public void rasterizePolygons() throws Exception {
        SimpleFeatureCollection features = createPolys();
        ReferencedEnvelope bounds = features.getBounds();

        Dimension gridDim = new Dimension((int) bounds.getWidth(), (int) bounds.getHeight());

        String covName = "Test";
        ProgressListener monitor = null;

        GridCoverage2D cov =
                VectorToRasterProcess.process(features, "value", gridDim, bounds, covName, monitor);

        // textPrint(cov);

        /*
         * Compare the coverage to the input features. We are expecting
         * to see the two small rectangles (values 1 and 3) 'on top' of
         * the larger rectangle (value 2)
         */
        Map<Integer, Envelope> rects = new HashMap<Integer, Envelope>();
        SimpleFeatureIterator iter = features.features();
        while (iter.hasNext()) {
            SimpleFeature sf = iter.next();
            rects.put(
                    (Integer) sf.getAttribute("value"),
                    ((Geometry) sf.getDefaultGeometry()).getEnvelopeInternal());
        }

        try {
            MathTransform2D mt = cov.getGridGeometry().getGridToCRS2D(PixelOrientation.CENTER);
            Point gridP = new Point();
            Point2D.Double geoP = new Point2D.Double();
            int[] covValue = new int[1];
            for (gridP.y = 0; gridP.y < gridDim.height; gridP.y++) {
                for (gridP.x = 0; gridP.x < gridDim.width; gridP.x++) {
                    mt.transform(gridP, geoP);
                }

                cov.evaluate(geoP, covValue);
                if (covValue[0] != 0) {
                    assertTrue(rects.get(covValue[0]).contains(geoP.x, geoP.y));
                }
            }

        } catch (TransformException tex) {
            throw new RuntimeException(tex);
        }
    }

    @Test
    public void rasterizePoints() throws Exception {
        ReferencedEnvelope bounds =
                new ReferencedEnvelope(-10, 10, -20, 20, DefaultEngineeringCRS.GENERIC_2D);
        Dimension gridDim = new Dimension(100, 100);

        SimpleFeatureCollection features = createPoints(bounds, gridDim);

        String covName = "Test";
        ProgressListener monitor = null;

        GridCoverage2D cov =
                VectorToRasterProcess.process(features, "value", gridDim, bounds, covName, monitor);

        SimpleFeatureIterator iter = features.features();
        int[] covValues = new int[1];
        try {
            while (iter.hasNext()) {
                SimpleFeature feature = iter.next();
                Coordinate coord = ((Geometry) feature.getDefaultGeometry()).getCoordinate();
                Point2D worldPos = new Point2D.Double(coord.x, coord.y);
                int value = (Integer) feature.getAttribute("value");

                cov.evaluate(worldPos, covValues);
                assertEquals(value, covValues[0]);
            }
        } finally {
            iter.close();
        }
    }

    /** Runs the Process.execute method using LineStrings with float values. */
    @Test
    public void executeProcessWithFloat() throws Exception {
        // System.out.println("   execute process using float values");
        Process p = Processors.createProcess(new NameImpl("vec", "VectorToRaster"));
        assertNotNull(p);

        SimpleFeatureCollection features = createFloatLines();
        ReferencedEnvelope bounds = features.getBounds();

        Dimension gridDim = new Dimension((int) bounds.getWidth(), (int) bounds.getHeight());

        ProgressListener monitor = null;

        Map<String, Object> map = new HashMap<String, Object>();
        map.put(AbstractFeatureCollectionProcessFactory.FEATURES.key, features);
        map.put("attribute", "value");
        map.put("rasterWidth", gridDim.width);
        map.put("rasterHeight", gridDim.height);
        map.put("bounds", bounds);

        Map<String, Object> result = p.execute(map, monitor);
        GridCoverage2D cov = (GridCoverage2D) result.get("result");

        // textPrintFloat(cov);

        /*
         * Verify each vertex is the correct value
         */
        SimpleFeatureIterator iter = features.features();
        float[] covValue = new float[1];
        GridCoordinates2D gridP = new GridCoordinates2D();
        while (iter.hasNext()) {
            SimpleFeature feature = iter.next();
            Coordinate[] coords = ((Geometry) feature.getDefaultGeometry()).getCoordinates();
            for (Coordinate coord : coords) {
                gridP.x = coord.x == gridDim.width ? (int) coord.x - 1 : (int) coord.x;
                gridP.y = coord.y == gridDim.height ? (int) coord.y - 1 : (int) coord.y;

                cov.evaluate(gridP, covValue);
                Float value = (Float) feature.getAttribute("value");
                assertEquals(value.floatValue(), covValue[0], .01);
            }
        }
    }

    /**
     * Runs the Process.execute method using LineStrings with String values that evaluate to
     * integers.
     */
    @Test
    public void executeProcessWithString() throws Exception {
        Process p = Processors.createProcess(new NameImpl("vec", "VectorToRaster"));
        assertNotNull(p);

        SimpleFeatureCollection features = createStringLines();
        ReferencedEnvelope bounds = features.getBounds();

        Dimension gridDim = new Dimension((int) bounds.getWidth(), (int) bounds.getHeight());

        ProgressListener monitor = null;

        Map<String, Object> map = new HashMap<String, Object>();
        map.put(AbstractFeatureCollectionProcessFactory.FEATURES.key, features);
        map.put("attribute", "value");
        map.put("rasterWidth", gridDim.width);
        map.put("rasterHeight", gridDim.height);
        map.put("bounds", bounds);

        Map<String, Object> result = p.execute(map, monitor);
        GridCoverage2D cov = (GridCoverage2D) result.get("result");

        // textPrint(cov);

        /*
         * Verify each vertex is the correct value
         */
        SimpleFeatureIterator iter = features.features();
        int[] covValue = new int[1];
        GridCoordinates2D gridP = new GridCoordinates2D();
        while (iter.hasNext()) {
            SimpleFeature feature = iter.next();
            Coordinate[] coords = ((Geometry) feature.getDefaultGeometry()).getCoordinates();
            for (Coordinate coord : coords) {
                gridP.x = coord.x == gridDim.width ? (int) coord.x - 1 : (int) coord.x;
                gridP.y = coord.y == gridDim.height ? (int) coord.y - 1 : (int) coord.y;

                cov.evaluate(gridP, covValue);
                int value = Integer.valueOf((String) feature.getAttribute("value"));
                assertEquals(covValue[0], value);
            }
        }
    }

    /**
     * Create a set of three features, each of which is a rectangular polygon. The features are
     * arranged roughly like this:
     *
     * <pre>
     *             |---------------|
     *             |               |
     *       |-----|----|     |----|-----|
     *       |     |    |     |    |     |
     *       |     |    |     |    |     |
     *       |     |    |     |    |     |
     *       |     |    |     |    |     |
     *       |-----|----|     |----|-----|
     *             |               |
     *             |---------------|
     * </pre>
     */
    private SimpleFeatureCollection createPolys() {
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName("testType");
        tb.add("shape", MultiPolygon.class);
        tb.add("name", String.class);
        tb.add("value", Integer.class);

        SimpleFeatureType type = tb.buildFeatureType();
        SimpleFeatureBuilder builder = new SimpleFeatureBuilder(type);
        WKTReader reader = new WKTReader();

        DefaultFeatureCollection fc = new DefaultFeatureCollection();
        SimpleFeature feature;

        feature =
                buildFeature(
                        builder,
                        reader,
                        "MULTIPOLYGON(((10 10, 10 20, 30 20, 30 10, 10 10)))",
                        "left",
                        1);
        fc.add(feature);

        feature =
                buildFeature(
                        builder,
                        reader,
                        "MULTIPOLYGON(((40 10, 40 20, 60 20, 60 10, 40 10)))",
                        "right",
                        3);
        fc.add(feature);

        feature =
                buildFeature(
                        builder,
                        reader,
                        "MULTIPOLYGON(((20 0, 20 30, 50 30, 50 0, 20 0)))",
                        "middle",
                        2);
        fc.add(feature);

        return fc;
    }

    /**
     * Creates randomly located points constrained so that only one point can lie in any grid cell.
     * The number of points will be approx number of grid cells / 4.
     */
    private SimpleFeatureCollection createPoints(ReferencedEnvelope bounds, Dimension gridDim)
            throws Exception {

        final double PROB_POINT = 0.25;
        final double xres = bounds.getWidth() / gridDim.width;
        final double yres = bounds.getHeight() / gridDim.height;

        final GridGeometry2D gridGeom =
                new GridGeometry2D(new GridEnvelope2D(0, 0, gridDim.width, gridDim.height), bounds);

        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName("testType");
        tb.setCRS(bounds.getCoordinateReferenceSystem());
        tb.add("shape", MultiPoint.class);
        tb.add("name", String.class);
        tb.add("value", Integer.class);

        SimpleFeatureType type = tb.buildFeatureType();
        SimpleFeatureBuilder builder = new SimpleFeatureBuilder(type);
        WKTReader reader = new WKTReader();

        DefaultFeatureCollection fc = new DefaultFeatureCollection();
        Random rand = new Random();

        GridCoordinates2D gridPos = new GridCoordinates2D();
        int i = 1;
        for (int y = 0; y < gridDim.height; y++) {
            for (int x = 0; x < gridDim.width; x++) {
                if (rand.nextDouble() < PROB_POINT) {
                    gridPos.setLocation(x, y);
                    DirectPosition worldPos = gridGeom.gridToWorld(gridPos);

                    String wkt =
                            String.format(
                                    Locale.US,
                                    "MULTIPOINT((%f %f))",
                                    worldPos.getOrdinate(0),
                                    worldPos.getOrdinate(1));
                    fc.add(buildFeature(builder, reader, wkt, "p" + i, i));
                    i++;
                }
            }
        }
        return fc;
    }

    /** Creates three lines using a String as the feature's value */
    private SimpleFeatureCollection createStringLines() {
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName("testContourType");
        tb.add("line", LineString.class);
        tb.add("name", String.class);
        tb.add("value", String.class);

        SimpleFeatureType type = tb.buildFeatureType();
        SimpleFeatureBuilder builder = new SimpleFeatureBuilder(type);
        WKTReader reader = new WKTReader();

        DefaultFeatureCollection fc = new DefaultFeatureCollection();
        SimpleFeature feature;

        feature = buildFeature(builder, reader, "LINESTRING(10 0, 10 20)", "horizontal", "1");
        fc.add(feature);

        feature = buildFeature(builder, reader, "LINESTRING(0 10, 20 10)", "vertical", "2");
        fc.add(feature);

        feature = buildFeature(builder, reader, "LINESTRING(0 20,0 0,20 0,20 20,0 20)", "box", "3");
        fc.add(feature);

        return fc;
    }

    /** Creates three lines using a float as the feature's value */
    private SimpleFeatureCollection createFloatLines() {
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName("testContourType");
        tb.add("line", LineString.class);
        tb.add("name", String.class);
        tb.add("value", Float.class);

        SimpleFeatureType type = tb.buildFeatureType();
        SimpleFeatureBuilder builder = new SimpleFeatureBuilder(type);
        WKTReader reader = new WKTReader();

        DefaultFeatureCollection fc = new DefaultFeatureCollection();
        SimpleFeature feature;

        feature =
                buildFeature(builder, reader, "LINESTRING(10 0, 10 20)", "horizontal", (float) 1.1);
        fc.add(feature);

        feature = buildFeature(builder, reader, "LINESTRING(0 10, 20 10)", "vertical", (float) 2.2);
        fc.add(feature);

        feature =
                buildFeature(
                        builder,
                        reader,
                        "LINESTRING(0 20,0 0,20 0,20 20,0 20)",
                        "box",
                        (float) 3.3);
        fc.add(feature);

        return fc;
    }

    /** Build the feature using a String value */
    private SimpleFeature buildFeature(
            SimpleFeatureBuilder builder, WKTReader reader, String wkt, String name, String value) {
        try {
            Geometry geom = reader.read(wkt);

            builder.add(geom);
            builder.add(name);
            builder.add(value);
            return builder.buildFeature(null);

        } catch (ParseException pex) {
            throw new RuntimeException("Error in the wkt: " + pex);
        }
    }

    /** Build the feature using a float value */
    private SimpleFeature buildFeature(
            SimpleFeatureBuilder builder, WKTReader reader, String wkt, String name, float value) {

        try {
            Geometry geom = reader.read(wkt);

            builder.add(geom);
            builder.add(name);
            builder.add(value);
            return builder.buildFeature(null);

        } catch (ParseException pex) {
            throw new RuntimeException("Error in the wkt: " + wkt);
        }
    }

    /** Build the feature using an int value */
    private SimpleFeature buildFeature(
            SimpleFeatureBuilder builder, WKTReader reader, String wkt, String name, int value) {

        try {
            Geometry geom = reader.read(wkt);

            builder.add(geom);
            builder.add(name);
            builder.add(value);
            return builder.buildFeature(null);

        } catch (ParseException pex) {
            throw new RuntimeException("Error in the wkt: " + wkt);
        }
    }

    /** Dump the float values in a (small) grid coverage to the console */
    private void textPrintFloat(GridCoverage2D cov) {
        RenderedImage img = cov.getRenderedImage();
        int nb = img.getSampleModel().getNumBands();
        RectIter iter = RectIterFactory.create(img, null);
        float[] pixel = new float[nb];
        do {
            do {
                iter.getPixel(pixel);
                for (int i = 0; i < nb; i++) {
                    // System.out.print(pixel[i]);
                }
            } while (!iter.nextPixelDone());
            iter.startPixels();
            // System.out.println();
        } while (!iter.nextLineDone());
    }

    /** Dump the int values in a (small) grid coverage to the console */
    private void textPrint(GridCoverage2D cov) {
        RenderedImage img = cov.getRenderedImage();
        int nb = img.getSampleModel().getNumBands();
        RectIter iter = RectIterFactory.create(img, null);
        int[] pixel = new int[nb];
        int w = img.getWidth();
        do {
            do {
                iter.getPixel(pixel);
                for (int i = 0; i < nb; i++) {
                    // System.out.print(pixel[i]);
                }
            } while (!iter.nextPixelDone());
            iter.startPixels();
            // System.out.println();
        } while (!iter.nextLineDone());
    }
}
