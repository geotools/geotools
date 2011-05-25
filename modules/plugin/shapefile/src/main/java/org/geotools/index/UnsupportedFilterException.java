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
package org.geotools.index;

/**
 * This exception is thrown when we are asked to use a filter that does not
 * contain a geometry literal (or anything else we can guess a bounds for).
 * 
 * @author Tommaso Nolli
 *
 * @source $URL$
 */
public class UnsupportedFilterException extends Exception {
    private static final long serialVersionUID = 3292904738782996000L;

    /**
     * 
     */
    public UnsupportedFilterException() {
        super();
    }

    /**
     * Filter is not supported.
     * 
     * @param message
     */
    public UnsupportedFilterException(String message) {
        super(message);
    }

    /**
     * Filter is not supported.
     * 
     * @param message
     * @param cause
     */
    public UnsupportedFilterException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Filter is not supported.
     * 
     * @param cause
     */
    public UnsupportedFilterException(Throwable cause) {
        super(cause);
    }
}
