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
import javax.measure.converter.UnitConverter;
import javax.measure.unit.NonSI;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;

import org.opengis.referencing.cs.EllipsoidalCS;
import org.opengis.referencing.cs.AxisDirection;
import org.opengis.referencing.cs.CoordinateSystemAxis;
import org.opengis.geometry.MismatchedDimensionException;

import org.geotools.resources.i18n.Errors;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.VocabularyKeys;


/**
 * A two- or three-dimensional coordinate system in which position is specified by geodetic
 * latitude, geodetic longitude, and (in the three-dimensional case) ellipsoidal height. An
 * {@code EllipsoidalCS} shall have two or three {@linkplain #getAxis axis}.
 *
 * <TABLE CELLPADDING='6' BORDER='1'>
 * <TR BGCOLOR="#EEEEFF"><TH NOWRAP>Used with CRS type(s)</TH></TR>
 * <TR><TD>
 *   {@link org.geotools.referencing.crs.DefaultGeographicCRS  Geographic},
 *   {@link org.geotools.referencing.crs.DefaultEngineeringCRS Engineering}
 * </TD></TR></TABLE>
 *
 * @since 2.1
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public class DefaultEllipsoidalCS extends AbstractCS implements EllipsoidalCS {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = -1452492488902329211L;

    /**
     * A two-dimensional ellipsoidal CS with
     * <var>{@linkplain DefaultCoordinateSystemAxis#GEODETIC_LONGITUDE geodetic longitude}</var>,
     * <var>{@linkplain DefaultCoordinateSystemAxis#GEODETIC_LATITUDE geodetic latitude}</var>
     * axis in decimal degrees.
     */
    public static DefaultEllipsoidalCS GEODETIC_2D = new DefaultEllipsoidalCS(
                    name(VocabularyKeys.GEODETIC_2D),
                    DefaultCoordinateSystemAxis.GEODETIC_LONGITUDE,
                    DefaultCoordinateSystemAxis.GEODETIC_LATITUDE);

    /**
     * A three-dimensional ellipsoidal CS with
     * <var>{@linkplain DefaultCoordinateSystemAxis#GEODETIC_LONGITUDE geodetic longitude}</var>,
     * <var>{@linkplain DefaultCoordinateSystemAxis#GEODETIC_LATITUDE geodetic latitude}</var>,
     * <var>{@linkplain DefaultCoordinateSystemAxis#ELLIPSOIDAL_HEIGHT ellipsoidal height}</var>
     * axis.
     */
    public static DefaultEllipsoidalCS GEODETIC_3D = new DefaultEllipsoidalCS(
                    name(VocabularyKeys.GEODETIC_3D),
                    DefaultCoordinateSystemAxis.GEODETIC_LONGITUDE,
                    DefaultCoordinateSystemAxis.GEODETIC_LATITUDE,
                    DefaultCoordinateSystemAxis.ELLIPSOIDAL_HEIGHT);

    /**
     * The axis number for longitude, latitude and height.
     * Will be constructed only when first needed.
     */
    private transient int longitudeAxis, latitudeAxis, heightAxis;

    /**
     * The unit converters for longitude, latitude and height.
     * Will be constructed only when first needed.
     */
    private transient UnitConverter longitudeConverter, latitudeConverter, heightConverter;

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
    public DefaultEllipsoidalCS(final EllipsoidalCS cs) {
        super(cs);
    }

    /**
     * Constructs a two-dimensional coordinate system from a name.
     *
     * @param name  The coordinate system name.
     * @param axis0 The first axis.
     * @param axis1 The second axis.
     */
    public DefaultEllipsoidalCS(final String               name,
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
    public DefaultEllipsoidalCS(final String               name,
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
    public DefaultEllipsoidalCS(final Map<String,?>   properties,
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
    public DefaultEllipsoidalCS(final Map<String,?>   properties,
                                final CoordinateSystemAxis axis0,
                                final CoordinateSystemAxis axis1,
                                final CoordinateSystemAxis axis2)
    {
        super(properties, new CoordinateSystemAxis[] {axis0, axis1, axis2});
    }

    /**
     * For {@link #usingUnit} usage only.
     */
    private DefaultEllipsoidalCS(final Map<String,?> properties, final CoordinateSystemAxis[] axis) {
        super(properties, axis);
    }

    /**
     * Returns {@code true} if the specified axis direction is allowed for this coordinate
     * system. The default implementation accepts only the following directions:
     * {@link AxisDirection#NORTH NORTH}, {@link AxisDirection#SOUTH SOUTH},
     * {@link AxisDirection#EAST  EAST},  {@link AxisDirection#WEST  WEST},
     * {@link AxisDirection#UP    UP} and {@link AxisDirection#DOWN  DOWN}.
     */
    @Override
    protected boolean isCompatibleDirection(AxisDirection direction) {
        direction = direction.absolute();
        return AxisDirection.NORTH.equals(direction) ||
               AxisDirection.EAST .equals(direction) ||
               AxisDirection.UP   .equals(direction);
    }

    /**
     * Returns {@code true} if the specified unit is compatible with
     * {@linkplain NonSI#DEGREE_ANGLE decimal degrees} (or {@linkplain SI#METER meters} in the
     * special case of height). This method is invoked at construction time for checking units
     * compatibility.
     *
     * @since 2.2
     */
    @Override
    protected boolean isCompatibleUnit(AxisDirection direction, final Unit<?> unit) {
        direction = direction.absolute();
        final Unit<?> expected = AxisDirection.UP.equals(direction) ? SI.METER : NonSI.DEGREE_ANGLE;
        return expected.isCompatible(unit);
    }

    /**
     * Update the converters.
     */
    private void update() {
        for (int i=getDimension(); --i>=0;) {
            final CoordinateSystemAxis axis = getAxis(i);
            final AxisDirection   direction = axis.getDirection().absolute();
            final Unit<?>              unit = axis.getUnit();
            if (AxisDirection.EAST.equals(direction)) {
                longitudeAxis      = i;
                longitudeConverter = unit.getConverterTo(NonSI.DEGREE_ANGLE);
                continue;
            }
            if (AxisDirection.NORTH.equals(direction)) {
                latitudeAxis      = i;
                latitudeConverter = unit.getConverterTo(NonSI.DEGREE_ANGLE);
                continue;
            }
            if (AxisDirection.UP.equals(direction)) {
                heightAxis      = i;
                heightConverter = unit.getConverterTo(SI.METER);
                continue;
            }
            // Should not happen, since 'isCompatibleDirection'
            // has already checked axis directions.
            throw new AssertionError(direction);
        }
    }

    /**
     * Returns the longitude found in the specified coordinate point,
     * always in {@linkplain NonSI#DEGREE_ANGLE decimal degrees}.
     *
     * @param  coordinates The coordinate point expressed in this coordinate system.
     * @return The longitude in the specified array, in {@linkplain NonSI#DEGREE_ANGLE decimal degrees}.
     * @throws MismatchedDimensionException is the coordinate point doesn't have the expected dimension.
     */
    public double getLongitude(final double[] coordinates)
            throws MismatchedDimensionException
    {
        ensureDimensionMatch("coordinates", coordinates);
        if (longitudeConverter == null) {
            update();
        }
        return longitudeConverter.convert(coordinates[longitudeAxis]);
    }

    /**
     * Returns the latitude found in the specified coordinate point,
     * always in {@linkplain NonSI#DEGREE_ANGLE decimal degrees}.
     *
     * @param  coordinates The coordinate point expressed in this coordinate system.
     * @return The latitude in the specified array, in {@linkplain NonSI#DEGREE_ANGLE decimal degrees}.
     * @throws MismatchedDimensionException is the coordinate point doesn't have the expected dimension.
     */
    public double getLatitude(final double[] coordinates)
            throws MismatchedDimensionException
    {
        ensureDimensionMatch("coordinates", coordinates);
        if (latitudeConverter == null) {
            update();
        }
        return latitudeConverter.convert(coordinates[latitudeAxis]);
    }

    /**
     * Returns the height found in the specified coordinate point,
     * always in {@linkplain SI#METER meters}.
     *
     * @param  coordinates The coordinate point expressed in this coordinate system.
     * @return The height in the specified array, in {@linkplain SI#METER meters}.
     * @throws MismatchedDimensionException is the coordinate point doesn't have the expected
     *         dimension.
     */
    public double getHeight(final double[] coordinates)
            throws MismatchedDimensionException
    {
        ensureDimensionMatch("coordinates", coordinates);
        if (heightConverter == null) {
            update();
            if (heightConverter == null) {
                throw new IllegalStateException(Errors.format(ErrorKeys.NOT_THREE_DIMENSIONAL_CS));
            }
        }
        return heightConverter.convert(coordinates[heightAxis]);
    }

    /**
     * Returns a new coordinate system with the same properties than the current one except for
     * axis units.
     *
     * @param  unit The unit for the new axis.
     * @return A coordinate system with axis using the specified units.
     * @throws IllegalArgumentException If the specified unit is incompatible with the expected one.
     *
     * @todo Current implementation can't work for 3D coordinate systems.
     *
     * @since 2.2
     */
    public DefaultEllipsoidalCS usingUnit(final Unit<?> unit) throws IllegalArgumentException {
        final CoordinateSystemAxis[] axis = axisUsingUnit(unit);
        if (axis == null) {
            return this;
        }
        return new DefaultEllipsoidalCS(getProperties(this, null), axis);
    }
}
