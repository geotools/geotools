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
 * Unit tests for {@link NTv2Transform} public methods
 *
 * @author Oscar Fonts
 */
public class NTv2TransformTest {

    private static final String TEST_GRID = "BALR2009.gsb";
    private static final String INEXISTENT_GRID = "this_NTv2_grid_does_not_exist";
    private static final double[] TEST_POINT_SRC = {3.084896111, 39.592654167};
    private static final double[] TEST_POINT_DST = {3.083801819, 39.5914804};
    private static final double TOLERANCE = 18E-7;

    private NTv2Transform transform;

    /**
     * Instantiates the test {@link NTv2Transform}
     *
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        transform = new NTv2Transform(new URI(TEST_GRID));
    }

    /**
     * Test method for {@link
     * org.geotools.referencing.operation.transform.NTv2Transform#getSourceDimensions()}.
     */
    @Test
    public void testGetSourceDimensions() {
        assertEquals(transform.getSourceDimensions(), 2);
    }

    /**
     * Test method for {@link
     * org.geotools.referencing.operation.transform.NTv2Transform#getTargetDimensions()}.
     */
    @Test
    public void testGetTargetDimensions() {
        assertEquals(transform.getTargetDimensions(), 2);
    }

    /**
     * Test method for {@link
     * org.geotools.referencing.operation.transform.NTv2Transform#getParameterValues()}.
     */
    @Test
    public void testGetParameterValues() {
        ParameterValueGroup pvg = transform.getParameterValues();

        // Descriptor is the same as provider's
        assertEquals(pvg.getDescriptor(), NTv2Transform.Provider.PARAMETERS);

        // One single value
        assertEquals(pvg.values().size(), 1);

        // Value accessible through its identifiers
        Object value = pvg.parameter("8656").getValue();
        assertTrue(value instanceof URI);
        assertEquals(value.toString(), TEST_GRID);

        value = pvg.parameter("Latitude and longitude difference file").getValue();
        assertTrue(value instanceof URI);
        assertEquals(value.toString(), TEST_GRID);
    }

    /**
     * Test method for {@link
     * org.geotools.referencing.operation.transform.NTv2Transform#NTv2Transform(java.net.URI)}.
     */
    @Test
    public void testNTv2Transform() throws Exception {

        try {
            new NTv2Transform(null);
        } catch (NoSuchIdentifierException e) {
            assert true;
        }

        try {
            new NTv2Transform(new URI(INEXISTENT_GRID));
        } catch (NoSuchIdentifierException e) {
            return;
        }

        try {
            new NTv2Transform(new URI(INEXISTENT_GRID));
        } catch (NoSuchIdentifierException e) {
            return;
        }
    }

    /**
     * Test method for {@link org.geotools.referencing.operation.transform.NTv2Transform#inverse()}.
     */
    @Test
    public void testInverse() throws TransformException {
        assertSame(transform, transform.inverse().inverse());

        double[] p1 = new double[2];
        double[] p2 = new double[2];

        transform.inverse().transform(TEST_POINT_SRC, 0, p1, 0, 1);
        transform.inverseTransform(TEST_POINT_SRC, 0, p2, 0, 1);
        assertEquals(p1[0], p2[0], TOLERANCE);
        assertEquals(p1[1], p2[1], TOLERANCE);
    }

    /**
     * Test method for {@link
     * org.geotools.referencing.operation.transform.NTv2Transform#transform(double[], int, double[],
     * int, int)}.
     */
    @Test
    public void testTransform() throws TransformException {
        double[] p = new double[2];
        transform.transform(TEST_POINT_SRC, 0, p, 0, 1);
        assertEquals(p[0], TEST_POINT_DST[0], TOLERANCE);
        assertEquals(p[1], TEST_POINT_DST[1], TOLERANCE);
    }

    @Test
    public void testTransformTranslated() throws TransformException {
        double[] src = new double[4];
        double[] dst = new double[4];
        src[2] = TEST_POINT_SRC[0];
        src[3] = TEST_POINT_SRC[1];

        transform.transform(src, 2, dst, 2, 1);
        assertEquals(0, dst[0], TOLERANCE);
        assertEquals(0, dst[1], TOLERANCE);
        assertEquals(dst[2], TEST_POINT_DST[0], TOLERANCE);
        assertEquals(dst[3], TEST_POINT_DST[1], TOLERANCE);
    }

    /**
     * Test method for {@link
     * org.geotools.referencing.operation.transform.NTv2Transform#inverseTransform(double[], int,
     * double[], int, int)}.
     */
    @Test
    public void testInverseTransform() throws TransformException {
        double[] p = new double[2];
        transform.inverseTransform(TEST_POINT_DST, 0, p, 0, 1);
        assertEquals(p[0], TEST_POINT_SRC[0], TOLERANCE);
        assertEquals(p[1], TEST_POINT_SRC[1], TOLERANCE);
    }

    @Test
    public void testInverseTransformTranslated() throws TransformException {
        double[] src = new double[4];
        double[] dst = new double[4];
        src[2] = TEST_POINT_DST[0];
        src[3] = TEST_POINT_DST[1];

        transform.inverseTransform(src, 2, dst, 2, 1);
        assertEquals(0, dst[0], TOLERANCE);
        assertEquals(0, dst[1], TOLERANCE);
        assertEquals(dst[2], TEST_POINT_SRC[0], TOLERANCE);
        assertEquals(dst[3], TEST_POINT_SRC[1], TOLERANCE);
    }

    @Test
    public void testHashCodeEquals() throws Exception {
        NTv2Transform t2 = new NTv2Transform(new URI(TEST_GRID));
        assertEquals(transform, t2);
        assertEquals(transform.hashCode(), t2.hashCode());
    }
}
