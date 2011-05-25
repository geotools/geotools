/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2001-2008, Open Source Geospatial Foundation (OSGeo)
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
 *
 *    This package contains documentation from OpenGIS specifications.
 *    OpenGIS consortium's work is fully acknowledged here.
 */
package org.geotools.referencing.operation;

import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.Matrix;
import org.geotools.referencing.operation.matrix.XMatrix;


/**
 * Interface for linear {@link MathTransform}s.  A linear transform can be express as an affine
 * transform using a {@linkplain #getMatrix matrix}. The {@linkplain Matrix#getNumCol number of
 * columns} is equals to the number of {@linkplain #getSourceDimensions source dimensions} plus 1,
 * and the {@linkplain Matrix#getNumRow number of rows} is equals to the number of
 * {@linkplain #getTargetDimensions target dimensions} plus 1.
 *
 * @since 2.0
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public interface LinearTransform extends MathTransform {
    /**
     * Returns this transform as an affine transform matrix.
     *
     * @return A copy of the underlying matrix.
     */
    Matrix getMatrix();

    /**
     * Tests whether this transform does not move any points, by using the provided
     * {@code tolerance} value. The signification of <cite>tolerance value</cite> is
     * the same than in the following pseudo-code:
     *
     * <blockquote><pre>
     * {@linkplain #getMatrix()}.{@linkplain XMatrix#isIdentity(double) isIdentity}(tolerance);
     * </pre></blockquote>
     *
     * @param tolerance The tolerance factor.
     * @return {@code true} if this transform is the identity one
     *
     * @since 2.4
     */
    boolean isIdentity(double tolerance);
}
