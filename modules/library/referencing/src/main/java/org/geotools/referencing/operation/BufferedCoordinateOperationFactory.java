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
package org.geotools.referencing.operation;

import java.util.Map;

import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.CoordinateOperation;
import org.opengis.referencing.operation.CoordinateOperationFactory;
import org.opengis.referencing.operation.OperationMethod;
import org.opengis.referencing.operation.OperationNotFoundException;

import org.geotools.factory.Hints;
import org.geotools.factory.BufferedFactory;
import org.geotools.util.Utilities;
import org.geotools.util.SoftValueHashMap;
import org.geotools.referencing.ReferencingFactoryFinder;


/**
 * Caches the {@linkplain CoordinateOperation coordinate operations} created by an other factory.
 * Those coordinate operations may be expensive to create. During rendering and during data I/O,
 * some implementations make use a lof of coordinate transformations, hence caching them might
 * help.
 * <p>
 * In most cases, users should not need to create an instance of this class explicitly. An instance
 * of {@code BufferedCoordinateOperationFactory} should be automatically registered and returned
 * by {@link ReferencingFactoryFinder} in default Geotools configuration.
 *
 * @since 2.3
 * @version $Id$
 * @source $URL$
 * @author Simone Giannecchini
 * @author Martin Desruisseaux
 */
