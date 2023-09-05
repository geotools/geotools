/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data;

import java.io.IOException;
import org.geotools.api.data.FeatureLock;
import org.geotools.api.data.FeatureLocking;
import org.geotools.api.data.Query;
import org.geotools.api.data.SimpleFeatureLocking;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.Filter;

/**
 * Bridges between {@link FeatureLocking<SimpleFeatureType, SimpleFeature>} and {@link
 * SimpleFeatureLocking}
 */
class SimpleFeatureLockingBridge extends SimpleFeatureStoreBridge implements SimpleFeatureLocking {

    public SimpleFeatureLockingBridge(FeatureLocking<SimpleFeatureType, SimpleFeature> delegate) {
        super(delegate);
    }

    protected FeatureLocking<SimpleFeatureType, SimpleFeature> delegate() {
        return (FeatureLocking<SimpleFeatureType, SimpleFeature>) delegate;
    }

    @Override
    public int lockFeatures(Query query) throws IOException {
        return delegate().lockFeatures(query);
    }

    @Override
    public int lockFeatures(Filter filter) throws IOException {
        return delegate().lockFeatures(filter);
    }

    @Override
    public int lockFeatures() throws IOException {
        return delegate().lockFeatures();
    }

    @Override
    public void setFeatureLock(FeatureLock lock) {
        delegate().setFeatureLock(lock);
    }

    @Override
    public void unLockFeatures() throws IOException {
        delegate().unLockFeatures();
    }

    @Override
    public void unLockFeatures(Filter filter) throws IOException {
        delegate().unLockFeatures(filter);
    }

    @Override
    public void unLockFeatures(Query query) throws IOException {
        delegate().unLockFeatures(query);
    }
}
