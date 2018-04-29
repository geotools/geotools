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

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.Envelope2D;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

public class GeostationarySatelliteTest {

    public static final String sphericalGeosWKT =
            "PROJCS[\"Geostationary_Satellite\","
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

    public static final String ellipsoidalGeosWKT =
            "PROJCS[\"Geostationary_Satellite\","
                    + "  GEOGCS[\"WGS 84\","
                    + "    DATUM[\"WGS_1984\","
                    + "      SPHEROID[\"WGS84\",6378137,298.257223563]],"
                    + "    PRIMEM[\"Greenwich\",0],UNIT[\"degree\",0.01745329251994328]],"
                    + "    PROJECTION[\"Geostationary_Satellite\"],"
                    + "    PARAMETER[\"central_meridian\", -135],"
                    + "    PARAMETER[\"satellite_height\",35785831.0],"
                    + "    PARAMETER[\"false_easting\",0],"
                    + "    PARAMETER[\"false_northing\",0],"
                    + "    UNIT[\"meter\", 1]]";

    static CoordinateReferenceSystem sphericalGeosCRS;
    static MathTransform sphericalGeosToGeog;
    static MathTransform geogToSphericalGeos;

    static CoordinateReferenceSystem ellipsoidalGeosCRS;
    static MathTransform ellipsoidalGeosToGeog;
    static MathTransform geogToEllipsoidalGeos;

    @BeforeClass
    public static void setupClass() throws FactoryException, TransformException {
        sphericalGeosCRS = CRS.parseWKT(sphericalGeosWKT);
        sphericalGeosToGeog =
                CRS.findMathTransform(
                        sphericalGeosCRS, CRS.getProjectedCRS(sphericalGeosCRS).getBaseCRS(), true);
        geogToSphericalGeos = sphericalGeosToGeog.inverse();

        ellipsoidalGeosCRS = CRS.parseWKT(ellipsoidalGeosWKT);
        ellipsoidalGeosToGeog =
                CRS.findMathTransform(
                        ellipsoidalGeosCRS,
                        CRS.getProjectedCRS(ellipsoidalGeosCRS).getBaseCRS(),
                        true);
        geogToEllipsoidalGeos = ellipsoidalGeosToGeog.inverse();
    }

    @Test
    public void testSpheroidalWKTParameters() {
        ParameterValueGroup parameters =
                CRS.getMapProjection(sphericalGeosCRS).getParameterValues();
        double satelliteHeight = parameters.parameter("satellite_height").doubleValue();
        assertThat(satelliteHeight, is(35832548.5));
    }

    @Test
    public void testEllipsoidalWKTParameters() {
        ParameterValueGroup parameters =
                CRS.getMapProjection(ellipsoidalGeosCRS).getParameterValues();
        double satelliteHeight = parameters.parameter("satellite_height").doubleValue();
        assertThat(satelliteHeight, is(35785831.0));
    }

    @Test
    public void testIsGeostationaryCRS() {
        assertThat(GeostationarySatellite.isGeostationaryCRS(sphericalGeosCRS), is(true));
        assertThat(GeostationarySatellite.isGeostationaryCRS(ellipsoidalGeosCRS), is(true));
        assertThat(
                GeostationarySatellite.isGeostationaryCRS(DefaultGeographicCRS.WGS84), is(false));
        assertThat(GeostationarySatellite.isGeostationaryCRS(null), is(false));
    }

