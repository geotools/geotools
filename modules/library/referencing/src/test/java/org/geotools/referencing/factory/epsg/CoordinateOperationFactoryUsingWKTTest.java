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
package org.geotools.referencing.factory.epsg;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.Properties;
import java.util.Set;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.operation.CoordinateOperation;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.CRS;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.geotools.util.factory.AbstractFactory;
import org.geotools.util.factory.Hints;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the {@link testCoordinateOperationFactoryUsingWKT} public methods.
 *
 * @author Oscar Fonts
 */
public class CoordinateOperationFactoryUsingWKTTest {

    CoordinateOperationFactoryUsingWKT factory;

    private static final String DEFINITIONS_FILE_NAME = "epsg_operations.properties";
    private static Properties properties;

    private static final String SOURCE_CRS = "EPSG:TEST1";
    private static final String TARGET_CRS = "EPSG:TEST2";
    private static final String CRS_PAIR = SOURCE_CRS + "," + TARGET_CRS;
    private static final String INVERSE_CRS_PAIR = TARGET_CRS + "," + SOURCE_CRS;
    private static final String INVALID_CRS = "nonexistent";

    private static final double[] SRC_TEST_POINT = {3.084896111, 39.592654167};
    private static final double[] DST_TEST_POINT = {3.0844689951999427, 39.594235744481225};

    /** @throws java.lang.Exception */
    @Before
    public void setUp() throws Exception {
        ReferencingFactoryFinder.addAuthorityFactory(new FactoryUsingWKT(null, AbstractFactory.MAXIMUM_PRIORITY));

        factory = (CoordinateOperationFactoryUsingWKT) ReferencingFactoryFinder.getCoordinateOperationAuthorityFactory(
                "EPSG",
                new Hints(Hints.COORDINATE_OPERATION_AUTHORITY_FACTORY, CoordinateOperationFactoryUsingWKT.class));

        // Read definitions
        properties = new Properties();
        properties.load(this.getClass().getResourceAsStream(DEFINITIONS_FILE_NAME));
    }

    /** @throws Exception */
    @Test
    public void testExtraDirectoryHint() throws Exception {
        Hints hints = new Hints(Hints.COORDINATE_OPERATION_AUTHORITY_FACTORY, CoordinateOperationFactoryUsingWKT.class);
        try {
            hints.put(Hints.CRS_AUTHORITY_EXTRA_DIRECTORY, "invalid");
            fail("Should of been tossed out as an invalid hint");
        } catch (IllegalArgumentException expected) {
            // This is the expected exception.
        }
        String directory = new File(".").getAbsolutePath();
        hints = new Hints(Hints.COORDINATE_OPERATION_AUTHORITY_FACTORY, CoordinateOperationFactoryUsingWKT.class);
        hints.put(Hints.CRS_AUTHORITY_EXTRA_DIRECTORY, directory);

        CoordinateOperationFactoryUsingWKT fact =
                (CoordinateOperationFactoryUsingWKT) ReferencingFactoryFinder.getCoordinateOperationAuthorityFactory(
                        "EPSG",
                        new Hints(
                                Hints.COORDINATE_OPERATION_AUTHORITY_FACTORY,
                                CoordinateOperationFactoryUsingWKT.class));

        // BTW testing the inverse construction
        CoordinateOperation co = fact.createCoordinateOperation(INVERSE_CRS_PAIR);
        CoordinateReferenceSystem crs = CRS.decode(TARGET_CRS);
        assertSame(crs, co.getSourceCRS());
        crs = CRS.decode(SOURCE_CRS);
        assertSame(crs, co.getTargetCRS());

        assertNotNull(co.getMathTransform());
        double[] p = new double[2];
        co.getMathTransform().transform(DST_TEST_POINT, 0, p, 0, 1);
        assertEquals(p[0], SRC_TEST_POINT[0], 1e-8);
        assertEquals(p[1], SRC_TEST_POINT[1], 1e-8);
    }

    /** Test method for {@link CoordinateOperationFactoryUsingWKT#getAuthority}. */
    @Test
    public void testGetAuthority() {
        assertEquals(factory.getAuthority(), Citations.EPSG);
    }

    /** Test method for {@link CoordinateOperationFactoryUsingWKT#createCoordinateOperation}. */
    @Test
    @SuppressWarnings("CatchFail")
    public void testCreateCoordinateOperation() throws TransformException {

        try {
            assertNull(factory.createCoordinateOperation(INVALID_CRS));
        } catch (FactoryException e) {
            fail(factory.getClass().getSimpleName()
                    + " threw a FactoryException when requesting"
                    + "a nonexistent operation. Instead, a NoSuchAuthorityCodeException was expected.");
        }

        try {
            // Test CoordinateOperation
            CoordinateOperation co = factory.createCoordinateOperation(CRS_PAIR);
            assertNotNull(co);

            // Test CRSs
            CoordinateReferenceSystem crs = CRS.decode(SOURCE_CRS);
            assertSame(crs, co.getSourceCRS());
            crs = CRS.decode(TARGET_CRS);
            assertSame(crs, co.getTargetCRS());

            // Test MathTransform
            assertNotNull(co.getMathTransform());
            double[] p = new double[2];
            co.getMathTransform().transform(SRC_TEST_POINT, 0, p, 0, 1);
            assertEquals(p[0], DST_TEST_POINT[0], 1e-8);
            assertEquals(p[1], DST_TEST_POINT[1], 1e-8);
        } catch (FactoryException e) {
            fail(factory.getClass().getSimpleName()
                    + " threw a FactoryException when creating"
                    + " coordinate operation from an existing code.");
        }
    }

    /** Test method for {@link CoordinateOperationFactoryUsingWKT#createFromCoordinateReferenceSystemCodes}. */
    @Test
    @SuppressWarnings("CatchFail")
    public void testCreateFromCoordinateReferenceSystemCodes() throws TransformException {
        try {
            Set<CoordinateOperation> cos = factory.createFromCoordinateReferenceSystemCodes(INVALID_CRS, INVALID_CRS);
            assertTrue(cos.isEmpty());
        } catch (FactoryException e) {
            fail(factory.getClass().getSimpleName()
                    + " threw a FactoryException when requesting"
                    + "a nonexistent operation. Instead, a NoSuchAuthorityCodeException was expected.");
        }

        try {
            // Test CoordinateOperation
            Set<CoordinateOperation> cos = factory.createFromCoordinateReferenceSystemCodes(SOURCE_CRS, TARGET_CRS);
            assertEquals(1, cos.size());
            CoordinateOperation co = cos.iterator().next();
            assertNotNull(co);

            // Test CRSs
            CoordinateReferenceSystem crs = CRS.decode(SOURCE_CRS);
            assertSame(crs, co.getSourceCRS());
            crs = CRS.decode(TARGET_CRS);
            assertSame(crs, co.getTargetCRS());

            // Test MathTransform
            assertNotNull(co.getMathTransform());
            double[] p = new double[2];
            co.getMathTransform().transform(SRC_TEST_POINT, 0, p, 0, 1);
            assertEquals(p[0], DST_TEST_POINT[0], 1e-8);
            assertEquals(p[1], DST_TEST_POINT[1], 1e-8);
        } catch (FactoryException e) {
            fail(factory.getClass().getSimpleName()
                    + " threw a FactoryException when creating"
                    + " coordinate operation from an existing code.");
        }
    }
}
