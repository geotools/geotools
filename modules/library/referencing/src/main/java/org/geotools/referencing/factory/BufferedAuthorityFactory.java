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

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import javax.measure.Unit;
import org.geotools.api.metadata.citation.Citation;
import org.geotools.api.metadata.extent.Extent;
import org.geotools.api.parameter.ParameterDescriptor;
import org.geotools.api.referencing.AuthorityFactory;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.IdentifiedObject;
import org.geotools.api.referencing.NoSuchAuthorityCodeException;
import org.geotools.api.referencing.crs.CRSAuthorityFactory;
import org.geotools.api.referencing.crs.CompoundCRS;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.crs.DerivedCRS;
import org.geotools.api.referencing.crs.EngineeringCRS;
import org.geotools.api.referencing.crs.GeocentricCRS;
import org.geotools.api.referencing.crs.GeographicCRS;
import org.geotools.api.referencing.crs.ImageCRS;
import org.geotools.api.referencing.crs.ProjectedCRS;
import org.geotools.api.referencing.crs.TemporalCRS;
import org.geotools.api.referencing.crs.VerticalCRS;
import org.geotools.api.referencing.cs.CSAuthorityFactory;
import org.geotools.api.referencing.cs.CartesianCS;
import org.geotools.api.referencing.cs.CoordinateSystem;
import org.geotools.api.referencing.cs.CoordinateSystemAxis;
import org.geotools.api.referencing.cs.CylindricalCS;
import org.geotools.api.referencing.cs.EllipsoidalCS;
import org.geotools.api.referencing.cs.PolarCS;
import org.geotools.api.referencing.cs.SphericalCS;
import org.geotools.api.referencing.cs.TimeCS;
import org.geotools.api.referencing.cs.VerticalCS;
import org.geotools.api.referencing.datum.Datum;
import org.geotools.api.referencing.datum.DatumAuthorityFactory;
import org.geotools.api.referencing.datum.Ellipsoid;
import org.geotools.api.referencing.datum.EngineeringDatum;
import org.geotools.api.referencing.datum.GeodeticDatum;
import org.geotools.api.referencing.datum.ImageDatum;
import org.geotools.api.referencing.datum.PrimeMeridian;
import org.geotools.api.referencing.datum.TemporalDatum;
import org.geotools.api.referencing.datum.VerticalDatum;
import org.geotools.api.referencing.operation.CoordinateOperation;
import org.geotools.api.referencing.operation.CoordinateOperationAuthorityFactory;
import org.geotools.api.referencing.operation.OperationMethod;
import org.geotools.api.util.InternationalString;
import org.geotools.metadata.i18n.ErrorKeys;
import org.geotools.metadata.i18n.LoggingKeys;
import org.geotools.metadata.i18n.Loggings;
import org.geotools.util.Utilities;
import org.geotools.util.factory.BufferedFactory;
import org.geotools.util.factory.Hints;

