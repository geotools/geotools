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

import org.opengis.referencing.cs.VerticalCS;
import org.opengis.referencing.cs.AxisDirection;
import org.opengis.referencing.cs.CoordinateSystemAxis;
import org.opengis.geometry.MismatchedDimensionException;

import org.geotools.measure.Measure;


/**
 * A one-dimensional coordinate system used to record the heights (or depths) of points. Such a
 * coordinate system is usually dependent on the Earth's gravity field, perhaps loosely as when
 * atmospheric pressure is the basis for the vertical coordinate system axis. An exact definition
 * is deliberately not provided as the complexities of the subject fall outside the scope of this
 * specification. A {@code VerticalCS} shall have one {@linkplain #getAxis axis}.
 *
 * <TABLE CELLPADDING='6' BORDER='1'>
 * <TR BGCOLOR="#EEEEFF"><TH NOWRAP>Used with CRS type(s)</TH></TR>
 * <TR><TD>
 *   {@link org.geotools.referencing.crs.DefaultVerticalCRS    Vertical},
 *   {@link org.geotools.referencing.crs.DefaultEngineeringCRS Engineering}
 * </TD></TR></TABLE>
 *
 * @since 2.1
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public class DefaultVerticalCS extends AbstractCS implements VerticalCS {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = 1201155778896630499L;;

    /**
     * A one-dimensional vertical CS with
     * <var>{@linkplain DefaultCoordinateSystemAxis#ELLIPSOIDAL_HEIGHT
     * ellipsoidal height}</var> axis in metres.
     */
    public static DefaultVerticalCS ELLIPSOIDAL_HEIGHT = new DefaultVerticalCS(
                    DefaultCoordinateSystemAxis.ELLIPSOIDAL_HEIGHT);

    /**
     * A one-dimensional vertical CS with
     * <var>{@linkplain DefaultCoordinateSystemAxis#GRAVITY_RELATED_HEIGHT
     * gravity-related height}</var> axis in metres.
     *
     * @since 2.5
     */
    public static DefaultVerticalCS GRAVITY_RELATED_HEIGHT = new DefaultVerticalCS(
                    DefaultCoordinateSystemAxis.GRAVITY_RELATED_HEIGHT);

    /**
     * @deprecated Renamed as {@link #GRAVITY_RELATED_HEIGHT}.
     */
    @Deprecated
    public static DefaultVerticalCS GRAVITY_RELATED = GRAVITY_RELATED_HEIGHT;

    /**
     * A one-dimensional vertical CS with
     * <var>{@linkplain DefaultCoordinateSystemAxis#DEPTH depth}</var>
     * axis in metres.
     */
    public static DefaultVerticalCS DEPTH = new DefaultVerticalCS(
                    DefaultCoordinateSystemAxis.DEPTH);

    /**
     * Constructs a new coordinate system with the same values than the specified one.
     * This copy constructor provides a way to wrap an arbitrary implementation into a
     * Geotools one or a user-defined one (as a subclass), usually in order to leverage
     * some implementation-specific API. This constructor performs a shallow copy,
     * i.e. the properties are not cloned.
     *
     * @since 2.2
     */
    public DefaultVerticalCS(final VerticalCS cs) {
        super(cs);
    }

    /**
     * Constructs a coordinate system with the same properties than the specified axis.
     * The inherited properties include the {@linkplain #getName name} and aliases.
     *
     * @param axis The axis.
     *
     * @since 2.5
     */
    public DefaultVerticalCS(final CoordinateSystemAxis axis) {
        super(getProperties(axis), new CoordinateSystemAxis[] {axis});
    }

    /**
     * Constructs a coordinate system from a name.
     *
     * @param name  The coordinate system name.
     * @param axis  The axis.
     */
    public DefaultVerticalCS(final String name, final CoordinateSystemAxis axis) {
        super(name, new CoordinateSystemAxis[] {axis});
    }

    /**
     * Constructs a coordinate system from a set of properties. The properties map is given unchanged
     * to the {@linkplain AbstractCS#AbstractCS(Map,CoordinateSystemAxis[]) super-class constructor}.
     *
     * @param properties Set of properties. Should contains at least {@code "name"}.
     * @param axis       The axis.
     */
    public DefaultVerticalCS(final Map<String,?> properties, final CoordinateSystemAxis axis) {
        super(properties, new CoordinateSystemAxis[] {axis});
    }

    /**
     * Returns {@code true} if the specified axis direction is allowed for this coordinate
     * system. The default implementation accepts only vertical directions (i.e.
     * {@link AxisDirection#UP UP} and {@link AxisDirection#DOWN DOWN}).
     */
    @Override
    protected boolean isCompatibleDirection(final AxisDirection direction) {
        return AxisDirection.UP.equals(direction.absolute());
    }

    /**
     * Computes the distance between two points.
     *
     * @param  coord1 Coordinates of the first point.
     * @param  coord2 Coordinates of the second point.
     * @return The distance between {@code coord1} and {@code coord2}.
     * @throws MismatchedDimensionException if a coordinate doesn't have the expected dimension.
     */
    @Override
    public Measure distance(final double[] coord1, final double[] coord2)
            throws MismatchedDimensionException
    {
        ensureDimensionMatch("coord1", coord1);
        ensureDimensionMatch("coord2", coord2);
        return new Measure(Math.abs(coord1[0] - coord2[0]), getDistanceUnit());
    }
}
