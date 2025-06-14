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
import javax.measure.Unit;
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
import org.geotools.api.util.InternationalString;
import org.geotools.util.ObjectCache;
import org.geotools.util.ObjectCaches;
import org.geotools.util.factory.BufferedFactory;
import org.geotools.util.factory.FactoryRegistryException;
import org.geotools.util.factory.GeoTools;
import org.geotools.util.factory.Hints;

/**
 * An authority factory that caches all objects created by delegate factories. This class is set up to cache the full
 * complement of referencing objects: In many cases a single implementation will be used for several the authority
 * factory interfaces - but this is not a requirement. The behaviour of the {@code createFoo(String)} methods first
 * looks if a previously created object exists for the given code. If such an object exists, it is returned directly.
 * The testing of the cache is synchronized and may block if the referencing object is under construction.
 *
 * <p>If the object is not yet created, the definition is delegated to the appropriate the {@linkplain an
 * AuthorityFactory authority factory} and the result is cached for next time.
 *
 * <p>This object is responsible for owning a {{ReferencingObjectCache}}; there are several implementations to choose
 * from on construction.
 *
 * @since 2.4
 * @version $Id$
 * @author Jody Garnett
 */
public final class CachedAuthorityDecorator extends AbstractAuthorityFactory
        implements AuthorityFactory,
                CRSAuthorityFactory,
                CSAuthorityFactory,
                DatumAuthorityFactory,
                CoordinateOperationAuthorityFactory,
                BufferedFactory {

    /** Cache to be used for referencing objects. */
    ObjectCache<Object, Object> cache;

    /** The delegate authority. */
    private AuthorityFactory authority;

    /** The delegate authority for coordinate reference systems. */
    private CRSAuthorityFactory crsAuthority;

    /** The delegate authority for coordinate sytems. */
    private CSAuthorityFactory csAuthority;

    /** The delegate authority for datums. */
    private DatumAuthorityFactory datumAuthority;

    /** The delegate authority for coordinate operations. */
    private CoordinateOperationAuthorityFactory operationAuthority;

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
    public CachedAuthorityDecorator(final AuthorityFactory factory) {
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
     * @param cache The cache to use
     */
    CachedAuthorityDecorator(AuthorityFactory factory, ObjectCache<Object, Object> cache) {
        super(((ReferencingFactory) factory).getPriority()); // TODO
        this.cache = cache;
        authority = factory;
        crsAuthority = (CRSAuthorityFactory) factory;
        csAuthority = (CSAuthorityFactory) factory;
        datumAuthority = (DatumAuthorityFactory) factory;
        operationAuthority = (CoordinateOperationAuthorityFactory) factory;
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
                    obj = authority.createObject(code);
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
        return authority.getAuthority();
    }

    @Override
    public Set<String> getAuthorityCodes(Class<? extends IdentifiedObject> type) throws FactoryException {
        return authority.getAuthorityCodes(type);
    }

    @Override
    public InternationalString getDescriptionText(String code) throws FactoryException {
        return authority.getDescriptionText(code);
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
    // CSAuthority
    //
    @Override
    public CartesianCS createCartesianCS(String code) throws FactoryException {
        final String key = toKey(code);
        CartesianCS cs = (CartesianCS) cache.get(key);
        if (cs == null) {
            try {
                cache.writeLock(key);
                cs = (CartesianCS) cache.peek(key);
                if (cs == null) {
                    cs = csAuthority.createCartesianCS(code);
                    cache.put(key, cs);
                }
            } finally {
                cache.writeUnLock(key);
            }
        }
        return cs;
    }

    @Override
    public CoordinateSystem createCoordinateSystem(String code) throws FactoryException {
        final String key = toKey(code);
        CoordinateSystem cs = (CoordinateSystem) cache.get(key);
        if (cs == null) {
            try {
                cache.writeLock(key);
                cs = (CoordinateSystem) cache.peek(key);
                if (cs == null) {
                    cs = csAuthority.createCoordinateSystem(code);
                    cache.put(key, cs);
                }
            } finally {
                cache.writeUnLock(key);
            }
        }
        return cs;
    }

    // sample implemenation with get/test
    @Override
    public CoordinateSystemAxis createCoordinateSystemAxis(String code) throws FactoryException {
        final String key = toKey(code);
        CoordinateSystemAxis axis = (CoordinateSystemAxis) cache.get(key);
        if (axis == null) {
            try {
                cache.writeLock(key);
                axis = (CoordinateSystemAxis) cache.peek(key);
                if (axis == null) {
                    axis = csAuthority.createCoordinateSystemAxis(code);
                    cache.put(key, axis);
                }
            } finally {
                cache.writeUnLock(key);
            }
        }
        return axis;
    }

    @Override
    public CylindricalCS createCylindricalCS(String code) throws FactoryException {
        final String key = toKey(code);
        CylindricalCS cs = (CylindricalCS) cache.get(key);
        if (cs == null) {
            try {
                cache.writeLock(key);
                cs = (CylindricalCS) cache.peek(key);
                if (cs == null) {
                    cs = csAuthority.createCylindricalCS(code);
                    cache.put(key, cs);
                }
            } finally {
                cache.writeUnLock(key);
            }
        }
        return cs;
    }

    @Override
    public EllipsoidalCS createEllipsoidalCS(String code) throws FactoryException {
        final String key = toKey(code);
        EllipsoidalCS cs = (EllipsoidalCS) cache.get(key);
        if (cs == null) {
            try {
                cache.writeLock(key);
                cs = (EllipsoidalCS) cache.peek(key);
                if (cs == null) {
                    cs = csAuthority.createEllipsoidalCS(code);
                    cache.put(key, cs);
                }
            } finally {
                cache.writeUnLock(key);
            }
        }
        return cs;
    }

    @Override
    public PolarCS createPolarCS(String code) throws FactoryException {
        final String key = toKey(code);
        PolarCS cs = (PolarCS) cache.get(key);
        if (cs == null) {
            try {
                cache.writeLock(key);
                cs = (PolarCS) cache.peek(key);
                if (cs == null) {
                    cs = csAuthority.createPolarCS(code);
                    cache.put(key, cs);
                }
            } finally {
                cache.writeUnLock(key);
            }
        }
        return cs;
    }

    @Override
    public SphericalCS createSphericalCS(String code) throws FactoryException {
        final String key = toKey(code);
        SphericalCS cs = (SphericalCS) cache.get(key);
        if (cs == null) {
            try {
                cache.writeLock(key);
                cs = (SphericalCS) cache.peek(key);
                if (cs == null) {
                    cs = csAuthority.createSphericalCS(code);
                    cache.put(key, cs);
                }
            } finally {
                cache.writeUnLock(key);
            }
        }
        return cs;
    }

    @Override
    public TimeCS createTimeCS(String code) throws FactoryException {
        final String key = toKey(code);
        TimeCS cs = (TimeCS) cache.get(key);
        if (cs == null) {
            try {
                cache.writeLock(key);
                cs = (TimeCS) cache.peek(key);
                if (cs == null) {
                    cs = csAuthority.createTimeCS(code);
                    cache.put(key, cs);
                }
            } finally {
                cache.writeUnLock(key);
            }
        }
        return cs;
    }

    @Override
    public Unit<?> createUnit(String code) throws FactoryException {
        final String key = toKey(code);
        Unit<?> unit = (Unit) cache.get(key);
        if (unit == null) {
            try {
                cache.writeLock(key);
                unit = (Unit) cache.peek(key);
                if (unit == null) {
                    unit = csAuthority.createUnit(code);
                    cache.put(key, unit);
                }
            } finally {
                cache.writeUnLock(key);
            }
        }
        return unit;
    }

    @Override
    public VerticalCS createVerticalCS(String code) throws FactoryException {
        final String key = toKey(code);
        VerticalCS cs = (VerticalCS) cache.get(key);
        if (cs == null) {
            try {
                cache.writeLock(key);
                cs = (VerticalCS) cache.peek(key);
                if (cs == null) {
                    cs = csAuthority.createVerticalCS(code);
                    cache.put(key, cs);
                }
            } finally {
                cache.writeUnLock(key);
            }
        }
        return cs;
    }

    //
    // DatumAuthorityFactory
    //
    @Override
    public Datum createDatum(String code) throws FactoryException {
        final String key = toKey(code);
        Datum datum = (Datum) cache.get(key);
        if (datum == null) {
            try {
                cache.writeLock(key);
                datum = (Datum) cache.peek(key);
                if (datum == null) {
                    datum = datumAuthority.createDatum(code);
                    cache.put(key, datum);
                }
            } finally {
                cache.writeUnLock(key);
            }
        }
        return datum;
    }

    @Override
    public Ellipsoid createEllipsoid(String code) throws FactoryException {
        final String key = toKey(code);
        Ellipsoid ellipsoid = (Ellipsoid) cache.get(key);
        if (ellipsoid == null) {
            try {
                cache.writeLock(key);
                ellipsoid = (Ellipsoid) cache.peek(key);
                if (ellipsoid == null) {
                    ellipsoid = datumAuthority.createEllipsoid(code);
                    cache.put(key, ellipsoid);
                }
            } finally {
                cache.writeUnLock(key);
            }
        }
        return ellipsoid;
    }

    @Override
    public EngineeringDatum createEngineeringDatum(String code) throws FactoryException {
        final String key = toKey(code);
        EngineeringDatum datum = (EngineeringDatum) cache.get(key);
        if (datum == null) {
            try {
                cache.writeLock(key);
                datum = (EngineeringDatum) cache.peek(key);
                if (datum == null) {
                    datum = datumAuthority.createEngineeringDatum(code);
                    cache.put(key, datum);
                }
            } finally {
                cache.writeUnLock(key);
            }
        }
        return datum;
    }

    @Override
    public GeodeticDatum createGeodeticDatum(String code) throws FactoryException {
        final String key = toKey(code);
        GeodeticDatum datum = (GeodeticDatum) cache.get(key);
        if (datum == null) {
            try {
                cache.writeLock(key);
                datum = (GeodeticDatum) cache.peek(key);
                if (datum == null) {
                    datum = datumAuthority.createGeodeticDatum(code);
                    cache.put(key, datum);
                }
            } finally {
                cache.writeUnLock(key);
            }
        }
        return datum;
    }

    @Override
    public ImageDatum createImageDatum(String code) throws FactoryException {
        final String key = toKey(code);
        ImageDatum datum = (ImageDatum) cache.get(key);
        if (datum == null) {
            try {
                cache.writeLock(key);
                datum = (ImageDatum) cache.peek(key);
                if (datum == null) {
                    datum = datumAuthority.createImageDatum(code);
                    cache.put(key, datum);
                }
            } finally {
                cache.writeUnLock(key);
            }
        }
        return datum;
    }

    @Override
    public PrimeMeridian createPrimeMeridian(String code) throws FactoryException {
        final String key = toKey(code);
        PrimeMeridian datum = (PrimeMeridian) cache.get(key);
        if (datum == null) {
            try {
                cache.writeLock(key);
                datum = (PrimeMeridian) cache.peek(key);
                if (datum == null) {
                    datum = datumAuthority.createPrimeMeridian(code);
                    cache.put(key, datum);
                }
            } finally {
                cache.writeUnLock(key);
            }
        }
        return datum;
    }

    @Override
    public TemporalDatum createTemporalDatum(String code) throws FactoryException {
        final String key = toKey(code);
        TemporalDatum datum = (TemporalDatum) cache.get(key);
        if (datum == null) {
            try {
                cache.writeLock(key);
                datum = (TemporalDatum) cache.peek(key);
                if (datum == null) {
                    datum = datumAuthority.createTemporalDatum(code);
                    cache.put(key, datum);
                }
            } finally {
                cache.writeUnLock(key);
            }
        }
        return datum;
    }

    @Override
    public VerticalDatum createVerticalDatum(String code) throws FactoryException {
        final String key = toKey(code);
        VerticalDatum datum = (VerticalDatum) cache.get(key);
        if (datum == null) {
            try {
                cache.writeLock(key);
                datum = (VerticalDatum) cache.peek(key);
                if (datum == null) {
                    datum = datumAuthority.createVerticalDatum(code);
                    cache.put(key, datum);
                }
            } finally {
                cache.writeUnLock(key);
            }
        }
        return datum;
    }

    @Override
    public CoordinateOperation createCoordinateOperation(String code) throws FactoryException {
        final String key = toKey(code);
        CoordinateOperation operation = (CoordinateOperation) cache.get(key);
        if (operation == null) {
            try {
                cache.writeLock(key);
                operation = (CoordinateOperation) cache.peek(key);
                if (operation == null) {
                    operation = operationAuthority.createCoordinateOperation(code);
                    cache.put(key, operation);
                }
            } finally {
                cache.writeUnLock(key);
            }
        }
        return operation;
    }

    @Override
    @SuppressWarnings("unchecked")
    public synchronized Set<CoordinateOperation> createFromCoordinateReferenceSystemCodes(
            final String sourceCode, final String targetCode) throws FactoryException {

        final Object key = ObjectCaches.toKey(getAuthority(), sourceCode, targetCode);
        Set<CoordinateOperation> operations = (Set<CoordinateOperation>) cache.get(key);
        if (operations == null) {
            try {
                cache.writeLock(key);
                operations = (Set<CoordinateOperation>) cache.peek(key);
                if (operations == null) {
                    operations = operationAuthority.createFromCoordinateReferenceSystemCodes(sourceCode, targetCode);
                    // can we not trust operationAuthority to return us an unmodifiableSet ?
                    // operations = Collections.unmodifiableSet( operations );

                    cache.put(key, operations);
                }
            } finally {
                cache.writeUnLock(key);
            }
        }
        return operations;
    }
    //
    // AbstractAuthorityFactory
    //
    @Override
    public IdentifiedObjectFinder getIdentifiedObjectFinder(Class<? extends IdentifiedObject> type)
            throws FactoryException {
        return delegate.getIdentifiedObjectFinder(type);
    }

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
}
