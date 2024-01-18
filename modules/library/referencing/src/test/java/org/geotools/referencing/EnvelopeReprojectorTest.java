/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2024, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing;

import static org.geotools.referencing.crs.DefaultGeographicCRS.WGS84;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.awt.geom.Rectangle2D;
import org.geotools.api.geometry.Bounds;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.operation.CoordinateOperation;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.api.referencing.operation.MathTransform2D;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.geometry.GeneralBounds;
import org.geotools.geometry.Position2D;
import org.geotools.geometry.util.XRectangle2D;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.junit.Ignore;
import org.junit.Test;

public class EnvelopeReprojectorTest {

    public static final double EPS = 0.01;

    /** Tests the transformations of an envelope. */
    @Test
    // code is using equals with extra parameters and semantics compared to the built-in equals
    @SuppressWarnings("PMD.SimplifiableTestAssertion")
    public void testEnvelopeTransformation() throws FactoryException, TransformException {
        final CoordinateReferenceSystem mapCRS = CRS.parseWKT(WKT.UTM_10N);
        final CoordinateReferenceSystem WGS84 = DefaultGeographicCRS.WGS84;
        final CoordinateOperation operation =
                CRS.getCoordinateOperationFactory(true).createOperation(WGS84, mapCRS);
        assertFalse(operation.getMathTransform().isIdentity());

        final GeneralBounds firstEnvelope =
                new GeneralBounds(new double[] {-124, 42}, new double[] {-122, 43});
        firstEnvelope.setCoordinateReferenceSystem(WGS84);

        final GeneralBounds transformedEnvelope =
                EnvelopeReprojector.transform(operation, firstEnvelope);
        transformedEnvelope.setCoordinateReferenceSystem(mapCRS);

        final CoordinateOperation inverse =
                CRS.getCoordinateOperationFactory(true).createOperation(mapCRS, WGS84);
        final GeneralBounds oldEnvelope =
                EnvelopeReprojector.transform(inverse, transformedEnvelope);
        oldEnvelope.setCoordinateReferenceSystem(WGS84);

        assertTrue(oldEnvelope.contains(firstEnvelope, true));
        assertTrue(oldEnvelope.equals(firstEnvelope, 0.02, true));
    }

    /**
     * Tests the transformations of an envelope when the two CRS have identify transforms but
     * different datum names
     */
    @Test
    public void testEnvelopeTransformation2() throws FactoryException, TransformException {
        final CoordinateReferenceSystem WGS84Altered = CRS.parseWKT(WKT.WGS84_ALTERED);
        final CoordinateReferenceSystem WGS84 = DefaultGeographicCRS.WGS84;
        final MathTransform crsTransform = CRS.findMathTransform(WGS84, WGS84Altered, true);
        assertTrue(crsTransform.isIdentity());

        final GeneralBounds firstEnvelope =
                new GeneralBounds(new double[] {-124, 42}, new double[] {-122, 43});
        firstEnvelope.setCoordinateReferenceSystem(WGS84);

        // this triggered a assertion error in GEOT-2934
        Bounds transformed = CRS.transform(firstEnvelope, WGS84Altered);

        // check the envelope is what we expect
        assertEquals(transformed.getCoordinateReferenceSystem(), WGS84Altered);
        double EPS = 1e-9;
        assertEquals(transformed.getMinimum(0), firstEnvelope.getMinimum(0), EPS);
        assertEquals(transformed.getMinimum(1), firstEnvelope.getMinimum(1), EPS);
        assertEquals(transformed.getMaximum(0), firstEnvelope.getMaximum(0), EPS);
        assertEquals(transformed.getMaximum(1), firstEnvelope.getMaximum(1), EPS);
    }

    @Test
    @Ignore
    public void testEnvelopeTransformClipping() throws Exception {
        final CoordinateReferenceSystem source = WGS84;
        final CoordinateReferenceSystem target =
                CRS.parseWKT(
                        "GEOGCS[\"GCS_North_American_1983\","
                                + "DATUM[\"North_American_Datum_1983\", "
                                + "SPHEROID[\"GRS_1980\", 6378137.0, 298.257222101]], "
                                + "PRIMEM[\"Greenwich\", 0.0], "
                                + "UNIT[\"degree\", 0.017453292519943295], "
                                + "AXIS[\"Longitude\", EAST], "
                                + "AXIS[\"Latitude\", NORTH]]");
        // bounds from geotiff
        GeneralBounds geotiff = new GeneralBounds(source);
        geotiff.add(new Position2D(source, -179.9, -90.0));
        geotiff.add(new Position2D(source, 180.0, 89.9));

        Bounds transformed = CRS.transform(geotiff, target);
        assertNotNull(transformed);
        assertTrue("clipped y", transformed.getUpperCorner().getOrdinate(1) > 88.0);
        assertTrue("clipped y", transformed.getLowerCorner().getOrdinate(1) < -88.0);
        assertTrue("clipped x", transformed.getUpperCorner().getOrdinate(0) > 170.0);
        assertTrue("clipped x", transformed.getLowerCorner().getOrdinate(0) < -170.0);
    }

