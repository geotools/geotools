package org.geotools.referencing.operation.matrix;

import static org.junit.Assert.*;

import org.junit.Test;

public class MatrixTest {

    @Test
    public void testMatrix1() {
        Matrix1 z = new Matrix1(0);
        XMatrix Z = new GeneralMatrix(z);
        assertEquals(Z.determinate(), z.determinate(), 0);
        assertEquals(Z.isAffine(), z.isAffine());
        assertEquals(Z.isIdentity(), z.isIdentity());

        Matrix1 u = new Matrix1(1);
        XMatrix U = new GeneralMatrix(u);
        assertEquals(U.determinate(), u.determinate(), 0);
        assertEquals(U.isAffine(), u.isAffine());
        assertEquals(U.isIdentity(), u.isIdentity());

        Matrix1 m = new Matrix1(3);
        XMatrix M = new GeneralMatrix(m);
        assertEquals(M.determinate(), m.determinate(), 0);
        assertEquals(M.isAffine(), m.isAffine());
        assertEquals(M.isIdentity(), m.isIdentity());

        Matrix1 a = new Matrix1();
        a.add(2, m);
        assertEquals(5, a.m00, 0);

        a.sub(1, m);
        assertEquals(-2, a.m00, 0);

        a.mul(2, m);
        assertEquals(6, a.m00, 0);
    }

    @Test
    public void testMatrix2() {
        Matrix2 z = new Matrix2(0.0, 0.0, 0.0, 0.0);
        XMatrix Z = new GeneralMatrix(z);
        assertEquals(Z.determinate(), z.determinate(), 0);
        assertEquals(Z.isAffine(), z.isAffine());
        assertEquals(Z.isIdentity(), z.isIdentity());

        Matrix2 u = new Matrix2();
        XMatrix U = new GeneralMatrix(u);
        assertEquals(U.determinate(), u.determinate(), 0);
        assertEquals(U.isAffine(), u.isAffine());
        assertEquals(U.isIdentity(), u.isIdentity());

        Matrix2 m = new Matrix2(1, 2, 3, 4);
        XMatrix M = new GeneralMatrix(m);
        assertEquals(M.determinate(), m.determinate(), 0);
        assertEquals(M.isAffine(), m.isAffine());
        assertEquals(M.isIdentity(), m.isIdentity());

        Matrix2 a = new Matrix2();
        a.add(2, m);
        assertEquals(3, a.m00, 0);

        a.sub(1, m);
        assertEquals(0, a.m00, 0);

        a.mul(2, m);
        assertEquals(2, a.m00, 0);

        double array[] = new double[2];
        m.getColumn(0, array);
        assertArrayEquals(array, new double[] {1, 3}, 0);
        m.getRow(1, array);
        assertArrayEquals(array, new double[] {3, 4}, 0);
    }

    @Test
    public void testMatrix3() {
        Matrix3 z = new Matrix3(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
        XMatrix Z = new GeneralMatrix(z);
        assertEquals(Z.determinate(), z.determinate(), 0);
        assertEquals(Z.isAffine(), z.isAffine());
        assertEquals(Z.isIdentity(), z.isIdentity());

        Matrix3 u = new Matrix3();
        XMatrix U = new GeneralMatrix(u);
        assertEquals(U.determinate(), u.determinate(), 0);
        assertEquals(U.isAffine(), u.isAffine());
        assertEquals(U.isIdentity(), u.isIdentity());

        Matrix3 m = new Matrix3(1, 2, 3, 4, 5, 6, 7, 8, 0);
        XMatrix M = new GeneralMatrix(m);
        assertEquals(M.determinate(), m.determinate(), 0);
        assertEquals(M.isAffine(), m.isAffine());
        assertEquals(M.isIdentity(), m.isIdentity());

        Matrix3 a = new Matrix3();
        a.add(2, m);
        assertEquals(3, a.mat.a11, 0);

        a.sub(1, m);
        assertEquals(0, a.mat.a11, 0);

        a.mul(2, m);
        assertEquals(2, a.mat.a11, 0);

        double array[] = new double[3];
        m.getColumn(0, array);
        assertArrayEquals(array, new double[] {1, 4, 7}, 0);
        m.getRow(1, array);
        assertArrayEquals(array, new double[] {4, 5, 6}, 0);
    }
}