/**
 * An authority factory that caches all objects created by an other factory. All {@code createFoo(String)} methods first
 * looks if a previously created object exists for the given code. If such an object exists, it is returned. Otherwise,
 * the object creation is delegated to the {@linkplain AbstractAuthorityFactory authority factory} specified at creation
 * time, and the result is cached in this buffered factory.
 *
 * <p>Objects are cached by strong references, up to the amount of objects specified at construction time. If a greater
 * amount of objects are cached, the oldest ones will be retained through a {@linkplain WeakReference weak reference}
 * instead of a strong one. This means that this buffered factory will continue to returns them as long as they are in
 * use somewhere else in the Java virtual machine, but will be discarted (and recreated on the fly if needed) otherwise.
 *
 * @since 2.1
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public class BufferedAuthorityFactory extends AbstractAuthorityFactory implements BufferedFactory {
    /** The default value for {@link #maxStrongReferences}. */
    static final int DEFAULT_MAX = 20;

    /**
     * The underlying authority factory. This field may be {@code null} if this object was created by the
     * {@linkplain #BufferedAuthorityFactory(AbstractAuthorityFactory,int) package protected constructor}. In this case,
     * the subclass is responsible for creating the backing store when
     * {@link DeferredAuthorityFactory#createBackingStore} is invoked.
     *
     * @see #getBackingStore
     * @see DeferredAuthorityFactory#createBackingStore
     */
    volatile AbstractAuthorityFactory backingStore;

    /** The pool of cached objects. */
    private final LinkedHashMap<Object, Object> pool = new LinkedHashMap<>(32, 0.75f, true);

    /**
     * The maximum number of objects to keep by strong reference. If a greater amount of objects are created, then the
     * strong references for the oldest ones are replaced by weak references.
     */
    private final int maxStrongReferences;

    /** The pool of objects identified by {@link #find}. */
    private final Map<IdentifiedObject, IdentifiedObject> findPool = new WeakHashMap<>();

    /**
     * Constructs an instance wrapping the specified factory with a default number of entries to keep by strong
     * reference.
     *
     * <p>This constructor is protected because subclasses must declare which of the {@link DatumAuthorityFactory},
     * {@link CSAuthorityFactory}, {@link CRSAuthorityFactory} and {@link CoordinateOperationAuthorityFactory}
     * interfaces they choose to implement.
     *
     * @param factory The factory to cache. Can not be {@code null}.
     */
    protected BufferedAuthorityFactory(final AbstractAuthorityFactory factory) {
        this(factory, DEFAULT_MAX);
    }

    /**
     * Constructs an instance wrapping the specified factory. The {@code maxStrongReferences} argument specify the
     * maximum number of objects to keep by strong reference. If a greater amount of objects are created, then the
     * strong references for the oldest ones are replaced by weak references.
     *
     * <p>This constructor is protected because subclasses must declare which of the {@link DatumAuthorityFactory},
     * {@link CSAuthorityFactory}, {@link CRSAuthorityFactory} and {@link CoordinateOperationAuthorityFactory}
     * interfaces they choose to implement.
     *
     * @param factory The factory to cache. Can not be {@code null}.
     * @param maxStrongReferences The maximum number of objects to keep by strong reference.
     */
    protected BufferedAuthorityFactory(AbstractAuthorityFactory factory, final int maxStrongReferences) {
        super(factory.getPriority());
        while (factory instanceof BufferedAuthorityFactory) {
            factory = ((BufferedAuthorityFactory) factory).backingStore;
        }
        this.backingStore = factory;
        this.maxStrongReferences = maxStrongReferences;
        completeHints();
    }

    /**
     * Constructs an instance without initial backing store. This constructor is for subclass constructors only.
     * Subclasses are responsible for creating an appropriate backing store when the
     * {@link DeferredAuthorityFactory#createBackingStore} method is invoked.
     *
     * @param priority The priority for this factory, as a number between {@link #MINIMUM_PRIORITY MINIMUM_PRIORITY} and
     *     {@link #MAXIMUM_PRIORITY MAXIMUM_PRIORITY} inclusive.
     * @param maxStrongReferences The maximum number of objects to keep by strong reference.
     * @see DeferredAuthorityFactory#createBackingStore
     */
    BufferedAuthorityFactory(final int priority, final int maxStrongReferences) {
        super(priority);
        this.maxStrongReferences = maxStrongReferences;
        // completeHints() will be invoked by DeferredAuthorityFactory.getBackingStore()
    }

    /**
     * Completes the set of hints according the value currently set in this object. This method is invoked by
     * {@code BufferedAuthorityFactory} or by {@code DeferredAuthorityFactory} at backing store creation time.
     *
     * <p>The backing store is of course an important dependency. This method gives a chance to
     * {@link org.geotools.util.factory.FactoryRegistry} to compare the user-requested hints (especially
     * {@link Hints#FORCE_LONGITUDE_FIRST_AXIS_ORDER}) against the backing store hints, by following the dependency
     * declared there.
     *
     * <p>DON'T FORGET to set those hints to {@code null} when {@link DeferredAuthorityFactory} dispose the backing
     * store.
     */
    final void completeHints() {
        if (backingStore instanceof DatumAuthorityFactory) {
            hints.put(Hints.DATUM_AUTHORITY_FACTORY, backingStore);
        }
        if (backingStore instanceof CSAuthorityFactory) {
            hints.put(Hints.CS_AUTHORITY_FACTORY, backingStore);
        }
        if (backingStore instanceof CRSAuthorityFactory) {
            hints.put(Hints.CRS_AUTHORITY_FACTORY, backingStore);
        }
        if (backingStore instanceof CoordinateOperationAuthorityFactory) {
            hints.put(Hints.COORDINATE_OPERATION_AUTHORITY_FACTORY, backingStore);
        }
    }

    /**
     * Returns the direct dependencies. The returned list contains the backing store specified at construction time, or
     * the exception if it can't be obtained.
     */
    @Override
    Collection<? super AuthorityFactory> dependencies() {
        Object factory;
        try {
            factory = getBackingStore();
        } catch (FactoryException e) {
            factory = e;
        }
        return Collections.singleton(factory);
    }

    /**
     * Returns the backing store authority factory.
     *
     * @return The backing store to uses in {@code createXXX(...)} methods.
     * @throws FactoryException if the creation of backing store failed.
     */
    AbstractAuthorityFactory getBackingStore() throws FactoryException {
        if (backingStore == null) {
            throw new FactoryException(ErrorKeys.DISPOSED_FACTORY);
        }
        return backingStore;
    }

    /**
     * Returns {@code true} if this factory is available. The default implementation returns {@code false} if no backing
     * store were setup and {@link DeferredAuthorityFactory#createBackingStore} throws an exception.
     */
    @Override
    boolean isAvailable() {
        try {
            return getBackingStore().isAvailable();
        } catch (FactoryNotFoundException exception) {
            /*
             * The factory is not available. This is error may be normal; it happens
             * for example if no gt2-epsg-hsql.jar (or similar JAR) are found in the
             * classpath, which is the case for example in GeoServer 1.3. Do not log
             * any stack trace,  since stack traces suggest more serious errors than
             * what we really have here.
             */
        } catch (FactoryException exception) {
            /*
             * The factory creation failed for an other reason, which may be more
             * serious. Now it is time to log a warning with a stack trace.
             */
            final Citation citation = getAuthority();
            final Collection<? extends InternationalString> titles = citation.getAlternateTitles();
            InternationalString title = citation.getTitle();
            if (titles != null) {
                for (final InternationalString candidate : titles) {
                    /*
                     * Uses the longuest title instead of the main one. In Geotools
                     * implementation, the alternate title may contains usefull informations
                     * like the EPSG database version number and the database engine.
                     */
                    if (candidate.length() > title.length()) {
                        title = candidate;
                    }
                }
            }
            final LogRecord record =
                    Loggings.format(Level.WARNING, LoggingKeys.UNAVAILABLE_AUTHORITY_FACTORY_$1, title);
            record.setSourceClassName(getClass().getName());
            record.setSourceMethodName("isAvailable");
            record.setThrown(exception);
            record.setLoggerName(LOGGER.getName());
            LOGGER.log(record);
        }
        return false;
    }

    /**
     * If this factory is a wrapper for the specified factory that do not add any additional
     * {@linkplain #getAuthorityCodes authority codes}, returns {@code true}. This method is for
     * {@link FallbackAuthorityFactory} internal use only and should not be public. A cheap test without
     * {@link #getBackingStore} invocation is suffisient for our needs.
     */
    @Override
    boolean sameAuthorityCodes(final AuthorityFactory factory) {
        final AbstractAuthorityFactory backingStore = this.backingStore; // Protect from changes.
        if (backingStore != null && backingStore.sameAuthorityCodes(factory)) {
            return true;
        }
        return super.sameAuthorityCodes(factory);
    }

    /** Returns the vendor responsible for creating the underlying factory implementation. */
    @Override
    public Citation getVendor() {
        return backingStore != null ? backingStore.getVendor() : super.getVendor();
    }

    /** Returns the organization or party responsible for definition and maintenance of the underlying database. */
    @Override
    public Citation getAuthority() {
        return backingStore != null ? backingStore.getAuthority() : null;
    }

    /**
     * Returns a description of the underlying backing store, or {@code null} if unknow. This is for example the
     * database software used for storing the data.
     *
     * @throws FactoryException if a failure occured while fetching the engine description.
     */
    @Override
    public String getBackingStoreDescription() throws FactoryException {
        return getBackingStore().getBackingStoreDescription();
    }

    /**
     * Returns the set of authority codes of the given type. The {@code type} argument specify the base class.
     *
     * @param type The spatial reference objects type.
     * @return The set of authority codes for spatial reference objects of the given type. If this factory doesn't
     *     contains any object of the given type, then this method returns an
     *     {@linkplain java.util.Collections.emptySet() empty set}.
     * @throws FactoryException if access to the underlying database failed.
     */
    @Override
    public Set<String> getAuthorityCodes(final Class<? extends IdentifiedObject> type) throws FactoryException {
        return getBackingStore().getAuthorityCodes(type);
    }

    /**
     * Gets a description of the object corresponding to a code.
     *
     * @param code Value allocated by authority.
     * @return A description of the object, or {@code null} if the object corresponding to the specified {@code code}
     *     has no description.
     * @throws NoSuchAuthorityCodeException if the specified {@code code} was not found.
     * @throws FactoryException if the query failed for some other reason.
     */
    @Override
    public InternationalString getDescriptionText(final String code)
            throws NoSuchAuthorityCodeException, FactoryException {
        return getBackingStore().getDescriptionText(code);
    }

    /**
     * Returns an arbitrary object from a code.
     *
     * @throws FactoryException if the object creation failed.
     */
    @Override
    public synchronized IdentifiedObject createObject(final String code) throws FactoryException {
        final IdentifiedObject object;
        final String key = trimAuthority(code);
        final Object cached = get(key);
        if (cached instanceof IdentifiedObject) {
            object = (IdentifiedObject) cached;
        } else {
            object = getBackingStore().createObject(code);
        }
        put(key, object);
        return object;
    }

    /**
     * Returns an arbitrary datum from a code.
     *
     * @throws FactoryException if the object creation failed.
     */
    @Override
    public synchronized Datum createDatum(final String code) throws FactoryException {
        final Datum datum;
        final String key = trimAuthority(code);
        final Object cached = get(key);
        if (cached instanceof Datum) {
            datum = (Datum) cached;
        } else {
            datum = getBackingStore().createDatum(code);
        }
        put(key, datum);
        return datum;
    }

    /**
     * Returns an engineering datum from a code.
     *
     * @throws FactoryException if the object creation failed.
     */
    @Override
    public synchronized EngineeringDatum createEngineeringDatum(final String code) throws FactoryException {
        final EngineeringDatum datum;
        final String key = trimAuthority(code);
        final Object cached = get(key);
        if (cached instanceof EngineeringDatum) {
            datum = (EngineeringDatum) cached;
        } else {
            datum = getBackingStore().createEngineeringDatum(code);
        }
        put(key, datum);
        return datum;
    }

    /**
     * Returns an image datum from a code.
     *
     * @throws FactoryException if the object creation failed.
     */
    @Override
    public synchronized ImageDatum createImageDatum(final String code) throws FactoryException {
        final ImageDatum datum;
        final String key = trimAuthority(code);
        final Object cached = get(key);
        if (cached instanceof ImageDatum) {
            datum = (ImageDatum) cached;
        } else {
            datum = getBackingStore().createImageDatum(code);
        }
        put(key, datum);
        return datum;
    }

    /**
     * Returns a vertical datum from a code.
     *
     * @throws FactoryException if the object creation failed.
     */
    @Override
    public synchronized VerticalDatum createVerticalDatum(final String code) throws FactoryException {
        final VerticalDatum datum;
        final String key = trimAuthority(code);
        final Object cached = get(key);
        if (cached instanceof VerticalDatum) {
            datum = (VerticalDatum) cached;
        } else {
            datum = getBackingStore().createVerticalDatum(code);
        }
        put(key, datum);
        return datum;
    }

    /**
     * Returns a temporal datum from a code.
     *
     * @throws FactoryException if the object creation failed.
     */
    @Override
    public synchronized TemporalDatum createTemporalDatum(final String code) throws FactoryException {
        final TemporalDatum datum;
        final String key = trimAuthority(code);
        final Object cached = get(key);
        if (cached instanceof TemporalDatum) {
            datum = (TemporalDatum) cached;
        } else {
            datum = getBackingStore().createTemporalDatum(code);
        }
        put(key, datum);
        return datum;
    }

    /**
     * Returns a geodetic datum from a code.
     *
     * @throws FactoryException if the object creation failed.
     */
    @Override
    public synchronized GeodeticDatum createGeodeticDatum(final String code) throws FactoryException {
        final GeodeticDatum datum;
        final String key = trimAuthority(code);
        final Object cached = get(key);
        if (cached instanceof GeodeticDatum) {
            datum = (GeodeticDatum) cached;
        } else {
            datum = getBackingStore().createGeodeticDatum(code);
        }
        put(key, datum);
        return datum;
    }

    /**
     * Returns an ellipsoid from a code.
     *
     * @throws FactoryException if the object creation failed.
     */
    @Override
    public synchronized Ellipsoid createEllipsoid(final String code) throws FactoryException {
        final Ellipsoid ellipsoid;
        final String key = trimAuthority(code);
        final Object cached = get(key);
        if (cached instanceof Ellipsoid) {
            ellipsoid = (Ellipsoid) cached;
        } else {
            ellipsoid = getBackingStore().createEllipsoid(code);
        }
        put(key, ellipsoid);
        return ellipsoid;
    }

    /**
     * Returns a prime meridian from a code.
     *
     * @throws FactoryException if the object creation failed.
     */
    @Override
    public synchronized PrimeMeridian createPrimeMeridian(final String code) throws FactoryException {
        final PrimeMeridian meridian;
        final String key = trimAuthority(code);
        final Object cached = get(key);
        if (cached instanceof PrimeMeridian) {
            meridian = (PrimeMeridian) cached;
        } else {
            meridian = getBackingStore().createPrimeMeridian(code);
        }
        put(key, meridian);
        return meridian;
    }

    /**
     * Returns an extent (usually an area of validity) from a code.
     *
     * @throws FactoryException if the object creation failed.
     */
    @Override
    public synchronized Extent createExtent(final String code) throws FactoryException {
        final Extent extent;
        final String key = trimAuthority(code);
        final Object cached = get(key);
        if (cached instanceof Extent) {
            extent = (Extent) cached;
        } else {
            extent = getBackingStore().createExtent(code);
        }
        put(key, extent);
        return extent;
    }

    /**
     * Returns an arbitrary coordinate system from a code.
     *
     * @throws FactoryException if the object creation failed.
     */
    @Override
    public synchronized CoordinateSystem createCoordinateSystem(final String code) throws FactoryException {
        final CoordinateSystem cs;
        final String key = trimAuthority(code);
        final Object cached = get(key);
        if (cached instanceof CoordinateSystem) {
            cs = (CoordinateSystem) cached;
        } else {
            cs = getBackingStore().createCoordinateSystem(code);
        }
        put(key, cs);
        return cs;
    }

    /**
     * Returns a cartesian coordinate system from a code.
     *
     * @throws FactoryException if the object creation failed.
     */
    @Override
    public synchronized CartesianCS createCartesianCS(final String code) throws FactoryException {
        final CartesianCS cs;
        final String key = trimAuthority(code);
        final Object cached = get(key);
        if (cached instanceof CartesianCS) {
            cs = (CartesianCS) cached;
        } else {
            cs = getBackingStore().createCartesianCS(code);
        }
        put(key, cs);
        return cs;
    }

    /**
     * Returns a polar coordinate system from a code.
     *
     * @throws FactoryException if the object creation failed.
     */
    @Override
    public synchronized PolarCS createPolarCS(final String code) throws FactoryException {
        final PolarCS cs;
        final String key = trimAuthority(code);
        final Object cached = get(key);
        if (cached instanceof PolarCS) {
            cs = (PolarCS) cached;
        } else {
            cs = getBackingStore().createPolarCS(code);
        }
        put(key, cs);
        return cs;
    }

    /**
     * Returns a cylindrical coordinate system from a code.
     *
     * @throws FactoryException if the object creation failed.
     */
    @Override
    public synchronized CylindricalCS createCylindricalCS(final String code) throws FactoryException {
        final CylindricalCS cs;
        final String key = trimAuthority(code);
        final Object cached = get(key);
        if (cached instanceof CylindricalCS) {
            cs = (CylindricalCS) cached;
        } else {
            cs = getBackingStore().createCylindricalCS(code);
        }
        put(key, cs);
        return cs;
    }

    /**
     * Returns a spherical coordinate system from a code.
     *
     * @throws FactoryException if the object creation failed.
     */
    @Override
    public synchronized SphericalCS createSphericalCS(final String code) throws FactoryException {
        final SphericalCS cs;
        final String key = trimAuthority(code);
        final Object cached = get(key);
        if (cached instanceof SphericalCS) {
            cs = (SphericalCS) cached;
        } else {
            cs = getBackingStore().createSphericalCS(code);
        }
        put(key, cs);
        return cs;
    }

    /**
     * Returns an ellipsoidal coordinate system from a code.
     *
     * @throws FactoryException if the object creation failed.
     */
    @Override
    public synchronized EllipsoidalCS createEllipsoidalCS(final String code) throws FactoryException {
        final EllipsoidalCS cs;
        final String key = trimAuthority(code);
        final Object cached = get(key);
        if (cached instanceof EllipsoidalCS) {
            cs = (EllipsoidalCS) cached;
        } else {
            cs = getBackingStore().createEllipsoidalCS(code);
        }
        put(key, cs);
        return cs;
    }

    /**
     * Returns a vertical coordinate system from a code.
     *
     * @throws FactoryException if the object creation failed.
     */
    @Override
    public synchronized VerticalCS createVerticalCS(final String code) throws FactoryException {
        final VerticalCS cs;
        final String key = trimAuthority(code);
        final Object cached = get(key);
        if (cached instanceof VerticalCS) {
            cs = (VerticalCS) cached;
        } else {
            cs = getBackingStore().createVerticalCS(code);
        }
        put(key, cs);
        return cs;
    }

    /**
     * Returns a temporal coordinate system from a code.
     *
     * @throws FactoryException if the object creation failed.
     */
    @Override
    public synchronized TimeCS createTimeCS(final String code) throws FactoryException {
        final TimeCS cs;
        final String key = trimAuthority(code);
        final Object cached = get(key);
        if (cached instanceof TimeCS) {
            cs = (TimeCS) cached;
        } else {
            cs = getBackingStore().createTimeCS(code);
        }
        put(key, cs);
        return cs;
    }

    /**
     * Returns a coordinate system axis from a code.
     *
     * @throws FactoryException if the object creation failed.
     */
    @Override
    public synchronized CoordinateSystemAxis createCoordinateSystemAxis(final String code) throws FactoryException {
        final CoordinateSystemAxis axis;
        final String key = trimAuthority(code);
        final Object cached = get(key);
        if (cached instanceof CoordinateSystemAxis) {
            axis = (CoordinateSystemAxis) cached;
        } else {
            axis = getBackingStore().createCoordinateSystemAxis(code);
        }
        put(key, axis);
        return axis;
    }

    /**
     * Returns an unit from a code.
     *
     * @throws FactoryException if the object creation failed.
     */
    @Override
    public synchronized Unit<?> createUnit(final String code) throws FactoryException {
        final Unit<?> unit;
        final String key = trimAuthority(code);
        final Object cached = get(key);
        if (cached instanceof Unit) {
            unit = (Unit) cached;
        } else {
            unit = getBackingStore().createUnit(code);
        }
        put(key, unit);
        return unit;
    }

    /**
     * Returns an arbitrary coordinate reference system from a code.
     *
     * @throws FactoryException if the object creation failed.
     */
    @Override
    public synchronized CoordinateReferenceSystem createCoordinateReferenceSystem(final String code)
            throws FactoryException {
        final CoordinateReferenceSystem crs;
        final String key = trimAuthority(code);
        final Object cached = get(key);
        if (cached instanceof CoordinateReferenceSystem) {
            crs = (CoordinateReferenceSystem) cached;
        } else {
            crs = getBackingStore().createCoordinateReferenceSystem(code);
        }
        put(key, crs);
        return crs;
    }

    /**
     * Returns a 3D coordinate reference system from a code.
     *
     * @throws FactoryException if the object creation failed.
     */
    @Override
    public synchronized CompoundCRS createCompoundCRS(final String code) throws FactoryException {
        final CompoundCRS crs;
        final String key = trimAuthority(code);
        final Object cached = get(key);
        if (cached instanceof CompoundCRS) {
            crs = (CompoundCRS) cached;
        } else {
            crs = getBackingStore().createCompoundCRS(code);
        }
        put(key, crs);
        return crs;
    }

    /**
     * Returns a derived coordinate reference system from a code.
     *
     * @throws FactoryException if the object creation failed.
     */
    @Override
    public synchronized DerivedCRS createDerivedCRS(final String code) throws FactoryException {
        final DerivedCRS crs;
        final String key = trimAuthority(code);
        final Object cached = get(key);
        if (cached instanceof DerivedCRS) {
            crs = (DerivedCRS) cached;
        } else {
            crs = getBackingStore().createDerivedCRS(code);
        }
        put(key, crs);
        return crs;
    }

    /**
     * Returns an engineering coordinate reference system from a code.
     *
     * @throws FactoryException if the object creation failed.
     */
    @Override
    public synchronized EngineeringCRS createEngineeringCRS(final String code) throws FactoryException {
        final EngineeringCRS crs;
        final String key = trimAuthority(code);
        final Object cached = get(key);
        if (cached instanceof EngineeringCRS) {
            crs = (EngineeringCRS) cached;
        } else {
            crs = getBackingStore().createEngineeringCRS(code);
        }
        put(key, crs);
        return crs;
    }

    /**
     * Returns a geographic coordinate reference system from a code.
     *
     * @throws FactoryException if the object creation failed.
     */
    @Override
    public synchronized GeographicCRS createGeographicCRS(final String code) throws FactoryException {
        final GeographicCRS crs;
        final String key = trimAuthority(code);
        final Object cached = get(key);
        if (cached instanceof GeographicCRS) {
            crs = (GeographicCRS) cached;
        } else {
            crs = getBackingStore().createGeographicCRS(code);
        }
        put(key, crs);
        return crs;
    }

    /**
     * Returns a geocentric coordinate reference system from a code.
     *
     * @throws FactoryException if the object creation failed.
     */
    @Override
    public synchronized GeocentricCRS createGeocentricCRS(final String code) throws FactoryException {
        final GeocentricCRS crs;
        final String key = trimAuthority(code);
        final Object cached = get(key);
        if (cached instanceof GeocentricCRS) {
            crs = (GeocentricCRS) cached;
        } else {
            crs = getBackingStore().createGeocentricCRS(code);
        }
        put(key, crs);
        return crs;
    }

    /**
     * Returns an image coordinate reference system from a code.
     *
     * @throws FactoryException if the object creation failed.
     */
    @Override
    public synchronized ImageCRS createImageCRS(final String code) throws FactoryException {
        final ImageCRS crs;
        final String key = trimAuthority(code);
        final Object cached = get(key);
        if (cached instanceof ImageCRS) {
            crs = (ImageCRS) cached;
        } else {
            crs = getBackingStore().createImageCRS(code);
        }
        put(key, crs);
        return crs;
    }

    /**
     * Returns a projected coordinate reference system from a code.
     *
     * @throws FactoryException if the object creation failed.
     */
    @Override
    public synchronized ProjectedCRS createProjectedCRS(final String code) throws FactoryException {
        final ProjectedCRS crs;
        final String key = trimAuthority(code);
        final Object cached = get(key);
        if (cached instanceof ProjectedCRS) {
            crs = (ProjectedCRS) cached;
        } else {
            crs = getBackingStore().createProjectedCRS(code);
        }
        put(key, crs);
        return crs;
    }

    /**
     * Returns a temporal coordinate reference system from a code.
     *
     * @throws FactoryException if the object creation failed.
     */
    @Override
    public synchronized TemporalCRS createTemporalCRS(final String code) throws FactoryException {
        final TemporalCRS crs;
        final String key = trimAuthority(code);
        final Object cached = get(key);
        if (cached instanceof TemporalCRS) {
            crs = (TemporalCRS) cached;
        } else {
            crs = getBackingStore().createTemporalCRS(code);
        }
        put(key, crs);
        return crs;
    }

    /**
     * Returns a vertical coordinate reference system from a code.
     *
     * @throws FactoryException if the object creation failed.
     */
    @Override
    public synchronized VerticalCRS createVerticalCRS(final String code) throws FactoryException {
        final VerticalCRS crs;
        final String key = trimAuthority(code);
        final Object cached = get(key);
        if (cached instanceof VerticalCRS) {
            crs = (VerticalCRS) cached;
        } else {
            crs = getBackingStore().createVerticalCRS(code);
        }
        put(key, crs);
        return crs;
    }

    /**
     * Returns a parameter descriptor from a code.
     *
     * @throws FactoryException if the object creation failed.
     * @since 2.2
     */
    @Override
    public synchronized ParameterDescriptor createParameterDescriptor(final String code) throws FactoryException {
        final ParameterDescriptor parameter;
        final String key = trimAuthority(code);
        final Object cached = get(key);
        if (cached instanceof ParameterDescriptor) {
            parameter = (ParameterDescriptor) cached;
        } else {
            parameter = getBackingStore().createParameterDescriptor(code);
        }
        put(key, parameter);
        return parameter;
    }

    /**
     * Returns an operation method from a code.
     *
     * @throws FactoryException if the object creation failed.
     * @since 2.2
     */
    @Override
    public synchronized OperationMethod createOperationMethod(final String code) throws FactoryException {
        final OperationMethod method;
        final String key = trimAuthority(code);
        final Object cached = get(key);
        if (cached instanceof OperationMethod) {
            method = (OperationMethod) cached;
        } else {
            method = getBackingStore().createOperationMethod(code);
        }
        put(key, method);
        return method;
    }

    /**
     * Returns an operation from a single operation code.
     *
     * @throws FactoryException if the object creation failed.
     * @since 2.2
     */
    @Override
    public synchronized CoordinateOperation createCoordinateOperation(final String code) throws FactoryException {
        final CoordinateOperation operation;
        final String key = trimAuthority(code);
        final Object cached = get(key);
        if (cached instanceof CoordinateOperation) {
            operation = (CoordinateOperation) cached;
        } else {
            operation = getBackingStore().createCoordinateOperation(code);
        }
        put(key, operation);
        return operation;
    }

    /**
     * Returns an operation from coordinate reference system codes.
     *
     * @throws FactoryException if the object creation failed.
     * @since 2.2
     */
    @Override
    public synchronized Set<CoordinateOperation> createFromCoordinateReferenceSystemCodes(
            final String sourceCRS, final String targetCRS) throws FactoryException {
        final Set<CoordinateOperation> operations;
        final CodePair key = new CodePair(trimAuthority(sourceCRS), trimAuthority(targetCRS));
        final Object cached = get(key);
        if (cached instanceof Set) {
            @SuppressWarnings("unchecked")
            Set<CoordinateOperation> cast = (Set<CoordinateOperation>) cached;
            operations = cast;
        } else {
            operations = Collections.unmodifiableSet(
                    getBackingStore().createFromCoordinateReferenceSystemCodes(sourceCRS, targetCRS));
        }
        put(key, operations);
        return operations;
    }

    /** A pair of codes for operations to cache with {@link #createFromCoordinateReferenceSystemCodes}. */
    private static final class CodePair {
        private final String source, target;

        public CodePair(final String source, final String target) {
            this.source = source;
            this.target = target;
        }

        @Override
        public int hashCode() {
            int code = 0;
            if (source != null) code = source.hashCode();
            if (target != null) code += target.hashCode() * 37;
            return code;
        }

        @Override
        public boolean equals(final Object other) {
            if (other instanceof CodePair) {
                final CodePair that = (CodePair) other;
                return Utilities.equals(this.source, that.source) && Utilities.equals(this.target, that.target);
            }
            return false;
        }

        @Override
        public String toString() {
            return source + " \u21E8 " + target;
        }
    }

    /**
     * Returns a finder which can be used for looking up unidentified objects. The default implementation delegates
     * lookup to the underlying backing store and caches the result.
     *
     * @throws FactoryException if the finder can not be created.
     * @since 2.4
     */
    @Override
    public synchronized IdentifiedObjectFinder getIdentifiedObjectFinder(final Class<? extends IdentifiedObject> type)
            throws FactoryException {
        return new Finder(getBackingStore().getIdentifiedObjectFinder(type));
    }

    /**
     * An implementation of {@link IdentifiedObjectFinder} which delegates the work to the underlying backing store and
     * caches the result.
     *
     * <p><b>Implementation note:</b> we will create objects using directly the underlying backing store, not using the
     * cache. This is because hundred of objects may be created during a scan while only one will be typically retained.
     * We don't want to overload the cache with every false candidates that we encounter during the scan.
     */
    private final class Finder extends IdentifiedObjectFinder.Adapter {
        /** Creates a finder for the underlying backing store. */
        Finder(final IdentifiedObjectFinder finder) {
            super(finder);
        }

        /**
         * Looks up an object from this authority factory which is equals, ignoring metadata, to the specified object.
         * The default implementation performs the same lookup than the backing store and caches the result.
         */
        @Override
        public IdentifiedObject find(final IdentifiedObject object) throws FactoryException {
            /*
             * Do not synchronize on 'BufferedAuthorityFactory.this'. This method may take a
             * while to execute and we don't want to block other threads. The synchronizations
             * in the 'create' methods and in the 'findPool' map should be suffisient.
             *
             * TODO: avoid to search for the same object twice. For now we consider that this
             *       is not a big deal if the same object is searched twice; it is "just" a
             *       waste of CPU.
             */
            IdentifiedObject candidate;
            synchronized (findPool) {
                candidate = findPool.get(object);
            }
            if (candidate == null) {
                // Must delegates to 'finder' (not to 'super') in order to take
                // advantage of the method overriden by AllAuthoritiesFactory.
                candidate = finder.find(object);
                if (candidate != null) {
                    synchronized (findPool) {
                        findPool.put(object, candidate);
                    }
                }
            }
            return candidate;
        }

        /** Returns the identifier for the specified object. */
        @Override
        public String findIdentifier(final IdentifiedObject object) throws FactoryException {
            IdentifiedObject candidate;
            synchronized (findPool) {
                candidate = findPool.get(object);
            }
            if (candidate != null) {
                return getIdentifier(candidate);
            }
            // We don't rely on super-class implementation, because we want to
            // take advantage of the method overriden by AllAuthoritiesFactory.
            return finder.findIdentifier(object);
        }
    }

    /**
     * Releases resources immediately instead of waiting for the garbage collector.
     *
     * @throws FactoryException if an error occured while disposing the factory.
     */
    @Override
    public synchronized void dispose() throws FactoryException {
        if (backingStore != null) {
            backingStore.dispose();
            backingStore = null;
        }
        pool.clear();
        findPool.clear();
        super.dispose();
    }

    /**
     * Returns an object from the pool for the specified code. If the object was retained as a {@linkplain Reference
     * weak reference}, the {@link Reference#get referent} is returned.
     *
     * @todo Consider logging a message here to the finer or finest level.
     */
    private Object get(final Object key) {
        assert Thread.holdsLock(this);
        Object object = pool.get(key);
        if (object instanceof Reference) {
            object = ((Reference<?>) object).get();
        }
        return object;
    }

    /**
     * Put an element in the pool. This method is invoked everytime a {@code createFoo(...)} method is invoked, even if
     * an object was already in the pool for the given code, for the following reasons: 1) Replaces weak reference by
     * strong reference (if applicable) and 2) Alters the linked hash set order, so that this object is declared as the
     * last one used.
     */
    private void put(final Object key, final Object object) {
        assert Thread.holdsLock(this);
        pool.put(key, object);
        int toReplace = pool.size() - maxStrongReferences;
        if (toReplace > 0) {
            for (final Iterator<Map.Entry<Object, Object>> it = pool.entrySet().iterator(); it.hasNext(); ) {
                final Map.Entry<Object, Object> entry = it.next();
                final Object value = entry.getValue();
                if (value instanceof Reference) {
                    if (((Reference) value).get() == null) {
                        it.remove();
                    }
                    continue;
                }
                entry.setValue(new WeakReference<>(value));
                if (--toReplace == 0) {
                    break;
                }
            }
        }
    }
}
