/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008-2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.data.complex;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import org.geotools.api.data.Query;
import org.geotools.api.data.Transaction;
import org.geotools.api.feature.Feature;
import org.geotools.api.feature.FeatureVisitor;
import org.geotools.api.feature.type.FeatureType;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.sort.SortBy;
import org.geotools.api.util.ProgressListener;
import org.geotools.data.DataUtilities;
import org.geotools.data.crs.ReprojectFeatureResults;
import org.geotools.feature.CollectionListener;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;

/**
 * {@link FeatureCollection} for a {@link MappingFeatureIterator}.
 *
 * @author Ben Caradoc-Davies (CSIRO Earth Science and Resource Engineering)
 * @since 2.6
 */
public class MappingFeatureCollection implements FeatureCollection<FeatureType, Feature> {

    private final AppSchemaDataAccess store;

    private final FeatureTypeMapping mapping;

    private final Query query;

    private Filter unrolledFilter = null;

    public MappingFeatureCollection(AppSchemaDataAccess store, FeatureTypeMapping mapping, Query query) {
        this.store = store;
        this.mapping = mapping;
        this.query = query;
    }

    public void setUnrolledFilter(Filter unrolledFilter) {
        this.unrolledFilter = unrolledFilter;
    }

    /**
     * @see org.geotools.feature.FeatureCollection#accepts(org.geotools.api.feature.FeatureVisitor,
     *     org.geotools.api.util.ProgressListener)
     */
    @Override
    public void accepts(FeatureVisitor visitor, ProgressListener progress) throws IOException {
        DataUtilities.visit(this, visitor, progress);
    }

    /**
     * Not a supported operation.
     *
     * @see org.geotools.feature.FeatureCollection#add(org.geotools.api.feature.Feature)
     */
    public boolean add(Feature obj) {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.feature.FeatureCollection#addAll(java.util.Collection)
     */
    public boolean addAll(Collection<? extends Feature> collection) {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.feature.FeatureCollection#addAll(org.geotools.feature.FeatureCollection)
     */
    public boolean addAll(FeatureCollection<? extends FeatureType, ? extends Feature> resource) {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.geotools.feature.FeatureCollection#addListener(org.geotools.feature.CollectionListener)
     */
    public void addListener(CollectionListener listener) throws NullPointerException {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.feature.FeatureCollection#clear()
     */
    public void clear() {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.feature.FeatureCollection#close(org.geotools.feature.FeatureIterator)
     */
    public void close(FeatureIterator<Feature> close) {
        close.close();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.feature.FeatureCollection#close(java.util.Iterator)
     */
    public void close(Iterator<Feature> close) {
        ((IMappingFeatureIterator) close).close();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.feature.FeatureCollection#contains(java.lang.Object)
     */
    @Override
    public boolean contains(Object o) {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.feature.FeatureCollection#containsAll(java.util.Collection)
     */
    @Override
    public boolean containsAll(Collection<?> o) {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.feature.FeatureCollection#features()
     */
    @Override
    public FeatureIterator<Feature> features() {
        try {
            return MappingFeatureIteratorFactory.getInstance(store, mapping, query, unrolledFilter);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This overload allows client code to explicitly specify the transaction that the created iterator will be working
     * against.
     *
     * <p>Passing <code>null</code> is equivalent to calling {@link #features()} and lets the iterator decide whether a
     * new transaction should be created (and closed when the iterator is closed) or not. Currently, a new transaction
     * is created by {@link DataAccessMappingFeatureIterator} only if a database backend is available and joining is
     * enabled, to reduce the number of concurrent connections opened due to feature chaining.
     *
     * @see org.geotools.feature.FeatureCollection#features()
     * @param transaction the transaction the created iterator will be working against
     */
    public FeatureIterator<Feature> features(Transaction transaction) {
        try {
            return MappingFeatureIteratorFactory.getInstance(store, mapping, query, unrolledFilter, transaction);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public XmlMappingFeatureIterator features(String xpath, String value) throws IOException {
        return new XmlMappingFeatureIterator(store, mapping, query, xpath, value);
    }

    /**
     * Stolen from {@link ReprojectFeatureResults}.
     *
     * @see org.geotools.feature.FeatureCollection#getBounds()
     */
    @Override
    public ReferencedEnvelope getBounds() {
        try (FeatureIterator<Feature> features = features()) {
            Envelope newBBox = new Envelope();
            Envelope internal;
            Feature feature;
            while (features.hasNext()) {
                feature = features.next();
                final Geometry geometry = feature.getDefaultGeometryProperty() != null
                        ? (Geometry) feature.getDefaultGeometryProperty().getValue()
                        : null;
                if (geometry != null) {
                    internal = geometry.getEnvelopeInternal();
                    newBBox.expandToInclude(internal);
                }
            }
            return ReferencedEnvelope.reference(newBBox);
        } catch (Exception e) {
            throw new RuntimeException("Exception occurred while computing bounds", e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.feature.FeatureCollection#getID()
     */
    @Override
    public String getID() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.feature.FeatureCollection#getSchema()
     */
    @Override
    public FeatureType getSchema() {
        return (FeatureType) mapping.getTargetFeature().getType();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.feature.FeatureCollection#isEmpty()
     */
    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.feature.FeatureCollection#iterator()
     */
    public Iterator<Feature> iterator() {
        try {
            return MappingFeatureIteratorFactory.getInstance(store, mapping, query, unrolledFilter);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.feature.FeatureCollection#purge()
     */
    public void purge() {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.feature.FeatureCollection#remove(java.lang.Object)
     */
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.feature.FeatureCollection#removeAll(java.util.Collection)
     */
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.geotools.feature.FeatureCollection#removeListener(org.geotools.feature.CollectionListener
     * )
     */
    public void removeListener(CollectionListener listener) throws NullPointerException {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.feature.FeatureCollection#retainAll(java.util.Collection)
     */
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.feature.FeatureCollection#size()
     */
    @Override
    public int size() {
        try {
            return store.getCount(query);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.feature.FeatureCollection#sort(org.geotools.api.filter.sort.SortBy)
     */
    @Override
    public FeatureCollection<FeatureType, Feature> sort(SortBy order) {
        throw new UnsupportedOperationException();
    }

    public boolean isXmlCollection() {
        return mapping instanceof XmlFeatureTypeMapping;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.feature.FeatureCollection#subCollection(org.geotools.api.filter.Filter)
     */
    @Override
    public FeatureCollection<FeatureType, Feature> subCollection(Filter filter) {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.feature.FeatureCollection#toArray()
     */
    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.feature.FeatureCollection#toArray(O[])
     */
    @Override
    public <O> O[] toArray(O[] a) {
        throw new UnsupportedOperationException();
    }

    public Query getQuery() {
        return this.query;
    }
}
