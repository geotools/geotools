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

import org.opengis.referencing.operation.Matrix;


/**
 * Static utility methods for creating matrix. This factory selects one of the {@link Matrix1},
 * {@link Matrix2}, {@link Matrix3}, {@link Matrix4} or {@link GeneralMatrix} implementation
 * according the desired matrix size. Note that if the matrix size is know at compile time,
 * it may be more efficient to invoke directly the constructor of the appropriate class instead.
 *
 * @since 2.2
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public final class MatrixFactory {
    /**
     * Do not allows instantiation of this class.
     */
    private MatrixFactory() {
    }

    /**
     * Creates a square identity matrix of size {@code size}&nbsp;&times;&nbsp;{@code size}.
     *
     * @param size For an affine transform, this is the number of source and target dimensions + 1.
     * @return An identity matrix of the given size.
     */
    public static XMatrix create(final int size) {
        switch (size) {
            case 1:  return new Matrix1();
            case 2:  return new Matrix2();
            case 3:  return new Matrix3();
            case 4:  return new Matrix4();
            default: return new GeneralMatrix(size);
        }
    }

    /**
     * Creates a matrix of size {@code numRow}&nbsp;&times;&nbsp;{@code numCol}.
     * Elements on the diagonal <var>j==i</var> are set to 1.
     *
     * @param numRow For an affine transform, this is the number of
     *        {@linkplain org.opengis.referencing.operation.MathTransform#getTargetDimensions
     *        target dimensions} + 1.
     * @param numCol For an affine transform, this is the number of
     *        {@linkplain org.opengis.referencing.operation.MathTransform#getSourceDimensions
     *        source dimensions} + 1.
     * @return An identity matrix of the given size.
     */
    public static XMatrix create(final int numRow, final int numCol) {
        if (numRow == numCol) {
            return create(numRow);
        } else {
            return new GeneralMatrix(numRow, numCol);
        }
    }

    /**
     * Creates a new matrix which is a copy of the specified matrix.
     */
    public static XMatrix create(final Matrix matrix) {
        final int size = matrix.getNumRow();
        if (size == matrix.getNumCol()) {
            switch (size) {
                case 1:  return new Matrix1(matrix);
                case 2:  return new Matrix2(matrix);
                case 3:  return new Matrix3(matrix);
                case 4:  return new Matrix4(matrix);
            }
        }
        return new GeneralMatrix(matrix);
    }
}
