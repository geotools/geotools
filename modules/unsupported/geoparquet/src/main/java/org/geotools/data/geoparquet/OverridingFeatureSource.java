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

import java.io.IOException;
import org.geotools.api.data.DataAccess;
import org.geotools.api.data.DataStore;
import org.geotools.api.data.Query;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.Filter;
import org.geotools.data.simple.SimpleFeatureCollection;

/**
 * A feature source implementation that overrides the schema of its delegate source.
 *
 * <p>This implementation is used to provide feature sources with more specific geometry types than what the underlying
 * JDBC feature source is able to determine. It uses the GeoParquet metadata to narrowly type geometry fields according
 * to their actual content.
 *
 * <p>Key functionality includes:
 *
 * <ul>
 *   <li>Overriding the feature type schema with one that has more specific geometry types
 *   <li>Wrapping feature collections to ensure features report the correct schema
 *   <li>Delegating all other operations to the underlying feature source
 * </ul>
 */
class OverridingFeatureSource extends ForwardingFeatureSource {

    private SimpleFeatureType overridingType;
    private DataStore dataStore;

    /**
     * Creates a new overriding feature source.
     *
     * @param delegate The underlying feature source to delegate operations to
     * @param overridingStore The data store to report as the source of this feature source
     * @param overridingType The feature type with correct geometry types to use instead of the delegate's schema
     */
    public OverridingFeatureSource(
            SimpleFeatureSource delegate, DataStore overridingStore, SimpleFeatureType overridingType) {
        super(delegate);
        this.dataStore = overridingStore;
        this.overridingType = overridingType;
    }

    @Override
    public DataAccess<SimpleFeatureType, SimpleFeature> getDataStore() {
        return dataStore;
    }

    @Override
    public SimpleFeatureType getSchema() {
        return overridingType;
    }

    @Override
    public SimpleFeatureCollection getFeatures() throws IOException {
        return new OverridingFeatureCollection(delegate.getFeatures(), overridingType);
    }

    @Override
    public SimpleFeatureCollection getFeatures(Filter filter) throws IOException {
        return new OverridingFeatureCollection(delegate.getFeatures(filter), overridingType);
    }

    @Override
    public SimpleFeatureCollection getFeatures(Query query) throws IOException {
        return new OverridingFeatureCollection(delegate.getFeatures(query), overridingType);
    }
}
