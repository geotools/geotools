/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.operation;

import java.util.Collections;
import java.util.Map;
import javax.measure.unit.SI;
import javax.measure.unit.NonSI;

import org.opengis.referencing.cs.*;
import org.opengis.referencing.crs.*;
import org.opengis.referencing.datum.*;
import org.opengis.referencing.operation.*;
import org.opengis.referencing.IdentifiedObject;
import org.opengis.referencing.FactoryException;
import org.opengis.parameter.ParameterValueGroup;

import org.geotools.factory.Hints;
import org.geotools.referencing.ReferencingFactoryFinder;

import org.junit.*;
import static org.junit.Assert.*;


/**
 * Tests of the {@code createProjectedCRS(...)} setting up the CRS with a 3D cartesian output for
 * one test, and using a 2D + vertical CRS compound for the second test. This test constructs most
 * objects using GeoAPI only (except for a few helper classes).
 *
 * @source $URL$
 * @version $Id$
 * @author Justin Couch
 * @author Martin Desruisseaux
 */
public final class Transform3DTest {
    /**
     * Convenience method returning a set of properties for a CRS with the specified name.
     */
    private static Map<String,String> name(final String name) {
        return Collections.singletonMap(IdentifiedObject.NAME_KEY, name);
    }

    /**
     * Tests a 3D projected to geocentric transform.
     *
     * @throws FactoryException If an object can't be created.
     * @throws TransformException If a coordinate transformation failed.
     */
    @Test
    public void testProjectedToGeocentric() throws FactoryException, TransformException {
        // ----------------------------------------------------------
        // Gets factories to be used for all object creations
        // ----------------------------------------------------------
        final Hints                          hints = new Hints();
        final CSFactory                  csFactory = ReferencingFactoryFinder.getCSFactory                 (hints);
        final CRSFactory                crsFactory = ReferencingFactoryFinder.getCRSFactory                (hints);
        final DatumFactory            datumFactory = ReferencingFactoryFinder.getDatumFactory              (hints);
        final MathTransformFactory       mtFactory = ReferencingFactoryFinder.getMathTransformFactory      (hints);
        final CoordinateOperationFactory opFactory = ReferencingFactoryFinder.getCoordinateOperationFactory(hints);

        // ----------------------------------------------------------
        // Creates datum
        // ----------------------------------------------------------
        final PrimeMeridian greenwichMeridian = datumFactory.createPrimeMeridian(
                    name("Greenwich Meridian"), 0, NonSI.DEGREE_ANGLE);
        final Ellipsoid wgs84Ellipsoid = datumFactory.createFlattenedSphere(
                    name("WGS84 Ellipsoid"), 6378137, 298.257223563, SI.METER);
        final GeodeticDatum wgs84 = datumFactory.createGeodeticDatum(
                    name("WGS84 Datum"), wgs84Ellipsoid, greenwichMeridian);
        final VerticalDatum wgs84_height = datumFactory.createVerticalDatum(
                    name("WGS84 Ellispoidal height"), VerticalDatumType.ELLIPSOIDAL);

        // ----------------------------------------------------------
        // Creates non-standard (in geodesy) geocentric axis
        // ----------------------------------------------------------
        final CoordinateSystemAxis x_axis = csFactory.createCoordinateSystemAxis(
                    name("X"), "X", AxisDirection.OTHER, SI.METER);
        final CoordinateSystemAxis y_axis = csFactory.createCoordinateSystemAxis(
                    name("Y"), "Y", AxisDirection.WEST, SI.METER);
        final CoordinateSystemAxis z_axis = csFactory.createCoordinateSystemAxis(
                    name("Z"), "Z", AxisDirection.NORTH, SI.METER);

        // ----------------------------------------------------------
        // Creates target CRS
        // ----------------------------------------------------------
        final CartesianCS world_cs = csFactory.createCartesianCS(
                    name("Rendered Cartesian CS"), x_axis, z_axis, y_axis);
        final GeocentricCRS output_crs = crsFactory.createGeocentricCRS(
                    name("Output Cartesian CRS"), wgs84, world_cs);

        // ----------------------------------------------------------
        // Creates geographic and projected axis for source CRS
        // ----------------------------------------------------------
        final CoordinateSystemAxis latitude_axis = csFactory.createCoordinateSystemAxis(
                    name("Geodetic Latitude"), "lat", AxisDirection.NORTH, NonSI.DEGREE_ANGLE);
        final CoordinateSystemAxis longitude_axis = csFactory.createCoordinateSystemAxis(
                    name("Geodetic Longitude"), "lon", AxisDirection.EAST, NonSI.DEGREE_ANGLE);
        final CoordinateSystemAxis northing_axis = csFactory.createCoordinateSystemAxis(
                    name("Northing"), "N", AxisDirection.NORTH, SI.METER);
        final CoordinateSystemAxis easting_axis = csFactory.createCoordinateSystemAxis(
                    name("Easting"), "E", AxisDirection.EAST, SI.METER);
        final CoordinateSystemAxis height_axis = csFactory.createCoordinateSystemAxis(
                    name("Ellipsoidal height"), "Up", AxisDirection.UP, SI.METER);

        // ----------------------------------------------------------
        // Creates the geographic CRS
        // ----------------------------------------------------------
        final EllipsoidalCS ellipsoidal_2d_cs = csFactory.createEllipsoidalCS(
                name("2D ellipsoidal"), longitude_axis, latitude_axis);
        final EllipsoidalCS ellipsoidal_3d_cs = csFactory.createEllipsoidalCS(
                name("3D ellipsoidal"), longitude_axis, latitude_axis, height_axis);
        final GeographicCRS geographic_2d_crs = crsFactory.createGeographicCRS(
                name("2D geographic CRS"), wgs84, ellipsoidal_2d_cs);
        final GeographicCRS geographic_3d_crs = crsFactory.createGeographicCRS(
                name("3D geographic CRS"), wgs84, ellipsoidal_3d_cs);

        // ----------------------------------------------------------
        // Creates various coordinate systems for projected CRS
        // ----------------------------------------------------------
        final CartesianCS utm_cartesian_3d_cs = csFactory.createCartesianCS(
                    name("UTM 3D Cartesian CS"), northing_axis, easting_axis, height_axis);
        final CartesianCS utm_cartesian_2d_cs = csFactory.createCartesianCS(
                    name("UTM 2D Cartesian CS"), northing_axis, easting_axis);
        final VerticalCS utm_height_cs = csFactory.createVerticalCS(
                    name("Height CS"), height_axis);
        final VerticalCRS height_crs = crsFactory.createVerticalCRS(
                    name("WGS84 Height CRS"), wgs84_height, utm_height_cs);

        // ----------------------------------------------------------
        // Set the projection for UTM zone 12
        // ----------------------------------------------------------
        final int zone_num = 12;
        final ParameterValueGroup parameters = mtFactory.getDefaultParameters("Transverse_Mercator");
        parameters.parameter("central_meridian")  .setValue(-180 + zone_num*6 - 3);
        parameters.parameter("latitude_of_origin").setValue(0.0);
        parameters.parameter("scale_factor")      .setValue(0.9996);
        parameters.parameter("false_easting")     .setValue(500000.0);
        parameters.parameter("false_northing")    .setValue(0.0);

        // ----------------------------------------------------------
        // From here we create a 2D projected system and combine
        // it with a height-only CRS to give it a full 3D transform
        // ----------------------------------------------------------
        final ProjectedCRS proj_2d = crsFactory.createProjectedCRS(
                name("WGS 84 / UTM Zone 12/ 2D"), geographic_2d_crs,
                new DefiningConversion("Transverse_Mercator", parameters), utm_cartesian_2d_cs);
        final CompoundCRS compound_3d = crsFactory.createCompoundCRS(
                name("3D Compound WGS 84 / UTM Zone 12"), new CoordinateReferenceSystem[] { proj_2d, height_crs });
        final double[] out1 = checkTransformation(opFactory.createOperation(compound_3d, output_crs));

        // ----------------------------------------------------------
        // From here we create a 3D projected system directly
        // ----------------------------------------------------------
        final ProjectedCRS proj_3d = crsFactory.createProjectedCRS(
                name("WGS 84 / UTM Zone 12/ 3D"), geographic_3d_crs,
                new DefiningConversion("Transverse_Mercator", parameters), utm_cartesian_3d_cs);
        final double[] out2 = checkTransformation(opFactory.createOperation(proj_3d, output_crs));

        // ----------------------------------------------------------
        // The two set of transformed coordinates should be the same
        // ----------------------------------------------------------
        final int upper = out1.length;
        assertEquals(upper, out2.length);
        for (int i=0; i<upper; i++) {
            assertEquals(out1[i], out2[i], 1E-5);
        }
    }

