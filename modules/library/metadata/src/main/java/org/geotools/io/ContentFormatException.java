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
package org.geotools.io;

import java.io.IOException;


/**
 * Throws when a stream can't be parsed because some content uses an invalid format.
 * This exception typically has a {@link java.text.ParseException} has its cause.
 * It is similar in spirit to {@link java.util.InvalidPropertiesFormatException}.
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 *
 * @since 2.2
 *
 * @see java.util.InvalidPropertiesFormatException
 */
public class ContentFormatException extends IOException {
    /**
     * Serial version for compatibility with different versions.
     */
    private static final long serialVersionUID = 6152194019351374599L;

    /**
     * Constructs a new exception with the specified detail message.
     * The detail message is saved for later retrieval by the {@link #getMessage()} method.
     */
    public ContentFormatException(final String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified detail message and cause.
     * The cause is saved for later retrieval by the {@link #getCause()} method.
     */
    public ContentFormatException(final String message, final Throwable cause) {
        super(message);
        initCause(cause);
        // TODO: use super(message, cause) when we will be allowed to compile for Java 6.
    }
}
