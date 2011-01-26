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
package org.geotools.gml;

/**
 * An exception used to represent any GML related errors.
 *
 * @author Ian Turton, CCG
 * @source $URL$
 * @version $Id$
 */
public class GMLException extends java.lang.Exception {
    /**
     * Creates a new instance of <code>GMLException</code> without detail
     * message.
     */
    public GMLException() {
    }

    /**
     * Constructs an instance of <code>GMLException</code>  with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public GMLException(String msg) {
        super(msg);
    }
}
