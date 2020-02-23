/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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

import java.awt.geom.NoninvertibleTransformException;

/**
 * Matrix is singular, and thus an inverse is not available.
 *
 * <p>This is a Throwable version of !@link java.awt.geom.NoninvertibleTransformException}
 *
 * @see java.awt.geom.NoninvertibleTransformException
 * @author jody
 */
public class SingularMatrixException extends RuntimeException {
    private static final long serialVersionUID = 7539276472682701858L;

    /** Matrix is singular (often indicating an inverse is not available) */
    public SingularMatrixException(String message) {
        super(message);
    }

    /** Construct using provided message and cause */
    public SingularMatrixException(String message, Throwable cause) {
        super(message, cause);
    }

    public SingularMatrixException(NoninvertibleTransformException nonInvertable) {
        super(nonInvertable.getMessage(), nonInvertable);
    }
}
