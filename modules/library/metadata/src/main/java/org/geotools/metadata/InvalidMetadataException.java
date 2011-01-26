/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.metadata;


/**
 * Thrown when a {@linkplain org.geotools.metadata.iso.MetadataEntity metadata entity}
 * is in a invalid state, usually because a mandatory attribute is missing.
 *
 * @since 2.4
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (Geomatys)
 */
public class InvalidMetadataException extends IllegalStateException {
    /**
     * For cross-version compatibility.
     */
    private static final long serialVersionUID = 3219759595538181102L;

    /**
     * Creates a new exception with the specified detail message.
     *
     * @param message The detail message.
     */
    public InvalidMetadataException(final String message) {
        super(message);
    }
}
