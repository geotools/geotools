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
 * 
 *    Created on 22 October 2002, 15:29
 */
package org.geotools.filter;

/**
 * An exception that can be thrown by the StyleFactory if it fails to create
 * the  implementation of the StyleFactory.
 *
 * @author Ian Turton, CCG
 * @source $URL$
 * @version $Id$
 */
public class FilterFactoryCreationException extends java.lang.Exception {
    /**
     * Constructs an instance of <code>StyleFactoryCreationException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public FilterFactoryCreationException(String msg) {
        super(msg);
    }

    /**
     * Constructs an instance of <code>FilterFactoryCreationException</code>
     * with the specified root cause.
     *
     * @param cause the root cause of the exceptions.
     */
    public FilterFactoryCreationException(Exception cause) {
        super(cause);
    }

    /**
     * Constructs an instance of <code>FilterFactoryCreationException</code>
     * with the specified detail message and root cause.
     *
     * @param msg the detail message.
     * @param cause the root cause of the exceptions.
     */
    public FilterFactoryCreationException(String msg, Exception cause) {
        super(msg, cause);
    }
}
