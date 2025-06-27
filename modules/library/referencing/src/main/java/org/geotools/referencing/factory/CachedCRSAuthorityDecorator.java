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

// J2SE dependencies and extensions

import java.util.Set;
import org.geotools.api.metadata.citation.Citation;
import org.geotools.api.referencing.AuthorityFactory;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.IdentifiedObject;
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
import org.geotools.api.referencing.datum.DatumAuthorityFactory;
import org.geotools.api.referencing.operation.CoordinateOperationAuthorityFactory;
import org.geotools.api.util.InternationalString;
import org.geotools.util.ObjectCache;
import org.geotools.util.ObjectCaches;
import org.geotools.util.factory.BufferedFactory;
import org.geotools.util.factory.FactoryRegistryException;
import org.geotools.util.factory.GeoTools;
import org.geotools.util.factory.Hints;

/**
 * An authority factory that caches all objects created by the delegate CRSAuthorityFactory. The behaviour of the
 * {@code createFoo(String)} methods first looks if a previously created object exists for the given code. If such an
 * object exists, it is returned directly. The testing of the cache is synchronized and may block if the referencing
 * object is under construction.
 *
 * <p>If the object is not yet created, the definition is delegated to the appropriate the {@linkplain an
 * AuthorityFactory authority factory} and the result is cached for next time.
 *
 * <p>This object is responsible for owning a {{ReferencingObjectCache}}; there are several implementations to choose
 * from on construction.
 *
 * @since 2.4
 * @version $Id: BufferedAuthorityDecorator.java 26038 2007-06-27 01:58:12Z jgarnett $
 * @author Jody Garnett
 */