    @Test
    public void testCircumscribeFullDisk_Spheroidal() throws TransformException, FactoryException {

        final Envelope2D circumscribed =
                GeostationarySatellite.circumscribeFullDisk(sphericalGeosCRS);
        assertThat(circumscribed, is(notNullValue()));

        final DirectPosition2D p = new DirectPosition2D();

        p.setLocation(circumscribed.getCenterX(), circumscribed.getMaxY());
        sphericalGeosToGeog.transform(p, p);
        assertThat(p, is(notNullValue()));
        geogToSphericalGeos.transform(p, p);
        assertThat(p, is(notNullValue()));

        p.setLocation(circumscribed.getCenterX(), circumscribed.getMinY());
        sphericalGeosToGeog.transform(p, p);
        assertThat(p, is(notNullValue()));
        geogToSphericalGeos.transform(p, p);
        assertThat(p, is(notNullValue()));

        p.setLocation(circumscribed.getMaxX(), circumscribed.getCenterY());
        sphericalGeosToGeog.transform(p, p);
        assertThat(p, is(notNullValue()));
        geogToSphericalGeos.transform(p, p);
        assertThat(p, is(notNullValue()));

        p.setLocation(circumscribed.getMinX(), circumscribed.getCenterY());
        sphericalGeosToGeog.transform(p, p);
        assertThat(p, is(notNullValue()));
        geogToSphericalGeos.transform(p, p);
        assertThat(p, is(notNullValue()));

        // show that bounds of rectangle circumscribing full disk image is not
        // transformable stepping 1 meter outside the X and Y extents along the
        // orthogonal center axes
        final double tickle = 1;
        expectProjectionException(
                new Testable() {
                    public void test() throws Exception {
                        p.setLocation(circumscribed.getCenterX(), circumscribed.getMaxY() + tickle);
                        sphericalGeosToGeog.transform(p, p);
                    }
                });
        expectProjectionException(
                new Testable() {
                    public void test() throws Exception {
                        p.setLocation(circumscribed.getCenterX(), circumscribed.getMinY() - tickle);
                        sphericalGeosToGeog.transform(p, p);
                    }
                });
        expectProjectionException(
                new Testable() {
                    public void test() throws Exception {
                        p.setLocation(circumscribed.getMinX() - tickle, circumscribed.getCenterY());
                        sphericalGeosToGeog.transform(p, p);
                    }
                });
        expectProjectionException(
                new Testable() {
                    public void test() throws Exception {
                        p.setLocation(circumscribed.getMaxX() + tickle, circumscribed.getCenterY());
                        sphericalGeosToGeog.transform(p, p);
                    }
                });

        // show that bounds of rectangle circumscribing full disk image is not transformable
        expectProjectionException(
                new Testable() {
                    public void test() throws Exception {
                        p.setLocation(circumscribed.getMaxX(), circumscribed.getMaxY());
                        sphericalGeosToGeog.transform(p, p);
                    }
                });
        expectProjectionException(
                new Testable() {
                    public void test() throws Exception {
                        p.setLocation(circumscribed.getMaxX(), circumscribed.getMinY());
                        sphericalGeosToGeog.transform(p, p);
                    }
                });
        expectProjectionException(
                new Testable() {
                    public void test() throws Exception {
                        p.setLocation(circumscribed.getMinX(), circumscribed.getMaxY());
                        sphericalGeosToGeog.transform(p, p);
                    }
                });
        expectProjectionException(
                new Testable() {
                    public void test() throws Exception {
                        p.setLocation(circumscribed.getMinX(), circumscribed.getMinY());
                        sphericalGeosToGeog.transform(p, p);
                    }
                });
    }

