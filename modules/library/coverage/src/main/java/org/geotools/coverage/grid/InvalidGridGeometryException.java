/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.grid;

import org.geotools.resources.i18n.Errors;


/**
 * Thrown by {@link GeneralGridGeometry} when a grid geometry is in an invalid state. For example
 * this exception is thrown when {@link GeneralGridGeometry#getGridRange() getGridRange()} is
 * invoked while the grid geometry were built with a null
 * {@link org.opengis.coverage.grid.GridRange}.
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 *
 * @since 2.1
 */
public class InvalidGridGeometryException extends IllegalStateException {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = -7386283388753448743L;

    /**
     * Constructs an exception with no detail message.
     */
    public InvalidGridGeometryException() {
    }

    /**
     * Constructs an exception with a detail message from the specified error code.
     * Should not be public because the GeoTools I18N framework is not a commited one.
     */
    InvalidGridGeometryException(final int code) {
        super(Errors.format(code));
    }

    /**
     * Constructs an exception with the specified detail message.
     */
    public InvalidGridGeometryException(final String message) {
        super(message);
    }

    /**
     * Constructs an exception with the specified detail message and cause.
     */
    public InvalidGridGeometryException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
