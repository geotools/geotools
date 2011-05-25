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
package org.geotools.referencing.factory;

// OpenGIS dependencies
import org.opengis.referencing.FactoryException;


/**
 * Thrown when a requested factory has not been found. This exception may be thrown by
 * {@link DeferredAuthorityFactory#createBackingStore}.
 *
 * @since 2.3
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public class FactoryNotFoundException extends FactoryException {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = -661925454228937249L;

    /**
     * Construct an exception with no detail message.
     */
    public FactoryNotFoundException() {
    }

    /**
     * Construct an exception with the specified detail message.
     *
     * @param  message The detail message. The detail message is saved
     *         for later retrieval by the {@link #getMessage()} method.
     */
    public FactoryNotFoundException(String message) {
        super(message);
    }

    /**
     * Construct an exception with the specified cause. The detail message
     * is copied from the cause {@linkplain Exception#getLocalizedMessage
     * localized message}.
     *
     * @param  cause The cause for this exception. The cause is saved
     *         for later retrieval by the {@link #getCause()} method.
     */
    public FactoryNotFoundException(Exception cause) {
        super(cause.getLocalizedMessage(), cause);
    }

    /**
     * Construct an exception with the specified detail message and cause.
     * The cause is the exception thrown in the underlying database
     * (e.g. {@link java.io.IOException} or {@link java.sql.SQLException}).
     *
     * @param  message The detail message. The detail message is saved
     *         for later retrieval by the {@link #getMessage()} method.
     * @param  cause The cause for this exception. The cause is saved
     *         for later retrieval by the {@link #getCause()} method.
     */
    public FactoryNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
