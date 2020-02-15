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
import java.util.logging.Level;
import javax.measure.Unit;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.ObjectPoolFactory;
import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool.Config;
import org.apache.commons.pool.impl.GenericObjectPoolFactory;
import org.geotools.util.ObjectCache;
import org.geotools.util.ObjectCaches;
import org.geotools.util.factory.BufferedFactory;
import org.geotools.util.factory.Hints;
import org.opengis.metadata.citation.Citation;
import org.opengis.referencing.AuthorityFactory;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.IdentifiedObject;
import org.opengis.referencing.crs.CRSAuthorityFactory;
import org.opengis.referencing.crs.CompoundCRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.DerivedCRS;
import org.opengis.referencing.crs.GeocentricCRS;
import org.opengis.referencing.crs.GeographicCRS;
import org.opengis.referencing.crs.ImageCRS;
import org.opengis.referencing.crs.ProjectedCRS;
import org.opengis.referencing.crs.TemporalCRS;
import org.opengis.referencing.crs.VerticalCRS;
import org.opengis.referencing.cs.CSAuthorityFactory;
import org.opengis.referencing.cs.CartesianCS;
import org.opengis.referencing.cs.CoordinateSystem;
import org.opengis.referencing.cs.CoordinateSystemAxis;
import org.opengis.referencing.cs.CylindricalCS;
import org.opengis.referencing.cs.EllipsoidalCS;
import org.opengis.referencing.cs.PolarCS;
import org.opengis.referencing.cs.SphericalCS;
import org.opengis.referencing.cs.TimeCS;
import org.opengis.referencing.cs.VerticalCS;
import org.opengis.referencing.datum.Datum;
import org.opengis.referencing.datum.DatumAuthorityFactory;
import org.opengis.referencing.datum.Ellipsoid;
import org.opengis.referencing.datum.EngineeringDatum;
import org.opengis.referencing.datum.GeodeticDatum;
import org.opengis.referencing.datum.ImageDatum;
import org.opengis.referencing.datum.PrimeMeridian;
import org.opengis.referencing.datum.TemporalDatum;
import org.opengis.referencing.datum.VerticalDatum;
import org.opengis.referencing.operation.CoordinateOperation;
import org.opengis.referencing.operation.CoordinateOperationAuthorityFactory;
import org.opengis.util.InternationalString;

/**
 * An authority mediator that consults (a possibily shared) cache before delegating the generation
 * of the content to a "worker" authority factory. The behaviour of the {@code createFoo(String)}
 * methods first looks if a previously created object exists for the given code. If such an object
 * exists, it is returned directly. The testing of the cache is synchronized and may block if the
 * referencing object is under construction.
 *
 * <p>If the object is not yet created, the definition is delegated to the appropriate {@code
 * createFoo} method of the factory, which will cache the result for next time.
 *
 * <p>This object is responsible for maintaining an {{ObjectCache}} of "workers" based on the
 * following:
 *
 * <ul>
 *   <li>Hints.AUTHORITY_MAX_ACTIVE (default 2) - indicates the maximum number of worker created, if
 *       non positive the number of workers is unbounded.
 *   <li>Hints.
 * </ul>
 *
 * @since 2.4
 * @version $Id$
 * @author Jody Garnett (Refractions Research)
 * @author Cory Horner (Refractions Research)
 */
