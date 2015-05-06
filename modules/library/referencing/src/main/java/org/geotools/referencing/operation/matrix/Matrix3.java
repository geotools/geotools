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

import java.awt.geom.AffineTransform;

import org.opengis.referencing.operation.Matrix;
import org.geotools.resources.i18n.Errors;
import org.geotools.resources.i18n.ErrorKeys;

/**
 * A matrix of fixed {@value #SIZE}&times;{@value #SIZE} size.
 *
 * @since 2.2
 * @version 13.0
 * 
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 * @deprecated Use GeneralMatrix
 */
public class Matrix3 extends GeneralMatrix implements XMatrix {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = 8902061778871586612L;

    /**
     * The matrix size, which is {@value}.
     */
    public static final int SIZE = 3;

    /**
     * Creates a new identity matrix.
     */
    public Matrix3() {
        super(SIZE);
        setIdentity();
    }

    /**
     * Creates a new matrix initialized to the specified values.
     */
    public Matrix3(double m00, double m01, double m02,
                   double m10, double m11, double m12,
                   double m20, double m21, double m22)
    {
        super(SIZE,SIZE,
              new double[]{
                 m00, m01, m02,
                 m10, m11, m12,
                 m20, m21, m22}
        );
    }

    /**
     * Constructs a 3&times;3 matrix from the specified affine transform.
     */
    public Matrix3(final AffineTransform transform) {
        super(SIZE);
        setMatrix(transform);
    }

    /**
     * Creates a new matrix initialized to the same value than the specified one.
     * The specified matrix size must be {@value #SIZE}&times;{@value #SIZE}.
     */
    public Matrix3(final Matrix matrix) {
        super(SIZE);
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
     * Returns {@code true} if at least one value is {@code NaN}.
     *
     * @since 2.3
     */
    public final boolean isNaN() {
        return Double.isNaN(mat.data[0]) || Double.isNaN(mat.data[1]) || Double.isNaN(mat.data[2]) ||
               Double.isNaN(mat.data[3]) || Double.isNaN(mat.data[4]) || Double.isNaN(mat.data[5]) ||
               Double.isNaN(mat.data[6]) || Double.isNaN(mat.data[7]) || Double.isNaN(mat.data[8]);
    }

    /**
     * Sets this matrix to the specified affine transform.
     *
     * @since 2.3
     */
    public void setMatrix(final AffineTransform transform) {
        mat.data[0]=transform.getScaleX(); mat.data[1]=transform.getShearX(); mat.data[2]=transform.getTranslateX();
        mat.data[3]=transform.getShearY(); mat.data[4]=transform.getScaleY(); mat.data[5]=transform.getTranslateY();
        mat.data[6]=0;                     mat.data[7]=0;                     mat.data[8]=1;
    }

    /**
     * Returns {@code true} if this matrix is equals to the specified affine transform.
     *
     * @since 2.3
     */
    public boolean equalsAffine(final AffineTransform transform) {
        return mat.data[0]==transform.getScaleX() && mat.data[1]==transform.getShearX() && mat.data[2]==transform.getTranslateX() &&
               mat.data[3]==transform.getShearY() && mat.data[4]==transform.getScaleY() && mat.data[5]==transform.getTranslateY() &&
               mat.data[6]==0                     && mat.data[7]==0                     && mat.data[8]==1;
    }

     @Override
    public int hashCode() {
        return GeneralMatrix.hashCode(this);
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
        return new Matrix3(this);
    }
}
