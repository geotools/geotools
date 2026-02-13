/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.geotools.api.geometry.Bounds;
import org.geotools.api.parameter.ParameterValueGroup;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.geometry.Position2D;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.junit.BeforeClass;
import org.junit.Test;

public class GeostationarySatelliteTest {

    public static final String sphericalGeosWKT = "PROJCS[\"Geostationary_Satellite\","
            + "  GEOGCS[\"Custom Geographic CS\","
            + "    DATUM[\"Custom Datum\","
            + "      SPHEROID[\"Sphere\",6367451.5, 0]],"
            + "    PRIMEM[\"Greenwich\",0],UNIT[\"degree\",0.0174532925199433]],"
            + "    PROJECTION[\"Geostationary_Satellite\"],"
            + "    PARAMETER[\"central_meridian\", -135],"
            + "    PARAMETER[\"satellite_height\",35832548.5],"
            + "    PARAMETER[\"false_easting\",0],"
            + "    PARAMETER[\"false_northing\",0],"
            + "    UNIT[\"meter\", 1]]";

    public static final String ellipsoidalGeosWKT = "PROJCS[\"Geostationary_Satellite\","
            + "  GEOGCS[\"WGS 84\","
            + "    DATUM[\"WGS_1984\","
            + "      SPHEROID[\"WGS84\",6378137,298.257223563]],"
            + "    PRIMEM[\"Greenwich\",0],UNIT[\"degree\",0.01745329251994328]],"
            + "    PROJECTION[\"Geostationary_Satellite\"],"
            + "    PARAMETER[\"central_meridian\", -135],"
            + "    PARAMETER[\"satellite_height\",35785831.0],"
            + "    PARAMETER[\"false_easting\",0],"
            + "    PARAMETER[\"false_northing\",0],"
            + "    PARAMETER[\"sweep\",0],"
            + "    UNIT[\"meter\", 1]]";

    public static final int DIMENSION_X = 0;
    public static final int DIMENSION_Y = 1;

    static CoordinateReferenceSystem sphericalGeosCRS;
    static MathTransform sphericalGeosToGeog;
    static MathTransform geogToSphericalGeos;

    static CoordinateReferenceSystem ellipsoidalGeosCRS;
    static MathTransform ellipsoidalGeosToGeog;
    static MathTransform geogToEllipsoidalGeos;

    @BeforeClass
    public static void setupClass() throws FactoryException, TransformException {
        sphericalGeosCRS = CRS.parseWKT(sphericalGeosWKT);
        sphericalGeosToGeog = CRS.findMathTransform(
                sphericalGeosCRS, CRS.getProjectedCRS(sphericalGeosCRS).getBaseCRS(), true);
        geogToSphericalGeos = sphericalGeosToGeog.inverse();

        ellipsoidalGeosCRS = CRS.parseWKT(ellipsoidalGeosWKT);
        ellipsoidalGeosToGeog = CRS.findMathTransform(
                ellipsoidalGeosCRS, CRS.getProjectedCRS(ellipsoidalGeosCRS).getBaseCRS(), true);
        geogToEllipsoidalGeos = ellipsoidalGeosToGeog.inverse();
    }

    @Test
    public void testSpheroidalWKTParameters() {
        ParameterValueGroup parameters = CRS.getMapProjection(sphericalGeosCRS).getParameterValues();
        double satelliteHeight = parameters.parameter("satellite_height").doubleValue();
        assertThat(satelliteHeight, is(35832548.5));
        double sweep = parameters.parameter("sweep").doubleValue();
        assertThat(sweep, is(1.));
    }

    @Test
    public void testEllipsoidalWKTParameters() {
        ParameterValueGroup parameters =
                CRS.getMapProjection(ellipsoidalGeosCRS).getParameterValues();
        double satelliteHeight = parameters.parameter("satellite_height").doubleValue();
        assertThat(satelliteHeight, is(35785831.0));
        double sweep = parameters.parameter("sweep").doubleValue();
        assertThat(sweep, is(0.));
    }

