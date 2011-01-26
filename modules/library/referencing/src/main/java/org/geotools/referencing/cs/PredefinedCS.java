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
package org.geotools.referencing.cs;

import java.util.List;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;

import org.opengis.referencing.cs.*;
import org.geotools.resources.i18n.Errors;
import org.geotools.resources.i18n.ErrorKeys;


/**
 * Converts an arbitrary CS into one of the predefined constants provided in the
 * {@link org.geotools.referencing.cs} package. The main usage for this class is
 * to reorder the axis in some "standard" order like (<var>x</var>, <var>y</var>,
 * <var>z</var>) or (<var>longitude</var>, <var>latitude</var>). What "standard"
 * order means is sometime an arbitrary choice, which explain why this class is
 * not public at this time.
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
final class PredefinedCS implements Comparator<CoordinateSystem> {
    /**
     * An instance of {@link PredefinedCS}. Will be created only when first needed.
     */
    private static Comparator<CoordinateSystem> csComparator;

    /**
     * Our ordering for coordinate system objects.
     */
    @SuppressWarnings("unchecked")
    private final Class<? extends CoordinateSystem>[] types = new Class[] {
        CartesianCS  .class,
        AffineCS     .class,
        EllipsoidalCS.class,
        SphericalCS  .class,
        CylindricalCS.class,
        PolarCS      .class,
        VerticalCS   .class,
        TimeCS       .class,
        LinearCS     .class,
        UserDefinedCS.class
    };

    /**
     * Creates a comparator.
     */
    private PredefinedCS() {
    }

    /**
     * Compares the ordering between two coordinate systems. This comparator is used for sorting
     * the axis in an user-supplied compound CS in an order closes to some "standard" order.
     */
    public int compare(final CoordinateSystem object1, final CoordinateSystem object2) {
        final Class<? extends CoordinateSystem> type1 = object1.getClass();
        final Class<? extends CoordinateSystem> type2 = object2.getClass();
        for (int i=0; i<types.length; i++) {
            final Class<?> type = types[i];
            final boolean a1 = type.isAssignableFrom(type1);
            final boolean a2 = type.isAssignableFrom(type2);
            if (a1) return a2 ? 0 : -1;
            if (a2) return a1 ? 0 : +1;
        }
        return 0;
    }

    /**
     * Implementation of the {@link AbstractCS#standard} method.
     */
    static CoordinateSystem standard(final CoordinateSystem cs) throws IllegalArgumentException {
        final int dimension = cs.getDimension();
        if (cs instanceof CartesianCS) {
            switch (dimension) {
                case 2: {
                    if (DefaultCartesianCS.PROJECTED.axisColinearWith(cs)) {
                        return DefaultCartesianCS.PROJECTED;
                    }
                    if (DefaultCartesianCS.GRID.axisColinearWith(cs)) {
                        return DefaultCartesianCS.GRID;
                    }
                    if (DefaultCartesianCS.GENERIC_2D.directionColinearWith(cs)) {
                        return DefaultCartesianCS.GENERIC_2D;
                    }
                    return rightHanded((CartesianCS) cs);
                }
                case 3: {
                    if (DefaultCartesianCS.GEOCENTRIC.axisColinearWith(cs)) {
                        return DefaultCartesianCS.GEOCENTRIC;
                    }
                    if (DefaultCartesianCS.GENERIC_3D.directionColinearWith(cs)) {
                        return DefaultCartesianCS.GENERIC_3D;
                    }
                    return rightHanded((CartesianCS) cs);
                }
            }
        }
        if (cs instanceof AffineCS) {
            return rightHanded((AffineCS) cs);
        }
        if (cs instanceof EllipsoidalCS) {
            switch (dimension) {
                case 2: return DefaultEllipsoidalCS.GEODETIC_2D;
                case 3: return DefaultEllipsoidalCS.GEODETIC_3D;
            }
        }
        if (cs instanceof SphericalCS) {
            switch (dimension) {
                case 3: return DefaultSphericalCS.GEOCENTRIC;
            }
        }
        if (cs instanceof VerticalCS) {
            switch (dimension) {
                case 1: {
                    return DefaultVerticalCS.ELLIPSOIDAL_HEIGHT;
                }
            }
        }
        if (cs instanceof TimeCS) {
            switch (dimension) {
                case 1: return DefaultTimeCS.DAYS;
            }
        }
        if (cs instanceof DefaultCompoundCS) {
            final List<CoordinateSystem> components = ((DefaultCompoundCS) cs).getCoordinateSystems();
            final CoordinateSystem[] user = new CoordinateSystem[components.size()];
            final CoordinateSystem[] std  = new CoordinateSystem[user.length];
            for (int i=0; i<std.length; i++) {
                std[i] = standard(user[i] = components.get(i));
            }
            if (csComparator == null) {
                csComparator = new PredefinedCS();
            }
            Arrays.sort(std, csComparator);
            return Arrays.equals(user, std) ? cs : new DefaultCompoundCS(std);
        }
        throw new IllegalArgumentException(
                Errors.format(ErrorKeys.UNSUPPORTED_COORDINATE_SYSTEM_$1, cs.getName().getCode()));
    }

    /**
     * Reorder the axis in the specified Affine CS in an attempt to get a right-handed system.
     * Units are standardized to meters in the process. If no axis change is needed, then this
     * method returns {@code cs} unchanged.
     */
    private static AffineCS rightHanded(final AffineCS cs) {
        boolean changed = false;
        final int dimension = cs.getDimension();
        final CoordinateSystemAxis[] axis = new CoordinateSystemAxis[dimension];
        for (int i=0; i<dimension; i++) {
            /*
             * Gets the axis and replaces it by one of the predefined constants declared in
             * DefaultCoordinateSystemAxis, if possible. The predefined constants use ISO 19111
             * names with metres or degrees units, so it is pretty close to the "standard" axis
             * we are looking for.
             */
            CoordinateSystemAxis axe = axis[i] = cs.getAxis(i);
            DefaultCoordinateSystemAxis standard = DefaultCoordinateSystemAxis.getPredefined(axe);
            if (standard != null) {
                axe = standard;
            }
            /*
             * Changes units to meters. Every units in an affine CS should be linear or
             * dimensionless (the later is used for grid coordinates).  The 'usingUnit'
             * method will thrown an exception if the unit is incompatible. See
             * DefaultAffineCS.isCompatibleUnit(Unit).
             */
            final Unit<?> unit = axe.getUnit();
            if (!Unit.ONE.equals(unit) && !SI.METER.equals(unit)) {
                if (!(axe instanceof DefaultCoordinateSystemAxis)) {
                    axe = new DefaultCoordinateSystemAxis(axe);
                }
                axe = ((DefaultCoordinateSystemAxis) axe).usingUnit(SI.METER);
            }
            changed |= (axe != axis[i]);
            axis[i] = axe;
        }
        /*
         * Sorts the axis in an attempt to create a right-handed system
         * and creates a new Coordinate System if at least one axis changed.
         */
        changed |= ComparableAxisWrapper.sort(axis);
        if (!changed) {
            return cs;
        }
        final Map<String,?> properties = DefaultAffineCS.getProperties(cs, null);
        if (cs instanceof CartesianCS) {
            return new DefaultCartesianCS(properties, axis);
        }
        return new DefaultAffineCS(properties, axis);
    }
}
