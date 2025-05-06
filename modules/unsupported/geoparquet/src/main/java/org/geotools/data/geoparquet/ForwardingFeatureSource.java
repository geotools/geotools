/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2025, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.geoparquet;

import java.awt.RenderingHints.Key;
import java.io.IOException;
import java.util.Set;
import org.geotools.api.data.DataAccess;
import org.geotools.api.data.FeatureListener;
import org.geotools.api.data.Query;
import org.geotools.api.data.QueryCapabilities;
import org.geotools.api.data.ResourceInfo;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.Name;
import org.geotools.api.filter.Filter;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;

/**
 * A simple delegating feature source implementation that forwards all method calls to a delegate.
 *
 * <p>This class serves as a base class for more specialized feature source implementations that need to override or
 * augment the behavior of an existing feature source. Subclasses only need to override the methods they want to modify.
 */
class ForwardingFeatureSource implements SimpleFeatureSource {

    protected final SimpleFeatureSource delegate;

    /**
     * Creates a new forwarding feature source.
     *
     * @param delegate The feature source to delegate operations to
     */
    public ForwardingFeatureSource(SimpleFeatureSource delegate) {
        this.delegate = delegate;
    }

    @Override
    public DataAccess<SimpleFeatureType, SimpleFeature> getDataStore() {
        return delegate.getDataStore();
    }

    @Override
    public SimpleFeatureType getSchema() {
        return delegate.getSchema();
    }

    @Override
    public SimpleFeatureCollection getFeatures() throws IOException {
        return delegate.getFeatures();
    }

    @Override
    public SimpleFeatureCollection getFeatures(Filter filter) throws IOException {
        return delegate.getFeatures(filter);
    }

    @Override
    public SimpleFeatureCollection getFeatures(Query query) throws IOException {
        return delegate.getFeatures(query);
    }

    @Override
    public Name getName() {
        return delegate.getName();
    }

    @Override
    public ResourceInfo getInfo() {
        return delegate.getInfo();
    }

    @Override
    public QueryCapabilities getQueryCapabilities() {
        return delegate.getQueryCapabilities();
    }

    @Override
    public void addFeatureListener(FeatureListener listener) {
        delegate.addFeatureListener(listener);
    }

    @Override
    public void removeFeatureListener(FeatureListener listener) {
        delegate.removeFeatureListener(listener);
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
    public Set<Key> getSupportedHints() {
        return delegate.getSupportedHints();
    }
}
