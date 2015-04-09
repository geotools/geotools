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

/**
 * Tests {@link org.geotools.referencing.operation.matrix.GeneralMatrix}.
 *
 *
 *
 * @source $URL$
 * @version $Id$
 * @author James Hughes
 */
package org.geotools.referencing.operation.matrix;

import org.junit.Test;

import java.awt.geom.AffineTransform;

import static org.junit.Assert.*;

public class GeneralMatrixTest {

    private static double EPSILON_TOLERANCE = 0.000001;

    private static double[][] zero2 = new double[][] {
            {0.0, 0.0},
            {0.0, 0.0}
    };

    private static double[][] id2 = new double[][] {
            {1.0, 0.0},
            {0.0, 1.0}
    };

    private static double[][] id4 = new double[][]{
            {1.0, 0.0, 0.0, 0.0},
            {0.0, 1.0, 0.0, 0.0},
            {0.0, 0.0, 1.0, 0.0},
            {0.0, 0.0, 0.0, 1.0}
    };

    private static double[][] id23 = new double[][] {
            {1.0, 0.0, 0.0},
            {0.0, 1.0, 0.0}
    };

    private static double[][] id32 = new double[][] {
            {1.0, 0.0},
            {0.0, 1.0},
            {0.0, 0.0}
    };

    private static double[][] array1 = new double[][] {
            {1.2, -3.4},
            {-5.6, 7.8},
            {9.0, -1.0}
    };

    private static double[][] negativeArray1 = new double[][] {
            {-1.2, 3.4},
            {5.6, -7.8},
            {-9.0, 1.0}
    };

    private static double[] array1flatten = new double[] {
            1.2, -3.4, -5.6, 7.8, 9.0, -1.0
    };

    private static AffineTransform affineTransform = new AffineTransform(1.2, 3.4, 5.6, 7.8, 9.0, 1.0);

    private static double[][] affineMatrix = new double[][] {
            {1.2, 5.6, 9.0},
            {3.4, 7.8, 1.0},
            {0.0, 0.0, 1.0}
    };

    private static double[][] matrix33 = new double[][] {
            {1.2, 5.6, 9.0},
            {3.4, 7.8, 1.0},
            {-2.3, 4.6, 1.0}
    };

    private static double[][] sub32 = new double[][] {
            {1.2, 5.6},
            {3.4, 7.8},
            {-2.3, 4.6}
    };

    private static double[][] sub22 = new double[][] {
            {1.2, 5.6},
            {3.4, 7.8}
    };

    private static double [][] arrayA = new double[][] {
            {2, 6},
            {4, 7}
    };

    private static double [][] arrayAInverse = new double[][] {
            {-0.7, 0.6},
            {0.4, -0.2}
    };

    private static GeneralMatrix generalAffineMatrix = new GeneralMatrix(affineMatrix);
    private static GeneralMatrix matrix1 = new GeneralMatrix(array1);

    AffineTransform2D affineTransform2D = new AffineTransform2D(affineTransform);

    @Test
    public void constructorTests() {
        GeneralMatrix squareId2 = new GeneralMatrix(2);
        double[][] array2 = squareId2.getElements();
        assertArrayEquals(array2, id2);

        GeneralMatrix squareId = new GeneralMatrix(4);
        double[][] array = squareId.getElements();
        assertArrayEquals(array, id4);

        GeneralMatrix squareId23 = new GeneralMatrix(2, 3);
        double[][] array23 = squareId23.getElements();
        assertArrayEquals(array23, id23);


        GeneralMatrix squareId32 = new GeneralMatrix(3, 2);
        double[][] array32 = squareId32.getElements();
        assertArrayEquals(array32, id32);

        GeneralMatrix matrix1 = new GeneralMatrix(3, 2, array1flatten);
        double[][] matrix1array = matrix1.getElements();
        assertArrayEquals(array1, matrix1array);

        GeneralMatrix matrix2 = new GeneralMatrix(array1);
        double [][] matrix2array = matrix2.getElements();
        assertArrayEquals(array1, matrix2array);

        Matrix2 matrix2zero = new Matrix2(0, 0, 0, 0);
        GeneralMatrix gm2zero = new GeneralMatrix(matrix2zero);
        assertArrayEquals(gm2zero.getElements(), zero2);

        matrix2zero.setIdentity();
        GeneralMatrix gm2id = new GeneralMatrix(matrix2zero);
        assertArrayEquals(gm2id.getElements(), id2);

        GeneralMatrix gmsid2 = new GeneralMatrix(squareId2);
        double [][] gmsid2array = gmsid2.getElements();
        assertArrayEquals(gmsid2array, id2);

        GeneralMatrix affineGeneralMatrix = new GeneralMatrix(affineTransform);
        double [][] affineMatrixarray = affineGeneralMatrix.getElements();
        assertArrayEquals(affineMatrixarray, affineMatrix);


        // TODO: Test for  public GeneralMatrix(final Envelope srcRegion, final Envelope dstRegion)
        // TODO: Test for  public GeneralMatrix(final AxisDirection[] srcAxis, final AxisDirection[] dstAxis)
        // TODO: Test for  public GeneralMatrix(final Envelope srcRegion, final AxisDirection[] srcAxis, final Envelope dstRegion, final AxisDirection[] dstAxis)
    }

