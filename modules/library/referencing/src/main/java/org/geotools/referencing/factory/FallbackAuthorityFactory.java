/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.factory;

import java.util.Set;
import java.util.Iterator;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import javax.measure.unit.Unit;

import org.opengis.referencing.cs.*;
import org.opengis.referencing.crs.*;
import org.opengis.referencing.datum.*;
import org.opengis.referencing.operation.*;
import org.opengis.referencing.IdentifiedObject;
import org.opengis.referencing.AuthorityFactory;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.metadata.extent.Extent;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.util.InternationalString;

import org.geotools.resources.i18n.Errors;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Loggings;
import org.geotools.resources.i18n.LoggingKeys;
import org.geotools.factory.FactoryNotFoundException;


/**
 * A factory which delegates all object creation to a <cite>primary</cite> factory,
 * and fallback on an other one if the primary factory failed.
 *
 * @since 2.3
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 *
 * @todo Needs a mechanism for avoiding to query the same factory twice when the fallback is the
 *       same instance than the primary factory for some {@link AuthorityFactory} interfaces.
 */
public class FallbackAuthorityFactory extends AuthorityFactoryAdapter {
    /**
     * The factory to use as a fallback if the primary factory failed.
     */
    private final AbstractAuthorityFactory fallback;

    /**
     * The number of time the primary factory failed and the fallback factory was used
     * instead. This information is provided mostly for debugging and testing purpose.
     */
    private static int failureCount;

    /**
     * Returns {@code true} if the two specified factories can be used in a
     * {@code FallbackAuthorityFactory}. If this method returns {@code false},
     * then we should not create instance of this class since it would be useless.
     */
    static boolean chainable(final AuthorityFactory primary, final AuthorityFactory fallback) {
        return (interfaceMask(primary) & interfaceMask(fallback)) != 0;
    }

    /**
     * Wraps a primary and a fallback authority factories.
     * <p>
     * This constructor is protected because subclasses must declare which of the
     * {@link DatumAuthorityFactory}, {@link CSAuthorityFactory}, {@link CRSAuthorityFactory}
     * and {@link CoordinateOperationAuthorityFactory} interfaces they choose to implement.
     *
     * @param primary The primary factory.
     * @param fallback The factory to use as a fallback if the primary factory failed.
     *
     * @see #create
     */
    protected FallbackAuthorityFactory(final AuthorityFactory primary,
                                       final AuthorityFactory fallback)
    {
        super(primary, fallback);
        ensureNonNull("fallback", fallback);
        this.fallback = (fallback instanceof AbstractAuthorityFactory) ?
                (AbstractAuthorityFactory) fallback : new AuthorityFactoryAdapter(fallback);
    }

    /**
     * Wraps the specified authority factories. If the specified collection contains more than
     * one element, then a chain of {@code FallbackAuthorityFactory} instances is created.
     *
     * @param <T> The interface to implement.
     * @param  type The interface to implement. Should be one of {@link DatumAuthorityFactory},
     *         {@link CSAuthorityFactory}, {@link CRSAuthorityFactory} or
     *         {@link CoordinateOperationAuthorityFactory}.
     * @param  factories The factories to wrap, in iteration order.
     * @return The given factories as a chain of fallback factories.
     * @throws FactoryNotFoundException if the collection doesn't contains at least one element.
     * @throws ClassCastException if {@code type} is illegal.
     */
    public static <T extends AuthorityFactory> T create(final Class<T> type, final Collection<T> factories)
            throws FactoryNotFoundException, ClassCastException
    {
        ensureNonNull("type", type);
        ensureNonNull("factories", factories);
        if (factories.isEmpty()) {
            throw new FactoryNotFoundException(Errors.format(ErrorKeys.FACTORY_NOT_FOUND_$1, type));
        }
        return type.cast(create(false, interfaceMask(type), factories.iterator()));
    }

    /**
     * Wraps the specified authority factories. If the specified collection contains more than
     * one element, then a chain of {@code FallbackAuthorityFactory} instances is created. The
     * type is inferred from the factories found in the collection.
     * <p>
     * Consider using <code>{@linkplain #create(Class, Collection) create}(type, factories)</code>
     * instead when the type is known at compile time.
     *
     * @param  factories The factories to wrap, in iteration order.
     * @return The given factories as a chain of fallback factories.
     * @throws FactoryNotFoundException if the collection doesn't contains at least one element.
     *
     * @since 2.4
     */
    public static AuthorityFactory create(final Collection<? extends AuthorityFactory> factories)
            throws FactoryNotFoundException
    {
        ensureNonNull("factories", factories);
        if (factories.isEmpty()) {
            throw new FactoryNotFoundException(Errors.format(
                    ErrorKeys.FACTORY_NOT_FOUND_$1, AuthorityFactory.class));
        }
        return create(false, interfaceMask(factories), factories.iterator());
    }

