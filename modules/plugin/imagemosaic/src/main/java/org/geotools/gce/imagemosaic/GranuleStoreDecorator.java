/*
 * GeoTools - The Open Source Java GIS Toolkit
 * http://geotools.org
 *
 * (C) 2017, Open Source Geospatial Foundation (OSGeo)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation;
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 */
package org.geotools.gce.imagemosaic;

import java.io.IOException;
import org.geotools.coverage.grid.io.GranuleStore;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.factory.Hints;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;

/**
 * Plain delegating base class for {@link GranuleStore} decorators. Meant to allow easier/clearer
 * implementation of subclasses overriding specific methods
 */
class GranuleStoreDecorator implements GranuleStore {

    protected GranuleStore delegate;

    public GranuleStoreDecorator(GranuleStore delegate) {
        this.delegate = delegate;
    }

    @Override
    public void addGranules(SimpleFeatureCollection granules) {
        delegate.addGranules(granules);
    }

    @Override
    public int removeGranules(Filter filter) {
        return delegate.removeGranules(filter);
    }

    @Override
    public int removeGranules(Filter filter, Hints hints) {
        return delegate.removeGranules(filter, hints);
    }

    @Override
    public void updateGranules(String[] attributeNames, Object[] attributeValues, Filter filter) {
        delegate.updateGranules(attributeNames, attributeValues, filter);
    }

    @Override
    public Transaction getTransaction() {
        return delegate.getTransaction();
    }

    @Override
    public void setTransaction(Transaction transaction) {
        delegate.setTransaction(transaction);
    }

    @Override
    public SimpleFeatureCollection getGranules(Query q) throws IOException {
        return delegate.getGranules(q);
    }

    @Override
    public int getCount(Query q) throws IOException {
        return delegate.getCount(q);
    }

    @Override
    public ReferencedEnvelope getBounds(Query q) throws IOException {
        return delegate.getBounds(q);
    }

    @Override
    public SimpleFeatureType getSchema() throws IOException {
        return delegate.getSchema();
    }

    @Override
    public void dispose() throws IOException {
        delegate.dispose();
    }
}