public final class CachedCRSAuthorityDecorator extends AbstractAuthorityFactory
        implements AuthorityFactory, CRSAuthorityFactory, BufferedFactory {

    /** Cache to be used for referencing objects. */
    ObjectCache<Object, Object> cache;

    /** The delegate authority for coordinate reference systems. */
    private CRSAuthorityFactory crsAuthority;

    /** The delegate authority for searching. */
    private AbstractAuthorityFactory delegate;

    /**
     * Constructs an instance wrapping the specified factory with a default cache.
     *
     * <p>The provided authority factory must implement {@link DatumAuthorityFactory}, {@link CSAuthorityFactory},
     * {@link CRSAuthorityFactory} and {@link CoordinateOperationAuthorityFactory} .
     *
     * @param factory The factory to cache. Can not be {@code null}.
     */
    public CachedCRSAuthorityDecorator(final CRSAuthorityFactory factory) {
        this(factory, createCache(GeoTools.getDefaultHints()));
    }

    /**
     * Constructs an instance wrapping the specified factory. The {@code maxStrongReferences} argument specify the
     * maximum number of objects to keep by strong reference. If a greater amount of objects are created, then the
     * strong references for the oldest ones are replaced by weak references.
     *
     * <p>This constructor is protected because subclasses must declare which of the {@link DatumAuthorityFactory},
     * {@link CSAuthorityFactory}, {@link CRSAuthorityFactory} {@link SearchableAuthorityFactory} and
     * {@link CoordinateOperationAuthorityFactory} interfaces they choose to implement.
     *
     * @param factory The factory to cache. Can not be {@code null}.
     * @param cache The underlying cache
     */
    CachedCRSAuthorityDecorator(CRSAuthorityFactory factory, ObjectCache<Object, Object> cache) {
        super(((ReferencingFactory) factory).getPriority()); // TODO
        this.cache = cache;
        crsAuthority = factory;
        this.delegate = (AbstractAuthorityFactory) factory;
    }

    /** Utility method used to produce cache based on hint */
    static <K, V> ObjectCache<K, V> createCache(final Hints hints) throws FactoryRegistryException {
        return ObjectCaches.create(hints);
    }

    //
    // Utility Methods and Cache Care and Feeding
    //
    String toKey(String code) {
        return ObjectCaches.toKey(getAuthority(), code);
    }

    //
    // AuthorityFactory
    //
    @Override
    public IdentifiedObject createObject(String code) throws FactoryException {
        final String key = toKey(code);
        IdentifiedObject obj = (IdentifiedObject) cache.get(key);
        if (obj == null) {
            try {
                cache.writeLock(key);
                obj = (IdentifiedObject) cache.peek(key);
                if (obj == null) {
                    obj = crsAuthority.createObject(code);
                    cache.put(key, obj);
                }
            } finally {
                cache.writeUnLock(key);
            }
        }
        return obj;
    }

    @Override
    public Citation getAuthority() {
        return crsAuthority.getAuthority();
    }

    @Override
    public Set<String> getAuthorityCodes(Class<? extends IdentifiedObject> type) throws FactoryException {
        return crsAuthority.getAuthorityCodes(type);
    }

    @Override
    public InternationalString getDescriptionText(String code) throws FactoryException {
        return crsAuthority.getDescriptionText(code);
    }

    //
    // CRSAuthority
    //
    @Override
    public synchronized CompoundCRS createCompoundCRS(final String code) throws FactoryException {
        final String key = toKey(code);
        CompoundCRS crs = (CompoundCRS) cache.get(key);
        if (crs == null) {
            try {
                cache.writeLock(key);
                crs = (CompoundCRS) cache.peek(key);
                if (crs == null) {
                    crs = crsAuthority.createCompoundCRS(code);
                    cache.put(key, crs);
                }
            } finally {
                cache.writeUnLock(key);
            }
        }
        return crs;
    }

    @Override
    public CoordinateReferenceSystem createCoordinateReferenceSystem(String code) throws FactoryException {
        final String key = toKey(code);
        CoordinateReferenceSystem crs = (CoordinateReferenceSystem) cache.get(key);
        if (crs == null) {
            try {
                cache.writeLock(key);
                crs = (CoordinateReferenceSystem) cache.peek(key);
                if (crs == null) {
                    crs = crsAuthority.createCoordinateReferenceSystem(code);
                    cache.put(key, crs);
                }
            } finally {
                cache.writeUnLock(key);
            }
        }
        return crs;
    }

    @Override
    public DerivedCRS createDerivedCRS(String code) throws FactoryException {
        final String key = toKey(code);
        DerivedCRS crs = (DerivedCRS) cache.get(key);
        if (crs == null) {
            try {
                cache.writeLock(key);
                crs = (DerivedCRS) cache.peek(key);
                if (crs == null) {
                    crs = crsAuthority.createDerivedCRS(code);
                    cache.put(key, crs);
                }
            } finally {
                cache.writeUnLock(key);
            }
        }
        return crs;
    }

    @Override
    public EngineeringCRS createEngineeringCRS(String code) throws FactoryException {
        final String key = toKey(code);
        EngineeringCRS crs = (EngineeringCRS) cache.get(key);
        if (crs == null) {
            try {
                cache.writeLock(key);
                crs = (EngineeringCRS) cache.peek(key);
                if (crs == null) {
                    crs = crsAuthority.createEngineeringCRS(code);
                    cache.put(key, crs);
                }
            } finally {
                cache.writeUnLock(key);
            }
        }
        return crs;
    }

    @Override
    public GeocentricCRS createGeocentricCRS(String code) throws FactoryException {
        final String key = toKey(code);
        GeocentricCRS crs = (GeocentricCRS) cache.get(key);
        if (crs == null) {
            try {
                cache.writeLock(key);
                crs = (GeocentricCRS) cache.peek(key);
                if (crs == null) {
                    crs = crsAuthority.createGeocentricCRS(code);
                    cache.put(key, crs);
                }
            } finally {
                cache.writeUnLock(key);
            }
        }
        return crs;
    }

    @Override
    public GeographicCRS createGeographicCRS(String code) throws FactoryException {
        final String key = toKey(code);
        GeographicCRS crs = (GeographicCRS) cache.get(key);
        if (crs == null) {
            try {
                cache.writeLock(key);
                crs = (GeographicCRS) cache.peek(key);
                if (crs == null) {
                    crs = crsAuthority.createGeographicCRS(code);
                    cache.put(key, crs);
                }
            } finally {
                cache.writeUnLock(key);
            }
        }
        return crs;
    }

    @Override
    public ImageCRS createImageCRS(String code) throws FactoryException {
        final String key = toKey(code);
        ImageCRS crs = (ImageCRS) cache.get(key);
        if (crs == null) {
            try {
                cache.writeLock(key);
                crs = (ImageCRS) cache.peek(key);
                if (crs == null) {
                    crs = crsAuthority.createImageCRS(code);
                    cache.put(key, crs);
                }
            } finally {
                cache.writeUnLock(key);
            }
        }
        return crs;
    }

    @Override
    public ProjectedCRS createProjectedCRS(String code) throws FactoryException {
        final String key = toKey(code);
        ProjectedCRS crs = (ProjectedCRS) cache.get(key);
        if (crs == null) {
            try {
                cache.writeLock(key);
                crs = (ProjectedCRS) cache.peek(key);
                if (crs == null) {
                    crs = crsAuthority.createProjectedCRS(code);
                    cache.put(key, crs);
                }
            } finally {
                cache.writeUnLock(key);
            }
        }
        return crs;
    }

    @Override
    public TemporalCRS createTemporalCRS(String code) throws FactoryException {
        final String key = toKey(code);
        TemporalCRS crs = (TemporalCRS) cache.get(key);
        if (crs == null) {
            try {
                cache.writeLock(key);
                crs = (TemporalCRS) cache.peek(key);
                if (crs == null) {
                    crs = crsAuthority.createTemporalCRS(code);
                    cache.put(key, crs);
                }
            } finally {
                cache.writeUnLock(key);
            }
        }
        return crs;
    }

    @Override
    public VerticalCRS createVerticalCRS(String code) throws FactoryException {
        final String key = toKey(code);
        VerticalCRS crs = (VerticalCRS) cache.get(key);
        if (crs == null) {
            try {
                cache.writeLock(key);
                crs = (VerticalCRS) cache.peek(key);
                if (crs == null) {
                    crs = crsAuthority.createVerticalCRS(code);
                    cache.put(key, crs);
                }
            } finally {
                cache.writeUnLock(key);
            }
        }
        return crs;
    }

    //
    // AbstractAuthorityFactory
    //
    @Override
    public void dispose() throws FactoryException {
        delegate.dispose();
        cache.clear();
        cache = null;
        delegate = null;
    }

    @Override
    public String getBackingStoreDescription() throws FactoryException {
        return delegate.getBackingStoreDescription();
    }

    /**
     * Returns a finder which can be used for looking up unidentified objects. The default implementation delegates
     * lookup to the underlying backing store and caches the result.
     *
     * @since 2.4
     */
    @Override
    public synchronized IdentifiedObjectFinder getIdentifiedObjectFinder(final Class<? extends IdentifiedObject> type)
            throws FactoryException {
        return new Finder(delegate.getIdentifiedObjectFinder(type), ObjectCaches.create("weak", 250));
    }

    /**
     * An implementation of {@link IdentifiedObjectFinder} which delegates the work to the underlying backing store and
     * caches the result.
     *
     * <p>A separate ObjectCache, findCache, is used to store the values created over the course of finding. The
     * findCache is set up as a "chain" allowing it to use our cache to prevent duplication of effort. In the future
     * this findCache may be shared between instances.
     *
     * <p><b>Implementation note:</b> we will create objects using directly the underlying backing store, not using the
     * cache. This is because hundred of objects may be created during a scan while only one will be typically retained.
     * We don't want to overload the cache with every false candidates that we encounter during the scan.
     */
    private static final class Finder extends IdentifiedObjectFinder.Adapter {
        /** Cache used when finding */
        private ObjectCache<Object, Object> findCache;

        /** Creates a finder for the underlying backing store. */
        Finder(final IdentifiedObjectFinder finder, ObjectCache<Object, Object> tempCache) {
            super(finder);
            this.findCache = tempCache;
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
            IdentifiedObject candidate = (IdentifiedObject) findCache.get(object);

            if (candidate == null) {
                // Must delegates to 'finder' (not to 'super') in order to take
                // advantage of the method overriden by AllAuthoritiesFactory.
                IdentifiedObject found = finder.find(object);
                if (found != null) {
                    try {
                        findCache.writeLock(object);
                        candidate = (IdentifiedObject) findCache.peek(object);
                        if (candidate == null) {
                            findCache.put(object, found);
                            return found;
                        }

                    } finally {
                        findCache.writeLock(object);
                    }
                }
            }
            return candidate;
        }

        /** Returns the identifier for the specified object. */
        @Override
        public String findIdentifier(final IdentifiedObject object) throws FactoryException {
            IdentifiedObject candidate = (IdentifiedObject) findCache.get(object);
            if (candidate != null) {
                return getIdentifier(candidate);
            }
            // We don't rely on super-class implementation, because we want to
            // take advantage of the method overriden by AllAuthoritiesFactory.
            return finder.findIdentifier(object);
        }
    }
}