    /**
     * Wraps the specified authority factories. If the specified collection contains more than
     * one element, then a chain of {@code FallbackAuthorityFactory} instances is created.
     *
     * @param automatic {@code true} if {@code interfaceMask} should automatically
     *        be restricted to the factory types detected in the collection.
     * @param interfaceMask The value computed by {@link #interfaceMask(Class)} that
     *        describe the set of interfaces to be implemented by the returned factory.
     * @param factories The factories to chain.
     */
    private static AuthorityFactory create(final boolean automatic, int interfaceMask,
                                           final Iterator<? extends AuthorityFactory> factories)
            throws FactoryNotFoundException
    {
        AuthorityFactory primary = factories.next();
        if (factories.hasNext()) {
            AuthorityFactory fallback = create(true, interfaceMask, factories);
            while (fallback != primary) { // Paranoiac check
                if (!sameAuthorityCodes(fallback, primary)) {
                    /*
                     * (Note: argument order is significant in the above method call)
                     * Creates a "primary - fallback" chain only if the fallback is not
                     * performing the same work than the primary factory, for example:
                     *
                     *   - A BufferedAuthorityFactory wrapping the primary factory. Since
                     *     the primary factory is tested first, the second one is useless.
                     *
                     *   - A OrderedAxisAuthorityFactory wrapping the primary factory. If
                     *     the primary factory failed to create a CRS, the second factory
                     *     should fail too since it relies on the first one.
                     */
                    if (automatic) {
                        // Restricts the interface to be implemented to the
                        // same set of interfaces than the backing factories.
                        interfaceMask &= (interfaceMask(primary) | interfaceMask(fallback));
                    }
                    primary = create(interfaceMask, primary, fallback);
                } else {
                    /*
                     * If the fallback is redundant, we should be done (just return the primary
                     * factory). A special case occurs if the fallback is an other instance of
                     * FallbackAuthorityFactory. We want to discard only the redundant primary
                     * factory (this is why we don't override sameAuthorityCodes(...) for testing
                     * the fallback). The fallback may have value, so we test it recursively.
                     */
                    if (fallback instanceof FallbackAuthorityFactory) {
                        fallback = ((FallbackAuthorityFactory) fallback).fallback;
                        continue;
                    }
                }
                break;
            }
        }
        return primary;
    }

    /**
     * Returns the direct dependencies. The returned list contains the backing store and the
     * fallback specified at construction time, or the exception if they can't be obtained.
     */
    @Override
    Collection<? super AuthorityFactory> dependencies() {
        final Collection<? super AuthorityFactory> dep = super.dependencies();
        dep.add(fallback);
        return dep;
    }

    /**
     * Returns the set of authority codes for the specified type. The default implementation
     * returns the union of the authority codes from the <cite>primary</cite> and the
     * <cite>fallback</cite> factories.
     */
    @Override
    public Set<String> getAuthorityCodes(final Class<? extends IdentifiedObject> type)
            throws FactoryException
    {
        final Set<String> codes = new LinkedHashSet<String>(super.getAuthorityCodes(type));
        codes.addAll(fallback.getAuthorityCodes(type));
        return codes;
    }

    /**
     * Returns a description for the object identified by the specified code.
     */
    @Override
    public InternationalString getDescriptionText(final String code) throws FactoryException {
        try {
            return super.getDescriptionText(code);
        } catch (FactoryException exception) {
            notifyFailure("getDescriptionText", exception);
            try {
                return fallback.getDescriptionText(code);
            } catch (NoSuchAuthorityCodeException ignore) {
                throw exception;
            }
        }
    }

    /**
     * Returns an arbitrary object from a code.
     */
    @Override
    public IdentifiedObject createObject(final String code) throws FactoryException {
        try {
            return super.createObject(code);
        } catch (FactoryException exception) {
            notifyFailure("createObject", exception);
            try {
                return fallback.createObject(code);
            } catch (NoSuchAuthorityCodeException ignore) {
                throw exception;
            }
        }
    }

