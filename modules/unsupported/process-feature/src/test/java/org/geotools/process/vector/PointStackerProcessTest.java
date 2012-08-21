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

import static junit.framework.Assert.*;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.FeatureCollections;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.process.ProcessException;
import org.geotools.process.vector.PointStackerProcess;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.TransformException;
import org.opengis.util.ProgressListener;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.impl.PackedCoordinateSequenceFactory;

/**
 * Unit test for PointStackerProcess.
 * 
 * @author Martin Davis, OpenGeo
 * 
 */
public class PointStackerProcessTest {
    @Test
    public void testSimple() throws ProcessException, TransformException {
        ReferencedEnvelope bounds = new ReferencedEnvelope(0, 10, 0, 10, DefaultGeographicCRS.WGS84);
        
        // Simple dataset with some coincident points
        Coordinate[] data = new Coordinate[] { new Coordinate(4, 4), new Coordinate(4.1, 4.1),
                new Coordinate(4.1, 4.1), new Coordinate(8, 8) };
        
        
        SimpleFeatureCollection fc = createPoints(data, bounds);
        ProgressListener monitor = null;

        PointStackerProcess psp = new PointStackerProcess();
        SimpleFeatureCollection result = psp.execute(fc, 100, // cellSize
                bounds, // outputBBOX
                1000, // outputWidth
                1000, // outputHeight
                monitor);
        
        checkSchemaCorrect(result.getSchema());
        assertEquals(2, result.size());
        checkResultPoint(result, new Coordinate(4, 4), 3, 2);
        checkResultPoint(result, new Coordinate(8, 8), 1, 1);
    }

    /**
     * Tests point stacking when output CRS is different to data CRS.
     * The result data should be reprojected.
     * 
     * @throws NoSuchAuthorityCodeException
     * @throws FactoryException
     * @throws TransformException 
     * @throws ProcessException 
     */
    @Test
    public void testReprojected() throws NoSuchAuthorityCodeException, FactoryException, ProcessException, TransformException {

        ReferencedEnvelope inBounds = new ReferencedEnvelope(0, 10, 0, 10, DefaultGeographicCRS.WGS84);
        
        // Dataset with some points located in appropriate area
        // points are close enough to create a single cluster
        Coordinate[] data = new Coordinate[] { new Coordinate(-121.813201, 48.777343), new Coordinate(-121.813, 48.777) };
        
        
        SimpleFeatureCollection fc = createPoints(data, inBounds);
        ProgressListener monitor = null;

        // Google Mercator BBOX for northern Washington State (roughly)
        CoordinateReferenceSystem webMerc = CRS.decode("EPSG:3785");
        ReferencedEnvelope outBounds = new ReferencedEnvelope(-1.4045034049133E7, -1.2937920131607E7, 5916835.1504419, 6386464.2521607, webMerc);

        PointStackerProcess psp = new PointStackerProcess();
        SimpleFeatureCollection result = psp.execute(fc, 100, // cellSize
                outBounds, // outputBBOX
                1810, // outputWidth
                768, // outputHeight
                monitor);
        
        checkSchemaCorrect(result.getSchema());
        assertEquals(1, result.size());
        assertEquals(inBounds.getCoordinateReferenceSystem(), result.getBounds().getCoordinateReferenceSystem());
        checkResultPoint(result, new Coordinate(-121.813201, 48.777343), 2, 2);
    }

    /**
     * Check that a result set contains a stacked point in the right cell with expected attribute
     * values. Because it's not known in advance what the actual location of a stacked point will
     * be, a nearest-point strategy is used.
     * 
     * @param result
     * @param coordinate
     * @param i
     * @param j
     */
    private void checkResultPoint(SimpleFeatureCollection result, Coordinate testPt,
            int expectedCount, int expectedCountUnique) {
        /**
         * Find closest point to loc pt, then check that the attributes match
         */
        double minDist = Double.MAX_VALUE;
        int count = -1;
        int countunique = -1;

        // find nearest result to testPt
        for (SimpleFeatureIterator it = result.features(); it.hasNext();) {
            SimpleFeature f = it.next();
            Coordinate outPt = ((Point) f.getDefaultGeometry()).getCoordinate();
            double dist = outPt.distance(testPt);
            if (dist < minDist) {
                minDist = dist;
                count = (Integer) f.getAttribute(PointStackerProcess.ATTR_COUNT);
                countunique = (Integer) f.getAttribute(PointStackerProcess.ATTR_COUNT_UNIQUE);
            }
        }
        assertEquals(expectedCount, count);
        assertEquals(expectedCountUnique, countunique);
    }

    private void checkResultBounds(SimpleFeatureCollection result, ReferencedEnvelope bounds) {
        boolean isInBounds = true;
        for (SimpleFeatureIterator it = result.features(); it.hasNext();) {
            SimpleFeature f = it.next();
            Coordinate outPt = ((Point) f.getDefaultGeometry()).getCoordinate();
            if (! bounds.contains(outPt)) {
                isInBounds = false;
                System.out.println("Found point out of bounds: " + f.getDefaultGeometry());
            }
        }
        assertTrue(isInBounds);
    }

    private void checkSchemaCorrect(SimpleFeatureType ft) {
        assertEquals(3, ft.getAttributeCount());
        assertEquals(Point.class, ft.getGeometryDescriptor().getType().getBinding());
        assertEquals(Integer.class, ft.getDescriptor(PointStackerProcess.ATTR_COUNT).getType()
                .getBinding());
        assertEquals(Integer.class, ft.getDescriptor(PointStackerProcess.ATTR_COUNT_UNIQUE)
                .getType().getBinding());

    }

    private SimpleFeatureCollection createPoints(Coordinate[] pts, ReferencedEnvelope bounds) {

        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName("data");
        tb.setCRS(bounds.getCoordinateReferenceSystem());
        tb.add("shape", MultiPoint.class);
        tb.add("value", Double.class);

        SimpleFeatureType type = tb.buildFeatureType();
        SimpleFeatureBuilder fb = new SimpleFeatureBuilder(type);
        SimpleFeatureCollection fc = FeatureCollections.newCollection();

        GeometryFactory factory = new GeometryFactory(new PackedCoordinateSequenceFactory());

        for (Coordinate p : pts) {
            Geometry point = factory.createPoint(p);
            fb.add(point);
            fb.add(p.z);
            fc.add(fb.buildFeature(null));
        }

        return fc;
    }

}