public class BufferedCoordinateOperationFactory extends AbstractCoordinateOperationFactory
        implements BufferedFactory
{
    /**
     * The priority level for this factory.
     */
    static final int PRIORITY = AuthorityBackedFactory.PRIORITY + 10;

    /**
     * Helper class used in order to build an hashing for a pair of source-destination
     * {@link CoordinateReferenceSystem} objects. This is used to cache the transformations
     * that are pretty time-consuming to build each time.
     */
    private static final class CRSPair {
        /**
         * The hash code value, computed once for ever at construction time.
         */
        private final int hash;

        /**
         * The source and target CRS.
         */
        private final CoordinateReferenceSystem sourceCRS, targetCRS;

        /**
         * Creates a {@code CRSPair} for the specified source and target CRS.
         */
        public CRSPair(final CoordinateReferenceSystem sourceCRS,
                       final CoordinateReferenceSystem targetCRS)
        {
            this.sourceCRS = sourceCRS;
            this.targetCRS = targetCRS;
            this.hash = (37 * sourceCRS.hashCode()) + targetCRS.hashCode();
        }

        /**
         * Returns the hash code value.
         */
        @Override
        public int hashCode() {
            return hash;
        }

        /**
         * Compares this pair to the specified object for equality.
         * <p>
         * <strong>Note:</strong> we perform the CRS comparaison using strict equality, not using
         * {@code equalsIgnoreMetadata}, because metadata matter since they are attributes of the
         * {@link CoordinateOperation} object to be created.
         */
        @Override
        public boolean equals(final Object object) {
            if (object == this) {
                return true;
            }
            if (object instanceof CRSPair) {
                final CRSPair that = (CRSPair) object;
                return Utilities.equals(this.sourceCRS, that.sourceCRS) &&
                       Utilities.equals(this.targetCRS, that.targetCRS);
            }
            return false;
        }
    }

    /**
     * The wrapped factory. If {@code null}, will be fetched when first needed.
     * We should not initialize this field using {@link ReferencingFactoryFinder} from the
     * no-argument constructor, since this constructor is typically invoked while
     * {@link ReferencingFactoryFinder} is still iterating over the registered implementations.
     */
    private CoordinateOperationFactory factory;

    /**
     * The pool of cached transformations. This map can not be static, because the values may
     * be different for the same ({@code sourceCRS}, {@code targetCRS}) pair dependending of
     * hint values like {@link Hints#LENIENT_DATUM_SHIFT}.
     */
    private final Map<CRSPair, CoordinateOperation> pool =
            new SoftValueHashMap<CRSPair, CoordinateOperation>();

    /**
     * Creates a buffered factory wrapping the {@linkplain AuthorityBackedFactory default one}.
     */
    public BufferedCoordinateOperationFactory() {
        super(null, PRIORITY);
        /*
         * Do not use FactoryFinder here (directly or indirectly through the call
         * to an other constructor), because this constructor is typically invoked
         * while FactoryFinder is iterating over registered implementations. We
         * left the 'factory' field uninitialized and will initialize it when first
         * needed.
         */
    }

    /**
     * Creates a buffered factory wrapping an other factory selected according the specified hints.
     *
     * @param userHints The hints to use for choosing a backing factory.
     */
    public BufferedCoordinateOperationFactory(final Hints userHints) {
        this(userHints, PRIORITY);
    }

    /**
     * Creates a buffered factory wrapping an other factory selected according the specified hints.
     *
     * @param userHints The hints to use for choosing a backing factory.
     * @param priority The priority for this factory, as a number between
     *        {@link #MINIMUM_PRIORITY MINIMUM_PRIORITY} and
     *        {@link #MAXIMUM_PRIORITY MAXIMUM_PRIORITY} inclusive.
     */
    public BufferedCoordinateOperationFactory(final Hints userHints, final int priority) {
        this(getBackingFactory(userHints), userHints, priority);
    }

    /**
     * Wraps the specified factory.
     *
     * @param factory  The factory to wrap.
     * @param priority The priority for this factory, as a number between
     *        {@link #MINIMUM_PRIORITY MINIMUM_PRIORITY} and
     *        {@link #MAXIMUM_PRIORITY MAXIMUM_PRIORITY} inclusive.
     */
    public BufferedCoordinateOperationFactory(final CoordinateOperationFactory factory,
                                              final int priority)
    {
        this(factory, null, priority);
    }

    /**
     * Work around for RFE #4093999 in Sun's bug database
     * ("Relax constraint on placement of this()/super() call in constructors").
     */
    private BufferedCoordinateOperationFactory(final CoordinateOperationFactory factory,
                                               final Hints userHints, final int priority)
    {
        super(factory, userHints, priority);
        this.factory = factory;
        ensureNonNull("factory", factory);
    }

    /**
     * Returns a backing factory from the specified hints.
     */
    private static CoordinateOperationFactory getBackingFactory(final Hints hints) {
        for (final CoordinateOperationFactory candidate : ReferencingFactoryFinder.getCoordinateOperationFactories(hints)) {
            if (!(candidate instanceof BufferedCoordinateOperationFactory)) {
                return candidate;
            }
        }
        // The following is likely to thrown a FactoryNotFoundException,
        // which is the intended behavior.
        return ReferencingFactoryFinder.getCoordinateOperationFactory(hints);
    }

    /**
     * Returns the backing factory. Coordinate operation creation will be delegated to this
     * factory when not available in the cache.
     */
    private final CoordinateOperationFactory getBackingFactory() {
        assert Thread.holdsLock(hints); // Same lock than the one used by getImplementationHints().
        if (factory == null) {
            factory = getBackingFactory(null);
        }
        return factory;
    }

    /**
     * Invoked by {@link #AbstractCoordinateOperationFactory} when the {@link #hints} map should
     * be initialized. The {@link Hints#COORDINATE_OPERATION_FACTORY} can not always be provided
     * at construction time, because the backing factory may be lazily created.
     */
    @Override
    void initializeHints() {
        super.initializeHints();
        hints.put(Hints.COORDINATE_OPERATION_FACTORY, getBackingFactory());
    }

    /**
     * Returns an operation for conversion or transformation between two coordinate reference
     * systems. If an operation was already created and still in the cache, the cached operation
     * is returned. Otherwise the operation creation is delegated to the
     * {@linkplain CoordinateOperationFactory coordinate operation factory} specified at
     * construction time and the result is cached.
     *
     * @param  sourceCRS Input coordinate reference system.
     * @param  targetCRS Output coordinate reference system.
     * @return A coordinate operation from {@code sourceCRS} to {@code targetCRS}.
     * @throws OperationNotFoundException if no operation path was found from {@code sourceCRS}
     *         to {@code targetCRS}.
     * @throws FactoryException if the operation creation failed for some other reason.
     */
    public CoordinateOperation createOperation(final CoordinateReferenceSystem sourceCRS,
                                               final CoordinateReferenceSystem targetCRS)
            throws OperationNotFoundException, FactoryException
    {
        ensureNonNull("sourceCRS", sourceCRS);
        ensureNonNull("targetCRS", targetCRS);
        final CRSPair key = new CRSPair(sourceCRS, targetCRS);
        CoordinateOperation op;
        synchronized (hints) { // This lock is indirectly required by getBackingFactory().
            op = pool.get(key);
            if (op == null) {
                op = getBackingFactory().createOperation(sourceCRS, targetCRS);
                pool.put(key, op);
            }
        }
        return op;
    }

    /**
     * Returns an operation for conversion or transformation between two coordinate reference
     * systems using the specified method. The current implementation delegates to the
     * {@linkplain CoordinateOperationFactory coordinate operation factory} specified at
     * construction time with no caching.
     *
     * @deprecated Will be removed.
     */
    public CoordinateOperation createOperation(final CoordinateReferenceSystem sourceCRS,
                                               final CoordinateReferenceSystem targetCRS,
                                               final OperationMethod method)
            throws OperationNotFoundException, FactoryException
    {
        synchronized (hints) { // This lock is indirectly required by getBackingFactory().
            return getBackingFactory().createOperation(sourceCRS, targetCRS, method);
        }
    }
}
