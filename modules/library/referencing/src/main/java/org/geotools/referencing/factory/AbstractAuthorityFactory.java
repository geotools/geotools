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
 *
 *    This package contains documentation from OpenGIS specifications.
 *    OpenGIS consortium's work is fully acknowledged here.
 */
package org.geotools.referencing.factory;

import java.util.Set;
import java.util.Collections;
import javax.measure.unit.Unit;

import org.opengis.referencing.*;
import org.opengis.referencing.cs.*;
import org.opengis.referencing.crs.*;
import org.opengis.referencing.datum.*;
import org.opengis.referencing.operation.*;
import org.opengis.util.InternationalString;
import org.opengis.metadata.extent.Extent;
import org.opengis.metadata.citation.Citation;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.util.GenericName;

import org.geotools.metadata.iso.citation.Citations;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Errors;
import org.geotools.util.NameFactory;


/**
 * Base class for authority factories. An <cite>authority</cite> is an organization that maintains
 * definitions of authority codes. An <cite>authority code</cite> is a compact string defined by
 * an authority to reference a particular spatial reference object. For example the
 * <A HREF="http://www.epsg.org">European Petroleum Survey Group (EPSG)</A> maintains
 * a database of coordinate systems, and other spatial referencing objects, where each
 * object has a code number ID. For example, the EPSG code for a WGS84 Lat/Lon coordinate
 * system is {@code "4326"}.
 * <p>
 * This class defines a default implementation for most methods defined in the
 * {@link DatumAuthorityFactory}, {@link CSAuthorityFactory} and {@link CRSAuthorityFactory}
 * interfaces. However, those interfaces do not appear in the {@code implements} clause of
 * this class declaration. This is up to subclasses to decide which interfaces they declare
 * to implement.
 * <p>
 * The default implementation for all {@code createFoo} methods ultimately invokes
 * {@link #createObject}, which may be the only method that a subclass need to override.
 * However, other methods may be overridden as well for better performances.
 *
 * @since 2.1
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public abstract class AbstractAuthorityFactory extends ReferencingFactory
        implements AuthorityFactory
{
    /**
     * Constructs an instance using the specified priority level.
     *
     * @param priority The priority for this factory, as a number between
     *        {@link #MINIMUM_PRIORITY MINIMUM_PRIORITY} and
     *        {@link #MAXIMUM_PRIORITY MAXIMUM_PRIORITY} inclusive.
     */
    protected AbstractAuthorityFactory(final int priority) {
        super(priority);
    }

    /**
     * Returns {@code true} if this factory is available. This method may returns {@code false}
     * for example if a connection to the EPSG database failed. This method is defined here for
     * implementation convenience, but not yet public because not yet applicable. It will be made
     * public in {@link DeferredAuthorityFactory} and {@link AuthorityFactoryAdapter} subclasses,
     * which implement the {@link org.geotools.factory.OptionalFactory} interface.
     */
    boolean isAvailable() {
        return true;
    }

    /**
     * If this factory is a wrapper for the specified factory that do not add any additional
     * {@linkplain #getAuthorityCodes authority codes}, returns {@code true}. Example of such
     * wrappers are {@link BufferedAuthorityFactory} and {@link TransformedAuthorityFactory}.
     * <p>
     * This method should perform a cheap test. It is for {@link FallbackAuthorityFactory}
     * internal use only and should not be public.
     */
    boolean sameAuthorityCodes(final AuthorityFactory factory) {
        return factory == this;
    }

    /**
     * Returns the organization or party responsible for definition and maintenance of the
     * database.
     */
    public abstract Citation getAuthority();

    /**
     * Returns a description of the underlying backing store, or {@code null} if unknown.
     * This is for example the database software used for storing the data.
     * The default implementation returns always {@code null}.
     *
     * @return The description of the underlying backing store, or {@code null}.
     * @throws FactoryException if a failure occurs while fetching the engine description.
     */
    public String getBackingStoreDescription() throws FactoryException {
        return null;
    }

    /**
     * Returns an arbitrary object from a code. The returned object will typically be an instance
     * of {@link Datum}, {@link CoordinateSystem}, {@link CoordinateReferenceSystem} or
     * {@link CoordinateOperation}. The default implementation always throw an exception.
     * Subclasses should override this method if they are capable to automatically detect
     * the object type from its code.
     *
     * @param  code Value allocated by authority.
     * @throws NoSuchAuthorityCodeException if the specified {@code code} was not found.
     * @throws FactoryException if the object creation failed for some other reason.
     *
     * @see #createCoordinateReferenceSystem
     * @see #createDatum
     * @see #createEllipsoid
     * @see #createUnit
     */
    public IdentifiedObject createObject(final String code)
            throws NoSuchAuthorityCodeException, FactoryException
    {
        ensureNonNull("code", code);
        throw noSuchAuthorityCode(IdentifiedObject.class, code);
    }

    /**
     * Returns an arbitrary {@linkplain Datum datum} from a code.
     * The default implementation invokes <code>{@linkplain #createObject createObject}(code)</code>.
     *
     * @param  code Value allocated by authority.
     * @return The datum for the given code.
     * @throws NoSuchAuthorityCodeException if the specified {@code code} was not found.
     * @throws FactoryException if the object creation failed for some other reason.
     *
     * @see #createGeodeticDatum
     * @see #createVerticalDatum
     * @see #createTemporalDatum
     */
    public Datum createDatum(final String code)
            throws NoSuchAuthorityCodeException, FactoryException
    {
        final IdentifiedObject object = createObject(code);
        try {
            return (Datum) object;
        } catch (ClassCastException exception) {
            throw noSuchAuthorityCode(Datum.class, code, exception);
        }
    }

    /**
     * Creates a {@linkplain EngineeringDatum engineering datum} from a code.
     * The default implementation invokes <code>{@linkplain #createDatum createDatum}(code)</code>.
     *
     * @param  code Value allocated by authority.
     * @return The datum for the given code.
     * @throws NoSuchAuthorityCodeException if the specified {@code code} was not found.
     * @throws FactoryException if the object creation failed for some other reason.
     *
     * @see #createEngineeringCRS
     */
    public EngineeringDatum createEngineeringDatum(final String code)
            throws NoSuchAuthorityCodeException, FactoryException
    {
        final Datum datum = createDatum(code);
        try {
            return (EngineeringDatum) datum;
        } catch (ClassCastException exception) {
            throw noSuchAuthorityCode(EngineeringDatum.class, code, exception);
        }
    }

    /**
     * Creates a {@linkplain ImageDatum image datum} from a code.
     * The default implementation invokes <code>{@linkplain #createDatum createDatum}(code)</code>.
     *
     * @param  code Value allocated by authority.
     * @return The datum for the given code.
     * @throws NoSuchAuthorityCodeException if the specified {@code code} was not found.
     * @throws FactoryException if the object creation failed for some other reason.
     *
     * @see #createImageCRS
     */
    public ImageDatum createImageDatum(final String code)
            throws NoSuchAuthorityCodeException, FactoryException
    {
        final Datum datum = createDatum(code);
        try {
            return (ImageDatum) datum;
        } catch (ClassCastException exception) {
            throw noSuchAuthorityCode(ImageDatum.class, code, exception);
        }
    }

    /**
     * Creates a {@linkplain VerticalDatum vertical datum} from a code.
     * The default implementation invokes <code>{@linkplain #createDatum createDatum}(code)</code>.
     *
     * @param  code Value allocated by authority.
     * @return The datum for the given code.
     * @throws NoSuchAuthorityCodeException if the specified {@code code} was not found.
     * @throws FactoryException if the object creation failed for some other reason.
     *
     * @see #createVerticalCRS
     */
    public VerticalDatum createVerticalDatum(final String code)
            throws NoSuchAuthorityCodeException, FactoryException
    {
        final Datum datum = createDatum(code);
        try {
            return (VerticalDatum) datum;
        } catch (ClassCastException exception) {
            throw noSuchAuthorityCode(VerticalDatum.class, code, exception);
        }
    }

    /**
     * Creates a {@linkplain TemporalDatum temporal datum} from a code.
     * The default implementation invokes <code>{@linkplain #createDatum createDatum}(code)</code>.
     *
     * @param  code Value allocated by authority.
     * @return The datum for the given code.
     * @throws NoSuchAuthorityCodeException if the specified {@code code} was not found.
     * @throws FactoryException if the object creation failed for some other reason.
     *
     * @see #createTemporalCRS
     */
    public TemporalDatum createTemporalDatum(final String code)
            throws NoSuchAuthorityCodeException, FactoryException
    {
        final Datum datum = createDatum(code);
        try {
            return (TemporalDatum) datum;
        } catch (ClassCastException exception) {
            throw noSuchAuthorityCode(TemporalDatum.class, code, exception);
        }
    }

    /**
     * Returns a {@linkplain GeodeticDatum geodetic datum} from a code.
     * The default implementation invokes <code>{@linkplain #createDatum createDatum}(code)</code>.
     *
     * @param  code Value allocated by authority.
     * @return The datum for the given code.
     * @throws NoSuchAuthorityCodeException if the specified {@code code} was not found.
     * @throws FactoryException if the object creation failed for some other reason.
     *
     * @see #createEllipsoid
     * @see #createPrimeMeridian
     * @see #createGeographicCRS
     * @see #createProjectedCRS
     */
    public GeodeticDatum createGeodeticDatum(final String code)
            throws NoSuchAuthorityCodeException, FactoryException
    {
        final Datum datum = createDatum(code);
        try {
            return (GeodeticDatum) datum;
        } catch (ClassCastException exception) {
            throw noSuchAuthorityCode(GeodeticDatum.class, code, exception);
        }
    }

    /**
     * Returns an {@linkplain Ellipsoid ellipsoid} from a code.
     * The default implementation invokes <code>{@linkplain #createObject createObject}(code)</code>.
     *
     * @param  code Value allocated by authority.
     * @return The ellipsoid for the given code.
     * @throws NoSuchAuthorityCodeException if the specified {@code code} was not found.
     * @throws FactoryException if the object creation failed for some other reason.
     *
     * @see #createGeodeticDatum
     */
    public Ellipsoid createEllipsoid(final String code)
            throws NoSuchAuthorityCodeException, FactoryException
    {
        final IdentifiedObject object = createObject(code);
        try {
            return (Ellipsoid) object;
        } catch (ClassCastException exception) {
            throw noSuchAuthorityCode(Ellipsoid.class, code, exception);
        }
    }

    /**
     * Returns a {@linkplain PrimeMeridian prime meridian} from a code.
     * The default implementation invokes <code>{@linkplain #createObject createObject}(code)</code>.
     *
     * @param  code Value allocated by authority.
     * @return The prime meridian for the given code.
     * @throws NoSuchAuthorityCodeException if the specified {@code code} was not found.
     * @throws FactoryException if the object creation failed for some other reason.
     *
     * @see #createGeodeticDatum
     */
    public PrimeMeridian createPrimeMeridian(final String code)
            throws NoSuchAuthorityCodeException, FactoryException
    {
        final IdentifiedObject object = createObject(code);
        try {
            return (PrimeMeridian) object;
        } catch (ClassCastException exception) {
            throw noSuchAuthorityCode(PrimeMeridian.class, code, exception);
        }
    }

    /**
     * Returns a {@linkplain Extent extent} (usually an area of validity) from a code.
     * The default implementation invokes <code>{@linkplain #createObject createObject}(code)</code>.
     *
     * @param  code Value allocated by authority.
     * @return The extent for the given code.
     * @throws NoSuchAuthorityCodeException if the specified {@code code} was not found.
     * @throws FactoryException if the object creation failed for some other reason.
     */
    public Extent createExtent(final String code)
            throws NoSuchAuthorityCodeException, FactoryException
    {
        final IdentifiedObject object = createObject(code);
        try {
            return (Extent) object;
        } catch (ClassCastException exception) {
            throw noSuchAuthorityCode(Extent.class, code, exception);
        }
    }

    /**
     * Returns an arbitrary {@linkplain CoordinateSystem coordinate system} from a code.
     * The default implementation invokes <code>{@linkplain #createObject createObject}(code)</code>.
     *
     * @param  code Value allocated by authority.
     * @return The coordinate system for the given code.
     * @throws NoSuchAuthorityCodeException if the specified {@code code} was not found.
     * @throws FactoryException if the object creation failed for some other reason.
     */
    public CoordinateSystem createCoordinateSystem(final String code)
            throws NoSuchAuthorityCodeException, FactoryException
    {
        final IdentifiedObject object = createObject(code);
        try {
            return (CoordinateSystem) object;
        } catch (ClassCastException exception) {
            throw noSuchAuthorityCode(CoordinateSystem.class, code, exception);
        }
    }

    /**
     * Creates a cartesian coordinate system from a code.
     * The default implementation invokes
     * <code>{@linkplain #createCoordinateSystem createCoordinateSystem}(code)</code>.
     *
     * @param  code Value allocated by authority.
     * @return The coordinate system for the given code.
     * @throws NoSuchAuthorityCodeException if the specified {@code code} was not found.
     * @throws FactoryException if the object creation failed for some other reason.
     */
    public CartesianCS createCartesianCS(final String code)
            throws NoSuchAuthorityCodeException, FactoryException
    {
        final CoordinateSystem cs = createCoordinateSystem(code);
        try {
            return (CartesianCS) cs;
        } catch (ClassCastException exception) {
            throw noSuchAuthorityCode(CartesianCS.class, code, exception);
        }
    }

    /**
     * Creates a polar coordinate system from a code.
     * The default implementation invokes
     * <code>{@linkplain #createCoordinateSystem createCoordinateSystem}(code)</code>.
     *
     * @param  code Value allocated by authority.
     * @return The coordinate system for the given code.
     * @throws NoSuchAuthorityCodeException if the specified {@code code} was not found.
     * @throws FactoryException if the object creation failed for some other reason.
     */
    public PolarCS createPolarCS(final String code)
            throws NoSuchAuthorityCodeException, FactoryException
    {
        final CoordinateSystem cs = createCoordinateSystem(code);
        try {
            return (PolarCS) cs;
        } catch (ClassCastException exception) {
            throw noSuchAuthorityCode(PolarCS.class, code, exception);
        }
    }

    /**
     * Creates a cylindrical coordinate system from a code.
     * The default implementation invokes
     * <code>{@linkplain #createCoordinateSystem createCoordinateSystem}(code)</code>.
     *
     * @param  code Value allocated by authority.
     * @return The coordinate system for the given code.
     * @throws NoSuchAuthorityCodeException if the specified {@code code} was not found.
     * @throws FactoryException if the object creation failed for some other reason.
     */
    public CylindricalCS createCylindricalCS(final String code)
            throws NoSuchAuthorityCodeException, FactoryException
    {
        final CoordinateSystem cs = createCoordinateSystem(code);
        try {
            return (CylindricalCS) cs;
        } catch (ClassCastException exception) {
            throw noSuchAuthorityCode(CylindricalCS.class, code, exception);
        }
    }

    /**
     * Creates a spherical coordinate system from a code.
     * The default implementation invokes
     * <code>{@linkplain #createCoordinateSystem createCoordinateSystem}(code)</code>.
     *
     * @param  code Value allocated by authority.
     * @return The coordinate system for the given code.
     * @throws NoSuchAuthorityCodeException if the specified {@code code} was not found.
     * @throws FactoryException if the object creation failed for some other reason.
     */
    public SphericalCS createSphericalCS(final String code)
            throws NoSuchAuthorityCodeException, FactoryException
    {
        final CoordinateSystem cs = createCoordinateSystem(code);
        try {
            return (SphericalCS) cs;
        } catch (ClassCastException exception) {
            throw noSuchAuthorityCode(SphericalCS.class, code, exception);
        }
    }

    /**
     * Creates an ellipsoidal coordinate system from a code.
     * The default implementation invokes
     * <code>{@linkplain #createCoordinateSystem createCoordinateSystem}(code)</code>.
     *
     * @param  code Value allocated by authority.
     * @return The coordinate system for the given code.
     * @throws NoSuchAuthorityCodeException if the specified {@code code} was not found.
     * @throws FactoryException if the object creation failed for some other reason.
     */
    public EllipsoidalCS createEllipsoidalCS(final String code)
            throws NoSuchAuthorityCodeException, FactoryException
    {
        final CoordinateSystem cs = createCoordinateSystem(code);
        try {
            return (EllipsoidalCS) cs;
        } catch (ClassCastException exception) {
            throw noSuchAuthorityCode(EllipsoidalCS.class, code, exception);
        }
    }

    /**
     * Creates a vertical coordinate system from a code.
     * The default implementation invokes
     * <code>{@linkplain #createCoordinateSystem createCoordinateSystem}(code)</code>.
     *
     * @param  code Value allocated by authority.
     * @return The coordinate system for the given code.
     * @throws NoSuchAuthorityCodeException if the specified {@code code} was not found.
     * @throws FactoryException if the object creation failed for some other reason.
     */
    public VerticalCS createVerticalCS(final String code)
            throws NoSuchAuthorityCodeException, FactoryException
    {
        final CoordinateSystem cs = createCoordinateSystem(code);
        try {
            return (VerticalCS) cs;
        } catch (ClassCastException exception) {
            throw noSuchAuthorityCode(VerticalCS.class, code, exception);
        }
    }

    /**
     * Creates a temporal coordinate system from a code.
     * The default implementation invokes
     * <code>{@linkplain #createCoordinateSystem createCoordinateSystem}(code)</code>.
     *
     * @param  code Value allocated by authority.
     * @return The coordinate system for the given code.
     * @throws NoSuchAuthorityCodeException if the specified {@code code} was not found.
     * @throws FactoryException if the object creation failed for some other reason.
     */
    public TimeCS createTimeCS(final String code)
            throws NoSuchAuthorityCodeException, FactoryException
    {
        final CoordinateSystem cs = createCoordinateSystem(code);
        try {
            return (TimeCS) cs;
        } catch (ClassCastException exception) {
            throw noSuchAuthorityCode(TimeCS.class, code, exception);
        }
    }

    /**
     * Returns a {@linkplain CoordinateSystemAxis coordinate system axis} from a code.
     * The default implementation invokes <code>{@linkplain #createObject createObject}(code)</code>.
     *
     * @param  code Value allocated by authority.
     * @return The axis for the given code.
     * @throws NoSuchAuthorityCodeException if the specified {@code code} was not found.
     * @throws FactoryException if the object creation failed for some other reason.
     */
    public CoordinateSystemAxis createCoordinateSystemAxis(final String code)
            throws NoSuchAuthorityCodeException, FactoryException
    {
        final IdentifiedObject object = createObject(code);
        try {
            return (CoordinateSystemAxis) object;
        } catch (ClassCastException exception) {
            throw noSuchAuthorityCode(CoordinateSystemAxis.class, code, exception);
        }
    }

    /**
     * Returns an {@linkplain Unit unit} from a code.
     * The default implementation invokes <code>{@linkplain #createObject createObject}(code)</code>.
     *
     * @param  code Value allocated by authority.
     * @return The unit for the given code.
     * @throws NoSuchAuthorityCodeException if the specified {@code code} was not found.
     * @throws FactoryException if the object creation failed for some other reason.
     */
    public Unit<?> createUnit(final String code)
            throws NoSuchAuthorityCodeException, FactoryException
    {
        final IdentifiedObject object = createObject(code);
        try {
            return (Unit) object;
        } catch (ClassCastException exception) {
            throw noSuchAuthorityCode(Unit.class, code, exception);
        }
    }

    /**
     * Returns an arbitrary {@linkplain CoordinateReferenceSystem coordinate reference system}
     * from a code. If the coordinate reference system type is know at compile time, it is
     * recommended to invoke the most precise method instead of this one (for example
     * <code>&nbsp;{@linkplain #createGeographicCRS createGeographicCRS}(code)&nbsp;</code>
     * instead of <code>&nbsp;createCoordinateReferenceSystem(code)&nbsp;</code> if the caller
     * know he is asking for a {@linkplain GeographicCRS geographic coordinate reference system}).
     *
     * @param  code Value allocated by authority.
     * @return The coordinate reference system for the given code.
     * @throws NoSuchAuthorityCodeException if the specified {@code code} was not found.
     * @throws FactoryException if the object creation failed for some other reason.
     *
     * @see #createGeographicCRS
     * @see #createProjectedCRS
     * @see #createVerticalCRS
     * @see #createTemporalCRS
     * @see #createCompoundCRS
     */
    public CoordinateReferenceSystem createCoordinateReferenceSystem(final String code)
            throws NoSuchAuthorityCodeException, FactoryException
    {
        final IdentifiedObject object = createObject(code);
        try {
            return (CoordinateReferenceSystem) object;
        } catch (ClassCastException exception) {
            throw noSuchAuthorityCode(CoordinateReferenceSystem.class, code, exception);
        }
    }

    /**
     * Creates a 3D coordinate reference system from a code.
     *
     * @param  code Value allocated by authority.
     * @return The coordinate reference system for the given code.
     * @throws NoSuchAuthorityCodeException if the specified {@code code} was not found.
     * @throws FactoryException if the object creation failed for some other reason.
     */
    public CompoundCRS createCompoundCRS(final String code)
            throws NoSuchAuthorityCodeException, FactoryException
    {
        final CoordinateReferenceSystem crs = createCoordinateReferenceSystem(code);
        try {
            return (CompoundCRS) crs;
        } catch (ClassCastException exception) {
            throw noSuchAuthorityCode(CompoundCRS.class, code, exception);
        }
    }

    /**
     * Creates a derived coordinate reference system from a code.
     *
     * @param  code Value allocated by authority.
     * @return The coordinate reference system for the given code.
     * @throws NoSuchAuthorityCodeException if the specified {@code code} was not found.
     * @throws FactoryException if the object creation failed for some other reason.
     */
    public DerivedCRS createDerivedCRS(final String code)
            throws NoSuchAuthorityCodeException, FactoryException
    {
        final CoordinateReferenceSystem crs = createCoordinateReferenceSystem(code);
        try {
            return (DerivedCRS) crs;
        } catch (ClassCastException exception) {
            throw noSuchAuthorityCode(DerivedCRS.class, code, exception);
        }
    }

    /**
     * Creates a {@linkplain EngineeringCRS engineering coordinate reference system} from a code.
     *
     * @param  code Value allocated by authority.
     * @return The coordinate reference system for the given code.
     * @throws NoSuchAuthorityCodeException if the specified {@code code} was not found.
     * @throws FactoryException if the object creation failed for some other reason.
     */
    public EngineeringCRS createEngineeringCRS(final String code)
            throws NoSuchAuthorityCodeException, FactoryException
    {
        final CoordinateReferenceSystem crs = createCoordinateReferenceSystem(code);
        try {
            return (EngineeringCRS) crs;
        } catch (ClassCastException exception) {
            throw noSuchAuthorityCode(EngineeringCRS.class, code, exception);
        }
    }

    /**
     * Returns a {@linkplain GeographicCRS geographic coordinate reference system} from a code.
     *
     * @param  code Value allocated by authority.
     * @return The coordinate reference system for the given code.
     * @throws NoSuchAuthorityCodeException if the specified {@code code} was not found.
     * @throws FactoryException if the object creation failed for some other reason.
     *
     * @see #createGeodeticDatum
     */
    public GeographicCRS createGeographicCRS(final String code)
            throws NoSuchAuthorityCodeException, FactoryException
    {
        final CoordinateReferenceSystem crs = createCoordinateReferenceSystem(code);
        try {
            return (GeographicCRS) crs;
        } catch (ClassCastException exception) {
            throw noSuchAuthorityCode(GeographicCRS.class, code, exception);
        }
    }

    /**
     * Returns a {@linkplain GeocentricCRS geocentric coordinate reference system} from a code.
     *
     * @param  code Value allocated by authority.
     * @return The coordinate reference system for the given code.
     * @throws NoSuchAuthorityCodeException if the specified {@code code} was not found.
     * @throws FactoryException if the object creation failed.
     *
     * @see #createGeodeticDatum
     */
    public GeocentricCRS createGeocentricCRS(final String code)
            throws NoSuchAuthorityCodeException, FactoryException
    {
        final CoordinateReferenceSystem crs = createCoordinateReferenceSystem(code);
        try {
            return (GeocentricCRS) crs;
        } catch (ClassCastException exception) {
            throw noSuchAuthorityCode(GeocentricCRS.class, code, exception);
        }
    }

    /**
     * Creates a {@linkplain ImageCRS image coordinate reference system} from a code.
     *
     * @param  code Value allocated by authority.
     * @return The coordinate reference system for the given code.
     * @throws NoSuchAuthorityCodeException if the specified {@code code} was not found.
     * @throws FactoryException if the object creation failed for some other reason.
     */
    public ImageCRS createImageCRS(final String code)
            throws NoSuchAuthorityCodeException, FactoryException
    {
        final CoordinateReferenceSystem crs = createCoordinateReferenceSystem(code);
        try {
            return (ImageCRS) crs;
        } catch (ClassCastException exception) {
            throw noSuchAuthorityCode(ImageCRS.class, code, exception);
        }
    }

    /**
     * Returns a {@linkplain ProjectedCRS projected coordinate reference system} from a code.
     *
     * @param  code Value allocated by authority.
     * @return The coordinate reference system for the given code.
     * @throws NoSuchAuthorityCodeException if the specified {@code code} was not found.
     * @throws FactoryException if the object creation failed for some other reason.
     *
     * @see #createGeodeticDatum
     */
    public ProjectedCRS createProjectedCRS(final String code)
            throws NoSuchAuthorityCodeException, FactoryException
    {
        final CoordinateReferenceSystem crs = createCoordinateReferenceSystem(code);
        try {
            return (ProjectedCRS) crs;
        } catch (ClassCastException exception) {
            throw noSuchAuthorityCode(ProjectedCRS.class, code, exception);
        }
    }

    /**
     * Creates a {@linkplain TemporalCRS temporal coordinate reference system} from a code.
     *
     * @param  code Value allocated by authority.
     * @return The coordinate reference system for the given code.
     * @throws NoSuchAuthorityCodeException if the specified {@code code} was not found.
     * @throws FactoryException if the object creation failed for some other reason.
     *
     * @see #createTemporalDatum
     */
    public TemporalCRS createTemporalCRS(final String code)
            throws NoSuchAuthorityCodeException, FactoryException
    {
        final CoordinateReferenceSystem crs = createCoordinateReferenceSystem(code);
        try {
            return (TemporalCRS) crs;
        } catch (ClassCastException exception) {
            throw noSuchAuthorityCode(TemporalCRS.class, code, exception);
        }
    }

    /**
     * Creates a {@linkplain VerticalCRS vertical coordinate reference system} from a code.
     *
     * @param  code Value allocated by authority.
     * @return The coordinate reference system for the given code.
     * @throws NoSuchAuthorityCodeException if the specified {@code code} was not found.
     * @throws FactoryException if the object creation failed for some other reason.
     *
     * @see #createVerticalDatum
     */
    public VerticalCRS createVerticalCRS(final String code)
            throws NoSuchAuthorityCodeException, FactoryException
    {
        final CoordinateReferenceSystem crs = createCoordinateReferenceSystem(code);
        try {
            return (VerticalCRS) crs;
        } catch (ClassCastException exception) {
            throw noSuchAuthorityCode(VerticalCRS.class, code, exception);
        }
    }

    /**
     * Creates a parameter descriptor from a code.
     *
     * @param  code Value allocated by authority.
     * @return The coordinate reference system for the given code.
     * @throws NoSuchAuthorityCodeException if the specified {@code code} was not found.
     * @throws FactoryException if the object creation failed for some other reason.
     *
     * @since 2.2
     */
    public ParameterDescriptor createParameterDescriptor(final String code)
            throws NoSuchAuthorityCodeException, FactoryException
    {
        final IdentifiedObject operation = createObject(code);
        try {
            return (ParameterDescriptor) operation;
        } catch (ClassCastException exception) {
            throw noSuchAuthorityCode(ParameterDescriptor.class, code, exception);
        }
    }

    /**
     * Creates an operation method from a code.
     *
     * @param  code Value allocated by authority.
     * @return The operation method for the given code.
     * @throws NoSuchAuthorityCodeException if the specified {@code code} was not found.
     * @throws FactoryException if the object creation failed for some other reason.
     *
     * @since 2.2
     */
    public OperationMethod createOperationMethod(final String code)
            throws NoSuchAuthorityCodeException, FactoryException
    {
        final IdentifiedObject operation = createObject(code);
        try {
            return (OperationMethod) operation;
        } catch (ClassCastException exception) {
            throw noSuchAuthorityCode(OperationMethod.class, code, exception);
        }
    }

    /**
     * Creates an operation from a single operation code.
     *
     * @param  code Value allocated by authority.
     * @return The operation for the given code.
     * @throws NoSuchAuthorityCodeException if the specified {@code code} was not found.
     * @throws FactoryException if the object creation failed for some other reason.
     *
     * @since 2.2
     */
    public CoordinateOperation createCoordinateOperation(final String code)
            throws NoSuchAuthorityCodeException, FactoryException
    {
        final IdentifiedObject operation = createObject(code);
        try {
            return (CoordinateOperation) operation;
        } catch (ClassCastException exception) {
            throw noSuchAuthorityCode(CoordinateOperation.class, code, exception);
        }
    }

    /**
     * Creates an operation from coordinate reference system codes. The default implementation
     * returns an {@linkplain Collections#EMPTY_SET empty set}. We do not delegate to some kind
     * of {@linkplain CoordinateOperationFactory#createOperation(CoordinateReferenceSystem,
     * CoordinateReferenceSystem) coordinate operation factory method} because the usual contract
     * for this method is to extract the information from an authority database like
     * <A HREF="http://www.epsg.org">EPSG</A>, not to compute operations on-the-fly.
     * <p>
     * <strong>Rational:</strong> Coordinate operation factory
     * {@linkplain org.geotools.referencing.operation.AuthorityBackedFactory backed by an authority}
     * will invoke this method. If this method invoked the coordinate operation factory in turn, the
     * application could be trapped in infinite recursive calls.
     *
     * @param  sourceCRS   Coded value of source coordinate reference system.
     * @param  targetCRS   Coded value of target coordinate reference system.
     * @return The operations from {@code sourceCRS} to {@code targetCRS}.
     * @throws NoSuchAuthorityCodeException if a specified code was not found.
     * @throws FactoryException if the object creation failed for some other reason.
     *
     * @since 2.2
     */
    public Set<CoordinateOperation> createFromCoordinateReferenceSystemCodes(String sourceCRS, String targetCRS)
            throws NoSuchAuthorityCodeException, FactoryException
    {
        return Collections.emptySet();
    }

    /**
     * Returns a finder which can be used for looking up unidentified objects. The finder
     * fetchs a fully {@linkplain IdentifiedObject identified object} from an incomplete one,
     * for example from an object without identifier or "{@code AUTHORITY[...]}" element in
     * <cite>Well Known Text</cite> terminology.
     *
     * @param  type The type of objects to look for. Should be a GeoAPI interface like
     *         {@code GeographicCRS.class}, but this method accepts also implementation
     *         class. If the type is unknown, use {@code IdentifiedObject.class}. A more
     *         accurate type may help to speed up the search, since it reduces the amount
     *         of tables to scan in some implementations like the factories backed by
     *         EPSG database.
     * @return A finder to use for looking up unidentified objects.
     * @throws FactoryException if the finder can not be created.
     *
     * @since 2.4
     */
    public IdentifiedObjectFinder getIdentifiedObjectFinder(Class<? extends IdentifiedObject> type)
            throws FactoryException
    {
        return new IdentifiedObjectFinder(this, type);
    }

    /**
     * Releases resources immediately instead of waiting for the garbage collector.
     * Once a factory has been disposed, further {@code create(...)} invocations
     * may throw a {@link FactoryException}. Disposing a previously-disposed factory,
     * however, has no effect.
     *
     * @throws FactoryException if an error occured while disposing the factory.
     */
    public void dispose() throws FactoryException {
        // To be overridden by subclasses.
    }

    /**
     * Creates an exception for an unknow authority code. This convenience method is provided
     * for implementation of {@code createXXX} methods.
     *
     * @param  type  The GeoAPI interface that was to be created
     *               (e.g. {@code CoordinateReferenceSystem.class}).
     * @param  code  The unknow authority code.
     * @param  cause The cause of this error, or {@code null}.
     * @return An exception initialized with an error message built
     *         from the specified informations.
     */
    private NoSuchAuthorityCodeException noSuchAuthorityCode(final Class              type,
                                                             final String             code,
                                                             final ClassCastException cause)
    {
        final NoSuchAuthorityCodeException exception = noSuchAuthorityCode(type, code);
        exception.initCause(cause);
        return exception;
    }

    /**
     * Trims the authority scope, if present. For example if this factory is an EPSG authority
     * factory and the specified code start with the "EPSG:" prefix, then the prefix is removed.
     * Otherwise, the string is returned unchanged (except for leading and trailing spaces).
     *
     * @param  code The code to trim.
     * @return The code without the authority scope.
     */
    protected String trimAuthority(String code) {
        /*
         * IMPLEMENTATION NOTE: This method is overrided in PropertyAuthorityFactory. If
         * implementation below is modified, it is probably worth to revisit the overrided
         * method as well.
         */
        code = code.trim();
        final GenericName name  = NameFactory.create(code);
        final GenericName scope = name.scope().name();
        if (scope == null) {
            return code;
        }
        if (Citations.identifierMatches(getAuthority(), scope.toString())) {
            return name.tip().toString().trim();
        }
        return code;
    }

    /**
     * Creates an exception for an unknow authority code. This convenience method is provided
     * for implementation of {@code createXXX} methods.
     *
     * @param  type  The GeoAPI interface that was to be created
     *               (e.g. {@code CoordinateReferenceSystem.class}).
     * @param  code  The unknow authority code.
     * @return An exception initialized with an error message built
     *         from the specified informations.
     */
    protected final NoSuchAuthorityCodeException noSuchAuthorityCode(final Class  type,
                                                                     final String code)
    {
        final InternationalString authority = getAuthority().getTitle();
        return new NoSuchAuthorityCodeException(Errors.format(ErrorKeys.NO_SUCH_AUTHORITY_CODE_$3,
                   code, authority, type), authority.toString(), code);
    }
}
