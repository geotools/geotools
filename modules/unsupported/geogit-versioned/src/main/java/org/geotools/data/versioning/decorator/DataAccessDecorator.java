/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2010, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.versioning.decorator;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.geogit.api.GeoGIT;
import org.geogit.repository.Repository;
import org.geotools.data.DataAccess;
import org.geotools.data.FeatureLocking;
import org.geotools.data.FeatureSource;
import org.geotools.data.FeatureStore;
import org.geotools.data.Query;
import org.geotools.data.ServiceInfo;
import org.geotools.data.versioning.VersioningDataAccess;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Id;
import org.opengis.filter.identity.Identifier;
import org.opengis.filter.identity.ResourceId;
import org.springframework.util.Assert;

import com.google.common.base.Throwables;

/**
 * Decorator around an unversioned DataAccess allowing it to be used in conjunction
 * with a GeoGit {@link Repository} for revision information.
 *
 * @param <T> FeatureType
 * @param <F> Feature
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class DataAccessDecorator<T extends FeatureType, F extends Feature>
        implements VersioningDataAccess<T, F> {
    
    /** Checkout database used to reflect unversioned "live" data */
    protected DataAccess<T, F> unversioned;
    
    /**
     * Default GeoGit repository to use for storing revision history.
     * <p>
     * Subclasses can use {@link #getRepository(Name)} to access while still allowing support
     * for multiple repositories if needed.
     */
    private Repository repository;
    

    public DataAccessDecorator(DataAccess unversioned, Repository versioningRepo) {
        Assert.notNull(unversioned);
        Assert.notNull(versioningRepo);
        Assert.isTrue(!(unversioned instanceof DataAccessDecorator));
        this.unversioned = unversioned;
        this.repository = versioningRepo;
    }

    public boolean isVersioned(Name name) {
        boolean isVersioned = repository.getWorkingTree().hasRoot(name);
        return isVersioned;
    }

    /**
     * @see org.geotools.data.DataAccess#dispose()
     */
    @Override
    public void dispose() {
        if (unversioned != null) {
            unversioned.dispose();
            unversioned = null;
        }
    }

    /**
     * @see org.geotools.data.DataAccess#getFeatureSource(org.opengis.feature.type.Name)
     */
    @Override
    public FeatureSource<T, F> getFeatureSource(Name typeName)
            throws IOException {
        FeatureSource source = unversioned.getFeatureSource(typeName);
        if (source instanceof FeatureLocking) {
            return createFeatureLocking((FeatureLocking) source);
        } else if (source instanceof FeatureStore) {
            return createFeatureStore((FeatureStore) source);
        }
        return createFeatureSource(source);
    }

    /**
     * @see org.geotools.data.DataAccess#getInfo()
     */
    @Override
    public ServiceInfo getInfo() {
        return unversioned.getInfo();
    }

    /**
     * @see org.geotools.data.DataAccess#createSchema(org.opengis.feature.type.FeatureType)
     */
    @Override
    public void createSchema(T featureType) throws IOException {
        unversioned.createSchema(featureType);
        try {
            repository.getWorkingTree().init(featureType);
            GeoGIT ggit = new GeoGIT(repository);
            ggit.add().call();
            ggit.commit().call();
        } catch (Exception e) {
            Throwables.propagate(e);
        }
    }

    /**
     * @see org.geotools.data.DataAccess#updateSchema(org.opengis.feature.type.Name,
     *      org.opengis.feature.type.FeatureType)
     */
    @Override
    public void updateSchema(Name typeName, T featureType) throws IOException {
        unversioned.updateSchema(typeName, featureType);
        // Is any effort needed to track the schema change in the geogit repository
    }

    /**
     * @see org.geotools.data.DataAccess#getNames()
     */
    @Override
    public List<Name> getNames() throws IOException {
        return unversioned.getNames();
    }

    /**
     * @see org.geotools.data.DataAccess#getSchema(org.opengis.feature.type.Name)
     */
    @Override
    public T getSchema(Name name) throws IOException {
        return unversioned.getSchema(name);
    }

    /**
     * Used to retreive past revisions from GeoGit repository; the revision history range
     * is provided by versionFilter (which is required tocontain a {@link ResourceId}.
     * 
     * @precondition {@code typeName != null && versioningFilter != null}
     * @precondition {@code versioningFilter.getIdentifiers().size() > 0}
     * @postcondition {@code $return != null}
     * @param typeName
     * @param versioningFilter Id Filter providing revision history range
     * @param extraQuery
     * @return features
     * @throws IOException
     */
    public FeatureCollection getFeatures(final Name typeName,
            final Id versioningFilter, final Query extraQuery)
            throws IOException {
        Assert.notNull(typeName);
        Assert.notNull(versioningFilter);
        Assert.isTrue(versioningFilter.getIdentifiers().size() > 0);

        final Set<Identifier> identifiers = versioningFilter.getIdentifiers();
        final Set<ResourceId> resourceIds = new HashSet<ResourceId>();
        for (Identifier id : identifiers) {
            if (id instanceof ResourceId) {
                resourceIds.add((ResourceId) id);
            }
        }
        if (resourceIds.size() == 0) {
            throw new IllegalArgumentException("At least one "
                    + ResourceId.class.getName() + " should be provided: "
                    + identifiers);
        }

        final FeatureType featureType = this.getSchema(typeName);
        ResourceIdFeatureCollector versionCollector;
        versionCollector = new ResourceIdFeatureCollector(repository, featureType,
                resourceIds);

        DefaultFeatureCollection features = new DefaultFeatureCollection(null,
                (SimpleFeatureType) featureType);
        
        for (Feature f : versionCollector) {
            features.add((SimpleFeature) f);
        }
        return features;
    }
    
    /**
     * Lookup appropriate repository for provided typeName.
     * <b>
     * By default there is a single GeoGit repository associated with the {@link #unversioned}
     * DataStore. When making use of more than one Repository you can override this method to
     * perform the mapping.
     * 
     * @param typeName
     * @return Repository for use with the provided typeName
     */
    protected Repository getRepository( Name typeName ){
        return repository;
    }
    /**
     * Return a {@link FeatureSourceDecorator} using the repository provided
     * by {@link #getRepository(Name)}.
     * 
     * @param source 
     * @return FeatureSource allowing access to source and repository data
     */
    protected FeatureSource<T, F> createFeatureSource(FeatureSource<T, F> source) {
        Repository repo = getRepository( source.getName() );
        return new FeatureSourceDecorator(source, repo);
    }
    /**
     * Return a {@link FeatureStoreDecorator} using the repository provided
     * by {@link #getRepository(Name)}.
     * 
     * @param store 
     * @return FeatureSource allowing access to source and repository data
     */
    protected FeatureStore<T, F> createFeatureStore(FeatureStore<T, F> store) {
        Repository repo = getRepository( store.getName() );
        return new FeatureStoreDecorator(store, repo);
    }

    /**
     * Return a {@link FeatureLockingDecorator} using the repository provided
     * by {@link #getRepository(Name)}.
     * 
     * @param locking 
     * @return FeatureSource allowing access to source and repository data
     */
    protected FeatureLocking<T, F> createFeatureLocking(
            FeatureLocking<T, F> locking) {
        Repository repo = getRepository( locking.getName() );
        return new FeatureLockingDecorator(locking, repo);
    }
}
