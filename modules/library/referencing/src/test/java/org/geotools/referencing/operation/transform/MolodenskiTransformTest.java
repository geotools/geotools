/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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

import org.geotools.referencing.operation.TransformTestBase;

import org.junit.*;
import static org.junit.Assert.*;


/**
 * Tests the {@link MolodenskiTransform} class.
 *
 * @author Tara Athan
 *
 *
 * @source $URL$
 */
public final class MolodenskiTransformTest extends TransformTestBase {
    /**
     * An array with a variety of test points
     */
    private float[] srcFloat2 = {
           0.0f,      0.0f,
           0.0f,     89.999f,
           0.0f,    -89.999f,
         179.999f,    0.0f,
        -179.999f,    0.0f,
           0.0f,      0.0f,
        -123.19641f, 39.26859f
    };

    /**
     * The molodenski transform to use for testing.
     */
    private MolodenskiTransform molodenski00;

    /**
     * Sets up common objects used for all tests. Source ellipsoid is WGS84.
     * Target ellipsoid is the same (that is, we are testing an identity transform).
     */
    @Before
    public void setUp() {
        double a  = 6378137.0;
        double b  = 6356752.0;
        molodenski00 = new MolodenskiTransform(false, a, b, false, a, b, false, 0.0, 0.0, 0.0);
    }

    /**
     * Tests overwriting the source array, with a target offset slightyly greater than
     * the source offset.
     */
    @Test
    public void testArrayOverwrite() {
        int srcOff = 0;
        int dstOff = 2;
        int numPts = 2;
        float[] overWriteTestArray = srcFloat2.clone();
        molodenski00.transform(overWriteTestArray, srcOff, overWriteTestArray, dstOff, numPts);

        int dim = 2;
        for (int i=0; i<numPts; i++) {
            assertEquals(srcFloat2[srcOff+dim*i  ], overWriteTestArray[dstOff+dim*i  ], 1E-6);
            assertEquals(srcFloat2[srcOff+dim*i+1], overWriteTestArray[dstOff+dim*i+1], 1E-6);
        }
    }
}
