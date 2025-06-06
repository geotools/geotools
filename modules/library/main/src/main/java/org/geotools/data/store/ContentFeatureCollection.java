/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.store;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.api.data.FeatureReader;
import org.geotools.api.data.Join;
import org.geotools.api.data.Query;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.feature.type.GeometryDescriptor;
import org.geotools.api.filter.Filter;
import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.FeatureTypes;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.logging.Logging;
import org.locationtech.jts.geom.Point;

/**
 * A FeatureCollection that completely delegates to a backing FetaureSource#getReader
 *
 * @author Jody Garnett (Refractions Research, Inc.)
 */
public class ContentFeatureCollection implements SimpleFeatureCollection {

    protected static final Logger LOGGER = Logging.getLogger(ContentFeatureCollection.class);
    /** feature store the collection originated from. */
    protected ContentFeatureSource featureSource;

    protected Query query;

    /** feature (possibly retyped from feautre source original) type */
    protected SimpleFeatureType featureType;
    /** state of the feature source */
    protected ContentState state;

    protected ContentFeatureCollection(ContentFeatureSource featureSource, Query query) {
        this.featureSource = featureSource;
        this.query = query;

        this.featureType = featureSource.getSchema();

        // retype feature type if necessary
        if (query.getPropertyNames() != Query.ALL_NAMES) {
            this.featureType = SimpleFeatureTypeBuilder.retype(this.featureType, query.getPropertyNames());
        }
        // Check for change in coordinate reference system
        // (Even if featureSource.canReproject the feature reader, we will need to adjust the
        //  featureType generated here to be correct)
        try {
            if (query.getCoordinateSystemReproject() != null) {
                this.featureType =
                        FeatureTypes.transform(this.featureType, query.getCoordinateSystemReproject(), false, true);
            } else if (query.getCoordinateSystem() != null) {
                this.featureType = FeatureTypes.transform(this.featureType, query.getCoordinateSystem(), false, true);
            }
        } catch (SchemaException e) {
            LOGGER.log(Level.FINER, "Problem handling Query change of CoordinateReferenceSystem:" + e, e);
        }

        // check for join and expand attributes as necessary
        if (!query.getJoins().isEmpty()) {
            SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
            tb.init(this.featureType);

            for (Join join : query.getJoins()) {
                try {
                    String typeName = join.getTypeName();
                    tb.userData(
                            ContentDataStore.JOINED_FEATURE_TYPE,
                            featureSource.getDataStore().getSchema(typeName));
                } catch (Exception e) {
                    LOGGER.log(Level.WARNING, "Failed to retrieve the joined feature type");
                }

                tb.add(join.attributeName(), SimpleFeature.class);
            }
            this.featureType = tb.buildFeatureType();
        }
    }

    @Override
    public SimpleFeatureType getSchema() {
        return featureType;
    }

    // Visitors
    @Override
    public void accepts(
            org.geotools.api.feature.FeatureVisitor visitor, org.geotools.api.util.ProgressListener progress)
            throws IOException {
        featureSource.accepts(query, visitor, progress);
    }

    // Iterators
    public static class WrappingFeatureIterator implements SimpleFeatureIterator {

        FeatureReader<SimpleFeatureType, SimpleFeature> delegate;

        public WrappingFeatureIterator(FeatureReader<SimpleFeatureType, SimpleFeature> delegate) {
            this.delegate = delegate;
        }