    @Test
    public void testEllipsoidProjection() throws Exception {
        // Calculated with pyproj: proj=geos +lon_0=-135 +h=35785831 +x_0=0 +y_0=0 +ellps=WGS84 +sweep=x
        double[] wgs84 = {
            -71.391245, 41.766279,
            -80.452193, -5.547325,
            -53.700994, 0.0, // Limb (approx 81.3 deg East of center)
            -135.0, 64.143628 // High Latitude (due North of center)
        };
        double[] geos = {
            3778584.7403456536, 3762727.556421232,
            4779926.185724244, -569498.6040787804,
            5434177.815700539, 0.0,
            0.0, 5159593.473948262
        };

        double[] actual = new double[wgs84.length];

        // Forward Transform (wgs84 -> Geos)
        geogToEllipsoidalGeos.transform(wgs84, 0, actual, 0, wgs84.length / 2);
        assertArrayEquals("Forward transform failed accuracy test", geos, actual, 1 /* meter */);

        // And back, inverse Transform (Geos -> wgs84)
        double[] actualWgs = new double[wgs84.length];
        ellipsoidalGeosToGeog.transform(geos, 0, actualWgs, 0, geos.length / 2);
        assertArrayEquals(
                "Inverse transform failed accuracy test (check your latitude math!)",
                wgs84,
                actualWgs,
                1e-6 /* degree */);
    }

    @Test
    public void testIsGeostationaryCRS() {
        assertThat(GeostationarySatellite.isGeostationaryCRS(sphericalGeosCRS), is(true));
        assertThat(GeostationarySatellite.isGeostationaryCRS(ellipsoidalGeosCRS), is(true));
        assertThat(GeostationarySatellite.isGeostationaryCRS(DefaultGeographicCRS.WGS84), is(false));
        assertThat(GeostationarySatellite.isGeostationaryCRS(null), is(false));
    }

    @Test
    public void testCircumscribeFullDisk_Spheroidal() throws TransformException, FactoryException {

        final Bounds circumscribed = GeostationarySatellite.circumscribeFullDisk(sphericalGeosCRS);
        assertThat(circumscribed, is(notNullValue()));
        assertEquals("Median dim 0 is wrong", 0.0, circumscribed.getMedian(0), 0.00001);
        assertEquals("Median dim 1 is wrong", 0.0, circumscribed.getMedian(1), 0.00001);
        final Position2D p = new Position2D();

        p.setLocation(circumscribed.getMedian(0), circumscribed.getMaximum(1));
        sphericalGeosToGeog.transform(p, p);
        assertThat(p, is(notNullValue()));
        geogToSphericalGeos.transform(p, p);
        assertThat(p, is(notNullValue()));

        p.setLocation(circumscribed.getMedian(0), circumscribed.getMinimum(1));
        sphericalGeosToGeog.transform(p, p);
        assertThat(p, is(notNullValue()));
        geogToSphericalGeos.transform(p, p);
        assertThat(p, is(notNullValue()));

        p.setLocation(circumscribed.getMaximum(0), circumscribed.getMedian(1));
        sphericalGeosToGeog.transform(p, p);
        assertThat(p, is(notNullValue()));
        geogToSphericalGeos.transform(p, p);
        assertThat(p, is(notNullValue()));

        p.setLocation(circumscribed.getMinimum(0), circumscribed.getMedian(1));
        sphericalGeosToGeog.transform(p, p);
        assertThat(p, is(notNullValue()));
        geogToSphericalGeos.transform(p, p);
        assertThat(p, is(notNullValue()));

        // show that bounds of rectangle circumscribing full disk image is not
        // transformable stepping 1 meter outside the X and Y extents along the
        // orthogonal center axes
        final double tickle = 1;
        expectProjectionException(() -> {
            p.setLocation(circumscribed.getMedian(0), circumscribed.getMaximum(0) + tickle);
            sphericalGeosToGeog.transform(p, p);
        });
        expectProjectionException(() -> {
            p.setLocation(circumscribed.getMedian(0), circumscribed.getMinimum(1) - tickle);
            sphericalGeosToGeog.transform(p, p);
        });
        expectProjectionException(() -> {
            p.setLocation(circumscribed.getMinimum(0) - tickle, circumscribed.getMedian(1));
            sphericalGeosToGeog.transform(p, p);
        });
        expectProjectionException(() -> {
            p.setLocation(circumscribed.getMaximum(0) + tickle, circumscribed.getMedian(1));
            sphericalGeosToGeog.transform(p, p);
        });

        // show that bounds of rectangle circumscribing full disk image is not transformable
        expectProjectionException(() -> {
            p.setLocation(circumscribed.getMaximum(0), circumscribed.getMaximum(0));
            sphericalGeosToGeog.transform(p, p);
        });
        expectProjectionException(() -> {
            p.setLocation(circumscribed.getMaximum(0), circumscribed.getMinimum(1));
            sphericalGeosToGeog.transform(p, p);
        });
        expectProjectionException(() -> {
            p.setLocation(circumscribed.getMinimum(0), circumscribed.getMaximum(0));
            sphericalGeosToGeog.transform(p, p);
        });
        expectProjectionException(() -> {
            p.setLocation(circumscribed.getMinimum(0), circumscribed.getMinimum(1));
            sphericalGeosToGeog.transform(p, p);
        });
    }