    /**
     * Returns an arbitrary {@linkplain org.opengis.referencing.datum.Datum datum} from a code.
     *
     * @throws FactoryException if the object creation failed for all factories.
     */
    @Override
    public org.opengis.referencing.datum.Datum createDatum(final String code) throws FactoryException {
        try {
            return super.createDatum(code);
        } catch (FactoryException exception) {
            notifyFailure("createDatum", exception);
            try {
                return fallback.createDatum(code);
            } catch (NoSuchAuthorityCodeException ignore) {
                throw exception;
            }
        }
    }

    /**
     * Creates a {@linkplain EngineeringDatum engineering datum} from a code.
     *
     * @throws FactoryException if the object creation failed for all factories.
     */
    @Override
    public EngineeringDatum createEngineeringDatum(final String code) throws FactoryException {
        try {
            return super.createEngineeringDatum(code);
        } catch (FactoryException exception) {
            notifyFailure("createEngineeringDatum", exception);
            try {
                return fallback.createEngineeringDatum(code);
            } catch (NoSuchAuthorityCodeException ignore) {
                throw exception;
            }
        }
    }

    /**
     * Creates a {@linkplain ImageDatum image datum} from a code.
     *
     * @throws FactoryException if the object creation failed for all factories.
     */
    @Override
    public ImageDatum createImageDatum(final String code) throws FactoryException {
        try {
            return super.createImageDatum(code);
        } catch (FactoryException exception) {
            notifyFailure("createImageDatum", exception);
            try {
                return fallback.createImageDatum(code);
            } catch (NoSuchAuthorityCodeException ignore) {
                throw exception;
            }
        }
    }

    /**
     * Creates a {@linkplain VerticalDatum vertical datum} from a code.
     *
     * @throws FactoryException if the object creation failed for all factories.
     */
    @Override
    public VerticalDatum createVerticalDatum(final String code) throws FactoryException {
        try {
            return super.createVerticalDatum(code);
        } catch (FactoryException exception) {
            notifyFailure("createVerticalDatum", exception);
            try {
                return fallback.createVerticalDatum(code);
            } catch (NoSuchAuthorityCodeException ignore) {
                throw exception;
            }
        }
    }

    /**
     * Creates a {@linkplain TemporalDatum temporal datum} from a code.
     *
     * @throws FactoryException if the object creation failed for all factories.
     */
    @Override
    public TemporalDatum createTemporalDatum(final String code) throws FactoryException {
        try {
            return super.createTemporalDatum(code);
        } catch (FactoryException exception) {
            notifyFailure("createTemporalDatum", exception);
            try {
                return fallback.createTemporalDatum(code);
            } catch (NoSuchAuthorityCodeException ignore) {
                throw exception;
            }
        }
    }

    /**
     * Returns a {@linkplain GeodeticDatum geodetic datum} from a code.
     *
     * @throws FactoryException if the object creation failed for all factories.
     */
    @Override
    public GeodeticDatum createGeodeticDatum(final String code) throws FactoryException {
        try {
            return super.createGeodeticDatum(code);
        } catch (FactoryException exception) {
            notifyFailure("createGeodeticDatum", exception);
            try {
                return fallback.createGeodeticDatum(code);
            } catch (NoSuchAuthorityCodeException ignore) {
                throw exception;
            }
        }
    }

    /**
     * Returns an {@linkplain Ellipsoid ellipsoid} from a code.
     *
     * @throws FactoryException if the object creation failed for all factories.
     */
    @Override
    public Ellipsoid createEllipsoid(final String code) throws FactoryException {
        try {
            return super.createEllipsoid(code);
        } catch (FactoryException exception) {
            notifyFailure("createEllipsoid", exception);
            try {
                return fallback.createEllipsoid(code);
            } catch (NoSuchAuthorityCodeException ignore) {
                throw exception;
            }
        }
    }

    /**
     * Returns a {@linkplain PrimeMeridian prime meridian} from a code.
     *
     * @throws FactoryException if the object creation failed for all factories.
     */
    @Override
    public PrimeMeridian createPrimeMeridian(final String code) throws FactoryException {
        try {
            return super.createPrimeMeridian(code);
        } catch (FactoryException exception) {
            notifyFailure("createPrimeMeridian", exception);
            try {
                return fallback.createPrimeMeridian(code);
            } catch (NoSuchAuthorityCodeException ignore) {
                throw exception;
            }
        }
    }

