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
import java.util.HashMap;
import java.util.Map;
import javax.measure.unit.Unit;
import javax.measure.unit.NonSI;
import javax.measure.quantity.Angle;

import org.opengis.referencing.cs.AxisDirection;
import org.opengis.referencing.cs.CoordinateSystem;
import org.opengis.referencing.cs.CoordinateSystemAxis;
import org.opengis.referencing.cs.EllipsoidalCS;
import org.opengis.referencing.datum.Ellipsoid;
import org.opengis.referencing.datum.GeodeticDatum;
import org.opengis.referencing.crs.GeographicCRS;
import org.opengis.geometry.MismatchedDimensionException;

import org.geotools.measure.Measure;
import org.geotools.metadata.iso.extent.ExtentImpl;
import org.geotools.referencing.wkt.Formatter;
import org.geotools.referencing.AbstractReferenceSystem;  // For javadoc
import org.geotools.referencing.cs.DefaultEllipsoidalCS;
import org.geotools.referencing.datum.DefaultEllipsoid;
import org.geotools.referencing.datum.DefaultGeodeticDatum;
import org.geotools.util.UnsupportedImplementationException;


/**
 * A coordinate reference system based on an ellipsoidal approximation of the geoid; this provides
 * an accurate representation of the geometry of geographic features for a large portion of the
 * earth's surface.
 *
 * <TABLE CELLPADDING='6' BORDER='1'>
 * <TR BGCOLOR="#EEEEFF"><TH NOWRAP>Used with CS type(s)</TH></TR>
 * <TR><TD>
 *   {@link EllipsoidalCS Ellipsoidal}
 * </TD></TR></TABLE>
 *
 * @since 2.1
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public class DefaultGeographicCRS extends AbstractSingleCRS implements GeographicCRS {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = 861224913438092335L;

    /**
     * A two-dimensional geographic coordinate reference system using WGS84 datum.
     * This CRS uses (<var>longitude</var>,<var>latitude</var>) ordinates with longitude values
     * increasing East and latitude values increasing North. Angular units are decimal degrees and
     * prime meridian is Greenwich.
     */
    public static final DefaultGeographicCRS WGS84;

    /**
     * A three-dimensional geographic coordinate reference system using WGS84 datum.
     * This CRS uses (<var>longitude</var>,<var>latitude</var>,<var>height</var>)
     * ordinates with longitude values increasing East, latitude values increasing
     * North and height above the ellipsoid in metres. Angular units are decimal degrees and
     * prime meridian is Greenwich.
     */
    public static final DefaultGeographicCRS WGS84_3D;
    static {
        final Map<String,Object> properties = new HashMap<String,Object>(4);
        properties.put(NAME_KEY, "WGS84(DD)"); // Name used in WCS 1.0.
        final String[] alias = {
            "WGS84",
            "WGS 84"  // EPSG name.
        };
        properties.put(ALIAS_KEY, alias);
        properties.put(DOMAIN_OF_VALIDITY_KEY, ExtentImpl.WORLD);
        WGS84    = new DefaultGeographicCRS(properties, DefaultGeodeticDatum.WGS84,
                                            DefaultEllipsoidalCS.GEODETIC_2D);
        alias[1] = "WGS 84 (geographic 3D)"; // Replaces the EPSG name.
        WGS84_3D = new DefaultGeographicCRS(properties, DefaultGeodeticDatum.WGS84,
                                            DefaultEllipsoidalCS.GEODETIC_3D);
    }

    /**
     * Constructs a new geographic CRS with the same values than the specified one.
     * This copy constructor provides a way to wrap an arbitrary implementation into a
     * Geotools one or a user-defined one (as a subclass), usually in order to leverage
     * some implementation-specific API. This constructor performs a shallow copy,
     * i.e. the properties are not cloned.
     *
     * @param crs The coordinate reference system to copy.
     *
     * @since 2.2
     */
    public DefaultGeographicCRS(final GeographicCRS crs) {
        super(crs);
    }

    /**
     * Constructs a geographic CRS with the same properties than the given datum.
     * The inherited properties include the {@linkplain #getName name} and aliases.
     *
     * @param datum The datum.
     * @param cs The coordinate system.
     *
     * @since 2.5
     */
    public DefaultGeographicCRS(final GeodeticDatum datum, final EllipsoidalCS cs) {
        this(getProperties(datum), datum, cs);
    }

    /**
     * Constructs a geographic CRS from a name.
     *
     * @param name The name.
     * @param datum The datum.
     * @param cs The coordinate system.
     */
    public DefaultGeographicCRS(final String         name,
                                final GeodeticDatum datum,
                                final EllipsoidalCS    cs)
    {
        this(Collections.singletonMap(NAME_KEY, name), datum, cs);
    }

    /**
     * Constructs a geographic CRS from a set of properties. The properties are given unchanged to
     * the {@linkplain AbstractReferenceSystem#AbstractReferenceSystem(Map) super-class constructor}.
     *
     * @param properties Set of properties. Should contains at least {@code "name"}.
     * @param datum The datum.
     * @param cs The coordinate system.
     */
    public DefaultGeographicCRS(final Map<String,?> properties,
                                final GeodeticDatum datum,
                                final EllipsoidalCS cs)
    {
        super(properties, datum, cs);
    }

    /**
     * Returns the coordinate system.
     */
    @Override
    public EllipsoidalCS getCoordinateSystem() {
        return (EllipsoidalCS) super.getCoordinateSystem();
    }

    /**
     * Returns the datum.
     */
    @Override
    public GeodeticDatum getDatum() {
        return (GeodeticDatum) super.getDatum();
    }

    /**
     * Computes the orthodromic distance between two points. This convenience method delegates
     * the work to the underlyling {@linkplain DefaultEllipsoid ellipsoid}, if possible.
     *
     * @param  coord1 Coordinates of the first point.
     * @param  coord2 Coordinates of the second point.
     * @return The distance between {@code coord1} and {@code coord2}.
     * @throws UnsupportedOperationException if this coordinate reference system can't compute
     *         distances.
     * @throws MismatchedDimensionException if a coordinate doesn't have the expected dimension.
     */
    @Override
    public Measure distance(final double[] coord1, final double[] coord2)
            throws UnsupportedOperationException, MismatchedDimensionException
    {
        final DefaultEllipsoidalCS cs;
        final DefaultEllipsoid e;
        if (!(coordinateSystem instanceof DefaultEllipsoidalCS)) {
            throw new UnsupportedImplementationException(coordinateSystem.getClass());
        }
        final Ellipsoid ellipsoid = ((GeodeticDatum) datum).getEllipsoid();
        if (!(ellipsoid instanceof DefaultEllipsoid)) {
            throw new UnsupportedImplementationException(ellipsoid.getClass());
        }
        cs = (DefaultEllipsoidalCS) coordinateSystem;
        e  = (DefaultEllipsoid)     ellipsoid;
        if (coord1.length!=2 || coord2.length!=2 || cs.getDimension()!=2) {
            /*
             * Not yet implemented (an exception will be thrown later).
             * We should probably revisit the way we compute distances.
             */
            return super.distance(coord1, coord2);
        }
        return new Measure(e.orthodromicDistance(cs.getLongitude(coord1),
                                                 cs.getLatitude (coord1),
                                                 cs.getLongitude(coord2),
                                                 cs.getLatitude (coord2)), e.getAxisUnit());
    }

    /**
     * Returns a hash value for this geographic CRS.
     *
     * @return The hash code value. This value doesn't need to be the same
     *         in past or future versions of this class.
     */
    @Override
    public int hashCode() {
        return (int)serialVersionUID ^ super.hashCode();
    }

    /**
     * Returns the angular unit of the specified coordinate system.
     * The preference will be given to the longitude axis, if found.
     */
    static Unit<Angle> getAngularUnit(final CoordinateSystem coordinateSystem) {
        Unit<Angle> unit = NonSI.DEGREE_ANGLE;
        for (int i=coordinateSystem.getDimension(); --i>=0;) {
            final CoordinateSystemAxis axis = coordinateSystem.getAxis(i);
            final Unit<?> candidate = axis.getUnit();
            if (NonSI.DEGREE_ANGLE.isCompatible(candidate)) {
                unit = candidate.asType(Angle.class);
                if (AxisDirection.EAST.equals(axis.getDirection().absolute())) {
                    break; // Found the longitude axis.
                }
            }
        }
        return unit;
    }

    /**
     * Format the inner part of a
     * <A HREF="http://geoapi.sourceforge.net/snapshot/javadoc/org/opengis/referencing/doc-files/WKT.html"><cite>Well
     * Known Text</cite> (WKT)</A> element.
     *
     * @param  formatter The formatter to use.
     * @return The name of the WKT element type, which is {@code "GEOGCS"}.
     */
    @Override
    protected String formatWKT(final Formatter formatter) {
        final Unit<Angle> oldUnit = formatter.getAngularUnit();
        final Unit<Angle> unit = getAngularUnit(coordinateSystem);
        formatter.setAngularUnit(unit);
        formatter.append(datum);
        formatter.append(((GeodeticDatum) datum).getPrimeMeridian());
        formatter.append(unit);
        final int dimension = coordinateSystem.getDimension();
        for (int i=0; i<dimension; i++) {
            formatter.append(coordinateSystem.getAxis(i));
        }
        if (!unit.equals(getUnit())) {
            formatter.setInvalidWKT(GeographicCRS.class);
        }
        formatter.setAngularUnit(oldUnit);
        return "GEOGCS";
    }
}