    @Test
    public void testCircumscribeFullDisk_Ellipsoidal() throws TransformException, FactoryException {

        final Bounds circumscribed = GeostationarySatellite.circumscribeFullDisk(ellipsoidalGeosCRS);
        assertThat(circumscribed, is(notNullValue()));

        final Position2D p = new Position2D();

        p.setLocation(circumscribed.getMedian(DIMENSION_X), circumscribed.getMaximum(DIMENSION_Y));
        ellipsoidalGeosToGeog.transform(p, p);
        assertThat(p, is(notNullValue()));
        geogToEllipsoidalGeos.transform(p, p);
        assertThat(p, is(notNullValue()));

        p.setLocation(circumscribed.getMedian(DIMENSION_X), circumscribed.getMinimum(DIMENSION_Y));
        ellipsoidalGeosToGeog.transform(p, p);
        assertThat(p, is(notNullValue()));
        geogToEllipsoidalGeos.transform(p, p);
        assertThat(p, is(notNullValue()));

        p.setLocation(circumscribed.getMaximum(DIMENSION_X), circumscribed.getMedian(DIMENSION_Y));
        ellipsoidalGeosToGeog.transform(p, p);
        assertThat(p, is(notNullValue()));
        geogToEllipsoidalGeos.transform(p, p);
        assertThat(p, is(notNullValue()));

        p.setLocation(circumscribed.getMinimum(DIMENSION_X), circumscribed.getMedian(DIMENSION_Y));
        ellipsoidalGeosToGeog.transform(p, p);
        assertThat(p, is(notNullValue()));
        geogToEllipsoidalGeos.transform(p, p);
        assertThat(p, is(notNullValue()));

        // show that bounds of rectangle circumscribing full disk image is not
        // transformable stepping 1 meter outside the X and Y extents along the
        // orthogonal center axes
        final double tickle = 1;
        expectProjectionException(() -> {
            p.setLocation(circumscribed.getMedian(0), circumscribed.getMaximum(0) + tickle);
            ellipsoidalGeosToGeog.transform(p, p);
        });
        expectProjectionException(() -> {
            p.setLocation(circumscribed.getMedian(0), circumscribed.getMinimum(1) - tickle);
            ellipsoidalGeosToGeog.transform(p, p);
        });
        expectProjectionException(() -> {
            p.setLocation(circumscribed.getMinimum(0) - tickle, circumscribed.getMedian(1));
            ellipsoidalGeosToGeog.transform(p, p);
        });
        expectProjectionException(() -> {
            p.setLocation(circumscribed.getMaximum(0) + tickle, circumscribed.getMedian(1));
            ellipsoidalGeosToGeog.transform(p, p);
        });

        // show that bounds of rectangle circumscribing full disk image is not transformable
        expectProjectionException(() -> {
            p.setLocation(circumscribed.getMaximum(0), circumscribed.getMaximum(0));
            ellipsoidalGeosToGeog.transform(p, p);
        });
        expectProjectionException(() -> {
            p.setLocation(circumscribed.getMaximum(0), circumscribed.getMinimum(1));
            ellipsoidalGeosToGeog.transform(p, p);
        });
        expectProjectionException(() -> {
            p.setLocation(circumscribed.getMinimum(0), circumscribed.getMaximum(0));
            ellipsoidalGeosToGeog.transform(p, p);
        });
        expectProjectionException(() -> {
            p.setLocation(circumscribed.getMinimum(0), circumscribed.getMinimum(1));
            ellipsoidalGeosToGeog.transform(p, p);
        });
    }

