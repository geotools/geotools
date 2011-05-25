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
import org.opengis.referencing.cs.UserDefinedCS;
import org.opengis.referencing.cs.CoordinateSystemAxis;


/**
 * A two- or three-dimensional coordinate system that consists of any combination of coordinate
 * axes not covered by any other Coordinate System type. An example is a multilinear coordinate
 * system which contains one coordinate axis that may have any 1-D shape which has no intersections
 * with itself. This non-straight axis is supplemented by one or two straight axes to complete a 2
 * or 3 dimensional coordinate system. The non-straight axis is typically incrementally straight or
 * curved. A {@code UserDefinedCS} shall have two or three
 * {@linkplain #getAxis axis}.
 *
 * @since 2.1
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public class DefaultUserDefinedCS extends AbstractCS implements UserDefinedCS {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = -4904091898305706316L;

    /**
     * Constructs a new coordinate system with the same values than the specified one.
     * This copy constructor provides a way to wrap an arbitrary implementation into a
     * Geotools one or a user-defined one (as a subclass), usually in order to leverage
     * some implementation-specific API. This constructor performs a shallow copy,
     * i.e. the properties are not cloned.
     *
     * @since 2.2
     */
    public DefaultUserDefinedCS(final UserDefinedCS cs) {
        super(cs);
    }

    /**
     * Constructs a two-dimensional coordinate system from a name.
     *
     * @param name  The coordinate system name.
     * @param axis0 The first axis.
     * @param axis1 The second axis.
     */
    public DefaultUserDefinedCS(final String               name,
                                final CoordinateSystemAxis axis0,
                                final CoordinateSystemAxis axis1)
    {
        super(name, new CoordinateSystemAxis[] {axis0, axis1});
    }

    /**
     * Constructs a three-dimensional coordinate system from a name.
     *
     * @param name  The coordinate system name.
     * @param axis0 The first axis.
     * @param axis1 The second axis.
     * @param axis2 The third axis.
     */
    public DefaultUserDefinedCS(final String               name,
                                final CoordinateSystemAxis axis0,
                                final CoordinateSystemAxis axis1,
                                final CoordinateSystemAxis axis2)
    {
        super(name, new CoordinateSystemAxis[] {axis0, axis1, axis2});
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
    public DefaultUserDefinedCS(final Map<String,?>   properties,
                                final CoordinateSystemAxis axis0,
                                final CoordinateSystemAxis axis1)
    {
        super(properties, new CoordinateSystemAxis[] {axis0, axis1});
    }

    /**
     * Constructs a three-dimensional coordinate system from a set of properties.
     * The properties map is given unchanged to the
     * {@linkplain AbstractCS#AbstractCS(Map,CoordinateSystemAxis[]) super-class constructor}.
     *
     * @param properties Set of properties. Should contains at least {@code "name"}.
     * @param axis0 The first axis.
     * @param axis1 The second axis.
     * @param axis2 The third axis.
     */
    public DefaultUserDefinedCS(final Map<String,?>   properties,
                                final CoordinateSystemAxis axis0,
                                final CoordinateSystemAxis axis1,
                                final CoordinateSystemAxis axis2)
    {
        super(properties, new CoordinateSystemAxis[] {axis0, axis1, axis2});
    }
}
