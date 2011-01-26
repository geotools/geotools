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
import org.geotools.util.Utilities;


/**
 * An affine matrix of fixed {@value #SIZE}&times;{@value #SIZE} size. Here, the term "affine"
 * means a matrix with the last row fixed to {@code [0,0,1]} values. Such matrices are used for
 * affine transformations in a 2D space.
 * <p>
 * This class both extends the <cite>Java2D</cite> {@link AffineTransform} class and implements
 * the {@link Matrix} interface. It allows interoperbility for code that need to pass the same
 * matrix to both <cite>Java2D</cite> API and more generic API working with coordinates of
 * arbitrary dimension.
 * <p>
 * This class do not implements the {@link XMatrix} interface because the inherited {@code invert()}
 * method (new in J2SE 1.6) declares a checked exception, {@code setZero()} would be an unsupported
 * operation (because it is not possible to change the value at {@code (2,2)}), {@code transpose()}
 * would fails in most cases, and {@code isAffine()} would be useless.
 *
 * @since 2.3
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public class AffineTransform2D extends AffineTransform implements Matrix {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = -9104194268576601386L;

    /**
     * The matrix size, which is {@value}.
     */
    public static final int SIZE = 3;

    /**
     * Creates a new identity matrix.
     */
    public AffineTransform2D() {
    }

    /**
     * Constructs a 3&times;3 matrix from the specified affine transform.
     */
    public AffineTransform2D(final AffineTransform transform) {
        super(transform);
    }

    /**
     * Creates a new matrix initialized to the same value than the specified one.
     * The specified matrix size must be {@value #SIZE}&times;{@value #SIZE}.
     */
    public AffineTransform2D(final Matrix matrix) {
        if (matrix.getNumRow()!=SIZE || matrix.getNumCol()!=SIZE) {
            throw new IllegalArgumentException(Errors.format(ErrorKeys.ILLEGAL_MATRIX_SIZE));
        }
        for (int i=0; i<SIZE; i++) {
            checkLastRow(i, matrix.getElement(SIZE-1, i));
        }
        int c=0;
        final double[] values = new double[6];
        for (int j=0; j<SIZE-1; j++) {
            for (int i=0; i<SIZE; i++) {
                values[c++] = matrix.getElement(j,i);
            }
        }
        assert c == values.length : c;
        // TODO: invokes the super constructor instead if Sun fixes RFE #4093999
        setTransform(values);
    }

    /**
     * Sets this affine transform to the specified flat matrix.
     */
    private void setTransform(final double[] matrix) {
        setTransform(matrix[0], matrix[1], matrix[2], matrix[3], matrix[4], matrix[5]);
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
     * Retrieves the value at the specified row and column of this matrix.
     *
     * @param row    The row number to be retrieved (zero indexed).
     * @param column The column number to be retrieved (zero indexed).
     * @return The value at the indexed element.
     */
    public double getElement(final int row, final int column) {
        switch (row) {
            case 0: {
                switch (column) {
                    case 0: return getScaleX();
                    case 1: return getShearX();
                    case 2: return getTranslateX();
                }
                break;
            }
            case 1: {
                switch (column) {
                    case 0: return getShearY();
                    case 1: return getScaleY();
                    case 2: return getTranslateY();
                }
                break;
            }
            case 2: {
                switch (column) {
                    case 0: // fall through
                    case 1: return 0;
                    case 2: return 1;
                }
                break;
            }
            default: {
                throw new IndexOutOfBoundsException(
                        Errors.format(ErrorKeys.ILLEGAL_ARGUMENT_$2, "column", column));
            }
        }
        throw new IndexOutOfBoundsException(Errors.format(
                ErrorKeys.ILLEGAL_ARGUMENT_$2, "row", row));
    }

    /**
     * Modifies the value at the specified row and column of this matrix.
     *
     * @param row    The row number to be retrieved (zero indexed).
     * @param column The column number to be retrieved (zero indexed).
     * @param value  The new matrix element value.
     */
    public void setElement(final int row, final int column, final double value) {
        if (row<0 || row>=SIZE) {
            throw new IndexOutOfBoundsException(Errors.format(
                    ErrorKeys.ILLEGAL_ARGUMENT_$2, "row", row));
        }
        if (column<0 || column>=SIZE) {
            throw new IndexOutOfBoundsException(Errors.format(
                    ErrorKeys.ILLEGAL_ARGUMENT_$2, "column", column));
        }
        if (row == SIZE-1) {
            checkLastRow(column, value);
            return; // Nothing to set.
        }
        final double[] matrix = new double[6];
        getMatrix(matrix);
        matrix[row*SIZE + column] = value;
        setTransform(matrix);
        assert Double.compare(getElement(row, column), value) == 0 : value;
    }

    /**
     * Check if the specified value is valid for the last row if this matrix.
     * The last row contains only 0 values except the last column which is set to 1.
     * This method throws an exception if the specified value is not the expected one.
     */
    private static void checkLastRow(final int column, final double value)
            throws IllegalArgumentException
    {
        if (value != (column == SIZE-1 ? 1 : 0)) {
            throw new IllegalArgumentException(Errors.format(ErrorKeys.ILLEGAL_ARGUMENT_$2,
                      "matrix[" + (SIZE-1) + ',' + column + ']', value));
        }
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
     * Returns a clone of this affine transform.
     */
    @Override
    public AffineTransform2D clone() {
        return (AffineTransform2D) super.clone();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof AffineTransform)) {
            return false;
        }

        AffineTransform a = (AffineTransform) obj;
        
        return Utilities.equals(getScaleX(), a.getScaleX()) &&
            Utilities.equals(getScaleY(), a.getScaleY()) &&
            Utilities.equals(getShearX(), a.getShearY()) &&
            Utilities.equals(getTranslateX(), a.getTranslateX()) &&
            Utilities.equals(getTranslateY(), a.getTranslateY());
    }
}
