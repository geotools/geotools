package org.geotools.data.collection;

import java.awt.RenderingHints.Key;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.geotools.data.DataAccess;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultQuery;
import org.geotools.data.FeatureListener;
import org.geotools.data.FilteringFeatureReader;
import org.geotools.data.Query;
import org.geotools.data.QueryCapabilities;
import org.geotools.data.ReTypeFeatureReader;
import org.geotools.data.ResourceInfo;
import org.geotools.data.crs.ReprojectFeatureReader;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.store.EmptyFeatureCollection;
import org.geotools.data.store.ReTypingFeatureCollection;
import org.geotools.data.store.ReprojectingFeatureCollection;
import org.geotools.factory.Hints;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.collection.AdaptorFeatureCollection;
import org.geotools.feature.collection.DecoratingSimpleFeatureCollection;
import org.geotools.feature.collection.FilteringSimpleFeatureCollection;
import org.geotools.feature.collection.MaxSimpleFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.opengis.filter.sort.SortBy;

/**
 * This is a "port" of ContentFeatureSource to work with an iterator.
 * <p>
 * To use this class please "wrap" CollectionFeatureSource around your choice of FeatureCollection.
 * 
 * <pre>
 * SimpleFeatureCollection collection = new ListFeatureCollection(schema);
 * collection.add(feature1);
 * collection.add(feature2);
 * FeatureSource source = new CollectionFeatureSource(collection);
 * </pre>
 * <p>
 * Note to implementors: If you are performing "real I/O" please use ContentFeatureSource as it
 * provides support for IOException.
 * 
 * @author Jody
 */
public class CollectionFeatureSource implements SimpleFeatureSource {
    protected SimpleFeatureCollection collection;

    /**
     * observers
     */
    protected List<FeatureListener> listeners = null;

    private QueryCapabilities capabilities;

    private Set<Key> hints;

    public CollectionFeatureSource(SimpleFeatureCollection collection) {
        this.collection = collection;
    }

    public SimpleFeatureType getSchema() {
        return collection.getSchema();
    }

    public synchronized void addFeatureListener(FeatureListener listener) {
        if (listeners == null) {
            listeners = Collections.synchronizedList(new ArrayList<FeatureListener>());
        }
        listeners.add(listener);
    }

    public synchronized void removeFeatureListener(FeatureListener listener) {
        if (listeners == null) {
            return;
        }
        listeners.remove(listener);
    }

    public ReferencedEnvelope getBounds() throws IOException {
        return collection.getBounds();
    }

    public ReferencedEnvelope getBounds(Query query) throws IOException {
        return getFeatures(query).getBounds();
    }

    public int getCount(Query query) throws IOException {
        return getFeatures(query).size();
    }

    public DataAccess<SimpleFeatureType, SimpleFeature> getDataStore() {
        throw new UnsupportedOperationException("CollectionFeatureSource is an inmemory wrapper");
    }

    public ResourceInfo getInfo() {
        throw new UnsupportedOperationException("CollectionFeatureSource is an inmemory wrapper");
    }

    public Name getName() {
        return collection.getSchema().getName();
    }

    public synchronized QueryCapabilities getQueryCapabilities() {
        if (capabilities == null) {
            capabilities = new QueryCapabilities() {
                public boolean isOffsetSupported() {
                    return true;
                }

                public boolean isReliableFIDSupported() {
                    return true;
                }

                public boolean supportsSorting(org.opengis.filter.sort.SortBy[] sortAttributes) {
                    return true;
                }
            };
        }
        return capabilities;
    }

    public synchronized Set<Key> getSupportedHints() {
        if (hints == null) {
            Set<Key> supports = new HashSet<Key>();
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
    public SimpleFeatureCollection getFeatures() throws IOException {
        return getFeatures( Query.ALL );
    }

    public SimpleFeatureCollection getFeatures(Filter filter) {
        Query query = new Query(getSchema().getTypeName(), filter);
        return getFeatures(query);
    }

    public SimpleFeatureCollection getFeatures(Query query) {
        query = DataUtilities.resolvePropertyNames(query, getSchema());
        final int offset = query.getStartIndex() != null ? query.getStartIndex() : 0;
        if (offset > 0 & query.getSortBy() == null) {
            if (!getQueryCapabilities().supportsSorting(query.getSortBy())){
                throw new IllegalStateException("Feature source does not support this sorting "
                        + "so there is no way a stable paging (offset/limit) can be performed");
            }
            Query copy = new Query(query);
            copy.setSortBy(new SortBy[] { SortBy.NATURAL_ORDER });
            query = copy;
        }
        SimpleFeatureCollection features = collection;
        // step one: filter
        if( query.getFilter() != null && query.getFilter().equals(Filter.EXCLUDE)){
            return new EmptyFeatureCollection( getSchema() );
        }
        if (query.getFilter() != null && query.getFilter() != Filter.INCLUDE) {
            features = new FilteringSimpleFeatureCollection(features, query.getFilter());
        }
        // step two: reproject
        if (query.getCoordinateSystemReproject() != null) {
            features = new ReprojectingFeatureCollection(features, query
                    .getCoordinateSystemReproject());
        }
        // step two sort! (note this makes a sorted copy)
        if (query.getSortBy() != null && query.getSortBy().length != 0) {
            SimpleFeature array[] = features.toArray(new SimpleFeature[features.size()]);
            // Arrays sort is stable (not resorting equal elements)
            for (SortBy sortBy : query.getSortBy()) {
                Comparator<SimpleFeature> comparator = DataUtilities.sortComparator(sortBy);
                Arrays.sort(array, comparator);
            }
            ArrayList<SimpleFeature> list = new ArrayList<SimpleFeature>(Arrays.asList(array));
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
            SimpleFeatureType target = SimpleFeatureTypeBuilder.retype(schema, query
                    .getPropertyNames());

            // do an equals check because we may have needlessly retyped (that is,
            // the subclass might be able to only partially retype)
            if (!target.equals(schema)) {
                features = new ReTypingFeatureCollection(features, target);
            }
        }
        // Wrap up the results in a method that allows subCollection
        return new SubCollection( query, features );
    }

    /**
     * SubCollection for CollectionFeatureSource.
     * <p>
     * Will route any calls refining the feature collection back to CollectionFeatureSource. This is
     * based on the success of ContentFeatureCollection.
     * </p>
     * 
     * @author Jody
     */
    protected class SubCollection extends DecoratingSimpleFeatureCollection {
        private Query query;
        protected SubCollection(Query query, SimpleFeatureCollection features) {
            super(features);
            this.query = query;
        }
        public SimpleFeatureCollection subCollection(Filter filter) {
            Query q = new Query(getSchema().getTypeName(), filter);
            
            Query subQuery = DataUtilities.mixQueries(query, q, q.getHandle() );
            return CollectionFeatureSource.this.getFeatures( subQuery );
        }
        @Override
        public SimpleFeatureCollection sort(SortBy order) {
            Query q = new Query( getSchema().getTypeName() );
            q.setSortBy( new SortBy[]{ order } );
            
            Query subQuery = DataUtilities.mixQueries(query, q, q.getHandle() );
            return CollectionFeatureSource.this.getFeatures( subQuery );
        }
    }
}