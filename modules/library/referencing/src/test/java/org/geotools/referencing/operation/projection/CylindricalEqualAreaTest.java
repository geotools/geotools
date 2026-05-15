/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.referencing.operation.projection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.awt.geom.Point2D;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.junit.Test;

/** Tests for {@link CylindricalEqualArea}. */
public class CylindricalEqualAreaTest {

    @Test
    public void testEpsg6933FromWKT() throws Exception {
        // @formatter:off
        CoordinateReferenceSystem cea6933 =
                CRS.parseWKT("PROJCS[\"WGS 84 / NSIDC EASE-Grid 2.0 Global\", " + "  GEOGCS[\"WGS 84\", "
                        + "    DATUM[\"WGS_1984\", "
                        + "      SPHEROID[\"WGS 84\",6378137,298.257223563, "
                        + "        AUTHORITY[\"EPSG\",\"7030\"]], "
                        + "      AUTHORITY[\"EPSG\",\"6326\"]], "
                        + "    PRIMEM[\"Greenwich\",0, "
                        + "      AUTHORITY[\"EPSG\",\"8901\"]], "
                        + "    UNIT[\"degree\",0.0174532925199433, "
                        + "      AUTHORITY[\"EPSG\",\"9122\"]], "
                        + "    AUTHORITY[\"EPSG\",\"4326\"]], "
                        + "  PROJECTION[\"Cylindrical_Equal_Area\"], "
                        + "  PARAMETER[\"standard_parallel_1\",30], "
                        + "  PARAMETER[\"central_meridian\",0], "
                        + "  PARAMETER[\"false_easting\",0], "
                        + "  PARAMETER[\"false_northing\",0], "
                        + "  UNIT[\"metre\",1, "
                        + "    AUTHORITY[\"EPSG\",\"9001\"]], "
                        + "  AXIS[\"Easting\",EAST], "
                        + "  AXIS[\"Northing\",NORTH], "
                        + "  AUTHORITY[\"EPSG\",\"6933\"]]");
        // @formatter:on

        MathTransform to6933 = CRS.findMathTransform(DefaultGeographicCRS.WGS84, cea6933, true);
        MathTransform to4326 = CRS.findMathTransform(cea6933, DefaultGeographicCRS.WGS84, true);

        Point2D geographic = new Point2D.Double(10.0, 10.0);
        Point2D projected = doTransform(to6933, geographic);

        assertEquals(964862.802508965, projected.getX(), 1e-6);
        assertEquals(1269436.7435378374, projected.getY(), 1e-6);

        Point2D roundTrip = doTransform(to4326, projected);

        assertFalse(Double.isNaN(roundTrip.getX()));
        assertFalse(Double.isNaN(roundTrip.getY()));
        assertEquals(10.0, roundTrip.getX(), 1e-6);
        assertEquals(10.0, roundTrip.getY(), 1e-6);
    }

    private static Point2D doTransform(MathTransform transform, Point2D point) {
        double[] output = new double[2];
        double[] input = {point.getX(), point.getY()};
        try {
            transform.transform(input, 0, output, 0, 1);
        } catch (TransformException e) {
            throw new RuntimeException(e);
        }
        return new Point2D.Double(output[0], output[1]);
    }
}
