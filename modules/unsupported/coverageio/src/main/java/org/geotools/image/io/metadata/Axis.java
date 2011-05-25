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
package org.geotools.image.io.metadata;

import java.util.Date;
import org.opengis.referencing.cs.CoordinateSystemAxis;


/**
 * An {@code <Axis>} element in
 * {@linkplain GeographicMetadataFormat geographic metadata format}.
 *
 * @since 2.4
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 *
 * @see CoordinateSystemAxis
 */
public class Axis extends MetadataAccessor {
    /**
     * Creates a parser for an axis. This constructor should not be invoked
     * directly; use {@link ImageReferencing#getAxis} instead.
     *
     * @param metadata  The metadata which contains this axis.
     * @param index The band index for this instance.
     */
    protected Axis(final ImageReferencing metadata, final int index) {
        super(metadata.cs);
        selectChild(index);
    }

    /**
     * Creates a parser for an axis. This constructor should not be invoked
     * directly; use {@link ImageReferencing#getAxis} instead.
     *
     * @param parent The set of all axis.
     * @param index  The axis index for this instance.
     */
    Axis(final ChildList<Axis> parent, final int index) {
        super(parent);
        selectChild(index);
    }

    /**
     * Returns the name for this axis, or {@code null} if none.
     */
    public String getName() {
        return getAttributeAsString("name");
    }

    /**
     * Sets the name for this axis.
     *
     * @param name The axis name, or {@code null} if none.
     */
    public void setName(final String name) {
        setAttributeAsString("name", name);
    }

    /**
     * Returns the direction for this axis, or {@code null} if none.
     */
    public String getDirection() {
        return getAttributeAsString("direction");
    }

    /**
     * Sets the direction for this axis.
     *
     * @param direction The axis direction, or {@code null} if none.
     */
    public void setDirection(final String direction) {
        setAttributeAsEnum("direction", direction, GeographicMetadataFormat.DIRECTIONS);
    }

    /**
     * Returns the units for this axis, or {@code null} if none.
     */
    public String getUnits() {
        return getAttributeAsString("units");
    }

    /**
     * Sets the units for this axis.
     *
     * @param units The axis units, or {@code null} if none.
     */
    public void setUnits(final String units) {
        setAttributeAsString("units", units);
    }

    /**
     * Returns the time origin for this axis, or {@code null} if none.
     */
    public Date getTimeOrigin() {
        return getAttributeAsDate("origin");
    }

    /**
     * Sets the time origin for this axis.
     *
     * @param origin The axis time origin, or {@code null} if none.
     */
    public void setTimeOrigin(final Date origin) {
        setAttributeAsDate("origin", origin);
    }
}
