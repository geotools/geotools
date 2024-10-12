/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2023, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.transform;

import java.io.IOException;
import java.util.List;
import org.geotools.api.data.FeatureLock;
import org.geotools.api.data.Query;
import org.geotools.api.data.SimpleFeatureLocking;
import org.geotools.api.feature.type.Name;
import org.geotools.api.filter.Filter;

public class TransformFeatureLocking extends TransformFeatureStore implements SimpleFeatureLocking {
    private final SimpleFeatureLocking locking;

    public TransformFeatureLocking(SimpleFeatureLocking locking, Name name, List<Definition> definitions)
            throws IOException {
        super(locking, name, definitions);
        this.locking = locking;
    }

    @Override
    public void setFeatureLock(FeatureLock lock) {
        locking.setFeatureLock(lock);
    }

    @Override
    public int lockFeatures(Query query) throws IOException {
        Query txQuery = transformer.transformQuery(query);
        return locking.lockFeatures(txQuery);
    }

    @Override
    public int lockFeatures(Filter filter) throws IOException {
        Filter txFilter = transformer.transformFilter(filter);
        return locking.lockFeatures(txFilter);
    }

    @Override
    public int lockFeatures() throws IOException {
        return locking.lockFeatures();
    }

    @Override
    public void unLockFeatures() throws IOException {
        locking.unLockFeatures();
    }

    @Override
    public void unLockFeatures(Filter filter) throws IOException {
        Filter txFilter = transformer.transformFilter(filter);
        locking.unLockFeatures(txFilter);
    }

    @Override
    public void unLockFeatures(Query query) throws IOException {
        Query txQuery = transformer.transformQuery(query);
        locking.unLockFeatures(txQuery);
    }
}
