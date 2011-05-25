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
package org.geotools.referencing.factory;

import java.util.Arrays;
import java.util.Comparator;
import javax.measure.unit.SI;
import javax.measure.unit.NonSI;
import javax.measure.unit.Unit;

import org.opengis.referencing.cs.*;
import org.opengis.referencing.crs.CRSAuthorityFactory;

import org.geotools.factory.Hints;
import org.geotools.factory.FactoryRegistryException;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.geotools.referencing.cs.DefaultCoordinateSystemAxis;
import org.geotools.resources.i18n.Errors;
import org.geotools.resources.i18n.ErrorKeys;


/**
 * An authority factory which delegates all the work to an other factory, and reorder the axis in
 * some pre-determined order. This factory is mostly used by application expecting geographic
 * coordinates in (<var>longitude</var>, <var>latitude</var>) order, while most geographic CRS
 * specified in the <A HREF="http://www.epsg.org">EPSG database</A> use the opposite axis order.
 * <p>
 * It is better to avoid this class if you can. This class exists primarily for compatibility with
 * external data or applications that assume (<var>longitude</var>, <var>latitude</var>) axis order
 * no matter what the EPSG database said, for example Shapefiles.
 * <p>
 * The axis order can be specified at construction time as an array of {@linkplain AxisDirection
 * axis directions}. If no such array is explicitly specified, then the default order is
 * {@linkplain AxisDirection#EAST               East},
 * {@linkplain AxisDirection#EAST_NORTH_EAST    East-North-East},
 * {@linkplain AxisDirection#NORTH_EAST         North-East},
 * {@linkplain AxisDirection#NORTH_NORTH_EAST   North-North-East},
 * {@linkplain AxisDirection#NORTH              North},
 * {@linkplain AxisDirection#UP                 Up},
 * {@linkplain AxisDirection#GEOCENTRIC_X       Geocentric X},
 * {@linkplain AxisDirection#GEOCENTRIC_Y       Geocentric Y},
 * {@linkplain AxisDirection#GEOCENTRIC_Z       Geocentric Z},
 * {@linkplain AxisDirection#COLUMN_POSITIVE    Column},
 * {@linkplain AxisDirection#ROW_POSITIVE       Row},
 * {@linkplain AxisDirection#DISPLAY_RIGHT      Display right},
 * {@linkplain AxisDirection#DISPLAY_UP         Display up} and
 * {@linkplain AxisDirection#FUTURE             Future}.
 * This means that, for example, axis with East or West direction will be placed before any
 * axis with North or South direction. Axis directions not specified in the table (for example
 * {@link AxisDirection#OTHER OTHER}) will be ordered last. This is somewhat equivalent to the
 * ordering of {@link Double#NaN NaN} values in an array of {@code double}.
 * <p>
 * <strong>Notes:</strong>
 * <ul>
 *   <li>This class compares only the "{@linkplain AxisDirection#absolute absolute}" axis
 *       directions, so North and South are considered equivalent.</li>
 *   <li>The default direction order may changes in future Geotools version in order
 *       to fit what appears to be the most common usage on the market.</li>
 *   <li>The actual axis ordering is determined by the {@link #compare compare} method
 *       implementation. Subclasses may override this method if the want to provide a more
 *       sophesticated axis ordering.</li>
 * </ul>
 * <p>
 * For some authority factories, an instance of this class can be obtained by passing a
 * {@link Hints#FORCE_LONGITUDE_FIRST_AXIS_ORDER FORCE_LONGITUDE_FIRST_AXIS_ORDER} hint
 * to the <code>{@linkplain ReferencingFactoryFinder#getCRSAuthorityFactory
 * FactoryFinder.getCRSAuthorityFactory}(...)</code> method. Whatever this hint is supported
 * or not is authority dependent. Example:
 *
 * <blockquote><pre>
 * Hints                   hints = new Hints(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE);
 * CRSAuthorityFactory   factory = FactoryFinder.getCRSAuthorityFactory("EPSG", hints);
 * CoordinateReferenceSystem crs = factory.createCoordinateReferenceSystem("EPSG:4326");
 * </pre></blockquote>
 *
 * This class is named <cite>ordered axis authority factory</cite> instead of something like
 * <cite>longitude first axis order</cite> because the axis order can be user-supplied. The
 * (<var>longitude</var>, <var>latitude</var>) order just appears to be the default one.
 *
 * @since 2.2
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 *
 * @see Hints#FORCE_LONGITUDE_FIRST_AXIS_ORDER
 * @see Hints#FORCE_STANDARD_AXIS_UNITS
 * @tutorial http://docs.codehaus.org/display/GEOTOOLS/The+axis+order+issue
 */
