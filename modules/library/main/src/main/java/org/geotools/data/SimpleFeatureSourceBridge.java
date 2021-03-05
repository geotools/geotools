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

import java.awt.RenderingHints.Key;
import java.io.IOException;
import java.util.Set;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;

/**
 * Bridges between {@link FeatureSource<SimpleFeatureType, SimpleFeature>} and {@link
 * SimpleFeatureSource}
 */
class SimpleFeatureSourceBridge implements SimpleFeatureSource {

    protected FeatureSource<SimpleFeatureType, SimpleFeature> delegate;

    public SimpleFeatureSourceBridge(FeatureSource<SimpleFeatureType, SimpleFeature> delegate) {
        this.delegate = delegate;
    }

    @Override
    public void addFeatureListener(FeatureListener listener) {
        delegate.addFeatureListener(listener);
    }

    @Override
    public ReferencedEnvelope getBounds() throws IOException {
        return delegate.getBounds();
    }

    @Override
    public ReferencedEnvelope getBounds(Query query) throws IOException {
        return delegate.getBounds(query);
    }

    @Override
    public int getCount(Query query) throws IOException {
        return delegate.getCount(query);
    }

    @Override
    public DataAccess<SimpleFeatureType, SimpleFeature> getDataStore() {
        return delegate.getDataStore();
    }

    @Override
    public SimpleFeatureCollection getFeatures() throws IOException {
        return DataUtilities.simple(delegate.getFeatures());
    }

    @Override
    public SimpleFeatureCollection getFeatures(Filter filter) throws IOException {
        return DataUtilities.simple(delegate.getFeatures(filter));
    }

    @Override
    public SimpleFeatureCollection getFeatures(Query query) throws IOException {
        return DataUtilities.simple(delegate.getFeatures(query));
    }

    @Override
    public ResourceInfo getInfo() {
        return delegate.getInfo();
    }

    @Override
    public Name getName() {
        return delegate.getName();
    }

    @Override
    public QueryCapabilities getQueryCapabilities() {
        return delegate.getQueryCapabilities();
    }

    @Override
    public SimpleFeatureType getSchema() {
        return delegate.getSchema();
    }

    @Override
    public Set<Key> getSupportedHints() {
        return delegate.getSupportedHints();
    }

    @Override
    public void removeFeatureListener(FeatureListener listener) {
        delegate.removeFeatureListener(listener);
    }
}