    /**
     * Returns a {@linkplain Extent extent} (usually an area of validity) from a code.
     *
     * @throws FactoryException if the object creation failed for all factories.
     */
    @Override
    public Extent createExtent(final String code) throws FactoryException {
        try {
            return super.createExtent(code);
        } catch (FactoryException exception) {
            notifyFailure("createExtent", exception);
            try {
                return fallback.createExtent(code);
            } catch (NoSuchAuthorityCodeException ignore) {
                throw exception;
            }
        }
    }

    /**
     * Returns an arbitrary {@linkplain CoordinateSystem coordinate system} from a code.
     *
     * @throws FactoryException if the object creation failed for all factories.
     */
    @Override
    public CoordinateSystem createCoordinateSystem(final String code) throws FactoryException {
        try {
            return super.createCoordinateSystem(code);
        } catch (FactoryException exception) {
            notifyFailure("createCoordinateSystem", exception);
            try {
                return fallback.createCoordinateSystem(code);
            } catch (NoSuchAuthorityCodeException ignore) {
                throw exception;
            }
        }
    }

    /**
     * Creates a cartesian coordinate system from a code.
     *
     * @throws FactoryException if the object creation failed for all factories.
     */
    @Override
    public CartesianCS createCartesianCS(final String code) throws FactoryException {
        try {
            return super.createCartesianCS(code);
        } catch (FactoryException exception) {
            notifyFailure("createCartesianCS", exception);
            try {
                return fallback.createCartesianCS(code);
            } catch (NoSuchAuthorityCodeException ignore) {
                throw exception;
            }
        }
    }

    /**
     * Creates a polar coordinate system from a code.
     *
     * @throws FactoryException if the object creation failed for all factories.
     */
    @Override
    public PolarCS createPolarCS(final String code) throws FactoryException {
        try {
            return super.createPolarCS(code);
        } catch (FactoryException exception) {
            notifyFailure("createPolarCS", exception);
            try {
                return fallback.createPolarCS(code);
            } catch (NoSuchAuthorityCodeException ignore) {
                throw exception;
            }
        }
    }

    /**
     * Creates a cylindrical coordinate system from a code.
     *
     * @throws FactoryException if the object creation failed for all factories.
     */
    @Override
    public CylindricalCS createCylindricalCS(final String code) throws FactoryException {
        try {
            return super.createCylindricalCS(code);
        } catch (FactoryException exception) {
            notifyFailure("createCylindricalCS", exception);
            try {
                return fallback.createCylindricalCS(code);
            } catch (NoSuchAuthorityCodeException ignore) {
                throw exception;
            }
        }
    }

    /**
     * Creates a spherical coordinate system from a code.
     *
     * @throws FactoryException if the object creation failed for all factories.
     */
    @Override
    public SphericalCS createSphericalCS(final String code) throws FactoryException {
        try {
            return super.createSphericalCS(code);
        } catch (FactoryException exception) {
            notifyFailure("createSphericalCS", exception);
            try {
                return fallback.createSphericalCS(code);
            } catch (NoSuchAuthorityCodeException ignore) {
                throw exception;
            }
        }
    }

    /**
     * Creates an ellipsoidal coordinate system from a code.
     *
     * @throws FactoryException if the object creation failed for all factories.
     */
    @Override
    public EllipsoidalCS createEllipsoidalCS(final String code) throws FactoryException {
        try {
            return super.createEllipsoidalCS(code);
        } catch (FactoryException exception) {
            notifyFailure("createEllipsoidalCS", exception);
            try {
                return fallback.createEllipsoidalCS(code);
            } catch (NoSuchAuthorityCodeException ignore) {
                throw exception;
            }
        }
    }

    /**
     * Creates a vertical coordinate system from a code.
     *
     * @throws FactoryException if the object creation failed for all factories.
     */
    @Override
    public VerticalCS createVerticalCS(final String code) throws FactoryException {
        try {
            return super.createVerticalCS(code);
        } catch (FactoryException exception) {
            notifyFailure("createVerticalCS", exception);
            try {
                return fallback.createVerticalCS(code);
            } catch (NoSuchAuthorityCodeException ignore) {
                throw exception;
            }
        }
    }

    /**
     * Creates a temporal coordinate system from a code.
     *
     * @throws FactoryException if the object creation failed for all factories.
     */
    @Override
    public TimeCS createTimeCS(final String code) throws FactoryException {
        try {
            return super.createTimeCS(code);
        } catch (FactoryException exception) {
            notifyFailure("createTimeCS", exception);
            try {
                return fallback.createTimeCS(code);
            } catch (NoSuchAuthorityCodeException ignore) {
                throw exception;
            }
        }
    }

