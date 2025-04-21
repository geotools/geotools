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

import java.util.NoSuchElementException;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.store.ReTypingFeatureCollection;
import org.geotools.feature.DecoratingFeature;

/**
 * A feature collection that overrides the schema of its features.
 *
 * <p>This implementation extends ReTypingFeatureCollection to wrap features with the correct schema that has
 * appropriate geometry types based on GeoParquet metadata. It ensures that the thread-local context is properly set
 * during feature iteration for proper handling of geometry types.
 */
class OverridingFeatureCollection extends ReTypingFeatureCollection {

    private SimpleFeatureType featureType;

    /**
     * Creates a new overriding feature collection.
     *
     * @param delegate The underlying feature collection to delegate operations to
     * @param featureType The feature type with correct geometry types to use instead of the delegate's schema
     */
    protected OverridingFeatureCollection(SimpleFeatureCollection delegate, SimpleFeatureType featureType) {
        super(delegate, featureType);
        this.featureType = featureType;
    }

    @Override
    public SimpleFeatureType getSchema() {
        return featureType;
    }

    /**
     * Returns an iterator for accessing the features in this collection.
     *
     * <p>This method sets the GeoParquetDialect.CURRENT_TYPENAME thread-local variable before creating the iterator, to
     * ensure proper geometry type handling during SQL generation. This is needed because when iterating through
     * features, the GeoTools rendering pipeline may trigger additional SQL queries that need to be aware of the correct
     * geometry types.
     *
     * @return A SimpleFeatureIterator for accessing features with the correct schema
     */
    @Override
    public SimpleFeatureIterator features() {
        GeoParquetDialect.CURRENT_TYPENAME.set(featureType.getTypeName());
        try {
            return new OverridingFeatureIterator(delegate.features(), featureType);
        } finally {
            GeoParquetDialect.CURRENT_TYPENAME.remove();
        }
    }

    /**
     * An iterator that wraps features to override their feature type.
     *
     * <p>This iterator delegates to an underlying feature iterator but wraps each returned feature in an
     * OverridingFeature that reports the correct feature type schema.
     */
    static class OverridingFeatureIterator implements SimpleFeatureIterator {

        private SimpleFeatureIterator features;
        private SimpleFeatureType featureType;

        /**
         * Creates a new overriding feature iterator.
         *
         * @param features The underlying feature iterator to delegate to
         * @param featureType The feature type that should be reported by returned features
         */
        public OverridingFeatureIterator(SimpleFeatureIterator features, SimpleFeatureType featureType) {
            this.features = features;
            this.featureType = featureType;
        }

        @Override
        public boolean hasNext() {
            return features.hasNext();
        }

        @Override
        public SimpleFeature next() throws NoSuchElementException {
            SimpleFeature feature = features.next();
            return new OverridingFeature(feature, featureType);
        }

        @Override
        public void close() {
            features.close();
        }
    }

    /**
     * A feature wrapper that overrides the feature type of the delegate feature.
     *
     * <p>This implementation decorates an existing feature but returns a different feature type when getFeatureType()
     * is called. This allows for providing features with more specific geometry type information than what the
     * underlying feature has.
     */
    static class OverridingFeature extends DecoratingFeature {

        private SimpleFeatureType featureTypeOverride;

        /**
         * Creates a new overriding feature.
         *
         * @param feature The underlying feature to delegate operations to
         * @param featureType The feature type to return instead of the delegate's type
         */
        public OverridingFeature(SimpleFeature feature, SimpleFeatureType featureType) {
            super(feature);
            this.featureTypeOverride = featureType;
        }

        @Override
        public SimpleFeatureType getFeatureType() {
            return featureTypeOverride;
        }
    }
}
