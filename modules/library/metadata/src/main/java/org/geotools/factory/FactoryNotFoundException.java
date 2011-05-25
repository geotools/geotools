/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.factory;


/**
 * Thrown when a factory can't be found in the registery.
 *
 * @since 2.1
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
public class FactoryNotFoundException extends FactoryRegistryException {
    /**
     * For cross-version compatibility.
     */
    private static final long serialVersionUID = 7555229653402417318L;

    /**
     * Creates a new exception with the specified detail message.
     */
    public FactoryNotFoundException(final String message) {
        super(message);
    }

    /**
     * Creates a new exception with the specified detail message and cause.
     */
    public FactoryNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
