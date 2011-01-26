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
package org.geotools.referencing.crs;

import java.util.Map;

import org.opengis.referencing.cs.CoordinateSystem;
import org.opengis.referencing.cs.CoordinateSystemAxis;
import org.opengis.referencing.crs.SingleCRS;
import org.opengis.referencing.datum.Datum;

import org.geotools.referencing.AbstractIdentifiedObject;
import org.geotools.referencing.wkt.Formatter;


/**
 * Abstract coordinate reference system, consisting of a single
 * {@linkplain CoordinateSystem coordinate system} and a single
 * {@linkplain Datum datum} (as opposed to {@linkplain DefaultCompoundCRS compound CRS}).
 * <p>
 * A coordinate reference system consists of an ordered sequence of coordinate system
 * axes that are related to the earth through a datum. A coordinate reference system
 * is defined by one datum and by one coordinate system. Most coordinate reference system
 * do not move relative to the earth, except for engineering coordinate reference systems
 * defined on moving platforms such as cars, ships, aircraft, and spacecraft.
 * <p>
 * Coordinate reference systems are commonly divided into sub-types. The common classification
 * criterion for sub-typing of coordinate reference systems is the way in which they deal with
 * earth curvature. This has a direct effect on the portion of the earth's surface that can be
 * covered by that type of CRS with an acceptable degree of error. The exception to the rule is
 * the subtype "Temporal" which has been added by analogy.
 * <p>
 * This class is conceptually <cite>abstract</cite>, even if it is technically possible to
 * instantiate it. Typical applications should create instances of the most specific subclass with
 * {@code Default} prefix instead. An exception to this rule may occurs when it is not possible to
 * identify the exact type.
 *
 * @since 2.1
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 *
 * @see org.geotools.referencing.cs.AbstractCS
 * @see org.geotools.referencing.datum.AbstractDatum
 */
public class AbstractSingleCRS extends AbstractCRS implements SingleCRS {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = 1815712797774273L;

    /**
     * The datum.
     */
    protected final Datum datum;

    /**
     * Constructs a new coordinate reference system with the same values than the specified one.
     * This copy constructor provides a way to wrap an arbitrary implementation into a
     * Geotools one or a user-defined one (as a subclass), usually in order to leverage
     * some implementation-specific API. This constructor performs a shallow copy,
     * i.e. the properties are not cloned.
     *
     * @param crs The coordinate reference system to copy.
     *
     * @since 2.2
     */
    public AbstractSingleCRS(final SingleCRS crs) {
        super(crs);
        datum = crs.getDatum();
    }

    /**
     * Constructs a coordinate reference system from a set of properties.
     * The properties are given unchanged to the
     * {@linkplain org.geotools.referencing.AbstractReferenceSystem#AbstractReferenceSystem(Map)
     * super-class constructor}.
     *
     * @param properties Set of properties. Should contains at least {@code "name"}.
     * @param datum The datum.
     * @param cs The coordinate system.
     */
    public AbstractSingleCRS(final Map<String,?> properties,
                             final Datum datum,
                             final CoordinateSystem cs)
    {
        super(properties, cs);
        this.datum = datum;
        ensureNonNull("datum", datum);
    }

    /**
     * Returns the datum.
     *
     * @return The datum.
     */
    public Datum getDatum() {
        return datum;
    }

    /**
     * Returns the dimension of the underlying {@linkplain CoordinateSystem coordinate system}.
     * This is equivalent to <code>{@linkplain #coordinateSystem}.{@linkplain
     * CoordinateSystem#getDimension getDimension}()</code>.
     *
     * @return The dimension of this coordinate reference system.
     */
    public int getDimension() {
        return coordinateSystem.getDimension();
    }

    /**
     * Returns the axis for the underlying {@linkplain CoordinateSystem coordinate system} at
     * the specified dimension. This is equivalent to
     * <code>{@linkplain #coordinateSystem}.{@linkplain CoordinateSystem#getAxis getAxis}(dimension)</code>.
     *
     * @param  dimension The zero based index of axis.
     * @return The axis at the specified dimension.
     * @throws IndexOutOfBoundsException if {@code dimension} is out of bounds.
     */
    public CoordinateSystemAxis getAxis(int dimension) throws IndexOutOfBoundsException {
        return coordinateSystem.getAxis(dimension);
    }

    /**
     * Compare this coordinate reference system with the specified object for equality.
     * If {@code compareMetadata} is {@code true}, then all available properties are
     * compared including {@linkplain #getValidArea valid area} and {@linkplain #getScope scope}.
     *
     * @param  object The object to compare to {@code this}.
     * @param  compareMetadata {@code true} for performing a strict comparaison, or
     *         {@code false} for comparing only properties relevant to transformations.
     * @return {@code true} if both objects are equal.
     */
    @Override
    public boolean equals(final AbstractIdentifiedObject object, final boolean compareMetadata) {
        if (super.equals(object, compareMetadata)) {
            final AbstractSingleCRS that = (AbstractSingleCRS) object;
            return equals(this.datum, that.datum, compareMetadata);
        }
        return false;
    }

    /**
     * Returns a hash value for this CRS. {@linkplain #getName Name},
     * {@linkplain #getIdentifiers identifiers} and {@linkplain #getRemarks remarks}
     * are not taken in account. In other words, two CRS objects will return the same
     * hash value if they are equal in the sense of
     * <code>{@link #equals(AbstractIdentifiedObject,boolean) equals}(AbstractIdentifiedObject,
     * <strong>false</strong>)</code>.
     *
     * @return The hash code value. This value doesn't need to be the same
     *         in past or future versions of this class.
     */
    @Override
    public int hashCode() {
        return super.hashCode() ^ datum.hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    final void formatDefaultWKT(final Formatter formatter) {
        formatter.append(datum);
        super.formatDefaultWKT(formatter);
    }
}
