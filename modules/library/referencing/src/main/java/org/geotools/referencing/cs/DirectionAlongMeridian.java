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
package org.geotools.referencing.cs;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.opengis.referencing.cs.AxisDirection;


/**
 * Parses {@linkplain AxisDirection axis direction} of the kind
 * "<cite>South along 90 deg East</cite>". Those directions are
 * used in the EPSG database for polar stereographic projections.
 *
 * @version $Id$
 * @source $URL$
 * @author Martin Desruisseaux
 * @since 2.7.2
 */
public final class DirectionAlongMeridian implements Comparable, Serializable {
    /**
     * For cross-version compatibility.
     */
    private static final long serialVersionUID = 1602711631943838328L;

    /**
     * For floating point comparaisons.
     */
    static final double EPS = 1E-10;

    /**
     * A parser for EPSG axis names. Examples:
     *
     * "<cite>South along 180 deg</cite>",
     * "<cite>South along 90 deg East</cite>"
     */
    private static final Pattern EPSG = Pattern.compile(
            "(\\p{Graph}+)\\s+along\\s+([\\-\\p{Digit}\\.]+)\\s*(deg|°)\\s*(\\p{Graph}+)?",
            Pattern.CASE_INSENSITIVE);

    /**
     * The base directions we are interested in. Any direction not in
     * this group will be rejected by our parser.
     */
    private static final AxisDirection[] BASE_DIRECTIONS = new AxisDirection[] {
        AxisDirection.NORTH,
        AxisDirection.SOUTH,
        AxisDirection.EAST,
        AxisDirection.WEST
    };

    /**
     * The direction. Will be created only when first needed.
     *
     * @see #getDirection
     */
    private transient AxisDirection direction;

    /**
     * The base direction, which must be {@link AxisDirection#NORTH} or
     * {@link AxisDirection#SOUTH}.
     */
    public final AxisDirection baseDirection;

    /**
     * The meridian, in degrees.
     */
    public final double meridian;

    /**
     * Creates a direction.
     */
    private DirectionAlongMeridian(final AxisDirection baseDirection, final double meridian) {
        this.baseDirection = baseDirection;
        this.meridian      = meridian;
    }

    /**
     * Returns the dimension along meridian for the specified axis direction, or {@code null} if
     * none.
     */
    public static DirectionAlongMeridian parse(final AxisDirection direction) {
        final DirectionAlongMeridian candidate = parse(direction.name());
        if (candidate != null) {
            candidate.direction = direction;
        }
        return candidate;
    }

    /**
     * If the specified name is a direction along some specific meridian,
     * returns information about that. Otherwise returns {@code null}.
     */
    public static DirectionAlongMeridian parse(final String name) {
        final Matcher m = EPSG.matcher(name);
        if (!m.matches()) {
            // Not the expected pattern.
            return null;
        }
        String group = m.group(1);
        final AxisDirection baseDirection = findDirection(BASE_DIRECTIONS, group);
        if (baseDirection == null || !AxisDirection.NORTH.equals(baseDirection.absolute())) {
            // We expected "North" or "South" direction.
            return null;
        }
        group = m.group(2);
        double meridian;
        try {
            meridian = Double.parseDouble(group);
        } catch (NumberFormatException exception) {
            // Not a legal axis direction. Just ignore the exception,
            // since we are supposed to return 'null' in this situation.
            return null;
        }
        if (!(meridian >= -180 && meridian <= 180)) {
            // Meridian is NaN or is not in the valid range.
            return null;
        }
        group = m.group(4);
        if (group != null) {
            final AxisDirection sign = findDirection(BASE_DIRECTIONS, group);
            final AxisDirection abs = sign.absolute();
            if (sign == null || !AxisDirection.EAST.equals(abs)) {
                // We expected "East" or "West" direction.
                return null;
            }
            if (sign != abs) {
                meridian = -meridian;
            }
        }
        return new DirectionAlongMeridian(baseDirection, meridian);
    }

    /**
     * Searchs for the specified name in the specified set of directions.
     */
    private static AxisDirection findDirection(final AxisDirection[] values, final String direction) {
        for (int i=0; i<values.length; i++) {
            final AxisDirection candidate = values[i];
            final String name = candidate.name();
            if (direction.equalsIgnoreCase(name)) {
                return candidate;
            } 
            
            // check for common abbreviations
            if(direction.length() == 1) {
                if(candidate == AxisDirection.NORTH && direction.equals("N"))
                    return candidate;
                if(candidate == AxisDirection.SOUTH && direction.equals("S"))
                    return candidate;
                if(candidate == AxisDirection.WEST && direction.equals("W"))
                    return candidate;
                if(candidate == AxisDirection.EAST && direction.equals("E"))
                    return candidate;
            }
        }
        return null;
    }

    /**
     * Searchs for the specified name.
     */
    static AxisDirection findDirection(String direction) {
        final AxisDirection[] values = AxisDirection.values();
        AxisDirection candidate = findDirection(values, direction);
        if (candidate == null) {
            String modified = direction.replace('-', '_');
            if (modified != direction) {
                direction = modified;
                candidate = findDirection(values, modified);
            }
            if (candidate == null) {
                modified = direction.replace(' ', '_');
                if (modified != direction) {
                    candidate = findDirection(values, modified);
                }
            }
        }
        return candidate;
    }

