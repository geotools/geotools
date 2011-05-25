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

import java.util.Collections;
import java.util.Map;
import javax.measure.unit.SI;

import org.opengis.referencing.cs.CoordinateSystem;
import org.opengis.referencing.crs.EngineeringCRS;
import org.opengis.referencing.datum.EngineeringDatum;

import org.geotools.referencing.AbstractIdentifiedObject;
import org.geotools.referencing.AbstractReferenceSystem;
import org.geotools.referencing.datum.DefaultEngineeringDatum;
import org.geotools.referencing.cs.DefaultCoordinateSystemAxis;
import org.geotools.referencing.cs.DefaultCartesianCS;
import org.geotools.referencing.wkt.Formatter;
import org.geotools.resources.i18n.VocabularyKeys;
import org.geotools.util.Utilities;


/**
 * A contextually local coordinate reference system. It can be divided into two broad categories:
 * <ul>
 *   <li>earth-fixed systems applied to engineering activities on or near the surface of the
 *       earth;</li>
 *   <li>CRSs on moving platforms such as road vehicles, vessels, aircraft, or spacecraft.</li>
 * </ul>
 *
 * <TABLE CELLPADDING='6' BORDER='1'>
 * <TR BGCOLOR="#EEEEFF"><TH NOWRAP>Used with CS type(s)</TH></TR>
 * <TR><TD>
 *   {@link org.opengis.referencing.cs.CartesianCS    Cartesian},
 *   {@link org.opengis.referencing.cs.AffineCS       Affine},
 *   {@link org.opengis.referencing.cs.EllipsoidalCS  Ellipsoidal},
 *   {@link org.opengis.referencing.cs.SphericalCS    Spherical},
 *   {@link org.opengis.referencing.cs.CylindricalCS  Cylindrical},
 *   {@link org.opengis.referencing.cs.PolarCS        Polar},
 *   {@link org.opengis.referencing.cs.VerticalCS     Vertical},
 *   {@link org.opengis.referencing.cs.LinearCS       Linear}
 * </TD></TR></TABLE>
 *
 * @since 2.1
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public class DefaultEngineeringCRS extends AbstractSingleCRS implements EngineeringCRS {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = 6695541732063382701L;

    /**
     * A cartesian local coordinate system.
     */
    private static final class Cartesian extends DefaultEngineeringCRS {
        /** Serial number for interoperability with different versions. */
        private static final long serialVersionUID = -1773381554353809683L;

        /** Constructs a coordinate system with the given name. */
        public Cartesian(final int key, final CoordinateSystem cs) {
            super(name(key), DefaultEngineeringDatum.UNKNOW, cs);
        }

        /**
         * Compares the specified object to this CRS for equality. This method is overridden
         * because, otherwise, {@code CARTESIAN_xD} and {@code GENERIC_xD} would be considered
         * equals when metadata are ignored.
         */
        @Override
        public boolean equals(final AbstractIdentifiedObject object, final boolean compareMetadata) {
            if (super.equals(object, compareMetadata)) {
                if (compareMetadata) {
                    // No need to performs the check below if metadata were already compared.
                    return true;
                }
                final Cartesian that = (Cartesian) object;
                return Utilities.equals(this.getName().getCode(), that.getName().getCode());
            }
            return false;
        }
    }

    /**
     * A two-dimensional cartesian coordinate reference system with
     * {@linkplain DefaultCoordinateSystemAxis#X x},
     * {@linkplain DefaultCoordinateSystemAxis#Y y}
     * axis in {@linkplain SI#METER metres}. By default, this CRS has no transformation
     * path to any other CRS (i.e. a map using this CS can't be reprojected to a
     * {@linkplain DefaultGeographicCRS geographic coordinate reference system} for example).
     */
    public static final DefaultEngineeringCRS CARTESIAN_2D =
            new Cartesian(VocabularyKeys.CARTESIAN_2D, DefaultCartesianCS.GENERIC_2D);

    /**
     * A three-dimensional cartesian coordinate reference system with
     * {@linkplain DefaultCoordinateSystemAxis#X x},
     * {@linkplain DefaultCoordinateSystemAxis#Y y},
     * {@linkplain DefaultCoordinateSystemAxis#Z z}
     * axis in {@linkplain SI#METER metres}. By default, this CRS has no transformation
     * path to any other CRS (i.e. a map using this CS can't be reprojected to a
     * {@linkplain DefaultGeographicCRS geographic coordinate reference system} for example).
     */
    public static final DefaultEngineeringCRS CARTESIAN_3D =
            new Cartesian(VocabularyKeys.CARTESIAN_3D, DefaultCartesianCS.GENERIC_3D);

    /**
     * A two-dimensional wildcard coordinate system with
     * {@linkplain DefaultCoordinateSystemAxis#X x},
     * {@linkplain DefaultCoordinateSystemAxis#Y y}
     * axis in {@linkplain SI#METER metres}. At the difference of {@link #CARTESIAN_2D},
     * this coordinate system is treated specially by the default {@linkplain
     * org.geotools.referencing.operation.DefaultCoordinateOperationFactory coordinate operation
     * factory} with loose transformation rules: if no transformation path were found (for example
     * through a {@linkplain DefaultDerivedCRS derived CRS}), then the transformation from this
     * CRS to any CRS with a compatible number of dimensions is assumed to be the identity
     * transform. This CRS is usefull as a kind of wildcard when no CRS were explicitly specified.
     */
    public static final DefaultEngineeringCRS GENERIC_2D =
            new Cartesian(VocabularyKeys.GENERIC_CARTESIAN_2D, DefaultCartesianCS.GENERIC_2D);

    /**
     * A three-dimensional wildcard coordinate system with
     * {@linkplain DefaultCoordinateSystemAxis#X x},
     * {@linkplain DefaultCoordinateSystemAxis#Y y},
     * {@linkplain DefaultCoordinateSystemAxis#Z z}
     * axis in {@linkplain SI#METER metres}. At the difference of {@link #CARTESIAN_3D},
     * this coordinate system is treated specially by the default {@linkplain
     * org.geotools.referencing.operation.DefaultCoordinateOperationFactory coordinate operation
     * factory} with loose transformation rules: if no transformation path were found (for example
     * through a {@linkplain DefaultDerivedCRS derived CRS}), then the transformation from this
     * CRS to any CRS with a compatible number of dimensions is assumed to be the identity
     * transform. This CRS is usefull as a kind of wildcard when no CRS were explicitly specified.
     */
    public static final DefaultEngineeringCRS GENERIC_3D =
            new Cartesian(VocabularyKeys.GENERIC_CARTESIAN_3D, DefaultCartesianCS.GENERIC_3D);

    /**
     * Constructs a new enginnering CRS with the same values than the specified one.
     * This copy constructor provides a way to wrap an arbitrary implementation into a
     * Geotools one or a user-defined one (as a subclass), usually in order to leverage
     * some implementation-specific API. This constructor performs a shallow copy,
     * i.e. the properties are not cloned.
     *
     * @param crs The CRS to copy.
     *
     * @since 2.2
     */
    public DefaultEngineeringCRS(final EngineeringCRS crs) {
        super(crs);
    }

    /**
     * Constructs an engineering CRS from a name.
     *
     * @param name The name.
     * @param datum The datum.
     * @param cs The coordinate system.
     */
    public DefaultEngineeringCRS(final String            name,
                                 final EngineeringDatum datum,
                                 final CoordinateSystem    cs)
    {
        this(Collections.singletonMap(NAME_KEY, name), datum, cs);
    }

    /**
     * Constructs an engineering CRS from a set of properties. The properties are given unchanged to
     * the {@linkplain AbstractReferenceSystem#AbstractReferenceSystem(Map) super-class constructor}.
     *
     * @param properties Set of properties. Should contains at least {@code "name"}.
     * @param datum The datum.
     * @param cs The coordinate system.
     */
    public DefaultEngineeringCRS(final Map<String,?> properties,
                                 final EngineeringDatum   datum,
                                 final CoordinateSystem      cs)
    {
        super(properties, datum, cs);
    }

    /**
     * Returns the datum.
     */
    @Override
    public EngineeringDatum getDatum() {
        return (EngineeringDatum) super.getDatum();
    }

    /**
     * Returns a hash value for this derived CRS.
     *
     * @return The hash code value. This value doesn't need to be the same
     *         in past or future versions of this class.
     */
    @Override
    public int hashCode() {
        return (int)serialVersionUID ^ super.hashCode();
    }

    /**
     * Format the inner part of a
     * <A HREF="http://geoapi.sourceforge.net/snapshot/javadoc/org/opengis/referencing/doc-files/WKT.html"><cite>Well
     * Known Text</cite> (WKT)</A> element.
     *
     * @param  formatter The formatter to use.
     * @return The name of the WKT element type, which is {@code "LOCAL_CS"}.
     */
    @Override
    protected String formatWKT(final Formatter formatter) {
        formatDefaultWKT(formatter);
        return "LOCAL_CS";
    }
}