    @Test
    public void getElementsTest() {
        double [][] affineTransformElements = GeneralMatrix.getElements(affineTransform2D);
        assertArrayEquals(affineTransformElements, affineMatrix);

        double [][] generalAffineMatrixElements = GeneralMatrix.getElements(generalAffineMatrix);
        assertArrayEquals(generalAffineMatrixElements, affineMatrix);
    }

    @Test(expected = IllegalStateException.class)
    public void affineTest() {
        assertTrue(generalAffineMatrix.isAffine());
        assertFalse(matrix1.isAffine());

        AffineTransform affineTransform1 = generalAffineMatrix.toAffineTransform2D();

        assertNotNull(affineTransform1);
        //TODO add test for toAffineTransform2d
        matrix1.toAffineTransform2D();
    }

    @Test
    public void negateTest() {
        GeneralMatrix gm = new GeneralMatrix(array1);
        gm.negate();

        assertArrayEquals(gm.getElements(), negativeArray1);

        gm.negate();
        assertArrayEquals(gm.getElements(), array1);
    }

    @Test
    public void invertTest() {
        GeneralMatrix gm = new GeneralMatrix(id4);
        gm.invert();

        GeneralMatrix gm2 = new GeneralMatrix(id4);
        GeneralMatrix.epsilonEquals(gm, gm2, EPSILON_TOLERANCE);

        GeneralMatrix gma = new GeneralMatrix(arrayA);
        gma.invert();

        GeneralMatrix gmaInverse = new GeneralMatrix(arrayAInverse);

        GeneralMatrix.epsilonEquals(gma, gmaInverse, EPSILON_TOLERANCE);

        gma.invert();
        GeneralMatrix gma2 = new GeneralMatrix(arrayA);
        GeneralMatrix.epsilonEquals(gma, gma2, EPSILON_TOLERANCE);
    }

    @Test
    public void sizeTests() {
        GeneralMatrix gm = new GeneralMatrix(id32);

        assertEquals(gm.getNumRow(), 3);
        assertEquals(gm.getNumCol(), 2);
    }

    @Test
    public void getSetElementTest() {
        GeneralMatrix gm = new GeneralMatrix(id2);

        assertEquals(gm.getElement(0, 0), 1.0, EPSILON_TOLERANCE);

        double [] newRow = {10.11, 12.23};

        gm.setRow(0, newRow);
        assertEquals(gm.getElement(0, 0), 10.11, EPSILON_TOLERANCE);

        gm.setElement(0, 0, 12.23);
        assertEquals(gm.getElement(0, 0), 12.23, EPSILON_TOLERANCE);

        gm.setZero();
        assertEquals(gm.getElement(0, 0), 0.0, EPSILON_TOLERANCE);
    }

    @Test
    public void identityTest() {
        GeneralMatrix id = new GeneralMatrix(id4);
        assertTrue(id.isIdentity());

        id.setElement(0, 0, 1.0001);
        assertFalse(id.isIdentity());
        assertTrue(GeneralMatrix.isIdentity(id, 0.01));

        id.setIdentity();
        assertTrue(id.isIdentity());
    }

    @Test
    public void equalsHashcodeTest() {
        GeneralMatrix gm1 = new GeneralMatrix(affineMatrix);
        GeneralMatrix gm2 = new GeneralMatrix(affineMatrix);

        assertTrue(gm1.equals(gm2));

        gm2.setElement(2, 2, gm2.getElement(2, 2) + 0.0001);
        assertTrue(gm1.equals(gm2, 0.001));
        assertTrue(GeneralMatrix.epsilonEquals(gm1, gm2, 0.001));
        assertFalse(gm1.equals(gm2));

        assertEquals(gm1.hashCode(), gm2.hashCode());  // JNH: This is an implementation detail.
    }

    //@Test
    public void loadTest() {
        //TODO test load functions
    }

    //@Test
    public void toStringTest() {
        // TODO test toString
    }

    @Test
    public void copySubMatrixTest() {
        GeneralMatrix big = new GeneralMatrix(matrix33);

        GeneralMatrix m32 = new GeneralMatrix(3, 2);
        GeneralMatrix m22 = new GeneralMatrix(2, 2);
        GeneralMatrix second = new GeneralMatrix(2, 2);

        big.copySubMatrix(0, 0, 3, 2, 0, 0, m32);
        assertArrayEquals(m32.getElements(), sub32);

        m32.copySubMatrix(0, 0, 2, 2, 0, 0, m22);
        assertArrayEquals(m22.getElements(), sub22);

        big.copySubMatrix(0, 0, 2, 2, 0, 0, second);
        assertTrue(m22.equals(second));

        // TODO test getCol/Row?
    }

    //@Test
    public void multiplicationTests() {
        // TODO Test, multiply, mul(GM), mul(GM,GM)
    }

    //@Test
    public void setSizeTest() {

    }

    //@Test
    public void subTests() {

    }

}