    /**
     * Tries to transforms some points using the specified operation.
     * This method transforms two points from the projected CRS to the geocentric CRS.
     * The first point is on the ellipsoid, and the second point is 10 km above the ellipsoid.
     * After transformation to geocentric CRS using a cartesian CS, the two points should still
     * ten kilometers apart each others.
     *
     * @return Transformed coordinates.
     */
    private static double[] checkTransformation(final CoordinateOperation operation) throws TransformException {
        assertTrue(operation.getSourceCRS()                       instanceof  ProjectedCRS);
        assertTrue(operation.getTargetCRS()                       instanceof GeocentricCRS);
        assertTrue(operation.getTargetCRS().getCoordinateSystem() instanceof   CartesianCS);

        final MathTransform mt = operation.getMathTransform();

        // Now a couple of transforms to show it working or not working...
        final double[] input  = {41451.73, 572227, 0};
        final double[] output = new double[input.length * 2];

        mt.transform(input, 0, output, 0, 1);

        input[2] = 10000;
        mt.transform(input, 0, output, input.length, 1);

        double distance = 0;
        for (int i=0; i<input.length; i++) {
            final double delta = output[i] - output[input.length + i];
            distance += delta*delta;
        }
        distance = Math.sqrt(distance);
        assertEquals("Distance", 10000, distance, 1E-5);

        return output;
    }
}