public class OrderedAxisAuthorityFactory extends TransformedAuthorityFactory
        implements CSAuthorityFactory, CRSAuthorityFactory, Comparator/*<CoordinateSystemAxis>*/
{
    /**
     * The default order for axis directions. Note that this array needs to contain only the
     * "{@linkplain AxisDirection#absolute absolute}" directions.
     *
     * REMINDER: If this array is modified, don't forget to update the class javadoc above.
     */
    private static final AxisDirection[] DEFAULT_ORDER = {
        AxisDirection.EAST,
        AxisDirection.EAST_NORTH_EAST,
        AxisDirection.NORTH_EAST,
        AxisDirection.NORTH_NORTH_EAST,
        AxisDirection.NORTH,
        AxisDirection.UP,
        AxisDirection.GEOCENTRIC_X,
        AxisDirection.GEOCENTRIC_Y,
        AxisDirection.GEOCENTRIC_Z,
        AxisDirection.COLUMN_POSITIVE,
        AxisDirection.ROW_POSITIVE,
        AxisDirection.DISPLAY_RIGHT,
        AxisDirection.DISPLAY_UP,
        AxisDirection.FUTURE
    };

    /**
     * The rank to be given to each axis direction. The rank is stored at the indice
     * corresponding to the direction {@linkplain AxisDirection#ordinal ordinal} value.
     */
    private final int[] directionRanks;

    /**
     * {@code true} if this authority factory should also force the axis to their standard
     * direction. For example if {@code true}, then axis with increasing values toward South
     * will be converted to axis with increasing values toward North. The default value is
     * {@code false}.
     *
     * @see Hints#FORCE_STANDARD_AXIS_DIRECTIONS
     * @since 2.3
     */
    protected final boolean forceStandardDirections;

    /**
     * {@code true} if this authority factory should also force all angular units to
     * decimal degrees and linear units to meters. The default value is {@code false}.
     *
     * @see Hints#FORCE_STANDARD_AXIS_UNITS
     * @since 2.3
     */
    protected final boolean forceStandardUnits;

    /**
     * Creates a factory which will reorder the axis of all objects created by the default
     * authority factories. The factories are fetched using {@link ReferencingFactoryFinder}. This
     * constructor accepts the following hints:
     * <p>
     * <ul>
     *   <li>{@link Hints#FORCE_STANDARD_AXIS_UNITS}</li>
     *   <li>{@link Hints#FORCE_STANDARD_AXIS_DIRECTIONS}</li>
     *   <li>All hints understood by {@link ReferencingFactoryFinder}</li>
     * </ul>
     *
     * @param  authority The authority to wraps (example: {@code "EPSG"}). If {@code null},
     *         then all authority factories must be explicitly specified in the set of hints.
     * @param  userHints An optional set of hints, or {@code null} if none.
     * @param  axisOrder An array of axis directions that determine the axis order wanted,
     *         or {@code null} for the default axis order.
     * @throws FactoryRegistryException if at least one factory can not be obtained.
     * @throws IllegalArgumentException If at least two axis directions are colinear.
     *
     * @since 2.3
     */
    public OrderedAxisAuthorityFactory(final String          authority,
                                       final Hints           userHints,
                                       final AxisDirection[] axisOrder)
            throws FactoryRegistryException, IllegalArgumentException
    {
        super(authority, userHints);
        forceStandardUnits      = booleanValue(userHints, Hints.FORCE_STANDARD_AXIS_UNITS);
        forceStandardDirections = booleanValue(userHints, Hints.FORCE_STANDARD_AXIS_DIRECTIONS);
        directionRanks          = computeDirectionRanks(axisOrder);
        completeHints();
    }

    /**
     * Creates a factory which will reorder the axis of all objects created by the supplied
     * factory. This constructor accepts the following optional hints:
     * <p>
     * <ul>
     *   <li>{@link Hints#FORCE_STANDARD_AXIS_UNITS}</li>
     *   <li>{@link Hints#FORCE_STANDARD_AXIS_DIRECTIONS}</li>
     * </ul>
     *
     * @param  factory   The factory that produces objects using arbitrary axis order.
     * @param  userHints An optional set of hints, or {@code null} if none.
     * @param  axisOrder An array of axis directions that determine the axis order wanted,
     *                   or {@code null} for the default axis order.
     * @throws IllegalArgumentException If at least two axis directions are colinear.
     *
     * @since 2.3
     */
    public OrderedAxisAuthorityFactory(final AbstractAuthorityFactory factory,
                                       final Hints                    userHints,
                                       final AxisDirection[]          axisOrder)
            throws IllegalArgumentException
    {
        super(factory);
        forceStandardUnits      = booleanValue(userHints, Hints.FORCE_STANDARD_AXIS_UNITS);
        forceStandardDirections = booleanValue(userHints, Hints.FORCE_STANDARD_AXIS_DIRECTIONS);
        directionRanks          = computeDirectionRanks(axisOrder);
        completeHints();
    }

    /**
     * Returns the boolean value for the specified hint.
     */
    private static boolean booleanValue(final Hints userHints, final Hints.Key key) {
        if (userHints != null) {
            final Boolean value = (Boolean) userHints.get(key);
            if (value != null) {
                return value.booleanValue();
            }
        }
        return false;
    }

    /**
     * Completes the set of hints according the value currently set in this object.
     * This method is invoked by constructors only.
     */
    private void completeHints() {
        hints.put(Hints.FORCE_STANDARD_AXIS_UNITS,      Boolean.valueOf(forceStandardUnits));
        hints.put(Hints.FORCE_STANDARD_AXIS_DIRECTIONS, Boolean.valueOf(forceStandardDirections));
        // The following hint has no effect on this class behaviour,
        // but tells to the user what this factory do about axis order.
        if (compare(DefaultCoordinateSystemAxis.EASTING, DefaultCoordinateSystemAxis.NORTHING) < 0) {
            hints.put(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE);
        }
    }

    /**
     * Computes the rank for every direction in the specified. The rank is stored in an array
     * at the indice corresponding to the direction {@linkplain AxisDirection#ordinal ordinal}
     * value. This method is used by constructors for computing the {@link #directionRanks} field.
     *
     * @throws IllegalArgumentException If at least two axis directions are colinear.
     */
    private static int[] computeDirectionRanks(AxisDirection[] axisOrder)
            throws IllegalArgumentException
    {
        if (axisOrder == null) {
            axisOrder = DEFAULT_ORDER;
        }
        int length = 0;
        for (int i=0; i<axisOrder.length; i++) {
            final int ordinal = axisOrder[i].absolute().ordinal() + 1;
            if (ordinal > length) {
                length = ordinal;
            }
        }
        final int[] directionRanks = new int[length];
        Arrays.fill(directionRanks, length);
        for (int i=0; i<axisOrder.length; i++) {
            final int ordinal  = axisOrder[i].absolute().ordinal();
            final int previous = directionRanks[ordinal];
            if (previous != length) {
                // TODO: Use the localized version of 'getName' in GeoAPI 2.1
                throw new IllegalArgumentException(Errors.format(ErrorKeys.COLINEAR_AXIS_$2,
                                          axisOrder[previous].name(), axisOrder[i].name()));
            }
            directionRanks[ordinal] = i;
        }
        return directionRanks;
    }

    /**
     * Returns the rank for the specified axis. Any axis that were not specified
     * at construction time will ordered after all known axis.
     */
    private final int rank(final CoordinateSystemAxis axis) {
        int c = axis.getDirection().absolute().ordinal();
        c = (c>=0 && c<directionRanks.length) ? directionRanks[c] : directionRanks.length;
        return c;
    }

    /**
     * Compares two axis for order. This method is invoked automatically by the
     * {@link #replace(CoordinateSystem) replace} method for ordering the axis in a
     * coordinate system. The default implementation orders the axis according their
     * {@linkplain CoordinateSystemAxis#getDirection direction}, using the direction
     * table given at {@linkplain #OrderedAxisAuthorityFactory(AbstractAuthorityFactory,
     * Hints, AxisDirection[]) construction time} (see also the class description).
     * Subclasses may override this method if they want to define a more sophesticated
     * axis ordering.
     *
     * @param  axis1 The first axis to compare.
     * @param  axis2 The second axis to compare.
     * @return A negative integer if {@code axis1} should appears before {@code axis2}, or a
     *         positive number if {@code axis2} should appears before {@code axis1}, or 0 if
     *         the two axis are unordered one relative to the other.
     *
     * @todo The argument type will be changed to {@link CoordinateSystemAxis} when we will
     *       be allowed to compile for J2SE 1.5.
     *
     * @since 2.3
     */
    public int compare(final Object axis1, final Object axis2) {
        return rank((CoordinateSystemAxis) axis1) - rank((CoordinateSystemAxis) axis2);
    }

    /**
     * Replaces the specified unit, if applicable. This method is invoked automatically by the
     * {@link #replace(CoordinateSystem)} method. The default implementation replaces the unit
     * only if the {@link Hints#FORCE_STANDARD_AXIS_UNITS FORCE_STANDARD_AXIS_UNITS} hint was
     * specified as {@link Boolean#TRUE TRUE} at construction time. In such case, the default
     * substitution table is:
     * <p>
     * <ul>
     *   <li>Any linear units converted to {@linkplain SI#METER meters}</li>
     *   <li>{@linkplain SI#RADIAN Radians} and {@linkplain NonSI#GRADE grades} converted to
     *       {@linkplain NonSI#DEGREE_ANGLE decimal degrees}</li>
     * </ul>
     * <p>
     * This default substitution table may be expanded in future Geotools versions.
     *
     * @since 2.3
     */
    @Override
    protected Unit<?> replace(final Unit<?> units) {
        if (forceStandardUnits) {
            if (units.isCompatible(SI.METER)) {
                return SI.METER;
            }
            if (units.equals(SI.RADIAN) || units.equals(NonSI.GRADE)) {
                return NonSI.DEGREE_ANGLE;
            }
        }
        return units;
    }

    /**
     * Replaces the specified direction, if applicable. This method is invoked automatically by the
     * {@link #replace(CoordinateSystem)} method. The default implementation replaces the direction
     * only if the {@link Hints#FORCE_STANDARD_AXIS_DIRECTIONS FORCE_STANDARD_AXIS_DIRECTIONS} hint
     * was specified as {@link Boolean#TRUE TRUE} at construction time. In such case, the default
     * substitution table is as specified in the {@link AxisDirection#absolute} method.
     * Subclasses may override this method if they want to use a different substitution table.
     *
     * @since 2.3
     */
    @Override
    protected AxisDirection replace(final AxisDirection direction) {
        return (forceStandardDirections) ? direction.absolute() : direction;
    }
}
