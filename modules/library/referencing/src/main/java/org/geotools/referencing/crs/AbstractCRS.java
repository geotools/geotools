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
import java.util.HashMap;
import javax.measure.unit.Unit;

import org.opengis.referencing.cs.CoordinateSystem;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.util.InternationalString;

import org.geotools.measure.Measure;
import org.geotools.referencing.AbstractIdentifiedObject;
import org.geotools.referencing.AbstractReferenceSystem;
import org.geotools.referencing.cs.AbstractCS;
import org.geotools.referencing.wkt.Formatter;
import org.geotools.resources.CRSUtilities;
import org.geotools.resources.i18n.Vocabulary;
import org.geotools.util.UnsupportedImplementationException;


/**
 * Abstract coordinate reference system, usually defined by a coordinate system and a datum.
 *
 * @since 2.1
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 *
 * @see AbstractCS
 * @see org.geotools.referencing.datum.AbstractDatum
 * @tutorial http://docs.codehaus.org/display/GEOTOOLS/Coordinate+Transformation+Services+for+Geotools+2.1
 */
public abstract class AbstractCRS extends AbstractReferenceSystem implements CoordinateReferenceSystem {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = -7433284548909530047L;

    /**
     * The coordinate system.
     */
    protected final CoordinateSystem coordinateSystem;

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
    public AbstractCRS(final CoordinateReferenceSystem crs) {
        super(crs);
        coordinateSystem = crs.getCoordinateSystem();
    }

    /**
     * Constructs a coordinate reference system from a set of properties. The properties are given
     * unchanged to the {@linkplain AbstractReferenceSystem#AbstractReferenceSystem(Map) super-class
     * constructor}.
     *
     * @param properties Set of properties. Should contains at least {@code "name"}.
     * @param cs The coordinate system.
     */
    public AbstractCRS(final Map<String,?> properties, final CoordinateSystem cs) {
        super(properties);
        ensureNonNull("cs", cs);
        this.coordinateSystem = cs;
    }

    /**
     * Creates a name for the predefined constants in subclasses. The name is an unlocalized String
     * object. However, since this method is used for creation of convenience objects only (not for
     * objects created from an "official" database), the "unlocalized" name is actually choosen
     * according the user's locale at class initialization time. The same name is also added in
     * a localizable form as an alias. Since the {@link #nameMatches} convenience method checks
     * the alias, it still possible to consider two objects are equivalent even if their names
     * were formatted in different locales.
     */
    static Map<String,?> name(final int key) {
        final Map<String,Object> properties = new HashMap<String,Object>(4);
        final InternationalString name = Vocabulary.formatInternational(key);
        properties.put(NAME_KEY,  name.toString());
        properties.put(ALIAS_KEY, name);
        return properties;
    }

    /**
     * Returns the coordinate system.
     */
    public CoordinateSystem getCoordinateSystem() {
        return coordinateSystem;
    }

    /**
     * Returns the unit used for all axis. If not all axis uses the same unit,
     * then this method returns {@code null}. This method is often used for
     * Well Know Text (WKT) formatting.
     */
    final Unit<?> getUnit() {
        return CRSUtilities.getUnit(coordinateSystem);
    }

    /**
     * Computes the distance between two points. This convenience method delegates the work to
     * the underlyling {@linkplain AbstractCS coordinate system}, if possible.
     *
     * @param  coord1 Coordinates of the first point.
     * @param  coord2 Coordinates of the second point.
     * @return The distance between {@code coord1} and {@code coord2}.
     * @throws UnsupportedOperationException if this coordinate reference system can't compute
     *         distances.
     * @throws MismatchedDimensionException if a coordinate doesn't have the expected dimension.
     */
    public Measure distance(final double[] coord1, final double[] coord2)
            throws UnsupportedOperationException, MismatchedDimensionException
    {
        if (coordinateSystem instanceof AbstractCS) {
            return ((AbstractCS) coordinateSystem).distance(coord1, coord2);
        }
        throw new UnsupportedImplementationException(coordinateSystem.getClass());
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
            final AbstractCRS that = (AbstractCRS) object;
            return equals(this.coordinateSystem, that.coordinateSystem, compareMetadata);
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
        return (int)serialVersionUID ^ coordinateSystem.hashCode();
    }

    /**
     * Formats the inner part of a
     * <A HREF="http://geoapi.sourceforge.net/snapshot/javadoc/org/opengis/referencing/doc-files/WKT.html"><cite>Well
     * Known Text</cite> (WKT)</A> element. The default implementation writes the following
     * elements:
     * <ul>
     *   <li>The {@linkplain #datum datum}, if any.</li>
     *   <li>The unit if all axis use the same unit. Otherwise the unit is omitted and
     *       the WKT format is {@linkplain Formatter#isInvalidWKT flagged as invalid}.</li>
     *   <li>All {@linkplain #coordinateSystem coordinate system}'s axis.</li>
     * </ul>
     *
     * @param  formatter The formatter to use.
     * @return The name of the WKT element type (e.g. {@code "GEOGCS"}).
     */
    @Override
    protected String formatWKT(final Formatter formatter) {
        formatDefaultWKT(formatter);
        // Will declares the WKT as invalid.
        return super.formatWKT(formatter);
    }

    /**
     * Default implementation of {@link #formatWKT}. For {@link DefaultEngineeringCRS}
     * and {@link DefaultVerticalCRS} use only.
     */
    void formatDefaultWKT(final Formatter formatter) {
        final Unit<?> unit = getUnit();
        formatter.append(unit);
        final int dimension = coordinateSystem.getDimension();
        for (int i=0; i<dimension; i++) {
            formatter.append(coordinateSystem.getAxis(i));
        }
        if (unit == null) {
            formatter.setInvalidWKT(CoordinateReferenceSystem.class);
        }
    }
}
