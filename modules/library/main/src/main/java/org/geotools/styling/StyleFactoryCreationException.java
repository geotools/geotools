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
 * Created on 22 October 2002, 15:29
 */
package org.geotools.styling;

/**
 * An exception that can be thrown by the StyleFactory if it fails to create
 * the  implementation of the StyleFactory. $Id:
 * StyleFactoryCreationException.java,v 1.1 2002/10/22 17:02:03 ianturton Exp
 * $
 *
 * @author iant
 * @source $URL$
 */
public class StyleFactoryCreationException extends java.lang.Exception {
    private static final long serialVersionUID = -1809128211848169317L;

    /**
     * Creates a new instance of <code>StyleFactoryCreationException</code>
     * without detail message.
     */
    public StyleFactoryCreationException() {
    }

    /**
     * Constructs an instance of <code>StyleFactoryCreationException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public StyleFactoryCreationException(String msg) {
        super(msg);
    }

    public StyleFactoryCreationException(Exception e) {
        super(e);
    }

    public StyleFactoryCreationException(String msg, Exception e) {
        super(msg, e);
    }
}
