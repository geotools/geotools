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
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;

/**
 * Bridges between {@link FeatureSource<SimpleFeatureType, SimpleFeature>} and {@link SimpleFeatureSource}
 */

class SimpleFeatureSourceBridge implements SimpleFeatureSource {

    protected FeatureSource<SimpleFeatureType, SimpleFeature> delegate;

    public SimpleFeatureSourceBridge(FeatureSource<SimpleFeatureType, SimpleFeature> delegate) {
        this.delegate = delegate;
    }

    public void addFeatureListener(FeatureListener listener) {
        delegate.addFeatureListener(listener);
    }

    public ReferencedEnvelope getBounds() throws IOException {
        return delegate.getBounds();
    }

    public ReferencedEnvelope getBounds(Query query) throws IOException {
        return delegate.getBounds(query);
    }

    public int getCount(Query query) throws IOException {
        return delegate.getCount(query);
    }

    public DataAccess<SimpleFeatureType, SimpleFeature> getDataStore() {
        return delegate.getDataStore();
    }

    public SimpleFeatureCollection getFeatures() throws IOException {
        return DataUtilities.simple(delegate.getFeatures());
    }

    public SimpleFeatureCollection getFeatures(Filter filter) throws IOException {
        return DataUtilities.simple(delegate.getFeatures(filter));
    }

    public SimpleFeatureCollection getFeatures(Query query) throws IOException {
        return DataUtilities.simple(delegate.getFeatures(query));
    }

    public ResourceInfo getInfo() {
        return delegate.getInfo();
    }

    public Name getName() {
        return delegate.getName();
    }

    public QueryCapabilities getQueryCapabilities() {
        return delegate.getQueryCapabilities();
    }

    public SimpleFeatureType getSchema() {
        return delegate.getSchema();
    }

    public Set<Key> getSupportedHints() {
        return delegate.getSupportedHints();
    }

    public void removeFeatureListener(FeatureListener listener) {
        delegate.removeFeatureListener(listener);
    }

}
