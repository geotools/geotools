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
package org.geotools.data.jdbc;

/**
 * Indicates a client class has attempted to encode a filter not supported by the SQLEncoder being used, or that there
 * were io problems.
 *
 * @author Chris Holmes, TOPP
 */
public class FilterToSQLException extends Exception {
    private static final long serialVersionUID = -2394509611777950167L;

    /**
     * Constructor with message argument.
     *
     * @param message Reason for the exception being thrown
     */
    public FilterToSQLException(String message) {
        super(message);
    }

    /**
     * Constructs a new instance of DataSourceException
     *
     * @param msg A message explaining the exception
     * @param exp the throwable object which caused this exception
     */
    public FilterToSQLException(String msg, Throwable exp) {
        super(msg, exp);
    }
}