    /**
     * Returns the axis direction for this object. If a suitable axis direction already exists,
     * it will be returned. Otherwise a new one is created and returned.
     */
    public AxisDirection getDirection() {
        if (direction != null) {
            return direction;
        }
        final String name = toString();
        synchronized (AxisDirection.class) {
            /*
             * The calls to  'AxisDirection.values()' and 'findDirection(...)'  should be performed
             * inside the synchronized block, since we try to avoid the creation of many directions
             * for the same name.   Strictly speaking, this synchronization is not suffisient since
             * it doesn't apply to the creation of axis directions from outside this class.  But it
             * okay if this code is the only place where we create axis directions with name of the
             * kind "South among 90°E". This assumption holds for Geotools implementation.
             */
            direction = findDirection(name);
            if (direction == null) {
                direction = AxisDirection.valueOf(name);
            }
        }
        return direction;
    }

    /**
     * Returns the arithmetic (counterclockwise) angle from this direction to the specified
     * direction, in decimal degrees. This method returns a value between -180° and +180°, or
     * {@link Double#NaN NaN} if the {@linkplain #baseDirection base directions} don't match.
     * A positive angle denote a right-handed system.
     * <p>
     * Example: the angle from "<cite>North along 90 deg East</cite>" to
     * "<cite>North along 0 deg</cite> is 90°.
     */
    public double getAngle(final DirectionAlongMeridian other) {
        if (!baseDirection.equals(other.baseDirection)) {
            return Double.NaN;
        }
        /*
         * We want the following pair of axis:
         * (NORTH along 90°E, NORTH along 0°)
         * to give a positive angle of 90°
         */
        double angle = meridian - other.meridian;
        /*
         * Forces to the [-180° .. +180°] range.
         */
        if (angle < -180) {
            angle += 360;
        } else if (angle > 180) {
            angle -= 360;
        }
        /*
         * Reverses the sign for axis oriented toward SOUTH,
         * so a positive angle is a right-handed system.
         */
        if (!baseDirection.equals(baseDirection.absolute())) {
            angle = -angle;
        }
        return angle;
    }

    /**
     * Compares this direction with the specified one for order. This method tries to reproduce
     * the ordering used for the majority of coordinate systems in the EPSG database, i.e. the
     * ordering of a right-handed coordinate system. Examples of ordered pairs that we should
     * get (extracted from the EPSG database):
     *
     * <table>
     *   <tr><td>North along 90 deg East,</td>  <td>North along 0 deg</td></tr>
     *   <tr><td>North along 75 deg West,</td>  <td>North along 165 deg West</td></tr>
     *   <tr><td>South along 90 deg West,</td>  <td>South along 0 deg</td></tr>
     *   <tr><td>South along 180 deg,</td>      <td>South along 90 deg West</td></tr>
     *   <tr><td>North along 130 deg West</td>  <td>North along 140 deg East</td></tr>
     * </table>
     */
    public int compareTo(final Object object) {
        final DirectionAlongMeridian that = (DirectionAlongMeridian) object;
        final int c = baseDirection.compareTo(that.baseDirection);
        if (c != 0) {
            return c;
        }
        final double angle = getAngle(that);
        if (angle < 0) return +1;  // Really the opposite sign.
        if (angle > 0) return -1;  // Really the opposite sign.
        return 0;
    }

    /**
     * Tests this object for equality with the specified one.
     * This method is used mostly for assertions.
     */
    @Override
    public boolean equals(final Object object) {
        if (object instanceof DirectionAlongMeridian) {
            final DirectionAlongMeridian that = (DirectionAlongMeridian) object;
            return baseDirection.equals(that.baseDirection) &&
                   Double.doubleToLongBits(meridian) == Double.doubleToLongBits(that.meridian);
        }
        return false;
    }

    /**
     * Returns a hash code value, for consistency with {@link #equals}.
     */
    @Override
    public int hashCode() {
        final long code = Double.doubleToLongBits(meridian);
        return (int)serialVersionUID ^ (int)code ^ (int)(code >> 32) + 37*baseDirection.hashCode();
    }

    /**
     * Returns a string representation of this direction, using a syntax matching the one used
     * by EPSG. This string representation will be used for creating a new {@link AxisDirection}.
     * The generated name should be identical to EPSG name, but we use the generated one anyway
     * (rather than the one provided by EPSG) in order to make sure that we create a single
     * {@link AxisDirection} for a given direction; we avoid potential differences like lower
     * versus upper cases, amount of white space, <cite>etc</cite>.
     */
    @Override
    public String toString() {
        final StringBuilder buffer = new StringBuilder(baseDirection.name());
        toLowerCase(buffer, 0);
        buffer.append(" along ");
        final double md = Math.abs(meridian);
        final int    mi = (int) md;
        if (md == mi) {
            buffer.append(mi);
        } else {
            buffer.append(md);
        }
        buffer.append(" deg");
        if (md != 0 && mi != 180) {
            buffer.append(' ');
            final int base = buffer.length();
            final AxisDirection sign = meridian < 0 ? AxisDirection.WEST : AxisDirection.EAST;
            buffer.append(sign.name());
            toLowerCase(buffer, base);
        }
        final String name = buffer.toString();
        assert EPSG.matcher(name).matches() : name;
        return name;
    }

    /**
     * Changes the buffer content to lower case from {@code base+1} to
     * the end of the buffer. For {@link #toString} internal use only.
     */
    private static void toLowerCase(final StringBuilder buffer, final int base) {
        for (int i=buffer.length(); --i>base;) {
            buffer.setCharAt(i, Character.toLowerCase(buffer.charAt(i)));
        }
    }
}
