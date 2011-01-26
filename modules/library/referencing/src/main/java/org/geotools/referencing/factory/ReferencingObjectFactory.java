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
package org.geotools.referencing.factory;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import javax.measure.unit.Unit;
import javax.measure.quantity.Angle;
import javax.measure.quantity.Length;

import org.opengis.referencing.*;
import org.opengis.referencing.cs.*;
import org.opengis.referencing.crs.*;
import org.opengis.referencing.datum.*;
import org.opengis.referencing.operation.*;
import org.opengis.parameter.ParameterValueGroup;

import org.geotools.factory.Hints;
import org.geotools.factory.Factory;
import org.geotools.factory.BufferedFactory;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.geotools.referencing.wkt.Parser;
import org.geotools.referencing.wkt.Symbols;
import org.geotools.referencing.cs.*;
import org.geotools.referencing.crs.*;
import org.geotools.referencing.datum.*;
import org.geotools.referencing.operation.MathTransformProvider;
import org.geotools.referencing.operation.DefaultMathTransformFactory;
import org.geotools.util.CanonicalSet;


/**
 * Builds Geotools implementations of {@linkplain CoordinateReferenceSystem CRS},
 * {@linkplain CoordinateSystem CS} and {@linkplain Datum datum} objects. Most factory methods
 * expect properties given through a {@link Map} argument. The content of this map is described
 * in the {@link ObjectFactory} interface.
 *
 * @since 2.4
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public class ReferencingObjectFactory extends ReferencingFactory
        implements CSFactory, DatumFactory, CRSFactory, BufferedFactory
{
    /**
     * The math transform factory. Will be created only when first needed.
     */
    private MathTransformFactory mtFactory;

    /**
     * The object to use for parsing <cite>Well-Known Text</cite> (WKT) strings.
     * Will be created only when first needed.
     */
    private Parser parser;

    /**
     * Set of weak references to existing objects (identifiers, CRS, Datum, whatever).
     * This set is used in order to return a pre-existing object instead of creating a
     * new one.
     */
    private final CanonicalSet<IdentifiedObject> pool;

    /**
     * Constructs a default factory. This method is public in order to allows instantiations
     * from a {@linkplain javax.imageio.spi.ServiceRegistry service registry}. Users should
     * not instantiate this factory directly, but use one of the following lines instead:
     *
     * <blockquote><pre>
     * {@linkplain DatumFactory} factory = FactoryFinder.{@linkplain ReferencingFactoryFinder#getDatumFactory getDatumFactory}null);
     * {@linkplain CSFactory}    factory = FactoryFinder.{@linkplain ReferencingFactoryFinder#getCSFactory    getCSFactory}(null);
     * {@linkplain CRSFactory}   factory = FactoryFinder.{@linkplain ReferencingFactoryFinder#getCRSFactory   getCRSFactory}(null);
     * </pre></blockquote>
     */
    public ReferencingObjectFactory() {
        this(null);
    }

    /**
     * Constructs a factory with the specified hints. Users should not instantiate this
     * factory directly, but use one of the following lines instead:
     *
     * <blockquote><pre>
     * {@linkplain DatumFactory} factory = FactoryFinder.{@linkplain ReferencingFactoryFinder#getDatumFactory getDatumFactory}(hints);
     * {@linkplain CSFactory}    factory = FactoryFinder.{@linkplain ReferencingFactoryFinder#getCSFactory    getCSFactory}(hints);
     * {@linkplain CRSFactory}   factory = FactoryFinder.{@linkplain ReferencingFactoryFinder#getCRSFactory   getCRSFactory}(hints);
     * </pre></blockquote>
     *
     * @param hints An optional set of hints, or {@code null} if none.
     *
     * @since 2.5
     */
    public ReferencingObjectFactory(final Hints hints) {
        pool = CanonicalSet.newInstance(IdentifiedObject.class);
        if (hints != null && !hints.isEmpty()) {
            /*
             * Creates the dependencies (MathTransform factory, WKT parser...) now because
             * we need to process user's hints. Then, we will keep only the relevant hints.
             */
            mtFactory = ReferencingFactoryFinder.getMathTransformFactory(hints);
            final DatumFactory datumFactory = ReferencingFactoryFinder.getDatumFactory(hints);
            createParser(datumFactory, mtFactory);
            addHints(datumFactory);
            addHints(mtFactory);
        }
    }

    /**
     * Copies the hints from the supplied factory. Note that we do not expose the factories
     * themself (at the contrary of what we usually do) because it is a little bit strange
     * to declare that this factory depends on an other {@link DatumFactory}. It is only a
     * trick for getting the WKT paser to work with aliases.
     *
     * @todo We should remove this trick if we can. Possible alternatives may be: make
     *       DatumAliases to implements CRSFactory with appropriate createWKT(String)
     *       method; move the createWKT(String) method out of CRSFactory interface.
     */
    private void addHints(final Object factory) {
        if (factory instanceof Factory) {
            hints.putAll(((Factory) factory).getImplementationHints());
        }
    }

    /**
     * Returns the math transform factory for internal usage only. The hints given to
     * {@link ReferencingFactoryFinder} must be null, since the non-null case should
     * have been handled by the constructor.
     *
     * @see #createParser
     */
    private synchronized MathTransformFactory getMathTransformFactory() {
        if (mtFactory == null) {
            mtFactory = ReferencingFactoryFinder.getMathTransformFactory(null);
        }
        return mtFactory;
    }



    /////////////////////////////////////////////////////////////////////////////////////////
    ////////                                                                         ////////
    ////////                        D A T U M   F A C T O R Y                        ////////
    ////////                                                                         ////////
    /////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Creates an ellipsoid from radius values.
     *
     * @param  properties Name and other properties to give to the new object.
     * @param  semiMajorAxis Equatorial radius in supplied linear units.
     * @param  semiMinorAxis Polar radius in supplied linear units.
     * @param  unit Linear units of ellipsoid axes.
     * @throws FactoryException if the object creation failed.
     */
    public Ellipsoid createEllipsoid(Map<String,?> properties,
            double semiMajorAxis, double semiMinorAxis, Unit<Length> unit) throws FactoryException
    {
        Ellipsoid ellipsoid;
        try {
            ellipsoid = DefaultEllipsoid.createEllipsoid(
                        properties, semiMajorAxis, semiMinorAxis, unit);
        } catch (IllegalArgumentException exception) {
            throw new FactoryException(exception);
        }
        ellipsoid = pool.unique(ellipsoid);
        return ellipsoid;
    }

    /**
     * Creates an ellipsoid from an major radius, and inverse flattening.
     *
     * @param  properties Name and other properties to give to the new object.
     * @param  semiMajorAxis Equatorial radius in supplied linear units.
     * @param  inverseFlattening Eccentricity of ellipsoid.
     * @param  unit Linear units of major axis.
     * @throws FactoryException if the object creation failed.
     */
    public Ellipsoid createFlattenedSphere(Map<String,?> properties,
            double semiMajorAxis, double inverseFlattening, Unit<Length> unit) throws FactoryException
    {
        Ellipsoid ellipsoid;
        try {
            ellipsoid = DefaultEllipsoid.createFlattenedSphere(
                        properties, semiMajorAxis, inverseFlattening, unit);
        } catch (IllegalArgumentException exception) {
            throw new FactoryException(exception);
        }
        ellipsoid = pool.unique(ellipsoid);
        return ellipsoid;
    }

    /**
     * Creates a prime meridian, relative to Greenwich.
     *
     * @param  properties Name and other properties to give to the new object.
     * @param  longitude Longitude of prime meridian in supplied angular units East of Greenwich.
     * @param  angularUnit Angular units of longitude.
     * @throws FactoryException if the object creation failed.
     */
    public PrimeMeridian createPrimeMeridian(Map<String,?> properties,
            double longitude, Unit<Angle> angularUnit) throws FactoryException
    {
        PrimeMeridian meridian;
        try {
            meridian = new DefaultPrimeMeridian(properties, longitude, angularUnit);
        } catch (IllegalArgumentException exception) {
            throw new FactoryException(exception);
        }
        meridian = pool.unique(meridian);
        return meridian;
    }

    /**
     * Creates geodetic datum from ellipsoid and (optionaly) Bursa-Wolf parameters.
     *
     * @param  properties Name and other properties to give to the new object.
     * @param  ellipsoid Ellipsoid to use in new geodetic datum.
     * @param  primeMeridian Prime meridian to use in new geodetic datum.
     * @throws FactoryException if the object creation failed.
     */
    public GeodeticDatum createGeodeticDatum(Map<String,?> properties,
            Ellipsoid ellipsoid, PrimeMeridian primeMeridian) throws FactoryException
    {
        GeodeticDatum datum;
        try {
            datum = new DefaultGeodeticDatum(properties, ellipsoid, primeMeridian);
        } catch (IllegalArgumentException exception) {
            throw new FactoryException(exception);
        }
        datum = pool.unique(datum);
        return datum;
    }

    /**
     * Creates a vertical datum from an enumerated type value.
     *
     * @param  properties Name and other properties to give to the new object.
     * @param  type The type of this vertical datum (often geoidal).
     * @throws FactoryException if the object creation failed.
     */
    public VerticalDatum createVerticalDatum(Map<String,?> properties, VerticalDatumType type)
            throws FactoryException
    {
        VerticalDatum datum;
        try {
            datum = new DefaultVerticalDatum(properties, type);
        } catch (IllegalArgumentException exception) {
            throw new FactoryException(exception);
        }
        datum = pool.unique(datum);
        return datum;
    }

    /**
     * Creates a temporal datum from an enumerated type value.
     *
     * @param  properties Name and other properties to give to the new object.
     * @param  origin The date and time origin of this temporal datum.
     * @throws FactoryException if the object creation failed.
     */
    public TemporalDatum createTemporalDatum(Map<String,?> properties, Date origin)
            throws FactoryException
    {
        TemporalDatum datum;
        try {
            datum = new DefaultTemporalDatum(properties, origin);
        } catch (IllegalArgumentException exception) {
            throw new FactoryException(exception);
        }
        datum = pool.unique(datum);
        return datum;
    }

    /**
     * Creates an engineering datum.
     *
     * @param  properties Name and other properties to give to the new object.
     * @throws FactoryException if the object creation failed.
     */
    public EngineeringDatum createEngineeringDatum(Map<String,?> properties)
            throws FactoryException
    {
        EngineeringDatum datum;
        try {
            datum = new DefaultEngineeringDatum(properties);
        } catch (IllegalArgumentException exception) {
            throw new FactoryException(exception);
        }
        datum = pool.unique(datum);
        return datum;
    }

    /**
     * Creates an image datum.
     *
     * @param  properties Name and other properties to give to the new object.
     * @param  pixelInCell Specification of the way the image grid is associated
     *         with the image data attributes.
     * @throws FactoryException if the object creation failed.
     */
    public ImageDatum createImageDatum(Map<String,?> properties, PixelInCell pixelInCell)
            throws FactoryException
    {
        ImageDatum datum;
        try {
            datum = new DefaultImageDatum(properties, pixelInCell);
        } catch (IllegalArgumentException exception) {
            throw new FactoryException(exception);
        }
        datum = pool.unique(datum);
        return datum;
    }



    /////////////////////////////////////////////////////////////////////////////////////////
    ////////                                                                         ////////
    ////////            C O O R D I N A T E   S Y S T E M   F A C T O R Y            ////////
    ////////                                                                         ////////
    /////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Creates a coordinate system axis from an abbreviation and a unit.
     *
     * @param  properties Name and other properties to give to the new object.
     * @param  abbreviation The coordinate axis abbreviation.
     * @param  direction The axis direction.
     * @param  unit The coordinate axis unit.
     * @throws FactoryException if the object creation failed.
     */
    public CoordinateSystemAxis createCoordinateSystemAxis(Map<String,?> properties,
            String abbreviation, AxisDirection direction, Unit<?> unit) throws FactoryException
    {
        CoordinateSystemAxis axis;
        try {
            axis = new DefaultCoordinateSystemAxis(properties, abbreviation, direction, unit);
        } catch (IllegalArgumentException exception) {
            throw new FactoryException(exception);
        }
        axis = pool.unique(axis);
        return axis;
    }

    /**
     * Creates a two dimensional cartesian coordinate system from the given pair of axis.
     *
     * @param  properties Name and other properties to give to the new object.
     * @param  axis0 The first  axis.
     * @param  axis1 The second axis.
     * @throws FactoryException if the object creation failed.
     */
    public CartesianCS createCartesianCS(Map<String,?>   properties,
                                         CoordinateSystemAxis axis0,
                                         CoordinateSystemAxis axis1) throws FactoryException
    {
        CartesianCS cs;
        try {
            cs = new DefaultCartesianCS(properties, axis0, axis1);
        } catch (IllegalArgumentException exception) {
            throw new FactoryException(exception);
        }
        cs = pool.unique(cs);
        return cs;
    }

    /**
     * Creates a three dimensional cartesian coordinate system from the given set of axis.
     *
     * @param  properties Name and other properties to give to the new object.
     * @param  axis0 The first  axis.
     * @param  axis1 The second axis.
     * @param  axis2 The third  axis.
     * @throws FactoryException if the object creation failed.
     */
    public CartesianCS createCartesianCS(Map<String,?>   properties,
                                         CoordinateSystemAxis axis0,
                                         CoordinateSystemAxis axis1,
                                         CoordinateSystemAxis axis2) throws FactoryException
    {
        CartesianCS cs;
        try {
            cs = new DefaultCartesianCS(properties, axis0, axis1, axis2);
        } catch (IllegalArgumentException exception) {
            throw new FactoryException(exception);
        }
        cs = pool.unique(cs);
        return cs;
    }

    /**
     * Creates a two dimensional coordinate system from the given pair of axis.
     *
     * @param  properties Name and other properties to give to the new object.
     * @param  axis0 The first  axis.
     * @param  axis1 The second axis.
     * @throws FactoryException if the object creation failed.
     */
    public AffineCS createAffineCS(Map<String,?>   properties,
                                   CoordinateSystemAxis axis0,
                                   CoordinateSystemAxis axis1) throws FactoryException
    {
        AffineCS cs;
        try {
            cs = new DefaultAffineCS(properties, axis0, axis1);
        } catch (IllegalArgumentException exception) {
            throw new FactoryException(exception);
        }
        cs = pool.unique(cs);
        return cs;
    }

    /**
     * Creates a three dimensional coordinate system from the given set of axis.
     *
     * @param  properties Name and other properties to give to the new object.
     * @param  axis0 The first  axis.
     * @param  axis1 The second axis.
     * @param  axis2 The third  axis.
     * @throws FactoryException if the object creation failed.
     */
    public AffineCS createAffineCS(Map<String,?>   properties,
                                   CoordinateSystemAxis axis0,
                                   CoordinateSystemAxis axis1,
                                   CoordinateSystemAxis axis2) throws FactoryException
    {
        AffineCS cs;
        try {
            cs = new DefaultAffineCS(properties, axis0, axis1, axis2);
        } catch (IllegalArgumentException exception) {
            throw new FactoryException(exception);
        }
        cs = pool.unique(cs);
        return cs;
    }

    /**
     * Creates a polar coordinate system from the given pair of axis.
     *
     * @param  properties Name and other properties to give to the new object.
     * @param  axis0 The first  axis.
     * @param  axis1 The second axis.
     * @throws FactoryException if the object creation failed.
     */
    public PolarCS createPolarCS(Map<String,?>   properties,
                                 CoordinateSystemAxis axis0,
                                 CoordinateSystemAxis axis1) throws FactoryException
    {
        PolarCS cs;
        try {
            cs = new DefaultPolarCS(properties, axis0, axis1);
        } catch (IllegalArgumentException exception) {
            throw new FactoryException(exception);
        }
        cs = pool.unique(cs);
        return cs;
    }

    /**
     * Creates a cylindrical coordinate system from the given set of axis.
     *
     * @param  properties Name and other properties to give to the new object.
     * @param  axis0 The first  axis.
     * @param  axis1 The second axis.
     * @param  axis2 The third  axis.
     * @throws FactoryException if the object creation failed.
     */
    public CylindricalCS createCylindricalCS(Map<String,?>   properties,
                                             CoordinateSystemAxis axis0,
                                             CoordinateSystemAxis axis1,
                                             CoordinateSystemAxis axis2) throws FactoryException
    {
        CylindricalCS cs;
        try {
            cs = new DefaultCylindricalCS(properties, axis0, axis1, axis2);
        } catch (IllegalArgumentException exception) {
            throw new FactoryException(exception);
        }
        cs = pool.unique(cs);
        return cs;
    }

    /**
     * Creates a spherical coordinate system from the given set of axis.
     *
     * @param  properties Name and other properties to give to the new object.
     * @param  axis0 The first  axis.
     * @param  axis1 The second axis.
     * @param  axis2 The third  axis.
     * @throws FactoryException if the object creation failed.
     */
    public SphericalCS createSphericalCS(Map<String,?>   properties,
                                         CoordinateSystemAxis axis0,
                                         CoordinateSystemAxis axis1,
                                         CoordinateSystemAxis axis2) throws FactoryException
    {
        SphericalCS cs;
        try {
            cs = new DefaultSphericalCS(properties, axis0, axis1, axis2);
        } catch (IllegalArgumentException exception) {
            throw new FactoryException(exception);
        }
        cs = pool.unique(cs);
        return cs;
    }

    /**
     * Creates an ellipsoidal coordinate system without ellipsoidal height.
     *
     * @param  properties Name and other properties to give to the new object.
     * @param  axis0 The first  axis.
     * @param  axis1 The second axis.
     * @throws FactoryException if the object creation failed.
     */
    public EllipsoidalCS createEllipsoidalCS(Map<String,?>   properties,
                                             CoordinateSystemAxis axis0,
                                             CoordinateSystemAxis axis1) throws FactoryException
    {
        EllipsoidalCS cs;
        try {
            cs = new DefaultEllipsoidalCS(properties, axis0, axis1);
        } catch (IllegalArgumentException exception) {
            throw new FactoryException(exception);
        }
        cs = pool.unique(cs);
        return cs;
    }

    /**
     * Creates an ellipsoidal coordinate system with ellipsoidal height.
     *
     * @param  properties Name and other properties to give to the new object.
     * @param  axis0 The first  axis.
     * @param  axis1 The second axis.
     * @param  axis2 The third  axis.
     * @throws FactoryException if the object creation failed.
     */
    public EllipsoidalCS createEllipsoidalCS(Map<String,?>   properties,
                                             CoordinateSystemAxis axis0,
                                             CoordinateSystemAxis axis1,
                                             CoordinateSystemAxis axis2) throws FactoryException
    {
        EllipsoidalCS cs;
        try {
            cs = new DefaultEllipsoidalCS(properties, axis0, axis1, axis2);
        } catch (IllegalArgumentException exception) {
            throw new FactoryException(exception);
        }
        cs = pool.unique(cs);
        return cs;
    }

    /**
     * Creates a vertical coordinate system.
     *
     * @param  properties Name and other properties to give to the new object.
     * @param  axis The axis.
     * @throws FactoryException if the object creation failed.
     */
    public VerticalCS createVerticalCS(Map<String,?>  properties,
                                       CoordinateSystemAxis axis) throws FactoryException
    {
        VerticalCS cs;
        try {
            cs = new DefaultVerticalCS(properties, axis);
        } catch (IllegalArgumentException exception) {
            throw new FactoryException(exception);
        }
        cs = pool.unique(cs);
        return cs;
    }

    /**
     * Creates a temporal coordinate system.
     *
     * @param  properties Name and other properties to give to the new object.
     * @param  axis The axis.
     * @throws FactoryException if the object creation failed.
     */
    public TimeCS createTimeCS(Map<String,?>  properties,
                               CoordinateSystemAxis axis) throws FactoryException
    {
        TimeCS cs;
        try {
            cs = new DefaultTimeCS(properties, axis);
        } catch (IllegalArgumentException exception) {
            throw new FactoryException(exception);
        }
        cs = pool.unique(cs);
        return cs;
    }

    /**
     * Creates a linear coordinate system.
     *
     * @param  properties Name and other properties to give to the new object.
     * @param  axis The axis.
     * @throws FactoryException if the object creation failed.
     */
    public LinearCS createLinearCS(Map<String,?>  properties,
                                   CoordinateSystemAxis axis) throws FactoryException
    {
        LinearCS cs;
        try {
            cs = new DefaultLinearCS(properties, axis);
        } catch (IllegalArgumentException exception) {
            throw new FactoryException(exception);
        }
        cs = pool.unique(cs);
        return cs;
    }

    /**
     * Creates a two dimensional user defined coordinate system from the given pair of axis.
     *
     * @param  properties Name and other properties to give to the new object.
     * @param  axis0 The first  axis.
     * @param  axis1 The second axis.
     * @throws FactoryException if the object creation failed.
     */
    public UserDefinedCS createUserDefinedCS(Map<String,?>   properties,
                                             CoordinateSystemAxis axis0,
                                             CoordinateSystemAxis axis1) throws FactoryException
    {
        UserDefinedCS cs;
        try {
            cs = new DefaultUserDefinedCS(properties, axis0, axis1);
        } catch (IllegalArgumentException exception) {
            throw new FactoryException(exception);
        }
        cs = pool.unique(cs);
        return cs;
    }

    /**
     * Creates a three dimensional user defined coordinate system from the given set of axis.
     *
     * @param  properties Name and other properties to give to the new object.
     * @param  axis0 The first  axis.
     * @param  axis1 The second axis.
     * @param  axis2 The third  axis.
     * @throws FactoryException if the object creation failed.
     */
    public UserDefinedCS createUserDefinedCS(Map<String,?>   properties,
                                             CoordinateSystemAxis axis0,
                                             CoordinateSystemAxis axis1,
                                             CoordinateSystemAxis axis2) throws FactoryException
    {
        UserDefinedCS cs;
        try {
            cs = new DefaultUserDefinedCS(properties, axis0, axis1, axis2);
        } catch (IllegalArgumentException exception) {
            throw new FactoryException(exception);
        }
        cs = pool.unique(cs);
        return cs;
    }



    /////////////////////////////////////////////////////////////////////////////////////////
    ////////                                                                         ////////
    ////////  C O O R D I N A T E   R E F E R E N C E   S Y S T E M   F A C T O R Y  ////////
    ////////                                                                         ////////
    /////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Creates a compound coordinate reference system from an ordered
     * list of {@code CoordinateReferenceSystem} objects.
     *
     * @param  properties Name and other properties to give to the new object.
     * @param  elements ordered array of {@code CoordinateReferenceSystem} objects.
     * @throws FactoryException if the object creation failed.
     */
    public CompoundCRS createCompoundCRS(Map<String,?> properties,
            CoordinateReferenceSystem[] elements) throws FactoryException
    {
        CompoundCRS crs;
        try {
            crs = new DefaultCompoundCRS(properties, elements);
        } catch (IllegalArgumentException exception) {
            throw new FactoryException(exception);
        }
        crs = pool.unique(crs);
        return crs;
    }

    /**
     * Creates a engineering coordinate reference system.
     *
     * @param  properties Name and other properties to give to the new object.
     * @param  datum Engineering datum to use in created CRS.
     * @param  cs The coordinate system for the created CRS.
     * @throws FactoryException if the object creation failed.
     */
    public EngineeringCRS createEngineeringCRS(Map<String,?> properties,
                                               EngineeringDatum datum,
                                               CoordinateSystem cs) throws FactoryException
    {
        EngineeringCRS crs;
        try {
            crs = new DefaultEngineeringCRS(properties, datum, cs);
        } catch (IllegalArgumentException exception) {
            throw new FactoryException(exception);
        }
        crs = pool.unique(crs);
        return crs;
    }

    /**
     * Creates an image coordinate reference system.
     *
     * @param  properties Name and other properties to give to the new object.
     * @param  datum Image datum to use in created CRS.
     * @param  cs The Cartesian or Oblique Cartesian coordinate system for the created CRS.
     * @throws FactoryException if the object creation failed.
     */
    public ImageCRS createImageCRS(Map<String,?> properties,
                                   ImageDatum    datum,
                                   AffineCS      cs) throws FactoryException
    {
        ImageCRS crs;
        try {
            crs = new DefaultImageCRS(properties, datum, cs);
        } catch (IllegalArgumentException exception) {
            throw new FactoryException(exception);
        }
        crs = pool.unique(crs);
        return crs;
    }

    /**
     * Creates a temporal coordinate reference system.
     *
     * @param  properties Name and other properties to give to the new object.
     * @param  datum Temporal datum to use in created CRS.
     * @param  cs The Temporal coordinate system for the created CRS.
     * @throws FactoryException if the object creation failed.
     */
    public TemporalCRS createTemporalCRS(Map<String,?> properties,
                                         TemporalDatum datum,
                                         TimeCS        cs) throws FactoryException
    {
        TemporalCRS crs;
        try {
            crs = new DefaultTemporalCRS(properties, datum, cs);
        } catch (IllegalArgumentException exception) {
            throw new FactoryException(exception);
        }
        crs = pool.unique(crs);
        return crs;
    }

    /**
     * Creates a vertical coordinate reference system.
     *
     * @param  properties Name and other properties to give to the new object.
     * @param  datum Vertical datum to use in created CRS.
     * @param  cs The Vertical coordinate system for the created CRS.
     * @throws FactoryException if the object creation failed.
     */
    public VerticalCRS createVerticalCRS(Map<String,?> properties,
                                         VerticalDatum datum,
                                         VerticalCS    cs) throws FactoryException
    {
        VerticalCRS crs;
        try {
            crs = new DefaultVerticalCRS(properties, datum, cs);
        } catch (IllegalArgumentException exception) {
            throw new FactoryException(exception);
        }
        crs = pool.unique(crs);
        return crs;
    }

    /**
     * Creates a geocentric coordinate reference system from a {@linkplain CartesianCS
     * cartesian coordinate system}.
     *
     * @param  properties Name and other properties to give to the new object.
     * @param  datum Geodetic datum to use in created CRS.
     * @param  cs The cartesian coordinate system for the created CRS.
     * @throws FactoryException if the object creation failed.
     */
    public GeocentricCRS createGeocentricCRS(Map<String,?> properties,
                                             GeodeticDatum datum,
                                             CartesianCS   cs) throws FactoryException
    {
        GeocentricCRS crs;
        try {
            crs = new DefaultGeocentricCRS(properties, datum, cs);
        } catch (IllegalArgumentException exception) {
            throw new FactoryException(exception);
        }
        crs = pool.unique(crs);
        return crs;
    }

    /**
     * Creates a geocentric coordinate reference system from a {@linkplain SphericalCS
     * spherical coordinate system}.
     *
     * @param  properties Name and other properties to give to the new object.
     * @param  datum Geodetic datum to use in created CRS.
     * @param  cs The spherical coordinate system for the created CRS.
     * @throws FactoryException if the object creation failed.
     */
    public GeocentricCRS createGeocentricCRS(Map<String,?> properties,
                                             GeodeticDatum datum,
                                             SphericalCS   cs) throws FactoryException
    {
        GeocentricCRS crs;
        try {
            crs = new DefaultGeocentricCRS(properties, datum, cs);
        } catch (IllegalArgumentException exception) {
            throw new FactoryException(exception);
        }
        crs = pool.unique(crs);
        return crs;
    }

    /**
     * Creates a geographic coordinate reference system.
     * It could be <var>Latitude</var>/<var>Longitude</var> or
     * <var>Longitude</var>/<var>Latitude</var>.
     *
     * @param  properties Name and other properties to give to the new object.
     * @param  datum Geodetic datum to use in created CRS.
     * @param  cs The ellipsoidal coordinate system for the created CRS.
     * @throws FactoryException if the object creation failed.
     */
    public GeographicCRS createGeographicCRS(Map<String,?> properties,
                                             GeodeticDatum datum,
                                             EllipsoidalCS cs) throws FactoryException
    {
        GeographicCRS crs;
        try {
            crs = new DefaultGeographicCRS(properties, datum, cs);
        } catch (IllegalArgumentException exception) {
            throw new FactoryException(exception);
        }
        crs = pool.unique(crs);
        return crs;
    }

    /**
     * Creates a derived coordinate reference system. If the transformation is an affine
     * map performing a rotation, then any mixed axes must have identical units.
     * For example, a (<var>lat_deg</var>, <var>lon_deg</var>, <var>height_feet</var>)
     * system can be rotated in the (<var>lat</var>, <var>lon</var>) plane, since both
     * affected axes are in decimal degrees. But you should not rotate this coordinate
     * system in any other plane.
     * <p>
     * <b>NOTE:</b>
     * It is the user's responsability to ensure that the {@code baseToDerived} transform performs
     * all required steps, including {@linkplain AbstractCS#swapAndScaleAxis unit conversions and
     * change of axis order}, if needed. The {@link ReferencingFactoryContainer} class provides
     * conveniences methods for this task.
     *
     * @param  properties Name and other properties to give to the new object.
     * @param  method A description of the {@linkplain Conversion#getMethod method for the
     *         conversion}.
     * @param  base Coordinate reference system to base the derived CRS on.
     * @param  baseToDerived The transform from the base CRS to returned CRS.
     * @param  derivedCS The coordinate system for the derived CRS.
     * @throws FactoryException if the object creation failed.
     *
     * @deprecated Use {@link CoordinateOperationFactory#createDefiningConversion} followed by
     *             {@link #createDerivedCRS} instead.
     */
    public DerivedCRS createDerivedCRS(Map<String,?>       properties,
                                       OperationMethod         method,
                                       CoordinateReferenceSystem base,
                                       MathTransform    baseToDerived,
                                       CoordinateSystem     derivedCS) throws FactoryException
    {
        DerivedCRS crs;
        try {
            crs = new DefaultDerivedCRS(properties, method, base, baseToDerived, derivedCS);
        } catch (IllegalArgumentException exception) {
            throw new FactoryException(exception);
        }
        crs = pool.unique(crs);
        return crs;
    }

    /**
     * Creates a derived coordinate reference system from a conversion.
     * It is the user's responsability to ensure that the conversion performs all required steps,
     * including {@linkplain AbstractCS#swapAndScaleAxis unit conversions and change of axis order},
     * if needed.
     *
     * @param  properties Name and other properties to give to the new object.
     * @param  baseCRS Coordinate reference system to base projection on.
     * @param  conversionFromBase The {@linkplain DefiningConversion defining conversion}.
     * @param  derivedCS The coordinate system for the derived CRS.
     * @throws FactoryException if the object creation failed.
     *
     * @since 2.5
     */
    public DerivedCRS createDerivedCRS(Map<String,?>          properties,
                                       CoordinateReferenceSystem baseCRS,
                                       Conversion     conversionFromBase,
                                       CoordinateSystem        derivedCS) throws FactoryException
    {
        MathTransform mt = conversionFromBase.getMathTransform();
        if (mt == null) {
            final ParameterValueGroup parameters = conversionFromBase.getParameterValues();
            final MathTransformFactory mtFactory = getMathTransformFactory();
            mt = mtFactory.createParameterizedTransform(parameters);
        }
        DerivedCRS crs;
        try {
            crs = new DefaultDerivedCRS(properties, conversionFromBase, baseCRS, mt, derivedCS);
        } catch (IllegalArgumentException exception) {
            throw new FactoryException(exception);
        }
        crs = pool.unique(crs);
        return crs;
    }

    /**
     * Creates a projected coordinate reference system from a transform.
     * <p>
     * <b>NOTE:</b>
     * It is the user's responsability to ensure that the {@code baseToDerived} transform performs
     * all required steps, including {@linkplain AbstractCS#swapAndScaleAxis unit conversions and
     * change of axis order}, if needed. The {@link ReferencingFactoryContainer} class provides
     * conveniences methods for this task.
     *
     * @param  properties Name and other properties to give to the new object.
     * @param  method A description of the {@linkplain Conversion#getMethod method for the
     *         projection}.
     * @param  base Geographic coordinate reference system to base projection on.
     * @param  baseToDerived The transform from the geographic to the projected CRS.
     * @param  derivedCS The coordinate system for the projected CRS.
     * @throws FactoryException if the object creation failed.
     *
     * @deprecated Use {@link CoordinateOperationFactory#createDefiningConversion} followed by
     *             {@link #createProjectedCRS} instead.
     */
    public ProjectedCRS createProjectedCRS(Map<String,?>   properties,
                                           OperationMethod method,
                                           GeographicCRS   base,
                                           MathTransform   baseToDerived,
                                           CartesianCS     derivedCS) throws FactoryException
    {
        ProjectedCRS crs;
        try {
            crs = new DefaultProjectedCRS(properties, method, base, baseToDerived, derivedCS);
        } catch (IllegalArgumentException exception) {
            throw new FactoryException(exception);
        }
        crs = pool.unique(crs);
        return crs;
    }

    /**
     * Creates a projected coordinate reference system from a conversion. The supplied
     * conversion should <strong>not</strong> includes the operation steps for performing
     * {@linkplain AbstractCS#swapAndScaleAxis unit conversions and change of axis order}
     * since those operations will be inferred by this constructor
     *
     * @param  properties Name and other properties to give to the new object.
     * @param  baseCRS Geographic coordinate reference system to base projection on.
     * @param  conversionFromBase The {@linkplain DefiningConversion defining conversion}.
     * @param  derivedCS The coordinate system for the projected CRS.
     * @throws FactoryException if the object creation failed.
     *
     * @since 2.5
     */
    public ProjectedCRS createProjectedCRS(Map<String,?> properties,
                                           GeographicCRS baseCRS,
                                           Conversion    conversionFromBase,
                                           CartesianCS   derivedCS) throws FactoryException
    {
        MathTransform mt;
        final MathTransform existing = conversionFromBase.getMathTransform();
        final MathTransformFactory mtFactory = getMathTransformFactory();
        if (existing != null && mtFactory instanceof DefaultMathTransformFactory) {
            /*
             * In the particular case of GeoTools implementation, we use a shortcut which avoid
             * the cost of creating a new parameterized transform; we use directly the existing
             * transform instance instead. It also avoid slight rounding errors as a side-effect.
             * This is because when a MapProjection is created, the angular parameters given to
             * the construtor are converted from degrees to radians. When the parameters are asked
             * back with 'conversionFromBase.getParameterValues()', they are converted back from
             * radians to degrees resulting in values slightly different than the original ones.
             * Those slight differences are enough for getting a math transform which is different
             * in the sense of MapProjection.equals(Object), with the usual consequences on cached
             * instances.
             */
            mt = ((DefaultMathTransformFactory) mtFactory).createBaseToDerived(baseCRS, existing, derivedCS);
        } else {
            /*
             * Non-GeoTools implementation, or no existing MathTransform instance.
             * Creates the transform from the parameters.
             */
            final ParameterValueGroup parameters = conversionFromBase.getParameterValues();
            mt = mtFactory.createBaseToDerived(baseCRS, parameters, derivedCS);
            OperationMethod method = conversionFromBase.getMethod();
            if (!(method instanceof MathTransformProvider)) {
                /*
                 * Our Geotools implementation of DefaultProjectedCRS may not be able to detect
                 * the conversion type (PlanarProjection, CylindricalProjection, etc.)  because
                 * we rely on the Geotools-specific MathTransformProvider for that. We will try
                 * to help it with the optional "conversionType" hint,  providing that the user
                 * do not already provides this hint.
                 */
                if (!properties.containsKey(DefaultProjectedCRS.CONVERSION_TYPE_KEY)) {
                    method = mtFactory.getLastMethodUsed();
                    if (method instanceof MathTransformProvider) {
                        final Map<String,Object> copy = new HashMap<String,Object>(properties);
                        copy.put(DefaultProjectedCRS.CONVERSION_TYPE_KEY,
                                ((MathTransformProvider) method).getOperationType());
                        properties = copy;
                    }
                }
            }
            /*
             * If the user gave an explicit conversion, checks if it is suitable.
             * It may not be suitable is unit conversion, axis switch, etc. have
             * been inserted in the operation chain by 'createBaseToDerived'.
             */
            if (existing != null && existing.equals(mt)) {
                mt = existing;
            }
        }
        ProjectedCRS crs;
        try {
            crs = new DefaultProjectedCRS(properties, conversionFromBase, baseCRS, mt, derivedCS);
        } catch (IllegalArgumentException exception) {
            throw new FactoryException(exception);
        }
        crs = pool.unique(crs);
        return crs;
    }

    /**
     * Creates a coordinate reference system object from a XML string.
     *
     * @param  xml Coordinate reference system encoded in XML format.
     * @throws FactoryException if the object creation failed.
     *
     * @todo Not yet implemented.
     */
    public CoordinateReferenceSystem createFromXML(String xml) throws FactoryException {
        throw new FactoryException("Not yet implemented");
    }

    /**
     * Creates a coordinate reference system object from a string.
     *
     * @param  wkt Coordinate system encoded in Well-Known Text format.
     * @throws FactoryException if the object creation failed.
     */
    public synchronized CoordinateReferenceSystem createFromWKT(final String wkt)
            throws FactoryException
    {
        /*
         * Note: while this factory is thread safe, the WKT parser is not.
         * Since we share a single instance of this parser, we must synchronize.
         */
        if (parser == null) {
            createParser(ReferencingFactoryFinder.getDatumFactory(null),
                    getMathTransformFactory());
        }
        try {
            return parser.parseCoordinateReferenceSystem(wkt);
        } catch (ParseException exception) {
            final Throwable cause = exception.getCause();
            if (cause instanceof FactoryException) {
                throw (FactoryException) cause;
            }
            throw new FactoryException(exception);
        }
    }

    /**
     * Creates inconditionnaly the WKT parser. This is factored out as a single private method
     * for making easier to spot the places in this code that need to create the parser and the
     * datum-alias patch.
     */
    private void createParser(final DatumFactory datumFactory, final MathTransformFactory mtFactory) {
        parser = new Parser(Symbols.DEFAULT, datumFactory, this, this, mtFactory);
    }
}
