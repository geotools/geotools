/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.processing;


/**
 * Throws when a "scale" operation has been requested
 * but the specified grid coverage can't be scaled.
 *
 * @source $URL$
 * @version $Id$
 * @author Simone Giannecchini
 *
 * @since 2.3
 */
public class CannotScaleException extends CoverageProcessingException {
    /**
     * Serial number for interoperability with different versions.
     */
	private static final long serialVersionUID = 8644771885589352455L;

    /**
     * Creates a new exception without detail message.
     */
	public CannotScaleException() {
		super();
	}

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message the detail message.
     */
	public CannotScaleException(String message) {
		super(message);
	}

    /**
     * Constructs a new exception with the specified cause.
     *
     * @param cause The cause of this exception.
     */
	public CannotScaleException(Throwable cause) {
		super(cause);
	}

    /**
     * Constructs a new exception with the specified detail message and cause.
     *
     * @param message the detail message.
     * @param cause The cause of this exception.
     */
	public CannotScaleException(String message, Throwable cause) {
		super(message, cause);
	}
}
