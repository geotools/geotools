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

import org.geotools.referencing.CRS;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.geotools.referencing.factory.epsg.CoordinateOperationFactoryUsingWKT;
import org.geotools.referencing.factory.epsg.FactoryUsingWKT;
import org.geotools.util.factory.AbstractFactory;
import org.geotools.util.factory.Hints;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.ConcatenatedOperation;
import org.opengis.referencing.operation.CoordinateOperation;
import org.opengis.referencing.operation.MathTransform;

/**
 * Makes sure the {@link testCoordinateOperationFactoryUsingWKT} is not ignored when referencing is
 * setup in "Longitude first" mode.
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
    public static void setUp() throws Exception {
        // force longitude first mode
        System.setProperty("org.geotools.referencing.forceXY", "true");

        CRS.reset("all");

        ReferencingFactoryFinder.addAuthorityFactory(
                new FactoryUsingWKT(null, AbstractFactory.MAXIMUM_PRIORITY));

        ReferencingFactoryFinder.addAuthorityFactory(
                new CoordinateOperationFactoryUsingWKT(null, AbstractFactory.MAXIMUM_PRIORITY));
    }

    @AfterClass
    public static void tearDown() {
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
        CoordinateOperation co =
                CRS.getCoordinateOperationFactory(true).createOperation(source, target);
        ConcatenatedOperation cco = (ConcatenatedOperation) co;
        // the EPSG one only has two steps, the non EPSG one 4
        assertEquals(2, cco.getOperations().size());
    }
}
