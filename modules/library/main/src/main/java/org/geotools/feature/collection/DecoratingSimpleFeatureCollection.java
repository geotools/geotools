/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.feature.collection;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.CollectionListener;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.sort.SortBy;

/**
 * A FeatureCollection which completley delegates to another FeatureCollection.
 * <p>
 * This class should be subclasses by classes which must somehow decorate 
 * another SimpleFeatureCollection and override the relevant methods. 
 * </p>
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 * @since 2.5
 *
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/library/main/src/main/java/org/geotools/feature/collection/DecoratingSimpleFeatureCollection.java $
 */
public class DecoratingSimpleFeatureCollection implements SimpleFeatureCollection {

    /**
     * the delegate
     */
    protected SimpleFeatureCollection delegate;

    protected DecoratingSimpleFeatureCollection(FeatureCollection<SimpleFeatureType,SimpleFeature> delegate) {
        this.delegate = DataUtilities.simple(delegate);
    }
    
    protected DecoratingSimpleFeatureCollection(SimpleFeatureCollection delegate) {
        this.delegate = delegate;
    }

    public void accepts(org.opengis.feature.FeatureVisitor visitor,
            org.opengis.util.ProgressListener progress) throws IOException {
        delegate.accepts(visitor, progress);
    }
    
    public boolean add(SimpleFeature o) {
        return delegate.add(o);
    }

    public boolean addAll(Collection c) {
        return delegate.addAll(c);
    }

    public boolean addAll(FeatureCollection c) {
        return delegate.addAll(c);
    }
    
    public void addListener(CollectionListener listener)
            throws NullPointerException {
        delegate.addListener(listener);
    }

    public void clear() {
        delegate.clear();
    }

    public void close(FeatureIterator<SimpleFeature> close) {
        delegate.close(close);
    }

    public void close(Iterator<SimpleFeature> close) {
        delegate.close(close);
    }

    public boolean contains(Object o) {
        return delegate.contains(o);
    }

    public boolean containsAll(Collection c) {
        return delegate.containsAll(c);
    }

    public boolean equals(Object o) {
        return delegate.equals(o);
    }

    public SimpleFeatureIterator features() {
        return delegate.features();
    }

    public ReferencedEnvelope getBounds() {
        return delegate.getBounds();
    }

    public SimpleFeatureType getSchema() {
        return delegate.getSchema();
    }

    public int hashCode() {
        return delegate.hashCode();
    }

    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    public Iterator iterator() {
        return delegate.iterator();
    }

    public void purge() {
        delegate.purge();
    }

    public boolean remove(Object o) {
        return delegate.remove(o);
    }

    public boolean removeAll(Collection c) {
        return delegate.removeAll(c);
    }

    public void removeListener(CollectionListener listener)
            throws NullPointerException {
        delegate.removeListener(listener);
    }

    public boolean retainAll(Collection c) {
        return delegate.retainAll(c);
    }

    public int size() {
        return delegate.size();
    }

    public SimpleFeatureCollection sort(SortBy order) {
        return delegate.sort(order);
    }

    public SimpleFeatureCollection subCollection(Filter filter) {
        return delegate.subCollection(filter);
    }

    public Object[] toArray() {
        return delegate.toArray();
    }

    public Object[] toArray(Object[] a) {
        return delegate.toArray(a);
    }
	public String getID() {
		return delegate.getID();
	}
}
