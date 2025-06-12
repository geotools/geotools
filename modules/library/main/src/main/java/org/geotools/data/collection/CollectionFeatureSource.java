/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.collection;

import java.awt.RenderingHints.Key;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
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
import org.geotools.api.filter.sort.SortBy;
import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.store.EmptyFeatureCollection;
import org.geotools.data.store.ReTypingFeatureCollection;
import org.geotools.data.store.ReprojectingFeatureCollection;
import org.geotools.feature.collection.DecoratingSimpleFeatureCollection;
import org.geotools.feature.collection.FilteringSimpleFeatureCollection;
import org.geotools.feature.collection.MaxSimpleFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;

/**
 * This is a "port" of ContentFeatureSource to work with an iterator.
 *
 * <p>To use this class please "wrap" CollectionFeatureSource around your choice of FeatureCollection.
 *
 * <pre>
 * SimpleFeatureCollection collection = new ListFeatureCollection(schema);
 * collection.add(feature1);
 * collection.add(feature2);
 * FeatureSource source = new CollectionFeatureSource(collection);
 * </pre>
 *
 * <p>Note to implementors: If you are performing "real I/O" please use ContentFeatureSource as it provides support for
 * IOException.
 *
 * @author Jody
 */
public class CollectionFeatureSource implements SimpleFeatureSource {
    protected SimpleFeatureCollection collection;

    /** observers */
    protected List<FeatureListener> listeners = null;

    private QueryCapabilities capabilities;

    private Set<Key> hints;

    public CollectionFeatureSource(SimpleFeatureCollection collection) {
        this.collection = collection;
    }

    @Override
    public SimpleFeatureType getSchema() {
        return collection.getSchema();
    }

    @Override
    public synchronized void addFeatureListener(FeatureListener listener) {
        if (listeners == null) {
            listeners = Collections.synchronizedList(new ArrayList<>());
        }
        listeners.add(listener);
    }

    @Override
    public synchronized void removeFeatureListener(FeatureListener listener) {
        if (listeners == null) {
            return;
        }
        listeners.remove(listener);
    }

    @Override
    public ReferencedEnvelope getBounds() throws IOException {
        return collection.getBounds();
    }

    @Override
    public ReferencedEnvelope getBounds(Query query) throws IOException {
        return getFeatures(query).getBounds();
    }

    @Override
    public int getCount(Query query) throws IOException {
        return getFeatures(query).size();
    }

    @Override
    public DataAccess<SimpleFeatureType, SimpleFeature> getDataStore() {
        throw new UnsupportedOperationException("CollectionFeatureSource is an inmemory wrapper");
    }

    @Override
    public ResourceInfo getInfo() {
        throw new UnsupportedOperationException("CollectionFeatureSource is an inmemory wrapper");
    }

    @Override
    public Name getName() {
        return collection.getSchema().getName();
    }

    @Override
    public synchronized QueryCapabilities getQueryCapabilities() {
        if (capabilities == null) {
            capabilities = new QueryCapabilities() {
                @Override
                public boolean isOffsetSupported() {
                    return true;
                }

                @Override
                public boolean isReliableFIDSupported() {
                    return true;
                }

                @Override
                public boolean supportsSorting(org.geotools.api.filter.sort.SortBy... sortAttributes) {
                    return true;
                }
            };
        }
        return capabilities;
    }

    @Override
    public synchronized Set<Key> getSupportedHints() {
        if (hints == null) {
            Set<Key> supports = new HashSet<>();
            // supports.add( Hints.FEATURE_DETACHED );
            hints = Collections.unmodifiableSet(supports);
        }
        return hints;
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append("CollectionFeatureSource:");
        buf.append(collection);
        return buf.toString();
    }

    //
    // GET FEATURES
    // This forms the heart of the CollectionFeatureSource implementation
    // Use: DataUtilities.mixQueries(this.query, query, "subCollection" ) as needed
    //
    @Override
    public SimpleFeatureCollection getFeatures() throws IOException {
        return getFeatures(Query.ALL);
    }

