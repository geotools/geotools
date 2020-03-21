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
import static org.junit.Assert.assertTrue;

import java.awt.geom.Point2D;
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
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.util.ProgressListener;

/** @author Martin Davis - OpenGeo */
public class BarnesSurfaceProcessTest {

    /**
     * A test of a simple surface, validating that the process can be invoked and return a
     * reasonable result in a simple situation.
     */
    @Test
    public void testSimpleSurface() {

        ReferencedEnvelope bounds =
                new ReferencedEnvelope(0, 30, 0, 30, DefaultGeographicCRS.WGS84);
        Coordinate[] data =
                new Coordinate[] {
                    new Coordinate(10, 10, 100),
                    new Coordinate(10, 20, 20),
                    new Coordinate(20, 10, 0),
                    new Coordinate(20, 20, 80)
                };
        SimpleFeatureCollection fc = createPoints(data, bounds);

        ProgressListener monitor = null;

        BarnesSurfaceProcess process = new BarnesSurfaceProcess();
        GridCoverage2D cov =
                process.execute(
                        fc, // data
                        "value", // valueAttr
                        1000, // dataLimit
                        10.0, // scale
                        (Double) null, // convergence
                        (Integer) 2, // passes
                        (Integer) null, // minObservations
                        (Double) null, // maxObservationDistance
                        -999.0, // noDataValue
                        1, // pixelsPerCell
                        0.0, // queryBuffer
                        bounds, // outputEnv
                        100, // outputWidth
                        100, // outputHeight
                        monitor // monitor)
                        );

        //      System.out.println(coverageValue(cov, 20, 20));

        double ERROR_TOL = 10;

        for (Coordinate p : data) {
            float covval = coverageValue(cov, p.x, p.y);
            double error = Math.abs(p.getZ() - covval);
            assertTrue(error < ERROR_TOL);
        }

        assertEquals(1, cov.getSampleDimensions().length);
        assertEquals("values", cov.getSampleDimensions()[0].getDescription().toString());
    }

    private float coverageValue(GridCoverage2D cov, double x, double y) {
        float[] covVal = new float[1];
        Point2D worldPos = new Point2D.Double(x, y);
        cov.evaluate(worldPos, covVal);
        return covVal[0];
    }

    private SimpleFeatureCollection createPoints(Coordinate[] pts, ReferencedEnvelope bounds) {

        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName("obsType");
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