    /**
     * Returns a {@linkplain CoordinateSystemAxis coordinate system axis} from a code.
     *
     * @throws FactoryException if the object creation failed for all factories.
     */
    @Override
    public CoordinateSystemAxis createCoordinateSystemAxis(final String code)
            throws FactoryException
    {
        try {
            return super.createCoordinateSystemAxis(code);
        } catch (FactoryException exception) {
            notifyFailure("createCoordinateSystemAxis", exception);
            try {
                return fallback.createCoordinateSystemAxis(code);
            } catch (NoSuchAuthorityCodeException ignore) {
                throw exception;
            }
        }
    }

    /**
     * Returns an {@linkplain Unit unit} from a code.
     *
     * @throws FactoryException if the object creation failed for all factories.
     */
    @Override
    public Unit<?> createUnit(final String code) throws FactoryException {
        try {
            return super.createUnit(code);
        } catch (FactoryException exception) {
            notifyFailure("createUnit", exception);
            try {
                return fallback.createUnit(code);
            } catch (NoSuchAuthorityCodeException ignore) {
                throw exception;
            }
        }
    }

    /**
     * Returns an arbitrary {@linkplain CoordinateReferenceSystem coordinate reference system}
     * from a code.
     *
     * @throws FactoryException if the object creation failed for all factories.
     */
    @Override
    public CoordinateReferenceSystem createCoordinateReferenceSystem(final String code)
            throws FactoryException
    {
        try {
            return super.createCoordinateReferenceSystem(code);
        } catch (FactoryException exception) {
            notifyFailure("createCoordinateReferenceSystem", exception);
            try {
                return fallback.createCoordinateReferenceSystem(code);
            } catch (NoSuchAuthorityCodeException ignore) {
                throw exception;
            }
        }
    }

    /**
     * Creates a 3D coordinate reference system from a code.
     *
     * @throws FactoryException if the object creation failed for all factories.
     */
    @Override
    public CompoundCRS createCompoundCRS(final String code) throws FactoryException {
        try {
            return super.createCompoundCRS(code);
        } catch (FactoryException exception) {
            notifyFailure("createCompoundCRS", exception);
            try {
                return fallback.createCompoundCRS(code);
            } catch (NoSuchAuthorityCodeException ignore) {
                throw exception;
            }
        }
    }

    /**
     * Creates a derived coordinate reference system from a code.
     *
     * @throws FactoryException if the object creation failed for all factories.
     */
    @Override
    public DerivedCRS createDerivedCRS(final String code) throws FactoryException {
        try {
            return super.createDerivedCRS(code);
        } catch (FactoryException exception) {
            notifyFailure("createDerivedCRS", exception);
            try {
                return fallback.createDerivedCRS(code);
            } catch (NoSuchAuthorityCodeException ignore) {
                throw exception;
            }
        }
    }

    /**
     * Creates a {@linkplain EngineeringCRS engineering coordinate reference system} from a code.
     *
     * @throws FactoryException if the object creation failed for all factories.
     */
    @Override
    public EngineeringCRS createEngineeringCRS(final String code) throws FactoryException {
        try {
            return super.createEngineeringCRS(code);
        } catch (FactoryException exception) {
            notifyFailure("createEngineeringCRS", exception);
            try {
                return fallback.createEngineeringCRS(code);
            } catch (NoSuchAuthorityCodeException ignore) {
                throw exception;
            }
        }
    }

    /**
     * Returns a {@linkplain GeographicCRS geographic coordinate reference system} from a code.
     *
     * @throws FactoryException if the object creation failed for all factories.
     */
    @Override
    public GeographicCRS createGeographicCRS(final String code) throws FactoryException {
        try {
            return super.createGeographicCRS(code);
        } catch (FactoryException exception) {
            notifyFailure("createGeographicCRS", exception);
            try {
                return fallback.createGeographicCRS(code);
            } catch (NoSuchAuthorityCodeException ignore) {
                throw exception;
            }
        }
    }

