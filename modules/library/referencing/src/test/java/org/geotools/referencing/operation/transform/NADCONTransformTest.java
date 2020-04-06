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
package org.geotools.referencing.operation.transform;

import static org.junit.Assert.*;

import java.net.URI;
import org.junit.Before;
import org.junit.Test;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.NoSuchIdentifierException;
import org.opengis.referencing.operation.TransformException;

/**
 * Unit tests for {@link NADCONTransform} public methods
 *
 * @author Andrea Aime - GeoSolutions
 */
public class NADCONTransformTest {

    private static final String STPAUL_LAS = "stpaul.las";
    private static final String STPAUL_LOS = "stpaul.los";
    private static final double TOLERANCE = 18E-7;

    private static final double[] TEST_POINT_SRC = {-170.25, 57.125};
    private static final double[] TEST_POINT_DST = {-170.252283, 57.125655};

    private NADCONTransform transform;

    /**
     * Instantiates the test {@link NADCONTransform}
     *
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        transform = new NADCONTransform(new URI(STPAUL_LAS), new URI(STPAUL_LOS));
    }

    /**
     * Test method for {@link
     * org.geotools.referencing.operation.transform.NADCONTransform#getSourceDimensions()}.
     */
    @Test
    public void testGetSourceDimensions() {
        assertEquals(transform.getSourceDimensions(), 2);
    }

    /**
     * Test method for {@link
     * org.geotools.referencing.operation.transform.NADCONTransform#getTargetDimensions()}.
     */
    @Test
    public void testGetTargetDimensions() {
        assertEquals(transform.getTargetDimensions(), 2);
    }

    /**
     * Test method for {@link
     * org.geotools.referencing.operation.transform.NADCONTransform#getParameterValues()}.
     */
    @Test
    public void testGetParameterValues() {
        ParameterValueGroup pvg = transform.getParameterValues();

        // Descriptor is the same as provider's
        assertEquals(pvg.getDescriptor(), NADCONTransform.Provider.PARAMETERS);

        // One single value
        assertEquals(pvg.values().size(), 2);

        Object value = pvg.parameter("Latitude difference file").getValue();
        assertTrue(value instanceof URI);
        assertEquals(value.toString(), STPAUL_LAS);

        value = pvg.parameter("Longitude difference file").getValue();
        assertTrue(value instanceof URI);
        assertEquals(value.toString(), STPAUL_LOS);
    }

    /**
     * Test method for {@link
     * org.geotools.referencing.operation.transform.NADCONTransform#NADCONTransform(java.net.URI)}.
     */
    @Test
    public void testNADCONTransform() throws Exception {

        try {
            new NADCONTransform(null, null);
        } catch (NoSuchIdentifierException e) {
            assert true;
        }
    }

    /**
     * Test method for {@link
     * org.geotools.referencing.operation.transform.NADCONTransform#inverse()}.
     */
    @Test
    public void testInverse() throws TransformException {
        assertSame(transform, transform.inverse().inverse());

        double[] p1 = new double[2];
        double[] p2 = new double[2];

        transform.inverse().transform(TEST_POINT_DST, 0, p1, 0, 1);
        transform.inverseTransform(TEST_POINT_DST, 0, p2, 0, 1);
        assertEquals(p1[0], p2[0], TOLERANCE);
        assertEquals(p1[1], p2[1], TOLERANCE);
    }

    /**
     * Test method for {@link
     * org.geotools.referencing.operation.transform.NADCONTransform#transform(double[], int,
     * double[], int, int)}.
     */
    @Test
    public void testTransform() throws TransformException {
        double[] p = new double[2];
        transform.transform(TEST_POINT_SRC, 0, p, 0, 1);
        assertEquals(p[0], TEST_POINT_DST[0], TOLERANCE);
        assertEquals(p[1], TEST_POINT_DST[1], TOLERANCE);
    }

    /**
     * Test method for {@link
     * org.geotools.referencing.operation.transform.NADCONTransform#inverseTransform(double[], int,
     * double[], int, int)}.
     */
    @Test
    public void testInverseTransform() throws TransformException {
        double[] p = new double[2];
        transform.inverseTransform(TEST_POINT_DST, 0, p, 0, 1);
        assertEquals(p[0], TEST_POINT_SRC[0], TOLERANCE);
        assertEquals(p[1], TEST_POINT_SRC[1], TOLERANCE);
    }
}
