/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2012, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.factory.epsg.hsql;

import static org.junit.Assert.assertEquals;

import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.operation.ConcatenatedOperation;
import org.geotools.api.referencing.operation.CoordinateOperation;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.referencing.CRS;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.factory.epsg.CoordinateOperationFactoryUsingWKT;
import org.geotools.referencing.factory.epsg.FactoryUsingWKT;
import org.geotools.util.factory.AbstractFactory;
import org.geotools.util.factory.Hints;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Makes sure the {@link testCoordinateOperationFactoryUsingWKT} is not ignored when referencing is setup in "Longitude
 * first" mode.
 *
 * @author Oscar Fonts
 * @author Andrea Aime
 */
public class LongitudeFirstFactoryOverrideTest {

    private static final String SOURCE_CRS = "EPSG:TEST1";
    private static final String TARGET_CRS = "EPSG:TEST2";

    private static final double[] SRC_TEST_POINT = {39.592654167, 3.084896111};
    private static final double[] DST_TEST_POINT = {39.594235744481225, 3.0844689951999427};

    /** @throws java.lang.Exception */
    @BeforeClass
    public static void setUpClass() throws Exception {
        // force longitude first mode
        System.setProperty("org.geotools.referencing.forceXY", "true");

        CRS.reset("all");

        ReferencingFactoryFinder.addAuthorityFactory(new FactoryUsingWKT(null, AbstractFactory.MAXIMUM_PRIORITY));

        ReferencingFactoryFinder.addAuthorityFactory(
                new CoordinateOperationFactoryUsingWKT(null, AbstractFactory.MAXIMUM_PRIORITY));
    }

    @AfterClass
    public static void tearDownClass() {
        // unset axis ordering hint
        System.clearProperty("org.geotools.referencing.forceXY");
        Hints.removeSystemDefault(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER);

        CRS.reset("all");
    }

    /** Test method for {@link CoordinateOperationFactoryUsingWKT#createCoordinateOperation}. */
    @Test
    public void testCreateOperationFromCustomCodes() throws Exception {
        // Test CRSs
        CoordinateReferenceSystem source = CRS.decode(SOURCE_CRS);
        CoordinateReferenceSystem target = CRS.decode(TARGET_CRS);
        MathTransform mt = CRS.findMathTransform(source, target, true);

        // Test MathTransform
        double[] p = new double[2];
        mt.transform(SRC_TEST_POINT, 0, p, 0, 1);
        assertEquals(p[0], DST_TEST_POINT[0], 1e-8);
        assertEquals(p[1], DST_TEST_POINT[1], 1e-8);
    }

    /** Test method for {@link CoordinateOperationFactoryUsingWKT#createCoordinateOperation}. */
    @Test
    public void testOverrideEPSGOperation() throws Exception {
        // Test CRSs
        CoordinateReferenceSystem source = CRS.decode("EPSG:4269");
        CoordinateReferenceSystem target = CRS.decode("EPSG:4326");
        MathTransform mt = CRS.findMathTransform(source, target, true);

        // Test MathTransform
        double[] p = new double[2];
        mt.transform(SRC_TEST_POINT, 0, p, 0, 1);
        assertEquals(p[0], DST_TEST_POINT[0], 1e-8);
        assertEquals(p[1], DST_TEST_POINT[1], 1e-8);
    }

    /** Check we are actually using the EPSG database for anything not in override */
    @Test
    public void testFallbackOnEPSGDatabase() throws Exception {
        // Test CRSs
        CoordinateReferenceSystem source = CRS.decode("EPSG:3003");
        CoordinateReferenceSystem target = CRS.decode("EPSG:4326");
        CoordinateOperation co = CRS.getCoordinateOperationFactory(true).createOperation(source, target);
        ConcatenatedOperation cco = (ConcatenatedOperation) co;
        // the EPSG one only has two steps, the non EPSG one 4
        assertEquals(2, cco.getOperations().size());
    }

    @Test
    public void testCreateFromCRSCodesWGS84() throws FactoryException {
        CoordinateReferenceSystem crs5681 = CRS.decode("EPSG:5681");
        // Getting a transformation from a EPSG code to the WGS84 constant, should still
        // pick up the transformation override
        CoordinateOperation operation =
                CRS.getCoordinateOperationFactory(true).createOperation(crs5681, DefaultGeographicCRS.WGS84);

        String expected =
                """
                CONCAT_MT[PARAM_MT["Ellipsoid_To_Geocentric",\s
                    PARAMETER["dim", 2],\s
                    PARAMETER["semi_major", 6377397.155],\s
                    PARAMETER["semi_minor", 6356078.962818189]],\s
                  PARAM_MT["Coordinate Frame Rotation (geog2D domain)",\s
                    PARAMETER["dx", 584.9636],\s
                    PARAMETER["dy", 107.7175],\s
                    PARAMETER["dz", 413.8067],\s
                    PARAMETER["ex", -1.1155],\s
                    PARAMETER["ey", -0.2824],\s
                    PARAMETER["ez", 3.1384],\s
                    PARAMETER["ppm", 7.99220000002876]],\s
                  PARAM_MT["Geocentric_To_Ellipsoid",\s
                    PARAMETER["dim", 2],\s
                    PARAMETER["semi_major", 6378137.0],\s
                    PARAMETER["semi_minor", 6356752.314245179]]]""";
        assertEquals(normalize(expected), normalize(operation.getMathTransform().toWKT()));
    }

    /**
     * Replaces all whitespace (including newlines) with a single space. Avoids issues with platform dependent newlines
     * in WKT representations
     */
    private String normalize(String s) {
        return s.replaceAll("\\s+", " ");
    }
}