    @Test
    public void testInscribeFullDiskEstimate_Spheroidal() throws TransformException, FactoryException {

        final Bounds inscribed = GeostationarySatellite.inscribeFullDiskEstimate(sphericalGeosCRS);
        assertThat(inscribed, is(notNullValue()));

        final Position2D p = new Position2D();

        p.setLocation(inscribed.getMaximum(0), inscribed.getMaximum(0));
        sphericalGeosToGeog.transform(p, p);
        assertThat(p, is(notNullValue()));
        geogToSphericalGeos.transform(p, p);
        assertThat(p, is(notNullValue()));

        p.setLocation(inscribed.getMaximum(0), inscribed.getMinimum(1));
        sphericalGeosToGeog.transform(p, p);
        assertThat(p, is(notNullValue()));
        geogToSphericalGeos.transform(p, p);
        assertThat(p, is(notNullValue()));

        p.setLocation(inscribed.getMinimum(0), inscribed.getMaximum(0));
        sphericalGeosToGeog.transform(p, p);
        assertThat(p, is(notNullValue()));
        geogToSphericalGeos.transform(p, p);
        assertThat(p, is(notNullValue()));

        p.setLocation(inscribed.getMinimum(0), inscribed.getMinimum(1));
        sphericalGeosToGeog.transform(p, p);
        assertThat(p, is(notNullValue()));
        geogToSphericalGeos.transform(p, p);
        assertThat(p, is(notNullValue()));

        // Inscribed rectangle is smaller than largest inscribing rectangle, hence ESTIMATE
        //        final double tickle = 1;
        //        expectProjectionException(new Testable() { public void test() throws Exception {
        //            p.setLocation(inscribed.getMaximum(0) + tickle, inscribed.getMaximum(0) +
        // tickle);
        //            sphericalGeosToGeog.transform(p, p);
        //        }});
        //        expectProjectionException(new Testable() { public void test() throws Exception {
        //            p.setLocation(inscribed.getMaximum(0) + tickle, inscribed.getMinimum(1) -
        // tickle);
        //            sphericalGeosToGeog.transform(p, p);
        //        }});
        //        expectProjectionException(new Testable() { public void test() throws Exception {
        //            p.setLocation(inscribed.getMinimum(0) - tickle, inscribed.getMaximum(0) +
        // tickle);
        //            sphericalGeosToGeog.transform(p, p);
        //        }});
        //        expectProjectionException(new Testable() { public void test() throws Exception {
        //            p.setLocation(inscribed.getMinimum(0) - tickle, inscribed.getMinimum(1) -
        // tickle);
        //            sphericalGeosToGeog.transform(p, p);
        //        }});
    }

