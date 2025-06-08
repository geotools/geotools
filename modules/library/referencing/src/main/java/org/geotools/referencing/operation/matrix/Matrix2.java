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

import java.io.Serializable;
import org.geotools.api.referencing.operation.Matrix;
import org.geotools.metadata.i18n.ErrorKeys;

/**
 * A matrix of fixed {@value #SIZE}&times;{@value #SIZE} size.
 *
 * @since 2.2
 * @version 13.0
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public class Matrix2 implements XMatrix, Serializable {
    /** Serial number for interoperability with different versions. */
    private static final long serialVersionUID = 7116561372481474290L;

    /** The matrix size, which is {@value}. */
    public static final int SIZE = 2;

    /** The first matrix element in the first row. */
    public double m00;

    /** The second matrix element in the first row. */
    public double m01;

    /** The first matrix element in the second row. */
    public double m10;

    /** The second matrix element in the second row. */
    public double m11;

    /** Creates a new identity matrix. */
    public Matrix2() {
        m00 = m11 = 1;
    }

    /** Creates a new matrix initialized to the specified values. */
    public Matrix2(final double m00, final double m01, final double m10, final double m11) {
        this.m00 = m00;
        this.m01 = m01;
        this.m10 = m10;
        this.m11 = m11;
    }

    /**
     * Creates a new matrix initialized to the same value than the specified one. The specified matrix size must be
     * {@value #SIZE}&times;{@value #SIZE}.
     */
    public Matrix2(final Matrix matrix) {
        if (matrix.getNumRow() != SIZE || matrix.getNumCol() != SIZE) {
            throw new IllegalArgumentException(ErrorKeys.ILLEGAL_MATRIX_SIZE);
        }
        m00 = matrix.getElement(0, 0);
        m01 = matrix.getElement(0, 1);
        m10 = matrix.getElement(1, 0);
        m11 = matrix.getElement(1, 1);
    }

    /** Used to cast/copy matrix to Matrix2 */
    Matrix2 internal(Matrix matrix) {
        if (matrix instanceof Matrix2) {
            return (Matrix2) matrix;
        } else {
            if (matrix.getNumRow() != SIZE || matrix.getNumCol() != SIZE) {
                throw new IllegalArgumentException(ErrorKeys.ILLEGAL_MATRIX_SIZE);
            }
            return new Matrix2(matrix);
        }
    }

    /** Returns the number of rows in this matrix, which is always {@value #SIZE} in this implementation. */
    @Override
    public final int getNumRow() {
        return SIZE;
    }

    /** Returns the number of colmuns in this matrix, which is always {@value #SIZE} in this implementation. */
    @Override
    public final int getNumCol() {
        return SIZE;
    }

    /** {@inheritDoc} */
    @Override
    public final double getElement(final int row, final int col) {
        switch (row) {
            case 0: {
                switch (col) {
                    case 0:
                        return m00;
                    case 1:
                        return m01;
                }
                break;
            }
            case 1: {
                switch (col) {
                    case 0:
                        return m10;
                    case 1:
                        return m11;
                }
                break;
            }
        }
        throw new IndexOutOfBoundsException();
    }

    /** {@inheritDoc} */
    @Override
    public final void setElement(final int row, final int col, final double value) {
        switch (row) {
            case 0: {
                switch (col) {
                    case 0:
                        m00 = value;
                        return;
                    case 1:
                        m01 = value;
                        return;
                }
                break;
            }
            case 1: {
                switch (col) {
                    case 0:
                        m10 = value;
                        return;
                    case 1:
                        m11 = value;
                        return;
                }
                break;
            }
        }
        throw new IndexOutOfBoundsException();
    }

    /** {@inheritDoc} */
    @Override
    public final void setZero() {
        m00 = m01 = m10 = m11 = 0;
    }

    /** {@inheritDoc} */
    @Override
    public final void setIdentity() {
        m01 = m10 = 0;
        m00 = m11 = 1;
        assert isIdentity();
    }

    /** {@inheritDoc} */
    @Override
    public final boolean isIdentity() {
        return m01 == 0 && m10 == 0 && m00 == 1 && m11 == 1;
    }

    /** {@inheritDoc} */
    @Override
    public final boolean isIdentity(double tolerance) {
        return GeneralMatrix.isIdentity(this, tolerance);
    }

    /** {@inheritDoc} */
    @Override
    public final boolean isAffine() {
        return m10 == 0 && m11 == 1;
    }

    /** {@inheritDoc} */
    @Override
    public final void negate() {
        m00 = -m00;
        m01 = -m01;
        m10 = -m10;
        m11 = -m11;
    }

    @Override
    public void negate(Matrix matrix) {
        Matrix2 k = internal(matrix);
        m00 = -k.m00;
        m01 = -k.m01;
        m10 = -k.m10;
        m11 = -k.m11;
    }

    /** {@inheritDoc} */
    @Override
    public final void transpose() {
        final double swap = m10;
        m10 = m01;
        m01 = swap;
    }

    @Override
    public void transpose(Matrix matrix) {
        Matrix2 k = internal(matrix);
        m00 = k.m00;
        m01 = k.m10;
        m10 = k.m01;
        m11 = k.m11;
    }

    /** Inverts this matrix in place. */
    @Override
    public final void invert() {
        final double det = m00 * m11 - m01 * m10;
        if (det == 0) {
            throw new SingularMatrixException("Determinate is zero, cannot invert matrix");
        }
        final double swap = m00;
        m00 = m11 / det;
        m11 = swap / det;
        m10 = -m10 / det;
        m01 = -m01 / det;
    }

    @Override
    public void invert(Matrix matrix) throws SingularMatrixException {
        Matrix2 k = internal(matrix);
        final double det = k.m00 * k.m11 - k.m01 * k.m10;
        if (det == 0) {
            throw new SingularMatrixException("Determinate is zero, cannot invert matrix");
        }
        m00 = k.m11 / det;
        m11 = k.m00 / det;
        m10 = -k.m10 / det;
        m01 = -k.m01 / det;
    }

    @Override
    public final void multiply(final Matrix matrix) {
        mul(matrix);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Matrix matrix, final double tolerance) {
        return GeneralMatrix.epsilonEquals(this, matrix, tolerance);
    }

    /**
     * Returns {@code true} if the specified object is of type {@code Matrix2} and all of the data members are equal to
     * the corresponding data members in this matrix.
     */
    @Override
    public boolean equals(final Object object) {
        if (object != null && object.getClass().equals(getClass())) {
            final Matrix2 that = (Matrix2) object;
            return Double.doubleToLongBits(this.m00) == Double.doubleToLongBits(that.m00)
                    && Double.doubleToLongBits(this.m01) == Double.doubleToLongBits(that.m01)
                    && Double.doubleToLongBits(this.m10) == Double.doubleToLongBits(that.m10)
                    && Double.doubleToLongBits(this.m11) == Double.doubleToLongBits(that.m11);
        }
        return false;
    }

    /** Returns a hash code value based on the data values in this object. */
    @Override
    public int hashCode() {
        return (int) (Double.doubleToLongBits(m00)
                        + 37 * Double.doubleToLongBits(m01)
                        + 37 * Double.doubleToLongBits(m10)
                        + 37 * Double.doubleToLongBits(m11)
                ^ serialVersionUID);
    }

    /**
     * Returns a string representation of this matrix. The returned string is implementation dependent. It is usually
     * provided for debugging purposes only.
     */
    @Override
    public String toString() {
        return GeneralMatrix.toString(this);
    }

    /** Returns a clone of this matrix. */
    @Override
    public Matrix2 clone() {
        try {
            return (Matrix2) super.clone();
        } catch (CloneNotSupportedException e) {
            // Should not happen, since we are cloneable.
            throw new AssertionError(e);
        }
    }

    @Override
    public void getRow(int row, double[] array) {
        if (array.length != SIZE) {
            throw new IllegalArgumentException("Call getRow received an array of length "
                    + array.length
                    + ".  "
                    + "The dimensions of the matrix is 2 by 2.");
        }
        if (row == 0) {
            array[0] = m00;
            array[1] = m01;
        } else if (row == 1) {
            array[0] = m10;
            array[1] = m11;
        } else {
            throw new IllegalArgumentException("Specified element is out of bounds: (" + row + ", 0)");
        }
    }

    @Override
    public void setRow(int row, double... values) {
        if (values.length != SIZE) {
            throw new IllegalArgumentException("Call setRow received an array of length "
                    + values.length
                    + ".  "
                    + "The dimensions of the matrix is 2 by 2.");
        }
        if (row == 0) {
            m00 = values[0];
            m01 = values[1];
        } else if (row == 1) {
            m10 = values[0];
            m11 = values[1];
        } else {
            throw new IllegalArgumentException("Specified element is out of bounds: (" + row + " , 0)");
        }
    }

    @Override
    public void getColumn(int column, double[] array) {
        if (array.length != SIZE) {
            throw new IllegalArgumentException("Call getColumn received an array of length "
                    + array.length
                    + ".  "
                    + "The dimensions of the matrix is 2 by 2.");
        }
        if (column == 0) {
            array[0] = m00;
            array[1] = m10;
        } else if (column == 1) {
            array[0] = m01;
            array[1] = m11;
        } else {
            throw new IllegalArgumentException("Specified element is out of bounds: (0 , " + column + ")");
        }
    }

    @Override
    public void setColumn(int column, double... values) {
        if (values.length != SIZE) {
            throw new IllegalArgumentException("Call setColumn received an array of length "
                    + values.length
                    + ".  "
                    + "The dimensions of the matrix is 2 by 2.");
        }
        if (column == 0) {
            m00 = values[0];
            m10 = values[1];
        } else if (column == 1) {
            m01 = values[0];
            m11 = values[1];
        } else {
            throw new IllegalArgumentException("Specified element is out of bounds: (0 , " + column + ")");
        }
    }

    @Override
    public void add(double scalar) {
        m00 += scalar;
        m01 += scalar;
        m10 += scalar;
        m11 += scalar;
    }

    @Override
    public void add(double scalar, XMatrix matrix) {
        final Matrix2 k = internal(matrix);
        m00 = scalar + k.m00;
        m01 = scalar + k.m01;
        m10 = scalar + k.m10;
        m11 = scalar + k.m11;
    }

    @Override
    public void add(XMatrix matrix) {
        final Matrix2 k = internal(matrix);
        m00 += k.m00;
        m01 += k.m01;
        m10 += k.m10;
        m11 += k.m11;
    }

    @Override
    public void add(XMatrix matrix1, XMatrix matrix2) {
        final Matrix2 a = internal(matrix1);
        final Matrix2 b = internal(matrix2);
        m00 = a.m00 + b.m00;
        m01 = a.m01 + b.m01;
        m10 = a.m10 + b.m10;
        m11 = a.m11 + b.m11;
    }

    @Override
    public double determinate() {
        return m00 * m11 - m01 * m10;
    }

    @Override
    public void mul(double scalar) {
        m00 *= scalar;
        m01 *= scalar;
        m10 *= scalar;
        m11 *= scalar;
    }

    @Override
    public void mul(double scalar, Matrix matrix) {
        final Matrix2 k = internal(matrix);
        m00 = scalar * k.m00;
        m01 = scalar * k.m01;
        m10 = scalar * k.m10;
        m11 = scalar * k.m11;
    }

    @Override
    public void mul(Matrix matrix) {
        final Matrix2 k = internal(matrix);
        double m0 = m00;
        double m1 = m01;
        m00 = m0 * k.m00 + m1 * k.m10;
        m01 = m0 * k.m01 + m1 * k.m11;
        m0 = m10;
        m1 = m11;
        m10 = m0 * k.m00 + m1 * k.m10;
        m11 = m0 * k.m01 + m1 * k.m11;
    }

    @Override
    public void mul(Matrix matrix1, Matrix matrix2) {
        final Matrix2 a = internal(matrix1);
        final Matrix2 b = internal(matrix2);
        m00 = a.m00 * b.m00 + a.m10 * b.m01;
        m01 = a.m00 * b.m10 + a.m10 * b.m11;
        m10 = a.m01 * b.m00 + a.m11 * b.m01;
        m11 = a.m01 * b.m10 + a.m11 * b.m11;
    }

    @Override
    public void sub(double scalar) {
        m00 -= scalar;
        m01 -= scalar;
        m10 -= scalar;
        m11 -= scalar;
    }

    @Override
    public void sub(double scalar, Matrix matrix) {
        final Matrix2 k = internal(matrix);
        m00 = scalar - k.m00;
        m01 = scalar - k.m01;
        m10 = scalar - k.m10;
        m11 = scalar - k.m11;
    }

    @Override
    public void sub(Matrix matrix) {
        final Matrix2 k = internal(matrix);
        m00 -= k.m00;
        m01 -= k.m01;
        m10 -= k.m10;
        m11 -= k.m11;
    }

    @Override
    public void sub(Matrix matrix1, Matrix matrix2) {
        final Matrix2 a = internal(matrix1);
        final Matrix2 b = internal(matrix2);
        m00 = a.m00 - b.m00;
        m01 = a.m01 - b.m01;
        m10 = a.m10 - b.m10;
        m11 = a.m11 - b.m11;
    }
}
