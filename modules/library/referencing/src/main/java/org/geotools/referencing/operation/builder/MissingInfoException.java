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
package org.geotools.referencing.operation.builder;

import org.opengis.referencing.FactoryException;

/**
 * Thrown when a required operation can't be performed because some information is missing or isn't
 * set up properly.
 *
 * @since 2.4
 * @source $URL$
 * @version $Id$
 * @author Jan Jezek
 */
public class MissingInfoException extends FactoryException {

    /** Serial number for interoperability with different versions. */
    private static final long serialVersionUID = -3128525157353302290L;

    /**
     * Constructs an exception with the specified detail message.
     *
     * @param message The cause for this exception. The cause is saved for later retrieval by the
     *     {@link #getCause()} method.
     */
    public MissingInfoException(String message) {
        super(message);
    }

    /** Constructs an exception with no detail message. */
    public MissingInfoException() {
        super();
    }
}
