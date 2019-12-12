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

import java.awt.geom.Point2D;
import java.util.Arrays;
import org.apache.commons.lang3.SerializationUtils;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.junit.Assert;
import org.junit.Test;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

/** Tests for {@link AzimuthalEquidistant}. */
public class AzimuthalEquidistantTest {

    /** Test that parameter values are correctly converted to WKT. */
    @Test
    public void toWKT() throws Exception {
        // @formatter:off
        CoordinateReferenceSystem crs =
                CRS.parseWKT(
                        "PROJCS[\"unnamed\", "
                                + "GEOGCS[\"unnamed ellipse\", "
                                + "DATUM[\"unknown\", SPHEROID[\"unnamed\",6370841.391468334,0]], "
                                + "PRIMEM[\"Greenwich\",0], "
                                + "UNIT[\"degree\",0.0174532925199433]], "
                                + "PROJECTION[\"Azimuthal_Equidistant\"], "
                                + "PARAMETER[\"latitude_of_center\",42.42], "
                                + "PARAMETER[\"longitude_of_center\",16.16], "
                                + "PARAMETER[\"false_easting\",100000], "
                                + "PARAMETER[\"false_northing\",200000],"
                                + "UNIT[\"metre\", 1, AUTHORITY[\"EPSG\",\"9001\"]]]");
        // @formatter:on
        String wkt = crs.toWKT();
        Assert.assertTrue(wkt.contains("PROJECTION[\"Azimuthal_Equidistant\"]"));
        Assert.assertTrue(wkt.contains("PARAMETER[\"latitude_of_center\", 42.42]"));
        Assert.assertTrue(wkt.contains("PARAMETER[\"longitude_of_center\", 16.16]"));
        Assert.assertTrue(wkt.contains("PARAMETER[\"false_easting\", 100000.0]"));
        Assert.assertTrue(wkt.contains("PARAMETER[\"false_northing\", 200000.0]"));
    }

    @Test
    public void testLegacyProjectionParameters() throws Exception {
        CoordinateReferenceSystem azeq =
                CRS.parseWKT(
                        "PROJCS[\"Azeq test\", GEOGCS[\"WGS 84\", "
                                + "DATUM[\"World Geodetic System 1984\", "
                                + "SPHEROID[\"WGS 84\", 6378137.0, 298.257223563, "
                                + "AUTHORITY[\"EPSG\",\"7030\"]], "
                                + "AUTHORITY[\"EPSG\",\"6326\"]], "
                                + "PRIMEM[\"Greenwich\", 0.0, "
                                + "AUTHORITY[\"EPSG\",\"8901\"]], "
                                + "UNIT[\"degree\", 0.017453292519943295], "
                                + "AUTHORITY[\"EPSG\",\"4326\"]], "
                                + "PROJECTION[\"Azimuthal Equidistant\"], "
                                + "PARAMETER[\"latitude_of_origin\", 42.56], "
                                + "PARAMETER[\"longitude_of_origin\", -71.43], "
                                + "PARAMETER[\"scale_factor\", 1.0], "
                                + "PARAMETER[\"false_easting\", 0.0], "
                                + "PARAMETER[\"false_northing\", 0.0], "
                                + "UNIT[\"m\", 1.0],  AUTHORITY[\"EPSG\",\"741002\"]]");

        MathTransform transform = CRS.findMathTransform(azeq, DefaultGeographicCRS.WGS84);
        Point2D out = doTransform(transform, new Point2D.Double(0, 0));
        assertEquals(-71.43, out.getX(), 0.0001);
        assertEquals(42.56, out.getY(), 0.0001);

        Point2D out2 = doTransform(transform.inverse(), out);
        assertEquals(0, out2.getX(), 0.0001);
        assertEquals(0, out2.getY(), 0.0001);

        Point2D point = doTransform(transform.inverse(), new Point2D.Double(20, 60));
        assertEquals(3740188.0147449127, point.getX(), 0.0001);
        assertEquals(4831302.159548063, point.getY(), 0.0001);

        // transform object should be serializable
        byte[] serialized = SerializationUtils.serialize((java.io.Serializable) transform);
        MathTransform trans2 = SerializationUtils.deserialize(serialized);
        Point2D point2 = doTransform(trans2.inverse(), new Point2D.Double(20, 60));
        assertEquals(3740188.0147449127, point2.getX(), 0.0001);
        assertEquals(4831302.159548063, point2.getY(), 0.0001);
    }

    @Test
    public void testAutoCode() throws Exception {
        CoordinateReferenceSystem azeq = CRS.decode("AUTO:97003,9001,-71.43,42.56", true);
        MathTransform transform = CRS.findMathTransform(azeq, DefaultGeographicCRS.WGS84);
        Point2D point = doTransform(transform.inverse(), new Point2D.Double(20, 60));
        assertEquals(3740188.0147449127, point.getX(), 0.0001);
        assertEquals(4831302.159548063, point.getY(), 0.0001);
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

    @Test
    public void testReprojectAlongDateline() throws FactoryException, TransformException {
        CoordinateReferenceSystem crs = CRS.decode("AUTO:97003,9001,170,-16", true);
        MathTransform mt = CRS.findMathTransform(DefaultGeographicCRS.WGS84, crs);
        double[] src = new double[] {179.8, -11};
        double[] dst = new double[2];
        for (int i = 0; i < 40; i++) {
            mt.transform(src, 0, dst, 0, 1);
            System.out.println(Arrays.toString(src) + " --> " + Arrays.toString(dst));
            src[0] += 0.1;
        }
    }
}
