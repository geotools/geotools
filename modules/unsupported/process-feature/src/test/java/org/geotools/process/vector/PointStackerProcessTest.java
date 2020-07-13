/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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

import static org.junit.Assert.*;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.process.ProcessException;
import org.geotools.process.vector.PointStackerProcess.PreserveLocation;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.impl.PackedCoordinateSequenceFactory;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.TransformException;
import org.opengis.util.ProgressListener;

/**
 * Unit test for PointStackerProcess. Added tests for @see testWeightClusterPosition
 *
 * @author Martin Davis, OpenGeo
 * @author Cosmin Cioranu, Private
 */
public class PointStackerProcessTest {
    @Test
    public void testSimple() throws ProcessException, TransformException {
        ReferencedEnvelope bounds =
                new ReferencedEnvelope(0, 10, 0, 10, DefaultGeographicCRS.WGS84);

        // Simple dataset with some coincident points
        Coordinate[] data =
                new Coordinate[] {
                    new Coordinate(4, 4),
                    new Coordinate(4.1, 4.1),
                    new Coordinate(4.1, 4.1),
                    new Coordinate(8, 8)
                };

        SimpleFeatureCollection fc = createPoints(data, bounds);
        ProgressListener monitor = null;

        PointStackerProcess psp = new PointStackerProcess();
        SimpleFeatureCollection result =
                psp.execute(
                        fc, 100, // cellSize
                        null, // weightClusterPosition
                        null, // normalize
                        null, // preserve location
                        bounds, // outputBBOX
                        1000, // outputWidth
                        1000, // outputHeight
                        monitor);

        checkSchemaCorrect(result.getSchema(), false);
        assertEquals(2, result.size());
        checkResultPoint(result, new Coordinate(4, 4), 3, 2, null, null);
        checkResultPoint(result, new Coordinate(8, 8), 1, 1, null, null);
    }

    @Test
    public void testNormal() throws ProcessException, TransformException {
        ReferencedEnvelope bounds =
                new ReferencedEnvelope(0, 10, 0, 10, DefaultGeographicCRS.WGS84);

        // Simple dataset with some coincident points
        Coordinate[] data =
                new Coordinate[] {
                    new Coordinate(4, 4),
                    new Coordinate(4.1, 4.1),
                    new Coordinate(4.1, 4.1),
                    new Coordinate(8, 8)
                };

        SimpleFeatureCollection fc = createPoints(data, bounds);
        ProgressListener monitor = null;

        PointStackerProcess psp = new PointStackerProcess();
        SimpleFeatureCollection result =
                psp.execute(
                        fc, 100, // cellSize
                        false, // weighClusterPostion
                        true, // normalize
                        null, // preserve location
                        bounds, // outputBBOX
                        1000, // outputWidth
                        1000, // outputHeight
                        monitor);

        checkSchemaCorrect(result.getSchema(), true);
        assertEquals(2, result.size());
        checkResultPoint(result, new Coordinate(4, 4), 3, 2, 1.0d, 1.0d);
        checkResultPoint(result, new Coordinate(8, 8), 1, 1, 1.0d / 3, 1.0d / 2);
    }

    @Test
    public void testPreserveSingle() throws ProcessException, TransformException {
        ReferencedEnvelope bounds =
                new ReferencedEnvelope(0, 10, 0, 10, DefaultGeographicCRS.WGS84);

        // Simple dataset with some coincident points
        Coordinate[] data =
                new Coordinate[] {
                    new Coordinate(4, 4),
                    new Coordinate(6.5, 6.5),
                    new Coordinate(6.5, 6.5),
                    new Coordinate(8, 8),
                    new Coordinate(8.3, 8.3)
                };

        SimpleFeatureCollection fc = createPoints(data, bounds);
        ProgressListener monitor = null;

        PointStackerProcess psp = new PointStackerProcess();
        SimpleFeatureCollection result =
                psp.execute(
                        fc,
                        100, // cellSize
                        false, // weightClusterPosition
                        true, // normalize
                        PreserveLocation.Single, // preserve location
                        bounds, // outputBBOX
                        1000, // outputWidth
                        1000, // outputHeight
                        monitor);

        checkSchemaCorrect(result.getSchema(), true);
        assertEquals(3, result.size());
        checkStackedPoint(new Coordinate(4, 4), 1, 1, getResultPoint(result, new Coordinate(4, 4)));
        checkStackedPoint(null, 2, 1, getResultPoint(result, new Coordinate(6.5, 6.5)));
        checkStackedPoint(null, 2, 2, getResultPoint(result, new Coordinate(8, 8)));
    }

