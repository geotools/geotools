/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.grid.io;

import java.io.IOException;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.store.ReTypingFeatureCollection;
import org.geotools.util.factory.Hints;
import org.opengis.filter.Filter;

/** Write supporting subclass of {@link RenamingGranuleSource} */
public class RenamingGranuleStore extends RenamingGranuleSource implements GranuleStore {

    private final GranuleStore store;

    public RenamingGranuleStore(String name, GranuleStore delegate) throws IOException {
        super(name, delegate);
        this.store = delegate;
    }

    @Override
    public void addGranules(SimpleFeatureCollection granules) {
        try {
            ReTypingFeatureCollection backMapped =
                    new ReTypingFeatureCollection(granules, delegate.getSchema());
            store.addGranules(backMapped);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int removeGranules(Filter filter) {
        return store.removeGranules(filter);
    }

    @Override
    public int removeGranules(Filter filter, Hints hints) {
        return store.removeGranules(filter, hints);
    }

    @Override
    public void updateGranules(String[] attributeNames, Object[] attributeValues, Filter filter) {
        store.updateGranules(attributeNames, attributeValues, filter);
    }

    @Override
    public Transaction getTransaction() {
        return store.getTransaction();
    }

    @Override
    public void setTransaction(Transaction transaction) {
        store.setTransaction(transaction);
    }
}