public abstract class AbstractAuthorityMediator extends AbstractAuthorityFactory
        implements AuthorityFactory,
                CRSAuthorityFactory,
                CSAuthorityFactory,
                DatumAuthorityFactory,
                CoordinateOperationAuthorityFactory,
                BufferedFactory {

    static final int PRIORITY = MAXIMUM_PRIORITY - 10;

    /**
     * Cache to be used for referencing objects defined by this authority. Please note that this
     * cache may be shared!
     *
     * <p>Your cache may grow to considerable size during actual use; in addition to storing
     * CoordinateReferenceSystems (by code); it will also store all the component parts (each under
     * its own code), along with MathTransformations between two CoordinateReferenceSystems. So even
     * if you are only planning on working with 50 CoordianteReferenceSystems please keep in mind
     * that you will need larger cache size in order to prevent a bottleneck.
     */
    ObjectCache cache;

    /**
     * The findCache is used to store search results; often match a "raw" CoordinateReferenceSystem
     * created from WKT (as the key) with a "real" CoordianteReferenceSystem as defined by this
     * authority.
     */
    ObjectCache findCache;

    /**
     * Pool to hold workers which will be used to construct referencing objects which are not
     * present in the cache.
     */
    private ObjectPool workers;

    /**
     * Configuration object for the object pool. The constructor reads its hints and sets the pool
     * configuration in this object;
     */
    Config poolConfig = new Config();

    /** A container of the "real factories" actually used to construct objects. */
    protected final ReferencingFactoryContainer factories;

    /** Constructs an instance making use of the default cache and priority level. */
    protected AbstractAuthorityMediator() {
        this(PRIORITY);
    }

    /** Constructs an instance based on the provided Hints */
    protected AbstractAuthorityMediator(Hints hints) {
        this(PRIORITY, hints);
    }

    /** Constructs an instance making use of the default cache. */
    protected AbstractAuthorityMediator(int priority) {
        this(priority, ObjectCaches.create("weak", 50), ReferencingFactoryContainer.instance(null));
    }

    /** Constructs an instance making use of the default cache. */
    protected AbstractAuthorityMediator(int priority, Hints hints) {
        this(priority, ObjectCaches.create(hints), ReferencingFactoryContainer.instance(hints));
        // configurable behaviour
        poolConfig.minIdle = Hints.AUTHORITY_MIN_IDLE.toValue(hints);
        poolConfig.maxIdle = Hints.AUTHORITY_MAX_IDLE.toValue(hints);
        poolConfig.maxActive = Hints.AUTHORITY_MAX_ACTIVE.toValue(hints);
        poolConfig.minEvictableIdleTimeMillis = Hints.AUTHORITY_MIN_EVICT_IDLETIME.toValue(hints);
        poolConfig.softMinEvictableIdleTimeMillis =
                Hints.AUTHORITY_SOFTMIN_EVICT_IDLETIME.toValue(hints);
        poolConfig.timeBetweenEvictionRunsMillis =
                Hints.AUTHORITY_TIME_BETWEEN_EVICTION_RUNS.toValue(hints);

        // static behaviour
        poolConfig.maxWait = -1; // block indefinitely until a worker is available
        poolConfig.whenExhaustedAction = GenericObjectPool.WHEN_EXHAUSTED_BLOCK;
    }

    /**
     * Constructs an instance making use of the indicated cache.
     *
     * <p>This constructor is protected because subclasses must declare which of the {@link
     * DatumAuthorityFactory}, {@link CSAuthorityFactory}, {@link CRSAuthorityFactory} and {@link
     * CoordinateOperationAuthorityFactory} interfaces they choose to implement.
     *
     * @param cache The cache to use
     */
    protected AbstractAuthorityMediator(
            int priority, ObjectCache cache, ReferencingFactoryContainer container) {
        super(priority);
        this.factories = container;
        this.cache = cache;
        this.findCache = ObjectCaches.chain(ObjectCaches.create("weak", 0), cache);
    }

    protected void completeHints() {
        hints.put(Hints.DATUM_AUTHORITY_FACTORY, this);
        hints.put(Hints.CS_AUTHORITY_FACTORY, this);
        hints.put(Hints.CRS_AUTHORITY_FACTORY, this);
        hints.put(Hints.COORDINATE_OPERATION_AUTHORITY_FACTORY, this);
    }

    /** True if this mediator is currently connected to one or more workers. */
    public boolean isConnected() {
        return (workers.getNumActive() + workers.getNumIdle()) > 0;
    }

    ObjectPool getPool() {
        if (workers == null) {
            // create pool
            PoolableObjectFactory objectFactory = new AuthorityPoolableObjectFactory();
            ObjectPoolFactory poolFactory = new GenericObjectPoolFactory(objectFactory, poolConfig);
            this.setPool(poolFactory.createPool());
        }
        return workers;
    }

    void setPool(ObjectPool pool) {
        this.workers = pool;
    }

    //
    // Utility Methods and Cache Care and Feeding
    //
    protected String toKey(String code) {
        return ObjectCaches.toKey(getAuthority(), code);
    }

    /**
     * Trims the authority scope, if present. For example if this factory is an EPSG authority
     * factory and the specified code start with the "EPSG:" prefix, then the prefix is removed.
     * Otherwise, the string is returned unchanged (except for leading and trailing spaces).
     *
     * @param code The code to trim.
     * @return The code without the authority scope.
     */
    protected String trimAuthority(String code) {
        return toKey(code);
    }

    /** The authority body of the objects this factory provides. */
    public abstract Citation getAuthority();

    public Set getAuthorityCodes(Class type) throws FactoryException {
        Set codes = (Set) cache.get(type);
        if (codes == null) {
            try {
                cache.writeLock(type);
                codes = (Set) cache.peek(type);
                if (codes == null) {
                    AbstractCachedAuthorityFactory worker = null;
                    try {
                        worker = (AbstractCachedAuthorityFactory) getPool().borrowObject();
                        codes = worker.getAuthorityCodes(type);
                        cache.put(type, codes);
                    } catch (FactoryException e) {
                        throw e;
                    } catch (Exception e) {
                        throw new FactoryException(e);
                    } finally {
                        try {
                            getPool().returnObject(worker);
                        } catch (Exception e) {
                            LOGGER.log(Level.WARNING, "Unable to return worker " + e, e);
                        }
                    }
                }
            } finally {
                cache.writeUnLock(type);
            }
        }
        return codes;
    }

    public abstract InternationalString getDescriptionText(String code) throws FactoryException;

    public IdentifiedObject createObject(String code) throws FactoryException {
        final String key = toKey(code);
        IdentifiedObject obj = (IdentifiedObject) cache.get(key);
        if (obj == null) {
            try {
                cache.writeLock(key);
                obj = (IdentifiedObject) cache.peek(key);
                if (obj == null) {
                    AbstractCachedAuthorityFactory worker = null;
                    try {
                        worker = (AbstractCachedAuthorityFactory) getPool().borrowObject();
                        obj = worker.createDerivedCRS(code);
                        cache.put(key, obj);
                    } catch (FactoryException e) {
                        throw e;
                    } catch (Exception e) {
                        throw new FactoryException(e);
                    } finally {
                        try {
                            getPool().returnObject(worker);
                        } catch (Exception e) {
                            LOGGER.log(Level.WARNING, "Unable to return worker " + e, e);
                        }
                    }
                }
            } finally {
                cache.writeUnLock(key);
            }
        }
        return obj;
    }

    //
    // CRSAuthority
    //
    public synchronized CompoundCRS createCompoundCRS(final String code) throws FactoryException {
        final String key = toKey(code);
        CompoundCRS crs = (CompoundCRS) cache.get(key);
        if (crs == null) {
            try {
                cache.writeLock(key);
                crs = (CompoundCRS) cache.peek(key);
                if (crs == null) {
                    AbstractCachedAuthorityFactory worker = null;
                    try {
                        worker = (AbstractCachedAuthorityFactory) getPool().borrowObject();
                        crs = worker.createCompoundCRS(code);
                        cache.put(key, crs);
                    } catch (FactoryException e) {
                        throw e;
                    } catch (Exception e) {
                        throw new FactoryException(e);
                    } finally {
                        try {
                            getPool().returnObject(worker);
                        } catch (Exception e) {
                            LOGGER.log(Level.WARNING, "Unable to return worker " + e, e);
                        }
                    }
                }
            } finally {
                cache.writeUnLock(key);
            }
        }
        return crs;
    }

    public CoordinateReferenceSystem createCoordinateReferenceSystem(String code)
            throws FactoryException {
        final String key = toKey(code);
        return createWith(
                key,
                new WorkerSafeRunnable() {
                    public Object run(AbstractCachedAuthorityFactory worker)
                            throws FactoryException {
                        return worker.createCoordinateReferenceSystem(key);
                    }
                });
    }

    public DerivedCRS createDerivedCRS(String code) throws FactoryException {
        final String key = toKey(code);
        return createWith(
                key,
                new WorkerSafeRunnable() {
                    public Object run(AbstractCachedAuthorityFactory worker)
                            throws FactoryException {
                        return worker.createEngineeringCRS(key);
                    }
                });
    }

    public GeocentricCRS createGeocentricCRS(String code) throws FactoryException {
        final String key = toKey(code);
        return createWith(
                key,
                new WorkerSafeRunnable() {
                    public Object run(AbstractCachedAuthorityFactory worker)
                            throws FactoryException {
                        return worker.createGeocentricCRS(key);
                    }
                });
    }

    public GeographicCRS createGeographicCRS(String code) throws FactoryException {
        final String key = toKey(code);
        return createWith(
                key,
                new WorkerSafeRunnable() {
                    public Object run(AbstractCachedAuthorityFactory worker)
                            throws FactoryException {
                        return worker.createGeographicCRS(key);
                    }
                });
    }

    public ImageCRS createImageCRS(String code) throws FactoryException {
        final String key = toKey(code);
        return createWith(
                key,
                new WorkerSafeRunnable() {
                    public Object run(AbstractCachedAuthorityFactory worker)
                            throws FactoryException {
                        return worker.createImageCRS(key);
                    }
                });
    }

    public ProjectedCRS createProjectedCRS(String code) throws FactoryException {
        final String key = toKey(code);
        return createWith(
                key,
                new WorkerSafeRunnable() {
                    public Object run(AbstractCachedAuthorityFactory worker)
                            throws FactoryException {
                        return worker.createProjectedCRS(key);
                    }
                });
    }

    public TemporalCRS createTemporalCRS(String code) throws FactoryException {
        final String key = toKey(code);
        return createWith(
                key,
                new WorkerSafeRunnable() {
                    public Object run(AbstractCachedAuthorityFactory worker)
                            throws FactoryException {
                        return worker.createTemporalCRS(key);
                    }
                });
    }

    public VerticalCRS createVerticalCRS(String code) throws FactoryException {
        final String key = toKey(code);
        return createWith(
                key,
                new WorkerSafeRunnable() {
                    public Object run(AbstractCachedAuthorityFactory worker)
                            throws FactoryException {
                        return worker.createVerticalCRS(key);
                    }
                });
    }

    //
    // CSAuthority
    //
    public CartesianCS createCartesianCS(String code) throws FactoryException {
        final String key = toKey(code);
        return createWith(
                key,
                new WorkerSafeRunnable() {
                    public Object run(AbstractCachedAuthorityFactory worker)
                            throws FactoryException {
                        return worker.createCartesianCS(key);
                    }
                });
    }

    public CoordinateSystem createCoordinateSystem(String code) throws FactoryException {
        final String key = toKey(code);
        return createWith(
                key,
                new WorkerSafeRunnable() {
                    public Object run(AbstractCachedAuthorityFactory worker)
                            throws FactoryException {
                        return worker.createCoordinateSystem(key);
                    }
                });
    }

    // sample implemenation with get/test
    public CoordinateSystemAxis createCoordinateSystemAxis(String code) throws FactoryException {
        final String key = toKey(code);
        return createWith(
                key,
                new WorkerSafeRunnable() {
                    public Object run(AbstractCachedAuthorityFactory worker)
                            throws FactoryException {
                        return worker.createCoordinateSystemAxis(key);
                    }
                });
    }

    public CylindricalCS createCylindricalCS(String code) throws FactoryException {
        final String key = toKey(code);
        return createWith(
                key,
                new WorkerSafeRunnable() {
                    public Object run(AbstractCachedAuthorityFactory worker)
                            throws FactoryException {
                        return worker.createCylindricalCS(key);
                    }
                });
    }

    public EllipsoidalCS createEllipsoidalCS(String code) throws FactoryException {
        final String key = toKey(code);
        return createWith(
                key,
                new WorkerSafeRunnable() {
                    public Object run(AbstractCachedAuthorityFactory worker)
                            throws FactoryException {
                        return worker.createEllipsoidalCS(key);
                    }
                });
    }

    public PolarCS createPolarCS(String code) throws FactoryException {
        final String key = toKey(code);
        return createWith(
                key,
                new WorkerSafeRunnable() {
                    public Object run(AbstractCachedAuthorityFactory worker)
                            throws FactoryException {
                        return worker.createPolarCS(key);
                    }
                });
    }

    public SphericalCS createSphericalCS(String code) throws FactoryException {
        final String key = toKey(code);
        return createWith(
                key,
                new WorkerSafeRunnable() {
                    public Object run(AbstractCachedAuthorityFactory worker)
                            throws FactoryException {
                        return worker.createSphericalCS(key);
                    }
                });
    }

    public TimeCS createTimeCS(String code) throws FactoryException {
        final String key = toKey(code);
        return createWith(
                key,
                new WorkerSafeRunnable() {
                    public Object run(AbstractCachedAuthorityFactory worker)
                            throws FactoryException {
                        return worker.createTimeCS(key);
                    }
                });
    }

    public Unit<?> createUnit(String code) throws FactoryException {
        final String key = toKey(code);
        return createWith(
                key,
                new WorkerSafeRunnable() {
                    public Object run(AbstractCachedAuthorityFactory worker)
                            throws FactoryException {
                        return worker.createUnit(key);
                    }
                });
    }

    public VerticalCS createVerticalCS(String code) throws FactoryException {
        final String key = toKey(code);
        return createWith(
                key,
                new WorkerSafeRunnable() {
                    public Object run(AbstractCachedAuthorityFactory worker)
                            throws FactoryException {
                        return worker.createVerticalCS(key);
                    }
                });
    }

    //
    // DatumAuthorityFactory
    //
    public Datum createDatum(String code) throws FactoryException {
        final String key = toKey(code);
        return createWith(
                key,
                new WorkerSafeRunnable() {
                    public Object run(AbstractCachedAuthorityFactory worker)
                            throws FactoryException {
                        return worker.createDatum(key);
                    }
                });
    }

    public Ellipsoid createEllipsoid(String code) throws FactoryException {
        final String key = toKey(code);
        return createWith(
                key,
                new WorkerSafeRunnable() {
                    public Object run(AbstractCachedAuthorityFactory worker)
                            throws FactoryException {
                        return worker.createEllipsoid(key);
                    }
                });
    }

    public EngineeringDatum createEngineeringDatum(String code) throws FactoryException {
        final String key = toKey(code);
        return createWith(
                key,
                new WorkerSafeRunnable() {
                    public Object run(AbstractCachedAuthorityFactory worker)
                            throws FactoryException {
                        return worker.createEngineeringDatum(key);
                    }
                });
    }

    public GeodeticDatum createGeodeticDatum(String code) throws FactoryException {
        final String key = toKey(code);
        return createWith(
                key,
                new WorkerSafeRunnable() {
                    public Object run(AbstractCachedAuthorityFactory worker)
                            throws FactoryException {
                        return worker.createGeodeticDatum(key);
                    }
                });
    }

    public ImageDatum createImageDatum(String code) throws FactoryException {
        final String key = toKey(code);
        return createWith(
                key,
                new WorkerSafeRunnable() {
                    public Object run(AbstractCachedAuthorityFactory worker)
                            throws FactoryException {
                        return worker.createImageDatum(key);
                    }
                });
    }

    public PrimeMeridian createPrimeMeridian(String code) throws FactoryException {
        final String key = toKey(code);
        return createWith(
                key,
                new WorkerSafeRunnable() {
                    public Object run(AbstractCachedAuthorityFactory worker)
                            throws FactoryException {
                        return worker.createPrimeMeridian(key);
                    }
                });
    }

    public TemporalDatum createTemporalDatum(String code) throws FactoryException {
        final String key = toKey(code);
        return createWith(
                key,
                new WorkerSafeRunnable() {
                    public Object run(AbstractCachedAuthorityFactory worker)
                            throws FactoryException {
                        return worker.createTemporalDatum(key);
                    }
                });
    }

    public VerticalDatum createVerticalDatum(String code) throws FactoryException {
        final String key = toKey(code);
        return createWith(
                key,
                new WorkerSafeRunnable() {
                    public Object run(AbstractCachedAuthorityFactory worker)
                            throws FactoryException {
                        return worker.createVerticalDatum(key);
                    }
                });
    }

    public CoordinateOperation createCoordinateOperation(String code) throws FactoryException {
        final String key = toKey(code);
        return createWith(
                key,
                new WorkerSafeRunnable() {
                    public Object run(AbstractCachedAuthorityFactory worker)
                            throws FactoryException {
                        return worker.createCoordinateOperation(key);
                    }
                });
    }

    public synchronized Set /* <CoordinateOperation> */ createFromCoordinateReferenceSystemCodes(
            final String sourceCode, final String targetCode) throws FactoryException {

        final Object key = ObjectCaches.toKey(getAuthority(), sourceCode, targetCode);
        return createWith(
                key,
                new WorkerSafeRunnable() {
                    public Object run(AbstractCachedAuthorityFactory worker)
                            throws FactoryException {
                        return worker.createFromCoordinateReferenceSystemCodes(
                                sourceCode, targetCode);
                    }
                });
    }

    /**
     * This method is used to cut down the amount of try/catch/finally code needed when working with
     * the cache and workers.
     *
     * <p>This code brings together two try/catch/finally blocks.
     *
     * <p>For cache management:
     *
     * <pre><code>
     *  T value = (T) cache.get(key);
     *  if (value == null) {
     *      try {
     *          cache.writeLock(key);
     *          value = (T) cache.peek(key);
     *          if (value == null) {
     *          	....generate value....
     *              cache.put( key, value );
     *          }
     *      } finally {
     *          cache.writeUnLock(key);
     *      }
     *  }
     * </code></pre>
     *
     * And worker management when generating values:
     *
     * <pre><code>
     * AbstractCachedAuthorityFactory worker = null;
     * try {
     *  worker = (AbstractCachedAuthorityFactory) getPool().borrowObject();
     *  value = (T) runner.run( worker );
     * } catch (FactoryException e) {
     *     throw e;
     * } catch (Exception e) {
     *     throw new FactoryException(e);
     * } finally {
     *     try {
     *         getPool().returnObject(worker);
     *     } catch (Exception e) {
     *         LOGGER.log(Level.WARNING, "Unable to return worker " + e, e);
     *     }
     * }
     * </code></pre>
     *
     * @param key Used to look in the cache
     * @param runner Used to generate a value in the case of a cache miss
     * @return value from either the cache or generated
     */
    protected <T> T createWith(Object key, WorkerSafeRunnable runner) throws FactoryException {
        T value = (T) cache.get(key);
        if (value == null) {
            try {
                cache.writeLock(key);
                value = (T) cache.peek(key);
                if (value == null) {
                    AbstractCachedAuthorityFactory worker = null;
                    try {
                        worker = (AbstractCachedAuthorityFactory) getPool().borrowObject();
                        value = (T) runner.run(worker);
                    } catch (FactoryException e) {
                        throw e;
                    } catch (Exception e) {
                        throw new FactoryException(e);
                    } finally {
                        try {
                            getPool().returnObject(worker);
                        } catch (Exception e) {
                            LOGGER.log(Level.WARNING, "Unable to return worker " + e, e);
                        }
                    }
                    cache.put(key, value);
                }
            } finally {
                cache.writeUnLock(key);
            }
        }
        return value;
    }
    /**
     * An interface describing a portion of work for which a worker is needed.
     *
     * <p>The worker is borrowed from the pool
     */
    protected abstract class WorkerSafeRunnable {
        public abstract Object run(AbstractCachedAuthorityFactory worker) throws FactoryException;
    }

    public String getBackingStoreDescription() throws FactoryException {
        AbstractCachedAuthorityFactory worker = null;
        try {
            worker = (AbstractCachedAuthorityFactory) getPool().borrowObject();
            return worker.getBackingStoreDescription();
        } catch (FactoryException e) {
            throw e;
        } catch (Exception e) {
            throw new FactoryException(e);
        } finally {
            try {
                getPool().returnObject(worker);
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Unable to return worker " + e, e);
            }
        }
    }
    /**
     * Clean up the object pool of workers (since we are shutting down).
     *
     * <p>Subclasses may wish to override this method if they have their own resources to clean up
     * (like a database connection). If you do this please remember to call super.dispose().
     */
    public void dispose() throws FactoryException {
        if (workers != null) {
            try {
                workers.clear();
            } catch (FactoryException e) {
                throw e;
            } catch (Exception e) {
                throw new FactoryException(e);
            }
            workers = null;
        }
    }
    /**
     * Creates the objects, subclasses of AbstractCachedAuthorityFactory, which are held by the
     * ObjectPool. This implementation simply delegates each method to the subclass.
     *
     * @author Cory Horner (Refractions Research)
     */
    private class AuthorityPoolableObjectFactory implements PoolableObjectFactory {

        AuthorityPoolableObjectFactory() {}

        public void activateObject(Object obj) throws Exception {
            AbstractCachedAuthorityFactory worker = (AbstractCachedAuthorityFactory) obj;
            worker.cache = cache;
            activateWorker(worker);
        }

        public void destroyObject(Object obj) throws Exception {
            destroyWorker((AbstractCachedAuthorityFactory) obj);
        }

        public Object makeObject() throws Exception {
            AbstractCachedAuthorityFactory worker = makeWorker();
            return worker;
        }

        public void passivateObject(Object obj) throws Exception {
            passivateWorker((AbstractCachedAuthorityFactory) obj);
        }

        public boolean validateObject(Object obj) {
            return validateWorker((AbstractCachedAuthorityFactory) obj);
        }
    }

    /**
     * Reinitialize an instance to be returned by the pool.
     *
     * <p>Please note that BEFORE this method has been called AbstractAuthorityMediator has already:
     *
     * <ul>
     *   <li>provided the worker with the single shared <code>cache</code>
     *   <li>provided the worker with the single shared <code>findCache</code>
     * </ul>
     */
    protected abstract void activateWorker(AbstractCachedAuthorityFactory worker) throws Exception;

    /** Destroys an instance no longer needed by the pool. */
    protected abstract void destroyWorker(AbstractCachedAuthorityFactory worker) throws Exception;

    /** Creates an instance that can be returned by the pool. */
    protected abstract AbstractCachedAuthorityFactory makeWorker() throws Exception;

    /** Un-initialize an instance to be returned to the pool. */
    protected abstract void passivateWorker(AbstractCachedAuthorityFactory worker) throws Exception;

    /** Ensures that the instance is safe to be returned by the pool. */
    protected abstract boolean validateWorker(AbstractCachedAuthorityFactory worker);

    /**
     * Returns a finder which can be used for looking up unidentified objects.
     *
     * <p>The returned implementation will make use of workers as needed.
     *
     * @param type The type of objects to look for.
     * @return A finder to use for looking up unidentified objects.
     * @throws FactoryException if the finder can not be created.
     * @since 2.4
     */
    public IdentifiedObjectFinder getIdentifiedObjectFinder(
            final Class /* <? extends IdentifiedObject> */ type) throws FactoryException {
        return new LazyCachedFinder(type);
    }
    /**
     * An {@link IdentifiedObjectFinder} which uses a worker when searching.
     *
     * <p>The worker used is configured to store answers in a separate <code>findCache</code>,
     * rather that disrupt the regular <code>cached</code>(which is focused on retaining codes
     * requested by the user application). This is because hundred of objects may be created during
     * a scan while only one will be typically retained. We don't want to overload the cache with
     * every false candidates that we encounter during the scan.
     *
     * <p>Because the worker is configured differently before use we must be careful to return it to
     * its original state before returning back to the <code>workers</code> pool.
     */
    private final class LazyCachedFinder extends IdentifiedObjectFinder {
        private Class type;
        /** Creates a finder for the underlying backing store. */
        LazyCachedFinder(final Class type) {
            super(AbstractAuthorityMediator.this, type);
            this.type = type;
        }

        /**
         * Looks up an object from this authority factory which is equals, ignoring metadata, to the
         * specified object. The default implementation performs the same lookup than the backing
         * store and caches the result.
         */
        @Override
        public IdentifiedObject find(final IdentifiedObject object) throws FactoryException {
            IdentifiedObject candidate;
            candidate = (IdentifiedObject) findCache.get(object);
            if (candidate != null) {
                return candidate;
            }
            try {
                findCache.writeLock(object); // avoid searching for the same object twice
                IdentifiedObject found;
                AbstractCachedAuthorityFactory worker = null;
                try {
                    worker = (AbstractCachedAuthorityFactory) getPool().borrowObject();
                    worker.cache = ObjectCaches.chain(ObjectCaches.create("weak", 3000), cache);
                    worker.findCache = findCache;

                    setProxy(AuthorityFactoryProxy.getInstance(worker, type));

                    found = super.find(object);
                } catch (Exception e) {
                    throw new FactoryException(e);
                } finally {
                    setProxy(null);
                    if (worker != null) {
                        worker.cache = cache;
                        worker.findCache = findCache;
                        try {
                            getPool().returnObject(worker);
                        } catch (Exception e) {
                            LOGGER.log(Level.WARNING, "Unable to return worker " + e, e);
                        }
                    }
                }
                if (found == null) {
                    return null; // not found
                }
                candidate = (IdentifiedObject) findCache.peek(object);
                if (candidate == null) {
                    findCache.put(object, found);
                    return found;
                } else {
                    return candidate;
                }
            } finally {
                findCache.writeUnLock(object);
            }
        }

        protected Citation getAuthority() {
            return AbstractAuthorityMediator.this.getAuthority();
        }
        /** Returns the identifier for the specified object. */
        @Override
        public String findIdentifier(final IdentifiedObject object) throws FactoryException {
            IdentifiedObject candidate;
            candidate = (IdentifiedObject) findCache.get(object);
            if (candidate != null) {
                return getIdentifier(candidate);
            }
            return super.findIdentifier(object);
        }
    }
}