    @Test
    public void testPreserveSuperimposed() throws ProcessException, TransformException {
        ReferencedEnvelope bounds =
                new ReferencedEnvelope(0, 10, 0, 10, DefaultGeographicCRS.WGS84);

        // Simple dataset with some coincident points
        Coordinate[] data =
                new Coordinate[] {
                    new Coordinate(4, 4),
                    new Coordinate(6.5, 6.5),
                    new Coordinate(6.5, 6.5),
                    new Coordinate(8, 8),
                    new Coordinate(8.3, 8.3)
                };

        SimpleFeatureCollection fc = createPoints(data, bounds);
        ProgressListener monitor = null;

        PointStackerProcess psp = new PointStackerProcess();
        SimpleFeatureCollection result =
                psp.execute(
                        fc,
                        100, // cellSize
                        false, // weightClusterPosition
                        true, // normalize
                        PreserveLocation.Superimposed, // preserve location
                        bounds, // outputBBOX
                        1000, // outputWidth
                        1000, // outputHeight
                        monitor);

        checkSchemaCorrect(result.getSchema(), true);
        assertEquals(3, result.size());
        checkStackedPoint(new Coordinate(4, 4), 1, 1, getResultPoint(result, new Coordinate(4, 4)));
        checkStackedPoint(
                new Coordinate(6.5, 6.5), 2, 1, getResultPoint(result, new Coordinate(6.5, 6.5)));
        checkStackedPoint(null, 2, 2, getResultPoint(result, new Coordinate(8, 8)));
    }

    private void checkStackedPoint(
            Coordinate expectedCoordinate, int count, int countUnique, SimpleFeature f) {
        if (expectedCoordinate != null) {
            Point p = (Point) f.getDefaultGeometry();
            assertEquals(expectedCoordinate, p.getCoordinate());
        }

        assertEquals(count, f.getAttribute(PointStackerProcess.ATTR_COUNT));
        assertEquals(countUnique, f.getAttribute(PointStackerProcess.ATTR_COUNT_UNIQUE));
    }

