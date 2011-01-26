/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.metadata.sql;


/**
 * Throws when a metadata method failed. The cause for this exception
 * is typically a {@link java.sql.SQLException}.
 *
 * @since 2.1
 * @source $URL$
 * @version $Id$
 * @author Touraïvane
 * @author Martin Desruisseaux (IRD)
 */
public class MetadataException extends RuntimeException {
    /**
     * For cross-version compatibility.
     */
    private static final long serialVersionUID = -7156617726114815455L;

    /**
     * Constructs an instance of {@code MetadataException} with the specified
     * detail message.
     *
     * @param message The detail message.
     */
    public MetadataException(final String message) {
        super(message);
    }

    /**
     * Constructs an instance of {@code MetadataException} with the specified cause.
     *
     * @param message The detail message.
     * @param cause The cause of this exception.
     */
    public MetadataException(final String message, final Exception cause) {
        super(message, cause);
    }
}
