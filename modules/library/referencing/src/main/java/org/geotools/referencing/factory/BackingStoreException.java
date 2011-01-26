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
package org.geotools.referencing.factory;

// J2SE direct dependencies
import java.io.IOException;    // For javadoc
import java.sql.SQLException;  // For javadoc


/**
 * Thrown to indicate that an {@link IdentifiedObjectSet} operation could not complete because of a
 * failure in the backing store, or a failure to contact the backing store. This exception usually
 * has an {@link IOException} or a {@link SQLException} as its {@linkplain #getCause cause}.
 *
 * @since 2.3
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public class BackingStoreException extends RuntimeException {
    /**
     * Serial version UID allowing cross compiler use of {@code BackingStoreException}.
     */
    private static final long serialVersionUID = 4257200758051575441L;

    /**
     * Constructs a new exception with no detail message.
     */
    public BackingStoreException() {
    }

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message the detail message, saved for later retrieval by the {@link #getMessage} method.
     */
    public BackingStoreException(final String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified cause.
     *
     * @param cause the cause, saved for later retrieval by the {@link Throwable#getCause} method.
     */
    public BackingStoreException(final Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new exception with the specified detail message and cause.
     *
     * @param message the detail message, saved for later retrieval by the {@link #getMessage} method.
     * @param cause the cause, saved for later retrieval by the {@link Throwable#getCause} method.
     */
    public BackingStoreException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