    /**
     * Returns a {@linkplain GeocentricCRS geocentric coordinate reference system} from a code.
     *
     * @throws FactoryException if the object creation failed for all factories.
     */
    @Override
    public GeocentricCRS createGeocentricCRS(final String code) throws FactoryException {
        try {
            return super.createGeocentricCRS(code);
        } catch (FactoryException exception) {
            notifyFailure("createGeocentricCRS", exception);
            try {
                return fallback.createGeocentricCRS(code);
            } catch (NoSuchAuthorityCodeException ignore) {
                throw exception;
            }
        }
    }

    /**
     * Creates a {@linkplain ImageCRS image coordinate reference system} from a code.
     *
     * @throws FactoryException if the object creation failed for all factories.
     */
    @Override
    public ImageCRS createImageCRS(final String code) throws FactoryException {
        try {
            return super.createImageCRS(code);
        } catch (FactoryException exception) {
            notifyFailure("createImageCRS", exception);
            try {
                return fallback.createImageCRS(code);
            } catch (NoSuchAuthorityCodeException ignore) {
                throw exception;
            }
        }
    }

    /**
     * Returns a {@linkplain ProjectedCRS projected coordinate reference system} from a code.
     *
     * @throws FactoryException if the object creation failed for all factories.
     */
    @Override
    public ProjectedCRS createProjectedCRS(final String code) throws FactoryException {
        try {
            return super.createProjectedCRS(code);
        } catch (FactoryException exception) {
            notifyFailure("createProjectedCRS", exception);
            try {
                return fallback.createProjectedCRS(code);
            } catch (NoSuchAuthorityCodeException ignore) {
                throw exception;
            }
        }
    }

    /**
     * Creates a {@linkplain TemporalCRS temporal coordinate reference system} from a code.
     *
     * @throws FactoryException if the object creation failed for all factories.
     */
    @Override
    public TemporalCRS createTemporalCRS(final String code) throws FactoryException {
        try {
            return super.createTemporalCRS(code);
        } catch (FactoryException exception) {
            notifyFailure("createTemporalCRS", exception);
            try {
                return fallback.createTemporalCRS(code);
            } catch (NoSuchAuthorityCodeException ignore) {
                throw exception;
            }
        }
    }

    /**
     * Creates a {@linkplain VerticalCRS vertical coordinate reference system} from a code.
     *
     * @throws FactoryException if the object creation failed for all factories.
     */
    @Override
    public VerticalCRS createVerticalCRS(final String code) throws FactoryException {
        try {
            return super.createVerticalCRS(code);
        } catch (FactoryException exception) {
            notifyFailure("createVerticalCRS", exception);
            try {
                return fallback.createVerticalCRS(code);
            } catch (NoSuchAuthorityCodeException ignore) {
                throw exception;
            }
        }
    }

    /**
     * Creates a parameter descriptor from a code.
     *
     * @throws FactoryException if the object creation failed for all factories.
     */
    @Override
    public ParameterDescriptor createParameterDescriptor(final String code) throws FactoryException {
        try {
            return super.createParameterDescriptor(code);
        } catch (FactoryException exception) {
            notifyFailure("createParameterDescriptor", exception);
            try {
                return fallback.createParameterDescriptor(code);
            } catch (NoSuchAuthorityCodeException ignore) {
                throw exception;
            }
        }
    }

    /**
     * Creates an operation method from a code.
     *
     * @throws FactoryException if the object creation failed for all factories.
     */
    @Override
    public OperationMethod createOperationMethod(final String code) throws FactoryException {
        try {
            return super.createOperationMethod(code);
        } catch (FactoryException exception) {
            notifyFailure("createOperationMethod", exception);
            try {
                return fallback.createOperationMethod(code);
            } catch (NoSuchAuthorityCodeException ignore) {
                throw exception;
            }
        }
    }

    /**
     * Creates an operation from a single operation code.
     *
     * @throws FactoryException if the object creation failed for all factories.
     */
    @Override
    public CoordinateOperation createCoordinateOperation(final String code) throws FactoryException {
        try {
            return super.createCoordinateOperation(code);
        } catch (FactoryException exception) {
            notifyFailure("createCoordinateOperation", exception);
            try {
                return fallback.createCoordinateOperation(code);
            } catch (NoSuchAuthorityCodeException ignore) {
                throw exception;
            }
        }
    }

