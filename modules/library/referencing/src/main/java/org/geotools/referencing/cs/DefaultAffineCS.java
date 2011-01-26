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
import javax.measure.unit.SI;
import javax.measure.unit.Unit;

import org.opengis.referencing.cs.AffineCS;
import org.opengis.referencing.cs.AxisDirection;
import org.opengis.referencing.cs.CoordinateSystemAxis;


/**
 * A two- or three-dimensional coordinate system with straight axes that are not necessarily
 * orthogonal. An {@code AffineCS} shall have two or three {@linkplain #getAxis axis}.
 *
 * <TABLE CELLPADDING='6' BORDER='1'>
 * <TR BGCOLOR="#EEEEFF"><TH NOWRAP>Used with CRS type(s)</TH></TR>
 * <TR><TD>
 *   {@link org.geotools.referencing.crs.DefaultEngineeringCRS Engineering},
 *   {@link org.geotools.referencing.crs.DefaultImageCRS       Image}
 * </TD></TR></TABLE>
 *
 * @since 2.1
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 *
 * @see DefaultCartesianCS
 */
public class DefaultAffineCS extends AbstractCS implements AffineCS {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = 7977674229369042440L;

    /**
     * Constructs a new coordinate system with the same values than the specified one.
     * This copy constructor provides a way to wrap an arbitrary implementation into a
     * Geotools one or a user-defined one (as a subclass), usually in order to leverage
     * some implementation-specific API. This constructor performs a shallow copy,
     * i.e. the properties are not cloned.
     *
     * @param cs The coordinate system to copy.
     *
     * @since 2.2
     */
    public DefaultAffineCS(final AffineCS cs) {
        super(cs);
    }

    /**
     * Constructs a two-dimensional coordinate system from a name.
     *
     * @param name  The coordinate system name.
     * @param axis0 The first axis.
     * @param axis1 The second axis.
     */
    public DefaultAffineCS(final String               name,
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
    public DefaultAffineCS(final String               name,
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
    public DefaultAffineCS(final Map<String,?>   properties,
                           final CoordinateSystemAxis axis0,
                           final CoordinateSystemAxis axis1)
    {
        super(properties, new CoordinateSystemAxis[] {axis0, axis1});
    }

    /**
     * Constructs a three-dimensional coordinate system from a set of properties.
     * The properties map is given unchanged to the superclass constructor.
     *
     * @param properties Set of properties. Should contains at least {@code "name"}.
     * @param axis0 The first axis.
     * @param axis1 The second axis.
     * @param axis2 The third axis.
     */
    public DefaultAffineCS(final Map<String,?>   properties,
                           final CoordinateSystemAxis axis0,
                           final CoordinateSystemAxis axis1,
                           final CoordinateSystemAxis axis2)
    {
        super(properties, new CoordinateSystemAxis[] {axis0, axis1, axis2});
    }

    /**
     * For {@link #usingUnit} and {@link PredefinedCS#rightHanded} usage only.
     */
    DefaultAffineCS(final Map<String,?> properties, final CoordinateSystemAxis[] axis) {
        super(properties, axis);
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

    /**
     * Returns {@code true} if the specified unit is compatible with {@linkplain SI#METER meters}.
     * In addition, this method also accepts {@link Unit#ONE}, which is used for coordinates in a
     * grid. This method is invoked at construction time for checking units compatibility.
     *
     * @since 2.2
     */
    @Override
    protected boolean isCompatibleUnit(final AxisDirection direction, final Unit<?> unit) {
        return SI.METER.isCompatible(unit) || Unit.ONE.equals(unit);
        // Note: this condition is also coded in PredefinedCS.rightHanded(AffineCS).
    }

    // TODO: Uncomment when we will be allowed to compile for J2SE 1.5
    /*
     * Returns a new coordinate system with the same properties than the current one except for
     * axis units.
     *
     * @param  unit The unit for the new axis.
     * @return A coordinate system with axis using the specified units.
     * @throws IllegalArgumentException If the specified unit is incompatible with the expected one.
     *
     * @since 2.5
     *
    public DefaultAffineCS usingUnit(final Unit unit) throws IllegalArgumentException {
        final CoordinateSystemAxis[] axis = axisUsingUnit(unit);
        if (axis == null) {
            return this;
        }
        return new DefaultAffineCS(getProperties(this, null), axis);
    }
     */
}
