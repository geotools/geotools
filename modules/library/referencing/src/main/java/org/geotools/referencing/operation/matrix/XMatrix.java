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

import org.opengis.annotation.Extension;
import org.opengis.referencing.operation.Matrix;

/**
 * A matrix capable of some matrix operations. The basic {@link Matrix} interface is basically just
 * a two dimensional array of numbers. The {@code XMatrix} interface add {@linkplain #invert
 * inversion} and {@linkplain #multiply multiplication} capabilities.
 *
 * @since 2.2
 * @version 14
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 * @author Simone Giannecchini
 */
public interface XMatrix extends Matrix {
    /** Returns the number of rows in this matrix. */
    int getNumRow();

    /** Returns the number of colmuns in this matrix. */
    int getNumCol();

    /** Returns the element at the specified index. */
    double getElement(int row, int column);

    /** Extract row to provided array */
    void getRow(int row, double[] array);

    /** Sets the value of the row using an array of values. */
    void setRow(int row, double... values);

    /** Extract col to provided array. */
    public void getColumn(int col, double[] array);

    /** Sets the value of the column using an array of values. */
    @Extension
    public void setColumn(int column, double... values);

    /** Sets all the values in this matrix to zero. */
    void setZero();

    /** Sets this matrix to the identity matrix. */
    void setIdentity();

    /**
     * Returns {@code true} if this matrix is an identity matrix using the provided tolerance. This
     * method is equivalent to computing the difference between this matrix and an identity matrix
     * of identical size, and returning {@code true} if and only if all differences are smaller than
     * or equal to {@code tolerance}.
     *
     * @param tolerance The tolerance value.
     * @return {@code true} if this matrix is close enough to the identity matrix given the
     *     tolerance value.
     * @since 2.4
     */
    boolean isIdentity(double tolerance);

    /**
     * Compares the element values.
     *
     * @param matrix The matrix to compare.
     * @param tolerance The tolerance value.
     * @return {@code true} if this matrix is close enough to the given matrix given the tolerance
     *     value.
     * @since 2.5
     */
    boolean equals(Matrix matrix, double tolerance);

    /**
     * Returns {@code true} if this matrix is an affine transform. A transform is affine if the
     * matrix is square and last row contains only zeros, except in the last column which contains
     * 1.
     *
     * @return {@code true} if this matrix is affine.
     */
    boolean isAffine();

    /** Negates the value of this matrix: {@code this = -this}. */
    void negate();

    /**
     * Negates the value of this matrix: {@code this = -matrix}.
     *
     * @param matrix Matrix to negated
     */
    void negate(Matrix matrix);

    /** Sets the value of this matrix to its transpose. */
    void transpose();

    /**
     * Set to the transpose of the provided matrix.
     *
     * @param matrix The original matrix. Not modified.
     */
    void transpose(Matrix matrix);

    /**
     * Inverts this matrix in place.
     *
     * @throws SingularMatrixException if this matrix is not invertible.
     */
    void invert() throws SingularMatrixException;

    /**
     * Set to the inverse of the provided matrix.
     *
     * @param matrix The matrix that is to be inverted. Not modified.
     * @throws SingularMatrixException if this matrix is not invertible.
     */
    void invert(Matrix matrix) throws SingularMatrixException;

    /**
     * Performs an in-place scalar addition.
     *
     * @param scalar The value that's added to each element.
     */
    void add(double scalar);

    /**
     * Set to the scalar addition of <code>scalar+matrix<code>
     *
     * @param scalar The value that's added to each element.
     * @param matrix The matrix that is to be added. Not modified.
     */
    void add(double scalar, XMatrix matrix);

    /**
     * Set to the matrix addition of <code>this+matrix</code>.
     *
     * @param matrix The matrix that is to be added. Not modified.
     */
    void add(XMatrix matrix);

    /**
     * Set to the matrix addition of <code>matrix1+matrix2</code>.
     *
     * @param matrix1 First matrix to be added. Not modified.
     * @param matrix2 Second matrix to be added. Not modified.
     */
    void add(XMatrix matrix1, XMatrix matrix2);

    /** Computes the determinant */
    double determinate();

    /**
     * Sets the value of this matrix to the result of multiplying itself with the specified matrix.
     * In other words, performs {@code this} = {@code this} &times; {@code matrix}. In the context
     * of coordinate transformations, this is equivalent to <code>
     * {@linkplain java.awt.geom.AffineTransform#concatenate AffineTransform.concatenate}</code>:
     * first transforms by the supplied transform and then transform the result by the original
     * transform.
     *
     * @param matrix The matrix to multiply to this matrix.
     */
    void multiply(Matrix matrix);

    /** Sets this matrix to the result of multiplying itself with the provided scalar. */
    void mul(double scalar);

    /**
     * Sets the value of this matrix to the result of multiplying the provided scalar and matrix.
     */
    void mul(double scalar, Matrix matrix);

    /**
     * Sets the value of this matrix to the result of multiplying itself with the specified matrix.
     * In other words, performs {@code this} = {@code this} &times; {@code matrix}. In the context
     * of coordinate transformations, this is equivalent to <code>
     * {@linkplain java.awt.geom.AffineTransform#concatenate AffineTransform.concatenate}</code>:
     * first transforms by the supplied transform and then transform the result by the original
     * transform.
     *
     * @param matrix The matrix to multiply to this matrix.
     */
    void mul(Matrix matrix);

    /** Sets the value of this matrix to the result of multiplying matrix1 and matrix2. */
    void mul(Matrix matrix1, Matrix matrix2);

    /** In-place matrix subtraction: <code>this - scalar</code>. */
    void sub(double scalar);

    /**
     * Set to the difference of <code>scalar - matrix2</code>.
     *
     * @param matrix matrix, not modified
     */
    void sub(double scalar, Matrix matrix);

    /**
     * In-place matrix subtraction: <code>this - matrix</code>.
     *
     * @param matrix m by n matrix. Not modified.
     */
    void sub(Matrix matrix);

    /**
     * Set to the difference of <code>matrix1 - matrix2</code>.
     *
     * @param matrix1 matrix, not modified
     * @param matrix2 matrix, not modified
     */
    void sub(Matrix matrix1, Matrix matrix2);
}