    /**
     * Creates an operation from coordinate reference system codes.
     *
     * @throws FactoryException if the object creation failed for all factories.
     */
    @Override
    public Set<CoordinateOperation> createFromCoordinateReferenceSystemCodes(
            final String sourceCRS, final String targetCRS)
            throws FactoryException
    {
        try {
            return super.createFromCoordinateReferenceSystemCodes(sourceCRS, targetCRS);
        } catch (FactoryException exception) {
            notifyFailure("createFromCoordinateReferenceSystemCodes", exception);
            try {
                return fallback.createFromCoordinateReferenceSystemCodes(sourceCRS, targetCRS);
            } catch (NoSuchAuthorityCodeException ignore) {
                throw exception;
            }
        }
    }

    /**
     * Returns a finder which can be used for looking up unidentified objects.
     * The default implementation delegates the lookups to the primary factory,
     * and fallback on the second one if the primary factory can't find a match.
     *
     * @since 2.4
     */
    @Override
    public IdentifiedObjectFinder getIdentifiedObjectFinder(Class<? extends IdentifiedObject> type)
            throws FactoryException
    {
        return new Finder(type);
    }

    /**
     * A {@link IdentifiedObjectFinder} which fallback to the second factory
     * if the primary one can't find a match.
     */
    private final class Finder extends AuthorityFactoryAdapter.Finder {
        /**
         * The fallback. Will be created only when first needed.
         */
        private transient IdentifiedObjectFinder fallback;

        /**
         * Creates a finder for the underlying backing store.
         */
        Finder(final Class<? extends IdentifiedObject> type) throws FactoryException {
            super(type);
        }

        /**
         * Makes sure that {@link #fallback} is initialized.
         */
        private void ensureFallback() throws FactoryException {
            if (fallback == null) {
                fallback = FallbackAuthorityFactory.this.fallback.getIdentifiedObjectFinder(getProxy().getType());
            }
            fallback.setFullScanAllowed(isFullScanAllowed());
        }

        /**
         * Lookups for the specified object.
         */
        @Override
        public IdentifiedObject find(final IdentifiedObject object) throws FactoryException {
            IdentifiedObject candidate = finder.find(object);
            if (candidate != null) {
                return candidate;
            }
            ensureFallback();
            candidate = fallback.find(object);
            return candidate;
        }

        /**
         * Returns the identifier of the specified object, or {@code null} if none.
         */
        @Override
        public String findIdentifier(final IdentifiedObject object) throws FactoryException {
            String candidate = finder.findIdentifier(object);
            if (candidate != null) {
                return candidate;
            }
            ensureFallback();
            candidate = fallback.findIdentifier(object);
            return candidate;
        }
    }

    /**
     * Invoked by <code>create</code><var>Foo</var><code>(String)</code> methods when the
     * <cite>primary</cite> factory failed to create an object. Note that it doesn't imply
     * anything about the success of <cite>fallback</cite> factory. The default implementation
     * log a message to the {@link Level#FINE FINE} level.
     *
     * @param method The name of the invoked method.
     * @param exception The exception that occured. It is often possible to
     *        get the authority code from some subclasses of this exception.
     */
    private static void notifyFailure(final String method, final FactoryException exception) {
        failureCount++;
        if (LOGGER.isLoggable(Level.FINE)) {
            final LogRecord record = Loggings.format(Level.FINE,
                    LoggingKeys.FALLBACK_FACTORY_$1, exception);
            record.setSourceClassName(FallbackAuthorityFactory.class.getName());
            record.setSourceMethodName(method);
            record.setLoggerName(LOGGER.getName());
            LOGGER.log(record);
        }
    }

    /**
     * Returns the number of time the primary factory failed and the fallback factory was
     * used instead. This information is provided mostly for debugging and testing purpose.
     * It is approximative because incrementation is not sychronized.
     */
    static int getFailureCount() {
        return failureCount;
    }

    /**
     * Returns a mask that represent the implemented interfaces.
     */
    private static int interfaceMask(final Collection<? extends AuthorityFactory> factories) {
        int mask = 0;
        for (final AuthorityFactory factory : factories) {
            mask |= interfaceMask(factory);
        }
        return mask;
    }

    /**
     * Returns a mask that represent the implemented interface.
     */
    private static int interfaceMask(final AuthorityFactory factory) {
        return interfaceMask(factory.getClass());
    }

    /**
     * Returns a mask that represent the implemented interface.
     */
    private static int interfaceMask(final Class<? extends AuthorityFactory> type) {
        int mask = 0; // Will be a set of bit flags, as set below.
        if (CoordinateOperationAuthorityFactory.class.isAssignableFrom(type)) mask |= 1;
        if (                 CSAuthorityFactory.class.isAssignableFrom(type)) mask |= 2;
        if (              DatumAuthorityFactory.class.isAssignableFrom(type)) mask |= 4;
        if (                CRSAuthorityFactory.class.isAssignableFrom(type)) mask |= 8;
        return mask;
    }

