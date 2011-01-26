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
 *
 *    This package contains documentation from OpenGIS specifications.
 *    OpenGIS consortium's work is fully acknowledged here.
 */
package org.geotools.referencing.factory;

// J2SE dependencies
import java.util.Set;
import java.util.Arrays;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

// OpenGIS dependencies
import org.opengis.referencing.*;
import org.opengis.referencing.cs.*;
import org.opengis.referencing.crs.*;
import org.opengis.referencing.datum.*;
import org.opengis.referencing.operation.*;
import org.opengis.parameter.ParameterDescriptor;

// Geotools dependencies
import org.geotools.resources.Classes;
import org.geotools.resources.i18n.Errors;
import org.geotools.resources.i18n.ErrorKeys;

/**
 * Delegates object creations to one of the {@code create} methods in a backing
 * {@linkplain AuthorityFactory authority factory}. It is possible to use the generic
 * {@link AuthorityFactory#createObject createObject} method instead of this class,
 * but some factory implementations are more efficient when we use the most specific
 * {@code create} method. For example when using a
 * {@linkplain org.geotools.referencing.factory.epsg.DirectEpsgFactory EPSG factory backed
 * by a SQL database}, invoking {@link CRSAuthorityFactory#createCoordinateReferenceSystem
 * createCoordinateReferenceSystem} instead of {@link AuthorityFactory#createObject createObject}
 * method will reduce the amount of tables to be queried.
 * <p>
 * This class is useful when the same {@code create} method need to be invoked often,
 * but is unknown at compile time. It may also be used as a workaround for authority
 * factories that don't implement the {@code createObject} method.
 * <p>
 * <b>Example:</b> The following code creates a proxy which will delegates its work to the
 * {@link CRSAuthorityFactory#createGeographicCRS createGeographicCRS} method.
 *
 * <blockquote><pre>
 * AuthorityFactory factory = ...;
 * AuthorityFactoryProxy<GeographicCRS> proxy =
 *         AuthorityFactoryProxy.getInstance(GeographicCRS.class, factory);
 *
 * String code = ...;
 * // Invokes CRSAuthorityFactory.createGeographicCRS(code);
 * GeographicCRS crs = proxy.create(code);
 * </pre></blockquote>
 *
 * @since 2.4
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
abstract class AuthorityFactoryProxy {
    /**
     * The types that factories can be create. The most
     * specific types must appear first in this list.
     */
    private static final Class/*<? extends IdentifiedObject>*/[] TYPES = {
        CoordinateOperation      .class,
        OperationMethod          .class,
        ParameterDescriptor      .class,
        ProjectedCRS             .class,
        GeographicCRS            .class,
        GeocentricCRS            .class,
        ImageCRS                 .class,
        DerivedCRS               .class,
        VerticalCRS              .class,
        TemporalCRS              .class,
        EngineeringCRS           .class,
        CompoundCRS              .class,
        CoordinateReferenceSystem.class,
        CoordinateSystemAxis     .class,
        CartesianCS              .class,
        EllipsoidalCS            .class,
        SphericalCS              .class,
        CylindricalCS            .class,
        PolarCS                  .class,
        VerticalCS               .class,
        TimeCS                   .class,
        CoordinateSystem         .class,
        PrimeMeridian            .class,
        Ellipsoid                .class,
        GeodeticDatum            .class,
        ImageDatum               .class,
        VerticalDatum            .class,
        TemporalDatum            .class,
        EngineeringDatum         .class,
        Datum                    .class,
        IdentifiedObject         .class
    };

    /**
     * Creates a new proxy.
     */
    AuthorityFactoryProxy() {
    }

    /**
     * Returns a proxy instance which will create objects of the specified type using the
     * specified factory.
     *
     * @param  factory The factory to use for object creations.
     * @param  type    The type of objects to be created by the proxy.
     */
    public static AuthorityFactoryProxy getInstance(final AuthorityFactory factory,
                                                    Class/*<? extends IdentifiedObject>*/ type)
    {
        AbstractAuthorityFactory.ensureNonNull("type",    type);
        AbstractAuthorityFactory.ensureNonNull("factory", factory);
        type = getType(type);
        /*
         * Checks for some special cases for which a fast implementation is available.
         */
        if (factory instanceof CRSAuthorityFactory) {
            final CRSAuthorityFactory crsFactory = (CRSAuthorityFactory) factory;
            if (type.equals(             ProjectedCRS.class)) return new  Projected(crsFactory);
            if (type.equals(            GeographicCRS.class)) return new Geographic(crsFactory);
            if (type.equals(CoordinateReferenceSystem.class)) return new        CRS(crsFactory);
        }
        /*
         * Fallback on the generic case using reflection.
         */
        return new Default(factory, type);
    }

    /**
     * Returns the main GeoAPI interface implemented by an object of the specified type.
     * The {@code type} argument is often some implementation class like
     * {@link org.geotools.referencing.crs.DefaultProjectedCRS}. This method returns
     * the most specific GeoAPI interface implemented by {@code type}, providing that
     * a corresponding {@code create} method exists in some {@linkplain AuthorityFactory
     * authority factory}. For example this method may returns {@link ProjectedCRS} or
     * {@link DerivedCRS} class, but not {@link GeneralDerivedCRS}.
     *
     * @param  type The implementation class.
     * @return The most specific GeoAPI interface implemented by {@code type}.
     * @throws IllegalArgumentException if the type doesn't implement a valid interface.
     */
    public static Class/*<? extends IdentifiedObject>*/ getType(
            final Class/*<? extends IdentifiedObject>*/ type)
            throws IllegalArgumentException
    {
        for (int i=0; i<TYPES.length; i++) {
            final Class/*<? extends IdentifiedObject>*/ candidate = TYPES[i];
            if (candidate.isAssignableFrom(type)) {
                return candidate;
            }
        }
        throw new IllegalArgumentException(Errors.format(ErrorKeys.ILLEGAL_CLASS_$2, type,
                IdentifiedObject.class));
    }

    /**
     * Returns the type of the objects to be created by this proxy instance.
     */
    public abstract Class/*<? extends IdentifiedObject>*/ getType();

    /**
     * Returns the authority factory used by the {@link #create create} method.
     */
    public abstract AuthorityFactory getAuthorityFactory();

    /**
     * Returns the set of authority codes.
     *
     * @throws FactoryException if access to the underlying database failed.
     */
    public final Set/*<String>*/ getAuthorityCodes() throws FactoryException {
        return getAuthorityFactory().getAuthorityCodes(getType());
    }

    /**
     * Creates an object for the specified code. This method will delegates to the most
     * specific {@code create} method from the authority factory. The returned object
     * will always be of the type returned by {@link #getType()}.
     *
     * @throws NoSuchAuthorityCodeException if the specified {@code code} was not found.
     * @throws FactoryException if the object creation failed for some other reason.
     */
    public abstract IdentifiedObject create(String code)
            throws NoSuchAuthorityCodeException, FactoryException;

    /**
     * Returns a string representation of this proxy, for debugging purpose only.
     */
    @Override
    public String toString() {
        return toString(AuthorityFactoryProxy.class);
    }

    /**
     * Returns a string representation of the specified object, for debugging purpose only.
     */
    final String toString(final Class owner) {
        final AuthorityFactory factory = getAuthorityFactory();
        return Classes.getShortName(owner) + '[' +
               Classes.getShortName(getType()) + " in " +
               Classes.getShortClassName(factory) + "(\"" +
               factory.getAuthority().getTitle() + "\")]";
    }


    /**
     * A default implementation using reflections. To be used only when we don't provide
     * a specialized, more efficient, implementation.
     *
     * @version $Id$
     * @author Martin Desruisseaux
     */
    private static final class Default extends AuthorityFactoryProxy {
        /**
         * The argument types of {@code createFoo} methods.
         */
        private static final Class[] PARAMETERS = new Class[] {String.class};

        /**
         * The authority factory on which to delegates.
         */
        private final AuthorityFactory factory;

        /**
         * The type of the objects to be created.
         */
        private final Class/*<? extends IdentifiedObject>*/ type;

        /**
         * The {@code createFoo} method to invoke.
         */
        private final Method method;

        /**
         * Creates a new proxy which will delegates the object creation to the specified instance.
         */
        Default(final AuthorityFactory factory, final Class/*<? extends IdentifiedObject>*/ type)
                throws IllegalArgumentException
        {
            this.factory = factory;
            this.type = type;
            final Method[] candidates = factory.getClass().getMethods();
            for (int i=0; i<candidates.length; i++) {
                final Method c = candidates[i];
                if (c.getName().startsWith("create") && type.equals(c.getReturnType()) &&
                        Arrays.equals(PARAMETERS, c.getParameterTypes()))
                {
                    method = c;
                    return;
                }
            }
            throw new IllegalArgumentException(Errors.format(ErrorKeys.UNKNOW_TYPE_$1, type));
        }

        /**
         * {@inheritDoc}
         */
        public Class/*<? extends IdentifiedObject>*/ getType() {
            return type;
        }

        /**
         * {@inheritDoc}
         */
        public AuthorityFactory getAuthorityFactory() {
            return factory;
        }

        /**
         * {@inheritDoc}
         */
        public IdentifiedObject create(final String code) throws FactoryException {
            try {
                return (IdentifiedObject) method.invoke(factory, code);
            } catch (InvocationTargetException exception) {
                final Throwable cause = exception.getCause();
                if (cause instanceof FactoryException) {
                    throw (FactoryException) cause;
                }
                if (cause instanceof RuntimeException) {
                    throw (RuntimeException) cause;
                }
                if (cause instanceof Error) {
                    throw (Error) cause;
                }
                throw new FactoryException(cause.getLocalizedMessage(), cause);
            } catch (IllegalAccessException exception) {
                throw new FactoryException(exception.getLocalizedMessage(), exception);
            }
        }
    }


    /**
     * An implementation for {@link CoordinateReferenceSystem} objects.
     *
     * @version $Id$
     * @author Martin Desruisseaux
     */
    private static class CRS extends AuthorityFactoryProxy {
        /** The authority factory on which to delegates. */
        protected final CRSAuthorityFactory factory;

        protected CRS(final CRSAuthorityFactory factory) {
            this.factory = factory;
        }

        public Class getType() {
            return CoordinateReferenceSystem.class;
        }

        public final AuthorityFactory getAuthorityFactory() {
            return factory;
        }

        public IdentifiedObject create(final String code) throws FactoryException {
            return factory.createCoordinateReferenceSystem(code);
        }
    }


    /**
     * An implementation for {@link GeographicCRS} objects.
     *
     * @version $Id$
     * @author Martin Desruisseaux
     */
    private static final class Geographic extends CRS {
        protected Geographic(final CRSAuthorityFactory factory) {
            super(factory);
        }

        @Override
        public Class getType() {
            return GeographicCRS.class;
        }

        @Override
        public IdentifiedObject create(final String code) throws FactoryException {
            return factory.createGeographicCRS(code);
        }
    }


    /**
     * An implementation for {@link ProjectedCRS} objects.
     *
     * @version $Id$
     * @author Martin Desruisseaux
     */
    private static final class Projected extends CRS {
        protected Projected(final CRSAuthorityFactory factory) {
            super(factory);
        }

        @Override
        public Class getType() {
            return ProjectedCRS.class;
        }

        @Override
        public IdentifiedObject create(final String code) throws FactoryException {
            return factory.createProjectedCRS(code);
        }
    }
}
