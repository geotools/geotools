/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.referencing.operation;

import org.opengis.util.Cloneable;
import org.opengis.annotation.UML;
import org.opengis.annotation.Extension;

import static org.opengis.annotation.Specification.*;


/**
 * A two dimensional array of numbers. Row and column numbering begins with zero. The API for
 * this interface matches closely the API in various {@linkplain javax.vecmath.GMatrix matrix}
 * implementations available in <A HREF="http://java.sun.com/products/java-media/3D/">Java3D</A>,
 * which should enable straightforward implementations. Java3D provides matrix for the general
 * case and optimized versions for 3&times;3 and 4&times;4 cases, which are quite common in a
 * transformation package.
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/referencing/operation/Matrix.java $
 * @version <A HREF="http://www.opengis.org/docs/01-009.pdf">Implementation specification 1.0</A>
 * @author  Martin Desruisseaux (IRD)
 * @since   GeoAPI 1.0
 *
 * @see javax.vecmath.Matrix3d
 * @see javax.vecmath.Matrix4d
 * @see javax.vecmath.GMatrix
 * @see java.awt.geom.AffineTransform
 * @see javax.media.jai.PerspectiveTransform
 * @see javax.media.j3d.Transform3D
 * @see <A HREF="http://math.nist.gov/javanumerics/jama/">Jama matrix</A>
 * @see <A HREF="http://jcp.org/jsr/detail/83.jsp">JSR-83 Multiarray package</A>
 */
@UML(identifier="PT_Matrix", specification=OGC_01009)
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
     * @param row    The row number to be retrieved (zero indexed).
     * @param column The column number to be retrieved (zero indexed).
     * @return The value at the indexed element.
     */
    @Extension
    double getElement(int row, int column);
    // Same signature than GMatrix, for straightforward implementation.

    /**
     * Modifies the value at the specified row and column of this matrix.
     *
     * @param row    The row number to be retrieved (zero indexed).
     * @param column The column number to be retrieved (zero indexed).
     * @param value  The new matrix element value.
     */
    @Extension
    void setElement(int row, int column, double value);
    // Same signature than GMatrix, for straightforward implementation.

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
