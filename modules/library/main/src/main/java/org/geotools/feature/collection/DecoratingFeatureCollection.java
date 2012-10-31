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

import java.util.Collection;

import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.sort.SortBy;

/**
 * A FeatureCollection which completely delegates to another FeatureCollection.
 * <p>
 * This class should be subclasses by classes which must somehow decorate 
 * another SimpleFeatureCollection and override the relevant methods. 
 * </p>
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 * @since 2.5
 *
 * @source $URL$
 */
public class DecoratingFeatureCollection<T extends FeatureType, F extends Feature> implements
        FeatureCollection<T, F> {

    /**
     * the delegate
     */
    protected FeatureCollection<T, F> delegate;

    protected DecoratingFeatureCollection(FeatureCollection<T, F> delegate) {
        this.delegate = delegate;
    }

    public void accepts(org.opengis.feature.FeatureVisitor visitor,
            org.opengis.util.ProgressListener progress) {
        FeatureIterator<F> it = features();

        try {
            Exception exception = null;
            while (it.hasNext()) {
                try {
                    visitor.visit(it.next());
                } catch (Exception e) {
                    if (exception != null)
                        exception = e;
                }
            }

            if (exception != null) {
                if (exception instanceof RuntimeException) {
                    throw (RuntimeException) exception;
                } else {
                    throw new RuntimeException(exception);
                }
            }
        } finally {
        	it.close();
        }
    }
    
    public boolean contains(Object o) {
        return delegate.contains(o);
    }

    public boolean containsAll(Collection<?> c) {
        return delegate.containsAll(c);
    }

    public boolean equals(Object o) {
        return delegate.equals(o);
    }

    public FeatureIterator<F> features() {
        return delegate.features();
    }

    public ReferencedEnvelope getBounds() {
        return delegate.getBounds();
    }

    public T getSchema() {
        return delegate.getSchema();
    }

    public int hashCode() {
        return delegate.hashCode();
    }

    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    public int size() {
        return delegate.size();
    }

    public FeatureCollection<T, F> sort(SortBy order) {
        return delegate.sort(order);
    }

    public FeatureCollection<T, F> subCollection(Filter filter) {
        return delegate.subCollection(filter);
    }

    public Object[] toArray() {
        return delegate.toArray();
    }

    public <O> O[] toArray(O[] a) {
        return delegate.toArray(a);
    }
	public String getID() {
		return delegate.getID();
	}
}
