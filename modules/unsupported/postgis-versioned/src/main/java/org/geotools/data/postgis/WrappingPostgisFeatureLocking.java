/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.postgis;

import java.io.IOException;

import org.geotools.data.FeatureLock;
import org.geotools.data.FeatureLocking;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureLocking;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;

/**
 * Really delegates everything to a wrapped feature locking, but allows to
 * advertise a data store other than the original one
 * 
 * @author aaime
 * @since 2.4
 * 
 */
class WrappingPostgisFeatureLocking extends WrappingPostgisFeatureStore
        implements FeatureLocking<SimpleFeatureType, SimpleFeature> {

    private FeatureLocking<SimpleFeatureType, SimpleFeature> wrappedLocking;

    public WrappingPostgisFeatureLocking(SimpleFeatureLocking wrapped,
            VersionedPostgisDataStore store) {
        super(wrapped, store);
        this.wrappedLocking = wrapped;
    }

    public int lockFeatures() throws IOException {
        return wrappedLocking.lockFeatures();
    }

    public int lockFeatures(Filter filter) throws IOException {
        return wrappedLocking.lockFeatures(filter);
    }

    public int lockFeatures(Query query) throws IOException {
        return wrappedLocking.lockFeatures(query);
    }

    public void setFeatureLock(FeatureLock lock) {
        wrappedLocking.setFeatureLock(lock);
    }

    public void unLockFeatures() throws IOException {
        wrappedLocking.unLockFeatures();
    }

    public void unLockFeatures(Filter filter) throws IOException {
        wrappedLocking.unLockFeatures(filter);
    }

    public void unLockFeatures(Query query) throws IOException {
        wrappedLocking.unLockFeatures(query);
    }

}