    @Test
    public void testCircumscribeFullDisk_Ellipsoidal() throws TransformException, FactoryException {

        final Envelope2D circumscribed =
                GeostationarySatellite.circumscribeFullDisk(ellipsoidalGeosCRS);
        assertThat(circumscribed, is(notNullValue()));

        final DirectPosition2D p = new DirectPosition2D();

        p.setLocation(circumscribed.getCenterX(), circumscribed.getMaxY());
        ellipsoidalGeosToGeog.transform(p, p);
        assertThat(p, is(notNullValue()));
        geogToEllipsoidalGeos.transform(p, p);
        assertThat(p, is(notNullValue()));

        p.setLocation(circumscribed.getCenterX(), circumscribed.getMinY());
        ellipsoidalGeosToGeog.transform(p, p);
        assertThat(p, is(notNullValue()));
        geogToEllipsoidalGeos.transform(p, p);
        assertThat(p, is(notNullValue()));

        p.setLocation(circumscribed.getMaxX(), circumscribed.getCenterY());
        ellipsoidalGeosToGeog.transform(p, p);
        assertThat(p, is(notNullValue()));
        geogToEllipsoidalGeos.transform(p, p);
        assertThat(p, is(notNullValue()));

        p.setLocation(circumscribed.getMinX(), circumscribed.getCenterY());
        ellipsoidalGeosToGeog.transform(p, p);
        assertThat(p, is(notNullValue()));
        geogToEllipsoidalGeos.transform(p, p);
        assertThat(p, is(notNullValue()));

        // show that bounds of rectangle circumscribing full disk image is not
        // transformable stepping 1 meter outside the X and Y extents along the
        // orthogonal center axes
        final double tickle = 1;
        expectProjectionException(
                new Testable() {
                    public void test() throws Exception {
                        p.setLocation(circumscribed.getCenterX(), circumscribed.getMaxY() + tickle);
                        ellipsoidalGeosToGeog.transform(p, p);
                    }
                });
        expectProjectionException(
                new Testable() {
                    public void test() throws Exception {
                        p.setLocation(circumscribed.getCenterX(), circumscribed.getMinY() - tickle);
                        ellipsoidalGeosToGeog.transform(p, p);
                    }
                });
        expectProjectionException(
                new Testable() {
                    public void test() throws Exception {
                        p.setLocation(circumscribed.getMinX() - tickle, circumscribed.getCenterY());
                        ellipsoidalGeosToGeog.transform(p, p);
                    }
                });
        expectProjectionException(
                new Testable() {
                    public void test() throws Exception {
                        p.setLocation(circumscribed.getMaxX() + tickle, circumscribed.getCenterY());
                        ellipsoidalGeosToGeog.transform(p, p);
                    }
                });

        // show that bounds of rectangle circumscribing full disk image is not transformable
        expectProjectionException(
                new Testable() {
                    public void test() throws Exception {
                        p.setLocation(circumscribed.getMaxX(), circumscribed.getMaxY());
                        ellipsoidalGeosToGeog.transform(p, p);
                    }
                });
        expectProjectionException(
                new Testable() {
                    public void test() throws Exception {
                        p.setLocation(circumscribed.getMaxX(), circumscribed.getMinY());
                        ellipsoidalGeosToGeog.transform(p, p);
                    }
                });
        expectProjectionException(
                new Testable() {
                    public void test() throws Exception {
                        p.setLocation(circumscribed.getMinX(), circumscribed.getMaxY());
                        ellipsoidalGeosToGeog.transform(p, p);
                    }
                });
        expectProjectionException(
                new Testable() {
                    public void test() throws Exception {
                        p.setLocation(circumscribed.getMinX(), circumscribed.getMinY());
                        ellipsoidalGeosToGeog.transform(p, p);
                    }
                });
    }

    @Test
    public void testInscribeFullDiskEstimate_Spheroidal()
            throws TransformException, FactoryException {

        final Envelope2D inscribed =
                GeostationarySatellite.inscribeFullDiskEstimate(sphericalGeosCRS);
        assertThat(inscribed, is(notNullValue()));

        final DirectPosition2D p = new DirectPosition2D();

        p.setLocation(inscribed.getMaxX(), inscribed.getMaxY());
        sphericalGeosToGeog.transform(p, p);
        assertThat(p, is(notNullValue()));
        geogToSphericalGeos.transform(p, p);
        assertThat(p, is(notNullValue()));

        p.setLocation(inscribed.getMaxX(), inscribed.getMinY());
        sphericalGeosToGeog.transform(p, p);
        assertThat(p, is(notNullValue()));
        geogToSphericalGeos.transform(p, p);
        assertThat(p, is(notNullValue()));

        p.setLocation(inscribed.getMinX(), inscribed.getMaxY());
        sphericalGeosToGeog.transform(p, p);
        assertThat(p, is(notNullValue()));
        geogToSphericalGeos.transform(p, p);
        assertThat(p, is(notNullValue()));

        p.setLocation(inscribed.getMinX(), inscribed.getMinY());
        sphericalGeosToGeog.transform(p, p);
        assertThat(p, is(notNullValue()));
        geogToSphericalGeos.transform(p, p);
        assertThat(p, is(notNullValue()));

        // Inscribed rectangle is smaller than largest inscribing rectangle, hence ESTIMATE
        //        final double tickle = 1;
        //        expectProjectionException(new Testable() { public void test() throws Exception {
        //            p.setLocation(inscribed.getMaxX() + tickle, inscribed.getMaxY() + tickle);
        //            sphericalGeosToGeog.transform(p, p);
        //        }});
        //        expectProjectionException(new Testable() { public void test() throws Exception {
        //            p.setLocation(inscribed.getMaxX() + tickle, inscribed.getMinY() - tickle);
        //            sphericalGeosToGeog.transform(p, p);
        //        }});
        //        expectProjectionException(new Testable() { public void test() throws Exception {
        //            p.setLocation(inscribed.getMinX() - tickle, inscribed.getMaxY() + tickle);
        //            sphericalGeosToGeog.transform(p, p);
        //        }});
        //        expectProjectionException(new Testable() { public void test() throws Exception {
        //            p.setLocation(inscribed.getMinX() - tickle, inscribed.getMinY() - tickle);
        //            sphericalGeosToGeog.transform(p, p);
        //        }});
    }

