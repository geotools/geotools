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
package org.geotools.xml.filter;


/**
 * Indicates an illegal filter configuration according to the compliance level set.
 *
 * @author Jesse
 * @since 2.2.1
 *
 *
 * @source $URL$
 */
public class UnsupportedFilterException extends RuntimeException {
    private static final long serialVersionUID = 1522023598004933175L;

    public UnsupportedFilterException(String message, Exception e) {
        super(message, e);
    }

    public UnsupportedFilterException(String string) {
        super(string);
    }
}
