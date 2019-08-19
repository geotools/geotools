/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011-2015, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.referencing.operation;

import static org.opengis.annotation.Specification.*;

import org.opengis.annotation.Extension;
import org.opengis.annotation.UML;
import org.opengis.util.Cloneable;

/**
 * A two dimensional array of numbers. Row and column numbering begins with zero.
 *
 * @version <A HREF="http://www.opengis.org/docs/01-009.pdf">Implementation specification 1.0</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 1.0
 * @see java.awt.geom.AffineTransform
 * @see <A HREF="http://ejml.org/"EJML</A>
 */
@UML(identifier = "PT_Matrix", specification = OGC_01009)
public interface Matrix extends Cloneable {
    /**
     * Returns the number of rows in this matrix.
     *
     * @return The number of rows in this matrix.
     */
    @Extension
    int getNumRow();
    // Same signature than GMatrix, for straightforward implementation.

    /**
     * Returns the number of columns in this matrix.
     *
     * @return The number of columns in this matrix.
     */
    @Extension
    int getNumCol();
    // Same signature than GMatrix, for straightforward implementation.

    /**
     * Retrieves the value at the specified row and column of this matrix.
     *
     * @param row The row number to be retrieved (zero indexed).
     * @param column The column number to be retrieved (zero indexed).
     * @return The value at the indexed element.
     */
    @Extension
    double getElement(int row, int column);

    /**
     * Modifies the value at the specified row and column of this matrix.
     *
     * @param row The row number to be retrieved (zero indexed).
     * @param column The column number to be retrieved (zero indexed).
     * @param value The new matrix element value.
     */
    @Extension
    void setElement(int row, int column, double value);

    /**
     * Returns {@code true} if this matrix is an identity matrix.
     *
     * @return {@code true} if this matrix is an identity matrix.
     */
    @Extension
    boolean isIdentity();

    /**
     * Returns a clone of this matrix.
     *
     * @return A clone of this matrix.
     */
    Matrix clone();
}
