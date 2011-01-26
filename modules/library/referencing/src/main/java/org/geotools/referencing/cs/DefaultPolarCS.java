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
 *
 *    This package contains documentation from OpenGIS specifications.
 *    OpenGIS consortium's work is fully acknowledged here.
 */
package org.geotools.referencing.cs;

import java.util.Map;
import org.opengis.referencing.cs.PolarCS;
import org.opengis.referencing.cs.AxisDirection;
import org.opengis.referencing.cs.CoordinateSystemAxis;


/**
 * A two-dimensional coordinate system in which position is specified by the distance from the
 * origin and the angle between the line from the origin to a point and a reference direction.
 * A {@code PolarCS} shall have two {@linkplain #getAxis axis}.
 *
 * <TABLE CELLPADDING='6' BORDER='1'>
 * <TR BGCOLOR="#EEEEFF"><TH NOWRAP>Used with CRS type(s)</TH></TR>
 * <TR><TD>
 *   {@link org.geotools.referencing.crs.DefaultEngineeringCRS Engineering}
 * </TD></TR></TABLE>
 *
 * @since 2.1
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 *
 * @see DefaultCylindricalCS
 */
public class DefaultPolarCS extends AbstractCS implements PolarCS {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = 3960197260975470951L;

    /**
     * Constructs a new coordinate system with the same values than the specified one.
     * This copy constructor provides a way to wrap an arbitrary implementation into a
     * Geotools one or a user-defined one (as a subclass), usually in order to leverage
     * some implementation-specific API. This constructor performs a shallow copy,
     * i.e. the properties are not cloned.
     *
     * @since 2.2
     */
    public DefaultPolarCS(final PolarCS cs) {
        super(cs);
    }

    /**
     * Constructs a two-dimensional coordinate system from a name.
     *
     * @param name  The coordinate system name.
     * @param axis0 The first axis.
     * @param axis1 The second axis.
     */
    public DefaultPolarCS(final String               name,
                          final CoordinateSystemAxis axis0,
                          final CoordinateSystemAxis axis1)
    {
        super(name, new CoordinateSystemAxis[] {axis0, axis1});
    }

    /**
     * Constructs a two-dimensional coordinate system from a set of properties.
     * The properties map is given unchanged to the
     * {@linkplain AbstractCS#AbstractCS(Map,CoordinateSystemAxis[]) super-class constructor}.
     *
     * @param properties Set of properties. Should contains at least {@code "name"}.
     * @param axis0 The first axis.
     * @param axis1 The second axis.
     */
    public DefaultPolarCS(final Map<String,?>   properties,
                          final CoordinateSystemAxis axis0,
                          final CoordinateSystemAxis axis1)
    {
        super(properties, new CoordinateSystemAxis[] {axis0, axis1});
    }

    /**
     * Returns {@code true} if the specified axis direction is allowed for this coordinate
     * system. The default implementation accepts all directions except temporal ones (i.e.
     * {@link AxisDirection#FUTURE FUTURE} and {@link AxisDirection#PAST PAST}).
     */
    @Override
    protected boolean isCompatibleDirection(final AxisDirection direction) {
        return !AxisDirection.FUTURE.equals(direction.absolute());
    }
}
