/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2001-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.datum;

import java.util.Collections;
import java.util.Date;
import java.util.Map;

import org.opengis.util.InternationalString;
import org.opengis.referencing.datum.TemporalDatum;
import org.geotools.referencing.AbstractIdentifiedObject;
import org.geotools.resources.i18n.VocabularyKeys;


/**
 * A temporal datum defines the origin of a temporal coordinate reference system.
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 *
 * @since 2.1
 */
public class DefaultTemporalDatum extends AbstractDatum implements TemporalDatum {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = 3357241732140076884L;

    /**
     * Datum for time measured since January 1st, 4713 BC at 12:00 UTC.
     *
     * @see org.geotools.referencing.crs.DefaultTemporalCRS#JULIAN
     *
     * @since 2.5
     */
    public static final DefaultTemporalDatum JULIAN = new DefaultTemporalDatum(
            name(VocabularyKeys.JULIAN), new Date(-2440588 * (24*60*60*1000L) + (12*60*60*1000L)));

    /**
     * Datum for time measured since November 17, 1858 at 00:00 UTC.
     * A <cite>Modified Julian day</cite> (MJD) is defined relative to <cite>Julian day</cite>
     * (JD) as {@code MJD = JD − 2400000.5}.
     *
     * @see org.geotools.referencing.crs.DefaultTemporalCRS#MODIFIED_JULIAN
     *
     * @since 2.5
     */
    public static final DefaultTemporalDatum MODIFIED_JULIAN = new DefaultTemporalDatum(
            name(VocabularyKeys.MODIFIED_JULIAN), new Date(-40587 * (24*60*60*1000L)));

    /**
     * Datum for time measured since May 24, 1968 at 00:00 UTC. This epoch was introduced by NASA
     * for the space program. A <cite>Truncated Julian day</cite> (TJD) is defined relative to
     * <cite>Julian day</cite> (JD) as {@code TJD = JD − 2440000.5}.
     *
     * @see org.geotools.referencing.crs.DefaultTemporalCRS#TRUNCATED_JULIAN
     *
     * @since 2.5
     */
    public static final DefaultTemporalDatum TRUNCATED_JULIAN = new DefaultTemporalDatum(
            name(VocabularyKeys.TRUNCATED_JULIAN), new Date(-587 * (24*60*60*1000L)));

    /**
     * Datum for time measured since December 31, 1899 at 12:00 UTC.
     * A <cite>Dublin Julian day</cite> (DJD) is defined relative to <cite>Julian day</cite> (JD)
     * as {@code DJD = JD − 2415020}.
     *
     * @see org.geotools.referencing.crs.DefaultTemporalCRS#DUBLIN_JULIAN
     *
     * @since 2.5
     */
    public static final DefaultTemporalDatum DUBLIN_JULIAN = new DefaultTemporalDatum(
            name(VocabularyKeys.DUBLIN_JULIAN), new Date(-25568 * (24*60*60*1000L) + (12*60*60*1000L)));

    /**
     * Default datum for time measured since January 1st, 1970 at 00:00 UTC.
     *
     * @see org.geotools.referencing.crs.DefaultTemporalCRS#UNIX
     * @see org.geotools.referencing.crs.DefaultTemporalCRS#JAVA
     */
    public static final DefaultTemporalDatum UNIX = new DefaultTemporalDatum(
            "UNIX", new Date(0));

    /**
     * The date and time origin of this temporal datum.
     */
    private final long origin;

    /**
     * Constructs a new datum with the same values than the specified one.
     * This copy constructor provides a way to wrap an arbitrary implementation into a
     * Geotools one or a user-defined one (as a subclass), usually in order to leverage
     * some implementation-specific API. This constructor performs a shallow copy,
     * i.e. the properties are not cloned.
     *
     * @since 2.2
     */
    public DefaultTemporalDatum(final TemporalDatum datum) {
        super(datum);
        origin = datum.getOrigin().getTime();
    }

    /**
     * Constructs a temporal datum from a name.
     *
     * @param name   The datum name.
     * @param origin The date and time origin of this temporal datum.
     */
    public DefaultTemporalDatum(final String name, final Date origin) {
        this(Collections.singletonMap(NAME_KEY, name), origin);
    }

    /**
     * Constructs a temporal datum from a set of properties. The properties map is given
     * unchanged to the {@linkplain AbstractDatum#AbstractDatum(Map) super-class constructor}.
     *
     * @param properties Set of properties. Should contains at least {@code "name"}.
     * @param origin The date and time origin of this temporal datum.
     */
    public DefaultTemporalDatum(final Map<String,?> properties, final Date origin) {
        super(properties);
        ensureNonNull("origin", origin);
        this.origin = origin.getTime();
    }

    /**
     * The date and time origin of this temporal datum.
     *
     * @return The date and time origin of this temporal datum.
     */
    public Date getOrigin() {
        return new Date(origin);
    }

    /**
     * Description of the point or points used to anchor the datum to the Earth.
     */
    @Override
    public InternationalString getAnchorPoint() {
        return super.getAnchorPoint();
    }

    /**
     * The time after which this datum definition is valid.
     */
    @Override
    public Date getRealizationEpoch() {
        return super.getRealizationEpoch();
    }

    /**
     * Compare this temporal datum with the specified object for equality.
     *
     * @param  object The object to compare to {@code this}.
     * @param  compareMetadata {@code true} for performing a strict comparaison, or
     *         {@code false} for comparing only properties relevant to transformations.
     * @return {@code true} if both objects are equal.
     */
    @Override
    public boolean equals(final AbstractIdentifiedObject object, final boolean compareMetadata) {
        if (object == this) {
            return true; // Slight optimization.
        }
        if (super.equals(object, compareMetadata)) {
            final DefaultTemporalDatum that = (DefaultTemporalDatum) object;
            return this.origin == that.origin;
        }
        return false;
    }

    /**
     * Returns a hash value for this temporal datum. {@linkplain #getName Name},
     * {@linkplain #getRemarks remarks} and the like are not taken in account. In
     * other words, two temporal datums will return the same hash value if they
     * are equal in the sense of
     * <code>{@link #equals equals}(AbstractIdentifiedObject, <strong>false</strong>)</code>.
     *
     * @return The hash code value. This value doesn't need to be the same
     *         in past or future versions of this class.
     */
    @Override
    public int hashCode() {
        return super.hashCode() ^ (int)origin ^ (int)(origin >>> 32);
    }
}
