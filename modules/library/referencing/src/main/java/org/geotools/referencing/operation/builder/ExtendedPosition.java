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
package org.geotools.referencing.operation.builder;

import org.geotools.geometry.DirectPosition2D;
import org.opengis.geometry.DirectPosition;


/**
 * DirectPosition associated with another DirectPosition.
 *
 * @since 2.4
 * @source $URL$
 * @version $Id$
 * @author Jan Jezek
 */
class ExtendedPosition extends DirectPosition2D {
    /**  */
    private static final long serialVersionUID = 4400395722009854165L;

    /** Coordinate associated with original coordinate. */
    private DirectPosition mappedposition;

    /**
     * Creates a MappedPosition
     * @param c the original DirectPosition.
     * @param mappedposition the associated DirectPosition.
     */
    public ExtendedPosition(DirectPosition c, DirectPosition mappedposition) {
        super(c);
        this.mappedposition = mappedposition;
    }

    /**
     * Returns the mapped DirectPosition.
     *
     * @return this coordinate's associated coordinate
     */
    public DirectPosition getMappedposition() {
        return mappedposition;
    }

    /**
     * Sets the mapped DirectPosition.
     *
     * @param mappedCoordinate Coordinate to be mapped to the existing one.
     */
    public void setMappedposition(DirectPosition mappedCoordinate) {
        this.mappedposition = mappedCoordinate;
    }
}