    /**
     * Creates a factory from a mask computed by {@link #interfaceMask}.
     */
    private static AuthorityFactory create(final int mask,
            final AuthorityFactory primary, final AuthorityFactory fallback)
    {
        /*
         * The following assertion fails if we try to implements some
         * interfaces not supported by the primary or fallback factory.
         */
        assert (mask & ~(interfaceMask(primary) | interfaceMask(fallback))) == 0 : mask;
        final AuthorityFactory factory;
        /*
         * In the 'switch' statement below, we do not implement all possible combinaisons
         * of authority factories. Only a few common combinaisons are listed. Other
         * combinaisons will fallback on some reasonable default. We may complete the
         * list later if there is a need for that.
         */
        switch (mask) {
            case 15: factory = new All                     (primary, fallback); break;
            case 14: factory = new CRS_Datum_CS            (primary, fallback); break;
            case 13: //      = new CRS_Datum_Operation     (primary, fallback); break;
            case 12: //      = new CRS_Datum               (primary, fallback); break;
            case 11: //      = new CRS_CS_Operation        (primary, fallback); break;
            case 10: //      = new CRS_CS                  (primary, fallback); break;
            case  9: //      = new CRS_Operation           (primary, fallback); break;
            case  8: factory = new CRS                     (primary, fallback); break;
            case  7: //      = new Datum_CS_Operation      (primary, fallback); break;
            case  6: //      = new Datum_CS                (primary, fallback); break;
            case  5: //      = new Datum_Operation         (primary, fallback); break;
            case  4: factory = new Datum                   (primary, fallback); break;
            case  3: //      = new CS_Operation            (primary, fallback); break;
            case  2: factory = new CS                      (primary, fallback); break;
            case  1: factory = new Operation               (primary, fallback); break;
            case  0: factory = new FallbackAuthorityFactory(primary, fallback); break;
            default: throw new AssertionError(mask); // Should never happen.
        }
        /*
         * The following assertion fails if 'factory' implements some interfaces
         * that wasn't requested. The opposite is allowed however: 'factory' may
         * not implement every requested interfaces.
         */
        assert (interfaceMask(factory) & ~mask) == 0 : mask;
        return factory;
    }

    /** For internal use by {@link FallbackAuthorityFactory#create} only. */
    private static final class CRS extends FallbackAuthorityFactory
            implements CRSAuthorityFactory
    {
        CRS(final AuthorityFactory primary, final AuthorityFactory fallback) {
            super(primary, fallback);
        }
    }

    /** For internal use by {@link FallbackAuthorityFactory#create} only. */
    private static final class CS extends FallbackAuthorityFactory
            implements CSAuthorityFactory
    {
        CS(final AuthorityFactory primary, final AuthorityFactory fallback) {
            super(primary, fallback);
        }
    }

    /** For internal use by {@link FallbackAuthorityFactory#create} only. */
    private static final class Datum extends FallbackAuthorityFactory
            implements DatumAuthorityFactory
    {
        Datum(final AuthorityFactory primary, final AuthorityFactory fallback) {
            super(primary, fallback);
        }
    }

    /** For internal use by {@link FallbackAuthorityFactory#create} only. */
    private static final class Operation extends FallbackAuthorityFactory
            implements CoordinateOperationAuthorityFactory
    {
        Operation(final AuthorityFactory primary, final AuthorityFactory fallback) {
            super(primary, fallback);
        }
    }

    /** For internal use by {@link FallbackAuthorityFactory#create} only. */
    private static final class CRS_Datum_CS extends FallbackAuthorityFactory
            implements CRSAuthorityFactory, CSAuthorityFactory, DatumAuthorityFactory
    {
        CRS_Datum_CS(final AuthorityFactory primary, final AuthorityFactory fallback) {
            super(primary, fallback);
        }
    }

    /** For internal use by {@link FallbackAuthorityFactory#create} only. */
    private static final class All extends FallbackAuthorityFactory implements CRSAuthorityFactory,
            CSAuthorityFactory, DatumAuthorityFactory, CoordinateOperationAuthorityFactory
    {
        All(final AuthorityFactory primary, final AuthorityFactory fallback) {
            super(primary, fallback);
        }
    }
}
