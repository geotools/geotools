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
package org.geotools.feature;


/**
 * Indicates client class has attempted to create an invalid schema.
 * @source $URL$
 */
public class SchemaException extends Exception {
    /**
     *
     */
    private static final long serialVersionUID = 5453903672192802976L;

    public SchemaException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructor with message argument.
     *
     * @param message Reason for the exception being thrown
     */
    public SchemaException(String message) {
        super(message);
    }

    /**
     * Constructor with message argument and cause.
     *
     * @param message Reason for the exception being thrown
     * @param cause Cause of SchemaException
     */
    public SchemaException(String message, Throwable cause) {
        super(message, cause);
    }
}
