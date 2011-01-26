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
import java.util.Map;

import org.opengis.referencing.datum.VerticalDatum;
import org.opengis.referencing.datum.VerticalDatumType;

import org.geotools.referencing.AbstractIdentifiedObject;
import org.geotools.referencing.wkt.Formatter;
import org.geotools.resources.i18n.VocabularyKeys;
import org.geotools.util.Utilities;


/**
 * A textual description and/or a set of parameters identifying a particular reference level
 * surface used as a zero-height surface. The description includes its position with respect
 * to the Earth for any of the height types recognized by this standard. There are several
 * types of vertical datums, and each may place constraints on the
 * {@linkplain org.opengis.referencing.cs.CoordinateSystemAxis coordinate system axis} with which
 * it is combined to create a {@linkplain org.opengis.referencing.crs.VerticalCRS vertical CRS}.
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 *
 * @since 2.1
 */
public class DefaultVerticalDatum extends AbstractDatum implements VerticalDatum {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = 380347456670516572L;

    /**
     * A copy of the list of vertical types.
     */
    private static final VerticalDatumType[] TYPES = VerticalDatumType.values();

    /**
     * Mapping between {@linkplain VerticalDatumType vertical datum type} and the numeric
     * values used in legacy specification (OGC 01-009).
     */
    private static final short[] LEGACY_CODES = new short[TYPES.length];
    static {
        LEGACY_CODES[VerticalDatumType.GEOIDAL      .ordinal()] = 2005; // CS_VD_GeoidModelDerived
        LEGACY_CODES[VerticalDatumType.ELLIPSOIDAL  .ordinal()] = 2002; // CS_VD_Ellipsoidal
        LEGACY_CODES[VerticalDatumType.DEPTH        .ordinal()] = 2006; // CS_VD_Depth
        LEGACY_CODES[VerticalDatumType.BAROMETRIC   .ordinal()] = 2003; // CS_VD_AltitudeBarometric
        LEGACY_CODES[VerticalDatumType.ORTHOMETRIC  .ordinal()] = 2001; // CS_VD_Orthometric
        LEGACY_CODES[VerticalDatumType.OTHER_SURFACE.ordinal()] = 2000; // CS_VD_Other
    }

    /**
     * The type of this vertical datum. Default is "geoidal".
     */
    private final VerticalDatumType type;

    /**
     * Default vertical datum for {@linkplain VerticalDatumType#GEOIDAL geoidal heights}.
     */
    public static final DefaultVerticalDatum GEOIDAL =
            new DefaultVerticalDatum(name(VocabularyKeys.GEOIDAL), VerticalDatumType.GEOIDAL);

    /**
     * Default vertical datum for ellipsoidal heights. Ellipsoidal heights
     * are measured along the normal to the ellipsoid used in the definition
     * of horizontal datum.
     */
    public static final DefaultVerticalDatum ELLIPSOIDAL =
            new DefaultVerticalDatum(name(VocabularyKeys.ELLIPSOIDAL), VerticalDatumType.ELLIPSOIDAL);

    /**
     * Constructs a vertical datum from a name.
     *
     * @param name   The datum name.
     * @param type   The type of this vertical datum.
     */
    public DefaultVerticalDatum(final String name, final VerticalDatumType type) {
        this(Collections.singletonMap(NAME_KEY, name), type);
    }

    /**
     * Constructs a new datum with the same values than the specified one.
     * This copy constructor provides a way to wrap an arbitrary implementation into a
     * Geotools one or a user-defined one (as a subclass), usually in order to leverage
     * some implementation-specific API. This constructor performs a shallow copy,
     * i.e. the properties are not cloned.
     *
     * @param datum The datum to copy.
     *
     * @since 2.2
     */
    public DefaultVerticalDatum(final VerticalDatum datum) {
        super(datum);
        type = datum.getVerticalDatumType();
    }

    /**
     * Constructs a vertical datum from a set of properties. The properties map is given
     * unchanged to the {@linkplain AbstractDatum#AbstractDatum(Map) super-class constructor}.
     *
     * @param properties Set of properties. Should contains at least {@code "name"}.
     * @param type       The type of this vertical datum.
     */
    public DefaultVerticalDatum(final Map<String,?> properties, final VerticalDatumType type) {
        super(properties);
        this.type = type;
        ensureNonNull("type", type);
    }

    /**
     * The type of this vertical datum. Default is geoidal.
     *
     * @return The type of this vertical datum.
     */
    public VerticalDatumType getVerticalDatumType() {
        return type;
    }

    /**
     * Returns the legacy code for the datum type.
     */
    @Override
    final int getLegacyDatumType() {
        final int ordinal = type.ordinal();
        if (ordinal>=0 && ordinal<LEGACY_CODES.length) {
            assert type.equals(TYPES[ordinal]) : type;
            return LEGACY_CODES[ordinal];
        }
        return 0;
    }

    /**
     * Returns the vertical datum type from a legacy code. The legacy codes were defined in
     * <A HREF="http://www.opengis.org/docs/01-009.pdf">Coordinate Transformation Services</A>
     * (OGC 01-009), which also defined the
     * <A HREF="http://geoapi.sourceforge.net/snapshot/javadoc/org/opengis/referencing/doc-files/WKT.html"><cite>Well
     * Known Text</cite> (WKT)</A> format. This method is used for WKT parsing.
     *
     * @param  code The legacy vertical datum code.
     * @return The vertical datum type, or {@code null} if the code is unrecognized.
     */
    public static VerticalDatumType getVerticalDatumTypeFromLegacyCode(final int code) {
        for (int i=0; i<LEGACY_CODES.length; i++) {
            if (LEGACY_CODES[i] == code) {
                return TYPES[i];
            }
        }
        return null;
    }

    /**
     * Compare this vertical datum with the specified object for equality.
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
            final DefaultVerticalDatum that = (DefaultVerticalDatum) object;
            return Utilities.equals(this.type, that.type);
        }
        return false;
    }

    /**
     * Returns a hash value for this vertical datum. {@linkplain #getName Name},
     * {@linkplain #getRemarks remarks} and the like are not taken in account. In
     * other words, two vertical datums will return the same hash value if they
     * are equal in the sense of
     * <code>{@link #equals equals}(AbstractIdentifiedObject, <strong>false</strong>)</code>.
     *
     * @return The hash code value. This value doesn't need to be the same
     *         in past or future versions of this class.
     */
    @Override
    public int hashCode() {
        return super.hashCode() ^ type.hashCode();
    }

    /**
     * Format the inner part of a
     * <A HREF="http://geoapi.sourceforge.net/snapshot/javadoc/org/opengis/referencing/doc-files/WKT.html"><cite>Well
     * Known Text</cite> (WKT)</A> element.
     *
     * @param  formatter The formatter to use.
     * @return The WKT element name, which is "VERT_DATUM"
     */
    @Override
    protected String formatWKT(final Formatter formatter) {
        super.formatWKT(formatter);
        return "VERT_DATUM";
    }
}