    /**
     * Tests point stacking when output CRS is different to data CRS. The result data should be
     * reprojected.
     */
    @Test
    public void testReprojected()
            throws NoSuchAuthorityCodeException, FactoryException, ProcessException,
                    TransformException {

        ReferencedEnvelope inBounds =
                new ReferencedEnvelope(0, 10, 0, 10, DefaultGeographicCRS.WGS84);

        // Dataset with some points located in appropriate area
        // points are close enough to create a single cluster
        Coordinate[] data =
                new Coordinate[] {
                    new Coordinate(-121.813201, 48.777343), new Coordinate(-121.813, 48.777)
                };

        SimpleFeatureCollection fc = createPoints(data, inBounds);
        ProgressListener monitor = null;

        // Google Mercator BBOX for northern Washington State (roughly)
        CoordinateReferenceSystem webMerc = CRS.decode("EPSG:3785");
        ReferencedEnvelope outBounds =
                new ReferencedEnvelope(
                        -1.4045034049133E7,
                        -1.2937920131607E7,
                        5916835.1504419,
                        6386464.2521607,
                        webMerc);

        PointStackerProcess psp = new PointStackerProcess();
        SimpleFeatureCollection result =
                psp.execute(
                        fc, 100, // cellSize
                        null, // weightClusterPosition
                        null, // normalize
                        null, // preserve location
                        outBounds, // outputBBOX
                        1810, // outputWidth
                        768, // outputHeight
                        monitor);

        checkSchemaCorrect(result.getSchema(), false);
        assertEquals(1, result.size());
        assertEquals(
                inBounds.getCoordinateReferenceSystem(),
                result.getBounds().getCoordinateReferenceSystem());
        checkResultPoint(result, new Coordinate(-121.813201, 48.777343), 2, 2, null, null);
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testWeightClusterPosition()
            throws NoSuchAuthorityCodeException, FactoryException, ProcessException,
                    TransformException {

        ReferencedEnvelope inBounds =
                new ReferencedEnvelope(0, 10, 0, 10, DefaultGeographicCRS.WGS84);

        // Dataset with some points located in appropriate area
        // points are close enough to create a single cluster
        Coordinate[] data =
                new Coordinate[] {
                    new Coordinate(-121.813201, 48.777343), new Coordinate(-121.813, 48.777)
                };

        SimpleFeatureCollection fc = createPoints(data, inBounds);
        ProgressListener monitor = null;

        // Google Mercator BBOX for northern Washington State (roughly)
        CoordinateReferenceSystem webMerc = CRS.decode("EPSG:3785");
        ReferencedEnvelope outBounds =
                new ReferencedEnvelope(
                        -1.4045034049133E7,
                        -1.2937920131607E7,
                        5916835.1504419,
                        6386464.2521607,
                        webMerc);

        PointStackerProcess psp = new PointStackerProcess();
        SimpleFeatureCollection result =
                psp.execute(
                        fc, 100, // cellSize
                        true, // weightClusterPosition
                        null, // normalize
                        null, // preserve location
                        outBounds, // outputBBOX
                        1810, // outputWidth
                        768, // outputHeight
                        monitor);

        // check if we did not alter the results
        checkSchemaCorrect(result.getSchema(), false);
        assertEquals(1, result.size());
        assertEquals(
                inBounds.getCoordinateReferenceSystem(),
                result.getBounds().getCoordinateReferenceSystem());
        checkResultPoint(result, new Coordinate(-121.813201, 48.777343), 2, 2, null, null);

        return;
    }

    /** Get the stacked point closest to the provided coordinate */
    private SimpleFeature getResultPoint(SimpleFeatureCollection result, Coordinate testPt) {
        /** Find closest point to loc pt, then check that the attributes match */
        double minDist = Double.MAX_VALUE;

        // find nearest result to testPt
        SimpleFeature closest = null;
        for (SimpleFeatureIterator it = result.features(); it.hasNext(); ) {
            SimpleFeature f = it.next();
            Coordinate outPt = ((Point) f.getDefaultGeometry()).getCoordinate();
            double dist = outPt.distance(testPt);
            if (dist < minDist) {
                closest = f;
                minDist = dist;
            }
        }

        return closest;
    }

    /**
     * Check that a result set contains a stacked point in the right cell with expected attribute
     * values. Because it's not known in advance what the actual location of a stacked point will
     * be, a nearest-point strategy is used.
     */
    private void checkResultPoint(
            SimpleFeatureCollection result,
            Coordinate testPt,
            int expectedCount,
            int expectedCountUnique,
            Double expectedProportion,
            Double expectedProportionUnique) {

        SimpleFeature f = getResultPoint(result, testPt);
        assertNotNull(f);

        /** Find closest point to loc pt, then check that the attributes match */
        int count = (Integer) f.getAttribute(PointStackerProcess.ATTR_COUNT);
        int countunique = (Integer) f.getAttribute(PointStackerProcess.ATTR_COUNT_UNIQUE);
        double normCount = Double.NaN;
        double normCountUnique = Double.NaN;
        if (expectedProportion != null) {
            normCount = (Double) f.getAttribute(PointStackerProcess.ATTR_NORM_COUNT);
            normCountUnique = (Double) f.getAttribute(PointStackerProcess.ATTR_NORM_COUNT_UNIQUE);
        }

        assertEquals(expectedCount, count);
        assertEquals(expectedCountUnique, countunique);
        if (expectedProportion != null) assertEquals(expectedProportion, normCount, 0.0001);
        if (expectedProportionUnique != null)
            assertEquals(expectedProportionUnique, normCountUnique, 0.0001);
    }

    private void checkSchemaCorrect(SimpleFeatureType ft, boolean includeProportionColumns) {
        if (includeProportionColumns) {
            // assertEquals(5, ft.getAttributeCount()); old version before adding envelope
            assertEquals(7, ft.getAttributeCount());
        } else {
            // assertEquals(3, ft.getAttributeCount()); old version before adding envelope.
            assertEquals(5, ft.getAttributeCount());
        }
        assertEquals(Point.class, ft.getGeometryDescriptor().getType().getBinding());
        assertEquals(
                Integer.class,
                ft.getDescriptor(PointStackerProcess.ATTR_COUNT).getType().getBinding());
        assertEquals(
                Integer.class,
                ft.getDescriptor(PointStackerProcess.ATTR_COUNT_UNIQUE).getType().getBinding());
        if (includeProportionColumns) {
            assertEquals(
                    Double.class,
                    ft.getDescriptor(PointStackerProcess.ATTR_NORM_COUNT).getType().getBinding());
            assertEquals(
                    Double.class,
                    ft.getDescriptor(PointStackerProcess.ATTR_NORM_COUNT_UNIQUE)
                            .getType()
                            .getBinding());
        }
    }

    private SimpleFeatureCollection createPoints(Coordinate[] pts, ReferencedEnvelope bounds) {

        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName("data");
        tb.setCRS(bounds.getCoordinateReferenceSystem());
        tb.add("shape", MultiPoint.class);
        tb.add("value", Double.class);

        SimpleFeatureType type = tb.buildFeatureType();
        SimpleFeatureBuilder fb = new SimpleFeatureBuilder(type);
        DefaultFeatureCollection fc = new DefaultFeatureCollection();

        GeometryFactory factory = new GeometryFactory(new PackedCoordinateSequenceFactory());

        for (Coordinate p : pts) {
            Geometry point = factory.createPoint(p);
            fb.add(point);
            fb.add(p.getZ());
            fc.add(fb.buildFeature(null));
        }

        return fc;
    }
}
