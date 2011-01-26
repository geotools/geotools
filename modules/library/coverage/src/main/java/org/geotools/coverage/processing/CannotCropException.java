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
 * Throws when a "crop" operation has been requested
 * but the specified grid coverage can't be scaled.
 *
 * @source $URL$
 * @version $Id$
 * @author Simone Giannecchini
 *
 * @since 2.3
 */
public class CannotCropException extends CoverageProcessingException {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = -4382377333378224973L;

    /**
     * Creates a new exception without detail message.
     */
    public CannotCropException() {
    }

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message the detail message.
     */
    public CannotCropException(String message) {
		super(message);
	}

    /**
     * Constructs a new exception with the specified detail message and cause.
     *
     * @param message the detail message.
     * @param cause The cause of this exception.
     */
	public CannotCropException(String message, Throwable exception) {
		super(message,exception);
	}
}