    /**
     * Tests the transformations of a rectangle using a coordinate operation. With assertions
     * enabled, this also test the transformation of an envelope.
     */
    @Test
    public void testTransformationOverPole() throws FactoryException, TransformException {
        final CoordinateReferenceSystem mapCRS = CRS.parseWKT(WKT.POLAR_STEREOGRAPHIC);
        final CoordinateReferenceSystem WGS84 = DefaultGeographicCRS.WGS84;
        final CoordinateOperation operation =
                CRS.getCoordinateOperationFactory(false).createOperation(mapCRS, WGS84);
        final MathTransform transform = operation.getMathTransform();
        assertTrue(transform instanceof MathTransform2D);
        /*
         * The rectangle to test, which contains the South pole.
         */
        Rectangle2D envelope =
                XRectangle2D.createFromExtremums(
                        -3943612.4042124213,
                        -4078471.954436003,
                        3729092.5890516187,
                        4033483.085688618);
        /*
         * This is what we get without special handling of singularity point.
         * Note that is doesn't include the South pole as we would expect.
         */
        Rectangle2D expected =
                XRectangle2D.createFromExtremums(
                        -178.49352310409273,
                        -88.99136583196398,
                        137.56220967463082,
                        -40.905775004205864);
        /*
         * Tests what we actually get.
         */
        Rectangle2D actual = CRS.transform((MathTransform2D) transform, envelope, null);
        assertTrue(XRectangle2D.equalsEpsilon(expected, actual));
        /*
         * Using the transform(CoordinateOperation, ...) method,
         * the singularity at South pole is taken in account.
         */
        expected = XRectangle2D.createFromExtremums(-180, -90, 180, -40.905775004205864);
        actual = CRS.transform(operation, envelope, actual);
        assertTrue(XRectangle2D.equalsEpsilon(expected, actual));
        /*
         * The rectangle to test, which contains the South pole, but this time the south
         * pole is almost in a corner of the rectangle
         */
        envelope = XRectangle2D.createFromExtremums(-4000000, -4000000, 300000, 30000);
        expected = XRectangle2D.createFromExtremums(-180, -90, 180, -41.03163170198091);
        actual = CRS.transform(operation, envelope, actual);
        assertTrue(XRectangle2D.equalsEpsilon(expected, actual));
    }

    @Test
    public void testReprojectEnvelope() throws Exception {
        String wkt =
                "GEOGCS[\"WGS84(DD)\","
                        + "DATUM[\"WGS84\", "
                        + "SPHEROID[\"WGS84\", 6378137.0, 298.257223563]],"
                        + "PRIMEM[\"Greenwich\", 0.0],"
                        + "UNIT[\"degree\", 0.017453292519943295],"
                        + "AXIS[\"Geodetic longitude\", EAST],"
                        + "AXIS[\"Geodetic latitude\", NORTH]]";
        CoordinateReferenceSystem wgs84 = CRS.parseWKT(wkt);
        wkt =
                "PROJCS[\"WGS 84 / UTM zone 32N\", \n"
                        + "  GEOGCS[\"WGS 84\", \n"
                        + "    DATUM[\"World Geodetic System 1984\", \n"
                        + "      SPHEROID[\"WGS 84\", 6378137.0, 298.257223563, AUTHORITY[\"EPSG\",\"7030\"]], \n"
                        + "      AUTHORITY[\"EPSG\",\"6326\"]], \n"
                        + "    PRIMEM[\"Greenwich\", 0.0, AUTHORITY[\"EPSG\",\"8901\"]], \n"
                        + "    UNIT[\"degree\", 0.017453292519943295], \n"
                        + "    AXIS[\"Geodetic longitude\", EAST], \n"
                        + "    AXIS[\"Geodetic latitude\", NORTH], \n"
                        + "    AUTHORITY[\"EPSG\",\"4326\"]], \n"
                        + "  PROJECTION[\"Transverse Mercator\", AUTHORITY[\"EPSG\",\"9807\"]], \n"
                        + "  PARAMETER[\"central_meridian\", 9.0], \n"
                        + "  PARAMETER[\"latitude_of_origin\", 0.0], \n"
                        + "  PARAMETER[\"scale_factor\", 0.9996], \n"
                        + "  PARAMETER[\"false_easting\", 500000.0], \n"
                        + "  PARAMETER[\"false_northing\", 0.0], \n"
                        + "  UNIT[\"m\", 1.0], \n"
                        + "  AXIS[\"Easting\", EAST], \n"
                        + "  AXIS[\"Northing\", NORTH], \n"
                        + "  AUTHORITY[\"EPSG\",\"32632\"]]";
        CoordinateReferenceSystem utm32n = CRS.parseWKT(wkt);

        GeneralBounds envelope = new GeneralBounds(utm32n);
        envelope.setEnvelope(895817.968, 4439270.710, 1081186.865, 4617454.766);
        // used to throw an exception here
        GeneralBounds transformed = CRS.transform(envelope, wgs84);
        assertEquals(13.63, transformed.getMinimum(0), EPS);
        assertEquals(39.9, transformed.getMinimum(1), EPS);
        assertEquals(15.96, transformed.getMaximum(0), EPS);
        assertEquals(41.61, transformed.getMaximum(1), EPS);
    }

    @Test
    public void testOrthographic() throws Exception {
        // centered orthographic
        CoordinateReferenceSystem orthographic = CRS.decode("AUTO:42003,9001,0,0");
        // off center bounds, the right corners are outside of the sphere
        GeneralBounds bounds = new GeneralBounds(WGS84);
        bounds.setEnvelope(-90, -30, 90, 90);
        testOrthographicDisc(orthographic, bounds);
    }

    private static void testOrthographicDisc(
            CoordinateReferenceSystem orthographic, GeneralBounds bounds)
            throws FactoryException, TransformException {
        CoordinateOperation operation =
                CRS.getCoordinateOperationFactory(true).createOperation(WGS84, orthographic);

        // off center bounds, the right corners are outside of the sphere
        Bounds result = EnvelopeReprojector.transform(operation, bounds);
        double radius = 6378137;
        // the reprojector accounted for the max elongation on the equator and expanded the bounds
        // so that they are symmetric
        assertEquals(-radius, result.getMinimum(0), EPS);
        assertEquals(radius, result.getMaximum(0), EPS);
    }
}
