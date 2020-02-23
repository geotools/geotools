/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.operation.matrix;

import java.awt.geom.AffineTransform;
import java.io.Serializable;
import org.ejml.UtilEjml;
import org.ejml.data.DMatrix3x3;
import org.ejml.dense.fixed.CommonOps_DDF3;
import org.geotools.metadata.i18n.ErrorKeys;
import org.geotools.metadata.i18n.Errors;
import org.opengis.referencing.operation.Matrix;

/**
 * A matrix of fixed {@value #SIZE}&times;{@value #SIZE} size.
 *
 * @since 2.2
 * @version 13.0
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public class Matrix3 implements XMatrix, Serializable {
    /** Serial number for interoperability with different versions. */
    private static final long serialVersionUID = 8902061778871586612L;

    /** The matrix size, which is {@value}. */
    public static final int SIZE = 3;

    DMatrix3x3 mat;
    /** Creates a new identity matrix. */
    public Matrix3() {
        mat = new DMatrix3x3();
        setIdentity();
    }

    /** Creates a new matrix initialized to the specified values. */
    public Matrix3(
            double m00,
            double m01,
            double m02,
            double m10,
            double m11,
            double m12,
            double m20,
            double m21,
            double m22) {
        mat = new DMatrix3x3(m00, m01, m02, m10, m11, m12, m20, m21, m22);
    }

    /** Constructs a 3&times;3 matrix from the specified affine transform. */
    public Matrix3(final AffineTransform transform) {
        mat = new DMatrix3x3();
        setMatrix(transform);
    }

    /**
     * Creates a new matrix initialized to the same value than the specified one. The specified
     * matrix size must be {@value #SIZE}&times;{@value #SIZE}.
     */
    public Matrix3(final Matrix matrix) {
        mat = new DMatrix3x3();
        if (matrix.getNumRow() != SIZE || matrix.getNumCol() != SIZE) {
            throw new IllegalArgumentException(Errors.format(ErrorKeys.ILLEGAL_MATRIX_SIZE));
        }
        for (int j = 0; j < SIZE; j++) {
            for (int i = 0; i < SIZE; i++) {
                setElement(j, i, matrix.getElement(j, i));
            }
        }
    }
    /**
     * Cast (or convert) Matrix to internal DMatrixRMaj representation required for CommonOps_DDF3.
     */
    private DMatrix3x3 internal(Matrix matrix) {
        if (matrix instanceof Matrix3) {
            return ((Matrix3) matrix).mat;
        } else {
            DMatrix3x3 a =
                    new DMatrix3x3(
                            matrix.getElement(0, 0),
                            matrix.getElement(0, 1),
                            matrix.getElement(0, 2),
                            matrix.getElement(1, 0),
                            matrix.getElement(1, 1),
                            matrix.getElement(1, 2),
                            matrix.getElement(2, 0),
                            matrix.getElement(2, 1),
                            matrix.getElement(2, 2));
            return a;
        }
    }
    /**
     * Returns the number of rows in this matrix, which is always {@value #SIZE} in this
     * implementation.
     */
    public final int getNumRow() {
        return SIZE;
    }

    /**
     * Returns the number of colmuns in this matrix, which is always {@value #SIZE} in this
     * implementation.
     */
    public final int getNumCol() {
        return SIZE;
    }

    /**
     * Returns {@code true} if at least one value is {@code NaN}.
     *
     * @since 2.3
     */
    public final boolean isNaN() {
        return Double.isNaN(mat.a11)
                || Double.isNaN(mat.a12)
                || Double.isNaN(mat.a13)
                || Double.isNaN(mat.a21)
                || Double.isNaN(mat.a22)
                || Double.isNaN(mat.a23)
                || Double.isNaN(mat.a31)
                || Double.isNaN(mat.a32)
                || Double.isNaN(mat.a33);
    }

    /**
     * Sets this matrix to the specified affine transform.
     *
     * @since 2.3
     */
    public void setMatrix(final AffineTransform transform) {
        mat.a11 = transform.getScaleX();
        mat.a12 = transform.getShearX();
        mat.a13 = transform.getTranslateX();
        mat.a21 = transform.getShearY();
        mat.a22 = transform.getScaleY();
        mat.a23 = transform.getTranslateY();
        mat.a31 = 0;
        mat.a32 = 0;
        mat.a33 = 1;
    }

    /**
     * Returns {@code true} if this matrix is equals to the specified affine transform.
     *
     * @since 2.3
     */
    public boolean equalsAffine(final AffineTransform transform) {
        return mat.a11 == transform.getScaleX()
                && mat.a12 == transform.getShearX()
                && mat.a13 == transform.getTranslateX()
                && mat.a21 == transform.getShearY()
                && mat.a22 == transform.getScaleY()
                && mat.a23 == transform.getTranslateY()
                && mat.a31 == 0
                && mat.a32 == 0
                && mat.a33 == 1;
    }

    /** Returns a clone of this matrix. */
    @Override
    public Matrix3 clone() {
        return new Matrix3(this);
    }

    /** {@inheritDoc} */
    public final boolean isAffine() {
        return mat.a31 == 0 && mat.a32 == 0 && mat.a33 == 1;
    }

    /** Changes the sign of each element in the matrix. */
    @Override
    public void negate() {
        CommonOps_DDF3.changeSign(mat);
    }

    @Override
    public void negate(Matrix matrix) {
        DMatrix3x3 a = internal(matrix);
        CommonOps_DDF3.changeSign(a);
        this.mat = a;
    }

    /** Transposes the matrix. */
    @Override
    public void transpose() {
        CommonOps_DDF3.transpose(mat);
    }

    @Override
    public void transpose(Matrix matrix) {
        DMatrix3x3 a = internal(matrix);
        CommonOps_DDF3.transpose(a, mat);
    }

    @Override
    public void invert() {
        DMatrix3x3 ret = new DMatrix3x3();
        boolean success = CommonOps_DDF3.invert(mat, ret);
        if (!success) {
            throw new SingularMatrixException("Could not invert, possible singular matrix?");
        }
        mat = ret;
    }

    @Override
    public void invert(Matrix matrix) throws SingularMatrixException {
        DMatrix3x3 a = internal(matrix);
        boolean success = CommonOps_DDF3.invert(a, mat);
        if (!success) {
            throw new SingularMatrixException("Could not invert, possible singular matrix?");
        }
        this.mat = a;
    }

    /**
     * Returns the value at the row, column position in the matrix.
     *
     * @return Matrix value at the given row and column.
     */
    @Override
    public double getElement(int row, int column) {
        return mat.get(row, column);
    }

    public void setColumn(int column, double... values) {
        if (values.length != mat.getNumCols()) {
            throw new IllegalArgumentException(
                    "Call setRow received an array of length "
                            + values.length
                            + ".  "
                            + "The dimensions of the matrix is "
                            + mat.getNumRows()
                            + " by "
                            + mat.getNumCols()
                            + ".");
        }
        for (int i = 0; i < values.length; i++) {
            mat.set(i, column, values[i]);
        }
    }

    public void setRow(int row, double... values) {
        if (values.length != mat.getNumCols()) {
            throw new IllegalArgumentException(
                    "Call setRow received an array of length "
                            + values.length
                            + ".  "
                            + "The dimensions of the matrix is "
                            + mat.getNumRows()
                            + " by "
                            + mat.getNumCols()
                            + ".");
        }

        for (int i = 0; i < values.length; i++) {
            mat.set(row, i, values[i]);
        }
    }

    /** Sets the value of the row, column position in the matrix. */
    @Override
    public void setElement(int row, int column, double value) {
        mat.set(row, column, value);
    }

    /** Sets each value of the matrix to 0.0. */
    @Override
    public void setZero() {
        CommonOps_DDF3.fill(mat, 0);
    }

    /** Sets the main diagonal of this matrix to be 1.0. */
    @Override
    public void setIdentity() {
        CommonOps_DDF3.setIdentity(mat);
    }

    /** Returns {@code true} if this matrix is an identity matrix. */
    public final boolean isIdentity() {
        final int numRow = getNumRow();
        final int numCol = getNumCol();
        if (numRow != numCol) {
            return false;
        }
        for (int j = 0; j < numRow; j++) {
            for (int i = 0; i < numCol; i++) {
                if (getElement(j, i) != (i == j ? 1.0 : 0.0)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * {@inheritDoc}
     *
     * @since 2.3.1
     */
    public final boolean isIdentity(double tolerance) {
        return GeneralMatrix.isIdentity(this, tolerance);
    }

    /** {@inheritDoc} */
    public final void multiply(final Matrix matrix) {
        mul(matrix);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        if (mat == null) {
            return prime * result;
        }
        result = prime * result + SIZE; // for hashCode compatibility with GeneralMatrix
        result = prime * result + SIZE;
        long bits = Double.doubleToRawLongBits(mat.a11);
        result = prime * result + ((int) (bits ^ (bits >>> 32)));
        bits = Double.doubleToRawLongBits(mat.a12);
        result = prime * result + ((int) (bits ^ (bits >>> 32)));
        bits = Double.doubleToRawLongBits(mat.a13);
        result = prime * result + ((int) (bits ^ (bits >>> 32)));
        bits = Double.doubleToRawLongBits(mat.a21);
        result = prime * result + ((int) (bits ^ (bits >>> 32)));
        bits = Double.doubleToRawLongBits(mat.a22);
        result = prime * result + ((int) (bits ^ (bits >>> 32)));
        bits = Double.doubleToRawLongBits(mat.a23);
        result = prime * result + ((int) (bits ^ (bits >>> 32)));
        bits = Double.doubleToRawLongBits(mat.a31);
        result = prime * result + ((int) (bits ^ (bits >>> 32)));
        bits = Double.doubleToRawLongBits(mat.a32);
        result = prime * result + ((int) (bits ^ (bits >>> 32)));
        bits = Double.doubleToRawLongBits(mat.a33);
        result = prime * result + ((int) (bits ^ (bits >>> 32)));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Matrix3 other = (Matrix3) obj;
        return equals(other, 0);
    }

    public boolean equals(final Matrix matrix, final double tolerance) {
        return GeneralMatrix.epsilonEquals(this, matrix, tolerance);
    }

    /**
     * Returns an affine transform for this matrix. This is a convenience method for
     * interoperability with Java2D.
     *
     * @return The affine transform for this matrix.
     * @throws IllegalStateException if this matrix is not 3&times;3, or if the last row is not
     *     {@code [0 0 1]}.
     */
    public final AffineTransform toAffineTransform2D() throws IllegalStateException {
        if (isAffine()) {
            return new AffineTransform(
                    getElement(0, 0),
                    getElement(1, 0),
                    getElement(0, 1),
                    getElement(1, 1),
                    getElement(0, 2),
                    getElement(1, 2));
        }
        throw new IllegalStateException(Errors.format(ErrorKeys.NOT_AN_AFFINE_TRANSFORM));
    }

    /**
     * Returns a string representation of this matrix. The returned string is implementation
     * dependent. It is usually provided for debugging purposes only.
     */
    @Override
    public String toString() {
        return GeneralMatrix.toString(this);
    }

    /** Extract col to provided array. */
    public void getColumn(int col, double[] array) {
        for (int j = 0; j < array.length; j++) {
            array[j] = mat.get(j, col);
        }
    }

    @Override
    public void mul(double scalar) {
        CommonOps_DDF3.scale(scalar, this.mat);
    }

    @Override
    public void mul(double scalar, Matrix matrix) {
        DMatrix3x3 ret = new DMatrix3x3();
        CommonOps_DDF3.scale(scalar, internal(matrix), ret);
        mat = ret;
    }

    /** Extract row to provided array */
    public void getRow(int row, double[] array) {
        for (int i = 0; i < array.length; i++) {
            array[i] = mat.get(row, i);
        }
    }

    //
    // In-place operations
    //
    /** In-place multiply with provided matrix. */
    public final void mul(Matrix matrix) {
        DMatrix3x3 b = internal(matrix);
        DMatrix3x3 ret = new DMatrix3x3();
        CommonOps_DDF3.mult(mat, b, ret);
        mat = ret;
    }

    /** In-place update from matrix1 * matrix2. */
    public void mul(Matrix matrix1, Matrix matrix2) {
        DMatrix3x3 a = internal(matrix1);
        DMatrix3x3 b = internal(matrix2);
        if (a == mat || b == mat) {
            mat = new DMatrix3x3();
        }
        CommonOps_DDF3.mult(a, b, mat);
    }

    @Override
    public void sub(double scalar) {
        mat.a11 -= scalar;
        mat.a12 -= scalar;
        mat.a12 -= scalar;
        mat.a21 -= scalar;
        mat.a22 -= scalar;
        mat.a22 -= scalar;
        mat.a31 -= scalar;
        mat.a32 -= scalar;
        mat.a32 -= scalar;
    }

    @Override
    public void sub(double scalar, Matrix matrix) {
        DMatrix3x3 a = internal(matrix);
        mat.a11 = scalar - a.a11;
        mat.a12 = scalar - a.a12;
        mat.a12 = scalar - a.a13;
        mat.a21 = scalar - a.a21;
        mat.a22 = scalar - a.a22;
        mat.a22 = scalar - a.a23;
        mat.a31 = scalar - a.a31;
        mat.a32 = scalar - a.a32;
        mat.a32 = scalar - a.a33;
    }

    public void sub(Matrix matrix) {
        DMatrix3x3 a = internal(matrix);
        mat.a11 -= a.a11;
        mat.a12 -= a.a12;
        mat.a12 -= a.a13;
        mat.a21 -= a.a21;
        mat.a22 -= a.a22;
        mat.a22 -= a.a23;
        mat.a31 -= a.a31;
        mat.a32 -= a.a32;
        mat.a32 -= a.a33;
    }

    public void sub(Matrix matrix1, Matrix matrix2) {
        DMatrix3x3 a = internal(matrix1);
        DMatrix3x3 b = internal(matrix2);
        mat.a11 = a.a11 - b.a11;
        mat.a12 = a.a12 - b.a12;
        mat.a12 = a.a13 - b.a13;
        mat.a21 = a.a21 - b.a21;
        mat.a22 = a.a22 - b.a22;
        mat.a22 = a.a23 - b.a23;
        mat.a31 = a.a31 - b.a31;
        mat.a32 = a.a32 - b.a32;
        mat.a32 = a.a33 - b.a33;
    }

    /** Update in place to the provided matrix (row-order). */
    public void set(double[] matrix) {
        mat.a11 = matrix[0];
        mat.a12 = matrix[1];
        mat.a13 = matrix[2];
        mat.a21 = matrix[3];
        mat.a22 = matrix[4];
        mat.a23 = matrix[5];
        mat.a31 = matrix[6];
        mat.a32 = matrix[7];
        mat.a33 = matrix[8];
    }

    @Override
    public void add(double scalar) {
        mat.a11 += scalar;
        mat.a12 += scalar;
        mat.a12 += scalar;
        mat.a21 += scalar;
        mat.a22 += scalar;
        mat.a22 += scalar;
        mat.a31 += scalar;
        mat.a32 += scalar;
        mat.a32 += scalar;
    }

    @Override
    public void add(double scalar, XMatrix matrix) {
        DMatrix3x3 a = internal(matrix);
        mat.a11 = scalar + a.a11;
        mat.a12 = scalar + a.a12;
        mat.a12 = scalar + a.a13;
        mat.a21 = scalar + a.a21;
        mat.a22 = scalar + a.a22;
        mat.a22 = scalar + a.a23;
        mat.a31 = scalar + a.a31;
        mat.a32 = scalar + a.a32;
        mat.a32 = scalar + a.a33;
    }

    @Override
    public void add(XMatrix matrix) {
        CommonOps_DDF3.add(mat, internal(matrix), mat);
    }

    @Override
    public void add(XMatrix matrix1, XMatrix matrix2) {
        DMatrix3x3 a = internal(matrix1);
        DMatrix3x3 b = internal(matrix2);
        CommonOps_DDF3.add(a, b, mat);
    }

    @Override
    public double determinate() {
        double det = CommonOps_DDF3.det(mat);
        // if the decomposition silently failed then the matrix is most likely singular
        if (UtilEjml.isUncountable(det)) return 0;
        return det;
    }
}
