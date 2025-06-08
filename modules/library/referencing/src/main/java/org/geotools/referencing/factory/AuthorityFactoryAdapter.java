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
 */
package org.geotools.referencing.factory;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
import org.geotools.api.referencing.operation.CoordinateOperationFactory;
import org.geotools.api.referencing.operation.OperationMethod;
import org.geotools.api.util.InternationalString;
import org.geotools.metadata.i18n.ErrorKeys;
import org.geotools.metadata.i18n.LoggingKeys;
import org.geotools.metadata.i18n.Loggings;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.CRS;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.geotools.util.factory.AbstractFactory;
import org.geotools.util.factory.Factory;
import org.geotools.util.factory.FactoryRegistryException;
import org.geotools.util.factory.Hints;
import org.geotools.util.factory.OptionalFactory;

/**
 * An authority factory which delegates {@linkplain CoordinateReferenceSystem CRS}, {@linkplain CoordinateSystem CS} or
 * {@linkplain Datum datum} objects creation to some other factory implementations.
 *
 * <p>All constructors are protected because this class must be subclassed in order to determine which of the
 * {@link DatumAuthorityFactory}, {@link CSAuthorityFactory} and {@link CRSAuthorityFactory} interfaces to implement.
 *
 * @since 2.2
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public class AuthorityFactoryAdapter extends AbstractAuthorityFactory implements OptionalFactory {
    /** List of hint keys related to authority factories. */
    private static final Hints.Key[] TYPES = {
        Hints.CRS_AUTHORITY_FACTORY,
        Hints.CS_AUTHORITY_FACTORY,
        Hints.DATUM_AUTHORITY_FACTORY,
        Hints.COORDINATE_OPERATION_AUTHORITY_FACTORY
    };

    /** The underlying {@linkplain Datum datum} authority factory, or {@code null} if none. */
    final DatumAuthorityFactory datumFactory;

    /** The underlying {@linkplain CoordinateSystem coordinate system} authority factory, or {@code null} if none. */
    final CSAuthorityFactory csFactory;

    /**
     * The underlying {@linkplain CoordinateReferenceSystem coordinate reference system} authority factory, or
     * {@code null} if none.
     */
    final CRSAuthorityFactory crsFactory;

    /**
     * The underlying {@linkplain CoordinateOperation coordinate operation} authority factory, or {@code null} if none.
     */
    final CoordinateOperationAuthorityFactory operationFactory;

    /**
     * A set of low-level factories to be used if none were found in {@link #datumFactory}, {@link #csFactory},
     * {@link #crsFactory} or {@link #operationFactory}. Will be created only when first needed.
     *
     * @see #getFactoryContainer
     */
    private transient ReferencingFactoryContainer factories;

    /**
     * Creates a wrapper around no factory. This constructor should never be used except by subclasses overriding the
     * <code>get</code><var>Foo</var><code>AuthorityFactory</code> methods.
     *
     * @param priority The priority for this factory, as a number between {@link #MINIMUM_PRIORITY MINIMUM_PRIORITY} and
     *     {@link #MAXIMUM_PRIORITY MAXIMUM_PRIORITY} inclusive.
     */
    AuthorityFactoryAdapter(final int priority) {
        super(priority);
        datumFactory = null;
        csFactory = null;
        crsFactory = null;
        operationFactory = null;
    }

    /**
     * Creates a wrapper around the specified factory. The {@link #priority priority} field will be set to the same
     * value than the specified factory. Subclasses should override the {@link #getPriority() getPriority()} method if
     * they want to set a higher or lower priority for this instance.
     *
     * @param factory The factory to wrap.
     */
    protected AuthorityFactoryAdapter(final AuthorityFactory factory) {
        this(factory, null);
    }

    /** For {@link FallbackAuthorityFactory} constructor only. */
    AuthorityFactoryAdapter(final AuthorityFactory factory, final AuthorityFactory fallback) {
        this(
                factory instanceof CRSAuthorityFactory
                        ? (CRSAuthorityFactory) factory
                        : fallback instanceof CRSAuthorityFactory ? (CRSAuthorityFactory) fallback : null,
                factory instanceof CSAuthorityFactory
                        ? (CSAuthorityFactory) factory
                        : fallback instanceof CSAuthorityFactory ? (CSAuthorityFactory) fallback : null,
                factory instanceof DatumAuthorityFactory
                        ? (DatumAuthorityFactory) factory
                        : fallback instanceof DatumAuthorityFactory ? (DatumAuthorityFactory) fallback : null,
                factory instanceof CoordinateOperationAuthorityFactory
                        ? (CoordinateOperationAuthorityFactory) factory
                        : fallback instanceof CoordinateOperationAuthorityFactory
                                ? (CoordinateOperationAuthorityFactory) fallback
                                : null);
    }

    /**
     * Creates a wrapper around the specified factories. The {@link #priority priority} field will be set to the highest
     * priority found in the specified factories. Subclasses should override the {@link #getPriority() getPriority()}
     * method if they want to set a higher or lower priority for this instance.
     *
     * @param crsFactory The {@linkplain CoordinateReferenceSystem coordinate reference system} authority factory, or
     *     {@code null}.
     * @param csFactory The {@linkplain CoordinateSystem coordinate system} authority factory, or {@code null}.
     * @param datumFactory The {@linkplain Datum datum} authority factory, or {@code null}.
     * @param opFactory The {@linkplain CoordinateOperation coordinate operation} authority factory, or {@code null}.
     */
    protected AuthorityFactoryAdapter(
            final CRSAuthorityFactory crsFactory,
            final CSAuthorityFactory csFactory,
            final DatumAuthorityFactory datumFactory,
            final CoordinateOperationAuthorityFactory opFactory) {
        super(Math.max(
                getPriority(datumFactory),
                Math.max(getPriority(csFactory), Math.max(getPriority(crsFactory), getPriority(opFactory)))));

        if (this instanceof CRSAuthorityFactory) {
            ensureNonNull("crsFactory", crsFactory);
        }
        if (this instanceof CSAuthorityFactory) {
            ensureNonNull("csFactory", csFactory);
        }
        if (this instanceof DatumAuthorityFactory) {
            ensureNonNull("datumFactory", datumFactory);
        }
        if (this instanceof CoordinateOperationAuthorityFactory) {
            ensureNonNull("opFactory", opFactory);
        }
        store(Hints.DATUM_AUTHORITY_FACTORY, this.datumFactory = datumFactory);
        store(Hints.CS_AUTHORITY_FACTORY, this.csFactory = csFactory);
        store(Hints.CRS_AUTHORITY_FACTORY, this.crsFactory = crsFactory);
        store(Hints.COORDINATE_OPERATION_AUTHORITY_FACTORY, this.operationFactory = opFactory);
    }

    /** Returns the priority of the specified factory, or {@link #NORMAL_PRIORITY} if unknown. */
    private static int getPriority(final AuthorityFactory factory) {
        return factory instanceof AbstractFactory ? ((AbstractFactory) factory).getPriority() : NORMAL_PRIORITY;
    }

    /** Adds the specified factory to the set of hints, if non null. */
    private void store(final Hints.Key key, final AuthorityFactory factory) {
        if (factory != null) {
            if (hints.put(key, factory) != null) {
                // Should never happen since 'hints' should be initially empty.
                throw new AssertionError(key);
            }
        }
    }

    /**
     * Creates a wrappers around the default factories for the specified authority. The factories are fetched using
     * {@link ReferencingFactoryFinder}.
     *
     * @param authority The authority to wraps (example: {@code "EPSG"}). If {@code null}, then all authority factories
     *     must be explicitly specified in the set of hints.
     * @param userHints An optional set of hints, or {@code null} if none.
     * @throws FactoryRegistryException if at least one factory can not be obtained.
     * @since 2.4
     */
    protected AuthorityFactoryAdapter(final String authority, final Hints userHints) throws FactoryRegistryException {
        this(
                ReferencingFactoryFinder.getCRSAuthorityFactory(
                        authority, trim(userHints, Hints.CRS_AUTHORITY_FACTORY)),
                ReferencingFactoryFinder.getCSAuthorityFactory(authority, trim(userHints, Hints.CS_AUTHORITY_FACTORY)),
                ReferencingFactoryFinder.getDatumAuthorityFactory(
                        authority, trim(userHints, Hints.DATUM_AUTHORITY_FACTORY)),
                ReferencingFactoryFinder.getCoordinateOperationAuthorityFactory(
                        authority, trim(userHints, Hints.COORDINATE_OPERATION_AUTHORITY_FACTORY)));
    }

    /**
     * Removes every {@code *_AUTHORITY_FACTORY} hints except the specified one. The removal, if needed, is performed in
     * a copy of the supplied hints in order to keep user's map unmodified.
     *
     * <p>This removal is performed because {@code *_AUTHORITY_FACTORY} hints are typically supplied to the above
     * constructor in order to initialize the {@link #crsFactory}, {@link #csFactory}, <cite>etc.</cite> fields. But
     * because the same map of hints is used for every call to
     * {@code ReferencingFactoryFinder.getFooAuthorityFactory(...)}, if we don't perform this removal, then the
     * {@code CRS_AUTHORITY_FACTORY} hint is taken in account for fetching other factories like
     * {@link CSAuthorityFactory}. We may think that it is not a problem since CS authority factory should not care
     * about {@code CRS_AUTHORITY_FACTORY} hint. But... our EPSG authority factory implements both
     * {@link CRSAuthorityFactory} and {@link CSAuthorityFactory} interfaces, so our {@link CSAuthorityFactory}
     * implementation do have CRS-related hints.
     *
     * <p>Conclusion: if we do not remove those hints, it typically leads to failure to find a CS authority factory
     * using this specific CRS authority factory. We may argue that this is a Geotools design problem. Maybe... this is
     * not a trivial issue. So we are better to not document that in public API for now.
     *
     * @param userHints The user hints to trim. This map will never be modified.
     * @param keep The hint to <strong>not</strong> remove.
     * @return A copy of {@code userHints} without the authority hints, or {@code userHints} if no change were required.
     */
    private static Hints trim(final Hints userHints, final Hints.Key keep) {
        Hints reduced = userHints;
        if (userHints != null) {
            for (final Hints.Key key : TYPES) {
                if (!keep.equals(key)) {
                    if (reduced == userHints) {
                        if (!userHints.containsKey(key)) {
                            continue;
                        }
                        // Copies the map only if we need to modify it.
                        reduced = new Hints(userHints);
                    }
                    reduced.remove(key);
                }
            }
        }
        return reduced;
    }

    /** Returns the {@linkplain #hints hints} extented will all hints specified in dependencies. */
    private Hints hints() {
        final Hints extended = new Hints(hints);
        if (operationFactory != null) addAll(operationFactory, extended);
        if (datumFactory != null) addAll(datumFactory, extended);
        if (csFactory != null) addAll(csFactory, extended);
        if (crsFactory != null) addAll(crsFactory, extended);
        extended.putAll(hints); // Gives precedence to the hints from this class.
        return extended;
    }

    /** Adds all hints from the specified factory into the specified set of hints. */
    private static void addAll(final AuthorityFactory factory, final Hints hints) {
        if (factory instanceof Factory) {
            hints.putAll(((Factory) factory).getImplementationHints());
        }
    }

    /**
     * Returns the direct dependencies. The returned list contains the backing store specified at construction time, or
     * the exception if the backing store can't be obtained.
     */
    @Override
    Collection<? super AuthorityFactory> dependencies() {
        final List<Object> dep = new ArrayList<>(4);
        Object factory;
        try {
            factory = getAuthorityFactory(null);
        } catch (FactoryException e) {
            factory = e;
        }
        dep.add(factory);
        return dep;
    }

    /**
     * If this factory is a wrapper for the specified factory that do not add any additional
     * {@linkplain #getAuthorityCodes authority codes}, returns {@code true}. This method is for
     * {@link FallbackAuthorityFactory} internal use only and should not be public. We expect only a simple check, so we
     * don't invoke the {@code getFooAuthorityFactory(...)} methods.
     */
    @Override
    boolean sameAuthorityCodes(final AuthorityFactory factory) {
        if (!isCodeMethodOverriden()) {
            /*
             * Tests wrapped factories only if the 'toBackingFactoryCode(String)' method is not
             * overwritten, otherwise we can't assume that the authority codes are the same. The
             * impact on the main subclasses are usually as below:
             *
             *     URN_AuthorityFactory           - excluded
             *     HTTP_AuthorityFactory          - excluded
             *     HTTP_URI_AuthorityFactory      - excluded
             *     OrderedAxisAuthorityFactory    - make the test below
             *     FallbackAuthorityFactory       - make the test below
             *
             * Note: in the particular case of FallbackAuthorityFactory, we test the
             *       primary factory only, not the fallback. This behavior matches the
             *       FallbackAuthorityFactory.create(boolean,int,Iterator) need, which
             *       will process this case in a special way.
             */
            if (sameAuthorityCodes(crsFactory, factory)
                    && sameAuthorityCodes(csFactory, factory)
                    && sameAuthorityCodes(datumFactory, factory)
                    && sameAuthorityCodes(operationFactory, factory)) {
                return true;
            }
        }
        return super.sameAuthorityCodes(factory);
    }

    /**
     * Helper methods for {@link #sameAuthorityCodes(AuthorityFactory)} and
     * {@link FallbackAuthorityFactory#create(boolean,int,Iterator)} implementations. If there is no backing store,
     * returns {@code true} in order to take in account only the backing stores that are assigned. This behavior match
     * the need of the above-cited implementations.
     */
    static boolean sameAuthorityCodes(final AuthorityFactory backingStore, final AuthorityFactory factory) {
        if (backingStore instanceof AbstractAuthorityFactory) {
            if (((AbstractAuthorityFactory) backingStore).sameAuthorityCodes(factory)) {
                return true;
            }
        }
        return factory == backingStore || backingStore == null;
    }

    /**
     * Returns {@code true} if this factory is ready for use. This default implementation checks the availability of
     * CRS, CS, datum and operation authority factories specified at construction time.
     *
     * @return {@code true} if this factory is ready for use.
     */
    @Override
    public boolean isAvailable() {
        return isAvailable(crsFactory)
                && isAvailable(csFactory)
                && isAvailable(datumFactory)
                && isAvailable(operationFactory);
    }

    /** Checks the availability of the specified factory. */
    private static boolean isAvailable(final AuthorityFactory factory) {
        return !(factory instanceof OptionalFactory) || ((OptionalFactory) factory).isAvailable();
    }

    /**
     * Replaces the specified unit, if applicable. To be overridden with {@code protected} access by
     * {@link TransformedAuthorityFactory}.
     */
    Unit<?> replace(Unit<?> units) throws FactoryException {
        return units;
    }

    /**
     * Replaces (if needed) the specified axis by a new one. To be overridden with {@code protected} access by
     * {@link TransformedAuthorityFactory}.
     */
    CoordinateSystemAxis replace(CoordinateSystemAxis axis) throws FactoryException {
        return axis;
    }

    /**
     * Replaces (if needed) the specified coordinate system by a new one. To be overridden with {@code protected} access
     * by {@link TransformedAuthorityFactory}.
     */
    CoordinateSystem replace(CoordinateSystem cs) throws FactoryException {
        return cs;
    }

    /**
     * Replaces (if needed) the specified datum by a new one. To be overridden with {@code protected} access by
     * {@link TransformedAuthorityFactory}.
     */
    Datum replace(Datum datum) throws FactoryException {
        return datum;
    }

    /**
     * Replaces (if needed) the specified coordinate reference system. To be overridden with {@code protected} access by
     * {@link TransformedAuthorityFactory}.
     */
    CoordinateReferenceSystem replace(CoordinateReferenceSystem crs) throws FactoryException {
        return crs;
    }

    /**
     * Replaces (if needed) the specified coordinate operation. To be overridden with {@code protected} access by
     * {@link TransformedAuthorityFactory}.
     */
    CoordinateOperation replace(CoordinateOperation operation) throws FactoryException {
        return operation;
    }

    /** Delegates the work to an appropriate {@code replace} method for the given object. */
    private IdentifiedObject replaceObject(final IdentifiedObject object) throws FactoryException {
        if (object instanceof CoordinateReferenceSystem) {
            return replace((CoordinateReferenceSystem) object);
        }
        if (object instanceof CoordinateSystem) {
            return replace((CoordinateSystem) object);
        }
        if (object instanceof CoordinateSystemAxis) {
            return replace((CoordinateSystemAxis) object);
        }
        if (object instanceof Datum) {
            return replace((Datum) object);
        }
        if (object instanceof CoordinateOperation) {
            return replace((CoordinateOperation) object);
        }
        return object;
    }

    /**
     * Returns one of the underlying factories as an instance of the GeoTools implementation. If there is none of them,
     * then returns {@code null} or throws an exception if {@code caller} is not null.
     */
    private AbstractAuthorityFactory getGeotoolsFactory(final String caller, final String code)
            throws FactoryException {
        final AuthorityFactory candidate = getAuthorityFactory(code);
        if (candidate instanceof AbstractAuthorityFactory) {
            return (AbstractAuthorityFactory) candidate;
        }
        if (caller == null) {
            return null;
        }
        throw new FactoryException(MessageFormat.format(ErrorKeys.GEOTOOLS_EXTENSION_REQUIRED_$1, caller));
    }

    /**
     * Returns a description of the underlying backing store, or {@code null} if unknow.
     *
     * @throws FactoryException if a failure occured while fetching the engine description.
     */
    @Override
    public String getBackingStoreDescription() throws FactoryException {
        final AbstractAuthorityFactory factory = getGeotoolsFactory(null, null);
        return factory != null ? factory.getBackingStoreDescription() : null;
    }

    /** Returns the vendor responsible for creating this factory implementation. */
    @Override
    public Citation getVendor() {
        return getAuthorityFactory().getVendor();
    }

    /** Returns the organization or party responsible for definition and maintenance of the database. */
    @Override
    public Citation getAuthority() {
        return getAuthorityFactory().getAuthority();
    }

    /**
     * Returns the set of authority code for the specified type.
     *
     * @todo We should returns the union of authority codes from all underlying factories.
     */
    @Override
    public Set<String> getAuthorityCodes(final Class<? extends IdentifiedObject> type) throws FactoryException {
        return getAuthorityFactory(null).getAuthorityCodes(type);
    }

    /** Returns a description for the object identified by the specified code. */
    @Override
    public InternationalString getDescriptionText(final String code) throws FactoryException {
        return getAuthorityFactory(code).getDescriptionText(toBackingFactoryCode(code));
    }

    /**
     * Returns an arbitrary object from a code.
     *
     * @throws FactoryException if the object creation failed.
     * @see #createCoordinateReferenceSystem
     * @see #createDatum
     * @see #createEllipsoid
     * @see #createUnit
     */
    @Override
    public IdentifiedObject createObject(final String code) throws FactoryException {
        return replaceObject(getAuthorityFactory(code).createObject(toBackingFactoryCode(code)));
    }

    /**
     * Returns an arbitrary {@linkplain Datum datum} from a code.
     *
     * @throws FactoryException if the object creation failed.
     * @see #createGeodeticDatum
     * @see #createVerticalDatum
     * @see #createTemporalDatum
     */
    @Override
    public Datum createDatum(final String code) throws FactoryException {
        return replace(getDatumAuthorityFactory(code).createDatum(toBackingFactoryCode(code)));
    }

    /**
     * Creates a {@linkplain EngineeringDatum engineering datum} from a code.
     *
     * @throws FactoryException if the object creation failed.
     * @see #createEngineeringCRS
     */
    @Override
    public EngineeringDatum createEngineeringDatum(final String code) throws FactoryException {
        return (EngineeringDatum)
                replace(getDatumAuthorityFactory(code).createEngineeringDatum(toBackingFactoryCode(code)));
    }

    /**
     * Creates a {@linkplain ImageDatum image datum} from a code.
     *
     * @throws FactoryException if the object creation failed.
     * @see #createImageCRS
     */
    @Override
    public ImageDatum createImageDatum(final String code) throws FactoryException {
        return (ImageDatum) replace(getDatumAuthorityFactory(code).createImageDatum(toBackingFactoryCode(code)));
    }

    /**
     * Creates a {@linkplain VerticalDatum vertical datum} from a code.
     *
     * @throws FactoryException if the object creation failed.
     * @see #createVerticalCRS
     */
    @Override
    public VerticalDatum createVerticalDatum(final String code) throws FactoryException {
        return (VerticalDatum) replace(getDatumAuthorityFactory(code).createVerticalDatum(toBackingFactoryCode(code)));
    }

    /**
     * Creates a {@linkplain TemporalDatum temporal datum} from a code.
     *
     * @throws FactoryException if the object creation failed.
     * @see #createTemporalCRS
     */
    @Override
    public TemporalDatum createTemporalDatum(final String code) throws FactoryException {
        return (TemporalDatum) replace(getDatumAuthorityFactory(code).createTemporalDatum(toBackingFactoryCode(code)));
    }

    /**
     * Returns a {@linkplain GeodeticDatum geodetic datum} from a code.
     *
     * @throws FactoryException if the object creation failed.
     * @see #createEllipsoid
     * @see #createPrimeMeridian
     * @see #createGeographicCRS
     * @see #createProjectedCRS
     */
    @Override
    public GeodeticDatum createGeodeticDatum(final String code) throws FactoryException {
        return (GeodeticDatum) replace(getDatumAuthorityFactory(code).createGeodeticDatum(toBackingFactoryCode(code)));
    }

    /**
     * Returns an {@linkplain Ellipsoid ellipsoid} from a code.
     *
     * @throws FactoryException if the object creation failed.
     * @see #createGeodeticDatum
     */
    @Override
    public Ellipsoid createEllipsoid(final String code) throws FactoryException {
        return getDatumAuthorityFactory(code).createEllipsoid(toBackingFactoryCode(code));
    }

    /**
     * Returns a {@linkplain PrimeMeridian prime meridian} from a code.
     *
     * @throws FactoryException if the object creation failed.
     * @see #createGeodeticDatum
     */
    @Override
    public PrimeMeridian createPrimeMeridian(final String code) throws FactoryException {
        return getDatumAuthorityFactory(code).createPrimeMeridian(toBackingFactoryCode(code));
    }

    /**
     * Returns a {@linkplain Extent extent} (usually an area of validity) from a code.
     *
     * @throws FactoryException if the object creation failed.
     */
    @Override
    public Extent createExtent(final String code) throws FactoryException {
        return getGeotoolsFactory("createExtent", code).createExtent(toBackingFactoryCode(code));
    }

    /**
     * Returns an arbitrary {@linkplain CoordinateSystem coordinate system} from a code.
     *
     * @throws FactoryException if the object creation failed.
     */
    @Override
    public CoordinateSystem createCoordinateSystem(final String code) throws FactoryException {
        return replace(getCSAuthorityFactory(code).createCoordinateSystem(toBackingFactoryCode(code)));
    }

    /**
     * Creates a cartesian coordinate system from a code.
     *
     * @throws FactoryException if the object creation failed.
     */
    @Override
    public CartesianCS createCartesianCS(final String code) throws FactoryException {
        return (CartesianCS) replace(getCSAuthorityFactory(code).createCartesianCS(toBackingFactoryCode(code)));
    }

    /**
     * Creates a polar coordinate system from a code.
     *
     * @throws FactoryException if the object creation failed.
     */
    @Override
    public PolarCS createPolarCS(final String code) throws FactoryException {
        return (PolarCS) replace(getCSAuthorityFactory(code).createPolarCS(toBackingFactoryCode(code)));
    }

    /**
     * Creates a cylindrical coordinate system from a code.
     *
     * @throws FactoryException if the object creation failed.
     */
    @Override
    public CylindricalCS createCylindricalCS(final String code) throws FactoryException {
        return (CylindricalCS) replace(getCSAuthorityFactory(code).createCylindricalCS(toBackingFactoryCode(code)));
    }

    /**
     * Creates a spherical coordinate system from a code.
     *
     * @throws FactoryException if the object creation failed.
     */
    @Override
    public SphericalCS createSphericalCS(final String code) throws FactoryException {
        return (SphericalCS) replace(getCSAuthorityFactory(code).createSphericalCS(toBackingFactoryCode(code)));
    }

    /**
     * Creates an ellipsoidal coordinate system from a code.
     *
     * @throws FactoryException if the object creation failed.
     */
    @Override
    public EllipsoidalCS createEllipsoidalCS(final String code) throws FactoryException {
        return (EllipsoidalCS) replace(getCSAuthorityFactory(code).createEllipsoidalCS(toBackingFactoryCode(code)));
    }

    /**
     * Creates a vertical coordinate system from a code.
     *
     * @throws FactoryException if the object creation failed.
     */
    @Override
    public VerticalCS createVerticalCS(final String code) throws FactoryException {
        return (VerticalCS) replace(getCSAuthorityFactory(code).createVerticalCS(toBackingFactoryCode(code)));
    }

    /**
     * Creates a temporal coordinate system from a code.
     *
     * @throws FactoryException if the object creation failed.
     */
    @Override
    public TimeCS createTimeCS(final String code) throws FactoryException {
        return (TimeCS) replace(getCSAuthorityFactory(code).createTimeCS(toBackingFactoryCode(code)));
    }

    /**
     * Returns a {@linkplain CoordinateSystemAxis coordinate system axis} from a code.
     *
     * @throws FactoryException if the object creation failed.
     */
    @Override
    public CoordinateSystemAxis createCoordinateSystemAxis(final String code) throws FactoryException {
        return replace(getCSAuthorityFactory(code).createCoordinateSystemAxis(toBackingFactoryCode(code)));
    }

    /**
     * Returns an {@linkplain Unit unit} from a code.
     *
     * @throws FactoryException if the object creation failed.
     */
    @Override
    public Unit<?> createUnit(final String code) throws FactoryException {
        return replace(getCSAuthorityFactory(code).createUnit(toBackingFactoryCode(code)));
    }

    /**
     * Returns an arbitrary {@linkplain CoordinateReferenceSystem coordinate reference system} from a code.
     *
     * @throws FactoryException if the object creation failed.
     * @see #createGeographicCRS
     * @see #createProjectedCRS
     * @see #createVerticalCRS
     * @see #createTemporalCRS
     * @see #createCompoundCRS
     */
    @Override
    public CoordinateReferenceSystem createCoordinateReferenceSystem(final String code) throws FactoryException {
        final CRSAuthorityFactory factory = getCRSAuthorityFactory(code);
        final CoordinateReferenceSystem crs =
                replace(factory.createCoordinateReferenceSystem(toBackingFactoryCode(code)));
        notifySuccess("createCoordinateReferenceSystem", code, factory, crs);

        return crs;
    }

    /**
     * Creates a 3D coordinate reference system from a code.
     *
     * @throws FactoryException if the object creation failed.
     */
    @Override
    public CompoundCRS createCompoundCRS(final String code) throws FactoryException {
        return (CompoundCRS) replace(getCRSAuthorityFactory(code).createCompoundCRS(toBackingFactoryCode(code)));
    }

    /**
     * Creates a derived coordinate reference system from a code.
     *
     * @throws FactoryException if the object creation failed.
     */
    @Override
    public DerivedCRS createDerivedCRS(final String code) throws FactoryException {
        return (DerivedCRS) replace(getCRSAuthorityFactory(code).createDerivedCRS(toBackingFactoryCode(code)));
    }

    /**
     * Creates a {@linkplain EngineeringCRS engineering coordinate reference system} from a code.
     *
     * @throws FactoryException if the object creation failed.
     */
    @Override
    public EngineeringCRS createEngineeringCRS(final String code) throws FactoryException {
        return (EngineeringCRS) replace(getCRSAuthorityFactory(code).createEngineeringCRS(toBackingFactoryCode(code)));
    }

    /**
     * Returns a {@linkplain GeographicCRS geographic coordinate reference system} from a code.
     *
     * @throws FactoryException if the object creation failed.
     */
    @Override
    public GeographicCRS createGeographicCRS(final String code) throws FactoryException {
        return (GeographicCRS) replace(getCRSAuthorityFactory(code).createGeographicCRS(toBackingFactoryCode(code)));
    }

    /**
     * Returns a {@linkplain GeocentricCRS geocentric coordinate reference system} from a code.
     *
     * @throws FactoryException if the object creation failed.
     */
    @Override
    public GeocentricCRS createGeocentricCRS(final String code) throws FactoryException {
        return (GeocentricCRS) replace(getCRSAuthorityFactory(code).createGeocentricCRS(toBackingFactoryCode(code)));
    }

    /**
     * Creates a {@linkplain ImageCRS image coordinate reference system} from a code.
     *
     * @throws FactoryException if the object creation failed.
     */
    @Override
    public ImageCRS createImageCRS(final String code) throws FactoryException {
        return (ImageCRS) replace(getCRSAuthorityFactory(code).createImageCRS(toBackingFactoryCode(code)));
    }

    /**
     * Returns a {@linkplain ProjectedCRS projected coordinate reference system} from a code.
     *
     * @throws FactoryException if the object creation failed.
     */
    @Override
    public ProjectedCRS createProjectedCRS(final String code) throws FactoryException {
        return (ProjectedCRS) replace(getCRSAuthorityFactory(code).createProjectedCRS(toBackingFactoryCode(code)));
    }

    /**
     * Creates a {@linkplain TemporalCRS temporal coordinate reference system} from a code.
     *
     * @throws FactoryException if the object creation failed.
     */
    @Override
    public TemporalCRS createTemporalCRS(final String code) throws FactoryException {
        return (TemporalCRS) replace(getCRSAuthorityFactory(code).createTemporalCRS(toBackingFactoryCode(code)));
    }

    /**
     * Creates a {@linkplain VerticalCRS vertical coordinate reference system} from a code.
     *
     * @throws FactoryException if the object creation failed.
     */
    @Override
    public VerticalCRS createVerticalCRS(final String code) throws FactoryException {
        return (VerticalCRS) replace(getCRSAuthorityFactory(code).createVerticalCRS(toBackingFactoryCode(code)));
    }

    /**
     * Creates a parameter descriptor from a code.
     *
     * @throws FactoryException if the object creation failed.
     */
    @Override
    public ParameterDescriptor createParameterDescriptor(final String code) throws FactoryException {
        return getGeotoolsFactory("createParameterDescriptor", code)
                .createParameterDescriptor(toBackingFactoryCode(code));
    }

    /**
     * Creates an operation method from a code.
     *
     * @throws FactoryException if the object creation failed.
     */
    @Override
    public OperationMethod createOperationMethod(final String code) throws FactoryException {
        return getGeotoolsFactory("createOperationMethod", code).createOperationMethod(toBackingFactoryCode(code));
    }

    /**
     * Creates an operation from a single operation code.
     *
     * @throws FactoryException if the object creation failed.
     */
    @Override
    public CoordinateOperation createCoordinateOperation(final String code) throws FactoryException {
        return replace(
                getCoordinateOperationAuthorityFactory(code).createCoordinateOperation(toBackingFactoryCode(code)));
    }

    /**
     * Creates an operation from coordinate reference system codes.
     *
     * @throws FactoryException if the object creation failed.
     */
    @Override
    public Set<CoordinateOperation> createFromCoordinateReferenceSystemCodes(
            final String sourceCRS, final String targetCRS) throws FactoryException {
        final CoordinateOperationAuthorityFactory factory = getCoordinateOperationAuthorityFactory(sourceCRS);
        final CoordinateOperationAuthorityFactory check = getCoordinateOperationAuthorityFactory(targetCRS);
        if (factory != check) {
            /*
             * No coordinate operation because of mismatched factories. This is not
             * illegal - the result is an empty set - but it is worth to notify the
             * user since this case has some chances to be an user error.
             */
            final LogRecord record = Loggings.format(
                    Level.WARNING, LoggingKeys.MISMATCHED_COORDINATE_OPERATION_FACTORIES_$2, sourceCRS, targetCRS);
            record.setSourceMethodName("createFromCoordinateReferenceSystemCodes");
            record.setSourceClassName(AuthorityFactoryAdapter.class.getName());
            record.setLoggerName(LOGGER.getName());
            LOGGER.log(record);
            return Collections.emptySet();
        }
        return factory.createFromCoordinateReferenceSystemCodes(
                toBackingFactoryCode(sourceCRS), toBackingFactoryCode(targetCRS));
    }

    /**
     * Returns a finder which can be used for looking up unidentified objects. The default implementation delegates the
     * lookups to the underlying factory.
     *
     * @throws FactoryException if the object creation failed.
     * @since 2.4
     */
    @Override
    public IdentifiedObjectFinder getIdentifiedObjectFinder(Class<? extends IdentifiedObject> type)
            throws FactoryException {
        return new Finder(type);
    }

    /** Log a message when a CRS is found. Child objects that doesn't create their own CRS-objects should not report. */
    protected void notifySuccess(
            final String method,
            final String code,
            final CRSAuthorityFactory factory,
            final CoordinateReferenceSystem crs) {
        if (crs != null && LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine(String.format(
                    "CRS for code:%s found by factory:%s",
                    code, factory.getClass().getName()));
        }
    }

    /**
     * A {@link IdentifiedObjectFinder} which tests {@linkplain AuthorityFactoryAdapter#replaceObject modified objects}
     * in addition of original object.
     */
    class Finder extends IdentifiedObjectFinder.Adapter {
        /** Creates a finder for the underlying backing store. */
        protected Finder(final Class<? extends IdentifiedObject> type) throws FactoryException {
            super(getGeotoolsFactory("getIdentifiedObjectFinder", null).getIdentifiedObjectFinder(type));
        }

        /**
         * Returns {@code candidate}, or an object derived from {@code candidate}, if it is
         * {@linkplain CRS#equalsIgnoreMetadata equals ignoring metadata} to the specified model. Otherwise returns
         * {@code null}.
         *
         * @throws FactoryException if an error occured while creating a derived object.
         */
        @Override
        protected IdentifiedObject deriveEquivalent(final IdentifiedObject candidate, final IdentifiedObject model)
                throws FactoryException {
            final IdentifiedObject modified = replaceObject(candidate);
            if (modified != candidate) {
                if (CRS.equalsIgnoreMetadata(modified, model)) {
                    return modified;
                }
            }
            return super.deriveEquivalent(candidate, model);
        }
    }

    /**
     * Creates an exception for a missing factory. We actually returns an instance of
     * {@link NoSuchAuthorityCodeException} because this kind of exception is treated especially by
     * {@link FallbackAuthorityFactory}.
     */
    private FactoryException missingFactory(final Class category, final String code) {
        return new NoSuchAuthorityCodeException(
                MessageFormat.format(ErrorKeys.FACTORY_NOT_FOUND_$1, category),
                Citations.getIdentifier(getAuthority()),
                trimAuthority(code));
    }

    /**
     * For internal use by {@link #getAuthority} and {@link #getVendor} only. Its only purpose is to catch the
     * {@link FactoryException} for methods that don't allow it. The protected method should be used instead when this
     * exception is allowed.
     */
    private AuthorityFactory getAuthorityFactory() {
        try {
            return getAuthorityFactory(null);
        } catch (FactoryException cause) {
            throw new IllegalStateException(ErrorKeys.UNDEFINED_PROPERTY, cause);
        }
    }

    /**
     * Returns an authority factory of the specified type. This method delegates to:
     *
     * <ul>
     *   <li>{@link #getCRSAuthorityFactory} if {@code type} is {@code CRSAuthorityFactory.class};
     *   <li>{@link #getCSAuthorityFactory} if {@code type} is {@code CSAuthorityFactory.class};
     *   <li>{@link #getDatumAuthorityFactory} if {@code type} is {@code DatumAuthorityFactory.class};
     *   <li>{@link #CoordinateOperationAuthorityFactory} if {@code type} is
     *       {@code CoordinateOperationAuthorityFactory.class};
     * </ul>
     *
     * @throws IllegalArgumentException if the specified {@code type} is invalid.
     * @throws FactoryException if no suitable factory were found.
     */
    <T extends AuthorityFactory> T getAuthorityFactory(final Class<T> type, final String code) throws FactoryException {
        final AuthorityFactory f;
        if (CRSAuthorityFactory.class.equals(type)) {
            f = getCRSAuthorityFactory(code);
        } else if (CSAuthorityFactory.class.equals(type)) {
            f = getCSAuthorityFactory(code);
        } else if (DatumAuthorityFactory.class.equals(type)) {
            f = getDatumAuthorityFactory(code);
        } else if (CoordinateOperationAuthorityFactory.class.equals(type)) {
            f = getCoordinateOperationAuthorityFactory(code);
        } else {
            throw new IllegalArgumentException(MessageFormat.format(ErrorKeys.ILLEGAL_ARGUMENT_$2, "type", type));
        }
        return type.cast(f);
    }

    /**
     * Returns a generic object factory to use for the specified code. The default implementation returns one of the
     * factory specified at construction time. Subclasses can override this method in order to select a different
     * factory implementation depending on the code value.
     *
     * <p><strong>Note:</strong> The value of the {@code code} argument given to this method may be {@code null} when a
     * factory is needed for some global task, like {@link #getAuthorityCodes} method execution.
     *
     * @param code The authority code given to this class. Note that the code to be given to the returned factory
     *     {@linkplain #toBackingFactoryCode may be different}.
     * @return A factory for the specified authority code (never {@code null}).
     * @throws FactoryException if no suitable factory were found.
     * @since 2.4
     */
    protected AuthorityFactory getAuthorityFactory(final String code) throws FactoryException {
        if (crsFactory != null) return crsFactory;
        if (csFactory != null) return csFactory;
        if (datumFactory != null) return datumFactory;
        if (operationFactory != null) return operationFactory;
        throw missingFactory(AuthorityFactory.class, code);
    }

    /**
     * Returns the datum factory to use for the specified code. The default implementation always returns the factory
     * specified at construction time. Subclasses can override this method in order to select a different factory
     * implementation depending on the code value.
     *
     * @param code The authority code given to this class. Note that the code to be given to the returned factory
     *     {@linkplain #toBackingFactoryCode may be different}.
     * @return A factory for the specified authority code (never {@code null}).
     * @throws FactoryException if no datum factory were specified at construction time.
     * @since 2.4
     */
    protected DatumAuthorityFactory getDatumAuthorityFactory(final String code) throws FactoryException {
        if (datumFactory == null) {
            throw missingFactory(DatumAuthorityFactory.class, code);
        }
        return datumFactory;
    }

    /**
     * Returns the coordinate system factory to use for the specified code. The default implementation always returns
     * the factory specified at construction time. Subclasses can override this method in order to select a different
     * factory implementation depending on the code value.
     *
     * @param code The authority code given to this class. Note that the code to be given to the returned factory
     *     {@linkplain #toBackingFactoryCode may be different}.
     * @return A factory for the specified authority code (never {@code null}).
     * @throws FactoryException if no coordinate system factory were specified at construction time.
     * @since 2.4
     */
    protected CSAuthorityFactory getCSAuthorityFactory(final String code) throws FactoryException {
        if (csFactory == null) {
            throw missingFactory(CSAuthorityFactory.class, code);
        }
        return csFactory;
    }

    /**
     * Returns the coordinate reference system factory to use for the specified code. The default implementation always
     * returns the factory specified at construction time. Subclasses can override this method in order to select a
     * different factory implementation depending on the code value.
     *
     * @param code The authority code given to this class. Note that the code to be given to the returned factory
     *     {@linkplain #toBackingFactoryCode may be different}.
     * @return A factory for the specified authority code (never {@code null}).
     * @throws FactoryException if no coordinate reference system factory were specified at construction time.
     * @since 2.4
     */
    protected CRSAuthorityFactory getCRSAuthorityFactory(final String code) throws FactoryException {
        if (crsFactory == null) {
            throw missingFactory(CRSAuthorityFactory.class, code);
        }
        return crsFactory;
    }

    /**
     * Returns the coordinate operation factory to use for the specified code. The default implementation always returns
     * the factory specified at construction time. Subclasses can override this method in order to select a different
     * factory implementation depending on the code value.
     *
     * @param code The authority code given to this class. Note that the code to be given to the returned factory
     *     {@linkplain #toBackingFactoryCode may be different}.
     * @return A factory for the specified authority code (never {@code null}).
     * @throws FactoryException if no coordinate operation factory were specified at construction time.
     * @since 2.4
     */
    protected CoordinateOperationAuthorityFactory getCoordinateOperationAuthorityFactory(final String code)
            throws FactoryException {
        if (operationFactory == null) {
            throw missingFactory(CoordinateOperationAuthorityFactory.class, code);
        }
        return operationFactory;
    }

    /**
     * Returns a coordinate operation factory for this adapter. This method will try to fetch this information from the
     * coordinate operation authority factory, or will returns the default one if no explicit factory were found.
     */
    final CoordinateOperationFactory getCoordinateOperationFactory() throws FactoryException {
        if (operationFactory instanceof Factory) {
            final Factory factory = (Factory) operationFactory;
            final Map hints = factory.getImplementationHints();
            final Object candidate = hints.get(Hints.COORDINATE_OPERATION_FACTORY);
            if (candidate instanceof CoordinateOperationFactory) {
                return (CoordinateOperationFactory) candidate;
            }
        }
        return ReferencingFactoryFinder.getCoordinateOperationFactory(hints());
    }

    /**
     * Suggests a low-level factory group. If {@code crs} is {@code true}, then this method will try to fetch the
     * factory group from the CRS authority factory. Otherwise it will try to fetch the factory group from the CS
     * authority factory. This is used by subclasses like {@link TransformedAuthorityFactory} that need low-level access
     * to factories. Do not change this method into a public one; we would need a better API before to do such thing.
     */
    final ReferencingFactoryContainer getFactoryContainer(final boolean crs) {
        final AuthorityFactory factory;
        if (crs) {
            factory = crsFactory;
        } else {
            factory = csFactory;
        }
        if (factory instanceof DirectAuthorityFactory) {
            return ((DirectAuthorityFactory) factory).factories;
        }
        // No predefined factory group. Create one.
        if (factories == null) {
            factories = ReferencingFactoryContainer.instance(hints());
        }
        return factories;
    }

    /**
     * Returns the code to be given to the wrapped factories. This method is automatically invoked by all {@code create}
     * methods before to forward the code to the {@linkplain #getCRSAuthorityFactory CRS},
     * {@linkplain #getCSAuthorityFactory CS}, {@linkplain #getDatumAuthorityFactory datum} or
     * {@linkplain #operationFactory operation} factory. The default implementation returns the {@code code} unchanged.
     *
     * @param code The code given to this factory.
     * @return The code to give to the underlying factories.
     * @throws FactoryException if the code can't be converted.
     * @since 2.4
     */
    protected String toBackingFactoryCode(final String code) throws FactoryException {
        return code;
    }

    /** Returns {@code true} if the {@link #toBackingFactoryCode} method is overriden. */
    @SuppressWarnings("ReturnValueIgnored")
    final boolean isCodeMethodOverriden() {
        final Class<?>[] arguments = {String.class};
        for (Class<?> type = getClass(); !AuthorityFactoryAdapter.class.equals(type); type = type.getSuperclass()) {
            try {
                type.getDeclaredMethod("toBackingFactoryCode", arguments);
            } catch (NoSuchMethodException e) {
                // The method is not overriden in this class.
                // Checks in the super-class.
                continue;
            } catch (SecurityException e) {
                // We are not allowed to get this information.
                // Conservatively assumes that the method is overriden.
            }
            return true;
        }
        return false;
    }

    @Override
    public void dispose() throws FactoryException {
        super.dispose();
        disposeAbstractAuthorityFactory(datumFactory);
        disposeAbstractAuthorityFactory(csFactory);
        disposeAbstractAuthorityFactory(crsFactory);
        disposeAbstractAuthorityFactory(operationFactory);
    }

    private void disposeAbstractAuthorityFactory(Object factory) throws FactoryException {
        if (factory instanceof AbstractAuthorityFactory) {
            ((AbstractAuthorityFactory) factory).dispose();
        }
    }
}
