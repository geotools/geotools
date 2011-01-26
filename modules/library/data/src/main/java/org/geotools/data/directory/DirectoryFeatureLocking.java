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
package org.geotools.data.directory;

import java.io.IOException;

import org.geotools.data.FeatureLock;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureLocking;
import org.opengis.filter.Filter;

public class DirectoryFeatureLocking extends DirectoryFeatureStore implements
        SimpleFeatureLocking {

    SimpleFeatureLocking flocking;

    public DirectoryFeatureLocking(
            SimpleFeatureLocking locking) {
        super(locking);
        this.flocking = locking;
    }

    public int lockFeatures() throws IOException {
        return flocking.lockFeatures();
    }

    public int lockFeatures(Filter filter) throws IOException {
        return flocking.lockFeatures(filter);
    }

    public void setFeatureLock(FeatureLock lock) {
        flocking.setFeatureLock(lock);
    }

    public int lockFeatures(Query query) throws IOException {
        return flocking.lockFeatures(query);
    }

    public void unLockFeatures() throws IOException {
        flocking.unLockFeatures();
    }

    public void unLockFeatures(Filter filter) throws IOException {
        flocking.unLockFeatures(filter);
    }

    public void unLockFeatures(Query query) throws IOException {
        flocking.unLockFeatures(query);
    }
    
    @Override
    public SimpleFeatureLocking unwrap() {
        return flocking;
    }

}
