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
 *
 *    Created on August 12, 2003, 7:29 PM
 */
package org.geotools.feature;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.geotools.feature.visitor.FeatureVisitor;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.ProgressListener;
import org.opengis.feature.GeometryAttribute;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.opengis.filter.identity.FeatureId;
import org.opengis.filter.sort.SortBy;

import com.vividsolutions.jts.geom.Geometry;

/**
 * @author jamesm
 * @source $URL:
 *         http://svn.geotools.org/geotools/trunk/gt/modules/library/main/src/test/java/org/geotools/feature/MockFeatureCollection.java $
 */
public class MockFeatureCollection implements FeatureCollection<SimpleFeatureType, SimpleFeature> {

    /** Creates a new instance of MockFeatureCollection */
    public MockFeatureCollection() {
    }

    public void accepts(FeatureVisitor visitor, ProgressListener progress)
            throws IOException {
    }
    
    public void accepts(org.opengis.feature.FeatureVisitor visitor,
            org.opengis.util.ProgressListener progress) {
    }

    public void addListener(CollectionListener listener)
            throws NullPointerException {
    }

    public void close(FeatureIterator<SimpleFeature> close) {
    }

    public void close(Iterator close) {
    }

    public FeatureIterator<SimpleFeature> features() {
        return null;
    }

    public SimpleFeatureType getSchema() {
        return null;
    }

    public void removeListener(CollectionListener listener)
            throws NullPointerException {
    }

    public FeatureCollection<SimpleFeatureType, SimpleFeature> sort(SortBy order) {
        return null;
    }

    public FeatureCollection<SimpleFeatureType, SimpleFeature> subCollection(Filter filter) {
        return null;
    }

    public Iterator iterator() {
        return null;
    }

    public void purge() {
    }

    public boolean add(SimpleFeature o) {
        return false;
    }

    public boolean addAll(Collection c) {
        return false;
    }
    public boolean addAll(
		FeatureCollection<? extends SimpleFeatureType, ? extends SimpleFeature> resource) {
    	return false;
    }
    public void clear() {
    }

    public boolean contains(Object o) {
        return false;
    }

    public boolean containsAll(Collection c) {
        return false;
    }

    public boolean isEmpty() {
        return false;
    }

    public boolean remove(Object o) {
        return false;
    }

    public boolean removeAll(Collection c) {
        return false;
    }

    public boolean retainAll(Collection c) {
        return false;
    }

    public int size() {
        return 0;
    }

    public Object[] toArray() {
        return null;
    }

    public Object[] toArray(Object[] a) {
        return null;
    }

    public ReferencedEnvelope getBounds() {
        return null;
    }

    public String getID() {
        return null;
    }
   
}