    @Test
    public void testInscribeFullDiskEstimate_Ellipsoidal()
            throws TransformException, FactoryException {

        final Envelope2D inscribed =
                GeostationarySatellite.inscribeFullDiskEstimate(ellipsoidalGeosCRS);
        assertThat(inscribed, is(notNullValue()));

        final DirectPosition2D p = new DirectPosition2D();

        p.setLocation(inscribed.getMaxX(), inscribed.getMaxY());
        ellipsoidalGeosToGeog.transform(p, p);
        assertThat(p, is(notNullValue()));
        geogToEllipsoidalGeos.transform(p, p);
        assertThat(p, is(notNullValue()));

        p.setLocation(inscribed.getMaxX(), inscribed.getMinY());
        ellipsoidalGeosToGeog.transform(p, p);
        assertThat(p, is(notNullValue()));
        geogToEllipsoidalGeos.transform(p, p);
        assertThat(p, is(notNullValue()));

        p.setLocation(inscribed.getMinX(), inscribed.getMaxY());
        ellipsoidalGeosToGeog.transform(p, p);
        assertThat(p, is(notNullValue()));
        geogToEllipsoidalGeos.transform(p, p);
        assertThat(p, is(notNullValue()));

        p.setLocation(inscribed.getMinX(), inscribed.getMinY());
        ellipsoidalGeosToGeog.transform(p, p);
        assertThat(p, is(notNullValue()));
        geogToEllipsoidalGeos.transform(p, p);
        assertThat(p, is(notNullValue()));

        // Inscribed rectangle is smaller than largest inscribing rectangle, hence ESTIMATE
        //        final double tickle = 1;
        //        expectProjectionException(new Testable() { public void test() throws Exception {
        //            p.setLocation(inscribed.getMaxX() + tickle, inscribed.getMaxY() + tickle);
        //            ellipsoidalGeosToGeog.transform(p, p);
        //        }});
        //        expectProjectionException(new Testable() { public void test() throws Exception {
        //            p.setLocation(inscribed.getMaxX() + tickle, inscribed.getMinY() - tickle);
        //            ellipsoidalGeosToGeog.transform(p, p);
        //        }});
        //        expectProjectionException(new Testable() { public void test() throws Exception {
        //            p.setLocation(inscribed.getMinX() - tickle, inscribed.getMaxY() + tickle);
        //            ellipsoidalGeosToGeog.transform(p, p);
        //        }});
        //        expectProjectionException(new Testable() { public void test() throws Exception {
        //            p.setLocation(inscribed.getMinX() - tickle, inscribed.getMinY() - tickle);
        //            ellipsoidalGeosToGeog.transform(p, p);
        //        }});
    }

    private void expectProjectionException(Testable testable) {
        expectException(ProjectionException.class, testable);
    }

    private <T extends Exception> void expectException(Class<T> clazz, Testable testable) {
        try {
            testable.test();
            fail(String.format("Expected exception, %s, but not thrown", clazz));
        } catch (Exception e) {
            if (!clazz.isInstance(e)) {
                fail(String.format("Expected exception of %s but got %s", clazz, e.getClass()));
            }
        }
    }

    public interface Testable {
        void test() throws Exception;
    }
}
