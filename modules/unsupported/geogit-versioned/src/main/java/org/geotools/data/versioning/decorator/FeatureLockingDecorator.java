/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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

import org.geogit.repository.Repository;
import org.geotools.data.FeatureLock;
import org.geotools.data.FeatureLocking;
import org.geotools.data.Query;
import org.geotools.data.versioning.VersioningFeatureLocking;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;
import org.opengis.filter.Filter;

@SuppressWarnings("rawtypes")
public class FeatureLockingDecorator<T extends FeatureType, F extends Feature>
        extends FeatureStoreDecorator<T, F> implements
        VersioningFeatureLocking<T, F> {

    public FeatureLockingDecorator(FeatureLocking unversioned, Repository repo) {
        super(unversioned, repo);
    }

    @Override
    public void setFeatureLock(FeatureLock lock) {
        ((FeatureLocking) unversioned).setFeatureLock(lock);
    }

    @Override
    public int lockFeatures(Query query) throws IOException {
        return ((FeatureLocking) unversioned).lockFeatures(query);
    }

    @Override
    public int lockFeatures(Filter filter) throws IOException {
        return ((FeatureLocking) unversioned).lockFeatures(filter);
    }

    @Override
    public int lockFeatures() throws IOException {
        return ((FeatureLocking) unversioned).lockFeatures();
    }

    @Override
    public void unLockFeatures() throws IOException {
        ((FeatureLocking) unversioned).unLockFeatures();
    }

    @Override
    public void unLockFeatures(Filter filter) throws IOException {
        ((FeatureLocking) unversioned).unLockFeatures(filter);
    }

    @Override
    public void unLockFeatures(Query query) throws IOException {
        ((FeatureLocking) unversioned).unLockFeatures(query);
    }
}
