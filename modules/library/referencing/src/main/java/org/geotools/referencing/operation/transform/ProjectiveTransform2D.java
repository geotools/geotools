/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.operation.transform;

import org.opengis.referencing.operation.Matrix;
import org.opengis.referencing.operation.MathTransform2D;
import org.opengis.referencing.operation.NoninvertibleTransformException;


/**
 * Projective transform in 2D case.
 *
 * @source $URL$
 * @version $Id$
 * @author Jan Jezek
 */
final class ProjectiveTransform2D extends ProjectiveTransform implements MathTransform2D {
    /**
     * For cross-version compatibility.
     */
    private static final long serialVersionUID = -3101392684596817045L;

    /**
     * Creates projective transform from a matrix.
     */
    public ProjectiveTransform2D(final Matrix matrix) {
        super(matrix);
    }

    /**
     * Creates the inverse transform of this object.
     */
    @Override
    public MathTransform2D inverse() throws NoninvertibleTransformException {
        return (MathTransform2D) super.inverse();
    }

    /**
     * Creates an inverse transform using the specified matrix.
     */
    @Override
    MathTransform2D createInverse(final Matrix matrix) {
        return new ProjectiveTransform2D(matrix);
    }
}
