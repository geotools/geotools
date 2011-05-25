/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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

import javax.vecmath.SingularMatrixException;
import org.opengis.referencing.operation.Matrix;


/**
 * A matrix capables to perform some matrix operations. The basic {@link Matrix} interface
 * is basically just a two dimensional array of numbers. The {@code XMatrix} interface add
 * {@linkplain #invert inversion} and {@linkplain #multiply multiplication} capabilities.
 * It is used as a bridge across various matrix implementations in Java3D
 * ({@link javax.vecmath.Matrix3f}, {@link javax.vecmath.Matrix3d}, {@link javax.vecmath.Matrix4f},
 * {@link javax.vecmath.Matrix4d}, {@link javax.vecmath.GMatrix}).
 *
 * @since 2.2
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 * @author Simone Giannecchini
 */
public interface XMatrix extends Matrix {
    /**
     * Returns the number of rows in this matrix.
     */
    int getNumRow();

    /**
     * Returns the number of colmuns in this matrix.
     */
    int getNumCol();

    /**
     * Returns the element at the specified index.
     */
    double getElement(int row, int column);

    /**
     * Set the element at the specified index.
     */
    void setElement(int row, int column, double value);

    /**
     * Sets all the values in this matrix to zero.
     */
    void setZero();

    /**
     * Sets this matrix to the identity matrix.
     */
    void setIdentity();

    /**
     * Returns {@code true} if this matrix is an identity matrix.
     */
    boolean isIdentity();

    /**
     * Returns {@code true} if this matrix is an identity matrix using the provided tolerance.
     * This method is equivalent to computing the difference between this matrix and an identity
     * matrix of identical size, and returning {@code true} if and only if all differences are
     * smaller than or equal to {@code tolerance}.
     *
     * @param tolerance The tolerance value.
     * @return {@code true} if this matrix is close enough to the identity matrix
     *         given the tolerance value.
     *
     * @since 2.4
     */
    boolean isIdentity(double tolerance);

    /**
     * Returns {@code true} if this matrix is an affine transform.
     * A transform is affine if the matrix is square and last row contains
     * only zeros, except in the last column which contains 1.
     *
     * @return {@code true} if this matrix is affine.
     */
    boolean isAffine();

    /**
     * Negates the value of this matrix: {@code this} = {@code -this}.
     */
    void negate();

    /**
     * Sets the value of this matrix to its transpose.
     */
    void transpose();

    /**
     * Inverts this matrix in place.
     *
     * @throws SingularMatrixException if this matrix is not invertible.
     */
    void invert() throws SingularMatrixException;

    /**
     * Sets the value of this matrix to the result of multiplying itself with the specified matrix.
     * In other words, performs {@code this} = {@code this} &times; {@code matrix}. In the context
     * of coordinate transformations, this is equivalent to
     * <code>{@linkplain java.awt.geom.AffineTransform#concatenate AffineTransform.concatenate}</code>:
     * first transforms by the supplied transform and then transform the result by the original
     * transform.
     *
     * @param matrix The matrix to multiply to this matrix.
     */
    void multiply(Matrix matrix);

    /**
     * Compares the element values regardless the object class. This is similar to a call to
     * <code>{@linkplain javax.vecmath.GMatrix#epsilonEquals GMatrix.epsilonEquals}(matrix,
     * tolerance)</code>. The method name is intentionally different in order to avoid
     * ambiguities at compile-time.
     *
     * @param matrix    The matrix to compare.
     * @param tolerance The tolerance value.
     * @return {@code true} if this matrix is close enough to the given matrix
     *         given the tolerance value.
     *
     * @since 2.5
     */
    boolean equals(Matrix matrix, double tolerance);
}
