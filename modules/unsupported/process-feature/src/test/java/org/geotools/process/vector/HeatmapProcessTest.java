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

import static org.junit.Assert.assertTrue;

import java.awt.geom.Point2D;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.util.ProgressListener;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.impl.PackedCoordinateSequenceFactory;

/** @author Martin Davis - OpenGeo */
public class HeatmapProcessTest {

    /**
     * A test of a simple surface, validating that the process can be invoked and return a reasonable result in a simple
     * situation.
     *
     * <p>Test includes data which lies outside the heatmap buffer area, to check that it is filtered correctly (i.e.
     * does not cause out-of-range errors, and does not affect generated surface).
     */
    @Test
    public void testSimpleSurface() {

        ReferencedEnvelope bounds = new ReferencedEnvelope(0, 10, 0, 10, DefaultGeographicCRS.WGS84);
        Coordinate[] data = {
            new Coordinate(4, 4),
            new Coordinate(4, 6),
            // include a coordinate outside the heatmap buffer bounds, to ensure it is
            // filtered correctly
            new Coordinate(100, 100)
        };
        SimpleFeatureCollection fc = createPoints(data, bounds);

        ProgressListener monitor = null;

        HeatmapProcess process = new HeatmapProcess();
        GridCoverage2D cov = process.execute(
                fc, // data
                20, // radius
                null, // weightAttr
                1, // pixelsPerCell
                bounds, // outputEnv
                100, // outputWidth
                100, // outputHeight
                monitor // monitor)
                );

        // following tests are checking for an appropriate shape for the surface

        float center1 = coverageValue(cov, 4, 4);
        float center2 = coverageValue(cov, 4, 6);
        float midway = coverageValue(cov, 4, 5);
        float far = coverageValue(cov, 9, 9);

        // peaks are roughly equal
        float peakDiff = Math.abs(center1 - center2);
        assert peakDiff < center1 / 10;

        // dip between peaks
        assertTrue(midway > center1 / 2);

        // surface is flat far away
        assertTrue(far < center1 / 1000);
    }

    private float coverageValue(GridCoverage2D cov, double x, double y) {
        float[] covVal = new float[1];
        Point2D worldPos = new Point2D.Double(x, y);
        cov.evaluate(worldPos, covVal);
        return covVal[0];
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
