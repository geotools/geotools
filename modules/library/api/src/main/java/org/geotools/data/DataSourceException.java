/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data;


/**
 * Thrown when there is an error in a datasource.
 * <p>
 * This class was used back in Java 1.3 before the initCause() method
 * was available for IOException. Since this class is used to pass on
 * problems from external services, providing the root cause is
 * important.
 * </p>
 *
 * @source $URL$
 */
public class DataSourceException extends java.io.IOException {
    private static final long serialVersionUID = -602847953059978370L;

    /**
     * Constructs a new instance of DataSourceException
     *
     * @param msg A message explaining the exception
     */
    public DataSourceException(String msg) {
        super(msg);
    }

    /**
     * Constructs a new instance of DataSourceException
     *
     * @param cause A message explaining the exception
     */
    public DataSourceException(Throwable cause) {
        super(cause.getMessage());
        initCause(cause);
    }

    /**
     * Constructs a new instance of DataSourceException
     *
     * @param msg A message explaining the exception
     * @param cause the throwable object which caused this exception
     */
    public DataSourceException(String msg, Throwable cause) {
        super(msg);
        initCause(cause);
    }
}