    @Test
    public void testInscribeFullDiskEstimate_Ellipsoidal() throws TransformException, FactoryException {

        final Bounds inscribed = GeostationarySatellite.inscribeFullDiskEstimate(ellipsoidalGeosCRS);
        assertThat(inscribed, is(notNullValue()));
        assertEquals("Median dim 0 is wrong", 0.0, inscribed.getMedian(0), 0.00001);
        assertEquals("Median dim 1 is wrong", 0.0, inscribed.getMedian(1), 0.00001);
        final Position2D p = new Position2D();

        p.setLocation(inscribed.getMaximum(0), inscribed.getMaximum(1));
        ellipsoidalGeosToGeog.transform(p, p);
        assertThat(p, is(notNullValue()));
        geogToEllipsoidalGeos.transform(p, p);
        assertThat(p, is(notNullValue()));

        p.setLocation(inscribed.getMaximum(0), inscribed.getMinimum(1));
        ellipsoidalGeosToGeog.transform(p, p);
        assertThat(p, is(notNullValue()));
        geogToEllipsoidalGeos.transform(p, p);
        assertThat(p, is(notNullValue()));

        p.setLocation(inscribed.getMinimum(0), inscribed.getMaximum(1));
        ellipsoidalGeosToGeog.transform(p, p);
        assertThat(p, is(notNullValue()));
        geogToEllipsoidalGeos.transform(p, p);
        assertThat(p, is(notNullValue()));

        p.setLocation(inscribed.getMinimum(0), inscribed.getMinimum(1));
        ellipsoidalGeosToGeog.transform(p, p);
        assertThat(p, is(notNullValue()));
        geogToEllipsoidalGeos.transform(p, p);
        assertThat(p, is(notNullValue()));

        // Inscribed rectangle is smaller than largest inscribing rectangle, hence ESTIMATE
        //        final double tickle = 1;
        //        expectProjectionException(new Testable() { public void test() throws Exception {
        //            p.setLocation(inscribed.getMaximum(0) + tickle, inscribed.getMaximum(0) +
        // tickle);
        //            ellipsoidalGeosToGeog.transform(p, p);
        //        }});
        //        expectProjectionException(new Testable() { public void test() throws Exception {
        //            p.setLocation(inscribed.getMaximum(0) + tickle, inscribed.getMinimum(1) -
        // tickle);
        //            ellipsoidalGeosToGeog.transform(p, p);
        //        }});
        //        expectProjectionException(new Testable() { public void test() throws Exception {
        //            p.setLocation(inscribed.getMinimum(0) - tickle, inscribed.getMaximum(0) +
        // tickle);
        //            ellipsoidalGeosToGeog.transform(p, p);
        //        }});
        //        expectProjectionException(new Testable() { public void test() throws Exception {
        //            p.setLocation(inscribed.getMinimum(0) - tickle, inscribed.getMinimum(1) -
        // tickle);
        //            ellipsoidalGeosToGeog.transform(p, p);
        //        }});
    }

    @Test(expected = ProjectionException.class)
    public void testOffDiskVisibility() throws Exception {
        // A point slightly further out than the maximum possible X/Y
        // For WGS84 at 35,785,831m, the horizon is ~5434177m
        double[] offDisk = {5500000.0, 0.0};
        double[] result = new double[2];
        ellipsoidalGeosToGeog.transform(offDisk, 0, result, 0, 1);
    }

    @Test(expected = ProjectionException.class)
    public void testHorizonPrecisionFailure() throws Exception {
        // This value is roughly 20cm past the physical horizon of the Earth
        double xPastHorizon = 5434178.0;
        double y = 0.0;

        double[] result = new double[2];
        ellipsoidalGeosToGeog.transform(new double[] {xPastHorizon, y}, 0, result, 0, 1);
    }

    private void expectProjectionException(Testable testable) {
        expectException(ProjectionException.class, testable);
    }

    private <T extends Exception> void expectException(Class<T> clazz, Testable testable) {
        try {
            testable.test();
            fail("Expected exception, %s, but not thrown".formatted(clazz));
        } catch (Exception e) {
            if (!clazz.isInstance(e)) {
                fail("Expected exception of %s but got %s".formatted(clazz, e.getClass()));
            }
        }
    }

    @SuppressWarnings("PMD.UnitTestShouldUseTestAnnotation")
    public interface Testable {
        void test() throws Exception;
    }
}
