/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2025, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.vectortiles.store;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import org.geotools.api.data.SimpleFeatureReader;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.Filter;

class StreamFeatureReader<F> implements SimpleFeatureReader {

    protected final SimpleFeatureType targetSchema;
    private final Stream<F> features;

    private Function<F, SimpleFeature> mapper;
    private Predicate<SimpleFeature> filter = Objects::nonNull;
    private long offset = 0;
    private long limit = Long.MAX_VALUE;

    private Iterator<SimpleFeature> iterator;

    public StreamFeatureReader(SimpleFeatureType targetSchema, Stream<F> features) {
        this.targetSchema = requireNonNull(targetSchema, "targetSchema");
        this.features = requireNonNull(features, "features");
        this.mapper = f -> {
            throw new IllegalStateException("Feature mapper is not set, can't convert " + f);
        };
    }

    public StreamFeatureReader(SimpleFeatureType schema, Stream<F> features, Function<F, SimpleFeature> mapper) {
        this.targetSchema = requireNonNull(schema, "schema");
        this.features = requireNonNull(features, "features");
        this.mapper = requireNonNull(mapper, "mapper");
    }

    public static StreamFeatureReader<SimpleFeature> empty(SimpleFeatureType schema) {
        return new StreamFeatureReader<>(schema, Stream.empty(), Function.identity());
    }

    public StreamFeatureReader<F> mapper(Function<F, SimpleFeature> mapper) {
        this.mapper = requireNonNull(mapper, "mapper");
        return this;
    }

    public StreamFeatureReader<F> filter(Filter filter) {
        Predicate<SimpleFeature> nonNull = Objects::nonNull;
        this.filter = nonNull.and(requireNonNull(filter)::evaluate);
        return this;
    }

    public StreamFeatureReader<F> offset(long offset) {
        this.offset = requirePositive(offset, "offset has to be >= 0");
        return this;
    }

    public StreamFeatureReader<F> limit(long limit) {
        this.limit = requirePositive(limit, "limit has to be >= 0");
        return this;
    }

    @Override
    public SimpleFeatureType getFeatureType() {
        return targetSchema;
    }

    @Override
    public SimpleFeature next() throws NoSuchElementException {
        return iterator.next();
    }

    @Override
    public boolean hasNext() throws IOException {
        return iterator().hasNext();
    }

    @Override
    public void close() throws IOException {
        features.close();
    }

    private Iterator<SimpleFeature> iterator() {
        if (this.iterator == null) {
            this.iterator = features.map(mapper)
                    .filter(filter)
                    .skip(offset)
                    .limit(limit)
                    .iterator();
        }
        return this.iterator;
    }

    private long requirePositive(long param, String errorMsg) {
        if (param < 0) {
            throw new IllegalArgumentException(errorMsg);
        }
        return param;
    }
}