    @Override
    public SimpleFeatureCollection getFeatures(Filter filter) {
        Query query = new Query(getSchema().getTypeName(), filter);
        return getFeatures(query);
    }

    @Override
    public SimpleFeatureCollection getFeatures(Query query) {
        query = DataUtilities.resolvePropertyNames(query, getSchema());
        final int offset = query.getStartIndex() != null ? query.getStartIndex() : 0;
        if (offset > 0 && query.getSortBy() == null) {
            if (!getQueryCapabilities().supportsSorting(query.getSortBy())) {
                throw new IllegalStateException("Feature source does not support this sorting "
                        + "so there is no way a stable paging (offset/limit) can be performed");
            }
            Query copy = new Query(query);
            copy.setSortBy(SortBy.NATURAL_ORDER);
            query = copy;
        }
        SimpleFeatureCollection features = collection;
        // step one: filter
        if (query.getFilter() != null && query.getFilter().equals(Filter.EXCLUDE)) {
            return new EmptyFeatureCollection(getSchema());
        }
        if (query.getFilter() != null && query.getFilter() != Filter.INCLUDE) {
            features = new FilteringSimpleFeatureCollection(features, query.getFilter());
        }
        // step two: reproject
        if (query.getCoordinateSystemReproject() != null) {
            features = new ReprojectingFeatureCollection(features, query.getCoordinateSystemReproject());
        }
        // step two sort! (note this makes a sorted copy)
        if (query.getSortBy() != null && query.getSortBy().length != 0) {
            SimpleFeature[] array = features.toArray(new SimpleFeature[features.size()]);
            // Arrays sort is stable (not resorting equal elements)
            for (SortBy sortBy : query.getSortBy()) {
                Comparator<SimpleFeature> comparator = DataUtilities.sortComparator(sortBy);
                Arrays.sort(array, comparator);
            }
            ArrayList<SimpleFeature> list = new ArrayList<>(Arrays.asList(array));
            features = new ListFeatureCollection(getSchema(), list);
        }

        // step three skip to start and return max number of fetaures
        if (offset > 0 || !query.isMaxFeaturesUnlimited()) {
            long max = Long.MAX_VALUE;
            if (!query.isMaxFeaturesUnlimited()) {
                max = query.getMaxFeatures();
            }
            features = new MaxSimpleFeatureCollection(features, offset, max);
        }

        // step four - retyping
        // (It would be nice to do this earlier so as to not have all the baggage
        // of unneeded attributes)
        if (query.getPropertyNames() != Query.ALL_NAMES) {
            // rebuild the type and wrap the reader
            SimpleFeatureType schema = features.getSchema();
            SimpleFeatureType target = SimpleFeatureTypeBuilder.retype(schema, query.getPropertyNames());

            // do an equals check because we may have needlessly retyped (that is,
            // the subclass might be able to only partially retype)
            if (!target.equals(schema)) {
                features = new ReTypingFeatureCollection(features, target);
            }
        }
        // Wrap up the results in a method that allows subCollection
        return new SubCollection(query, features);
    }

    /**
     * SubCollection for CollectionFeatureSource.
     *
     * <p>Will route any calls refining the feature collection back to CollectionFeatureSource. This is based on the
     * success of ContentFeatureCollection.
     *
     * @author Jody
     */
    protected class SubCollection extends DecoratingSimpleFeatureCollection {
        private Query query;

        protected SubCollection(Query query, SimpleFeatureCollection features) {
            super(features);
            this.query = query;
        }

        @Override
        public SimpleFeatureCollection subCollection(Filter filter) {
            Query q = new Query(getSchema().getTypeName(), filter);

            Query subQuery = DataUtilities.mixQueries(query, q, q.getHandle());
            return CollectionFeatureSource.this.getFeatures(subQuery);
        }

        @Override
        public SimpleFeatureCollection sort(SortBy order) {
            Query q = new Query(getSchema().getTypeName());
            q.setSortBy(order);

            Query subQuery = DataUtilities.mixQueries(query, q, q.getHandle());
            return CollectionFeatureSource.this.getFeatures(subQuery);
        }
    }
}