        @Override
        public boolean hasNext() {
            try {
                return delegate.hasNext();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public SimpleFeature next() throws java.util.NoSuchElementException {
            try {
                return delegate.next();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void close() {
            try {
                delegate.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public SimpleFeatureIterator features() {
        try {
            return new WrappingFeatureIterator(featureSource.getReader(query));
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public ReferencedEnvelope getBounds() {
        try {
            ReferencedEnvelope result = featureSource.getBounds(query);
            if (result != null) {
                return result;
            }

            // ops, we have to compute the results by hand. Let's load just the
            // geometry attributes though
            Query q = new Query(query);
            List<String> geometries = new ArrayList<>();
            for (AttributeDescriptor ad : getSchema().getAttributeDescriptors()) {
                if (ad instanceof GeometryDescriptor) {
                    geometries.add(ad.getLocalName());
                }
            }
            // no geometries, no bounds
            if (geometries.isEmpty()) {
                return new ReferencedEnvelope();
            } else {
                q.setPropertyNames(geometries);
            }
            // grab the features and scan through them
            try (FeatureReader<SimpleFeatureType, SimpleFeature> reader = featureSource.getReader(q)) {
                while (reader.hasNext()) {
                    SimpleFeature f = reader.next();
                    ReferencedEnvelope featureBounds = ReferencedEnvelope.reference(f.getBounds());
                    if (result == null) {
                        result = featureBounds;
                    } else if (featureBounds != null) {
                        result.expandToInclude(featureBounds);
                    }
                }
                // return the results if we got any, or return an empty one otherwise
                if (result != null) {
                    return result;
                } else {
                    return ReferencedEnvelope.create(getSchema().getCoordinateReferenceSystem());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int size() {
        try {
            int size = featureSource.getCount(query);
            if (size >= 0) {
                return size;
            } else {
                // we have to iterate, probably best if we do a minimal query that
                // only loads a short attribute
                AttributeDescriptor chosen = getSmallAttributeInSchema();

                // build the minimal query
                Query q = new Query(query);
                if (chosen != null) {
                    q.setPropertyNames(Collections.singletonList(chosen.getLocalName()));
                }
                // bean counting...
                try (FeatureReader<?, ?> fr = featureSource.getReader(q)) {
                    int count = 0;
                    while (fr.hasNext()) {
                        fr.next();
                        count++;
                    }
                    return count;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private AttributeDescriptor getSmallAttributeInSchema() {
        AttributeDescriptor chosen = null;
        for (AttributeDescriptor ad : getSchema().getAttributeDescriptors()) {
            if (chosen == null || size(ad) < size(chosen)) {
                chosen = ad;
            }
        }
        return chosen;
    }

    /**
     * Quick heuristic used to find a small attribute to use when scanning over the entire collection just to get the
     * size of the collection. The result is indicative, just an heuristic to quickly compare attributes in order to
     * find a suitably small one, it's not meant to be the actual size of an attribute in bytes.
     */
    int size(AttributeDescriptor ad) {
        Class<?> binding = ad.getType().getBinding();
        if (binding.isPrimitive() || Number.class.isAssignableFrom(binding) || Date.class.isAssignableFrom(binding)) {
            return 4;
        } else if (binding.equals(String.class)) {
            int fieldLen = FeatureTypes.getFieldLength(ad);
            if (fieldLen > 0) {
                return fieldLen * 2;
            } else {
                return Integer.MAX_VALUE;
            }
        } else if (Point.class.isAssignableFrom(binding)) {
            return 4 * 3;
        } else {
            return Integer.MAX_VALUE;
        }
    }

    @Override
    public boolean isEmpty() {
        // build a minimal query
        Query notEmptyQuery = new Query(query);
        notEmptyQuery.setMaxFeatures(1);

        AttributeDescriptor smallAttribute = getSmallAttributeInSchema();
        if (smallAttribute != null) {
            notEmptyQuery.setPropertyNames(Collections.singletonList(smallAttribute.getLocalName()));
        }

        try {
            try (FeatureReader<?, ?> fr = featureSource.getReader(notEmptyQuery)) {
                return !fr.hasNext();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    ContentFeatureStore ensureFeatureStore() {
        if (featureSource instanceof ContentFeatureStore) {
            return (ContentFeatureStore) featureSource;
        }
        throw new UnsupportedOperationException("read only");
    }

    @Override
    public SimpleFeatureCollection sort(org.geotools.api.filter.sort.SortBy sort) {
        Query query = new Query();
        query.setSortBy(sort);

        query = DataUtilities.mixQueries(this.query, query, null);
        return new ContentFeatureCollection(featureSource, query);
    }

    @Override
    public SimpleFeatureCollection subCollection(Filter filter) {
        Query query = new Query();
        query.setFilter(filter);

        query = DataUtilities.mixQueries(this.query, query, null);
        return new ContentFeatureCollection(featureSource, query);
    }

    //
    // The following were slated to be unsupported by a proposal from
    // jdeolive; since it has not been accepted the following
    // implementations are provided but not recommended
    //
    // There are completely implemented in terms of reader.
    /**
     * Returns <tt>true</tt> if this collection contains the specified element. <tt></tt>.
     *
     * <p>This implementation iterates over the elements in the collection, checking each element in turn for equality
     * with the specified element.
     *
     * @param o object to be checked for containment in this collection.
     * @return <tt>true</tt> if this collection contains the specified element.
     */
    @Override
    public boolean contains(Object o) {
        // TODO: base this on reader
        try (SimpleFeatureIterator e = this.features()) {
            if (o == null) {
                while (e.hasNext()) {
                    if (e.next() == null) {
                        return true;
                    }
                }
            } else {
                while (e.hasNext()) {
                    if (o.equals(e.next())) {
                        return true;
                    }
                }
            }
            return false;
        }
    }
    /**
     * Returns <tt>true</tt> if this collection contains all of the elements in the specified collection.
     *
     * <p>
     *
     * @param c collection to be checked for containment in this collection.
     * @return <tt>true</tt> if this collection contains all of the elements in the specified collection.
     * @throws NullPointerException if the specified collection is null.
     * @see #contains(Object)
     */
    @Override
    @SuppressWarnings("PMD.UseTryWithResources")
    public boolean containsAll(Collection<?> c) {
        Iterator<?> e = c.iterator();
        try {
            while (e.hasNext()) {
                if (!contains(e.next())) {
                    return false;
                }
            }
            return true;
        } finally {
            if (e instanceof FeatureIterator) {
                ((FeatureIterator<?>) e).close();
            }
        }
    }

    public boolean remove(Object o) {
        //        if( featureSource instanceof SimpleFeatureStore){
        //            SimpleFeatureStore featureStore = (SimpleFeatureStore) featureSource;
        //            if( o instanceof SimpleFeature ){
        //                SimpleFeature feature = (SimpleFeature) o;
        //                FeatureId fid = feature.getIdentifier();
        //                FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        //                Set<FeatureId> ids = Collections.singleton(fid);
        //                Filter remove = ff.id(ids);
        //                try {
        //                    featureStore.removeFeatures( remove );
        //                    return true;
        //                } catch (IOException e) {
        //                    //LOGGER.log(Level.FINER, e.getMessage(), e);
        //                    return false; // unable to remove
        //                }
        //            }
        //            else {
        //                return false; // nothing to do; we can only remove features
        //            }
        //        }
        throw new UnsupportedOperationException("Content is not writable; FeatureStore not available");
    }

    /**
     * Array of all the elements.
     *
     * @return an array containing all of the elements in this collection.
     */
    @Override
    public Object[] toArray() {
        // code based on AbstractFeatureCollection
        // TODO: base this on reader
        ArrayList<SimpleFeature> array = new ArrayList<>();
        try (FeatureIterator<SimpleFeature> e = features()) {
            while (e.hasNext()) {
                array.add(e.next());
            }
            return array.toArray(new SimpleFeature[array.size()]);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] array) {
        int size = size();
        if (array.length < size) {
            array = (T[]) java.lang.reflect.Array.newInstance(array.getClass().getComponentType(), size);
        }
        try (FeatureIterator<SimpleFeature> it = features()) {
            Object[] result = array;
            for (int i = 0; it.hasNext() && i < size; i++) {
                result[i] = it.next();
            }
            if (array.length > size) {
                array[size] = null;
            }
            return array;
        }
    }

    @Override
    public String getID() {
        return null; // Only useful for XML Content
    }
}
