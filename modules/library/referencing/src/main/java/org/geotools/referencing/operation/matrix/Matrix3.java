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

import javax.vecmath.Matrix3d;
import java.awt.geom.AffineTransform;
import org.opengis.referencing.operation.Matrix;
import org.geotools.resources.i18n.Errors;
import org.geotools.resources.i18n.ErrorKeys;


/**
 * A matrix of fixed {@value #SIZE}&times;{@value #SIZE} size. This specialized matrix provides
 * better accuracy than {@link GeneralMatrix} for matrix inversion and multiplication.
 *
 * @since 2.2
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public class Matrix3 extends Matrix3d implements XMatrix {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = 8902061778871586611L;

    /**
     * The matrix size, which is {@value}.
     */
    public static final int SIZE = 3;

    /**
     * Creates a new identity matrix.
     */
    public Matrix3() {
        setIdentity();
    }

    /**
     * Creates a new matrix initialized to the specified values.
     */
    public Matrix3(double m00, double m01, double m02,
                   double m10, double m11, double m12,
                   double m20, double m21, double m22)
    {
        super(m00, m01, m02,
              m10, m11, m12,
              m20, m21, m22);
    }

    /**
     * Constructs a 3&times;3 matrix from the specified affine transform.
     */
    public Matrix3(final AffineTransform transform) {
        setMatrix(transform);
    }

    /**
     * Creates a new matrix initialized to the same value than the specified one.
     * The specified matrix size must be {@value #SIZE}&times;{@value #SIZE}.
     */
    public Matrix3(final Matrix matrix) {
        if (matrix.getNumRow()!=SIZE || matrix.getNumCol()!=SIZE) {
            throw new IllegalArgumentException(Errors.format(ErrorKeys.ILLEGAL_MATRIX_SIZE));
        }
        for (int j=0; j<SIZE; j++) {
            for (int i=0; i<SIZE; i++) {
                setElement(j,i, matrix.getElement(j,i));
            }
        }
    }

    /**
     * Returns the number of rows in this matrix, which is always {@value #SIZE}
     * in this implementation.
     */
    public final int getNumRow() {
        return SIZE;
    }

    /**
     * Returns the number of colmuns in this matrix, which is always {@value #SIZE}
     * in this implementation.
     */
    public final int getNumCol() {
        return SIZE;
    }

    /**
     * {@inheritDoc}
     */
    public final boolean isIdentity() {
        for (int j=0; j<SIZE; j++) {
            for (int i=0; i<SIZE; i++) {
                if (getElement(j,i) != ((i==j) ? 1 : 0)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    public final boolean isIdentity(double tolerance) {
    	return GeneralMatrix.isIdentity(this, tolerance);
    }

    /**
     * {@inheritDoc}
     */
    public final boolean isAffine() {
        return m20==0 && m21==0 && m22==1;
    }

    /**
     * Returns {@code true} if at least one value is {@code NaN}.
     *
     * @since 2.3
     */
    public final boolean isNaN() {
        return Double.isNaN(m00) || Double.isNaN(m01) || Double.isNaN(m02) ||
               Double.isNaN(m10) || Double.isNaN(m11) || Double.isNaN(m12) ||
               Double.isNaN(m20) || Double.isNaN(m21) || Double.isNaN(m22);
    }

    /**
     * {@inheritDoc}
     */
    public final void multiply(final Matrix matrix) {
        final Matrix3d m;
        if (matrix instanceof Matrix3d) {
            m = (Matrix3d) matrix;
        } else {
            m = new Matrix3(matrix);
        }
        mul(m);
    }

    /**
     * Sets this matrix to the specified affine transform.
     *
     * @since 2.3
     */
    public void setMatrix(final AffineTransform transform) {
        m00=transform.getScaleX(); m01=transform.getShearX(); m02=transform.getTranslateX();
        m10=transform.getShearY(); m11=transform.getScaleY(); m12=transform.getTranslateY();
        m20=0;                     m21=0;                     m22=1;
    }

    /**
     * Returns {@code true} if this matrix is equals to the specified affine transform.
     *
     * @since 2.3
     */
    public boolean equalsAffine(final AffineTransform transform) {
        return m00==transform.getScaleX() && m01==transform.getShearX() && m02==transform.getTranslateX() &&
               m10==transform.getShearY() && m11==transform.getScaleY() && m12==transform.getTranslateY() &&
               m20==0                     && m21==0                     && m22==1;
    }

    /**
     * {@inheritDoc}
     */
    public boolean equals(final Matrix matrix, final double tolerance) {
        return GeneralMatrix.epsilonEquals(this, matrix, tolerance);
    }

    /**
     * Returns a string representation of this matrix. The returned string is implementation
     * dependent. It is usually provided for debugging purposes only.
     */
    @Override
    public String toString() {
        return GeneralMatrix.toString(this);
    }

    /**
     * Returns a clone of this matrix.
     */
    @Override
    public Matrix3 clone() {
        return (Matrix3) super.clone();
    }
}
